package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.Hrmsubcompany;
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
public class HrmsubcompanyBean {
	private HrmsubcompanyHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<Hrmsubcompany> insertBeans = new ArrayList<Hrmsubcompany>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = HrmsubcompanyBean.class.getDeclaredMethods();
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
	private HrmsubcompanyBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public HrmsubcompanyBean copyValueForm(Object source){
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
	public HrmsubcompanyBean copyNotNullValueForm(Object source){
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
	public HrmsubcompanyBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public HrmsubcompanyBean(HrmsubcompanyHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:id
	*Comment:
	*/

	public HrmsubcompanyBean setId(int id){
		paramTypeMap.put("id", ClassUtils.wrapperToPrimitive(Integer.class));
		paramValueMap.put("id", id);
		if(this.current != null){
			current.setId(id);
		}
		return this;
	}
	
	/**
	*Name:分部简称
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanyname(String subcompanyname){
		paramTypeMap.put("subcompanyname", String.class);
		paramValueMap.put("subcompanyname", subcompanyname);
		if(this.current != null){
			current.setSubcompanyname(subcompanyname);
		}
		return this;
	}

	/**
	*Name:分部简称
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanynameIfNotNull(String subcompanyname){
	    if(subcompanyname == null){
	    	return this;
	    }
		return setSubcompanyname(subcompanyname);
	}
	/**
	*Name:分部简称
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanynameWithDefaultValueWhenNull(String subcompanyname,String defaultValue){
		String temp = subcompanyname;
	    if(subcompanyname == null){
	    	temp = defaultValue;
	    }
		return setSubcompanyname(temp);
	}
	
	/**
	*Name:分部简称
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanynameIfNotNullAndNotEmpty(String subcompanyname){
		if(StringUtils.isEmpty(subcompanyname)){
	    	return this;
	    }
	    return setSubcompanyname(subcompanyname);
	}
	/**
	*Name:分部简称
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanynameWithDefaultValueWhenNullOrEmpty(String subcompanyname,String defaultValue){
		String temp = subcompanyname;
		if(StringUtils.isEmpty(subcompanyname)){
	    	temp = defaultValue;
	    }
	    return setSubcompanyname(temp);
	}
	
	/**
	*Name:分部描述
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanydesc(String subcompanydesc){
		paramTypeMap.put("subcompanydesc", String.class);
		paramValueMap.put("subcompanydesc", subcompanydesc);
		if(this.current != null){
			current.setSubcompanydesc(subcompanydesc);
		}
		return this;
	}

	/**
	*Name:分部描述
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanydescIfNotNull(String subcompanydesc){
	    if(subcompanydesc == null){
	    	return this;
	    }
		return setSubcompanydesc(subcompanydesc);
	}
	/**
	*Name:分部描述
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanydescWithDefaultValueWhenNull(String subcompanydesc,String defaultValue){
		String temp = subcompanydesc;
	    if(subcompanydesc == null){
	    	temp = defaultValue;
	    }
		return setSubcompanydesc(temp);
	}
	
	/**
	*Name:分部描述
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanydescIfNotNullAndNotEmpty(String subcompanydesc){
		if(StringUtils.isEmpty(subcompanydesc)){
	    	return this;
	    }
	    return setSubcompanydesc(subcompanydesc);
	}
	/**
	*Name:分部描述
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanydescWithDefaultValueWhenNullOrEmpty(String subcompanydesc,String defaultValue){
		String temp = subcompanydesc;
		if(StringUtils.isEmpty(subcompanydesc)){
	    	temp = defaultValue;
	    }
	    return setSubcompanydesc(temp);
	}
	
	/**
	*Name:所属总部ID
	*Comment:
	*/

	public HrmsubcompanyBean setCompanyid(Integer companyid){
		paramTypeMap.put("companyid", Integer.class);
		paramValueMap.put("companyid", companyid);
		if(this.current != null){
			current.setCompanyid(companyid);
		}
		return this;
	}

	/**
	*Name:所属总部ID
	*Comment:
	*/

	public HrmsubcompanyBean setCompanyidIfNotNull(Integer companyid){
	    if(companyid == null){
	    	return this;
	    }
		return setCompanyid(companyid);
	}
	/**
	*Name:所属总部ID
	*Comment:
	*/

	public HrmsubcompanyBean setCompanyidWithDefaultValueWhenNull(Integer companyid,Integer defaultValue){
		Integer temp = companyid;
	    if(companyid == null){
	    	temp = defaultValue;
	    }
		return setCompanyid(temp);
	}
	
	
	/**
	*Name:上级分部Id
	*Comment:
	*/

	public HrmsubcompanyBean setSupsubcomid(Integer supsubcomid){
		paramTypeMap.put("supsubcomid", Integer.class);
		paramValueMap.put("supsubcomid", supsubcomid);
		if(this.current != null){
			current.setSupsubcomid(supsubcomid);
		}
		return this;
	}

	/**
	*Name:上级分部Id
	*Comment:
	*/

	public HrmsubcompanyBean setSupsubcomidIfNotNull(Integer supsubcomid){
	    if(supsubcomid == null){
	    	return this;
	    }
		return setSupsubcomid(supsubcomid);
	}
	/**
	*Name:上级分部Id
	*Comment:
	*/

	public HrmsubcompanyBean setSupsubcomidWithDefaultValueWhenNull(Integer supsubcomid,Integer defaultValue){
		Integer temp = supsubcomid;
	    if(supsubcomid == null){
	    	temp = defaultValue;
	    }
		return setSupsubcomid(temp);
	}
	
	
	/**
	*Name:url
	*Comment:
	*/

	public HrmsubcompanyBean setUrl(String url){
		paramTypeMap.put("url", String.class);
		paramValueMap.put("url", url);
		if(this.current != null){
			current.setUrl(url);
		}
		return this;
	}

	/**
	*Name:url
	*Comment:
	*/

	public HrmsubcompanyBean setUrlIfNotNull(String url){
	    if(url == null){
	    	return this;
	    }
		return setUrl(url);
	}
	/**
	*Name:url
	*Comment:
	*/

	public HrmsubcompanyBean setUrlWithDefaultValueWhenNull(String url,String defaultValue){
		String temp = url;
	    if(url == null){
	    	temp = defaultValue;
	    }
		return setUrl(temp);
	}
	
	/**
	*Name:url
	*Comment:
	*/

	public HrmsubcompanyBean setUrlIfNotNullAndNotEmpty(String url){
		if(StringUtils.isEmpty(url)){
	    	return this;
	    }
	    return setUrl(url);
	}
	/**
	*Name:url
	*Comment:
	*/

	public HrmsubcompanyBean setUrlWithDefaultValueWhenNullOrEmpty(String url,String defaultValue){
		String temp = url;
		if(StringUtils.isEmpty(url)){
	    	temp = defaultValue;
	    }
	    return setUrl(temp);
	}
	
	/**
	*Name:序列号
	*Comment:
	*/

	public HrmsubcompanyBean setShoworder(Integer showorder){
		paramTypeMap.put("showorder", Integer.class);
		paramValueMap.put("showorder", showorder);
		if(this.current != null){
			current.setShoworder(showorder);
		}
		return this;
	}

	/**
	*Name:序列号
	*Comment:
	*/

	public HrmsubcompanyBean setShoworderIfNotNull(Integer showorder){
	    if(showorder == null){
	    	return this;
	    }
		return setShoworder(showorder);
	}
	/**
	*Name:序列号
	*Comment:
	*/

	public HrmsubcompanyBean setShoworderWithDefaultValueWhenNull(Integer showorder,Integer defaultValue){
		Integer temp = showorder;
	    if(showorder == null){
	    	temp = defaultValue;
	    }
		return setShoworder(temp);
	}
	
	
	/**
	*Name:封存标识
	*Comment:
	*/

	public HrmsubcompanyBean setCanceled(Character canceled){
		paramTypeMap.put("canceled", Character.class);
		paramValueMap.put("canceled", canceled);
		if(this.current != null){
			current.setCanceled(canceled);
		}
		return this;
	}

	/**
	*Name:封存标识
	*Comment:
	*/

	public HrmsubcompanyBean setCanceledIfNotNull(Character canceled){
	    if(canceled == null){
	    	return this;
	    }
		return setCanceled(canceled);
	}
	/**
	*Name:封存标识
	*Comment:
	*/

	public HrmsubcompanyBean setCanceledWithDefaultValueWhenNull(Character canceled,Character defaultValue){
		Character temp = canceled;
	    if(canceled == null){
	    	temp = defaultValue;
	    }
		return setCanceled(temp);
	}
	
	
	/**
	*Name:分部编码
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanycode(String subcompanycode){
		paramTypeMap.put("subcompanycode", String.class);
		paramValueMap.put("subcompanycode", subcompanycode);
		if(this.current != null){
			current.setSubcompanycode(subcompanycode);
		}
		return this;
	}

	/**
	*Name:分部编码
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanycodeIfNotNull(String subcompanycode){
	    if(subcompanycode == null){
	    	return this;
	    }
		return setSubcompanycode(subcompanycode);
	}
	/**
	*Name:分部编码
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanycodeWithDefaultValueWhenNull(String subcompanycode,String defaultValue){
		String temp = subcompanycode;
	    if(subcompanycode == null){
	    	temp = defaultValue;
	    }
		return setSubcompanycode(temp);
	}
	
	/**
	*Name:分部编码
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanycodeIfNotNullAndNotEmpty(String subcompanycode){
		if(StringUtils.isEmpty(subcompanycode)){
	    	return this;
	    }
	    return setSubcompanycode(subcompanycode);
	}
	/**
	*Name:分部编码
	*Comment:
	*/

	public HrmsubcompanyBean setSubcompanycodeWithDefaultValueWhenNullOrEmpty(String subcompanycode,String defaultValue){
		String temp = subcompanycode;
		if(StringUtils.isEmpty(subcompanycode)){
	    	temp = defaultValue;
	    }
	    return setSubcompanycode(temp);
	}
	
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmsubcompanyBean setOutkey(String outkey){
		paramTypeMap.put("outkey", String.class);
		paramValueMap.put("outkey", outkey);
		if(this.current != null){
			current.setOutkey(outkey);
		}
		return this;
	}

	/**
	*Name:outkey
	*Comment:
	*/

	public HrmsubcompanyBean setOutkeyIfNotNull(String outkey){
	    if(outkey == null){
	    	return this;
	    }
		return setOutkey(outkey);
	}
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmsubcompanyBean setOutkeyWithDefaultValueWhenNull(String outkey,String defaultValue){
		String temp = outkey;
	    if(outkey == null){
	    	temp = defaultValue;
	    }
		return setOutkey(temp);
	}
	
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmsubcompanyBean setOutkeyIfNotNullAndNotEmpty(String outkey){
		if(StringUtils.isEmpty(outkey)){
	    	return this;
	    }
	    return setOutkey(outkey);
	}
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmsubcompanyBean setOutkeyWithDefaultValueWhenNullOrEmpty(String outkey,String defaultValue){
		String temp = outkey;
		if(StringUtils.isEmpty(outkey)){
	    	temp = defaultValue;
	    }
	    return setOutkey(temp);
	}
	
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/

	public HrmsubcompanyBean setBudgetatuomoveorder(Integer budgetatuomoveorder){
		paramTypeMap.put("budgetatuomoveorder", Integer.class);
		paramValueMap.put("budgetatuomoveorder", budgetatuomoveorder);
		if(this.current != null){
			current.setBudgetatuomoveorder(budgetatuomoveorder);
		}
		return this;
	}

	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/

	public HrmsubcompanyBean setBudgetatuomoveorderIfNotNull(Integer budgetatuomoveorder){
	    if(budgetatuomoveorder == null){
	    	return this;
	    }
		return setBudgetatuomoveorder(budgetatuomoveorder);
	}
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/

	public HrmsubcompanyBean setBudgetatuomoveorderWithDefaultValueWhenNull(Integer budgetatuomoveorder,Integer defaultValue){
		Integer temp = budgetatuomoveorder;
	    if(budgetatuomoveorder == null){
	    	temp = defaultValue;
	    }
		return setBudgetatuomoveorder(temp);
	}
	
	
	/**
	*Name:拼音
	*Comment:
	*/

	public HrmsubcompanyBean setEcologyPinyinSearch(String ecologyPinyinSearch){
		paramTypeMap.put("ecologyPinyinSearch", String.class);
		paramValueMap.put("ecologyPinyinSearch", ecologyPinyinSearch);
		if(this.current != null){
			current.setEcologyPinyinSearch(ecologyPinyinSearch);
		}
		return this;
	}

	/**
	*Name:拼音
	*Comment:
	*/

	public HrmsubcompanyBean setEcologyPinyinSearchIfNotNull(String ecologyPinyinSearch){
	    if(ecologyPinyinSearch == null){
	    	return this;
	    }
		return setEcologyPinyinSearch(ecologyPinyinSearch);
	}
	/**
	*Name:拼音
	*Comment:
	*/

	public HrmsubcompanyBean setEcologyPinyinSearchWithDefaultValueWhenNull(String ecologyPinyinSearch,String defaultValue){
		String temp = ecologyPinyinSearch;
	    if(ecologyPinyinSearch == null){
	    	temp = defaultValue;
	    }
		return setEcologyPinyinSearch(temp);
	}
	
	/**
	*Name:拼音
	*Comment:
	*/

	public HrmsubcompanyBean setEcologyPinyinSearchIfNotNullAndNotEmpty(String ecologyPinyinSearch){
		if(StringUtils.isEmpty(ecologyPinyinSearch)){
	    	return this;
	    }
	    return setEcologyPinyinSearch(ecologyPinyinSearch);
	}
	/**
	*Name:拼音
	*Comment:
	*/

	public HrmsubcompanyBean setEcologyPinyinSearchWithDefaultValueWhenNullOrEmpty(String ecologyPinyinSearch,String defaultValue){
		String temp = ecologyPinyinSearch;
		if(StringUtils.isEmpty(ecologyPinyinSearch)){
	    	temp = defaultValue;
	    }
	    return setEcologyPinyinSearch(temp);
	}
	
	/**
	*Name:限制用户数
	*Comment:
	*/

	public HrmsubcompanyBean setLimitusers(Integer limitusers){
		paramTypeMap.put("limitusers", Integer.class);
		paramValueMap.put("limitusers", limitusers);
		if(this.current != null){
			current.setLimitusers(limitusers);
		}
		return this;
	}

	/**
	*Name:限制用户数
	*Comment:
	*/

	public HrmsubcompanyBean setLimitusersIfNotNull(Integer limitusers){
	    if(limitusers == null){
	    	return this;
	    }
		return setLimitusers(limitusers);
	}
	/**
	*Name:限制用户数
	*Comment:
	*/

	public HrmsubcompanyBean setLimitusersWithDefaultValueWhenNull(Integer limitusers,Integer defaultValue){
		Integer temp = limitusers;
	    if(limitusers == null){
	    	temp = defaultValue;
	    }
		return setLimitusers(temp);
	}
	
	
	/**
	*Name:等级
	*Comment:
	*/

	public HrmsubcompanyBean setTlevel(Integer tlevel){
		paramTypeMap.put("tlevel", Integer.class);
		paramValueMap.put("tlevel", tlevel);
		if(this.current != null){
			current.setTlevel(tlevel);
		}
		return this;
	}

	/**
	*Name:等级
	*Comment:
	*/

	public HrmsubcompanyBean setTlevelIfNotNull(Integer tlevel){
	    if(tlevel == null){
	    	return this;
	    }
		return setTlevel(tlevel);
	}
	/**
	*Name:等级
	*Comment:
	*/

	public HrmsubcompanyBean setTlevelWithDefaultValueWhenNull(Integer tlevel,Integer defaultValue){
		Integer temp = tlevel;
	    if(tlevel == null){
	    	temp = defaultValue;
	    }
		return setTlevel(temp);
	}
	
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<Hrmsubcompany> update(){
		return this.helper.each(new Each<Hrmsubcompany>(){

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> list) {
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
	public Hrmsubcompany updateUnique(){
		Hrmsubcompany bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<Hrmsubcompany>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(Hrmsubcompany bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<Hrmsubcompany> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private Hrmsubcompany current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public HrmsubcompanyBean create(){
		Hrmsubcompany bean = new Hrmsubcompany();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<Hrmsubcompany> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (Hrmsubcompany bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public Hrmsubcompany insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		Hrmsubcompany bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public Hrmsubcompany ifFoundUpdateElseInsert(){
		Hrmsubcompany bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new Hrmsubcompany();
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
		Hrmsubcompany bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<Hrmsubcompany> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Hrmsubcompany bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<Hrmsubcompany> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<Hrmsubcompany> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<Hrmsubcompany> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<Hrmsubcompany> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<Hrmsubcompany> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<Hrmsubcompany> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (Hrmsubcompany bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			Hrmsubcompany bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(Hrmsubcompany bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = Hrmsubcompany.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
