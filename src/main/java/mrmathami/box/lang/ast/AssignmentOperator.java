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

import mrmathami.annotations.Nonnull;
import mrmathami.annotations.Nullable;

import java.util.List;

public enum AssignmentOperator {
	ASSIGN("=", null),
	SHALLOW_CLONE(":=", null),
	ARITHMETIC_MULTIPLY("*=", Operator.ARITHMETIC_MULTIPLY),
	ARITHMETIC_DIVIDE("/=", Operator.ARITHMETIC_DIVIDE),
	ARITHMETIC_MODULUS("%=", Operator.ARITHMETIC_MODULUS),
	ARITHMETIC_ADD("+=", Operator.ARITHMETIC_ADD),
	ARITHMETIC_SUBTRACT("-=", Operator.ARITHMETIC_SUBTRACT),
	BIT_SHIFT_LEFT("<<=", Operator.BIT_SHIFT_LEFT),
	BIT_SHIFT_RIGHT(">>=", Operator.BIT_SHIFT_RIGHT),
	BIT_AND("&=", Operator.BIT_AND),
	BIT_XOR("^=", Operator.BIT_XOR),
	BIT_OR("|=", Operator.BIT_OR),
	LOGIC_AND("&&=", Operator.LOGIC_AND),
	LOGIC_XOR("^^=", Operator.LOGIC_OR),
	LOGIC_OR("||=", Operator.LOGIC_XOR);

	@Nonnull public static final List<AssignmentOperator> values = List.of(values());

	@Nonnull private final String string;
	@Nullable private final Operator operator;

	AssignmentOperator(@Nonnull String string, @Nullable Operator operator) {
		this.string = string;
		this.operator = operator;
	}

	@Nonnull
	@Override
	public final String toString() {
		return string;
	}

	public final boolean isAugmented() {
		return operator != null;
	}

	@Nullable
	public final Operator getOperator() {
		return operator;
	}
}
