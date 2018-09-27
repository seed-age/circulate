package com.sunnsoft.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.File;

/**
 * 
 * @author llade
 * 
 */
public class LocalFileStore implements InitializingBean, FileStore {

	String rootFilePath ;
	File rootFile;
	boolean emptyRoot;//初始化的时候，删除root目录下的内容。对于临时目录有用

	public boolean isEmptyRoot() {
		return emptyRoot;
	}

	public void setEmptyRoot(boolean emptyRoot) {
		this.emptyRoot = emptyRoot;
	}

	public void setRootFile(File rootFile) {
		this.rootFile = rootFile;
	}

	public String getRootFilePath() {
		return rootFilePath;
	}

	public void setRootFilePath(String rootFilePath) {
		this.rootFilePath = rootFilePath;
	}

	/**
	 * 获取fileStore对象的root File
	 */
	public File getRootFile() {
		return this.rootFile;
	}
	/**
	 * 根据相对路径名和文件名获取与root的File
	 */
	
	public File getFile(String fileName) {
		File parent = this.rootFile;
		String[] filesName = fileName.split("[\\/]");
		for (int i = 0; i < filesName.length; i++) {
			if(StringUtils.isNotEmpty(filesName[i])){
				if("..".equals(filesName[i].trim())){
					return null;
				}
				parent = new File(parent,filesName[i]);
			}
		}
		return parent;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.rootFilePath);
		this.rootFile = new File(this.rootFilePath);
		if (!this.rootFile.exists()) {
			this.rootFile.mkdirs();
		} else if (emptyRoot) {
			FileUtils.cleanDirectory(this.rootFile);
		}

	}

	public static void main(String[] args) throws Exception{
		LocalFileStore temp = new LocalFileStore();
		temp.setRootFilePath("D:/project/file/");
		temp.afterPropertiesSet();
		System.out.println(temp.getFile("xxx.txt") +" exists "+temp.getFile("xxx.txt").exists());
		System.out.println(temp.getFile("xxx/xxx.txt") +" exists "+temp.getFile("xxx/xxx.txt").exists());
		System.out.println(temp.getFile("xxx\\xxx.txt") +" exists "+temp.getFile("xxx\\xxx.txt").exists());
		System.out.println(temp.getFile("abc/xxx.txt") +" exists "+temp.getFile("abc/xxx.txt").exists());
		System.out.println(temp.getFile("../a.jpg") );
	}

}
