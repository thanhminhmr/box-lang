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

package mrmathami.box.lang.ast.expression.access;

import mrmathami.annotations.Nonnull;
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.definition.FunctionDefinition;
import mrmathami.box.lang.ast.expression.Expression;
import mrmathami.box.lang.ast.identifier.FunctionIdentifier;
import mrmathami.box.lang.ast.type.Type;

import java.util.List;

public final class FunctionCallExpression implements AccessibleExpression {
	@Nonnull private final FunctionIdentifier identifier;
	@Nonnull private final List<Expression> arguments;

	public FunctionCallExpression(@Nonnull FunctionIdentifier identifier, @Nonnull List<Expression> arguments) {
		this.identifier = identifier;
		this.arguments = arguments;
	}

	@Nonnull
	@Override
	public Type resolveType() throws InvalidASTException {
		final FunctionDefinition definition = identifier.resolveDefinition();
		if (definition.getParameters().size() != arguments.size()) {
			throw new InvalidASTException("Invalid function call expression: wrong number of parameters!");
		}
		return definition.getReturnType();
	}

	@Nonnull
	public FunctionIdentifier getIdentifier() {
		return identifier;
	}

	@Nonnull
	public List<Expression> getArguments() {
		return arguments;
	}
}
