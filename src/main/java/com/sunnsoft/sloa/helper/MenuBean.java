package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.Menu;
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
public class MenuBean {
	private MenuHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<Menu> insertBeans = new ArrayList<Menu>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = MenuBean.class.getDeclaredMethods();
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
	private MenuBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public MenuBean copyValueForm(Object source){
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
	public MenuBean copyNotNullValueForm(Object source){
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
	public MenuBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public MenuBean(MenuHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:菜单ID
	*Comment:
	*/

	public MenuBean setMenuId(Long menuId){
		paramTypeMap.put("menuId", Long.class);
		paramValueMap.put("menuId", menuId);
		if(this.current != null){
			current.setMenuId(menuId);
		}
		return this;
	}

	/**
	*Name:菜单ID
	*Comment:
	*/

	public MenuBean setMenuIdIfNotNull(Long menuId){
	    if(menuId == null){
	    	return this;
	    }
		return setMenuId(menuId);
	}
	/**
	*Name:菜单ID
	*Comment:
	*/

	public MenuBean setMenuIdWithDefaultValueWhenNull(Long menuId,Long defaultValue){
		Long temp = menuId;
	    if(menuId == null){
	    	temp = defaultValue;
	    }
		return setMenuId(temp);
	}
	
	

	public MenuBean setMenu(Menu menu){
		paramTypeMap.put("menu", Menu.class);
		paramValueMap.put("menu", menu);
		if(this.current != null){
			current.setMenu(menu);
		}
		return this;
	}


	public MenuBean setMenuIfNotNull(Menu menu){
	    if(menu == null){
	    	return this;
	    }
		return setMenu(menu);
	}

	public MenuBean setMenuWithDefaultValueWhenNull(Menu menu,Menu defaultValue){
		Menu temp = menu;
	    if(menu == null){
	    	temp = defaultValue;
	    }
		return setMenu(temp);
	}
	
	
	/**
	*Name:菜单名称
	*Comment:
	*/

	public MenuBean setMenuText(String menuText){
		paramTypeMap.put("menuText", String.class);
		paramValueMap.put("menuText", menuText);
		if(this.current != null){
			current.setMenuText(menuText);
		}
		return this;
	}

	/**
	*Name:菜单名称
	*Comment:
	*/

	public MenuBean setMenuTextIfNotNull(String menuText){
	    if(menuText == null){
	    	return this;
	    }
		return setMenuText(menuText);
	}
	/**
	*Name:菜单名称
	*Comment:
	*/

	public MenuBean setMenuTextWithDefaultValueWhenNull(String menuText,String defaultValue){
		String temp = menuText;
	    if(menuText == null){
	    	temp = defaultValue;
	    }
		return setMenuText(temp);
	}
	
	/**
	*Name:菜单名称
	*Comment:
	*/

	public MenuBean setMenuTextIfNotNullAndNotEmpty(String menuText){
		if(StringUtils.isEmpty(menuText)){
	    	return this;
	    }
	    return setMenuText(menuText);
	}
	/**
	*Name:菜单名称
	*Comment:
	*/

	public MenuBean setMenuTextWithDefaultValueWhenNullOrEmpty(String menuText,String defaultValue){
		String temp = menuText;
		if(StringUtils.isEmpty(menuText)){
	    	temp = defaultValue;
	    }
	    return setMenuText(temp);
	}
	
	/**
	*Name:图标路径
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIcon(String icon){
		paramTypeMap.put("icon", String.class);
		paramValueMap.put("icon", icon);
		if(this.current != null){
			current.setIcon(icon);
		}
		return this;
	}

	/**
	*Name:图标路径
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconIfNotNull(String icon){
	    if(icon == null){
	    	return this;
	    }
		return setIcon(icon);
	}
	/**
	*Name:图标路径
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconWithDefaultValueWhenNull(String icon,String defaultValue){
		String temp = icon;
	    if(icon == null){
	    	temp = defaultValue;
	    }
		return setIcon(temp);
	}
	
	/**
	*Name:图标路径
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconIfNotNullAndNotEmpty(String icon){
		if(StringUtils.isEmpty(icon)){
	    	return this;
	    }
	    return setIcon(icon);
	}
	/**
	*Name:图标路径
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconWithDefaultValueWhenNullOrEmpty(String icon,String defaultValue){
		String temp = icon;
		if(StringUtils.isEmpty(icon)){
	    	temp = defaultValue;
	    }
	    return setIcon(temp);
	}
	
	/**
	*Name:ACTION路径
	*Comment:
	*/

	public MenuBean setActionPath(String actionPath){
		paramTypeMap.put("actionPath", String.class);
		paramValueMap.put("actionPath", actionPath);
		if(this.current != null){
			current.setActionPath(actionPath);
		}
		return this;
	}

	/**
	*Name:ACTION路径
	*Comment:
	*/

	public MenuBean setActionPathIfNotNull(String actionPath){
	    if(actionPath == null){
	    	return this;
	    }
		return setActionPath(actionPath);
	}
	/**
	*Name:ACTION路径
	*Comment:
	*/

	public MenuBean setActionPathWithDefaultValueWhenNull(String actionPath,String defaultValue){
		String temp = actionPath;
	    if(actionPath == null){
	    	temp = defaultValue;
	    }
		return setActionPath(temp);
	}
	
	/**
	*Name:ACTION路径
	*Comment:
	*/

	public MenuBean setActionPathIfNotNullAndNotEmpty(String actionPath){
		if(StringUtils.isEmpty(actionPath)){
	    	return this;
	    }
	    return setActionPath(actionPath);
	}
	/**
	*Name:ACTION路径
	*Comment:
	*/

	public MenuBean setActionPathWithDefaultValueWhenNullOrEmpty(String actionPath,String defaultValue){
		String temp = actionPath;
		if(StringUtils.isEmpty(actionPath)){
	    	temp = defaultValue;
	    }
	    return setActionPath(temp);
	}
	
	/**
	*Name:EXT_ID
	*Comment:
	*/

	public MenuBean setExtId(String extId){
		paramTypeMap.put("extId", String.class);
		paramValueMap.put("extId", extId);
		if(this.current != null){
			current.setExtId(extId);
		}
		return this;
	}

	/**
	*Name:EXT_ID
	*Comment:
	*/

	public MenuBean setExtIdIfNotNull(String extId){
	    if(extId == null){
	    	return this;
	    }
		return setExtId(extId);
	}
	/**
	*Name:EXT_ID
	*Comment:
	*/

	public MenuBean setExtIdWithDefaultValueWhenNull(String extId,String defaultValue){
		String temp = extId;
	    if(extId == null){
	    	temp = defaultValue;
	    }
		return setExtId(temp);
	}
	
	/**
	*Name:EXT_ID
	*Comment:
	*/

	public MenuBean setExtIdIfNotNullAndNotEmpty(String extId){
		if(StringUtils.isEmpty(extId)){
	    	return this;
	    }
	    return setExtId(extId);
	}
	/**
	*Name:EXT_ID
	*Comment:
	*/

	public MenuBean setExtIdWithDefaultValueWhenNullOrEmpty(String extId,String defaultValue){
		String temp = extId;
		if(StringUtils.isEmpty(extId)){
	    	temp = defaultValue;
	    }
	    return setExtId(temp);
	}
	
	/**
	*Name:布局
	*Comment:
	*/

	public MenuBean setLayout(String layout){
		paramTypeMap.put("layout", String.class);
		paramValueMap.put("layout", layout);
		if(this.current != null){
			current.setLayout(layout);
		}
		return this;
	}

	/**
	*Name:布局
	*Comment:
	*/

	public MenuBean setLayoutIfNotNull(String layout){
	    if(layout == null){
	    	return this;
	    }
		return setLayout(layout);
	}
	/**
	*Name:布局
	*Comment:
	*/

	public MenuBean setLayoutWithDefaultValueWhenNull(String layout,String defaultValue){
		String temp = layout;
	    if(layout == null){
	    	temp = defaultValue;
	    }
		return setLayout(temp);
	}
	
	/**
	*Name:布局
	*Comment:
	*/

	public MenuBean setLayoutIfNotNullAndNotEmpty(String layout){
		if(StringUtils.isEmpty(layout)){
	    	return this;
	    }
	    return setLayout(layout);
	}
	/**
	*Name:布局
	*Comment:
	*/

	public MenuBean setLayoutWithDefaultValueWhenNullOrEmpty(String layout,String defaultValue){
		String temp = layout;
		if(StringUtils.isEmpty(layout)){
	    	temp = defaultValue;
	    }
	    return setLayout(temp);
	}
	
	/**
	*Name:是否叶节点
	*Comment:
	*/

	public MenuBean setLeaf(boolean leaf){
		paramTypeMap.put("leaf", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("leaf", leaf);
		if(this.current != null){
			current.setLeaf(leaf);
		}
		return this;
	}
	
	/**
	*Name:是否展开
	*Comment:
	*/

	public MenuBean setExpanded(boolean expanded){
		paramTypeMap.put("expanded", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("expanded", expanded);
		if(this.current != null){
			current.setExpanded(expanded);
		}
		return this;
	}
	
	/**
	*Name:排序
	*Comment:
	*/

	public MenuBean setIndexValue(int indexValue){
		paramTypeMap.put("indexValue", ClassUtils.wrapperToPrimitive(Integer.class));
		paramValueMap.put("indexValue", indexValue);
		if(this.current != null){
			current.setIndexValue(indexValue);
		}
		return this;
	}
	
	/**
	*Name:是否停用
	*Comment:
	*/

	public MenuBean setEnabled(boolean enabled){
		paramTypeMap.put("enabled", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("enabled", enabled);
		if(this.current != null){
			current.setEnabled(enabled);
		}
		return this;
	}
	
	/**
	*Name:图标class
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconCls(String iconCls){
		paramTypeMap.put("iconCls", String.class);
		paramValueMap.put("iconCls", iconCls);
		if(this.current != null){
			current.setIconCls(iconCls);
		}
		return this;
	}

	/**
	*Name:图标class
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconClsIfNotNull(String iconCls){
	    if(iconCls == null){
	    	return this;
	    }
		return setIconCls(iconCls);
	}
	/**
	*Name:图标class
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconClsWithDefaultValueWhenNull(String iconCls,String defaultValue){
		String temp = iconCls;
	    if(iconCls == null){
	    	temp = defaultValue;
	    }
		return setIconCls(temp);
	}
	
	/**
	*Name:图标class
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconClsIfNotNullAndNotEmpty(String iconCls){
		if(StringUtils.isEmpty(iconCls)){
	    	return this;
	    }
	    return setIconCls(iconCls);
	}
	/**
	*Name:图标class
	*Comment:图标class和图标路径只能填写其中一种。
	*/

	public MenuBean setIconClsWithDefaultValueWhenNullOrEmpty(String iconCls,String defaultValue){
		String temp = iconCls;
		if(StringUtils.isEmpty(iconCls)){
	    	temp = defaultValue;
	    }
	    return setIconCls(temp);
	}
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<Menu> update(){
		return this.helper.each(new Each<Menu>(){

			@Override
			public void each(Menu bean, List<Menu> list) {
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
	public Menu updateUnique(){
		Menu bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<Menu>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(Menu bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<Menu> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private Menu current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public MenuBean create(){
		Menu bean = new Menu();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<Menu> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (Menu bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public Menu insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		Menu bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public Menu ifFoundUpdateElseInsert(){
		Menu bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new Menu();
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
		Menu bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<Menu> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Menu bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<Menu> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<Menu> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<Menu> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<Menu> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<Menu> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<Menu> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (Menu bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			Menu bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(Menu bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = Menu.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
