/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config.configuration;

/**
 * @author KevinZeng
 * 
 */
public class BusConfiguration {
	/**
	 * <<APPLICTION LEVEL>><br>
	 * 1. Connect<br>
	 * Define all applications which need connecting to Bus Controller<br>
	 */
	private static BusAppItem[] sAppNeedConnecting = new BusAppItem[] {
            BusAppItem.APP_LAUNCHER,
            BusAppItem.APP_MUSIC,
            BusAppItem.APP_RADIO
    };
	/**
	 * <<CLIENT LEVLE>><br>
	 * 1. Connect<br>
	 * 2. Client Data Notification<br>
	 * Define all clients which need connecting to Bus Controller<br>
	 */
	private static BusClientItem[] sClientNeedConnecting = new BusClientItem[] {
            BusClientItem.CLIENT_APP_LAUNCHER,
            BusClientItem.CLIENT_APP_LOCALMUSIC,
            BusClientItem.CLIENT_APP_LOCALRADIO
    };


	private static SubscribedItem[] sItemDataNotifys = new SubscribedItem[] {
            SubscribedItem.DATA_SAMPLE_1,
            SubscribedItem.DATA_SAMPLE_2
    };

	/* ************************************************************************
	 * Methods
	 */
	private BusConfiguration() {

	}

	/* ************************************************************************
	 * Function
	 */
	/**
	 * Return all application system predefined<br>
	 * 
	 * @return
	 */
	public static BusAppItem[] getConfigAppList() {
		return sAppNeedConnecting;
	}

	/**
	 * Return all clients system predefined<br>
	 * 
	 * @return
	 */
	public static BusClientItem[] getConfigClientList() {
		return sClientNeedConnecting;
	}

	/**
	 * Return all type items for item data notification.<br>
	 * 
	 * @return
	 */
	public static SubscribedItem[] getConfigItemDataNotifyItems() {
		return sItemDataNotifys;
	}

}
