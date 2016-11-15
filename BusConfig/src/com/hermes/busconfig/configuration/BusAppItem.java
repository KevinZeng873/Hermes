/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busconfig.configuration;

/**
 *
 * @author KevinZeng
 *
 */
public enum BusAppItem {

	/* ************************************************************************
	 * Constants Definition for all application supported in Hermes BUS
	 */
	/**
	 * <Launcher Application>
	 */
	APP_LAUNCHER("com.hermes.bus.requestapp.LAUNCHER", 1001, 0x1 + 0x2 + 0x4 + 0x8),
	/**
	 * <Radio Application>
	 */
	APP_RADIO("com.hermes.bus.requestapp.RADIO", 1002, 0x1 + 0x2 + 0x4 + 0x8),
	/**
	 * <Music Application>
	 */
	APP_MUSIC("com.hermes.bus.requestapp.MUSIC", 1003, 0x1 + 0x2 + 0x4 + 0x8),
	

	/* ************************************************************************
	 * Members
	 */
	private String mAppName;
	private int mAppIdentifier;
	/**
	 * 0(0x01) - log.i <br>
	 * 1(0x02) - log.d <br>
	 * 2(0x04) - log.w <br>
	 * 3(0x08) - enable crash handling <br>
	 */
	private long mAppSettings;

	/* ************************************************************************
	 * Methods
	 */
	private BusAppItem(String appName, int appIdentifier, long settings) {
		this.mAppName = appName;
		this.mAppIdentifier = appIdentifier;
		this.mAppSettings = settings;
	}

	/* ************************************************************************
	 * Functions
	 */
	public String getName() {
		return mAppName;
	}

	public int getIdentifier() {
		return mAppIdentifier;
	}

	public long getAppSettings() {
		return mAppSettings;
	}

	public boolean isLogiEnable() {
		return ((mAppSettings & 0x1) == 0) ? false : true;
	}

	public boolean isLogdEnable() {
		return ((mAppSettings & 0x2) == 0) ? false : true;
	}

	public boolean isLogwEnable() {
		return ((mAppSettings & 0x4) == 0) ? false : true;
	}

	public boolean isCrashHandling() {
		return ((mAppSettings & 0x8) == 0) ? false : true;
	}
	}
