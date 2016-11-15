/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busconfig.remotecall;

import com.hermes.busconfig.configuration.BusClientItem;

/**
 * Utilities class for remote call directly between two clients
 * 
 * @author KevinZeng
 * 
 */
public class RemoteCallConfig {

	/* ************************************************************************
	 * Message-In Definition supported in Hermes BUS
	 */
	private static final int MSGIN_BASE_APP = 0x00000001;
	private static final int MSGIN_BASE_SERV = 0x01000001;
    private static final int MSGIN_BASE_MID_SERV = 0x01000001;

    private static final int DIRECTDATA_SCOPE = 0x2FF;

	

	/* ************************************************************************
	 * Message-Return Definition supported in hermes BUS
	 */
	private static final int MSGRTN_BASE = 0x01000001;

	/*
	 * <Common Message-Return>
	 */
	public static final int MSGRTN_COMMON_BASE = MSGRTN_BASE + 1;
	/**
	 * ERROR<br>
	 * The client, which want to do a remote call, is not connected to hermes BUS.
	 */
	public static final int MSGRTN_COMMON_ERR_NO_ACCESS_BUS = MSGRTN_COMMON_BASE + 1;
	/**
	 * ERROR<br>
	 * The client being remote-called is not available in hermes BUS.
	 */
	public static final int MSGRTN_COMMON_ERR_NO_CLIENT = MSGRTN_COMMON_BASE + 2;
	/**
	 * ERROR<br>
	 * This message-in is not supported.
	 */
	public static final int MSGRTN_COMMON_ERR_MSGIN_NOT_SUPPORT = MSGRTN_COMMON_BASE + 3;
	/**
	 * ERROR<br>
	 * Exception happens
	 */
	public static final int MSGRTN_COMMON_ERR_EXCEPTION = MSGRTN_COMMON_BASE + 4;
	/**
	 * ERROR<br>
	 * Unknown
	 */
	public static final int MSGRTN_COMMON_ERR_UNKNOWN = MSGRTN_COMMON_BASE + 5;
	/**
	 * ERROR<br>
	 * The callee client doesn't support this message
	 */
	public static final int MSGRTN_COMMON_ERR_CLIENT_NOT_SUPPORT_MSG = MSGRTN_COMMON_BASE + 6;

	/**
	 * SUCCESS<br>
	 * return an integer value
	 */
	public static final int MSGRTN_COMMON_SUC_INT = MSGRTN_COMMON_BASE + 100;
	/**
	 * SUCCESS<br>
	 * return an string value
	 */
	public static final int MSGRTN_COMMON_SUC_STRING = MSGRTN_COMMON_BASE + 101;
	/**
	 * SUCCESS<br>
	 * return an string array
	 */
	public static final int MSGRTN_COMMON_SUC_STRINGLIST = MSGRTN_COMMON_BASE + 102;

	/* ************************************************************************
	 * Functions
	 */
	/**
	 * 
	 * @param messageIn
	 * @return 0 means no client found
	 */
	public static int getClientIdentiferFromMessageIn(int messageIn) {
		

		// client not found
		return 0;
	}
}
