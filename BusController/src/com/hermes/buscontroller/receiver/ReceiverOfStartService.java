/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.receiver;

import com.hermes.buscontroller.BusControllerService;
import com.hermes.buscontroller.log.LogicLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author KevinZeng
 * 
 */
public class ReceiverOfStartService extends BroadcastReceiver {
	private static final String TAG = "RECEIVER_START";

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		LogicLogger.i(TAG, "recevied, start service---");
		// start Bus Service
		Intent intent = new Intent(arg0, BusControllerService.class);
		//intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		arg0.startService(intent);
		// also need do initialization operations if need
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					BusControllerService.getInstance().reload();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
