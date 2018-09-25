package com.sunnsoft.util.struts2;

public class Results {
	/**
	 * "success" struts结果字符串
	 */
	public static final String SUCCESS = "success";
	/**
	 * "failure" struts结果字符串
	 */
	public static final String FAILURE = "failure";
	
	
	/**
	 * 平台定义的全局result successJson结果字符串，详见：/WEB-INF/commons/successJson.jsp
	 * 支持action成员变量msg显示。
	 * JSP内容如下JSON形式：
	 * {
	 *		"success":true,
	 *		"msg":"${msg}"
	 * }
	 */
	public static final String GLOBAL_SUCCESS_JSON = "successJson";
	
	
	/**
	 * 平台定义的全局result failureJson结果字符串，详见：/WEB-INF/commons/failureJson.jsp
	 * 支持action成员变量msg显示，主要是用于配合extjs或web前端数据显示使用。
	 * JSP内容如下JSON形式：
	 * {
	 *		"success":false,
	 *		"msg":"${msg}"
	 * }
	 */
	public static final String GLOBAL_FAILURE_JSON = "failureJson";
	
	
	/**
	 * 平台定义的全局result formJson结果字符串，详见：/WEB-INF/commons/formJson.jsp
	 * 支持action成员变量msg和json显示。
	 * JSP内容如下JSON形式：
	 * {
	 *		"success":true,
	 *		"code":"${code}",
	 *		"msg":"${msg}",
	 *		"data":${json}
	 * }
	 */
	public static final String GLOBAL_FORM_JSON = "formJson";
	
	
	/**
	 * 平台定义的全局result json结果字符串，详见：/WEB-INF/commons/json.jsp
	 * 支持action成员变量json直接原样打印,建议给结果json字符串使用。
	 * JSP内容如下：
	 * ${json}
	 */
	public static final String GLOBAL_JSON = "json";
	
	
	/** 
	 * 平台定义的全局result data结果字符串，详见：/WEB-INF/commons/data.jsp
	 * 支持action成员变量resultString直接原样打印。
	 * JSP内容如下：
	 * ${resultString}
	 * 
	 * 本结果其实和GLOBAL_JSON等效，只不过赋值的成员变量不一样，以示区分。
	 */
	public static final String GLOBAL_DATA="data";
	
	
	/** 
	 * 平台定义的全局result jsonp结果字符串，详见：/WEB-INF/commons/jsonp.jsp
	 * 专门给继承com.sharera.ext.base.Jsonp 的action使用,支持jsonp 跨域ajax调用，不常用（可能存在XSS攻击安全风险）
	 */
	public static final String GLOBAL_JSONP="jsonp";
	
}
