package com.sunnsoft.actionbase;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.util.FileStore;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Deprecated
public abstract class BaseUploadFileSave extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected FileStore fileStore;// 目标
	protected FileStore userTempStore;// 源
	protected HttpServletRequest request;
	protected String errorMsg;

	protected int editId;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
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
		// System.out.println("uploadFileName=bas="+getUploadFileName());

		// SimpleDateFormat sd=new SimpleDateFormat("yyyyMMdd");
		// Date d=new Date();
		// String fileName=sd.format(new Date())
		// String fileName= sd.format(new Date())+this.eid+"_"+uploadFileName;
		// ActionContext.getContext().getSession().put("fileNameForAdd",fileName);//保存的文件
		String oldFileName = getOldFileName();
		if (oldFileName != null) {// 旧图如果存在，则删除旧图片
			File oldFile = new File(fileStore.getRootFile(), oldFileName);
			oldFile.delete();
		}

		String uploadFileName = getUploadFileName();
		if (StringUtils.isNotBlank(uploadFileName)) {
			File file = null;
			// 是新增的文件，文件名称从会话里面读出来的
			if (uploadFileName.equals("fileNameForAdd")) {
				uploadFileName = (String) ActionContext.getContext()
						.getSession().get("fileNameForAdd");
				file = new File(userTempStore.getFile(request.getSession()
						.getId()), uploadFileName);
				File file2 = new File(userTempStore.getFile(request
						.getSession().getId()), this.editId
						+ uploadFileName.substring(14));
				file.renameTo(file2);// 把file重命名了file2,再将file2复制到源目当
				FileUtils.copyFileToDirectory(file2, fileStore.getRootFile());

			} else {
				file = new File(userTempStore.getFile(request.getSession()
						.getId()), uploadFileName);
				FileUtils.copyFileToDirectory(file, fileStore.getRootFile());
			}

		}

		return super.execute();
	}

	public int getEditId() {
		return editId;
	}

	public void setEditId(int editId) {
		this.editId = editId;
	}

}
