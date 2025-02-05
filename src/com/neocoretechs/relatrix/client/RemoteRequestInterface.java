package com.neocoretechs.relatrix.client;
import com.neocoretechs.relatrix.server.GenerateClientBindings;
/**
 * Part of the toolset including {@link ServerInvokeMethod}, {@link RemoteResponseInterface}, {@link GenerateClientBindings}
 * to affect the creation of maintenance of 2 tier client/server models using established infrastructure.<p/>
 * Defines a contract for a request to a remote server that provides baseline parameters necessary to invoke
 * a remote method using an established session. The baseline data includes method name, session Id, and the
 * class and value of the parameters to the remote method call.<p/> 
 * The method is expected to reside in the designated target server instance.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2015,2022
 *
 */
public interface RemoteRequestInterface {

	public String getSession();

	public String getMethodName();

	public Object[] getParamArray();

	/**
	 * @return An array of Class objects for the parameters of the remote method
	 */
	public Class<?>[] getParams();

}