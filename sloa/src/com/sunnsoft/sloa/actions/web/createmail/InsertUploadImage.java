package com.sunnsoft.sloa.actions.web.createmail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.opensymphony.xwork2.ActionContext;
import com.sunnsoft.sloa.actions.common.BaseParameter;
/**
 * 新建传阅--富文本上传图片
 * @author chenjian
 *
 */
public class InsertUploadImage extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private String err = ""; //默认为空字符串
	private String msg; // 返回信息
	private File fileData; // 上传文件
	private String fileDataFileName; // 文件名
	
	@Action(interceptorRefs = { @InterceptorRef(value = "fileUpload"), @InterceptorRef("extStack") })
	@Override
	public String execute() throws Exception {

		// 获取response、request对象
		ActionContext ac = ActionContext.getContext();
		HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);

		response.setContentType("text/html;charset=gbk");

		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String saveRealFilePath = ServletActionContext.getServletContext().getRealPath("/web/images/upload");
		File fileDir = new File(saveRealFilePath);
		if (!fileDir.exists()) { // 如果不存在 则创建
			fileDir.mkdirs();
		}
		File savefile;
		savefile = new File(saveRealFilePath + "/" + fileDataFileName);
		try {
			FileUtils.copyFile(fileData, savefile);
		} catch (IOException e) {
			err = "错误" + e.getMessage();
			e.printStackTrace();
		}
		String file_Name = request.getContextPath() + "/web/images/upload/" + fileDataFileName;

		msg = "{\"success\":\"" + true + "\",\"file_path\":\"" + file_Name + "\"}";
		out.print(msg); // 返回msg信息
		
		
		
		return null;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public File getFileData() {
		return fileData;
	}

	public void setFileData(File fileData) {
		this.fileData = fileData;
	}

	public String getFileDataFileName() {
		return fileDataFileName;
	}

	public void setFileDataFileName(String fileDataFileName) {
		this.fileDataFileName = fileDataFileName;
	}

}
