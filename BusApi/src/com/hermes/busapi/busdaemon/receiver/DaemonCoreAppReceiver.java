/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */


package com.hermes.busapi.busdaemon.receiver;

import com.hermes.busapi.busdaemon.BusDaemonService;
import com.hermes.busapi.log.BusLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author KevinZeng
 * 
 */
public class DaemonCoreAppReceiver extends BroadcastReceiver {
	private static final String TAG = "AppReceiver";

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		if (null != arg1) {
			/*
			 * Start Bus Deamon Service
			 */
			BusLogger.i(TAG,
					"Receive an broadcast - action=" + arg1.getAction());
			Intent intent = new Intent(arg0, BusDaemonService.class);
			arg0.startService(intent);
		}
	}

}
