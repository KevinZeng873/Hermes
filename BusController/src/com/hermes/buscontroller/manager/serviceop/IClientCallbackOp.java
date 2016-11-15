/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.manager.serviceop;

import com.hermes.busconfig.communication.OverProcessMessage;

/**
 * This interface, inherited by ServiceManager, provides the interface
 * for CALLBACK of Bus Client.
 * 
 * @author KevinZeng
 * 
 */
public interface IClientCallbackOp {
	/**
	 * Send this data client notification to the target client.<br>
	 * 
	 * @param client2Sending
	 * @param dataClientIdentifier
	 * @param opMsg
	 * @return
	 */
	public int sendClientDataNotification(int client2Sending,
			int dataClientIdentifier, OverProcessMessage opMsg);

	/**
	 * Send the item data notification to the target client.<br>
	 * 
	 * @param client2Sending
	 * @param itemIndex
	 * @param opMsg
	 * @return
	 */
	public int sendItemDataNotification(int client2Sending, int itemIndex,
			OverProcessMessage opMsg);

	/**
	 * Send a direct data to the target client.<br>
	 * 
	 * @param client2Sending
	 * @param opMsg
	 * @return
	 */
	public int sendDirectData(int client2Sending, int postClientIdentifier,
			OverProcessMessage opMsg);
}
