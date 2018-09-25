package com.sunnsoft.sloa.actions.web.send;

import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.struts2.Results;

/**
 * 已发/已收传阅 -- 点击关注: 表示单个关注传阅，点击进行关注.
 * 
 * @author chenjian
 *
 */
public class InsertAttention extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 收件人Id(也是当前用户)
	private Long mailId; // 传阅Id
	private boolean attention; // true 为关注 false 取消关注
	private int status; // 1 为已发 2 为已删除 3 为 已收

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID,不能为空");
		Assert.notNull(mailId, "传阅Id,不能为空");

		// 根据传阅ID查询传阅数据
		Mail mail = Services.getMailService().findById(mailId);

		// 判断是这次的关注操作 是已发 还是 已收
		switch (status) {
		case 1: // 1 表示已发操作

			String name = "已发传阅";

			// 判断是 关注 还是 取消关注
			if (attention) { // true 为关注
				// 判断是否是这个用户
				if (mail.getUserId() == userId.longValue()) {
					// 判断该传阅有没有被关注
					if (mail.getAttention().equals(false)) { // false 表示 未关注
						// 新增一条关注记录
						Services.getUserCollectionService().createHelper().bean().create().setMail(mail)
								.setUserId(mail.getUserId()).setLastName(mail.getLastName())
								.setWorkCode(mail.getWorkCode()).setCollectionTime(new Date()).setDifferentiate(name)
								.insertUnique();
						// 设置是否关注这个字段为 true 表示关注
						mail.setAttention(attention);
						// 更新
						Services.getMailService().update(mail);
						code = "200";
						msg = "已发传阅关注成功!";
					}
				}

			} else { // false 为 取消关注
				// 判断是否是这个用户
				if (mail.getUserId() == userId.longValue()) {
					// 判断该传阅有没有被关注
					if (mail.getAttention().equals(true)) { // true 表示 已关注
						// 查询出该关注记录
						Services.getUserCollectionService().createHelper().getMail().Eq(mail).getUserId().Eq(userId)
								.getDifferentiate().Eq(name).delete();
						// 设置是否关注这个字段为 false 表示关注
						mail.setAttention(attention);
						// 更新
						Services.getMailService().update(mail);
						code = "200";
						msg = "已发传阅, 取消关注!";
					}
				}
			}

			try {
				if (code.equals("200")) {
					success = true;
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
				code = "404";
				msg = "操作失误!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			break;

		case 2: // 已删除
			String deleteName = "已删除传阅";

			// 判断是 关注 还是 取消关注
			if (attention) { // true 为关注
				// 判断是否是这个用户
				if (mail.getUserId() == userId.longValue()) {
					// 判断该传阅有没有被关注
					if (mail.getAttention().equals(false)) { // false 表示 未关注
						// 新增一条关注记录
						Services.getUserCollectionService().createHelper().bean().create().setMail(mail)
								.setUserId(mail.getUserId()).setLastName(mail.getLastName())
								.setWorkCode(mail.getWorkCode()).setCollectionTime(new Date())
								.setDifferentiate(deleteName).insertUnique();
						// 设置是否关注这个字段为 true 表示关注
						mail.setAttention(attention);
						// 更新
						Services.getMailService().update(mail);
						code = "200";
						msg = "已删除传阅关注成功!";
					}
				}

			} else { // false 为 取消关注
				// 判断是否是这个用户
				if (mail.getUserId() == userId.longValue()) {
					// 判断该传阅有没有被关注
					if (mail.getAttention().equals(true)) { // true 表示 已关注
						// 查询出该关注记录
						Services.getUserCollectionService().createHelper().getMail().Eq(mail).getUserId().Eq(userId)
								.getDifferentiate().Eq(deleteName).delete();
						// 设置是否关注这个字段为 false 表示关注
						mail.setAttention(attention);
						// 更新
						Services.getMailService().update(mail);
						code = "200";
						msg = "已删除传阅, 取消关注!";
					}
				}
			}

			try {
				if (code.equals("200")) {
					success = true;
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
				code = "404";
				msg = "操作失误!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			break;

		case 3: // 3 表示已收操作
			String reName = "收到传阅";

			// 获取接收人的集合
			List<Receive> receives = mail.getReceives();
			// 遍历
			for (Receive receive : receives) {
				// 判断
				if (attention) { // true 表示 关注
					// 判断是否是这个用户
					if (receive.getUserId() == userId.longValue()) {
						// 判断该传阅有没有被关注
						if (receive.getReceiveAttention() == null || receive.getReceiveAttention().equals(false)) { // true 表示 已关注
							// 新增一条关注记录
							Services.getUserCollectionService().createHelper().bean().create().setMail(mail)
									.setUserId(receive.getUserId()).setLastName(receive.getLastName())
									.setWorkCode(receive.getWorkCode()).setCollectionTime(new Date())
									.setDifferentiate(reName).insertUnique();
							// true 表示关注
							receive.setReceiveAttention(true);
							// 更新
							Services.getReceiveService().update(receive);
							code = "200";
							msg = "收到传阅,关注成功!";
						}
					}
				} else { // 取消关注
					// 判断是否是这个用户
					if (receive.getUserId() == userId.longValue()) {
						// 判断该传阅有没有被关注
						if (receive.getReceiveAttention().equals(true)) { // true 表示 已关注
							// 查询出该关注记录
							Services.getUserCollectionService().createHelper().getMail().Eq(mail).getUserId().Eq(userId)
									.getDifferentiate().Eq(reName).delete();
							// 设置是否关注这个字段为 false 表示关注
							receive.setReceiveAttention(attention);
							// 更新
							Services.getReceiveService().update(receive);
							code = "200";
							msg = "收到传阅,取消关注!";
						}
					}
				}
			}

			try {
				if (code.equals("200")) {
					success = true;
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}
			} catch (Exception e) {
				e.printStackTrace();
				success = false;
				code = "404";
				msg = "操作失误!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
			break;

		default:
			msg = "网络繁忙,请重新刷新页面";
			success = false;
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		msg = "网络繁忙,请重新刷新页面1";
		success = false;
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

	public boolean isAttention() {
		return attention;
	}

	public void setAttention(boolean attention) {
		this.attention = attention;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
