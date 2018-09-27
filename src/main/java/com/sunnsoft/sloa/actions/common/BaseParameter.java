package com.sunnsoft.sloa.actions.common;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @description:Action基类;
 * @author:liujun
 * @date 2017年8月9日下午2:54:07
 */
public class BaseParameter extends ActionSupport 
{
	private static final long serialVersionUID = 1L;
	/*// 请求参数;
	public Integer page;       // 页码(分页查询接口使用);
	public Integer pageSize;   // 每页显示数据条数(分页查询接口使用);
*/	
	// 接口返回数据;
	public Boolean success;    // 接口请求状态;
	public String msg;         // 提示信息;
	public String code;        // 接口返回编码,针对部分接口可能需要额外提示信息使用;   
	public String json;        // 接口返回数据;
	
	public String getMsg() 
	{
		return msg;
	}
	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	
	public String getJson() 
	{
		return json;
	}
	
	public void setJson(String json) 
	{
		this.json = json;
	}

	public Boolean getSuccess()
	{
		return success;
	}

	public void setSuccess(Boolean success)
	{
		this.success = success;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	/*public Integer getPage()
	{
		return page;
	}

	public void setPage(Integer page)
	{
		this.page = page;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}*/
	
}
