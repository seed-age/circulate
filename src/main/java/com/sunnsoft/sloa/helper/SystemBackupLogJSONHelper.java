package com.sunnsoft.sloa.helper;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.sunnsoft.sloa.db.vo.SystemBackupLog;
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
public class SystemBackupLogJSONHelper {
	
	private SystemBackupLogHelper helper;
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
	
	public SystemBackupLogJSONHelper(SystemBackupLogHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(SystemBackupLog.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(SystemBackupLog.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(SystemBackupLog.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(SystemBackupLog.class);
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
			jsonHelper.excludeAll(SystemBackupLog.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(SystemBackupLog.class);
				break;
			case 2:
				jsonHelper.excludeChildren(SystemBackupLog.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(SystemBackupLog.class);
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
	public SystemBackupLogJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public SystemBackupLogJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public SystemBackupLogHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public SystemBackupLogJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public SystemBackupLogJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public SystemBackupLogJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public SystemBackupLogJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public SystemBackupLogJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public SystemBackupLogJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public SystemBackupLogJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<SystemBackupLog> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<SystemBackupLog>() {

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<SystemBackupLog> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<SystemBackupLog>() {

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<SystemBackupLog> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<SystemBackupLog>() {

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<SystemBackupLog> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<SystemBackupLog>() {

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<SystemBackupLog> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<SystemBackupLog>() {

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> beans) {
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
	public String uniqueJson(EachEntity2Map<SystemBackupLog> eachEntity2Map){
		SystemBackupLog bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<SystemBackupLog> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<SystemBackupLog>(){

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<SystemBackupLog> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<SystemBackupLog> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<SystemBackupLog> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<SystemBackupLog>(){

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<SystemBackupLog> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<SystemBackupLog>(){

			@Override
			public void each(SystemBackupLog bean, List<SystemBackupLog> beans) {
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
	 * 序列化json时候，包含 logId (ID)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper includeLogId(){
		this.excludeAll = true;
		this.includes.add("logId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 logId (ID)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper excludeLogId(){
		this.excludeAll = false;
		this.excludes.add("logId");
		return this;
	}
	/**
	 * 序列化json时候，包含 operator (操作人)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper includeOperator(){
		this.excludeAll = true;
		this.includes.add("operator");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 operator (操作人)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper excludeOperator(){
		this.excludeAll = false;
		this.excludes.add("operator");
		return this;
	}
	/**
	 * 序列化json时候，包含 logTime (日志时间)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper includeLogTime(){
		this.excludeAll = true;
		this.includes.add("logTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 logTime (日志时间)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper excludeLogTime(){
		this.excludeAll = false;
		this.excludes.add("logTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 action (操作)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper includeAction(){
		this.excludeAll = true;
		this.includes.add("action");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 action (操作)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper excludeAction(){
		this.excludeAll = false;
		this.excludes.add("action");
		return this;
	}
	/**
	 * 序列化json时候，包含 ip (ip地址)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper includeIp(){
		this.excludeAll = true;
		this.includes.add("ip");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ip (ip地址)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper excludeIp(){
		this.excludeAll = false;
		this.excludes.add("ip");
		return this;
	}
	/**
	 * 序列化json时候，包含 identityInfo (身份信息)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper includeIdentityInfo(){
		this.excludeAll = true;
		this.includes.add("identityInfo");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 identityInfo (身份信息)字段
	 * @return
	 */
	public SystemBackupLogJSONHelper excludeIdentityInfo(){
		this.excludeAll = false;
		this.excludes.add("identityInfo");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key logId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public SystemBackupLogJSONHelper replaceKeyForLogId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("logId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, SystemBackupLog.class, "logId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key operator 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public SystemBackupLogJSONHelper replaceKeyForOperator(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("operator", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, SystemBackupLog.class, "operator", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key logTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public SystemBackupLogJSONHelper replaceKeyForLogTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("logTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, SystemBackupLog.class, "logTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key action 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public SystemBackupLogJSONHelper replaceKeyForAction(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("action", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, SystemBackupLog.class, "action", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ip 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public SystemBackupLogJSONHelper replaceKeyForIp(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ip", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, SystemBackupLog.class, "ip", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key identityInfo 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public SystemBackupLogJSONHelper replaceKeyForIdentityInfo(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("identityInfo", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, SystemBackupLog.class, "identityInfo", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 logId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public SystemBackupLogJSONHelper filterLogId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, SystemBackupLog.class, Long.class, filterValue, "logId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 operator 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public SystemBackupLogJSONHelper filterOperator(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, SystemBackupLog.class, String.class, filterValue, "operator");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 logTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public SystemBackupLogJSONHelper filterLogTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, SystemBackupLog.class, Date.class, filterValue, "logTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 action 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public SystemBackupLogJSONHelper filterAction(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, SystemBackupLog.class, String.class, filterValue, "action");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ip 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public SystemBackupLogJSONHelper filterIp(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, SystemBackupLog.class, String.class, filterValue, "ip");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 identityInfo 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public SystemBackupLogJSONHelper filterIdentityInfo(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, SystemBackupLog.class, String.class, filterValue, "identityInfo");
		return this;
	}
	
}
