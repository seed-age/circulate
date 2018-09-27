package com.sunnsoft.sloa.actions.app.alreadydelete;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.helper.MailHelper;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 已删除:传阅关键词查询，全局模糊查询，包括主题、接收人，发起人、和传阅详情内容
 * 
 * @author chenjian
 *
 */
public class FindLikeDelete extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long userId; // 发件人Id(也是当前用户)
	private String likeName; // 客户端传过来的(用户输入的)
	//private String startTime; // 开始时间
	//private String endTime; // 结束时间

	private Integer page; // 当前页
	private Integer pageRows; // 每页几条记录数

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "发件人ID不能为空!");
		//Assert.notNull(likeName, "收件人ID不能为空!");

		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		// 设置默认值, 如果搜索条件为null 默认值为 ""
		//this.startTime = (startTime.equals("")) ? null : startTime;

		MailHelper helper = Services.getMailService().createHelper().getStatus().Eq(7).getUserId().Eq(userId);

		// 进行判断
		/*if (startTime != null) {
			// 设置开始时间 和 结束时间 以及 日期转换的格式
			String timeStr = getDateTime();
			helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime().Le(StrToDate(endTime, timeStr));
		}*/
		if(likeName != null) {
			if(likeName.equals("")) {
				likeName = null;
			}
		}
		// 分页查询
		json = helper.ignoreEmptyValueCondiction().startOr().getTitle().Like(likeName).getAllReceiveName().Like(likeName).getLastName()
				.Like(likeName).getMailContent().Like(likeName).stopOr().getSendTime().Desc().json()
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
					}
				});

		if (json != null) {
			success = true;
			msg = "查询成功!";
			code = "200";
			return Results.GLOBAL_FORM_JSON;

		} else {
			success = false;
			code = "404";
			msg = "网络繁忙, 请稍后再试!";
			return Results.GLOBAL_FORM_JSON;
		}

	}

	/**
	 * 设置开始时间 和 结束时间 以及 日期转换的格式
	 * 
	 * @return
	 */
	public String getDateTime() {

		String timeStr = "";

		/*if (startTime != null && endTime != null) {
			startTime += " 00:00:00";
			endTime += " 23:59:59";
			timeStr = "yyyy-MM-dd HH:mm:ss";
		}*/

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

	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}

	/*public String getStartTime() {
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
	}*/

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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
