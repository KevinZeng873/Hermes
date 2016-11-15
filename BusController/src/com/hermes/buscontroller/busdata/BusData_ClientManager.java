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
import com.hermes.busconfig.configuration.BusClientItem;

/**
 * After receiving updated information, this class manage to <br>
 * send the updated information to required client.<br>
 * Single Instance<br>
 *
 * @author KevinZeng
 */
public class BusData_ClientManager {
    private static final String TAG = "CN_M";
    /* ************************************************************************
     * Constants
     */
    private static final int FIX_NUMBER_OF_THREAD = 10;

    /* ************************************************************************
     * Members
     */
    private Map<Integer, BusData_Client> mClientNotifyMap = null;

    /**
     * Thread pool to handle the update client data
     */
    private ExecutorService mThreadPoolExecutor = Executors
            .newFixedThreadPool(BusData_ClientManager.FIX_NUMBER_OF_THREAD);

    /* ************************************************************************
     * Single Instance
     */
    private static class ClientNotifyManagerFactory {
        private static BusData_ClientManager instance = new BusData_ClientManager();
    }

    public static BusData_ClientManager getInstance() {
        return ClientNotifyManagerFactory.instance;
    }

	/* ************************************************************************
     * Methods
	 */

    /**
     * Constructor
     */
    private BusData_ClientManager() {
        init();
    }

    /**
     * Do things<br>
     * 1. Load all clients<br>
     */
    private void init() {
        mClientNotifyMap = new HashMap<Integer, BusData_Client>();
        BusClientItem[] clientItemList = BusConfigManager.getConfigClientList();
        for (int i = 0; i < clientItemList.length; ++i) {
            mClientNotifyMap.put(clientItemList[i].getIdentifier(),
                    new BusData_Client());
        }
        dumpTable();
    }

    private void dumpTable() {
        LogicLogger.i(TAG,
                "===================BUSDATA_CLIENT TABLE======================");
        Iterator iter = mClientNotifyMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, BusData_Client> entry = (Map.Entry<Integer, BusData_Client>) iter
                    .next();
            Integer clientSelf = entry.getKey();
            BusData_Client value = entry.getValue();
            LogicLogger.i(TAG, clientSelf + " " + value.getInfo());
        }
        LogicLogger.i(TAG,
                "-------------------BUSDATA_CLIENT TABLE----------------------");
    }

	/* ************************************************************************
	 * Functions
	 */

    /**
     * Add a request client which want to get the pointed client data<br>
     * notification.<br>
     *
     * @param clientSelf
     * @param clientIdentifier
     * @return 0 means no error happens
     */
    public int addRequestClient(int clientSelf, int clientIdentifier) {
        // find the client data notification
        BusData_Client record = mClientNotifyMap.get(clientIdentifier);
        if (null == record) {
            LogicLogger.e(TAG, "NO BUSDATA_CLIENT - " + clientIdentifier);
            return -1;
        }
        // add the client to the record of client data notification
        record.addRegisteredClient(clientSelf);
        LogicLogger.d(TAG, "ADD BUSDATA_CLIENT[" + clientIdentifier + "] - "
                + clientSelf);
        return 0;
    }

    /**
     * Remove a request client which doesn't want to get the pointed client<br>
     * data notification any more.<br>
     *
     * @param clientSelf
     * @param clientIdentifier
     * @return 0 means no error happens
     */
    public int removeRequestClient(int clientSelf, int clientIdentifier) {
        // find the client data notification
        BusData_Client record = mClientNotifyMap.get(clientIdentifier);
        if (null == record) {
            LogicLogger.e(TAG, "NO BUSDATA_CLIENT - " + clientIdentifier);
            return -1;
        }
        // remove the client from the record of client data notification
        record.removeRegisteredClient(clientSelf);
        LogicLogger.d(TAG, "REMOVE BUSDATA_CLIENT[" + clientIdentifier + "] - "
                + clientSelf);
        return 0;
    }

    /**
     * When bus controller receives BusData_Client from a client,<br>
     * BusData_ClientManager handles this message and then sends it to clients<br>
     * which subscribes this BusData_Client.<br>
     *
     * @param dataClientIdentifier
     * @param opMsg
     * @return 0 means no error happens
     */
    public int handleUpdatedClientData(int dataClientIdentifier,
                                       OverProcessMessage opMsg) {
        if (!mThreadPoolExecutor.isShutdown()) {
            ClientDataCallable updateCallable = new ClientDataCallable(
                    dataClientIdentifier, opMsg);
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
     * Thread to deal with a update BusData_Client.<br>
     *
     * @author KevinZeng
     */
    private class ClientDataCallable implements Callable<Integer> {

        private int mDataClientIdentifier = 0;
        private OverProcessMessage mOpMsg = null;

        public ClientDataCallable(int dataClientIdentifier,
                                  OverProcessMessage opMsg) {
            mDataClientIdentifier = dataClientIdentifier;
            mOpMsg = opMsg;
        }

        @Override
        public Integer call() throws Exception {
            // find the identified client
            BusData_Client record = mClientNotifyMap
                    .get(mDataClientIdentifier);
            if (null == record) {
                LogicLogger.e(TAG,
                        "new BUSDATA_Client comes, but NO it in System"
                                + mDataClientIdentifier);
                return -1;
            }
            // send message to clients which request to get this client's data
            // notification.
            return record.sendClientDataNotification(mDataClientIdentifier,
                    mOpMsg);
        }

    }

}
