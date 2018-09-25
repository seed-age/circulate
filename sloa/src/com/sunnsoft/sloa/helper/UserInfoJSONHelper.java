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
public class UserInfoJSONHelper {
	
	private UserInfoHelper helper;
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
	
	public UserInfoJSONHelper(UserInfoHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(UserInfo.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(UserInfo.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(UserInfo.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(UserInfo.class);
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
			jsonHelper.excludeAll(UserInfo.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(UserInfo.class);
				break;
			case 2:
				jsonHelper.excludeChildren(UserInfo.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(UserInfo.class);
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
	public UserInfoJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public UserInfoJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public UserInfoHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public UserInfoJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public UserInfoJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public UserInfoJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public UserInfoJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public UserInfoJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public UserInfoJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public UserInfoJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<UserInfo> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<UserInfo>() {

			@Override
			public void each(UserInfo bean, List<UserInfo> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<UserInfo> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<UserInfo>() {

			@Override
			public void each(UserInfo bean, List<UserInfo> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<UserInfo> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<UserInfo>() {

			@Override
			public void each(UserInfo bean, List<UserInfo> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<UserInfo> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<UserInfo>() {

			@Override
			public void each(UserInfo bean, List<UserInfo> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<UserInfo> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<UserInfo>() {

			@Override
			public void each(UserInfo bean, List<UserInfo> beans) {
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
	public String uniqueJson(EachEntity2Map<UserInfo> eachEntity2Map){
		UserInfo bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<UserInfo> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<UserInfo>(){

			@Override
			public void each(UserInfo bean, List<UserInfo> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<UserInfo> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<UserInfo> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<UserInfo> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<UserInfo>(){

			@Override
			public void each(UserInfo bean, List<UserInfo> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<UserInfo> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<UserInfo>(){

			@Override
			public void each(UserInfo bean, List<UserInfo> beans) {
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
	 * 序列化json时候，包含 userId (用户ID)字段
	 * @return
	 */
	public UserInfoJSONHelper includeUserId(){
		this.excludeAll = true;
		this.includes.add("userId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userId (用户ID)字段
	 * @return
	 */
	public UserInfoJSONHelper excludeUserId(){
		this.excludeAll = false;
		this.excludes.add("userId");
		return this;
	}
	/**
	 * 序列化json时候，包含 user ()字段
	 * @return
	 */
	public UserInfoJSONHelper includeUser(){
		this.excludeAll = true;
		this.includes.add("user");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 user ()字段
	 * @return
	 */
	public UserInfoJSONHelper excludeUser(){
		this.excludeAll = false;
		this.excludes.add("user");
		return this;
	}
	/**
	 * 序列化json时候，包含 lastName (姓)字段
	 * @return
	 */
	public UserInfoJSONHelper includeLastName(){
		this.excludeAll = true;
		this.includes.add("lastName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 lastName (姓)字段
	 * @return
	 */
	public UserInfoJSONHelper excludeLastName(){
		this.excludeAll = false;
		this.excludes.add("lastName");
		return this;
	}
	/**
	 * 序列化json时候，包含 firstName (名)字段
	 * @return
	 */
	public UserInfoJSONHelper includeFirstName(){
		this.excludeAll = true;
		this.includes.add("firstName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 firstName (名)字段
	 * @return
	 */
	public UserInfoJSONHelper excludeFirstName(){
		this.excludeAll = false;
		this.excludes.add("firstName");
		return this;
	}
	/**
	 * 序列化json时候，包含 userName (姓名)字段
	 * @return
	 */
	public UserInfoJSONHelper includeUserName(){
		this.excludeAll = true;
		this.includes.add("userName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userName (姓名)字段
	 * @return
	 */
	public UserInfoJSONHelper excludeUserName(){
		this.excludeAll = false;
		this.excludes.add("userName");
		return this;
	}
	/**
	 * 序列化json时候，包含 email (电子邮件)字段
	 * @return
	 */
	public UserInfoJSONHelper includeEmail(){
		this.excludeAll = true;
		this.includes.add("email");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 email (电子邮件)字段
	 * @return
	 */
	public UserInfoJSONHelper excludeEmail(){
		this.excludeAll = false;
		this.excludes.add("email");
		return this;
	}
	/**
	 * 序列化json时候，包含 phoneNumber (联系电话)字段
	 * @return
	 */
	public UserInfoJSONHelper includePhoneNumber(){
		this.excludeAll = true;
		this.includes.add("phoneNumber");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 phoneNumber (联系电话)字段
	 * @return
	 */
	public UserInfoJSONHelper excludePhoneNumber(){
		this.excludeAll = false;
		this.excludes.add("phoneNumber");
		return this;
	}
	/**
	 * 序列化json时候，包含 remark (备注)字段
	 * @return
	 */
	public UserInfoJSONHelper includeRemark(){
		this.excludeAll = true;
		this.includes.add("remark");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 remark (备注)字段
	 * @return
	 */
	public UserInfoJSONHelper excludeRemark(){
		this.excludeAll = false;
		this.excludes.add("remark");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key userId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserInfoJSONHelper replaceKeyForUserId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserInfo.class, "userId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key user 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserInfoJSONHelper replaceKeyForUser(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("user", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserInfo.class, "user", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key lastName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserInfoJSONHelper replaceKeyForLastName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("lastName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserInfo.class, "lastName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key firstName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserInfoJSONHelper replaceKeyForFirstName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("firstName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserInfo.class, "firstName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserInfoJSONHelper replaceKeyForUserName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserInfo.class, "userName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key email 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserInfoJSONHelper replaceKeyForEmail(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("email", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserInfo.class, "email", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key phoneNumber 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserInfoJSONHelper replaceKeyForPhoneNumber(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("phoneNumber", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserInfo.class, "phoneNumber", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key remark 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserInfoJSONHelper replaceKeyForRemark(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("remark", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserInfo.class, "remark", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 userId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserInfoJSONHelper filterUserId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserInfo.class, Long.class, filterValue, "userId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 user 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserInfoJSONHelper filterUser(FilterValue<User> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserInfo.class, User.class, filterValue, "user");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 lastName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserInfoJSONHelper filterLastName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserInfo.class, String.class, filterValue, "lastName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 firstName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserInfoJSONHelper filterFirstName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserInfo.class, String.class, filterValue, "firstName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserInfoJSONHelper filterUserName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserInfo.class, String.class, filterValue, "userName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 email 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserInfoJSONHelper filterEmail(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserInfo.class, String.class, filterValue, "email");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 phoneNumber 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserInfoJSONHelper filterPhoneNumber(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserInfo.class, String.class, filterValue, "phoneNumber");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 remark 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserInfoJSONHelper filterRemark(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserInfo.class, String.class, filterValue, "remark");
		return this;
	}
	
}
