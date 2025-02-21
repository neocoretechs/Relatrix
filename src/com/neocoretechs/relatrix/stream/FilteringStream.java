package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.relatrix.AbstractRelation;

/**
 * The attempt here is to provide a more customizable retrieval filter.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 * @param <T>
 */
public abstract class FilteringStream<T> extends RelatrixStream<T> implements FilterInterface {
	private AbstractRelation template;
	public FilteringStream(AbstractRelation template, short[] dmr_return) throws IOException {
		super(template, dmr_return);
		this.template = template;
	}

	@Override
	public abstract boolean isFilter(Comparable o1, Comparable o2);


}
