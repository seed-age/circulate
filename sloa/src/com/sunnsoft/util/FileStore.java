package com.sunnsoft.util;

import java.io.File;

public interface FileStore {
	/**
	 * 获取 FileStore代表的根目录。
	 * @return
	 */
	public abstract File getRootFile();
	/**
	 * 根据路径名获取相应文件，不管是否存在都返回对应的文件对象
	 * 如果文件路径中有".."，则返回null
	 * @param fileName
	 * @return
	 */
	public abstract File getFile(String fileName);
	

}