package com.sunnsoft.util.file.download;

import com.sunnsoft.util.FileStore;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 可以支持断点续传,主要用于文件下载应用。不适合图片等展示在浏览器的文件
 * @author Administrator
 *
 */
public class FileDownloadFilter implements Filter {

	FileStore store;
	boolean sessionPath;//如果为ture，切分出文件名之后，从FileStore获取文件的时候自动把用户session当做父目录，相当于root/sessionId/target.file 映射到url：mapping/target.file,对于临时的session目录非常有用。
	String fileNotFoundDefault;//无图片的时候显示的图片,这个路径和servlet context相对路径。
	ServletContext context;
	@Override
	public void init(FilterConfig config) throws ServletException {
		String beanId = config.getInitParameter("fileStoreBeanId");
		String sessionPathStr = config.getInitParameter("sessionPath");
		fileNotFoundDefault = config.getInitParameter("fileNotFoundDefault");
		if ("true".equals(sessionPathStr))
			sessionPath = true;
		if (beanId == null)
			beanId = "fileStore";
		// System.out.println(WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext()));
		this.context = config.getServletContext();
		store = (FileStore) WebApplicationContextUtils
				.getRequiredWebApplicationContext(context)
				.getBean(beanId);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String fileName = req.getPathInfo();
//		String contentType = MimeTypeDictionary.getMimeType(fileName);
		File file = null;
		if (sessionPath) {
			File userDir = store.getFile(req.getSession().getId());
			if (!userDir.exists()) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} else {
				file = new File(userDir, fileName);
			}
		} else {
			file = store.getFile(fileName);
//			System.out.println("file:"+file);
//			System.out.println("file.exists:"+file.exists());
		}
		if (file == null || !file.exists() || file.isDirectory()) {
			if (fileNotFoundDefault == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} else {
				// System.out.println(this.config.getServletContext());
				file = new File(this.context.getRealPath(
						fileNotFoundDefault));
			}
		}
		OutputStream os = null;

		FileInputStream is = null;
		long start = 0;
		long end = 0;
		try {

			is = new FileInputStream(file);
			long fSize = file.length();
			end = fSize -1;
			//System.out.println("Content-Length:"+fSize);
			byte xx[] = new byte[4096];
			resp.setHeader("Accept-Ranges", "bytes");
			resp.setHeader("Content-Length", fSize + "");
			resp.setHeader("Content-Type", "application/octet-stream");
			resp.setDateHeader("Last-Modified", file.lastModified());
			resp.setHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			String range = req.getHeader("Range");
			long[] ranges = new long[2]; 
			if (range != null) {

				// 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)

				resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				
//				System.out.println("range:"+range);
				Pattern p = Pattern.compile("\\d+");
				Matcher m = p.matcher(range);
				int index = 0;
				while(m.find()){
					if(index == 0){
						start = Long.parseLong(m.group());
					}else{
						long temp = Long.parseLong(m.group());
						if(temp < end)end = temp;
					}
					index ++;
				}
				
			}

			if (start != 0) {

				String contentRange = new StringBuilder("bytes ").append(
						new Long(start).toString()).append("-").append(
						new Long(end).toString()).append("/").append(
						new Long(fSize).toString()).toString();

				resp.setHeader("Content-Range", contentRange);
				resp.flushBuffer();
//				System.out.println("Content-Range=" + contentRange);

				// 略过已经传输过的字节

				is.skip(start);

			}

			os = resp.getOutputStream();
			long trans = 0;

			while (true) {
				long remain = end + 1L - trans;
				int n = 0;
				if(remain >= 4096){
					n = is.read(xx);
				}else if(remain > 0){
					n = is.read(xx, 0, (int)remain);
				}else{
					break;
				}
				if (n != -1 && n > 0) {
					os.write(xx, 0, n);
					trans +=(long)n;
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {

			if (is != null)
				is.close();
			if (os != null)
				os.close();
		}

	}


}
