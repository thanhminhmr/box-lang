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

package mrmathami.box.lang.parser;

import mrmathami.annotations.Nonnull;
import mrmathami.annotations.Nullable;
import mrmathami.annotations.Unsigned;
import mrmathami.box.lang.BoxLangLexer;
import mrmathami.box.lang.BoxLangParser;
import mrmathami.box.lang.BoxLangParser.AccessExpressionContext;
import mrmathami.box.lang.BoxLangParser.AccessSuffixContext;
import mrmathami.box.lang.BoxLangParser.AddExpressionContext;
import mrmathami.box.lang.BoxLangParser.AndAndExpressionContext;
import mrmathami.box.lang.BoxLangParser.AndExpressionContext;
import mrmathami.box.lang.BoxLangParser.ArrayCreationExpressionContext;
import mrmathami.box.lang.BoxLangParser.ArraySizeLiteralContext;
import mrmathami.box.lang.BoxLangParser.ArraySuffixContext;
import mrmathami.box.lang.BoxLangParser.ArrayTypeContext;
import mrmathami.box.lang.BoxLangParser.AssignableExpressionContext;
import mrmathami.box.lang.BoxLangParser.AssignmentStatementContext;
import mrmathami.box.lang.BoxLangParser.BeforeArrayTypeContext;
import mrmathami.box.lang.BoxLangParser.BlockStatementContext;
import mrmathami.box.lang.BoxLangParser.BreakStatementContext;
import mrmathami.box.lang.BoxLangParser.CastExpressionContext;
import mrmathami.box.lang.BoxLangParser.ComparisonExpressionContext;
import mrmathami.box.lang.BoxLangParser.CompilationUnitContext;
import mrmathami.box.lang.BoxLangParser.ConditionalExpressionContext;
import mrmathami.box.lang.BoxLangParser.ContinueStatementContext;
import mrmathami.box.lang.BoxLangParser.DefinitionContext;
import mrmathami.box.lang.BoxLangParser.ExpressionContext;
import mrmathami.box.lang.BoxLangParser.ExpressionListContext;
import mrmathami.box.lang.BoxLangParser.FunctionCallExpressionContext;
import mrmathami.box.lang.BoxLangParser.FunctionCallStatementContext;
import mrmathami.box.lang.BoxLangParser.FunctionDefinitionContext;
import mrmathami.box.lang.BoxLangParser.IfStatementContext;
import mrmathami.box.lang.BoxLangParser.LiteralExpressionContext;
import mrmathami.box.lang.BoxLangParser.LoopStatementContext;
import mrmathami.box.lang.BoxLangParser.MultiplyExpressionContext;
import mrmathami.box.lang.BoxLangParser.NegativeExpressionContext;
import mrmathami.box.lang.BoxLangParser.NotExpressionContext;
import mrmathami.box.lang.BoxLangParser.OrExpressionContext;
import mrmathami.box.lang.BoxLangParser.OrOrExpressionContext;
import mrmathami.box.lang.BoxLangParser.ParameterDefinitionContext;
import mrmathami.box.lang.BoxLangParser.ParenthesesExpressionContext;
import mrmathami.box.lang.BoxLangParser.ReturnStatementContext;
import mrmathami.box.lang.BoxLangParser.ShiftExpressionContext;
import mrmathami.box.lang.BoxLangParser.SignExpressionContext;
import mrmathami.box.lang.BoxLangParser.SimpleTypeContext;
import mrmathami.box.lang.BoxLangParser.SingleStatementContext;
import mrmathami.box.lang.BoxLangParser.StatementContext;
import mrmathami.box.lang.BoxLangParser.TupleCreationExpressionContext;
import mrmathami.box.lang.BoxLangParser.TupleDefinitionContext;
import mrmathami.box.lang.BoxLangParser.TupleTypeContext;
import mrmathami.box.lang.BoxLangParser.TypeContext;
import mrmathami.box.lang.BoxLangParser.VariableExpressionContext;
import mrmathami.box.lang.BoxLangParser.VariableInitializerContext;
import mrmathami.box.lang.BoxLangParser.VariablesDefinitionContext;
import mrmathami.box.lang.BoxLangParser.XorExpressionContext;
import mrmathami.box.lang.BoxLangParser.XorXorExpressionContext;
import mrmathami.box.lang.ast.CompilationUnit;
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.definition.ParameterDefinition;
import mrmathami.box.lang.ast.definition.Definition;
import mrmathami.box.lang.ast.definition.FunctionDefinition;
import mrmathami.box.lang.ast.definition.TupleDefinition;
import mrmathami.box.lang.ast.definition.TupleMemberDefinition;
import mrmathami.box.lang.ast.definition.VariableDefinition;
import mrmathami.box.lang.ast.expression.CastExpression;
import mrmathami.box.lang.ast.expression.ComparisonExpression;
import mrmathami.box.lang.ast.expression.ConditionalExpression;
import mrmathami.box.lang.ast.expression.Expression;
import mrmathami.box.lang.ast.expression.LiteralExpression;
import mrmathami.box.lang.ast.expression.ShiftExpression;
import mrmathami.box.lang.ast.expression.SimpleBinaryExpression;
import mrmathami.box.lang.ast.expression.UnaryExpression;
import mrmathami.box.lang.ast.expression.access.AccessExpression;
import mrmathami.box.lang.ast.expression.access.AccessibleExpression;
import mrmathami.box.lang.ast.expression.access.ArrayAccessExpression;
import mrmathami.box.lang.ast.expression.access.AssignableExpression;
import mrmathami.box.lang.ast.expression.access.FunctionCallExpression;
import mrmathami.box.lang.ast.expression.access.MemberAccessExpression;
import mrmathami.box.lang.ast.expression.access.ParameterExpression;
import mrmathami.box.lang.ast.expression.access.VariableExpression;
import mrmathami.box.lang.ast.expression.creation.ArrayCreationExpression;
import mrmathami.box.lang.ast.expression.creation.TupleCreationExpression;
import mrmathami.box.lang.ast.expression.other.AssignmentOperator;
import mrmathami.box.lang.ast.expression.other.Keyword;
import mrmathami.box.lang.ast.expression.other.Operator;
import mrmathami.box.lang.ast.identifier.FunctionIdentifier;
import mrmathami.box.lang.ast.identifier.Identifier;
import mrmathami.box.lang.ast.identifier.MemberIdentifier;
import mrmathami.box.lang.ast.identifier.ParameterIdentifier;
import mrmathami.box.lang.ast.identifier.TupleIdentifier;
import mrmathami.box.lang.ast.identifier.VariableIdentifier;
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
import mrmathami.utils.Pair;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import static mrmathami.box.lang.BoxLangParser.AccessibleExpressionContext;
import static mrmathami.box.lang.BoxLangParser.MemberDefinitionContext;
import static mrmathami.box.lang.BoxLangParser.ParameterExpressionContext;

public class Builder {
	@Nonnull private static final BigInteger SIGNER_I8 = new BigInteger("-100", 16);
	@Nonnull private static final BigInteger SIGNER_I16 = new BigInteger("-10000", 16);
	@Nonnull private static final BigInteger SIGNER_I32 = new BigInteger("-100000000", 16);
	@Nonnull private static final BigInteger SIGNER_I64 = new BigInteger("-10000000000000000", 16);

	@Nullable private Builder parentBuilder;
	@Nonnull private final List<Identifier> identifiers = new ArrayList<>();

	//region parsed context processor

	@Nonnull
	private CompilationUnit compilationUnit(@Nonnull CompilationUnitContext context)
			throws InvalidASTException {
		final List<Definition> definitions = new ArrayList<>();
		for (final var definitionContext : context.definition()) {
			definitions.addAll(definition(definitionContext));
		}
		return new CompilationUnit(definitions);
	}

	//region definition

	@Nonnull
	private List<? extends Definition> definition(@Nonnull DefinitionContext context)
			throws InvalidASTException {
		final TupleDefinitionContext tupleDefinition = context.tupleDefinition();
		if (tupleDefinition != null) return List.of(tupleDefinition(tupleDefinition));
		final VariablesDefinitionContext variablesDefinition = context.variablesDefinition();
		if (variablesDefinition != null) return variablesDefinition(variablesDefinition);
		final FunctionDefinitionContext functionDefinition = context.functionDefinition();
		if (functionDefinition != null) return List.of(functionDefinition(functionDefinition));
		// how do we get here?
		throw new InvalidASTException("Invalid definition: " + context);
	}

	@Nonnull
	private List<VariableDefinition> variablesDefinition(@Nonnull VariablesDefinitionContext context)
			throws InvalidASTException {
		final Type type = type(context.type());
		final List<VariableDefinition> definitions = new ArrayList<>();
		for (final var variableInitializerContext : context.variableInitializer()) {
			definitions.add(variableDefinition(type, variableInitializerContext));
		}
		return definitions;
	}

	@Nonnull
	private VariableDefinition variableDefinition(@Nonnull Type type, @Nonnull VariableInitializerContext context)
			throws InvalidASTException {
		final VariableIdentifier variableIdentifier = variableIdentifier(context.VariableIdentifier());
		final ExpressionContext expressionContext = context.expression();
		final Expression expression = expressionContext != null ? expression(expressionContext) : null;
		return new VariableDefinition(type, variableIdentifier, expression);
	}

	@Nonnull
	private TupleDefinition tupleDefinition(@Nonnull TupleDefinitionContext context)
			throws InvalidASTException {
		final TupleIdentifier identifier = tupleIdentifier(context.TupleIdentifier());
		final List<TupleMemberDefinition> definitions = new ArrayList<>();
		for (final var memberDefinitionContext : context.memberDefinition()) {
			definitions.add(memberDefinition(memberDefinitionContext));
		}
		return new TupleDefinition(identifier, definitions);
	}

	@Nonnull
	private TupleMemberDefinition memberDefinition(@Nonnull MemberDefinitionContext context)
			throws InvalidASTException {
		final Type type = type(context.type());
		final MemberIdentifier identifier = memberIdentifier(context.MemberIdentifier());
		return new TupleMemberDefinition(type, identifier);
	}

	@Nonnull
	private FunctionDefinition functionDefinition(@Nonnull FunctionDefinitionContext context)
			throws InvalidASTException {
		final Type type = type(context.type());
		final FunctionIdentifier identifier = functionIdentifier(context.FunctionIdentifier());
		final List<ParameterDefinition> definitions = new ArrayList<>();
		for (final var parameterDefinitionContext : context.parameterDefinition()) {
			definitions.add(parameterDefinition(parameterDefinitionContext));
		}
		final BlockStatement statement = blockStatement(context.blockStatement());
		return new FunctionDefinition(type, identifier, definitions, statement);
	}

	@Nonnull
	private ParameterDefinition parameterDefinition(@Nonnull ParameterDefinitionContext context)
			throws InvalidASTException {
		final Type type = type(context.type());
		final ParameterIdentifier identifier = parameterIdentifier(context.ParameterIdentifier());
		return new ParameterDefinition(type, identifier);
	}

	//endregion definition

	//region identifier

	@Nonnull
	private VariableIdentifier variableIdentifier(@Nonnull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.VariableIdentifier;
		return new VariableIdentifier(identifier.getText());
	}

	@Nonnull
	private TupleIdentifier tupleIdentifier(@Nonnull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.TupleIdentifier;
		return new TupleIdentifier(identifier.getText());
	}

	@Nonnull
	private MemberIdentifier memberIdentifier(@Nonnull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.MemberIdentifier;
		return new MemberIdentifier(identifier.getText());
	}

	@Nonnull
	private FunctionIdentifier functionIdentifier(@Nonnull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.FunctionIdentifier;
		return new FunctionIdentifier(identifier.getText());
	}

	@Nonnull
	private ParameterIdentifier parameterIdentifier(@Nonnull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.ParameterIdentifier;
		return new ParameterIdentifier(identifier.getText());
	}

	//endregion identifier

	//region type

	@Nonnull
	private Type type(@Nonnull TypeContext context)
			throws InvalidASTException {
		final ArrayTypeContext arrayType = context.arrayType();
		if (arrayType != null) return arrayType(arrayType);
		final SimpleTypeContext simpleType = context.simpleType();
		if (simpleType != null) return simpleType(simpleType);
		final TupleTypeContext tupleType = context.tupleType();
		if (tupleType != null) return tupleType(tupleType);
		throw new InvalidASTException("Unknown type: " + context);
	}

	//region ArrayType

	@Nonnull
	private Type beforeArrayType(@Nonnull BeforeArrayTypeContext context)
			throws InvalidASTException {
		final SimpleTypeContext simpleType = context.simpleType();
		if (simpleType != null) return simpleType(simpleType);
		final TupleTypeContext tupleType = context.tupleType();
		if (tupleType != null) return tupleType(tupleType);
		throw new InvalidASTException("Unknown type: " + context);
	}

	@Nonnull
	private long[] arraySuffix(@Nonnull ArraySuffixContext context)
			throws InvalidASTException {
		final List<ArraySizeLiteralContext> sizeLiterals = context.arraySizeLiteral();
		final long[] dimensionSizes = new long[sizeLiterals.size()];
		int i = 0;
		for (final ArraySizeLiteralContext sizeLiteral : sizeLiterals) {
			final TerminalNode untypedNumberLiteral = sizeLiteral.UntypedNumberLiteral();
			dimensionSizes[i++] = untypedNumberLiteral != null
					? untypedNumber(untypedNumberLiteral.toString())
					: -1;
		}
		return dimensionSizes;
	}

	@Nonnull
	private Type arrayType(@Nonnull ArrayTypeContext context)
			throws InvalidASTException {
		Type currentType = beforeArrayType(context.beforeArrayType());
		for (final ArraySuffixContext arraySuffix : context.arraySuffix()) {
			currentType = new ArrayType(currentType, arraySuffix(arraySuffix));
		}
		return currentType;
	}

	//endregion ArrayType

	@Nonnull
	private SimpleType simpleType(@Nonnull SimpleTypeContext context) {
		assert context.getStart() == context.getStop();
		final String text = context.getText();
		for (final SimpleType value : SimpleType.values) {
			if (value.toString().equals(text)) return value;
		}
		throw up();
	}

	@Nonnull
	private TupleType tupleType(@Nonnull TupleTypeContext tupleType) {
		return new TupleType(tupleIdentifier(tupleType.TupleIdentifier()));
	}

	//endregion type

	//region expression

	//region misc

	@Nonnull
	private Expression miscSingleBinaryExpression(@Nonnull List<? extends ParserRuleContext> expressionContexts,
			@Nonnull Operator operator, @Nonnull BiFunction<List<Expression>, Operator, Expression> constructor)
			throws InvalidASTException {
		final List<Expression> expressions = new ArrayList<>();
		for (final ParserRuleContext expressionContext : expressionContexts) {
			expressions.add(expression(expressionContext));
		}
		return constructor.apply(expressions, operator);
	}

	@Nonnull
	private Expression miscCombinedBinaryExpression(@Nonnull List<? extends ParserRuleContext> expressionContexts,
			@Nonnull List<? extends ParserRuleContext> operatorContexts,
			@Nonnull BiFunction<List<Expression>, Operator, Expression> constructor)
			throws InvalidASTException {
		final Deque<Expression> expressions = new ArrayDeque<>();
		for (final ParserRuleContext expressionContext : expressionContexts) {
			expressions.add(expression(expressionContext));
		}
		final Deque<Operator> operators = new ArrayDeque<>();
		for (final ParserRuleContext operatorContext : operatorContexts) {
			operators.add(binaryOperator(operatorContext));
		}
		while (true) {
			assert expressions.size() >= 2 && expressions.size() == operators.size() + 1;

			final List<Expression> operands = new ArrayList<>(expressions.size());
			operands.add(expressions.poll());
			operands.add(expressions.poll());

			final Operator operator = operators.poll();
			assert operator != null && operator.isBinary();

			while (operator.equals(operators.peek())) {
				operators.poll();
				operands.add(expressions.poll());
			}
			final Expression expression = constructor.apply(operands, operator);
			if (operators.isEmpty()) return expression;

			assert expressions.size() == operators.size();
			expressions.push(expression);
		}
	}

	@Nonnull
	private List<Expression> miscExpressionList(@Nonnull ExpressionListContext context)
			throws InvalidASTException {
		final List<Expression> expressions = new ArrayList<>();
		for (final ExpressionContext expression : context.expression()) {
			expressions.add(expression(expression));
		}
		return expressions;
	}

	//endregion misc

	@Nonnull
	private Expression expression(@Nonnull ParserRuleContext context)
			throws InvalidASTException {
		final var conditionalExpression = context.getChild(ConditionalExpressionContext.class, 0);
		if (conditionalExpression != null) return conditionalExpression(conditionalExpression);
		final var orOrExpression = context.getChild(OrOrExpressionContext.class, 0);
		if (orOrExpression != null) return orOrExpression(orOrExpression);
		final var xorXorExpression = context.getChild(XorXorExpressionContext.class, 0);
		if (xorXorExpression != null) return xorXorExpression(xorXorExpression);
		final var andAndExpression = context.getChild(AndAndExpressionContext.class, 0);
		if (andAndExpression != null) return andAndExpression(andAndExpression);
		final var notExpression = context.getChild(NotExpressionContext.class, 0);
		if (notExpression != null) return notExpression(notExpression);
		final var comparisonExpression = context.getChild(ComparisonExpressionContext.class, 0);
		if (comparisonExpression != null) return comparisonExpression(comparisonExpression);
		final var addExpression = context.getChild(AddExpressionContext.class, 0);
		if (addExpression != null) return addExpression(addExpression);
		final var multiplyExpression = context.getChild(MultiplyExpressionContext.class, 0);
		if (multiplyExpression != null) return multiplyExpression(multiplyExpression);
		final var signExpression = context.getChild(SignExpressionContext.class, 0);
		if (signExpression != null) return signExpression(signExpression);
		final var orExpression = context.getChild(OrExpressionContext.class, 0);
		if (orExpression != null) return orExpression(orExpression);
		final var xorExpression = context.getChild(XorExpressionContext.class, 0);
		if (xorExpression != null) return xorExpression(xorExpression);
		final var andExpression = context.getChild(AndExpressionContext.class, 0);
		if (andExpression != null) return andExpression(andExpression);
		final var shiftExpression = context.getChild(ShiftExpressionContext.class, 0);
		if (shiftExpression != null) return shiftExpression(shiftExpression);
		final var negativeExpression = context.getChild(NegativeExpressionContext.class, 0);
		if (negativeExpression != null) return negativeExpression(negativeExpression);
		final var accessExpression = context.getChild(AccessExpressionContext.class, 0);
		if (accessExpression != null) return accessExpression(accessExpression);
		final var variableExpression = context.getChild(VariableExpressionContext.class, 0);
		if (variableExpression != null) return variableExpression(variableExpression);
		final var parameterExpression = context.getChild(ParameterExpressionContext.class, 0);
		if (parameterExpression != null) return parameterExpression(parameterExpression);
		final var functionCallExpression = context.getChild(FunctionCallExpressionContext.class, 0);
		if (functionCallExpression != null) return functionCallExpression(functionCallExpression);
		final var literalExpression = context.getChild(LiteralExpressionContext.class, 0);
		if (literalExpression != null) return literalExpression(literalExpression);
		final var castExpression = context.getChild(CastExpressionContext.class, 0);
		if (castExpression != null) return castExpression(castExpression);
		final var parenthesesExpression = context.getChild(ParenthesesExpressionContext.class, 0);
		if (parenthesesExpression != null) return parenthesesExpression(parenthesesExpression);
		final var arrayCreationExpression = context.getChild(ArrayCreationExpressionContext.class, 0);
		if (arrayCreationExpression != null) return arrayCreationExpression(arrayCreationExpression);
		final var tupleCreationExpression = context.getChild(TupleCreationExpressionContext.class, 0);
		if (tupleCreationExpression != null) return tupleCreationExpression(tupleCreationExpression);
		throw up();
	}

	@Nonnull
	private Expression conditionalExpression(@Nonnull ConditionalExpressionContext context)
			throws InvalidASTException {
		final Expression condition = expression(context.beforeConditionalExpression());
		final Expression trueExpression = expression(context.expression(0));
		final Expression falseExpression = expression(context.expression(1));
		return new ConditionalExpression(condition, trueExpression, falseExpression);
	}

	@Nonnull
	private Expression orOrExpression(@Nonnull OrOrExpressionContext context)
			throws InvalidASTException {
		return miscSingleBinaryExpression(context.beforeOrOrExpression(),
				Operator.LOGIC_OR, SimpleBinaryExpression::new);
	}

	@Nonnull
	private Expression xorXorExpression(@Nonnull XorXorExpressionContext context)
			throws InvalidASTException {
		return miscSingleBinaryExpression(context.beforeXorXorExpression(),
				Operator.LOGIC_XOR, SimpleBinaryExpression::new);
	}

	@Nonnull
	private Expression andAndExpression(@Nonnull AndAndExpressionContext context)
			throws InvalidASTException {
		return miscSingleBinaryExpression(context.beforeAndAndExpression(),
				Operator.LOGIC_AND, SimpleBinaryExpression::new);
	}

	@Nonnull
	private Expression notExpression(@Nonnull NotExpressionContext context)
			throws InvalidASTException {
		final Expression expression = expression(context.beforeUnaryExpression());
		return new UnaryExpression(Operator.LOGIC_NOT, expression);
	}

	@Nonnull
	private Expression comparisonExpression(@Nonnull ComparisonExpressionContext context)
			throws InvalidASTException {
		return miscCombinedBinaryExpression(context.beforeComparisonExpression(),
				context.comparisonOperator(), ComparisonExpression::new);
	}

	@Nonnull
	private Expression addExpression(@Nonnull AddExpressionContext context)
			throws InvalidASTException {
		return miscCombinedBinaryExpression(context.beforeAddExpression(),
				context.addOperator(), SimpleBinaryExpression::new);
	}

	@Nonnull
	private Expression multiplyExpression(@Nonnull MultiplyExpressionContext context)
			throws InvalidASTException {
		return miscCombinedBinaryExpression(context.beforeMultiplyExpression(),
				context.multiplyOperator(), SimpleBinaryExpression::new);
	}

	@Nonnull
	private Expression signExpression(@Nonnull SignExpressionContext context)
			throws InvalidASTException {
		final Expression expression = expression(context.beforeUnaryExpression());
		return new UnaryExpression(unaryOperator(context.signOperator()), expression);
	}

	@Nonnull
	private Expression orExpression(@Nonnull OrExpressionContext context)
			throws InvalidASTException {
		return miscSingleBinaryExpression(context.beforeOrExpression(),
				Operator.BIT_OR, SimpleBinaryExpression::new);
	}

	@Nonnull
	private Expression xorExpression(@Nonnull XorExpressionContext context)
			throws InvalidASTException {
		return miscSingleBinaryExpression(context.beforeXorExpression(),
				Operator.BIT_XOR, SimpleBinaryExpression::new);
	}

	@Nonnull
	private Expression andExpression(@Nonnull AndExpressionContext context)
			throws InvalidASTException {
		return miscSingleBinaryExpression(context.beforeAndExpression(),
				Operator.BIT_AND, SimpleBinaryExpression::new);
	}

	@Nonnull
	private Expression shiftExpression(@Nonnull ShiftExpressionContext context)
			throws InvalidASTException {
		return miscCombinedBinaryExpression(context.beforeShiftExpression(),
				context.shiftOperator(), ShiftExpression::new);
	}

	@Nonnull
	private Expression negativeExpression(@Nonnull NegativeExpressionContext context)
			throws InvalidASTException {
		final Expression expression = expression(context.beforeUnaryExpression());
		return new UnaryExpression(Operator.BIT_NEGATIVE, expression);
	}

	//region AccessExpression

	@Nonnull
	private AccessExpression miscAccessSuffix(@Nonnull AccessibleExpression accessibleExpression,
			@Nonnull AccessSuffixContext context)
			throws InvalidASTException {
		final TerminalNode memberIdentifier = context.MemberIdentifier();
		if (memberIdentifier != null) {
			return new MemberAccessExpression(accessibleExpression, memberIdentifier(memberIdentifier));
		}
		final List<Expression> expressions = miscExpressionList(context.expressionList());
		return new ArrayAccessExpression(accessibleExpression, expressions);
	}

	@Nonnull
	private AccessExpression accessExpression(@Nonnull AccessExpressionContext context)
			throws InvalidASTException {
		final AccessibleExpression accessibleExpression = accessibleExpression(context.accessibleExpression());
		final Iterator<AccessSuffixContext> iterator = context.accessSuffix().iterator();
		assert iterator.hasNext();
		AccessExpression accessExpression = miscAccessSuffix(accessibleExpression, iterator.next());
		while (iterator.hasNext()) {
			accessExpression = miscAccessSuffix(accessExpression, iterator.next());
		}
		return accessExpression;
	}

	@Nonnull
	private AccessibleExpression accessibleExpression(@Nonnull AccessibleExpressionContext context)
			throws InvalidASTException {
		final VariableExpressionContext variableExpression = context.variableExpression();
		if (variableExpression != null) return variableExpression(variableExpression);
		final ParameterExpressionContext parameterExpression = context.parameterExpression();
		if (parameterExpression != null) return parameterExpression(parameterExpression);
		return functionCallExpression(context.functionCallExpression());
	}

	@Nonnull
	private AssignableExpression assignableExpression(@Nonnull AssignableExpressionContext context)
			throws InvalidASTException {
		final AccessExpressionContext accessExpression = context.accessExpression();
		if (accessExpression != null) return accessExpression(accessExpression);
		return variableExpression(context.variableExpression());
	}

	//endregion AccessExpression

	@Nonnull
	private VariableExpression variableExpression(@Nonnull VariableExpressionContext context) {
		return new VariableExpression(variableIdentifier(context.VariableIdentifier()));
	}

	@Nonnull
	private ParameterExpression parameterExpression(@Nonnull ParameterExpressionContext context) {
		return new ParameterExpression(parameterIdentifier(context.ParameterIdentifier()));
	}

	@Nonnull
	private FunctionCallExpression functionCallExpression(@Nonnull FunctionCallExpressionContext context)
			throws InvalidASTException {
		final FunctionIdentifier identifier = functionIdentifier(context.FunctionIdentifier());
		final ExpressionListContext expressionList = context.expressionList();
		final List<Expression> expressions = expressionList != null
				? miscExpressionList(expressionList)
				: List.of();
		return new FunctionCallExpression(identifier, expressions);
	}

	@Nonnull
	private Expression literalExpression(@Nonnull LiteralExpressionContext context)
			throws InvalidASTException {
		assert context.getStart() == context.getStop();
		final TerminalNode typedNumberLiteral = context.TypedNumberLiteral();
		if (typedNumberLiteral != null) return typedNumber(typedNumberLiteral.getText());
		if (context.False() != null) return new LiteralExpression(SimpleType.BOOL, Keyword.FALSE);
		if (context.True() != null) return new LiteralExpression(SimpleType.BOOL, Keyword.TRUE);
		if (context.Null() != null) return new LiteralExpression(SimpleType.VOID, Keyword.NULL);
		throw up();
	}

	@Nonnull
	private Expression castExpression(@Nonnull CastExpressionContext context)
			throws InvalidASTException {
		final SimpleType type = simpleType(context.simpleType());
		final Expression expression = expression(context.expression());
		return new CastExpression(type, expression);
	}

	@Nonnull
	private Expression parenthesesExpression(@Nonnull ParenthesesExpressionContext context)
			throws InvalidASTException {
		return expression(context.expression());
	}

	@Nonnull
	private Expression arrayCreationExpression(@Nonnull ArrayCreationExpressionContext context)
			throws InvalidASTException {
		final Type innerType = type(context.type());
		final List<Expression> expressions = miscExpressionList(context.expressionList());
		final long[] dimensionSizes = new long[expressions.size()];
		Arrays.fill(dimensionSizes, -1);
		final ArrayType type = new ArrayType(innerType, dimensionSizes);
		return new ArrayCreationExpression(type, expressions);
	}

	@Nonnull
	private Expression tupleCreationExpression(@Nonnull TupleCreationExpressionContext context)
			throws InvalidASTException {
		final TupleType type = tupleType(context.tupleType());
		final List<Expression> expressions = miscExpressionList(context.expressionList());
		return new TupleCreationExpression(type, expressions);
	}

	//endregion expression

	//region statement

	@Nonnull
	private List<? extends Statement> statement(@Nonnull StatementContext context)
			throws InvalidASTException {
		final DefinitionContext definition = context.definition();
		if (definition != null) return definition(definition);
		final SingleStatementContext singleStatement = context.singleStatement();
		if (singleStatement != null) return List.of(singleStatement(singleStatement));
		throw new InvalidASTException("Invalid statement: " + context);
	}

	@Nonnull
	private SingleStatement singleStatement(@Nonnull SingleStatementContext context)
			throws InvalidASTException {
		final FunctionCallStatementContext functionCallStatement = context.functionCallStatement();
		if (functionCallStatement != null) return functionCallStatement(functionCallStatement);
		final ReturnStatementContext returnStatement = context.returnStatement();
		if (returnStatement != null) return returnStatement(returnStatement);
		final BreakStatementContext breakStatement = context.breakStatement();
		if (breakStatement != null) return breakStatement(breakStatement);
		final ContinueStatementContext continueStatement = context.continueStatement();
		if (continueStatement != null) return continueStatement(continueStatement);
		final AssignmentStatementContext assignmentStatement = context.assignmentStatement();
		if (assignmentStatement != null) return assignmentStatement(assignmentStatement);
		final BlockStatementContext blockStatement = context.blockStatement();
		if (blockStatement != null) return blockStatement(blockStatement);
		final IfStatementContext ifStatement = context.ifStatement();
		if (ifStatement != null) return ifStatement(ifStatement);
		return loopStatement(context.loopStatement());
	}

	@Nonnull
	private FunctionCallStatement functionCallStatement(@Nonnull FunctionCallStatementContext context)
			throws InvalidASTException {
		final FunctionCallExpression expression = functionCallExpression(context.functionCallExpression());
		return new FunctionCallStatement(expression);
	}

	@Nonnull
	private ReturnStatement returnStatement(@Nonnull ReturnStatementContext context)
			throws InvalidASTException {
		final ExpressionContext expressionContext = context.expression();
		final Expression expression = expressionContext != null ? expression(expressionContext) : null;
		return new ReturnStatement(expression);
	}

	@Nonnull
	private BreakStatement breakStatement(@Nonnull BreakStatementContext context)
			throws InvalidASTException {
		final TerminalNode untypedNumber = context.UntypedNumberLiteral();
		final long value = untypedNumber != null ? untypedNumber(untypedNumber.getText()) : 0;
		return new BreakStatement(value);
	}

	@Nonnull
	private ContinueStatement continueStatement(@Nonnull ContinueStatementContext context)
			throws InvalidASTException {
		final TerminalNode untypedNumber = context.UntypedNumberLiteral();
		final long value = untypedNumber != null ? untypedNumber(untypedNumber.getText()) : 0;
		return new ContinueStatement(value);
	}

	@Nonnull
	private AssignmentStatement assignmentStatement(@Nonnull AssignmentStatementContext context)
			throws InvalidASTException {
		final AssignableExpression assignableExpression = assignableExpression(context.assignableExpression());
		final AssignmentOperator assignmentOperator = assignmentOperator(context.assignmentOperator());
		final Expression expression = expression(context.expression());
		return new AssignmentStatement(assignableExpression, assignmentOperator, expression);
	}

	@Nonnull
	private BlockStatement blockStatement(@Nonnull BlockStatementContext context)
			throws InvalidASTException {
		final List<Statement> statements = new ArrayList<>();
		for (final var statementContext : context.statement()) {
			statements.addAll(statement(statementContext));
		}
		return new BlockStatement(statements);
	}

	@Nonnull
	private IfStatement ifStatement(@Nonnull IfStatementContext context)
			throws InvalidASTException {
		final List<Pair<Expression, SingleStatement>> pairs = new ArrayList<>();
		for (final var singleCaseContext : context.ifSingleCase()) {
			final Expression condition = expression(singleCaseContext.expression());
			final SingleStatementContext statementContext = singleCaseContext.singleStatement();
			final SingleStatement statement = statementContext != null
					? singleStatement(statementContext)
					: null;
			pairs.add(Pair.immutableOf(condition, statement));
		}
		final SingleStatementContext statementContext = context.singleStatement();
		final SingleStatement alternativeStatement = statementContext != null
				? singleStatement(statementContext)
				: null;
		return new IfStatement(pairs, alternativeStatement);
	}

	@Nonnull
	private LoopStatement loopStatement(@Nonnull LoopStatementContext context)
			throws InvalidASTException {
		final SingleStatementContext singleStatement = context.singleStatement();
		final SingleStatement statement = singleStatement != null ? singleStatement(singleStatement) : null;
		return new LoopStatement(statement);
	}

	//endregion statement

	//region misc: number parser

	@Nonnull
	private static BigInteger parseUntypedNumber(@Nonnull String untypedNumber) {
		assert untypedNumber.matches("[0-9]+|0x[0-9A-Fa-f]+|0t[0-7]+|0b[01]+");
		if (untypedNumber.length() >= 3 && untypedNumber.charAt(0) == '0') {
			if (untypedNumber.charAt(1) == 't') {
				return new BigInteger(untypedNumber.substring(2), 8);
			} else if (untypedNumber.charAt(1) == 'x') {
				return new BigInteger(untypedNumber.substring(2), 16);
			} else if (untypedNumber.charAt(1) == 'b') {
				return new BigInteger(untypedNumber.substring(2), 2);
			}
		}
		return new BigInteger(untypedNumber, 10);
	}

	@Unsigned
	private long untypedNumber(@Nonnull String untypedNumber)
			throws InvalidASTException {
		assert untypedNumber.matches("[0-9]+|0x[0-9A-Fa-f]+|0t[0-7]+|0b[01]+");
		try {
			return parseUntypedNumber(untypedNumber).longValueExact();
		} catch (ArithmeticException exception) {
			throw new InvalidASTException("Invalid untyped number: " + untypedNumber, exception);
		}
	}

	@Nonnull
	private LiteralExpression typedNumber(@Nonnull String typedNumber)
			throws InvalidASTException {
		assert typedNumber.matches("([0-9]+|0x[0-9A-Fa-f]+|0t[0-7]+|0b[01]+)[bsilBSIL]");
		final int lastIndex = typedNumber.length() - 1;
		final char suffix = typedNumber.charAt(lastIndex);
		final SimpleType type
				= suffix == 'b' ? SimpleType.NUMBER_U8
				: suffix == 's' ? SimpleType.NUMBER_U16
				: suffix == 'i' ? SimpleType.NUMBER_U32
				: suffix == 'l' ? SimpleType.NUMBER_U64
				: suffix == 'B' ? SimpleType.NUMBER_I8
				: suffix == 'S' ? SimpleType.NUMBER_I16
				: suffix == 'I' ? SimpleType.NUMBER_I32
				: suffix == 'L' ? SimpleType.NUMBER_I64
				: null;
		assert type != null;
		final BigInteger number = parseUntypedNumber(typedNumber.substring(0, lastIndex));
		final int bitLength = number.bitLength();
		if (type == SimpleType.NUMBER_U8) {
			if (bitLength <= 8) return new LiteralExpression(type, number);
		} else if (type == SimpleType.NUMBER_U16) {
			if (bitLength <= 16) return new LiteralExpression(type, number);
		} else if (type == SimpleType.NUMBER_U32) {
			if (bitLength <= 32) return new LiteralExpression(type, number);
		} else if (type == SimpleType.NUMBER_U64) {
			if (bitLength <= 64) return new LiteralExpression(type, number);
		} else if (type == SimpleType.NUMBER_I8) {
			if (bitLength <= 7) return new LiteralExpression(type, number);
			if (bitLength == 8) return new LiteralExpression(type, number.add(SIGNER_I8));
		} else if (type == SimpleType.NUMBER_I16) {
			if (bitLength <= 15) return new LiteralExpression(type, number);
			if (bitLength == 16) return new LiteralExpression(type, number.add(SIGNER_I16));
		} else if (type == SimpleType.NUMBER_I32) {
			if (bitLength <= 31) return new LiteralExpression(type, number);
			if (bitLength == 32) return new LiteralExpression(type, number.add(SIGNER_I32));
		} else {
			if (bitLength <= 63) return new LiteralExpression(type, number);
			if (bitLength == 64) return new LiteralExpression(type, number.add(SIGNER_I64));
		}
		throw new InvalidASTException("Invalid typed number: " + typedNumber);
	}

	//endregion misc: number parser

	//region misc: operator parser

	@Nonnull
	private AssignmentOperator assignmentOperator(@Nonnull ParserRuleContext context) {
		assert context.getStart() == context.getStop();
		final String text = context.getText();
		for (final AssignmentOperator value : AssignmentOperator.values) {
			if (value.toString().equals(text)) return value;
		}
		throw up();
	}

	@Nonnull
	private Operator unaryOperator(@Nonnull ParserRuleContext context) {
		assert context.getStart() == context.getStop();
		final String text = context.getText();
		for (final Operator value : Operator.values) {
			if (value.isUnary() && value.toString().equals(text)) return value;
		}
		throw up();
	}

	@Nonnull
	private Operator binaryOperator(@Nonnull ParserRuleContext context) {
		assert context.getStart() == context.getStop();
		final String text = context.getText();
		for (final Operator value : Operator.values) {
			if (value.isBinary() && value.toString().equals(text)) return value;
		}
		throw up();
	}

	//endregion misc: operator parser

	//endregion parsed context processor

	//region


	//endregion

	@Nonnull
	private static RuntimeException up() {
		return new RuntimeException("This should not happen!");
	}

	public static void main(@Nonnull String[] args) throws IOException, InvalidASTException {
		final String string = Files.readString(Path.of("./test/input.txt"));
		final BoxLangLexer lexer = new BoxLangLexer(CharStreams.fromString(string));

		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final BoxLangParser parser = new BoxLangParser(tokens);
//		final ParseTree tree = parser.compilationUnit();
//
//		System.out.println(tree);

		final Builder builder = new Builder();
		final CompilationUnit compilationUnit = builder.compilationUnit(parser.compilationUnit());
		System.out.println(compilationUnit);
	}
}