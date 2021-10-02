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
import mrmathami.box.lang.BoxLangParser.BeforeAndAndExpressionContext;
import mrmathami.box.lang.BoxLangParser.BeforeAndExpressionContext;
import mrmathami.box.lang.BoxLangParser.BeforeArrayTypeContext;
import mrmathami.box.lang.BoxLangParser.BeforeOrExpressionContext;
import mrmathami.box.lang.BoxLangParser.BeforeOrOrExpressionContext;
import mrmathami.box.lang.BoxLangParser.BeforeXorExpressionContext;
import mrmathami.box.lang.BoxLangParser.BeforeXorXorExpressionContext;
import mrmathami.box.lang.BoxLangParser.BlockStatementContext;
import mrmathami.box.lang.BoxLangParser.BreakStatementContext;
import mrmathami.box.lang.BoxLangParser.CastExpressionContext;
import mrmathami.box.lang.BoxLangParser.ComparisonExpressionContext;
import mrmathami.box.lang.BoxLangParser.CompilationUnitContext;
import mrmathami.box.lang.BoxLangParser.ConditionalExpressionContext;
import mrmathami.box.lang.BoxLangParser.ContinueStatementContext;
import mrmathami.box.lang.BoxLangParser.ExpressionContext;
import mrmathami.box.lang.BoxLangParser.ExpressionListContext;
import mrmathami.box.lang.BoxLangParser.FunctionCallExpressionContext;
import mrmathami.box.lang.BoxLangParser.FunctionCallStatementContext;
import mrmathami.box.lang.BoxLangParser.FunctionDefinitionContext;
import mrmathami.box.lang.BoxLangParser.GlobalDefinitionContext;
import mrmathami.box.lang.BoxLangParser.IfStatementContext;
import mrmathami.box.lang.BoxLangParser.LabelDefinitionContext;
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
import mrmathami.box.lang.BoxLangParser.VariableDefinitionContext;
import mrmathami.box.lang.BoxLangParser.VariableExpressionContext;
import mrmathami.box.lang.BoxLangParser.XorExpressionContext;
import mrmathami.box.lang.BoxLangParser.XorXorExpressionContext;
import mrmathami.box.lang.ast.AssignmentOperator;
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
import mrmathami.box.lang.ast.identifier.FunctionIdentifier;
import mrmathami.box.lang.ast.identifier.Identifier;
import mrmathami.box.lang.ast.identifier.LabelIdentifier;
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
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static mrmathami.box.lang.BoxLangParser.AccessibleExpressionContext;
import static mrmathami.box.lang.BoxLangParser.MemberDefinitionContext;
import static mrmathami.box.lang.BoxLangParser.ParameterExpressionContext;

public class Builder implements AutoCloseable {
	private static final @NotNull BigInteger SIGNER_I8 = new BigInteger("-100", 16);
	private static final @NotNull BigInteger SIGNER_I16 = new BigInteger("-10000", 16);
	private static final @NotNull BigInteger SIGNER_I32 = new BigInteger("-100000000", 16);
	private static final @NotNull BigInteger SIGNER_I64 = new BigInteger("-10000000000000000", 16);

	private final @Nullable Builder parentBuilder;

	private final @NotNull List<@NotNull MemberAccessExpression> members;

	private final @NotNull List<@NotNull Identifier> Identifiers = new ArrayList<>();
	private final Map<@NotNull String, @NotNull Definition> definitions = new HashMap<>();

	public Builder() {
		this.parentBuilder = null;
		this.members = new ArrayList<>();
	}

	private Builder(@NotNull Builder parentBuilder) {
		this.parentBuilder = parentBuilder;
		this.members = parentBuilder.members;
	}

//	@NotNull
//	private static <A, B, C> Pair<A, B> newPair(@Nullable C any) {
//		return Pair.mutableOf(null, null);
//	}

	//region resolver

	@Override
	public void close() throws InvalidASTException {
		// resolve all identifiers
		for (final Identifier identifier : Identifiers) {
			final String name = identifier.getName();
			final Definition definition = lookupDefinition(name);
			if (definition != null) {
				identifier.internalSetDefinition(definition);
			} else if (parentBuilder != null) {
				parentBuilder.Identifiers.add(identifier);
			} else {
				throw new InvalidASTException("Unresolved identifier: " + identifier.getName());
			}
		}
		if (parentBuilder == null) {
			for (final MemberAccessExpression memberAccessExpression : members) {
				final Type type = memberAccessExpression.getAccessibleExpression().resolveType();
				if (type instanceof TupleType) {
					final TupleType tupleType = (TupleType) type;
					final TupleDefinition definition = tupleType.getIdentifier().resolveDefinition();
					final MemberIdentifier identifier = memberAccessExpression.getIdentifier();
					final String name = identifier.getName();
					final Optional<MemberDefinition> optional = definition.getMembers()
							.stream()
							.filter(member -> name.equals(member.getIdentifier().getName()))
							.findFirst();
					if (optional.isEmpty()) throw new InvalidASTException("Unresolved identifier: " + name);
					identifier.internalSetDefinition(optional.get());
				} else {
					throw new InvalidASTException("Member access non-tuple type: " + type);
				}
			}
		}
	}

	private @Nullable Definition lookupDefinition(@NotNull String name) {
		final Definition definition = definitions.get(name);
		assert !(definition instanceof MemberDefinition);
		return definition != null
				? definition
				: parentBuilder != null
				? parentBuilder.lookupDefinition(name)
				: null;
	}

	private <E extends Definition> @NotNull E newDefinition(@NotNull String name, @NotNull E definition)
			throws InvalidASTException {
		assert !(definition instanceof MemberDefinition);
		if (definitions.put(name, definition) == null) {
			return definition;
		}
		throw new InvalidASTException("Redefined definition: " + name);
	}

	private <E extends Identifier> @NotNull E newIdentifier(@NotNull E identifier) {
		assert !(identifier instanceof MemberIdentifier);
		Identifiers.add(identifier);
		return identifier;
	}

	private @NotNull MemberAccessExpression newMember(@NotNull MemberAccessExpression member) {
		members.add(member);
		return member;
	}

	//endregion resolver

	//region parsed context processor

	private @NotNull CompilationUnit compilationUnit(@NotNull CompilationUnitContext context)
			throws InvalidASTException {
		final List<GlobalDefinition> definitions = new ArrayList<>();
		for (final var globalDefinitionContext : context.globalDefinition()) {
			definitions.add(globalDefinition(globalDefinitionContext));
		}
		return new CompilationUnit(definitions);
	}

	//region definition

	private @NotNull GlobalDefinition globalDefinition(@NotNull GlobalDefinitionContext context)
			throws InvalidASTException {
		final TupleDefinitionContext tupleDefinition = context.tupleDefinition();
		if (tupleDefinition != null) return tupleDefinition(tupleDefinition);
		final VariableDefinitionContext variableDefinition = context.variableDefinition();
		if (variableDefinition != null) return variableDefinition(variableDefinition);
		final FunctionDefinitionContext functionDefinition = context.functionDefinition();
		if (functionDefinition != null) return functionDefinition(functionDefinition);
		throw up();
	}

	private @NotNull VariableDefinition variableDefinition(@NotNull VariableDefinitionContext context)
			throws InvalidASTException {
		final Type type = type(context.type());
		final VariableIdentifier identifier = variableIdentifier(context.VariableIdentifier());
		final ExpressionContext expressionContext = context.expression();
		final Expression expression = expressionContext != null ? expression(expressionContext) : null;
		final VariableDefinition definition = new VariableDefinition(type, identifier, expression);
		return newDefinition(identifier.getName(), definition);
	}

	private @NotNull TupleDefinition tupleDefinition(@NotNull TupleDefinitionContext context)
			throws InvalidASTException {
		try (final Builder builder = new Builder(this)) {
			final TupleIdentifier identifier = tupleIdentifier(context.TupleIdentifier());
			final List<MemberDefinition> definitions = new ArrayList<>();
			for (final var memberDefinitionContext : context.memberDefinition()) {
				// these members are in the inner scope
				definitions.add(builder.memberDefinition(memberDefinitionContext));
			}
			final TupleDefinition definition = new TupleDefinition(identifier, definitions);
			return newDefinition(identifier.getName(), definition);
		}
	}

	private @NotNull MemberDefinition memberDefinition(@NotNull MemberDefinitionContext context)
			throws InvalidASTException {
		final Type type = type(context.type());
		final MemberIdentifier identifier = memberIdentifier(context.MemberIdentifier());
		// Note: this does not use newDefinition because member scope is tuple access only
		return new MemberDefinition(type, identifier);
	}

	private @NotNull FunctionDefinition functionDefinition(@NotNull FunctionDefinitionContext context)
			throws InvalidASTException {
		try (final Builder builder = new Builder(this)) {
			final Type type = type(context.type());
			final FunctionIdentifier identifier = functionIdentifier(context.FunctionIdentifier());
			final List<ParameterDefinition> definitions = new ArrayList<>();
			for (final var parameterDefinitionContext : context.parameterDefinition()) {
				// these parameters are in the inner scope
				definitions.add(builder.parameterDefinition(parameterDefinitionContext));
			}
			// this block is in the inner scope
			final BlockStatement statement = builder.blockStatement(context.blockStatement());
			final FunctionDefinition definition = new FunctionDefinition(type, identifier, definitions, statement);
			return newDefinition(identifier.getName(), definition);
		}
	}

	private @NotNull ParameterDefinition parameterDefinition(@NotNull ParameterDefinitionContext context)
			throws InvalidASTException {
		final Type type = type(context.type());
		final ParameterIdentifier identifier = parameterIdentifier(context.ParameterIdentifier());
		final ParameterDefinition definition = new ParameterDefinition(type, identifier);
		return newDefinition(identifier.getName(), definition);
	}

	private @NotNull LabelDefinition labelDefinition(@NotNull LabelDefinitionContext context)
			throws InvalidASTException {
		final LabelIdentifier identifier = labelIdentifier(context.LabelIdentifier());
		final LoopStatement loopStatement = loopStatement(context.loopStatement());
		final LabelDefinition definition = new LabelDefinition(identifier, loopStatement);
		return newDefinition(identifier.getName(), definition);
	}

	//endregion definition

	//region identifier

	private @NotNull VariableIdentifier variableIdentifier(@NotNull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.VariableIdentifier;
		return newIdentifier(new VariableIdentifier(identifier.getText()));
	}

	private @NotNull TupleIdentifier tupleIdentifier(@NotNull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.TupleIdentifier;
		return newIdentifier(new TupleIdentifier(identifier.getText()));
	}

	private @NotNull MemberIdentifier memberIdentifier(@NotNull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.MemberIdentifier;
		// Note: this does not use newIdentifier because member scope is tuple access only
		return new MemberIdentifier(identifier.getText());
	}

	private @NotNull FunctionIdentifier functionIdentifier(@NotNull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.FunctionIdentifier;
		return newIdentifier(new FunctionIdentifier(identifier.getText()));
	}

	private @NotNull ParameterIdentifier parameterIdentifier(@NotNull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.ParameterIdentifier;
		return newIdentifier(new ParameterIdentifier(identifier.getText()));
	}

	private @NotNull LabelIdentifier labelIdentifier(@NotNull TerminalNode identifier) {
		assert identifier.getSymbol().getType() == BoxLangLexer.LabelIdentifier;
		return newIdentifier(new LabelIdentifier(identifier.getText()));
	}

	//endregion identifier

	//region type

	private @NotNull Type type(@NotNull TypeContext context)
			throws InvalidASTException {
		final ArrayTypeContext arrayType = context.arrayType();
		if (arrayType != null) return arrayType(arrayType);
		final SimpleTypeContext simpleType = context.simpleType();
		if (simpleType != null) return simpleType(simpleType);
		final TupleTypeContext tupleType = context.tupleType();
		if (tupleType != null) return tupleType(tupleType);
		throw up();
	}

	//region ArrayType

	private @NotNull Type beforeArrayType(@NotNull BeforeArrayTypeContext context) {
		final SimpleTypeContext simpleType = context.simpleType();
		if (simpleType != null) return simpleType(simpleType);
		final TupleTypeContext tupleType = context.tupleType();
		if (tupleType != null) return tupleType(tupleType);
		throw up();
	}

	private long @NotNull [] arraySuffix(@NotNull ArraySuffixContext context)
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

	private @NotNull Type arrayType(@NotNull ArrayTypeContext context)
			throws InvalidASTException {
		Type currentType = beforeArrayType(context.beforeArrayType());
		for (final ArraySuffixContext arraySuffix : context.arraySuffix()) {
			currentType = new ArrayType(currentType, arraySuffix(arraySuffix));
		}
		return currentType;
	}

	//endregion ArrayType

	private @NotNull SimpleType simpleType(@NotNull SimpleTypeContext context) {
		assert context.getStart() == context.getStop();
		final String text = context.getText();
		for (final SimpleType value : SimpleType.values) {
			if (value.toString().equals(text)) return value;
		}
		throw up();
	}

	private @NotNull TupleType tupleType(@NotNull TupleTypeContext tupleType) {
		return new TupleType(tupleIdentifier(tupleType.TupleIdentifier()));
	}

	//endregion type

	//region expression

	//region misc

	private List<Expression> miscExpressionList(@NotNull ExpressionListContext context)
			throws InvalidASTException {
		final List<Expression> expressions = new ArrayList<>();
		for (final ExpressionContext expression : context.expression()) {
			expressions.add(expression(expression));
		}
		return expressions;
	}

	private <E extends Collection<@NotNull Expression>> @NotNull E miscParseExpressions(@NotNull E expressions,
			@NotNull List<? extends @NotNull ParserRuleContext> expressionContexts) throws InvalidASTException {
		for (final ParserRuleContext expressionContext : expressionContexts) {
			expressions.add(expression(expressionContext));
		}
		return expressions;
	}

	private void miscParseBinaryOperators(@NotNull Collection<NormalOperator> normalOperators,
			@NotNull List<? extends @NotNull ParserRuleContext> operatorContexts) {
		for (final ParserRuleContext expressionContext : operatorContexts) {
			normalOperators.add(binaryOperator(expressionContext));
		}
	}

	private @NotNull Expression miscCombinedBinaryExpression(
			@NotNull List<? extends ParserRuleContext> expressionContexts,
			@NotNull List<? extends ParserRuleContext> operatorContexts,
			@NotNull BiFunction<List<Expression>, NormalOperator, Expression> constructor)
			throws InvalidASTException {
		assert expressionContexts.size() >= 2 && expressionContexts.size() == operatorContexts.size() + 1;
		if (expressionContexts.size() == 2) {
			// this case is much more common
			final List<Expression> operands = List.of(
					expression(expressionContexts.get(0)),
					expression(expressionContexts.get(1))
			);
			final NormalOperator operator = binaryOperator(operatorContexts.get(0));
			return constructor.apply(operands, operator);
		} else {
			// this case is less common
			final Deque<Expression> expressions = miscParseExpressions(
					new ArrayDeque<>(expressionContexts.size()), expressionContexts);

			final Deque<NormalOperator> operators = new ArrayDeque<>(operatorContexts.size());
			miscParseBinaryOperators(operators, operatorContexts);

			while (true) {
				assert expressions.size() >= 2 && expressions.size() == operators.size() + 1;

				final List<Expression> operands = new ArrayList<>(expressions.size());
				operands.add(expressions.poll());
				operands.add(expressions.poll());

				final NormalOperator operator = operators.poll();
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
	}

	//endregion misc

	private @NotNull Expression expression(@NotNull ParserRuleContext context)
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

	private @NotNull Expression conditionalExpression(@NotNull ConditionalExpressionContext context)
			throws InvalidASTException {
		final Expression condition = expression(context.beforeConditionalExpression());
		final Expression trueExpression = expression(context.expression(0));
		final Expression falseExpression = expression(context.expression(1));
		return new ConditionalExpression(condition, trueExpression, falseExpression);
	}

	private @NotNull Expression orOrExpression(@NotNull OrOrExpressionContext context)
			throws InvalidASTException {
		final List<BeforeOrOrExpressionContext> expressionContexts = context.beforeOrOrExpression();
		final List<Expression> expressions = miscParseExpressions(
				new ArrayList<>(expressionContexts.size()), expressionContexts);
		return new SimpleBinaryExpression(expressions, NormalOperator.LOGIC_OR);
	}

	private @NotNull Expression xorXorExpression(@NotNull XorXorExpressionContext context)
			throws InvalidASTException {
		final List<BeforeXorXorExpressionContext> expressionContexts = context.beforeXorXorExpression();
		final List<Expression> expressions = miscParseExpressions(
				new ArrayList<>(expressionContexts.size()), expressionContexts);
		return new SimpleBinaryExpression(expressions, NormalOperator.LOGIC_XOR);
	}

	private @NotNull Expression andAndExpression(@NotNull AndAndExpressionContext context)
			throws InvalidASTException {
		final List<BeforeAndAndExpressionContext> expressionContexts = context.beforeAndAndExpression();
		final List<Expression> expressions = miscParseExpressions(
				new ArrayList<>(expressionContexts.size()), expressionContexts);
		return new SimpleBinaryExpression(expressions, NormalOperator.LOGIC_AND);
	}

	private @NotNull Expression notExpression(@NotNull NotExpressionContext context)
			throws InvalidASTException {
		final Expression expression = expression(context.beforeUnaryExpression());
		return new UnaryExpression(NormalOperator.LOGIC_NOT, expression);
	}

	private @NotNull Expression comparisonExpression(@NotNull ComparisonExpressionContext context)
			throws InvalidASTException {
		return miscCombinedBinaryExpression(context.beforeComparisonExpression(),
				context.comparisonOperator(), ComparisonExpression::new);
	}

	private @NotNull Expression addExpression(@NotNull AddExpressionContext context)
			throws InvalidASTException {
		return miscCombinedBinaryExpression(context.beforeAddExpression(),
				context.addOperator(), SimpleBinaryExpression::new);
	}

	private @NotNull Expression multiplyExpression(@NotNull MultiplyExpressionContext context)
			throws InvalidASTException {
		return miscCombinedBinaryExpression(context.beforeMultiplyExpression(),
				context.multiplyOperator(), SimpleBinaryExpression::new);
	}

	private @NotNull Expression signExpression(@NotNull SignExpressionContext context)
			throws InvalidASTException {
		final Expression expression = expression(context.beforeUnaryExpression());
		return new UnaryExpression(unaryOperator(context.signOperator()), expression);
	}

	private @NotNull Expression orExpression(@NotNull OrExpressionContext context)
			throws InvalidASTException {
		final List<BeforeOrExpressionContext> expressionContexts = context.beforeOrExpression();
		final List<Expression> expressions = miscParseExpressions(
				new ArrayList<>(expressionContexts.size()), expressionContexts);
		return new SimpleBinaryExpression(expressions, NormalOperator.BIT_OR);
	}

	private @NotNull Expression xorExpression(@NotNull XorExpressionContext context)
			throws InvalidASTException {
		final List<BeforeXorExpressionContext> expressionContexts = context.beforeXorExpression();
		final List<Expression> expressions = miscParseExpressions(
				new ArrayList<>(expressionContexts.size()), expressionContexts);
		return new SimpleBinaryExpression(expressions, NormalOperator.BIT_XOR);
	}

	private @NotNull Expression andExpression(@NotNull AndExpressionContext context)
			throws InvalidASTException {
		final List<BeforeAndExpressionContext> expressionContexts = context.beforeAndExpression();
		final List<Expression> expressions = miscParseExpressions(
				new ArrayList<>(expressionContexts.size()), expressionContexts);
		return new SimpleBinaryExpression(expressions, NormalOperator.BIT_AND);
	}

	private @NotNull Expression shiftExpression(@NotNull ShiftExpressionContext context)
			throws InvalidASTException {
		return miscCombinedBinaryExpression(context.beforeShiftExpression(),
				context.shiftOperator(), ShiftExpression::new);
	}

	private @NotNull Expression negativeExpression(@NotNull NegativeExpressionContext context)
			throws InvalidASTException {
		final Expression expression = expression(context.beforeUnaryExpression());
		return new UnaryExpression(NormalOperator.BIT_NEGATIVE, expression);
	}

	//region AccessExpression

	private @NotNull AccessExpression miscAccessSuffix(@NotNull AccessibleExpression accessibleExpression,
			@NotNull AccessSuffixContext context)
			throws InvalidASTException {
		final TerminalNode memberIdentifier = context.MemberIdentifier();
		if (memberIdentifier != null) {
			final MemberIdentifier identifier = memberIdentifier(memberIdentifier);
			return newMember(new MemberAccessExpression(accessibleExpression, identifier));
		}
		final List<ExpressionContext> expressionContexts = context.expressionList().expression();
		final List<Expression> expressions = miscParseExpressions(
				new ArrayList<>(expressionContexts.size()), expressionContexts);
		return new ArrayAccessExpression(accessibleExpression, expressions);
	}

	private @NotNull AccessExpression accessExpression(@NotNull AccessExpressionContext context)
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

	private @NotNull AccessibleExpression accessibleExpression(@NotNull AccessibleExpressionContext context)
			throws InvalidASTException {
		final VariableExpressionContext variableExpression = context.variableExpression();
		if (variableExpression != null) return variableExpression(variableExpression);
		final ParameterExpressionContext parameterExpression = context.parameterExpression();
		if (parameterExpression != null) return parameterExpression(parameterExpression);
		final FunctionCallExpressionContext functionCallExpression = context.functionCallExpression();
		if (functionCallExpression != null) return functionCallExpression(functionCallExpression);
		throw up();
	}

	private @NotNull AssignableExpression assignableExpression(@NotNull AssignableExpressionContext context)
			throws InvalidASTException {
		final AccessExpressionContext accessExpression = context.accessExpression();
		if (accessExpression != null) return accessExpression(accessExpression);
		final VariableExpressionContext variableExpression = context.variableExpression();
		if (variableExpression != null) return variableExpression(variableExpression);
		throw up();
	}

	//endregion AccessExpression

	private @NotNull VariableExpression variableExpression(@NotNull VariableExpressionContext context) {
		return new VariableExpression(variableIdentifier(context.VariableIdentifier()));
	}

	private @NotNull ParameterExpression parameterExpression(@NotNull ParameterExpressionContext context) {
		return new ParameterExpression(parameterIdentifier(context.ParameterIdentifier()));
	}

	private @NotNull FunctionCallExpression functionCallExpression(@NotNull FunctionCallExpressionContext context)
			throws InvalidASTException {
		final FunctionIdentifier identifier = functionIdentifier(context.FunctionIdentifier());
		final ExpressionListContext expressionList = context.expressionList();
		if (expressionList != null) {
			final List<ExpressionContext> expressionContexts = expressionList.expression();
			final List<Expression> expressions = miscParseExpressions(
					new ArrayList<>(expressionContexts.size()), expressionContexts);
			return new FunctionCallExpression(identifier, expressions);
		} else {
			return new FunctionCallExpression(identifier, List.of());
		}
	}

	private @NotNull Expression literalExpression(@NotNull LiteralExpressionContext context)
			throws InvalidASTException {
		assert context.getStart() == context.getStop();
		final TerminalNode typedNumberLiteral = context.TypedNumberLiteral();
		if (typedNumberLiteral != null) return typedNumber(typedNumberLiteral.getText());
		if (context.False() != null) return new LiteralExpression(SimpleType.BOOL, Keyword.FALSE);
		if (context.True() != null) return new LiteralExpression(SimpleType.BOOL, Keyword.TRUE);
		if (context.Null() != null) return new LiteralExpression(SimpleType.VOID, Keyword.NULL);
		throw up();
	}

	private @NotNull Expression castExpression(@NotNull CastExpressionContext context)
			throws InvalidASTException {
		final SimpleType type = simpleType(context.simpleType());
		final Expression expression = expression(context.expression());
		return new CastExpression(type, expression);
	}

	private @NotNull Expression parenthesesExpression(@NotNull ParenthesesExpressionContext context)
			throws InvalidASTException {
		return expression(context.expression());
	}

	private @NotNull Expression arrayCreationExpression(@NotNull ArrayCreationExpressionContext context)
			throws InvalidASTException {
		final Type innerType = type(context.type());
		final List<Expression> expressions = miscExpressionList(context.expressionList());
		final long[] dimensionSizes = new long[expressions.size()];
		Arrays.fill(dimensionSizes, -1);
		final ArrayType type = new ArrayType(innerType, dimensionSizes);
		return new ArrayCreationExpression(type, expressions);
	}

	private @NotNull Expression tupleCreationExpression(@NotNull TupleCreationExpressionContext context)
			throws InvalidASTException {
		final TupleType type = tupleType(context.tupleType());
		final List<Expression> expressions = miscExpressionList(context.expressionList());
		return new TupleCreationExpression(type, expressions);
	}

	//endregion expression

	//region statement

	private @NotNull Statement statement(@NotNull StatementContext context)
			throws InvalidASTException {
		final VariableDefinitionContext variableDefinition = context.variableDefinition();
		if (variableDefinition != null) return variableDefinition(variableDefinition);
		final LabelDefinitionContext labelDefinition = context.labelDefinition();
		if (labelDefinition != null) return labelDefinition(labelDefinition);
		final SingleStatementContext singleStatement = context.singleStatement();
		if (singleStatement != null) return singleStatement(singleStatement);
		throw up();
	}

	private @NotNull SingleStatement singleStatement(@NotNull SingleStatementContext context)
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
		final LoopStatementContext loopStatement = context.loopStatement();
		if (loopStatement != null) return loopStatement(loopStatement);
		final BlockStatementContext blockStatement = context.blockStatement();
		if (blockStatement != null) return blockStatement(blockStatement);
		final IfStatementContext ifStatement = context.ifStatement();
		if (ifStatement != null) return ifStatement(ifStatement);
		throw up();
	}

	private @NotNull FunctionCallStatement functionCallStatement(@NotNull FunctionCallStatementContext context)
			throws InvalidASTException {
		final FunctionCallExpression expression = functionCallExpression(context.functionCallExpression());
		return new FunctionCallStatement(expression);
	}

	private @NotNull ReturnStatement returnStatement(@NotNull ReturnStatementContext context)
			throws InvalidASTException {
		final ExpressionContext expressionContext = context.expression();
		final Expression expression = expressionContext != null ? expression(expressionContext) : null;
		return new ReturnStatement(expression);
	}

	private @NotNull BreakStatement breakStatement(@NotNull BreakStatementContext context) {
		final TerminalNode labelIdentifier = context.LabelIdentifier();
		return new BreakStatement(labelIdentifier != null ? labelIdentifier(labelIdentifier) : null);
	}

	private @NotNull ContinueStatement continueStatement(@NotNull ContinueStatementContext context) {
		final TerminalNode labelIdentifier = context.LabelIdentifier();
		return new ContinueStatement(labelIdentifier != null ? labelIdentifier(labelIdentifier) : null);
	}

	private @NotNull LoopStatement loopStatement(@NotNull LoopStatementContext context)
			throws InvalidASTException {
		final SingleStatementContext singleStatement = context.singleStatement();
		final SingleStatement statement = singleStatement != null ? singleStatement(singleStatement) : null;
		return new LoopStatement(statement);
	}

	private @NotNull AssignmentStatement assignmentStatement(@NotNull AssignmentStatementContext context)
			throws InvalidASTException {
		final AssignableExpression assignableExpression = assignableExpression(context.assignableExpression());
		final AssignmentOperator assignmentOperator = assignmentOperator(context.assignmentOperator());
		final Expression expression = expression(context.expression());
		return new AssignmentStatement(assignableExpression, assignmentOperator, expression);
	}

	private @NotNull BlockStatement blockStatement(@NotNull BlockStatementContext context)
			throws InvalidASTException {
		try (final Builder builder = new Builder(this)) {
			final List<Statement> statements = new ArrayList<>();
			for (final var statementContext : context.statement()) {
				// these statements are in the inner scope
				statements.add(builder.statement(statementContext));
			}
			return new BlockStatement(statements);
		}
	}

	private @NotNull IfStatement ifStatement(@NotNull IfStatementContext context)
			throws InvalidASTException {
		final List<Pair<Expression, SingleStatement>> pairs = new ArrayList<>();
		for (final var singleCaseContext : context.ifSingleCase()) {
			final Expression condition = expression(singleCaseContext.expression());
			final SingleStatementContext statementContext = singleCaseContext.singleStatement();
			final SingleStatement statement = statementContext != null
					? singleStatement(statementContext)
					: null;
			pairs.add(Tuples.pair(condition, statement));
		}
		final SingleStatementContext statementContext = context.singleStatement();
		final SingleStatement alternativeStatement = statementContext != null
				? singleStatement(statementContext)
				: null;
		return new IfStatement(pairs, alternativeStatement);
	}

	//endregion statement

	//region misc: number parser

	private static @NotNull BigInteger parseUntypedNumber(@NotNull String untypedNumber) {
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

	@Range(from = 0, to = Long.MAX_VALUE)
	private long untypedNumber(@NotNull String untypedNumber)
			throws InvalidASTException {
		assert untypedNumber.matches("[0-9]+|0x[0-9A-Fa-f]+|0t[0-7]+|0b[01]+");
		try {
			return parseUntypedNumber(untypedNumber).longValueExact();
		} catch (ArithmeticException exception) {
			throw new InvalidASTException("Invalid untyped number: " + untypedNumber, exception);
		}
	}

	private @NotNull LiteralExpression typedNumber(@NotNull String typedNumber)
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

	private @NotNull AssignmentOperator assignmentOperator(@NotNull ParserRuleContext context) {
		assert context.getStart() == context.getStop();
		final String text = context.getText();
		for (final AssignmentOperator value : AssignmentOperator.values) {
			if (value.toString().equals(text)) return value;
		}
		throw up();
	}

	private @NotNull NormalOperator unaryOperator(@NotNull ParserRuleContext context) {
		assert context.getStart() == context.getStop();
		final String text = context.getText();
		for (final NormalOperator value : NormalOperator.values) {
			if (value.isUnary() && value.toString().equals(text)) return value;
		}
		throw up();
	}

	private @NotNull NormalOperator binaryOperator(@NotNull ParserRuleContext context) {
		assert context.getStart() == context.getStop();
		final String text = context.getText();
		for (final NormalOperator value : NormalOperator.values) {
			if (value.isBinary() && value.toString().equals(text)) return value;
		}
		throw up();
	}

	//endregion misc: operator parser

	//endregion parsed context processor

	//region


	//endregion

	private static @NotNull RuntimeException up() {
		return new RuntimeException("This should not happen!");
	}

	public static void main(@NotNull String[] args) throws IOException, InvalidASTException {
		final String string = Files.readString(Path.of("./test/input.txt"));
		final BoxLangLexer lexer = new BoxLangLexer(CharStreams.fromString(string));

		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final BoxLangParser parser = new BoxLangParser(tokens);
//		final ParseTree tree = parser.compilationUnit();
//
//		System.out.println(tree);

		final CompilationUnit compilationUnit;
		try (final Builder builder = new Builder()) {
			compilationUnit = builder.compilationUnit(parser.compilationUnit());
		}
		System.out.println(compilationUnit);
	}
}
