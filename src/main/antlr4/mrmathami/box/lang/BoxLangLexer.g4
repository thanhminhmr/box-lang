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

lexer grammar BoxLangLexer;

Void: 'void';
Bool: 'bool';

NumberU8: 'u8';
NumberU16: 'u16';
NumberU32: 'u32';
NumberU64: 'u64';
NumberI8: 'i8';
NumberI16: 'i16';
NumberI32: 'i32';
NumberI64: 'i64';

True: 'true';
False: 'false';
Null: 'null';

Return: 'return';
Break: 'break';
Continue: 'continue';
If: 'if';
Else: 'else';
Loop: 'loop';

LeftParen: '(';
RightParen: ')';
LeftBracket: '[';
RightBracket: ']';
LeftBrace: '{';
RightBrace: '}';

Semi: ';';
Comma: ',';

Not: '!';
Tilde: '~';

Plus: '+';
Minus: '-';

Star: '*';
Div: '/';
Mod: '%';

LeftShift: '<<';
RightShift: '>>';
And: '&';
Or: '|';
Xor: '^';

Less: '<';
Greater: '>';
LessOrEqual: '<=';
GreaterOrEqual: '>=';

Equal: '==';
NotEqual: '!=';

Identical: '===';
NotIdentical: '!==';

AndAnd: '&&';
OrOr: '||';
XorXor: '^^';

Question: '?';
Colon: ':';

Assign: '=';
CloneAssign: ':=';
StarAssign: '*=';
DivAssign: '/=';
ModAssign: '%=';
PlusAssign: '+=';
MinusAssign: '-=';
LeftShiftAssign: '<<=';
RightShiftAssign: '>>=';
AndAssign: '&=';
XorAssign: '^=';
OrAssign: '|=';
AndAndAssign: '&&=';
XorXorAssign: '^^=';
OrOrAssign: '||=';

//endregion Brackets and operators

//region Mumber literals

fragment DecimalLiteral: [0-9]+;
fragment OctalLiteral: '0t' [0-7]+;
fragment HexadecimalLiteral: '0x' [0-9A-Fa-f]+;
fragment BinaryLiteral: '0b' [01]+;
UntypedNumberLiteral:
	DecimalLiteral
	| OctalLiteral
	| HexadecimalLiteral
	| BinaryLiteral;
TypedNumberLiteral: (
		DecimalLiteral
		| OctalLiteral
		| HexadecimalLiteral
		| BinaryLiteral
	) [bsilBSIL];

//endregion Number literals

//region Identifiers

TupleIdentifier: [A-Z] [0-9A-Z_a-z]*;
MemberIdentifier: '.' [a-zA-Z] [0-9A-Z_a-z]*;
FunctionIdentifier: '_' [0-9A-Z_a-z]*;
ParameterIdentifier: '$' [0-9A-Z_a-z]*;
VariableIdentifier: [a-z] [0-9A-Z_a-z]*;
LabelIdentifier: '@' [0-9A-Z_a-z]*;

//endregion Identifiers

// any ascii control character or space character
Whitespace: [\t\n\r ]+ -> channel(HIDDEN);
BlockComment: '/*' [^*]* '*'+ ([^/] [^*]* '*'+)* '/' -> channel(HIDDEN);
LineComment: '//' ~[\r\n]* -> channel(HIDDEN);
