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
public class SystemBackupLogBean {
	private SystemBackupLogHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<SystemBackupLog> insertBeans = new ArrayList<SystemBackupLog>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = SystemBackupLogBean.class.getDeclaredMethods();
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
	private SystemBackupLogBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public SystemBackupLogBean copyValueForm(Object source){
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
	public SystemBackupLogBean copyNotNullValueForm(Object source){
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
	public SystemBackupLogBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public SystemBackupLogBean(SystemBackupLogHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:ID
	*Comment:
	*/

	public SystemBackupLogBean setLogId(Long logId){
		paramTypeMap.put("logId", Long.class);
		paramValueMap.put("logId", logId);
		if(this.current != null){
			current.setLogId(logId);
		}
		return this;
	}

	/**
	*Name:ID
	*Comment:
	*/

	public SystemBackupLogBean setLogIdIfNotNull(Long logId){
	    if(logId == null){
	    	return this;
	    }
		return setLogId(logId);
	}
	/**
	*Name:ID
	*Comment:
	*/

	public SystemBackupLogBean setLogIdWithDefaultValueWhenNull(Long logId,Long defaultValue){
		Long temp = logId;
	    if(logId == null){
	    	temp = defaultValue;
	    }
		return setLogId(temp);
	}
	
	
	/**
	*Name:操作人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public SystemBackupLogBean setOperator(String operator){
		paramTypeMap.put("operator", String.class);
		paramValueMap.put("operator", operator);
		if(this.current != null){
			current.setOperator(operator);
		}
		return this;
	}

	/**
	*Name:操作人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public SystemBackupLogBean setOperatorIfNotNull(String operator){
	    if(operator == null){
	    	return this;
	    }
		return setOperator(operator);
	}
	/**
	*Name:操作人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public SystemBackupLogBean setOperatorWithDefaultValueWhenNull(String operator,String defaultValue){
		String temp = operator;
	    if(operator == null){
	    	temp = defaultValue;
	    }
		return setOperator(temp);
	}
	
	/**
	*Name:操作人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public SystemBackupLogBean setOperatorIfNotNullAndNotEmpty(String operator){
		if(StringUtils.isEmpty(operator)){
	    	return this;
	    }
	    return setOperator(operator);
	}
	/**
	*Name:操作人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public SystemBackupLogBean setOperatorWithDefaultValueWhenNullOrEmpty(String operator,String defaultValue){
		String temp = operator;
		if(StringUtils.isEmpty(operator)){
	    	temp = defaultValue;
	    }
	    return setOperator(temp);
	}
	
	/**
	*Name:日志时间
	*Comment:
	*/

	public SystemBackupLogBean setLogTime(Date logTime){
		paramTypeMap.put("logTime", Date.class);
		paramValueMap.put("logTime", logTime);
		if(this.current != null){
			current.setLogTime(logTime);
		}
		return this;
	}

	/**
	*Name:日志时间
	*Comment:
	*/

	public SystemBackupLogBean setLogTimeIfNotNull(Date logTime){
	    if(logTime == null){
	    	return this;
	    }
		return setLogTime(logTime);
	}
	/**
	*Name:日志时间
	*Comment:
	*/

	public SystemBackupLogBean setLogTimeWithDefaultValueWhenNull(Date logTime,Date defaultValue){
		Date temp = logTime;
	    if(logTime == null){
	    	temp = defaultValue;
	    }
		return setLogTime(temp);
	}
	
	
	/**
	*Name:操作
	*Comment:
	*/

	public SystemBackupLogBean setAction(String action){
		paramTypeMap.put("action", String.class);
		paramValueMap.put("action", action);
		if(this.current != null){
			current.setAction(action);
		}
		return this;
	}

	/**
	*Name:操作
	*Comment:
	*/

	public SystemBackupLogBean setActionIfNotNull(String action){
	    if(action == null){
	    	return this;
	    }
		return setAction(action);
	}
	/**
	*Name:操作
	*Comment:
	*/

	public SystemBackupLogBean setActionWithDefaultValueWhenNull(String action,String defaultValue){
		String temp = action;
	    if(action == null){
	    	temp = defaultValue;
	    }
		return setAction(temp);
	}
	
	/**
	*Name:操作
	*Comment:
	*/

	public SystemBackupLogBean setActionIfNotNullAndNotEmpty(String action){
		if(StringUtils.isEmpty(action)){
	    	return this;
	    }
	    return setAction(action);
	}
	/**
	*Name:操作
	*Comment:
	*/

	public SystemBackupLogBean setActionWithDefaultValueWhenNullOrEmpty(String action,String defaultValue){
		String temp = action;
		if(StringUtils.isEmpty(action)){
	    	temp = defaultValue;
	    }
	    return setAction(temp);
	}
	
	/**
	*Name:ip地址
	*Comment:
	*/

	public SystemBackupLogBean setIp(String ip){
		paramTypeMap.put("ip", String.class);
		paramValueMap.put("ip", ip);
		if(this.current != null){
			current.setIp(ip);
		}
		return this;
	}

	/**
	*Name:ip地址
	*Comment:
	*/

	public SystemBackupLogBean setIpIfNotNull(String ip){
	    if(ip == null){
	    	return this;
	    }
		return setIp(ip);
	}
	/**
	*Name:ip地址
	*Comment:
	*/

	public SystemBackupLogBean setIpWithDefaultValueWhenNull(String ip,String defaultValue){
		String temp = ip;
	    if(ip == null){
	    	temp = defaultValue;
	    }
		return setIp(temp);
	}
	
	/**
	*Name:ip地址
	*Comment:
	*/

	public SystemBackupLogBean setIpIfNotNullAndNotEmpty(String ip){
		if(StringUtils.isEmpty(ip)){
	    	return this;
	    }
	    return setIp(ip);
	}
	/**
	*Name:ip地址
	*Comment:
	*/

	public SystemBackupLogBean setIpWithDefaultValueWhenNullOrEmpty(String ip,String defaultValue){
		String temp = ip;
		if(StringUtils.isEmpty(ip)){
	    	temp = defaultValue;
	    }
	    return setIp(temp);
	}
	
	/**
	*Name:身份信息
	*Comment:可以是用户名，用户实体id，session id等描述用户的信息，以便核对。
不和用户表直接外键关联。
	*/

	public SystemBackupLogBean setIdentityInfo(String identityInfo){
		paramTypeMap.put("identityInfo", String.class);
		paramValueMap.put("identityInfo", identityInfo);
		if(this.current != null){
			current.setIdentityInfo(identityInfo);
		}
		return this;
	}

	/**
	*Name:身份信息
	*Comment:可以是用户名，用户实体id，session id等描述用户的信息，以便核对。
不和用户表直接外键关联。
	*/

	public SystemBackupLogBean setIdentityInfoIfNotNull(String identityInfo){
	    if(identityInfo == null){
	    	return this;
	    }
		return setIdentityInfo(identityInfo);
	}
	/**
	*Name:身份信息
	*Comment:可以是用户名，用户实体id，session id等描述用户的信息，以便核对。
不和用户表直接外键关联。
	*/

	public SystemBackupLogBean setIdentityInfoWithDefaultValueWhenNull(String identityInfo,String defaultValue){
		String temp = identityInfo;
	    if(identityInfo == null){
	    	temp = defaultValue;
	    }
		return setIdentityInfo(temp);
	}
	
	/**
	*Name:身份信息
	*Comment:可以是用户名，用户实体id，session id等描述用户的信息，以便核对。
不和用户表直接外键关联。
	*/

	public SystemBackupLogBean setIdentityInfoIfNotNullAndNotEmpty(String identityInfo){
		if(StringUtils.isEmpty(identityInfo)){
	    	return this;
	    }
	    return setIdentityInfo(identityInfo);
	}
	/**
	*Name:身份信息
	*Comment:可以是用户名，用户实体id，session id等描述用户的信息，以便核对。
不和用户表直接外键关联。
	*/

	public SystemBackupLogBean setIdentityInfoWithDefaultValueWhenNullOrEmpty(String identityInfo,String defaultValue){
		String temp = identityInfo;
		if(StringUtils.isEmpty(identityInfo)){
	    	temp = defaultValue;
	    }
	    return setIdentityInfo(temp);
	}
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<SystemBackupLog> update(){
		return this.helper.each(new Each<SystemBackupLog>(){

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> list) {
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
	public SystemBackupLog updateUnique(){
		SystemBackupLog bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<SystemBackupLog>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(SystemBackupLog bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<SystemBackupLog> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private SystemBackupLog current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public SystemBackupLogBean create(){
		SystemBackupLog bean = new SystemBackupLog();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<SystemBackupLog> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (SystemBackupLog bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public SystemBackupLog insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		SystemBackupLog bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public SystemBackupLog ifFoundUpdateElseInsert(){
		SystemBackupLog bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new SystemBackupLog();
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
		SystemBackupLog bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<SystemBackupLog> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		SystemBackupLog bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<SystemBackupLog> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<SystemBackupLog> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<SystemBackupLog> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<SystemBackupLog> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<SystemBackupLog> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<SystemBackupLog> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (SystemBackupLog bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			SystemBackupLog bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(SystemBackupLog bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = SystemBackupLog.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
