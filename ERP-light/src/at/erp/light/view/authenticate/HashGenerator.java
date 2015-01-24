package at.erp.light.view.authenticate;

import java.security.MessageDigest;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class HashGenerator {

	/**
	 * generated Hex Hash for password "default"
	 * MqPIAmlF4i2dSb0oR-JzwxG9yZidfKtK-n4AgMspQgpJbfJ3RC6Sg
	 */
	
	/**
	 * Compare plain password with hash 
	 * @param plain text
	 * @param hash .
	 * @return if they match
	 */
	
	public static void main(String ...args)
	{
		String password = "default";
		String hashed = hashPasswordWithSalt(password);
		
		System.out.println(hashed + " compared successful: " + comparePasswordWithHash(password, hashed));
	}
	
	
	public static boolean comparePasswordWithHash(String plain, String hash)
	{
		String salt = hash.substring(0, 10);
		String hashedCompare = hash.substring(10);
		
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			md.update((salt + plain).getBytes("UTF-8")); // Change this to "UTF-16" if needed
			digest = md.digest();
			
			//Urlsave therefore db save
			String reHashed = Base64.encodeBase64URLSafeString(digest);
			
			//Comparing Hex values because encoded string comparison isn't reliable
			
			if (reHashed.equals(hashedCompare))
				return true;
			else
				return false;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	/**
	 * Generates hash
	 * @param text given string to hash
	 * @return the hashed value
	 */
	public static String hashPasswordWithSalt(String text) {
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String salt = Base64.encodeBase64URLSafeString(SecureRandom.getSeed(10));
			salt=salt.substring(0,10); //Cause encoding returns something strange
			md.update((salt + text).getBytes("UTF-8")); // Change this to
																// "UTF-16" if
																// needed
			digest = md.digest();
			return (salt + Base64.encodeBase64URLSafeString(digest));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
