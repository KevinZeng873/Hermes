package com.hermes.busconfig;
/**
 * This AIDL provide application the ability to communicate with Hermes Bus
 * Author: KevinZeng
 */
import com.hermes.busconfig.IBusListener;
import com.hermes.busconfig.communication.OverProcessMessage;

interface IBusConnection {
	/* ************************************************************************
	 * Function for all clients
	 */
	 
	String getVersion();
	/**
	 * Set the callback on Hermes Bus
	 */
	boolean setListener(String identifierName, int clientId, IBusListener listener);

	/**
	 * An application may have one or more clients which all need register on Hermes Bus.
	 * Client( N * (N *M) ---> Hermes Bus! Wonderful Design!!!
	 */
	int assignNewClientId(String packageName, String appIdentifierName, int clientIdentifier);
	
	/**
	 * Write information into Hermes Bus
	 */
	 void writeInfo(int type, String info);
	 
	/**
	 * Client wants to get notification from the pointed client
	 */
	int registerBusData_Client(int clientSelf, int clientIdentifier);
	 
	/**
	 * Client wants to move the point client's notification
	 */
	int unregisterBusData_Client(int clientSelf, int clientIdentifier);
	 
	/**
	 * Client wants to get the notification for certain item.
	 */
	int registerBusData_Item(int clientSelf, int index);
	 
	/**
	 * client remove notification from the pointed item
	 */
	int unregisterBusData_Item(int clientSelf, int index);
	
	/**
	 * get a client's listener from Hermes Bus
	 */
	IBusListener getClientListener(in int calleeClientIdentifier, in int callerClientIdentifier);
	
	/* ************************************************************************
	 * Function for client which sends client data notification
	 */
	int updateBusData_Client(in int dataClientIdentifier, in OverProcessMessage opMsg);
	 
	/* ************************************************************************
	 * Function for client which sends item data notification
	 */
	int updateBusData_Item(in int clientSelf, in int itemIndex, in OverProcessMessage opMsg);
	
	/* ************************************************************************
	 * Function for client which sends a message to another client
	 */
	int postBusDirectData(in int targetClientIdentifier, in int postClientIdentifier,in OverProcessMessage opMsg);
}
