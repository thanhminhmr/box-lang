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

import mrmathami.annotations.Nonnull;
import mrmathami.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public final class ArrayType implements Type {
	@Nonnull private final Type innerType;
	@Nonnull private final long[] dimensionSizes;

	public ArrayType(@Nonnull Type innerType, @Nonnull long[] dimensionSizes) {
		this.innerType = innerType;
		this.dimensionSizes = dimensionSizes;
	}

	@Nonnull
	public Type getInnerType() {
		return innerType;
	}

	@Nonnull
	public Type getMostInnerType() {
		return innerType instanceof ArrayType
				? ((ArrayType) innerType).getMostInnerType()
				: innerType;
	}

	public int getNumOfDimensions() {
		return dimensionSizes.length;
	}

	@Nonnull
	public long[] getDimensionSizes() {
		return dimensionSizes.clone();
	}

	@Override
	public String toString() {
		return innerType + Arrays.toString(dimensionSizes);
	}

	@Override
	public boolean isAssignableFrom(@Nonnull Type type) {
		if (!(type instanceof ArrayType)) return false;
		final ArrayType arrayType = (ArrayType) type;
		final int length = dimensionSizes.length;
		final long[] sizes = arrayType.dimensionSizes;
		if (length != sizes.length) return false;
		for (int i = 0; i < length; i++) {
			if (dimensionSizes[i] >= 0 && sizes[i] >= 0 && dimensionSizes[i] != sizes[i]) return false;
		}
		return true;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (this == object) return true;
		if (!(object instanceof ArrayType)) return false;
		final ArrayType arrayType = (ArrayType) object;
		return innerType.equals(arrayType.innerType) && Arrays.equals(dimensionSizes, arrayType.dimensionSizes);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(innerType);
		result = 31 * result + Arrays.hashCode(dimensionSizes);
		return result;
	}
}
