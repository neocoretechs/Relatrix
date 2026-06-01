package com.neocoretechs.relatrix.stream.json;

import java.io.IOException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.stream.FilterInterface;

/**
 * The attempt here is to provide a more customizable retrieval filter.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2015,2021
 * @param <T>
 */
public abstract class FilteringStreamJson<T> extends RelatrixStreamJson<T> implements FilterInterface {
	private AbstractRelation template;
	public FilteringStreamJson(AbstractRelation template, short[] dmr_return) throws IOException {
		super(template, dmr_return);
		this.template = template;
	}

	@Override
	public abstract boolean isFilter(Comparable o1, Comparable o2);


}
