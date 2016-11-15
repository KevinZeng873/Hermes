/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */


package com.hermes.busapi.crash;

import android.content.Context;

/**
 * 
 * @author KevinZeng
 *
 */
public class CrashHandlingManager {
	

	/* ************************************************************************
	 * Single Instance
	 */
	private static class CrashHandlingManagerFactory{
		private static CrashHandlingManager instance = new CrashHandlingManager();
	}
	
	private CrashHandlingManager(){
		
	}
	
	public static CrashHandlingManager getInstance(){
		return CrashHandlingManagerFactory.instance;
	}
	
	/* ************************************************************************
	 * Functions
	 */
	public boolean init(Context context){
		if(null == context){
			return false;
		}
		
		CrashHandler handler = CrashHandler.getInstance();
		Thread.setDefaultUncaughtExceptionHandler(handler);

		return true;
	}
}
