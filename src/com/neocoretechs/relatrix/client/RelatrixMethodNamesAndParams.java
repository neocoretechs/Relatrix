package com.neocoretechs.relatrix.client;
import java.io.*;
import java.util.*;

/**
* Method names and parameters for a remote "handler" class. One per class.
* Passed to client on RemoteObject creation.  This will contain the
* methods to be advertised to the rest of the world.  A call
* from remote client will verify the method before remote call
* @author Jonathan Groff Copyright (C) NeoCoreTechs, Inc. 1998-2000,2015
*/
public final class RelatrixMethodNamesAndParams implements Serializable {
       static final long serialVersionUID = 8837760295724028863L;
       public transient Class<?> classClass;
       public String className;
       public transient Vector<String> methodNames = new Vector<String>();
       public transient Class<?>[][] methodParams;
       public String[] methodSigs;
       public transient Class<?>[] returnTypes;

       /**
       * No arg ctor call for deserialized
       */
       public RelatrixMethodNamesAndParams() {}

       public String[] getMethodSigs() { return methodSigs; }

       public Class<?>[] getReturnTypes() { return returnTypes; }

       public Vector<String> getMethodNames() { return methodNames; }
       
       public int getMethodIndex(String name) {
    	  return methodNames.indexOf(name);
       }

}
