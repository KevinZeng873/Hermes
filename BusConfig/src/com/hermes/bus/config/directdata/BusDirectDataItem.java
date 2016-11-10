/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config.directdata;

import com.hermes.bus.config.configuration.BusClientItem;

/**
 * 
 * @author KevinZeng
 * 
 */
public class BusDirectDataItem {
	/* ************************************************************************
	 * Message-Sent Definition supported in hermes BUS
	 */
	private static final int MSGSENT_BASE_APP = 0x00000001;
	private static final int MSGSENT_BASE_SERV = 0x01000001;
    private static final int MSGSENT_BASE_MID_SERV = 0x02000001;

    private static final int DIRECTDATA_SCOPE = 0x2FF;
	/*
	 * <Launcher>
	 */
	public interface ICoreDirectDataLauncher {
		/** Message Begin */
		public static final int MSGSENT_LAUNCHER_BASE = MSGSENT_BASE_APP + DIRECTDATA_SCOPE * 0;

		/** Extend Message Begin */
		public static final int MSGSENT_LAUNCHER_EXTEND_BASE = MSGSENT_BASE_APP + DIRECTDATA_SCOPE * 1;

		/** Message End */
		public static final int MSGSENT_LAUNCHER_END = MSGSENT_BASE_APP + DIRECTDATA_SCOPE * 2 - 1;
	}

	/* ************************************************************************
	 * Functions
	 */

	/**
	 * 
	 * @param messageSent
	 * @return
	 */
	public static int getClientIdentiferFromMessageIn(int messageSent) {
		if (ICoreDirectDataLauncher.MSGSENT_LAUNCHER_BASE < messageSent
				&& messageSent < ICoreDirectDataLauncher.MSGSENT_LAUNCHER_END) {
			return BusClientItem.CLIENT_APP_LAUNCHER.getIdentifier();
		}
		// client not found
		return 0;
	}

}
