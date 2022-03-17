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

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;

public final class IndentAppender implements Appendable {
	private final @NotNull Closeable closeable = () -> IndentAppender.this.indent -= 1;
	private final @NotNull Appendable appendable;

	private int indent = 0;
	private int newLine = 0;

	public IndentAppender(@NotNull Appendable appendable) {
		this.appendable = appendable;
	}

	public @NotNull Closeable indent() {
		this.indent += 1;
		return closeable;
	}

	private void doNewLine() throws IOException {
		if (newLine > 0) {
			appendable.append("\n".repeat(newLine)).append("\t".repeat(indent));
			this.newLine = 0;
		}
	}

	public @NotNull IndentAppender newLine() {
		this.newLine += 1;
		return this;
	}

	@Override
	public @NotNull IndentAppender append(@NotNull CharSequence charSequence) throws IOException {
		doNewLine();
		appendable.append(charSequence);
		return this;
	}

	@Override
	public @NotNull IndentAppender append(@NotNull CharSequence charSequence, int start, int end) throws IOException {
		doNewLine();
		appendable.append(charSequence, start, end);
		return this;
	}

	@Override
	public @NotNull IndentAppender append(char c) throws IOException {
		doNewLine();
		appendable.append(c);
		return this;
	}
}
