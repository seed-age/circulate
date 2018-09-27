package com.sunnsoft.test;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CryptoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	
	public static void test1() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		Cipher cipher = Cipher.getInstance("RSA");

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        // 生成钥匙对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 得到公钥
        Key publicKey = keyPair.getPublic();
        
        

        // 得到私钥
        Key privateKey = keyPair.getPrivate();

        // 设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 对数据进行加密
        byte[] result = cipher.doFinal("aaa".getBytes());
	}
}
