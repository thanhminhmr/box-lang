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

import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.NormalOperator;
import mrmathami.box.lang.ast.type.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SimpleBinaryExpression extends BinaryExpression {
	public SimpleBinaryExpression(@NotNull List<@NotNull Expression> operands, @NotNull NormalOperator operator) {
		super(operands, operator);
		assert operands.size() >= 2;
	}

	@Override
	public @NotNull Type resolveType() throws InvalidASTException {
		return sameTypeCheck(operands);
	}
}
