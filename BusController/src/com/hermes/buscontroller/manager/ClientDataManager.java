/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.manager;

import java.util.ArrayList;

import android.content.Context;
import android.os.RemoteException;

import com.hermes.buscontroller.busutil.BusClientWakeuper;
import com.hermes.buscontroller.log.LogicLogger;
import com.hermes.busconfig.IBusListener;
import com.hermes.busconfig.communication.OverProcessMessage;
import com.hermes.busconfig.configuration.BusConfigManager;
import com.hermes.busconfig.configuration.BusClientItem;
import com.hermes.busconfig.define.IServiceDefinition.ICoreRequestReturnValue;

/**
 * manager all data for clients of applications
 *
 * @author KevinZeng
 */
public class ClientDataManager {
    private static final String TAG = "M_CD";

    private static final int APPID_BASE_INDEX = 1001;

    /* ************************************************************************
     * Members
     */
    private ArrayList<ClientData> mClientList = new ArrayList<ClientData>();

    /* ************************************************************************
     * Methods
     */
    public ClientDataManager() {

    }

    /* ************************************************************************
     * APPID Management
     */
    private int mNewAppId = ClientDataManager.APPID_BASE_INDEX;

    private int createOneNewClientId() {
        return mNewAppId++;
    }

	/* ************************************************************************
     * Client Management Data
	 */

    /**
     * Actually we don't remove this item from list. we do things:<br>
     * 1. Clear all info of this item except of PackageName
     *
     * @param packageName
     */
    public void removeClientBasedOnPackageName(String packageName) {
        // LogicLogger.i(TAG,
        // "removeClient - remove all client which are belonged to "
        // + packageName);
        for (int i = 0; i < mClientList.size(); ++i) {
            if (mClientList.get(i).getPackageName()
                    .equalsIgnoreCase(packageName)) {
                mClientList.get(i).setDead();
                LogicLogger.i(TAG, "removeClient - put down  " + packageName
                        + " : " + mClientList.get(i).getClientItem().getName());
            }
        }
        // LogicLogger.e(TAG, "removeClient - cannot find " + packageName);
    }

    /**
     * get a client ID for the client
     *
     * @param packageName
     * @return 0 means failed
     */
    public int assignNewClientId(String packageName, int clientIdentifier) {
        /**
         * find the item in list based on package name<br>
         * If found, create a new appid for this item.
         */
        for (int i = 0; i < mClientList.size(); ++i) {
            if (mClientList.get(i).getClientItem().getIdentifier() == clientIdentifier) {
                if (!mClientList.get(i).isDead()) {
                    LogicLogger.e(TAG,
                            "assignNewClientId - don't ask to new appid again."
                                    + mClientList.get(i).getClientItem()
                                    .getName());
                    /**
                     * Dead can not speak!
                     */
                    return 0;
                }
                int newID = createOneNewClientId();
                LogicLogger.i(TAG, "assignNewClientId - assign a new appid:"
                        + newID + " for "
                        + mClientList.get(i).getClientItem().getName());
                mClientList.get(i).setClientId(newID);
                return newID;
            }
        }
        // LogicLogger.e(TAG, "assignNewClientId - cannot find :" + clientName
        // + " Are you sure that you have binded me?");
        BusClientItem[] items = BusConfigManager.getConfigClientList();
        for (int j = 0; j < items.length; ++j) {
            if (items[j].getIdentifier() == clientIdentifier) {
                int newId = createOneNewClientId();
                LogicLogger.i(TAG, "assignNewClientId - assign a new appid:"
                        + newId + " for client " + clientIdentifier);
                mClientList.add(new ClientData(packageName, items[j], newId));
                return newId;
            }
        }
        return 0;
    }

    /**
     * Add a listener(callback) to the pointed client
     *
     * @param clientId
     * @param listener
     * @return
     */
    public boolean setClientListener(String identifierName, int clientId,
                                     IBusListener listener) {
        /**
         * find the item in list based on appID.<br>
         * If found, create a new appid for this item. If found nothing, GOD!
         */
        for (int i = 0; i < mClientList.size(); ++i) {
            if (mClientList.get(i).getClientId() == clientId) {
                mClientList.get(i).setAppListener(listener);
                LogicLogger.i(TAG,
                        "setClientListener - succeed to set listener for clientid:"
                                + clientId);
                dumpClientList();

                return true;
            }
        }
        LogicLogger.e(TAG, "setClientListener - cannot find appid:" + clientId);
        return false;
    }

    /**
     * Important Functions: <br>
     * 1. load the defined clients from the configuration modules.<br>
     * 2. ask every client to connect to hermes bus.<br>
     */
    public void requestAllDefinedClient2Connect(final Context context) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                    checkAllClientsAndSendRequestIfNeed(context);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Important Functions: <br>
     * 2. ask all clients in the pointed app to connect to hermes bus.<br>
     */
    public void requestCertainAppDefinedClient2Connect(final Context context,
                                                       final String appPackageName) {
        // new Thread(new Runnable() {
        // public void run() {
        try {
            Thread.sleep(1000);
            BusClientWakeuper wakeuper = new BusClientWakeuper(context);
            for (int i = 0; i < mClientList.size(); ++i) {
                if (0 == mClientList.get(i).getPackageName()
                        .compareTo(appPackageName)) {
                    wakeuper.wakeupCoreClient(mClientList.get(i)
                            .getClientItem().getName());
                }
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // }
        // }).start();
    }

    /**
     * print the list of configured clients which are required to connected to
     * hermes bus.
     */
    public void printDefinedClients() {
        BusClientItem[] clients = BusConfigManager.getConfigClientList();
        LogicLogger.i(TAG,
                "====================Defined Clients====================");
        for (int i = 0; i < clients.length; ++i) {
            LogicLogger.i(TAG, clients[i].getName());
        }
        LogicLogger.i(TAG,
                "--------------------Defined Clients--------------------");
    }

    /**
     * print the list of connected clients.
     */
    public void dumpClientList() {
        LogicLogger.i(TAG,
                "===================dumpClientList======================");
        for (int i = 0; i < mClientList.size(); ++i) {
            LogicLogger.i(TAG, mClientList.get(i).getPackageName() + " "
                    + +mClientList.get(i).getClientId() + "  "
                    + mClientList.get(i).getClientItem().getName());
        }
        LogicLogger.i(TAG,
                "-------------------dumpClientList----------------------");
    }


    /**
     * (Bus Controller --> Client)<br>
     * do actions on the specified client<br>
     *
     * @param clientIdentifier
     * @param requestType
     * @param requestOp
     * @return
     */
    public int opRequestEvent(int clientIdentifier, int requestType,
                              int requestOp) {
		/*
		 * find clients in the connected client list
		 */
        // dumpClientList();

        for (int i = 0; i < mClientList.size(); ++i) {
            ClientData clientData = mClientList.get(i);
            if (clientIdentifier == clientData.getClientItem().getIdentifier()) {

                try {
                    IBusListener listener = clientData.getIAppListener();
                    if (null != listener) {
                        return listener.busRequestEvent(clientData.getClientId(),
                                requestType, requestOp);
                    }
					/*
					 * if listener is null, the client is not
					 * available.
					 */
                    break;
                } catch (RemoteException e) {
                    LogicLogger.e(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }
		/*
		 * return no client find if we cannot find this client.
		 */
        return ICoreRequestReturnValue.REQUESTOP_RETURN_CLIENT_NOT_AVAILABLE;
    }

    public boolean isClientDead(int clientIdentifier) {
        for (int i = 0; i < mClientList.size(); ++i) {
            if (mClientList.get(i).getClientItem().getIdentifier() == clientIdentifier) {
                return mClientList.get(i).isDead();
            }
        }
        return false;
    }

    /**
     * bus controller sends BusData_Client to client<br>
     *
     * @param client2Sending
     * @param dataClientIdentifier
     * @param opMsg
     * @return
     */
    public int sendClientData2Client(int client2Sending,
                                     int dataClientIdentifier, OverProcessMessage opMsg) {
        LogicLogger.d(TAG, "BusData_Client to client=" + client2Sending
                + " dataclient=" + dataClientIdentifier + " msg.what="
                + opMsg.what);
        for (int i = 0; i < mClientList.size(); ++i) {
            ClientData clientData = mClientList.get(i);
            if (client2Sending == clientData.getClientItem().getIdentifier()) {
                try {
                    IBusListener listener = clientData.getIAppListener();
                    if (null != listener) {
                        return listener.busData_Client(client2Sending,
                                dataClientIdentifier, opMsg);
                    }
					/*
					 * if listener is null, the client is not
					 * available.
					 */
                    // return -1;
                } catch (RemoteException e) {
                    LogicLogger.e(TAG, e.toString());
                    e.printStackTrace();
                }
                return -1;
            }
        }
        return -1;
    }

    /**
     * Bus controller sends BusData_Item to clients<br>
     *
     * @param client2Sending
     * @param itemIndex
     * @param opMsg
     * @return
     */
    public int sendItemData2Client(int client2Sending, int itemIndex,
                                   OverProcessMessage opMsg) {
        LogicLogger.d(TAG, "BusData_Item to client=" + client2Sending
                + " item=" + itemIndex + " msg.what=" + opMsg.what);
        for (int i = 0; i < mClientList.size(); ++i) {
            ClientData clientData = mClientList.get(i);
            if (client2Sending == clientData.getClientItem().getIdentifier()) {
                try {
                    IBusListener listener = clientData.getIAppListener();
                    if (null != listener) {
                        return listener.busData_Item(client2Sending,
                                itemIndex, opMsg);
                    }
                } catch (RemoteException e) {
                    LogicLogger.e(TAG, e.toString());
                    e.printStackTrace();
                }
                return -1;
            }
        }
        return -1;
    }

    /**
     * bus controller sends direct data to client<br>
     *
     * @param client2Sending
     * @param postClientIdentifier
     * @param opMsg
     * @return
     */
    public int sendDirectData2Client(int client2Sending,
                                     int postClientIdentifier, OverProcessMessage opMsg) {
        LogicLogger.d(TAG, "DirectData to client=" + client2Sending
                + " postClient=" + postClientIdentifier + " msg.what="
                + opMsg.what);
        for (int i = 0; i < mClientList.size(); ++i) {
            ClientData clientData = mClientList.get(i);
            if (client2Sending == clientData.getClientItem().getIdentifier()) {
                try {
                    IBusListener listener = clientData.getIAppListener();
                    if (null != listener) {
                        return listener.busDirectData_Item(client2Sending,
                                postClientIdentifier, opMsg);
                    }
                } catch (RemoteException e) {
                    LogicLogger.e(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * Return a client's listener<br>
     *
     * @param clientIdentifier
     * @return
     */
    public IBusListener getClientAppListener(int clientIdentifier) {
        for (int i = 0; i < mClientList.size(); ++i) {
            ClientData clientData = mClientList.get(i);
            if (clientIdentifier == clientData.getClientItem().getIdentifier()) {
                return clientData.getIAppListener();
            }
        }
        LogicLogger.e(TAG, "getClientAppListener - no client["
                + clientIdentifier + "]");
        return null;
    }

	/* ************************************************************************
	 * Members
	 */

    /**
     * Important Functions: <br>
     * 1. load the defined clients from the configuration modules.<br>
     * 2. ask every client to connect to hermes bus.<br>
     */
    private void checkAllClientsAndSendRequestIfNeed(Context context) {
        // load configuration to get the defined clients
        BusClientItem[] clients = BusConfigManager.getConfigClientList();
        // initialize
        BusClientWakeuper wakeuper = new BusClientWakeuper(context);
        /**
         * Look over every client and wake it up if it's dead.
         */
        for (int i = 0; i < clients.length; ++i) {
            boolean needTell = true;
            for (int j = 0; j < mClientList.size(); ++j) {
                if (mClientList.get(j).getClientItem().getIdentifier() == clients[i]
                        .getIdentifier()) {
                    if (!mClientList.get(j).isDead()) {
                        needTell = false;
                    }
                }
            }
            if (needTell) {
                wakeuper.wakeupCoreClient(clients[i].getName());
            }
        }
    }
}
