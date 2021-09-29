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

import mrmathami.box.lang.ast.type.SimpleType;
import mrmathami.box.lang.ast.type.Type;
import mrmathami.box.lang.visitor.Visitor;
import mrmathami.box.lang.visitor.VisitorException;
import org.jetbrains.annotations.NotNull;

public final class CastExpression implements Expression {
	private final @NotNull SimpleType type;
	private final @NotNull Expression expression;

	public CastExpression(@NotNull SimpleType type, @NotNull Expression expression) {
		this.type = type;
		this.expression = expression;
	}

	public @NotNull SimpleType getType() {
		return type;
	}

	public @NotNull Expression getExpression() {
		return expression;
	}

	@Override
	public @NotNull Type resolveType() {
		// todo
		return type;
	}

	@Override
	public boolean visit(@NotNull Visitor visitor) throws VisitorException {
		final int enter = visitor.enter(this);
		if (enter != 0) return enter < 0;

		if (type.visit(visitor)) return true;
		if (expression.visit(visitor)) return true;

		return visitor.leave(this) < 0;
	}
}
