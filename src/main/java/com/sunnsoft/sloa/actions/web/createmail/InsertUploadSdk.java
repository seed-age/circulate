package com.sunnsoft.sloa.actions.web.createmail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.lenovo.css.lenovocloud.sdk.model.UploadModel;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;

/**
 * 新建传阅--上传附件(上传到联想网盘): 点击添加附件, 可以多个附件上传. 返回数据, 附件的名称, 大小, 状态 (默认上传到个人空间)
 * 
 * @author chenjian
 *
 */
public class InsertUploadSdk extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUploadSdk.class);
	
	private int status; // 0 表示 上传到个人空间   1 上传到企业空间
	private Long userId; // 上传这个附件的传阅对象的ID

	private File file[]; // 上传的文件
	private String fileFileName[]; // 上传的文件名

	@Resource
	private Config config;
	
	@Action(interceptorRefs = { @InterceptorRef(value = "fileUpload"), @InterceptorRef("extStack") })
	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!");
		Assert.notNull(file, "上传的附件不能为空!");

		//根据上传附件的用户ID, 查询该用户的信息
		UserMssage mssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId.intValue()).uniqueResult();
		
		status = 1; //web端 默认上传到企业空间
		
		LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
		
		//使用网盘统一账号 system_OA 进行登录(只限于web端)
		String session = LenovoCloudSDKUtils.getLenovoCloudSDKSession(sdk,config);
		
		LOGGER.warn("web端    --->  进行上传附件, session: " + session);
		// 创建list集合
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		// 上传文件
		// 1. 使用UUID生成附件上传批次ID ,用该批次ID进行管理多个上次附件
		String uuidBulkId = UUID.randomUUID().toString();
		
		for (int i = 0; i < file.length; i++) {
			// 创建map集合, 存放上传完附件后,返回给前端的数据
			Map<String, Object> map = new HashMap<String, Object>();

			// 2. 获取上传文件的后缀
			String typeName = fileFileName[i].substring(fileFileName[i].lastIndexOf(".") + 1).trim();

			// 3. 获取由UUID+图片后缀生成的图片名字
			// 3.1 获取上传附件后缀;
			String suffix = fileFileName[i].substring(fileFileName[i].lastIndexOf("."));
			// 3.2 重新生成上传之后的附件名称;
			String newName = UUID.randomUUID().toString() + suffix;

			// 4.设置存储上传文件的路径
			String path = config.getBoxUploadUrl() + newName;

			// 设置上传文件的标签
			String tags = typeName + ",网盘," + mssage.getLoginId() + "," + mssage.getFullName();

			/*
			 * 调用上传文件的方法 file(文件) , path(网盘中的路径) , filename(文件名(不是uuid生成的文件名)必填，即文件的备注名，
			 * 上传至网盘中，会写入到备注中，) , tags(标签列表) pathType - PathType.ENT : 企业空间， PathType.SELF :
			 * 个人空间, 默认为个人空间，如果传入null，则认为是个人空间
			 */

			AttachmentItem attachmentItem = null;
			
			if (status == 0) {// 0 表示 个人空间 
				UploadModel uploadFile = sdk.uploadFile(file[i], path, fileFileName[i], tags, session, PathType.SELF);

				attachmentItem = getItem(uploadFile, list, uuidBulkId, i, map, typeName, newName, mssage);
			}
			
			if (status == 1) { //1 表示企业空间

				UploadModel uploadFile = sdk.uploadFile(file[i], path, fileFileName[i], tags, session, PathType.ENT);
				
				attachmentItem = getItem(uploadFile, list, uuidBulkId, i, map, typeName, newName, mssage);
			}

			if (attachmentItem != null) {
				success = true;
				msg = "上传附件成功..";
				code = "200";
			}
		}

		json = JSONObject.toJSONString(list);

		if (json != null) {
			
			LOGGER.warn("web端    --->  上传附件成功!::::::::::::::::");
			return Results.GLOBAL_FORM_JSON;
		} else {
			success = false;
			msg = "上传附件失败..";
			code = "404";
			return Results.GLOBAL_FORM_JSON;
		}
	}

	public AttachmentItem getItem(UploadModel uploadFile, List<Map<String, Object>> list, String uuidBulkId, int i,
			Map<String, Object> map, String typeName, String newName, UserMssage mssage) {
		Long neid = uploadFile.getNeid();
		String rev = uploadFile.getRev();
		LOGGER.warn("web端    --->  neid: " + neid);
		LOGGER.warn("web端    --->  rev: " + rev);
		AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().bean().create()
				.setBulkId(uuidBulkId).setUserId(userId).setCreator(mssage.getLastName()).setCreateTime(new Date())
				.setFileName(fileFileName[i]).setFileCategory(typeName).setSaveName(newName)
				.setUrlPath(uploadFile.getPath()).setAttached(false).setState(0).setItemSize(uploadFile.getSize())
				.setItemNeid(neid).setItemRev(rev).setItemDifferentiate(status).insertUnique();

		map.put("itemId", attachmentItem.getItemId());
		map.put("fileName", attachmentItem.getFileName());
		map.put("itemSize", attachmentItem.getItemSize());
		map.put("state", attachmentItem.getState());
		map.put("bulkId", attachmentItem.getBulkId()); // 添加附件上传批次ID

		list.add(map);
		
		return attachmentItem;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}
}
