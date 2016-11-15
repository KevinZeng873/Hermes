package com.hermes.busconfig;

import com.hermes.busconfig.communication.OverProcessMessage;

/**
 * This AIDL provide remote call between two clients
 * Author: KevinZeng
 */
interface IRemoteCall {
	/**
	 *
	 */
	OverProcessMessage callClient(int fromClientIdentifier, int toClientIdentifier, in OverProcessMessage opMsg);
}
