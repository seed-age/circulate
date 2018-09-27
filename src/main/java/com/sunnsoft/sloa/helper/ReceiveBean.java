package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.gteam.db.helper.hibernate.Each;
import org.gteam.db.helper.hibernate.HelperException;
import org.gteam.db.helper.hibernate.ScrollEach;
import org.gteam.db.helper.json.EachEntity2Map;
import org.gteam.util.EntityUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 *  
 * @author llade
 *
 * Bean 操作类
 */
@SuppressWarnings("unchecked")
public class ReceiveBean {
	private ReceiveHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<Receive> insertBeans = new ArrayList<Receive>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = ReceiveBean.class.getDeclaredMethods();
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
	private ReceiveBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public ReceiveBean copyValueForm(Object source){
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
	public ReceiveBean copyNotNullValueForm(Object source){
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
	public ReceiveBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public ReceiveBean(ReceiveHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:收件id
	*Comment:收件id
	*/

	public ReceiveBean setReceiveId(Long receiveId){
		paramTypeMap.put("receiveId", Long.class);
		paramValueMap.put("receiveId", receiveId);
		if(this.current != null){
			current.setReceiveId(receiveId);
		}
		return this;
	}

	/**
	*Name:收件id
	*Comment:收件id
	*/

	public ReceiveBean setReceiveIdIfNotNull(Long receiveId){
	    if(receiveId == null){
	    	return this;
	    }
		return setReceiveId(receiveId);
	}
	/**
	*Name:收件id
	*Comment:收件id
	*/

	public ReceiveBean setReceiveIdWithDefaultValueWhenNull(Long receiveId,Long defaultValue){
		Long temp = receiveId;
	    if(receiveId == null){
	    	temp = defaultValue;
	    }
		return setReceiveId(temp);
	}
	
	

	public ReceiveBean setMail(Mail mail){
		paramTypeMap.put("mail", Mail.class);
		paramValueMap.put("mail", mail);
		if(this.current != null){
			current.setMail(mail);
		}
		return this;
	}


	public ReceiveBean setMailIfNotNull(Mail mail){
	    if(mail == null){
	    	return this;
	    }
		return setMail(mail);
	}

	public ReceiveBean setMailWithDefaultValueWhenNull(Mail mail,Mail defaultValue){
		Mail temp = mail;
	    if(mail == null){
	    	temp = defaultValue;
	    }
		return setMail(temp);
	}
	
	
	/**
	*Name:收件人id
	*Comment:收件人id
	*/

	public ReceiveBean setUserId(long userId){
		paramTypeMap.put("userId", ClassUtils.wrapperToPrimitive(Long.class));
		paramValueMap.put("userId", userId);
		if(this.current != null){
			current.setUserId(userId);
		}
		return this;
	}
	
	/**
	*Name:收件人工作编号
	*Comment:收件人工作编号
	*/

	public ReceiveBean setWorkCode(String workCode){
		paramTypeMap.put("workCode", String.class);
		paramValueMap.put("workCode", workCode);
		if(this.current != null){
			current.setWorkCode(workCode);
		}
		return this;
	}

	/**
	*Name:收件人工作编号
	*Comment:收件人工作编号
	*/

	public ReceiveBean setWorkCodeIfNotNull(String workCode){
	    if(workCode == null){
	    	return this;
	    }
		return setWorkCode(workCode);
	}
	/**
	*Name:收件人工作编号
	*Comment:收件人工作编号
	*/

	public ReceiveBean setWorkCodeWithDefaultValueWhenNull(String workCode,String defaultValue){
		String temp = workCode;
	    if(workCode == null){
	    	temp = defaultValue;
	    }
		return setWorkCode(temp);
	}
	
	/**
	*Name:收件人工作编号
	*Comment:收件人工作编号
	*/

	public ReceiveBean setWorkCodeIfNotNullAndNotEmpty(String workCode){
		if(StringUtils.isEmpty(workCode)){
	    	return this;
	    }
	    return setWorkCode(workCode);
	}
	/**
	*Name:收件人工作编号
	*Comment:收件人工作编号
	*/

	public ReceiveBean setWorkCodeWithDefaultValueWhenNullOrEmpty(String workCode,String defaultValue){
		String temp = workCode;
		if(StringUtils.isEmpty(workCode)){
	    	temp = defaultValue;
	    }
	    return setWorkCode(temp);
	}
	
	/**
	*Name:收件人姓名
	*Comment:收件人的姓名
	*/

	public ReceiveBean setLastName(String lastName){
		paramTypeMap.put("lastName", String.class);
		paramValueMap.put("lastName", lastName);
		if(this.current != null){
			current.setLastName(lastName);
		}
		return this;
	}

	/**
	*Name:收件人姓名
	*Comment:收件人的姓名
	*/

	public ReceiveBean setLastNameIfNotNull(String lastName){
	    if(lastName == null){
	    	return this;
	    }
		return setLastName(lastName);
	}
	/**
	*Name:收件人姓名
	*Comment:收件人的姓名
	*/

	public ReceiveBean setLastNameWithDefaultValueWhenNull(String lastName,String defaultValue){
		String temp = lastName;
	    if(lastName == null){
	    	temp = defaultValue;
	    }
		return setLastName(temp);
	}
	
	/**
	*Name:收件人姓名
	*Comment:收件人的姓名
	*/

	public ReceiveBean setLastNameIfNotNullAndNotEmpty(String lastName){
		if(StringUtils.isEmpty(lastName)){
	    	return this;
	    }
	    return setLastName(lastName);
	}
	/**
	*Name:收件人姓名
	*Comment:收件人的姓名
	*/

	public ReceiveBean setLastNameWithDefaultValueWhenNullOrEmpty(String lastName,String defaultValue){
		String temp = lastName;
		if(StringUtils.isEmpty(lastName)){
	    	temp = defaultValue;
	    }
	    return setLastName(temp);
	}
	
	/**
	*Name:收件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public ReceiveBean setLoginId(String loginId){
		paramTypeMap.put("loginId", String.class);
		paramValueMap.put("loginId", loginId);
		if(this.current != null){
			current.setLoginId(loginId);
		}
		return this;
	}

	/**
	*Name:收件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public ReceiveBean setLoginIdIfNotNull(String loginId){
	    if(loginId == null){
	    	return this;
	    }
		return setLoginId(loginId);
	}
	/**
	*Name:收件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public ReceiveBean setLoginIdWithDefaultValueWhenNull(String loginId,String defaultValue){
		String temp = loginId;
	    if(loginId == null){
	    	temp = defaultValue;
	    }
		return setLoginId(temp);
	}
	
	/**
	*Name:收件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public ReceiveBean setLoginIdIfNotNullAndNotEmpty(String loginId){
		if(StringUtils.isEmpty(loginId)){
	    	return this;
	    }
	    return setLoginId(loginId);
	}
	/**
	*Name:收件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public ReceiveBean setLoginIdWithDefaultValueWhenNullOrEmpty(String loginId,String defaultValue){
		String temp = loginId;
		if(StringUtils.isEmpty(loginId)){
	    	temp = defaultValue;
	    }
	    return setLoginId(temp);
	}
	
	/**
	*Name:收件人的分部全称
	*Comment:收件人的分部全称
	*/

	public ReceiveBean setSubcompanyName(String subcompanyName){
		paramTypeMap.put("subcompanyName", String.class);
		paramValueMap.put("subcompanyName", subcompanyName);
		if(this.current != null){
			current.setSubcompanyName(subcompanyName);
		}
		return this;
	}

	/**
	*Name:收件人的分部全称
	*Comment:收件人的分部全称
	*/

	public ReceiveBean setSubcompanyNameIfNotNull(String subcompanyName){
	    if(subcompanyName == null){
	    	return this;
	    }
		return setSubcompanyName(subcompanyName);
	}
	/**
	*Name:收件人的分部全称
	*Comment:收件人的分部全称
	*/

	public ReceiveBean setSubcompanyNameWithDefaultValueWhenNull(String subcompanyName,String defaultValue){
		String temp = subcompanyName;
	    if(subcompanyName == null){
	    	temp = defaultValue;
	    }
		return setSubcompanyName(temp);
	}
	
	/**
	*Name:收件人的分部全称
	*Comment:收件人的分部全称
	*/

	public ReceiveBean setSubcompanyNameIfNotNullAndNotEmpty(String subcompanyName){
		if(StringUtils.isEmpty(subcompanyName)){
	    	return this;
	    }
	    return setSubcompanyName(subcompanyName);
	}
	/**
	*Name:收件人的分部全称
	*Comment:收件人的分部全称
	*/

	public ReceiveBean setSubcompanyNameWithDefaultValueWhenNullOrEmpty(String subcompanyName,String defaultValue){
		String temp = subcompanyName;
		if(StringUtils.isEmpty(subcompanyName)){
	    	temp = defaultValue;
	    }
	    return setSubcompanyName(temp);
	}
	
	/**
	*Name:收件人的部门全称
	*Comment:收件人的部门全称
	*/

	public ReceiveBean setDepartmentName(String departmentName){
		paramTypeMap.put("departmentName", String.class);
		paramValueMap.put("departmentName", departmentName);
		if(this.current != null){
			current.setDepartmentName(departmentName);
		}
		return this;
	}

	/**
	*Name:收件人的部门全称
	*Comment:收件人的部门全称
	*/

	public ReceiveBean setDepartmentNameIfNotNull(String departmentName){
	    if(departmentName == null){
	    	return this;
	    }
		return setDepartmentName(departmentName);
	}
	/**
	*Name:收件人的部门全称
	*Comment:收件人的部门全称
	*/

	public ReceiveBean setDepartmentNameWithDefaultValueWhenNull(String departmentName,String defaultValue){
		String temp = departmentName;
	    if(departmentName == null){
	    	temp = defaultValue;
	    }
		return setDepartmentName(temp);
	}
	
	/**
	*Name:收件人的部门全称
	*Comment:收件人的部门全称
	*/

	public ReceiveBean setDepartmentNameIfNotNullAndNotEmpty(String departmentName){
		if(StringUtils.isEmpty(departmentName)){
	    	return this;
	    }
	    return setDepartmentName(departmentName);
	}
	/**
	*Name:收件人的部门全称
	*Comment:收件人的部门全称
	*/

	public ReceiveBean setDepartmentNameWithDefaultValueWhenNullOrEmpty(String departmentName,String defaultValue){
		String temp = departmentName;
		if(StringUtils.isEmpty(departmentName)){
	    	temp = defaultValue;
	    }
	    return setDepartmentName(temp);
	}
	
	/**
	*Name:接收传阅的时间
	*Comment:接收传阅的时间
	*/

	public ReceiveBean setReceiveTime(Date receiveTime){
		paramTypeMap.put("receiveTime", Date.class);
		paramValueMap.put("receiveTime", receiveTime);
		if(this.current != null){
			current.setReceiveTime(receiveTime);
		}
		return this;
	}

	/**
	*Name:接收传阅的时间
	*Comment:接收传阅的时间
	*/

	public ReceiveBean setReceiveTimeIfNotNull(Date receiveTime){
	    if(receiveTime == null){
	    	return this;
	    }
		return setReceiveTime(receiveTime);
	}
	/**
	*Name:接收传阅的时间
	*Comment:接收传阅的时间
	*/

	public ReceiveBean setReceiveTimeWithDefaultValueWhenNull(Date receiveTime,Date defaultValue){
		Date temp = receiveTime;
	    if(receiveTime == null){
	    	temp = defaultValue;
	    }
		return setReceiveTime(temp);
	}
	
	
	/**
	*Name:加入时间
	*Comment:添加联系人的时间
	*/

	public ReceiveBean setJoinTime(Date joinTime){
		paramTypeMap.put("joinTime", Date.class);
		paramValueMap.put("joinTime", joinTime);
		if(this.current != null){
			current.setJoinTime(joinTime);
		}
		return this;
	}

	/**
	*Name:加入时间
	*Comment:添加联系人的时间
	*/

	public ReceiveBean setJoinTimeIfNotNull(Date joinTime){
	    if(joinTime == null){
	    	return this;
	    }
		return setJoinTime(joinTime);
	}
	/**
	*Name:加入时间
	*Comment:添加联系人的时间
	*/

	public ReceiveBean setJoinTimeWithDefaultValueWhenNull(Date joinTime,Date defaultValue){
		Date temp = joinTime;
	    if(joinTime == null){
	    	temp = defaultValue;
	    }
		return setJoinTime(temp);
	}
	
	
	/**
	*Name:状态
	*Comment:状态: 0 未开封  1 已开封  
	*/

	public ReceiveBean setReceiveStatus(int receiveStatus){
		paramTypeMap.put("receiveStatus", ClassUtils.wrapperToPrimitive(Integer.class));
		paramValueMap.put("receiveStatus", receiveStatus);
		if(this.current != null){
			current.setReceiveStatus(receiveStatus);
		}
		return this;
	}
	
	/**
	*Name:传阅筛选状态
	*Comment:4 草稿  5 未读 6 已读  
	*/

	public ReceiveBean setMailState(Integer mailState){
		paramTypeMap.put("mailState", Integer.class);
		paramValueMap.put("mailState", mailState);
		if(this.current != null){
			current.setMailState(mailState);
		}
		return this;
	}

	/**
	*Name:传阅筛选状态
	*Comment:4 草稿  5 未读 6 已读  
	*/

	public ReceiveBean setMailStateIfNotNull(Integer mailState){
	    if(mailState == null){
	    	return this;
	    }
		return setMailState(mailState);
	}
	/**
	*Name:传阅筛选状态
	*Comment:4 草稿  5 未读 6 已读  
	*/

	public ReceiveBean setMailStateWithDefaultValueWhenNull(Integer mailState,Integer defaultValue){
		Integer temp = mailState;
	    if(mailState == null){
	    	temp = defaultValue;
	    }
		return setMailState(temp);
	}
	
	
	/**
	*Name:传阅流程状态
	*Comment:(冗余字段) 1 发阅中 2 待办传阅 3 已完成
	*/

	public ReceiveBean setStepStatus(Integer stepStatus){
		paramTypeMap.put("stepStatus", Integer.class);
		paramValueMap.put("stepStatus", stepStatus);
		if(this.current != null){
			current.setStepStatus(stepStatus);
		}
		return this;
	}

	/**
	*Name:传阅流程状态
	*Comment:(冗余字段) 1 发阅中 2 待办传阅 3 已完成
	*/

	public ReceiveBean setStepStatusIfNotNull(Integer stepStatus){
	    if(stepStatus == null){
	    	return this;
	    }
		return setStepStatus(stepStatus);
	}
	/**
	*Name:传阅流程状态
	*Comment:(冗余字段) 1 发阅中 2 待办传阅 3 已完成
	*/

	public ReceiveBean setStepStatusWithDefaultValueWhenNull(Integer stepStatus,Integer defaultValue){
		Integer temp = stepStatus;
	    if(stepStatus == null){
	    	temp = defaultValue;
	    }
		return setStepStatus(temp);
	}
	
	
	/**
	*Name:开封时间
	*Comment:记录打开传阅的时间
	*/

	public ReceiveBean setOpenTime(Date openTime){
		paramTypeMap.put("openTime", Date.class);
		paramValueMap.put("openTime", openTime);
		if(this.current != null){
			current.setOpenTime(openTime);
		}
		return this;
	}

	/**
	*Name:开封时间
	*Comment:记录打开传阅的时间
	*/

	public ReceiveBean setOpenTimeIfNotNull(Date openTime){
	    if(openTime == null){
	    	return this;
	    }
		return setOpenTime(openTime);
	}
	/**
	*Name:开封时间
	*Comment:记录打开传阅的时间
	*/

	public ReceiveBean setOpenTimeWithDefaultValueWhenNull(Date openTime,Date defaultValue){
		Date temp = openTime;
	    if(openTime == null){
	    	temp = defaultValue;
	    }
		return setOpenTime(temp);
	}
	
	
	/**
	*Name:是否确认
	*Comment:false 未确认 true 已确认 (传阅开封, 不一定是传阅确认   但传阅确认必须是传阅开封
)
	*/

	public ReceiveBean setIfConfirm(Boolean ifConfirm){
		paramTypeMap.put("ifConfirm", Boolean.class);
		paramValueMap.put("ifConfirm", ifConfirm);
		if(this.current != null){
			current.setIfConfirm(ifConfirm);
		}
		return this;
	}

	/**
	*Name:是否确认
	*Comment:false 未确认 true 已确认 (传阅开封, 不一定是传阅确认   但传阅确认必须是传阅开封
)
	*/

	public ReceiveBean setIfConfirmIfNotNull(Boolean ifConfirm){
	    if(ifConfirm == null){
	    	return this;
	    }
		return setIfConfirm(ifConfirm);
	}
	/**
	*Name:是否确认
	*Comment:false 未确认 true 已确认 (传阅开封, 不一定是传阅确认   但传阅确认必须是传阅开封
)
	*/

	public ReceiveBean setIfConfirmWithDefaultValueWhenNull(Boolean ifConfirm,Boolean defaultValue){
		Boolean temp = ifConfirm;
	    if(ifConfirm == null){
	    	temp = defaultValue;
	    }
		return setIfConfirm(temp);
	}
	
	
	/**
	*Name:确认时间
	*Comment:确认时间
	*/

	public ReceiveBean setAffirmTime(Date affirmTime){
		paramTypeMap.put("affirmTime", Date.class);
		paramValueMap.put("affirmTime", affirmTime);
		if(this.current != null){
			current.setAffirmTime(affirmTime);
		}
		return this;
	}

	/**
	*Name:确认时间
	*Comment:确认时间
	*/

	public ReceiveBean setAffirmTimeIfNotNull(Date affirmTime){
	    if(affirmTime == null){
	    	return this;
	    }
		return setAffirmTime(affirmTime);
	}
	/**
	*Name:确认时间
	*Comment:确认时间
	*/

	public ReceiveBean setAffirmTimeWithDefaultValueWhenNull(Date affirmTime,Date defaultValue){
		Date temp = affirmTime;
	    if(affirmTime == null){
	    	temp = defaultValue;
	    }
		return setAffirmTime(temp);
	}
	
	
	/**
	*Name:确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注
	*/

	public ReceiveBean setRemark(String remark){
		paramTypeMap.put("remark", String.class);
		paramValueMap.put("remark", remark);
		if(this.current != null){
			current.setRemark(remark);
		}
		return this;
	}

	/**
	*Name:确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注
	*/

	public ReceiveBean setRemarkIfNotNull(String remark){
	    if(remark == null){
	    	return this;
	    }
		return setRemark(remark);
	}
	/**
	*Name:确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注
	*/

	public ReceiveBean setRemarkWithDefaultValueWhenNull(String remark,String defaultValue){
		String temp = remark;
	    if(remark == null){
	    	temp = defaultValue;
	    }
		return setRemark(temp);
	}
	
	/**
	*Name:确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注
	*/

	public ReceiveBean setRemarkIfNotNullAndNotEmpty(String remark){
		if(StringUtils.isEmpty(remark)){
	    	return this;
	    }
	    return setRemark(remark);
	}
	/**
	*Name:确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注
	*/

	public ReceiveBean setRemarkWithDefaultValueWhenNullOrEmpty(String remark,String defaultValue){
		String temp = remark;
		if(StringUtils.isEmpty(remark)){
	    	temp = defaultValue;
	    }
	    return setRemark(temp);
	}
	
	/**
	*Name:确认/标识
	*Comment:该字段用于记录收件人在确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setConfirmRecord(String confirmRecord){
		paramTypeMap.put("confirmRecord", String.class);
		paramValueMap.put("confirmRecord", confirmRecord);
		if(this.current != null){
			current.setConfirmRecord(confirmRecord);
		}
		return this;
	}

	/**
	*Name:确认/标识
	*Comment:该字段用于记录收件人在确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setConfirmRecordIfNotNull(String confirmRecord){
	    if(confirmRecord == null){
	    	return this;
	    }
		return setConfirmRecord(confirmRecord);
	}
	/**
	*Name:确认/标识
	*Comment:该字段用于记录收件人在确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setConfirmRecordWithDefaultValueWhenNull(String confirmRecord,String defaultValue){
		String temp = confirmRecord;
	    if(confirmRecord == null){
	    	temp = defaultValue;
	    }
		return setConfirmRecord(temp);
	}
	
	/**
	*Name:确认/标识
	*Comment:该字段用于记录收件人在确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setConfirmRecordIfNotNullAndNotEmpty(String confirmRecord){
		if(StringUtils.isEmpty(confirmRecord)){
	    	return this;
	    }
	    return setConfirmRecord(confirmRecord);
	}
	/**
	*Name:确认/标识
	*Comment:该字段用于记录收件人在确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setConfirmRecordWithDefaultValueWhenNullOrEmpty(String confirmRecord,String defaultValue){
		String temp = confirmRecord;
		if(StringUtils.isEmpty(confirmRecord)){
	    	temp = defaultValue;
	    }
	    return setConfirmRecord(temp);
	}
	
	/**
	*Name:序号
	*Comment:序号
	*/

	public ReceiveBean setSerialNum(Integer serialNum){
		paramTypeMap.put("serialNum", Integer.class);
		paramValueMap.put("serialNum", serialNum);
		if(this.current != null){
			current.setSerialNum(serialNum);
		}
		return this;
	}

	/**
	*Name:序号
	*Comment:序号
	*/

	public ReceiveBean setSerialNumIfNotNull(Integer serialNum){
	    if(serialNum == null){
	    	return this;
	    }
		return setSerialNum(serialNum);
	}
	/**
	*Name:序号
	*Comment:序号
	*/

	public ReceiveBean setSerialNumWithDefaultValueWhenNull(Integer serialNum,Integer defaultValue){
		Integer temp = serialNum;
	    if(serialNum == null){
	    	temp = defaultValue;
	    }
		return setSerialNum(temp);
	}
	
	
	/**
	*Name:是否重新确认
	*Comment:该字段用于重新确认传阅, false 未重新确认 true 重新确认 
	*/

	public ReceiveBean setAfreshConfim(Boolean afreshConfim){
		paramTypeMap.put("afreshConfim", Boolean.class);
		paramValueMap.put("afreshConfim", afreshConfim);
		if(this.current != null){
			current.setAfreshConfim(afreshConfim);
		}
		return this;
	}

	/**
	*Name:是否重新确认
	*Comment:该字段用于重新确认传阅, false 未重新确认 true 重新确认 
	*/

	public ReceiveBean setAfreshConfimIfNotNull(Boolean afreshConfim){
	    if(afreshConfim == null){
	    	return this;
	    }
		return setAfreshConfim(afreshConfim);
	}
	/**
	*Name:是否重新确认
	*Comment:该字段用于重新确认传阅, false 未重新确认 true 重新确认 
	*/

	public ReceiveBean setAfreshConfimWithDefaultValueWhenNull(Boolean afreshConfim,Boolean defaultValue){
		Boolean temp = afreshConfim;
	    if(afreshConfim == null){
	    	temp = defaultValue;
	    }
		return setAfreshConfim(temp);
	}
	
	
	/**
	*Name:(重新)确认/标识
	*Comment:该字段用于记录收件人在重新 确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setAcRecord(String acRecord){
		paramTypeMap.put("acRecord", String.class);
		paramValueMap.put("acRecord", acRecord);
		if(this.current != null){
			current.setAcRecord(acRecord);
		}
		return this;
	}

	/**
	*Name:(重新)确认/标识
	*Comment:该字段用于记录收件人在重新 确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setAcRecordIfNotNull(String acRecord){
	    if(acRecord == null){
	    	return this;
	    }
		return setAcRecord(acRecord);
	}
	/**
	*Name:(重新)确认/标识
	*Comment:该字段用于记录收件人在重新 确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setAcRecordWithDefaultValueWhenNull(String acRecord,String defaultValue){
		String temp = acRecord;
	    if(acRecord == null){
	    	temp = defaultValue;
	    }
		return setAcRecord(temp);
	}
	
	/**
	*Name:(重新)确认/标识
	*Comment:该字段用于记录收件人在重新 确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setAcRecordIfNotNullAndNotEmpty(String acRecord){
		if(StringUtils.isEmpty(acRecord)){
	    	return this;
	    }
	    return setAcRecord(acRecord);
	}
	/**
	*Name:(重新)确认/标识
	*Comment:该字段用于记录收件人在重新 确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveBean setAcRecordWithDefaultValueWhenNullOrEmpty(String acRecord,String defaultValue){
		String temp = acRecord;
		if(StringUtils.isEmpty(acRecord)){
	    	temp = defaultValue;
	    }
	    return setAcRecord(temp);
	}
	
	/**
	*Name:(重新)确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注(重新)
	*/

	public ReceiveBean setAfreshRemark(String afreshRemark){
		paramTypeMap.put("afreshRemark", String.class);
		paramValueMap.put("afreshRemark", afreshRemark);
		if(this.current != null){
			current.setAfreshRemark(afreshRemark);
		}
		return this;
	}

	/**
	*Name:(重新)确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注(重新)
	*/

	public ReceiveBean setAfreshRemarkIfNotNull(String afreshRemark){
	    if(afreshRemark == null){
	    	return this;
	    }
		return setAfreshRemark(afreshRemark);
	}
	/**
	*Name:(重新)确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注(重新)
	*/

	public ReceiveBean setAfreshRemarkWithDefaultValueWhenNull(String afreshRemark,String defaultValue){
		String temp = afreshRemark;
	    if(afreshRemark == null){
	    	temp = defaultValue;
	    }
		return setAfreshRemark(temp);
	}
	
	/**
	*Name:(重新)确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注(重新)
	*/

	public ReceiveBean setAfreshRemarkIfNotNullAndNotEmpty(String afreshRemark){
		if(StringUtils.isEmpty(afreshRemark)){
	    	return this;
	    }
	    return setAfreshRemark(afreshRemark);
	}
	/**
	*Name:(重新)确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注(重新)
	*/

	public ReceiveBean setAfreshRemarkWithDefaultValueWhenNullOrEmpty(String afreshRemark,String defaultValue){
		String temp = afreshRemark;
		if(StringUtils.isEmpty(afreshRemark)){
	    	temp = defaultValue;
	    }
	    return setAfreshRemark(temp);
	}
	
	/**
	*Name:(重新)确认时间
	*Comment:确认时间(重新)
	*/

	public ReceiveBean setMhTime(Date mhTime){
		paramTypeMap.put("mhTime", Date.class);
		paramValueMap.put("mhTime", mhTime);
		if(this.current != null){
			current.setMhTime(mhTime);
		}
		return this;
	}

	/**
	*Name:(重新)确认时间
	*Comment:确认时间(重新)
	*/

	public ReceiveBean setMhTimeIfNotNull(Date mhTime){
	    if(mhTime == null){
	    	return this;
	    }
		return setMhTime(mhTime);
	}
	/**
	*Name:(重新)确认时间
	*Comment:确认时间(重新)
	*/

	public ReceiveBean setMhTimeWithDefaultValueWhenNull(Date mhTime,Date defaultValue){
		Date temp = mhTime;
	    if(mhTime == null){
	    	temp = defaultValue;
	    }
		return setMhTime(temp);
	}
	
	
	/**
	*Name:是否关注
	*Comment:收件人的关注状态:  0 为未关注 （false），1 已关注（true）
	*/

	public ReceiveBean setReceiveAttention(Boolean receiveAttention){
		paramTypeMap.put("receiveAttention", Boolean.class);
		paramValueMap.put("receiveAttention", receiveAttention);
		if(this.current != null){
			current.setReceiveAttention(receiveAttention);
		}
		return this;
	}

	/**
	*Name:是否关注
	*Comment:收件人的关注状态:  0 为未关注 （false），1 已关注（true）
	*/

	public ReceiveBean setReceiveAttentionIfNotNull(Boolean receiveAttention){
	    if(receiveAttention == null){
	    	return this;
	    }
		return setReceiveAttention(receiveAttention);
	}
	/**
	*Name:是否关注
	*Comment:收件人的关注状态:  0 为未关注 （false），1 已关注（true）
	*/

	public ReceiveBean setReceiveAttentionWithDefaultValueWhenNull(Boolean receiveAttention,Boolean defaultValue){
		Boolean temp = receiveAttention;
	    if(receiveAttention == null){
	    	temp = defaultValue;
	    }
		return setReceiveAttention(temp);
	}
	
	
	/**
	*Name:区别
	*Comment:区别是谁添加的联系人, 存放添加该联系人的用户ID
	*/

	public ReceiveBean setReDifferentiate(Long reDifferentiate){
		paramTypeMap.put("reDifferentiate", Long.class);
		paramValueMap.put("reDifferentiate", reDifferentiate);
		if(this.current != null){
			current.setReDifferentiate(reDifferentiate);
		}
		return this;
	}

	/**
	*Name:区别
	*Comment:区别是谁添加的联系人, 存放添加该联系人的用户ID
	*/

	public ReceiveBean setReDifferentiateIfNotNull(Long reDifferentiate){
	    if(reDifferentiate == null){
	    	return this;
	    }
		return setReDifferentiate(reDifferentiate);
	}
	/**
	*Name:区别
	*Comment:区别是谁添加的联系人, 存放添加该联系人的用户ID
	*/

	public ReceiveBean setReDifferentiateWithDefaultValueWhenNull(Long reDifferentiate,Long defaultValue){
		Long temp = reDifferentiate;
	    if(reDifferentiate == null){
	    	temp = defaultValue;
	    }
		return setReDifferentiate(temp);
	}
	
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<Receive> update(){
		return this.helper.each(new Each<Receive>(){

			@Override
			public void each(Receive bean, List<Receive> list) {
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
	public Receive updateUnique(){
		Receive bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<Receive>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(Receive bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<Receive> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private Receive current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public ReceiveBean create(){
		Receive bean = new Receive();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<Receive> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (Receive bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public Receive insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		Receive bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public Receive ifFoundUpdateElseInsert(){
		Receive bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new Receive();
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
		Receive bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<Receive> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Receive bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<Receive> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<Receive> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<Receive> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<Receive> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<Receive> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<Receive> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (Receive bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			Receive bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(Receive bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = Receive.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
