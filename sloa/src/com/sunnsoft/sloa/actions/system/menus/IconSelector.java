package com.sunnsoft.sloa.actions.system.menus;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.util.struts2.Results;

public class IconSelector extends ActionSupport implements ServletContextAware{

	private ServletContext context;
	
	private String json;
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -826975297284694218L;

	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}

	@Override
	public String execute() throws Exception {
		String extIconDir = "extjs/icons/img/";
		File dir = new File(context.getRealPath(extIconDir));
		File[] files = dir.listFiles();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < files.length; i++) {
			sb.append("{\"iconPath\":\"");
			File file = files[i];
			String filePath = "../" + extIconDir + file.getName();
			sb.append(filePath).append("\"}");
			if(i != files.length-1){
				sb.append(",");
			}
		}
		sb.append("]");
		json = sb.toString();
		return Results.GLOBAL_JSON;
	}
	
	
}
