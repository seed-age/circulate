package com.sunnsoft.sloa.actions.app.wait;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;

/**
 * 待发传阅 (PC/APP)-- 列表: 展示所有待发传阅记录 (默认降序)
 * 
 * @author chenjian
 *
 */
public class GridWaitSendList extends BaseParameter {

	private static final long serialVersionUID = 1L;

	/**
	 * Name:发件人id Comment:发件人id
	 */
	private Long userId;
	private Integer orderBy; // 排序

	private Integer page; // 当前页
	private Integer pageRows; // 每页几条记录数

	@Override
	public String execute() throws Exception {

		// 校验数据
		Assert.notNull(userId, "当前用户ID不能为空");

		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		// 给排序设置默认值 默认值为 2 默认降序
		this.orderBy = (orderBy == null) ? 2 : orderBy;

		if (orderBy == 2) { // 2 表示降序
			// 查询数据
			json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(1).getCreateTime()
					.Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
								// 把mail设置null
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
							
							String string = stripHtml(mail.getMailContent());
							//System.out.println("转义之后: " + string);
							mail.setMailContent(string); 
							map.put("mailContent", string);
							
						}
					});
			
			success = true;
			code = "200";
			msg = "查询待发传阅! desc";
			
		} else if (orderBy == 1) { // 1 表示升序
			
			json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(1).getCreateTime().Asc()
					.json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
								// 把mail设置null
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
							
							map.put("receivess", receiveList);
							String string = stripHtml(mail.getMailContent());
							System.out.println("列表, 转义之后: " + string);
							mail.setMailContent(string);
						}
					});
			
			success = true;
			code = "200";
			msg = "查询待发传阅! asc";
		}
		if (json != null) {
			return Results.GLOBAL_FORM_JSON;
		} else {
			success = false;
			msg = "网络繁忙!";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

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
