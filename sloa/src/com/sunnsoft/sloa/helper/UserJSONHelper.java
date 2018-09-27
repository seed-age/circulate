package com.sunnsoft.sloa.helper;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.gteam.db.dao.PageList;
import org.gteam.db.dao.PageScroll;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.db.helper.hibernate.Each;
import org.gteam.db.helper.hibernate.HelperException;
import org.gteam.db.helper.json.EachEntity2Map;
import org.gteam.db.helper.json.EachJSON;
import org.gteam.db.helper.json.FilterValue;
import org.gteam.service.IService;
import org.gteam.util.EntityUtils;
import org.gteam.util.FastJSONUtils;
import org.gteam.util.JSONUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("unchecked")
public class UserJSONHelper {
	
	private UserHelper helper;
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
	
	public UserJSONHelper(UserHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(User.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(User.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(User.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(User.class);
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
			jsonHelper.excludeAll(User.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(User.class);
				break;
			case 2:
				jsonHelper.excludeChildren(User.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(User.class);
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
	public UserJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public UserJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public UserHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public UserJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public UserJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public UserJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public UserJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public UserJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public UserJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public UserJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<User> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<User>() {

			@Override
			public void each(User bean, List<User> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<User> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<User>() {

			@Override
			public void each(User bean, List<User> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<User> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<User>() {

			@Override
			public void each(User bean, List<User> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<User> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<User>() {

			@Override
			public void each(User bean, List<User> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<User> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<User>() {

			@Override
			public void each(User bean, List<User> beans) {
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
	public String uniqueJson(EachEntity2Map<User> eachEntity2Map){
		User bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<User> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<User>(){

			@Override
			public void each(User bean, List<User> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<User> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<User> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<User> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<User>(){

			@Override
			public void each(User bean, List<User> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<User> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<User>(){

			@Override
			public void each(User bean, List<User> beans) {
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
	public UserJSONHelper includeUserId(){
		this.excludeAll = true;
		this.includes.add("userId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userId (用户ID)字段
	 * @return
	 */
	public UserJSONHelper excludeUserId(){
		this.excludeAll = false;
		this.excludes.add("userId");
		return this;
	}
	/**
	 * 序列化json时候，包含 accountName (登录名)字段
	 * @return
	 */
	public UserJSONHelper includeAccountName(){
		this.excludeAll = true;
		this.includes.add("accountName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 accountName (登录名)字段
	 * @return
	 */
	public UserJSONHelper excludeAccountName(){
		this.excludeAll = false;
		this.excludes.add("accountName");
		return this;
	}
	/**
	 * 序列化json时候，包含 nickName (昵称)字段
	 * @return
	 */
	public UserJSONHelper includeNickName(){
		this.excludeAll = true;
		this.includes.add("nickName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 nickName (昵称)字段
	 * @return
	 */
	public UserJSONHelper excludeNickName(){
		this.excludeAll = false;
		this.excludes.add("nickName");
		return this;
	}
	/**
	 * 序列化json时候，包含 createTime (创建日期)字段
	 * @return
	 */
	public UserJSONHelper includeCreateTime(){
		this.excludeAll = true;
		this.includes.add("createTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 createTime (创建日期)字段
	 * @return
	 */
	public UserJSONHelper excludeCreateTime(){
		this.excludeAll = false;
		this.excludes.add("createTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 userPassword (密码)字段
	 * @return
	 */
	public UserJSONHelper includeUserPassword(){
		this.excludeAll = true;
		this.includes.add("userPassword");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userPassword (密码)字段
	 * @return
	 */
	public UserJSONHelper excludeUserPassword(){
		this.excludeAll = false;
		this.excludes.add("userPassword");
		return this;
	}
	/**
	 * 序列化json时候，包含 admin (是否管理员)字段
	 * @return
	 */
	public UserJSONHelper includeAdmin(){
		this.excludeAll = true;
		this.includes.add("admin");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 admin (是否管理员)字段
	 * @return
	 */
	public UserJSONHelper excludeAdmin(){
		this.excludeAll = false;
		this.excludes.add("admin");
		return this;
	}
	/**
	 * 序列化json时候，包含 enabled (是否启用)字段
	 * @return
	 */
	public UserJSONHelper includeEnabled(){
		this.excludeAll = true;
		this.includes.add("enabled");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 enabled (是否启用)字段
	 * @return
	 */
	public UserJSONHelper excludeEnabled(){
		this.excludeAll = false;
		this.excludes.add("enabled");
		return this;
	}
	/**
	 * 序列化json时候，包含 lastLogin (最后登录时间)字段
	 * @return
	 */
	public UserJSONHelper includeLastLogin(){
		this.excludeAll = true;
		this.includes.add("lastLogin");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 lastLogin (最后登录时间)字段
	 * @return
	 */
	public UserJSONHelper excludeLastLogin(){
		this.excludeAll = false;
		this.excludes.add("lastLogin");
		return this;
	}
	/**
	 * 序列化json时候，包含 priority (优先级)字段
	 * @return
	 */
	public UserJSONHelper includePriority(){
		this.excludeAll = true;
		this.includes.add("priority");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 priority (优先级)字段
	 * @return
	 */
	public UserJSONHelper excludePriority(){
		this.excludeAll = false;
		this.excludes.add("priority");
		return this;
	}
	/**
	 * 序列化json时候，包含 softDelete (是否软删除)字段
	 * @return
	 */
	public UserJSONHelper includeSoftDelete(){
		this.excludeAll = true;
		this.includes.add("softDelete");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 softDelete (是否软删除)字段
	 * @return
	 */
	public UserJSONHelper excludeSoftDelete(){
		this.excludeAll = false;
		this.excludes.add("softDelete");
		return this;
	}
	/**
	 * 序列化json时候，包含 accessToken (登录令牌)字段
	 * @return
	 */
	public UserJSONHelper includeAccessToken(){
		this.excludeAll = true;
		this.includes.add("accessToken");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 accessToken (登录令牌)字段
	 * @return
	 */
	public UserJSONHelper excludeAccessToken(){
		this.excludeAll = false;
		this.excludes.add("accessToken");
		return this;
	}
	/**
	 * 序列化json时候，包含 accessTokenExpire (令牌失效时间)字段
	 * @return
	 */
	public UserJSONHelper includeAccessTokenExpire(){
		this.excludeAll = true;
		this.includes.add("accessTokenExpire");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 accessTokenExpire (令牌失效时间)字段
	 * @return
	 */
	public UserJSONHelper excludeAccessTokenExpire(){
		this.excludeAll = false;
		this.excludes.add("accessTokenExpire");
		return this;
	}
	/**
	 * 序列化json时候，包含 userInfo ()字段
	 * @return
	 */
	public UserJSONHelper includeUserInfo(){
		this.excludeAll = true;
		this.includes.add("userInfo");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userInfo ()字段
	 * @return
	 */
	public UserJSONHelper excludeUserInfo(){
		this.excludeAll = false;
		this.excludes.add("userInfo");
		return this;
	}
	/**
	 * 序列化json时候，包含 roles ()字段
	 * @return
	 */
	public UserJSONHelper includeRoles(){
		this.excludeAll = true;
		this.includes.add("roles");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 roles ()字段
	 * @return
	 */
	public UserJSONHelper excludeRoles(){
		this.excludeAll = false;
		this.excludes.add("roles");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key userId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForUserId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "userId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key accountName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForAccountName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("accountName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "accountName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key nickName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForNickName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("nickName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "nickName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key createTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForCreateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("createTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "createTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userPassword 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForUserPassword(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userPassword", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "userPassword", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key admin 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForAdmin(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("admin", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "admin", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key enabled 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForEnabled(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("enabled", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "enabled", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key lastLogin 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForLastLogin(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("lastLogin", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "lastLogin", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key priority 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForPriority(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("priority", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "priority", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key softDelete 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForSoftDelete(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("softDelete", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "softDelete", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key accessToken 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForAccessToken(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("accessToken", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "accessToken", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key accessTokenExpire 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForAccessTokenExpire(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("accessTokenExpire", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "accessTokenExpire", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userInfo 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForUserInfo(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userInfo", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "userInfo", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key roles 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserJSONHelper replaceKeyForRoles(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("roles", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, User.class, "roles", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 userId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterUserId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Long.class, filterValue, "userId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 accountName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterAccountName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, String.class, filterValue, "accountName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 nickName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterNickName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, String.class, filterValue, "nickName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 createTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterCreateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Date.class, filterValue, "createTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userPassword 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterUserPassword(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, String.class, filterValue, "userPassword");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 admin 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterAdmin(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Boolean.class, filterValue, "admin");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 enabled 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterEnabled(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Boolean.class, filterValue, "enabled");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 lastLogin 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterLastLogin(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Date.class, filterValue, "lastLogin");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 priority 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterPriority(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Integer.class, filterValue, "priority");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 softDelete 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterSoftDelete(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Boolean.class, filterValue, "softDelete");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 accessToken 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterAccessToken(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, String.class, filterValue, "accessToken");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 accessTokenExpire 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterAccessTokenExpire(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Date.class, filterValue, "accessTokenExpire");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userInfo 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterUserInfo(FilterValue<UserInfo> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, UserInfo.class, filterValue, "userInfo");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 roles 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserJSONHelper filterRoles(FilterValue<Role> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, User.class, Role.class, filterValue, "roles");
		return this;
	}
	
}
