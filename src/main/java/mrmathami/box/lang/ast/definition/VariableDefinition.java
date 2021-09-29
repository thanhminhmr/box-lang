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

import mrmathami.box.lang.ast.expression.Expression;
import mrmathami.box.lang.ast.identifier.VariableIdentifier;
import mrmathami.box.lang.ast.statement.Statement;
import mrmathami.box.lang.ast.type.Type;
import mrmathami.box.lang.visitor.Visitor;
import mrmathami.box.lang.visitor.VisitorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VariableDefinition implements GlobalDefinition, Statement {
	private final @NotNull Type type;
	private final @NotNull VariableIdentifier identifier;
	private final @Nullable Expression initialValue;

	public VariableDefinition(@NotNull Type type, @NotNull VariableIdentifier identifier,
			@Nullable Expression initialValue) {
		this.type = type;
		this.identifier = identifier;
		this.initialValue = initialValue;
	}

	public @NotNull Type getType() {
		return type;
	}

	@Override
	public @NotNull VariableIdentifier getIdentifier() {
		return identifier;
	}

	public @Nullable Expression getInitialValue() {
		return initialValue;
	}

	@Override
	public boolean visit(@NotNull Visitor visitor) throws VisitorException {
		final int enter = visitor.enter(this);
		if (enter != 0) return enter < 0;

		if (type.visit(visitor)) return true;
		if (identifier.visit(visitor)) return true;
		if (initialValue != null && initialValue.visit(visitor)) return true;

		return visitor.leave(this) < 0;
	}
}
