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

package mrmathami.box.lang.ast.statement;

import mrmathami.annotations.Nonnull;
import mrmathami.annotations.Nullable;
import mrmathami.box.lang.ast.expression.Expression;
import mrmathami.utils.Pair;

import java.util.List;

public final class IfStatement implements SingleStatement {
	@Nonnull private final List<Pair<Expression, SingleStatement>> conditionStatementPairs;
	@Nullable private final SingleStatement alternativeStatement;

	public IfStatement(@Nonnull List<Pair<Expression, SingleStatement>> conditionStatementPairs,
			@Nullable SingleStatement alternativeStatement) {
		this.conditionStatementPairs = conditionStatementPairs;
		this.alternativeStatement = alternativeStatement;
	}

	@Nonnull
	public List<Pair<Expression, SingleStatement>> getConditionStatementPairs() {
		return conditionStatementPairs;
	}

	@Nullable
	public SingleStatement getAlternativeStatement() {
		return alternativeStatement;
	}
}
