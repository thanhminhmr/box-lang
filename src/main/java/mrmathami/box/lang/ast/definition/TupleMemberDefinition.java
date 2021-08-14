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
import mrmathami.box.lang.ast.identifier.MemberIdentifier;
import mrmathami.box.lang.ast.type.Type;

public final class TupleMemberDefinition implements Definition {
	@Nonnull private final Type type;
	@Nonnull private final MemberIdentifier identifier;

	public TupleMemberDefinition(@Nonnull Type type, @Nonnull MemberIdentifier identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	@Nonnull
	public Type getType() {
		return type;
	}

	@Nonnull
	@Override
	public MemberIdentifier getIdentifier() {
		return identifier;
	}
}
