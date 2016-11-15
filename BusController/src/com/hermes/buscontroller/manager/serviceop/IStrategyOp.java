/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.manager.serviceop;

/**
 * XxxStrategyOperator call features of ServiceManager through this interface.<br>
 * 
 * @author KevinZeng
 * 
 */
public interface IStrategyOp {
	int requestEvent(int clientIdentifier, int requestType, int requestOp);
}
