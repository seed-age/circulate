package com.sunnsoft.sloa.actions.web.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.util.HrmMessagePushUtils;
import com.sunnsoft.sloa.util.mail.MessageUtils;
import com.sunnsoft.util.struts2.Results;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 收到传阅(PC端/APP端)--点击 跳过/ 点击编辑--选择跳过 : 批量快捷处理未读传阅，把未读传阅改为已读，同时并确认.
 *
 * @author chenjian
 *
 */
public class BatchUpdateState extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long[] mailId; // 前端页面传递过来的传阅ID
	private Long userId; // 前端页面传递过来的收件人ID(也就是当前用户)

	private boolean success = false;

	@Override
	public String execute() throws Exception {
		// 校验数据
		Assert.notNull(mailId, "传阅ID不能为空");
		Assert.notNull(userId, "收件人ID(也是当前用户ID)不能为空");

		// 定义一个统计这次标记传阅为已读状态的变量
		int mailCount = 0;

		// 遍历传阅ID
		for (long l : mailId) {

			// 通过传阅ID去查找收件人
			Mail mail = Services.getMailService().findById(l);

			// 获取传阅对应的收件人数据
			List<Receive> receives = mail.getReceives();

			// 定义一个标记, 统计有多少个接收人确认了传阅 1
			int count = 0;
			// 定义一个标记, 表示当前收件人已经确认传阅了
			boolean re = false;

			// 遍历收件人集合
			String lastName = "";
			for (Receive receive : receives) {
				//用于消息推送
				lastName = receive.getLastName();

				// 一旦接收人确认了传阅, 那么发件人的传阅流程状态就改变了, 变成 1(传阅中)
				// 如果,该传阅已经有一个或是多个接收人对该传阅进行了确认, 所以该传阅的流程状态是 1(传阅中)
				// 进行判断, 如果其余的接收人 都已经确认传阅的话, 就进来
				if (receive.getIfConfirm() == true) {
					// 再次判断, 如果 确认/标识 为 null, 证明传阅没有确认
					if (receive.getConfirmRecord() != null) {
						count++; // 表示 有其他接收人已经确认传阅了 +1
						continue;
					}
				}
				// 通过userId(页面传过来的当前用户ID)和查找到的收件人ID进行比较, 如果相等 和 是否确认为 false,就修改该收件人数据
				if (receive.getUserId() == userId && receive.getIfConfirm() == false) {
					// 如果相等, 证明就是该用户的收到传阅的记录. 进行修改收到传阅记录
					// 修改收件人的传阅状态, 进行修改该传阅的状态 修改为 6 (表示已读) 以及进行确认传阅
					receive.setReceiveStatus(1); // 1 表示 已开封
					receive.setOpenTime(new Date()); // 记录打开传阅的时间
					receive.setMailState(6); // 收件人的传阅筛选状态 6 表示 已读
					receive.setIfConfirm(true); // true 表示 已确认该传阅
					receive.setAffirmTime(new Date()); // 设置确认时间
					receive.setRemark("传阅已确认"); // 确认时的 确认信息
					// 设置确认/标识
					receive.setConfirmRecord(
							receive.getRemark() + "  (" + dateToString(receive.getAffirmTime()) + ")");
					receive.setStepStatus(1); // 修改当前收件人这边的传阅流程状态 1 表示传阅中

					// 更新数据
					Services.getReceiveService().update(receive);
					re = true;
					success = true;
				}

				// 如果为true 进来
				if (re) {
					count++; // 表示当前接收人对该传阅已经确认了
					mailCount++; // 标记这次修改是否成功;
					re = false;
				}

			}

			// 如果count == 0 那么证明这个次的批量修改失败
			if (count > 0) {
				// 获取最新的传阅数据
				Mail newMail = Services.getMailService().findById(l);
				// 获取最新的接收人数据
				List<Receive> receivesList = newMail.getReceives();

				// 再判断, 如果count相等于收件人集合的数量那么就表示该传阅已经全部确认了
				if (count == receivesList.size()) {
					// 如果是true, 那么就修改该传阅的流程状态
					newMail.setStepStatus(3); // 表示 该传阅已完成
					// 更新
					Services.getMailService().update(newMail);

					// 遍历
					for (Receive receivess : receivesList) {
						// 设置接收人的传阅流程状态
						receivess.setStepStatus(3); // 3 表示 该传阅已经 传阅完成了
						// 更新
						Services.getReceiveService().update(receivess);
					}

					msg = "该传阅已完成";
				}
			}
			if(mail.getIfRead() || mail.getIfRemind()) {

				// 推送消息 --> (app)
				MessageUtils.pushEmobile(mail.getLoginId(), 2, mail.getMailId(), userId.intValue());
				// 推送消息 --> (web)
				HrmMessagePushUtils.getSendPush(lastName, 2, mail.getUserId()+"", mail.getUserId(), 3, mail.getMailId());
			}
		}

		// 进行判断. 如果相等, 表示这次修改成功
		if (mailCount > 0) {

			if (mailCount == mailId.length) {
				msg = "标识传阅为已读状态成功.";
				code = "200";
				json = "null";
				return Results.GLOBAL_FORM_JSON;

			}

			msg = "标识传阅为已读状态成功!";
			success = true;
			code = "200";
			json = "null";
			return Results.GLOBAL_FORM_JSON;

		} else {

			msg = "标识传阅为已读状态失败!";
			success = false;
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
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

	public Long[] getMailId() {
		return mailId;
	}

	public void setMailId(Long[] mailId) {
		this.mailId = mailId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
