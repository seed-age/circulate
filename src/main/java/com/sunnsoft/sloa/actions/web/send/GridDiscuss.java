package com.sunnsoft.sloa.actions.web.send;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Discuss;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.util.Map;
/**
 * 已发传阅--评论：点击评论icon，跳转评论列表页面，查看评论.(分页)
 * @author chenjian
 *
 */
public class GridDiscuss extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long mailId; //传阅ID
	
	private Integer page; // 当前页
	private Integer pageRows; // 每页几条记录数
	
	@Override
	public String execute() throws Exception {

		//校验参数
		Assert.notNull(mailId, "传阅ID不能为空!!");
		
		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10
		
		// 查询出对应的传阅记录
		Mail mail = Services.getMailService().findById(mailId);
				
		// 查询评论列表内容
		json = Services.getDiscussService().createHelper().getMail().Eq(mail)
					.getCreateTime().Desc().json().listPageJson(page, pageRows, new EachEntity2Map<Discuss>() {

						@Override
						public void each(Discuss discuss, Map<String, Object> map) {
								
						}
					});
		
		if(json != null) {
			success = true;
			code = "200";
			msg = "查询评论列表成功!";
			return Results.GLOBAL_FORM_JSON;
			
		}else {
			success = false;
			code = "404";
			msg = "查询评论列表失败!";
			return Results.GLOBAL_FORM_JSON;
		}
	}

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
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
