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
public interface IBusAppServiceDef {
	final String BUSSERVICE_PACKAGENAME = "com.hermes.bus.buscontroller";
	final String BUSSERVICE_SERVICENAME = "com.hermes.bus.buscontroller.BusService";
	/**
	 * When client connects App, it needs provide its identifier name.<br>
	 * This string is the Key bundled in string extra.<br>
	 */
	final String APPSERVICE_APPLICATION_PACKAGENAME = "ApplicationPackageName";
	final String APPSERVICE_APPLICATION_IDENTIFIERNAME = "ApplicationIdentifierName";

	public interface IAppServiceRtnValue {
		final int SERVICE_RETURN_SUCCEED = 0x00000000;
		final int SERVICE_RETURN_ERROR_UNKNOWN = 0x00000001;
	}

	/* ************************************************************************
	 * Type  to ask clients
	 */
	public interface IRequestEventType {
		/**
		 * tells client the functional operation
		 */
		final int REQUESTTYPE_FUNCTION = 0x00000001;
		/**
		 * tells client to get the relevant status of client.
		 * .
		 */
		final int REQUESTTYPE_GETINFO = 0x00000002;
		/**
		 * tells client the client operation.
		 */
		final int REQUESTTYPE_CLIENT = 0x00000003;
	}

	/* ************************************************************************
	 * Request operation to ask
	 */
	public interface IRequestOperation {
		/* ********************************************************************
		 * Operations for REQUESTTYPE_FUNCTION
		 */


		/* ********************************************************************
		 * Operations for REQUESTTYPE_GETINFO
		 */

		/* ********************************************************************
		 * Operations for REQUESTTYPE_CLIENT
		 */
	}

	/* ************************************************************************
	 * The return value
	 */
	public interface ICoreRequestReturnValue {
		/**
		 * No defined
		 */
		final int REQUESTOP_RETURN_UNDEFINED = 0x00000000;
		/**
		 * Cannot find this client
		 */
		final int REQUESTOP_RETURN_CLIENT_NOT_AVAILABLE = 0x00000001;
		/**
		 * tells that the answer is YES
		 */
		final int REQUESTOP_RETURN_YES = 0x00000002;
		/**
		 *  tells that the answer is NO
		 */
		final int REQUESTOP_RETURN_NO = 0x00000003;
		/**
		 * tells that nothing need worrying and
		 * nothing need doing.
		 */
		final int REQUESTOP_RETURN_NEGATIVE = 0x00000004;

	}
}
