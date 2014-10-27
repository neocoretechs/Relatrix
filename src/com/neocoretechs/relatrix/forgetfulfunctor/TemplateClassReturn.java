package com.neocoretechs.relatrix.forgetfulfunctor;

import java.io.IOException;

import com.neocoretechs.relatrix.Relatrix;

@SuppressWarnings("serial")
public class TemplateClassReturn extends TemplateClass {
	public TemplateClassReturn(Class<?> class1) {
		super(class1);
	}
	public char getRetrievalPredicate() throws IOException {
		return Relatrix.OPERATOR_TUPLE_CHAR;
	}
}
