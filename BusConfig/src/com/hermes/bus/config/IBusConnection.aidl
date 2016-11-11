/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config;
/**
 * This AIDL provide application the ability to "call" CoreAppService
 * Author: KevinZeng
 */
import com.hermes.bus.config.IBusListener;
import com.hermes.bus.config.communication.OverProcessMessage;

interface IBusConnection {
	/* ************************************************************************
	 * Function for all clients
	 */
	 
	String getVersion();
	/**
	 * Set the callback connection from service to App
	 */
	boolean setListener(String identifierName, int clientId, IBusListener listener);

	/**
	 * An application may have one or more clients which all need register on service.
	 * Client( N * (N *M) ---> service! Wonderful Design!!!
	 */
	int assignNewClientId(String packageName, String appIdentifierName, int clientIdentifier);
	
	/**
	 * Write information into service
	 */
	 void writeInfo(int type, String info);
	 
	/**
	 * Client wants to get notification from the pointed client
	 */
	int registerClientDataNotify(int clientSelf, int clientIdentifier);
	 
	/**
	 * Client wants to move the point client's notification
	 */
	int unregisterClientDataNotify(int clientSelf, int clientIdentifier);
	 
	/**
	 * Client wants to get the notification for certain item.
	 */
	int registerItemDataNotify(int clientSelf, int index);
	 
	/**
	 * client remove notification from the pointed item
	 */
	int unregisterItemDataNotify(int clientSelf, int index);  
	
	/**
	 * get a client's listener
	 */
	IBusListener getClientListener(in int calleeClientIdentifier, in int callerClientIdentifier);
	
	/* ************************************************************************
	 * Function for client which sends client data notification
	 */
	int updateClientData(in int dataClientIdentifier, in OverProcessMessage opMsg);
	 
	/* ************************************************************************
	 * Function for client which sends item data notification
	 */
	int updateItemData(in int clientSelf, in int itemIndex, in OverProcessMessage opMsg);
	
	/* ************************************************************************
	 * Function for client which sends a message to another client
	 */
	int postDirectData(in int targetClientIdentifier, in int postClientIdentifier,in OverProcessMessage opMsg);
}
