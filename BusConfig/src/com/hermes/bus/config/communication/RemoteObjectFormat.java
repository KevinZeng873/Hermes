/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */
package com.hermes.bus.config.communication;

import android.os.Bundle;

public interface RemoteObjectFormat {

	public final static byte NotSupporyt_Type = 0;

	public final static byte Int_Type = 1;

	public final static byte String_Type = 2;

	public final static String FormatKey = "Format";

	public final static byte REMOTE_UNKNOWN = 0;

	public final static byte REMOTE_BYTELIST = 1;

	public final static byte REMOTE_STRINGLIST = 2;

	public final static byte REMOTE_INTLIST = 3;

	public final static byte REMOTE_STRING = 4;

	public final static byte REMOTE_INT = 5;

	public final static byte REMOTE_BYTE = 6;

	public final static byte REMOTE_BASECOMBINE = 7;

	public final static byte REMOTE_COMBINELIST = 8;

	public final static byte REMOTE_APPLIST = 9;

	public final static String BYTELIST_KeyName = "byte[]";

	public final static String STRINGLIST_KeyName = "String[]";

	public final static String INTLIST_KeyName = "int[]";

	public final static String STRING_KeyName = "String";

	public final static String INT_KeyName = "int";

	public final static String BYTE_KeyName = "byte";

	public final static String COMBINLIST_KeyName = "CombOBJ[]";

	public final static String APPLIST_KeyName = "App[]";

	public final static String TSP_CityName = "citynamte";

	public final static String TSP_Latitude = "latitude";

	public final static String TSP_Longitude = "longitude";

	public final static String TSP_ProName = "ProName";

	/* ************************************************************************
	 * Key definition for TBT - TBT
	 */
	public final static String TBTTBT_TYPE = "type";
	public final static String TBTTBT_VALUE = "value";
	public final static String TBTTBT_LINKNAME = "linkname";
	public final static String TBTTBT_TURNROAD = "turnroadname";

	/* ************************************************************************
	 * Key definition for TBT - Location
	 */
	public final static String TBTLOC_TIMESTAMP = "timestamp";
	public final static String TBTLOC_BEAR = "bear";
	public final static String TBTLOC_SPEED = "speed";
	public final static String TBTLOC_LONGITUDE = "longitude";
	public final static String TBTLOC_LATITUDE = "latitude";
	public final static String TBTLOC_RAWLONGITUDE = "rawLongitude";
	public final static String TBTLOC_RAWLATITUDE = "rawLatitude";
	public final static String TBTLOC_ALTITUDE = "altitude";
	public final static String TBTLOC_ACCURACY = "accuracy";

	/**
	 * For TBT - TBT message
	 * 
	 */
	public class TbtTbtMessageBody {
		public int mType;
		public int[] mValue;
		public String mLinkName;
		public String mTurnRoadName;
	}

	/**
	 * For TBT - Location message
	 * 
	 */
	public class TbtLocationMessageBody {
		public long mTimeStamp;
		public int mBear;
		public int mSpeed;
		public double mLongitude;
		public double mLatitude;
		public double mRawLongitude;
		public double mRawLatitude;
		public double mAltidue;
		public float mAccuracy;
	}

	public class MessageBody {
		public int what;

		public int arg1;

		public int arg2;

		public byte[] obj;
	}

	public class MessageBody1 {
		public int arg1;

		public int arg2;

		public int arg3;

		public String value;
	}

	public class MessageBody2 {
		public int arg1;

		public int arg2;

		public int arg3;
	}

	public class RemoteObj {

		public static Bundle FormatRemoteObj(MessageBody data) {
			Bundle obj = null;
			if (data != null) {
				obj = new Bundle();
				obj.putByte(FormatKey, REMOTE_BASECOMBINE);
				obj.putInt(INT_KeyName + String.valueOf(1), data.what);
				obj.putInt(INT_KeyName + String.valueOf(2), data.arg1);
				obj.putInt(INT_KeyName + String.valueOf(3), data.arg2);
				if (data.obj != null)
					obj.putByteArray(BYTELIST_KeyName, data.obj);
			}
			return obj;
		}

		public static Bundle FormatRemoteObj(MessageBody1 msg) {
			Bundle obj = null;
			obj = new Bundle();
			obj.putByte(FormatKey, REMOTE_BASECOMBINE);
			obj.putInt(INT_KeyName + String.valueOf(1), msg.arg1);
			obj.putInt(INT_KeyName + String.valueOf(2), msg.arg2);
			obj.putInt(INT_KeyName + String.valueOf(3), msg.arg3);
			if (msg.value != null)
				obj.putString(STRING_KeyName, msg.value);
			return obj;
		}

		public static Bundle FormatRemoteObj(MessageBody2 msg) {
			Bundle obj = null;
			obj = new Bundle();
			obj.putByte(FormatKey, REMOTE_BASECOMBINE);
			obj.putInt(INT_KeyName + String.valueOf(1), msg.arg1);
			obj.putInt(INT_KeyName + String.valueOf(2), msg.arg2);
			obj.putInt(INT_KeyName + String.valueOf(3), msg.arg3);
			return obj;
		}

		public static Bundle FormatRemoteObj(MessageBody1[] List) {
			Bundle obj = null;
			MessageBody1 temp = null;
			if (List != null && List.length > 0) {
				obj = new Bundle();
				obj.putByte(FormatKey, REMOTE_COMBINELIST);
				obj.putByte(COMBINLIST_KeyName, (byte) List.length);

				for (int i = 0; i < List.length; i++) {
					temp = List[i];
					obj.putInt(
							INT_KeyName + String.valueOf(1) + String.valueOf(i),
							temp.arg1);
					obj.putInt(
							INT_KeyName + String.valueOf(2) + String.valueOf(i),
							temp.arg2);
					obj.putInt(
							INT_KeyName + String.valueOf(3) + String.valueOf(i),
							temp.arg3);
					if (temp.value != null)
						obj.putString(STRING_KeyName + String.valueOf(i),
								temp.value);
				}
			}
			return obj;
		}

		public static Bundle FormatRemoteObj(MessageBody[] List) {
			Bundle obj = null;
			MessageBody temp = null;
			if (List != null && List.length > 0) {
				obj = new Bundle();
				obj.putByte(FormatKey, REMOTE_COMBINELIST);
				obj.putByte(COMBINLIST_KeyName, (byte) List.length);

				for (int i = 0; i < List.length; i++) {
					temp = List[i];
					obj.putInt(
							INT_KeyName + String.valueOf(1) + String.valueOf(i),
							temp.what);
					obj.putInt(
							INT_KeyName + String.valueOf(2) + String.valueOf(i),
							temp.arg1);
					obj.putInt(
							INT_KeyName + String.valueOf(3) + String.valueOf(i),
							temp.arg2);
					if (temp.obj != null)
						obj.putByteArray(BYTELIST_KeyName + String.valueOf(i),
								temp.obj);
				}
			}
			return obj;
		}

		public static Bundle FormatRemoteObj(int item) {
			Bundle obj = null;
			obj = new Bundle();
			obj.putByte(FormatKey, REMOTE_INT);
			obj.putInt(INT_KeyName, item);
			return obj;
		}

		public static Bundle FormatRemoteObj(String item) {
			Bundle obj = null;
			if (item != null && item.isEmpty() == false) {
				obj = new Bundle();// TODO: later use pool method
				obj.putByte(FormatKey, REMOTE_STRING);
				obj.putString(STRING_KeyName, item);
			}
			return obj;
		}

		public static Bundle FormatRemoteObj(String[] List) {
			Bundle obj = null;
			if (List != null && List.length > 0) {
				obj = new Bundle();
				obj.putByte(FormatKey, REMOTE_STRINGLIST);
				obj.putStringArray(STRINGLIST_KeyName, List);
			}
			return obj;
		}

		public static Bundle FormatRemoteObj(int[] List) {
			Bundle obj = null;
			if (List != null && List.length > 0) {
				obj = new Bundle();
				obj.putByte(FormatKey, REMOTE_INTLIST);
				obj.putIntArray(INTLIST_KeyName, List);
			}
			return obj;
		}

		public static Bundle FormatRemoteObj(byte[] List) {
			Bundle obj = null;
			if (List != null && List.length > 0) {
				obj = new Bundle();
				obj.putByte(FormatKey, REMOTE_BYTELIST);
				obj.putByteArray(BYTELIST_KeyName, List);
			}
			return obj;
		}

		public static byte GetType(Bundle obj) {
			byte type = REMOTE_UNKNOWN;
			if (obj != null) {
				type = obj.getByte(FormatKey);
			}
			return type;
		}

		public static String[] GetStringList(Bundle obj) {
			String[] data = null;
			if (obj != null) {
				if (REMOTE_STRINGLIST == obj.getByte(FormatKey)) {
					data = obj.getStringArray(STRINGLIST_KeyName);
				}
			}
			return data;
		}

		public static int[] GetIntList(Bundle obj) {
			int[] data = null;
			if (obj != null) {
				if (REMOTE_INTLIST == obj.getByte(FormatKey)) {
					data = obj.getIntArray(INTLIST_KeyName);
				}
			}
			return data;
		}

		public static byte[] GetByteList(Bundle obj) {
			byte[] data = null;
			if (obj != null) {
				if (REMOTE_BYTELIST == obj.getByte(FormatKey)) {
					data = obj.getByteArray(BYTELIST_KeyName);
				}
			}
			return data;
		}

		public static String GetString(Bundle obj) {
			String data = null;
			if (obj != null) {
				if (REMOTE_STRING == obj.getByte(FormatKey)) {
					data = obj.getString(STRING_KeyName);
				}
			}
			return data;
		}

		public static int GetInt(Bundle obj) {
			int data = 0;
			if (obj != null) {
				if (REMOTE_INT == obj.getByte(FormatKey)) {
					data = obj.getInt(INT_KeyName);
				}
			}
			return data;
		}

		public static MessageBody GetMessageBody(Bundle obj) {
			MessageBody data = new MessageBody();
			if (obj != null) {
				if (REMOTE_BASECOMBINE == obj.getByte(FormatKey)) {
					data.what = obj.getInt(INT_KeyName + String.valueOf(1));
					data.arg1 = obj.getInt(INT_KeyName + String.valueOf(2));
					data.arg2 = obj.getInt(INT_KeyName + String.valueOf(3));
					data.obj = obj.getByteArray(BYTELIST_KeyName);
				}
			}
			return data;
		}

		public static MessageBody1 GetMessageBody1(Bundle obj) {
			MessageBody1 data = new MessageBody1();
			if (obj != null) {
				if (REMOTE_BASECOMBINE == obj.getByte(FormatKey)) {
					data.arg1 = obj.getInt(INT_KeyName + String.valueOf(1));
					data.arg2 = obj.getInt(INT_KeyName + String.valueOf(2));
					data.arg3 = obj.getInt(INT_KeyName + String.valueOf(3));
					data.value = obj.getString(STRING_KeyName);
				}
			}
			return data;
		}

		public static MessageBody2 GetMessageBody2(Bundle obj) {
			MessageBody2 data = new MessageBody2();
			if (obj != null) {
				if (REMOTE_BASECOMBINE == obj.getByte(FormatKey)) {
					data.arg1 = obj.getInt(INT_KeyName + String.valueOf(1));
					data.arg2 = obj.getInt(INT_KeyName + String.valueOf(2));
					data.arg3 = obj.getInt(INT_KeyName + String.valueOf(3));
				}
			}
			return data;
		}

		public static MessageBody[] GetMessageBodyList(Bundle obj) {
			MessageBody[] data = null;
			int num = 0;
			if (obj != null) {
				if (REMOTE_COMBINELIST == obj.getByte(FormatKey)) {
					num = obj.getByte(COMBINLIST_KeyName);
					if (num > 0) {
						data = new MessageBody[num];
						for (int i = 0; i < num; i++) {
							data[i] = new MessageBody();
							data[i].what = obj.getInt(INT_KeyName
									+ String.valueOf(1) + String.valueOf(i));
							data[i].arg1 = obj.getInt(INT_KeyName
									+ String.valueOf(2) + String.valueOf(i));
							data[i].arg2 = obj.getInt(INT_KeyName
									+ String.valueOf(3) + String.valueOf(i));
							data[i].obj = obj.getByteArray(BYTELIST_KeyName
									+ String.valueOf(i));
						}
					}
				}
			}
			return data;
		}

		public static MessageBody1[] GetMessageBody1List(Bundle obj) {
			MessageBody1[] data = null;
			int num = 0;
			if (obj != null) {
				if (REMOTE_COMBINELIST == obj.getByte(FormatKey)) {
					num = obj.getByte(COMBINLIST_KeyName);
					if (num > 0) {
						data = new MessageBody1[num];
						for (int i = 0; i < num; i++) {
							data[i] = new MessageBody1();
							data[i].arg1 = obj.getInt(INT_KeyName
									+ String.valueOf(1) + String.valueOf(i));
							data[i].arg2 = obj.getInt(INT_KeyName
									+ String.valueOf(2) + String.valueOf(i));
							data[i].arg3 = obj.getInt(INT_KeyName
									+ String.valueOf(3) + String.valueOf(i));
							data[i].value = obj.getString(STRING_KeyName
									+ String.valueOf(i));
						}
					}
				}
			}
			return data;
		}
	}
}
