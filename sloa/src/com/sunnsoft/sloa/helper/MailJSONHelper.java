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
public class MailJSONHelper {
	
	private MailHelper helper;
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
	
	public MailJSONHelper(MailHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(Mail.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(Mail.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(Mail.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(Mail.class);
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
			jsonHelper.excludeAll(Mail.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(Mail.class);
				break;
			case 2:
				jsonHelper.excludeChildren(Mail.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(Mail.class);
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
	public MailJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public MailJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public MailHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public MailJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public MailJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public MailJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public MailJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public MailJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public MailJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public MailJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<Mail> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<Mail>() {

			@Override
			public void each(Mail bean, List<Mail> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<Mail> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<Mail>() {

			@Override
			public void each(Mail bean, List<Mail> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<Mail> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<Mail>() {

			@Override
			public void each(Mail bean, List<Mail> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<Mail> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<Mail>() {

			@Override
			public void each(Mail bean, List<Mail> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<Mail> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<Mail>() {

			@Override
			public void each(Mail bean, List<Mail> beans) {
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
	public String uniqueJson(EachEntity2Map<Mail> eachEntity2Map){
		Mail bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<Mail> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<Mail>(){

			@Override
			public void each(Mail bean, List<Mail> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<Mail> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<Mail> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<Mail> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<Mail>(){

			@Override
			public void each(Mail bean, List<Mail> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<Mail> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<Mail>(){

			@Override
			public void each(Mail bean, List<Mail> beans) {
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
	 * 序列化json时候，包含 mailId (传阅id)字段
	 * @return
	 */
	public MailJSONHelper includeMailId(){
		this.excludeAll = true;
		this.includes.add("mailId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 mailId (传阅id)字段
	 * @return
	 */
	public MailJSONHelper excludeMailId(){
		this.excludeAll = false;
		this.excludes.add("mailId");
		return this;
	}
	/**
	 * 序列化json时候，包含 userId (发件人id)字段
	 * @return
	 */
	public MailJSONHelper includeUserId(){
		this.excludeAll = true;
		this.includes.add("userId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userId (发件人id)字段
	 * @return
	 */
	public MailJSONHelper excludeUserId(){
		this.excludeAll = false;
		this.excludes.add("userId");
		return this;
	}
	/**
	 * 序列化json时候，包含 workCode (发件人工作编号)字段
	 * @return
	 */
	public MailJSONHelper includeWorkCode(){
		this.excludeAll = true;
		this.includes.add("workCode");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 workCode (发件人工作编号)字段
	 * @return
	 */
	public MailJSONHelper excludeWorkCode(){
		this.excludeAll = false;
		this.excludes.add("workCode");
		return this;
	}
	/**
	 * 序列化json时候，包含 lastName (发件人姓名)字段
	 * @return
	 */
	public MailJSONHelper includeLastName(){
		this.excludeAll = true;
		this.includes.add("lastName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 lastName (发件人姓名)字段
	 * @return
	 */
	public MailJSONHelper excludeLastName(){
		this.excludeAll = false;
		this.excludes.add("lastName");
		return this;
	}
	/**
	 * 序列化json时候，包含 loginId (发件人登录名)字段
	 * @return
	 */
	public MailJSONHelper includeLoginId(){
		this.excludeAll = true;
		this.includes.add("loginId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 loginId (发件人登录名)字段
	 * @return
	 */
	public MailJSONHelper excludeLoginId(){
		this.excludeAll = false;
		this.excludes.add("loginId");
		return this;
	}
	/**
	 * 序列化json时候，包含 subcompanyName (发件人的分部全称)字段
	 * @return
	 */
	public MailJSONHelper includeSubcompanyName(){
		this.excludeAll = true;
		this.includes.add("subcompanyName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 subcompanyName (发件人的分部全称)字段
	 * @return
	 */
	public MailJSONHelper excludeSubcompanyName(){
		this.excludeAll = false;
		this.excludes.add("subcompanyName");
		return this;
	}
	/**
	 * 序列化json时候，包含 departmentName (发件人的部门全称)字段
	 * @return
	 */
	public MailJSONHelper includeDepartmentName(){
		this.excludeAll = true;
		this.includes.add("departmentName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 departmentName (发件人的部门全称)字段
	 * @return
	 */
	public MailJSONHelper excludeDepartmentName(){
		this.excludeAll = false;
		this.excludes.add("departmentName");
		return this;
	}
	/**
	 * 序列化json时候，包含 allReceiveName (收件人名字)字段
	 * @return
	 */
	public MailJSONHelper includeAllReceiveName(){
		this.excludeAll = true;
		this.includes.add("allReceiveName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 allReceiveName (收件人名字)字段
	 * @return
	 */
	public MailJSONHelper excludeAllReceiveName(){
		this.excludeAll = false;
		this.excludes.add("allReceiveName");
		return this;
	}
	/**
	 * 序列化json时候，包含 title (传阅主题)字段
	 * @return
	 */
	public MailJSONHelper includeTitle(){
		this.excludeAll = true;
		this.includes.add("title");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 title (传阅主题)字段
	 * @return
	 */
	public MailJSONHelper excludeTitle(){
		this.excludeAll = false;
		this.excludes.add("title");
		return this;
	}
	/**
	 * 序列化json时候，包含 mailContent (邮件内容)字段
	 * @return
	 */
	public MailJSONHelper includeMailContent(){
		this.excludeAll = true;
		this.includes.add("mailContent");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 mailContent (邮件内容)字段
	 * @return
	 */
	public MailJSONHelper excludeMailContent(){
		this.excludeAll = false;
		this.excludes.add("mailContent");
		return this;
	}
	/**
	 * 序列化json时候，包含 createTime (创建传阅的时间)字段
	 * @return
	 */
	public MailJSONHelper includeCreateTime(){
		this.excludeAll = true;
		this.includes.add("createTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 createTime (创建传阅的时间)字段
	 * @return
	 */
	public MailJSONHelper excludeCreateTime(){
		this.excludeAll = false;
		this.excludes.add("createTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 sendTime (发送传阅的时间)字段
	 * @return
	 */
	public MailJSONHelper includeSendTime(){
		this.excludeAll = true;
		this.includes.add("sendTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 sendTime (发送传阅的时间)字段
	 * @return
	 */
	public MailJSONHelper excludeSendTime(){
		this.excludeAll = false;
		this.excludes.add("sendTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 status (传阅状态)字段
	 * @return
	 */
	public MailJSONHelper includeStatus(){
		this.excludeAll = true;
		this.includes.add("status");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 status (传阅状态)字段
	 * @return
	 */
	public MailJSONHelper excludeStatus(){
		this.excludeAll = false;
		this.excludes.add("status");
		return this;
	}
	/**
	 * 序列化json时候，包含 stepStatus (传阅流程状态)字段
	 * @return
	 */
	public MailJSONHelper includeStepStatus(){
		this.excludeAll = true;
		this.includes.add("stepStatus");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 stepStatus (传阅流程状态)字段
	 * @return
	 */
	public MailJSONHelper excludeStepStatus(){
		this.excludeAll = false;
		this.excludes.add("stepStatus");
		return this;
	}
	/**
	 * 序列化json时候，包含 completeTime (设置的完成时间)字段
	 * @return
	 */
	public MailJSONHelper includeCompleteTime(){
		this.excludeAll = true;
		this.includes.add("completeTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 completeTime (设置的完成时间)字段
	 * @return
	 */
	public MailJSONHelper excludeCompleteTime(){
		this.excludeAll = false;
		this.excludes.add("completeTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifImportant (重要传阅)字段
	 * @return
	 */
	public MailJSONHelper includeIfImportant(){
		this.excludeAll = true;
		this.includes.add("ifImportant");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifImportant (重要传阅)字段
	 * @return
	 */
	public MailJSONHelper excludeIfImportant(){
		this.excludeAll = false;
		this.excludes.add("ifImportant");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifUpdate (允许修订附件)字段
	 * @return
	 */
	public MailJSONHelper includeIfUpdate(){
		this.excludeAll = true;
		this.includes.add("ifUpdate");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifUpdate (允许修订附件)字段
	 * @return
	 */
	public MailJSONHelper excludeIfUpdate(){
		this.excludeAll = false;
		this.excludes.add("ifUpdate");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifUpload (允许上传附件)字段
	 * @return
	 */
	public MailJSONHelper includeIfUpload(){
		this.excludeAll = true;
		this.includes.add("ifUpload");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifUpload (允许上传附件)字段
	 * @return
	 */
	public MailJSONHelper excludeIfUpload(){
		this.excludeAll = false;
		this.excludes.add("ifUpload");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifRead (开封已阅确认)字段
	 * @return
	 */
	public MailJSONHelper includeIfRead(){
		this.excludeAll = true;
		this.includes.add("ifRead");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifRead (开封已阅确认)字段
	 * @return
	 */
	public MailJSONHelper excludeIfRead(){
		this.excludeAll = false;
		this.excludes.add("ifRead");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifNotify (短信提醒)字段
	 * @return
	 */
	public MailJSONHelper includeIfNotify(){
		this.excludeAll = true;
		this.includes.add("ifNotify");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifNotify (短信提醒)字段
	 * @return
	 */
	public MailJSONHelper excludeIfNotify(){
		this.excludeAll = false;
		this.excludes.add("ifNotify");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifRemind (确认时提醒)字段
	 * @return
	 */
	public MailJSONHelper includeIfRemind(){
		this.excludeAll = true;
		this.includes.add("ifRemind");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifRemind (确认时提醒)字段
	 * @return
	 */
	public MailJSONHelper excludeIfRemind(){
		this.excludeAll = false;
		this.excludes.add("ifRemind");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifRemindAll (确认时提醒所有传阅对象)字段
	 * @return
	 */
	public MailJSONHelper includeIfRemindAll(){
		this.excludeAll = true;
		this.includes.add("ifRemindAll");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifRemindAll (确认时提醒所有传阅对象)字段
	 * @return
	 */
	public MailJSONHelper excludeIfRemindAll(){
		this.excludeAll = false;
		this.excludes.add("ifRemindAll");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifSecrecy (传阅密送)字段
	 * @return
	 */
	public MailJSONHelper includeIfSecrecy(){
		this.excludeAll = true;
		this.includes.add("ifSecrecy");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifSecrecy (传阅密送)字段
	 * @return
	 */
	public MailJSONHelper excludeIfSecrecy(){
		this.excludeAll = false;
		this.excludes.add("ifSecrecy");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifAdd (允许新添加人员)字段
	 * @return
	 */
	public MailJSONHelper includeIfAdd(){
		this.excludeAll = true;
		this.includes.add("ifAdd");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifAdd (允许新添加人员)字段
	 * @return
	 */
	public MailJSONHelper excludeIfAdd(){
		this.excludeAll = false;
		this.excludes.add("ifAdd");
		return this;
	}
	/**
	 * 序列化json时候，包含 ifSequence (有序确认)字段
	 * @return
	 */
	public MailJSONHelper includeIfSequence(){
		this.excludeAll = true;
		this.includes.add("ifSequence");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ifSequence (有序确认)字段
	 * @return
	 */
	public MailJSONHelper excludeIfSequence(){
		this.excludeAll = false;
		this.excludes.add("ifSequence");
		return this;
	}
	/**
	 * 序列化json时候，包含 hasAttachment (是否有附件)字段
	 * @return
	 */
	public MailJSONHelper includeHasAttachment(){
		this.excludeAll = true;
		this.includes.add("hasAttachment");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 hasAttachment (是否有附件)字段
	 * @return
	 */
	public MailJSONHelper excludeHasAttachment(){
		this.excludeAll = false;
		this.excludes.add("hasAttachment");
		return this;
	}
	/**
	 * 序列化json时候，包含 enabled (是否启用软删除)字段
	 * @return
	 */
	public MailJSONHelper includeEnabled(){
		this.excludeAll = true;
		this.includes.add("enabled");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 enabled (是否启用软删除)字段
	 * @return
	 */
	public MailJSONHelper excludeEnabled(){
		this.excludeAll = false;
		this.excludes.add("enabled");
		return this;
	}
	/**
	 * 序列化json时候，包含 attention (是否关注)字段
	 * @return
	 */
	public MailJSONHelper includeAttention(){
		this.excludeAll = true;
		this.includes.add("attention");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 attention (是否关注)字段
	 * @return
	 */
	public MailJSONHelper excludeAttention(){
		this.excludeAll = false;
		this.excludes.add("attention");
		return this;
	}
	/**
	 * 序列化json时候，包含 ruleName (传阅规则)字段
	 * @return
	 */
	public MailJSONHelper includeRuleName(){
		this.excludeAll = true;
		this.includes.add("ruleName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ruleName (传阅规则)字段
	 * @return
	 */
	public MailJSONHelper excludeRuleName(){
		this.excludeAll = false;
		this.excludes.add("ruleName");
		return this;
	}
	/**
	 * 序列化json时候，包含 deleteTime (删除传阅的时间)字段
	 * @return
	 */
	public MailJSONHelper includeDeleteTime(){
		this.excludeAll = true;
		this.includes.add("deleteTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 deleteTime (删除传阅的时间)字段
	 * @return
	 */
	public MailJSONHelper excludeDeleteTime(){
		this.excludeAll = false;
		this.excludes.add("deleteTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 attachmentItems ()字段
	 * @return
	 */
	public MailJSONHelper includeAttachmentItems(){
		this.excludeAll = true;
		this.includes.add("attachmentItems");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 attachmentItems ()字段
	 * @return
	 */
	public MailJSONHelper excludeAttachmentItems(){
		this.excludeAll = false;
		this.excludes.add("attachmentItems");
		return this;
	}
	/**
	 * 序列化json时候，包含 receives ()字段
	 * @return
	 */
	public MailJSONHelper includeReceives(){
		this.excludeAll = true;
		this.includes.add("receives");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 receives ()字段
	 * @return
	 */
	public MailJSONHelper excludeReceives(){
		this.excludeAll = false;
		this.excludes.add("receives");
		return this;
	}
	/**
	 * 序列化json时候，包含 discusses ()字段
	 * @return
	 */
	public MailJSONHelper includeDiscusses(){
		this.excludeAll = true;
		this.includes.add("discusses");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 discusses ()字段
	 * @return
	 */
	public MailJSONHelper excludeDiscusses(){
		this.excludeAll = false;
		this.excludes.add("discusses");
		return this;
	}
	/**
	 * 序列化json时候，包含 userCollections ()字段
	 * @return
	 */
	public MailJSONHelper includeUserCollections(){
		this.excludeAll = true;
		this.includes.add("userCollections");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userCollections ()字段
	 * @return
	 */
	public MailJSONHelper excludeUserCollections(){
		this.excludeAll = false;
		this.excludes.add("userCollections");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key mailId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForMailId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("mailId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "mailId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForUserId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "userId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key workCode 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForWorkCode(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("workCode", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "workCode", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key lastName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForLastName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("lastName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "lastName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key loginId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForLoginId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("loginId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "loginId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key subcompanyName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForSubcompanyName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("subcompanyName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "subcompanyName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key departmentName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForDepartmentName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("departmentName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "departmentName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key allReceiveName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForAllReceiveName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("allReceiveName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "allReceiveName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key title 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForTitle(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("title", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "title", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key mailContent 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForMailContent(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("mailContent", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "mailContent", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key createTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForCreateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("createTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "createTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key sendTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForSendTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("sendTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "sendTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key status 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForStatus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("status", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "status", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key stepStatus 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForStepStatus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("stepStatus", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "stepStatus", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key completeTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForCompleteTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("completeTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "completeTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifImportant 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfImportant(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifImportant", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifImportant", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifUpdate 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfUpdate(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifUpdate", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifUpdate", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifUpload 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfUpload(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifUpload", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifUpload", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifRead 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfRead(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifRead", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifRead", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifNotify 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfNotify(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifNotify", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifNotify", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifRemind 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfRemind(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifRemind", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifRemind", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifRemindAll 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfRemindAll(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifRemindAll", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifRemindAll", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifSecrecy 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfSecrecy(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifSecrecy", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifSecrecy", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifAdd 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfAdd(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifAdd", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifAdd", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ifSequence 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForIfSequence(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ifSequence", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ifSequence", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key hasAttachment 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForHasAttachment(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("hasAttachment", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "hasAttachment", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key enabled 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForEnabled(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("enabled", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "enabled", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key attention 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForAttention(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("attention", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "attention", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ruleName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForRuleName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ruleName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "ruleName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key deleteTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForDeleteTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("deleteTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "deleteTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key attachmentItems 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForAttachmentItems(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("attachmentItems", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "attachmentItems", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key receives 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForReceives(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("receives", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "receives", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key discusses 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForDiscusses(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("discusses", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "discusses", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userCollections 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MailJSONHelper replaceKeyForUserCollections(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userCollections", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Mail.class, "userCollections", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 mailId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterMailId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Long.class, filterValue, "mailId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterUserId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Long.class, filterValue, "userId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 workCode 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterWorkCode(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "workCode");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 lastName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterLastName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "lastName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 loginId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterLoginId(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "loginId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 subcompanyName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterSubcompanyName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "subcompanyName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 departmentName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterDepartmentName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "departmentName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 allReceiveName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterAllReceiveName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "allReceiveName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 title 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterTitle(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "title");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 mailContent 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterMailContent(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "mailContent");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 createTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterCreateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Date.class, filterValue, "createTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 sendTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterSendTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Date.class, filterValue, "sendTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 status 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterStatus(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Integer.class, filterValue, "status");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 stepStatus 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterStepStatus(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Integer.class, filterValue, "stepStatus");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 completeTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterCompleteTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Date.class, filterValue, "completeTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifImportant 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfImportant(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifImportant");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifUpdate 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfUpdate(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifUpdate");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifUpload 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfUpload(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifUpload");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifRead 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfRead(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifRead");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifNotify 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfNotify(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifNotify");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifRemind 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfRemind(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifRemind");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifRemindAll 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfRemindAll(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifRemindAll");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifSecrecy 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfSecrecy(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifSecrecy");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifAdd 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfAdd(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifAdd");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ifSequence 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterIfSequence(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "ifSequence");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 hasAttachment 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterHasAttachment(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "hasAttachment");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 enabled 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterEnabled(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "enabled");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 attention 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterAttention(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Boolean.class, filterValue, "attention");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ruleName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterRuleName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, String.class, filterValue, "ruleName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 deleteTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterDeleteTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Date.class, filterValue, "deleteTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 attachmentItems 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterAttachmentItems(FilterValue<AttachmentItem> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, AttachmentItem.class, filterValue, "attachmentItems");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 receives 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterReceives(FilterValue<Receive> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Receive.class, filterValue, "receives");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 discusses 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterDiscusses(FilterValue<Discuss> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, Discuss.class, filterValue, "discusses");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userCollections 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MailJSONHelper filterUserCollections(FilterValue<UserCollection> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Mail.class, UserCollection.class, filterValue, "userCollections");
		return this;
	}
	
}
