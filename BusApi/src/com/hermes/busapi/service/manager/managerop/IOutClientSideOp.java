/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busapi.service.manager.managerop;

import android.os.Message;

/**
 * 
 * @author KevinZeng
 * 
 */
public interface IOutClientSideOp {
	Message remoteDirectCall(int calleeClientIdentifier,
			int callerClientIdentifier, Message msgIn);
}
