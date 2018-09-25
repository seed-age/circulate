package com.sunnsoft.sloa.actions.system.users;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.sloa.util.SysLogger;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

import org.gteam.db.helper.hibernate.Each;
import org.gteam.util.FastJSONUtils;

public class DeleteBeans extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private UserService userService;
	

	private SysLogger sysLogger;
	
	private List<Long> ids;
	
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	private String msg;
	
	public SysLogger getSysLogger() {
		return sysLogger;
	}
	public void setSysLogger(SysLogger sysLogger) {
		this.sysLogger = sysLogger;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Override
	public String execute() throws Exception {
		if(!UserUtils.getCurrentUser().isAdmin()){
			this.msg = "当前用户不是管理员，不能删除账户";
			return Results.GLOBAL_FAILURE_JSON;
		}
		if(ids != null && ! ids.isEmpty()){
			userService.createHelper().getUserId().In(ids).each(new Each<User>(){

				@Override
				public void each(User bean, List<User> beans) {
					String beforeDeleteJson = FastJSONUtils.getJsonHelper().excludeForeignObject(User.class).toJSONString(bean);
					sysLogger.log("删除账户",bean.getNickName(),"账户详情:",beforeDeleteJson);
				}
				
			});
			userService.deleteByIdList(ids);
		}
		return Results.GLOBAL_SUCCESS_JSON;
	}
	
	

}
