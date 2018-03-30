package com.darkxell.client.launchable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.darkxell.common.util.Logger;

/** Class that holds the main encryption/hashing methods used by the client. */
public class Encryption {

	public static final String HASHSALTTYPE_CLIENT = "client";
	public static final String HASHSALTTYPE_SERVER = "server";
	public static final String HASHSALTTYPE_LOGIN = "login";

	public static String clientHash(String tohash, String salt, String hashtype) throws Exception {
		try {
			return sha256(tohash + salt + hashtype);
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

	/**
	 * Do NOT call this method unless you want to scare the hell out of your
	 * user's ass. <br/>
	 * Or perhaps, you know, if a major security breach is discovered in
	 * SHA-256, and the user was under attack while java runtime environnement
	 * libraries used in this project are not updated.<br/>
	 * This method will display a breach error message and instructions in
	 * english, and will terminate the application.
	 */
	public static void death256message() {
		Logger.w("Client is currently reading Encryption.death256message(). Good luck.");
		Logger.e("If you are reading this message, something went HORRIBLY WRONG."
				+ "\nREAD the following instructions with caution and contact a developper."
				+ "\nYour password information has most likely been compromised by a third party programm on your computer."
				+ "\nIf you use automatic login since the beginning, you are most likely safe on other services that are not this game."
				+ "\nIf you typed you password recently, it is very likely to be known by everyone at this point."
				+ "\nWe advise changing any identical or resembling password on other websites/services WITH AN OTHER MACHINE/COMPUTER."
				+ "\n-------------------------------------------------------------------------------------------------"
				+ "\nNote that this message isn't intended to be seen, ever. Please contact a developper if you see it."
				+ "\nTerminating this for your own safety.");
		System.exit(666);
	}

}
