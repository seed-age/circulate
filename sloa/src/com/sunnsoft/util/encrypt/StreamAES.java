package com.sunnsoft.util.encrypt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException; 

/**
 * 
 * @author 林宇民 Andy(llade)
 * AES加密和解密文件和输入输出流
 */
public class StreamAES {
	
  private Key key; 
  
  public StreamAES(String str) { 
    getKey(str);//生成密匙 
  } 
  /** 
  * 根据参数生成加密用到的密钥KEY
  */ 
  public void getKey(String strKey) { 
    try { 
        KeyGenerator generator = KeyGenerator.getInstance("AES"); 
        generator.init(new SecureRandom(strKey.getBytes())); 
        this.key = generator.generateKey(); 
        generator = null; 
    } catch (Exception e) { 
        throw new RuntimeException("Error initializing StreamAES. Cause: " + e); 
    } 
  } 
  
  /**
   * 包装一个输入流成为加密输入流。
   * @param input 正常的字节流
   * @return 被包装的加密输入流，能用read方法读取被包装的正常字节流里的字数组，并自动加密并返回加密后的字节数组。
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   */
  public InputStream encryptInputStream(InputStream input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
	  Cipher cipher = Cipher.getInstance("AES"); 
	  cipher.init(Cipher.ENCRYPT_MODE, this.key);
	  return new CipherInputStream(input, cipher);
  }
  
  /**
   * 包装一个输出流成为加密输出流。
   * @param output 被包装的、接收加密后字节数组的目标输出流
   * @return 加密后的输出流，能用write方法写入正常字节数组，自动解加密，并将加密过后的字符串写入到目标输出流。
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   */
  public OutputStream encryptOutputStream(OutputStream output) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
	  Cipher cipher = Cipher.getInstance("AES"); 
	  cipher.init(Cipher.ENCRYPT_MODE, this.key);
	  return new CipherOutputStream(output, cipher);
  }
  /**
   * 包装一个解密输入流。
   * @param input 被加密过的输入流
   * @return 被包装过后的解密输入流，能用read方法读取被加密过的输入字节数组并自动解密成正常字节数组。
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   */
  public InputStream decryptInputStream(InputStream input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
	  Cipher cipher = Cipher.getInstance("AES"); 
	  cipher.init(Cipher.DECRYPT_MODE, this.key);
	  return new CipherInputStream(input, cipher);
  }
  /**
   * 包装一个解密输出流。
   * @param output 正常的输出流，用来接收解密出字符数组的目标输出。
   * @return 被包装的输出流，能用write方法接收加密过的字节数组并自动解密成正常字节数组，并写入到正常的目标输出流中。
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   */
  public OutputStream decryptOutputStream(OutputStream output) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
	  Cipher cipher = Cipher.getInstance("AES"); 
	  cipher.init(Cipher.DECRYPT_MODE, this.key);
	  return new CipherOutputStream(output, cipher);
  }
  /**
   * 把未加密过的原始的输入流，经过加密，输出到目标输出流中。
   * @param originInput 未加密过的原始的输入流
   * @param encryptedOutput 经过加密，输出的目标输出流
   * @throws InvalidKeyException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws IOException
   */
  public void encryptStreamPipeLine(InputStream originInput , OutputStream encryptedOutput) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
	  OutputStream cos = this.encryptOutputStream(encryptedOutput);
	  byte[] buffer = new byte[1024]; 
	  int r; 
	  while ((r = originInput.read(buffer)) != -1) { 
		  cos.write(buffer, 0, r); 
	  }
	  originInput.close();
	  cos.flush();
	  cos.close();
  }
  /**
   * 从被加密过的输入流里读取并解密成正常的字节流
   * @param encryptedInput 被加密过的输入流
   * @param decryptedOutput 解密成正常字节的目标输出流
   * @throws InvalidKeyException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws IOException
   */
  public void decryptStreamPipeLine(InputStream encryptedInput , OutputStream decryptedOutput) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException{
	  InputStream cis = this.decryptInputStream(encryptedInput);
	  byte[] buffer = new byte[1024]; 
	  int r; 
	  while ((r = cis.read(buffer)) != -1) {
		  decryptedOutput.write(buffer, 0, r); 
	  }
	  cis.close();
	  decryptedOutput.flush();
	  decryptedOutput.close();
  }

  /**
   * 文件采用AES算法进行加密并保存目标文件中 
   * @param inputFile 要加密的文件 
   * @param destFile 加密后存放的文件
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   * @throws IOException
   */
  public void encrypt(File inputFile, File destFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException { 
    InputStream is = new FileInputStream(inputFile); 
    OutputStream out = new FileOutputStream(destFile); 
    this.encryptStreamPipeLine(is, out);
  } 
  
  /**
   * 文件采用AES算法解密文件 
   * @param inputFile 已加密的文件
   * @param destFile 解密后存放的文件
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   * @throws IOException
   */
  public void decrypt(File inputFile, File destFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException { 
    InputStream is = new FileInputStream(inputFile); 
    OutputStream out = new FileOutputStream(destFile); 
    this.decryptStreamPipeLine(is, out);
  } 
  
  
  public static void main(String[] args) throws Exception { 
    StreamAES td = new StreamAES("aaa"); 
    td.encrypt(new File("d:/tmp/test.txt"), new File("d:/tmp/en_test.txt")); //加密 
    StreamAES td2 = new StreamAES("aaa"); 
    td2.decrypt(new File("d:/tmp/en_test.txt"),new File("d:/tmp/de_test.txt")); //解密 
    
  } 
}