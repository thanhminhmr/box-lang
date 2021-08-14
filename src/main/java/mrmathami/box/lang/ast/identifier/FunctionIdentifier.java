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

package mrmathami.box.lang.ast.identifier;

import mrmathami.annotations.Nonnull;
import mrmathami.annotations.Nullable;
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.definition.FunctionDefinition;

import java.util.Objects;

public final class FunctionIdentifier extends Identifier {
	@Nullable private FunctionDefinition definition;

	public FunctionIdentifier(@Nonnull String name) {
		super(name);
	}

	@Nonnull
	@Override
	public FunctionDefinition resolveDefinition() throws InvalidASTException {
		if (definition != null) return definition;
		throw new InvalidASTException("Invalid function identifier: definition not found!");
	}

	public void setDefinition(@Nullable FunctionDefinition definition) {
		this.definition = definition;
	}
}
