package com.sunnsoft.ext.base;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.util.FileStore;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class StrutsUploadAction extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected FileStore userTempStore;
	protected HttpServletRequest request;
	protected File upload;
	protected String uploadContentType;
	protected String uploadFileName;
	protected String saveName;
	protected String msg;

	public FileStore getUserTempStore() {
		return userTempStore;
	}

	public void setUserTempStore(FileStore fileStore) {
		this.userTempStore = fileStore;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	protected File beforeSave(File file) {
		return file;
	}

	@Override
	@Action("file-upload-input")
	public String input() throws Exception {
		return SUCCESS;
	}

	public String execute() throws Exception {
		if (uploadFileName == null) {
			this.msg = "不支持的文件类型或大小超过限制";
			return "failure";
		}
		if (uploadFileName.lastIndexOf(".") == -1) {
			this.msg = "非法的文件后缀";
			return "failure";
		}
		String suffix = uploadFileName.substring(uploadFileName
				.lastIndexOf("."));
		saveName = java.util.UUID.randomUUID().toString() + suffix;
		File tempStore = userTempStore.getFile(request.getSession().getId());
		if (!tempStore.exists()) {
			tempStore.mkdir();
		}
		File storeFile = new File(tempStore, saveName);
		if (storeFile.exists()) {
			this.msg = "文件上传冲突，请再上传一次";
			return "failure";
		}
		// System.out.println(upload);
		// System.out.println(this.uploadContentType);
		upload = this.beforeSave(upload);
		FileUtils.copyFile(upload, storeFile);

		// System.out.println("upload===========" + upload.getAbsoluteFile()
		// + ",storeFile====" + storeFile.getAbsoluteFile());

		this.msg = "上传成功";

		// System.out.println("uploadFileName====" + uploadFileName +
		// ",saveName="
		// + saveName);
		// ActionContext.getContext().getSession().put("uploadFileName",
		// uploadFileName);
		// ActionContext.getContext().getSession().put("saveName", saveName);
		// ActionContext.getContext().getSession().put("storeFile",
		// storeFile.getAbsoluteFile());

		return super.execute();
	}

	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

}
