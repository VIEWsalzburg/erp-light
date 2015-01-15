package at.erp.light.view.authenticate;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

public class HashGenerator {

	/**
	 * generated Hex Hash for password "default"
	 * FB1C60ADBF08B2E679DF8BF1E52AB8836C978AC0A2C6F898C8B5E8B9B1FFD384014D45362F20F184CD7E
	 */
	
	/**
	 * Compare plain password with hash 
	 * @param plain text
	 * @param hash .
	 * @return if they match
	 */
	
	public static String toHexString(byte[] array) {
	    return DatatypeConverter.printHexBinary(array);
	}

	public static byte[] toByteArray(String s) {
	    return DatatypeConverter.parseHexBinary(s);
	}
	
	public static void main(String ...args)
	{
		String password = "default";
		String hashed = hashPasswordWithSalt(password);
		System.out.println(hashed);
	}
	
	
	public static boolean comparePasswordWithHash(String plain, String hashHex)
	{
		String hash = new String(toByteArray(hashHex));
		String salt = hash.substring(0, 10);
		String hashedCompare = hash.substring(10);
		
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			md.update((salt + plain).getBytes("UTF-8")); // Change this to "UTF-16" if needed
			digest = md.digest();
			
			String reHashed = new String(digest);
			
			//Comparing Hex values because encoded string comparison isn't reliable
			
			if (toHexString(reHashed.getBytes()).equals(toHexString(hashedCompare.getBytes())))
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
			String salt = new String(SecureRandom.getSeed(10));

			md.update((salt + text).getBytes("UTF-8")); // Change this to
																// "UTF-16" if
																// needed
			digest = md.digest();
			return toHexString((salt + new String(digest)).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
