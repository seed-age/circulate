package com.sunnsoft.sloa.actions.web.send;

import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.struts2.Results;

/**
 * 已发传阅(web端)---点击删除: 批量删除传阅(逻辑删除)
 * 
 * @author chenjian
 *
 */
public class DeleteMail extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long userId; // 用户ID
	private long[] mailId; // 传阅ID(因为要批量操作,用数组)

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!!");
		Assert.notNull(mailId, "传阅ID不能为空!!");
		// 进行判断
		if (mailId.length == 0) {
			success = false;
			code = "205";
			msg = "请选择你所要删除的传阅!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		// 遍历数组
		for (int i = 0; i < mailId.length; i++) {
			// 查询数据, 并修改状态
			Mail mail = Services.getMailService().findById(mailId[i]);

			// 判断
			if (mail.getUserId() == userId) {

				// 修改状态
				mail.setStatus(7); // 7 表示已删除
				//设置删除时间
				mail.setDeleteTime(new Date());
				// 更新
				Services.getMailService().update(mail);

				// 同时删除对应的接收人信息
				List<Receive> receives = mail.getReceives();
				for (Receive receive : receives) {
					// 执行删除操作
					Services.getReceiveService().delete(receive);
				}

				success = true;
				msg = "删除成功!";
				code = "200";
			} else {
				success = false;
				msg = "删除失败!";
				code = "500";
			}
		}

		if (success) {
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		} else {
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

	public long[] getMailId() {
		return mailId;
	}

	public void setMailId(long[] mailId) {
		this.mailId = mailId;
	}

}
