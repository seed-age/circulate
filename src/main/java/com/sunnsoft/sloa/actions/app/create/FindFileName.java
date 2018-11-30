package com.sunnsoft.sloa.actions.app.create;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.FileModel;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.lenovo.css.lenovocloud.sdk.model.UploadModel;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * 根据文件ID查询文件信息,并添加文件到本地数据库
 *
 * @author chenjian
 *
 */
public class FindFileName extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FindFileName.class);

	private long mailId; // 传阅ID
	private Long[] neid; // 文件ID
	private Long userId; // 用户ID

	@Resource
	private Config config;

	@Override
	public String execute() throws Exception {
		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!");
		Assert.notNull(neid, "上传的附件不能为空!");

		Mail mail1 = Services.getMailService().findById(mailId);
		if(mail1.getStepStatus() == 3){ // 已完成的传阅  不能进行添加文件的操作
			success = false;
			msg = "已完成的传阅, 不能进行添加文件的操作";
			code = "205";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		// 网盘对象
		LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
		// 根据用户ID查询用户信息
		UserMssage mssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId.intValue())
				.uniqueResult();

		String session = null;
		try {
			//根据SSO登录获取当前用户的session
			session = LenovoCloudSDKUtils.getSession(mssage);
			if(session.equals("401")) {
				success = false;
				code = "205";
				msg = "请开通网盘账号";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
//			LOGGER.warn("根据LoginId获取session失败: :::::::::" + session);
			/*//设置正式环境的账号和密码
			String userSlug = config.getUserSlug();
			String password = config.getPassword();
			// 用户登录(测试用户)
			// 调用登录方法, 设置账户和密码
			UserLoginModel login = sdk.login(userSlug, password);
			// 获取session
			session = login.getXLENOVOSESSID();*/
		}

		try {

			// 校验文件是否是cad文件
			for (int i = 0; i < neid.length ; i++) {
				// 根据文件ID获取到文件信息
				FileModel fileModel = sdk.getFileById(neid[i], session);

				// 文件原名
				String desc = fileModel.getDesc();
				String fileName = null;
				//文件后缀
				String typeName = null;

				//判断
				if (desc.equals("")) {
					//使用路径中的文件名
					String path = fileModel.getPath();
					//分割
					String[] split = path.split("/");
					//取最后一个, 得到文件名称
					fileName = split[split.length - 1];

					// 获取文件后缀
					typeName = fileName.substring(fileName.lastIndexOf(".") + 1).trim();

				} else {
					//得到文件名称
					fileName = desc;
					// 获取文件后缀
					typeName = desc.substring(desc.lastIndexOf(".") + 1).trim();
				}

				// 如果是 cad 文件, 则跳过此次循环.
				if (typeName.equalsIgnoreCase("dwt") || typeName.equalsIgnoreCase("dwg") || typeName.equalsIgnoreCase("dws") || typeName.equalsIgnoreCase("dxf")) {
					success = false;
					code = "205";
					msg = "目前不支持dwg格式的文件";
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}

			}
			Map<String, Object> map = new HashMap<>();
			List<AttachmentItem> list = new ArrayList<>();
			// 生成批次ID
			String uuId = UUID.randomUUID().toString();
			// 状态
			boolean item = false;

			int count = 0;
			for (int i = 0; i < neid.length; i++) {

				// 根据文件ID获取到文件信息
				FileModel fileModel = sdk.getFileById(neid[i], session);

				// 文件原名
				String desc = fileModel.getDesc();
				LOGGER.warn("文件原名: " + desc);
				//文件名
				String fileName = null;
				//文件后缀
				String typeName = null;
				//上传附件后缀
				String suffix = null;
				//判断
				if (desc.equals("")) {
					//使用路径中的文件名
					String path = fileModel.getPath();
					LOGGER.warn("文件路径: " + path);
					//分割
					String[] split = path.split("/");
					//取最后一个, 得到文件名称
					fileName = split[split.length - 1];

					// 获取文件后缀
					typeName = fileName.substring(fileName.lastIndexOf(".") + 1).trim();
					// 3.1 获取上传附件后缀;
					suffix = fileName.substring(fileName.lastIndexOf("."));
				} else {
					//得到文件名称
					fileName = desc;
					// 获取文件后缀
					typeName = desc.substring(desc.lastIndexOf(".") + 1).trim();

					// 3.1 获取上传附件后缀;
					suffix = desc.substring(desc.lastIndexOf("."));
				}

				// 3.2 重新生成上传之后的附件名称;
				String newName = UUID.randomUUID().toString() + suffix;

				// 判断该文件存放的位置
				String pathType = fileModel.getPathType();

				// 判断, 如果该文件是存在企业空间 返回 1 个人空间 返回 0
				int status = (pathType.equals("ent")) ? 1 : 0;

				LOGGER.warn("添加的文件归属(企业空间 : 1   个人空间 : 0): " + status);

				Long neidUpload = null;
				String revUpload = null;
				String uploadPath = null;
				String fileSize = null;
				//只有是从个人空间添加文件才会进来
				if(status == 0) {

					LOGGER.warn("开始执行添加的文件上传网盘的企业空间 ::::::::::::");
					//对用户从个人空间添加的文件进行下载, 并且上传到企业空间
					InputStream downloadFile = sdk.downloadFile(fileModel.getPath(), session, fileModel.getNeid(), fileModel.getRev(), PathType.SELF);
					LOGGER.warn("从个人空间添加的文件进行下载, 返回的流: " + downloadFile);
					// 使用system_OA账号进行上传文件
					LenovoCloudSDK lenovoCloudSDK = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
					String systemOAsession = LenovoCloudSDKUtils.getLenovoCloudSDKSession(lenovoCloudSDK, config);
					LOGGER.warn("使用 system_OA 账号进行上传, 返回的session:  " + systemOAsession);
					// 4.设置上传文件的存储路径
					String path = config.getBoxUploadUrl() + newName;
					LOGGER.warn("设置上传文件的存储路径: " + path);
					// 设置上传文件的标签
					String tags = typeName + ",网盘," + mssage.getLoginId() + "," + mssage.getFullName();
					// 调用上传文件
					UploadModel uploadFileWithInputStream = lenovoCloudSDK.uploadFileWithInputStream(downloadFile, path, fileName,
							tags, systemOAsession, PathType.ENT);

					LOGGER.warn("调用上传方法进行上传后, 返回的对象: " + uploadFileWithInputStream);

					neidUpload = uploadFileWithInputStream.getNeid();
					revUpload = uploadFileWithInputStream.getRev();
					uploadPath = uploadFileWithInputStream.getPath();
					fileSize = uploadFileWithInputStream.getSize();
					LOGGER.warn("网盘文件ID: " + neidUpload + " , 网盘文件版本: " + revUpload + " , 存放路径: " + uploadPath + " , 文件大小: " + fileSize);
				}else {
					neidUpload = fileModel.getNeid();
					revUpload = fileModel.getRev();
					uploadPath = fileModel.getPath();
					fileSize = fileModel.getSize();
				}

				//定义标识
				boolean file = false;
				// 判断, 如果传阅ID大于0 说明有参数, 就不是在新建传阅中添加附加的
				if (mailId > 0) {
					// 根据传阅ID查询传阅对象
					Mail mail = Services.getMailService().findById(mailId);

					//需求: 不可以添加名字重复的附件
					//1. 获取到该传阅的所有附件信息
					List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
					//2. 遍历
					for (AttachmentItem attachmentItem : attachmentItems) {
						//获取已经添加的附件名字
						String name = attachmentItem.getFileName();
						//判断
						if(name.equals(fileName)) {
							//如果相同,就进来
							file = true; //设置为true
							break;
						}
					}

					//如果为 true
					if(file) {
						//直接跳过本次循环
						continue;
					}

					// 把文件信息保存到数据库
					/*AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().bean().create()
							.setMail(mail).setBulkId(uuId).setUserId(userId).setCreator(mssage.getLastName())
							.setCreateTime(new Date()).setFileName(fileName).setFileCategory(typeName)
							.setSaveName(newName).setUrlPath(fileModel.getPath()).setAttached(false).setState(0)
							.setItemSize(fileModel.getSize()).setItemNeid(fileModel.getNeid())
							.setItemRev(fileModel.getRev()).setItemDifferentiate(status).insertUnique();*/

					AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().bean().create()
							.setMail(mail).setBulkId(uuId).setUserId(userId).setCreator(mssage.getLastName())
							.setCreateTime(new Date()).setFileName(fileName).setFileCategory(typeName)
							.setSaveName(newName).setUrlPath(uploadPath).setAttached(false).setState(0)
							.setItemSize(fileSize).setItemNeid(neidUpload)
							.setItemRev(revUpload).setItemDifferentiate(status).insertUnique();

					if (mssage != null && attachmentItem != null) {
						//获取接收人对象集合
						List<Receive> receives = mail.getReceives();
						// 设置
						mail.setHasAttachment(true);
						// 更新
						Services.getMailService().update(mail);
						// 遍历
						for (Receive receive : receives) {
							// 判断,只要是已经确认该传阅的联系人, 就需要重新确认
							if (receive.getIfConfirm() == true) {
								// 设置开启重新确认
								receive.setAfreshConfim(true); // true 为开启重新确认
								// 更新
								Services.getReceiveService().update(receive);
								count++;
							}
						}
						item = true;
						if (neid.length == count) {
							success = true;
							code = "200";
							msg = "从网盘中添加文件成功!";
							json = "null";
							return Results.GLOBAL_FORM_JSON;
						}
						continue;
					}
				}

				//如果为 true
				if(file) {
					//直接跳过本次循环
					continue;
				}

				// 把文件信息保存到数据库
				/*AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().bean().create()
						.setBulkId(uuId).setUserId(userId).setCreator(mssage.getLastName()).setCreateTime(new Date())
						.setFileName(fileName).setFileCategory(typeName).setSaveName(newName)
						.setUrlPath(fileModel.getPath()).setAttached(false).setState(0).setItemSize(fileModel.getSize())
						.setItemNeid(fileModel.getNeid()).setItemRev(fileModel.getRev()).setItemDifferentiate(status)
						.insertUnique();*/
				AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().bean().create()
						.setBulkId(uuId).setUserId(userId).setCreator(mssage.getLastName()).setCreateTime(new Date())
						.setFileName(fileName).setFileCategory(typeName).setSaveName(newName)
						.setUrlPath(uploadPath).setAttached(false).setState(0).setItemSize(fileSize)
						.setItemNeid(neidUpload).setItemRev(revUpload).setItemDifferentiate(status)
						.insertUnique();

				if (mssage != null && attachmentItem != null) {
					// 把文件对象添加到map集合中
					list.add(attachmentItem);
					item = true;
				}
			}
			map.put("attachmentItemss", list);

			json = JSONObject.toJSONString(list);

			if (item == true && json != null) {
				success = true;
				code = "200";
				msg = "从网盘中添加文件成功!";
				return Results.GLOBAL_FORM_JSON;
			}

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			code = "4000";
			msg = "从网盘中添加文件失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		success = false;
		code = "205";
		msg = "不能重复添加文件名相同的文件!";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
	}

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
	}

	public Long[] getNeid() {
		return neid;
	}

	public void setNeid(Long[] neid) {
		this.neid = neid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
