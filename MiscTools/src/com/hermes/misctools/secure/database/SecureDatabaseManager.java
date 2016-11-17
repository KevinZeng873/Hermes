package com.hermes.misctools.secure.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * SecureDatabaseManager
 * 
 * @author KevinZeng
 * 
 */
public class SecureDatabaseManager {
	private static final String TAG = "DB_M";
	private SecureDatabaseHelper mDbHelper = null;

	public SecureDatabaseManager(Context context) {
		mDbHelper = new SecureDatabaseHelper(context);
	}

	/**
	 * Store the key-value pair
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean storeKeyValue(String key, String value) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String sqlWhere = SecureDatabaseHelper.KEY_TABLE_FIELD_KEY + "='"
				+ key +"'";
		try {
			Cursor cursor = db.query(SecureDatabaseHelper.KEY_TABLE_NAME, null,
					sqlWhere, null, null, null, null);
			/*
			 * store the value or replace it with new value
			 */
			if (null != cursor && cursor.moveToFirst() == true) {
				// replace the existed
				String sqlUpdate = "UPDATE "
						+ SecureDatabaseHelper.KEY_TABLE_NAME + " SET "
						+ SecureDatabaseHelper.KEY_TABLE_FIELD_VALUE
						+ "='" + value + "' WHERE "
						+ SecureDatabaseHelper.KEY_TABLE_FIELD_KEY + "='" + key
						+ "'";
				//Log.i(TAG, sqlUpdate);
				db.execSQL(sqlUpdate);
			} else {
				// insert new
				String sqlInsert = "INSERT INTO "
						+ SecureDatabaseHelper.KEY_TABLE_NAME + " ("
						+ SecureDatabaseHelper.KEY_TABLE_FIELD_KEY + ", "
						+ SecureDatabaseHelper.KEY_TABLE_FIELD_VALUE
						+ ") values('" + key + "', '" + value + "');";
				//Log.i(TAG, sqlInsert);
				db.execSQL(sqlInsert);
			}
			// close cursor
			if(null != cursor){
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return false;
		}
		db.close();
		return true;
	}

	public String retrieveKeyValue(String key) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String sqlWhere = SecureDatabaseHelper.KEY_TABLE_FIELD_KEY + "='"
				+ key +"'";
		try {
			Cursor cursor = db.query(SecureDatabaseHelper.KEY_TABLE_NAME, null,
					sqlWhere, null, null, null, null);
			if (cursor != null && cursor.moveToFirst() == true) {
				// get one
				String value = cursor.getString(1);
				cursor.close();
				db.close();
				return value;
			}
			// close cursor
			if(null != cursor){
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			db.close();
			return null;
		}
		db.close();
		return null;
	}
}
