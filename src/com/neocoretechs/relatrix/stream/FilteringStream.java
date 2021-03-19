package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.Morphism;

/**
 * The attempt here is to provide a more customizable retrieval filter.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105
 * @param <T>
 */
public abstract class FilteringStream<T> extends RelatrixStream<T> implements FilterInterface {
	private Morphism template;
	public FilteringStream(TransactionalTreeSet bts, Morphism template, short[] dmr_return) throws IOException {
		super(bts, template, dmr_return);
		this.template = template;
	}

	@Override
	public abstract boolean isFilter(Comparable o1, Comparable o2);


}
