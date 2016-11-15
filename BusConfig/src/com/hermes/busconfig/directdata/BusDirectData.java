/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busconfig.directdata;

import com.hermes.busconfig.configuration.BusClientItem;

/**
 * 
 * @author KevinZeng
 * 
 */
public class BusDirectData {
	/* ************************************************************************
	 * Message-Sent Definition supported in Hermes BUS
	 */
	private static final int MSGSENT_BASE_APP = 0x00000001;
	private static final int MSGSENT_BASE_SERV = 0x01000001;
    private static final int MSGSENT_BASE_MID_SERV = 0x02000001;

    private static final int DIRECTDATA_SCOPE = 0x2FF;

	

	/* ************************************************************************
	 * Functions
	 */

	/**
	 * 
	 * @param messageSent
	 * @return
	 */
	public static int getClientIdentiferFromMessageIn(int messageSent) {
		

		// client not found
		return 0;
	}

}
