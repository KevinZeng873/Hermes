/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.busdaemon;

import com.hermes.busapi.log.BusLogger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * @author KevinZeng
 * 
 */
public class BusDaemonService extends Service {
	private static final String TAG = "BUSAPPSERV";

	@Override
	public void onCreate() {
		BusLogger.d(TAG, "onCreate - ");
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		BusLogger.d(TAG, "onDestroy - ");
		super.onDestroy();
	}
}
