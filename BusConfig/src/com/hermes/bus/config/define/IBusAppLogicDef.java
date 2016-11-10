/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config.define;

/**
 * 
 * @author KevinZeng
 * 
 */
public interface IBusAppLogicDef {
	/* ************************************************************************
	 */
	public interface IAppRequestEventType {
		/**
		 * Client want to tell CoreAppService about its info
		 */
		final int APP_REQUEST_TYPE_WRITEINFO = 0x00000001;
		/**
		 * Client want to know the certain information from bus controller <br>
		 * in synchronized way<br>
		 */
		final int APP_REQUEST_TYPE_GETINFO_SYNCRONIZED = 0x00000002;
		/**
		 * Client want to get notification from CoreAppService <br>
		 * in periodically way<br>
		 */
		final int APP_REQUEST_TYPE_GETNOTIFICATION = 0x00000003;

	}

	/* ************************************************************************
	 */
	public interface IAppRequestEventOp {
		/* ********************************************************************
		 * type: WRITEINFO
		 */
		final int APP_REQUEST_OP_WRITEINFO_INFO = 0x00000001;
		final int APP_REQUEST_OP_WRITEINFO_CRASH = 0x00000002;
		/* ********************************************************************
		 * type: GETINFO_SYNCHRONIZED
		 */
		/* ********************************************************************
		 * type: GETNOTIFICATION
		 */
	}
}
