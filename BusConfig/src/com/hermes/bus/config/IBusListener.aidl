/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config;

import com.hermes.bus.config.communication.OverProcessMessage;

/**
 * This AIDL provide service the ability to "call" connected Application
 * Author: KevinZeng
 */
interface IBusListener {
	/**
	 * service sends the request event to ask client to do something.
	 */
	int requestEvent(int clientId, int requestType, int requestOp);

	/**
	 * service sends the client data notification to client.
	 */
	int clientDataNotify(in int clientSelf, in int clientIdentifier, in OverProcessMessage opMsg);
	
	/**
	 * service sends the item data notification to client.
	 */
	int itemDataNotify(in int clientSelf, in int itemIndex, in OverProcessMessage opMsg);
	
	/**
	 * A client sends a message to another client through service
	 */
	int directDataNotify(in int targetClient, in int postClientIdentifier, in OverProcessMessage opMsg);
	
	/**
	 * A client can call another client through this method directly.
	 */
	OverProcessMessage remoteCall(in int calleeClientIdentifier, in int callerClientIdentifier, in OverProcessMessage opMsg);
}
