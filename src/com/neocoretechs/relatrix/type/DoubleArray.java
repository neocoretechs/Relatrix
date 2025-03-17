package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Provides the ability to store ordered instances of numeric arrays.
 * Order is compared by performing Double.compare on each element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class DoubleArray implements Serializable, Comparable {
	private static final long serialVersionUID = 1770682436375373700L;
	private double[] arrayType;
	public DoubleArray(int len) {
		arrayType = new double[len];
	}

	public double getElement(int index) {
		return arrayType[index];
	}
	
	public double[] get() {
		return arrayType;
	}
	
	public List<?> asList() {
		return Arrays.asList(arrayType);
	}
	
	@Override
	public int compareTo(Object o) {
		for(int i = 0; i < arrayType.length; i++) {
			int cmp = Double.compare( arrayType[i], ((DoubleArray)o).getElement(i));
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
		if (!(obj instanceof DoubleArray)) {
			return false;
		}
		DoubleArray other = (DoubleArray) obj;
		return Objects.equals(arrayType, other.arrayType);
	}

}
