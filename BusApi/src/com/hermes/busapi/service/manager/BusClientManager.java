/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.service.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;

import com.hermes.busapi.busdaemon.BusDaemonService;
import com.hermes.busapi.busdaemon.manager.op.ICoreClient;
import com.hermes.busapi.log.BusLogger;
import com.hermes.busapi.service.IBusClientListener;
import com.hermes.busconfig.configuration.BusClientItem;
import com.hermes.busconfig.directdata.BusDirectData;
import com.hermes.busconfig.remotecall.RemoteCallConfig;

/**
 * 
 * @author KevinZeng
 * 
 */
public class BusClientManager {
	private String TAG = "BusClientMgr";

	/* ************************************************************************
	 * Members
	 */
	private BusClientItem mClientItem;

	private IBusClientListener mICoreClientListener;

	private ClientSideOperation mClientSideOperation = new ClientSideOperation();

	/* ************************************************************************
	 * Methods
	 */
	public BusClientManager(BusClientItem clientItem) {
		mClientItem = clientItem;
		TAG = TAG + "/" + clientItem.getName();
	}

	/* ************************************************************************
	 * Provided Operation Interface
	 */

	/**
	 * get operation interface of CoreAppLogic's initialization<br>
	 * 
	 * @return
	 */
	public ICoreClient getClient() {
		return mClientSideOperation;
	}


	/* ************************************************************************
	 * Methods
	 */
	/**
	 * This is dynamic receiver to let Bus Controller can ask client to connect
	 * to hermes bus.
	 * 
	 * @param context
	 */
	private void registerClientReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(mClientItem.getName());
		context.registerReceiver(mClientReceiver, filter);
	}

	/**
	 * Receiver for Bus Controller to send "connect" request intent.
	 */
	private BroadcastReceiver mClientReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			BusLogger.d(TAG, "Client's onReceive -");
			BusAppManager.getIClientSideOp().registerClient(
					mClientItem.getIdentifier(), mICoreClientListener);
		}
	};

	/* ************************************************************************
	 * Internal Class
	 */
	/**
	 * Provide client's operation for application<br>
	 * 
	 * @author KevinZeng
	 * 
	 */
	private class ClientSideOperation implements ICoreClient {

		@Override
		public void init(Context context, IBusClientListener listener) {
			// TODO Auto-generated method stub
			BusLogger.d(TAG, "init -");

			mICoreClientListener = listener;

			/*
			 * Initialize instance of connection and no need to connect to hermes bus
			 */
			HermesBusConn.getInstance().init(context);

			Intent intent = new Intent(context, BusDaemonService.class);
			context.startService(intent);
			/*
			 * register the dynamic receiver
			 */
			registerClientReceiver(context);
		}

		@Override
		public int registerBusData_Item(int itemDataIndex) {
			BusLogger.d(TAG, "registerBusData_Item - Client:"
					+ mClientItem.getName() + " ItemData[" + itemDataIndex
					+ "]");
			return BusAppManager.getIClientSideOp().registerBusData_Item(
					mClientItem.getIdentifier(), itemDataIndex);
		}

		@Override
		public int unregisterBusData_Item(int itemDataIndex) {
			BusLogger.d(TAG, "unregisterBusData_Item - Client:"
					+ mClientItem.getName() + " ItemData[" + itemDataIndex
					+ "]");
			return BusAppManager.getIClientSideOp().unregisterBusData_Item(
					mClientItem.getIdentifier(), itemDataIndex);
		}

		@Override
		public int registerBusData_Client(int clientIdentifier) {
			BusLogger.d(TAG, "registerBusData_Client - Client:"
					+ mClientItem.getName() + " ClientDataIdentifier["
					+ clientIdentifier + "]");
			return BusAppManager.getIClientSideOp().registerBusData_Client(
					mClientItem.getIdentifier(), clientIdentifier);
		}

		@Override
		public int unregisterBusData_Client(int clientIdentifier) {
			BusLogger.d(TAG, "unregisterBusData_Client - Client:"
					+ mClientItem.getName() + " ClientDataIdentifier["
					+ clientIdentifier + "]");
			return BusAppManager.getIClientSideOp()
					.unregisterBusData_Client(mClientItem.getIdentifier(),
							clientIdentifier);
		}

		@Override
		public int updateBusData_Client(Message msg) {
			BusLogger.d(TAG,
					"updateBusData_Client - Client:" + mClientItem.getName()
							+ " - what=" + msg.what);
			return BusAppManager.getIClientSideOp().updateBusData_Client(
					mClientItem.getIdentifier(), msg);
		}

		@Override
		public int updateBusData_Item(int itemIndex, Message msg) {
			BusLogger
					.d(TAG, "updateBusData_Item - Client:" + mClientItem.getName()
							+ " ItemData[" + itemIndex + "] - what=" + msg.what);
			return BusAppManager.getIClientSideOp().updateBusData_Item(
					mClientItem.getIdentifier(), itemIndex, msg);
		}

		@Override
		public int sendDirectData(int targetClientIdentifier, Message msg) {
			BusLogger.d(TAG,
					"sendDirectData - Client:" + mClientItem.getName()
							+ " target ClientIdentifier["
							+ targetClientIdentifier + "] - what=" + msg.what);
			return BusAppManager.getIClientSideOp().sendBusDirectData(
					targetClientIdentifier, mClientItem.getIdentifier(), msg);
		}

		@Override
		public Message remoteDirectCall(Message msgIn) {
			BusLogger.d(TAG,
					"remoteCall - Client:" + mClientItem.getName() + " - what="
							+ msgIn.what);
			/*
			 * Get the target client based on message.what
			 */
			int calleeClientIdentifier = RemoteCallConfig
					.getClientIdentiferFromMessageIn(msgIn.what);
			if (0 == calleeClientIdentifier) {
				/*
				 * Error: message is not supported
				 */
				Message msgReturn = Message.obtain();
				msgReturn.what = RemoteCallConfig.MSGRTN_COMMON_ERR_MSGIN_NOT_SUPPORT;
				return msgReturn;
			}
			return BusAppManager.getIClientSideOp().remoteDirectCall(
					calleeClientIdentifier, mClientItem.getIdentifier(), msgIn);
		}

		@Override
		public int sendDirectDataAutomatically(Message msg) {
			BusLogger.d(TAG, "sendDirectDataAutomatically - what="
					+ msg.what);
			/*
			 * Get the target client based on message.what
			 */
			int targetClientIdentifier = BusDirectData
					.getClientIdentiferFromMessageIn(msg.what);
			if (0 == targetClientIdentifier) {
				BusLogger.e(TAG, "No client for this message!");
				return 0;
			}
			return BusAppManager.getIClientSideOp().sendBusDirectData(
					targetClientIdentifier, mClientItem.getIdentifier(), msg);
		}

		@Override
		public boolean isClientOnBus(int targetClientIdentifier) {
			return BusAppManager.getIClientSideOp().isClientOnBus(
					targetClientIdentifier, mClientItem.getIdentifier());
		}

	}
}
