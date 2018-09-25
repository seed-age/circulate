package com.sunnsoft.sloa.actions.app.send;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.gteam.util.FastJSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.lenovo.css.lenovocloud.sdk.model.UploadModel;
import com.lenovo.css.lenovocloud.sdk.model.UserLoginModel;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.FileStore;
import com.sunnsoft.util.struts2.Results;

/**
 * 已发传阅(APP端)--传阅详情页面--附件列表---点击下载附件--从网盘上下载附件.
 * 
 * @author chenjian
 *
 */
public class SendDownloadFile extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SendDownloadFile.class);

	private Long itemId; // 附件ID
	private Integer userId; // 用户ID

	@Resource
	private Config config;

	@Resource
	private FileStore generalFileStore;

	@Action(interceptorRefs = { @InterceptorRef(value = "fileUpload"), @InterceptorRef("extStack") })
	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(itemId, "附件ID不能为空!");
		Assert.notNull(userId, "用户ID不能为空!");

		AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().getItemId().Eq(itemId)
				.uniqueResult();

		// 获取后缀
		String fileCategory = attachmentItem.getFileCategory();
		String[] type = { "zip", "rar", "dwg", "dxf", "dwt", "dws", "ipa", "pxl", "deb", "apk", "xap" };

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

		String fileName1 = attachmentItem.getLocalhostUrlPath() != null ? attachmentItem.getLocalhostUrlPath() : attachmentItem.getSaveName();
		File file = generalFileStore.getFile(fileName1);
		if(file.exists()) {
			LOGGER.warn("存在。。。。......." + fileName1);
			
			json = FastJSONUtils.getJsonHelper().toJSONString(
					"/file/" + fileName1);
			
			LOGGER.warn("已经下载的文件，预览路径：            /file/" + fileName1);
			
			success = true;
			code = "200";
			msg = "下载附件成功!";
			return Results.GLOBAL_FORM_JSON;
		}else {
			LOGGER.warn("不存在。。。。" + fileName1);
		}
		
		// 获取附件上传的URL
		String urlPath = attachmentItem.getUrlPath();

		// 获取文件上传到网盘的ID
		Long neid = attachmentItem.getItemNeid();

		// 获取文件上传到网盘的版本
		String rev = attachmentItem.getItemRev();

		LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);

		String session = null;

		UserMssage userMssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId).uniqueResult();

		try {
			session = LenovoCloudSDKUtils.getSession(userMssage);
		} catch (Exception e) {
			e.printStackTrace();
			// 设置正式账号和密码
			String userSlug = config.getUserSlug();
			String password = config.getPassword();
			// 用户登录(测试用户)
			// 调用登录方法, 设置账户和密码
			UserLoginModel login = sdk.login(userSlug, password);
			// 获取session
			session = login.getXLENOVOSESSID();
		}
		// 判断
		Integer differentiate = attachmentItem.getItemDifferentiate();
		boolean status = false;
		InputStream downloadFile = null;
		if (differentiate == 0) {
			// 返回一个流
			downloadFile = sdk.downloadFile(urlPath, session, neid, rev, PathType.SELF);
			// 使用system_OA账号进行上传文件
			LenovoCloudSDK lenovoCloudSDK = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
			String systemOAsession = LenovoCloudSDKUtils.getLenovoCloudSDKSession(lenovoCloudSDK, config);
			// 获取文件原名
			String name = attachmentItem.getFileName();
			// 2. 获取上传文件的后缀
			String typeName = attachmentItem.getFileCategory();
			// 3.1 获取上传附件后缀;
			String suffix = name.substring(name.lastIndexOf("."));
			// 3.2 重新生成上传之后的附件名称;
			String newName = UUID.randomUUID().toString() + suffix;
			// 4.设置上传文件的存储路径
			String path = config.getBoxUploadUrl() + newName;
			// 设置上传文件的标签
			String tags = typeName + ",网盘," + userMssage.getLoginId() + "," + userMssage.getFullName();
			// 调用上传文件
			UploadModel uploadFileWithInputStream = lenovoCloudSDK.uploadFileWithInputStream(downloadFile, path, name,
					tags, systemOAsession, PathType.ENT);
			Long neidUpload = uploadFileWithInputStream.getNeid();
			String revUpload = uploadFileWithInputStream.getRev();

			if (uploadFileWithInputStream != null) {
				// 更新
				attachmentItem.setUrlPath(uploadFileWithInputStream.getPath());
				attachmentItem.setItemNeid(uploadFileWithInputStream.getNeid());
				attachmentItem.setItemRev(uploadFileWithInputStream.getRev());
				attachmentItem.setItemDifferentiate(1);
				Services.getAttachmentItemService().update(attachmentItem);
			}

			// system_OA调用下载方法
			downloadFile = lenovoCloudSDK.downloadFile(uploadFileWithInputStream.getPath(), systemOAsession, neidUpload,
					revUpload, PathType.ENT);
			status = true;
		}

		if (differentiate == 1) {

			// 返回一个流
			downloadFile = sdk.downloadFile(urlPath, session, neid, rev, PathType.ENT);
			
			if(downloadFile != null) {
				// 使用system_OA账号进行上传文件
				LenovoCloudSDK lenovoCloudSDK = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
				String systemOAsession = LenovoCloudSDKUtils.getLenovoCloudSDKSession(lenovoCloudSDK, config);
				downloadFile = sdk.downloadFile(urlPath, systemOAsession, neid, rev, PathType.ENT);
				status = true;
				
			}else {
				
				// 使用system_OA账号进行上传文件
				LenovoCloudSDK lenovoCloudSDK = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
				String systemOAsession = LenovoCloudSDKUtils.getLenovoCloudSDKSession(lenovoCloudSDK, config);
				// 获取文件原名
				String name = attachmentItem.getFileName();
				// 2. 获取上传文件的后缀
				String typeName = attachmentItem.getFileCategory();
				// 3.1 获取上传附件后缀;
				String suffix = name.substring(name.lastIndexOf("."));
				// 3.2 重新生成上传之后的附件名称;
				String newName = UUID.randomUUID().toString() + suffix;
				// 4.设置上传文件的存储路径
				String path = config.getBoxUploadUrl() + newName;
				// 设置上传文件的标签
				String tags = typeName + ",网盘," + userMssage.getLoginId() + "," + userMssage.getFullName();
				// 调用上传文件
				UploadModel uploadFileWithInputStream = lenovoCloudSDK.uploadFileWithInputStream(downloadFile, path, name,
						tags, systemOAsession, PathType.ENT);
				Long neidUpload = uploadFileWithInputStream.getNeid();
				String revUpload = uploadFileWithInputStream.getRev();

				if (uploadFileWithInputStream != null) {
					// 更新
					attachmentItem.setUrlPath(uploadFileWithInputStream.getPath());
					attachmentItem.setItemNeid(uploadFileWithInputStream.getNeid());
					attachmentItem.setItemRev(uploadFileWithInputStream.getRev());
					attachmentItem.setItemDifferentiate(1);
					Services.getAttachmentItemService().update(attachmentItem);
				}
				
				// system_OA调用下载方法
				downloadFile = lenovoCloudSDK.downloadFile(uploadFileWithInputStream.getPath(), systemOAsession, neidUpload,
						revUpload, PathType.ENT);
			}

		}
		// 生成UUID
		String newImageName = UUID.randomUUID().toString() + "." + attachmentItem.getFileCategory();
		
		String fileName = attachmentItem.getFileName();
		// 构建路径
		File storeFile = new File(generalFileStore.getRootFile(), newImageName);
		// 下载文件
		boolean item = inputstreamtofile(downloadFile, storeFile);

		// 对中文进行编码
		String path = "/file/" + storeFile.getName();
		
		LOGGER.warn("本地存储文件路径:  " + "/file/" + storeFile.getName());
		
		if (status || item) {
			// 进行设置: download ---> 设置为 true 表示 强制浏览器下载附件并且不打开
			// rename ---> 表示对下载的附件进行重命名, 并且进行编码
			String encode = URLEncoder.encode(fileName, "UTF-8");
			json = FastJSONUtils.getJsonHelper().toJSONString(
					"/file/" + storeFile.getName() + "?download=true&rename=" + URLEncoder.encode(encode, "UTF-8"));
			LOGGER.warn("返回给APP端的访问路径： " + "/file/" + storeFile.getName() + "?download=true&rename=" + URLEncoder.encode(encode, "UTF-8"));
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
