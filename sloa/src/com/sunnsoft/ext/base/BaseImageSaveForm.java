package com.sunnsoft.ext.base;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.util.FileStore;

public abstract class BaseImageSaveForm extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected FileStore fileStore;// 目标
	protected FileStore userTempStore;// 源
	protected HttpServletRequest request;
	protected String errorMsg;

	public FileStore getFileStore() {
		return fileStore;
	}

	public void setFileStore(FileStore fileStore) {
		this.fileStore = fileStore;
	}

	public FileStore getUserTempStore() {
		return userTempStore;
	}

	public void setUserTempStore(FileStore userTempStore) {
		this.userTempStore = userTempStore;
	}

	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	/**
	 * 提供上传文件保存的名字
	 * 
	 * @return
	 */
	protected abstract String getUploadFileName();

	/**
	 * 提供需要删除的旧文件的名字。(当文档更新的时候)
	 * 
	 * @return
	 */
	protected abstract String getOldFileName();

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String execute() throws Exception {
		String uploadFileName = getUploadFileName();
		if (StringUtils.isNotBlank(uploadFileName)) {
			File file = new File(userTempStore.getFile(request.getSession()
					.getId()), uploadFileName);
			String oldFileName = getOldFileName();
			if (!uploadFileName.equals(oldFileName)) {// 文件名与旧图不一样，表示有新图片,需要保存。
				if (oldFileName != null) {// 旧图如果存在，则删除旧图片
					File oldFile = new File(fileStore.getRootFile(),
							oldFileName);
					oldFile.delete();
				}
				File dulplicatFile = new File(fileStore.getRootFile(),
						uploadFileName);
				if (dulplicatFile.exists()) {// 新图片和其他文件名冲突了。
					System.out.println("图片未保存，存在同名文件" + uploadFileName + "!");
					this.setErrorMsg("图片未保存，存在同名文件" + uploadFileName);
					return "failure";
				}
				FileUtils.copyFileToDirectory(file, fileStore.getRootFile());
			}
		}
		return super.execute();
	}

}
