package com.sunnsoft.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 对于内网无法访问的外网图片的机器，有时候需要转发外网的图片到内网，则使用此filter。
 * @author llade
 *
 */
public class ImageBridge extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String s = req.getQueryString(); 
			URL url = new URL(s);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setInstanceFollowRedirects(true);
			// 允许读写
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 缓存关闭
			connection.setUseCaches(false);

			connection.setRequestMethod("GET");
			InputStream in = connection.getInputStream();
			
			String contentType = connection.getHeaderField("Content-Type");
			if(contentType != null){
				resp.addHeader("Content-Type", contentType);
			}else{
				resp.addHeader("Content-Type", "image/jpeg");
			}

			BufferedInputStream bi = new BufferedInputStream(in);
			byte[] buffer = new byte[4096];
			int read = 0;
			while ((read = bi.read(buffer)) != -1) {
				resp.getOutputStream().write(buffer, 0, read);
			}
		} catch (Exception e) {
			resp.setStatus(404);
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

}
