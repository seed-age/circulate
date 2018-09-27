package com.sunnsoft.sloa.actions.web.createmail;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.lenovo.css.lenovocloud.sdk.model.PreviewFileModel;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 新建传阅/已发传阅: 点击附件可对附件进行预览操作 b、预览：点击预览，新页面打开预览附件文件 (只实现了预览功能, 也只能打开word文档)
 * 
 * @author chenjian
 *
 */
public class PreviewFile extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(PreviewFile.class);
	
	private String bulkId; // 附件上传批次ID
	private long itemId; // 本地数据库的附件ID

	@Resource
	private Config config;
	
	@Override
	public String execute() throws Exception {

		long statrTime = System.currentTimeMillis();
		LOGGER.warn("开始调用传阅接口时间: " + statrTime);
		
		// 校验参数
		Assert.notNull(itemId, "附件ID不能为空!");
		Assert.notNull(bulkId, "附件的批次ID不能为空!");
		
		// 去数据库中查询附件数据
		AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().getBulkId().Eq(bulkId)
				.getItemId().Eq(itemId).uniqueResult();

		//doc, docx, xls, xlsx, ppt, pptx, pdf, txt, dwg, dps, wps, et
		String[] type = {"zip", "rar", "txt", "exe", "apk", "ISO", "msi", "mp4", "avi", "mpeg", "dat"};
		
		for (String typeName : type) {
			// 进行判断
			if (attachmentItem.getFileCategory().equalsIgnoreCase(typeName)) {
				success = false;
				code = "205";
				msg = "该文件类型不支持预览!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		}


		try {
			
			long boxStatrTime = System.currentTimeMillis();
			
			LOGGER.warn("调用网盘预览接口开始时间: " + boxStatrTime);
			
			// 1. 设置url(测试地址)
			LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);

			//使用网盘统一账号 system_OA 进行登录(只限于web端)
			String session = LenovoCloudSDKUtils.getLenovoCloudSDKSession(sdk, config);
			
			// 拼接url
			String path = attachmentItem.getUrlPath();

			// 判断
			Integer differentiate = attachmentItem.getItemDifferentiate();

			// 4. 调用预览文件的方法
			PreviewFileModel previewFile = null;
			if (differentiate == 0) { // 0 表示 个人空间
				previewFile = sdk.previewFile(path, attachmentItem.getItemNeid(), attachmentItem.getItemRev(), session,
						PathType.SELF);
			}

			if (differentiate == 1) { // 1 表示 企业空间
				previewFile = sdk.previewFile(path, attachmentItem.getItemNeid(), attachmentItem.getItemRev(), session,
						PathType.ENT);
			}

			String previewUrl = previewFile.getPreviewUrl();
			
			long boxEndTime = System.currentTimeMillis();
			LOGGER.warn("调用网盘预览接口结束时间: " + boxEndTime);
			LOGGER.warn("调用网盘预览接口消耗时间: " + (boxEndTime - boxStatrTime));
			
			LOGGER.warn("web端  -->  预览路径: " + previewUrl);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("url", previewUrl);
			json = JSONObject.toJSONString(map);
		} catch (Exception e) {
			success = false;
			code = "404";
			msg = "预览附件失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		if (json != null) {
			
			
			success = true;
			code = "200";
			msg = "获取附件预览url成功!";
			
			long endTime = System.currentTimeMillis();
			LOGGER.warn("调用传阅系统接口结束时间: " + endTime);
			LOGGER.warn("调用传阅系统接口消耗时间: " + (endTime - statrTime));
			
			return Results.GLOBAL_FORM_JSON;

		} else {
			success = false;
			code = "404";
			msg = "获取附件预览url失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
	}

	public String getBulkId() {
		return bulkId;
	}

	public void setBulkId(String bulkId) {
		this.bulkId = bulkId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
}
