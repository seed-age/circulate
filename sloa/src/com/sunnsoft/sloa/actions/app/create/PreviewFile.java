package com.sunnsoft.sloa.actions.app.create;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.Assert;

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

/**
 * 新建传阅/已发传阅: 点击附件可对附件进行预览操作
	  b、预览：点击预览，新页面打开预览附件文件  (只实现了预览功能, 也只能打开word文档)
 *  
 * @author chenjian
 *
 */
public class PreviewFile extends BaseParameter {

	private static final long serialVersionUID = 1L;

	//private Integer userId; //用户ID
	private String bulkId;  // 附件上传批次ID
	private long itemId;	// 本地数据库的附件ID
	@Resource
	private Config config;
	
	@Override
	public String execute() throws Exception {
		// 校验参数
		Assert.notNull(itemId, "附件ID不能为空!");
		Assert.notNull(bulkId, "附件的批次ID不能为空!");
		
		// 2. 设置url(测试地址)
		LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
		//根据SSO登录获取当前用户的session
		String session = LenovoCloudSDKUtils.getLenovoCloudSDKSession(sdk, config);
		
		//去数据库中查询附件数据
		AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().getBulkId().Eq(bulkId).getItemId().Eq(itemId).uniqueResult();
		
		String[] type = {"zip","rar","txt","gif","webp","apng","dwg","dxf","dwt","dws"};
		for (String typeName : type) {
			if(attachmentItem.getFileCategory().equalsIgnoreCase(typeName)) {
				success = false;
				code = "205";
				msg = "该文件类型不支持预览!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		}

		//拼接url
		String path = config.getBoxUrl() + attachmentItem.getUrlPath();
		
		//判断
		Integer differentiate = attachmentItem.getItemDifferentiate();
		
		//4. 调用预览文件的方法
		PreviewFileModel previewFile = null;
		if(differentiate == 0) { //0 表示 个人空间
			 previewFile = sdk.previewFile(path, attachmentItem.getItemNeid(), attachmentItem.getItemRev(), session, PathType.SELF);
		}
		
		if(differentiate == 1) { //1 表示 企业空间
			previewFile = sdk.previewFile(path, attachmentItem.getItemNeid(), attachmentItem.getItemRev(), session, PathType.ENT);
		}
		
		
		String previewUrl = previewFile.getPreviewUrl();
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", previewUrl);
		json = JSONObject.toJSONString(map);
		
		if(json != null) {
			success = true;
			code = "200";
			msg = "获取附件预览url成功!";
			return Results.GLOBAL_FORM_JSON;
			
		}else {
			success = false;
			code = "404";
			msg = "预览失败!";
			json ="null";
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
