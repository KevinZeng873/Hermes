/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.manager.serviceop;

import com.hermes.busconfig.IBusListener;
import com.hermes.busconfig.communication.OverProcessMessage;


/**
 * This interface, inherited by ServiceManager, provides the <br>
 * operation interface for connection of hermes bus.<br>
 * 
 * @author KevinZeng
 * 
 */
public interface IConnectionOp {
	/**
	 * 
	 * @param packageName
	 * @param clientIdentifier
	 * @return 0 is an invalid id
	 */
	public int assignNewClientId(String packageName, int clientIdentifier);

	/**
	 * 
	 * @param appId
	 * @param listener
	 * @return
	 */
	public boolean setClientListener(String identifierName, int appId,
			IBusListener listener);

	/**
	 * client subscribes BusData_Client
	 * 
	 * @param clientSelf
	 * @param clientIdentifier
	 * @return
	 */
	public int registerClientDataNotify(int clientSelf, int clientIdentifier);

	/**
	 * Client un-subscribes BusData_Client
	 * 
	 * @param clientSelf
	 * @param clientIdentifier
	 * @return
	 */
	public int unregisterClientDataNotify(int clientSelf, int clientIdentifier);

	/**
	 * client subscribes BusData_Item
	 * 
	 * @param clientSelf
	 * @param itemIndex
	 * @return
	 */
	public int registerItemDataNotify(int clientSelf, int itemIndex);

	/**
	 * client un-subscribes BusData_Item
	 * 
	 * @param clientSelf
	 * @param itemIndex
	 * @return
	 */
	public int unregisterItemDataNotify(int clientSelf, int itemIndex);

	/**
	 * A client sends its BusData_Client to hermes bus<br>
	 *
	 * @param dataClientIdentifier
	 * @param opMsg
     * @return
     */
	public int updateClientData(int dataClientIdentifier,
			OverProcessMessage opMsg);

	/**
	 * client sends its BusData_Item to hermes bus
	 * 
	 * @param itemIndex
	 * @param opMsg
	 * @return
	 */
	public int updateItemData(int itemIndex, OverProcessMessage opMsg);

	/**
	 * A client sends direct data<br>
	 * 
	 * @param targetClientIdentifier
	 * @param postClientIdentifier
	 * @param opMsg
	 * @return
	 */
	public int postDirectData(int targetClientIdentifier,
			int postClientIdentifier, OverProcessMessage opMsg);

	/**
	 * Return a client's listener<br>
	 * 
	 * @param clientIdentifier
	 * @return
	 */
	public IBusListener getClientAppListener(int clientIdentifier);
}
