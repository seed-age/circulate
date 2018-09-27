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
			.expireAfterAccess(30, TimeUnit.SECONDS)
			// 构建cache实例
			.build();

	@Override
	public String execute() throws Exception {
		
		this.json = cache.getIfPresent("ContStateNumber" + userId);
		if (json != null && !json.equals("0")) {
			msg = "根据条件查询, 对传阅的状态进行统计!";
			success = true;
			code = "200";
			return Results.GLOBAL_FORM_JSON;

		}
		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!");

		// 传阅中: 统计收到传阅, 状态为传阅中的数量 *
		int receiveInCount = 0;
		// 传阅完成: 统计收到传阅, 状态为已完成的数量 *
		int ceceiveCompleteCount = 0;

		// 收到传阅
		//收到传阅: 统计收到传阅, 状态为待办的传阅数量 *
		long todoCount = Services.getReceiveService().createHelper().getUserId().Eq(userId).getStepStatus().Eq(2).rowCount();
		//收到传阅: 统计收到传阅, 状态为未读的传阅数量 *
		long unreadCount = Services.getReceiveService().createHelper().getUserId().Eq(userId).getMailState().Eq(5).rowCount();
		// 获取当前用户,除开 流程 为 2 的所有已收数据
		List<Receive> list = Services.getReceiveService().createHelper().getUserId().Eq(userId).getStepStatus().Ne(2)
				.list();
		// 遍历
		for (Receive receive : list) {
			// 取出流程状态
			Integer stepStatus = receive.getStepStatus();
			switch (stepStatus) {
			case 0:
				continue;
			case 1:// 传阅中
				receiveInCount++;
				continue;
			case 3: // 已完成
				ceceiveCompleteCount++;
				continue;
			}

		}

				
		// 已发传阅---> 1. 传阅中 2. 已完成 下面都实现了
		// 已删除传阅数量
		int deleteCount = 0;
		// 待发传阅: 统计属于当前用户的传阅, 状态为待发的传阅数量 *
		int waitSendCount = 0;

		//已发传阅: 统计已发传阅中状态为 传阅中 的数量 * 
		long sendInCount = Services.getMailService().createHelper().getUserId().Eq(userId).getStepStatus().Eq(1).getStatus().Eq(0).rowCount();
		//已发传阅: 统计已发传阅中状态为 已完成 的数量 * 
		long completeCount = Services.getMailService().createHelper().getUserId().Eq(userId).getStepStatus().Eq(3).getStatus().Eq(0).rowCount();
		// 获取当前用户,除开 流程 为 3 的所有已发数据
		List<Mail> listMail = Services.getMailService().createHelper().getUserId().Eq(userId).startOr().getStatus().Eq(1).getStatus().Eq(7).list();

		// 遍历
		for (Mail mail : listMail) {
			// 获取已发传阅的状态
			Integer status = mail.getStatus();
			if (status == 1) { // 待发传阅
				waitSendCount++;
				continue;
			}
			if (status == 7) { // 已删除
				deleteCount++;
				continue;
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("todoCount", todoCount); // 统计收到传阅, 状态为待办的传阅数量
		map.put("unreadCount", unreadCount); // 统计收到传阅, 状态为未读的传阅数量 *
		map.put("deleteCount", deleteCount);
		map.put("ceceiveCompleteCount", ceceiveCompleteCount);
		map.put("completeCount", completeCount);
		map.put("receiveInCount", receiveInCount);
		map.put("sendInCount", sendInCount);
		map.put("waitSendCount", waitSendCount);

		json = JSONObject.toJSONString(map);
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
