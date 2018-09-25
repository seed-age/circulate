package com.sunnsoft.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sunnsoft.sloa.actions.web.received.SendDownloadFile;
import com.sunnsoft.util.FileStore;
import com.sunnsoft.util.ImageUtils;
import com.sunnsoft.util.MimeTypeDictionary;

public class FileDownloadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FileStore store;
	boolean sessionPath;//如果为ture，切分出文件名之后，从FileStore获取文件的时候自动把用户session当做父目录，相当于root/sessionId/target.file 映射到url：mapping/target.file,对于临时的session目录非常有用。
	String fileNotFoundDefault;//无图片的时候显示的图片,这个路径和servlet context相对路径。
	ServletConfig config;
	
	//可自动剪切图片的规格，在url后带上s=规格 参数就可以获得指定规格的图片，注意，剪切功能不支持文件名有下划线的文件，避免被人恶意利用剪切功能进行重复剪切浪费存储空间。
	//常见类型："32","36","48","64","72","80","96","128","144","192","256","320","360","400","480","512","720","768","800","900","1024","1080","1440"
	private String[] allowSizes = new String[] {"32","36","48","64","72","96","128","144","192","256","360","400","480","720","768","800","900","1080","1280","1440"};
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		String beanId = config.getInitParameter("fileStoreBeanId");
		String sessionPathStr = config.getInitParameter("sessionPath");
		fileNotFoundDefault = config.getInitParameter("fileNotFoundDefault");
		String sizes = config.getInitParameter("allowSizes");
		if(sizes != null) {
			allowSizes =  sizes.trim().split(",");
		}
		if ("true".equals(sessionPathStr))
			sessionPath = true;
		if (beanId == null)
			beanId = "fileStore";
		this.config = config;
		store = (FileStore) WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getServletContext())
				.getBean(beanId);
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String fileName = req.getPathInfo();
		String contentType = MimeTypeDictionary.getMimeType(fileName);
		String size = req.getParameter("s");
		String newFileName = null;
		if(size != null && ArrayUtils.contains(allowSizes, size) && fileName.lastIndexOf(".") != -1 && fileName.indexOf("_") == -1) {
			size = size.trim();
			String preffix = fileName.substring(0,fileName.lastIndexOf('.'));
			String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
			newFileName = preffix + "_" + size + "." + suffix;
		}
		File file = null;
		File newFile = null;
		if (sessionPath) {
			File userDir = store.getFile(req.getSession().getId());
			if (!userDir.exists()) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} else {
				file = new File(userDir, fileName);
				if(newFileName != null) {
					newFile = new File(userDir, newFileName);
				}
			}
		} else {
			file = store.getFile(fileName);
			if(newFileName != null) {
				newFile = store.getFile(newFileName);
			}
		}
		
		
		if (file == null || !file.exists() || file.isDirectory()) {
			if (fileNotFoundDefault == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} else {
				file = new File(this.config.getServletContext().getRealPath(
						fileNotFoundDefault));
			}
		}
		
		if("true".equals(req.getParameter("download"))){//是否下载
			String fileRenameName = req.getParameter("rename");//是否重命名为制定文件名。
			if(StringUtils.isBlank(fileRenameName)){
				fileRenameName = fileName;
			}
			//String encode = URLEncoder.encode(fileRenameName, "UTF-8");
			resp.setHeader("Content-Disposition", "attachment;filename="+ fileRenameName); 
		}
		
		if(newFile != null) {
			if(!newFile.exists()){
				if(ImageUtils.isImage(file)) {
					int sizeInt = Integer.parseInt(size);
					ImageUtils.cutImageKeepScaleByWidth(file, newFile, sizeInt);
					file = newFile;
				}
			}else {
				file = newFile;
			}
		}
		
		resp.addHeader("Content-Type", contentType);
		resp.addDateHeader("Last-Modified", file.lastModified());
		resp.addHeader("Content-Length", String.valueOf(file.length()));
		resp.flushBuffer();
		FileInputStream fi = new FileInputStream(file);
		BufferedInputStream bi = new BufferedInputStream(fi);
		byte[] buffer = new byte[4096];
		int read = 0;
		while ((read = bi.read(buffer)) != -1) {
			resp.getOutputStream().write(buffer, 0, read);
		}
	}
}
