package com.hermes.misctools.log;

import android.util.Log;


public class MiscLogger {

	private static String TAG = "MISC";
	private static boolean PRINT_LOG_I = true;
	private static boolean PRINT_LOG_D = true;
	private static boolean PRINT_LOG_E = true;
	private static boolean PRINT_LOG_TEST = false;

	/* ***************************************************************
	 * Log Output Method
	 */
	public static void setTAG(String tag){
		TAG = tag;
	}

	public static void i(String title, String info) {
		if (PRINT_LOG_I) {
			Log.i(TAG, title +"#" + info);
		}
	}

	public static void d(String title, String info) {
		if (PRINT_LOG_D) {
			Log.i(TAG, title +"#" + info);
		}
	}

	public static void e(String title, String info) {
		if (PRINT_LOG_E) {
			Log.i(TAG, title +"#" + info);
		}
	}

	public static void test_output(String info) {
		if (PRINT_LOG_TEST) {
			Log.i(TAG, "<Unit Test> : " + info);
		}
	}
}
