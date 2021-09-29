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

package mrmathami.box.lang.ast.expression;

import mrmathami.box.lang.ast.Keyword;
import mrmathami.box.lang.ast.type.SimpleType;
import mrmathami.box.lang.ast.type.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

public final class LiteralExpression implements Expression {
	private final @NotNull SimpleType type;
	private final @Nullable BigInteger number;
	private final @Nullable Keyword keyword;

	public LiteralExpression(@NotNull SimpleType type, @NotNull BigInteger number) {
		this.type = type;
		this.number = number;
		this.keyword = null;
	}

	public LiteralExpression(@NotNull SimpleType type, @Nullable Keyword keyword) {
		this.type = type;
		this.number = null;
		this.keyword = keyword;
	}

	@Override
	public @NotNull Type resolveType() {
		return type;
	}

	public @NotNull BigInteger getNumber() {
		assert number != null;
		return number;
	}

	public @NotNull Keyword getKeyword() {
		assert keyword != null;
		return keyword;
	}
}
