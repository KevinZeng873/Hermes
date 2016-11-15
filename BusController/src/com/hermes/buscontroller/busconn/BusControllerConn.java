/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.busconn;

import android.os.RemoteException;

import com.hermes.buscontroller.log.LogicLogger;
import com.hermes.buscontroller.manager.AppDataManager;
import com.hermes.buscontroller.manager.ServiceManager;
import com.hermes.buscontroller.manager.serviceop.IConnectionOp;
import com.hermes.busconfig.IBusConnection;
import com.hermes.busconfig.IBusListener;
import com.hermes.busconfig.communication.OverProcessMessage;

/**
 * This class is the AIDL.stub to provide client with connection of Hermes bus.
 * 
 * @author KevinZeng
 * 
 */
public class BusControllerConn {
	private static final String TAG = "BUS_CONN";

	private IConnectionOp mIConnectionOp = null;

	public BusControllerConn() {
	}

	/* ************************************************************************
	 * ICoreAppConnection Implementation
	 */
	private IBusConnection.Stub mICoreAppConnectionStub = new IBusConnection.Stub() {

		/* ********************************************************************
		 * Function for all clients
		 */

		@Override
		public String getVersion() throws RemoteException {
			return "0.1";
		}

		@Override
		public boolean setListener(String identifierName, int clientId,
				IBusListener appCallback) throws RemoteException {
			AppDataManager.getInstance().setWatchdogForAppCallback(
					identifierName, appCallback);
			/*
			 * now we know we have a callback of this application.
			 */
			AppDataManager.getInstance().setAppCBConected(identifierName);

			/*
			 * give the listener to client data
			 */
			return mIConnectionOp.setClientListener(identifierName, clientId,
					appCallback);
		}

		@Override
		public int assignNewClientId(String packageName,
				String appIdentifierName, int clientIdentifier)
				throws RemoteException {
			// store a record for the application
			AppDataManager.getInstance().addNewItem(packageName,
					appIdentifierName);
			// assign a new client id for this client
			return mIConnectionOp.assignNewClientId(packageName,
					clientIdentifier);
		}

		@Override
		public void writeInfo(int type, String info) throws RemoteException {

		}

		@Override
		public int registerBusData_Client(int clientSelf, int clientIdentifier)
				throws RemoteException {
			// LogicLogger.i(TAG, "*****************************" + clientSelf +
			// ":"
			// + clientIdentifier);
			return mIConnectionOp.registerClientDataNotify(clientSelf,
					clientIdentifier);
		}

		@Override
		public int unregisterBusData_Client(int clientSelf,
				int clientIdentifier) throws RemoteException {
			// TODO Auto-generated method stub
			return mIConnectionOp.unregisterClientDataNotify(clientSelf,
					clientIdentifier);
		}

		@Override
		public int registerBusData_Item(int clientSelf, int itemIndex)
				throws RemoteException {
			// TODO Auto-generated method stub
			return mIConnectionOp.registerItemDataNotify(clientSelf, itemIndex);
		}

		@Override
		public int unregisterBusData_Item(int clientSelf, int itemIndex)
				throws RemoteException {
			// TODO Auto-generated method stub
			return mIConnectionOp.unregisterItemDataNotify(clientSelf,
					itemIndex);
		}

		/* ********************************************************************
		 * Function for client which sends BusData_Client
		 */
		/**
		 * No need transfer remote message to local message because<br>
		 * we will send it to other processes.<br>
		 */
		@Override
		public int updateBusData_Client(int dataClientIdentifier,
				OverProcessMessage opMsg) throws RemoteException {
			LogicLogger.d(TAG, "receive BusData_Client - dataClient="
					+ dataClientIdentifier + " msg.what=" + opMsg.what);
			return mIConnectionOp.updateClientData(dataClientIdentifier, opMsg);
		}

		/* ********************************************************************
		 * Function for client which sends BusData_Item
		 */
		@Override
		public int updateBusData_Item(int clientIdentifier, int itemIndex,
				OverProcessMessage opMsg) throws RemoteException {
			LogicLogger.d(TAG, "receive BusData_Item - item=" + itemIndex);
			return mIConnectionOp.updateItemData(itemIndex, opMsg);
		}

		/**
		 * Function for client which sends a direct data to another client
		 */
		@Override
		public int postBusDirectData(int targetClientIdentifier,
				int postClientIdentifier, OverProcessMessage opMsg)
				throws RemoteException {
			LogicLogger.d(TAG, "receive DirectData - targetClient="
					+ targetClientIdentifier + " postClient="
					+ postClientIdentifier + " msg.what=" + opMsg.what);
			return mIConnectionOp.postDirectData(targetClientIdentifier,
					postClientIdentifier, opMsg);
		}

		/**
		 * Function for client which want to get another client's listener<br>
		 * in order to do a direct remote call.<br>
		 */
		@Override
		public IBusListener getClientListener(int calleeClientIdentifier,
				int callerClientIdentifier) throws RemoteException {
			LogicLogger.d(TAG, "RemoteCall - callee identifier:"
					+ calleeClientIdentifier + " caller identifier:"
					+ callerClientIdentifier);
			return mIConnectionOp.getClientAppListener(calleeClientIdentifier);
		}

	};

	public IBusConnection.Stub getICoreAppConnectionStub() {
		/*
		 * Now I guess a client to bind, so we initialize the connection
		 * interface.
		 */
		if (null == mIConnectionOp) {
			mIConnectionOp = ServiceManager.getIConnectionOp();
		}
		return mICoreAppConnectionStub;
	}
}
