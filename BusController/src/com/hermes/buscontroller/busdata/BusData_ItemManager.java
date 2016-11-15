/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.busdata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.hermes.buscontroller.log.LogicLogger;
import com.hermes.busconfig.communication.OverProcessMessage;
import com.hermes.busconfig.configuration.BusConfigManager;

/**
 * This is a single instance manager to handle BusData_Item<br>
 * 
 * @author KevinZeng
 * 
 */
public class BusData_ItemManager {
	private static final String TAG = "IN_M";

	/* ************************************************************************
	 * Constants
	 */
	private static final int FIX_NUMBER_OF_THREAD = 15;

	/* ************************************************************************
	 * Members
	 */
	private Map<Integer, BusData_Item> mItemNotifyMap = null;

	private ExecutorService mThreadPoolExecutor = Executors
			.newFixedThreadPool(BusData_ItemManager.FIX_NUMBER_OF_THREAD);

	/* ************************************************************************
	 * Single Instance
	 */
	private static class ItemNotifyManagerFactory {
		private static BusData_ItemManager instance = new BusData_ItemManager();
	}

	public static BusData_ItemManager getInstance() {
		return ItemNotifyManagerFactory.instance;
	}

	/* ************************************************************************
	 * Methods
	 */
	private BusData_ItemManager() {
		init();
	}

	/**
	 * Do things<br>
	 * 1. load all type items<br>
	 */
	private void init() {
		mItemNotifyMap = new HashMap<Integer, BusData_Item>();
		com.hermes.busconfig.configuration.BusData_Item[] itemDataNotifys = BusConfigManager
				.getConfigItemDataNotifyItems();
		for (int i = 0; i < itemDataNotifys.length; ++i) {
			mItemNotifyMap.put(itemDataNotifys[i].getIndex(),
					new BusData_Item());
		}
		dumpTable();
	}

	private void dumpTable() {
		LogicLogger.i(TAG,
				"===================BusData_Item Table======================");
		Iterator iter = mItemNotifyMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, BusData_Item> entry = (Map.Entry<Integer, BusData_Item>) iter
					.next();
			Integer key = entry.getKey();
			BusData_Item value = entry.getValue();
			LogicLogger.i(TAG, key + "  " + value.getInfo());
		}
		LogicLogger.i(TAG,
				"------------------BusData_Item Table----------------------");
	}

	/* ************************************************************************
	 * Functions
	 */
	/**
	 * Add a client which want to get pointed item's notification.<br>
	 * 
	 * @param clientSelf
	 * @param itemIndex
	 * @return
	 */
	public int addRequestClient(int clientSelf, int itemIndex) {
		// find the item and add the client into list
		BusData_Item record = mItemNotifyMap.get(itemIndex);
		if (null == record) {
			LogicLogger.e(TAG, "No BusData_Item - " + itemIndex);
			return -1;
		}
		record.addRegisteredClient(clientSelf);
		LogicLogger.d(TAG, "Add BusData_Item - client:" + clientSelf + "  item:"
				+ itemIndex);
		return 0;
	}

	/**
	 * Remove a client which receive the item's notification.<br>
	 * 
	 * @param clientSelf
	 * @param itemIndex
	 * @return
	 */
	public int removeRequestClient(int clientSelf, int itemIndex) {
		// find the item and remove the client from list
		BusData_Item record = mItemNotifyMap.get(itemIndex);
		if (null == record) {
			LogicLogger.i(TAG, "No BusData_Item - " + itemIndex);
			return -1;
		}
		record.removeRegisteredClient(clientSelf);
		LogicLogger.d(TAG, "Remove BusData_Item - client:" + clientSelf
				+ "  item:" + itemIndex);
		return 0;
	}

	/**
	 * When bus controller receives the item data notification,<br>
	 * BusData_ItemManager handles this message and then sends it<br>
	 * to clients which request to get the kind of item data <br>
	 * notification.<br>
	 * 
	 * @param itemIndex
	 * @param opMsg
	 * @return
	 */
	public int handleUpdatedItemData(int itemIndex, OverProcessMessage opMsg) {
		if (!mThreadPoolExecutor.isShutdown()) {
			ItemDataCallable updateCallable = new ItemDataCallable(itemIndex,
					opMsg);
			FutureTask<Integer> callableTask = new FutureTask<Integer>(
					updateCallable);
			mThreadPoolExecutor.submit(callableTask);
		}

		return 0;
	}

	/* ************************************************************************
	 * Internal Classes
	 */

	/**
	 * Thread to deal with a update item data<br>
	 * 
	 * @author KevinZeng
	 * 
	 */
	private class ItemDataCallable implements Callable<Integer> {

		private int mItemIndex = 0;
		private OverProcessMessage mOpMsg = null;

		public ItemDataCallable(int itemIndex, OverProcessMessage opMsg) {
			mItemIndex = itemIndex;
			mOpMsg = opMsg;
		}

		@Override
		public Integer call() throws Exception {
			// find the identified item
			BusData_Item record = mItemNotifyMap.get(mItemIndex);
			if (null == record) {
				LogicLogger.e(TAG,
						"BusData_Item comes, but no it in System - "
								+ mItemIndex);
				return -1;
			}
			// send the notification to clients
			return record.sendItemDataNotifcation(mItemIndex, mOpMsg);
		}

	}
}
