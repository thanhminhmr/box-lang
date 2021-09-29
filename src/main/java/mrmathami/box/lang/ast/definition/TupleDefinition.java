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

import mrmathami.box.lang.ast.identifier.TupleIdentifier;
import mrmathami.box.lang.visitor.Visitor;
import mrmathami.box.lang.visitor.VisitorException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class TupleDefinition implements GlobalDefinition {
	private final @NotNull TupleIdentifier identifier;
	private final @NotNull List<@NotNull MemberDefinition> members;

	public TupleDefinition(@NotNull TupleIdentifier identifier, @NotNull List<@NotNull MemberDefinition> members) {
		this.identifier = identifier;
		this.members = members;
	}

	@Override
	public @NotNull TupleIdentifier getIdentifier() {
		return identifier;
	}

	@NotNull
	public List<@NotNull MemberDefinition> getMembers() {
		return Collections.unmodifiableList(members);
	}


	@Override
	public boolean visit(@NotNull Visitor visitor) throws VisitorException {
		final int enter = visitor.enter(this);
		if (enter != 0) return enter < 0;

		if (identifier.visit(visitor)) return true;
		for (final MemberDefinition member : members) {
			if (member.visit(visitor)) return true;
		}

		return visitor.leave(this) < 0;
	}
}
