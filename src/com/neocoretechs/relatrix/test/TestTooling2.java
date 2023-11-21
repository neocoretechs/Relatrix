package com.neocoretechs.relatrix.test;

import com.neocoretechs.relatrix.CompareAndSerialize;
import com.neocoretechs.relatrix.ComparisonOrderField;
import com.neocoretechs.relatrix.ComparisonOrderMethod;

@CompareAndSerialize
public class TestTooling2 {
	@ComparisonOrderField
	private int i;
	@ComparisonOrderField
	private String j;
	@ComparisonOrderField
	byte[] l;
	@ComparisonOrderMethod
	public byte[] getL() {
		return l;
	}
}
