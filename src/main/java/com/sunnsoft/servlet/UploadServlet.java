package com.sunnsoft.servlet;

import com.sunnsoft.exception.UpLoadImgFormatException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.RandomStringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
/**
 * 
 * @author llade
 * 不够通用，废弃，仅作为代码示例参考。
 */
@Deprecated
public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1000 * 1024 * 1024);
		List<?> itemList = null;

		String localName = null;
		String fileName = null;
		try {
			itemList = upload.parseRequest(req);
			if (itemList != null) {
				Iterator<?> it = itemList.iterator();
				while (it.hasNext()) {
					FileItem item = (FileItem) it.next();
					if (chkImgName(item.getName())) {
						if (!item.isFormField()) {
							String uploadPath = this
									.getServletContext()
									.getRealPath(
											"/temp/" + req.getSession().getId());
							String totalName = item.getName();
							localName = "temp";
							if (totalName != null) {
								int index = totalName.lastIndexOf("\\");
								localName = totalName.substring(index + 1);
							}
							String suffix = localName.substring(localName
									.lastIndexOf(".") + 1);
							fileName = RandomStringUtils.random(18, true, true)
									+ "." + suffix;
							File userDir = new File(uploadPath);
							if (!userDir.exists()) {
								userDir.mkdir();
							}
							try {
								File file = new File(userDir, fileName);
								item.write(file);
								req.setAttribute("uploadMsg", "上传成功！");
								req.setAttribute("localName", localName);
								req.setAttribute("fileName", fileName);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
								req.setAttribute("uploadError", "上传错误：找不到"
										+ uploadPath + "请检查！");
							} catch (Exception e) {
								e.printStackTrace();
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
			req.setAttribute("uploadMsg", "请求数据的size超出了规定的大小");
			// req.getRequestDispatcher("/uploadError.jsp").forward(req, resp);
		} catch (FileUploadBase.InvalidContentTypeException e) {
			// 无效的请求类型,即请求类型enctype != "multipart/form-data"
			req.setAttribute("uploadMsg", "请求类型enctype != multipart/form-data");
			// req.getRequestDispatcher("/uploadError.jsp").forward(req, resp);
		} catch (FileUploadException e) {
			// 如果都不是以上子异常,则抛出此总的异常,出现此异常原因无法说明.
			req.setAttribute("uploadMsg", "上传过程异常，导致其原因可能是磁盘已满或者其它原因");
			// req.getRequestDispatcher("/uploadError.jsp").forward(req, resp);
		} catch (UpLoadImgFormatException e) {
			req.setAttribute("uploadMsg", "只能上传jpg 或 gif或png 格式的图片文件!");
		} finally {
			req.getRequestDispatcher("/Main/backend/upload.jsp").forward(req,
					resp);
		}
	}

	private static boolean chkImgName(String imgName) {
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
}
