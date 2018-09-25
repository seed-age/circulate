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
public class UserCollectionBean {
	private UserCollectionHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<UserCollection> insertBeans = new ArrayList<UserCollection>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = UserCollectionBean.class.getDeclaredMethods();
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
	private UserCollectionBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public UserCollectionBean copyValueForm(Object source){
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
	public UserCollectionBean copyNotNullValueForm(Object source){
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
	public UserCollectionBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public UserCollectionBean(UserCollectionHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:收藏id
	*Comment:收藏id
	*/

	public UserCollectionBean setCollectionId(Long collectionId){
		paramTypeMap.put("collectionId", Long.class);
		paramValueMap.put("collectionId", collectionId);
		if(this.current != null){
			current.setCollectionId(collectionId);
		}
		return this;
	}

	/**
	*Name:收藏id
	*Comment:收藏id
	*/

	public UserCollectionBean setCollectionIdIfNotNull(Long collectionId){
	    if(collectionId == null){
	    	return this;
	    }
		return setCollectionId(collectionId);
	}
	/**
	*Name:收藏id
	*Comment:收藏id
	*/

	public UserCollectionBean setCollectionIdWithDefaultValueWhenNull(Long collectionId,Long defaultValue){
		Long temp = collectionId;
	    if(collectionId == null){
	    	temp = defaultValue;
	    }
		return setCollectionId(temp);
	}
	
	

	public UserCollectionBean setMail(Mail mail){
		paramTypeMap.put("mail", Mail.class);
		paramValueMap.put("mail", mail);
		if(this.current != null){
			current.setMail(mail);
		}
		return this;
	}


	public UserCollectionBean setMailIfNotNull(Mail mail){
	    if(mail == null){
	    	return this;
	    }
		return setMail(mail);
	}

	public UserCollectionBean setMailWithDefaultValueWhenNull(Mail mail,Mail defaultValue){
		Mail temp = mail;
	    if(mail == null){
	    	temp = defaultValue;
	    }
		return setMail(temp);
	}
	
	
	/**
	*Name:收藏人id
	*Comment:收藏人id
	*/

	public UserCollectionBean setUserId(long userId){
		paramTypeMap.put("userId", ClassUtils.wrapperToPrimitive(Long.class));
		paramValueMap.put("userId", userId);
		if(this.current != null){
			current.setUserId(userId);
		}
		return this;
	}
	
	/**
	*Name:收藏人工作编号
	*Comment:收藏人工作编号
	*/

	public UserCollectionBean setWorkCode(String workCode){
		paramTypeMap.put("workCode", String.class);
		paramValueMap.put("workCode", workCode);
		if(this.current != null){
			current.setWorkCode(workCode);
		}
		return this;
	}

	/**
	*Name:收藏人工作编号
	*Comment:收藏人工作编号
	*/

	public UserCollectionBean setWorkCodeIfNotNull(String workCode){
	    if(workCode == null){
	    	return this;
	    }
		return setWorkCode(workCode);
	}
	/**
	*Name:收藏人工作编号
	*Comment:收藏人工作编号
	*/

	public UserCollectionBean setWorkCodeWithDefaultValueWhenNull(String workCode,String defaultValue){
		String temp = workCode;
	    if(workCode == null){
	    	temp = defaultValue;
	    }
		return setWorkCode(temp);
	}
	
	/**
	*Name:收藏人工作编号
	*Comment:收藏人工作编号
	*/

	public UserCollectionBean setWorkCodeIfNotNullAndNotEmpty(String workCode){
		if(StringUtils.isEmpty(workCode)){
	    	return this;
	    }
	    return setWorkCode(workCode);
	}
	/**
	*Name:收藏人工作编号
	*Comment:收藏人工作编号
	*/

	public UserCollectionBean setWorkCodeWithDefaultValueWhenNullOrEmpty(String workCode,String defaultValue){
		String temp = workCode;
		if(StringUtils.isEmpty(workCode)){
	    	temp = defaultValue;
	    }
	    return setWorkCode(temp);
	}
	
	/**
	*Name:收藏人的姓名
	*Comment:收藏人的姓名
	*/

	public UserCollectionBean setLastName(String lastName){
		paramTypeMap.put("lastName", String.class);
		paramValueMap.put("lastName", lastName);
		if(this.current != null){
			current.setLastName(lastName);
		}
		return this;
	}

	/**
	*Name:收藏人的姓名
	*Comment:收藏人的姓名
	*/

	public UserCollectionBean setLastNameIfNotNull(String lastName){
	    if(lastName == null){
	    	return this;
	    }
		return setLastName(lastName);
	}
	/**
	*Name:收藏人的姓名
	*Comment:收藏人的姓名
	*/

	public UserCollectionBean setLastNameWithDefaultValueWhenNull(String lastName,String defaultValue){
		String temp = lastName;
	    if(lastName == null){
	    	temp = defaultValue;
	    }
		return setLastName(temp);
	}
	
	/**
	*Name:收藏人的姓名
	*Comment:收藏人的姓名
	*/

	public UserCollectionBean setLastNameIfNotNullAndNotEmpty(String lastName){
		if(StringUtils.isEmpty(lastName)){
	    	return this;
	    }
	    return setLastName(lastName);
	}
	/**
	*Name:收藏人的姓名
	*Comment:收藏人的姓名
	*/

	public UserCollectionBean setLastNameWithDefaultValueWhenNullOrEmpty(String lastName,String defaultValue){
		String temp = lastName;
		if(StringUtils.isEmpty(lastName)){
	    	temp = defaultValue;
	    }
	    return setLastName(temp);
	}
	
	/**
	*Name:收藏时间
	*Comment:收藏时间
	*/

	public UserCollectionBean setCollectionTime(Date collectionTime){
		paramTypeMap.put("collectionTime", Date.class);
		paramValueMap.put("collectionTime", collectionTime);
		if(this.current != null){
			current.setCollectionTime(collectionTime);
		}
		return this;
	}

	/**
	*Name:收藏时间
	*Comment:收藏时间
	*/

	public UserCollectionBean setCollectionTimeIfNotNull(Date collectionTime){
	    if(collectionTime == null){
	    	return this;
	    }
		return setCollectionTime(collectionTime);
	}
	/**
	*Name:收藏时间
	*Comment:收藏时间
	*/

	public UserCollectionBean setCollectionTimeWithDefaultValueWhenNull(Date collectionTime,Date defaultValue){
		Date temp = collectionTime;
	    if(collectionTime == null){
	    	temp = defaultValue;
	    }
		return setCollectionTime(temp);
	}
	
	
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人收藏的记录或是已收传阅人的收藏记录
	*/

	public UserCollectionBean setDifferentiate(String differentiate){
		paramTypeMap.put("differentiate", String.class);
		paramValueMap.put("differentiate", differentiate);
		if(this.current != null){
			current.setDifferentiate(differentiate);
		}
		return this;
	}

	/**
	*Name:区别
	*Comment:区别是: 已发传阅人收藏的记录或是已收传阅人的收藏记录
	*/

	public UserCollectionBean setDifferentiateIfNotNull(String differentiate){
	    if(differentiate == null){
	    	return this;
	    }
		return setDifferentiate(differentiate);
	}
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人收藏的记录或是已收传阅人的收藏记录
	*/

	public UserCollectionBean setDifferentiateWithDefaultValueWhenNull(String differentiate,String defaultValue){
		String temp = differentiate;
	    if(differentiate == null){
	    	temp = defaultValue;
	    }
		return setDifferentiate(temp);
	}
	
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人收藏的记录或是已收传阅人的收藏记录
	*/

	public UserCollectionBean setDifferentiateIfNotNullAndNotEmpty(String differentiate){
		if(StringUtils.isEmpty(differentiate)){
	    	return this;
	    }
	    return setDifferentiate(differentiate);
	}
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人收藏的记录或是已收传阅人的收藏记录
	*/

	public UserCollectionBean setDifferentiateWithDefaultValueWhenNullOrEmpty(String differentiate,String defaultValue){
		String temp = differentiate;
		if(StringUtils.isEmpty(differentiate)){
	    	temp = defaultValue;
	    }
	    return setDifferentiate(temp);
	}
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<UserCollection> update(){
		return this.helper.each(new Each<UserCollection>(){

			@Override
			public void each(UserCollection bean, List<UserCollection> list) {
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
	public UserCollection updateUnique(){
		UserCollection bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<UserCollection>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(UserCollection bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<UserCollection> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private UserCollection current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public UserCollectionBean create(){
		UserCollection bean = new UserCollection();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<UserCollection> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (UserCollection bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public UserCollection insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		UserCollection bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public UserCollection ifFoundUpdateElseInsert(){
		UserCollection bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new UserCollection();
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
		UserCollection bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<UserCollection> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		UserCollection bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<UserCollection> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<UserCollection> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<UserCollection> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<UserCollection> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<UserCollection> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<UserCollection> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (UserCollection bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			UserCollection bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(UserCollection bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = UserCollection.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
