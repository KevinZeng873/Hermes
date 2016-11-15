/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */


package com.hermes.busapi.log;

import android.util.Log;


public class BusLogger {

	private static String TAG = "BUSAPI";
	private static boolean PRINT_LOG_I = true;
	private static boolean PRINT_LOG_D = true;
	private static boolean PRINT_LOG_E = true;
	private static boolean PRINT_LOG_W = true;
	private static boolean PRINT_LOG_TEST = false;
	
	/* ***************************************************************
	 * Enable/Disable
	 */
	public static void enableLogi(boolean b){
		BusLogger.PRINT_LOG_I = b;
	}
	
	public static void enableLogd(boolean b){
		BusLogger.PRINT_LOG_D = b;
	}
	
	public static void enableLogw(boolean b){
		BusLogger.PRINT_LOG_W = b;
	}

	/* ***************************************************************
	 * Log Output Method
	 */
	public static void setTAG(String tag){
		TAG = tag;
	}

	public static void i(String title, String info) {
		if (PRINT_LOG_I) {
			Log.i(TAG, title +"@" + info);
		}
	}

	public static void d(String title, String info) {
		if (PRINT_LOG_D) {
			Log.d(TAG, title +"@" + info);
		}
	}

	public static void w(String title, String info) {
		if (PRINT_LOG_W) {
			Log.w(TAG, title +"@" + info);
		}
	}
	
	public static void e(String title, String info) {
		if (PRINT_LOG_E) {
			Log.e(TAG, title +"@" + info);
		}
	}

	public static void test_output(String info) {
		if (PRINT_LOG_TEST) {
			Log.d(TAG, "UNITTEST@" + info);
		}
	}
}
