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
import com.hermes.buscontroller.manager.ServiceManager;
import com.hermes.buscontroller.manager.serviceop.IServiceOp;
import com.hermes.busconfig.define.IServiceDefinition;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * This class is the main service class.<br>
 * <br>
 * 
 * @author KevinZeng
 * 
 */
public class BusControllerService extends Service {
	private static final String TAG = "HBC_S";

	private IServiceOp mIServiceOp = null;

	private static BusControllerService instance = null;

	@Override
	public void onCreate() {
		LogicLogger.i(TAG, "onCreate -");
		super.onCreate();

		// initialize service's manager
		mIServiceOp = ServiceManager.getIServiceOp();
		mIServiceOp.init(this);

		instance = this;
		
		// Ask app/clients to connect
		reload();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		String appPackageName = arg0
				.getStringExtra(IServiceDefinition.COREAPPSERVICE_APPLICATION_PACKAGENAME);
		String appIdentifierName = arg0
				.getStringExtra(IServiceDefinition.COREAPPSERVICE_APPLICATION_IDENTIFIERNAME);
		
		LogicLogger.i(TAG, "onBind - " + appPackageName + "("
				+ appIdentifierName + ")");
		if (null == appPackageName || appPackageName.isEmpty()) {
			return null;
		}

		// add the package name and identifier name pair
		//mIServiceOp.addNewAppAndIdentifier(appPackageName, appIdentifierName);
		
		return mIServiceOp.getCoreAppConnectionStub();
		
	}

	@Override
	public void onRebind(Intent intent) {
		String appPackageName = intent
				.getStringExtra(IServiceDefinition.COREAPPSERVICE_APPLICATION_PACKAGENAME);
		String appIdentifierNameString = intent
				.getStringExtra(IServiceDefinition.COREAPPSERVICE_APPLICATION_IDENTIFIERNAME);
		LogicLogger.i(TAG, "onRebind - " + appPackageName + "("
				+ appIdentifierNameString + ")");
		if (null == appPackageName || appPackageName.isEmpty()) {
			return;
		}

	}

	@Override
	public void onDestroy() {
		LogicLogger.d(TAG, "destroy");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent arg0) {
		String appPackageName = arg0
				.getStringExtra(IServiceDefinition.COREAPPSERVICE_APPLICATION_PACKAGENAME);
		String appIdentifierNameString = arg0
				.getStringExtra(IServiceDefinition.COREAPPSERVICE_APPLICATION_IDENTIFIERNAME);
		LogicLogger.d(TAG, "onUnbind - " + appPackageName + "("
				+ appIdentifierNameString + ")");
		//mIServiceOp.removeBindedApp(appPackageName);
		return true;
		// return super.onUnbind(arg0);
	}

	public void reload() {
		LogicLogger.i(TAG, "reload, reload config if need");
		/**
		 * send broadcast to start the daemon service in certain applications.
		 */
		mIServiceOp.wakeupDeamonOfApplications();
	}

	public static BusControllerService getInstance() {
		return instance;
	}
}
