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

import mrmathami.box.lang.ast.expression.Expression;
import mrmathami.box.lang.visitor.Visitor;
import mrmathami.box.lang.visitor.VisitorException;
import org.eclipse.collections.api.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class IfStatement implements SingleStatement {
	private final @NotNull List<@NotNull Pair<@NotNull Expression, @Nullable SingleStatement>> conditionStatementPairs;
	private final @Nullable SingleStatement alternativeStatement;

	public IfStatement(
			@NotNull List<@NotNull Pair<@NotNull Expression, @Nullable SingleStatement>> conditionStatementPairs,
			@Nullable SingleStatement alternativeStatement) {
		this.conditionStatementPairs = conditionStatementPairs;
		this.alternativeStatement = alternativeStatement;
	}

	public @NotNull List<@NotNull Pair<@NotNull Expression, @Nullable SingleStatement>> getConditionStatementPairs() {
		return conditionStatementPairs;
	}

	public @Nullable SingleStatement getAlternativeStatement() {
		return alternativeStatement;
	}
	
	@Override
	public boolean visit(@NotNull Visitor visitor) throws VisitorException {
		final int enter = visitor.enter(this);
		if (enter != 0) return enter < 0;

		for (final Pair<Expression, SingleStatement> pair : conditionStatementPairs) {
			final Expression expression = pair.getOne();
			if (expression.visit(visitor)) return true;

			final SingleStatement singleStatement = pair.getTwo();
			if (singleStatement.visit(visitor)) return true;
		}
		if (alternativeStatement != null && alternativeStatement.visit(visitor)) return true;

		return visitor.leave(this) < 0;
	}
}
