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
public class UserMssageJSONHelper {
	
	private UserMssageHelper helper;
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
	
	public UserMssageJSONHelper(UserMssageHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(UserMssage.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(UserMssage.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(UserMssage.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(UserMssage.class);
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
			jsonHelper.excludeAll(UserMssage.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(UserMssage.class);
				break;
			case 2:
				jsonHelper.excludeChildren(UserMssage.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(UserMssage.class);
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
	public UserMssageJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public UserMssageJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public UserMssageHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public UserMssageJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public UserMssageJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public UserMssageJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public UserMssageJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public UserMssageJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public UserMssageJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public UserMssageJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<UserMssage> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<UserMssage>() {

			@Override
			public void each(UserMssage bean, List<UserMssage> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<UserMssage> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<UserMssage>() {

			@Override
			public void each(UserMssage bean, List<UserMssage> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<UserMssage> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<UserMssage>() {

			@Override
			public void each(UserMssage bean, List<UserMssage> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<UserMssage> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<UserMssage>() {

			@Override
			public void each(UserMssage bean, List<UserMssage> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<UserMssage> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<UserMssage>() {

			@Override
			public void each(UserMssage bean, List<UserMssage> beans) {
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
	public String uniqueJson(EachEntity2Map<UserMssage> eachEntity2Map){
		UserMssage bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<UserMssage> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<UserMssage>(){

			@Override
			public void each(UserMssage bean, List<UserMssage> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<UserMssage> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<UserMssage> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<UserMssage> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<UserMssage>(){

			@Override
			public void each(UserMssage bean, List<UserMssage> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<UserMssage> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<UserMssage>(){

			@Override
			public void each(UserMssage bean, List<UserMssage> beans) {
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
	 * 序列化json时候，包含 userMssageId (用户信息id)字段
	 * @return
	 */
	public UserMssageJSONHelper includeUserMssageId(){
		this.excludeAll = true;
		this.includes.add("userMssageId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userMssageId (用户信息id)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeUserMssageId(){
		this.excludeAll = false;
		this.excludes.add("userMssageId");
		return this;
	}
	/**
	 * 序列化json时候，包含 userId (用户ID)字段
	 * @return
	 */
	public UserMssageJSONHelper includeUserId(){
		this.excludeAll = true;
		this.includes.add("userId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userId (用户ID)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeUserId(){
		this.excludeAll = false;
		this.excludes.add("userId");
		return this;
	}
	/**
	 * 序列化json时候，包含 workCode (用户编号)字段
	 * @return
	 */
	public UserMssageJSONHelper includeWorkCode(){
		this.excludeAll = true;
		this.includes.add("workCode");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 workCode (用户编号)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeWorkCode(){
		this.excludeAll = false;
		this.excludes.add("workCode");
		return this;
	}
	/**
	 * 序列化json时候，包含 lastName (用户姓名)字段
	 * @return
	 */
	public UserMssageJSONHelper includeLastName(){
		this.excludeAll = true;
		this.includes.add("lastName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 lastName (用户姓名)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeLastName(){
		this.excludeAll = false;
		this.excludes.add("lastName");
		return this;
	}
	/**
	 * 序列化json时候，包含 loginId (用户登录名)字段
	 * @return
	 */
	public UserMssageJSONHelper includeLoginId(){
		this.excludeAll = true;
		this.includes.add("loginId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 loginId (用户登录名)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeLoginId(){
		this.excludeAll = false;
		this.excludes.add("loginId");
		return this;
	}
	/**
	 * 序列化json时候，包含 deptFullname (部门全称)字段
	 * @return
	 */
	public UserMssageJSONHelper includeDeptFullname(){
		this.excludeAll = true;
		this.includes.add("deptFullname");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 deptFullname (部门全称)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeDeptFullname(){
		this.excludeAll = false;
		this.excludes.add("deptFullname");
		return this;
	}
	/**
	 * 序列化json时候，包含 fullName (分部全称)字段
	 * @return
	 */
	public UserMssageJSONHelper includeFullName(){
		this.excludeAll = true;
		this.includes.add("fullName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 fullName (分部全称)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeFullName(){
		this.excludeAll = false;
		this.excludes.add("fullName");
		return this;
	}
	/**
	 * 序列化json时候，包含 departmentId (用户部门ID)字段
	 * @return
	 */
	public UserMssageJSONHelper includeDepartmentId(){
		this.excludeAll = true;
		this.includes.add("departmentId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 departmentId (用户部门ID)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeDepartmentId(){
		this.excludeAll = false;
		this.excludes.add("departmentId");
		return this;
	}
	/**
	 * 序列化json时候，包含 subcompanyId1 (用户分部ID)字段
	 * @return
	 */
	public UserMssageJSONHelper includeSubcompanyId1(){
		this.excludeAll = true;
		this.includes.add("subcompanyId1");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 subcompanyId1 (用户分部ID)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeSubcompanyId1(){
		this.excludeAll = false;
		this.excludes.add("subcompanyId1");
		return this;
	}
	/**
	 * 序列化json时候，包含 status (用户状态)字段
	 * @return
	 */
	public UserMssageJSONHelper includeStatus(){
		this.excludeAll = true;
		this.includes.add("status");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 status (用户状态)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeStatus(){
		this.excludeAll = false;
		this.excludes.add("status");
		return this;
	}
	/**
	 * 序列化json时候，包含 dsporder (显示顺序)字段
	 * @return
	 */
	public UserMssageJSONHelper includeDsporder(){
		this.excludeAll = true;
		this.includes.add("dsporder");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 dsporder (显示顺序)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeDsporder(){
		this.excludeAll = false;
		this.excludes.add("dsporder");
		return this;
	}
	/**
	 * 序列化json时候，包含 boxSession (网盘session)字段
	 * @return
	 */
	public UserMssageJSONHelper includeBoxSession(){
		this.excludeAll = true;
		this.includes.add("boxSession");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 boxSession (网盘session)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeBoxSession(){
		this.excludeAll = false;
		this.excludes.add("boxSession");
		return this;
	}
	/**
	 * 序列化json时候，包含 createTime (创建时间)字段
	 * @return
	 */
	public UserMssageJSONHelper includeCreateTime(){
		this.excludeAll = true;
		this.includes.add("createTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 createTime (创建时间)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeCreateTime(){
		this.excludeAll = false;
		this.excludes.add("createTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 updateTime (更新时间)字段
	 * @return
	 */
	public UserMssageJSONHelper includeUpdateTime(){
		this.excludeAll = true;
		this.includes.add("updateTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 updateTime (更新时间)字段
	 * @return
	 */
	public UserMssageJSONHelper excludeUpdateTime(){
		this.excludeAll = false;
		this.excludes.add("updateTime");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key userMssageId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForUserMssageId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userMssageId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "userMssageId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForUserId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "userId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key workCode 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForWorkCode(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("workCode", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "workCode", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key lastName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForLastName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("lastName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "lastName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key loginId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForLoginId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("loginId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "loginId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key deptFullname 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForDeptFullname(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("deptFullname", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "deptFullname", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key fullName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForFullName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("fullName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "fullName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key departmentId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForDepartmentId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("departmentId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "departmentId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key subcompanyId1 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForSubcompanyId1(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("subcompanyId1", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "subcompanyId1", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key status 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForStatus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("status", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "status", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key dsporder 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForDsporder(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("dsporder", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "dsporder", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key boxSession 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForBoxSession(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("boxSession", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "boxSession", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key createTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForCreateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("createTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "createTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key updateTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public UserMssageJSONHelper replaceKeyForUpdateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("updateTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, UserMssage.class, "updateTime", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 userMssageId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterUserMssageId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, Long.class, filterValue, "userMssageId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterUserId(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, Integer.class, filterValue, "userId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 workCode 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterWorkCode(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "workCode");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 lastName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterLastName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "lastName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 loginId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterLoginId(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "loginId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 deptFullname 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterDeptFullname(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "deptFullname");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 fullName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterFullName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "fullName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 departmentId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterDepartmentId(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "departmentId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 subcompanyId1 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterSubcompanyId1(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "subcompanyId1");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 status 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterStatus(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "status");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 dsporder 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterDsporder(FilterValue<Float> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, Float.class, filterValue, "dsporder");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 boxSession 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterBoxSession(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, String.class, filterValue, "boxSession");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 createTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterCreateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, Date.class, filterValue, "createTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 updateTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public UserMssageJSONHelper filterUpdateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, UserMssage.class, Date.class, filterValue, "updateTime");
		return this;
	}
	
}
