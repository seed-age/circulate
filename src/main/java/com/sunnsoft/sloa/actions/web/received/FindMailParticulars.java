package com.sunnsoft.sloa.actions.web.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.util.ConstantUtils;
import com.sunnsoft.sloa.util.HrmMessagePushUtils;
import com.sunnsoft.sloa.util.mail.MessageUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 点击 已收/已发/待发/已删除 --> 传阅列表中某一条数据: 跳转到传阅详情页面, 展示该传阅的详细信息.
 *
 * @author chenjian
 *
 */
public class FindMailParticulars extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 用户ID
	private Long mailId; // 传阅ID
	private Integer mailStatus; // 待发 为 0 / 已发 为 1 /已删除 为 2 / 已收 为 3
	private static boolean status = false; // 判断该传阅是否 是未读状态
	private static boolean receivedStatus = false; // 判断该传阅是否 是已读状态
	private static boolean ifReadStatus = false; // 判断该传阅是否 勾选了 开封已阅确认 这个选项.

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户的ID不能为空..");
		Assert.notNull(mailId, "传阅的ID不能为空..");
		Assert.notNull(mailStatus, "区分状态参数不能为空..");

		try {
			// 使用switch语句
			switch (mailStatus) {

				case 0: // 待发传阅详情

					json = Services.getMailService().createHelper().getUserId().Eq(userId).getMailId().Eq(mailId)
							.getStatus().Eq(1).json().uniqueJson(new EachEntity2Map<Mail>() {
								@Override
								public void each(Mail mail, Map<String, Object> map) {
									List<AttachmentItem> itemList = new ArrayList<>();
									List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
									for (AttachmentItem attachmentItem : attachmentItems) {
										// 创建附件对象
										AttachmentItem item = new AttachmentItem();
										// 设置需要的值
										item.setItemId(attachmentItem.getItemId()); // 附件ID
										item.setBulkId(attachmentItem.getBulkId()); // 附近批次ID
										item.setUserId(attachmentItem.getUserId()); // 创建人ID
										item.setCreator(attachmentItem.getCreator()); // 上传附件的传阅对象
										item.setCreateTime(attachmentItem.getCreateTime()); // 上传附件的时间
										item.setFileName(attachmentItem.getFileName()); // 附件原名
										item.setFileCategory(attachmentItem.getFileCategory()); // 附件后缀
										item.setSaveName(attachmentItem.getSaveName()); // 附件保存名
										item.setUrlPath(attachmentItem.getUrlPath()); // url
										item.setState(attachmentItem.getState()); // 附件状态
										item.setItemSize(attachmentItem.getItemSize()); // 附件大小
										item.setItemNeid(attachmentItem.getItemNeid()); // 附件的网盘ID
										item.setItemRev(attachmentItem.getItemRev()); // 附件的网盘版本
										item.setItemDifferentiate(attachmentItem.getItemDifferentiate()); // 区别
										itemList.add(item);

									}
									map.put("attachmentItemss", itemList);

									List<Receive> receiveList = new ArrayList<>();
									List<Receive> receives = mail.getReceives();
									for (Receive receive : receives) {
										Receive receivess = new Receive();
										receivess.setReceiveId(receive.getReceiveId()); // 收件ID
										receivess.setUserId(receive.getUserId()); // 收件人ID
										receivess.setWorkCode(receive.getWorkCode()); // 收件人工作编号
										receivess.setLastName(receive.getLastName()); // 收件人姓名
										receivess.setLoginId(receive.getLoginId()); // 收件人登录名
										receivess.setSubcompanyName(receive.getSubcompanyName()); // 收件人的分部全称
										receivess.setDepartmentName(receive.getDepartmentName()); // 收件人的部门全称
										receivess.setReceiveTime(receive.getReceiveTime()); // 接收时间
										receivess.setAffirmTime(receive.getAffirmTime()); // 确认时间
										receivess.setReceiveStatus(receive.getReceiveStatus()); // 收件状态: 0 未开封 1 已开封
										receivess.setRemark(receive.getRemark()); // 确认信息备注
										receivess.setMailState(receive.getMailState()); // 传阅筛选状态
										receivess.setStepStatus(receive.getStepStatus()); // 传阅流程状态
										receivess.setReceiveAttention(receive.getReceiveAttention()); // 收件人的关注状态
										receivess.setOpenTime(receive.getOpenTime()); // 记录打开传阅的时间
										receivess.setIfConfirm(receive.getIfConfirm()); // 是否确认
										receivess.setConfirmRecord(receive.getConfirmRecord()); // 确认/标识
										receivess.setSerialNum(receive.getSerialNum()); // 序号
										receivess.setAfreshConfim(receive.getAfreshConfim());// 是否重新确认
										receivess.setAcRecord(receive.getAcRecord()); // (重新)确认/标识
										receivess.setAfreshRemark(receive.getAfreshRemark());// (重新)确认信息备注
										receivess.setMhTime(receive.getMhTime()); // (重新)确认时间
										receivess.setJoinTime(receive.getJoinTime()); // 添加联系人的时间
										receivess.setReDifferentiate(receive.getReDifferentiate()); // 区别是谁添加的联系人,
										// 存放添加该联系人的用户ID
										receiveList.add(receivess);
									}

									map.put("receivess", receiveList);

								}
							});

					success = true;
					code = "200";
					msg = "查询待发传阅详情成功!";
					break;

				case 1: // 已发传阅

					json = Services.getMailService().createHelper().getUserId().Eq(userId).getMailId().Eq(mailId).startOr()
							.getStepStatus().Eq(1).getStepStatus().Eq(3).stopOr().getStatus().Eq(0).json()
							.uniqueJson(new EachEntity2Map<Mail>() {

								@Override
								public void each(Mail mail, Map<String, Object> map) {
									List<AttachmentItem> itemList = new ArrayList<>();
									List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
									for (AttachmentItem attachmentItem : attachmentItems) {
										// 创建附件对象
										AttachmentItem item = new AttachmentItem();
										// 设置需要的值
										item.setItemId(attachmentItem.getItemId()); // 附件ID
										item.setBulkId(attachmentItem.getBulkId()); // 附近批次ID
										item.setUserId(attachmentItem.getUserId()); // 创建人ID
										item.setCreator(attachmentItem.getCreator()); // 上传附件的传阅对象
										item.setCreateTime(attachmentItem.getCreateTime()); // 上传附件的时间
										item.setFileName(attachmentItem.getFileName()); // 附件原名
										item.setFileCategory(attachmentItem.getFileCategory()); // 附件后缀
										item.setSaveName(attachmentItem.getSaveName()); // 附件保存名
										item.setUrlPath(attachmentItem.getUrlPath()); // url
										item.setState(attachmentItem.getState()); // 附件状态
										item.setItemSize(attachmentItem.getItemSize()); // 附件大小
										item.setItemNeid(attachmentItem.getItemNeid()); // 附件的网盘ID
										item.setItemRev(attachmentItem.getItemRev()); // 附件的网盘版本
										item.setItemDifferentiate(attachmentItem.getItemDifferentiate()); // 区别
										itemList.add(item);

									}
									map.put("attachmentItemss", itemList);

									List<Receive> receiveList = new ArrayList<>();
									List<Receive> receives = mail.getReceives();
									for (Receive receive : receives) {
										Receive receivess = new Receive();
										receivess.setReceiveId(receive.getReceiveId()); // 收件ID
										receivess.setUserId(receive.getUserId()); // 收件人ID
										receivess.setWorkCode(receive.getWorkCode()); // 收件人工作编号
										receivess.setLastName(receive.getLastName()); // 收件人姓名
										receivess.setLoginId(receive.getLoginId()); // 收件人登录名
										receivess.setSubcompanyName(receive.getSubcompanyName()); // 收件人的分部全称
										receivess.setDepartmentName(receive.getDepartmentName()); // 收件人的部门全称
										receivess.setReceiveTime(receive.getReceiveTime()); // 接收时间
										receivess.setAffirmTime(receive.getAffirmTime()); // 确认时间
										receivess.setReceiveStatus(receive.getReceiveStatus()); // 收件状态: 0 未开封 1 已开封
										receivess.setRemark(receive.getRemark()); // 确认信息备注
										receivess.setMailState(receive.getMailState()); // 传阅筛选状态
										receivess.setStepStatus(receive.getStepStatus()); // 传阅流程状态
										receivess.setReceiveAttention(receive.getReceiveAttention()); // 收件人的关注状态
										receivess.setOpenTime(receive.getOpenTime()); // 记录打开传阅的时间
										receivess.setIfConfirm(receive.getIfConfirm()); // 是否确认
										receivess.setConfirmRecord(receive.getConfirmRecord()); // 确认/标识
										receivess.setSerialNum(receive.getSerialNum()); // 序号
										receivess.setAfreshConfim(receive.getAfreshConfim());// 是否重新确认
										receivess.setAcRecord(receive.getAcRecord()); // (重新)确认/标识
										receivess.setAfreshRemark(receive.getAfreshRemark());// (重新)确认信息备注
										receivess.setMhTime(receive.getMhTime()); // (重新)确认时间
										receivess.setJoinTime(receive.getJoinTime()); // 添加联系人的时间
										receivess.setReDifferentiate(receive.getReDifferentiate()); // 区别是谁添加的联系人,
										// 存放添加该联系人的用户ID
										receiveList.add(receivess);
									}

									map.put("receivess", receiveList);
								}
							});

					success = true;
					code = "200";
					msg = "查询已发传阅详情成功!";
					break;

				case 2: // 已删除

					json = Services.getMailService().createHelper().getUserId().Eq(userId).getMailId().Eq(mailId)
							.getStatus().Eq(7).json().uniqueJson(new EachEntity2Map<Mail>() {

								@Override
								public void each(Mail mail, Map<String, Object> map) {
									List<AttachmentItem> itemList = new ArrayList<>();
									List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
									for (AttachmentItem attachmentItem : attachmentItems) {
										// 创建附件对象
										AttachmentItem item = new AttachmentItem();
										// 设置需要的值
										item.setItemId(attachmentItem.getItemId()); // 附件ID
										item.setBulkId(attachmentItem.getBulkId()); // 附近批次ID
										item.setUserId(attachmentItem.getUserId()); // 创建人ID
										item.setCreator(attachmentItem.getCreator()); // 上传附件的传阅对象
										item.setCreateTime(attachmentItem.getCreateTime()); // 上传附件的时间
										item.setFileName(attachmentItem.getFileName()); // 附件原名
										item.setFileCategory(attachmentItem.getFileCategory()); // 附件后缀
										item.setSaveName(attachmentItem.getSaveName()); // 附件保存名
										item.setUrlPath(attachmentItem.getUrlPath()); // url
										item.setState(attachmentItem.getState()); // 附件状态
										item.setItemSize(attachmentItem.getItemSize()); // 附件大小
										item.setItemNeid(attachmentItem.getItemNeid()); // 附件的网盘ID
										item.setItemRev(attachmentItem.getItemRev()); // 附件的网盘版本
										item.setItemDifferentiate(attachmentItem.getItemDifferentiate()); // 区别
										itemList.add(item);

									}
									map.put("attachmentItemss", itemList);
								}
							});

					success = true;
					code = "200";
					msg = "查询已删除传阅详情成功!";

					break;
				case 3: // 已收传阅

					Mail m = Services.getMailService().findById(mailId);

					json = Services.getReceiveService().createHelper().getUserId().Eq(userId).getMail().Eq(m).json().uniqueJson(new EachEntity2Map<Receive>() {
						@Override
						public void each(Receive receive, Map<String, Object> map) {
							map.clear();

							map.put("afreshConfimss", receive.getAfreshConfim());
							map.put("ifConfirmss", receive.getIfConfirm());
							map.put("stepStatus", receive.getStepStatus());
							map.put("receiveAttention", receive.getReceiveAttention());
							map.put("receiveTime", receive.getReceiveTime());

							Mail mail = receive.getMail();
							map.put("lastName", mail.getLastName());
							map.put("ifUpload", mail.getIfUpload());
							map.put("loginId", mail.getLoginId());
							map.put("ifRemind", mail.getIfRemind());
							map.put("ifNotify", mail.getIfNotify());
							map.put("title", mail.getTitle());
							map.put("enabled", mail.isEnabled());
							map.put("ifSequence", mail.getIfSequence());
							map.put("ifImportant", mail.getIfImportant());
							map.put("hasAttachment", mail.getHasAttachment());
							map.put("workCode", mail.getWorkCode());
							map.put("ruleName", mail.getRuleName());
							map.put("subcompanyName", mail.getSubcompanyName());
							map.put("departmentName", mail.getDepartmentName());
							map.put("ifRead", mail.getIfRead());
							map.put("allReceiveName", mail.getAllReceiveName());
							map.put("completeTime", mail.getCompleteTime());
							map.put("userId", mail.getUserId());
							map.put("ifSecrecy", mail.getIfSecrecy());
							map.put("sendTime", mail.getSendTime());
							map.put("ifAdd", mail.getIfAdd());
							map.put("createTime", mail.getCreateTime());
							map.put("deleteTime", mail.getDeleteTime());
							// 按照实地徐经理的要求, 把传阅内容去掉 或者设置为  null
							map.put("mailContent", null);
							map.put("attention", mail.getAttention());
							map.put("mailId", mail.getMailId());
							map.put("ifUpdate", mail.getIfUpdate());
							map.put("ifRemindAll", mail.getIfRemindAll());
							map.put("status", mail.getStatus());

							List<AttachmentItem> itemList = new ArrayList<>();
							List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
							for (AttachmentItem attachmentItem : attachmentItems) {
								// 创建附件对象
								AttachmentItem item = new AttachmentItem();
								// 设置需要的值
								item.setItemId(attachmentItem.getItemId()); // 附件ID
								item.setBulkId(attachmentItem.getBulkId()); // 附近批次ID
								item.setUserId(attachmentItem.getUserId()); // 创建人ID
								item.setCreator(attachmentItem.getCreator()); // 上传附件的传阅对象
								item.setCreateTime(attachmentItem.getCreateTime()); // 上传附件的时间
								item.setFileName(attachmentItem.getFileName()); // 附件原名
								item.setFileCategory(attachmentItem.getFileCategory()); // 附件后缀
								item.setSaveName(attachmentItem.getSaveName()); // 附件保存名
								item.setUrlPath(attachmentItem.getUrlPath()); // url
								item.setState(attachmentItem.getState()); // 附件状态
								item.setItemSize(attachmentItem.getItemSize()); // 附件大小
								item.setItemNeid(attachmentItem.getItemNeid()); // 附件的网盘ID
								item.setItemRev(attachmentItem.getItemRev()); // 附件的网盘版本
								item.setItemDifferentiate(attachmentItem.getItemDifferentiate()); // 区别
								itemList.add(item);

							}
							map.put("attachmentItemss", itemList);

							// 判断如果是未读
							if (receive.getMailState() == ConstantUtils.RECEIVE_UNREAD_STATUS) {
								receivedStatus = true;
								status = true;
							}else {
								receivedStatus = false;
								status = false;
							}

							/*
							 * 进行判断, 如果发送人在发送传阅的时候勾选了 开封已阅确认 这个选项; 那么在接收人接收到传阅,并且点击该传阅进入到传阅详情的时候;
							 * 该传阅就已经是确认状态了!
							 */
							if (mail.getIfRead()) { // 如果为 true 就进来
								ifReadStatus = true;
								status = false; // 设置为false， 不走普通的判断
							}else {
								ifReadStatus = false;
							}

						}
					});


					// 如果为 true 就表示该传阅时 未读 为开封状态.
					if (receivedStatus) {

						// 根据传阅ID查询传阅信息
						Mail mail = Services.getMailService().createHelper().getMailId().Eq(mailId).uniqueResult();

						// 根据用户ID和传阅对象查询到接收人信息
						Receive receive = Services.getReceiveService().createHelper().getUserId().Eq(userId).getMail()
								.Eq(mail).uniqueResult();

						// 定义一个变量,用于统计
						int count = 0;

						if (status) {

							receive.setMailState(ConstantUtils.RECEIVE_READ_STATUS); // 表示已读
							receive.setReceiveStatus(ConstantUtils.RECEIVE_OPEN_STATUS); // 表示 已开封
							receive.setOpenTime(new Date()); // 开封时间
							// 更新
							Services.getReceiveService().update(receive);
							msg = "更新状态!";
							status = false;
						}

						// 判断
						if (ifReadStatus) { // 如果为true, 说明该传阅是勾选了 开封已阅确认 这个选项的

							// 设置
							// 修改收件人的传阅状态, 进行修改该传阅的状态 修改为 6 (表示已读) 以及进行确认传阅
							receive.setReceiveStatus(ConstantUtils.RECEIVE_OPEN_STATUS); // 1 表示 已开封
							receive.setOpenTime(new Date()); // 记录打开传阅的时间
							receive.setMailState(ConstantUtils.RECEIVE_READ_STATUS); // 收件人的传阅筛选状态 6 表示 已读
							receive.setIfConfirm(true); // true 表示 已确认该传阅
							receive.setAffirmTime(new Date()); // 设置确认时间
							receive.setRemark("传阅已确认"); // 确认时的 确认信息
							// 设置确认/标识
//						receive.setConfirmRecord(receive.getRemark() + "  确认(" + dateToString(receive.getAffirmTime()) + ")");
							receive.setConfirmRecord(receive.getRemark() + "  (" + dateToString(receive.getAffirmTime()) + ")");
							receive.setStepStatus(ConstantUtils.MAIL_HALFWAY_STATUS); // 修改当前收件人这边的传阅流程状态 1 表示传阅中

							// 更新
							Services.getReceiveService().update(receive);
							count++; // 表示 该传阅已经有一个人确认了.
							msg = "该传阅是开封已阅确认!";

							// 只有勾选了 确认时提醒 ,  确认时提醒所有传阅对象 才进行消息推送
							if (mail.getIfRemind() || mail.getIfRemindAll()) {
								try {

									// 获取消息推送人的名称
									String lastName = receive.getLastName();
									// 拼接, 接收消息的Id
									String ids = "";
									String userIds = "";
									List<Receive> receives = mail.getReceives();
									for (Receive r : receives) {
										// 不需要当前用户的ID
										if(r.getUserId() != userId){
											// 拼接
											ids += r.getLoginId() + ",";
											userIds += r.getUserId() + ",";
										}
									}

									// 调用消息推送接口
									getPush(mail, lastName, ids, userIds);
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("===============传阅开封已阅确认的时候:     消息推送失败============");
								}
							}

							ifReadStatus = false;

						}

						// 判断
						if (count > 0) {
							// 定义标识
							int countStatus = 0;

							// 获取最新的mail数据
							Mail newMail = Services.getMailService().createHelper().getMailId().Eq(mailId).uniqueResult();
							// 获取最新的接收人数据
							List<Receive> receives = newMail.getReceives();
							// 遍历
							for (Receive newReceive : receives) {
								Boolean ifConfirm = newReceive.getIfConfirm();
								String remark = newReceive.getRemark();
								// 判断, 如果为true 并且 不为空
								if (ifConfirm && remark != null) {
									countStatus++;
								}
							}

							// 判断, 如果相等表示所有的接收人都已经确认传阅了
							if (countStatus == receives.size()) {
								// 遍历
								for (Receive newReceive : receives) {
									// 设置
									newReceive.setStepStatus(ConstantUtils.MAIL_COMPLETE_STATUS); // 表示 已完成
									// 更新
									Services.getReceiveService().update(newReceive);
								}
								// 设置
								newMail.setStepStatus(ConstantUtils.MAIL_COMPLETE_STATUS); // 已发传阅 --> 表示该传阅已完成
								// 更新
								Services.getMailService().update(newMail);
								msg = msg + " 该传阅已完成!";
							}

							// 重新获取最新的数据
							getReceives();
						}

						receivedStatus = false;
					}



					success = true;
					code = "200";
					msg = "查询已收传阅详情成功!" + msg;
					break;

				default:
					success = false;
					code = "404";
					msg = "查询传阅详情失败!";
					break;
			}
		} catch (Exception e) {
			success = false;
			code = "500";
			msg = "网络繁忙,请返回主页面!";
			json = "null";

			// 对静态变量恢复初始值.
			status = false; // 判断该传阅是否 是未读状态
			receivedStatus = false; // 判断该传阅是否 是已读状态
			ifReadStatus = false; // 判断该传阅是否 勾选了 开封已阅确认 这个选项.

			e.printStackTrace();
			return Results.GLOBAL_FORM_JSON;
		}finally {
			// 对静态变量恢复初始值.
			status = false; // 判断该传阅是否 是未读状态
			receivedStatus = false; // 判断该传阅是否 是已读状态
			ifReadStatus = false; // 判断该传阅是否 勾选了 开封已阅确认 这个选项.
		}

		// 对静态变量恢复初始值.
		status = false; // 判断该传阅是否 是未读状态
		receivedStatus = false; // 判断该传阅是否 是已读状态
		ifReadStatus = false; // 判断该传阅是否 勾选了 开封已阅确认 这个选项.

		if (json != null) {
			return Results.GLOBAL_FORM_JSON;
		} else {
			return Results.GLOBAL_FORM_JSON;
		}
	}

	public void getReceives() {

		Mail m = Services.getMailService().findById(mailId);

		json = Services.getReceiveService().createHelper().getUserId().Eq(userId).getMail().Eq(m).json().uniqueJson(new EachEntity2Map<Receive>() {
			@Override
			public void each(Receive receive, Map<String, Object> map) {
				map.clear();

				map.put("afreshConfimss", receive.getAfreshConfim());
				map.put("ifConfirmss", receive.getIfConfirm());
				map.put("stepStatus", receive.getStepStatus());
				map.put("receiveAttention", receive.getReceiveAttention());
				map.put("receiveTime", receive.getReceiveTime());

				Mail mail = receive.getMail();
				map.put("lastName", mail.getLastName());
				map.put("ifUpload", mail.getIfUpload());
				map.put("loginId", mail.getLoginId());
				map.put("ifRemind", mail.getIfRemind());
				map.put("ifNotify", mail.getIfNotify());
				map.put("title", mail.getTitle());
				map.put("enabled", mail.isEnabled());
				map.put("ifSequence", mail.getIfSequence());
				map.put("ifImportant", mail.getIfImportant());
				map.put("hasAttachment", mail.getHasAttachment());
				map.put("workCode", mail.getWorkCode());
				map.put("ruleName", mail.getRuleName());
				map.put("subcompanyName", mail.getSubcompanyName());
				map.put("departmentName", mail.getDepartmentName());
				map.put("ifRead", mail.getIfRead());
				map.put("allReceiveName", mail.getAllReceiveName());
				map.put("completeTime", mail.getCompleteTime());
				map.put("userId", mail.getUserId());
				map.put("ifSecrecy", mail.getIfSecrecy());
				map.put("sendTime", mail.getSendTime());
				map.put("ifAdd", mail.getIfAdd());
				map.put("createTime", mail.getCreateTime());
				map.put("deleteTime", mail.getDeleteTime());
				map.put("mailContent", mail.getMailContent());
				map.put("attention", mail.getAttention());
				map.put("mailId", mail.getMailId());
				map.put("ifUpdate", mail.getIfUpdate());
				map.put("ifRemindAll", mail.getIfRemindAll());
				map.put("status", mail.getStatus());

				List<AttachmentItem> itemList = new ArrayList<>();
				List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
				for (AttachmentItem attachmentItem : attachmentItems) {
					// 创建附件对象
					AttachmentItem item = new AttachmentItem();
					// 设置需要的值
					item.setItemId(attachmentItem.getItemId()); // 附件ID
					item.setBulkId(attachmentItem.getBulkId()); // 附近批次ID
					item.setUserId(attachmentItem.getUserId()); // 创建人ID
					item.setCreator(attachmentItem.getCreator()); // 上传附件的传阅对象
					item.setCreateTime(attachmentItem.getCreateTime()); // 上传附件的时间
					item.setFileName(attachmentItem.getFileName()); // 附件原名
					item.setFileCategory(attachmentItem.getFileCategory()); // 附件后缀
					item.setSaveName(attachmentItem.getSaveName()); // 附件保存名
					item.setUrlPath(attachmentItem.getUrlPath()); // url
					item.setState(attachmentItem.getState()); // 附件状态
					item.setItemSize(attachmentItem.getItemSize()); // 附件大小
					item.setItemNeid(attachmentItem.getItemNeid()); // 附件的网盘ID
					item.setItemRev(attachmentItem.getItemRev()); // 附件的网盘版本
					item.setItemDifferentiate(attachmentItem.getItemDifferentiate()); // 区别
					itemList.add(item);

				}
				map.put("attachmentItemss", itemList);

			}
		});
	}

	private void getPush(Mail mail, String lastName, String ids, String userIds) {
		if (mail.getIfRemindAll()) {

//			ids += mail.getLoginId() + "";
			userIds += mail.getUserId() + "";

			// 推送消息 --> (app)
			MessageUtils.pushEmobile(ids, 2, mail.getMailId(), userId.intValue(), 3);
			MessageUtils.pushEmobile(mail.getLoginId(), 2, mail.getMailId(), userId.intValue(), 1);
			// 推送消息 --> (web)
			HrmMessagePushUtils.getSendPush(lastName, 4, userIds, userId, 4, mailId, true);

			return;
		}

		if (mail.getIfRemind()) {
			// 推送消息 --> (app)
			MessageUtils.pushEmobile(mail.getLoginId(), 2, mail.getMailId(), userId.intValue(), 1);
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

	public Integer getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(Integer mailStatus) {
		this.mailStatus = mailStatus;
	}

}
