package com.sunnsoft.sloa.actions.app.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Discuss;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 收到传阅: 发表评论，点击文本框输入评论内容，点击“发送”按钮，发布评论
 * 
 * @author chenjian
 *
 */
public class InsertDiscuss extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 用户ID
	private Long mailId; // 传阅id
	private String discussContent; // 讨论的内容
	
	@Override
	public String execute() throws Exception {

		// 校验数据
		Assert.notNull(userId, "收件人ID不能为空!");
		Assert.notNull(mailId, "传阅ID不能为空!");
		Assert.notNull(discussContent, "评论内容不能为空!");

		try {
			//查询出对应的传阅记录
			Mail mail = Services.getMailService().findById(mailId);
			
			Receive receive = Services.getReceiveService().createHelper().getMail().Eq(mail).getUserId().Eq(userId).uniqueResult();
			
			// 新增讨论记录
			Discuss discuss = Services.getDiscussService().createHelper().bean().create().setMail(mail).setUserId(userId)
					.setLastName(receive.getLastName()).setLoginId(receive.getLoginId()).setWorkCode(receive.getWorkCode())
					.setDiscussContent(discussContent).setCreateTime(new Date()).setDifferentiate("已收传阅").insertUnique();

			// 进行判断
			if (discuss != null) {
				success = true;
				msg = "发布讨论成功";
				code = "200";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			} else {
				msg = "发布讨论失败...";
				success = false;
				code = "404";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
			msg = "内容错误, 不能发送表情!";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

	public String getDiscussContent() {
		return discussContent;
	}

	public void setDiscussContent(String discussContent) {
		this.discussContent = discussContent;
	}
	
}
