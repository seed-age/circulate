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
public class DiscussBean {
	private DiscussHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<Discuss> insertBeans = new ArrayList<Discuss>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = DiscussBean.class.getDeclaredMethods();
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
	private DiscussBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public DiscussBean copyValueForm(Object source){
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
	public DiscussBean copyNotNullValueForm(Object source){
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
	public DiscussBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public DiscussBean(DiscussHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:讨论id
	*Comment:讨论id
	*/

	public DiscussBean setDiscussId(Long discussId){
		paramTypeMap.put("discussId", Long.class);
		paramValueMap.put("discussId", discussId);
		if(this.current != null){
			current.setDiscussId(discussId);
		}
		return this;
	}

	/**
	*Name:讨论id
	*Comment:讨论id
	*/

	public DiscussBean setDiscussIdIfNotNull(Long discussId){
	    if(discussId == null){
	    	return this;
	    }
		return setDiscussId(discussId);
	}
	/**
	*Name:讨论id
	*Comment:讨论id
	*/

	public DiscussBean setDiscussIdWithDefaultValueWhenNull(Long discussId,Long defaultValue){
		Long temp = discussId;
	    if(discussId == null){
	    	temp = defaultValue;
	    }
		return setDiscussId(temp);
	}
	
	

	public DiscussBean setMail(Mail mail){
		paramTypeMap.put("mail", Mail.class);
		paramValueMap.put("mail", mail);
		if(this.current != null){
			current.setMail(mail);
		}
		return this;
	}


	public DiscussBean setMailIfNotNull(Mail mail){
	    if(mail == null){
	    	return this;
	    }
		return setMail(mail);
	}

	public DiscussBean setMailWithDefaultValueWhenNull(Mail mail,Mail defaultValue){
		Mail temp = mail;
	    if(mail == null){
	    	temp = defaultValue;
	    }
		return setMail(temp);
	}
	
	
	/**
	*Name:发布的时间
	*Comment:发布的时间
	*/

	public DiscussBean setCreateTime(Date createTime){
		paramTypeMap.put("createTime", Date.class);
		paramValueMap.put("createTime", createTime);
		if(this.current != null){
			current.setCreateTime(createTime);
		}
		return this;
	}

	/**
	*Name:发布的时间
	*Comment:发布的时间
	*/

	public DiscussBean setCreateTimeIfNotNull(Date createTime){
	    if(createTime == null){
	    	return this;
	    }
		return setCreateTime(createTime);
	}
	/**
	*Name:发布的时间
	*Comment:发布的时间
	*/

	public DiscussBean setCreateTimeWithDefaultValueWhenNull(Date createTime,Date defaultValue){
		Date temp = createTime;
	    if(createTime == null){
	    	temp = defaultValue;
	    }
		return setCreateTime(temp);
	}
	
	
	/**
	*Name:讨论的内容
	*Comment:讨论的内容
	*/

	public DiscussBean setDiscussContent(String discussContent){
		paramTypeMap.put("discussContent", String.class);
		paramValueMap.put("discussContent", discussContent);
		if(this.current != null){
			current.setDiscussContent(discussContent);
		}
		return this;
	}

	/**
	*Name:讨论的内容
	*Comment:讨论的内容
	*/

	public DiscussBean setDiscussContentIfNotNull(String discussContent){
	    if(discussContent == null){
	    	return this;
	    }
		return setDiscussContent(discussContent);
	}
	/**
	*Name:讨论的内容
	*Comment:讨论的内容
	*/

	public DiscussBean setDiscussContentWithDefaultValueWhenNull(String discussContent,String defaultValue){
		String temp = discussContent;
	    if(discussContent == null){
	    	temp = defaultValue;
	    }
		return setDiscussContent(temp);
	}
	
	/**
	*Name:讨论的内容
	*Comment:讨论的内容
	*/

	public DiscussBean setDiscussContentIfNotNullAndNotEmpty(String discussContent){
		if(StringUtils.isEmpty(discussContent)){
	    	return this;
	    }
	    return setDiscussContent(discussContent);
	}
	/**
	*Name:讨论的内容
	*Comment:讨论的内容
	*/

	public DiscussBean setDiscussContentWithDefaultValueWhenNullOrEmpty(String discussContent,String defaultValue){
		String temp = discussContent;
		if(StringUtils.isEmpty(discussContent)){
	    	temp = defaultValue;
	    }
	    return setDiscussContent(temp);
	}
	
	/**
	*Name:讨论人id
	*Comment:讨论人id
	*/

	public DiscussBean setUserId(long userId){
		paramTypeMap.put("userId", ClassUtils.wrapperToPrimitive(Long.class));
		paramValueMap.put("userId", userId);
		if(this.current != null){
			current.setUserId(userId);
		}
		return this;
	}
	
	/**
	*Name:讨论人工作编号
	*Comment:讨论人工作编号
	*/

	public DiscussBean setWorkCode(String workCode){
		paramTypeMap.put("workCode", String.class);
		paramValueMap.put("workCode", workCode);
		if(this.current != null){
			current.setWorkCode(workCode);
		}
		return this;
	}

	/**
	*Name:讨论人工作编号
	*Comment:讨论人工作编号
	*/

	public DiscussBean setWorkCodeIfNotNull(String workCode){
	    if(workCode == null){
	    	return this;
	    }
		return setWorkCode(workCode);
	}
	/**
	*Name:讨论人工作编号
	*Comment:讨论人工作编号
	*/

	public DiscussBean setWorkCodeWithDefaultValueWhenNull(String workCode,String defaultValue){
		String temp = workCode;
	    if(workCode == null){
	    	temp = defaultValue;
	    }
		return setWorkCode(temp);
	}
	
	/**
	*Name:讨论人工作编号
	*Comment:讨论人工作编号
	*/

	public DiscussBean setWorkCodeIfNotNullAndNotEmpty(String workCode){
		if(StringUtils.isEmpty(workCode)){
	    	return this;
	    }
	    return setWorkCode(workCode);
	}
	/**
	*Name:讨论人工作编号
	*Comment:讨论人工作编号
	*/

	public DiscussBean setWorkCodeWithDefaultValueWhenNullOrEmpty(String workCode,String defaultValue){
		String temp = workCode;
		if(StringUtils.isEmpty(workCode)){
	    	temp = defaultValue;
	    }
	    return setWorkCode(temp);
	}
	
	/**
	*Name:讨论人的姓名
	*Comment:讨论人的姓名
	*/

	public DiscussBean setLastName(String lastName){
		paramTypeMap.put("lastName", String.class);
		paramValueMap.put("lastName", lastName);
		if(this.current != null){
			current.setLastName(lastName);
		}
		return this;
	}

	/**
	*Name:讨论人的姓名
	*Comment:讨论人的姓名
	*/

	public DiscussBean setLastNameIfNotNull(String lastName){
	    if(lastName == null){
	    	return this;
	    }
		return setLastName(lastName);
	}
	/**
	*Name:讨论人的姓名
	*Comment:讨论人的姓名
	*/

	public DiscussBean setLastNameWithDefaultValueWhenNull(String lastName,String defaultValue){
		String temp = lastName;
	    if(lastName == null){
	    	temp = defaultValue;
	    }
		return setLastName(temp);
	}
	
	/**
	*Name:讨论人的姓名
	*Comment:讨论人的姓名
	*/

	public DiscussBean setLastNameIfNotNullAndNotEmpty(String lastName){
		if(StringUtils.isEmpty(lastName)){
	    	return this;
	    }
	    return setLastName(lastName);
	}
	/**
	*Name:讨论人的姓名
	*Comment:讨论人的姓名
	*/

	public DiscussBean setLastNameWithDefaultValueWhenNullOrEmpty(String lastName,String defaultValue){
		String temp = lastName;
		if(StringUtils.isEmpty(lastName)){
	    	temp = defaultValue;
	    }
	    return setLastName(temp);
	}
	
	/**
	*Name:讨论人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public DiscussBean setLoginId(String loginId){
		paramTypeMap.put("loginId", String.class);
		paramValueMap.put("loginId", loginId);
		if(this.current != null){
			current.setLoginId(loginId);
		}
		return this;
	}

	/**
	*Name:讨论人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public DiscussBean setLoginIdIfNotNull(String loginId){
	    if(loginId == null){
	    	return this;
	    }
		return setLoginId(loginId);
	}
	/**
	*Name:讨论人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public DiscussBean setLoginIdWithDefaultValueWhenNull(String loginId,String defaultValue){
		String temp = loginId;
	    if(loginId == null){
	    	temp = defaultValue;
	    }
		return setLoginId(temp);
	}
	
	/**
	*Name:讨论人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public DiscussBean setLoginIdIfNotNullAndNotEmpty(String loginId){
		if(StringUtils.isEmpty(loginId)){
	    	return this;
	    }
	    return setLoginId(loginId);
	}
	/**
	*Name:讨论人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public DiscussBean setLoginIdWithDefaultValueWhenNullOrEmpty(String loginId,String defaultValue){
		String temp = loginId;
		if(StringUtils.isEmpty(loginId)){
	    	temp = defaultValue;
	    }
	    return setLoginId(temp);
	}
	
	/**
	*Name:讨论人的头像url
	*Comment:讨论人的头像URL
	*/

	public DiscussBean setHeadPortraitUrl(String headPortraitUrl){
		paramTypeMap.put("headPortraitUrl", String.class);
		paramValueMap.put("headPortraitUrl", headPortraitUrl);
		if(this.current != null){
			current.setHeadPortraitUrl(headPortraitUrl);
		}
		return this;
	}

	/**
	*Name:讨论人的头像url
	*Comment:讨论人的头像URL
	*/

	public DiscussBean setHeadPortraitUrlIfNotNull(String headPortraitUrl){
	    if(headPortraitUrl == null){
	    	return this;
	    }
		return setHeadPortraitUrl(headPortraitUrl);
	}
	/**
	*Name:讨论人的头像url
	*Comment:讨论人的头像URL
	*/

	public DiscussBean setHeadPortraitUrlWithDefaultValueWhenNull(String headPortraitUrl,String defaultValue){
		String temp = headPortraitUrl;
	    if(headPortraitUrl == null){
	    	temp = defaultValue;
	    }
		return setHeadPortraitUrl(temp);
	}
	
	/**
	*Name:讨论人的头像url
	*Comment:讨论人的头像URL
	*/

	public DiscussBean setHeadPortraitUrlIfNotNullAndNotEmpty(String headPortraitUrl){
		if(StringUtils.isEmpty(headPortraitUrl)){
	    	return this;
	    }
	    return setHeadPortraitUrl(headPortraitUrl);
	}
	/**
	*Name:讨论人的头像url
	*Comment:讨论人的头像URL
	*/

	public DiscussBean setHeadPortraitUrlWithDefaultValueWhenNullOrEmpty(String headPortraitUrl,String defaultValue){
		String temp = headPortraitUrl;
		if(StringUtils.isEmpty(headPortraitUrl)){
	    	temp = defaultValue;
	    }
	    return setHeadPortraitUrl(temp);
	}
	
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人评论的记录或是已收传阅人的评论记录
	*/

	public DiscussBean setDifferentiate(String differentiate){
		paramTypeMap.put("differentiate", String.class);
		paramValueMap.put("differentiate", differentiate);
		if(this.current != null){
			current.setDifferentiate(differentiate);
		}
		return this;
	}

	/**
	*Name:区别
	*Comment:区别是: 已发传阅人评论的记录或是已收传阅人的评论记录
	*/

	public DiscussBean setDifferentiateIfNotNull(String differentiate){
	    if(differentiate == null){
	    	return this;
	    }
		return setDifferentiate(differentiate);
	}
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人评论的记录或是已收传阅人的评论记录
	*/

	public DiscussBean setDifferentiateWithDefaultValueWhenNull(String differentiate,String defaultValue){
		String temp = differentiate;
	    if(differentiate == null){
	    	temp = defaultValue;
	    }
		return setDifferentiate(temp);
	}
	
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人评论的记录或是已收传阅人的评论记录
	*/

	public DiscussBean setDifferentiateIfNotNullAndNotEmpty(String differentiate){
		if(StringUtils.isEmpty(differentiate)){
	    	return this;
	    }
	    return setDifferentiate(differentiate);
	}
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人评论的记录或是已收传阅人的评论记录
	*/

	public DiscussBean setDifferentiateWithDefaultValueWhenNullOrEmpty(String differentiate,String defaultValue){
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
	public List<Discuss> update(){
		return this.helper.each(new Each<Discuss>(){

			@Override
			public void each(Discuss bean, List<Discuss> list) {
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
	public Discuss updateUnique(){
		Discuss bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<Discuss>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(Discuss bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<Discuss> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private Discuss current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public DiscussBean create(){
		Discuss bean = new Discuss();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<Discuss> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (Discuss bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public Discuss insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		Discuss bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public Discuss ifFoundUpdateElseInsert(){
		Discuss bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new Discuss();
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
		Discuss bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<Discuss> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Discuss bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<Discuss> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<Discuss> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<Discuss> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<Discuss> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<Discuss> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<Discuss> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (Discuss bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			Discuss bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(Discuss bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = Discuss.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
