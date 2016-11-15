package com.hermes.busconfig;

import com.hermes.busconfig.communication.OverProcessMessage;

/**
 * This AIDL provide CoreAppService the ability to "call" connected Application
 * Author: KevinZeng
 */
interface IBusListener {
	/**
	 * Bus sends the request event to ask client to do something.
	 */
	int busRequestEvent(int clientId, int requestType, int requestOp);

	/**
	 * Bus sends the client data notification to client.
	 */
	int busData_Client(in int clientSelf, in int clientIdentifier, in OverProcessMessage opMsg);
	
	/**
	 * Bus sends the item data notification to client.
	 */
	int busData_Item(in int clientSelf, in int itemIndex, in OverProcessMessage opMsg);
	
	/**
	 * A client sends a message to another client through Hermes Bus
	 */
	int busDirectData_Item(in int targetClient, in int postClientIdentifier, in OverProcessMessage opMsg);
	
	/**
	 * A client can call another client through this method directly.
	 */
	OverProcessMessage remoteCall(in int calleeClientIdentifier, in int callerClientIdentifier, in OverProcessMessage opMsg);
}
