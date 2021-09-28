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

import mrmathami.annotations.Internal;
import mrmathami.annotations.Nonnull;
import mrmathami.annotations.Nullable;
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.definition.Definition;
import mrmathami.box.lang.ast.definition.FunctionDefinition;
import mrmathami.box.lang.ast.definition.MemberDefinition;
import mrmathami.box.lang.ast.definition.ParameterDefinition;
import mrmathami.box.lang.ast.definition.TupleDefinition;
import mrmathami.box.lang.ast.definition.VariableDefinition;

import java.util.Map;

public abstract class Identifier {
	@Nonnull private static final Map<Class<? extends Identifier>, Class<? extends Definition>> CLASS_MAP = Map.of(
			VariableIdentifier.class, VariableDefinition.class,
			ParameterIdentifier.class, ParameterDefinition.class,
			FunctionIdentifier.class, FunctionDefinition.class,
			MemberIdentifier.class, MemberDefinition.class,
			TupleIdentifier.class, TupleDefinition.class
	);

	@Nonnull private final String name;
	@Nullable private Definition definition;

	Identifier(@Nonnull String name) {
		this.name = name;
	}

	@Nonnull
	public final String getName() {
		return name;
	}

	@Nonnull
	public Definition resolveDefinition() throws InvalidASTException {
		if (definition != null) return definition;
		throw new InvalidASTException("Definition not found for identifier " + name);
	}

	@Internal
	public final void setDefinition(@Nonnull Definition definition) throws InvalidASTException {
		if (CLASS_MAP.get(getClass()).isInstance(definition)) {
			this.definition = definition;
		} else {
			throw new InvalidASTException("Invalid definition for identifier!");
		}
	}
}
