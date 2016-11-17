package com.hermes.misctools.secure;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.content.Context;

import com.hermes.misctools.secure.crypto.Crypto;
import com.hermes.misctools.secure.database.SecureDatabaseManager;

/**
 * SecureProxy provides secure features<br>
 * 1. store/retrieve a value based on key<br>
 * 
 * @author KevinZeng
 * 
 */
public class SecureProxy {
	/**
	 * Store a value based on a key<br>
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean storeKeyValue(Context context, String key,
			String value) {
		Crypto crypto = new Crypto(context);
		if (key == null || value == null) {
			return false;
		}
		if (key.length() <= 0 || value.length() <= 0) {
			return false;
		}

		SecureDatabaseManager databaseMgr = new SecureDatabaseManager(context);
		try {
			return databaseMgr.storeKeyValue(
					crypto.armorEncrypt(key.getBytes()),
					crypto.armorEncrypt(value.getBytes()));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Retrieve a value based on a key<br>
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String retrieveKeyValue(Context context, String key) {
		Crypto crypto = new Crypto(context);
		if (key == null) {
			return null;
		}

		SecureDatabaseManager databaseMgr = new SecureDatabaseManager(context);
		try {
			String result = databaseMgr.retrieveKeyValue(crypto
					.armorEncrypt(key.getBytes()));
			if (result != null) {
				return crypto.armorDecrypt(result);
			}
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
