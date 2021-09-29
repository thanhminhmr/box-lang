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

package mrmathami.box.lang.ast.type;

import mrmathami.box.lang.ast.identifier.TupleIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TupleType implements Type {
	private final @NotNull TupleIdentifier identifier;

	public TupleType(@NotNull TupleIdentifier identifier) {
		this.identifier = identifier;
	}

	public @NotNull TupleIdentifier getIdentifier() {
		return identifier;
	}

	@Override
	public boolean isAssignableFrom(@NotNull Type type) {
		return equals(type);
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (this == object) return true;
		if (!(object instanceof TupleType)) return false;
		final TupleType tupleType = (TupleType) object;
		return identifier.equals(tupleType.identifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier);
	}
}
