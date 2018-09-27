package com.sunnsoft.sloa.actions.web.homepage;

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
 * 传阅一览--已删除: 点击跳转到已删除列表。(默认降序)
 * 
 * @author chenjian
 *
 */
public class GridDeleteList extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 发件人Id
	private Integer orderBy; // 排序

	private Integer page; // 当前页
	private Integer pageRows; // 显示几条记录

	@Override
	public String execute() throws Exception {

		// 参数校验
		Assert.notNull(userId, "发件人Id不能为空!");

		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10
		
		// 给排序设置默认值 默认值为 2 默认降序
		this.orderBy = (orderBy == null) ? 2 : orderBy;

		if (orderBy == 2) { // 2 表示降序
			json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(7).getDeleteTime()
					.Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
			msg = "查询已删除传阅! desc";
		} else if (orderBy == 1) { // 1 表示升序
			
			json = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(7).getDeleteTime()
					.Asc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
			msg = "查询已删除传阅! asc";
		}
		
		if (json != null) {
			return Results.GLOBAL_FORM_JSON;
		} else {
			success = false;
			msg = "网络繁忙, 请稍后再试!";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;

		}

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
