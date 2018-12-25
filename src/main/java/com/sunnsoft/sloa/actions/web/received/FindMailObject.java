package com.sunnsoft.sloa.actions.web.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.util.PageUtil;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * (web端)已收/已发---查询该传阅的	传阅对象列表! 没有分页
 *
 * @author chenjian
 *
 */
public class FindMailObject extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long mailId; // 传阅ID
	private Long userId; // 用户ID
	private Integer page;
	private Integer pageRows;



	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(mailId, "传阅ID不能为空!!");
		Assert.notNull(userId, "用户ID不能为空!!");

		// 默认参数
		page = PageUtils.defaultPageNumber(page);
		pageRows = PageUtils.defaultPageSize(pageRows);

		// 根据ID查询
		final Mail mail = Services.getMailService().findById(mailId);

		json = Services.getReceiveService().createHelper().getMail().Eq(mail).json().listPageJson(page, pageRows, new EachEntity2Map<Receive>() {

			@Override
			public void each(Receive receive, Map<String, Object> map) {
				int mailStatusss = 0;
				//判断
				if(receive.getMailState() == 5) {
					//如果是未读
					mailStatusss = 0;
				}

				if(receive.getMailState() == 6 && receive.getIfConfirm() == false) {
					//如果是 已读  但是又没有确认   就是等待确认
					mailStatusss = 1;
				}

				if(receive.getIfConfirm() == true) {
					//如果是确认  就是 2
					mailStatusss = 2 ;
				}

				map.put("mailStatusss", mailStatusss);

				if(receive.getAfreshRemark() != null) {
					//获取第一次确认的信息标识
					String confirmRecord = receive.getConfirmRecord();
					//获取第 N 次 重新确认的标识
					String acRecord = receive.getAcRecord();
					map.put("acRecord", confirmRecord + "<br/>" + acRecord);
				}

				map.put("sendTime",mail.getSendTime());

				//删除传阅对象, 有两种情况 :  一. 是发件人可以删除所有的传阅对象  ,  二是 谁添加该传阅对象  谁才有权限删除
				if(receive.getReDifferentiate() != null){
					Long reDifferentiate = receive.getReDifferentiate();
					if(reDifferentiate.equals(userId) || mail.getUserId() == userId) {
						map.put("authority", Boolean.TRUE);
					}else {
						map.put("authority", Boolean.FALSE);
					}
				}else {
					map.put("authority", Boolean.FALSE);
				}

			}
		});

		if(json != null) {
			success = true;
			code = "200";
			msg = "查询传阅对象成功!";
			return Results.GLOBAL_FORM_JSON;
		}else {
			success = false;
			code = "404";
			msg = "查询传阅对象失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
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

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
