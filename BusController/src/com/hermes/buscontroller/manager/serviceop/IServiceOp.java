/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.manager.serviceop;

import com.hermes.busconfig.IBusConnection;

import android.content.Context;


/**
 * 
 * @author KevinZeng
 * 
 */
public interface IServiceOp {
	public void init(Context context);

	/**
	 * Get the stub of connection which provider client with features of hermes bus<br>
	 * 
	 * @return
	 */
	public IBusConnection.Stub getCoreAppConnectionStub();

	/**
	 * 
	 * @param packageName
	 * @param wakeupagain - wake up this app again
	 */
	public void removeBindedAppClients(String packageName, boolean wakeupagain);

	/**
	 * Wake up the api daemon service in app
	 */
	public void wakeupDeamonOfApplications();

	/**
	 *
	 * @param clientIdentifier
	 * @return
     */
	public boolean isClientDead(int clientIdentifier);

}
