package com.sunnsoft.sloa.actions.web.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.helper.MailHelper;
import com.sunnsoft.sloa.helper.ReceiveHelper;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 收到传阅--传阅列表: 默认显示所有的已收传阅: 0 所有收到的传阅 1 传阅中 2 待办传阅 3 已完成 4 未读 5 已读
 *
 * @author chenjian
 *
 */
public class GridUnreadStateList extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 收件人Id(也是当前用户)
	private String likeName; // 客户端传过来的(用户输入的)
	private String startTime; // 开始时间
	private String endTime; // 结束时间
	private int receiveStatus; // 状态: 0 所有收到的传阅 1 传阅中 2 待办传阅 3 已完成
	private int status; // 状态: 0 全部 5 未读 6 已读 7 我的关注
	private Integer orderBy; // 排序

	private Integer page; // 当前页
	private Integer pageRows; // 每页几条记录数

	@Override
	public String execute() throws Exception {

		// 校验参数
//		Assert.notNull(receiveStatus, "传阅状态不能为空!");
		Assert.notNull(userId, "收件人ID不能为空!");

		/// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		// 给排序设置默认值 默认值为 2 默认降序
		this.orderBy = (orderBy == null) ? 2 : orderBy;

		// 给搜索条件设置默认值 默认值为 ""
		this.likeName = likeName.equals("") ? null : likeName;
		this.startTime = (startTime.equals("")) ? null : startTime;

		try {

			ReceiveHelper helper = Services.getReceiveService().createHelper().getUserId().Eq(userId);

			helper.enterMail().getEnabled().Eq(Boolean.FALSE).getStepStatus().In(1,3).ignoreEmptyValueCondiction()
					.startOr().getTitle().Like(likeName).getAllReceiveName().Like(likeName)
					.getLastName().Like(likeName).getMailContent().Like(likeName).stopOr()
					.back2Receives();


			helper.ignoreEmptyValueCondiction();

			if(status == 0){
				helper.getMailState().Ne(4);
			}else {
				if(status != 7){
					helper.getMailState().Eq(status);
				}
			}

			if(status == 7){
				helper.getReceiveAttention().Eq(Boolean.TRUE);
			}

			if(receiveStatus != 0){
				helper.getStepStatus().Eq(receiveStatus);
			}

			// 进行判断
			if (startTime != null) {
				// 设置开始时间 和 结束时间 以及 日期转换的格式
				String timeStr = getDateTime();
				helper.getReceiveTime().Ge(StrToDate(startTime, timeStr)).getReceiveTime()
						.Le(StrToDate(endTime, timeStr));
			}

			if(orderBy == 2){
				helper.getReceiveTime().Desc();
			}else {
				helper.getReceiveTime().Asc();
			}

			// 1. 查询该用户的已收传阅
//			json = Services.getReceiveService().createHelper().getUserId().Eq(userId)
//					.enterMail().getEnabled().Eq(Boolean.FALSE).getStepStatus().In(1,3).back2Receives()
//					.getReceiveTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Receive>() {
//
			json = helper.json().listPageJson(page, pageRows, new EachEntity2Map<Receive>() {
				@Override
				public void each(Receive receive, Map<String, Object> map) {
					map.put("receiveMailState", receive.getMailState());
					map.put("receiveStepStatus", receive.getStepStatus());

					Mail mail = receive.getMail();
					map.put("lastName", mail.getLastName());
					map.put("mailId", mail.getMailId());
					map.put("title", mail.getTitle());
					map.put("mailContent", mail.getMailContent());
					map.put("hasAttachment", mail.getHasAttachment());
					map.put("ruleName", mail.getRuleName());
					map.put("allReceiveName", mail.getAllReceiveName());
					map.put("ifAdd", mail.getIfAdd());
					map.put("ifImportant", mail.getIfImportant());
					map.put("ifNotify", mail.getIfNotify());
					map.put("ifRead", mail.getIfRead());
					map.put("ifRemind", mail.getIfRemind());
					map.put("ifRemindAll", mail.getIfRemindAll());
					map.put("ifSecrecy", mail.getIfSecrecy());
					map.put("ifSequence", mail.getIfSequence());
					map.put("ifUpdate", mail.getIfUpdate());
					map.put("ifUpload", mail.getIfUpload());

					List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
					List<Map<String, Object>> itemList = new ArrayList<>();
					for (AttachmentItem attachmentItem : attachmentItems) {
						Map<String, Object> itemMap = new HashMap<>();
						itemMap.put("itemId", attachmentItem.getItemId());
						itemMap.put("userId", attachmentItem.getUserId());
						itemMap.put("reator", attachmentItem.getCreator());
						itemMap.put("bulkId", attachmentItem.getBulkId());
						itemMap.put("createTime", attachmentItem.getCreateTime());
						itemMap.put("fileName", attachmentItem.getFileName());
						itemMap.put("fileCategory", attachmentItem.getFileCategory());
						itemMap.put("saveName", attachmentItem.getSaveName());
						itemMap.put("urlPath", attachmentItem.getUrlPath());
						itemMap.put("state", attachmentItem.getState());
						itemMap.put("itemSize", attachmentItem.getItemSize());
						itemMap.put("itemNeid", attachmentItem.getItemNeid());
						itemMap.put("itemRev", attachmentItem.getItemRev());
						itemList.add(itemMap);

					}
					map.put("attachmentItemss", itemList);
				}
			});

			success = true;
			code = "200";
			msg = "查询已收传阅";
			if (json != null) {
				return Results.GLOBAL_FORM_JSON;
			}

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			msg = "网络繁忙, 请稍后再试!";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		msg = "查询失败!";
		code = "404";
		json = "null";
		return Results.GLOBAL_FORM_JSON;

	}

	/**
	 * 设置开始时间 和 结束时间 以及 日期转换的格式
	 *
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

	public int getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(int receiveStatus) {
		this.receiveStatus = receiveStatus;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
