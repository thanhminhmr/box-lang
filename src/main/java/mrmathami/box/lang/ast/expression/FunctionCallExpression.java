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
import mrmathami.box.lang.ast.definition.FunctionDefinition;
import mrmathami.box.lang.ast.identifier.FunctionIdentifier;
import mrmathami.box.lang.ast.type.Type;
import mrmathami.box.lang.visitor.Visitor;
import mrmathami.box.lang.visitor.VisitorException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class FunctionCallExpression implements AccessibleExpression {
	private final @NotNull FunctionIdentifier identifier;
	private final @NotNull List<@NotNull Expression> arguments;

	public FunctionCallExpression(@NotNull FunctionIdentifier identifier, @NotNull List<@NotNull Expression> arguments) {
		this.identifier = identifier;
		this.arguments = arguments;
	}

	@Override
	public @NotNull Type resolveType() throws InvalidASTException {
		final FunctionDefinition definition = identifier.resolveDefinition();
		if (definition.getParameters().size() != arguments.size()) {
			throw new InvalidASTException("Invalid function call expression: wrong number of parameters!");
		}
		return definition.getReturnType();
	}

	public @NotNull FunctionIdentifier getIdentifier() {
		return identifier;
	}

	public @NotNull List<@NotNull Expression> getArguments() {
		return arguments;
	}

	@Override
	public boolean visit(@NotNull Visitor visitor) throws VisitorException {
		final int enter = visitor.enter(this);
		if (enter != 0) return enter < 0;

		if (identifier.visit(visitor)) return true;
		for (final Expression argument : arguments) {
			if (argument.visit(visitor)) return true;
		}

		return visitor.leave(this) < 0;
	}
}
