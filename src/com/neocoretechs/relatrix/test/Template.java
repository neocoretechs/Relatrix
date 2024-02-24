package com.neocoretechs.relatrix.test;

import com.neocoretechs.relatrix.DomainMapRange;
import com.neocoretechs.relatrix.key.PrimaryKey;

public class Template {

	public static void main(String[] args) throws CloneNotSupportedException {
		PrimaryKey primary1 = new PrimaryKey();
		System.out.println(primary1.clone());	
		System.out.println(primary1.superclass());	
		System.out.println(primary1.getClass().getName());	


	}

}
