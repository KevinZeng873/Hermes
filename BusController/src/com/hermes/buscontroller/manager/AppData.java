/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.manager;

import android.os.IBinder;
import android.os.RemoteException;

import com.hermes.buscontroller.log.LogicLogger;
import com.hermes.busconfig.IBusListener;

/**
 * 
 * @author KevinZeng
 * 
 */
public class AppData {
	private static final String TAG = "AppData";

	/* ************************************************************************
	 * Members
	 */
	private String mAppPackageName;
	private String mAppIdentifierName;

	private AppDeathHandler mDeathHandler = null;

	private boolean mAppCBConnected = false;

	/**
	 * how many time we have asked this application to connect
	 */
	private int mCountOfAskConnect = 0;

	/* ************************************************************************
	 * Methods
	 */
	/**
	 * Make the default constructor to private
	 */
	private AppData() {

	}

	public AppData(String packageName, String identifierName) {
		mAppPackageName = packageName;
		mAppIdentifierName = identifierName;
		makeInitStatus();

	}

	private void makeInitStatus() {
		mCountOfAskConnect = 0;
		mDeathHandler = null;
		mAppCBConnected = false;
	}

	/* ************************************************************************
	 * Functions
	 */
	public String getPackageName() {
		return mAppPackageName;
	}

	public String getAppIdentifierName() {
		return mAppIdentifierName;
	}

	public void setCBConnected() {
		mAppCBConnected = true;
	}

	public boolean isAppCBConnected() {
		return mAppCBConnected;
	}

	public void askAppConnectOnce() {
		++mCountOfAskConnect;
	}

	public int getCountOfAskConnect() {
		return mCountOfAskConnect;
	}

	/**
	 * Check if we have put a watch dog on the connected callback ADIL with<br>
	 * application.<br>
	 * 
	 * @return
	 */
	public boolean hasProcessWatchdog(String identifierName) {
		return true;
	}

	public void installWatchdog4AppCallback(IBusListener callback) {
		if (null != mDeathHandler) {
			LogicLogger.d(TAG, "watchdog existed on " + mAppIdentifierName
					+ "'s callback");
		}

		/*
		 * Set watch dog on the callback of application.
		 */
		mDeathHandler = new AppDeathHandler(callback);
		try {
			callback.asBinder().linkToDeath(mDeathHandler, 0);
		} catch (RemoteException e) {
			LogicLogger.e(TAG,
					"installWatchdog4AppCallback - exception: " + e.toString());
			e.printStackTrace();
			mDeathHandler = null;
		}
	}

	/* ************************************************************************
	 * Internal Classes
	 */

	/**
	 * DeathHandler for callback of application<br>
	 * 
	 * @author KevinZeng
	 * 
	 */
	private class AppDeathHandler implements IBinder.DeathRecipient {
		private IBusListener mCb;

		public AppDeathHandler(IBusListener Cb) {
			mCb = Cb;
		}

		@Override
		public void binderDied() {
			mCb.asBinder().unlinkToDeath(mDeathHandler, 0);
			mAppCBConnected = false;
			/*
			 * Unfortunately, the callback of application is died.<br>
			 */
			LogicLogger.i(TAG, "APP's CB died - " + mAppIdentifierName);
			/*
			 * remove all clients belonged to the application and wake it up
			 * again.
			 */
			ServiceManager.getIServiceOp().removeBindedAppClients(
					mAppPackageName, true);

			makeInitStatus();
		}

		public IBusListener getBinder() {
			return mCb;
		}
	}

}
