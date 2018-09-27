package com.sunnsoft.sloa.actions.web.send;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 已发传阅:传阅关键词查询，全局模糊查询，包括主题、接收人，发起人、和传阅详情内容
 * 
 * @author chenjian
 *
 */
public class FindSendLike extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 发件人Id(也是当前用户)
	private String likeName; // 客户端传过来的(用户输入的)
	private int status; // 0 无状态 1 传阅中 3 已完成 (默认为 0)

	private Integer page; // 当前页
	private Integer pageRows; // 每页几条记录数

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(likeName, "查询条件不能为空!");
		Assert.notNull(userId, "收件人ID不能为空!");

		if(likeName.equals("")) {
			success = false;
			msg = "搜索条件不能为空!";
			code = "205";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		
		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		// 判断
		if (status == 0) { // 无状态 查询出所有
			// 分页查询
			json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(status).startOr()
					.getTitle().Like(likeName).getAllReceiveName().Like(likeName).getLastName().Like(likeName)
					.getMailContent().Like(likeName).stopOr().getSendTime().Desc().json()
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
								receivess.setReDifferentiate(receive.getReDifferentiate()); // 区别是谁添加的联系人, 存放添加该联系人的用户ID
								receiveList.add(receivess);
							}

							map.put("receivess", receiveList);
						}
					});
		} else { // 1 传阅中 3 已完成 
			
			// 分页查询
			json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus().Eq(status).startOr()
					.getTitle().Like(likeName).getAllReceiveName().Like(likeName).getLastName().Like(likeName)
					.getMailContent().Like(likeName).stopOr().getSendTime().Desc().json()
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
								receivess.setReDifferentiate(receive.getReDifferentiate()); // 区别是谁添加的联系人, 存放添加该联系人的用户ID
								receiveList.add(receivess);
							}

							map.put("receivess", receiveList);
						}
					});

		}

		if (json != null) {
			success = true;
			msg = "查询成功!";
			code = "200";
			return Results.GLOBAL_FORM_JSON;

		} else {
			success = false;
			msg = "网络繁忙, 请稍后再试!";
			code = "404";
			return Results.GLOBAL_FORM_JSON;
		}

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
