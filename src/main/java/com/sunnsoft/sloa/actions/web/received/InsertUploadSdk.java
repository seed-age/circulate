package com.sunnsoft.sloa.actions.web.received;

import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
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
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * (PC端)上传附件(上传到联想网盘): 点击添加附件, 可以多个附件上传. 返回数据, 附件的名称, 大小, 状态 在已收传阅 进行新增上传附件.
 * 	新增完后, 需要开启重新确认.
 *
 * @author chenjian
 *
 */
public class InsertUploadSdk extends BaseParameter {

	private static final long serialVersionUID = 1L;
//	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUploadSdk.class);

	private int status; // 0 表示 上传到个人空间   1 上传到企业空间
	private long userId; // 上传这个附件的传阅对象的ID
	private Long mailId; // 传阅ID

	private File file[]; // 上传的文件
	private String fileFileName[]; // 上传的文件名

	@Resource
	private Config config;

	@Action(interceptorRefs = { @InterceptorRef(value = "fileUpload"), @InterceptorRef("extStack") })
	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!");
		Assert.notNull(mailId, "传阅ID不能为空!");
		Assert.notNull(file, "上传的附件不能为空!");
		//LOGGER.warn("::::::::::: status(0 表示 上传到个人空间   1 上传到企业空间): " + status);

		LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
		status = 1; //web端 默认上传到企业空间
		//使用网盘统一账号 system_OA 进行登录(只限于web端)
		String session = LenovoCloudSDKUtils.getLenovoCloudSDKSession(sdk, config);

		// 上传文件
		// 1. 使用UUID生成附件上传批次ID ,用该批次ID进行管理多个上次附件
//		String uuidBulkId = UUID.randomUUID().toString();

		String nameFile = "";
		try {
			//通过传阅ID获取该传阅的数据
			Mail mail = Services.getMailService().findById(mailId);
			if(mail.getStatus() == 7 || mail.getReceives().size() == 0){
				success = false;
				msg = "您打开的传阅已过期～";
				code = "403";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			if(mail.getStepStatus() == 3){ // 已完成的传阅  不能进行添加文件的操作
				success = false;
				msg = "已完成的传阅, 不能进行添加文件的操作";
				code = "404";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
			//获取存在的附件信息
			List<AttachmentItem> attachmentItems = mail.getAttachmentItems();

			String uuidBulkId = null;
			if(attachmentItems.size() > 0){
				uuidBulkId = attachmentItems.get(0).getBulkId();
			}else {
				uuidBulkId = UUID.randomUUID().toString();
			}

			//根据用户ID查询用户信息
			UserMssage mssage = Services.getUserMssageService().createHelper().getUserId().Eq((int)userId).uniqueResult();

			//定义一个标识
			boolean uploadName = false;
			//遍历
			for (int i = 0; i < file.length; i++) {

				//获取到要上传的文件名
				String name = fileFileName[i].replaceAll(" ", "");

				//遍历附件集合
				for (AttachmentItem attachmentItem : attachmentItems) {
					//取出已存在的附件名字
					String fileName = attachmentItem.getFileName();
					//进行判断, 如果相同
					if(name.equals(fileName)) {
						//设置标识
						uploadName = true;
						nameFile += fileName;
						break; //如果有相同的, 直接停止循环
					}
				}

				//判断
				if(uploadName) {
					//设置
					uploadName = false;
					continue; //如果有相同文件名, 直接跳过本次循环
				}

				// 2. 获取上传文件的后缀
				String typeName = name.substring(name.lastIndexOf(".") + 1).trim();

				// 3. 获取由UUID+图片后缀生成的图片名字
				// 3.1 获取上传附件后缀;
//				String suffix = fileFileName[i].substring(fileFileName[i].lastIndexOf("."));
				// 3.2 重新生成上传之后的附件名称;
//				String newName = UUID.randomUUID().toString() + suffix;

				// 4.设置上传文件的存储路径
//				String path = config.getBoxUploadUrl() + newName;
				String path = config.getBoxUploadUrl();

				if(mssage != null){
//					System.out.println("拼接上传路径: " + config.getBoxUploadUrl() + mssage.getDeptFullname() + "/" + mssage.getFullName() + "/" + mssage.getLastName() + "/" + name);
//					path = config.getBoxUploadUrl() + mssage.getDeptFullname() + "/" + mssage.getFullName() + "/" + mssage.getLastName() + "/" + name;
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH )+1;
					System.out.println(year + " 年 " + month + " 月");
					path = config.getBoxUploadUrl() + year + "/" + month + "/" + uuidBulkId + "/" + name;
				}

				// 设置上传文件的标签
				String tags = typeName + ",网盘," + mssage.getLoginId() + "," + mssage.getFullName();

				/*
				 * 调用上传文件的方法 file(文件) , path(网盘中的路径) , filename(文件名(不是uuid生成的文件名)必填，即文件的备注名，
				 * 上传至网盘中，会写入到备注中，) , tags(标签列表)
				 */
				AttachmentItem attachmentItem = null;
				if(status == 0) {
					UploadModel uploadFile = sdk.uploadFile(file[i], path, name, tags, session, PathType.SELF);
					Long neid = uploadFile.getNeid();
					String rev = uploadFile.getRev();

					attachmentItem = Services.getAttachmentItemService().createHelper().bean().create()
							.setBulkId(uuidBulkId).setUserId(userId).setCreator(mssage.getLastName()).setCreateTime(new Date())
							.setFileName(name).setFileCategory(typeName).setSaveName(name)
							.setUrlPath(uploadFile.getPath()).setAttached(false).setState(2).setItemSize(uploadFile.getSize())
							.setItemNeid(neid).setItemRev(rev).setMail(mail)
							.setItemDifferentiate(status).insertUnique();

				}
				if(status == 1) {
					UploadModel uploadFile = sdk.uploadFile(file[i], path, name, tags, session, PathType.ENT);

					Long neid = uploadFile.getNeid();
					String rev = uploadFile.getRev();

					attachmentItem = Services.getAttachmentItemService().createHelper().bean().create()
							.setBulkId(uuidBulkId).setUserId(userId).setCreator(mssage.getLastName()).setCreateTime(new Date())
							.setFileName(name).setFileCategory(typeName).setSaveName(name)
							.setUrlPath(uploadFile.getPath()).setAttached(false).setState(2).setItemSize(uploadFile.getSize())
							.setItemNeid(neid).setItemRev(rev).setMail(mail)
							.setItemDifferentiate(status).insertUnique();

				}

				if (attachmentItem != null) {
					success = true;
					msg = "再次新增附件成功,请刷新页面..";
					code = "200";

					//LOGGER.warn("::::::::::: 结束时间: " + endTime);

					//System.out.println("上传100M文件消耗了: " + s + "秒!!!");

					// 如果新增上传附件成功, 开启重新确认
					// 取出该传阅的联系人
					List<Receive> receives = mail.getReceives();
					mail.setHasAttachment(true);//新增附件成功， 设置为true
					//更新
					Services.getMailService().update(mail);

					// 遍历
					for (Receive receive : receives) {
						// 判断,只要是已经确认该传阅的联系人, 就需要重新确认
						if (receive.getIfConfirm() == true) {
							// 设置开启重新确认
							receive.setAfreshConfim(true); // true 为开启重新确认
							// 更新
							Services.getReceiveService().update(receive);
							msg = msg + "  已经开启重新确认!";
						}
					}

					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			msg = "添加文件失败!";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		success = false;
		msg = nameFile + " 文件已存在，请重名后再上传!";
		code = "205";
		json = "null";
		return Results.GLOBAL_FORM_JSON;

	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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
