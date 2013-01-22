package twitterApi.md5;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MD5Converter {
	
	public static boolean check(String passwd, String salts, String hashkey) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String salttmp[] = salts.split(",");
		byte salt[] = new byte[salttmp.length];

		for (int i = 0; i < salt.length; i++) {
			salt[i] = Byte.parseByte(salttmp[i]);
		}
		return MD5Converter.encode_with_salt(passwd, salt).get("hashkey").equals(hashkey);
	}
	
	public static Map<String, String> encode(String passwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Random rand = new Random();
		byte[] salt = new byte[12];
		rand.nextBytes(salt);
		return MD5Converter.encode_with_salt(passwd, salt);
	}
	
	private static Map<String, String> encode_with_salt(String passwd, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(salt);
		m.update(passwd.getBytes("UTF8"));
		byte s[] = m.digest();
		String hashkey = "";
		for (int i = 0; i < s.length; i++) {
			hashkey += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
		}
		String salt_str = "";
		for (int i = 0; i < salt.length-1; i++){
			salt_str += salt[i] + ",";
		}
		salt_str += salt[salt.length-1];
		Map<String, String> result = new HashMap<String, String>();
		result.put("salt", salt_str);
		result.put("hashkey", hashkey);
		return result;
	}
	
}
