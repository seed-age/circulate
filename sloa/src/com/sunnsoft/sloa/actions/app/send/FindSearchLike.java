package com.sunnsoft.sloa.actions.app.send;

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
import java.util.*;

/**
 * 搜索：对已发传阅信息进行搜索(接收人 , 传阅主题 , 传阅内容 , 发件人姓名)
 * 
 * @author chenjian
 *
 */
public class FindSearchLike extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 当前用户ID
	private String likeName; // 模糊搜索(用户输入的)
	//private String startTime; // 开始时间
	//private String endTime; // 结束时间

	private Integer page; // 当前页
	private Integer pageRows; // 每页几条记录数

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!!");

		// 搜索条件不能为空或是空串
		/*if (likeName == null || likeName.equals("")) {
			success = false;
			msg = "搜索条件不能为空!";
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}*/
		
		if(likeName != null) {
			if(likeName.equals("")) {
				likeName = null;
			}
		}

		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		// 设置默认值, 如果搜索条件为null 默认值为 ""
		//this.startTime = (startTime.equals("")) ? null : startTime;

		MailHelper helper = Services.getMailService().createHelper().getUserId().Eq(userId).getStatus().Eq(0);

		// 进行判断
		/*if (startTime != null) {
			// 设置开始时间 和 结束时间 以及 日期转换的格式
			String timeStr = getDateTime();
			helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime().Le(StrToDate(endTime, timeStr));
		}*/

		// 模糊查询
		json = helper.ignoreEmptyValueCondiction().startOr().getAllReceiveName().Like(likeName).getTitle().Like(likeName).getMailContent()
				.Like(likeName).getLastName().Like(likeName).stopOr().getSendTime().Desc().json()
				.listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

					@Override
					public void each(Mail mail, Map<String, Object> map) {
						List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
						List<Map<String, Object>> itemList = new ArrayList<>();
						for (AttachmentItem attachmentItem : attachmentItems) {
							// 去除关联的mail数据
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

		if (json != null) {
			success = true;
			msg = "搜索成功--desc";
			code = "200";
			return Results.GLOBAL_FORM_JSON;
		} else {
			success = false;
			msg = "没有搜索到符合条件的内容!";
			code = "404";
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
