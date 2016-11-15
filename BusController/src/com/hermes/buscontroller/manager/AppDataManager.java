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

import com.hermes.buscontroller.log.LogicLogger;
import com.hermes.busconfig.IBusListener;

/**
 * Single Instance
 * 
 * @author KevinZeng
 * 
 */
public class AppDataManager {
	private static final String TAG = "AppDataMgr";

	/* ************************************************************************
	 * Single Instance
	 */
	private AppDataManager() {

	}

	private static class AppDataManagerFactory {
		private static AppDataManager instance = new AppDataManager();
	}

	public static AppDataManager getInstance() {
		return AppDataManagerFactory.instance;
	}

	/* ************************************************************************
	 * Members
	 */
	private ArrayList<AppData> mAppDataList = new ArrayList<AppData>();

	/* ************************************************************************
	 * Functions
	 */
	/**
	 * Add a watch do on the application's callback<br>
	 * NOTE: only once
	 * 
	 * @param appIdentifierName
	 * @param appCallback
	 */
	public void setWatchdogForAppCallback(String appIdentifierName,
			IBusListener appCallback) {
		for (int i = 0; i < mAppDataList.size(); ++i) {
			if (0 == mAppDataList.get(i).getAppIdentifierName()
					.compareTo(appIdentifierName)) {
				mAppDataList.get(i).installWatchdog4AppCallback(appCallback);
				return;
			}
		}
	}

	/**
	 * record the application that its callback is being set.
	 * 
	 * @param identifierName
	 */
	public void setAppCBConected(String identifierName) {
		for (int i = 0; i < mAppDataList.size(); ++i) {
			if (0 == mAppDataList.get(i).getAppIdentifierName()
					.compareTo(identifierName)) {
				mAppDataList.get(i).setCBConnected();
			}
		}
		dumpAppList();
	}

	/**
	 * ask if the application has connected and set a callback.
	 * 
	 * @param appPackageName
	 * @return
	 */
	public boolean isAppCBConnected(String appPackageName) {
		for (int i = 0; i < mAppDataList.size(); ++i) {
			if (0 == mAppDataList.get(i).getPackageName()
					.compareTo(appPackageName)) {
				return mAppDataList.get(i).isAppCBConnected();
			}
		}
		return false;
	}

	/**
	 * record that we have ask application to connect 1 time.
	 * 
	 * @param appPackageName
	 */
	public void askAppConnectOnce(String appPackageName) {
		for (int i = 0; i < mAppDataList.size(); ++i) {
			if (0 == mAppDataList.get(i).getPackageName()
					.compareTo(appPackageName)) {
				mAppDataList.get(i).askAppConnectOnce();
			}
		}
	}

	/**
	 * return whether or not the app has connected to service<br>
	 * 
	 * @param appIdentifier
	 * @return
	 */
	public boolean isAppConnected(String appIdentifier) {
		for (int i = 0; i < mAppDataList.size(); ++i) {
			if (0 == mAppDataList.get(i).getAppIdentifierName()
					.compareTo(appIdentifier)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * return the count of applications which has connected.<br>
	 * 
	 * @return
	 */
	public int getCountOfAppCbConnected() {
		int count = 0;
		for(int i = 0; i < mAppDataList.size(); ++i){
			if(mAppDataList.get(i).isAppCBConnected()){
				++count;
			}
		}
		return count;
	}

	public int getCountOfAskConnect(String appPackageName) {
		for (int i = 0; i < mAppDataList.size(); ++i) {
			if (0 == mAppDataList.get(i).getPackageName()
					.compareTo(appPackageName)) {
				return mAppDataList.get(i).getCountOfAskConnect();
			}
		}
		/*
		 * if we cannot find one, we return a safe number.
		 */
		return ServiceManager.ASK_APP_CONNECT_COUNT_MAX;
	}

	/**
	 * Add one new application data, we have the limits:<br>
	 * 1. the package name cannot be duplicated.<br>
	 * 2. the identifier name cannot be duplicated.<br>
	 * 
	 * @param packageName
	 * @param appIdentifierName
	 */
	public void addNewItem(String packageName, String appIdentifierName) {
		/*
		 * check if the item is duplicated in the list
		 */
		for (int i = 0; i < mAppDataList.size(); ++i) {
			if (0 == mAppDataList.get(i).getPackageName()
					.compareTo(packageName)
					|| 0 == mAppDataList.get(i).getAppIdentifierName()
							.compareTo(appIdentifierName)) {
				/*
				 * this item is duplicated, do nothing just return
				 */
				LogicLogger.i(TAG, "addNewItem duplicated - " + packageName
						+ "/" + appIdentifierName);
				return;
			}
		}
		AppData data = new AppData(packageName, appIdentifierName);
		mAppDataList.add(data);
		dumpAppList();
	}

	/**
	 * Find the package's identifier name if find it.<br>
	 * Otherwise, return null;<br>
	 * 
	 * @param packageName
	 * @return
	 */
	public String getIdentifierName(String packageName) {
		for (int i = 0; i < mAppDataList.size(); ++i) {
			if (0 == mAppDataList.get(i).getPackageName()
					.compareTo(packageName)) {
				return mAppDataList.get(i).getAppIdentifierName();
			}
		}
		return null;
	}

	/* ************************************************************************
	 * Methods
	 */
	private void dumpAppList() {
		LogicLogger.i(TAG,
				"===================dumpAppDataList======================");
		for (int i = 0; i < mAppDataList.size(); ++i) {
			LogicLogger.i(TAG, mAppDataList.get(i).getPackageName() + "    "
					+ mAppDataList.get(i).getAppIdentifierName() + "    "
					+ mAppDataList.get(i).isAppCBConnected());
		}
		LogicLogger.i(TAG,
				"-------------------dumpAppDataList----------------------");
	}
}
