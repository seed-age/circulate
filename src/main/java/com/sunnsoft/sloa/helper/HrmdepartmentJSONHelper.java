package com.sunnsoft.sloa.helper;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.sunnsoft.sloa.db.vo.Hrmdepartment;
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
public class HrmdepartmentJSONHelper {
	
	private HrmdepartmentHelper helper;
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
	
	public HrmdepartmentJSONHelper(HrmdepartmentHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(Hrmdepartment.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(Hrmdepartment.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(Hrmdepartment.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(Hrmdepartment.class);
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
			jsonHelper.excludeAll(Hrmdepartment.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(Hrmdepartment.class);
				break;
			case 2:
				jsonHelper.excludeChildren(Hrmdepartment.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(Hrmdepartment.class);
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
	public HrmdepartmentJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public HrmdepartmentJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public HrmdepartmentHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public HrmdepartmentJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public HrmdepartmentJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public HrmdepartmentJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public HrmdepartmentJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public HrmdepartmentJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public HrmdepartmentJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public HrmdepartmentJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<Hrmdepartment> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<Hrmdepartment>() {

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<Hrmdepartment> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<Hrmdepartment>() {

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<Hrmdepartment> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<Hrmdepartment>() {

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<Hrmdepartment> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<Hrmdepartment>() {

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<Hrmdepartment> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<Hrmdepartment>() {

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> beans) {
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
	public String uniqueJson(EachEntity2Map<Hrmdepartment> eachEntity2Map){
		Hrmdepartment bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<Hrmdepartment> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<Hrmdepartment>(){

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<Hrmdepartment> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<Hrmdepartment> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<Hrmdepartment> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<Hrmdepartment>(){

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<Hrmdepartment> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<Hrmdepartment>(){

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> beans) {
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
	public HrmdepartmentJSONHelper includeId(){
		this.excludeAll = true;
		this.includes.add("id");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 id (id)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeId(){
		this.excludeAll = false;
		this.excludes.add("id");
		return this;
	}
	/**
	 * 序列化json时候，包含 departmentmark (部门标识)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeDepartmentmark(){
		this.excludeAll = true;
		this.includes.add("departmentmark");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 departmentmark (部门标识)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeDepartmentmark(){
		this.excludeAll = false;
		this.excludes.add("departmentmark");
		return this;
	}
	/**
	 * 序列化json时候，包含 departmentname (部门名称)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeDepartmentname(){
		this.excludeAll = true;
		this.includes.add("departmentname");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 departmentname (部门名称)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeDepartmentname(){
		this.excludeAll = false;
		this.excludes.add("departmentname");
		return this;
	}
	/**
	 * 序列化json时候，包含 subcompanyid1 (所属分部1)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeSubcompanyid1(){
		this.excludeAll = true;
		this.includes.add("subcompanyid1");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 subcompanyid1 (所属分部1)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeSubcompanyid1(){
		this.excludeAll = false;
		this.excludes.add("subcompanyid1");
		return this;
	}
	/**
	 * 序列化json时候，包含 supdepid (上级部门id)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeSupdepid(){
		this.excludeAll = true;
		this.includes.add("supdepid");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 supdepid (上级部门id)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeSupdepid(){
		this.excludeAll = false;
		this.excludes.add("supdepid");
		return this;
	}
	/**
	 * 序列化json时候，包含 allsupdepid (所有上级部门id)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeAllsupdepid(){
		this.excludeAll = true;
		this.includes.add("allsupdepid");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 allsupdepid (所有上级部门id)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeAllsupdepid(){
		this.excludeAll = false;
		this.excludes.add("allsupdepid");
		return this;
	}
	/**
	 * 序列化json时候，包含 showorder (显示顺序)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeShoworder(){
		this.excludeAll = true;
		this.includes.add("showorder");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 showorder (显示顺序)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeShoworder(){
		this.excludeAll = false;
		this.excludes.add("showorder");
		return this;
	}
	/**
	 * 序列化json时候，包含 canceled (封存标识)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeCanceled(){
		this.excludeAll = true;
		this.includes.add("canceled");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 canceled (封存标识)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeCanceled(){
		this.excludeAll = false;
		this.excludes.add("canceled");
		return this;
	}
	/**
	 * 序列化json时候，包含 departmentcode (部门编码)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeDepartmentcode(){
		this.excludeAll = true;
		this.includes.add("departmentcode");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 departmentcode (部门编码)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeDepartmentcode(){
		this.excludeAll = false;
		this.excludes.add("departmentcode");
		return this;
	}
	/**
	 * 序列化json时候，包含 coadjutant (协办人)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeCoadjutant(){
		this.excludeAll = true;
		this.includes.add("coadjutant");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 coadjutant (协办人)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeCoadjutant(){
		this.excludeAll = false;
		this.excludes.add("coadjutant");
		return this;
	}
	/**
	 * 序列化json时候，包含 zzjgbmfzr (组织架构部门负责人)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeZzjgbmfzr(){
		this.excludeAll = true;
		this.includes.add("zzjgbmfzr");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 zzjgbmfzr (组织架构部门负责人)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeZzjgbmfzr(){
		this.excludeAll = false;
		this.excludes.add("zzjgbmfzr");
		return this;
	}
	/**
	 * 序列化json时候，包含 zzjgbmfgld (组织架构部门分管领导)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeZzjgbmfgld(){
		this.excludeAll = true;
		this.includes.add("zzjgbmfgld");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 zzjgbmfgld (组织架构部门分管领导)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeZzjgbmfgld(){
		this.excludeAll = false;
		this.excludes.add("zzjgbmfgld");
		return this;
	}
	/**
	 * 序列化json时候，包含 jzglbmfzr (矩阵管理部门负责人)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeJzglbmfzr(){
		this.excludeAll = true;
		this.includes.add("jzglbmfzr");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 jzglbmfzr (矩阵管理部门负责人)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeJzglbmfzr(){
		this.excludeAll = false;
		this.excludes.add("jzglbmfzr");
		return this;
	}
	/**
	 * 序列化json时候，包含 jzglbmfgld (矩阵管理部门分管领导)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeJzglbmfgld(){
		this.excludeAll = true;
		this.includes.add("jzglbmfgld");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 jzglbmfgld (矩阵管理部门分管领导)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeJzglbmfgld(){
		this.excludeAll = false;
		this.excludes.add("jzglbmfgld");
		return this;
	}
	/**
	 * 序列化json时候，包含 bmfzr (矩阵部门负责人员)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeBmfzr(){
		this.excludeAll = true;
		this.includes.add("bmfzr");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 bmfzr (矩阵部门负责人员)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeBmfzr(){
		this.excludeAll = false;
		this.excludes.add("bmfzr");
		return this;
	}
	/**
	 * 序列化json时候，包含 bmfgld (矩阵部门分管领导)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeBmfgld(){
		this.excludeAll = true;
		this.includes.add("bmfgld");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 bmfgld (矩阵部门分管领导)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeBmfgld(){
		this.excludeAll = false;
		this.excludes.add("bmfgld");
		return this;
	}
	/**
	 * 序列化json时候，包含 outkey (outkey)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeOutkey(){
		this.excludeAll = true;
		this.includes.add("outkey");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 outkey (outkey)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeOutkey(){
		this.excludeAll = false;
		this.excludes.add("outkey");
		return this;
	}
	/**
	 * 序列化json时候，包含 budgetatuomoveorder (budgetatuomoveorder)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeBudgetatuomoveorder(){
		this.excludeAll = true;
		this.includes.add("budgetatuomoveorder");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 budgetatuomoveorder (budgetatuomoveorder)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeBudgetatuomoveorder(){
		this.excludeAll = false;
		this.excludes.add("budgetatuomoveorder");
		return this;
	}
	/**
	 * 序列化json时候，包含 ecologyPinyinSearch (ecology_pinyin_search)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeEcologyPinyinSearch(){
		this.excludeAll = true;
		this.includes.add("ecologyPinyinSearch");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 ecologyPinyinSearch (ecology_pinyin_search)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeEcologyPinyinSearch(){
		this.excludeAll = false;
		this.excludes.add("ecologyPinyinSearch");
		return this;
	}
	/**
	 * 序列化json时候，包含 tlevel (tlevel)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper includeTlevel(){
		this.excludeAll = true;
		this.includes.add("tlevel");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 tlevel (tlevel)字段
	 * @return
	 */
	public HrmdepartmentJSONHelper excludeTlevel(){
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
	public HrmdepartmentJSONHelper replaceKeyForId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("id", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "id", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key departmentmark 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForDepartmentmark(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("departmentmark", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "departmentmark", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key departmentname 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForDepartmentname(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("departmentname", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "departmentname", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key subcompanyid1 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForSubcompanyid1(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("subcompanyid1", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "subcompanyid1", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key supdepid 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForSupdepid(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("supdepid", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "supdepid", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key allsupdepid 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForAllsupdepid(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("allsupdepid", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "allsupdepid", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key showorder 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForShoworder(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("showorder", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "showorder", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key canceled 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForCanceled(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("canceled", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "canceled", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key departmentcode 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForDepartmentcode(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("departmentcode", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "departmentcode", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key coadjutant 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForCoadjutant(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("coadjutant", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "coadjutant", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key zzjgbmfzr 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForZzjgbmfzr(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("zzjgbmfzr", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "zzjgbmfzr", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key zzjgbmfgld 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForZzjgbmfgld(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("zzjgbmfgld", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "zzjgbmfgld", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key jzglbmfzr 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForJzglbmfzr(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("jzglbmfzr", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "jzglbmfzr", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key jzglbmfgld 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForJzglbmfgld(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("jzglbmfgld", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "jzglbmfgld", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key bmfzr 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForBmfzr(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("bmfzr", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "bmfzr", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key bmfgld 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForBmfgld(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("bmfgld", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "bmfgld", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key outkey 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForOutkey(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("outkey", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "outkey", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key budgetatuomoveorder 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForBudgetatuomoveorder(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("budgetatuomoveorder", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "budgetatuomoveorder", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key ecologyPinyinSearch 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForEcologyPinyinSearch(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("ecologyPinyinSearch", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "ecologyPinyinSearch", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key tlevel 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public HrmdepartmentJSONHelper replaceKeyForTlevel(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("tlevel", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Hrmdepartment.class, "tlevel", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 id 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterId(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, Integer.class, filterValue, "id");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 departmentmark 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterDepartmentmark(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "departmentmark");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 departmentname 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterDepartmentname(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "departmentname");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 subcompanyid1 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterSubcompanyid1(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, Integer.class, filterValue, "subcompanyid1");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 supdepid 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterSupdepid(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, Integer.class, filterValue, "supdepid");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 allsupdepid 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterAllsupdepid(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "allsupdepid");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 showorder 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterShoworder(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, Integer.class, filterValue, "showorder");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 canceled 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterCanceled(FilterValue<Character> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, Character.class, filterValue, "canceled");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 departmentcode 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterDepartmentcode(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "departmentcode");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 coadjutant 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterCoadjutant(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, Integer.class, filterValue, "coadjutant");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 zzjgbmfzr 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterZzjgbmfzr(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "zzjgbmfzr");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 zzjgbmfgld 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterZzjgbmfgld(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "zzjgbmfgld");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 jzglbmfzr 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterJzglbmfzr(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "jzglbmfzr");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 jzglbmfgld 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterJzglbmfgld(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "jzglbmfgld");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 bmfzr 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterBmfzr(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "bmfzr");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 bmfgld 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterBmfgld(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "bmfgld");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 outkey 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterOutkey(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "outkey");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 budgetatuomoveorder 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterBudgetatuomoveorder(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, Integer.class, filterValue, "budgetatuomoveorder");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 ecologyPinyinSearch 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterEcologyPinyinSearch(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, String.class, filterValue, "ecologyPinyinSearch");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 tlevel 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public HrmdepartmentJSONHelper filterTlevel(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Hrmdepartment.class, Integer.class, filterValue, "tlevel");
		return this;
	}
	
}
