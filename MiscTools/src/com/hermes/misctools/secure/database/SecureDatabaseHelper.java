package com.hermes.misctools.secure.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SecureDatabaseHelper
 * 
 * @author KevinZeng
 * 
 */
public class SecureDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DB_H";
	private static final String DATABASE_NAME = "CoreSecureDB";
	private static final int DATABASE_VERSION = 1;
	public static final String KEY_TABLE_NAME = "KeyValue";
	public static final String KEY_TABLE_FIELD_KEY = "Key";
	public static final String KEY_TABLE_FIELD_VALUE = "Value";

	SecureDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + KEY_TABLE_NAME + "("
				+ KEY_TABLE_FIELD_KEY + " text not null, "
				+ KEY_TABLE_FIELD_VALUE + " text not null " + ");";
		Log.i(TAG, sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
