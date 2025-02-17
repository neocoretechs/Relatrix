package com.neocoretechs.relatrix.test.server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoadTest {
	Process[] processes;
	  public LoadTest(String mainClass, int numInstances) throws IOException {
	    	processes = new Process[numInstances];
	    	for (int i = 0; i < numInstances; i++) {
	    		ProcessBuilder processBuilder = new ProcessBuilder(
	    				"java",
	    				"-cp",
	    				System.getProperty("java.class.path"),
	    				mainClass
	    				);
	    		Process process = processBuilder.inheritIO().start();
	    		System.out.println("Spawned process with PID: " + process);
	    		processes[i] = process;
	    		// Optional: Wait for the process to finish
	    		// process.waitFor();
	    	}
	    }
    public LoadTest(String mainClass, String arg, int numInstances) throws IOException {
    	processes = new Process[numInstances];
    	for (int i = 0; i < numInstances; i++) {
    		ProcessBuilder processBuilder = new ProcessBuilder(
    				"java",
    				"-cp",
    				System.getProperty("java.class.path"),
    				mainClass,
    				arg
    				);
    		Process process = processBuilder.inheritIO().start();
    		System.out.println("Spawned process with PID: " + process);
    		processes[i] = process;
    		// Optional: Wait for the process to finish
    		// process.waitFor();
    	}
    }
    public LoadTest(String mainClass, String arg, String arg2, int numInstances) throws IOException {
    	processes = new Process[numInstances];
    	for (int i = 0; i < numInstances; i++) {
    		ProcessBuilder processBuilder = new ProcessBuilder(
    				"java",
    				"-cp",
    				System.getProperty("java.class.path"),
    				mainClass,
    				arg,
    				arg2
    				);
    		Process process = processBuilder.inheritIO().start();
    		System.out.println("Spawned process with PID: " + process);
    		processes[i] = process;
    		// Optional: Wait for the process to finish
    		// process.waitFor();
    	}
    }
    public LoadTest(String mainClass, String arg, String arg2, String arg3, int numInstances) throws IOException {
    	processes = new Process[numInstances];
    	for (int i = 0; i < numInstances; i++) {
    		ProcessBuilder processBuilder = new ProcessBuilder(
    				"java",
    				"-cp",
    				System.getProperty("java.class.path"),
    				mainClass,
    				arg,
    				arg2,
    				arg3
    				);
    		Process process = processBuilder.inheritIO().start();
    		System.out.println("Spawned process with PID: " + process);
    		processes[i] = process;
    		// Optional: Wait for the process to finish
    		// process.waitFor();
    	}
    }
    public LoadTest(String mainClass, String arg, String arg2, String arg3, String arg4, int numInstances) throws IOException {
    	processes = new Process[numInstances];
    	for (int i = 0; i < numInstances; i++) {
    		ProcessBuilder processBuilder = new ProcessBuilder(
    				"java",
    				"-cp",
    				System.getProperty("java.class.path"),
    				mainClass,
    				arg,
    				arg2,
    				arg3,
    				arg4
    				);
    		Process process = processBuilder.inheritIO().start();
    		System.out.println("Spawned process with PID: " + process);
    		processes[i] = process;
    		// Optional: Wait for the process to finish
    		// process.waitFor();
    	}
    }
    public LoadTest(String mainClass, String arg, String arg2, String arg3, String arg4, String arg5, int numInstances) throws IOException {
    	processes = new Process[numInstances];
    	for (int i = 0; i < numInstances; i++) {
    		ProcessBuilder processBuilder = new ProcessBuilder(
    				"java",
    				"-cp",
    				System.getProperty("java.class.path"),
    				mainClass,
    				arg,
    				arg2,
    				arg3,
    				arg4,
    				arg5
    				);
    		Process process = processBuilder.inheritIO().start();
    		System.out.println("Spawned process with PID: " + process);
    		processes[i] = process;
    		// Optional: Wait for the process to finish
    		// process.waitFor();
    	}
    }
	public static void main(String[] args) throws Exception {
		if(args.length < 2) {
			System.out.println("usage: java LoadTest <number instances> <com.yourclass.name> [params]");
			System.exit(1);
		}
		int numInst = Integer.parseInt(args[0]);
		LoadTest loadTest = null;

		switch(args.length) {
		case 2:
			loadTest = new LoadTest(args[1], numInst);
			break;
		case 3:
			loadTest = new LoadTest(args[1], args[2], numInst);
			break;
		case 4:
			loadTest = new LoadTest(args[1], args[2], args[3], numInst);
			break;
		case 5:
			loadTest = new LoadTest(args[1], args[2], args[3], args[4], numInst);
			break;
		case 6:
			loadTest = new LoadTest(args[1], args[2], args[3], args[4], args[5], numInst);
			break;
		case 7:
			loadTest = new LoadTest(args[1], args[2], args[3], args[4], args[5], args[6], numInst);
			break;
		}
	
		while(true) {
			int allDone = 0;
			for(int i = 0; i < loadTest.processes.length; i++) {
				loadTest.processes[i].waitFor(5, TimeUnit.SECONDS);
				if(!loadTest.processes[i].isAlive())
					++allDone;
				else
					System.out.println("Process "+i+".)"+loadTest.processes[i]+" running...");
			}
			if(allDone == loadTest.processes.length)
				break;
		}
	}

}
