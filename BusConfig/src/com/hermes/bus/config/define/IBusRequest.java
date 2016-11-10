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
public interface IBusRequest {
	/* ************************************************************************
	 */
	public interface ISrcOp {
		final int REQUEST_SRC_STATUS = 0x00000101;
		final int REQUEST_SRC_LOSE = 0x00000102;
		final int REQUEST_SRC_OBTAIN = 0x00000103;
	}

}
