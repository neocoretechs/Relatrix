package com.neocoretechs.relatrix.stream;

import java.io.IOException;

import com.neocoretechs.bigsack.session.TransactionalTreeSet;
import com.neocoretechs.relatrix.Morphism;

/**
 * The attempt here is to provide a more customizable retrieval filter.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105
 */
public abstract class FilteringStream extends RelatrixStream implements FilterInterface {
	private Morphism template;
	public FilteringStream(TransactionalTreeSet bts, Morphism template, short[] dmr_return) throws IOException {
		super(bts, template, dmr_return);
		this.template = template;
	}
	/**
	 * Move through the returned set and apply filter
	 */
	public boolean hasNext() {
		if( buffer == null || needsIter) {
			needsIter = false;
			while(iter.hasNext()) {
				buffer = (Morphism)iter.next();
				if( !isFilter(template.domain, buffer.domain) &&
					!isFilter(template.map, buffer.map) &&
					!isFilter(template.range, buffer.range) ) {
					return true;
				}
			}
		}
		needsIter = true;
		return iter.hasNext();
	}
	
	@Override
	public abstract boolean isFilter(Comparable o1, Comparable o2);

	

}