/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.service;

import android.os.Message;

/**
 * User needs implements this interface for callback
 * 
 * @author KevinZeng
 * 
 */
public interface IBusClientListener {
	/**
	 * Service asks client to respond something in function level
	 * 
	 * @param requestOp
	 * @return
	 */
	int requestFunction(int requestOp);

	/**
	 * Service wants to get information about client.
	 * 
	 * @param requestOp
	 * @return
	 */
	int requestGetInfo(int requestOp);

	/**
	 * Service asks client to respond something in client level
	 * 
	 * @param requestOp
	 * @return
	 */
	int requestClient(int requestOp);

	/**
	 * Tell client that it has connected to Hermes Bus<br>
	 * 
	 * @return
	 */
	int connectedToBus();

	/**
	 * Tell client that it has disconnected from Hermes Bus<br>
	 * 
	 * @return
	 */
	int disconnectedFromBus();

	/**
	 * receive client data from Hermes Bus<br>
	 * 
	 * @param clientIdentifier
	 * @param msg
	 * @return
	 */
	int onReceiveBusData_Client(int clientIdentifier, Message msg);

	/**
	 * receive item data from Hermes Bus<br>
	 * 
	 * @param itemIndex
	 * @param msg
	 * @return
	 */
	int onReceiveBusData_Item(int itemIndex, Message msg);

	/**
	 * receive direct data item<br>
	 * 
	 * @param postClientIdentifier
	 * @param msg
	 * @return
	 */
	int onReceiveDirectData(int postClientIdentifier, Message msg);

	/**
	 * another client call this method directly. <br>
	 * DO NOT occupy this methods more time!!!<br>
	 * 
	 * @param msgIn
	 * @return
	 */
	Message remoteCall(Message msgIn);
}
