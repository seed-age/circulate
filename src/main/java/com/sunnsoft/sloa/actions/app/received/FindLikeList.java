package com.sunnsoft.sloa.actions.app.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.helper.ReceiveHelper;
import com.sunnsoft.sloa.util.ConstantUtils;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 收到传阅(PC端/APP端)---搜索:传阅关键词查询，全局模糊查询，包括主题、接收人，发起人、和传阅详情内容
 *
 * @author chenjian
 *
 */
public class FindLikeList extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long userId; // 收件人Id(也是当前用户)
	private String likeName; // 客户端传过来的(用户输入的)
	private int status; // 0 无状态 1 传阅中 2 待办传阅 3 已完成 4 未读 5 已读 6 我的关注 (默认为 0)

	private Integer page; // 当前页
	private Integer pageRows; // 每页几条记录数

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "收件人ID不能为空!");
		Assert.notNull(status, "状态, 不能为空!");

		// 设置默认参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		ReceiveHelper helper = Services.getReceiveService().createHelper().getUserId().Eq(userId);

		// 选择状态
		switch (status) {
			case 0: // 表示 展示所有收到的传阅
				helper.getMailState().Ne(ConstantUtils.RECEIVE_WAIT_STATUS);

				helper.enterMail().getEnabled().Eq(Boolean.FALSE).getStepStatus().In(1,3).ignoreEmptyValueCondiction()
						.startOr().getTitle().Like(likeName).getAllReceiveName().Like(likeName)
						.getLastName().Like(likeName).getMailContent().Like(likeName).stopOr()
						.back2Receives();

				break;

			case 1: // 1 传阅中

				helper.getStepStatus().Eq(ConstantUtils.MAIL_HALFWAY_STATUS);

				break;

			case 2: // 待办传阅

				helper.getStepStatus().Eq(ConstantUtils.RECEIVE_AWAIT_STATUS);

				break;

			case 3: // 已完成

				helper.getStepStatus().Eq(ConstantUtils.MAIL_COMPLETE_STATUS);

				break;

			case 5: // 未读

				helper.getMailState().Eq(ConstantUtils.RECEIVE_UNREAD_STATUS);

				break;

			case 6: //已确认

				helper.getStepStatus().Eq(ConstantUtils.MAIL_HALFWAY_STATUS);

				break;
		}


		json = helper.json().listPageJson(page, pageRows, new EachEntity2Map<Receive>() {
			@Override
			public void each(Receive receive, Map<String, Object> map) {
				map.clear();

				map.put("receiveStepStatus", receive.getStepStatus());
				map.put("receiveMailState", receive.getMailState());
				map.put("receiveAttention", receive.getReceiveAttention());
				map.put("receiveTime", receive.getReceiveTime());
				map.put("receiveJoinTime", receive.getJoinTime());

				Mail mail = receive.getMail();
				map.put("mailId", mail.getMailId());
				map.put("userId", mail.getUserId());
				map.put("lastName", mail.getLastName());
				map.put("loginId", mail.getLoginId());
				map.put("subcompanyName", mail.getSubcompanyName());
				map.put("departmentName", mail.getDepartmentName());
				map.put("allReceiveName", mail.getAllReceiveName());
				map.put("title", mail.getTitle());
				map.put("mailContent", mail.getMailContent());
				map.put("createTime", mail.getCreateTime());
				map.put("sendTime", mail.getSendTime());
				map.put("status", mail.getStatus());
				map.put("stepStatus", mail.getStepStatus());
				map.put("completeTime", mail.getCompleteTime());
				map.put("hasAttachment", mail.getHasAttachment());
				map.put("enabled", mail.isEnabled());
				map.put("attention", mail.getAttention());
				map.put("ruleName", mail.getRuleName());
				map.put("deleteTime", mail.getDeleteTime());
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

		try {
			if (json != null) {
				success = true;
				msg = "查询收到传阅成功";
				code = "200";
				return Results.GLOBAL_FORM_JSON;
			} else {
				success = true;
				msg = "查询收到传阅成功";
				code = "200";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e) {
			success = false;
			msg = "网络繁忙!";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
	}

	/**
	 * 设置开始时间 和 结束时间 以及 日期转换的格式
	 *
	 * @return

	public String getDateTime() {

	String timeStr = "";

	if (startTime != null && endTime != null) {
	startTime += " 00:00:00";
	endTime += " 23:59:59";
	timeStr = "yyyy-MM-dd HH:mm:ss";
	}

	return timeStr;
	}*/

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

	public String getMailStatus() {

		ReceiveHelper helper = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives()
				.getUserId().Eq(userId).getMailState().Eq(status);

		/*// 进行判断
		if (startTime != null) {
			// 设置开始时间 和 结束时间 以及 日期转换的格式
			String timeStr = getDateTime();
			helper.getReceiveTime().Ge(StrToDate(startTime, timeStr)).getReceiveTime().Le(StrToDate(endTime, timeStr));
		}*/

		// 分页查询
		json = helper.getReceiveTime().Desc().back2Mail().startOr().getTitle().Like(likeName)
				.getAllReceiveName().Like(likeName).getLastName().Like(likeName).getMailContent().Like(likeName)
				.stopOr().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

					@Override
					public void each(Mail mail, Map<String, Object> map) {
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

						List<Receive> receives = mail.getReceives();
						for (Receive receive : receives) {
							if (receive.getUserId() == userId) {
								map.put("receiveStepStatus", receive.getStepStatus());
								map.put("receiveMailState", receive.getMailState());
								map.put("receiveAttention", receive.getReceiveAttention());
							}
						}

					}
				});
		return json;
	}

	public String getReceiveList() {

		ReceiveHelper helper = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives()
				.getUserId().Eq(userId).getStepStatus().Eq(status);

		// 进行判断
		/*if (startTime != null) {
			// 设置开始时间 和 结束时间 以及 日期转换的格式
			String timeStr = getDateTime();
			helper.getReceiveTime().Ge(StrToDate(startTime, timeStr)).getReceiveTime().Le(StrToDate(endTime, timeStr));
		}*/

		// 分页查询
		json = helper.getReceiveTime().Desc().back2Mail().startOr().getTitle().Like(likeName).getAllReceiveName()
				.Like(likeName).getLastName().Like(likeName).getMailContent().Like(likeName).stopOr().json()
				.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

					@Override
					public void each(Mail mail, Map<String, Object> map) {
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

						List<Receive> receives = mail.getReceives();
						for (Receive receive : receives) {
							if (receive.getUserId() == userId) {
								map.put("receiveStepStatus", receive.getStepStatus());
								map.put("receiveMailState", receive.getMailState());
								map.put("receiveAttention", receive.getReceiveAttention());
							}
						}

					}
				});

		return json;
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
