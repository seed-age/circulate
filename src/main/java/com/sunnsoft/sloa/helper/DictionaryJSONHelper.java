package com.sunnsoft.sloa.helper;

import java.util.ArrayList;
import java.util.Date;
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
public class DictionaryJSONHelper {
	
	private DictionaryHelper helper;
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
	
	public DictionaryJSONHelper(DictionaryHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(Dictionary.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(Dictionary.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(Dictionary.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(Dictionary.class);
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
			jsonHelper.excludeAll(Dictionary.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(Dictionary.class);
				break;
			case 2:
				jsonHelper.excludeChildren(Dictionary.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(Dictionary.class);
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
	public DictionaryJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public DictionaryJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public DictionaryHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public DictionaryJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public DictionaryJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public DictionaryJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public DictionaryJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public DictionaryJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public DictionaryJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public DictionaryJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<Dictionary> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<Dictionary>() {

			@Override
			public void each(Dictionary bean, List<Dictionary> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<Dictionary> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<Dictionary>() {

			@Override
			public void each(Dictionary bean, List<Dictionary> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<Dictionary> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<Dictionary>() {

			@Override
			public void each(Dictionary bean, List<Dictionary> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<Dictionary> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<Dictionary>() {

			@Override
			public void each(Dictionary bean, List<Dictionary> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<Dictionary> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<Dictionary>() {

			@Override
			public void each(Dictionary bean, List<Dictionary> beans) {
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
	public String uniqueJson(EachEntity2Map<Dictionary> eachEntity2Map){
		Dictionary bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<Dictionary> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<Dictionary>(){

			@Override
			public void each(Dictionary bean, List<Dictionary> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<Dictionary> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<Dictionary> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<Dictionary> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<Dictionary>(){

			@Override
			public void each(Dictionary bean, List<Dictionary> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<Dictionary> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<Dictionary>(){

			@Override
			public void each(Dictionary bean, List<Dictionary> beans) {
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
	 * 序列化json时候，包含 dicId (ID)字段
	 * @return
	 */
	public DictionaryJSONHelper includeDicId(){
		this.excludeAll = true;
		this.includes.add("dicId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 dicId (ID)字段
	 * @return
	 */
	public DictionaryJSONHelper excludeDicId(){
		this.excludeAll = false;
		this.excludes.add("dicId");
		return this;
	}
	/**
	 * 序列化json时候，包含 keyName (键名)字段
	 * @return
	 */
	public DictionaryJSONHelper includeKeyName(){
		this.excludeAll = true;
		this.includes.add("keyName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 keyName (键名)字段
	 * @return
	 */
	public DictionaryJSONHelper excludeKeyName(){
		this.excludeAll = false;
		this.excludes.add("keyName");
		return this;
	}
	/**
	 * 序列化json时候，包含 keyValue (键值)字段
	 * @return
	 */
	public DictionaryJSONHelper includeKeyValue(){
		this.excludeAll = true;
		this.includes.add("keyValue");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 keyValue (键值)字段
	 * @return
	 */
	public DictionaryJSONHelper excludeKeyValue(){
		this.excludeAll = false;
		this.excludes.add("keyValue");
		return this;
	}
	/**
	 * 序列化json时候，包含 updateTime (更新日期)字段
	 * @return
	 */
	public DictionaryJSONHelper includeUpdateTime(){
		this.excludeAll = true;
		this.includes.add("updateTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 updateTime (更新日期)字段
	 * @return
	 */
	public DictionaryJSONHelper excludeUpdateTime(){
		this.excludeAll = false;
		this.excludes.add("updateTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 createTime (创建日期)字段
	 * @return
	 */
	public DictionaryJSONHelper includeCreateTime(){
		this.excludeAll = true;
		this.includes.add("createTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 createTime (创建日期)字段
	 * @return
	 */
	public DictionaryJSONHelper excludeCreateTime(){
		this.excludeAll = false;
		this.excludes.add("createTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 status (状态)字段
	 * @return
	 */
	public DictionaryJSONHelper includeStatus(){
		this.excludeAll = true;
		this.includes.add("status");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 status (状态)字段
	 * @return
	 */
	public DictionaryJSONHelper excludeStatus(){
		this.excludeAll = false;
		this.excludes.add("status");
		return this;
	}
	/**
	 * 序列化json时候，包含 description (作用描述)字段
	 * @return
	 */
	public DictionaryJSONHelper includeDescription(){
		this.excludeAll = true;
		this.includes.add("description");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 description (作用描述)字段
	 * @return
	 */
	public DictionaryJSONHelper excludeDescription(){
		this.excludeAll = false;
		this.excludes.add("description");
		return this;
	}
	/**
	 * 序列化json时候，包含 type (类型)字段
	 * @return
	 */
	public DictionaryJSONHelper includeType(){
		this.excludeAll = true;
		this.includes.add("type");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 type (类型)字段
	 * @return
	 */
	public DictionaryJSONHelper excludeType(){
		this.excludeAll = false;
		this.excludes.add("type");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key dicId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DictionaryJSONHelper replaceKeyForDicId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("dicId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Dictionary.class, "dicId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key keyName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DictionaryJSONHelper replaceKeyForKeyName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("keyName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Dictionary.class, "keyName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key keyValue 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DictionaryJSONHelper replaceKeyForKeyValue(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("keyValue", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Dictionary.class, "keyValue", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key updateTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DictionaryJSONHelper replaceKeyForUpdateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("updateTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Dictionary.class, "updateTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key createTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DictionaryJSONHelper replaceKeyForCreateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("createTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Dictionary.class, "createTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key status 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DictionaryJSONHelper replaceKeyForStatus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("status", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Dictionary.class, "status", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key description 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DictionaryJSONHelper replaceKeyForDescription(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("description", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Dictionary.class, "description", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key type 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DictionaryJSONHelper replaceKeyForType(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("type", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Dictionary.class, "type", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 dicId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DictionaryJSONHelper filterDicId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Dictionary.class, Long.class, filterValue, "dicId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 keyName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DictionaryJSONHelper filterKeyName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Dictionary.class, String.class, filterValue, "keyName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 keyValue 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DictionaryJSONHelper filterKeyValue(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Dictionary.class, String.class, filterValue, "keyValue");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 updateTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DictionaryJSONHelper filterUpdateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Dictionary.class, Date.class, filterValue, "updateTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 createTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DictionaryJSONHelper filterCreateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Dictionary.class, Date.class, filterValue, "createTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 status 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DictionaryJSONHelper filterStatus(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Dictionary.class, Boolean.class, filterValue, "status");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 description 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DictionaryJSONHelper filterDescription(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Dictionary.class, String.class, filterValue, "description");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 type 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DictionaryJSONHelper filterType(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Dictionary.class, String.class, filterValue, "type");
		return this;
	}
	
}
