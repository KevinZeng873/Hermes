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
public class BusConfigManager {
	/**
	 */
	private static BusAppItem[] sAppNeedConnecting = new BusAppItem[] {
			BusAppItem.APP_LAUNCHER,
			BusAppItem.APP_RADIO,
			BusAppItem.APP_MUSIC
    };
	/**
	 */
	private static BusClientItem[] sClientNeedConnecting = new BusClientItem[] {
            BusClientItem.CLIENT_APP_LAUNCHER,
            BusClientItem.CLIENT_APP_LOCALMUSIC
    };

	/**
	 * Define all clients which supports SRC operation<br>
	 */
	private static BusClientItem[] sSrcClients = new BusClientItem[] {
	    BusClientItem.CLIENT_APP_LOCALMUSIC,
        BusClientItem.CLIENT_APP_LOCALRADIO
    };

	/**
	 */
	private static BusData_Item[] sItemDataNotifys = new BusData_Item[] {
            
    };

	/* ************************************************************************
	 * Methods
	 */
	private BusConfigManager() {

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
	 * Return all clients which want to receive SRC event<br>
	 * 
	 * @return
	 */
	public static BusClientItem[] getConfigDefinedSrcClients() {
		return sSrcClients;
	}

	/**
	 * Return all type items for item data notification.<br>
	 * 
	 * @return
	 */
	public static BusData_Item[] getConfigItemDataNotifyItems() {
		return sItemDataNotifys;
	}

}
