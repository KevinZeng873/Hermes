/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller;

import com.hermes.buscontroller.log.LogicLogger;

import android.app.Application;
import android.content.Intent;

public class BusApplication extends Application {
	private static final String TAG = "BUSAPP";
	
	/* ************************************************************************
	 * Application Override
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		LogicLogger.d(TAG, "onCreate -");

		new Thread(new Runnable() {
			public void run() {
				try {

					LogicLogger.i(TAG, "onCreate - start Service");
					// start service
					Intent intent = new Intent(getApplicationContext(), BusControllerService.class);
					getApplicationContext().startService(intent);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
