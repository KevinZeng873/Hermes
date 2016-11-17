package com.hermes.misctools.secure.crypto;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

/**
 * KeyManager<br>
 * 1. accept a parameter as secure key : setId<br>
 * 2. accept a parameter as initial value : setIv<br>
 * 3. save the secure key in file<br>
 * 4. get the secure key : getId<br>
 * 5. get the initial value : getIv<br>
 * 
 * @author KevinZeng
 * 
 */
public class KeyManager {
	private static final String ID_DEFAULT = "12345678909876543212345678909876";
	private static final String IV_DEFAULT = "1234567890987654";

	private static final String TAG = "KeyManager";
	private static final String mFile1 = "id_value";
	private static final String mFile2 = "iv_value";

	private static Context mContext;

	public KeyManager(Context ctx) {
		mContext = ctx;
	}

	public void setId(byte[] data) {
		writer(data, mFile1);
	}

	public void setIv(byte[] data) {
		writer(data, mFile2);
	}

	/**
	 * Return the ID through reading ID file. Otherwise, return the default.
	 */
	public byte[] getId() {
		byte[] result = reader(mFile1);
		if(result != null){
			return result;
		}
		return ID_DEFAULT.getBytes();
	}

	/**
	 * Return the IV through reading IV file. Otherwise, return the default.
	 */
	public byte[] getIv() {
		byte[] result = reader(mFile2);
		if(result != null){
			return result;
		}
		return IV_DEFAULT.getBytes();
	}

	public byte[] reader(String file) {
		byte[] data = null;
		try {
			int bytesRead = 0;
			FileInputStream fis = mContext.openFileInput(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			while ((bytesRead = fis.read(b)) != -1) {
				bos.write(b, 0, bytesRead);
			}
			data = bos.toByteArray();
		} catch (FileNotFoundException e) {
			//Log.e(TAG, "File not found in getId()");
		} catch (IOException e) {
			Log.e(TAG, "IOException in getId():" + e.getMessage());
		}
		return data;
	}

	public void writer(byte[] data, String file) {
		try {
			FileOutputStream fos = mContext.openFileOutput(file,
					Context.MODE_PRIVATE);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not found in setId()");
		} catch (IOException e) {
			Log.e(TAG, "IOException in setId():" + e.getMessage());
		}
	}
}
