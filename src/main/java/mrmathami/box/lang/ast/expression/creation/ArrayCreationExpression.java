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

package mrmathami.box.lang.ast.expression.creation;

import mrmathami.annotations.Nonnull;
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.expression.Expression;
import mrmathami.box.lang.ast.type.ArrayType;
import mrmathami.box.lang.ast.type.Type;

import java.util.List;

public final class ArrayCreationExpression implements Expression {
	@Nonnull private final ArrayType type;
	@Nonnull private final List<Expression> dimensionExpressions;

	public ArrayCreationExpression(@Nonnull ArrayType type, @Nonnull List<Expression> dimensionExpressions) {
		this.type = type;
		this.dimensionExpressions = dimensionExpressions;
	}

	@Nonnull
	public ArrayType getType() {
		return type;
	}

	@Nonnull
	public List<Expression> getDimensionExpressions() {
		return dimensionExpressions;
	}

	@Nonnull
	@Override
	public ArrayType resolveType() throws InvalidASTException {
		// TODO
		return type;
	}
}
