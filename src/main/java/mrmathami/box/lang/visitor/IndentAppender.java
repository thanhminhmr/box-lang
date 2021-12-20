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
