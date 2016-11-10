/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config.feature;

/**
 * 
 * @author KevinZeng
 * 
 */
public class BusKeeperFeatureConfig {

	/* ************************************************************************
	 * Configurations
	 */
	/**
	 * Enable the function to send error report to TSP
	 */
	public static final boolean BUSKEEPER_ENABLE_ERRORREPORTER = false;
	/**
	 * Enable the function to send a first report to TSP
	 */
	public static final boolean BUSKEEPER_ENABLE_FIRSTREPORTER = false;
	/**
	 * Enable the function to send the tracking report to TSP
	 */
	public static final boolean BUSKEEPER_ENABLE_TRACKREPORTER = false;
}
