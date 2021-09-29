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

package mrmathami.box.lang.visitor;

import mrmathami.box.lang.ast.AstNode;
import org.jetbrains.annotations.NotNull;

public interface Visitor {
	int ABORT = -1;
	int CONTINUE = 0;
	int SKIP = 1; // only useful when entering a node. When leaving a node, this is equal to CONTINUE

	int enter(@NotNull AstNode node) throws VisitorException;

	int leave(@NotNull AstNode node) throws VisitorException;
}
