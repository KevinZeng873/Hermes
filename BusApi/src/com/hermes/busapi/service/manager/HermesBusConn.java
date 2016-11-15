/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.service.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.hermes.busconfig.IBusConnection;
import com.hermes.busconfig.IBusListener;
import com.hermes.busconfig.communication.OverProcessMessage;
import com.hermes.busconfig.configuration.BusAppItem;
import com.hermes.busconfig.define.IServiceDefinition;
import com.hermes.busconfig.remotecall.RemoteCallConfig;
import com.hermes.busapi.service.IBusClientListener;
import com.hermes.busapi.crash.CrashHandlingManager;
import com.hermes.busapi.log.BusLogger;

/**
 * This class is used to be connected to Hermes BUS through AIDL and so <br>
 * it must be a single instance!!!<br>
 * <p/>
 * Provider features: <br>
 * 1. Bind to Bus Controller through IBusConnection.aidl <br>
 * 2. Add the listener.stub based on IBusListener <br>
 * 3. any functions connection provided, <br>
 *
 * @author KevinZeng
 */
public final class HermesBusConn {
    private static final String TAG = "BUSCONN";

    /* ************************************************************************
     * Members
     */
    private Context mContext = null;

    /**
     * AIDL connection of Hermes Bus
     */
    private IBusConnection mIBusConnection = null;

    /**
     * Point out the connect status to Hermes Bus
     */
    private static boolean mConnected = false;

    /**
     * instance  of connection with hermes bus
     */
    public ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            BusLogger.d(TAG, "onServiceConnected -");
            mIBusConnection = IBusConnection.Stub.asInterface(arg1);
            if (null != mIBusConnection) {
                mConnected = true;
                /*
				 * Succeed to connect on hermes bus, then register client on it.
				 */
                BusLogger.i(TAG,
                        "Register clients on Bus");
                BusAppManager.getIClientSideOp().registerAllClient();

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mConnected = false;
            BusAppManager.getIServiceSideOp().serviceDisconnected();
            BusLogger.i(TAG, "onServiceDisconnected -");
            mIBusConnection = null;
        }

    };

    /* ************************************************************************
     * Single Instance
     */
    private HermesBusConn() {

    }

    private static class HermesBusConnFactory {
        private static HermesBusConn instance = new HermesBusConn();
    }

    public static HermesBusConn getInstance() {
        return HermesBusConnFactory.instance;
    }

    /* ************************************************************************
     * Functions
     */
    public void init(Context context) {
        if (null != mContext) {
            BusLogger.i(TAG, "init - skip.");
            return;
        }
        BusLogger.i(TAG, "init -");
        mContext = context;
		/*
		 * initialization for crash handling process
		 */
        if (null != mContext) {
            BusAppItem appItem = BusAppManager.getIAppSideOp()
                    .getAppIdentifierItem();
            if (appItem.isCrashHandling()) {
                CrashHandlingManager.getInstance().init(
                        mContext.getApplicationContext());
            }
        }
    }

    /**
     * Set a listener of client.<br>
     *
     * @param listener
     */
    public void setLogicListener(IBusClientListener listener) {
        BusAppListener.getInstance().setLogicListner(listener);

    }

    /**
     * Get the listener of connection with hermes bus<br>
     *
     * @return
     */
    public IBusListener.Stub getHermesBusListener() {
        return BusAppListener.getInstance().getIBusListenerStub();

    }

    /**
     * @return
     */
    public boolean isConnectedOnBus() {
        return mConnected;
    }

    /**
     * Connect to hermes bus<br>
     *
     * @return
     */
    public boolean connectService(String appIdentifierName) {
        String appName = mContext.getApplicationContext().getPackageName();
        BusLogger.i(TAG, appName
                + " will connect to hermes bus.");
        Intent intent = new Intent();
        intent.setPackage(IServiceDefinition.BUSCONTROLLER_PACKAGENAME);
        intent.setClassName(IServiceDefinition.BUSCONTROLLER_PACKAGENAME,
                IServiceDefinition.BUSCONTROLLER_SERVICENAME);
        intent = intent.putExtra(
                IServiceDefinition.COREAPPSERVICE_APPLICATION_PACKAGENAME,
                appName);
        intent = intent.putExtra(
                IServiceDefinition.COREAPPSERVICE_APPLICATION_IDENTIFIERNAME,
                appIdentifierName);

        return mContext.getApplicationContext().bindService(intent,
                mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Assign a new client Id for the client.<br>
     *
     * @param identifierName
     * @param clientIdentifier
     * @return
     */
    public int getNewAssignClientId(String identifierName, int clientIdentifier) {
        int clientId = 0;
        try {
            clientId = mIBusConnection.assignNewClientId(mContext
                            .getApplicationContext().getPackageName(), identifierName,
                    clientIdentifier);
            BusLogger.i(TAG, identifierName
                    + " get the assigned client id:" + clientId);
        } catch (RemoteException e) {
            BusLogger.i(TAG, identifierName
                    + " cannot get client id from hermes bus:" + e.toString());
            e.printStackTrace();
        }
        if (0 != clientId) {
            // add listener
            try {
                boolean ret = mIBusConnection.setListener(BusAppManager
                                .getIAppSideOp().getAppIdentifierItem().getName(),
                        clientId, BusAppListener.getInstance()
                                .getIBusListenerStub());
                BusLogger.i(TAG, identifierName
                        + " register listener on hermes bus:" + ret);
            } catch (RemoteException e) {
                BusLogger.i(TAG,
                        identifierName
                                + " failed to register listener on hermes bus:"
                                + e.toString());
                e.printStackTrace();
            }
        }
        return clientId;
    }

    /**
     * @param clientIdentifier
     * @return
     */
    public int registerBusData_Client(int clientSelf, int clientIdentifier) {
        if (null == mIBusConnection) {
            // No connection and we do nothing.
            BusLogger.e(TAG, "registerBusData_Client - no connection");
            return -1;
        }
        BusLogger.d(TAG, "registerBusData_Client - Client[" + clientSelf
                + "] register ClientData[" + clientIdentifier + "]");
        try {
            return mIBusConnection.registerBusData_Client(clientSelf,
                    clientIdentifier);
        } catch (RemoteException e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param clientIdentifier
     * @return
     */
    public int unregisterBusData_Client(int clientSelf, int clientIdentifier) {
        if (null == mIBusConnection) {
            // No connection and we do nothing.
            BusLogger.e(TAG, "unregisterBusData_Client - no connection");
            return -1;
        }
        BusLogger.d(TAG, "unregisterBusData_Client - Client[" + clientSelf
                + "] unregister ClientData[" + clientIdentifier + "]");
        try {
            return mIBusConnection.unregisterBusData_Client(clientSelf,
                    clientIdentifier);
        } catch (RemoteException e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param index
     * @return 0 mean succeed.
     */
    public int registerBusData_Item(int clientSelf, int index) {
        if (null == mIBusConnection) {
            // No connection and we do nothing.
            BusLogger.e(TAG, "registerBusData_Item - no connection");
            return -1;
        }
        BusLogger.d(TAG, "registerBusData_Item - Client[" + clientSelf
                + "] register ItemData[" + index + "]");
        try {
            return mIBusConnection
                    .registerBusData_Item(clientSelf, index);
        } catch (RemoteException e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param index
     * @return 0 means succeed
     */
    public int unregisterBusData_Item(int clientSelf, int index) {
        if (null == mIBusConnection) {
            // No connection and we do nothing.
            BusLogger.e(TAG, "unregisterBusData_Item - no connection");
            return -1;
        }
        BusLogger.d(TAG, "unregisterBusData_Item - Client[" + clientSelf
                + "] unregister ItemData[" + index + "]");
        try {
            return mIBusConnection.unregisterBusData_Item(clientSelf,
                    index);
        } catch (RemoteException e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param clientSelf
     * @param message
     * @return 0 means no error happens
     */
    public int updateBusData_Client(int clientSelf, Message message) {
        if (null == mIBusConnection) {
            // No connection and we do nothing.
            BusLogger.e(TAG, "updateBusData_Client - no connection");
            return -1;
        }
        BusLogger.d(TAG, "updateBusData_Client - Client[" + clientSelf
                + "] send ClientData - what=" + message.what);
        try {
            // transfer local message to remote message
            OverProcessMessage opMsg = OverProcessMessage.CopyObtain(message);
            // send the client data notification to hermes bus
            return mIBusConnection.updateBusData_Client(clientSelf, opMsg);
        } catch (RemoteException e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param itemIndex
     * @param message
     * @return
     */
    public int updateBusData_Item(int clientSelf, int itemIndex, Message message) {
        if (null == mIBusConnection) {
            // No connection and we do nothing.
            BusLogger.e(TAG, "updateBusData_Item - no connection");
            return -1;
        }
        BusLogger.d(TAG, "updateBusData_Item - Client[" + clientSelf + "] send ItemData["
                + itemIndex + "] - what=" + message.what);
        try {
            // transfer local message to remote message
            OverProcessMessage opMsg = OverProcessMessage.CopyObtain(message);
            // send the item data notification to hermes bus
            return mIBusConnection.updateBusData_Item(clientSelf, itemIndex,
                    opMsg);
        } catch (RemoteException e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param targetClientIdentifier
     * @param postClientIdentifier
     * @param message
     * @return
     */
    public int sendBusDirectData(int targetClientIdentifier,
                              int postClientIdentifier, Message message) {
        if (null == mIBusConnection) {
            // No connection and we do nothing.
            BusLogger.e(TAG, "sendBusDirectData - no connection");
            return -1;
        }
        BusLogger.d(TAG, "sendBusDirectData - Client[" + postClientIdentifier
                + "] send to Client[" + targetClientIdentifier
                + "] - what=" + message.what);
        try {
            // transfer local message to remote message
            OverProcessMessage opMsg = OverProcessMessage.CopyObtain(message);
            // send this direct data
            return mIBusConnection.postBusDirectData(targetClientIdentifier,
                    postClientIdentifier, opMsg);
        } catch (RemoteException e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            BusLogger.e(TAG, "Exception:" + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * check if the client is available on hermes bus<br>
     *
     * @param calleeClientIdentifier
     * @return
     */
    public boolean isClientOnBus(int calleeClientIdentifier,
                                     int callerClientIdentifier) {
        try {
			/*
			 * Get the callback of the callee's client from Hermes Bus
			 */
            IBusListener appListener = mIBusConnection
                    .getClientListener(calleeClientIdentifier,
                            callerClientIdentifier);
            if (null == appListener) {
				/*
				 * Return: no client or no listener
				 */
                return false;
            }
            return true;
        } catch (RemoteException e) {
            BusLogger.e(TAG,
                    "isClientOnBus - exception:" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            BusLogger.e(TAG,
                    "isClientOnBus - exception:" + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     *
     * @param calleeClientIdentifier
     * @param callerClientIdentifier
     * @param msgIn
     * @return
     */
    public Message remoteDirectCall(int calleeClientIdentifier,
                                    int callerClientIdentifier, Message msgIn) {
        Message msgReturn = Message.obtain();
        msgReturn.what = RemoteCallConfig.MSGRTN_COMMON_ERR_UNKNOWN;
        if (null == mIBusConnection) {
            BusLogger.e(TAG, "remoteDirectCall - no connection");
			/*
			 * No connection
			 */
            msgReturn.what = RemoteCallConfig.MSGRTN_COMMON_ERR_NO_ACCESS_BUS;
            return msgReturn;
        }

        try {
			/*
			 * Get the callback of the callee's client from hermes bus
			 */
            IBusListener appListener = mIBusConnection
                    .getClientListener(calleeClientIdentifier,
                            callerClientIdentifier);
            if (null == appListener) {
                BusLogger.e(TAG, "remoteDirectCall - call method=null");
				/*
				 * Error: no client or no listener
				 */
                msgReturn.what = RemoteCallConfig.MSGRTN_COMMON_ERR_NO_CLIENT;
                return msgReturn;
            }
			/*
			 * call the callback of the callee's client
			 */
            OverProcessMessage opMsgIn = OverProcessMessage.CopyObtain(msgIn);

            BusLogger.d(TAG, "remoteDirectCall - call callee["
                    + calleeClientIdentifier + "] caller["
                    + callerClientIdentifier + "] what=" + msgIn.what);

            OverProcessMessage opMsgReturn = appListener.remoteCall(
                    calleeClientIdentifier, callerClientIdentifier, opMsgIn);

            BusLogger.d(TAG, "remoteDirectCall - return callee["
                    + calleeClientIdentifier + "] caller["
                    + callerClientIdentifier + "] what=" + opMsgReturn.what);

            Message msgCallReturn = Message.obtain();
            msgCallReturn.what = opMsgReturn.what;
            msgCallReturn.arg1 = opMsgReturn.arg1;
            msgCallReturn.arg2 = opMsgReturn.arg2;
            if (null != opMsgReturn.obj) {
                msgCallReturn.obj = opMsgReturn.obj;
            }
            return msgCallReturn;
        } catch (RemoteException e) {
            msgReturn.what = RemoteCallConfig.MSGRTN_COMMON_ERR_EXCEPTION;
            BusLogger.e(TAG,
                    "remoteDirectCall - exception:" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            msgReturn.what = RemoteCallConfig.MSGRTN_COMMON_ERR_EXCEPTION;
            BusLogger.e(TAG,
                    "remoteDirectCall - exception:" + e.getMessage());
            e.printStackTrace();
        }
        return msgReturn;
    }

    /**
     * disconnect operation. DO not call it!
     */
    @SuppressWarnings("unused")
    private void disconnectService() {
        BusLogger.i(TAG, "disconnect -");
        if (null != mIBusConnection) {
            mContext.getApplicationContext().unbindService(mConnection);
        }
    }

	/* ************************************************************************
	 * Utils
	 */
}
