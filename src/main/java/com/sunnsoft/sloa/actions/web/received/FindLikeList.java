package com.sunnsoft.sloa.actions.web.received;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		if (likeName == null) {
			success = false;
			msg = "搜索条件不能为空!";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		// 判断
		switch (status) {
		case 0: // 0 无状态 表示查询所有 (默认为 0)
			// 分页查询
			json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
					.Eq(userId).back2Mail().startOr().getTitle().Like(likeName).getAllReceiveName().Like(likeName)
					.getLastName().Like(likeName).getMailContent().Like(likeName).stopOr().getSendTime().Desc().json()
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

			if (json != null) {
				success = true;
				msg = "查询成功!";
				code = "200";
				return Results.GLOBAL_FORM_JSON;
			}

			break;

		case 1: // 1 传阅中

			json = getReceiveList();

			if (json != null) {
				success = true;
				msg = "查询成功!";
				code = "200";
				return Results.GLOBAL_FORM_JSON;
			}

			break;
		case 2: // 2 待办传阅

			json = getReceiveList();

			if (json != null) {
				success = true;
				msg = "查询成功!";
				code = "200";
				return Results.GLOBAL_FORM_JSON;
			}
			break;

		case 3: // 3 已完成

			json = getReceiveList();

			if (json != null) {
				success = true;
				msg = "查询成功!";
				code = "200";
				return Results.GLOBAL_FORM_JSON;
			}

			break;

		case 4: // 4 未读

			json = getMailStatus();

			if (json != null) {
				success = true;
				msg = "查询成功!";
				code = "200";
				return Results.GLOBAL_FORM_JSON;
			}

			break;

		case 5: // 5 已读

			json = getMailStatus();

			if (json != null) {
				success = true;
				msg = "查询成功!";
				code = "200";
				return Results.GLOBAL_FORM_JSON;
			}

			break;

		case 6: // 6 我的关注
			
			Boolean attention = true;
			
			// 分页查询
			json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
					.Eq(userId).getReceiveAttention().Eq(attention).back2Mail().startOr().getTitle().Like(likeName)
					.getAllReceiveName().Like(likeName).getLastName().Like(likeName).getMailContent().Like(likeName)
					.stopOr().getSendTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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

			break;

		default:
			break;
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

	public String getMailStatus() {
		// 分页查询
		json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
				.Eq(userId).getMailState().Eq(status).back2Mail().startOr().getTitle().Like(likeName)
				.getAllReceiveName().Like(likeName).getLastName().Like(likeName).getMailContent().Like(likeName)
				.stopOr().getSendTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
		// 分页查询
		json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
				.Eq(userId).getStepStatus().Eq(status).back2Mail().startOr().getTitle().Like(likeName)
				.getAllReceiveName().Like(likeName).getLastName().Like(likeName).getMailContent().Like(likeName)
				.stopOr().getSendTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
