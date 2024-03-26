package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.Vector;

/**
* Method names and parameters for a remote "handler" class. One per class.
* Passed to client on RemoteObject creation.  This will contain the
* methods to be advertised to the rest of the world.  A call
* from remote client will verify the method before remote call
* @author Jonathan Groff Copyright (C) NeoCoreTechs, Inc. 1998-2000,2015
*/
public final class MethodNamesAndParams implements Serializable {
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
       public MethodNamesAndParams() {}

       public String[] getMethodSigs() { return methodSigs; }

       public Class<?>[] getReturnTypes() { return returnTypes; }

       public Vector<String> getMethodNames() { return methodNames; }
       /**
        * Search for the method name combined with as much of the fully qualified param signature as is required to
        * uniquely identify the method, such that if overloaded methods exist, they can be uniquely identified. SO,
        * last(java.lang.String) and last() would require name=last, methodSig="" or methodSig="java.lang.String" or just
        * methodSig="j" to locate overloaded method.
        * @param name method name full
        * @param methodSig parameter list partial with fully qualified class names
        * @return index of method in corresponding methodSigs and returnTypes arrays or -1 if name/sig cannot be found
        */
       public int getMethodIndex(String name, String methodSig) {
    	  int iname = methodNames.indexOf(name);
    	  while(iname != -1) {
    	 	  if(methodSigs[iname].contains(name+"("+methodSig))
        		  break;
    		  iname = methodNames.indexOf(name,iname);
    	  }
    	  return iname;
       }

}
