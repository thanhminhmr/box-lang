/*
 * Copyright (C) 2020-2021 Mai Thanh Minh (a.k.a. thanhminhmr or mrmathami)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package mrmathami.box.lang.visitor;

import mrmathami.box.lang.ast.AssignmentOperator;
import mrmathami.box.lang.ast.AstNode;
import mrmathami.box.lang.ast.CompilationUnit;
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.Keyword;
import mrmathami.box.lang.ast.NormalOperator;
import mrmathami.box.lang.ast.definition.Definition;
import mrmathami.box.lang.ast.definition.FunctionDefinition;
import mrmathami.box.lang.ast.definition.GlobalDefinition;
import mrmathami.box.lang.ast.definition.LabelDefinition;
import mrmathami.box.lang.ast.definition.MemberDefinition;
import mrmathami.box.lang.ast.definition.ParameterDefinition;
import mrmathami.box.lang.ast.definition.TupleDefinition;
import mrmathami.box.lang.ast.definition.VariableDefinition;
import mrmathami.box.lang.ast.expression.AccessExpression;
import mrmathami.box.lang.ast.expression.AccessibleExpression;
import mrmathami.box.lang.ast.expression.ArrayAccessExpression;
import mrmathami.box.lang.ast.expression.ArrayCreationExpression;
import mrmathami.box.lang.ast.expression.AssignableExpression;
import mrmathami.box.lang.ast.expression.BinaryExpression;
import mrmathami.box.lang.ast.expression.CastExpression;
import mrmathami.box.lang.ast.expression.ComparisonExpression;
import mrmathami.box.lang.ast.expression.ConditionalExpression;
import mrmathami.box.lang.ast.expression.Expression;
import mrmathami.box.lang.ast.expression.FunctionCallExpression;
import mrmathami.box.lang.ast.expression.LiteralExpression;
import mrmathami.box.lang.ast.expression.MemberAccessExpression;
import mrmathami.box.lang.ast.expression.ParameterExpression;
import mrmathami.box.lang.ast.expression.ShiftExpression;
import mrmathami.box.lang.ast.expression.SimpleBinaryExpression;
import mrmathami.box.lang.ast.expression.TupleCreationExpression;
import mrmathami.box.lang.ast.expression.UnaryExpression;
import mrmathami.box.lang.ast.expression.VariableExpression;
import mrmathami.box.lang.ast.identifier.LabelIdentifier;
import mrmathami.box.lang.ast.statement.AssignmentStatement;
import mrmathami.box.lang.ast.statement.BlockStatement;
import mrmathami.box.lang.ast.statement.BreakStatement;
import mrmathami.box.lang.ast.statement.ContinueStatement;
import mrmathami.box.lang.ast.statement.FunctionCallStatement;
import mrmathami.box.lang.ast.statement.IfStatement;
import mrmathami.box.lang.ast.statement.LoopStatement;
import mrmathami.box.lang.ast.statement.ReturnStatement;
import mrmathami.box.lang.ast.statement.SingleStatement;
import mrmathami.box.lang.ast.statement.Statement;
import mrmathami.box.lang.ast.type.ArrayType;
import mrmathami.box.lang.ast.type.SimpleType;
import mrmathami.box.lang.ast.type.TupleType;
import mrmathami.box.lang.ast.type.Type;
import mrmathami.box.lang.parser.Builder;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.eclipse.collections.api.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaTranslator {
	private final @NotNull Appendable appendable;


	public JavaTranslator(@NotNull Appendable writer) {
		this.appendable = writer;
	}

	public void fromCompilationUnit(@NotNull CompilationUnit compilationUnit)
			throws VisitorException, IOException {
		new JavaTranslator(appendable).compilationUnit(compilationUnit);
	}


	public void compilationUnit(@NotNull CompilationUnit compilationUnit) throws IOException {
		appendable.append("public final class JavaTranslatedBox {\n");
		for (final GlobalDefinition globalDefinition : compilationUnit.getDefinitions()) {
			globalDefinition(globalDefinition);
		}
		appendable.append("}\n");
	}


	// =========================================


	public static void main(@NotNull String[] strings) throws IOException, InvalidASTException {
		final String string = Files.readString(Path.of("./test/input.txt"));
		final CodePointCharStream charStream = CharStreams.fromString(string);

		final StringBuilder builder = new StringBuilder();
		final CompilationUnit compilationUnit = Builder.build(charStream);
		final JavaTranslator translator = new JavaTranslator(builder);
		translator.compilationUnit(compilationUnit);
		System.out.println(builder);
	}


	// =========================================

	private void definition(@NotNull Definition node) throws IOException {
		// TODO
	}

	private void globalDefinition(@NotNull GlobalDefinition node) throws IOException {
		if (node instanceof FunctionDefinition) {
			functionDefinition((FunctionDefinition) node);
		} else if (node instanceof TupleDefinition) {
			tupleDefinition((TupleDefinition) node);
		} else if (node instanceof VariableDefinition) {
			variableDefinition((VariableDefinition) node);
		} else {
			throw new AssertionError();
		}
	}

	private void functionDefinition(@NotNull FunctionDefinition node) throws IOException {
		appendable.append("public final ");
		type(node.getReturnType());
		appendable.append(' ').append(node.getIdentifier().getName()).append('(');
		boolean comma = false;
		for (final ParameterDefinition parameterDefinition : node.getParameters()) {
			if (comma) appendable.append(", ");
			parameterDefinition(parameterDefinition);
			comma = true;
		}
		appendable.append(") ");
		blockStatement(node.getBody());
		appendable.append('\n');
	}

	private void parameterDefinition(@NotNull ParameterDefinition node) throws IOException {
		type(node.getType());
		appendable.append(' ').append(node.getIdentifier().getName());
	}

	private void tupleDefinition(@NotNull TupleDefinition node) throws IOException {
		appendable.append("private static final class ").append(node.getIdentifier().getName()).append(" {\n");
		for (final MemberDefinition memberDefinition : node.getMembers()) {
			memberDefinition(memberDefinition);
		}
		appendable.append("}\n");
	}

	private void memberDefinition(@NotNull MemberDefinition node) throws IOException {
		appendable.append("private ");
		type(node.getType());
		appendable.append(' ').append(node.getIdentifier().getName()).append(";\n");
	}

	private void variableDefinition(@NotNull VariableDefinition node) throws IOException {
		appendable.append("private ");
		type(node.getType());
		appendable.append(' ').append(node.getIdentifier().getName());

		final Expression initialValue = node.getInitialValue();
		if (initialValue != null) {
			appendable.append(" = ");
			expression(initialValue);
		}
		appendable.append(";\n");
	}

	private void labelDefinition(@NotNull LabelDefinition node) throws IOException {
		appendable.append(node.getIdentifier().getName()).append(":\n");
		loopStatement(node.getLoopStatement());
	}

	// =========================================

	private void expression(@NotNull Expression node) throws IOException {
		if (node instanceof AccessibleExpression) {
			accessibleExpression((AccessibleExpression) node);
		} else if (node instanceof BinaryExpression) {
			binaryExpression((BinaryExpression) node);
		} else if (node instanceof ArrayCreationExpression) {
			arrayCreationExpression((ArrayCreationExpression) node);
		} else if (node instanceof CastExpression) {
			castExpression((CastExpression) node);
		} else if (node instanceof ConditionalExpression) {
			conditionalExpression((ConditionalExpression) node);
		} else if (node instanceof LiteralExpression) {
			literalExpression((LiteralExpression) node);
		} else if (node instanceof TupleCreationExpression) {
			tupleCreationExpression((TupleCreationExpression) node);
		} else if (node instanceof UnaryExpression) {
			unaryExpression((UnaryExpression) node);
		} else {
			throw new AssertionError();
		}
	}

	private void arrayCreationExpression(@NotNull ArrayCreationExpression node) throws IOException {
		appendable.append("(new ");
		type(node.getType());
		appendable.append('[');
		boolean comma = false;
		for (final Expression expression : node.getDimensionExpressions()) {
			if (comma) appendable.append(", ");
			expression(expression);
			comma = true;
		}
		appendable.append("])");
	}

	private void castExpression(@NotNull CastExpression node) throws IOException {
		appendable.append('(');
		type(node.getType());
		appendable.append('(');
		expression(node.getExpression());
		appendable.append("))");
	}

	private void conditionalExpression(@NotNull ConditionalExpression node) throws IOException {
		appendable.append("((");
		expression(node.getCondition());
		appendable.append(")?(");
		expression(node.getTrueExpression());
		appendable.append("):(");
		expression(node.getFalseExpression());
		appendable.append("))");
	}

	private void literalExpression(@NotNull LiteralExpression node) throws IOException {
		appendable.append('(');
		final Keyword keyword = node.getKeyword();
		final BigInteger number = node.getNumber();
		if (keyword != null && number == null) {
			appendable.append(keyword.toString());
		} else if (number != null && keyword == null) {
			appendable.append(number.toString());
		} else {
			throw new AssertionError();
		}
		appendable.append(')');
	}

	private void tupleCreationExpression(@NotNull TupleCreationExpression node) throws IOException {
		appendable.append("(new ");
		type(node.getType());
		appendable.append('(');
		boolean comma = false;
		for (final Expression expression : node.getMemberExpressions()) {
			if (comma) appendable.append(", ");
			expression(expression);
			comma = true;
		}
		appendable.append("))");
	}

	private void unaryExpression(@NotNull UnaryExpression node) throws IOException {
		appendable.append('(').append(node.getOperator().toString());
		expression(node.getExpression());
		appendable.append(')');
	}


	private void accessibleExpression(@NotNull AccessibleExpression node) throws IOException {
		if (node instanceof AssignableExpression) {
			assignableExpression((AssignableExpression) node);
		} else if (node instanceof FunctionCallExpression) {
			functionCallExpression((FunctionCallExpression) node);
		} else if (node instanceof ParameterExpression) {
			parameterExpression((ParameterExpression) node);
		} else {
			throw new AssertionError();
		}
	}

	private void functionCallExpression(@NotNull FunctionCallExpression node) throws IOException {
		appendable.append(node.getIdentifier().getName()).append('(');
		boolean comma = false;
		for (final Expression expression : node.getArguments()) {
			if (comma) appendable.append(", ");
			expression(expression);
			comma = true;
		}
		appendable.append(')');
	}

	private void parameterExpression(@NotNull ParameterExpression node) throws IOException {
		appendable.append(node.getIdentifier().getName());
	}


	private void assignableExpression(@NotNull AssignableExpression node) throws IOException {
		if (node instanceof AccessExpression) {
			accessExpression((AccessExpression) node);
		} else if (node instanceof VariableExpression) {
			variableExpression((VariableExpression) node);
		} else {
			throw new AssertionError();
		}
	}

	private void variableExpression(@NotNull VariableExpression node) throws IOException {
		appendable.append(node.getIdentifier().getName());
	}


	private void accessExpression(@NotNull AccessExpression node) throws IOException {
		if (node instanceof ArrayAccessExpression) {
			arrayAccessExpression((ArrayAccessExpression) node);
		} else if (node instanceof MemberAccessExpression) {
			memberAccessExpression((MemberAccessExpression) node);
		} else {
			throw new AssertionError();
		}
	}

	private void arrayAccessExpression(@NotNull ArrayAccessExpression node) throws IOException {
		accessibleExpression(node.getAccessibleExpression());
		appendable.append('[');
		boolean comma = false;
		for (final Expression expression : node.getExpressions()) {
			if (comma) appendable.append(',');
			expression(expression);
			comma = true;
		}
		appendable.append(']');
	}

	private void memberAccessExpression(@NotNull MemberAccessExpression node) throws IOException {
		accessibleExpression(node.getAccessibleExpression());
		appendable.append('.').append(node.getIdentifier().getName());
	}


	private void binaryExpression(@NotNull BinaryExpression node) throws IOException {
		if (node instanceof ComparisonExpression) {
			comparisonExpression((ComparisonExpression) node);
		} else if (node instanceof ShiftExpression) {
			shiftExpression((ShiftExpression) node);
		} else if (node instanceof SimpleBinaryExpression) {
			simpleBinaryExpression((SimpleBinaryExpression) node);
		} else {
			throw new AssertionError();
		}
	}

	private void comparisonExpression(@NotNull ComparisonExpression node) throws IOException {
		appendable.append('(');
		final NormalOperator operator = node.getOperator();
		boolean insert = false;
		for (final Expression operand : node.getOperands()) {
			if (insert) appendable.append(operator.toString());
			expression(operand);
			insert = true;
		}
		appendable.append(')');
	}

	private void shiftExpression(@NotNull ShiftExpression node) throws IOException {
		appendable.append('(');
		final NormalOperator operator = node.getOperator();
		boolean insert = false;
		for (final Expression operand : node.getOperands()) {
			if (insert) appendable.append(operator.toString());
			expression(operand);
			insert = true;
		}
		appendable.append(')');
	}

	private void simpleBinaryExpression(@NotNull SimpleBinaryExpression node) throws IOException {
		appendable.append('(');
		final NormalOperator operator = node.getOperator();
		boolean insert = false;
		for (final Expression operand : node.getOperands()) {
			if (insert) appendable.append(operator.toString());
			expression(operand);
			insert = true;
		}
		appendable.append(')');
	}

	// =========================================

	private void statement(@NotNull Statement node) throws IOException {
		if (node instanceof SingleStatement) {
			singleStatement((SingleStatement) node);
		} else if (node instanceof VariableDefinition) {
			variableDefinition((VariableDefinition) node);
		} else if (node instanceof LabelDefinition) {
			labelDefinition((LabelDefinition) node);
		} else {
			throw new AssertionError();
		}
	}

	private void singleStatement(@NotNull SingleStatement node) throws IOException {
		if (node instanceof AssignmentStatement) {
			assignmentStatement((AssignmentStatement) node);
		} else if (node instanceof BlockStatement) {
			blockStatement((BlockStatement) node);
		} else if (node instanceof BreakStatement) {
			breakStatement((BreakStatement) node);
		} else if (node instanceof ContinueStatement) {
			continueStatement((ContinueStatement) node);
		} else if (node instanceof FunctionCallStatement) {
			functionCallStatement((FunctionCallStatement) node);
		} else if (node instanceof IfStatement) {
			ifStatement((IfStatement) node);
		} else if (node instanceof LoopStatement) {
			loopStatement((LoopStatement) node);
		} else if (node instanceof ReturnStatement) {
			returnStatement((ReturnStatement) node);
		} else {
			throw new AssertionError();
		}
	}

	private void assignmentStatement(@NotNull AssignmentStatement node) throws IOException {
		// TODO
		final AssignmentOperator assignmentOperator = node.getAssignmentOperator();
		switch (assignmentOperator) {
			case ASSIGN:
				break;
			case SHALLOW_CLONE:
				break;
			case ARITHMETIC_MULTIPLY:
				break;
			case ARITHMETIC_DIVIDE:
				break;
			case ARITHMETIC_MODULUS:
				break;
			case ARITHMETIC_ADD:
				break;
			case ARITHMETIC_SUBTRACT:
				break;
			case BIT_SHIFT_LEFT:
				break;
			case BIT_SHIFT_RIGHT:
				break;
			case BIT_AND:
				break;
			case BIT_XOR:
				break;
			case BIT_OR:
				break;
			case LOGIC_AND:
				break;
			case LOGIC_XOR:
				break;
			case LOGIC_OR:
				break;
		}
		assignableExpression(node.getAssignableExpression());

		appendable.append(' ').append(assignmentOperator.toString()).append(' ');


	}

	private void blockStatement(@NotNull BlockStatement node) throws IOException {
		appendable.append("{\n");
		for (final Statement statement : node.getStatements()) {
			statement(statement);
		}
		appendable.append('}');
	}

	private void breakStatement(@NotNull BreakStatement node) throws IOException {
		final LabelIdentifier identifier = node.getLabelIdentifier();
		if (identifier == null) {
			appendable.append("break;\n");
		} else {
			appendable.append("break ").append(identifier.getName()).append(";\n");
		}
	}

	private void continueStatement(@NotNull ContinueStatement node) throws IOException {
		final LabelIdentifier identifier = node.getLabelIdentifier();
		if (identifier == null) {
			appendable.append("continue;\n");
		} else {
			appendable.append("continue ").append(identifier.getName()).append(";\n");
		}
	}

	private void loopStatement(@NotNull LoopStatement node) throws IOException {
		appendable.append("while (true) ");
		if (node.getStatement() != null) {
			statement(node.getStatement());
		} else {
			appendable.append(";\n");
		}
	}

	private void functionCallStatement(@NotNull FunctionCallStatement node) throws IOException {
		functionCallExpression(node.getFunctionCallExpression());
	}

	private void ifStatement(@NotNull IfStatement node) throws IOException {
		boolean first = true;
		for (final Pair<Expression, SingleStatement> pair : node.getConditionStatementPairs()) {
			final Expression expression = pair.getOne();
			final SingleStatement statement = pair.getTwo();
			if (first) appendable.append("else ");
			appendable.append("if (");
			expression(expression);
			appendable.append(") ");
			if (statement != null) {
				statement(statement);
			} else {
				appendable.append(";\n");
			}
			first = false;
		}
		final SingleStatement alternativeStatement = node.getAlternativeStatement();
		if (alternativeStatement != null) {
			appendable.append("else ");
			statement(alternativeStatement);
			appendable.append('\n');
		}
	}

	private void returnStatement(@NotNull ReturnStatement node) throws IOException {
		appendable.append("return");
		final Expression expression = node.getExpression();
		if (expression != null) {
			appendable.append(' ');
			expression(expression);
		}
		appendable.append(";\n");
	}

	// =========================================

	private void type(@NotNull Type type) throws IOException {
		if (type instanceof ArrayType) {
			arrayType((ArrayType) type);
		} else if (type instanceof TupleType) {
			tupleType((TupleType) type);
		} else if (type instanceof SimpleType) {
			simpleType((SimpleType) type);
		} else {
			throw new AssertionError();
		}
	}

	private void arrayType(@NotNull ArrayType type) throws IOException {
		type(type.getInnerType());
		appendable.append("[]");
	}

	private void tupleType(@NotNull TupleType type) throws IOException {
		appendable.append(type.getIdentifier().getName());
	}

	private void simpleType(@NotNull SimpleType type) throws IOException {
		if (type == SimpleType.VOID) {
			appendable.append("void");
		} else if (type == SimpleType.BOOL) {
			appendable.append("boolean");
		} else if (type == SimpleType.NUMBER_U8 || type == SimpleType.NUMBER_I8) {
			appendable.append("byte");
		} else if (type == SimpleType.NUMBER_U16 || type == SimpleType.NUMBER_I16) {
			appendable.append("short");
		} else if (type == SimpleType.NUMBER_U32 || type == SimpleType.NUMBER_I32) {
			appendable.append("int");
		} else if (type == SimpleType.NUMBER_U64 || type == SimpleType.NUMBER_I64) {
			appendable.append("long");
		}
	}

	// =========================================

	public int leave(@NotNull AstNode node) throws VisitorException {
		return 0;
	}
}
