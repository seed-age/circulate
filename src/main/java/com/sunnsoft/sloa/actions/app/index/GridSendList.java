package com.sunnsoft.sloa.actions.app.index;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 传阅首页(APP端)--已发传阅: 点击页面跳转到已发传阅 默认选中所有传阅列表，该列表的数据不做任何筛选(就是传阅中和已完成) status
 * 通过这个参数来决定用户需要那种状态的传阅数据 (默认所有) orderBy 通过这个参数来决定用户选择升序还是降序 (默认降序)
 * 
 * @author chenjian
 *
 */
public class GridSendList extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private long userId; // 发件人ID
	private int status; // 页面传递过来的传阅流程状态   0 表示所有   1 传阅中    3已完成  (默认为 0)
	private int orderBy; // 排序 1 为 升序 2为降序
	private Integer page; // 当前页
	private Integer pageRows; // 当前页显示记录数

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "收件人ID不能为空!");

		// 设置默认参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		// 给排序设置默认值 默认值为 2 默认降序
		this.orderBy = (orderBy == 0) ? 2 : orderBy;

		// 使用switch
		switch (status) {
		case 0: // 表示所有

			// 判断是升序还是降序
			if (orderBy == 2) { // 2 表示降序
				// 查询数据
				json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).startOr()
						.getStepStatus().Eq(1).getStepStatus().Eq(3).stopOr().getSendTime().Desc().json()
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
							}
						});

				success = true;
				code = "200";
				msg = "查询已发传阅..所有..desc";

			} else if (orderBy == 1) { // 1 表示升序
				// 查询数据
				json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).startOr()
						.getStepStatus().Eq(1).getStepStatus().Eq(3).stopOr().getSendTime().Asc().json()
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
							}
						});

				success = true;
				code = "200";
				msg = "查询已发传阅..所有..asc";
			}

			break;

		case 1: // 传阅中

			// 判断是升序还是降序
			if (orderBy == 2) { // 2 表示降序

				json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus()
						.Eq(1).getSendTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
							}
						});

				success = true;
				code = "200";
				msg = "查询已发传阅..传阅中..desc";

			} else if (orderBy == 1) { // 1 表示升序
				json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus()
						.Eq(1).getSendTime().Asc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
							}
						});

				success = true;
				code = "200";
				msg = "查询已发传阅..传阅中..asc";
			}
			break;

		case 3: // 完成

			// 判断是升序还是降序
			if (orderBy == 2) { // 2 表示降序
				json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus()
						.Eq(3).getSendTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
							}
						});

				success = true;
				code = "200";
				msg = "查询已发传阅..已完成..desc";

			} else if (orderBy == 1) { // 1 表示升序

				json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0).getStepStatus()
						.Eq(3).getSendTime().Asc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
							}
						});

				success = true;
				code = "200";
				msg = "查询已发传阅..已完成..asc";
			}
			break;

		default:
			success = false;
			code = "405";
			msg = "没有你要查询的数据!";
			json = "null";
			break;
		}

		if (json != null) {
			return Results.GLOBAL_FORM_JSON;
		} else {
			success = false;
			msg = "发生错误..";
			code = "404";
			return Results.GLOBAL_FORM_JSON;
		}
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
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
