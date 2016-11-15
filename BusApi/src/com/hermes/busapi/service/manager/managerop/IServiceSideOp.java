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

/**
 * Call features of BusAppManager from Hermes bus side
 * 
 * @author KevinZeng
 * 
 */
public interface IServiceSideOp {
	/**
	 * Bus Controller sends this request<br>
	 * 
	 * @param clientId
	 * @param requestType
	 * @param requestOp
	 * @return
	 */
	public int requestEvent(int clientId, int requestType, int requestOp);


	/**
	 *
	 * @param clientSelfIdentifier
	 * @param clientIdentifier
	 * @param msg
     * @return
     */
	public int onBusData_Client(int clientSelfIdentifier, int clientIdentifier,
			Message msg);

	/**
	 * @param itemIndex
	 * @param msg
	 * @return
	 */
	public int onBusData_Item(int clientSelfIdentifier, int itemIndex,
			Message msg);

	/**
	 * @param targetClientIdentifier
	 * @param postClientIdentifier
	 * @param msg
	 * @return
	 */
	public int onBusDirectData(int targetClientIdentifier,
			int postClientIdentifier, Message msg);

	/**
	 * when the connection is disconnected, tell logic.
	 */
	public void serviceDisconnected();

}
