package com.sunnsoft.sloa.actions.web.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.util.ConstantUtils;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 已发/收到传阅---传阅详情---点击删除传阅对象: 批量删除传阅人
 *
 * @author chenjian
 *
 */
public class BatchDeleteReceived extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private Long userId; // 当前用户ID
	private Long mailId; // 传阅ID
	private Long[] receiveUserId; // 传阅对象的ID(要删除的传阅对象)

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!");
		Assert.notNull(mailId, "传阅ID不能为空!");

		// 定义一个标记, 标识是否删除传阅对象成功
		int count = 0;

		// 定义一个删除传阅对象的标识
		int deleteCount = 0;

		// 根据传阅ID查询
		Mail mail = Services.getMailService().createHelper().getMailId().Eq(mailId).uniqueResult();
		List<Receive> ress = mail.getReceives();
		if (ress .size() == 1) {
			success = false;
			code = "404";
			msg = "传阅对象只有一个用户时, 不允许删除";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		Receive lastReceive = null;
		if (receiveUserId.length == ress.size() && receiveUserId.length > 1) {
			lastReceive = ress.get(ress.size() - 1);
		}

		try {

			List<Long> delIds = new ArrayList<>();

			// 遍历收件人数组
			for (Long l : receiveUserId) {

				// 根据收件人ID获取到要删除的收件人对象
				Receive receive = Services.getReceiveService().createHelper().getUserId().Eq(l).getMail().Eq(mail)
						.uniqueResult();

				// 获取要删除的传阅是谁添加的
				Long reDifferentiate = receive.getReDifferentiate();

				if (lastReceive != null) {
					if (lastReceive.getUserId() == receive.getUserId()){
						continue;
					}
				}

				// 判断 删除传阅对象, 有两种情况 : 一. 是发件人可以删除所有的传阅对象 , 二是 谁添加该传阅对象 谁才有权限删除
				if (reDifferentiate.equals(userId) || mail.getUserId() == userId) {
					delIds.add(receive.getUserId());
					// 执行删除
					Services.getReceiveService().delete(receive);
					// 删除成功 + 1
					count++;
				} else {
					// 如果删除失败, 就提示你没有权限删除传阅对象
					msg = receive.getLastName();
					deleteCount++;
				}

			}

			// 如果删除成功, count的值是大于0的
			if (count > 0) {
				// 拼接
				StringBuilder sb = new StringBuilder();

				// 删除完传阅对象后, 传阅对应的接收名字也要修改
				// 根据传阅ID, 获取最新的接收人信息
				Mail newMail = Services.getMailService().findById(mailId);
				// 获取最新的接收人数据
				List<Receive> receives = newMail.getReceives();
				// 遍历
				boolean flag = false;
				for (Receive receive : receives) {
					long userId = receive.getUserId();

					// 如果当前ID 和 被删除的ID相同, 则跳过此次循环
					for (Long id : delIds) {
						if (userId == id) {
							flag = true;
							continue;
						}
					}

					if (flag) {
						flag = false;
						continue;
					}
					// 取出接收人姓名
					String lastName = receive.getLastName();
					// 进行拼接
					sb.append(lastName).append(";");
				}

				// 去除最后面的 ; 号
				String allName = sb.toString().substring(0, sb.toString().length() - 1);

				// 重新设置
				newMail.setAllReceiveName(allName);
				// 更新
				Services.getMailService().update(newMail);

				// 如果当前传阅在删除传阅对象后, 只剩下一个传阅对象并且还是已确认的. 那么这封传阅则要变成已完成的.
				// 查询当前传阅最新的数据
				Mail mail1 = Services.getMailService().createHelper().getMailId().Eq(mailId).uniqueResult();
				List<Receive> receiveList = mail1.getReceives();
				List<Long> updateReceiveIdList = new ArrayList<>();
				int sum = 0; // 统计收件人,确认传阅的数量
				for (Receive receive : receiveList){

					if(receive.getIfConfirm() && !receive.getAfreshConfim()){
						sum++;
						updateReceiveIdList.add(receive.getUserId());
						continue;
					}

					// 如果当前ID 和 被删除的ID相同
					for (Long id : delIds) {
						if (receive.getUserId() == id) {
							sum++;
						}
					}

				}

				// 如果确认数量相等于, 则改变传阅的流程状态
				if (sum == receiveList.size()) {
					Services.getReceiveService().createHelper().getUserId().In(updateReceiveIdList)
							.bean().setStepStatus(ConstantUtils.MAIL_COMPLETE_STATUS).update();

					mail1.setStepStatus(ConstantUtils.MAIL_COMPLETE_STATUS);
					Services.getMailService().update(mail1);
				}
//				if(receives1.size() == 1){
//					Receive receive1 = receives1.get(0);
//					if(receive1.getIfConfirm() && !receive1.getAfreshConfim()){ // 如果已确认, 则改变传阅状态
//						receive1.setStepStatus(3);
//						Services.getReceiveService().update(receive1);
//						mail1.setStepStatus(3);
//						Services.getMailService().update(mail1);
//					}
//				}

				success = true;
				code = "200";
				msg = "删除传阅对象成功!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			// 如果删除传阅对象的标识 大于0
			if (deleteCount > 0) {
				success = true;
				code = "205";
				msg = "删除传阅对象成功! 其中 " + msg + " 传阅对象你没有权限删除!";
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

		success = false;
		code = "404";
		msg = "删除传阅对象失败!";
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
