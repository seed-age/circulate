package com.sunnsoft.sloa.actions.web.homepage;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
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
 * 已发传阅: 点击查看跳转到已发传阅列表（传阅数据筛选为 传阅中 , 已完成 其中的任意一个)
 *
 * @author chenjian
 *
 */
public class GridSendInList extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 发件人Id
	private String likeName; // 客户端传过来的(用户输入的)
	private String startTime; // 开始时间
	private String endTime; // 结束时间
	private int mailStatus; // 传阅的流程状态 1 表示传阅中 3 表示已完成 (默认值为 0 表示所有)
	private Integer orderBy; // 排序

	private Integer page; // 当前页
	private Integer pageRows; // 显示几条记录

	@Override
	public String execute() throws Exception {

		// 参数校验
		Assert.notNull(userId, "发件人Id不能为空!");
		Assert.notNull(mailStatus, "传阅的流程状态不能为空!");

		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10

		/// 给排序设置默认值 默认值为 2 默认降序
		this.orderBy = (orderBy == null) ? 2 : orderBy;

		// 设置默认值, 如果搜索条件为null 默认值为 ""
		this.likeName = (likeName.equals("")) ? null : likeName;
		this.startTime = (startTime.equals("")) ? null : startTime;

		MailHelper helper = Services.getMailService().createHelper().getUserId().Eq(userId).ignoreEmptyValueCondiction();

		//进行判断
		if(startTime != null) {
			//设置开始时间 和 结束时间 以及 日期转换的格式
			String timeStr = getDateTime();
			helper.getSendTime().Ge(StrToDate(startTime, timeStr)).getSendTime()
					.Le(StrToDate(endTime, timeStr));
		}

		helper.startOr().getTitle().Like(likeName).getAllReceiveName()
				.Like(likeName).getLastName().Like(likeName).getMailContent().Like(likeName).stopOr();


		// 使用switch 选择查询数据
		switch (mailStatus) {
			case 0: // 表示查询所有的已发传阅 包含 传阅中 , 已完成

				helper.getStatus().Eq(mailStatus);

				break;
			case 1: // 表示查询所有的 传阅中

				helper.getStatus().Eq(0).getStepStatus().Eq(mailStatus);

				break;
			case 3: // 表示查询所有的 已完成

				helper.getStatus().Eq(0).getStepStatus().Eq(mailStatus);

				break;
			default:
				msg = "网络繁忙, 请稍后再试!";
				success = false;
				code = "404";
				return Results.GLOBAL_FORM_JSON;
		}

		if (orderBy == 2) { // 2 表示降序
			helper.getSendTime().Desc();
		}else { // 1 表示升序
			helper.getSendTime().Asc();
		}

		json = helper.json().listPageJson(page, pageRows, new EachEntity2Map<Mail>() {

			@Override
			public void each(Mail mail, Map<String, Object> map) {
				map.clear();

				map.put("mailId", mail.getMailId());
				map.put("userId", mail.getUserId());
				map.put("lastName", mail.getLastName());
				map.put("title", mail.getTitle());
				map.put("status", mail.getStatus());
				map.put("stepStatus", mail.getStepStatus());
				map.put("allReceiveName", mail.getAllReceiveName());
				map.put("hasAttachment", mail.getHasAttachment());
				map.put("attention", mail.getAttention());
				map.put("createTime", mail.getCreateTime());
				map.put("sendTime", mail.getSendTime());
				map.put("deleteTime", mail.getDeleteTime());
			}
		});


		msg = "查询成功";
		success = true;
		code = "200";
		return Results.GLOBAL_FORM_JSON;
	}

	/**
	 * 设置开始时间 和 结束时间 以及 日期转换的格式
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(int mailStatus) {
		this.mailStatus = mailStatus;
	}

	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
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
