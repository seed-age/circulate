package com.sunnsoft.util.encrypt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 统一的Hash工具集,可以处理文件和输入字符串的消息摘要hash ，默认是Utf-8编码，其他编码类型用本类最基础的方法自行实现。
 * @author llade
 *
 */
public class HashUtils {
	
	
	/**
	 * 算出输入流的消息摘要 16进制hash ，用以校验文件完整和不被串改。
	 * @param inputStream
	 * @param algorithm
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String digestInputStream(InputStream inputStream , String algorithm) throws IOException, NoSuchAlgorithmException{
		byte[] buffer = new byte[1024];
		int read = inputStream.read(buffer, 0, 1024);
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		while (read > -1) {
			digest.update(buffer, 0, read);
			read = inputStream.read(buffer, 0, 1024);
		}
		return bytesToHexString(digest.digest());
	}
	/**
	 * 算出文件的消息摘要 16进制hash ，用以校验文件完整和不被串改。
	 * @param file
	 * @param algorithm "MD5" , "SHA"等MessageDigest支持的算法
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String digestFile(File file , String algorithm) throws IOException, NoSuchAlgorithmException{
		String result = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			result = digestInputStream(fis , algorithm);
		} catch (FileNotFoundException e) {
			 throw e;
		} catch (NoSuchAlgorithmException e) {
			 throw e;
		} catch (IOException e) {
			 throw e;
		}finally{
			if(fis != null){
				fis.close();
			}
		}
		return result;
		
	}
	/**
	 * 密钥相关的HmacSHA1 哈希算法。
	 * @param input
	 * @param key 密钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static String getHmacSHA1HashHex(String input , String key) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
		 return bytesToHexString(getHmacSHA1HashBytes(input, key));  
	}
	/**
	 * 密钥相关的HmacSHA1 哈希算法。
	 * @param input
	 * @param key 密钥
	 * @param encoding
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static String getHmacsh1HashHex(String input , String key , String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
		 return bytesToHexString(getHmacSHA1HashBytes(input, key,encoding));  
	}
	/**
	 * 密钥相关的HmacSHA1 哈希算法。
	 * @param input
	 * @param key 密钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static String getHmacSHA1HashBase64(String input , String key) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
		 return Base64Utils.encode(getHmacSHA1HashBytes(input, key));  
	}
	/**
	 * 密钥相关的HmacSHA1 哈希算法。
	 * @param input
	 * @param key 密钥
	 * @param encoding
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static String getHmacSHA1HashBase64(String input , String key , String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
		 return Base64Utils.encode(getHmacSHA1HashBytes(input, key , encoding));  
	}
	/**
	 * 密钥相关的HmacSHA1 哈希算法。
	 * @param input
	 * @param key 密钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static byte[] getHmacSHA1HashBytes(String input , String key) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
		 return getHmacSHA1HashBytes(input, key, "UTF-8");
	}
	/**
	 * 密钥相关的HmacSHA1 哈希算法。
	 * @param input
	 * @param key 密钥
	 * @param encoding
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static byte[] getHmacSHA1HashBytes(String input , String key,String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
		 Mac mac = Mac.getInstance("HmacSHA1");  
		 SecretKeySpec secret = new SecretKeySpec(  
		           key.getBytes(encoding), mac.getAlgorithm());  
		 mac.init(secret);
		 return mac.doFinal(input.getBytes(encoding));
	}
	/**
	 * MD5 hash,默认编码utf-8
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getMD5HashBytes(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return getHashBytes(input,"MD5","utf-8");
	}
	/**
	 * MD5 hash,默认编码utf-8
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5HashHex(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return bytesToHexString(getMD5HashBytes(input));
	}
	/**
	 * MD5 hash,默认编码utf-8
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5HashBase64(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return Base64Utils.encode(getMD5HashBytes(input));
	}
	/**
	 * SHA1 hash,默认编码utf-8
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getSHA1HashBytes(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return getSHAHashBytes(input,1);
	}
	/**
	 *  SHA1 hash,默认编码utf-8
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSHA1HashHex(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return bytesToHexString(getSHA1HashBytes(input));
	}
	/**
	 *  SHA1 hash,默认编码utf-8
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSHA1HashBase64(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return Base64Utils.encode(getSHA1HashBytes(input));
	}
	/**
	 * 各类强度的SHA hash ，默认utf-8编码
	 * @param input
	 * @param strength 支持1、224、256、384、512强度。
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getSHAHashBytes(String input , int strength) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return getHashBytes(input,"SHA-" + strength,"utf-8");
	}
	/**
	 * 各类强度的SHA hash ，默认utf-8编码
	 * @param input
	 * @param strength 支持1、224、256、384、512强度。
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSHAHashHex(String input , int strength) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return bytesToHexString(getSHAHashBytes(input,strength));
	}
	/**
	 * 各类强度的SHA hash ，默认utf-8编码
	 * @param input
	 * @param strength 支持1、224、256、384、512强度。
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSHAHashBase64(String input , int strength) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return Base64Utils.encode(getSHAHashBytes(input,strength));
	}
	
	/**
	 * 消息摘要方法
	 * @param input
	 * @param algorithm
	 * @param encoding
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getHashBytes(String input,String algorithm,String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md = MessageDigest.getInstance(algorithm);
		return md.digest(input.getBytes(encoding));
	}
	/**
	 * 消息摘要方法
	 * @param input
	 * @param algorithm
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHashHex(String input,String algorithm,String encoding) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return bytesToHexString(getHashBytes(input,algorithm,encoding));
	}
	/**
	 * 消息摘要方法
	 * @param input
	 * @param algorithm
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHashBase64(String input,String algorithm,String encoding) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		return Base64Utils.encode(getHashBytes(input,algorithm,encoding));
	}
	
	/** 
	 * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。  
	 * @param src byte[] data  
	 * @return  hex string  
	 */     
	public static String bytesToHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	} 
	
	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	 private static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  
	

}
