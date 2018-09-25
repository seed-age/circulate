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
public class UserBean {
	private UserHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<User> insertBeans = new ArrayList<User>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = UserBean.class.getDeclaredMethods();
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
	private UserBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public UserBean copyValueForm(Object source){
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
	public UserBean copyNotNullValueForm(Object source){
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
	public UserBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public UserBean(UserHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:用户ID
	*Comment:
	*/

	public UserBean setUserId(Long userId){
		paramTypeMap.put("userId", Long.class);
		paramValueMap.put("userId", userId);
		if(this.current != null){
			current.setUserId(userId);
		}
		return this;
	}

	/**
	*Name:用户ID
	*Comment:
	*/

	public UserBean setUserIdIfNotNull(Long userId){
	    if(userId == null){
	    	return this;
	    }
		return setUserId(userId);
	}
	/**
	*Name:用户ID
	*Comment:
	*/

	public UserBean setUserIdWithDefaultValueWhenNull(Long userId,Long defaultValue){
		Long temp = userId;
	    if(userId == null){
	    	temp = defaultValue;
	    }
		return setUserId(temp);
	}
	
	
	/**
	*Name:登录名
	*Comment:
	*/

	public UserBean setAccountName(String accountName){
		paramTypeMap.put("accountName", String.class);
		paramValueMap.put("accountName", accountName);
		if(this.current != null){
			current.setAccountName(accountName);
		}
		return this;
	}

	/**
	*Name:登录名
	*Comment:
	*/

	public UserBean setAccountNameIfNotNull(String accountName){
	    if(accountName == null){
	    	return this;
	    }
		return setAccountName(accountName);
	}
	/**
	*Name:登录名
	*Comment:
	*/

	public UserBean setAccountNameWithDefaultValueWhenNull(String accountName,String defaultValue){
		String temp = accountName;
	    if(accountName == null){
	    	temp = defaultValue;
	    }
		return setAccountName(temp);
	}
	
	/**
	*Name:登录名
	*Comment:
	*/

	public UserBean setAccountNameIfNotNullAndNotEmpty(String accountName){
		if(StringUtils.isEmpty(accountName)){
	    	return this;
	    }
	    return setAccountName(accountName);
	}
	/**
	*Name:登录名
	*Comment:
	*/

	public UserBean setAccountNameWithDefaultValueWhenNullOrEmpty(String accountName,String defaultValue){
		String temp = accountName;
		if(StringUtils.isEmpty(accountName)){
	    	temp = defaultValue;
	    }
	    return setAccountName(temp);
	}
	
	/**
	*Name:昵称
	*Comment:
	*/

	public UserBean setNickName(String nickName){
		paramTypeMap.put("nickName", String.class);
		paramValueMap.put("nickName", nickName);
		if(this.current != null){
			current.setNickName(nickName);
		}
		return this;
	}

	/**
	*Name:昵称
	*Comment:
	*/

	public UserBean setNickNameIfNotNull(String nickName){
	    if(nickName == null){
	    	return this;
	    }
		return setNickName(nickName);
	}
	/**
	*Name:昵称
	*Comment:
	*/

	public UserBean setNickNameWithDefaultValueWhenNull(String nickName,String defaultValue){
		String temp = nickName;
	    if(nickName == null){
	    	temp = defaultValue;
	    }
		return setNickName(temp);
	}
	
	/**
	*Name:昵称
	*Comment:
	*/

	public UserBean setNickNameIfNotNullAndNotEmpty(String nickName){
		if(StringUtils.isEmpty(nickName)){
	    	return this;
	    }
	    return setNickName(nickName);
	}
	/**
	*Name:昵称
	*Comment:
	*/

	public UserBean setNickNameWithDefaultValueWhenNullOrEmpty(String nickName,String defaultValue){
		String temp = nickName;
		if(StringUtils.isEmpty(nickName)){
	    	temp = defaultValue;
	    }
	    return setNickName(temp);
	}
	
	/**
	*Name:创建日期
	*Comment:
	*/

	public UserBean setCreateTime(Date createTime){
		paramTypeMap.put("createTime", Date.class);
		paramValueMap.put("createTime", createTime);
		if(this.current != null){
			current.setCreateTime(createTime);
		}
		return this;
	}

	/**
	*Name:创建日期
	*Comment:
	*/

	public UserBean setCreateTimeIfNotNull(Date createTime){
	    if(createTime == null){
	    	return this;
	    }
		return setCreateTime(createTime);
	}
	/**
	*Name:创建日期
	*Comment:
	*/

	public UserBean setCreateTimeWithDefaultValueWhenNull(Date createTime,Date defaultValue){
		Date temp = createTime;
	    if(createTime == null){
	    	temp = defaultValue;
	    }
		return setCreateTime(temp);
	}
	
	
	/**
	*Name:密码
	*Comment:
	*/

	public UserBean setUserPassword(String userPassword){
		paramTypeMap.put("userPassword", String.class);
		paramValueMap.put("userPassword", userPassword);
		if(this.current != null){
			current.setUserPassword(userPassword);
		}
		return this;
	}

	/**
	*Name:密码
	*Comment:
	*/

	public UserBean setUserPasswordIfNotNull(String userPassword){
	    if(userPassword == null){
	    	return this;
	    }
		return setUserPassword(userPassword);
	}
	/**
	*Name:密码
	*Comment:
	*/

	public UserBean setUserPasswordWithDefaultValueWhenNull(String userPassword,String defaultValue){
		String temp = userPassword;
	    if(userPassword == null){
	    	temp = defaultValue;
	    }
		return setUserPassword(temp);
	}
	
	/**
	*Name:密码
	*Comment:
	*/

	public UserBean setUserPasswordIfNotNullAndNotEmpty(String userPassword){
		if(StringUtils.isEmpty(userPassword)){
	    	return this;
	    }
	    return setUserPassword(userPassword);
	}
	/**
	*Name:密码
	*Comment:
	*/

	public UserBean setUserPasswordWithDefaultValueWhenNullOrEmpty(String userPassword,String defaultValue){
		String temp = userPassword;
		if(StringUtils.isEmpty(userPassword)){
	    	temp = defaultValue;
	    }
	    return setUserPassword(temp);
	}
	
	/**
	*Name:是否管理员
	*Comment:
	*/

	public UserBean setAdmin(boolean admin){
		paramTypeMap.put("admin", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("admin", admin);
		if(this.current != null){
			current.setAdmin(admin);
		}
		return this;
	}
	
	/**
	*Name:是否启用
	*Comment:0 为失效 （false），1 为正常（true）
	*/

	public UserBean setEnabled(boolean enabled){
		paramTypeMap.put("enabled", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("enabled", enabled);
		if(this.current != null){
			current.setEnabled(enabled);
		}
		return this;
	}
	
	/**
	*Name:最后登录时间
	*Comment:
	*/

	public UserBean setLastLogin(Date lastLogin){
		paramTypeMap.put("lastLogin", Date.class);
		paramValueMap.put("lastLogin", lastLogin);
		if(this.current != null){
			current.setLastLogin(lastLogin);
		}
		return this;
	}

	/**
	*Name:最后登录时间
	*Comment:
	*/

	public UserBean setLastLoginIfNotNull(Date lastLogin){
	    if(lastLogin == null){
	    	return this;
	    }
		return setLastLogin(lastLogin);
	}
	/**
	*Name:最后登录时间
	*Comment:
	*/

	public UserBean setLastLoginWithDefaultValueWhenNull(Date lastLogin,Date defaultValue){
		Date temp = lastLogin;
	    if(lastLogin == null){
	    	temp = defaultValue;
	    }
		return setLastLogin(temp);
	}
	
	
	/**
	*Name:优先级
	*Comment:
	*/

	public UserBean setPriority(int priority){
		paramTypeMap.put("priority", ClassUtils.wrapperToPrimitive(Integer.class));
		paramValueMap.put("priority", priority);
		if(this.current != null){
			current.setPriority(priority);
		}
		return this;
	}
	
	/**
	*Name:是否软删除
	*Comment:
	*/

	public UserBean setSoftDelete(boolean softDelete){
		paramTypeMap.put("softDelete", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("softDelete", softDelete);
		if(this.current != null){
			current.setSoftDelete(softDelete);
		}
		return this;
	}
	
	/**
	*Name:登录令牌
	*Comment:
	*/

	public UserBean setAccessToken(String accessToken){
		paramTypeMap.put("accessToken", String.class);
		paramValueMap.put("accessToken", accessToken);
		if(this.current != null){
			current.setAccessToken(accessToken);
		}
		return this;
	}

	/**
	*Name:登录令牌
	*Comment:
	*/

	public UserBean setAccessTokenIfNotNull(String accessToken){
	    if(accessToken == null){
	    	return this;
	    }
		return setAccessToken(accessToken);
	}
	/**
	*Name:登录令牌
	*Comment:
	*/

	public UserBean setAccessTokenWithDefaultValueWhenNull(String accessToken,String defaultValue){
		String temp = accessToken;
	    if(accessToken == null){
	    	temp = defaultValue;
	    }
		return setAccessToken(temp);
	}
	
	/**
	*Name:登录令牌
	*Comment:
	*/

	public UserBean setAccessTokenIfNotNullAndNotEmpty(String accessToken){
		if(StringUtils.isEmpty(accessToken)){
	    	return this;
	    }
	    return setAccessToken(accessToken);
	}
	/**
	*Name:登录令牌
	*Comment:
	*/

	public UserBean setAccessTokenWithDefaultValueWhenNullOrEmpty(String accessToken,String defaultValue){
		String temp = accessToken;
		if(StringUtils.isEmpty(accessToken)){
	    	temp = defaultValue;
	    }
	    return setAccessToken(temp);
	}
	
	/**
	*Name:令牌失效时间
	*Comment:
	*/

	public UserBean setAccessTokenExpire(Date accessTokenExpire){
		paramTypeMap.put("accessTokenExpire", Date.class);
		paramValueMap.put("accessTokenExpire", accessTokenExpire);
		if(this.current != null){
			current.setAccessTokenExpire(accessTokenExpire);
		}
		return this;
	}

	/**
	*Name:令牌失效时间
	*Comment:
	*/

	public UserBean setAccessTokenExpireIfNotNull(Date accessTokenExpire){
	    if(accessTokenExpire == null){
	    	return this;
	    }
		return setAccessTokenExpire(accessTokenExpire);
	}
	/**
	*Name:令牌失效时间
	*Comment:
	*/

	public UserBean setAccessTokenExpireWithDefaultValueWhenNull(Date accessTokenExpire,Date defaultValue){
		Date temp = accessTokenExpire;
	    if(accessTokenExpire == null){
	    	temp = defaultValue;
	    }
		return setAccessTokenExpire(temp);
	}
	
	

	public UserBean setUserInfo(UserInfo userInfo){
		paramTypeMap.put("userInfo", UserInfo.class);
		paramValueMap.put("userInfo", userInfo);
		if(this.current != null){
			current.setUserInfo(userInfo);
		}
		return this;
	}


	public UserBean setUserInfoIfNotNull(UserInfo userInfo){
	    if(userInfo == null){
	    	return this;
	    }
		return setUserInfo(userInfo);
	}

	public UserBean setUserInfoWithDefaultValueWhenNull(UserInfo userInfo,UserInfo defaultValue){
		UserInfo temp = userInfo;
	    if(userInfo == null){
	    	temp = defaultValue;
	    }
		return setUserInfo(temp);
	}
	
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<User> update(){
		return this.helper.each(new Each<User>(){

			@Override
			public void each(User bean, List<User> list) {
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
	public User updateUnique(){
		User bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<User>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(User bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<User> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private User current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public UserBean create(){
		User bean = new User();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<User> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (User bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public User insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		User bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public User ifFoundUpdateElseInsert(){
		User bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new User();
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
		User bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<User> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		User bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<User> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<User> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<User> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<User> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<User> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<User> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (User bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			User bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(User bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = User.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
