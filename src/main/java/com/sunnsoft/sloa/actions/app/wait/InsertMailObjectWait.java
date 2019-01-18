package com.sunnsoft.sloa.actions.app.wait;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.util.ConstantUtils;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 待发传阅---查看传阅对象: 点击添加联系人, 添加传阅对象
 * 
 * @author chenjian
 *
 */
public class InsertMailObjectWait extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 用户ID
	private Long mailId; // 传阅ID

	/** 该参数接收从前端页面传过来的接收人参数 */
	private Long[] receiveUserId; // 收件人id

	
	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID不能为空");
		Assert.notNull(mailId, "传阅ID不能为空");
		Assert.notNull(receiveUserId, "接收人ID不能为空");

		try {
			// 根据传阅ID查询数据
			Mail mail = Services.getMailService().findById(mailId);

			if (mail == null) {
				success = false;
				msg = "该传阅记录不存在!";
				code = "405";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			// 创建list集合, 用于存储userId.
			List<Long> list = new ArrayList<Long>(receiveUserId.length);
			// 把数组中的数据添加到集合中
			Collections.addAll(list, receiveUserId);

			// 获取接收人的数据
			List<Receive> receivesList = mail.getReceives();

			// 进行迭代
			Iterator<Long> it = list.iterator();
			while (it.hasNext()) {
				Long next = it.next();
				// 遍历接收人集合
				for (Receive receive : receivesList) {
					// 取出接收人userId, 进行判断
					if (next.equals(receive.getUserId()) || next.equals(mail.getUserId())) {
						// 如果相同, 就把这个元素删除
						it.remove();
					}
				}
			}

			int size = list.size();
			if (size == 0) {
				success = true;
				msg = "该联系人已经是传阅对象了!";
				code = "404";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
			// 拼接字符串
			StringBuilder sb = new StringBuilder();

			// 定义一个标记
			boolean res = false;
			// 添加新的收件人
			for (int i = 0; i < list.size(); i++) {

				// 根据ID查询用户信息
				UserMssage userMssage = Services.getUserMssageService().createHelper().getUserId()
						.Eq(list.get(i).intValue()).uniqueResult();

				// 新增收件人记录
				Services.getReceiveService().createHelper().bean().create().setMail(mail).setUserId(userMssage.getUserId())
						.setLastName(userMssage.getLastName()).setLoginId(userMssage.getLoginId()).setWorkCode(userMssage.getWorkCode())
						.setSubcompanyName(userMssage.getFullName()).setDepartmentName(userMssage.getDeptFullname())
						.setReceiveStatus(ConstantUtils.RECEIVE_NOTOPEN_STATUS)
						.setStepStatus(ConstantUtils.RECEIVE_NOTOPEN_STATUS)
						.setMailState(ConstantUtils.RECEIVE_WAIT_STATUS)
						.setReceiveTime(mail.getSendTime()).setJoinTime(mail.getCreateTime())
						.setIfConfirm(false).setReDifferentiate(userId).insertUnique();

				success = true;
				msg = "再次添加联系人成功!";

				sb.append(";").append(userMssage.getLastName());
				res = true;
			}

			if (res) { // true 表示联系人增加成功

				String allReceiveName = mail.getAllReceiveName();
				String allName = sb.toString();
				// 设置
				mail.setAllReceiveName(allReceiveName + allName);
				// 更新
				Services.getMailService().update(mail);

				code = "200";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			msg = "网络繁忙,请稍后再试!";
			code = "4000";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		success = false;
		msg = "添加联系人失败!";
		code = "404";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
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

	public Long[] getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Long[] receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
}
