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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum SimpleType implements Type {
	VOID("void"),
	BOOL("bool"),
	NUMBER_U8("u8"),
	NUMBER_U16("u16"),
	NUMBER_U32("u32"),
	NUMBER_U64("u64"),
	NUMBER_I8("i8"),
	NUMBER_I16("i16"),
	NUMBER_I32("i32"),
	NUMBER_I64("i64");

	public static final @NotNull List<@NotNull SimpleType> values = List.of(values());

	private final @NotNull String string;

	SimpleType(@NotNull String string) {
		this.string = string;
	}

	public final boolean isNumber() {
		return this != VOID && this != BOOL;
	}

	@Override
	public boolean isAssignableFrom(@NotNull Type type) {
		return this == type;
	}

	@Override
	public final @NotNull String toString() {
		return string;
	}
}
