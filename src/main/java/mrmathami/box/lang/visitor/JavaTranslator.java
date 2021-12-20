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

import mrmathami.box.lang.ast.*;
import mrmathami.box.lang.ast.definition.*;
import mrmathami.box.lang.ast.expression.*;
import mrmathami.box.lang.ast.identifier.*;
import mrmathami.box.lang.ast.statement.*;
import mrmathami.box.lang.ast.type.ArrayType;
import mrmathami.box.lang.ast.type.SimpleType;
import mrmathami.box.lang.ast.type.TupleType;
import mrmathami.box.lang.ast.type.Type;
import mrmathami.box.lang.parser.Builder;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.eclipse.collections.api.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaTranslator {
	private final @NotNull IndentAppender appender;


	public JavaTranslator(@NotNull Appendable appendable) {
		this.appender = new IndentAppender(appendable);
	}

	public void fromCompilationUnit(@NotNull CompilationUnit compilationUnit)
			throws VisitorException, IOException {
		new JavaTranslator(appender).compilationUnit(compilationUnit);
	}


	public void compilationUnit(@NotNull CompilationUnit compilationUnit) throws IOException {
		appender.append("public final class JavaTranslatedBox {");
		try (final Closeable ignored = appender.indent()) {
			for (final GlobalDefinition globalDefinition : compilationUnit.getDefinitions()) {
				globalDefinition(globalDefinition);
				appender.newLine();
			}
		}
		appender.append("}").newLine();
	}


	// =========================================


	public static void main(@NotNull String[] strings) throws IOException, InvalidASTException {
		final String string = Files.readString(Path.of("./test/input.txt"));
		final CodePointCharStream charStream = CharStreams.fromString(string);

//		final StringBuilder builder = new StringBuilder();
		final CompilationUnit compilationUnit = Builder.build(charStream);
//		final JavaTranslator translator = new JavaTranslator(builder);
		final JavaTranslator translator = new JavaTranslator(System.out);
		translator.compilationUnit(compilationUnit);
//		System.out.println(builder);
	}


	// =========================================

	private void identifier(@NotNull Identifier node) throws IOException {
		if (node instanceof FunctionIdentifier) {
			functionIdentifier((FunctionIdentifier) node);
		} else if (node instanceof LabelIdentifier) {
			labelIdentifier((LabelIdentifier) node);
		} else if (node instanceof MemberIdentifier) {
			memberIdentifier((MemberIdentifier) node);
		} else if (node instanceof ParameterIdentifier) {
			parameterIdentifier((ParameterIdentifier) node);
		} else if (node instanceof TupleIdentifier) {
			tupleIdentifier((TupleIdentifier) node);
		} else if (node instanceof VariableIdentifier) {
			variableIdentifier((VariableIdentifier) node);
		} else {
			throw new AssertionError();
		}
	}

	private void functionIdentifier(@NotNull FunctionIdentifier node) throws IOException {
		appender.append(node.getName());
	}

	private void labelIdentifier(@NotNull LabelIdentifier node) throws IOException {
		appender.append(node.getName().substring(1));
	}

	private void memberIdentifier(@NotNull MemberIdentifier node) throws IOException {
		appender.append(node.getName().substring(1));
	}

	private void parameterIdentifier(@NotNull ParameterIdentifier node) throws IOException {
		appender.append(node.getName().substring(1));
	}

	private void tupleIdentifier(@NotNull TupleIdentifier node) throws IOException {
		appender.append(node.getName());
	}

	private void variableIdentifier(@NotNull VariableIdentifier node) throws IOException {
		appender.append(node.getName());
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
		appender.newLine().append("final ");
		type(node.getReturnType());
		appender.append(' ');
		functionIdentifier(node.getIdentifier());
		appender.append('(');
		boolean separator = false;
		for (final ParameterDefinition parameterDefinition : node.getParameters()) {
			if (separator) appender.append(", ");
			parameterDefinition(parameterDefinition);
			separator = true;
		}
		appender.append(") ");
		blockStatement(node.getBody());
		appender.newLine();
	}

	private void parameterDefinition(@NotNull ParameterDefinition node) throws IOException {
		type(node.getType());
		appender.append(' ');
		parameterIdentifier(node.getIdentifier());
	}

	private void tupleDefinition(@NotNull TupleDefinition node) throws IOException {
		appender.append("static final class ");
		tupleIdentifier(node.getIdentifier());
		appender.append(" {").newLine();
		try (final Closeable ignored = appender.indent()) {
			for (final MemberDefinition memberDefinition : node.getMembers()) {
				memberDefinition(memberDefinition);
				appender.newLine();
			}
		}
		appender.append('}');
	}

	private void memberDefinition(@NotNull MemberDefinition node) throws IOException {
		type(node.getType());
		appender.append(' ');
		memberIdentifier(node.getIdentifier());
		appender.append(';');
	}

	private void variableDefinition(@NotNull VariableDefinition node) throws IOException {
		type(node.getType());
		appender.append(' ');
		variableIdentifier(node.getIdentifier());

		final Expression initialValue = node.getInitialValue();
		if (initialValue != null) {
			appender.append(" = ");
			expression(initialValue, false);
		}
		appender.append(';');
	}

	private void labelDefinition(@NotNull LabelDefinition node) throws IOException {
		labelIdentifier(node.getIdentifier());
		appender.append(": ");
		loopStatement(node.getLoopStatement());
	}

	// =========================================

	private void expression(@NotNull Expression node, boolean parentheses) throws IOException {
		if (node instanceof AccessibleExpression) {
			accessibleExpression((AccessibleExpression) node, parentheses);
		} else if (node instanceof BinaryExpression) {
			binaryExpression((BinaryExpression) node, parentheses);
		} else if (node instanceof ArrayCreationExpression) {
			arrayCreationExpression((ArrayCreationExpression) node, parentheses);
		} else if (node instanceof CastExpression) {
			castExpression((CastExpression) node, parentheses);
		} else if (node instanceof ConditionalExpression) {
			conditionalExpression((ConditionalExpression) node, parentheses);
		} else if (node instanceof LiteralExpression) {
			literalExpression((LiteralExpression) node, parentheses);
		} else if (node instanceof TupleCreationExpression) {
			tupleCreationExpression((TupleCreationExpression) node, parentheses);
		} else if (node instanceof UnaryExpression) {
			unaryExpression((UnaryExpression) node, parentheses);
		} else {
			throw new AssertionError();
		}
	}

	private void arrayCreationExpression(@NotNull ArrayCreationExpression node, boolean parentheses) throws IOException {
		appender.append("new ");
		final ArrayType arrayType = node.getType();
		type(arrayType.getInnerType());
		appender.append('[');
		boolean separator = false;
		for (final Expression expression : node.getDimensionExpressions()) {
			if (separator) appender.append("][");
			expression(expression, false);
			separator = true;
		}
		appender.append(']');
	}

	private void castExpression(@NotNull CastExpression node, boolean parentheses) throws IOException {
		type(node.getType());
		appender.append('(');
		expression(node.getExpression(), false);
		appender.append(')');
	}

	private void conditionalExpression(@NotNull ConditionalExpression node, boolean parentheses) throws IOException {
		if (parentheses) appender.append('(');
		expression(node.getCondition(), true);
		appender.append(" ? ");
		expression(node.getTrueExpression(), true);
		appender.append(" : ");
		expression(node.getFalseExpression(), true);
		if (parentheses) appender.append(')');
	}

	private void literalExpression(@NotNull LiteralExpression node, boolean parentheses) throws IOException {
		final Keyword keyword = node.getKeyword();
		final BigInteger number = node.getNumber();
		if (keyword != null && number == null) {
			appender.append(keyword.toString());
		} else if (number != null && keyword == null) {
			appender.append(number.toString());
		} else {
			throw new AssertionError();
		}
	}

	private void tupleCreationExpression(@NotNull TupleCreationExpression node, boolean parentheses) throws IOException {
		appender.append("new ");
		type(node.getType());
		appender.append('(');
		boolean separator = false;
		for (final Expression expression : node.getMemberExpressions()) {
			if (separator) appender.append(", ");
			expression(expression, false);
			separator = true;
		}
		appender.append(')');
	}

	private void unaryExpression(@NotNull UnaryExpression node, boolean parentheses) throws IOException {
		if (parentheses) appender.append('(');
		appender.append(node.getOperator().toString());
		expression(node.getExpression(), true);
		if (parentheses) appender.append(')');
	}


	private void accessibleExpression(@NotNull AccessibleExpression node, boolean parentheses) throws IOException {
		if (node instanceof AssignableExpression) {
			assignableExpression((AssignableExpression) node, parentheses);
		} else if (node instanceof FunctionCallExpression) {
			functionCallExpression((FunctionCallExpression) node, parentheses);
		} else if (node instanceof ParameterExpression) {
			parameterExpression((ParameterExpression) node, parentheses);
		} else {
			throw new AssertionError();
		}
	}

	private void functionCallExpression(@NotNull FunctionCallExpression node, boolean parentheses) throws IOException {
		functionIdentifier(node.getIdentifier());
		appender.append('(');
		boolean separator = false;
		for (final Expression expression : node.getArguments()) {
			if (separator) appender.append(", ");
			expression(expression, false);
			separator = true;
		}
		appender.append(')');
	}

	private void parameterExpression(@NotNull ParameterExpression node, boolean parentheses) throws IOException {
		parameterIdentifier(node.getIdentifier());
	}


	private void assignableExpression(@NotNull AssignableExpression node, boolean parentheses) throws IOException {
		if (node instanceof AccessExpression) {
			accessExpression((AccessExpression) node, parentheses);
		} else if (node instanceof VariableExpression) {
			variableExpression((VariableExpression) node, parentheses);
		} else {
			throw new AssertionError();
		}
	}

	private void variableExpression(@NotNull VariableExpression node, boolean parentheses) throws IOException {
		variableIdentifier(node.getIdentifier());
	}


	private void accessExpression(@NotNull AccessExpression node, boolean parentheses) throws IOException {
		if (node instanceof ArrayAccessExpression) {
			arrayAccessExpression((ArrayAccessExpression) node, parentheses);
		} else if (node instanceof MemberAccessExpression) {
			memberAccessExpression((MemberAccessExpression) node, parentheses);
		} else {
			throw new AssertionError();
		}
	}

	private void arrayAccessExpression(@NotNull ArrayAccessExpression node, boolean parentheses) throws IOException {
		accessibleExpression(node.getAccessibleExpression(), parentheses);
		appender.append('[');
		boolean separator = false;
		for (final Expression expression : node.getExpressions()) {
			if (separator) appender.append("][");
			expression(expression, false);
			separator = true;
		}
		appender.append(']');
	}

	private void memberAccessExpression(@NotNull MemberAccessExpression node, boolean parentheses) throws IOException {
		accessibleExpression(node.getAccessibleExpression(), parentheses);
		appender.append('.');
		memberIdentifier(node.getIdentifier());
	}


	private void binaryExpression(@NotNull BinaryExpression node, boolean parentheses) throws IOException {
		if (node instanceof ComparisonExpression) {
			comparisonExpression((ComparisonExpression) node, parentheses);
		} else if (node instanceof ShiftExpression) {
			shiftExpression((ShiftExpression) node, parentheses);
		} else if (node instanceof SimpleBinaryExpression) {
			simpleBinaryExpression((SimpleBinaryExpression) node, parentheses);
		} else {
			throw new AssertionError();
		}
	}

	private void comparisonExpression(@NotNull ComparisonExpression node, boolean parentheses) throws IOException {
		appender.append('(');
		final NormalOperator operator = node.getOperator();
		boolean insert = false;
		for (final Expression operand : node.getOperands()) {
			if (insert) appender.append(' ').append(operator.toString()).append(' ');
			expression(operand, parentheses);
			insert = true;
		}
		appender.append(')');
	}

	private void shiftExpression(@NotNull ShiftExpression node, boolean parentheses) throws IOException {
		appender.append('(');
		final NormalOperator operator = node.getOperator();
		boolean insert = false;
		for (final Expression operand : node.getOperands()) {
			if (insert) appender.append(' ').append(operator.toString()).append(' ');
			expression(operand, parentheses);
			insert = true;
		}
		appender.append(')');
	}

	private void simpleBinaryExpression(@NotNull SimpleBinaryExpression node, boolean parentheses) throws IOException {
		appender.append('(');
		final NormalOperator operator = node.getOperator();
		boolean insert = false;
		for (final Expression operand : node.getOperands()) {
			if (insert) appender.append(' ').append(operator.toString()).append(' ');
			expression(operand, parentheses);
			insert = true;
		}
		appender.append(')');
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
		assignableExpression(node.getAssignableExpression(), false);

		appender.append(' ').append(assignmentOperator.toString()).append(' ');
		expression(node.getExpression(), false);
		appender.append(';');
	}

	private void blockStatement(@NotNull BlockStatement node) throws IOException {
		appender.append('{').newLine();
		try (final Closeable ignored = appender.indent()) {
			for (final Statement statement : node.getStatements()) {
				statement(statement);
				appender.newLine();
			}
		}
		appender.append('}');
	}

	private void breakStatement(@NotNull BreakStatement node) throws IOException {
		final LabelIdentifier identifier = node.getLabelIdentifier();
		if (identifier == null) {
			appender.append("break;");
		} else {
			appender.append("break ");
			labelIdentifier(identifier);
			appender.append(";");
		}
	}

	private void continueStatement(@NotNull ContinueStatement node) throws IOException {
		final LabelIdentifier identifier = node.getLabelIdentifier();
		if (identifier == null) {
			appender.append("continue;");
		} else {
			appender.append("continue ");
			labelIdentifier(identifier);
			appender.append(";");
		}
	}

	private void loopStatement(@NotNull LoopStatement node) throws IOException {
		appender.append("while (true) ");
		if (node.getStatement() != null) {
			statement(node.getStatement());
		} else {
			appender.append(";");
		}
	}

	private void functionCallStatement(@NotNull FunctionCallStatement node) throws IOException {
		functionCallExpression(node.getFunctionCallExpression(), false);
	}

	private void ifStatement(@NotNull IfStatement node) throws IOException {
		boolean separator = false;
		for (final Pair<Expression, SingleStatement> pair : node.getConditionStatementPairs()) {
			final Expression expression = pair.getOne();
			final SingleStatement statement = pair.getTwo();
			if (separator) appender.append("else ");
			appender.append("if (");
			expression(expression, false);
			appender.append(") ");
			if (statement != null) {
				statement(statement);
			} else {
				appender.append(";");
			}
			separator = true;
		}
		final SingleStatement alternativeStatement = node.getAlternativeStatement();
		if (alternativeStatement != null) {
			appender.append("else ");
			statement(alternativeStatement);
		}
	}

	private void returnStatement(@NotNull ReturnStatement node) throws IOException {
		appender.append("return");
		final Expression expression = node.getExpression();
		if (expression != null) {
			appender.append(' ');
			expression(expression, false);
		}
		appender.append(";");
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
		/// without comments
		// appendable.append("[]".repeat(type.getNumOfDimensions()));
		final long[] dimensionSizes = type.getDimensionSizes();
		for (long dimensionSize : dimensionSizes) {
			appender.append("[/* ").append(String.valueOf(dimensionSize)).append(" */]");
		}
	}

	private void tupleType(@NotNull TupleType type) throws IOException {
		tupleIdentifier(type.getIdentifier());
	}

	private void simpleType(@NotNull SimpleType type) throws IOException {
		if (type == SimpleType.VOID) {
			appender.append("void");
		} else if (type == SimpleType.BOOL) {
			appender.append("boolean");
		} else if (type == SimpleType.NUMBER_U8 || type == SimpleType.NUMBER_I8) {
			appender.append("byte");
		} else if (type == SimpleType.NUMBER_U16 || type == SimpleType.NUMBER_I16) {
			appender.append("short");
		} else if (type == SimpleType.NUMBER_U32 || type == SimpleType.NUMBER_I32) {
			appender.append("int");
		} else if (type == SimpleType.NUMBER_U64 || type == SimpleType.NUMBER_I64) {
			appender.append("long");
		}
	}

	// =========================================

	public int leave(@NotNull AstNode node) throws VisitorException {
		return 0;
	}
}
