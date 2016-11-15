/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */


package com.hermes.busapi.service.manager.managerop;

import com.hermes.busconfig.configuration.BusAppItem;


/**
 * Call features of BusAppManager from application side
 * 
 * @author KevinZeng
 * 
 */
public interface IAppSideOp {
	/**
	 *
	 * @param identifierName
	 */
	void setAppIdentifierItem(BusAppItem appItem);
	
	BusAppItem getAppIdentifierItem();

}
