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
public class ReceiveJSONHelper {
	
	private ReceiveHelper helper;
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
	
	public ReceiveJSONHelper(ReceiveHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(Receive.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(Receive.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(Receive.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(Receive.class);
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
			jsonHelper.excludeAll(Receive.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(Receive.class);
				break;
			case 2:
				jsonHelper.excludeChildren(Receive.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(Receive.class);
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
	public ReceiveJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public ReceiveJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public ReceiveHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public ReceiveJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public ReceiveJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public ReceiveJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public ReceiveJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public ReceiveJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public ReceiveJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public ReceiveJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<Receive> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<Receive>() {

			@Override
			public void each(Receive bean, List<Receive> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<Receive> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<Receive>() {

			@Override
			public void each(Receive bean, List<Receive> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<Receive> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<Receive>() {

			@Override
			public void each(Receive bean, List<Receive> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<Receive> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<Receive>() {

			@Override
			public void each(Receive bean, List<Receive> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<Receive> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<Receive>() {

			@Override
			public void each(Receive bean, List<Receive> beans) {
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
	public String uniqueJson(EachEntity2Map<Receive> eachEntity2Map){
		Receive bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<Receive> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<Receive>(){

			@Override
			public void each(Receive bean, List<Receive> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<Receive> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<Receive> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<Receive> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<Receive>(){

			@Override
			public void each(Receive bean, List<Receive> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<Receive> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<Receive>(){

			@Override
			public void each(Receive bean, List<Receive> beans) {
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
	 * 序列化json时候，包含 receiveId (收件id)字段
	 * @return
	 */
	public ReceiveJSONHelper includeReceiveId(){
		this.excludeAll = true;
		this.includes.add("receiveId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 receiveId (收件id)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeReceiveId(){
		this.excludeAll = false;
		this.excludes.add("receiveId");
		return this;
	}
	/**
	 * 序列化json时候，包含 mail ()字段
	 * @return
	 */
	public ReceiveJSONHelper includeMail(){
		this.excludeAll = true;
		this.includes.add("mail");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 mail ()字段
	 * @return
	 */
	public ReceiveJSONHelper excludeMail(){
		this.excludeAll = false;
		this.excludes.add("mail");
		return this;
	}
	/**
	 * 序列化json时候，包含 userId (收件人id)字段
	 * @return
	 */
	public ReceiveJSONHelper includeUserId(){
		this.excludeAll = true;
		this.includes.add("userId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userId (收件人id)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeUserId(){
		this.excludeAll = false;
		this.excludes.add("userId");
		return this;
	}
	/**
	 * 序列化json时候，包含 workCode (收件人工作编号)字段
	 * @return
	 */
	public ReceiveJSONHelper includeWorkCode(){
		this.excludeAll = true;
		this.includes.add("workCode");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 workCode (收件人工作编号)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeWorkCode(){
		this.excludeAll = false;
		this.excludes.add("workCode");
		return this;
	}
	/**
	 * 序列化json时候，包含 lastName (收件人姓名)字段
	 * @return
	 */
	public ReceiveJSONHelper includeLastName(){
		this.excludeAll = true;
		this.includes.add("lastName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 lastName (收件人姓名)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeLastName(){
		this.excludeAll = false;
		this.excludes.add("lastName");
		return this;
	}
	/**
	 * 序列化json时候，包含 loginId (收件人登录名)字段
	 * @return
	 */
	public ReceiveJSONHelper includeLoginId(){
		this.excludeAll = true;
		this.includes.add("loginId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 loginId (收件人登录名)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeLoginId(){
		this.excludeAll = false;
		this.excludes.add("loginId");
		return this;
	}
	/**
	 * 序列化json时候，包含 subcompanyName (收件人的分部全称)字段
	 * @return
	 */
	public ReceiveJSONHelper includeSubcompanyName(){
		this.excludeAll = true;
		this.includes.add("subcompanyName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 subcompanyName (收件人的分部全称)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeSubcompanyName(){
		this.excludeAll = false;
		this.excludes.add("subcompanyName");
		return this;
	}
	/**
	 * 序列化json时候，包含 departmentName (收件人的部门全称)字段
	 * @return
	 */
	public ReceiveJSONHelper includeDepartmentName(){
		this.excludeAll = true;
		this.includes.add("departmentName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 departmentName (收件人的部门全称)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeDepartmentName(){
		this.excludeAll = false;
		this.excludes.add("departmentName");
		return this;
	}
	/**
	 * 序列化json时候，包含 receiveTime (接收传阅的时间)字段
	 * @return
	 */
	public ReceiveJSONHelper includeReceiveTime(){
		this.excludeAll = true;
		this.includes.add("receiveTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 receiveTime (接收传阅的时间)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeReceiveTime(){
		this.excludeAll = false;
		this.excludes.add("receiveTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 joinTime (加入时间)字段
	 * @return
	 */
	public ReceiveJSONHelper includeJoinTime(){
		this.excludeAll = true;
		this.includes.add("joinTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 joinTime (加入时间)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeJoinTime(){
		this.excludeAll = false;
		this.excludes.add("joinTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 receiveStatus (状态)字段
	 * @return
	 */
	public ReceiveJSONHelper includeReceiveStatus(){
		this.excludeAll = true;
		this.includes.add("receiveStatus");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 receiveStatus (状态)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeReceiveStatus(){
		this.excludeAll = false;
		this.excludes.add("receiveStatus");
		return this;
	}
	/**
	 * 序列化json时候，包含 mailState (传阅筛选状态)字段
	 * @return
	 */
	public ReceiveJSONHelper includeMailState(){
		this.excludeAll = true;
		this.includes.add("mailState");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 mailState (传阅筛选状态)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeMailState(){
		this.excludeAll = false;
		this.excludes.add("mailState");
		return this;
	}
	/**
	 * 序列化json时候，包含 stepStatus (传阅流程状态)字段
	 * @return
	 */
	public ReceiveJSONHelper includeStepStatus(){
		this.excludeAll = true;
		this.includes.add("stepStatus");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 stepStatus (传阅流程状态)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeStepStatus(){
		this.excludeAll = false;
		this.excludes.add("stepStatus");
		return this;
	}
	/**
	 * 序列化json时候，包含 openTime (开封时间)字段
	 * @return
	 */
	public ReceiveJSONHelper includeOpenTime(){
		this.excludeAll = true;
		this.includes.add("openTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 openTime (开封时间)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeOpenTime(){
		this.excludeAll = false;
		this.excludes.add("openTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifConfirm (是否确认)字段
	 * @return
	 */
	public ReceiveJSONHelper includeIfConfirm(){
		this.excludeAll = true;
		this.includes.add("ifConfirm");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifConfirm (是否确认)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeIfConfirm(){
		this.excludeAll = false;
		this.excludes.add("ifConfirm");
		return this;
	}
	/**
	 * 序列化json时候，包含 affirmTime (确认时间)字段
	 * @return
	 */
	public ReceiveJSONHelper includeAffirmTime(){
		this.excludeAll = true;
		this.includes.add("affirmTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 affirmTime (确认时间)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeAffirmTime(){
		this.excludeAll = false;
		this.excludes.add("affirmTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 remark (确认信息备注)字段
	 * @return
	 */
	public ReceiveJSONHelper includeRemark(){
		this.excludeAll = true;
		this.includes.add("remark");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 remark (确认信息备注)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeRemark(){
		this.excludeAll = false;
		this.excludes.add("remark");
		return this;
	}
	/**
	 * 序列化json时候，包含 confirmRecord (确认/标识)字段
	 * @return
	 */
	public ReceiveJSONHelper includeConfirmRecord(){
		this.excludeAll = true;
		this.includes.add("confirmRecord");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 confirmRecord (确认/标识)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeConfirmRecord(){
		this.excludeAll = false;
		this.excludes.add("confirmRecord");
		return this;
	}
	/**
	 * 序列化json时候，包含 serialNum (序号)字段
	 * @return
	 */
	public ReceiveJSONHelper includeSerialNum(){
		this.excludeAll = true;
		this.includes.add("serialNum");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 serialNum (序号)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeSerialNum(){
		this.excludeAll = false;
		this.excludes.add("serialNum");
		return this;
	}
	/**
	 * 序列化json时候，包含 afreshConfim (是否重新确认)字段
	 * @return
	 */
	public ReceiveJSONHelper includeAfreshConfim(){
		this.excludeAll = true;
		this.includes.add("afreshConfim");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 afreshConfim (是否重新确认)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeAfreshConfim(){
		this.excludeAll = false;
		this.excludes.add("afreshConfim");
		return this;
	}
	/**
	 * 序列化json时候，包含 acRecord ((重新)确认/标识)字段
	 * @return
	 */
	public ReceiveJSONHelper includeAcRecord(){
		this.excludeAll = true;
		this.includes.add("acRecord");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 acRecord ((重新)确认/标识)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeAcRecord(){
		this.excludeAll = false;
		this.excludes.add("acRecord");
		return this;
	}
	/**
	 * 序列化json时候，包含 afreshRemark ((重新)确认信息备注)字段
	 * @return
	 */
	public ReceiveJSONHelper includeAfreshRemark(){
		this.excludeAll = true;
		this.includes.add("afreshRemark");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 afreshRemark ((重新)确认信息备注)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeAfreshRemark(){
		this.excludeAll = false;
		this.excludes.add("afreshRemark");
		return this;
	}
	/**
	 * 序列化json时候，包含 mhTime ((重新)确认时间)字段
	 * @return
	 */
	public ReceiveJSONHelper includeMhTime(){
		this.excludeAll = true;
		this.includes.add("mhTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 mhTime ((重新)确认时间)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeMhTime(){
		this.excludeAll = false;
		this.excludes.add("mhTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 receiveAttention (是否关注)字段
	 * @return
	 */
	public ReceiveJSONHelper includeReceiveAttention(){
		this.excludeAll = true;
		this.includes.add("receiveAttention");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 receiveAttention (是否关注)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeReceiveAttention(){
		this.excludeAll = false;
		this.excludes.add("receiveAttention");
		return this;
	}
	/**
	 * 序列化json时候，包含 reDifferentiate (区别)字段
	 * @return
	 */
	public ReceiveJSONHelper includeReDifferentiate(){
		this.excludeAll = true;
		this.includes.add("reDifferentiate");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 reDifferentiate (区别)字段
	 * @return
	 */
	public ReceiveJSONHelper excludeReDifferentiate(){
		this.excludeAll = false;
		this.excludes.add("reDifferentiate");
		return this;
	}
	/**
	 * 序列化json时候，包含 updateRecords ()字段
	 * @return
	 */
	public ReceiveJSONHelper includeUpdateRecords(){
		this.excludeAll = true;
		this.includes.add("updateRecords");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 updateRecords ()字段
	 * @return
	 */
	public ReceiveJSONHelper excludeUpdateRecords(){
		this.excludeAll = false;
		this.excludes.add("updateRecords");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key receiveId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForReceiveId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("receiveId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "receiveId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key mail 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForMail(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("mail", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "mail", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForUserId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "userId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key workCode 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForWorkCode(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("workCode", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "workCode", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key lastName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForLastName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("lastName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "lastName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key loginId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForLoginId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("loginId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "loginId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key subcompanyName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForSubcompanyName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("subcompanyName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "subcompanyName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key departmentName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForDepartmentName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("departmentName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "departmentName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key receiveTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForReceiveTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("receiveTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "receiveTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key joinTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForJoinTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("joinTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "joinTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key receiveStatus 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForReceiveStatus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("receiveStatus", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "receiveStatus", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key mailState 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForMailState(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("mailState", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "mailState", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key stepStatus 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForStepStatus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("stepStatus", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "stepStatus", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key openTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForOpenTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("openTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "openTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifConfirm 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForIfConfirm(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifConfirm", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "ifConfirm", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key affirmTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForAffirmTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("affirmTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "affirmTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key remark 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForRemark(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("remark", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "remark", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key confirmRecord 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForConfirmRecord(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("confirmRecord", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "confirmRecord", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key serialNum 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForSerialNum(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("serialNum", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "serialNum", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key afreshConfim 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForAfreshConfim(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("afreshConfim", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "afreshConfim", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key acRecord 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForAcRecord(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("acRecord", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "acRecord", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key afreshRemark 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForAfreshRemark(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("afreshRemark", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "afreshRemark", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key mhTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForMhTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("mhTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "mhTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key receiveAttention 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForReceiveAttention(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("receiveAttention", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "receiveAttention", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key reDifferentiate 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForReDifferentiate(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("reDifferentiate", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "reDifferentiate", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key updateRecords 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public ReceiveJSONHelper replaceKeyForUpdateRecords(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("updateRecords", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Receive.class, "updateRecords", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 receiveId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterReceiveId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Long.class, filterValue, "receiveId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 mail 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterMail(FilterValue<Mail> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Mail.class, filterValue, "mail");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterUserId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Long.class, filterValue, "userId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 workCode 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterWorkCode(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "workCode");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 lastName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterLastName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "lastName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 loginId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterLoginId(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "loginId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 subcompanyName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterSubcompanyName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "subcompanyName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 departmentName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterDepartmentName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "departmentName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 receiveTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterReceiveTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Date.class, filterValue, "receiveTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 joinTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterJoinTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Date.class, filterValue, "joinTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 receiveStatus 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterReceiveStatus(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Integer.class, filterValue, "receiveStatus");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 mailState 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterMailState(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Integer.class, filterValue, "mailState");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 stepStatus 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterStepStatus(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Integer.class, filterValue, "stepStatus");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 openTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterOpenTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Date.class, filterValue, "openTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifConfirm 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterIfConfirm(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Boolean.class, filterValue, "ifConfirm");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 affirmTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterAffirmTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Date.class, filterValue, "affirmTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 remark 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterRemark(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "remark");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 confirmRecord 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterConfirmRecord(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "confirmRecord");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 serialNum 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterSerialNum(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Integer.class, filterValue, "serialNum");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 afreshConfim 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterAfreshConfim(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Boolean.class, filterValue, "afreshConfim");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 acRecord 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterAcRecord(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "acRecord");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 afreshRemark 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterAfreshRemark(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, String.class, filterValue, "afreshRemark");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 mhTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterMhTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Date.class, filterValue, "mhTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 receiveAttention 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterReceiveAttention(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Boolean.class, filterValue, "receiveAttention");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 reDifferentiate 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterReDifferentiate(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, Long.class, filterValue, "reDifferentiate");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 updateRecords 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public ReceiveJSONHelper filterUpdateRecords(FilterValue<UpdateRecord> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Receive.class, UpdateRecord.class, filterValue, "updateRecords");
		return this;
	}
	
}
