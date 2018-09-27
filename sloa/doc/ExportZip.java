package com.sunnsoft.yyai.actions.web.order;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.util.RandomUtils;
import com.sunnsoft.util.zip.ZipHelper;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:导出zip文件例子
 * @author:liujun
 * @date 2017年7月26日上午10:42:35
 */
public class ExportZip extends ActionSupport
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportZip.class);


	private File zipFile;
	@Action(interceptorRefs={
		@InterceptorRef("extStack")},
		results={@Result(name="success",type="stream",params={
			"contentType","application/octet-stream;charset=ISO8859-1",
			"inputName","fileInputStream",
			"contentDisposition","attachment;filename=${fileName}.zip",
			"bufferSize","1024"})})
	@Override
	public String execute() throws Exception {
		String tmpdir = System.getProperty("java.io.tmpdir");
		zipFile = new File(tmpdir,RandomUtils.generateMixString(25)+".zip");
		ZipHelper zip = new ZipHelper(zipFile);
		//生成zip文件 业务逻辑实现 
		zipFile.deleteOnExit();
		return super.execute();
	}
		
	public InputStream getFileInputStream() throws Exception 
	{
		InputStream inputStream = new FileInputStream(zipFile);
		return inputStream;
	}
	//实际文件名
	public String getFileName() throws UnsupportedEncodingException
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fileName = format.format(new Date());
		
		return new String(fileName.getBytes("GBK"), "ISO8859-1");
	}

	
}
