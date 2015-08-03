package com.neocoretechs.relatrix.client;


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