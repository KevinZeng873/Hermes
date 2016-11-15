/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.busutil;

import com.hermes.buscontroller.log.LogicLogger;

import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author KevinZeng
 *
 */
public class BusAppWakeuper {
	private static final String TAG = "CA_WAKE";
	
	/* ************************************************************************
	 * Members
	 */
	private Context mContext = null;
	/* ************************************************************************
	 * Members
	 */
	private BusAppWakeuper(){
		
	}

	public BusAppWakeuper(Context context){
		mContext = context;
	}

	
	/* ************************************************************************
	 * Functions
	 */
	public void wakeupApplication(String app){
		LogicLogger.d(TAG, "wakeupApplication - " + app);
		Intent intent = new Intent(app);
		intent = intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		if(null != mContext){
			mContext.sendBroadcast(intent);
		}else{
			LogicLogger.e(TAG, "wakeupApplication - mContext is null!");
		}
	}
}
