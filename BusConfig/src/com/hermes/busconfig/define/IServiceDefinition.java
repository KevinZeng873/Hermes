/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busconfig.define;

/**
 * 
 * @author KevinZeng
 * 
 */
public interface IServiceDefinition {
	final String BUSCONTROLLER_PACKAGENAME = "com.hermes.buscontroller";
	final String BUSCONTROLLER_SERVICENAME = "com.hermes.buscontroller.BusControllerService";
	/**
	 * When client connects CoreApp, it needs provide its identifier name.<br>
	 * This string is the Key bundled in string extra.<br>
	 */
	final String COREAPPSERVICE_APPLICATION_PACKAGENAME = "ApplicationPackageName";
	final String COREAPPSERVICE_APPLICATION_IDENTIFIERNAME = "ApplicationIdentifierName";

	public interface ICoreAppServiceRtnValue {
		final int SERVICE_RETURN_SUCCEED = 0x00000000;
		final int SERVICE_RETURN_ERROR_UNKNOWN = 0x00000001;
	}

	/* ************************************************************************
	 * Type for Bus Controller to ask clients
	 */
	public interface ICoreRequestEventType {
		/**
		 * Bus Controller tells client the functional operation
		 */
		final int REQUESTTYPE_FUNCTION = 0x00000001;
		/**
		 * Bus Controller tells client to get the relevant status of client.
		 *
		 */
		final int REQUESTTYPE_GETINFO = 0x00000002;
		/**
		 * Bus Controller tells client the client operation.
		 */
		final int REQUESTTYPE_CLIENT = 0x00000003;

	}

	/* ************************************************************************
	 * Request operation for Hermes Bus to ask
	 */
	public interface ICoreRequestOperation {
		/* ********************************************************************
		 * Operations for REQUESTTYPE_FUNCTION
		 */
		/**
		 * Service asks client to perform PREVIOUS function
		 */
		final int REQUESTOP_FUNCTION_PREV = 0x00000001;
		/**
		 * Service asks client to perform NEXT function
		 */
		final int REQUESTOP_FUNCTION_NEXT = 0x00000002;
		/**
		 * Service asks client to perform PLAY function
		 */
		final int REQUESTOP_FUNCTION_PLAY = 0x00000003;
		/**
		 * Service asks client to perform STOP function
		 */
		final int REQUESTOP_FUNCTION_STOP = 0x00000004;

		/**
		 * Service tells client to perform REWIND operation
		 */
		final int REQUESTOP_FUNCTiON_REWIND = 0x00000005;
		/**
		 * Service tells client to perform FORWARD operation
		 */
		final int REQUESTOP_FUNCTION_FORWARD = 0x00000006;

		/* ********************************************************************
		 * Operations for REQUESTTYPE_GETINFO
		 */
		/**
		 * Service asks client if its UI is on screen.
		 */
		final int REQUESTOP_GETINFO_IS_OPEN_UI_ON_SCREEN = 0x00000101;
		/**
		 * Service asks client if it's on PLAY function.
		 */
		final int REQUESTOP_GETINFO_IS_PLAY = 0x00000102;
		/**
		 * Service asks client if it's on STOP function.
		 */
		final int REQUESTOP_GETINFO_IS_STOP = 0x00000103;
		/**
		 * Service asks the client if it's the current SRC source <for SRC
		 * strategy>
		 */
		final int REQUESTOP_GETINFO_IS_SRC_FOCUS = 0x00000104;
		/**
		 * Service asks the client if it occupy the MEDIA <for MEDIA BUTTON
		 * strategy>
		 */
		final int REQUESTOP_GETINFO_IS_MEDIA_FOCUS = 0x00000105;

		/* ********************************************************************
		 * Operations for REQUESTTYPE_CLIENT
		 */
		/**
		 * Service asks client to show its UI
		 */
		final int REQUESTOP_CLIENT_OPEN_UI_ON_SCREEN = 0x00000201;
		/**
		 * Service tells client that it get the SRC focus
		 */
		final int REQUESTOP_CLIENT_SRC_GET_FOCUS = 0x00000202;
		/**
		 * Service tells client that it lose the SRC focus
		 */
		final int REQUESTOP_CLIENT_SRC_LOSE_FOCUS = 0x00000203;
		/**
		 * This is only a heart beat operation, client doesn't need respond
		 */
		final int REQUESTOP_CLIENT_HEARTBEAT_CHECK = 0x00000204;
		/**
		 * Service tells client that it receive the media button event
		 */
		final int REQUESTOP_CLIENT_MEDIA_BUTTON_ACTION = 0x00000205;
	}

	/* ************************************************************************
	 * The return value for Hermes Bus to send back to Hermes Bus
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
		 * Api tells caller that the answer is YES
		 */
		final int REQUESTOP_RETURN_YES = 0x00000002;
		/**
		 * Api tells caller that the answer is NO
		 */
		final int REQUESTOP_RETURN_NO = 0x00000003;
		/**
		 * Api tells caller that nothing need worrying and
		 * nothing need doing.
		 */
		final int REQUESTOP_RETURN_NEGATIVE = 0x00000004;

	}
}
