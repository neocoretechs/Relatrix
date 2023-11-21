package com.neocoretechs.relatrix.test;

import com.neocoretechs.relatrix.CompareAndSerialize;
import com.neocoretechs.relatrix.ComparisonOrderField;
import com.neocoretechs.relatrix.ComparisonOrderMethod;

@CompareAndSerialize
public class TestTooling {
	@ComparisonOrderField(order=1)
	int i;
	@ComparisonOrderField(order=2)
	String j;
	byte[] l;
	@ComparisonOrderMethod(order=3)
	public String getj() {
		return j;
	}
}
