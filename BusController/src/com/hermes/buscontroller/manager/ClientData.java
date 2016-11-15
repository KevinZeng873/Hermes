/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.buscontroller.manager;

import com.hermes.busconfig.IBusListener;
import com.hermes.busconfig.configuration.BusClientItem;


/**
 * @author KevinZeng
 */
class ClientData {

    /* ************************************************************************
     * Members
     */
    private String mPackageName;
    private BusClientItem mClientItem;
    private int mClientId;
    private IBusListener mClientListener;

    /**
     * everything but not mPackageName!!!
     */
    private void initData() {
        mClientId = 0;
        mClientListener = null;
    }

    private boolean isInitStatus() {
        if (0 == mClientId &&
                null == mClientListener) {
            return true;
        }
        return false;
    }

    /* ************************************************************************
     * Set/Get Operation
     */
    public ClientData(String packageName, BusClientItem clientItem) {
        initData();
        mClientItem = clientItem;
        mPackageName = packageName;
    }

    public ClientData(String packageName, BusClientItem clientItem, int clientId) {
        initData();
        mClientItem = clientItem;
        mPackageName = packageName;
        mClientId = clientId;
    }

    public BusClientItem getClientItem() {
        return mClientItem;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setClientId(int clientId) {
        mClientId = clientId;
    }

    public int getClientId() {
        return mClientId;
    }

    public void setAppListener(IBusListener listener) {
        mClientListener = listener;
    }

    public IBusListener getIAppListener() {
        return mClientListener;
    }

	/* ************************************************************************
     * Functions
	 */

    /**
     * clear all data but PackageName
     */
    public void setDead() {
        initData();
    }

    /**
     * answer the question - is this item dead?
     *
     * @return
     */
    public boolean isDead() {
        return isInitStatus();
    }

}
