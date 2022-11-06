package com.neocoretechs.relatrix.client;

/**
 * Defines a contract for a request to a remote Relatrix server that provides baseline parameters necessary to invoke
 * a remote method using an established session. The baseline data includes class name, method name, session Id, and the
 * class and value of the parameters to the remote method call.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2022
 *
 */
public interface RemoteRequestInterface {

	public String getClassName();

	public String getSession();

	public String getMethodName();

	public Object[] getParamArray();

	/**
	 * @return An array of Class objects for the parameters of the remote method
	 */
	public Class<?>[] getParams();

}