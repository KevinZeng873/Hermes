/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.busdaemon.manager.op;

import com.hermes.busapi.service.IBusClientListener;

import android.content.Context;
import android.os.Message;

/**
 * this interface help user to do some operations.
 * 
 * @author KevinZeng
 * 
 */
public interface ICoreClient {
	/**
	 * User calls this method to set the callback.<br>
	 * 
	 * @param context
	 */
	public void init(Context context, IBusClientListener listener);

	/**
	 * Client subscribe item data<br>
	 * 
	 * @param index
	 * @return
	 */
	public int registerBusData_Item(int index);

	/**
	 * @param index
	 * @return
	 */
	public int unregisterBusData_Item(int index);

	/**
	 * Client subscribes client data<br>
	 * 
	 * @param clientIdentifier
	 * @return
	 */
	public int registerBusData_Client(int clientIdentifier);

	/**
	 * @param clientIdentifier
	 * @return
	 */
	public int unregisterBusData_Client(int clientIdentifier);

	/**
	 * Client publish BusData_Client
	 * 
	 * @param msg
	 * @return
	 */
	public int updateBusData_Client(Message msg);

	/**
	 * Client publish BusData_Item
	 * 
	 * @param itemIndex
	 * @param msg
	 * @return
	 */
	public int updateBusData_Item(int itemIndex, Message msg);

	/**
	 * Client Sends DirectData to another client
	 * 
	 * @param targetClientIdentifier
	 * @param msg
	 * @return
	 */
	public int sendDirectData(int targetClientIdentifier, Message msg);

	/**
	 * Client Sends DirectData to another client
	 * 
	 * @param msg
	 * @return
	 */
	public int sendDirectDataAutomatically(Message msg);

	/**
	 * process call<br>
	 * 
	 * @param msgIn
	 * @return
	 */
	public Message remoteDirectCall(Message msgIn);

	/**
	 * Ask whether or not the pointed client is available in Hermes bus<br>
	 * 
	 * @param targetClientIdentifier
	 * @return
	 */
	public boolean isClientOnBus(int targetClientIdentifier);
}
