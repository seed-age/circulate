package com.sunnsoft.ext.base;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import java.io.File;

public abstract class StrutsExcelImportAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected File excel;
	protected String excelContentType;
	protected String excelFileName;
	// protected String msg = "上传成功";
	protected String msg = "";
	protected String result = "success";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public File getExcel() {
		return excel;
	}

	public void setExcel(File excel) {
		this.excel = excel;
	}

	public String getExcelContentType() {
		return excelContentType;
	}

	public void setExcelContentType(String excelContentType) {
		this.excelContentType = excelContentType;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	protected abstract void importData(File excelfile);

	@Override
	@Action(interceptorRefs = {
			@InterceptorRef(value = "fileUpload", params = {
					"allowedExtensions", "xls,xlsx", "maximumSize", "504800000" }),
			@InterceptorRef("extStack") })
	public String execute() throws Exception {
		// if(uploadFileName == null){
		// this.msg = "不支持的文件类型或大小超过限制";
		// return "failure";
		// }
		// if(uploadFileName.lastIndexOf(".") == -1){
		// this.msg = "非法的文件后缀";
		// return "failure";
		// }
		System.out.println(this.excelFileName);
		System.out.println(this.excelContentType);
		System.out.println(this.excel);
		this.importData(excel);
		return this.result;
	}

}
