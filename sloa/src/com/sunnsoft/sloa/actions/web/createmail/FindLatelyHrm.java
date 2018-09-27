package com.sunnsoft.sloa.actions.web.createmail;

import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.util.DateUtils;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 新建传阅--添加联系--点击最近: 查询出该用户在新建传阅中添加联系人时, 七天以内添加联系人的数据.
 * 
 * @author chenjian
 *
 */
public class FindLatelyHrm extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 当前用户ID

	@Override
	public String execute() throws Exception {

		// 参数校验
		Assert.notNull(userId, "用户ID不能为空!");

		// 创建list集合, 存储接收人ID
		List<Object> list = new ArrayList<Object>();

		// 获取当前时间
		Date newDate = new Date();

		Date dateToDay = DateUtils.dateToDay(newDate, 7);
		// 1. 根据用户ID查询该用户七天以内发送的传阅记录, 条件: 已当前时间为条件
		List<Mail> mailList = Services.getMailService().createHelper().getUserId().Eq(userId).getSendTime().Le(dateToDay)
				.list();

		// 进行遍历
		for (Mail mail : mailList) {
			// 取出对应的接收人信息
			List<Receive> receives = mail.getReceives();
			// 遍历
			for (Receive receive : receives) {
				// 获取联系人ID
				long userIdReceive = receive.getUserId();
				// 添加到集合
				list.add(userIdReceive);
			}
		}

		// 创建set集合, 用于去重
		Set<Object> set = new HashSet<Object>();
		// 把需要去重的集合添加进去
		set.addAll(list);
		// 创建新的集合, 用于存储去重后的数据
		List<Object> newList = new ArrayList<Object>();
		// 添加去重后的数据
		newList.addAll(set);

		// 存储联系人对象
		List<Object> listHrm = new ArrayList<>();

		// 遍历存储联系人数据的list集合
		for (int i = 0; i < newList.size(); i++) {
			long s = (long) newList.get(i);
			// 根据联系人ID查询联系人数据
			UserMssage mssage = Services.getUserMssageService().createHelper().getUserId().Eq((int) s).uniqueResult();
			listHrm.add(mssage);
		}

		//System.out.println("去重后的数量: " + newList.size());

		json = JSONObject.toJSONString(listHrm);

		success = true;
		code = "200";
		msg = "获取最近联系人成功!";
		return Results.GLOBAL_FORM_JSON;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
