package com.neocoretechs.relatrix.iterator;
/**
 * The attempt here is to provide a more customizable retrieval filter.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105
 */
public interface FilterInterface {
	public boolean isFilter(Comparable o1, Comparable o2);
}
