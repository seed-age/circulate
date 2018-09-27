package com.sunnsoft.sloa.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import java.lang.reflect.InvocationTargetException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.db.helper.hibernate.Each;
import org.gteam.db.helper.hibernate.HelperException;
import org.gteam.db.helper.json.EachJSON;
import org.gteam.db.helper.json.FilterValue;
import com.sunnsoft.sloa.db.vo.*;
import org.gteam.service.IService;
import org.gteam.util.FastJSONUtils;
import org.gteam.util.JSONUtils;
import org.gteam.db.dao.PageList;
import org.gteam.db.dao.PageScroll;
import org.gteam.db.helper.json.EachEntity2Map;
import org.gteam.util.EntityUtils;

@SuppressWarnings("unchecked")
public class RoleJSONHelper {
	
	private RoleHelper helper;
	private String dateFormat ;
	private boolean prettyFormat;
	private FastJSONUtils.JsonHelper fastJsonHelper;
	private JSONUtils.JsonHelper jsonHelper;
	private Set<String> excludes = new HashSet<String>();//只有excludeAll为false的时候使用
	private Set<String> includes = new HashSet<String>();//只有在excludeAll为true的时候使用
	private Map<String,String> replaceMap = new HashMap<String, String>();//存储被替换过的key,以便ValueFilter参考。
	private boolean excludeAll = false;//是否排除所有属性
	private int excludeRelactionType = 3;//只有excludeAll为false的时候使用 
	private List<NameFilter> nameFilters = new ArrayList<NameFilter>();
	private List<ValueFilter> valueFilters = new ArrayList<ValueFilter>();
	private boolean enableNameFilter;
	private boolean enableValueFilter;
	private boolean debug;
	private boolean convertBooleanToString;
	private boolean enableNullValue = true; //默认开启JSON的null值显示
	
	public RoleJSONHelper(RoleHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(Role.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(Role.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(Role.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(Role.class);
					break;
				default:
					break;
				}
			}
		}
		if(dateFormat != null){
			fastJsonHelper.setDateFormat(dateFormat);
		}
		if(convertBooleanToString){
			fastJsonHelper.registerValueFilter(new ValueFilter(){
			
				@Override
				public Object process(Object source, String name, Object value) {
					if(value instanceof Boolean){
						return String.valueOf(value);
					}
					return value;
				}
			
			});
		}
		fastJsonHelper.enableNullValue(enableNullValue);
		if(!ignoreExcludeAndFilter){
			for (Iterator<NameFilter> iterator = nameFilters.iterator(); iterator.hasNext();) {
				NameFilter nameFilter =  iterator.next();
				fastJsonHelper.registerNameFilter(nameFilter);
			}
			for (Iterator<ValueFilter> iterator = valueFilters.iterator(); iterator.hasNext();) {
				ValueFilter valueFilter = iterator.next();
				fastJsonHelper.registerValueFilter(valueFilter);
			}
		}
		if(excludeRelactionType != 3){
			return (String)this.helper.service.executeTransactional(new TransactionalCallBack(){
	
				@Override
				public Object execute(IService service) {
					String result = prettyFormat ? fastJsonHelper.toJSONString(object,true):fastJsonHelper.toJSONString(object);
					if(debug){
						System.out.println(result);
					}
					return result;
				}
				
			});
		}else{
			String result = prettyFormat ? fastJsonHelper.toJSONString(object,true):fastJsonHelper.toJSONString(object);
			if(debug){
				System.out.println(result);
			}
			return result;
		}
	}
	
	protected JSONObject jsonSerializer(final Object object){
		jsonHelper = JSONUtils.getJsonHelper();
		if(excludeAll){
			jsonHelper.excludeAll(Role.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(Role.class);
				break;
			case 2:
				jsonHelper.excludeChildren(Role.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(Role.class);
				break;
			default:
				break;
			}
		}
		if(dateFormat != null){
			jsonHelper.setDateFormat(dateFormat);
		}
		if(convertBooleanToString){
			jsonHelper.setAllBooleanToString();
		}
		if(excludeRelactionType != 3){
			return (JSONObject)this.helper.service.executeTransactional(new TransactionalCallBack(){
	
				@Override
				public Object execute(IService service) {
					JSONObject result = jsonHelper.toJSONObject(object);
					if(debug){
						System.out.println(result);
					}
					return result;
				}
				
			});
		}else{
			JSONObject result = jsonHelper.toJSONObject(object);
			if(debug){
				System.out.println(result);
			}
			return result;
		}
	}
	
	/**
	 * 设置JSON日期转换格式，默认是"yyyy-MM-dd HH:mm:ss"
	 * @param format
	 * @return
	 */
	public RoleJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public RoleJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public RoleHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public RoleJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public RoleJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public RoleJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public RoleJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public RoleJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public RoleJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public RoleJSONHelper keepXtoX(){
		this.excludeRelactionType = 0;
		return this;
	}
	/**
	 * 直接把查到的List转换为json字符串
	 * @return
	 */
	public String listJson(){
		return this.fastJsonSerializer(this.helper.list(),false);
	}
	
	/**
	 * 直接把查到的List转换为json字符串,可以使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要。
	 * @param eachEntity2Map 迭代实体和转换出来的map(排除了一对多，多对多，一对一关联关系)
	 * @return
	 */
	public String listJson(final EachEntity2Map<Role> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<Role>() {

			@Override
			public void each(Role bean, List<Role> beans) {
				try {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					list.add(map);
					eachEntity2Map.each(bean,map);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
		});
		return this.fastJsonSerializer(list,false);
	}
	
	/**
	 * 直接把查到的PageList转换为json字符串,使用默认分页
	 * @param page 第几页，1表示第一页
	 * @return
	 */
	public String listPageJson(int page){
		return this.fastJsonSerializer(this.helper.listPage(page),false);
	}
	
	/**
	 * 直接把查到的PageScroll转换为json字符串,使用默认分页
	 * @param page 第几页，1表示第一页
	 * @return
	 */
	public String listPageScrollJson(int page){
		return this.fastJsonSerializer(this.helper.listPageScroll(page),false);
	}
	
	/**
	 * 直接把查到的PageList转换为json字符串,使用默认分页,可以使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要。
	 * @param page
	 * @param eachEntity2Map 迭代实体和转换出来的map(排除了一对多，多对多，一对一关联关系)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String listPageJson(int page,final EachEntity2Map<Role> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<Role>() {

			@Override
			public void each(Role bean, List<Role> beans) {
				try {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					list.add(map);
					eachEntity2Map.each(bean,map);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
		});
		pageList.setList(list);
		return this.fastJsonSerializer(pageList,false);
	}
	
	/**
	 * 直接把查到的PageScroll转换为json字符串,使用默认分页,可以使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要。
	 * @param page
	 * @param eachEntity2Map 迭代实体和转换出来的map(排除了一对多，多对多，一对一关联关系)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String listPageScrollJson(int page,final EachEntity2Map<Role> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<Role>() {

			@Override
			public void each(Role bean, List<Role> beans) {
				try {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					list.add(map);
					eachEntity2Map.each(bean,map);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
		});
		pageScroll.setList(list);
		return this.fastJsonSerializer(pageScroll,false);
	}
	
	/**
	 * 直接把查到的PageList转换为json字符串
	 * @param page 第几页，1表示第一页
	 * @param pageSize 每页几行
	 * @return
	 */
	public String listPageJson(int page,int pageSize){
		return this.fastJsonSerializer(this.helper.listPage(page, pageSize),false);
	}
	
	/**
	 * 直接把查到的PageScroll转换为json字符串
	 * @param page 第几页，1表示第一页
	 * @param pageSize 每页几行
	 * @return
	 */
	public String listPageScrollJson(int page,int pageSize){
		return this.fastJsonSerializer(this.helper.listPageScroll(page, pageSize),false);
	}
	
	/**
	 * 直接把查到的PageList转换为json字符串,可以使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要。
	 * @param page 第几页，1表示第一页
	 * @param pageSize 每页几行
	 * @param eachEntity2Map 迭代实体和转换出来的map(排除了一对多，多对多，一对一关联关系)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<Role> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<Role>() {

			@Override
			public void each(Role bean, List<Role> beans) {
				try {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					list.add(map);
					eachEntity2Map.each(bean,map);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
		});
		pageList.setList(list);
		return this.fastJsonSerializer(pageList,false);
	}
	
	/**
	 * 直接把查到的PageScroll转换为json字符串,可以使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要。
	 * @param page 第几页，1表示第一页
	 * @param pageSize 每页几行
	 * @param eachEntity2Map 迭代实体和转换出来的map(排除了一对多，多对多，一对一关联关系)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<Role> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<Role>() {

			@Override
			public void each(Role bean, List<Role> beans) {
				try {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					list.add(map);
					eachEntity2Map.each(bean,map);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
		});
		pageScroll.setList(list);
		return this.fastJsonSerializer(pageScroll,false);
	}
	
	/**
	 * 查到的唯一对象转换为json字符串，当对象不止一个的时候抛出异常
	 * @return
	 */
	public String uniqueJson(){
		return this.fastJsonSerializer(this.helper.uniqueResult(),false);
	}
	
	/**
	 * 查到的唯一对象转换为json字符串，当对象不止一个的时候抛出异常,可以使用EachEntity2Map 对唯一的实体转的map进行调整,以符合实际需要。
	 * @param eachEntity2Map 处理实体和转换出来的map(排除了一对多，多对多，一对一关联关系)
	 * @return
	 */
	public String uniqueJson(EachEntity2Map<Role> eachEntity2Map){
		Role bean = this.helper.uniqueResult();
		Map map = null;
		try {
			map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		eachEntity2Map.each(bean, map);
		return this.fastJsonSerializer(map,false);
	}
	
	/**
	 * 查到的唯一对象转换为JSONObject，当对象不止一个的时候抛出异常
	 * @return
	 */
	public JSONObject uniqueJsonObject(){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		return this.jsonSerializer(this.helper.uniqueResult());
	}
	
	/**
	 * 迭代每个json对象和实体对象，进行处理
	 * @param eachJSON
	 * @return 返回 JSONArray对象
	 */
	public JSONArray jsonEach(final EachJSON<Role> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<Role>(){

			@Override
			public void each(Role bean, List<Role> beans) {
				JSONObject jsonObject = jsonSerializer(bean);
				eachJSON.each(bean, jsonObject);
				jsonArray.add(jsonObject);
			}
			
		});
		return jsonArray;
	}
	/**
	 * 迭代PageList里的每个json对象和实体对象，进行处理，使用默认分页
	 * @param page 第几页，1表示第一页
	 * @param eachJSON 
	 * @return
	 */
	public JSONObject jsonEachInPage(int page,EachJSON<Role> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		return this.jsonEachInPage(page, this.helper.pageSize, eachJSON);
	}
	/**
	 * 迭代PageScroll里的每个json对象和实体对象，进行处理，使用默认分页
	 * @param page 第几页，1表示第一页
	 * @param eachJSON 
	 * @return
	 */
	public JSONObject jsonEachInPageScroll(int page,EachJSON<Role> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		return this.jsonEachInPageScroll(page, this.helper.pageSize, eachJSON);
	}
	/**
	 * 迭代PageList里的每个json对象和实体对象，进行处理
	 * @param page 第几页，1表示第一页
	 * @param pageSize 每页几行
	 * @param eachJSON
	 * @return
	 */
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<Role> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		JSONUtils.JsonHelper innerHelper = JSONUtils.getJsonHelper();
		if(dateFormat != null){
			innerHelper.setDateFormat(dateFormat);
		}
		innerHelper.addExclude("list");
		final JSONArray jsonArray = new JSONArray();
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<Role>(){

			@Override
			public void each(Role bean, List<Role> beans) {
				JSONObject jsonObject = jsonSerializer(bean);
				eachJSON.each(bean, jsonObject);
				jsonArray.add(jsonObject);
			}
			
		}));
		
		result.put("list", jsonArray);
		
		return result;
	}
	
	/**
	 * 迭代PageScroll里的每个json对象和实体对象，进行处理
	 * @param page 第几页，1表示第一页
	 * @param pageSize 每页几行
	 * @param eachJSON
	 * @return
	 */
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<Role> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		JSONUtils.JsonHelper innerHelper = JSONUtils.getJsonHelper();
		if(dateFormat != null){
			innerHelper.setDateFormat(dateFormat);
		}
		innerHelper.addExclude("list");
		final JSONArray jsonArray = new JSONArray();
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<Role>(){

			@Override
			public void each(Role bean, List<Role> beans) {
				JSONObject jsonObject = jsonSerializer(bean);
				eachJSON.each(bean, jsonObject);
				jsonArray.add(jsonObject);
			}
			
		}));
		
		result.put("list", jsonArray);
		
		return result;
	}
	
	/**
	 * 直接序列化对象为json字符串，一般用来处理自己组装的Map和list等对象。
	 * @param object
	 * @return
	 */
	public String toJson(Object object){
		return this.fastJsonSerializer(object,false);
	}
	
	//----exclude或include序列化字段----//
	
	/**
	 * 序列化json时候，包含 roleId (角色ID)字段
	 * @return
	 */
	public RoleJSONHelper includeRoleId(){
		this.excludeAll = true;
		this.includes.add("roleId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 roleId (角色ID)字段
	 * @return
	 */
	public RoleJSONHelper excludeRoleId(){
		this.excludeAll = false;
		this.excludes.add("roleId");
		return this;
	}
	/**
	 * 序列化json时候，包含 role ()字段
	 * @return
	 */
	public RoleJSONHelper includeRole(){
		this.excludeAll = true;
		this.includes.add("role");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 role ()字段
	 * @return
	 */
	public RoleJSONHelper excludeRole(){
		this.excludeAll = false;
		this.excludes.add("role");
		return this;
	}
	/**
	 * 序列化json时候，包含 roleName (角色名称)字段
	 * @return
	 */
	public RoleJSONHelper includeRoleName(){
		this.excludeAll = true;
		this.includes.add("roleName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 roleName (角色名称)字段
	 * @return
	 */
	public RoleJSONHelper excludeRoleName(){
		this.excludeAll = false;
		this.excludes.add("roleName");
		return this;
	}
	/**
	 * 序列化json时候，包含 roleDescription (角色描述)字段
	 * @return
	 */
	public RoleJSONHelper includeRoleDescription(){
		this.excludeAll = true;
		this.includes.add("roleDescription");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 roleDescription (角色描述)字段
	 * @return
	 */
	public RoleJSONHelper excludeRoleDescription(){
		this.excludeAll = false;
		this.excludes.add("roleDescription");
		return this;
	}
	/**
	 * 序列化json时候，包含 status (状态)字段
	 * @return
	 */
	public RoleJSONHelper includeStatus(){
		this.excludeAll = true;
		this.includes.add("status");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 status (状态)字段
	 * @return
	 */
	public RoleJSONHelper excludeStatus(){
		this.excludeAll = false;
		this.excludes.add("status");
		return this;
	}
	/**
	 * 序列化json时候，包含 users ()字段
	 * @return
	 */
	public RoleJSONHelper includeUsers(){
		this.excludeAll = true;
		this.includes.add("users");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 users ()字段
	 * @return
	 */
	public RoleJSONHelper excludeUsers(){
		this.excludeAll = false;
		this.excludes.add("users");
		return this;
	}
	/**
	 * 序列化json时候，包含 menus ()字段
	 * @return
	 */
	public RoleJSONHelper includeMenus(){
		this.excludeAll = true;
		this.includes.add("menus");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 menus ()字段
	 * @return
	 */
	public RoleJSONHelper excludeMenus(){
		this.excludeAll = false;
		this.excludes.add("menus");
		return this;
	}
	/**
	 * 序列化json时候，包含 roles ()字段
	 * @return
	 */
	public RoleJSONHelper includeRoles(){
		this.excludeAll = true;
		this.includes.add("roles");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 roles ()字段
	 * @return
	 */
	public RoleJSONHelper excludeRoles(){
		this.excludeAll = false;
		this.excludes.add("roles");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key roleId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public RoleJSONHelper replaceKeyForRoleId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("roleId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Role.class, "roleId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key role 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public RoleJSONHelper replaceKeyForRole(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("role", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Role.class, "role", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key roleName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public RoleJSONHelper replaceKeyForRoleName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("roleName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Role.class, "roleName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key roleDescription 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public RoleJSONHelper replaceKeyForRoleDescription(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("roleDescription", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Role.class, "roleDescription", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key status 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public RoleJSONHelper replaceKeyForStatus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("status", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Role.class, "status", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key users 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public RoleJSONHelper replaceKeyForUsers(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("users", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Role.class, "users", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key menus 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public RoleJSONHelper replaceKeyForMenus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("menus", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Role.class, "menus", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key roles 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public RoleJSONHelper replaceKeyForRoles(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("roles", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Role.class, "roles", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 roleId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public RoleJSONHelper filterRoleId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Role.class, Long.class, filterValue, "roleId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 role 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public RoleJSONHelper filterRole(FilterValue<Role> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Role.class, Role.class, filterValue, "role");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 roleName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public RoleJSONHelper filterRoleName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Role.class, String.class, filterValue, "roleName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 roleDescription 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public RoleJSONHelper filterRoleDescription(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Role.class, String.class, filterValue, "roleDescription");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 status 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public RoleJSONHelper filterStatus(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Role.class, Boolean.class, filterValue, "status");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 users 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public RoleJSONHelper filterUsers(FilterValue<User> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Role.class, User.class, filterValue, "users");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 menus 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public RoleJSONHelper filterMenus(FilterValue<Menu> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Role.class, Menu.class, filterValue, "menus");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 roles 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public RoleJSONHelper filterRoles(FilterValue<Role> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Role.class, Role.class, filterValue, "roles");
		return this;
	}
	
}
