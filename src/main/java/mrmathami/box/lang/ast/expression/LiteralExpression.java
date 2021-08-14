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

import mrmathami.annotations.Nonnull;
import mrmathami.annotations.Nullable;
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.expression.Expression;
import mrmathami.box.lang.ast.expression.other.Keyword;
import mrmathami.box.lang.ast.type.SimpleType;
import mrmathami.box.lang.ast.type.Type;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public final class LiteralExpression implements Expression {
	@Nonnull private final SimpleType type;
	@Nullable private final BigInteger number;
	@Nullable private final Keyword keyword;

	public LiteralExpression(@Nonnull SimpleType type, @Nonnull BigInteger number) {
		this.type = type;
		this.number = number;
		this.keyword = null;
	}

	public LiteralExpression(@Nonnull SimpleType type, @Nullable Keyword keyword) {
		this.type = type;
		this.number = null;
		this.keyword = keyword;
	}

	@Nonnull
	@Override
	public Type resolveType() {
		return type;
	}

	@Nonnull
	public BigInteger getNumber() {
		assert number != null;
		return number;
	}

	@Nonnull
	public Keyword getKeyword() {
		assert keyword != null;
		return keyword;
	}
}
