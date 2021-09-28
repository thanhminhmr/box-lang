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
import mrmathami.box.lang.ast.identifier.TupleIdentifier;

import java.util.Collections;
import java.util.List;

public final class TupleDefinition implements Definition {
	@Nonnull private final TupleIdentifier identifier;
	@Nonnull private final List<MemberDefinition> members;

	public TupleDefinition(@Nonnull TupleIdentifier identifier, @Nonnull List<MemberDefinition> members) {
		this.identifier = identifier;
		this.members = members;
	}

	@Nonnull
	@Override
	public TupleIdentifier getIdentifier() {
		return identifier;
	}

	@Nonnull
	public List<MemberDefinition> getMembers() {
		return Collections.unmodifiableList(members);
	}
}
