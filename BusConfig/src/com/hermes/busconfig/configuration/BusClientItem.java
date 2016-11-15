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
 * BusClientItem is the pre-defined item for all clients which are<br>
 * controlled by Bus Controller.<br>
 * 
 * @author KevinZeng
 * 
 */
public enum BusClientItem {

	/* ************************************************************************
	 * Constant definition for all clients supported in Hermes BUS
	 */
	/**
	 * <Local Music Client>
	 */
	CLIENT_APP_LOCALMUSIC("Hermes.App.Client.LocalMusic", 5001),
	/**
	 * <Local Radio Client>
	 */
	CLIENT_APP_LOCALRADIO("Hermes.App.Client.LocalRadio", 5002),
	/**
	 * <Launcher Client>
	 */
	CLIENT_APP_LAUNCHER("Hermes.App.Client.Launcher", 5003),



	/* ************************************************************************
	 * Member
	 */
	private String mClientName;
	private int mCliendIdentifier;

	/* ************************************************************************
	 * Methods
	 */
	/**
	 * Never use this constructor<br>
	 */
	private BusClientItem() {

	}

	/**
	 * Constructor<br>
	 * 
	 * @param clientName
	 * @param clientIdentifier
	 */
	private BusClientItem(String clientName, int clientIdentifier) {
		this.mClientName = clientName;
		this.mCliendIdentifier = clientIdentifier;
	}

	/* ************************************************************************
	 * Functions
	 */
	public String getName() {
		return mClientName;
	}

	public int getIdentifier() {
		return mCliendIdentifier;
	}
}
