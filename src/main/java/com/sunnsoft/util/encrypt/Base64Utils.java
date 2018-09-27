package com.sunnsoft.util.encrypt;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {
	
	public static byte[] decode(String input){
		return Base64.decodeBase64(input);
	}
	
	public static String encode(byte[] input){
		return Base64.encodeBase64String(input);
	}

}
