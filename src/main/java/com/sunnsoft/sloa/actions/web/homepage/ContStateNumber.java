package com.sunnsoft.sloa.actions.web.homepage;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 1. 传阅一览(PC端)--统计传阅数量: 统计当前的传阅(包含已发/收到/待发/待办) 的传阅数量
 *
 * @author chenjian
 *
 */
public class ContStateNumber extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long userId; // 当前用户ID

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	final static Cache<String, String> cache = CacheBuilder.newBuilder()
			// 设置cache的初始大小为10，要合理设置该值
			.initialCapacity(10)
			// 设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
			.concurrencyLevel(20)
			// 设置cache中的数据在访问之后的存活时间为20秒
			.expireAfterAccess(10, TimeUnit.SECONDS)
			// 构建cache实例
			.build();

	@Override
	public String execute() throws Exception {

		this.json = cache.getIfPresent("ContStateNumber" + userId);
//		if (json != null && !json.equals("0")) {
//			msg = "根据条件查询, 对传阅的状态进行统计!";
//			success = true;
//			code = "200";
//			return Results.GLOBAL_FORM_JSON;
//
//		}
		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!");

		List<Mail> mailList = Services.getMailService().createHelper().getUserId().Eq(userId).list();

		// 已发传阅
		// 待发传阅: 统计属于当前用户的传阅, 状态为待发的传阅数量 *
		int sendWaitCount = 0;

		// 传阅中: 统计已发传阅, 状态为传阅中的数量 *
		int sendHalfwayCount = 0;

		// 传阅完成: 统计已发传阅, 状态为已完成的数量 *
		int sendCompleteCount = 0;

		// 已删除传阅数量
		int deleteCount = 0;

		for(Mail mail : mailList){
			// 待发送
			Integer status = mail.getStatus();
			if(status == 1){ // 1 待发传阅  7 已删除
				sendWaitCount++;
				continue;
			}else if(status == 7){
				deleteCount++; 	// 已删除
				continue;
			}

			if(mail.getStepStatus() == 1){ // 1 发阅中  3 已完成
				sendHalfwayCount++; // 传阅中
				continue;
			}else {
				sendCompleteCount++;	// 已完成
				continue;
			}

		}
		// 已发送
		int sendCount = sendHalfwayCount + sendCompleteCount;


		// 已收传阅
		// 已收传阅 -- 未读传阅
		int receiveUnreadCount = 0;
		// 已收传阅 -- 已读传阅
		int receiveReadCount = 0;
		// 已收传阅 -- 传阅中
		int receiveHalfwayCount = 0;
		// 已收传阅 -- 已完成
		int receiveCompleteCount = 0;

		List<Receive> receiveList = Services.getReceiveService().createHelper().getUserId().Eq(userId).list();
		for(Receive receive : receiveList){

			Integer stepStatus = receive.getStepStatus();
			if(stepStatus == 1){ // 1 发阅中 2 待办传阅 3 已完成
				receiveHalfwayCount++;
//					continue;
//				}else if(stepStatus == 2){
//                    receiveAwaitCount++;
			}else if(stepStatus == 3){
				receiveCompleteCount++;
//					continue;
			}

			if(receive.getMailState() == 5){ // 5 未读 6 已读
				receiveUnreadCount++;
				continue;
			}else if(receive.getMailState() == 6){
				receiveReadCount++;
				continue;
			}

		}
//		for(Mail mail : mailList){
//			List<Receive> receives = mail.getReceives();
//
//			for (Receive receive : receives){
//
//				Integer stepStatus = receive.getStepStatus();
//				if(stepStatus == 1){ // 1 发阅中 2 待办传阅 3 已完成
//					receiveHalfwayCount++;
//					continue;
////				}else if(stepStatus == 2){
////                    receiveAwaitCount++;
//				}else if(stepStatus == 3){
//					receiveCompleteCount++;
//					continue;
//				}
//
//				if(receive.getMailState() == 5){ // 5 未读 6 已读
//                    receiveUnreadCount++;
//					continue;
//				}else {
//                    receiveReadCount++;
//					continue;
//				}
//			}
//		}

		// 已收传阅 -- 待办传阅
		int receiveAwaitCount = receiveUnreadCount + receiveReadCount;

		JSONObject jsonObject = new JSONObject();
		// 已发传阅
		jsonObject.put("sendWaitCount", sendWaitCount);         // 已发传阅 -- 待发传阅数量
		jsonObject.put("sendHalfwayCount", sendHalfwayCount); 	// 已发传阅 -- 传阅中数量
		jsonObject.put("sendCompleteCount", sendCompleteCount); // 已发传阅 -- 传阅已完成数量
		jsonObject.put("deleteCount", deleteCount); 	        // 已发传阅 -- 已删除传阅数量
		jsonObject.put("sendCount", sendCount); 		        // 已发传阅 -- 已发送传阅数量

		// 已收传阅
		jsonObject.put("receiveAwaitCount", receiveAwaitCount); 			// 已收传阅 -- 待办传阅数量
		jsonObject.put("receiveUnreadCount", receiveUnreadCount); 			// 已收传阅 -- 未读传阅数量
		jsonObject.put("receiveReadCount", receiveReadCount); 				// 已收传阅 -- 已读传阅数量
		jsonObject.put("receiveHalfwayCount", receiveHalfwayCount); 		// 已收传阅 -- 传阅中数量
		jsonObject.put("receiveCompleteCount", receiveCompleteCount); 		// 已收传阅 -- 已完成传阅数量


		json = jsonObject.toString();
		if (json != null && !json.equals("0")) {
			cache.put("ContStateNumber" + userId, json);
			msg = "根据条件查询, 对传阅的状态进行统计!";
			success = true;
			code = "200";
			return Results.GLOBAL_FORM_JSON;

		} else {
			msg = "根据条件查询失败或是数量为 零...";
			success = false;
			code = "404";
			return Results.GLOBAL_FORM_JSON;
		}
	}
}
