package com.neocoretechs.relatrix.client;

import java.io.Serializable;
/**
 * The following class allows the transport of Relatrix method calls to the server
 * @author jg
 *
 */
public class RelatrixStatement implements Serializable {
    static final long serialVersionUID = 8649844374668828845L;
    private String session, className, methodName;
    Object objref;
    private Object[] paramArray;
    
    public RelatrixStatement() {}
    /**
    * Prep RelatrixStatement to send remote method call
    */
    public RelatrixStatement(String tsession, String tclass, Object tobjref, String tmeth, Object[] o1) {
             session = tsession;
             className = tclass;
             objref = tobjref;
             methodName = tmeth;
             paramArray = o1;
    }
    public void RelatirixStatment(String tsession, String tclass, Object tobjref, String tmeth, Object ... o1) {
             session = tsession;
             className = tclass;
             objref = tobjref;
             methodName = tmeth;
             paramArray = o1;
    }
 
 
    public String getClassName() { return className; }
    public String getSession() { return session; }
    public String getMethodName() { return methodName; }
    public Object getObjref() { return objref; }
    public Object[] getParamArray() { return paramArray; }

    /**
    * @return An array of Class objects for the parameters of the remote method
    */
    public Class[] getParams() {
             Class[] c = new Class[paramArray.length];
             for(int i = 0; i < paramArray.length; i++)
                     c[i] = paramArray[i].getClass();
             return c;
    }
  

    public String toString() { return "<Method call transport> Session: "+
             session+" Class: "+className+" Method: "+methodName+" Arg: "+
             (paramArray == null || paramArray.length == 0 ? "nil" :
             (paramArray[0] == null ? "NULL PARAM!" : paramArray[0])); }

}
