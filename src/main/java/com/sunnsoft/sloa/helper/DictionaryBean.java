package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.Dictionary;
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
public class DictionaryBean {
	private DictionaryHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<Dictionary> insertBeans = new ArrayList<Dictionary>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = DictionaryBean.class.getDeclaredMethods();
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
	private DictionaryBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public DictionaryBean copyValueForm(Object source){
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
	public DictionaryBean copyNotNullValueForm(Object source){
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
	public DictionaryBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public DictionaryBean(DictionaryHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:ID
	*Comment:
	*/

	public DictionaryBean setDicId(Long dicId){
		paramTypeMap.put("dicId", Long.class);
		paramValueMap.put("dicId", dicId);
		if(this.current != null){
			current.setDicId(dicId);
		}
		return this;
	}

	/**
	*Name:ID
	*Comment:
	*/

	public DictionaryBean setDicIdIfNotNull(Long dicId){
	    if(dicId == null){
	    	return this;
	    }
		return setDicId(dicId);
	}
	/**
	*Name:ID
	*Comment:
	*/

	public DictionaryBean setDicIdWithDefaultValueWhenNull(Long dicId,Long defaultValue){
		Long temp = dicId;
	    if(dicId == null){
	    	temp = defaultValue;
	    }
		return setDicId(temp);
	}
	
	
	/**
	*Name:键名
	*Comment:
	*/

	public DictionaryBean setKeyName(String keyName){
		paramTypeMap.put("keyName", String.class);
		paramValueMap.put("keyName", keyName);
		if(this.current != null){
			current.setKeyName(keyName);
		}
		return this;
	}

	/**
	*Name:键名
	*Comment:
	*/

	public DictionaryBean setKeyNameIfNotNull(String keyName){
	    if(keyName == null){
	    	return this;
	    }
		return setKeyName(keyName);
	}
	/**
	*Name:键名
	*Comment:
	*/

	public DictionaryBean setKeyNameWithDefaultValueWhenNull(String keyName,String defaultValue){
		String temp = keyName;
	    if(keyName == null){
	    	temp = defaultValue;
	    }
		return setKeyName(temp);
	}
	
	/**
	*Name:键名
	*Comment:
	*/

	public DictionaryBean setKeyNameIfNotNullAndNotEmpty(String keyName){
		if(StringUtils.isEmpty(keyName)){
	    	return this;
	    }
	    return setKeyName(keyName);
	}
	/**
	*Name:键名
	*Comment:
	*/

	public DictionaryBean setKeyNameWithDefaultValueWhenNullOrEmpty(String keyName,String defaultValue){
		String temp = keyName;
		if(StringUtils.isEmpty(keyName)){
	    	temp = defaultValue;
	    }
	    return setKeyName(temp);
	}
	
	/**
	*Name:键值
	*Comment:
	*/

	public DictionaryBean setKeyValue(String keyValue){
		paramTypeMap.put("keyValue", String.class);
		paramValueMap.put("keyValue", keyValue);
		if(this.current != null){
			current.setKeyValue(keyValue);
		}
		return this;
	}

	/**
	*Name:键值
	*Comment:
	*/

	public DictionaryBean setKeyValueIfNotNull(String keyValue){
	    if(keyValue == null){
	    	return this;
	    }
		return setKeyValue(keyValue);
	}
	/**
	*Name:键值
	*Comment:
	*/

	public DictionaryBean setKeyValueWithDefaultValueWhenNull(String keyValue,String defaultValue){
		String temp = keyValue;
	    if(keyValue == null){
	    	temp = defaultValue;
	    }
		return setKeyValue(temp);
	}
	
	/**
	*Name:键值
	*Comment:
	*/

	public DictionaryBean setKeyValueIfNotNullAndNotEmpty(String keyValue){
		if(StringUtils.isEmpty(keyValue)){
	    	return this;
	    }
	    return setKeyValue(keyValue);
	}
	/**
	*Name:键值
	*Comment:
	*/

	public DictionaryBean setKeyValueWithDefaultValueWhenNullOrEmpty(String keyValue,String defaultValue){
		String temp = keyValue;
		if(StringUtils.isEmpty(keyValue)){
	    	temp = defaultValue;
	    }
	    return setKeyValue(temp);
	}
	
	/**
	*Name:更新日期
	*Comment:
	*/

	public DictionaryBean setUpdateTime(Date updateTime){
		paramTypeMap.put("updateTime", Date.class);
		paramValueMap.put("updateTime", updateTime);
		if(this.current != null){
			current.setUpdateTime(updateTime);
		}
		return this;
	}

	/**
	*Name:更新日期
	*Comment:
	*/

	public DictionaryBean setUpdateTimeIfNotNull(Date updateTime){
	    if(updateTime == null){
	    	return this;
	    }
		return setUpdateTime(updateTime);
	}
	/**
	*Name:更新日期
	*Comment:
	*/

	public DictionaryBean setUpdateTimeWithDefaultValueWhenNull(Date updateTime,Date defaultValue){
		Date temp = updateTime;
	    if(updateTime == null){
	    	temp = defaultValue;
	    }
		return setUpdateTime(temp);
	}
	
	
	/**
	*Name:创建日期
	*Comment:
	*/

	public DictionaryBean setCreateTime(Date createTime){
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

	public DictionaryBean setCreateTimeIfNotNull(Date createTime){
	    if(createTime == null){
	    	return this;
	    }
		return setCreateTime(createTime);
	}
	/**
	*Name:创建日期
	*Comment:
	*/

	public DictionaryBean setCreateTimeWithDefaultValueWhenNull(Date createTime,Date defaultValue){
		Date temp = createTime;
	    if(createTime == null){
	    	temp = defaultValue;
	    }
		return setCreateTime(temp);
	}
	
	
	/**
	*Name:状态
	*Comment:0 为失效 （false），1 为正常（true）
	*/

	public DictionaryBean setStatus(boolean status){
		paramTypeMap.put("status", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("status", status);
		if(this.current != null){
			current.setStatus(status);
		}
		return this;
	}
	
	/**
	*Name:作用描述
	*Comment:
	*/

	public DictionaryBean setDescription(String description){
		paramTypeMap.put("description", String.class);
		paramValueMap.put("description", description);
		if(this.current != null){
			current.setDescription(description);
		}
		return this;
	}

	/**
	*Name:作用描述
	*Comment:
	*/

	public DictionaryBean setDescriptionIfNotNull(String description){
	    if(description == null){
	    	return this;
	    }
		return setDescription(description);
	}
	/**
	*Name:作用描述
	*Comment:
	*/

	public DictionaryBean setDescriptionWithDefaultValueWhenNull(String description,String defaultValue){
		String temp = description;
	    if(description == null){
	    	temp = defaultValue;
	    }
		return setDescription(temp);
	}
	
	/**
	*Name:作用描述
	*Comment:
	*/

	public DictionaryBean setDescriptionIfNotNullAndNotEmpty(String description){
		if(StringUtils.isEmpty(description)){
	    	return this;
	    }
	    return setDescription(description);
	}
	/**
	*Name:作用描述
	*Comment:
	*/

	public DictionaryBean setDescriptionWithDefaultValueWhenNullOrEmpty(String description,String defaultValue){
		String temp = description;
		if(StringUtils.isEmpty(description)){
	    	temp = defaultValue;
	    }
	    return setDescription(temp);
	}
	
	/**
	*Name:类型
	*Comment:
	*/

	public DictionaryBean setType(String type){
		paramTypeMap.put("type", String.class);
		paramValueMap.put("type", type);
		if(this.current != null){
			current.setType(type);
		}
		return this;
	}

	/**
	*Name:类型
	*Comment:
	*/

	public DictionaryBean setTypeIfNotNull(String type){
	    if(type == null){
	    	return this;
	    }
		return setType(type);
	}
	/**
	*Name:类型
	*Comment:
	*/

	public DictionaryBean setTypeWithDefaultValueWhenNull(String type,String defaultValue){
		String temp = type;
	    if(type == null){
	    	temp = defaultValue;
	    }
		return setType(temp);
	}
	
	/**
	*Name:类型
	*Comment:
	*/

	public DictionaryBean setTypeIfNotNullAndNotEmpty(String type){
		if(StringUtils.isEmpty(type)){
	    	return this;
	    }
	    return setType(type);
	}
	/**
	*Name:类型
	*Comment:
	*/

	public DictionaryBean setTypeWithDefaultValueWhenNullOrEmpty(String type,String defaultValue){
		String temp = type;
		if(StringUtils.isEmpty(type)){
	    	temp = defaultValue;
	    }
	    return setType(temp);
	}
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<Dictionary> update(){
		return this.helper.each(new Each<Dictionary>(){

			@Override
			public void each(Dictionary bean, List<Dictionary> list) {
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
	public Dictionary updateUnique(){
		Dictionary bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<Dictionary>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(Dictionary bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<Dictionary> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private Dictionary current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public DictionaryBean create(){
		Dictionary bean = new Dictionary();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<Dictionary> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (Dictionary bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public Dictionary insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		Dictionary bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public Dictionary ifFoundUpdateElseInsert(){
		Dictionary bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new Dictionary();
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
		Dictionary bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<Dictionary> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Dictionary bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<Dictionary> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<Dictionary> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<Dictionary> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<Dictionary> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<Dictionary> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<Dictionary> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (Dictionary bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			Dictionary bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(Dictionary bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = Dictionary.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
