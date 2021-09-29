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
import mrmathami.box.lang.ast.type.TupleType;
import mrmathami.box.lang.ast.type.Type;
import mrmathami.box.lang.visitor.Visitor;
import mrmathami.box.lang.visitor.VisitorException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TupleCreationExpression implements Expression {
	private final @NotNull TupleType type;
	private final @NotNull List<@NotNull Expression> memberExpressions;

	public TupleCreationExpression(@NotNull TupleType type, @NotNull List<@NotNull Expression> memberExpressions) {
		this.type = type;
		this.memberExpressions = memberExpressions;
	}

	public @NotNull TupleType getType() {
		return type;
	}

	public @NotNull List<@NotNull Expression> getMemberExpressions() {
		return memberExpressions;
	}

	@Override
	public @NotNull Type resolveType() throws InvalidASTException {
		// todo
		return type;
	}
	
	@Override
	public boolean visit(@NotNull Visitor visitor) throws VisitorException {
		final int enter = visitor.enter(this);
		if (enter != 0) return enter < 0;

		if (type.visit(visitor)) return true;
		for (final Expression memberExpression : memberExpressions) {
			if (memberExpression.visit(visitor)) return true;
		}

		return visitor.leave(this) < 0;
	}
}
