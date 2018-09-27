package com.sunnsoft.util.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipHelper {
	
	protected ZipOutputStream zOut;
	/**
	 * 供类内部使用得缓冲输出流
	 * 
	 */
	protected BufferedOutputStream bos ;
	
	protected String encoding="GBK";
	
	/**
	 * 构造函数，使用给定的ZipOutputStream来创建对象,表示最终输出的zip文件的输出方向
	 * @param out
	 */
	public ZipHelper(ZipOutputStream out) {
		super();
		zOut = out;
		bos = new BufferedOutputStream(zOut,4096);
		zOut.setEncoding(encoding);
	}
	
	/**
	 * 构造函数，使用给定的File对象 来创建对象,表示最终输出的zip文件的输出方向
	 * @param file 如果文件不存在则创建新的文件对象。
	 * @throws FileNotFoundException
	 */
	public ZipHelper(File file) throws FileNotFoundException{
		this(new ZipOutputStream(new FileOutputStream(file)));
	}
	
	/**
	 * 构造函数，使用给定的OutputStream来创建对象,表示最终输出的zip文件的输出方向
	 * @param out
	 */
	public ZipHelper(OutputStream out){
		this(new ZipOutputStream(out));
	}
	/**
	 * 构造函数，使用给定的filePath路径说表示的文件对象 来创建对象,表示最终输出的zip文件的输出方向
	 * @param filePath 如果文件不存在则创建新的文件对象。
	 * @throws FileNotFoundException
	 */
	public ZipHelper(String filePath) throws FileNotFoundException{
		this(new File(filePath));
	}
	
	/**
	 * 设置文件夹或者文件名的编码类别，默认是GBK（适合中文压缩软件）
	 * @param encoding
	 */
	public void setEncoding(String encoding){
		this.encoding = encoding;
		zOut.setEncoding(this.encoding);
	}
	/**
	 * 当前文件夹或者文件名的编码类型
	 * @return
	 */
	public String getEncoding(){
		return this.encoding;
	}
	
	/**
	 * 添加filePath指定的文件到zip文件，使用entryName表示在zip文件包里面的路径。
	 * 合法的entryName例子：dir/xxx.jpg
	 * @param filePath
	 * @param entryName
	 * @throws IOException
	 */
	public void addFile(String filePath,String entryName) throws IOException{
		this.addFile(new File(filePath), entryName);
	}
	/**
	 * 添加file文件到zip文件，使用entryName表示在zip文件包里面的路径。
	 * 合法的entryName例子：dir/xxx.jpg
	 * @param file
	 * @param entryName
	 * @throws IOException
	 */
	public void addFile(File file,String entryName) throws IOException{
		FileInputStream fis = new FileInputStream(file);
		this.addFileInputStream(fis, entryName);
	}
	/**
	 * 添加从输入流中读取，并输出到zip文件，使用entryName表示在zip文件包里面的路径。
	 * 合法的entryName例子：dir/xxx.jpg
	 * @param is
	 * @param entryName
	 * @throws IOException
	 */
	public void addFileInputStream(InputStream is,String entryName) throws IOException{
		ZipEntry entry = new ZipEntry(entryName);
		zOut.putNextEntry(entry); 
		BufferedInputStream bis = new BufferedInputStream(is);
		byte[] buffer = new byte[4096];
		int read = 0;
		while ((read = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, read);
		}
		bos.flush();//刷出缓冲区，使得最后缓冲的部分写入到流，这步骤是必须的，否则会导致文件不全。
		bis.close();//关闭文件输入流，释放系统资源
		zOut.closeEntry();
	}
	
	public void addDir(String dirPath,boolean includeSubDir) throws IOException{
		this.addDir(new File(dirPath), includeSubDir);
	}
	/**
	 * 简化的addDir方法，直接使用原目录名作为zip压缩条目（zip文件包里面的路径）
	 * @param dir
	 * @param includeSubDir
	 * @throws IOException
	 */
	
	public void addDir(File dir,boolean includeSubDir) throws IOException{
		this.addDir(dir, dir.getName(), includeSubDir);
	}
	/**
	 * 添加目标目录及其所属文件到zip文件中，根据参数是否includeSubDir遍历子目录
	 * @param dirPath 目录文件绝对路径
	 * @param dirEntryName 表示目录在zip文件包里面的路径
	 * @param includeSubDir 是否包含所有子目录
	 * @throws IOException
	 */
	public void addDir(String dirPath,String dirEntryName,boolean includeSubDir) throws IOException{
		this.addDir(new File(dirPath), dirEntryName, includeSubDir);
	}
	
	/**
	 * 添加目标目录及其所属文件到zip文件中，根据参数是否includeSubDir遍历子目录
	 * @param dir
	 * @param dirEntryName 表示目录在zip文件包里面的路径
	 * @param includeSubDir 是否包含所有子目录
	 * @throws IOException
	 */
	public void addDir(File dir,String dirEntryName,boolean includeSubDir) throws IOException{
		if(!dir.isDirectory()){
			throw new IOException("被压缩的不是目录，而是文件");
		}
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if(file.isDirectory()){
				if(includeSubDir){
					this.addDir(file, dirEntryName+(dirEntryName.endsWith("/")?"":"/")+file.getName(),true);
				}
			}else{
				this.addFile(file, dirEntryName+(dirEntryName.endsWith("/")?"":"/")+file.getName());
			}
		}
	}
	
	/**
	 * 返回当前使用的ZipOutputStream，以便外部直接输入文流到zip文件，使用entryName表示在zip文件包里面的路径。
	 * 合法的entryName例子：dir/xxx.jpg
	 * @param entryName
	 * @return
	 * @throws IOException
	 */
	public OutputStream getOutputStream (String entryName) throws IOException {
		ZipEntry entry = new ZipEntry(entryName);   
		zOut.putNextEntry(entry); 
		return this.zOut;
	}
	
	/**
	 * 关闭zip文件输出,并刷新缓冲区。
	 * @throws IOException
	 */
	public void close() throws IOException{
		zOut.flush();
		zOut.close();
	}
	
	public static void main(String[] args) throws IOException{
//		testZipFile();
		testZipDir();
	}
	
	public static void testZipFile() throws IOException{
		ZipHelper h = new ZipHelper("D:/project/gzctgd2/imgs/test_zip_file.zip");
		h.addFile("D:/project/gzctgd2/imgs/1.JPG", "中文目录/中文1.jpg");
		h.addFile("D:/project/gzctgd2/imgs/2.JPG", "zip1/2.jpg");
		h.addFile("D:/project/gzctgd2/imgs/3.JPG", "zip1/3.jpg");
		h.addFile("D:/project/gzctgd2/imgs/4.JPG", "zip1/4.jpg");
		h.addFile("D:/project/gzctgd2/imgs/5.JPG", "zip1/5.jpg");
		h.addFile("D:/project/gzctgd2/imgs/6.JPG", "zip1/6.jpg");
		h.addFile("D:/project/gzctgd2/imgs/7.JPG", "zip1/7.jpg");
		h.addFile("D:/project/gzctgd2/imgs/8.JPG", "zip1/8.jpg");
		h.addFile("D:/project/gzctgd2/imgs/9.JPG", "zip1/9.jpg");
		h.addFile("D:/project/gzctgd2/imgs/10.JPG", "zip1/10.jpg");
		h.addFile("D:/project/gzctgd2/imgs/11.JPG", "zip1/11.jpg");
		h.addFile("D:/project/gzctgd2/imgs/12.JPG", "zip1/12.jpg");
		h.close();
	}

	public static void testZipDir() throws IOException{
		ZipHelper h = new ZipHelper("D:/project/gzctgd2/imgs/test_zip_dir.zip");
		h.addFile("D:/project/gzctgd2/imgs/1.JPG", "中文目录/中文1.jpg");
		h.addDir("D:/project/gzctgd2/imgs/excel", "中文目录2", true);
		h.addDir("D:/project/gzctgd2/imgs/text", true);
	}
}
