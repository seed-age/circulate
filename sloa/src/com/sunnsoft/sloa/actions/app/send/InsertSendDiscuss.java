package com.sunnsoft.sloa.actions.app.send;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 已发传阅(APP端): 评论传阅 发表评论，点击文本框输入评论内容，点击“发送”按钮，发布评论
 * 
 * @author chenjian
 *
 */
public class InsertSendDiscuss extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long userId; // 用户ID
	private long mailId; // 传阅id
	private String discussContent; // 讨论的内容

	@Override
	public String execute() throws Exception {

			// 校验数据
			Assert.notNull(userId, "发件人ID不能为空!");
			Assert.notNull(discussContent, "评论内容不能为空!");
			try {

			// 查询出对应的传阅记录
			Mail mail = Services.getMailService().createHelper().getUserId().Eq(userId).getMailId().Eq(mailId).uniqueResult();

			// 新增讨论记录
			Services.getDiscussService().createHelper().bean().create().setMail(mail).setCreateTime(new Date())
					.setDiscussContent(discussContent).setUserId(mail.getUserId()).setLastName(mail.getLastName())
					.setWorkCode(mail.getWorkCode()).setLoginId(mail.getLoginId()).setDifferentiate("已发传阅").insertUnique();

			success = true;
			msg = "发布讨论成功";
			code = "200";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
	}

	public String getDiscussContent() {
		return discussContent;
	}

	public void setDiscussContent(String discussContent) {
		this.discussContent = discussContent;
	}

}
