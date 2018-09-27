package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.db.vo.UserInfo;
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
public class UserInfoBean {
	private UserInfoHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<UserInfo> insertBeans = new ArrayList<UserInfo>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = UserInfoBean.class.getDeclaredMethods();
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
	private UserInfoBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public UserInfoBean copyValueForm(Object source){
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
	public UserInfoBean copyNotNullValueForm(Object source){
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
	public UserInfoBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public UserInfoBean(UserInfoHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:用户ID
	*Comment:
	*/

	public UserInfoBean setUserId(long userId){
		paramTypeMap.put("userId", ClassUtils.wrapperToPrimitive(Long.class));
		paramValueMap.put("userId", userId);
		if(this.current != null){
			current.setUserId(userId);
		}
		return this;
	}
	

	public UserInfoBean setUser(User user){
		paramTypeMap.put("user", User.class);
		paramValueMap.put("user", user);
		if(this.current != null){
			current.setUser(user);
		}
		return this;
	}


	public UserInfoBean setUserIfNotNull(User user){
	    if(user == null){
	    	return this;
	    }
		return setUser(user);
	}

	public UserInfoBean setUserWithDefaultValueWhenNull(User user,User defaultValue){
		User temp = user;
	    if(user == null){
	    	temp = defaultValue;
	    }
		return setUser(temp);
	}
	
	
	/**
	*Name:姓
	*Comment:
	*/

	public UserInfoBean setLastName(String lastName){
		paramTypeMap.put("lastName", String.class);
		paramValueMap.put("lastName", lastName);
		if(this.current != null){
			current.setLastName(lastName);
		}
		return this;
	}

	/**
	*Name:姓
	*Comment:
	*/

	public UserInfoBean setLastNameIfNotNull(String lastName){
	    if(lastName == null){
	    	return this;
	    }
		return setLastName(lastName);
	}
	/**
	*Name:姓
	*Comment:
	*/

	public UserInfoBean setLastNameWithDefaultValueWhenNull(String lastName,String defaultValue){
		String temp = lastName;
	    if(lastName == null){
	    	temp = defaultValue;
	    }
		return setLastName(temp);
	}
	
	/**
	*Name:姓
	*Comment:
	*/

	public UserInfoBean setLastNameIfNotNullAndNotEmpty(String lastName){
		if(StringUtils.isEmpty(lastName)){
	    	return this;
	    }
	    return setLastName(lastName);
	}
	/**
	*Name:姓
	*Comment:
	*/

	public UserInfoBean setLastNameWithDefaultValueWhenNullOrEmpty(String lastName,String defaultValue){
		String temp = lastName;
		if(StringUtils.isEmpty(lastName)){
	    	temp = defaultValue;
	    }
	    return setLastName(temp);
	}
	
	/**
	*Name:名
	*Comment:
	*/

	public UserInfoBean setFirstName(String firstName){
		paramTypeMap.put("firstName", String.class);
		paramValueMap.put("firstName", firstName);
		if(this.current != null){
			current.setFirstName(firstName);
		}
		return this;
	}

	/**
	*Name:名
	*Comment:
	*/

	public UserInfoBean setFirstNameIfNotNull(String firstName){
	    if(firstName == null){
	    	return this;
	    }
		return setFirstName(firstName);
	}
	/**
	*Name:名
	*Comment:
	*/

	public UserInfoBean setFirstNameWithDefaultValueWhenNull(String firstName,String defaultValue){
		String temp = firstName;
	    if(firstName == null){
	    	temp = defaultValue;
	    }
		return setFirstName(temp);
	}
	
	/**
	*Name:名
	*Comment:
	*/

	public UserInfoBean setFirstNameIfNotNullAndNotEmpty(String firstName){
		if(StringUtils.isEmpty(firstName)){
	    	return this;
	    }
	    return setFirstName(firstName);
	}
	/**
	*Name:名
	*Comment:
	*/

	public UserInfoBean setFirstNameWithDefaultValueWhenNullOrEmpty(String firstName,String defaultValue){
		String temp = firstName;
		if(StringUtils.isEmpty(firstName)){
	    	temp = defaultValue;
	    }
	    return setFirstName(temp);
	}
	
	/**
	*Name:姓名
	*Comment:
	*/

	public UserInfoBean setUserName(String userName){
		paramTypeMap.put("userName", String.class);
		paramValueMap.put("userName", userName);
		if(this.current != null){
			current.setUserName(userName);
		}
		return this;
	}

	/**
	*Name:姓名
	*Comment:
	*/

	public UserInfoBean setUserNameIfNotNull(String userName){
	    if(userName == null){
	    	return this;
	    }
		return setUserName(userName);
	}
	/**
	*Name:姓名
	*Comment:
	*/

	public UserInfoBean setUserNameWithDefaultValueWhenNull(String userName,String defaultValue){
		String temp = userName;
	    if(userName == null){
	    	temp = defaultValue;
	    }
		return setUserName(temp);
	}
	
	/**
	*Name:姓名
	*Comment:
	*/

	public UserInfoBean setUserNameIfNotNullAndNotEmpty(String userName){
		if(StringUtils.isEmpty(userName)){
	    	return this;
	    }
	    return setUserName(userName);
	}
	/**
	*Name:姓名
	*Comment:
	*/

	public UserInfoBean setUserNameWithDefaultValueWhenNullOrEmpty(String userName,String defaultValue){
		String temp = userName;
		if(StringUtils.isEmpty(userName)){
	    	temp = defaultValue;
	    }
	    return setUserName(temp);
	}
	
	/**
	*Name:电子邮件
	*Comment:
	*/

	public UserInfoBean setEmail(String email){
		paramTypeMap.put("email", String.class);
		paramValueMap.put("email", email);
		if(this.current != null){
			current.setEmail(email);
		}
		return this;
	}

	/**
	*Name:电子邮件
	*Comment:
	*/

	public UserInfoBean setEmailIfNotNull(String email){
	    if(email == null){
	    	return this;
	    }
		return setEmail(email);
	}
	/**
	*Name:电子邮件
	*Comment:
	*/

	public UserInfoBean setEmailWithDefaultValueWhenNull(String email,String defaultValue){
		String temp = email;
	    if(email == null){
	    	temp = defaultValue;
	    }
		return setEmail(temp);
	}
	
	/**
	*Name:电子邮件
	*Comment:
	*/

	public UserInfoBean setEmailIfNotNullAndNotEmpty(String email){
		if(StringUtils.isEmpty(email)){
	    	return this;
	    }
	    return setEmail(email);
	}
	/**
	*Name:电子邮件
	*Comment:
	*/

	public UserInfoBean setEmailWithDefaultValueWhenNullOrEmpty(String email,String defaultValue){
		String temp = email;
		if(StringUtils.isEmpty(email)){
	    	temp = defaultValue;
	    }
	    return setEmail(temp);
	}
	
	/**
	*Name:联系电话
	*Comment:
	*/

	public UserInfoBean setPhoneNumber(String phoneNumber){
		paramTypeMap.put("phoneNumber", String.class);
		paramValueMap.put("phoneNumber", phoneNumber);
		if(this.current != null){
			current.setPhoneNumber(phoneNumber);
		}
		return this;
	}

	/**
	*Name:联系电话
	*Comment:
	*/

	public UserInfoBean setPhoneNumberIfNotNull(String phoneNumber){
	    if(phoneNumber == null){
	    	return this;
	    }
		return setPhoneNumber(phoneNumber);
	}
	/**
	*Name:联系电话
	*Comment:
	*/

	public UserInfoBean setPhoneNumberWithDefaultValueWhenNull(String phoneNumber,String defaultValue){
		String temp = phoneNumber;
	    if(phoneNumber == null){
	    	temp = defaultValue;
	    }
		return setPhoneNumber(temp);
	}
	
	/**
	*Name:联系电话
	*Comment:
	*/

	public UserInfoBean setPhoneNumberIfNotNullAndNotEmpty(String phoneNumber){
		if(StringUtils.isEmpty(phoneNumber)){
	    	return this;
	    }
	    return setPhoneNumber(phoneNumber);
	}
	/**
	*Name:联系电话
	*Comment:
	*/

	public UserInfoBean setPhoneNumberWithDefaultValueWhenNullOrEmpty(String phoneNumber,String defaultValue){
		String temp = phoneNumber;
		if(StringUtils.isEmpty(phoneNumber)){
	    	temp = defaultValue;
	    }
	    return setPhoneNumber(temp);
	}
	
	/**
	*Name:备注
	*Comment:
	*/

	public UserInfoBean setRemark(String remark){
		paramTypeMap.put("remark", String.class);
		paramValueMap.put("remark", remark);
		if(this.current != null){
			current.setRemark(remark);
		}
		return this;
	}

	/**
	*Name:备注
	*Comment:
	*/

	public UserInfoBean setRemarkIfNotNull(String remark){
	    if(remark == null){
	    	return this;
	    }
		return setRemark(remark);
	}
	/**
	*Name:备注
	*Comment:
	*/

	public UserInfoBean setRemarkWithDefaultValueWhenNull(String remark,String defaultValue){
		String temp = remark;
	    if(remark == null){
	    	temp = defaultValue;
	    }
		return setRemark(temp);
	}
	
	/**
	*Name:备注
	*Comment:
	*/

	public UserInfoBean setRemarkIfNotNullAndNotEmpty(String remark){
		if(StringUtils.isEmpty(remark)){
	    	return this;
	    }
	    return setRemark(remark);
	}
	/**
	*Name:备注
	*Comment:
	*/

	public UserInfoBean setRemarkWithDefaultValueWhenNullOrEmpty(String remark,String defaultValue){
		String temp = remark;
		if(StringUtils.isEmpty(remark)){
	    	temp = defaultValue;
	    }
	    return setRemark(temp);
	}
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<UserInfo> update(){
		return this.helper.each(new Each<UserInfo>(){

			@Override
			public void each(UserInfo bean, List<UserInfo> list) {
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
	public UserInfo updateUnique(){
		UserInfo bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<UserInfo>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(UserInfo bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<UserInfo> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private UserInfo current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public UserInfoBean create(){
		UserInfo bean = new UserInfo();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<UserInfo> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (UserInfo bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public UserInfo insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		UserInfo bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public UserInfo ifFoundUpdateElseInsert(){
		UserInfo bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new UserInfo();
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
		UserInfo bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<UserInfo> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		UserInfo bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<UserInfo> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<UserInfo> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<UserInfo> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<UserInfo> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<UserInfo> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<UserInfo> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (UserInfo bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			UserInfo bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(UserInfo bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = UserInfo.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
