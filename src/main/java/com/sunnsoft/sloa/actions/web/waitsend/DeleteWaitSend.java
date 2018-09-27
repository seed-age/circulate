package com.sunnsoft.sloa.actions.web.waitsend;

import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.util.struts2.Results;

/**
 * 待发传阅: 删除选择的传阅 (这里的删除是物理删除.) 批量
 * 
 * @author chenjian
 *
 */
public class DeleteWaitSend extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long mailId[];

	@Override
	public String execute() throws Exception {

		// 校验数据
		Assert.notNull(mailId, "传阅ID不能为空!");

		Services.getMailService().deleteByIdArray(mailId);

		success = true;
		msg = "删除成功!";
		code = "200";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
	}
	public Long[] getMailId() {
		return mailId;
	}

	public void setMailId(Long[] mailId) {
		this.mailId = mailId;
	}
}
