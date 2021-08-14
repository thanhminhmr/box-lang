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
import mrmathami.box.lang.ast.InvalidASTException;
import mrmathami.box.lang.ast.expression.other.Operator;
import mrmathami.box.lang.ast.type.Type;

import java.util.Iterator;
import java.util.List;

public abstract class BinaryExpression implements Expression {
	@Nonnull protected final List<Expression> operands;
	@Nonnull protected final Operator operator;

	BinaryExpression(@Nonnull List<Expression> operands, @Nonnull Operator operator) {
		assert operands.size() >= 2 && operator.isBinary();
		this.operands = operands;
		this.operator = operator;
	}

	static void sizeCheck(@Nonnull List<Expression> operands) throws InvalidASTException {
		if (operands.size() < 2) throw new InvalidASTException("Invalid binary expression: too few operands.");
	}

	static Type sameTypeCheck(@Nonnull List<Expression> operands) throws InvalidASTException {
		final Iterator<Expression> iterator = operands.iterator();
		final Type type = iterator.next().resolveType();

		do {
			if (!type.equals(iterator.next().resolveType())) {
				throw new InvalidASTException("Invalid binary expression: operands have different types.");
			}
		} while (iterator.hasNext());
		return type;
	}

	@Nonnull
	public final List<Expression> getOperands() {
		return operands;
	}

	@Nonnull
	public final Operator getOperator() {
		return operator;
	}
}
