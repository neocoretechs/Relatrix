package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Provides the ability to store ordered instances of numeric arrays.
 * Order is compared by performing Boolean.compare on each element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class BoolArray implements Serializable, Comparable {
	private static final long serialVersionUID = 1474146173336699338L;
	private boolean[] arrayType;
	public BoolArray(int len) {
		arrayType = new boolean[len];
	}

	public boolean getElement(int index) {
		return arrayType[index];
	}
	
	public boolean[] get() {
		return arrayType;
	}
	
	public List<?> asList() {
		return Arrays.asList(arrayType);
	}
	
	@Override
	public int compareTo(Object o) {
		for(int i = 0; i < arrayType.length; i++) {
			int cmp = Boolean.compare( arrayType[i], ((BoolArray)o).getElement(i));
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
		if (!(obj instanceof BoolArray)) {
			return false;
		}
		BoolArray other = (BoolArray) obj;
		return Objects.equals(arrayType, other.arrayType);
	}

}
