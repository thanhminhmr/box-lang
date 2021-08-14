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

parser grammar BoxLangParser;

options {
	tokenVocab = BoxLangLexer;
}

//region Misc
expressionList: expression (',' expression)*;

//endregion Misc

/* Main components */

//region type

simpleType:
	Void
	| Bool
	| NumberU8
	| NumberU16
	| NumberU32
	| NumberU64
	| NumberI8
	| NumberI16
	| NumberI32
	| NumberI64;

tupleType: TupleIdentifier;

//region array type

beforeArrayType: simpleType | tupleType;

arraySizeLiteral: UntypedNumberLiteral?;

arraySuffix: '[' arraySizeLiteral (',' arraySizeLiteral)* ']';

arrayType: beforeArrayType arraySuffix+;

//endregion array type

type: arrayType | simpleType | tupleType;

//endregion type

//region expression

//region primary expression

variableExpression: VariableIdentifier;

parameterExpression: ParameterIdentifier;

functionCallExpression:
	FunctionIdentifier '(' expressionList? ')';

//region access expression

accessSuffix: MemberIdentifier | '[' expressionList ']';

accessibleExpression:
	variableExpression
	| parameterExpression
	| functionCallExpression;

accessExpression: accessibleExpression accessSuffix+;

assignableExpression: accessExpression | variableExpression;

//endregion access expression

literalExpression: TypedNumberLiteral | True | False | Null;

castExpression: simpleType '(' expression ')';

parenthesesExpression: '(' expression ')';

//endregion primary expression

beforeUnaryExpression:
	accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

//region arithmetic expression

signOperator: '+' | '-';

signExpression: signOperator beforeUnaryExpression;

//-----

beforeMultiplyExpression:
	signExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

multiplyOperator: '*' | '/' | '%';

multiplyExpression:
	beforeMultiplyExpression (
		multiplyOperator beforeMultiplyExpression
	)+;

//-----

beforeAddExpression:
	multiplyExpression
	| signOperator
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

addOperator: '+' | '-';

addExpression:
	beforeAddExpression (addOperator beforeAddExpression)+;

//endregion arithmetic expression

//region bit manipulation expression

negativeExpression: '~' beforeUnaryExpression;

//-----

beforeShiftExpression:
	negativeExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

shiftOperator: '<<' | '>>';

shiftExpression:
	beforeShiftExpression (shiftOperator beforeShiftExpression)+;

//-----

beforeAndExpression:
	shiftExpression
	| negativeExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

andExpression: beforeAndExpression ('&' beforeAndExpression)+;

//-----

beforeXorExpression:
	andExpression
	| shiftExpression
	| negativeExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

xorExpression: beforeXorExpression ('^' beforeXorExpression)+;

//-----

beforeOrExpression:
	xorExpression
	| andExpression
	| shiftExpression
	| negativeExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

orExpression: beforeOrExpression ('|' beforeOrExpression)+;

//endregion bit manipulation expression

//region comparison expression

beforeComparisonExpression:
	addExpression
	| multiplyExpression
	| signOperator
	| orExpression
	| xorExpression
	| andExpression
	| shiftExpression
	| negativeExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

comparisonOperator:
	'<'
	| '>'
	| '<='
	| '>='
	| '=='
	| '!='
	| '==='
	| '!==';

comparisonExpression:
	beforeComparisonExpression (
		comparisonOperator beforeComparisonExpression
	)+;

//endregion comparison expression

//region logic expression

notExpression: '!' beforeUnaryExpression;

//-----

beforeAndAndExpression:
	notExpression
	| comparisonExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

andAndExpression:
	beforeAndAndExpression ('&&' beforeAndAndExpression)+;

//-----

beforeXorXorExpression:
	andAndExpression
	| notExpression
	| comparisonExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

xorXorExpression:
	beforeXorXorExpression ('^^' beforeXorXorExpression)+;

//-----

beforeOrOrExpression:
	xorXorExpression
	| andAndExpression
	| notExpression
	| comparisonExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

orOrExpression:
	beforeOrOrExpression ('||' beforeOrOrExpression)+;

//endregion logic expression

//region conditional expression

beforeConditionalExpression:
	orOrExpression
	| xorXorExpression
	| andAndExpression
	| notExpression
	| comparisonExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression;

conditionalExpression:
	beforeConditionalExpression '?' expression ':' expression;

//endregion conditional expression

arrayCreationExpression: type ':' '[' expressionList ']';

tupleCreationExpression: tupleType '(' expressionList ')';

expression:
	conditionalExpression
	| orOrExpression
	| xorXorExpression
	| andAndExpression
	| notExpression
	| comparisonExpression
	| addExpression
	| multiplyExpression
	| signOperator
	| orExpression
	| xorExpression
	| andExpression
	| shiftExpression
	| negativeExpression
	| accessExpression
	| variableExpression
	| parameterExpression
	| functionCallExpression
	| literalExpression
	| castExpression
	| parenthesesExpression
	| arrayCreationExpression
	| tupleCreationExpression;

//endregion expression

//region definition

memberDefinition: type MemberIdentifier;

tupleDefinition:
	TupleIdentifier '(' (
		memberDefinition (',' memberDefinition)*
	)? ')';

//region variable definition

variableInitializer: VariableIdentifier ('=' expression)?;

variablesDefinition:
	type variableInitializer (',' variableInitializer)*;

//endregion variable definition

//region function definition

parameterDefinition: type ParameterIdentifier;

functionDefinition:
	type FunctionIdentifier '(' (
		parameterDefinition (',' parameterDefinition)*
	)? ')' blockStatement;

//endregion function definition

definition:
	tupleDefinition ';'
	| variablesDefinition ';'
	| functionDefinition;

//endregion definition

//region statement

functionCallStatement: functionCallExpression ';';

returnStatement: Return expression? ';';

breakStatement: Break UntypedNumberLiteral? ';';

continueStatement: Continue UntypedNumberLiteral? ';';

//region assignment statement

assignmentOperator:
	'='
	| ':='
	| '*='
	| '/='
	| '%='
	| '+='
	| '-='
	| '<<='
	| '>>='
	| '&='
	| '^='
	| '|='
	| '&&='
	| '^^='
	| '||=';

assignmentStatement:
	assignableExpression assignmentOperator expression ';';

//endregion assignment statement

blockStatement: '{' (';' | statement)* '}';

//region if statement

ifSingleCase: If '(' expression ')' (';' | singleStatement);

ifStatement:
	ifSingleCase (Else ifSingleCase)* (
		Else (';' | singleStatement)
	)?;

//endregion if statement

loopStatement: Loop (';' | singleStatement);

singleStatement:
	functionCallStatement
	| returnStatement
	| breakStatement
	| continueStatement
	| assignmentStatement
	| blockStatement
	| ifStatement
	| loopStatement;

statement: singleStatement | definition;

//endregion statement

//region compilation unit

compilationUnit: (';' | definition)* EOF;

//endregion compilation unit
