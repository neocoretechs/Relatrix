package com.neocoretechs.relatrix.test;

import com.neocoretechs.relatrix.CompareAndSerialize;
import com.neocoretechs.relatrix.ComparisonOrderField;
import com.neocoretechs.relatrix.ComparisonOrderMethod;

@CompareAndSerialize
public class TestTooling3 implements /*java.io.Serializable*/ java.lang.Comparable{
	@ComparisonOrderField(order=1)
	int i;
	@ComparisonOrderField(order=2)
	String j;
	byte[] l;
	@ComparisonOrderMethod(order=3)
	public String getj() {
		return j;
	}
	
    @Override
    public int compareTo(Object o) {
            int n;
            if(i < ((TestTooling)o).i)
                    return -1;
            if(i > ((TestTooling)o).i)
                    return 1;
            n = j.compareTo(((TestTooling)o).j);
            if(n != 0)
                    return n;
            n = getj().compareTo(((TestTooling)o).getj());
            if(n != 0)
                    return n;
            return 0;
    }
    
}
