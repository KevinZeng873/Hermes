/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */


package com.hermes.busapi.service.manager.managerop;

import android.os.Message;

import com.hermes.busapi.service.IBusClientListener;

/**
 * Call features of BusAppManager from client side
 * 
 * @author KevinZeng
 * 
 */
public interface IClientSideOp {
	/**
	 * look over the whole list and if we find someone which has an invalid
	 * clientId, the add them on Hermes Bus.
	 */
	public void registerAllClient();

	/**
	 * Client is ready to connect to Hermes Bus.
	 * 
	 * @param clientIdentifier
	 * @param listener
	 */
	public void registerClient(int clientIdentifier,
			IBusClientListener listener);

	/**
	 * Client subscribes the BusData_Client
	 */
	int registerBusData_Client(int clientSelf, int clientIdentifier);

	/**
	 * Client un-subscribe BusData_Client
	 */
	int unregisterBusData_Client(int clientSelf, int clientIdentifier);

	/**
	 * Client subscribes BusData_Item
	 */
	int registerBusData_Item(int clientSelf, int index);

	/**
	 * client un-subscribes BusData_Item
	 */
	int unregisterBusData_Item(int clientSelf, int index);

	/**
	 * Client publishes BusData_Client
	 * 
	 * @param clientSelf
	 * @param message
	 * @return
	 */
	int updateBusData_Client(int clientSelf, Message message);

	/**
	 * Client publish BusData_Item
	 * 
	 * @param itemIndex
	 * @param message
	 * @return
	 */
	int updateBusData_Item(int clientSelf, int itemIndex, Message message);

	/**
	 * Client sends Bus direct data
	 * 
	 * @param targetClientIdentifier
	 * @param postClientIdentifier
	 * @param msg
	 * @return
	 */
	int sendBusDirectData(int targetClientIdentifier, int postClientIdentifier,
			Message msg);

	/**
	 * Client ask to do a remote call to another client<br>
	 *
	 * @param calleeClientIdentifier
	 * @param callerClientIdentifier
	 * @param msgIn
     * @return
     */
	Message remoteDirectCall(int calleeClientIdentifier,
			int callerClientIdentifier, Message msgIn);

	/**
	 * Client wants to know if a pointed client is on Hermes Bus.
	 * 
	 * @param calleeClientIdentifier
	 * @return
	 */
	boolean isClientOnBus(int calleeClientIdentifier,
			int callerClientIdentifier);

}
