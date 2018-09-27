package com.sunnsoft.sloa.helper;

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
public class UserMssageBean {
	private UserMssageHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<UserMssage> insertBeans = new ArrayList<UserMssage>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = UserMssageBean.class.getDeclaredMethods();
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
	private UserMssageBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public UserMssageBean copyValueForm(Object source){
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
	public UserMssageBean copyNotNullValueForm(Object source){
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
	public UserMssageBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public UserMssageBean(UserMssageHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:用户信息id
	*Comment:用户信息ID
	*/

	public UserMssageBean setUserMssageId(Long userMssageId){
		paramTypeMap.put("userMssageId", Long.class);
		paramValueMap.put("userMssageId", userMssageId);
		if(this.current != null){
			current.setUserMssageId(userMssageId);
		}
		return this;
	}

	/**
	*Name:用户信息id
	*Comment:用户信息ID
	*/

	public UserMssageBean setUserMssageIdIfNotNull(Long userMssageId){
	    if(userMssageId == null){
	    	return this;
	    }
		return setUserMssageId(userMssageId);
	}
	/**
	*Name:用户信息id
	*Comment:用户信息ID
	*/

	public UserMssageBean setUserMssageIdWithDefaultValueWhenNull(Long userMssageId,Long defaultValue){
		Long temp = userMssageId;
	    if(userMssageId == null){
	    	temp = defaultValue;
	    }
		return setUserMssageId(temp);
	}
	
	
	/**
	*Name:用户ID
	*Comment:用户ID
	*/

	public UserMssageBean setUserId(Integer userId){
		paramTypeMap.put("userId", Integer.class);
		paramValueMap.put("userId", userId);
		if(this.current != null){
			current.setUserId(userId);
		}
		return this;
	}

	/**
	*Name:用户ID
	*Comment:用户ID
	*/

	public UserMssageBean setUserIdIfNotNull(Integer userId){
	    if(userId == null){
	    	return this;
	    }
		return setUserId(userId);
	}
	/**
	*Name:用户ID
	*Comment:用户ID
	*/

	public UserMssageBean setUserIdWithDefaultValueWhenNull(Integer userId,Integer defaultValue){
		Integer temp = userId;
	    if(userId == null){
	    	temp = defaultValue;
	    }
		return setUserId(temp);
	}
	
	
	/**
	*Name:用户编号
	*Comment:用户工作编号
	*/

	public UserMssageBean setWorkCode(String workCode){
		paramTypeMap.put("workCode", String.class);
		paramValueMap.put("workCode", workCode);
		if(this.current != null){
			current.setWorkCode(workCode);
		}
		return this;
	}

	/**
	*Name:用户编号
	*Comment:用户工作编号
	*/

	public UserMssageBean setWorkCodeIfNotNull(String workCode){
	    if(workCode == null){
	    	return this;
	    }
		return setWorkCode(workCode);
	}
	/**
	*Name:用户编号
	*Comment:用户工作编号
	*/

	public UserMssageBean setWorkCodeWithDefaultValueWhenNull(String workCode,String defaultValue){
		String temp = workCode;
	    if(workCode == null){
	    	temp = defaultValue;
	    }
		return setWorkCode(temp);
	}
	
	/**
	*Name:用户编号
	*Comment:用户工作编号
	*/

	public UserMssageBean setWorkCodeIfNotNullAndNotEmpty(String workCode){
		if(StringUtils.isEmpty(workCode)){
	    	return this;
	    }
	    return setWorkCode(workCode);
	}
	/**
	*Name:用户编号
	*Comment:用户工作编号
	*/

	public UserMssageBean setWorkCodeWithDefaultValueWhenNullOrEmpty(String workCode,String defaultValue){
		String temp = workCode;
		if(StringUtils.isEmpty(workCode)){
	    	temp = defaultValue;
	    }
	    return setWorkCode(temp);
	}
	
	/**
	*Name:用户姓名
	*Comment:用户的姓名
	*/

	public UserMssageBean setLastName(String lastName){
		paramTypeMap.put("lastName", String.class);
		paramValueMap.put("lastName", lastName);
		if(this.current != null){
			current.setLastName(lastName);
		}
		return this;
	}

	/**
	*Name:用户姓名
	*Comment:用户的姓名
	*/

	public UserMssageBean setLastNameIfNotNull(String lastName){
	    if(lastName == null){
	    	return this;
	    }
		return setLastName(lastName);
	}
	/**
	*Name:用户姓名
	*Comment:用户的姓名
	*/

	public UserMssageBean setLastNameWithDefaultValueWhenNull(String lastName,String defaultValue){
		String temp = lastName;
	    if(lastName == null){
	    	temp = defaultValue;
	    }
		return setLastName(temp);
	}
	
	/**
	*Name:用户姓名
	*Comment:用户的姓名
	*/

	public UserMssageBean setLastNameIfNotNullAndNotEmpty(String lastName){
		if(StringUtils.isEmpty(lastName)){
	    	return this;
	    }
	    return setLastName(lastName);
	}
	/**
	*Name:用户姓名
	*Comment:用户的姓名
	*/

	public UserMssageBean setLastNameWithDefaultValueWhenNullOrEmpty(String lastName,String defaultValue){
		String temp = lastName;
		if(StringUtils.isEmpty(lastName)){
	    	temp = defaultValue;
	    }
	    return setLastName(temp);
	}
	
	/**
	*Name:用户登录名
	*Comment:系统账号(登录名)
	*/

	public UserMssageBean setLoginId(String loginId){
		paramTypeMap.put("loginId", String.class);
		paramValueMap.put("loginId", loginId);
		if(this.current != null){
			current.setLoginId(loginId);
		}
		return this;
	}

	/**
	*Name:用户登录名
	*Comment:系统账号(登录名)
	*/

	public UserMssageBean setLoginIdIfNotNull(String loginId){
	    if(loginId == null){
	    	return this;
	    }
		return setLoginId(loginId);
	}
	/**
	*Name:用户登录名
	*Comment:系统账号(登录名)
	*/

	public UserMssageBean setLoginIdWithDefaultValueWhenNull(String loginId,String defaultValue){
		String temp = loginId;
	    if(loginId == null){
	    	temp = defaultValue;
	    }
		return setLoginId(temp);
	}
	
	/**
	*Name:用户登录名
	*Comment:系统账号(登录名)
	*/

	public UserMssageBean setLoginIdIfNotNullAndNotEmpty(String loginId){
		if(StringUtils.isEmpty(loginId)){
	    	return this;
	    }
	    return setLoginId(loginId);
	}
	/**
	*Name:用户登录名
	*Comment:系统账号(登录名)
	*/

	public UserMssageBean setLoginIdWithDefaultValueWhenNullOrEmpty(String loginId,String defaultValue){
		String temp = loginId;
		if(StringUtils.isEmpty(loginId)){
	    	temp = defaultValue;
	    }
	    return setLoginId(temp);
	}
	
	/**
	*Name:部门全称
	*Comment:部门全称
	*/

	public UserMssageBean setDeptFullname(String deptFullname){
		paramTypeMap.put("deptFullname", String.class);
		paramValueMap.put("deptFullname", deptFullname);
		if(this.current != null){
			current.setDeptFullname(deptFullname);
		}
		return this;
	}

	/**
	*Name:部门全称
	*Comment:部门全称
	*/

	public UserMssageBean setDeptFullnameIfNotNull(String deptFullname){
	    if(deptFullname == null){
	    	return this;
	    }
		return setDeptFullname(deptFullname);
	}
	/**
	*Name:部门全称
	*Comment:部门全称
	*/

	public UserMssageBean setDeptFullnameWithDefaultValueWhenNull(String deptFullname,String defaultValue){
		String temp = deptFullname;
	    if(deptFullname == null){
	    	temp = defaultValue;
	    }
		return setDeptFullname(temp);
	}
	
	/**
	*Name:部门全称
	*Comment:部门全称
	*/

	public UserMssageBean setDeptFullnameIfNotNullAndNotEmpty(String deptFullname){
		if(StringUtils.isEmpty(deptFullname)){
	    	return this;
	    }
	    return setDeptFullname(deptFullname);
	}
	/**
	*Name:部门全称
	*Comment:部门全称
	*/

	public UserMssageBean setDeptFullnameWithDefaultValueWhenNullOrEmpty(String deptFullname,String defaultValue){
		String temp = deptFullname;
		if(StringUtils.isEmpty(deptFullname)){
	    	temp = defaultValue;
	    }
	    return setDeptFullname(temp);
	}
	
	/**
	*Name:分部全称
	*Comment:分部全称
	*/

	public UserMssageBean setFullName(String fullName){
		paramTypeMap.put("fullName", String.class);
		paramValueMap.put("fullName", fullName);
		if(this.current != null){
			current.setFullName(fullName);
		}
		return this;
	}

	/**
	*Name:分部全称
	*Comment:分部全称
	*/

	public UserMssageBean setFullNameIfNotNull(String fullName){
	    if(fullName == null){
	    	return this;
	    }
		return setFullName(fullName);
	}
	/**
	*Name:分部全称
	*Comment:分部全称
	*/

	public UserMssageBean setFullNameWithDefaultValueWhenNull(String fullName,String defaultValue){
		String temp = fullName;
	    if(fullName == null){
	    	temp = defaultValue;
	    }
		return setFullName(temp);
	}
	
	/**
	*Name:分部全称
	*Comment:分部全称
	*/

	public UserMssageBean setFullNameIfNotNullAndNotEmpty(String fullName){
		if(StringUtils.isEmpty(fullName)){
	    	return this;
	    }
	    return setFullName(fullName);
	}
	/**
	*Name:分部全称
	*Comment:分部全称
	*/

	public UserMssageBean setFullNameWithDefaultValueWhenNullOrEmpty(String fullName,String defaultValue){
		String temp = fullName;
		if(StringUtils.isEmpty(fullName)){
	    	temp = defaultValue;
	    }
	    return setFullName(temp);
	}
	
	/**
	*Name:用户部门ID
	*Comment:用户部门ID
	*/

	public UserMssageBean setDepartmentId(String departmentId){
		paramTypeMap.put("departmentId", String.class);
		paramValueMap.put("departmentId", departmentId);
		if(this.current != null){
			current.setDepartmentId(departmentId);
		}
		return this;
	}

	/**
	*Name:用户部门ID
	*Comment:用户部门ID
	*/

	public UserMssageBean setDepartmentIdIfNotNull(String departmentId){
	    if(departmentId == null){
	    	return this;
	    }
		return setDepartmentId(departmentId);
	}
	/**
	*Name:用户部门ID
	*Comment:用户部门ID
	*/

	public UserMssageBean setDepartmentIdWithDefaultValueWhenNull(String departmentId,String defaultValue){
		String temp = departmentId;
	    if(departmentId == null){
	    	temp = defaultValue;
	    }
		return setDepartmentId(temp);
	}
	
	/**
	*Name:用户部门ID
	*Comment:用户部门ID
	*/

	public UserMssageBean setDepartmentIdIfNotNullAndNotEmpty(String departmentId){
		if(StringUtils.isEmpty(departmentId)){
	    	return this;
	    }
	    return setDepartmentId(departmentId);
	}
	/**
	*Name:用户部门ID
	*Comment:用户部门ID
	*/

	public UserMssageBean setDepartmentIdWithDefaultValueWhenNullOrEmpty(String departmentId,String defaultValue){
		String temp = departmentId;
		if(StringUtils.isEmpty(departmentId)){
	    	temp = defaultValue;
	    }
	    return setDepartmentId(temp);
	}
	
	/**
	*Name:用户分部ID
	*Comment:用户分部ID
	*/

	public UserMssageBean setSubcompanyId1(String subcompanyId1){
		paramTypeMap.put("subcompanyId1", String.class);
		paramValueMap.put("subcompanyId1", subcompanyId1);
		if(this.current != null){
			current.setSubcompanyId1(subcompanyId1);
		}
		return this;
	}

	/**
	*Name:用户分部ID
	*Comment:用户分部ID
	*/

	public UserMssageBean setSubcompanyId1IfNotNull(String subcompanyId1){
	    if(subcompanyId1 == null){
	    	return this;
	    }
		return setSubcompanyId1(subcompanyId1);
	}
	/**
	*Name:用户分部ID
	*Comment:用户分部ID
	*/

	public UserMssageBean setSubcompanyId1WithDefaultValueWhenNull(String subcompanyId1,String defaultValue){
		String temp = subcompanyId1;
	    if(subcompanyId1 == null){
	    	temp = defaultValue;
	    }
		return setSubcompanyId1(temp);
	}
	
	/**
	*Name:用户分部ID
	*Comment:用户分部ID
	*/

	public UserMssageBean setSubcompanyId1IfNotNullAndNotEmpty(String subcompanyId1){
		if(StringUtils.isEmpty(subcompanyId1)){
	    	return this;
	    }
	    return setSubcompanyId1(subcompanyId1);
	}
	/**
	*Name:用户分部ID
	*Comment:用户分部ID
	*/

	public UserMssageBean setSubcompanyId1WithDefaultValueWhenNullOrEmpty(String subcompanyId1,String defaultValue){
		String temp = subcompanyId1;
		if(StringUtils.isEmpty(subcompanyId1)){
	    	temp = defaultValue;
	    }
	    return setSubcompanyId1(temp);
	}
	
	/**
	*Name:用户状态
	*Comment:用户状态
	*/

	public UserMssageBean setStatus(String status){
		paramTypeMap.put("status", String.class);
		paramValueMap.put("status", status);
		if(this.current != null){
			current.setStatus(status);
		}
		return this;
	}

	/**
	*Name:用户状态
	*Comment:用户状态
	*/

	public UserMssageBean setStatusIfNotNull(String status){
	    if(status == null){
	    	return this;
	    }
		return setStatus(status);
	}
	/**
	*Name:用户状态
	*Comment:用户状态
	*/

	public UserMssageBean setStatusWithDefaultValueWhenNull(String status,String defaultValue){
		String temp = status;
	    if(status == null){
	    	temp = defaultValue;
	    }
		return setStatus(temp);
	}
	
	/**
	*Name:用户状态
	*Comment:用户状态
	*/

	public UserMssageBean setStatusIfNotNullAndNotEmpty(String status){
		if(StringUtils.isEmpty(status)){
	    	return this;
	    }
	    return setStatus(status);
	}
	/**
	*Name:用户状态
	*Comment:用户状态
	*/

	public UserMssageBean setStatusWithDefaultValueWhenNullOrEmpty(String status,String defaultValue){
		String temp = status;
		if(StringUtils.isEmpty(status)){
	    	temp = defaultValue;
	    }
	    return setStatus(temp);
	}
	
	/**
	*Name:显示顺序
	*Comment:显示顺序
	*/

	public UserMssageBean setDsporder(Float dsporder){
		paramTypeMap.put("dsporder", Float.class);
		paramValueMap.put("dsporder", dsporder);
		if(this.current != null){
			current.setDsporder(dsporder);
		}
		return this;
	}

	/**
	*Name:显示顺序
	*Comment:显示顺序
	*/

	public UserMssageBean setDsporderIfNotNull(Float dsporder){
	    if(dsporder == null){
	    	return this;
	    }
		return setDsporder(dsporder);
	}
	/**
	*Name:显示顺序
	*Comment:显示顺序
	*/

	public UserMssageBean setDsporderWithDefaultValueWhenNull(Float dsporder,Float defaultValue){
		Float temp = dsporder;
	    if(dsporder == null){
	    	temp = defaultValue;
	    }
		return setDsporder(temp);
	}
	
	
	/**
	*Name:网盘session
	*Comment:网盘session
	*/

	public UserMssageBean setBoxSession(String boxSession){
		paramTypeMap.put("boxSession", String.class);
		paramValueMap.put("boxSession", boxSession);
		if(this.current != null){
			current.setBoxSession(boxSession);
		}
		return this;
	}

	/**
	*Name:网盘session
	*Comment:网盘session
	*/

	public UserMssageBean setBoxSessionIfNotNull(String boxSession){
	    if(boxSession == null){
	    	return this;
	    }
		return setBoxSession(boxSession);
	}
	/**
	*Name:网盘session
	*Comment:网盘session
	*/

	public UserMssageBean setBoxSessionWithDefaultValueWhenNull(String boxSession,String defaultValue){
		String temp = boxSession;
	    if(boxSession == null){
	    	temp = defaultValue;
	    }
		return setBoxSession(temp);
	}
	
	/**
	*Name:网盘session
	*Comment:网盘session
	*/

	public UserMssageBean setBoxSessionIfNotNullAndNotEmpty(String boxSession){
		if(StringUtils.isEmpty(boxSession)){
	    	return this;
	    }
	    return setBoxSession(boxSession);
	}
	/**
	*Name:网盘session
	*Comment:网盘session
	*/

	public UserMssageBean setBoxSessionWithDefaultValueWhenNullOrEmpty(String boxSession,String defaultValue){
		String temp = boxSession;
		if(StringUtils.isEmpty(boxSession)){
	    	temp = defaultValue;
	    }
	    return setBoxSession(temp);
	}
	
	/**
	*Name:创建时间
	*Comment:创建时间
	*/

	public UserMssageBean setCreateTime(Date createTime){
		paramTypeMap.put("createTime", Date.class);
		paramValueMap.put("createTime", createTime);
		if(this.current != null){
			current.setCreateTime(createTime);
		}
		return this;
	}

	/**
	*Name:创建时间
	*Comment:创建时间
	*/

	public UserMssageBean setCreateTimeIfNotNull(Date createTime){
	    if(createTime == null){
	    	return this;
	    }
		return setCreateTime(createTime);
	}
	/**
	*Name:创建时间
	*Comment:创建时间
	*/

	public UserMssageBean setCreateTimeWithDefaultValueWhenNull(Date createTime,Date defaultValue){
		Date temp = createTime;
	    if(createTime == null){
	    	temp = defaultValue;
	    }
		return setCreateTime(temp);
	}
	
	
	/**
	*Name:更新时间
	*Comment:更新时间
	*/

	public UserMssageBean setUpdateTime(Date updateTime){
		paramTypeMap.put("updateTime", Date.class);
		paramValueMap.put("updateTime", updateTime);
		if(this.current != null){
			current.setUpdateTime(updateTime);
		}
		return this;
	}

	/**
	*Name:更新时间
	*Comment:更新时间
	*/

	public UserMssageBean setUpdateTimeIfNotNull(Date updateTime){
	    if(updateTime == null){
	    	return this;
	    }
		return setUpdateTime(updateTime);
	}
	/**
	*Name:更新时间
	*Comment:更新时间
	*/

	public UserMssageBean setUpdateTimeWithDefaultValueWhenNull(Date updateTime,Date defaultValue){
		Date temp = updateTime;
	    if(updateTime == null){
	    	temp = defaultValue;
	    }
		return setUpdateTime(temp);
	}
	
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<UserMssage> update(){
		return this.helper.each(new Each<UserMssage>(){

			@Override
			public void each(UserMssage bean, List<UserMssage> list) {
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
	public UserMssage updateUnique(){
		UserMssage bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<UserMssage>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(UserMssage bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<UserMssage> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private UserMssage current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public UserMssageBean create(){
		UserMssage bean = new UserMssage();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<UserMssage> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (UserMssage bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public UserMssage insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		UserMssage bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public UserMssage ifFoundUpdateElseInsert(){
		UserMssage bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new UserMssage();
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
		UserMssage bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<UserMssage> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		UserMssage bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<UserMssage> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<UserMssage> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<UserMssage> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<UserMssage> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<UserMssage> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<UserMssage> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (UserMssage bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			UserMssage bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(UserMssage bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = UserMssage.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
