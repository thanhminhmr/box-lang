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

package mrmathami.box.lang.ast.definition;

import mrmathami.annotations.Nonnull;
import mrmathami.box.lang.ast.identifier.FunctionIdentifier;
import mrmathami.box.lang.ast.statement.BlockStatement;
import mrmathami.box.lang.ast.type.Type;

import java.util.Collections;
import java.util.List;

public final class FunctionDefinition implements Definition {
	@Nonnull private final Type returnType;
	@Nonnull private final FunctionIdentifier identifier;
	@Nonnull private final List<ParameterDefinition> parameters;
	@Nonnull private final BlockStatement body;

	public FunctionDefinition(@Nonnull Type returnType, @Nonnull FunctionIdentifier identifier,
			@Nonnull List<ParameterDefinition> parameters, @Nonnull BlockStatement body) {
		this.returnType = returnType;
		this.identifier = identifier;
		this.parameters = parameters;
		this.body = body;
	}

	@Nonnull
	public Type getReturnType() {
		return returnType;
	}

	@Nonnull
	@Override
	public FunctionIdentifier getIdentifier() {
		return identifier;
	}

	@Nonnull
	public List<ParameterDefinition> getParameters() {
		return Collections.unmodifiableList(parameters);
	}

	@Nonnull
	public BlockStatement getBody() {
		return body;
	}
}
