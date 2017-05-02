package com.neocoretechs.relatrix.iterator;

import java.io.IOException;

import com.neocoretechs.bigsack.session.BufferedTreeSet;
import com.neocoretechs.relatrix.Morphism;
import com.neocoretechs.relatrix.typedlambda.TemplateClass;
/**
 * The attempt here is to provide a more customizable retrieval filter.
 * @author jg Groff Copyright (C) NeoCoreTechs 2014,2105
 */
public class FilteringIterator extends RelatrixIterator implements FilterInterface {
	private Morphism template;
	public FilteringIterator(BufferedTreeSet bts, Morphism template, short[] dmr_return) throws IOException {
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
	public boolean isFilter(Comparable o1, Comparable o2) {
		Class co1=null, co2=null;
		if( o1 instanceof TemplateClass ) {
			co1 = ((TemplateClass)o1).getComparableClass();
		} else {
			co1 = o1.getClass();
		}
		if( o2 instanceof TemplateClass) {
			co2 = ((TemplateClass)o2).getComparableClass();
		} else {
			co2 = o2.getClass();
		}
		if( o1 instanceof TemplateClass || o2 instanceof TemplateClass ) {
			return co1.equals(co2);
		}
		return false;
	}
}
