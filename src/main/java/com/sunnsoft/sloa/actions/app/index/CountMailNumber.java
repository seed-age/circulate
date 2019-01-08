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
		int todoCount = 0;
		// 传阅中: 统计收到传阅, 状态为传阅中的数量
		int receiveInCount = 0;
		// 传阅完成: 统计收到传阅, 状态为已完成的数量
		int receiveCompleteCount = 0;
		// 收到传阅: 统计收到传阅, 状态为未读的传阅数量 *
		int unreadCount = 0;
		// 已收传阅 -- 已读传阅
		int receiveReadCount = 0;

		//查询
		List<Receive> receiveList = Services.getReceiveService().createHelper().getUserId().Eq(userId).list();

		//遍历
		for (Receive receive : receiveList) {

			int stepStatus = receive.getStepStatus();
			if(stepStatus == 1){ // 1 发阅中 2 待办传阅 3 已完成
				receiveInCount++;
			}else if(stepStatus == 2){
				todoCount++;
			}else if(stepStatus == 3){
				receiveCompleteCount++;
			}

			int mailState = receive.getMailState();
			if(mailState == 5){ // 5 未读 6 已读
				unreadCount++;
				continue;
			} else if(mailState == 6){
				receiveReadCount++;
				continue;
			}
		}

		// 收到传阅数等于待办传阅+传阅中+已完成
		int count = unreadCount + receiveReadCount;

		// ===================== 已发传阅 ================================

		// 统计已发传阅的数量(所有已发的传阅)
		int sendCount = 0;
		// 已发传阅: 统计已发传阅中状态为 传阅中 的数量
		int sendInCount = 0;
		// 已发传阅: 统计已发传阅中状态为 已完成 的数量
		int completeCount = 0;
		// 已删除传阅数量
		int deleteCount = 0;
		// 待发传阅: 统计属于当前用户的传阅, 状态为待发的传阅数量
		int waitSendCount = 0;

		//查询
		List<Mail> mailList = Services.getMailService().createHelper().getUserId().Eq(userId).list();

		for(Mail mail : mailList){
			// 待发送
			Integer status = mail.getStatus();
			if(status == 1){ // 1 待发传阅  7 已删除
				waitSendCount++;
				continue;
			}else if(status == 7){
				deleteCount++; 	// 已删除
				continue;
			}

			if(mail.getStepStatus() == 1){ // 1 发阅中  3 已完成
				sendInCount++; // 传阅中
				continue;
			}else {
				completeCount++;	// 已完成
				continue;
			}

		}

		// 所有已发的传阅的数量
		sendCount = sendInCount + completeCount;


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
