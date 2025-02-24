package com.neocoretechs.relatrix.iterator;

import java.io.IOException;

import com.neocoretechs.relatrix.AbstractRelation;

/**
 * The attempt here is to provide a more customizable retrieval filter.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2014,2105
 */
public abstract class FilteringIterator extends RelatrixIterator implements FilterInterface {
	private AbstractRelation template;
	public FilteringIterator(/*TransactionalTreeSet bts,*/ AbstractRelation template, short[] dmr_return) throws IOException {
		super(/*bts,*/ template, dmr_return);
		this.template = template;
	}
	/**
	 * Move through the returned set and apply filter
	 */
	public boolean hasNext() {
		if( buffer == null || needsIter) {
			needsIter = false;
			while(iter.hasNext()) {
				buffer = (AbstractRelation)iter.next();
				if( !isFilter(template.getDomain(), buffer.getDomain()) &&
					!isFilter(template.getMap(), buffer.getMap()) &&
					!isFilter(template.getRange(), buffer.getRange()) ) {
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
