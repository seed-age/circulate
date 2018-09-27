package com.sunnsoft.sloa.actions.app.create;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * 新建传阅--删除附件: 点击删除, 去网盘上查询该附件的信息, 并删除该附件(本地, 网盘上的不做删除).
 * 
 * @author chenjian
 *
 */
public class DeleteUpload extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 用户ID
	private Long itemId; // 附件ID
	private Long mailId; // 附件ID
	
	@Resource
	private Config config;

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!");
		Assert.notNull(itemId, "附件ID不能为空!");
		
		//判断: 如果mailId为空 表示在新建传阅中进行删除上传的附件
		if(mailId == null) {
			//直接根据itemId去数据库中查询, 查询到直接删除
			Services.getAttachmentItemService().deleteById(itemId);
			
			success = true;
			code = "200";
			msg = "删除附件成功!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		
		Assert.notNull(mailId, "传阅ID不能为空!");

		// 设置url
		//LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);

		// 联系网盘(正式环境地址)
		/*String boxUrl = config.getBoxUrl();
		sdk.setBoxUrl(boxUrl);*/

		// 根据传阅ID查询传阅数据
		Mail mail = Services.getMailService().findById(mailId);

		// 根据文件ID查询文件信息
		AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().getItemId().Eq(itemId)
				.uniqueResult();

		// 进行校验, 判断该用户是否用权限删除这个附件
		// 如果该附件是当前用户上传的, 或是 当前用户是发件人. 就进来
		if (attachmentItem.getUserId().equals(userId) || mail.getUserId() == userId.longValue()) {
			// 执行删除
			Services.getAttachmentItemService().delete(attachmentItem);

			List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
			if (attachmentItems.size() == 0) {
				mail.setHasAttachment(false);// 设置
			}
			// 更新
			Services.getMailService().update(mail);
			success = true;
			code = "200";
			msg = "删除附件成功!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		} else {

			success = false;
			code = "205";
			msg = "你没有权限删除该附件!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
					
		/*// 获取附件上传的URL
		String urlPath = attachmentItem.getUrlPath();

		// 获取文件上传到网盘的ID
		//Long neid = attachmentItem.getItemNeid();

		// 获取文件存放的位置
		Integer differentiate = attachmentItem.getItemDifferentiate();

		//UserMssage userMssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId.intValue()).uniqueResult();
		//使用网盘统一账号 system_OA 进行登录(只限于APP端的预览操作)
		String session = null;
		try {
			session = LenovoCloudSDKUtils.getLenovoCloudSDKSession(sdk, config);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("根据LoginId获取session失败: :::::::::" + session);
			//设置正式环境的账号和密码
			String userSlug = config.getUserSlug();
			String password = config.getPassword();
			
			// 调用登录方法, 设置账户和密码
			UserLoginModel login = sdk.login(userSlug, password);
			// 获取session
			session = login.getXLENOVOSESSID();
		}
		
		FileGetIdModel getIdByPath = null;
		
		if (differentiate == 0) { // 0 表示 个人空间

			// 调用查询文件的方法, 查询文件
			getIdByPath = sdk.fileGetIdByPath(urlPath, session, PathType.SELF);
		}
		if (differentiate == 1) { // 1 表示 企业空间

			// 调用查询文件的方法, 查询文件
			getIdByPath = sdk.fileGetIdByPath(urlPath, session, PathType.ENT);
		}

		// 获取文件的ID
		Long fileId = 0L;
		try {
			fileId = getIdByPath.getId();
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			code = "5000";
			msg = "删除附件失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		// 判断附件在本地存储的网盘ID 和 去网盘上查询得到的文件ID 是否相同
		if (attachmentItem.getItemNeid().equals(fileId)) {

			// 进行校验, 判断该用户是否用权限删除这个附件
			// 如果该附件是当前用户上传的, 或是 当前用户是发件人. 就进来
			if (attachmentItem.getUserId().equals(userId) || mail.getUserId() == userId.longValue()) {
				// 执行删除
				Services.getAttachmentItemService().delete(attachmentItem);

				List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
				if (attachmentItems.size() == 0) {
					mail.setHasAttachment(false);// 设置
				}
				// 更新
				Services.getMailService().update(mail);
				success = true;
				code = "200";
				msg = "删除附件成功!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			} else {

				success = false;
				code = "205";
				msg = "你没有权限删除该附件!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

		} else {
			success = false;
			code = "4000";
			msg = "删除附件失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}*/
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

}
