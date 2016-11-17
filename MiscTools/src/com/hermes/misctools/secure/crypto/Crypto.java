package com.hermes.misctools.secure.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.util.Base64;

/**
 * Crypto<br>
 * 
 * @author KevinZeng
 * 
 */
public class Crypto {
	private static final String mEngine = "AES";
	private static final String mCrypto = "AES/CBC/PKCS5Padding";
	private static Context mContext;

	public Crypto(Context ctx) {
		mContext = ctx;
	}

	public byte[] cipher(byte[] data, int mode)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		KeyManager km = new KeyManager(mContext);
		SecretKeySpec sks = new SecretKeySpec(km.getId(), mEngine);
		IvParameterSpec iv = new IvParameterSpec(km.getIv());
		Cipher c = Cipher.getInstance(mCrypto);
		c.init(mode, sks, iv);
		return c.doFinal(data);
	}

	public byte[] encrypt(byte[] data) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {
		return cipher(data, Cipher.ENCRYPT_MODE);
	}

	public byte[] decrypt(byte[] data) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {
		return cipher(data, Cipher.DECRYPT_MODE);
	}

	public String armorEncrypt(byte[] data) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {
		return Base64.encodeToString(encrypt(data), Base64.DEFAULT);
	}

	public String armorDecrypt(String data) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {
		return new String(decrypt(Base64.decode(data, Base64.DEFAULT)));
	}
}
