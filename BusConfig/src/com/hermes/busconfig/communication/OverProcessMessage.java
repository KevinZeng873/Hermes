/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busconfig.communication;

import java.util.ArrayList;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class can be transfer crossing different processes through AIDL<br>
 * 
 * @author KevinZeng
 * 
 */
public final class OverProcessMessage implements Parcelable {

	/* ************************************************************************
	 * Members
	 */
	public int what;

	public int arg1;

	public int arg2;

	public Bundle obj = null;
	/**
	 * flag for send operation<br>
	 */
	public boolean mSend = false;
	/**
	 * Object for synchronization.<br>
	 */
	private static Object mPoolSync = new Object();

	/**
	 * Pool for message in order to make performance and memory allocation<br>
	 * better.
	 */
	private static ArrayList<OverProcessMessage> mPool = new ArrayList<OverProcessMessage>();

	/* ************************************************************************
	 * Methods
	 */

	private OverProcessMessage() {

	}

	protected void readFromParcel(Parcel source) {
		what = source.readInt();
		arg1 = source.readInt();
		arg2 = source.readInt();
		mSend = source.readInt() == 0 ? false : true;

		if (source.readInt() != 0) {
			obj = source.readParcelable(getClass().getClassLoader());
		}
	}

	/* ************************************************************************
	 * Functions
	 */
	/**
	 * Return a new Message instance from the global pool. <br>
	 * Allows us to avoid allocating new objects in many cases.<br>
	 */
	public static OverProcessMessage Obtain() {
		synchronized (mPoolSync) {
			if (mPool.isEmpty()) {
				return new OverProcessMessage();
			} else {
				return mPool.remove(0);
			}
		}
	}

	public static OverProcessMessage ObtainWithData() {
		OverProcessMessage m = OverProcessMessage.Obtain();
		m.obj = new Bundle();
		return m;
	}

	public static OverProcessMessage Obtain(OverProcessMessage orig) {
		OverProcessMessage m = OverProcessMessage.Obtain();
		m.Clone(orig);
		return m;
	}

	/**
	 * Create a new over process message based on the input message<br>
	 * 
	 * @param orig
	 * @return
	 */
	public static OverProcessMessage CopyObtain(Message orig) {
		OverProcessMessage m = OverProcessMessage.Obtain();
		m.arg1 = orig.arg1;
		m.what = orig.what;
		m.arg2 = orig.arg2;
		if (orig.obj != null) {
			m.obj = new Bundle((Bundle) orig.obj);// Clone
		}
		return m;
	}

	/**
	 * Clone a over process message to itself<br>
	 * 
	 * @param orig
	 */
	public void Clone(OverProcessMessage orig) {
		this.arg2 = orig.arg2;
		this.arg1 = orig.arg1;
		this.what = orig.what;
		this.mSend = orig.mSend;
		if (orig.obj != null)
			this.obj = new Bundle(orig.obj);
	}

	public static void Return(OverProcessMessage msg) {
		synchronized (mPoolSync) {
			if (msg.obj != null)
				msg.obj = null;
			msg.what = -1;
			msg.arg1 = -1;
			msg.arg2 = -1;
			msg.mSend = false;
			if (mPool.size() < 100)
				mPool.add(msg);
		}
	}

	public static void Clearall() {
		synchronized (mPoolSync) {
			mPool.clear();
		}
	}

	// sometimes we store linked lists of these things

	public static final Parcelable.Creator<OverProcessMessage> CREATOR = new Parcelable.Creator<OverProcessMessage>() {
		public OverProcessMessage createFromParcel(Parcel source) {
			OverProcessMessage msg = new OverProcessMessage();
			msg.readFromParcel(source);
			return msg;
		}

		public OverProcessMessage[] newArray(int size) {
			// TODRemoteMessagerated method stub
			return new OverProcessMessage[size];
		}

	};

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;

	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(what);
		dest.writeInt(arg1);
		dest.writeInt(arg2);
		dest.writeInt(mSend ? 1 : 0);
		if (obj != null) {
			try {
				Parcelable p = (Parcelable) obj;
				dest.writeInt(1);
				dest.writeParcelable(p, flags);
			} catch (ClassCastException e) {
				throw new RuntimeException(
						"Can't marshal non-Parcelable objects across processes.");
			}
		} else {
			dest.writeInt(0);
		}
	}
}
