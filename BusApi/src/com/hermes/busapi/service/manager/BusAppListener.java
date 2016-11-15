/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.service.manager;

import android.os.Message;
import android.os.RemoteException;

import com.hermes.busapi.log.BusLogger;
import com.hermes.busapi.service.IBusClientListener;
import com.hermes.busconfig.IBusListener;
import com.hermes.busconfig.communication.OverProcessMessage;

/**
 * This listener is the callback of Hermes Bus. <br>
 * It must be a single instance. <br>
 * 
 * Provider features:<br>
 * 1. Receive the call from Hermes Bus through ICoreAppListener.aidl<br>
 * 
 * @author KevinZeng
 * 
 */
public final class BusAppListener {
	private static final String TAG = "BusAppListener";

	private IBusClientListener mIBusClientListener;

	public void setLogicListner(IBusClientListener listener) {
		mIBusClientListener = listener;
	}

	/* ************************************************************************
	 * Single Instance
	 */
	private BusAppListener() {

	}

	private static class CoreAppListenerFactory {
		private static com.hermes.busapi.service.manager.BusAppListener instance = new com.hermes.busapi.service.manager.BusAppListener();
	}

	public static com.hermes.busapi.service.manager.BusAppListener getInstance() {
		return CoreAppListenerFactory.instance;
	}

	/* ************************************************************************
	 * features
	 */
	private IBusListener.Stub mIBusListenerStub = new IBusListener.Stub() {

		/**
		 * Hermes Bus Controller perform a request<br>
		 */
		@Override
		public int busRequestEvent(int clientId, int requestType, int requestOp)
				throws RemoteException {
			return BusAppManager.getIServiceSideOp().requestEvent(clientId,
					requestType, requestOp);
		}

		/**
		 * Hermes Bus sends busData_Client on listener
		 */
		@Override
		public int busData_Client(int clientSelf, int dataClientId,
				OverProcessMessage opMsg) throws RemoteException {
			BusLogger.d(TAG, "BUSDATA_CLIENT - to clientid["
					+ clientSelf + "] - clientDataId[" + dataClientId
					+ "] - what=" + opMsg.what + " obj" + opMsg.obj);
			// send message to local client
			return BusAppManager.getIServiceSideOp().onBusData_Client(
					clientSelf, dataClientId, getMessageFromRemote(opMsg));
		}

		/**
		 * Hermes Bus sends busData_Item on listener
		 */
		@Override
		public int busData_Item(int clientSelf, int itemIndex,
				OverProcessMessage opMsg) throws RemoteException {
			BusLogger.d(TAG, "BUSDATA_ITEM - to clientid["
					+ clientSelf + "] - ItemData[" + itemIndex + "] what="
					+ opMsg.what + " obj" + opMsg.obj);
			// send message to local client
			return BusAppManager.getIServiceSideOp().onBusData_Item(
					clientSelf, itemIndex, getMessageFromRemote(opMsg));
		}

		/**
		 * Hermes Bus sends the direct data
		 */
		@Override
		public int busDirectData_Item(int targetClientIdentifier,
				int postClientIdentifier, OverProcessMessage opMsg)
				throws RemoteException {
			BusLogger.d(TAG, "DIRECTDATA - to clientid["
					+ targetClientIdentifier + "] from clientid["
					+ postClientIdentifier + "] what=" + opMsg.what + " obj"
					+ opMsg.obj);
			// send message to local client
			return BusAppManager.getIServiceSideOp().onBusDirectData(
					targetClientIdentifier, postClientIdentifier,
					getMessageFromRemote(opMsg));
		}

		@Override
		public OverProcessMessage remoteCall(int calleeClientIdentifier,
				int callerClientIdentifier, OverProcessMessage opMsgIn)
				throws RemoteException {
			// TODO Auto-generated method stub
			BusLogger.d(TAG, "REMOTECALL - callee:"
					+ calleeClientIdentifier + " caller:"
					+ callerClientIdentifier);
			try {
				Message msgReturn = BusAppManager.getIOutClientSideOp()
						.remoteDirectCall(calleeClientIdentifier,
								callerClientIdentifier,
								getMessageFromRemote(opMsgIn));
				
				OverProcessMessage opMsgReturn = OverProcessMessage
						.CopyObtain(msgReturn);
				return opMsgReturn;
			} catch (Exception e) {
				BusLogger.e(TAG,
						"remoteCall - exception:" + e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

	};

	public IBusListener.Stub getIBusListenerStub() {
		return mIBusListenerStub;
	}

	/* ************************************************************************
	 * Methods
	 */
	/**
	 * Transfer the remote message to local message<br>
	 * 
	 * @param opMsg
	 * @return
	 */
	public Message getMessageFromRemote(OverProcessMessage opMsg) {
		Message message = Message.obtain();
		message.what = opMsg.what;
		message.arg1 = opMsg.arg1;
		message.arg2 = opMsg.arg2;
		if (null != opMsg.obj) {
			message.obj = opMsg.obj;
		}
		return message;
	}

}
