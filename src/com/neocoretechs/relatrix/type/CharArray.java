package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Provides the ability to store ordered instances of numeric arrays.
 * Order is compared by performing Character.compare on each element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class CharArray implements Serializable, Comparable {
	private static final long serialVersionUID = 1474146173336699338L;
	private char[] arrayType;
	public CharArray(int len) {
		arrayType = new char[len];
	}

	public char getElement(int index) {
		return arrayType[index];
	}
	
	public char[] get() {
		return arrayType;
	}
	
	public List<?> asList() {
		return Arrays.asList(arrayType);
	}
	
	@Override
	public int compareTo(Object o) {
		for(int i = 0; i < arrayType.length; i++) {
			int cmp = Character.compare( arrayType[i], ((CharArray)o).getElement(i));
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
		if (!(obj instanceof CharArray)) {
			return false;
		}
		CharArray other = (CharArray) obj;
		return Objects.equals(arrayType, other.arrayType);
	}

}
