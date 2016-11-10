/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config.configuration;

/**
 * This is the pre-defined item which is the type of <br>
 * item data notification.<br>
 * 
 * @author KevinZeng
 * 
 */
public enum SubscribedItem {
    /*
     * CANBUS Service provides
     */
	DATA_SAMPLE_1(10001, "Sample 1"),
    DATA_SAMPLE_2(10002, "Sample 2");


	/* ************************************************************************
	 * Members
	 */
	private int mItemIndex;
	private String mItemDescription;

	/* ************************************************************************
	 * Methods
	 */
	/**
	 * Never use this constructor<br>
	 */
	private SubscribedItem() {

	}

	/**
	 * Constructor
	 */
	private SubscribedItem(int itemIndex, String itemDesc) {
		this.mItemIndex = itemIndex;
		this.mItemDescription = itemDesc;
	}

	/* ************************************************************************
	 * Functions
	 */
	public int getIndex() {
		return mItemIndex;
	}

	public String getDescription() {
		return mItemDescription;
	}

}
