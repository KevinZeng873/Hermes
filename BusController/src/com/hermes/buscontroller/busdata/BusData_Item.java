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
public class BusData_Item {
	private static final String TAG = "IN_Record";
	/* ************************************************************************
	 * Members
	 */
	/**
	 * They are the clients which is registered to receive the item data<br>
	 * notification.<br>
	 */
	private ArrayList<Integer> mRegisteredClientList = new ArrayList<Integer>();

	/* ************************************************************************
	 * Methods
	 */
	public BusData_Item() {

	}

	/* ************************************************************************
	 * Functions
	 */

	/**
	 * add a new registered client on the item data notification.<br>
	 * 
	 * @param clientIdentifier
	 * @return
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
	 * remove the client from the listener list.<br>
	 * 
	 * @param clientIdentifier
	 * @return
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

	public void removeAllRegisteredClient() {
		mRegisteredClientList.clear();
	}

	/**
	 * Send the inputed item data notification to registered clients.<br>
	 * 
	 * @param itemIndex
	 * @param opMsg
	 * @return
	 */
	public int sendItemDataNotifcation(int itemIndex, OverProcessMessage opMsg) {
		for (int i = 0; i < mRegisteredClientList.size(); ++i) {
			ServiceManager.getIClientCallbackOp().sendItemDataNotification(
					mRegisteredClientList.get(i), itemIndex, opMsg);
		}
		return 0;
	}

	/**
	 * dump instance's information
	 */
	public String getInfo() {
		String log = " ";
		for (int i = 0; i < mRegisteredClientList.size(); ++i) {
			log += "|" + mRegisteredClientList.get(i);
		}
		return log;
	}

}
