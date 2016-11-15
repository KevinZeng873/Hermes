/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hermes.buscontroller.busutil.BusAppWakeuper;
import com.hermes.buscontroller.busconn.BusControllerConn;
import com.hermes.buscontroller.busdata.BusData_ClientManager;
import com.hermes.buscontroller.busdata.DirectData_ItemManager;
import com.hermes.buscontroller.busdata.BusData_ItemManager;
import com.hermes.buscontroller.log.LogicLogger;
import com.hermes.buscontroller.manager.serviceop.IClientCallbackOp;
import com.hermes.buscontroller.manager.serviceop.IConnectionOp;
import com.hermes.buscontroller.manager.serviceop.IServiceOp;
import com.hermes.buscontroller.manager.serviceop.IStrategyOp;
import com.hermes.busconfig.IBusConnection;
import com.hermes.busconfig.IBusListener;
import com.hermes.busconfig.communication.OverProcessMessage;
import com.hermes.busconfig.configuration.BusConfigManager;
import com.hermes.busconfig.configuration.BusAppItem;

/**
 * 
 * @author KevinZeng
 * 
 */
public class ServiceManager {
	private static final String TAG = "SERVICE_M";

	/* ************************************************************************
	 * Constants
	 */
	/**
	 * Wake up one application based on its package name<br>
	 */
	private static final int WAKUP_ONE_APPLICATION = 0x00000001;
	/**
	 * Wake up all applications
	 */
	private static final int WAKEUP_ALL_APPLICATION = 0x00000002;

	/**
	 * for some reason, application cannot respond the notification. <br>
	 * so we need ask it again. this constant is the maximum for the request<br>
	 */
	public static final int ASK_APP_CONNECT_COUNT_MAX = 2000;

	/* ************************************************************************
	 * Members
	 */
	private Context mContext = null;
	private ClientDataManager mClientDataManager = null;
	private BusControllerConn mBusControllerConn = null;

	/**
	 * helper for connection operation
	 */
	private ConnectionOpHelper mConnectionOpHelper = null;
	/**
	 * helper for service operation
	 */
	private ServiceOpHelper mServiceOpHelper = null;
	/**
	 * helper for strategy operation
	 */
	private StrategyOpHelper mStrategyOpHelper = null;
	/**
	 * helper for client callback operation
	 */
	private ClientCallbackOpHelper mClientCallbackOpHelper = null;

	/* ************************************************************************
	 * Single Instance
	 */
	private ServiceManager() {
		mBusControllerConn = new BusControllerConn();
		mClientDataManager = new ClientDataManager();
		mConnectionOpHelper = new ConnectionOpHelper();
		mServiceOpHelper = new ServiceOpHelper();
		mStrategyOpHelper = new StrategyOpHelper();
		mClientCallbackOpHelper = new ClientCallbackOpHelper();
	}

	private static class ServiceManagerFactory {
		private static ServiceManager instance = new ServiceManager();
	}

	// public static ServiceManager getInstance(){
	// return ServiceManagerFactory.instance;
	// }
	public static IServiceOp getIServiceOp() {
		return ServiceManagerFactory.instance.mServiceOpHelper;
	}

	public static IConnectionOp getIConnectionOp() {
		// return ServiceManagerFactory.instance;
		return ServiceManagerFactory.instance.mConnectionOpHelper;
	}

	public static IStrategyOp getIStrategyOp() {
		return ServiceManagerFactory.instance.mStrategyOpHelper;
	}

	public static IClientCallbackOp getIClientCallbackOp() {
		return ServiceManagerFactory.instance.mClientCallbackOpHelper;
	}

	/* ************************************************************************
	 * Methods
	 */
	/**
	 * wake up the daemon for the certain application<br>
	 * 1. wake up the app<br>
	 * 2. wake up the client<br>
	 * 
	 * @param packageName
	 */
	private void wakeupOneAppDaemon(String packageName) {
		wakeupOneApplication(packageName);
	}

	/* ********************************************************************
	 * Constant
	 */
	private static final int CHECK_APP_CONNECT_INTERVAL = 3 * 1000; // 3
																	// seconds

	/* ********************************************************************
	 * Members
	 */

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WAKUP_ONE_APPLICATION:
				wakeupOneThread((String) (msg.obj));
				break;
			case WAKEUP_ALL_APPLICATION:
				wakeupAllAppThread();
				break;
			}
		}
	};

	/* ********************************************************************
	 * Functions
	 */
	private void wakeupOneApplication(String packageName) {
		// LogicLogger.d(TAG, "wakeupOneApplication - ");
		Message msg = mHandler.obtainMessage();
		msg.what = WAKUP_ONE_APPLICATION;
		msg.obj = packageName;
		mHandler.sendMessage(msg);

	}

	private void wakeupAllOrUnconnectedApplication() {
		Message msg = mHandler.obtainMessage();
		msg.what = WAKEUP_ALL_APPLICATION;
		mHandler.sendMessage(msg);
	}

	private void wakeupAllAppThread() {
		// Just Test, we should make it in a thread of strategy.
		new Thread(new Runnable() {
			public void run() {
				// Looper.prepare();
				// ask apps to start the Daemon service
				BusAppItem[] apps = BusConfigManager.getConfigAppList();
				for (int i = 0; i < apps.length; ++i) {
					if (!AppDataManager.getInstance().isAppConnected(
							apps[i].getName())) {
						LogicLogger.d(TAG, "wakeupDeamonOfApplications - "
								+ apps[i]);
						BusAppWakeuper wakeuper = new BusAppWakeuper(mContext);
						wakeuper.wakeupApplication(apps[i].getName());
					}
				}

				// ask clients to connect to CoreAppService
				mClientDataManager.requestAllDefinedClient2Connect(mContext);

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// check if all application have connected.
				if (AppDataManager.getInstance().getCountOfAppCbConnected() < apps.length) {
					wakeupAllOrUnconnectedApplication();
				}
			}
		}).start();
	}

	/* ********************************************************************
	 * Methods
	 */
	/**
	 * 1. ask the application to connect<br>
	 * 2. ask all the clients in this appliction to connect<br>
	 * 3. ask 1 and 2 over and over again if application doesn't connect<br>
	 * until the count reaches the maximum limit<br>
	 * 
	 * @param packageName
	 */
	private void wakeupOneThread(final String packageName) {
		new Thread(new Runnable() {
			public void run() {
				try {
					// LogicLogger.d(TAG, "wakeupOneThread - ");
					if (!AppDataManager.getInstance().isAppCBConnected(
							packageName)) {
						// record the count we have asked
						AppDataManager.getInstance().askAppConnectOnce(
								packageName);
						/*
						 * the application isn't connected or the callback has
						 * not been set.
						 */
						String identifierName = AppDataManager.getInstance()
								.getIdentifierName(packageName);
						if (null == identifierName) {
							LogicLogger.i(TAG,
									"wakeupOneAppDaemon - failed to find package "
											+ packageName);
							return;
						}
						/*
						 * wake up the application
						 */
						BusAppWakeuper wakeuper = new BusAppWakeuper(mContext);
						wakeuper.wakeupApplication(identifierName);
						/*
						 * wake up the clients we do have the clients in list so
						 * do not need load it from configuration module.
						 */
						mClientDataManager
								.requestCertainAppDefinedClient2Connect(
										mContext, packageName);

						/*
						 * try again if the application didn't connect
						 */
						if (AppDataManager.getInstance().getCountOfAskConnect(
								packageName) < ASK_APP_CONNECT_COUNT_MAX) {
							Message msgCheck = mHandler.obtainMessage();
							msgCheck.what = WAKUP_ONE_APPLICATION;
							msgCheck.obj = packageName;
							mHandler.sendMessageDelayed(msgCheck,
									CHECK_APP_CONNECT_INTERVAL);
						}
					}
				} catch (Exception e) {

				}

			}
		}).start();
	}

	/* ************************************************************************
	 * Internal Class
	 */
	/**
	 * Service provides connection operation to outside<br>
	 * 
	 * @author KevinZeng
	 * 
	 */
	private class ConnectionOpHelper implements IConnectionOp {

		/* ************************************************************************
		 * IConnectionOp Implementation
		 */
		@Override
		public int assignNewClientId(String packageName, int clientIdentifier) {
			return mClientDataManager.assignNewClientId(packageName,
					clientIdentifier);
		}

		@Override
		public boolean setClientListener(String identifierName, int clientId,
				IBusListener listener) {
			return mClientDataManager.setClientListener(identifierName,
					clientId, listener);
		}

		@Override
		public int registerClientDataNotify(int clientSelf, int clientIdentifier) {
			return BusData_ClientManager.getInstance().addRequestClient(
					clientSelf, clientIdentifier);

		}

		@Override
		public int unregisterClientDataNotify(int clientSelf,
				int clientIdentifier) {
			return BusData_ClientManager.getInstance().removeRequestClient(
					clientSelf, clientIdentifier);
		}

		@Override
		public int registerItemDataNotify(int clientSelf, int itemIndex) {
			// TODO Auto-generated method stub
			return BusData_ItemManager.getInstance().addRequestClient(clientSelf,
					itemIndex);
		}

		@Override
		public int unregisterItemDataNotify(int clientSelf, int itemIndex) {
			// TODO Auto-generated method stub
			return BusData_ItemManager.getInstance().removeRequestClient(
					clientSelf, itemIndex);
		}

		@Override
		public int updateClientData(int dataClientIdentifier,
				OverProcessMessage opMsg) {
			// TODO Auto-generated method stub
			return BusData_ClientManager.getInstance().handleUpdatedClientData(
					dataClientIdentifier, opMsg);
		}

		@Override
		public int updateItemData(int itemIndex, OverProcessMessage opMsg) {
			// TODO Auto-generated method stub
			return BusData_ItemManager.getInstance().handleUpdatedItemData(
					itemIndex, opMsg);
		}

		@Override
		public int postDirectData(int targetClientIdentifier,
				int postClientIdentifier, OverProcessMessage opMsg) {
			return DirectData_ItemManager.getInstance().handleDirectData(
					targetClientIdentifier, postClientIdentifier, opMsg);
		}

		@Override
		public IBusListener getClientAppListener(int clientIdentifier) {
			// TODO Auto-generated method stub
			return mClientDataManager.getClientAppListener(clientIdentifier);
		}

	}

	/**
	 * Service provides service operation to outside<br>
	 * 
	 * @author KevinZeng
	 * 
	 */
	private class ServiceOpHelper implements IServiceOp {
		@Override
		public void removeBindedAppClients(String packageName,
				boolean wakeupagain) {
			mClientDataManager.removeClientBasedOnPackageName(packageName);
			if (wakeupagain) {
				/*
				 * if someone died, we call it to connect again!
				 */
				wakeupOneAppDaemon(packageName);
			}
		}

		/**
		 * Wake up the daemon service in application
		 */
		@Override
		public void wakeupDeamonOfApplications() {
			/*
			 * print these configured clients
			 */

			mClientDataManager.printDefinedClients();
			mClientDataManager.dumpClientList();

			wakeupAllOrUnconnectedApplication();
		}

		@Override
		public void init(Context context) {
			// TODO Auto-generated method stub
			mContext = context;

			// initialize notifcation manager
			BusData_ClientManager.getInstance();
			BusData_ItemManager.getInstance();
		}

		@Override
		public IBusConnection.Stub getCoreAppConnectionStub() {
			// TODO Auto-generated method stub
			return mBusControllerConn.getICoreAppConnectionStub();
		}

		@Override
		public boolean isClientDead(int clientIdentifier) {
			// TODO Auto-generated method stub
			return mClientDataManager.isClientDead(clientIdentifier);
		}
	}

	/**
	 * Service provides strategy operation to outside through this helper<br>
	 * 
	 * @author KevinZeng
	 * 
	 */
	private class StrategyOpHelper implements IStrategyOp {
		/**
		 * Service sends the request to client.<br>
		 */
		@Override
		public int requestEvent(int clientIdentifier, int requestType,
				int requestOp) {

			return mClientDataManager.opRequestEvent(clientIdentifier,
					requestType, requestOp);
		}
	}

	/**
	 * Service provides Client's CallbackOpe to outside<br>
	 * 
	 * @author KevinZeng
	 * 
	 */
	private class ClientCallbackOpHelper implements IClientCallbackOp {

		@Override
		public int sendClientDataNotification(int client2Sending,
				int dataClientIdentifier, OverProcessMessage opMsg) {
			// TODO Auto-generated method stub
			return mClientDataManager.sendClientData2Client(client2Sending,
					dataClientIdentifier, opMsg);
		}

		@Override
		public int sendItemDataNotification(int client2Sending, int itemIndex,
				OverProcessMessage opMsg) {
			// TODO Auto-generated method stub
			return mClientDataManager.sendItemData2Client(client2Sending,
					itemIndex, opMsg);
		}

		@Override
		public int sendDirectData(int client2Sending, int postClientIdentifier,
				OverProcessMessage opMsg) {
			// TODO Auto-generated method stub
			return mClientDataManager.sendDirectData2Client(client2Sending,
					postClientIdentifier, opMsg);
		}

	}

}
