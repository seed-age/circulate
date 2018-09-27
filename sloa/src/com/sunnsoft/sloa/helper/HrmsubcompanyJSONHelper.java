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
public class HrmsubcompanyJSONHelper {
	
	private HrmsubcompanyHelper helper;
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
	
	public HrmsubcompanyJSONHelper(HrmsubcompanyHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(Hrmsubcompany.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(Hrmsubcompany.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(Hrmsubcompany.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(Hrmsubcompany.class);
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
			jsonHelper.excludeAll(Hrmsubcompany.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(Hrmsubcompany.class);
				break;
			case 2:
				jsonHelper.excludeChildren(Hrmsubcompany.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(Hrmsubcompany.class);
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
	public HrmsubcompanyJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public HrmsubcompanyJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public HrmsubcompanyHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public HrmsubcompanyJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public HrmsubcompanyJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public HrmsubcompanyJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public HrmsubcompanyJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public HrmsubcompanyJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public HrmsubcompanyJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public HrmsubcompanyJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<Hrmsubcompany> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<Hrmsubcompany>() {

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<Hrmsubcompany> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<Hrmsubcompany>() {

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<Hrmsubcompany> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<Hrmsubcompany>() {

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<Hrmsubcompany> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<Hrmsubcompany>() {

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<Hrmsubcompany> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<Hrmsubcompany>() {

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> beans) {
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
	public String uniqueJson(EachEntity2Map<Hrmsubcompany> eachEntity2Map){
		Hrmsubcompany bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<Hrmsubcompany> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<Hrmsubcompany>(){

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<Hrmsubcompany> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<Hrmsubcompany> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<Hrmsubcompany> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<Hrmsubcompany>(){

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<Hrmsubcompany> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<Hrmsubcompany>(){

			@Override
			public void each(Hrmsubcompany bean, List<Hrmsubcompany> beans) {
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
	 * 序列化json时候，包含 id (id)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeId(){
		this.excludeAll = true;
		this.includes.add("id");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 id (id)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeId(){
		this.excludeAll = false;
		this.excludes.add("id");
		return this;
	}
	/**
	 * 序列化json时候，包含 subcompanyname (分部简称)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeSubcompanyname(){
		this.excludeAll = true;
		this.includes.add("subcompanyname");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 subcompanyname (分部简称)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeSubcompanyname(){
		this.excludeAll = false;
		this.excludes.add("subcompanyname");
		return this;
	}
	/**
	 * 序列化json时候，包含 subcompanydesc (分部描述)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeSubcompanydesc(){
		this.excludeAll = true;
		this.includes.add("subcompanydesc");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 subcompanydesc (分部描述)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeSubcompanydesc(){
		this.excludeAll = false;
		this.excludes.add("subcompanydesc");
		return this;
	}
	/**
	 * 序列化json时候，包含 companyid (所属总部ID)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeCompanyid(){
		this.excludeAll = true;
		this.includes.add("companyid");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 companyid (所属总部ID)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeCompanyid(){
		this.excludeAll = false;
		this.excludes.add("companyid");
		return this;
	}
	/**
	 * 序列化json时候，包含 supsubcomid (上级分部Id)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeSupsubcomid(){
		this.excludeAll = true;
		this.includes.add("supsubcomid");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 supsubcomid (上级分部Id)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeSupsubcomid(){
		this.excludeAll = false;
		this.excludes.add("supsubcomid");
		return this;
	}
	/**
	 * 序列化json时候，包含 url (url)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeUrl(){
		this.excludeAll = true;
		this.includes.add("url");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 url (url)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeUrl(){
		this.excludeAll = false;
		this.excludes.add("url");
		return this;
	}
	/**
	 * 序列化json时候，包含 showorder (序列号)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeShoworder(){
		this.excludeAll = true;
		this.includes.add("showorder");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 showorder (序列号)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeShoworder(){
		this.excludeAll = false;
		this.excludes.add("showorder");
		return this;
	}
	/**
	 * 序列化json时候，包含 canceled (封存标识)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeCanceled(){
		this.excludeAll = true;
		this.includes.add("canceled");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 canceled (封存标识)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeCanceled(){
		this.excludeAll = false;
		this.excludes.add("canceled");
		return this;
	}
	/**
	 * 序列化json时候，包含 subcompanycode (分部编码)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeSubcompanycode(){
		this.excludeAll = true;
		this.includes.add("subcompanycode");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 subcompanycode (分部编码)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeSubcompanycode(){
		this.excludeAll = false;
		this.excludes.add("subcompanycode");
		return this;
	}
	/**
	 * 序列化json时候，包含 outkey (outkey)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeOutkey(){
		this.excludeAll = true;
		this.includes.add("outkey");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 outkey (outkey)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeOutkey(){
		this.excludeAll = false;
		this.excludes.add("outkey");
		return this;
	}
	/**
	 * 序列化json时候，包含 budgetatuomoveorder (budgetatuomoveorder)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeBudgetatuomoveorder(){
		this.excludeAll = true;
		this.includes.add("budgetatuomoveorder");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 budgetatuomoveorder (budgetatuomoveorder)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeBudgetatuomoveorder(){
		this.excludeAll = false;
		this.excludes.add("budgetatuomoveorder");
		return this;
	}
	/**
	 * 序列化json时候，包含 ecologyPinyinSearch (拼音)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeEcologyPinyinSearch(){
		this.excludeAll = true;
		this.includes.add("ecologyPinyinSearch");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ecologyPinyinSearch (拼音)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeEcologyPinyinSearch(){
		this.excludeAll = false;
		this.excludes.add("ecologyPinyinSearch");
		return this;
	}
	/**
	 * 序列化json时候，包含 limitusers (限制用户数)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeLimitusers(){
		this.excludeAll = true;
		this.includes.add("limitusers");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 limitusers (限制用户数)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeLimitusers(){
		this.excludeAll = false;
		this.excludes.add("limitusers");
		return this;
	}
	/**
	 * 序列化json时候，包含 tlevel (等级)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper includeTlevel(){
		this.excludeAll = true;
		this.includes.add("tlevel");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 tlevel (等级)字段
	 * @return
	 */
	public HrmsubcompanyJSONHelper excludeTlevel(){
		this.excludeAll = false;
		this.excludes.add("tlevel");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key id 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("id", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "id", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key subcompanyname 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForSubcompanyname(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("subcompanyname", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "subcompanyname", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key subcompanydesc 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForSubcompanydesc(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("subcompanydesc", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "subcompanydesc", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key companyid 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForCompanyid(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("companyid", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "companyid", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key supsubcomid 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForSupsubcomid(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("supsubcomid", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "supsubcomid", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key url 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForUrl(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("url", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "url", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key showorder 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForShoworder(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("showorder", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "showorder", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key canceled 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForCanceled(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("canceled", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "canceled", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key subcompanycode 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForSubcompanycode(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("subcompanycode", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "subcompanycode", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key outkey 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForOutkey(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("outkey", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "outkey", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key budgetatuomoveorder 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForBudgetatuomoveorder(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("budgetatuomoveorder", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "budgetatuomoveorder", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ecologyPinyinSearch 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForEcologyPinyinSearch(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ecologyPinyinSearch", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "ecologyPinyinSearch", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key limitusers 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForLimitusers(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("limitusers", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "limitusers", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key tlevel 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmsubcompanyJSONHelper replaceKeyForTlevel(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("tlevel", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmsubcompany.class, "tlevel", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 id 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterId(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, Integer.class, filterValue, "id");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 subcompanyname 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterSubcompanyname(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, String.class, filterValue, "subcompanyname");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 subcompanydesc 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterSubcompanydesc(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, String.class, filterValue, "subcompanydesc");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 companyid 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterCompanyid(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, Integer.class, filterValue, "companyid");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 supsubcomid 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterSupsubcomid(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, Integer.class, filterValue, "supsubcomid");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 url 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterUrl(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, String.class, filterValue, "url");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 showorder 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterShoworder(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, Integer.class, filterValue, "showorder");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 canceled 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterCanceled(FilterValue<Character> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, Character.class, filterValue, "canceled");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 subcompanycode 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterSubcompanycode(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, String.class, filterValue, "subcompanycode");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 outkey 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterOutkey(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, String.class, filterValue, "outkey");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 budgetatuomoveorder 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterBudgetatuomoveorder(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, Integer.class, filterValue, "budgetatuomoveorder");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ecologyPinyinSearch 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterEcologyPinyinSearch(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, String.class, filterValue, "ecologyPinyinSearch");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 limitusers 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterLimitusers(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, Integer.class, filterValue, "limitusers");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 tlevel 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmsubcompanyJSONHelper filterTlevel(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmsubcompany.class, Integer.class, filterValue, "tlevel");
		return this;
	}
	
}
