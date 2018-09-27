package com.sunnsoft.sloa.actions.app.received;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.util.HrmMessagePushUtils;
import com.sunnsoft.sloa.util.mail.MessageUtils;
import com.sunnsoft.util.struts2.Results;

/**
 * 已收传阅--在传阅详情页面中-->点击确认: 确认该传阅 .和 重新确认
 * 
 * @author chenjian
 *
 */
public class InsertConfirm extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long mailId; // 前端页面传递过来的传阅ID
	private long userId; // 前端页面传递过来的收件人ID(也就是当前用户)
	private String remark; // 用户的确认信息;

	private boolean statusConfirm; // true 表示 确认传阅 false 表示 重新确认该传阅

	@Override
	public String execute() throws Exception {
		// 校验数据
		Assert.notNull(mailId, "传阅ID不能为空");
		Assert.notNull(userId, "收件人ID(也是当前用户ID)不能为空");
		Assert.notNull(remark, "确认信息不能为空");

		// 通过传阅ID去查找收件人
		Mail mail = Services.getMailService().findById(mailId);

		// 获取传阅对应的收件人数据
		List<Receive> receives = mail.getReceives();

		if (statusConfirm) { // true 表示第一次确认

			// 定义一个标记, 统计有多少个接收人确认了传阅
			int count = 0;
			// 推送消息
			String lastName = "";
			// 拼接id
			String ids = "";
			String userIds = "";
			// 遍历收件人集合
			for (Receive receive : receives) {
				// 一旦该传阅的全部接收人对该传阅进行了确认, 那么该传阅的流程状态是 3 (已完成)
				// 进行判断, 如果其余的接收人 都已经确认传阅的话, 就进来
				if (receive.getIfConfirm() == true) {
					// 再次判断, 如果 确认/标识 为 null, 证明传阅没有确认
					if (receive.getConfirmRecord() != null) {
						count++; // 表示已经确认传阅的接收人 +1
					}
				}

				// 通过userId(页面传过来的当前用户ID)和查找到的收件人ID进行比较, 如果相等 和 是否确认为 false,就修改该收件人数据
				if (receive.getUserId() == userId && receive.getIfConfirm() == false) {
					
					lastName = receive.getLastName();
					// 如果相等, 证明就是该用户的收到传阅的记录. 进行修改收到传阅记录
					// 修改收件人的传阅状态, 进行修改该传阅的状态 修改为 6 (表示已读) 以及进行确认传阅
					receive.setReceiveStatus(1); // 1 表示 已开封
					receive.setOpenTime(new Date()); // 记录打开传阅的时间
					receive.setMailState(6); // 收件人的传阅筛选状态 6 表示 已读
					receive.setIfConfirm(true); // true 表示 已确认该传阅
					receive.setAffirmTime(new Date()); // 设置确认时间
					receive.setRemark(remark); // 确认时的 确认信息
					// 设置确认/标识
					receive.setConfirmRecord(
							receive.getRemark() + "  确认(" + dateToString(receive.getAffirmTime()) + ")");
					receive.setStepStatus(1); // 收件人这边的传阅流程状态 1 表示传阅中, 就不再是待办状态了 .

					// 更新数据
					Services.getReceiveService().update(receive);
					code = "200";
					count++; // 当前用户也已经确认该传阅 , 所有要 + 1
				}else {
					// 拼接
					ids += receive.getLoginId() + ",";
					userIds += receive.getUserId() + ",";
				}
			}

			// 再判断, 如果count相等于收件人集合的数量那么就是该传阅已经全部确认了
			if (count == receives.size()) {
				// 如果是true, 那么就修改该传阅的流程状态
				mail.setStepStatus(3); // 表示 该传阅已完成
				Services.getMailService().update(mail);

				// 那么, 收件人这边也要修改对应的流程状态, 修改为 已完成
				// 根据传阅ID获取传阅信息.
				Mail newMail = Services.getMailService().findById(mailId);
				// 获取最新的接收人信息
				List<Receive> newReceives = newMail.getReceives();
				// 遍历
				for (Receive receive : newReceives) {
					// 修改
					receive.setStepStatus(3); // 修改为 已完成
					// 更新
					Services.getReceiveService().update(receive);
				}

				msg = " 该传阅已完成";
			} else {
				msg = "该传阅还有其他收件人没有确认!";
			}

			// 消息推送
			getPush(mail, lastName, ids, userIds);

			success = true;
			msg = "确认传阅成功! " + msg;
			json = "null";
			return Results.GLOBAL_FORM_JSON;

		} else { // false 表示 第二次确认
			// 推送消息
			String lastName = "";
			// 拼接id
			String ids = "";
			String userIds = "";
			// 遍历收件人集合
			for (Receive receive : receives) {
				
				// 通过userId(页面传过来的当前用户ID)和查找到的收件人ID进行比较, 如果相等 和 是否开启重新确认为 true,就修改该收件人数据
				if (receive.getUserId() == userId && receive.getAfreshConfim() == true) {
					lastName = receive.getLastName();
					// 如果相等, 证明就是该用户的收到传阅的记录. 进行修改收到传阅记录
					receive.setAfreshConfim(false);
					receive.setMhTime(new Date()); // 设置重新确认时间
					receive.setAfreshRemark(remark); // 确认时的 确认信息(重新)
					// 设置确认/标识
					receive.setAcRecord(
							receive.getAfreshRemark() + "  重新确认(" + dateToString(receive.getMhTime()) + ")");

					// 更新数据
					Services.getReceiveService().update(receive);
					code = "200";
				}else {
					// 拼接
					ids += receive.getLoginId() + ",";
					userIds += receive.getUserId() + ",";
				}
			}

			// 消息推送
			getPush(mail, lastName, ids, userIds);

			success = true;
			msg = "重新确认传阅成功! ";
			json = "null";
			return Results.GLOBAL_FORM_JSON;

		}
	}

	// 调用消息推送接口
	private void getPush(Mail mail, String lastName, String ids, String userIds) {

		if (mail.getIfRemindAll()) { // 发消息给全部对象

			userIds += mail.getUserId() + "";
			ids += mail.getLoginId() + "";

			// 推送消息 --> (app)
			MessageUtils.pushEmobile(ids, 2, mail.getMailId());
			// 推送消息 --> (web)
			HrmMessagePushUtils.getSendPush(lastName, 4, userIds, userId, 4, mailId, true);

		}

		if (mail.getIfRemind()) { // 发消息给发件人
			// 推送消息 --> (app)
			MessageUtils.pushEmobile(mail.getLoginId(), 2, mail.getMailId());
			// 推送消息 --> (web)
			HrmMessagePushUtils.getSendPush(lastName, 2, mail.getUserId() + "", mail.getUserId(), 3, mail.getMailId());
		}
	}

	/**
	 * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss'(24小时制) 如Sat May 11 17:24:21
	 * CST 2002 to '2002-05-11 17:24:21'
	 * 
	 * @param time
	 *            Date 日期
	 * @return String 字符串
	 */
	public static String dateToString(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(time);

		return ctime;
	}

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isStatusConfirm() {
		return statusConfirm;
	}

	public void setStatusConfirm(boolean statusConfirm) {
		this.statusConfirm = statusConfirm;
	}

}
