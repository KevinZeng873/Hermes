/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.service.manager;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import android.os.Message;

import com.hermes.busapi.log.BusLogger;
import com.hermes.busapi.service.IBusClientListener;
import com.hermes.busconfig.define.IServiceDefinition.ICoreRequestEventType;
import com.hermes.busconfig.define.IServiceDefinition.ICoreRequestReturnValue;
import com.hermes.busconfig.configuration.BusAppItem;
import com.hermes.busapi.service.manager.managerop.IAppSideOp;
import com.hermes.busapi.service.manager.managerop.IClientSideOp;
import com.hermes.busapi.service.manager.managerop.IOutClientSideOp;
import com.hermes.busapi.service.manager.managerop.IServiceSideOp;

/**
 * Proxy class
 *
 * @author KevinZeng
 */
public final class BusAppManager {
    private static final String TAG = "BUSAPP";
    /* ************************************************************************
     * Constants
     */
    private static final int FIX_NUMBER_OF_THREAD = 10;

	/* ************************************************************************
     * Members
	 */
    /**
     * thread pool
     */
    private ExecutorService mThreadPoolExecutor = Executors
            .newFixedThreadPool(BusAppManager.FIX_NUMBER_OF_THREAD);

    /**
     * store local clients
     */
    private ArrayList<BusClient> mClientList = new ArrayList<BusClient>();
    private BusAppItem mBusAppItem;

    /**
     * instance for service operation
     */
    private ServiceSideOpHelper mServiceSideOpHelper = null;
    /**
     * instance for application operation
     */
    private AppSideOpHelper mAppSideOpHelper = null;
    /**
     * instance for out client operation
     */
    private OutClientSideOpHelper mOutClientSideOpHelper = null;
    /**
     * instance for client operation
     */
    private ClientSideOpHelper mClientSideOpHelper = null;


    /* ************************************************************************
     * Single Instance
     */
    private BusAppManager() {
        mServiceSideOpHelper = new ServiceSideOpHelper();
        mClientSideOpHelper = new ClientSideOpHelper();
        mAppSideOpHelper = new AppSideOpHelper();
        mOutClientSideOpHelper = new OutClientSideOpHelper();
    }

    private static class BusAppManagerFactory {
        private static BusAppManager instance = new BusAppManager();
    }

	/* ************************************************************************
     * Functions
	 */

    /**
     * Return the operation interface for client
     *
     * @return
     */
    public static IClientSideOp getIClientSideOp() {
        return BusAppManagerFactory.instance.mClientSideOpHelper;
    }

    /**
     */
    static IServiceSideOp getIServiceSideOp() {
        return BusAppManagerFactory.instance.mServiceSideOpHelper;
    }

    /**
     * Return the operation interface for app
     *
     * @return
     */
    public static IAppSideOp getIAppSideOp() {
        return BusAppManagerFactory.instance.mAppSideOpHelper;
    }

    public static IOutClientSideOp getIOutClientSideOp() {
        return BusAppManagerFactory.instance.mOutClientSideOpHelper;
    }

	/* ************************************************************************
	 * Methods
	 */

    /**
     * @param clientId
     * @return
     */
    private BusClient findClientById(int clientId) {
        for (int i = 0; i < mClientList.size(); ++i) {
            if (clientId == mClientList.get(i).getClientId()) {
                return mClientList.get(i);
            }
        }
        return null;
    }

    /**
     * @param clientIdentifier
     * @return
     */
    private BusClient findClientByIdentifier(int clientIdentifier) {
        for (int i = 0; i < mClientList.size(); ++i) {
            if (clientIdentifier == mClientList.get(i).getIdentifier()) {
                return mClientList.get(i);
            }
        }
        return null;
    }

    /**
     * Add a client into list. If it's existed, do nothing.
     *
     * @param clientIdentifier
     * @param listener
     */
    private synchronized void addNewClient(int clientIdentifier,
                                           IBusClientListener listener) {
        // If this client is existed, return with nothing doing.
        for (int i = 0; i < mClientList.size(); ++i) {
            if (mClientList.get(i).getIdentifier() == clientIdentifier) {
                BusLogger.d(TAG, "addNewClient - " + clientIdentifier
                        + " is existed! Nothing need doing.");
                return;
            }
        }
        // add this client into list
        mClientList.add(new BusClient(clientIdentifier, listener));
    }

	/* ************************************************************************
	 * Internal Class
	 */

    /**
     * ClientData record<br>
     *
     * @author KevinZeng
     */
    private class BusClient {
        private int mClientIdentifier = 0;
        private int mClientId = 0;
        /**
         * listener is compulsory
         */
        private IBusClientListener mICoreClientLogicListener = null;

        /**
         * Make constructor private!
         */
        private BusClient() {

        }

        /**
         * @param clientIdentifier
         * @param listener
         */
        public BusClient(int clientIdentifier, IBusClientListener listener) {
            mClientIdentifier = clientIdentifier;
            mClientId = 0;
            mICoreClientLogicListener = listener;
        }

        public IBusClientListener getICoreClientLogicListener() {
            return mICoreClientLogicListener;
        }

        public void setICoreClientLogicListener(IBusClientListener listener) {
            mICoreClientLogicListener = listener;
        }

        public int getIdentifier() {
            return mClientIdentifier;
        }

        public void setIdentifier(int clientIdentifier) {
            mClientIdentifier = clientIdentifier;
        }

        public int getClientId() {
            return mClientId;
        }

        public void setClientId(int clientId) {
            mClientId = clientId;
        }

        public boolean isClientIdValid() {
            return mClientId != 0;
        }
    }

    /**
     * Helper class for out client operation<br>
     *
     * @author KevinZeng
     */
    private class OutClientSideOpHelper implements IOutClientSideOp {

        @Override
        public Message remoteDirectCall(int calleeClientIdentifier,
                                        int callerClientIdentifier, Message msgIn) {
            BusLogger.d(TAG, "remoteDirectCall - callee clientIdentifier["
                    + calleeClientIdentifier + "] - what" + msgIn.what);
            BusClient clientData = findClientByIdentifier(calleeClientIdentifier);
            if (null != clientData) {

                IBusClientListener clientLogicListener = clientData
                        .getICoreClientLogicListener();
				/*
				 * reserved parameter
				 */
                msgIn.arg2 = callerClientIdentifier;

                //
                if (null != clientLogicListener) {
                    return clientLogicListener.remoteCall(msgIn);
                } else {
                    BusLogger.d(TAG,
                            "remoteDirectCall - no callback of caller client found!");
                }
            } else {
                BusLogger.d(TAG, "remoteDirectCall - no client found");
            }
            return null;
        }

    }

    /**
     * Helper class for service operation.<br>
     *
     * @author KevinZeng
     */
    private class ServiceSideOpHelper implements IServiceSideOp {
		/* ********************************************************************
		 * IServiceSideOp Implmentation
		 */

        /**
         * Hermes Bus is the caller, client is the callee.<br>
         * <SYNCHRONIZED OPERATION><br>
         */
        @Override
        public int requestEvent(int clientId, int requestType, int requestOp) {
            BusLogger.d(TAG, "<Cb>requestEvent - clientId[" + clientId
                    + "] type:" + requestType + " Op:" + requestOp);
            BusClient clientData = findClientById(clientId);
            if (null != clientData) {
                IBusClientListener clientLogicListener = clientData
                        .getICoreClientLogicListener();
                if (null != clientLogicListener) {
                    switch (requestType) {
                        case ICoreRequestEventType.REQUESTTYPE_FUNCTION:
                            return clientLogicListener.requestFunction(requestOp);
                        case ICoreRequestEventType.REQUESTTYPE_GETINFO:
                            return clientLogicListener.requestGetInfo(requestOp);
                        case ICoreRequestEventType.REQUESTTYPE_CLIENT:
                            return clientLogicListener.requestClient(requestOp);
                    }
                } else {
                    BusLogger.e(TAG,
                            "requestEvent - no client's listener found");
                }
            } else {
                BusLogger.e(TAG, "requestEvent - no client found");
            }
            return ICoreRequestReturnValue.REQUESTOP_RETURN_UNDEFINED;
        }

        @Override
        public void serviceDisconnected() {
            BusLogger.d(TAG, "<Cb>serviceDisconnected -");
            IBusClientListener clientLogicListener = null;
            for (int i = 0; i < mClientList.size(); ++i) {
                mClientList.get(i).setClientId(0);
				/*
				 * tell client "disconnect" status
				 */
                clientLogicListener = mClientList.get(i)
                        .getICoreClientLogicListener();
                if (null != clientLogicListener) {
                    mClientList.get(i).getICoreClientLogicListener()
                            .disconnectedFromBus();
                }
            }
        }

        @Override
        public int onBusData_Client(int clientSelfIdentifier,
                                    int dataClientIdentifier, Message msg) {
            if (!mThreadPoolExecutor.isShutdown()) {
                ClientDataCallable clientCallable = new ClientDataCallable(
                        clientSelfIdentifier, dataClientIdentifier, msg);
                FutureTask<Integer> callableTask = new FutureTask<Integer>(
                        clientCallable);
                mThreadPoolExecutor.submit(callableTask);
            } else {
                BusLogger.e(TAG,
                        "<Cb>onBusData_Client - thread pool is shutdown!");
            }
            return ICoreRequestReturnValue.REQUESTOP_RETURN_YES;
        }

        @Override
        public int onBusData_Item(int clientSelfIdentifier, int itemIndex,
                                  Message msg) {
            if (!mThreadPoolExecutor.isShutdown()) {
                ItemDataCallable itemCallable = new ItemDataCallable(
                        clientSelfIdentifier, itemIndex, msg);
                FutureTask<Integer> callableTask = new FutureTask<Integer>(
                        itemCallable);
                mThreadPoolExecutor.submit(callableTask);
            } else {
                BusLogger.e(TAG,
                        "<Cb>onBusData_Item - thread pool is shutdown!");
            }

            return ICoreRequestReturnValue.REQUESTOP_RETURN_YES;
        }

        @Override
        public int onBusDirectData(int targetClientIdentifier,
                                   int postClientIdentifier, Message msg) {
            if (!mThreadPoolExecutor.isShutdown()) {
                DirectDataCallable directCallable = new DirectDataCallable(
                        targetClientIdentifier, postClientIdentifier, msg);
                FutureTask<Integer> callableTask = new FutureTask<Integer>(
                        directCallable);
                mThreadPoolExecutor.submit(callableTask);
            } else {
                BusLogger.e(TAG,
                        "<Cb>onBusDirectData - thread pool is shutdown!");
            }
            return ICoreRequestReturnValue.REQUESTOP_RETURN_YES;
        }

    }

	/* ****************************************************************************
	 * Internal Classes
	 */

    /**
     * Thread for received BusDirectData
     *
     * @author KevinZeng
     */
    private class DirectDataCallable implements Callable<Integer> {

        private int mTargetClientIdentifier = 0;
        private int mPostClientIdentifier = 0;
        private Message mMsg = null;

        public DirectDataCallable(int targetClientIdentifier,
                                  int postClientIdentifier, Message msg) {
            mTargetClientIdentifier = targetClientIdentifier;
            mPostClientIdentifier = postClientIdentifier;
            mMsg = msg;
        }

        @Override
        public Integer call() throws Exception {
            // find the local client and call its callback
            BusClient clientData = findClientByIdentifier(mTargetClientIdentifier);
            if (null != clientData) {
                BusLogger.d(TAG,
                        "<Cb>Callable[DirectData] target clientid["
                                + clientData.getClientId() + "] - what="
                                + mMsg.what);
                IBusClientListener clientLogicListener = clientData
                        .getICoreClientLogicListener();
                if (null != clientLogicListener) {
                    clientLogicListener.onReceiveDirectData(mPostClientIdentifier,
                            mMsg);
                }
            }
            return 0;
        }

    }

    /**
     * Thread for received BusData_Item<br>
     *
     * @author KevinZeng
     */
    private class ItemDataCallable implements Callable<Integer> {
        private int mTargetClientIdentifier = 0;
        private int mItemIndex = 0;
        private Message mMsg = null;

        public ItemDataCallable(int targetClientIdentifier, int itemIndex,
                                Message msg) {
            mTargetClientIdentifier = targetClientIdentifier;
            mItemIndex = itemIndex;
            mMsg = msg;
        }

        @Override
        public Integer call() throws Exception {
            // find the local client and call its callback
            BusClient clientData = findClientByIdentifier(mTargetClientIdentifier);
            if (null != clientData) {
                BusLogger.d(TAG, "<Cb>Callable[ItemData] target clientid["
                        + clientData.getClientId() + "] - what=" + mMsg.what);
                IBusClientListener clientLogicListener = clientData
                        .getICoreClientLogicListener();
                if (null != clientLogicListener) {
                    clientLogicListener.onReceiveBusData_Item(mItemIndex, mMsg);
                }
            }
            return 0;
        }

    }

    /**
     * Thread for received BusData_Client<br>
     *
     * @author KevinZeng
     */
    private class ClientDataCallable implements Callable<Integer> {
        private int mTargetClientIdentifier = 0;
        private int mDataClientIdentifier = 0;
        private Message mMsg = null;

        public ClientDataCallable(int targetClientIdentifier,
                                  int dataClientIdentifier, Message msg) {
            mTargetClientIdentifier = targetClientIdentifier;
            mDataClientIdentifier = dataClientIdentifier;
            mMsg = msg;
        }

        @Override
        public Integer call() throws Exception {
            // find the local client and call its callback
            BusClient clientData = findClientByIdentifier(mTargetClientIdentifier);
            if (null != clientData) {
                BusLogger.d(TAG,
                        "<Cb>Callable[ClientData] target clientid["
                                + clientData.getClientId() + "] - what="
                                + mMsg.what);
                IBusClientListener clientLogicListener = clientData
                        .getICoreClientLogicListener();
                if (null != clientLogicListener) {
                    clientLogicListener.onReceiveBusData_Client(mDataClientIdentifier,
                            mMsg);
                }
            }
            return 0;
        }

    }

    /**
     * Helper class for client operations<br>
     *
     * @author KevinZeng
     */
    private class ClientSideOpHelper implements IClientSideOp {
		/* ************************************************************************
		 * IClientSideOp Implementation
		 */

        /**
         * @param clientIdentifier
         * @param listener
         */
        @Override
        public void registerClient(int clientIdentifier,
                                   IBusClientListener listener) {
            BusLogger.d(TAG, "registerClient - clientIdentifier:"
                    + clientIdentifier);
            addNewClient(clientIdentifier, listener);
            if (HermesBusConn.getInstance().isConnectedOnBus()) {
                registerAllClient();
            } else {
                // firstly, connect to hermes bus.
                HermesBusConn.getInstance().connectService(
                        mBusAppItem.getName());
            }
        }

        /**
         * look over the whole list and if we find someone which has an invalid
         * clientId, we add it to hermes bus.
         */
        @Override
        public void registerAllClient() {
            synchronized (mClientList) {
                for (int i = 0; i < mClientList.size(); ++i) {
                    if (!mClientList.get(i).isClientIdValid()) {
                        // add this client to hermes bus
                        BusLogger.d(TAG, "ask to connect to service - "
                                + mClientList.get(i).getIdentifier());
                        int clientId = HermesBusConn.getInstance()
                                .getNewAssignClientId(mBusAppItem.getName(),
                                        mClientList.get(i).getIdentifier());
                        mClientList.get(i).setClientId(clientId);
                        // tell client "connect" status
                        mClientList.get(i).getICoreClientLogicListener().connectedToBus();
                    }
                }
            }
        }

        @Override
        public int registerBusData_Client(int clientSelf, int clientIdentifier) {
            // TODO Auto-generated method stub
            return HermesBusConn.getInstance().registerBusData_Client(
                    clientSelf, clientIdentifier);
        }

        @Override
        public int unregisterBusData_Client(int clientSelf,
                                            int clientIdentifier) {
            // TODO Auto-generated method stub
            return HermesBusConn.getInstance()
                    .unregisterBusData_Client(clientSelf, clientIdentifier);
        }

        @Override
        public int registerBusData_Item(int clientSelf, int index) {
            // TODO Auto-generated method stub
            return HermesBusConn.getInstance().registerBusData_Item(
                    clientSelf, index);
        }

        @Override
        public int unregisterBusData_Item(int clientSelf, int index) {
            // TODO Auto-generated method stub
            return HermesBusConn.getInstance().unregisterBusData_Item(
                    clientSelf, index);
        }

        @Override
        public int updateBusData_Client(int clientSelf, Message message) {
            // TODO Auto-generated method stub
            return HermesBusConn.getInstance().updateBusData_Client(
                    clientSelf, message);
        }

        @Override
        public int updateBusData_Item(int clientSelf, int itemIndex, Message message) {
            // TODO Auto-generated method stub
            return HermesBusConn.getInstance().updateBusData_Item(
                    clientSelf, itemIndex, message);
        }

        @Override
        public int sendBusDirectData(int targetClientIdentifier,
                                     int postClientIdentifier, Message msg) {
            // TODO Auto-generated method stub
            return HermesBusConn.getInstance().sendBusDirectData(
                    targetClientIdentifier, postClientIdentifier, msg);
        }

        @Override
        public Message remoteDirectCall(int calleeClientIdentifier,
                                        int callerClientIdentifier, Message msgIn) {
            return HermesBusConn.getInstance().remoteDirectCall(
                    calleeClientIdentifier, callerClientIdentifier, msgIn);
        }

        @Override
        public boolean isClientOnBus(int calleeClientIdentifier,
                                     int callerClientIdentifier) {
            return HermesBusConn.getInstance().isClientOnBus(
                    calleeClientIdentifier, callerClientIdentifier);
        }

    }

    /**
     * Helper class for application operation<br>
     *
     * @author KevinZeng
     */
    private class AppSideOpHelper implements IAppSideOp {
		/* ************************************************************************
		 * IAppSideOp Implementation
		 */

        @Override
        public void setAppIdentifierItem(BusAppItem appItem) {
            mBusAppItem = appItem;
			/*
			 * initialize settings
			 */
            BusLogger.enableLogd(mBusAppItem.isLogdEnable());
            BusLogger.enableLogi(mBusAppItem.isLogiEnable());
            BusLogger.enableLogw(mBusAppItem.isLogwEnable());
        }

        @Override
        public BusAppItem getAppIdentifierItem() {
            // TODO Auto-generated method stub
            return mBusAppItem;
        }

    }

}
