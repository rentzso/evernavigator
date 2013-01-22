package twitterApi.md5;

import java.util.Map;

public class Tester {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws Exception {
		String[] passwords = {"belloeimpossibil", "bellatroppobella", "echiloconoscelui", "eilpapadirenzono"};
		for (int i = 0; i < passwords.length; i++ ){
			Map<String, String> results = MD5Converter.encode(passwords[i]);
			String salt = results.get("salt");
			String hashkey = results.get("hashkey");
			System.out.println(passwords[i] + ", " + hashkey + ", " + salt);
			System.out.println(MD5Converter.check(passwords[1], salt, hashkey));
		}

	}

}
