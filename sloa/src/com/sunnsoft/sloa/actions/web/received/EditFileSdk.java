package com.sunnsoft.sloa.actions.web.received;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.EditFileModel;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * (PC端)传阅详情页面---点击修改附件按钮: 在线修改传阅附件(网盘)
 * 
 * @author chenjian
 *
 */
public class EditFileSdk extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(EditFileSdk.class);
	
	private Long itemId; // 附件ID
	private Long userId; // 用户ID
	
	@Resource
	private Config config;
	
	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(itemId, "附件ID不能为空!");

		try {
			// 获取本地附件信息
			AttachmentItem item = Services.getAttachmentItemService().findById(itemId);
			// 获取当前文件的格式
			String fileCategory = item.getFileCategory();

			String[] edit = { "doc", "docx", "docm", "dotm", "xls", "xlsx", "xlsm", "xlt", "ppt", "pptx", "pps", "pot",
					"ppa" };
			
			UserMssage mssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId.intValue()).uniqueResult();
			
			boolean editStatus = false;
			// 遍历
			for (String name : edit) {
				// 校验
				if (fileCategory.equalsIgnoreCase(name)) {
					editStatus = true;
					break;
				}
			}

			if (editStatus) {

				// 格式
				String[] ifUpdateFlase = { "doc", "docx", "docm", "dotm", "xls", "xlsx", "xlsm", "xlt" };

				// 获取传阅信息
				Mail mail = item.getMail();
				// 获取该传阅是否允许修改 word文档 和 excel
				Boolean ifUpdate = mail.getIfUpdate();

				// 判断
				if (!ifUpdate) {
					// 遍历
					for (String type : ifUpdateFlase) {

						if (type.equalsIgnoreCase(fileCategory)) {
							success = false;
							code = "404";
							msg = "该传阅不允许修订Word、Excel附件!";
							json = "null";
							return Results.GLOBAL_FORM_JSON;
						}
					}
				}

				LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
				
				//使用网盘统一账号 system_OA 进行登录(只限于web端)
				//String session = LenovoCloudSDKUtils.getLenovoCloudSDKSession(sdk, config);
				//根据业务需求, web端在线编辑的时候使用个人账号进行编辑(在实地的  测试环境和生产环境使用)
				String loginId = LenovoCloudSDKUtils.getLoginId();
				LOGGER.warn("::::::::: 使用个人账号: " + loginId);
				String session = LenovoCloudSDKUtils.getSession(mssage);
				LOGGER.warn("::::::::: 使用个人账号的session: " + session);
				
				// 拼接url
				//String path = config.getBoxUrl() + item.getUrlPath();
				String path = item.getUrlPath();
				//LOGGER.warn("::::::::: 在线编辑的路径: " + path);
				// 获取附件在网盘的ID
				Long neid = item.getItemNeid();
				// 获取附件在网盘的版本
				String rev = item.getItemRev();

				// 调用接口中的编辑文件的方法
				Integer differentiate = item.getItemDifferentiate();
				EditFileModel editFile = null;
				if (differentiate == 0) { // 0 表示 个人空间
					editFile = sdk.editFile(path, neid, rev, session, PathType.SELF);

				}
				if (differentiate == 1) { // 1 表示 企业空间
					editFile = sdk.editFile(path, neid, rev, session, PathType.ENT);
				}

				Map<String, Object> map = new HashMap<String, Object>();

				// 获取编辑地址
				String editUrl = editFile.getEditUrl();
				map.put("editUrl", editUrl);
				//LOGGER.warn("::::::::: editFile:  " + editFile);
				LOGGER.warn("::::::::: editUrl返回的编辑链接:  " + editUrl);
				if (editFile != null) {
					// 判断
					if (editUrl != null && !editUrl.equals("")) {
						// 设置
						item.setState(1); // 1 表示修改
						// 更新状态
						Services.getAttachmentItemService().update(item);
					}
					success = true;
					code = "200";
					msg = "在线编辑附件!";
					json = JSONObject.toJSONString(map);
					return Results.GLOBAL_FORM_JSON;
				}

			} else {

				success = false;
				code = "205";
				msg = "该文件类型不支持编辑!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			success = false;
			code = "404";
			msg = "在线编辑附件失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			code = "500";
			msg = "在线编辑附件失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
