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
import mrmathami.box.lang.ast.type.Type;

public final class ConditionalExpression implements Expression {
	@Nonnull private final Expression condition;
	@Nonnull private final Expression trueExpression;
	@Nonnull private final Expression falseExpression;

	public ConditionalExpression(@Nonnull Expression condition, @Nonnull Expression trueExpression,
			@Nonnull Expression falseExpression) {
		this.condition = condition;
		this.trueExpression = trueExpression;
		this.falseExpression = falseExpression;
	}

	@Nonnull
	@Override
	public Type resolveType() throws InvalidASTException {
		final Type trueType = trueExpression.resolveType();
		final Type falseType = falseExpression.resolveType();
		if (trueType.equals(falseType)) return trueType;
		throw new InvalidASTException("Invalid conditional expression: result operands have different types!");
	}

	@Nonnull
	public Expression getCondition() {
		return condition;
	}

	@Nonnull
	public Expression getTrueExpression() {
		return trueExpression;
	}

	@Nonnull
	public Expression getFalseExpression() {
		return falseExpression;
	}
}
