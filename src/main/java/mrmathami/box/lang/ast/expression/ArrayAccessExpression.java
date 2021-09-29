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
import mrmathami.box.lang.ast.type.ArrayType;
import mrmathami.box.lang.ast.type.Type;
import mrmathami.box.lang.visitor.Visitor;
import mrmathami.box.lang.visitor.VisitorException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ArrayAccessExpression implements AccessExpression {
	private final @NotNull AccessibleExpression accessibleExpression;
	private final @NotNull List<@NotNull Expression> expressions;

	public ArrayAccessExpression(@NotNull AccessibleExpression accessibleExpression,
			@NotNull List<@NotNull Expression> expressions) {
		this.accessibleExpression = accessibleExpression;
		this.expressions = expressions;
	}

	@Override
	public @NotNull Type resolveType() throws InvalidASTException {
		final Type type = accessibleExpression.resolveType();
		if (!(type instanceof ArrayType)) {
			throw new InvalidASTException("Invalid array access expression: array access on non array type!");
		}
		final ArrayType arrayType = (ArrayType) type;
		if (expressions.size() != arrayType.getNumOfDimensions()) {
			throw new InvalidASTException("Invalid array access expression: wrong number of dimensions!");
		}
		return arrayType.getInnerType();
	}

	public @NotNull AccessibleExpression getAccessibleExpression() {
		return accessibleExpression;
	}

	public @NotNull List<@NotNull Expression> getExpressions() {
		return expressions;
	}

	@Override
	public boolean visit(@NotNull Visitor visitor) throws VisitorException {
		final int enter = visitor.enter(this);
		if (enter != 0) return enter < 0;

		if (accessibleExpression.visit(visitor)) return true;
		for (final Expression expression : expressions) {
			if (expression.visit(visitor)) return true;
		}

		return visitor.leave(this) < 0;
	}
}
