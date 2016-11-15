/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.busutil;

import android.content.Context;
import android.content.Intent;

import com.hermes.buscontroller.log.LogicLogger;

/**
 * AppDeamonWakeuper can wakeup the application based on requirements.<br>
 * 
 * @author KevinZeng
 * 
 */
public class BusClientWakeuper {
	private static final String TAG = "CC_WAKE";

	/* ************************************************************************
	 * Members
	 */
	private Context mContext = null;

	/* ************************************************************************
	 * Single Instance
	 */
	private BusClientWakeuper() {

	}

	public BusClientWakeuper(Context context){
		mContext = context;
	}

	/* ************************************************************************
	 * Functions
	 */
	public void wakeupCoreClient(String clientName) {
		LogicLogger.d(TAG, "wakeupCoreClient - " + clientName);
		Intent intent = new Intent(clientName);
		intent = intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		if (null != mContext) {
			mContext.sendBroadcast(intent);
		} else {
			LogicLogger.e(TAG, "wakeupApplication - mContext is null!");
		}
	}
}
