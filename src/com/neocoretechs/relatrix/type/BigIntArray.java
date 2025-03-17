package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Provides the ability to store ordered instances of numeric arrays.
 * Order is compared by performing BigInteger.compareTo on each element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class BigIntArray implements Serializable, Comparable {
	private static final long serialVersionUID = -3653846473704270093L;
	private BigInteger[] arrayType;
	public BigIntArray(int len) {
		arrayType = new BigInteger[len];
	}

	public BigInteger getElement(int index) {
		return arrayType[index];
	}
	
	public BigInteger[] get() {
		return arrayType;
	}
	
	public List<?> asList() {
		return Arrays.asList(arrayType);
	}
	
	@Override
	public int compareTo(Object o) {
		for(int i = 0; i < arrayType.length; i++) {
			int cmp = arrayType[i].compareTo(((BigIntArray)o).getElement(i));
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
		if (!(obj instanceof BigIntArray)) {
			return false;
		}
		BigIntArray other = (BigIntArray) obj;
		return Objects.equals(arrayType, other.arrayType);
	}

}
