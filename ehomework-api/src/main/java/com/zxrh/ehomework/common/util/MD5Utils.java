package com.zxrh.ehomework.common.util;

import org.springframework.util.DigestUtils;

public class MD5Utils{

	public static String encrypt(String string){
		return string==null?null:DigestUtils.md5DigestAsHex(string.getBytes());
	}
	
	public static boolean isCipher(String cipher,String plain){
		return plain==null?cipher==null:encrypt(plain).equals(cipher);
	}
	
}