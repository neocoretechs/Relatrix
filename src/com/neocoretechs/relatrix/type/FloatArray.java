package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Provides the ability to store ordered instances of numeric arrays.
 * Order is compared by performing Float.compare on each element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class FloatArray implements Serializable, Comparable {
	private static final long serialVersionUID = -6001100937246413179L;
	private float[] arrayType;
	public FloatArray(int len) {
		arrayType = new float[len];
	}

	public float getElement(int index) {
		return arrayType[index];
	}
	
	public float[] get() {
		return arrayType;
	}
	
	public List<?> asList() {
		return Arrays.asList(arrayType);
	}
	
	@Override
	public int compareTo(Object o) {
		for(int i = 0; i < arrayType.length; i++) {
			int cmp = Float.compare( arrayType[i], ((FloatArray)o).getElement(i));
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
		if (!(obj instanceof FloatArray)) {
			return false;
		}
		FloatArray other = (FloatArray) obj;
		return Objects.equals(arrayType, other.arrayType);
	}

}
