/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config;

import com.hermes.bus.config.communication.OverProcessMessage;

/**
 * This AIDL provide remote call between two clients
 * Author: KevinZeng
 */
interface IClientRemoteCallClient {
	/**
	 *
	 */
	OverProcessMessage callClient(int fromClientIdentifier, int toClientIdentifier, in OverProcessMessage opMsg);
}
