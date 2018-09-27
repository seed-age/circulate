package com.sunnsoft.sloa.actions.app.index;

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
 * 传阅首页(APP端)--收到传阅: 点击跳转到收到传阅列表， 展示的数据通过 传递一个状态 决定是 所有的 传阅中 待办 已完成 的其中一个状态的数据列表
 * , 并通过传递一个排序参数来 进行决定是降序还是升序, 并且分页
 * 
 * @author chenjian
 *
 */
public class GridReceiveList extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 收件人ID
	private int status; // 状态: 1 传阅中 2 待办传阅 3 已完成 0 所有收到的传阅  5 表示 未读   6 已确认
	private Integer orderBy; // 排序
	private Integer page; // 当前页
	private Integer pageRows; // 当前页显示记录数

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "收件人ID不能为空!");
		Assert.notNull(status, "状态, 不能为空!");

		// 设置默认参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		// 默认排序 2 为降序
		this.orderBy = (orderBy == null) ? 2 : orderBy;

		/*if(status == 5) {
			//查询未读数据
			List<Receive> list = Services.getReceiveService().createHelper().getUserId().Eq(userId).getMailState().Eq(5).list();
			
			//如果为未读数据为 0 返回的数据为: 所有传阅
			if(list == null || list.size() == 0) {
				status = 0;
			}
		}*/
		
		// 选择状态
		switch (status) {
		case 0: // 表示 展示所有收到的传阅
			// 判断是升序还是降序
			if (orderBy == 2) { // 2 表示降序

				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).startOr().getStepStatus().Eq(1).getStepStatus().Eq(2).getStepStatus().Eq(3).stopOr()
						.back2Mail().getSendTime().Desc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}

							}
						});

				success = true;
				code = "200";
				msg = "对已收传阅的所有传阅 进行降序!";

			} else if (orderBy == 1) { // 1 表示升序

				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).startOr().getStepStatus().Eq(1).getStepStatus().Eq(2).getStepStatus().Eq(3).stopOr()
						.back2Mail().getSendTime().Asc().json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
							}
						});

				success = true;
				code = "200";
				msg = "对已收传阅的所有传阅 进行升序!";
			}

			break;

		case 1: // 1 传阅中

			// 分页查询
			if (orderBy == 2) { // 2 表示降序
				// 分页查询数据
				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).getStepStatus().Eq(1).back2Mail().getSendTime().Desc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
							}
						});

				success = true;
				code = "200";
				msg = "对已收传阅的  传阅中  进行降序!";

			} else if (orderBy == 1) { // 1 表示升序

				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).getStepStatus().Eq(1).back2Mail().getSendTime().Asc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
								
							}
						});

				success = true;
				code = "200";
				msg = "对已收传阅的  传阅中  进行升序!";
			}

			break;

		case 2: // 待办传阅

			if (orderBy == 2) { // 2 表示降序
				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).getStepStatus().Eq(2).back2Mail().getSendTime().Desc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
							}
						});

				success = true;
				code = "200";
				msg = "对已收传阅的  待办传阅  进行降序!";

			} else if (orderBy == 1) { // 1 表示升序
				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).getStepStatus().Eq(2).back2Mail().getSendTime().Asc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
							}
						});

				success = true;
				code = "200";
				msg = "对已收传阅的  待办传阅  进行升序!";
			}

			break;

		case 3: // 已完成

			// 分页查询数据
			if (orderBy == 2) { // 2 表示降序

				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).getStepStatus().Eq(3).back2Mail().getSendTime().Desc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
							}
						});

				success = true;
				code = "200";
				msg = "对已收传阅的  已完成  进行降序!";

			} else if (orderBy == 1) { // 1 表示升序

				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).getStepStatus().Eq(3).back2Mail().getSendTime().Asc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}

							}
						});

				success = true;
				code = "200";
				msg = "对已收传阅的  已完成  进行升序!";
			}

			break;

		case 5: // 未读
			if (orderBy == 2) { // 2 表示降序
				// 分页查询数据
				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives()
						.getUserId().Eq(userId).getMailState().Eq(5).back2Mail().getSendTime().Desc().json()
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
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
							}
						});

				success = true;
				code = "200";
				msg = "查询已收传阅! desc";

			} else if (orderBy == 1) { // 1 表示升序
				// 分页查询数据
				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives()
						.getUserId().Eq(userId).getMailState().Eq(5).back2Mail().getSendTime().Asc().json()
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
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
							}
						});

				success = true;
				code = "200";
				msg = "查询已收传阅! asc";
			}

			break;
			
		case 6: //已确认
			// 分页查询
			if (orderBy == 2) { // 2 表示降序
				// 分页查询数据
				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).startOr().getStepStatus().Eq(1).getStepStatus().Eq(3).stopOr().back2Mail().getSendTime().Desc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
							}
						});
	
				success = true;
				code = "200";
				msg = "对已收传阅的  传阅中  进行降序!";
	
			} else if (orderBy == 1) { // 1 表示升序
	
				json = Services.getMailService().createHelper().ignoreEmptyValueCondiction().enterReceives().getUserId()
						.Eq(userId).startOr().getStepStatus().Eq(1).getStepStatus().Eq(3).stopOr().back2Mail().getSendTime().Asc().json()
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
									if(receive.getUserId() == userId) {
										map.put("receiveStepStatus", receive.getStepStatus());
										map.put("receiveMailState", receive.getMailState());
										map.put("receiveAttention", receive.getReceiveAttention());
										map.put("receiveTime", receive.getReceiveTime());
										map.put("receiveJoinTime", receive.getJoinTime());
									}
								}
								
							}
						});
	
				success = true;
				code = "200";
				msg = "对已收传阅的  传阅中  进行升序!";
			}
						
			break;
		default:
			success = false;
			msg = "没有你要查询的数据..";
			code = "405";
			json = "null";
			break;
		}

		try {
			if (json != null) {
				return Results.GLOBAL_FORM_JSON;
			} else {
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
