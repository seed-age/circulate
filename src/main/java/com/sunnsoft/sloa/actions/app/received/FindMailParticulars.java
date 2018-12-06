package com.sunnsoft.sloa.actions.app.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
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
									String string = stripHtml(mail.getMailContent());
									System.out.println("详情, 转义之后: " + string);
									mail.setMailContent(string);
									map.put("mailContent", string);
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

					json = Services.getMailService().createHelper().getMailId().Eq(mailId).json()
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

									List<Receive> receives = mail.getReceives();
									List<Receive> receivesList = new ArrayList<>();
									for (Receive receive : receives) {
										// 创建接收人对象
										Receive receivess = new Receive();
										receivess.setReceiveId(receive.getReceiveId()); // 收件ID
										receivess.setUserId(receive.getUserId()); // 接收人ID
										receivess.setWorkCode(receive.getWorkCode()); // 接收人工作编号
										receivess.setLastName(receive.getLastName()); // 接收人姓名
										receivess.setLoginId(receive.getLoginId()); // 接收人登录名
										receivess.setSubcompanyName(receive.getSubcompanyName()); // 分部全称
										receivess.setDepartmentName(receivess.getDepartmentName()); // 部门全称
										receivess.setReceiveTime(receive.getReceiveTime()); // 接收时间(PS: 和传阅的发送时间一样)
										receivess.setJoinTime(receive.getJoinTime());
										receivess.setReceiveStatus(receive.getReceiveStatus());
										receivess.setMailState(receive.getMailState());
										receivess.setStepStatus(receive.getStepStatus());
										receivess.setOpenTime(receive.getOpenTime());
										receivess.setIfConfirm(receive.getIfConfirm());
										receivess.setAffirmTime(receive.getAffirmTime());
										receivess.setRemark(receive.getRemark());
										receivess.setConfirmRecord(receive.getConfirmRecord());
										receivess.setSerialNum(receive.getSerialNum());
										receivess.setAfreshConfim(receive.getAfreshConfim());
										receivess.setAcRecord(receive.getAcRecord());
										receivess.setMhTime(receive.getMhTime());
										receivess.setReceiveAttention(receive.getReceiveAttention());
										receivess.setReDifferentiate(receive.getReDifferentiate());
										receivesList.add(receivess);
										// 判断
										if (receive.getUserId() == userId) {
											map.put("ifConfirmss", receive.getIfConfirm());
											map.put("afreshConfimss", receive.getAfreshConfim());
											map.put("receiveAttention", receive.getReceiveAttention());
											map.put("receiveTime", receive.getReceiveTime());
											// 再次判断
											if (receive.getMailState() == 5) {
												receivedStatus = true;
												status = true;
											}
										}
									}
									map.put("receivess", receivesList);

									/*
									 * 进行判断, 如果发送人在发送传阅的时候勾选了 开封已阅确认 这个选项; 那么在接收人接收到传阅,并且点击该传阅进入到传阅详情的时候;
									 * 该传阅就已经是确认状态了!
									 */
									if (mail.getIfRead()) { // 如果为 true 就进来
										ifReadStatus = true;
										status = false; // 设置为false， 不走普通的判断
									}
								}
							});

					// 如果为 true 就表示该传阅是 未读 并且是未开封状态.
					if (receivedStatus) {

						// 根据传阅ID查询传阅信息
						Mail mail = Services.getMailService().createHelper().getMailId().Eq(mailId).uniqueResult();
						// 根据用户ID和传阅对象查询到接收人信息
						Receive receive = Services.getReceiveService().createHelper().getUserId().Eq(userId).getMail()
								.Eq(mail).uniqueResult();

						// 定义一个变量,用于统计
						int count = 0;

						if (status) {

							receive.setMailState(6); // 表示已读
							receive.setReceiveStatus(1); // 表示 已开封
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
							receive.setReceiveStatus(1); // 1 表示 已开封
							receive.setOpenTime(new Date()); // 记录打开传阅的时间
							receive.setMailState(6); // 收件人的传阅筛选状态 6 表示 已读
							receive.setIfConfirm(true); // true 表示 已确认该传阅
							receive.setAffirmTime(new Date()); // 设置确认时间
							receive.setRemark("传阅已确认.."); // 确认时的 确认信息
							// 设置确认/标识
							receive.setConfirmRecord(
									receive.getRemark() + "  确认(" + dateToString(receive.getAffirmTime()) + ")");
							receive.setStepStatus(1); // 修改当前收件人这边的传阅流程状态 1 表示传阅中

							// 更新
							Services.getReceiveService().update(receive);
							count++; // 表示 该传阅已经有一个人确认了.
							msg = "该传阅是开封已阅确认!";

							// 推送消息 --> (app)
							MessageUtils.pushEmobile(mail.getLoginId(), 3, mail.getMailId(), userId.intValue());
							// 推送消息 --> (web)
							HrmMessagePushUtils.getSendPush(receive.getLastName(), 3, mail.getUserId() + "",
									mail.getUserId(), 3, mail.getMailId());

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
									newReceive.setStepStatus(3); // 表示 已完成
									// 更新
									Services.getReceiveService().update(newReceive);
								}
								// 设置
								newMail.setStepStatus(3); // 已发传阅 --> 表示该传阅已完成
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
					msg = "查询已收传阅详情成功!";
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
			msg = "网络繁忙,请稍后再试!";
			json = "null";
			e.printStackTrace();
			return Results.GLOBAL_FORM_JSON;
		}

		if (json != null) {
			return Results.GLOBAL_FORM_JSON;
		} else {
			return Results.GLOBAL_FORM_JSON;
		}
	}

	public void getReceives() {
		json = Services.getMailService().createHelper().getMailId().Eq(mailId).json()
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

						List<Receive> receives = mail.getReceives();
						List<Receive> receivesList = new ArrayList<>();
						for (Receive receive : receives) {
							// 创建接收人对象
							Receive receivess = new Receive();
							receivess.setReceiveId(receive.getReceiveId()); // 收件ID
							receivess.setUserId(receive.getUserId()); // 接收人ID
							receivess.setWorkCode(receive.getWorkCode()); // 接收人工作编号
							receivess.setLastName(receive.getLastName()); // 接收人姓名
							receivess.setLoginId(receive.getLoginId()); // 接收人登录名
							receivess.setSubcompanyName(receive.getSubcompanyName()); // 分部全称
							receivess.setDepartmentName(receivess.getDepartmentName()); // 部门全称
							receivess.setReceiveTime(receive.getReceiveTime()); // 接收时间(PS: 和传阅的发送时间一样)
							receivess.setJoinTime(receive.getJoinTime());
							receivess.setReceiveStatus(receive.getReceiveStatus());
							receivess.setMailState(receive.getMailState());
							receivess.setStepStatus(receive.getStepStatus());
							receivess.setOpenTime(receive.getOpenTime());
							receivess.setIfConfirm(receive.getIfConfirm());
							receivess.setAffirmTime(receive.getAffirmTime());
							receivess.setRemark(receive.getRemark());
							receivess.setConfirmRecord(receive.getConfirmRecord());
							receivess.setSerialNum(receive.getSerialNum());
							receivess.setAfreshConfim(receive.getAfreshConfim());
							receivess.setAcRecord(receive.getAcRecord());
							receivess.setMhTime(receive.getMhTime());
							receivess.setReceiveAttention(receive.getReceiveAttention());
							receivess.setReDifferentiate(receive.getReDifferentiate());
							receivesList.add(receivess);
							// 判断
							if (receive.getUserId() == userId) {
								map.put("ifConfirmss", receive.getIfConfirm());
								map.put("afreshConfimss", receive.getAfreshConfim());
								map.put("receiveAttention", receive.getReceiveAttention());
							}
						}
						map.put("receivess", receivesList);

					}
				});
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

	/**
	 * 去除html标签(用于待发传阅)
	 */
	public static String stripHtml(String content) {
		// <p>段落替换为换行
		content = content.replaceAll("<p .*?>", "\r\n");
		// <br><br/>替换为换行
		content = content.replaceAll("<br\\s*/?>", "\r\n");
		// 空格替换为换行
		content = content.replaceAll("&nbsp;", " ");
		// 去掉其它的<>之间的东西
		content = content.replaceAll("\\<.*?>", "");
		// 还原HTML
		// content = HTMLDecoder.decode(content);
		return content;
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
