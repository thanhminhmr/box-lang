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

package mrmathami.box.lang.ast.expression;

import mrmathami.annotations.Nonnull;
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.expression.other.Operator;
import mrmathami.box.lang.ast.type.ArrayType;
import mrmathami.box.lang.ast.type.Type;

import java.util.List;

public final class ShiftExpression extends BinaryExpression {
	public ShiftExpression(@Nonnull List<Expression> operands, @Nonnull Operator operator) {
		super(operands, operator);
	}

	@Nonnull
	@Override
	public Type resolveType() throws InvalidASTException {
		final Type firstType = operands.get(0).resolveType();
		final Type innerType = firstType instanceof ArrayType
				? ((ArrayType) firstType).getMostInnerType()
				: firstType;
		if (!Type.isNumber(innerType)) {
			throw new InvalidASTException("Invalid shift expression: invalid type of first operand.");
		}
		final Type secondType = operands.get(1).resolveType();
		if (!Type.isNumber(secondType)) {
			throw new InvalidASTException("Invalid shift expression: invalid type of second operand.");
		}
		return firstType;

	}
}
