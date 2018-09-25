package com.sunnsoft.sloa.actions.web.received;

import java.util.List;

import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.struts2.Results;
/**
 * 已收传阅----删除传阅对象: 单个删除传阅对象
 * @author chenjian
 *
 */
public class DeleteMailObject extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 当前用户ID
	private Long mailId; // 传阅ID
	private Long receiveUserId; // 传阅对象的ID(要删除的传阅对象)
	
	
	@Override
	public String execute() throws Exception {
		
		//校验参数
		Assert.notNull(userId, "用户ID不能为空!");
		Assert.notNull(mailId, "传阅ID不能为空!");
		
		try {
			//根据传阅ID查询
			Mail mail = Services.getMailService().createHelper().getMailId().Eq(mailId).uniqueResult();
			
			//获取到要删除的收件人对象
			Receive receive = Services.getReceiveService().createHelper().getUserId().Eq(receiveUserId).getMail().Eq(mail).uniqueResult();
			
			Long reDifferentiate = receive.getReDifferentiate();
			
			
			//删除传阅对象, 有两种情况 :  一. 是发件人可以删除所有的传阅对象  ,  二是 谁添加该传阅对象  谁才有权限删除
			if(reDifferentiate.equals(userId) || mail.getUserId() == userId) {
				//删除
				Services.getReceiveService().delete(receive);
				
				//删除完传阅对象后, 传阅对应的接收名字也要修改
				List<Receive> receives = mail.getReceives();
				
				//拼接
				StringBuilder sb = new StringBuilder();
				
				for (Receive receivess : receives) {
					String lastName = receivess.getLastName();
					sb.append(lastName).append(";");
				}
				
				String allName = sb.toString().substring(0, sb.toString().length() - 1);
				
				//重新设置
				mail.setAllReceiveName(allName);
				//更新
				Services.getMailService().update(mail);
				
				success = true;
				code = "200";
				msg = "删除传阅对象成功!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}else {
				success = false;
				code = "205";
				msg = "删除传阅对象失败!,你没有权限删除.";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
				
			}
		} catch (Exception e) {
			success = false;
			code = "500";
			msg = "网络繁忙, 请稍后再试!";
			json = "null";
			e.printStackTrace();
			return Results.GLOBAL_FORM_JSON;
		}
		
	}

	public Long getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Long receiveUserId) {
		this.receiveUserId = receiveUserId;
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
	
	
}
