package com.neocoretechs.relatrix.server;

import java.io.IOException;

import com.neocoretechs.bigsack.io.IoInterface;
/**
 * Interface to provide the contract between the IOWorker and the specific request it is processing.
 * Typically, we process the request, leave the result in an onboard variable, and get it
 * via one of the accessor methods for a long or object
 * Copyright (C) NeoCoreTechs 2014
 * @author jg
 *
 */
public interface IoRequestInterface {
	/**
	 * Method called to accomplish the main processing functionality
	 * This is called after being pulled off the request queue maintained by each IOWorker
	 * @throws IOException
	 */
	public void process() throws IOException;
	/**
	 * In the cases where we have a long value to return as in the offset of a block, 
	 * we can use this to get it stackwise
	 * @return
	 */
	public long getLongReturn();
	/**
	 * In the cases where we have an object to return, as in the case of an actual data block, 
	 * usually set up through the constructor of the request
	 * @return
	 */
	public Object getObjectReturn();
	/**
	 * The following method is called by the dispatcher before processing the request, specifically
	 * right before the IOWorker adds the request to the queue
	 * @param ioi
	 */
	public void setIoInterface(IoInterface ioi);
	/**
	 * The following method is called by the dispatcher before processing the request, specifically
	 * right before the IOWorker adds the request to the queue
	 * @param ioi
	 */
	public void setTablespace(int tablespace);
}
