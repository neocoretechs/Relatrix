package com.neocoretechs.relatrix.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.neocoretechs.relatrix.server.GenerateClientBindings;
import com.neocoretechs.relatrix.server.ServerInvokeMethod;
/**
* A basic Serializable helper class used with {@link ServerInvokeMethod} with arrays of Method names and 
* parameters for a target class to be passed to a remote "handler" class whose transport layer can be generated by hand or
* via {@link GenerateClientBindings}. There are typically one of these instances per target class.
* An instance of this may be passed to client or server on RemoteObject creation.  This will contain the
* methods to be advertised to the rest of the world.  A call
* from remote client may verify the method before remote call using an instance of this.<p/>
* An index into any of the arrays and/or collections will point to the corresponding elements in the other structures
* for that particular method of the target class.
* @author Jonathan Groff Copyright (C) NeoCoreTechs, Inc. 1998-2000,2015
*/
public final class MethodNamesAndParams implements Serializable {
       static final long serialVersionUID = 8837760295724028863L;
       public transient Class<?> classClass;
       public String className;
       public transient ArrayList<String> methodNames = new ArrayList<String>();
       public transient Class<?>[][] methodParams;
       public String[] methodSigs;
       public transient Class<?>[] returnTypes;

       /**
       * No arg ctor call for deserialized
       */
       public MethodNamesAndParams() {}

       public String[] getMethodSigs() { return methodSigs; }

       public Class<?>[] getReturnTypes() { return returnTypes; }

       public List<String> getMethodNames() { return methodNames; }
 

}
