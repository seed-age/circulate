package com.sunnsoft.sloa.actions.app.index;

import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 传阅首页(APP端): 统计数量 (当前用户所有的传阅, 不管是已发或者是收到)
 * 
 * @author chenjian
 *
 */
public class CountMailNumber extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 收件人ID

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String execute() throws Exception {

		// 参数校验
		Assert.notNull(userId, "发件人ID不能为空!");
		
		// 统计收到传阅的 , 待办传阅数量.
		long todoCount = 0;
		// 传阅中: 统计收到传阅, 状态为传阅中的数量
		long receiveInCount = 0;	
		// 传阅完成: 统计收到传阅, 状态为已完成的数量
		long receiveCompleteCount = 0;
		// 收到传阅: 统计收到传阅, 状态为未读的传阅数量 *
		long unreadCount = 0;
		/*		List<Receive> list = Services.getReceiveService().createHelper().getUserId().Eq(userId).getIfConfirm().Eq(false)
				.getStepStatus().Ne(0).list(); */		
		//查询
		List<Receive> list = Services.getReceiveService().createHelper().getUserId().Eq(userId)
				.getStepStatus().Ne(0).list();
		//遍历
		for (Receive receive : list) {
			//获取流程状态
			Integer stepStatus = receive.getStepStatus();
			//获取传阅状态
			Integer mailState = receive.getMailState();
			//判断
			if(mailState == 5) {
				unreadCount++;
			}
			//判断
			if(stepStatus == 1) {
				receiveInCount++;
				continue; 
			}else if(stepStatus == 2) {
				todoCount++;
				continue;
			}else if(stepStatus == 3) {
				receiveCompleteCount++;
				continue;
			}
		}
		
		// 收到传阅数等于待办传阅+传阅中+已完成
		int count = list.size();

		// 统计已发传阅的数量(所有已发的传阅)
		long sendCount = 0;
		// 已发传阅: 统计已发传阅中状态为 传阅中 的数量
		long sendInCount = 0;
		// 已发传阅: 统计已发传阅中状态为 已完成 的数量
		long completeCount = 0;
		//查询
		List<Mail> mailList = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).startOr()
				.getStepStatus().Eq(1).getStepStatus().Eq(3).stopOr().list();
		
		//遍历
		for (Mail mail : mailList) {
			//获取流程状态
			Integer stepStatus = mail.getStepStatus();
			//判断
			if(stepStatus == 1) {
				sendInCount++;
				continue;
			}else if(stepStatus == 3) {
				completeCount++;
				continue;
			}
		}
		
		// 所有已发的传阅的数量
		sendCount = mailList.size();
		
		// 已删除传阅数量
		long deleteCount = 0;
		// 待发传阅: 统计属于当前用户的传阅, 状态为待发的传阅数量
		long waitSendCount = 0;
		//查询
		List<Mail> lists = Services.getMailService().createHelper().getUserId().Eq(userId).startOr().getStatus().Eq(1).getStatus().Eq(7).stopOr().list();
		//遍历
		for (Mail mail : lists) {
			//获取传阅状态
			Integer status = mail.getStatus();
			//判断
			if(status == 1) {
				waitSendCount++;
				continue;
			}else if(status == 7) {
				deleteCount++;
				continue;
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deleteCount", deleteCount);
		map.put("count", count);
		map.put("sendCount", sendCount);
		map.put("todoCount", todoCount);
		map.put("waitSendCount", waitSendCount);
		map.put("receiveInCount", receiveInCount);
		map.put("sendInCount", sendInCount);
		map.put("completeCount", completeCount);
		map.put("receiveCompleteCount", receiveCompleteCount);
		map.put("unreadCount", unreadCount);

		json = JSONObject.toJSONString(map);

		if (json != null && !json.equals("0")) {
			success = true;
			msg = "根据条件查询, 对传阅的状态进行统计!";
			code = "200";
			return Results.GLOBAL_FORM_JSON;

		} else {
			success = false;
			msg = "根据条件查询失败或是数量为 零...";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

	}
}
