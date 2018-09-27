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
public class DiscussJSONHelper {
	
	private DiscussHelper helper;
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
	
	public DiscussJSONHelper(DiscussHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(Discuss.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(Discuss.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(Discuss.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(Discuss.class);
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
			jsonHelper.excludeAll(Discuss.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(Discuss.class);
				break;
			case 2:
				jsonHelper.excludeChildren(Discuss.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(Discuss.class);
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
	public DiscussJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public DiscussJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public DiscussHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public DiscussJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public DiscussJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public DiscussJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public DiscussJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public DiscussJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public DiscussJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public DiscussJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<Discuss> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<Discuss>() {

			@Override
			public void each(Discuss bean, List<Discuss> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<Discuss> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<Discuss>() {

			@Override
			public void each(Discuss bean, List<Discuss> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<Discuss> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<Discuss>() {

			@Override
			public void each(Discuss bean, List<Discuss> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<Discuss> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<Discuss>() {

			@Override
			public void each(Discuss bean, List<Discuss> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<Discuss> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<Discuss>() {

			@Override
			public void each(Discuss bean, List<Discuss> beans) {
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
	public String uniqueJson(EachEntity2Map<Discuss> eachEntity2Map){
		Discuss bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<Discuss> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<Discuss>(){

			@Override
			public void each(Discuss bean, List<Discuss> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<Discuss> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<Discuss> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<Discuss> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<Discuss>(){

			@Override
			public void each(Discuss bean, List<Discuss> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<Discuss> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<Discuss>(){

			@Override
			public void each(Discuss bean, List<Discuss> beans) {
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
	 * 序列化json时候，包含 discussId (讨论id)字段
	 * @return
	 */
	public DiscussJSONHelper includeDiscussId(){
		this.excludeAll = true;
		this.includes.add("discussId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 discussId (讨论id)字段
	 * @return
	 */
	public DiscussJSONHelper excludeDiscussId(){
		this.excludeAll = false;
		this.excludes.add("discussId");
		return this;
	}
	/**
	 * 序列化json时候，包含 mail ()字段
	 * @return
	 */
	public DiscussJSONHelper includeMail(){
		this.excludeAll = true;
		this.includes.add("mail");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 mail ()字段
	 * @return
	 */
	public DiscussJSONHelper excludeMail(){
		this.excludeAll = false;
		this.excludes.add("mail");
		return this;
	}
	/**
	 * 序列化json时候，包含 createTime (发布的时间)字段
	 * @return
	 */
	public DiscussJSONHelper includeCreateTime(){
		this.excludeAll = true;
		this.includes.add("createTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 createTime (发布的时间)字段
	 * @return
	 */
	public DiscussJSONHelper excludeCreateTime(){
		this.excludeAll = false;
		this.excludes.add("createTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 discussContent (讨论的内容)字段
	 * @return
	 */
	public DiscussJSONHelper includeDiscussContent(){
		this.excludeAll = true;
		this.includes.add("discussContent");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 discussContent (讨论的内容)字段
	 * @return
	 */
	public DiscussJSONHelper excludeDiscussContent(){
		this.excludeAll = false;
		this.excludes.add("discussContent");
		return this;
	}
	/**
	 * 序列化json时候，包含 userId (讨论人id)字段
	 * @return
	 */
	public DiscussJSONHelper includeUserId(){
		this.excludeAll = true;
		this.includes.add("userId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userId (讨论人id)字段
	 * @return
	 */
	public DiscussJSONHelper excludeUserId(){
		this.excludeAll = false;
		this.excludes.add("userId");
		return this;
	}
	/**
	 * 序列化json时候，包含 workCode (讨论人工作编号)字段
	 * @return
	 */
	public DiscussJSONHelper includeWorkCode(){
		this.excludeAll = true;
		this.includes.add("workCode");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 workCode (讨论人工作编号)字段
	 * @return
	 */
	public DiscussJSONHelper excludeWorkCode(){
		this.excludeAll = false;
		this.excludes.add("workCode");
		return this;
	}
	/**
	 * 序列化json时候，包含 lastName (讨论人的姓名)字段
	 * @return
	 */
	public DiscussJSONHelper includeLastName(){
		this.excludeAll = true;
		this.includes.add("lastName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 lastName (讨论人的姓名)字段
	 * @return
	 */
	public DiscussJSONHelper excludeLastName(){
		this.excludeAll = false;
		this.excludes.add("lastName");
		return this;
	}
	/**
	 * 序列化json时候，包含 loginId (讨论人登录名)字段
	 * @return
	 */
	public DiscussJSONHelper includeLoginId(){
		this.excludeAll = true;
		this.includes.add("loginId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 loginId (讨论人登录名)字段
	 * @return
	 */
	public DiscussJSONHelper excludeLoginId(){
		this.excludeAll = false;
		this.excludes.add("loginId");
		return this;
	}
	/**
	 * 序列化json时候，包含 headPortraitUrl (讨论人的头像url)字段
	 * @return
	 */
	public DiscussJSONHelper includeHeadPortraitUrl(){
		this.excludeAll = true;
		this.includes.add("headPortraitUrl");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 headPortraitUrl (讨论人的头像url)字段
	 * @return
	 */
	public DiscussJSONHelper excludeHeadPortraitUrl(){
		this.excludeAll = false;
		this.excludes.add("headPortraitUrl");
		return this;
	}
	/**
	 * 序列化json时候，包含 differentiate (区别)字段
	 * @return
	 */
	public DiscussJSONHelper includeDifferentiate(){
		this.excludeAll = true;
		this.includes.add("differentiate");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 differentiate (区别)字段
	 * @return
	 */
	public DiscussJSONHelper excludeDifferentiate(){
		this.excludeAll = false;
		this.excludes.add("differentiate");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key discussId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForDiscussId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("discussId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "discussId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key mail 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForMail(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("mail", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "mail", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key createTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForCreateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("createTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "createTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key discussContent 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForDiscussContent(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("discussContent", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "discussContent", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForUserId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "userId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key workCode 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForWorkCode(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("workCode", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "workCode", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key lastName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForLastName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("lastName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "lastName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key loginId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForLoginId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("loginId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "loginId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key headPortraitUrl 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForHeadPortraitUrl(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("headPortraitUrl", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "headPortraitUrl", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key differentiate 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public DiscussJSONHelper replaceKeyForDifferentiate(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("differentiate", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Discuss.class, "differentiate", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 discussId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterDiscussId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, Long.class, filterValue, "discussId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 mail 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterMail(FilterValue<Mail> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, Mail.class, filterValue, "mail");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 createTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterCreateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, Date.class, filterValue, "createTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 discussContent 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterDiscussContent(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, String.class, filterValue, "discussContent");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterUserId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, Long.class, filterValue, "userId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 workCode 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterWorkCode(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, String.class, filterValue, "workCode");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 lastName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterLastName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, String.class, filterValue, "lastName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 loginId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterLoginId(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, String.class, filterValue, "loginId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 headPortraitUrl 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterHeadPortraitUrl(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, String.class, filterValue, "headPortraitUrl");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 differentiate 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public DiscussJSONHelper filterDifferentiate(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Discuss.class, String.class, filterValue, "differentiate");
		return this;
	}
	
}
