package com.sunnsoft.sloa.actions.web.createmail;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.FileStore;
import com.sunnsoft.util.struts2.Results;

public class Test extends BaseParameter {

	private static final long serialVersionUID = -7308053269880898030L;

	@Resource
	private Config config;
	
	@Resource
	private FileStore generalFileStore;
	
	@Override
	public String execute() throws Exception {

		Map<String, Object> result = LenovoCloudSDKUtils.getSessionAndS("limin5");
		String session = (String) result.get("session");
		String s = (String) result.get("s");
		String code = (String) result.get("code");
		System.out.println(session);
		if(code.equals("401")) {
			msg = "该用户没有开通网盘账号!";
			json = "null";
			return Results.GLOBAL_SUCCESS_JSON;
		}
		
		String path = "测试文件";
		path = URLEncoder.encode(path, "UTF-8");
		
		String searchName = "c";
		searchName = URLEncoder.encode(searchName, "UTF-8");
		System.out.println(searchName);
		
		String appSearch = LenovoCloudSDKUtils.getAPPSearch(session, s, path, "self", 10, 3, "desc", searchName);
		
		System.out.println(URLDecoder.decode(searchName, "UTF-8"));
		
		msg = "查询文件成功!";
		json = appSearch;
		return Results.GLOBAL_FORM_JSON;
	}

	/**
	 * 删除文件夹下的所有文件
	 * @param file
	 */
	private void deleteFile(File file) {
		
		if (file.isFile()) {// 判断是否为文件，是，则删除
			System.out.println(file.getAbsoluteFile());// 打印路径
			file.delete();
		} else {// 不为文件，则为文件夹
			String[] childFilePath = file.list();// 获取文件夹下所有文件相对路径
			for (String path : childFilePath) {
				File childFile = new File(file.getAbsoluteFile() + "/" + path);
				deleteFile(childFile);// 递归，对每个都进行判断
			}
			System.out.println(file.getAbsoluteFile());
			file.delete();
		}
	}

}
