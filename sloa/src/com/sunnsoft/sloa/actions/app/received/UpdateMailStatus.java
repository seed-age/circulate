package com.sunnsoft.sloa.actions.app.received;

import java.util.List;

import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.struts2.Results;
/**
 * 已收传阅--点击传阅: 修改该传阅的状态!
 * @author chenjian
 *
 */
public class UpdateMailStatus extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long userId; //用户ID
	private long mailId; //传阅ID
	
	@Override
	public String execute() throws Exception {
		
		// 校验参数
		Assert.notNull(userId, "用户ID,不能为空");
		Assert.notNull(mailId, "传阅Id,不能为空");
		
		//根据传阅ID查询传阅信息
		Mail mail = Services.getMailService().createHelper().getMailId().Eq(mailId).uniqueResult();
		
		//获取收件人信息
		List<Receive> receives = mail.getReceives();
		
		//遍历取出数据
		for (Receive receive : receives) {
			//判断该传阅是否属于该用户
			if(receive.getUserId() == userId) {
				//如果属于, 继续判断该收件人的这条传阅是否是未读
				if(receive.getMailState() == 5) {
					//如果是未读, 就进来
					//设置该收件人的传阅状态
					receive.setMailState(6); // 6 表示已读
					//更新
					Services.getReceiveService().update(receive);
					
					success = true;
					msg = "更新状态成功!";
					code = "200";
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}else {
					success = true;
					msg = "该传阅状态已经是已读!";
					code = "201";
					json = "null";
				}
			}
		}
		
		return Results.GLOBAL_FORM_JSON;
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
	
}
