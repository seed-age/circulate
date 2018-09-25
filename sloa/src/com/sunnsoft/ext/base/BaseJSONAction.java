package com.sunnsoft.ext.base;

import java.lang.reflect.ParameterizedType;

import com.opensymphony.xwork2.ActionSupport;
import org.gteam.constants.SystemConstants;
import org.gteam.util.JSONUtils;
import org.gteam.util.JSONUtils.JsonHelper;

@SuppressWarnings("unchecked")
public class BaseJSONAction<T> extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7014149503508930061L;

	/**
	 * 存放被转换的JSON字符串
	 */
	protected String json;
	/**
	 * 状态消息
	 */
	protected String msg;
	/**
	 * 存放将被转换为JSON字符串的对象
	 */
	protected Object jsonResultObject;
	/**
	 * 根据泛型获得泛型的Class对象;
	 */
	protected Class<T> entityClass;
	/**
	 * 默认是yyyy-MM-dd HH:mm:ss;
	 */
	protected String dateFormat = SystemConstants.STANDARD_DATE_TIME_FORMAT;
	/**
	 * 自定义的JsonHelper，默认为空，如果设置了它，则使用此JsonHelper来序列化jsonResultObject对象
	 */
	protected JsonHelper jsonHelper;
	/**
	 * 设置是否作为array来转换JSON
	 */
	protected boolean jsonArray;
	

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String execute() throws Exception {
		if(jsonArray){
			json = jsonHelper != null ? jsonHelper.toJSONArray(jsonResultObject).toString() : JSONUtils.getJsonHelper().setDateFormat(
					this.dateFormat)
					.excludeForeignObject(entityClass).toJSONArray(jsonResultObject).toString();
		}else{
			json = jsonHelper != null ? jsonHelper.toJSONObject(jsonResultObject).toString() : JSONUtils.getJsonHelper().setDateFormat(
					this.dateFormat)
					.excludeForeignObject(entityClass).toJSONObject(jsonResultObject).toString();
		}
		return super.execute();
	}

	public BaseJSONAction() {
		super();
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
	}

	
}
