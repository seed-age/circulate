package com.sunnsoft.sloa.actions.web.received;

import java.util.Map;

import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;

/**
 * (web端)已收/已发---查询该传阅的	传阅对象列表! 没有分页
 * 
 * @author chenjian
 *
 */
public class FindMailObject extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long mailId; // 传阅ID

	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(mailId, "传阅ID不能为空!!");

		// 根据ID查询
		Mail mail = Services.getMailService().findById(mailId);
		
		json = Services.getReceiveService().createHelper().getMail().Eq(mail).json().listJson(new EachEntity2Map<Receive>() {

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

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}
	
	public static void main(String[] args) {
		String ss = "啦啦啦啦 确认(2018-03-19 11:52:01)";
		//System.out.println(ss);
		String s2 = "大叔大婶 重新确认(2018-03-25 13:52:01)";
		//System.out.println(s2);
		String s3 = "呵呵哒 重新确认(2018-03-27 15:52:01)";
		//System.out.println(ss + "\n" + s2 + "\n" + s3);
		
		String s4 = ss + "\n" + s2 + "\n" + s3;
		System.out.println(s4);
	}
}
