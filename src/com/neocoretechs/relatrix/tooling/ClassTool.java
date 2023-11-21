package com.neocoretechs.relatrix.tooling;

import java.io.IOException;

public class ClassTool {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, IOException {
		Object classToTool = Class.forName(args[0]).newInstance();
		InstrumentClass instrument = new InstrumentClass();
		instrument.process(null, classToTool);
	}

}
