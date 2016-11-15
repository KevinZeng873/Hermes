package com.hermes.busapi.activity;

import com.hermes.busapi.log.BusLogger;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author KevinZeng
 *
 */
public class LogicActivity extends Activity{
	private static final String TAG = "ACTIVITY";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BusLogger.d(TAG, "onCreate");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		BusLogger.d(TAG, "onResume");
	}
	
	@Override 
	protected void onPause(){
		super.onPause();
		BusLogger.d(TAG, "onPause");
	}
	
	@Override 
	protected void onStop(){
		super.onStop();
		BusLogger.d(TAG, "onStop");
	}
}
