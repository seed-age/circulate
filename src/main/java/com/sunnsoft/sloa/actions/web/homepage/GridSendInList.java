package com.sunnsoft.sloa.actions.web.homepage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.helper.MailHelper;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;

/**
 * 已发传阅: 点击查看跳转到已发传阅列表（传阅数据筛选为 传阅中 , 已完成 其中的任意一个)
 * 
 * @author chenjian
 *
 */
public class GridSendInList extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 发件人Id
	private String likeName; // 客户端传过来的(用户输入的)
	private String startTime; // 开始时间
	private String endTime; // 结束时间
	private int mailStatus; // 传阅的流程状态 1 表示传阅中 3 表示已完成 (默认值为 0 表示所有)
	private Integer orderBy; // 排序

	private Integer page; // 当前页
	private Integer pageRows; // 显示几条记录

	@Override
	public String execute() throws Exception {

		// 参数校验
		Assert.notNull(userId, "发件人Id不能为空!");
		Assert.notNull(mailStatus, "传阅的流程状态不能为空!");

		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		/// 给排序设置默认值 默认值为 2 默认降序
		this.orderBy = (orderBy == null) ? 2 : orderBy;

		// 设置默认值, 如果搜索条件为null 默认值为 ""
		this.likeName = (likeName == null) ? "" : likeName;
		this.startTime = (startTime.equals("")) ? null : startTime;

		// 使用switch 选择查询数据
		switch (mailStatus) {
		case 0: // 表示查询所有的已发传阅 包含 传阅中 , 已完成

			if (orderBy == 2) { // 2 表示降序

				if (likeName.length() > 0 || !likeName.equals("") || startTime != null) {
					
					
					MailHelper helper = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(mailStatus);
					
					//进行判断
					if(startTime != null) {
						//设置开始时间 和 结束时间 以及 日期转换的格式
						String timeStr = getDateTime();
						helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime()
						.Le(StrToDate(endTime, timeStr));
					}
					
					
					// 分页查询 传阅状态默认为 0 0 不做任何表示
					json = helper.startOr().getTitle().Like(likeName).getAllReceiveName()
							.Like(likeName).getLastName().Like(likeName).getMailContent().Like(likeName).stopOr()
							.getSendTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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

				} else {
					
					json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(mailStatus)
							.getSendTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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

				}
				success = true;
				code = "200";
				msg = "查询已发传阅!";
			} else if (orderBy == 1) { // 1 表示升序

				if (likeName.length() > 0 || !likeName.equals("") || startTime != null) {

					
					MailHelper helper = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(mailStatus);
					
					//进行判断
					if(startTime != null) {
						//设置开始时间 和 结束时间 以及 日期转换的格式
						String timeStr = getDateTime();
						helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime()
						.Le(StrToDate(endTime, timeStr));
					}
					
					
					// 分页查询 传阅状态默认为 0 0 不做任何表示
					json = helper.startOr().getTitle().Like(likeName).getAllReceiveName()
							.Like(likeName).getLastName().Like(likeName).getMailContent().Like(likeName).stopOr()
							.getSendTime().Asc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
				} else {
					json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(mailStatus)
							.getSendTime().Asc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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

				}

				success = true;
				code = "200";
				msg = "查询已发传阅!";
			}

			if (json != null) {
				return Results.GLOBAL_FORM_JSON;
			}

			break;
		case 1: // 表示查询所有的 传阅中

			if (orderBy == 2) { // 2 表示降序
				// 分页查询 传阅状态默认为 0 0 不做任何表示
				if (likeName.length() > 0 || !likeName.equals("") || startTime != null) {

					
					MailHelper helper = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus().Eq(mailStatus);
					
					//进行判断
					if(startTime != null) {
						//设置开始时间 和 结束时间 以及 日期转换的格式
						String timeStr = getDateTime();
						helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime()
						.Le(StrToDate(endTime, timeStr));
					}
					
					json = helper.startOr().getTitle().Like(likeName)
							.getAllReceiveName().Like(likeName).getLastName().Like(likeName).getMailContent()
							.Like(likeName).stopOr().getSendTime().Desc().json()
							.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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

				} else {
					json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0)
							.getStepStatus().Eq(mailStatus).getSendTime().Desc().json()
							.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
				}

				success = true;
				code = "200";
				msg = "查询已发传阅!";
			} else if (orderBy == 1) { // 1 表示升序

				if (likeName.length() > 0 || !likeName.equals("") || startTime != null) {
					
					
					MailHelper helper = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus().Eq(mailStatus);
					
					//进行判断
					if(startTime != null) {
						//设置开始时间 和 结束时间 以及 日期转换的格式
						String timeStr = getDateTime();
						helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime()
						.Le(StrToDate(endTime, timeStr));
					}
					
					// 分页查询 传阅状态默认为 0 0 不做任何表示
					json = helper.startOr().getTitle().Like(likeName)
							.getAllReceiveName().Like(likeName).getLastName().Like(likeName).getMailContent()
							.Like(likeName).stopOr().getSendTime().Asc().json()
							.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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

				} else {
					json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0)
							.getStepStatus().Eq(mailStatus).getSendTime().Asc().json()
							.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
				}
				success = true;
				code = "200";
				msg = "查询已发传阅!";
			}

			if (json != null) {
				return Results.GLOBAL_FORM_JSON;
			}

			break;
		case 3: // 表示查询所有的 已完成

			if (orderBy == 2) { // 2 表示降序

				if (likeName.length() > 0 || !likeName.equals("") || startTime != null) {

					
					MailHelper helper = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus().Eq(mailStatus);
					
					//进行判断
					if(startTime != null) {
						//设置开始时间 和 结束时间 以及 日期转换的格式
						String timeStr = getDateTime();
						helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime()
						.Le(StrToDate(endTime, timeStr));
					}
					
					json = helper.startOr().getTitle().Like(likeName)
							.getAllReceiveName().Like(likeName).getLastName().Like(likeName).getMailContent()
							.Like(likeName).stopOr().getSendTime().Desc().json()
							.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
				} else {

					json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0)
							.getStepStatus().Eq(mailStatus).getSendTime().Desc().json()
							.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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

				}
				// 分页查询 传阅状态默认为 0 0 不做任何表示

				success = true;
				code = "200";
				msg = "查询已发传阅!";
			} else if (orderBy == 1) { // 1 表示升序
				// 分页查询
				if (likeName.length() > 0 || !likeName.equals("") || startTime != null) {

					MailHelper helper = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus().Eq(mailStatus);
					
					//进行判断
					if(startTime != null) {
						
						//设置开始时间 和 结束时间 以及 日期转换的格式
						String timeStr = getDateTime();
						helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime()
						.Le(StrToDate(endTime, timeStr));
					}
					
					json = helper.startOr().getTitle().Like(likeName)
							.getAllReceiveName().Like(likeName).getLastName().Like(likeName).getMailContent()
							.Like(likeName).stopOr().getSendTime().Asc().json()
							.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
				} else {

					json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0)
							.getStepStatus().Eq(mailStatus).getSendTime().Asc().json()
							.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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

				}
				success = true;
				code = "200";
				msg = "查询已发传阅!";
			}

			if (json != null) {
				return Results.GLOBAL_FORM_JSON;
			}

			break;
		default:
			msg = "网络繁忙, 请稍后再试!";
			success = false;
			code = "404";
			return Results.GLOBAL_FORM_JSON;
		}

		msg = "查询失败";
		success = false;
		code = "404";
		return Results.GLOBAL_FORM_JSON;
	}

	/**
	 * 设置开始时间 和 结束时间 以及 日期转换的格式
	 * @return
	 */
	public String getDateTime() {
		
		String timeStr = "";
		
		if (startTime != null && endTime != null) {
			startTime += " 00:00:00";
			endTime += " 23:59:59";
			timeStr = "yyyy-MM-dd HH:mm:ss";
		}
		
		return timeStr;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public Date StrToDate(String str, String timeStr) {

		SimpleDateFormat format = new SimpleDateFormat(timeStr);

		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(int mailStatus) {
		this.mailStatus = mailStatus;
	}

	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageRows() {
		return pageRows;
	}

	public void setPageRows(Integer pageRows) {
		this.pageRows = pageRows;
	}
}