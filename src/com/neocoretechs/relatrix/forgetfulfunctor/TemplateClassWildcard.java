package com.neocoretechs.relatrix.forgetfulfunctor;

import java.io.IOException;

import com.neocoretechs.relatrix.Relatrix;

@SuppressWarnings("serial")
public class TemplateClassWildcard extends TemplateClass {
	public TemplateClassWildcard(Class<?> class1) {
		super(class1);
	}

	public char getRetrievalPredicate() throws IOException {
		return Relatrix.OPERATOR_WILDCARD_CHAR;
	}
}
