package com.sunnsoft.sloa.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.ClassUtils;

import org.gteam.db.helper.hibernate.Each;
import org.gteam.db.helper.hibernate.HelperException;
import org.gteam.db.helper.hibernate.ScrollEach;
import org.gteam.db.helper.json.EachEntity2Map;
import org.gteam.util.EntityUtils;

import com.sunnsoft.sloa.db.vo.*;

/**
 *  
 * @author llade
 *
 * Bean 操作类
 */
@SuppressWarnings("unchecked")
public class MailBean {
	private MailHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<Mail> insertBeans = new ArrayList<Mail>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = MailBean.class.getDeclaredMethods();
		List<Method> list = new ArrayList<Method>();
		for (Method m : ms) {
			if(m.getName().startsWith("set")){
				list.add(m);
			}
		}
		methods = list.toArray(new Method[0]);
	}
	
	private Method getSetterMethodFromPropName(String propName){
		String methodName = "set" + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
		for (Method m : methods) {
			if(m.getName().equals(methodName)){
				return m;
			}
		}
		return null;
	}
	/**
	 * 复制属性的核心方法
	 * @param source
	 * @param mode 0 表示复制所有属性，1表示不复制null属性，2表示不复制Null或者空字符串属性。
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 * @throws Exception
	 */
	private MailBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Field[] fs= source.getClass().getDeclaredFields();
		for (Field field : fs) {
			field.setAccessible(true);
			Object value = field.get(source);
			switch (mode) {
				case 0:
					invokePropSetter(field, value);
					break;
				case 1:
					if(value != null){
						invokePropSetter(field, value);
					}
					break;
				case 2:
					if(value != null){
						if(value instanceof String){
							if(StringUtils.isNotEmpty((String)value)){
								invokePropSetter(field, value);
							}
						}else{
							invokePropSetter(field, value);
						}
					}
					break;
				default:
					break;
			}
		}
		return this;
	}

	private void invokePropSetter(Field field, Object value)
			throws IllegalAccessException, InvocationTargetException {
		Method m = this.getSetterMethodFromPropName(field.getName());
		if(m != null){
			m.invoke(this, value);
		}
	}
	/**
	 * 从复制同名属性值到Bean操作类所操作的bean。
	 * 注意：不是通过getter来获取属性值的，而是直接访问source对象的同名属性。
	 * 并且继承的属性无效，一定要是当前source对象定义的属性
	 * source对象一般是struts action对象本身或者是bean对象相同的类。
	 * @param source
	 * @return
	 */
	public MailBean copyValueForm(Object source){
		try {
			this.copyValue(source, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	/**
	 * 类似copyValueForm，但不复制Null值
	 * @param source
	 * @return
	 */
	public MailBean copyNotNullValueForm(Object source){
		try {
			this.copyValue(source, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	/**
	 * 类似copyNotNullValueForm，除了不复制Null值，同时对于空String也不复制。
	 * @param source
	 * @return
	 */
	public MailBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public MailBean(MailHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:传阅id
	*Comment:传阅id
	*/

	public MailBean setMailId(Long mailId){
		paramTypeMap.put("mailId", Long.class);
		paramValueMap.put("mailId", mailId);
		if(this.current != null){
			current.setMailId(mailId);
		}
		return this;
	}

	/**
	*Name:传阅id
	*Comment:传阅id
	*/

	public MailBean setMailIdIfNotNull(Long mailId){
	    if(mailId == null){
	    	return this;
	    }
		return setMailId(mailId);
	}
	/**
	*Name:传阅id
	*Comment:传阅id
	*/

	public MailBean setMailIdWithDefaultValueWhenNull(Long mailId,Long defaultValue){
		Long temp = mailId;
	    if(mailId == null){
	    	temp = defaultValue;
	    }
		return setMailId(temp);
	}
	
	
	/**
	*Name:发件人id
	*Comment:发件人id
	*/

	public MailBean setUserId(long userId){
		paramTypeMap.put("userId", ClassUtils.wrapperToPrimitive(Long.class));
		paramValueMap.put("userId", userId);
		if(this.current != null){
			current.setUserId(userId);
		}
		return this;
	}
	
	/**
	*Name:发件人工作编号
	*Comment:发件人工作编号
	*/

	public MailBean setWorkCode(String workCode){
		paramTypeMap.put("workCode", String.class);
		paramValueMap.put("workCode", workCode);
		if(this.current != null){
			current.setWorkCode(workCode);
		}
		return this;
	}

	/**
	*Name:发件人工作编号
	*Comment:发件人工作编号
	*/

	public MailBean setWorkCodeIfNotNull(String workCode){
	    if(workCode == null){
	    	return this;
	    }
		return setWorkCode(workCode);
	}
	/**
	*Name:发件人工作编号
	*Comment:发件人工作编号
	*/

	public MailBean setWorkCodeWithDefaultValueWhenNull(String workCode,String defaultValue){
		String temp = workCode;
	    if(workCode == null){
	    	temp = defaultValue;
	    }
		return setWorkCode(temp);
	}
	
	/**
	*Name:发件人工作编号
	*Comment:发件人工作编号
	*/

	public MailBean setWorkCodeIfNotNullAndNotEmpty(String workCode){
		if(StringUtils.isEmpty(workCode)){
	    	return this;
	    }
	    return setWorkCode(workCode);
	}
	/**
	*Name:发件人工作编号
	*Comment:发件人工作编号
	*/

	public MailBean setWorkCodeWithDefaultValueWhenNullOrEmpty(String workCode,String defaultValue){
		String temp = workCode;
		if(StringUtils.isEmpty(workCode)){
	    	temp = defaultValue;
	    }
	    return setWorkCode(temp);
	}
	
	/**
	*Name:发件人姓名
	*Comment:发件人的姓名
	*/

	public MailBean setLastName(String lastName){
		paramTypeMap.put("lastName", String.class);
		paramValueMap.put("lastName", lastName);
		if(this.current != null){
			current.setLastName(lastName);
		}
		return this;
	}

	/**
	*Name:发件人姓名
	*Comment:发件人的姓名
	*/

	public MailBean setLastNameIfNotNull(String lastName){
	    if(lastName == null){
	    	return this;
	    }
		return setLastName(lastName);
	}
	/**
	*Name:发件人姓名
	*Comment:发件人的姓名
	*/

	public MailBean setLastNameWithDefaultValueWhenNull(String lastName,String defaultValue){
		String temp = lastName;
	    if(lastName == null){
	    	temp = defaultValue;
	    }
		return setLastName(temp);
	}
	
	/**
	*Name:发件人姓名
	*Comment:发件人的姓名
	*/

	public MailBean setLastNameIfNotNullAndNotEmpty(String lastName){
		if(StringUtils.isEmpty(lastName)){
	    	return this;
	    }
	    return setLastName(lastName);
	}
	/**
	*Name:发件人姓名
	*Comment:发件人的姓名
	*/

	public MailBean setLastNameWithDefaultValueWhenNullOrEmpty(String lastName,String defaultValue){
		String temp = lastName;
		if(StringUtils.isEmpty(lastName)){
	    	temp = defaultValue;
	    }
	    return setLastName(temp);
	}
	
	/**
	*Name:发件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public MailBean setLoginId(String loginId){
		paramTypeMap.put("loginId", String.class);
		paramValueMap.put("loginId", loginId);
		if(this.current != null){
			current.setLoginId(loginId);
		}
		return this;
	}

	/**
	*Name:发件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public MailBean setLoginIdIfNotNull(String loginId){
	    if(loginId == null){
	    	return this;
	    }
		return setLoginId(loginId);
	}
	/**
	*Name:发件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public MailBean setLoginIdWithDefaultValueWhenNull(String loginId,String defaultValue){
		String temp = loginId;
	    if(loginId == null){
	    	temp = defaultValue;
	    }
		return setLoginId(temp);
	}
	
	/**
	*Name:发件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public MailBean setLoginIdIfNotNullAndNotEmpty(String loginId){
		if(StringUtils.isEmpty(loginId)){
	    	return this;
	    }
	    return setLoginId(loginId);
	}
	/**
	*Name:发件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public MailBean setLoginIdWithDefaultValueWhenNullOrEmpty(String loginId,String defaultValue){
		String temp = loginId;
		if(StringUtils.isEmpty(loginId)){
	    	temp = defaultValue;
	    }
	    return setLoginId(temp);
	}
	
	/**
	*Name:发件人的分部全称
	*Comment:发件人的分部全称
	*/

	public MailBean setSubcompanyName(String subcompanyName){
		paramTypeMap.put("subcompanyName", String.class);
		paramValueMap.put("subcompanyName", subcompanyName);
		if(this.current != null){
			current.setSubcompanyName(subcompanyName);
		}
		return this;
	}

	/**
	*Name:发件人的分部全称
	*Comment:发件人的分部全称
	*/

	public MailBean setSubcompanyNameIfNotNull(String subcompanyName){
	    if(subcompanyName == null){
	    	return this;
	    }
		return setSubcompanyName(subcompanyName);
	}
	/**
	*Name:发件人的分部全称
	*Comment:发件人的分部全称
	*/

	public MailBean setSubcompanyNameWithDefaultValueWhenNull(String subcompanyName,String defaultValue){
		String temp = subcompanyName;
	    if(subcompanyName == null){
	    	temp = defaultValue;
	    }
		return setSubcompanyName(temp);
	}
	
	/**
	*Name:发件人的分部全称
	*Comment:发件人的分部全称
	*/

	public MailBean setSubcompanyNameIfNotNullAndNotEmpty(String subcompanyName){
		if(StringUtils.isEmpty(subcompanyName)){
	    	return this;
	    }
	    return setSubcompanyName(subcompanyName);
	}
	/**
	*Name:发件人的分部全称
	*Comment:发件人的分部全称
	*/

	public MailBean setSubcompanyNameWithDefaultValueWhenNullOrEmpty(String subcompanyName,String defaultValue){
		String temp = subcompanyName;
		if(StringUtils.isEmpty(subcompanyName)){
	    	temp = defaultValue;
	    }
	    return setSubcompanyName(temp);
	}
	
	/**
	*Name:发件人的部门全称
	*Comment:发件人的部门全称
	*/

	public MailBean setDepartmentName(String departmentName){
		paramTypeMap.put("departmentName", String.class);
		paramValueMap.put("departmentName", departmentName);
		if(this.current != null){
			current.setDepartmentName(departmentName);
		}
		return this;
	}

	/**
	*Name:发件人的部门全称
	*Comment:发件人的部门全称
	*/

	public MailBean setDepartmentNameIfNotNull(String departmentName){
	    if(departmentName == null){
	    	return this;
	    }
		return setDepartmentName(departmentName);
	}
	/**
	*Name:发件人的部门全称
	*Comment:发件人的部门全称
	*/

	public MailBean setDepartmentNameWithDefaultValueWhenNull(String departmentName,String defaultValue){
		String temp = departmentName;
	    if(departmentName == null){
	    	temp = defaultValue;
	    }
		return setDepartmentName(temp);
	}
	
	/**
	*Name:发件人的部门全称
	*Comment:发件人的部门全称
	*/

	public MailBean setDepartmentNameIfNotNullAndNotEmpty(String departmentName){
		if(StringUtils.isEmpty(departmentName)){
	    	return this;
	    }
	    return setDepartmentName(departmentName);
	}
	/**
	*Name:发件人的部门全称
	*Comment:发件人的部门全称
	*/

	public MailBean setDepartmentNameWithDefaultValueWhenNullOrEmpty(String departmentName,String defaultValue){
		String temp = departmentName;
		if(StringUtils.isEmpty(departmentName)){
	    	temp = defaultValue;
	    }
	    return setDepartmentName(temp);
	}
	
	/**
	*Name:收件人名字
	*Comment:可以有多个(冗余字段)
	*/

	public MailBean setAllReceiveName(String allReceiveName){
		paramTypeMap.put("allReceiveName", String.class);
		paramValueMap.put("allReceiveName", allReceiveName);
		if(this.current != null){
			current.setAllReceiveName(allReceiveName);
		}
		return this;
	}

	/**
	*Name:收件人名字
	*Comment:可以有多个(冗余字段)
	*/

	public MailBean setAllReceiveNameIfNotNull(String allReceiveName){
	    if(allReceiveName == null){
	    	return this;
	    }
		return setAllReceiveName(allReceiveName);
	}
	/**
	*Name:收件人名字
	*Comment:可以有多个(冗余字段)
	*/

	public MailBean setAllReceiveNameWithDefaultValueWhenNull(String allReceiveName,String defaultValue){
		String temp = allReceiveName;
	    if(allReceiveName == null){
	    	temp = defaultValue;
	    }
		return setAllReceiveName(temp);
	}
	
	/**
	*Name:收件人名字
	*Comment:可以有多个(冗余字段)
	*/

	public MailBean setAllReceiveNameIfNotNullAndNotEmpty(String allReceiveName){
		if(StringUtils.isEmpty(allReceiveName)){
	    	return this;
	    }
	    return setAllReceiveName(allReceiveName);
	}
	/**
	*Name:收件人名字
	*Comment:可以有多个(冗余字段)
	*/

	public MailBean setAllReceiveNameWithDefaultValueWhenNullOrEmpty(String allReceiveName,String defaultValue){
		String temp = allReceiveName;
		if(StringUtils.isEmpty(allReceiveName)){
	    	temp = defaultValue;
	    }
	    return setAllReceiveName(temp);
	}
	
	/**
	*Name:传阅主题
	*Comment:传阅主题(不做限制)
	*/

	public MailBean setTitle(String title){
		paramTypeMap.put("title", String.class);
		paramValueMap.put("title", title);
		if(this.current != null){
			current.setTitle(title);
		}
		return this;
	}

	/**
	*Name:传阅主题
	*Comment:传阅主题(不做限制)
	*/

	public MailBean setTitleIfNotNull(String title){
	    if(title == null){
	    	return this;
	    }
		return setTitle(title);
	}
	/**
	*Name:传阅主题
	*Comment:传阅主题(不做限制)
	*/

	public MailBean setTitleWithDefaultValueWhenNull(String title,String defaultValue){
		String temp = title;
	    if(title == null){
	    	temp = defaultValue;
	    }
		return setTitle(temp);
	}
	
	/**
	*Name:传阅主题
	*Comment:传阅主题(不做限制)
	*/

	public MailBean setTitleIfNotNullAndNotEmpty(String title){
		if(StringUtils.isEmpty(title)){
	    	return this;
	    }
	    return setTitle(title);
	}
	/**
	*Name:传阅主题
	*Comment:传阅主题(不做限制)
	*/

	public MailBean setTitleWithDefaultValueWhenNullOrEmpty(String title,String defaultValue){
		String temp = title;
		if(StringUtils.isEmpty(title)){
	    	temp = defaultValue;
	    }
	    return setTitle(temp);
	}
	
	/**
	*Name:邮件内容
	*Comment:邮件内容
	*/

	public MailBean setMailContent(String mailContent){
		paramTypeMap.put("mailContent", String.class);
		paramValueMap.put("mailContent", mailContent);
		if(this.current != null){
			current.setMailContent(mailContent);
		}
		return this;
	}

	/**
	*Name:邮件内容
	*Comment:邮件内容
	*/

	public MailBean setMailContentIfNotNull(String mailContent){
	    if(mailContent == null){
	    	return this;
	    }
		return setMailContent(mailContent);
	}
	/**
	*Name:邮件内容
	*Comment:邮件内容
	*/

	public MailBean setMailContentWithDefaultValueWhenNull(String mailContent,String defaultValue){
		String temp = mailContent;
	    if(mailContent == null){
	    	temp = defaultValue;
	    }
		return setMailContent(temp);
	}
	
	/**
	*Name:邮件内容
	*Comment:邮件内容
	*/

	public MailBean setMailContentIfNotNullAndNotEmpty(String mailContent){
		if(StringUtils.isEmpty(mailContent)){
	    	return this;
	    }
	    return setMailContent(mailContent);
	}
	/**
	*Name:邮件内容
	*Comment:邮件内容
	*/

	public MailBean setMailContentWithDefaultValueWhenNullOrEmpty(String mailContent,String defaultValue){
		String temp = mailContent;
		if(StringUtils.isEmpty(mailContent)){
	    	temp = defaultValue;
	    }
	    return setMailContent(temp);
	}
	
	/**
	*Name:创建传阅的时间
	*Comment:创建传阅的时间
	*/

	public MailBean setCreateTime(Date createTime){
		paramTypeMap.put("createTime", Date.class);
		paramValueMap.put("createTime", createTime);
		if(this.current != null){
			current.setCreateTime(createTime);
		}
		return this;
	}

	/**
	*Name:创建传阅的时间
	*Comment:创建传阅的时间
	*/

	public MailBean setCreateTimeIfNotNull(Date createTime){
	    if(createTime == null){
	    	return this;
	    }
		return setCreateTime(createTime);
	}
	/**
	*Name:创建传阅的时间
	*Comment:创建传阅的时间
	*/

	public MailBean setCreateTimeWithDefaultValueWhenNull(Date createTime,Date defaultValue){
		Date temp = createTime;
	    if(createTime == null){
	    	temp = defaultValue;
	    }
		return setCreateTime(temp);
	}
	
	
	/**
	*Name:发送传阅的时间
	*Comment:发送传阅的时间
	*/

	public MailBean setSendTime(Date sendTime){
		paramTypeMap.put("sendTime", Date.class);
		paramValueMap.put("sendTime", sendTime);
		if(this.current != null){
			current.setSendTime(sendTime);
		}
		return this;
	}

	/**
	*Name:发送传阅的时间
	*Comment:发送传阅的时间
	*/

	public MailBean setSendTimeIfNotNull(Date sendTime){
	    if(sendTime == null){
	    	return this;
	    }
		return setSendTime(sendTime);
	}
	/**
	*Name:发送传阅的时间
	*Comment:发送传阅的时间
	*/

	public MailBean setSendTimeWithDefaultValueWhenNull(Date sendTime,Date defaultValue){
		Date temp = sendTime;
	    if(sendTime == null){
	    	temp = defaultValue;
	    }
		return setSendTime(temp);
	}
	
	
	/**
	*Name:传阅状态
	*Comment:0 无状态   1 待发传阅  7 已删除
	*/

	public MailBean setStatus(Integer status){
		paramTypeMap.put("status", Integer.class);
		paramValueMap.put("status", status);
		if(this.current != null){
			current.setStatus(status);
		}
		return this;
	}

	/**
	*Name:传阅状态
	*Comment:0 无状态   1 待发传阅  7 已删除
	*/

	public MailBean setStatusIfNotNull(Integer status){
	    if(status == null){
	    	return this;
	    }
		return setStatus(status);
	}
	/**
	*Name:传阅状态
	*Comment:0 无状态   1 待发传阅  7 已删除
	*/

	public MailBean setStatusWithDefaultValueWhenNull(Integer status,Integer defaultValue){
		Integer temp = status;
	    if(status == null){
	    	temp = defaultValue;
	    }
		return setStatus(temp);
	}
	
	
	/**
	*Name:传阅流程状态
	*Comment:1 发阅中   3 已完成
	*/

	public MailBean setStepStatus(Integer stepStatus){
		paramTypeMap.put("stepStatus", Integer.class);
		paramValueMap.put("stepStatus", stepStatus);
		if(this.current != null){
			current.setStepStatus(stepStatus);
		}
		return this;
	}

	/**
	*Name:传阅流程状态
	*Comment:1 发阅中   3 已完成
	*/

	public MailBean setStepStatusIfNotNull(Integer stepStatus){
	    if(stepStatus == null){
	    	return this;
	    }
		return setStepStatus(stepStatus);
	}
	/**
	*Name:传阅流程状态
	*Comment:1 发阅中   3 已完成
	*/

	public MailBean setStepStatusWithDefaultValueWhenNull(Integer stepStatus,Integer defaultValue){
		Integer temp = stepStatus;
	    if(stepStatus == null){
	    	temp = defaultValue;
	    }
		return setStepStatus(temp);
	}
	
	
	/**
	*Name:设置的完成时间
	*Comment:传阅完成的时间
	*/

	public MailBean setCompleteTime(Date completeTime){
		paramTypeMap.put("completeTime", Date.class);
		paramValueMap.put("completeTime", completeTime);
		if(this.current != null){
			current.setCompleteTime(completeTime);
		}
		return this;
	}

	/**
	*Name:设置的完成时间
	*Comment:传阅完成的时间
	*/

	public MailBean setCompleteTimeIfNotNull(Date completeTime){
	    if(completeTime == null){
	    	return this;
	    }
		return setCompleteTime(completeTime);
	}
	/**
	*Name:设置的完成时间
	*Comment:传阅完成的时间
	*/

	public MailBean setCompleteTimeWithDefaultValueWhenNull(Date completeTime,Date defaultValue){
		Date temp = completeTime;
	    if(completeTime == null){
	    	temp = defaultValue;
	    }
		return setCompleteTime(temp);
	}
	
	
	/**
	*Name:重要传阅
	*Comment:重要传阅
	*/

	public MailBean setIfImportant(Boolean ifImportant){
		paramTypeMap.put("ifImportant", Boolean.class);
		paramValueMap.put("ifImportant", ifImportant);
		if(this.current != null){
			current.setIfImportant(ifImportant);
		}
		return this;
	}

	/**
	*Name:重要传阅
	*Comment:重要传阅
	*/

	public MailBean setIfImportantIfNotNull(Boolean ifImportant){
	    if(ifImportant == null){
	    	return this;
	    }
		return setIfImportant(ifImportant);
	}
	/**
	*Name:重要传阅
	*Comment:重要传阅
	*/

	public MailBean setIfImportantWithDefaultValueWhenNull(Boolean ifImportant,Boolean defaultValue){
		Boolean temp = ifImportant;
	    if(ifImportant == null){
	    	temp = defaultValue;
	    }
		return setIfImportant(temp);
	}
	
	
	/**
	*Name:允许修订附件
	*Comment:允许修订附件
	*/

	public MailBean setIfUpdate(Boolean ifUpdate){
		paramTypeMap.put("ifUpdate", Boolean.class);
		paramValueMap.put("ifUpdate", ifUpdate);
		if(this.current != null){
			current.setIfUpdate(ifUpdate);
		}
		return this;
	}

	/**
	*Name:允许修订附件
	*Comment:允许修订附件
	*/

	public MailBean setIfUpdateIfNotNull(Boolean ifUpdate){
	    if(ifUpdate == null){
	    	return this;
	    }
		return setIfUpdate(ifUpdate);
	}
	/**
	*Name:允许修订附件
	*Comment:允许修订附件
	*/

	public MailBean setIfUpdateWithDefaultValueWhenNull(Boolean ifUpdate,Boolean defaultValue){
		Boolean temp = ifUpdate;
	    if(ifUpdate == null){
	    	temp = defaultValue;
	    }
		return setIfUpdate(temp);
	}
	
	
	/**
	*Name:允许上传附件
	*Comment:允许上传附件
	*/

	public MailBean setIfUpload(Boolean ifUpload){
		paramTypeMap.put("ifUpload", Boolean.class);
		paramValueMap.put("ifUpload", ifUpload);
		if(this.current != null){
			current.setIfUpload(ifUpload);
		}
		return this;
	}

	/**
	*Name:允许上传附件
	*Comment:允许上传附件
	*/

	public MailBean setIfUploadIfNotNull(Boolean ifUpload){
	    if(ifUpload == null){
	    	return this;
	    }
		return setIfUpload(ifUpload);
	}
	/**
	*Name:允许上传附件
	*Comment:允许上传附件
	*/

	public MailBean setIfUploadWithDefaultValueWhenNull(Boolean ifUpload,Boolean defaultValue){
		Boolean temp = ifUpload;
	    if(ifUpload == null){
	    	temp = defaultValue;
	    }
		return setIfUpload(temp);
	}
	
	
	/**
	*Name:开封已阅确认
	*Comment:开封已阅确认
	*/

	public MailBean setIfRead(Boolean ifRead){
		paramTypeMap.put("ifRead", Boolean.class);
		paramValueMap.put("ifRead", ifRead);
		if(this.current != null){
			current.setIfRead(ifRead);
		}
		return this;
	}

	/**
	*Name:开封已阅确认
	*Comment:开封已阅确认
	*/

	public MailBean setIfReadIfNotNull(Boolean ifRead){
	    if(ifRead == null){
	    	return this;
	    }
		return setIfRead(ifRead);
	}
	/**
	*Name:开封已阅确认
	*Comment:开封已阅确认
	*/

	public MailBean setIfReadWithDefaultValueWhenNull(Boolean ifRead,Boolean defaultValue){
		Boolean temp = ifRead;
	    if(ifRead == null){
	    	temp = defaultValue;
	    }
		return setIfRead(temp);
	}
	
	
	/**
	*Name:短信提醒
	*Comment:短信提醒
	*/

	public MailBean setIfNotify(Boolean ifNotify){
		paramTypeMap.put("ifNotify", Boolean.class);
		paramValueMap.put("ifNotify", ifNotify);
		if(this.current != null){
			current.setIfNotify(ifNotify);
		}
		return this;
	}

	/**
	*Name:短信提醒
	*Comment:短信提醒
	*/

	public MailBean setIfNotifyIfNotNull(Boolean ifNotify){
	    if(ifNotify == null){
	    	return this;
	    }
		return setIfNotify(ifNotify);
	}
	/**
	*Name:短信提醒
	*Comment:短信提醒
	*/

	public MailBean setIfNotifyWithDefaultValueWhenNull(Boolean ifNotify,Boolean defaultValue){
		Boolean temp = ifNotify;
	    if(ifNotify == null){
	    	temp = defaultValue;
	    }
		return setIfNotify(temp);
	}
	
	
	/**
	*Name:确认时提醒
	*Comment:确认时提醒
	*/

	public MailBean setIfRemind(Boolean ifRemind){
		paramTypeMap.put("ifRemind", Boolean.class);
		paramValueMap.put("ifRemind", ifRemind);
		if(this.current != null){
			current.setIfRemind(ifRemind);
		}
		return this;
	}

	/**
	*Name:确认时提醒
	*Comment:确认时提醒
	*/

	public MailBean setIfRemindIfNotNull(Boolean ifRemind){
	    if(ifRemind == null){
	    	return this;
	    }
		return setIfRemind(ifRemind);
	}
	/**
	*Name:确认时提醒
	*Comment:确认时提醒
	*/

	public MailBean setIfRemindWithDefaultValueWhenNull(Boolean ifRemind,Boolean defaultValue){
		Boolean temp = ifRemind;
	    if(ifRemind == null){
	    	temp = defaultValue;
	    }
		return setIfRemind(temp);
	}
	
	
	/**
	*Name:确认时提醒所有传阅对象
	*Comment:确认时提醒所有传阅对象
	*/

	public MailBean setIfRemindAll(Boolean ifRemindAll){
		paramTypeMap.put("ifRemindAll", Boolean.class);
		paramValueMap.put("ifRemindAll", ifRemindAll);
		if(this.current != null){
			current.setIfRemindAll(ifRemindAll);
		}
		return this;
	}

	/**
	*Name:确认时提醒所有传阅对象
	*Comment:确认时提醒所有传阅对象
	*/

	public MailBean setIfRemindAllIfNotNull(Boolean ifRemindAll){
	    if(ifRemindAll == null){
	    	return this;
	    }
		return setIfRemindAll(ifRemindAll);
	}
	/**
	*Name:确认时提醒所有传阅对象
	*Comment:确认时提醒所有传阅对象
	*/

	public MailBean setIfRemindAllWithDefaultValueWhenNull(Boolean ifRemindAll,Boolean defaultValue){
		Boolean temp = ifRemindAll;
	    if(ifRemindAll == null){
	    	temp = defaultValue;
	    }
		return setIfRemindAll(temp);
	}
	
	
	/**
	*Name:传阅密送
	*Comment:传阅密送(不用实现)
	*/

	public MailBean setIfSecrecy(Boolean ifSecrecy){
		paramTypeMap.put("ifSecrecy", Boolean.class);
		paramValueMap.put("ifSecrecy", ifSecrecy);
		if(this.current != null){
			current.setIfSecrecy(ifSecrecy);
		}
		return this;
	}

	/**
	*Name:传阅密送
	*Comment:传阅密送(不用实现)
	*/

	public MailBean setIfSecrecyIfNotNull(Boolean ifSecrecy){
	    if(ifSecrecy == null){
	    	return this;
	    }
		return setIfSecrecy(ifSecrecy);
	}
	/**
	*Name:传阅密送
	*Comment:传阅密送(不用实现)
	*/

	public MailBean setIfSecrecyWithDefaultValueWhenNull(Boolean ifSecrecy,Boolean defaultValue){
		Boolean temp = ifSecrecy;
	    if(ifSecrecy == null){
	    	temp = defaultValue;
	    }
		return setIfSecrecy(temp);
	}
	
	
	/**
	*Name:允许新添加人员
	*Comment:允许新添加人员
	*/

	public MailBean setIfAdd(Boolean ifAdd){
		paramTypeMap.put("ifAdd", Boolean.class);
		paramValueMap.put("ifAdd", ifAdd);
		if(this.current != null){
			current.setIfAdd(ifAdd);
		}
		return this;
	}

	/**
	*Name:允许新添加人员
	*Comment:允许新添加人员
	*/

	public MailBean setIfAddIfNotNull(Boolean ifAdd){
	    if(ifAdd == null){
	    	return this;
	    }
		return setIfAdd(ifAdd);
	}
	/**
	*Name:允许新添加人员
	*Comment:允许新添加人员
	*/

	public MailBean setIfAddWithDefaultValueWhenNull(Boolean ifAdd,Boolean defaultValue){
		Boolean temp = ifAdd;
	    if(ifAdd == null){
	    	temp = defaultValue;
	    }
		return setIfAdd(temp);
	}
	
	
	/**
	*Name:有序确认
	*Comment:有序确认(留个字段,不实现)
	*/

	public MailBean setIfSequence(Boolean ifSequence){
		paramTypeMap.put("ifSequence", Boolean.class);
		paramValueMap.put("ifSequence", ifSequence);
		if(this.current != null){
			current.setIfSequence(ifSequence);
		}
		return this;
	}

	/**
	*Name:有序确认
	*Comment:有序确认(留个字段,不实现)
	*/

	public MailBean setIfSequenceIfNotNull(Boolean ifSequence){
	    if(ifSequence == null){
	    	return this;
	    }
		return setIfSequence(ifSequence);
	}
	/**
	*Name:有序确认
	*Comment:有序确认(留个字段,不实现)
	*/

	public MailBean setIfSequenceWithDefaultValueWhenNull(Boolean ifSequence,Boolean defaultValue){
		Boolean temp = ifSequence;
	    if(ifSequence == null){
	    	temp = defaultValue;
	    }
		return setIfSequence(temp);
	}
	
	
	/**
	*Name:是否有附件
	*Comment:0 为没有 （false），1 为存在（true）
	*/

	public MailBean setHasAttachment(Boolean hasAttachment){
		paramTypeMap.put("hasAttachment", Boolean.class);
		paramValueMap.put("hasAttachment", hasAttachment);
		if(this.current != null){
			current.setHasAttachment(hasAttachment);
		}
		return this;
	}

	/**
	*Name:是否有附件
	*Comment:0 为没有 （false），1 为存在（true）
	*/

	public MailBean setHasAttachmentIfNotNull(Boolean hasAttachment){
	    if(hasAttachment == null){
	    	return this;
	    }
		return setHasAttachment(hasAttachment);
	}
	/**
	*Name:是否有附件
	*Comment:0 为没有 （false），1 为存在（true）
	*/

	public MailBean setHasAttachmentWithDefaultValueWhenNull(Boolean hasAttachment,Boolean defaultValue){
		Boolean temp = hasAttachment;
	    if(hasAttachment == null){
	    	temp = defaultValue;
	    }
		return setHasAttachment(temp);
	}
	
	
	/**
	*Name:是否启用软删除
	*Comment:0 为未删除（false），1 为 删除（true）
	*/

	public MailBean setEnabled(boolean enabled){
		paramTypeMap.put("enabled", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("enabled", enabled);
		if(this.current != null){
			current.setEnabled(enabled);
		}
		return this;
	}
	
	/**
	*Name:是否关注
	*Comment:0 为未关注 （false），1 已关注（true）
	*/

	public MailBean setAttention(Boolean attention){
		paramTypeMap.put("attention", Boolean.class);
		paramValueMap.put("attention", attention);
		if(this.current != null){
			current.setAttention(attention);
		}
		return this;
	}

	/**
	*Name:是否关注
	*Comment:0 为未关注 （false），1 已关注（true）
	*/

	public MailBean setAttentionIfNotNull(Boolean attention){
	    if(attention == null){
	    	return this;
	    }
		return setAttention(attention);
	}
	/**
	*Name:是否关注
	*Comment:0 为未关注 （false），1 已关注（true）
	*/

	public MailBean setAttentionWithDefaultValueWhenNull(Boolean attention,Boolean defaultValue){
		Boolean temp = attention;
	    if(attention == null){
	    	temp = defaultValue;
	    }
		return setAttention(temp);
	}
	
	
	/**
	*Name:传阅规则
	*Comment:该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；
	*/

	public MailBean setRuleName(String ruleName){
		paramTypeMap.put("ruleName", String.class);
		paramValueMap.put("ruleName", ruleName);
		if(this.current != null){
			current.setRuleName(ruleName);
		}
		return this;
	}

	/**
	*Name:传阅规则
	*Comment:该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；
	*/

	public MailBean setRuleNameIfNotNull(String ruleName){
	    if(ruleName == null){
	    	return this;
	    }
		return setRuleName(ruleName);
	}
	/**
	*Name:传阅规则
	*Comment:该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；
	*/

	public MailBean setRuleNameWithDefaultValueWhenNull(String ruleName,String defaultValue){
		String temp = ruleName;
	    if(ruleName == null){
	    	temp = defaultValue;
	    }
		return setRuleName(temp);
	}
	
	/**
	*Name:传阅规则
	*Comment:该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；
	*/

	public MailBean setRuleNameIfNotNullAndNotEmpty(String ruleName){
		if(StringUtils.isEmpty(ruleName)){
	    	return this;
	    }
	    return setRuleName(ruleName);
	}
	/**
	*Name:传阅规则
	*Comment:该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；
	*/

	public MailBean setRuleNameWithDefaultValueWhenNullOrEmpty(String ruleName,String defaultValue){
		String temp = ruleName;
		if(StringUtils.isEmpty(ruleName)){
	    	temp = defaultValue;
	    }
	    return setRuleName(temp);
	}
	
	/**
	*Name:删除传阅的时间
	*Comment:删除传阅的时间
	*/

	public MailBean setDeleteTime(Date deleteTime){
		paramTypeMap.put("deleteTime", Date.class);
		paramValueMap.put("deleteTime", deleteTime);
		if(this.current != null){
			current.setDeleteTime(deleteTime);
		}
		return this;
	}

	/**
	*Name:删除传阅的时间
	*Comment:删除传阅的时间
	*/

	public MailBean setDeleteTimeIfNotNull(Date deleteTime){
	    if(deleteTime == null){
	    	return this;
	    }
		return setDeleteTime(deleteTime);
	}
	/**
	*Name:删除传阅的时间
	*Comment:删除传阅的时间
	*/

	public MailBean setDeleteTimeWithDefaultValueWhenNull(Date deleteTime,Date defaultValue){
		Date temp = deleteTime;
	    if(deleteTime == null){
	    	temp = defaultValue;
	    }
		return setDeleteTime(temp);
	}
	
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<Mail> update(){
		return this.helper.each(new Each<Mail>(){

			@Override
			public void each(Mail bean, List<Mail> list) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 本方法的行为如下：
	 * 1.如果找到唯一的匹配对象，则update bean ,并返回该对象。
	 * 2.如果没找到，则不做任何动作，并返回null.
	 * 3.如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 的bean 或 null
	 */
	public Mail updateUnique(){
		Mail bean =  this.helper.uniqueResult();
		if(bean != null){
			this.invokeSetters(bean);
			this.helper.service.update(bean);
		}
		return bean;
	}
	
	/**
	 * 批量更新大量数据。对于大数量的修改有利。需要注意，大批量修改数据的话，可能引起数据库update锁冲突，慎用。
	 * 此外，可以调用helper类的sleepInteval方法设置每批次的线程挂起时间。避免CPU占用过高
	 * @param flushValve 设置每更新flushValve个bean后清hibernate一级缓存并同步到数据库，避免OutOfMemory。
	 * @return 返回迭代的结果的总和
	 */
	public long batchUpdate(final int flushValve){
		return this.helper.scrollResult(new ScrollEach<Mail>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(Mail bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<Mail> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private Mail current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public MailBean create(){
		Mail bean = new Mail();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<Mail> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (Mail bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public Mail insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		Mail bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public Mail ifFoundUpdateElseInsert(){
		Mail bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new Mail();
		}
		this.invokeSetters(bean);
		this.helper.service.saveOrUpdate(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常。
	 * 最后返回bean的json 
	 * @return json字符串
	 */
	public String ifFoundUpdateElseInsertThenConvertJson(){
		Mail bean = this.ifFoundUpdateElseInsert();
		return this.helper.json().fastJsonSerializer(bean, false);
	}
	
	/**
	  * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常。使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要
	 * 最后返回bean的json 
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return json字符串 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<Mail> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Mail bean = this.ifFoundUpdateElseInsert();
		Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
		return this.helper.json().fastJsonSerializer(map, true);
	}
	
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中，并返回插入对象的json序列化字符串。对于一个Bean操作类，本方法只能被调用一次。
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String insertAndReturnJson() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(false, false, null);
	}

	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入对象的json序列化字符串，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String insertUniqueAndReturnJson() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, false, null);
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中，并返回插入对象的json序列化字符串，使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要。对于一个Bean操作类，本方法只能被调用一次。
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String insertJson(EachEntity2Map<Mail> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(false, false, eachEntity2Map);
	}
	/**
	  * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入对象的json序列化字符串，使用EachEntity2Map 对实体转换的map进行调整,以符合实际需要，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String insertUniqueJson(EachEntity2Map<Mail> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, false, eachEntity2Map);
	}
	/**
	 * 持久化到数据库，返回更新的数据库记录集合转换的json字符串。
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String updateAndReturnJson() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(false, true, null);
	}
	/**
	 * 本方法的行为如下：
	 * 1.如果找到唯一的匹配对象，则update bean ,并返回该对象json。
	 * 2.如果没找到，则不做任何动作，并返回null.
	 * 3.如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String updateUniqueAndReturnJson() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, null);
	}
	/**
	 * 持久化到数据库，返回更新的数据库记录集合转换的json字符串。
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String updateJson(EachEntity2Map<Mail> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(false, true, eachEntity2Map);
	}
	/**
	 * 本方法的行为如下：
	 * 1.如果找到唯一的匹配对象，则update bean ,并返回该对象json。
	 * 2.如果没找到，则不做任何动作，并返回null.
	 * 3.如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String updateUniqueJson(EachEntity2Map<Mail> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<Mail> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<Mail> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (Mail bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			Mail bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(Mail bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = Mail.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
