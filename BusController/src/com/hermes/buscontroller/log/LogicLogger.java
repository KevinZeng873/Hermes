/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.log;

import android.util.Log;

/**
 * 
 * @author KevinZeng
 *
 */
public class LogicLogger {
	private static final String TAG = "HBCS";
	private static boolean PRINT_LOG_I = true;
	private static boolean PRINT_LOG_D = false;
	private static boolean PRINT_LOG_E = true;
	
	public static void setLogOutputEnable(boolean enable){
		LogicLogger.PRINT_LOG_D = enable;
		LogicLogger.PRINT_LOG_E = enable;
		LogicLogger.PRINT_LOG_I = enable;
	}
	
	public static void i(String title, String info){
		if(LogicLogger.PRINT_LOG_I){
			Log.i(TAG, title + " @ " +info);
		}
	}
	public static void d(String title, String info){
		if(LogicLogger.PRINT_LOG_D){
			Log.d(TAG, title + " @ " +info);
		}
	}
	public static void e(String title, String info){
		if(LogicLogger.PRINT_LOG_E){
			Log.e(TAG, title + " @ " +info);
		}
	}
}
