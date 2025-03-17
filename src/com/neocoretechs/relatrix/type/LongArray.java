package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Provides the ability to store ordered instances of numeric arrays.
 * Order is compared by performing Long.compare on each element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class LongArray implements Serializable, Comparable {
	private static final long serialVersionUID = -88279691409396005L;
	private long[] arrayType;
	public LongArray(int len) {
		arrayType = new long[len];
	}

	public long getElement(int index) {
		return arrayType[index];
	}
	
	public long[] get() {
		return arrayType;
	}
	
	public List<?> asList() {
		return Arrays.asList(arrayType);
	}
	
	@Override
	public int compareTo(Object o) {
		for(int i = 0; i < arrayType.length; i++) {
			int cmp = Long.compare( arrayType[i], ((LongArray)o).getElement(i));
			if(cmp != 0 )
				return cmp;
		}
		return 0;
	}

	@Override
	public int hashCode() {
		return arrayType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LongArray)) {
			return false;
		}
		LongArray other = (LongArray) obj;
		return Objects.equals(arrayType, other.arrayType);
	}

}
