package com.darkxell.client.launchable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.darkxell.common.util.Logger;

/** Class that holds the main encryption/hashing methods used by the client. */
public class Encryption {

	public static final String HASHSALTTYPE_CLIENT = "client";
	public static final String HASHSALTTYPE_SERVER = "server";
	
	public static String clientHash(String tohash,String salt,String hashtype)throws Exception{
		try {
			return sha256(tohash+salt+hashtype);
		} catch (Exception e) {
			Logger.e("----------------------------CRITICAL ERROR----------------------------------");
			Logger.e("Failed hash for missing algorithm reasons, terminating execution immediately");
			Logger.e("----------------------------CRITICAL ERROR----------------------------------");
			e.printStackTrace();
			System.exit(403);
			throw e;
		}
	}

	private static String sha256(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; ++i)
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		return sb.toString();
	}
	
}
