/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.busdata;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.hermes.buscontroller.manager.ServiceManager;
import com.hermes.busconfig.communication.OverProcessMessage;

/**
 * This class handles the message posted by a client and then sends it to<br>
 * the target client.<br>
 * 
 * @author KevinZeng
 * 
 */
public class DirectData_ItemManager {
	private static final String TAG = "DD_M";

	/* ************************************************************************
	 * Constants
	 */
	private static final int FIX_NUMBER_OF_THREAD = 15;

	/* ************************************************************************
	 * Members
	 */
	private ExecutorService mThreadPoolExecutor = Executors
			.newFixedThreadPool(DirectData_ItemManager.FIX_NUMBER_OF_THREAD);

	/* ************************************************************************
	 * Single Instance
	 */
	private DirectData_ItemManager() {
		init();
	}

	private static class DirectDataManagerFactory {
		private static DirectData_ItemManager instance = new DirectData_ItemManager();
	}

	public static DirectData_ItemManager getInstance() {
		return DirectDataManagerFactory.instance;
	}

	/* ************************************************************************
	 * Methods
	 */
	private void init() {

	}

	/* ************************************************************************
	 * Functions
	 */
	public int handleDirectData(int targetClientIdentifier,
			int postClientIdentifier, OverProcessMessage opMsg) {
		if (!mThreadPoolExecutor.isShutdown()) {
			DirectDataCallable directCallable = new DirectDataCallable(
					targetClientIdentifier, postClientIdentifier, opMsg);
			FutureTask<Integer> callableTask = new FutureTask<Integer>(
					directCallable);
			mThreadPoolExecutor.submit(callableTask);
		}
		return 0;
	}

	/* ************************************************************************
	 * Internal Classes
	 */

	private class DirectDataCallable implements Callable<Integer> {

		private int mTargetClientIdentifier = 0;
		private int mPostClientIdentifier = 0;
		private OverProcessMessage mOpMsg = null;

		/**
		 * Constructor
		 */
		public DirectDataCallable(int targetClientIdentifier,
				int postClientIdentifier, OverProcessMessage opMsg) {
			mTargetClientIdentifier = targetClientIdentifier;
			mPostClientIdentifier = postClientIdentifier;
			mOpMsg = opMsg;
		}

		@Override
		public Integer call() throws Exception {
			ServiceManager.getIClientCallbackOp().sendDirectData(
					mTargetClientIdentifier, mPostClientIdentifier, mOpMsg);
			return 0;
		}

	}
}
