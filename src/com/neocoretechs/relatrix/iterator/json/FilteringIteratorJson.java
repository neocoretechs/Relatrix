package com.neocoretechs.relatrix.iterator.json;

import java.io.IOException;

import com.neocoretechs.relatrix.AbstractRelation;
import com.neocoretechs.relatrix.iterator.FilterInterface;
import com.neocoretechs.relatrix.parallel.ParallelExecutionContext;

/**
 * The attempt here is to provide a more customizable retrieval filter.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105
 */
public abstract class FilteringIteratorJson extends RelatrixIteratorJson implements FilterInterface {
	public FilteringIteratorJson(AbstractRelation template, short[] dmr_return, ParallelExecutionContext ctx) throws IOException {
		super(template, dmr_return, ctx);
	}
	/**
	 * Move through the returned set and apply filter
	 */
	public boolean hasNext() {
		if( buffer == null || needsIter) {
			needsIter = false;
			while(iter.hasNext()) {
				buffer = (AbstractRelation)iter.next();
				if( !isFilter(base.getDomain(), buffer.getDomain()) &&
					!isFilter(base.getMap(), buffer.getMap()) &&
					!isFilter(base.getRange(), buffer.getRange()) ) {
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
