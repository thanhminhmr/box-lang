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

import mrmathami.box.lang.ast.identifier.FunctionIdentifier;
import mrmathami.box.lang.ast.statement.BlockStatement;
import mrmathami.box.lang.ast.type.Type;
import mrmathami.box.lang.visitor.Visitor;
import mrmathami.box.lang.visitor.VisitorException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class FunctionDefinition implements GlobalDefinition {
	private final @NotNull Type returnType;
	private final @NotNull FunctionIdentifier identifier;
	private final @NotNull List<@NotNull ParameterDefinition> parameters;
	private final @NotNull BlockStatement body;

	public FunctionDefinition(@NotNull Type returnType, @NotNull FunctionIdentifier identifier,
			@NotNull List<@NotNull ParameterDefinition> parameters, @NotNull BlockStatement body) {
		this.returnType = returnType;
		this.identifier = identifier;
		this.parameters = parameters;
		this.body = body;
	}

	public @NotNull Type getReturnType() {
		return returnType;
	}

	@Override
	public @NotNull FunctionIdentifier getIdentifier() {
		return identifier;
	}

	public @NotNull List<@NotNull ParameterDefinition> getParameters() {
		return Collections.unmodifiableList(parameters);
	}

	public @NotNull BlockStatement getBody() {
		return body;
	}

	@Override
	public boolean visit(@NotNull Visitor visitor) throws VisitorException {
		final int enter = visitor.enter(this);
		if (enter != 0) return enter < 0;

		if (returnType.visit(visitor)) return true;
		if (identifier.visit(visitor)) return true;
		for (final ParameterDefinition parameter : parameters) {
			if (parameter.visit(visitor)) return true;
		}
		if (body.visit(visitor)) return true;

		return visitor.leave(this) < 0;
	}
}
