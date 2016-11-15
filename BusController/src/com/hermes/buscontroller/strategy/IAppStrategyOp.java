/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.strategy;

import android.content.Context;
import android.view.KeyEvent;

/**
 * Consumers call AppStrategyManager's functions through this interface
 * 
 * @author KevinZeng
 * 
 */
public interface IAppStrategyOp {
	/**
	 * only perform once!
	 * @param context
	 */
	void init(Context context);
	

}
