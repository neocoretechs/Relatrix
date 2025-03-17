package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Provides the ability to store ordered instances of numeric arrays.
 * Order is compared by performing Byte.compare on each element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class ByteArray implements Serializable, Comparable {
	private static final long serialVersionUID = 500877870572891355L;
	private byte[] arrayType;
	public ByteArray(int len) {
		arrayType = new byte[len];
	}

	public byte getElement(int index) {
		return arrayType[index];
	}
	
	public byte[] get() {
		return arrayType;
	}
	
	public List<?> asList() {
		return Arrays.asList(arrayType);
	}
	
	@Override
	public int compareTo(Object o) {
		for(int i = 0; i < arrayType.length; i++) {
			int cmp = Byte.compare( arrayType[i], ((ByteArray)o).getElement(i));
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
		if (!(obj instanceof ByteArray)) {
			return false;
		}
		ByteArray other = (ByteArray) obj;
		return Objects.equals(arrayType, other.arrayType);
	}

}
