/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.busdata;

import java.util.ArrayList;

import com.hermes.buscontroller.manager.ServiceManager;
import com.hermes.busconfig.communication.OverProcessMessage;

/**
 * 
 * @author KevinZeng
 * 
 */
public class BusData_Client {
	/* ************************************************************************
	 * Members
	 */

	/**
	 * They are the clients which is registered to receive the client data<br>
	 * notification.<br>
	 */
	private ArrayList<Integer> mRegisteredClientList = new ArrayList<Integer>();

	/* ************************************************************************
	 * Methods
	 */

	/* ************************************************************************
	 * Functions
	 */
	/**
	 * Constructor
	 */
	public BusData_Client() {
	}

	/**
	 * Add a new client so this client can receive notification when state<br>
	 * updated.<br>
	 * 
	 * @param clientIdentifier
	 */
	public int addRegisteredClient(int clientIdentifier) {
		for (int i = 0; i < mRegisteredClientList.size(); ++i) {
			if (mRegisteredClientList.get(i) == clientIdentifier) {
				// we don't add duplicated item
				return -1;
			}
		}
		mRegisteredClientList.add(clientIdentifier);
		return 0;
	}

	/**
	 * remove the client form listener list<br>
	 * 
	 * @param clientIdentifier
	 */
	public int removeRegisteredClient(int clientIdentifier) {
		for (int i = 0; i < mRegisteredClientList.size(); ++i) {
			if (mRegisteredClientList.get(i) == clientIdentifier) {
				mRegisteredClientList.remove(i);
				return 0;
			}
		}
		return -1;
	}

	/**
	 * Send the inputed client data notification to registered clients.<br>
	 * 
	 * @param dataClientIdentifier
	 * @param opMsg
	 * @return
	 */
	public int sendClientDataNotification(int dataClientIdentifier,
			OverProcessMessage opMsg) {
		for (int i = 0; i < mRegisteredClientList.size(); ++i) {
			ServiceManager.getIClientCallbackOp().sendClientDataNotification(
					mRegisteredClientList.get(i), dataClientIdentifier, opMsg);
		}
		return 0;
	}

	/**
	 * dump instance's information
	 */
	public String getInfo() {
		String log = "  ";
		for (int i = 0; i < mRegisteredClientList.size(); ++i) {
			log += "|" + mRegisteredClientList.get(i);
		}
		return log;
	}
}
