package com.sunnsoft.sloa.actions.app.wait;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;
/**
 * 待发传阅(APP)---点击编辑：批量删除传阅信息(物理删除)
 * @author chenjian
 *
 */
public class BatchDeleteWait extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long userId; //当前用户ID
	private long[] mailId; //选中的传阅ID
	
	@Override
	public String execute() throws Exception {
		
		//校验参数
		Assert.notNull(userId, "当前用户ID不能为空");
		Assert.notNull(mailId, "选中的传阅ID不能为空");
		//进行判断
		if(mailId.length == 0) {
			success = false;
			code = "205";
			msg = "请选择你所要删除的传阅!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		
		//遍历传阅ID数组
		for (int i = 0; i < mailId.length; i++) {
			
			//开始对选中的传阅ID进行物理删除
			Services.getMailService().deleteById(mailId[i]);
			success = true;
		}
		
		if(success == null) {
			code = "404";
			msg = "删除失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
			
		}
		
		code = "200";
		msg = "删除成功!";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long[] getMailId() {
		return mailId;
	}

	public void setMailId(long[] mailId) {
		this.mailId = mailId;
	}
}
