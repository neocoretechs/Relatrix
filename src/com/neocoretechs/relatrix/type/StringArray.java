package com.neocoretechs.relatrix.type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 * Provides the ability to store ordered instances of numeric arrays.
 * Order is compared by performing String.compareTo on each element.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2024
 *
 */
public class StringArray implements Serializable, Comparable {
	private static final long serialVersionUID = -4013285847253701629L;
	private String[] arrayType;
	public StringArray(int len) {
		arrayType = new String[len];
	}

	public String getElement(int index) {
		return arrayType[index];
	}
	
	public String[] get() {
		return arrayType;
	}
	
	public List<?> asList() {
		return Arrays.asList(arrayType);
	}
	
	@Override
	public int compareTo(Object o) {
		for(int i = 0; i < arrayType.length; i++) {
			int cmp = arrayType[i].compareTo(((StringArray)o).getElement(i));
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
		if (!(obj instanceof StringArray)) {
			return false;
		}
		StringArray other = (StringArray) obj;
		return Objects.equals(arrayType, other.arrayType);
	}

}
