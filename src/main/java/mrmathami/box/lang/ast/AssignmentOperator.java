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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum AssignmentOperator {
	ASSIGN("=", null),

	/**
	 * Shallow copy an array. Similar to Array::clone in Java.
	 */
	SHALLOW_CLONE(":=", null),

	ARITHMETIC_MULTIPLY("*=", NormalOperator.ARITHMETIC_MULTIPLY),
	ARITHMETIC_DIVIDE("/=", NormalOperator.ARITHMETIC_DIVIDE),
	ARITHMETIC_MODULUS("%=", NormalOperator.ARITHMETIC_MODULUS),
	ARITHMETIC_ADD("+=", NormalOperator.ARITHMETIC_ADD),
	ARITHMETIC_SUBTRACT("-=", NormalOperator.ARITHMETIC_SUBTRACT),
	BIT_SHIFT_LEFT("<<=", NormalOperator.BIT_SHIFT_LEFT),
	BIT_SHIFT_RIGHT(">>=", NormalOperator.BIT_SHIFT_RIGHT),

	BIT_AND("&=", NormalOperator.BIT_AND),
	BIT_XOR("^=", NormalOperator.BIT_XOR),
	BIT_OR("|=", NormalOperator.BIT_OR),

	LOGIC_AND("&&=", NormalOperator.LOGIC_AND),
	LOGIC_XOR("^^=", NormalOperator.LOGIC_OR),
	LOGIC_OR("||=", NormalOperator.LOGIC_XOR);

	public static final @NotNull List<@NotNull AssignmentOperator> values = List.of(values());

	private final @NotNull String string;
	private final @Nullable NormalOperator operator;

	AssignmentOperator(@NotNull String string, @Nullable NormalOperator operator) {
		this.string = string;
		this.operator = operator;
	}

	@Override
	public final @NotNull String toString() {
		return string;
	}

	public final boolean isAugmented() {
		return operator != null;
	}

	public final @Nullable NormalOperator getOperator() {
		return operator;
	}
}
