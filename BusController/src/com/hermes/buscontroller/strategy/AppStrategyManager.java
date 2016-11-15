/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.strategy;

import android.content.Context;
import android.view.KeyEvent;

import com.hermes.buscontroller.log.LogicLogger;
import com.hermes.busconfig.define.IServiceDefinition.ICoreRequestEventType;
import com.hermes.busconfig.define.IServiceDefinition.ICoreRequestOperation;

/**
 * AppStrategyManager is the manager of app's strategy.<br>
 * 
 * @author KevinZeng
 * 
 */
public class AppStrategyManager implements IAppStrategyOp {
	private static final String TAG = "AS_M";
	/* ************************************************************************
	 * Constant
	 */
	public static final int SUPPORTED_STRATEGY_REMOTEKEY = 0x00000001;
	public static final int SUPPORTED_STRATEGY_SRC = 0x00000002;

	/* ************************************************************************
	 * Members
	 */

	/* ************************************************************************
	 * Single Instance
	 */
	private AppStrategyManager() {

	}

	private static class AppStrategyManagerFactory {
		private static AppStrategyManager instance = new AppStrategyManager();
	}

	/* ************************************************************************
	 * Functions
	 */
	/**
	 * Return the operation interface of AppStrategyManager
	 * 
	 * @return
	 */
	public static IAppStrategyOp getIAppStategyOp() {
		return (IAppStrategyOp) AppStrategyManagerFactory.instance;
	}

	/* ************************************************************************
	 * Methods
	 */
	private void initOperator() {

	}

	private void initCoreRemoteKeyReceiver(Context context) {
	}
	

	/* ************************************************************************
	 * IAppStrategyOp Implementation
	 */
	@Override
	public void init(Context context) {
		LogicLogger.i(TAG, "init -");
		initOperator();
		initCoreRemoteKeyReceiver(context);
	}

	/* ************************************************************************
	 * Methods
	 */

}
