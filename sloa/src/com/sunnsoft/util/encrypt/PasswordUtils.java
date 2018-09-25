package com.sunnsoft.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author llade
 * 密码加密工具
 *
 */
public class PasswordUtils {
	

	public static String md5(String password){
		try {
			return HashUtils.getMD5HashHex(password);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 加“盐”的密码加密，更难破解。一般使用用户的登录名作为盐。本方法兼容spring security的加盐方式
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String md5(String password , String salt){
		return md5(password + "{" + salt + "}");
	}
	
	public static String sha1(String password){
		try {
			return HashUtils.getSHA1HashHex(password);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加“盐”的密码加密，更难破解。一般使用用户的登录名作为盐。本方法兼容spring security的加盐方式
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String sha1(String password , String salt){
		return sha1(password + "{" + salt + "}");
	}
	
	public static String sha(String password,int strength){
		try {
			return HashUtils.getSHAHashHex(password, strength);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	/**
	 * 加“盐”的密码加密，更难破解。一般使用用户的登录名作为盐。本方法兼容spring security的加盐方式
	 * @param password
	 * @param strength
	 * @param salt
	 * @return
	 */
	public static String sha(String password , int strength , String salt){
		return sha(password + "{" + salt + "}" , strength);
	}
	
	public static String md5AsBase64(String password){
		try {
			return HashUtils.getMD5HashBase64(password);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加“盐”的密码加密，更难破解。一般使用用户的登录名作为盐。本方法兼容spring security的加盐方式
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String md5AsBase64(String password , String salt){
		return md5AsBase64(password + "{" + salt + "}");
	}
	
	public static String sha1AsBase64(String password){
		try {
			return HashUtils.getSHA1HashBase64(password);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加“盐”的密码加密，更难破解。一般使用用户的登录名作为盐。本方法兼容spring security的加盐方式
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String sha1AsBase64(String password , String salt){
		return sha1AsBase64(password + "{" + salt + "}");
	}
	
	public static String shaAsBase64(String password,int strength){
		try {
			return HashUtils.getSHAHashBase64(password, strength);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	/**
	 * 加“盐”的密码加密，更难破解。一般使用用户的登录名作为盐。本方法兼容spring security的加盐方式
	 * @param password
	 * @param strength
	 * @param salt
	 * @return
	 */
	public static String shaAsBase64(String password , int strength , String salt){
		return shaAsBase64(password + "{" + salt + "}" , strength);
	}
}
