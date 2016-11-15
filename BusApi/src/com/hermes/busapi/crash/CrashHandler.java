/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.crash;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hermes.busconfig.communication.RemoteObjectFormat;
import com.hermes.busconfig.directdata.BusDirectData;
import com.hermes.busapi.service.manager.BusAppManager;
import com.hermes.busapi.log.BusLogger;

import android.os.Message;

/**
 * 
 * @author KevinZeng
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private static final CrashHandler sHandler = new CrashHandler();
	/**
	 * keep the default handler for exception
	 */
	private static final UncaughtExceptionHandler sDefaultHandler = Thread
			.getDefaultUncaughtExceptionHandler();
	private static final ExecutorService THREAD_POOL = Executors
			.newSingleThreadExecutor();
	private Future<?> future;

	public static CrashHandler getInstance() {
		return sHandler;
	}

	/**
	 * 1. save information in log file<br>
	 * 2. report the crash<br>
	 * 3. pass it to default exception handler<br>
	 */
	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {

		future = THREAD_POOL.submit(new Runnable() {
			public void run() {
				//
				String error = getErrorInfoFromTHrowable(ex);
				Message msg = Message.obtain();
				msg.what = BusDirectData.ICoreDirectDataCoreKeeper.MSGSENT_COREKEEPER_NOTIFY_CRASH_INFO;
				msg.obj = RemoteObjectFormat.RemoteObj.FormatRemoteObj(error);
				/*
				 * Send direct data to Keeper
				 */
				int targetClientIdentifier = BusDirectData
						.getClientIdentiferFromMessageIn(msg.what);
				if (0 == targetClientIdentifier) {
					BusLogger.e(TAG, "No client can handle this message!");
					return;
				}
				BusAppManager.getIClientSideOp().sendBusDirectData(
						targetClientIdentifier,
						BusAppManager.getIAppSideOp().getAppIdentifierItem()
								.getIdentifier(), msg);

			};
		});
		if (!future.isDone()) {
			try {
				future.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sDefaultHandler.uncaughtException(thread, ex);
	}

	public static String getErrorInfoFromTHrowable(Throwable ex) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return "\r\n" + sw.toString() + "\r\n";
		} catch (Exception e) {
			return "failed to getErrorInfoFromThrowable";
		}
	}

}