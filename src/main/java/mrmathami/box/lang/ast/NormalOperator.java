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

package mrmathami.box.lang.ast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum NormalOperator {
	SIGN_POSITIVE("+", 0b010001), // unary, arithmetic
	SIGN_NEGATIVE("-", 0b010001), // unary, arithmetic
	BIT_NEGATIVE("~", 0b010010), // unary, bitwise
	LOGIC_NOT("!", 0b010100), // unary, logic
	ARITHMETIC_MULTIPLY("*", 0b100001), // binary, arithmetic
	ARITHMETIC_DIVIDE("/", 0b100001), // binary, arithmetic
	ARITHMETIC_MODULUS("%", 0b100001), // binary, arithmetic
	ARITHMETIC_ADD("+", 0b100001), // binary, arithmetic
	ARITHMETIC_SUBTRACT("-", 0b100001), // binary, arithmetic
	BIT_SHIFT_LEFT("<<", 0b100010), // binary, bitwise
	BIT_SHIFT_RIGHT(">>", 0b100010), // binary, bitwise
	BIT_AND("&", 0b100010), // binary, bitwise
	BIT_XOR("^", 0b100010), // binary, bitwise
	BIT_OR("|", 0b100010), // binary, bitwise
	COMPARISON_LESS("<", 0b100100), // binary, comparison
	COMPARISON_GREATER(">", 0b100100), // binary, comparison
	COMPARISON_LESS_EQUAL("<=", 0b100100), // binary, comparison
	COMPARISON_GREATER_EQUAL(">=", 0b100100), // binary, comparison
	COMPARISON_EQUAL("==", 0b100100), // binary, comparison
	COMPARISON_NOT_EQUAL("!=", 0b100100), // binary, comparison
	COMPARISON_IDENTICAL("===", 0b100100), // binary, comparison
	COMPARISON_NOT_IDENTICAL("!==", 0b100100), // binary, comparison
	LOGIC_AND("&&", 0b101000), // binary, logical
	LOGIC_XOR("^^", 0b101000), // binary, logical
	LOGIC_OR("||", 0b101000); // binary, logical

	public static final int MASK_ARITHMETIC = 0b000001;
	public static final int MASK_BITWISE = 0b000010;
	public static final int MASK_COMPARISON = 0b000100;
	public static final int MASK_LOGIC = 0b001000;
	public static final int MASK_UNARY = 0b010000;
	public static final int MASK_BINARY = 0b100000;

	public static final @NotNull List<@NotNull NormalOperator> values = List.of(values());

	private final @NotNull String string;
	private final int flags;

	NormalOperator(@NotNull String string, int flags) {
		this.string = string;
		this.flags = flags;
	}

	@Override
	public final @NotNull String toString() {
		return string;
	}

	public final boolean isArithmetic() {
		return (flags & MASK_ARITHMETIC) != 0;
	}

	public final boolean isBitwise() {
		return (flags & MASK_BITWISE) != 0;
	}

	public final boolean isComparison() {
		return (flags & MASK_COMPARISON) != 0;
	}

	public final boolean isLogic() {
		return (flags & MASK_LOGIC) != 0;
	}

	public final boolean isUnary() {
		return (flags & MASK_UNARY) != 0;
	}

	public final boolean isBinary() {
		return (flags & MASK_BINARY) != 0;
	}

	public final boolean isMatchMask(int mask) {
		return (flags & mask) == mask;
	}
}
