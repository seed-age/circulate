package com.sunnsoft.ext.base;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.exception.UpLoadImgFormatException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.util.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author llade
 * 
 * @deprecated 用 StrutsUploadAction代替
 */


public class UploadAction extends ActionSupport implements ServletRequestAware,
		ServletContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Log logger = LogFactory.getLog(UploadAction.class);

	protected HttpServletRequest request;
	protected ServletContext context;
	protected int sizeThreshold = 4 * 1024;
	protected long sizeMax = 1024 * 1024 * 1024;
	protected String msg;
	protected String uploadFileName;
	protected String randomFileName;

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getRandomFileName() {
		return randomFileName;
	}

	public void setRandomFileName(String randomFileName) {
		this.randomFileName = randomFileName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getSizeThreshold() {
		return sizeThreshold;
	}

	public void setSizeThreshold(int sizeThreshold) {
		this.sizeThreshold = sizeThreshold;
	}

	public long getSizeMax() {
		return sizeMax;
	}

	public void setSizeMax(long sizeMax) {
		this.sizeMax = sizeMax;
	}

	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	@Override
	public String execute() throws Exception {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(sizeThreshold);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(sizeMax);
		List<?> itemList = null;

		try {
			itemList = upload.parseRequest(request);
			if (itemList != null) {
				Iterator<?> it = itemList.iterator();
				while (it.hasNext()) {
					FileItem item = (FileItem) it.next();
					if (chkImgName(item.getName())) {
						if (!item.isFormField()) {
							String uploadPath = this.context
									.getRealPath("/temp/"
											+ request.getSession().getId());
							System.out.println("图片上传======================="
									+ uploadPath);
							String totalName = item.getName();
							uploadFileName = "temp";
							if (totalName != null) {
								int index = totalName.lastIndexOf("\\");
								uploadFileName = totalName.substring(index + 1);
							}
							String suffix = uploadFileName
									.substring(uploadFileName.lastIndexOf(".") + 1);
							randomFileName = RandomStringUtils.random(18, true,
									true)
									+ "." + suffix;
							File userDir = new File(uploadPath);
							if (!userDir.exists()) {
								userDir.mkdir();
							}
							try {
								File file = new File(userDir, randomFileName);
								item.write(file);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
								msg = "上传错误：找不到" + uploadPath + ",请联系系统管理员";
								logger.error(msg, e);
								return "failure";
							} catch (Exception e) {
								e.printStackTrace();
								msg = "服务器文件写入磁盘异常,请联系系统管理员";
								logger.error(msg, e);
								return "failure";
								// 文件写入磁盘异常，待处理。。。。
							}
						}
					} else {
						throw new UpLoadImgFormatException();
					}
				}
			}// 参数接受完毕,并文件上传完毕
		} catch (FileUploadBase.SizeLimitExceededException e) {
			// 请求数据的size超出了规定的大小.
			e.printStackTrace();
			msg = "请求数据超出了规定的大小";
			logger.error(msg, e);
			return "failure";
			// request.setAttribute("uploadMsg", "请求数据的size超出了规定的大小");
			// req.getRequestDispatcher("/uploadError.jsp").forward(req, resp);
		} catch (FileUploadBase.InvalidContentTypeException e) {
			// 无效的请求类型,即请求类型enctype != "multipart/form-data"
			msg = "无效的请求类型,请求类型必须是enctype = \"multipart/form-data\"";
			logger.error(msg, e);
			return "failure";
			// request.setAttribute("uploadMsg",
			// "请求类型enctype != multipart/form-data");
			// req.getRequestDispatcher("/uploadError.jsp").forward(req, resp);
		} catch (FileUploadException e) {
			// 如果都不是以上子异常,则抛出此总的异常,出现此异常原因无法说明.
			msg = "上传过程异常，导致其原因可能是磁盘已满或者其它原因";
			logger.error(msg, e);
			return "failure";
			// request.setAttribute("uploadMsg", "上传过程异常，导致其原因可能是磁盘已满或者其它原因");
			// req.getRequestDispatcher("/uploadError.jsp").forward(req, resp);
		} catch (UpLoadImgFormatException e) {
			msg = "只能上传jpg 或 gif或png 格式的图片文件";
			logger.error(msg, e);
			return "failure";
			// request.setAttribute("uploadMsg", "只能上传jpg 或 gif或png 格式的图片文件!");
		}
		msg = uploadFileName + "上传成功";
		return SUCCESS;
	}

	protected static boolean chkImgName(String imgName) {
		String tempName = imgName.substring(imgName.length() - 3, imgName
				.length());
		if (tempName.equalsIgnoreCase("jpg")
				|| tempName.equalsIgnoreCase("gif")
				|| tempName.equalsIgnoreCase("png")
				|| tempName.equalsIgnoreCase("jpeg")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setServletContext(ServletContext ctx) {
		this.context = ctx;
	}
}
