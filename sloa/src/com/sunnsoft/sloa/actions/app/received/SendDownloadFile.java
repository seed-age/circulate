package com.sunnsoft.sloa.actions.app.received;

import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.FileStore;
import com.sunnsoft.util.struts2.Results;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.gteam.util.FastJSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * 已发传阅--传阅详情页面--附件列表---点击下载附件--从网盘上下载附件.
 * 
 * @author chenjian
 *
 */
public class SendDownloadFile extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SendDownloadFile.class);
	
	private Long itemId; // 附件ID
	private Integer userId; //用户ID

	@Resource
	private Config config;
	
	@Resource
	private FileStore generalFileStore;

	@Action(interceptorRefs = { @InterceptorRef(value = "fileUpload"), @InterceptorRef("extStack") })
	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(itemId, "附件ID不能为空!");
		//Assert.notNull(userId, "用户ID不能为空!");

		AttachmentItem attachmentItem = Services.getAttachmentItemService()
				.createHelper().getItemId().Eq(itemId).uniqueResult();

		// 获取后缀
		String fileCategory = attachmentItem.getFileCategory();
		String[] type = { "zip", "rar", "gif", "webp", "apng", "dwg", "dxf", "dwt", "dws", "ipa", "pxl", "deb", "apk", "xap" };

		for (String typeName : type) {
			// 判断是否相同
			if (fileCategory.equalsIgnoreCase(typeName)) {
				success = false;
				code = "205";
				msg = "该文件类型不支持预览!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		}

		String fileName = attachmentItem.getLocalhostUrlPath() != null ? attachmentItem.getLocalhostUrlPath() : attachmentItem.getSaveName();
		File file = generalFileStore.getFile(fileName);
		if(file.exists()) {
			LOGGER.warn("存在。。。。......." + fileName);
			
			json = FastJSONUtils.getJsonHelper().toJSONString(
					"/file/" + fileName);
			
			LOGGER.warn("已经下载的文件，预览路径：            /file/" + fileName);
			
			success = true;
			code = "200";
			msg = "下载附件成功!";
			return Results.GLOBAL_FORM_JSON;
		}else {
			LOGGER.warn("不存在。。。。" + fileName + "  需要重新下载 ");
		}
		// 获取附件上传的URL
		String urlPath = attachmentItem.getUrlPath();

		// 获取文件上传到网盘的ID
		Long neid = attachmentItem.getItemNeid();

		// 获取文件上传到网盘的版本
		String rev = attachmentItem.getItemRev();
		
		//网盘对象
		LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
		//返回system_OA用户登录网盘的session
		String session = LenovoCloudSDKUtils.getLenovoCloudSDKSession(sdk, config);
		
		LOGGER.warn("system_OA用户登录网盘的session: " + session);
		
		// 判断
		//Integer differentiate = attachmentItem.getItemDifferentiate();
		boolean status = false;
		/*if (differentiate == 0) {
			// 返回一个流
			downloadFile = sdk.downloadFile(urlPath, session, neid, rev, PathType.SELF);
			status = true;
		}*/
		//if (differentiate == 1) {

		InputStream downloadFile = sdk.downloadFile(urlPath, session, neid, rev, PathType.ENT);
		//}
		LOGGER.warn("下载文件返回的流对象: " + downloadFile);
		// 生成UUID
		String newImageName = UUID.randomUUID().toString() + "." + attachmentItem.getFileCategory();
		// 构建路径
		File storeFile = new File(generalFileStore.getRootFile(), newImageName);
		// 下载文件
		boolean item = inputstreamtofile(downloadFile, storeFile);
		status = true;
		//拼接URL
		String path = "/file/" + storeFile.getName();
		//LOGGER.warn("本地文件预览的路径: " + path);
		// 对中文进行编码
		if (status || item) {
			json = FastJSONUtils.getJsonHelper().toJSONString(path);
			
			LOGGER.warn("返回给APP端的访问路径： " + "/file/" + storeFile.getName());
			attachmentItem.setLocalhostUrlPath(storeFile.getName());
			attachmentItem.setUpdateTime(new Date());
			Services.getAttachmentItemService().update(attachmentItem);
			
			success = true;
			code = "200";
			msg = "下载附件成功!";
			return Results.GLOBAL_FORM_JSON;
		}
		success = false;
		code = "404";
		msg = "下载附件失败!";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
	}

	/**
	 *
	 * @param ins
	 *            输入文件流
	 * @param file
	 *            要保存的文件路径
	 */
	public static boolean inputstreamtofile(InputStream ins, File file) {
		boolean status = false;
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
				if (bytesRead > -1) {
					status = true;
				}
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}