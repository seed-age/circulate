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
public class MenuJSONHelper {
	
	private MenuHelper helper;
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
	
	public MenuJSONHelper(MenuHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(Menu.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(Menu.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(Menu.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(Menu.class);
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
			jsonHelper.excludeAll(Menu.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(Menu.class);
				break;
			case 2:
				jsonHelper.excludeChildren(Menu.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(Menu.class);
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
	public MenuJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public MenuJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public MenuHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public MenuJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public MenuJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public MenuJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public MenuJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public MenuJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public MenuJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public MenuJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<Menu> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<Menu>() {

			@Override
			public void each(Menu bean, List<Menu> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<Menu> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<Menu>() {

			@Override
			public void each(Menu bean, List<Menu> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<Menu> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<Menu>() {

			@Override
			public void each(Menu bean, List<Menu> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<Menu> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<Menu>() {

			@Override
			public void each(Menu bean, List<Menu> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<Menu> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<Menu>() {

			@Override
			public void each(Menu bean, List<Menu> beans) {
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
	public String uniqueJson(EachEntity2Map<Menu> eachEntity2Map){
		Menu bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<Menu> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<Menu>(){

			@Override
			public void each(Menu bean, List<Menu> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<Menu> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<Menu> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<Menu> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<Menu>(){

			@Override
			public void each(Menu bean, List<Menu> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<Menu> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<Menu>(){

			@Override
			public void each(Menu bean, List<Menu> beans) {
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
	 * 序列化json时候，包含 menuId (菜单ID)字段
	 * @return
	 */
	public MenuJSONHelper includeMenuId(){
		this.excludeAll = true;
		this.includes.add("menuId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 menuId (菜单ID)字段
	 * @return
	 */
	public MenuJSONHelper excludeMenuId(){
		this.excludeAll = false;
		this.excludes.add("menuId");
		return this;
	}
	/**
	 * 序列化json时候，包含 menu ()字段
	 * @return
	 */
	public MenuJSONHelper includeMenu(){
		this.excludeAll = true;
		this.includes.add("menu");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 menu ()字段
	 * @return
	 */
	public MenuJSONHelper excludeMenu(){
		this.excludeAll = false;
		this.excludes.add("menu");
		return this;
	}
	/**
	 * 序列化json时候，包含 menuText (菜单名称)字段
	 * @return
	 */
	public MenuJSONHelper includeMenuText(){
		this.excludeAll = true;
		this.includes.add("menuText");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 menuText (菜单名称)字段
	 * @return
	 */
	public MenuJSONHelper excludeMenuText(){
		this.excludeAll = false;
		this.excludes.add("menuText");
		return this;
	}
	/**
	 * 序列化json时候，包含 icon (图标路径)字段
	 * @return
	 */
	public MenuJSONHelper includeIcon(){
		this.excludeAll = true;
		this.includes.add("icon");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 icon (图标路径)字段
	 * @return
	 */
	public MenuJSONHelper excludeIcon(){
		this.excludeAll = false;
		this.excludes.add("icon");
		return this;
	}
	/**
	 * 序列化json时候，包含 actionPath (ACTION路径)字段
	 * @return
	 */
	public MenuJSONHelper includeActionPath(){
		this.excludeAll = true;
		this.includes.add("actionPath");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 actionPath (ACTION路径)字段
	 * @return
	 */
	public MenuJSONHelper excludeActionPath(){
		this.excludeAll = false;
		this.excludes.add("actionPath");
		return this;
	}
	/**
	 * 序列化json时候，包含 extId (EXT_ID)字段
	 * @return
	 */
	public MenuJSONHelper includeExtId(){
		this.excludeAll = true;
		this.includes.add("extId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 extId (EXT_ID)字段
	 * @return
	 */
	public MenuJSONHelper excludeExtId(){
		this.excludeAll = false;
		this.excludes.add("extId");
		return this;
	}
	/**
	 * 序列化json时候，包含 layout (布局)字段
	 * @return
	 */
	public MenuJSONHelper includeLayout(){
		this.excludeAll = true;
		this.includes.add("layout");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 layout (布局)字段
	 * @return
	 */
	public MenuJSONHelper excludeLayout(){
		this.excludeAll = false;
		this.excludes.add("layout");
		return this;
	}
	/**
	 * 序列化json时候，包含 leaf (是否叶节点)字段
	 * @return
	 */
	public MenuJSONHelper includeLeaf(){
		this.excludeAll = true;
		this.includes.add("leaf");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 leaf (是否叶节点)字段
	 * @return
	 */
	public MenuJSONHelper excludeLeaf(){
		this.excludeAll = false;
		this.excludes.add("leaf");
		return this;
	}
	/**
	 * 序列化json时候，包含 expanded (是否展开)字段
	 * @return
	 */
	public MenuJSONHelper includeExpanded(){
		this.excludeAll = true;
		this.includes.add("expanded");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 expanded (是否展开)字段
	 * @return
	 */
	public MenuJSONHelper excludeExpanded(){
		this.excludeAll = false;
		this.excludes.add("expanded");
		return this;
	}
	/**
	 * 序列化json时候，包含 indexValue (排序)字段
	 * @return
	 */
	public MenuJSONHelper includeIndexValue(){
		this.excludeAll = true;
		this.includes.add("indexValue");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 indexValue (排序)字段
	 * @return
	 */
	public MenuJSONHelper excludeIndexValue(){
		this.excludeAll = false;
		this.excludes.add("indexValue");
		return this;
	}
	/**
	 * 序列化json时候，包含 enabled (是否停用)字段
	 * @return
	 */
	public MenuJSONHelper includeEnabled(){
		this.excludeAll = true;
		this.includes.add("enabled");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 enabled (是否停用)字段
	 * @return
	 */
	public MenuJSONHelper excludeEnabled(){
		this.excludeAll = false;
		this.excludes.add("enabled");
		return this;
	}
	/**
	 * 序列化json时候，包含 iconCls (图标class)字段
	 * @return
	 */
	public MenuJSONHelper includeIconCls(){
		this.excludeAll = true;
		this.includes.add("iconCls");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 iconCls (图标class)字段
	 * @return
	 */
	public MenuJSONHelper excludeIconCls(){
		this.excludeAll = false;
		this.excludes.add("iconCls");
		return this;
	}
	/**
	 * 序列化json时候，包含 roles ()字段
	 * @return
	 */
	public MenuJSONHelper includeRoles(){
		this.excludeAll = true;
		this.includes.add("roles");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 roles ()字段
	 * @return
	 */
	public MenuJSONHelper excludeRoles(){
		this.excludeAll = false;
		this.excludes.add("roles");
		return this;
	}
	/**
	 * 序列化json时候，包含 menus ()字段
	 * @return
	 */
	public MenuJSONHelper includeMenus(){
		this.excludeAll = true;
		this.includes.add("menus");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 menus ()字段
	 * @return
	 */
	public MenuJSONHelper excludeMenus(){
		this.excludeAll = false;
		this.excludes.add("menus");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key menuId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForMenuId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("menuId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "menuId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key menu 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForMenu(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("menu", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "menu", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key menuText 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForMenuText(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("menuText", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "menuText", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key icon 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForIcon(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("icon", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "icon", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key actionPath 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForActionPath(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("actionPath", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "actionPath", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key extId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForExtId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("extId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "extId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key layout 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForLayout(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("layout", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "layout", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key leaf 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForLeaf(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("leaf", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "leaf", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key expanded 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForExpanded(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("expanded", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "expanded", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key indexValue 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForIndexValue(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("indexValue", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "indexValue", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key enabled 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForEnabled(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("enabled", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "enabled", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key iconCls 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForIconCls(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("iconCls", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "iconCls", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key roles 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForRoles(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("roles", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "roles", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key menus 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public MenuJSONHelper replaceKeyForMenus(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("menus", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, Menu.class, "menus", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 menuId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterMenuId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, Long.class, filterValue, "menuId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 menu 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterMenu(FilterValue<Menu> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, Menu.class, filterValue, "menu");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 menuText 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterMenuText(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, String.class, filterValue, "menuText");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 icon 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterIcon(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, String.class, filterValue, "icon");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 actionPath 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterActionPath(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, String.class, filterValue, "actionPath");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 extId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterExtId(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, String.class, filterValue, "extId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 layout 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterLayout(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, String.class, filterValue, "layout");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 leaf 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterLeaf(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, Boolean.class, filterValue, "leaf");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 expanded 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterExpanded(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, Boolean.class, filterValue, "expanded");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 indexValue 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterIndexValue(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, Integer.class, filterValue, "indexValue");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 enabled 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterEnabled(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, Boolean.class, filterValue, "enabled");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 iconCls 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterIconCls(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, String.class, filterValue, "iconCls");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 roles 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterRoles(FilterValue<Role> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, Role.class, filterValue, "roles");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 menus 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public MenuJSONHelper filterMenus(FilterValue<Menu> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, Menu.class, Menu.class, filterValue, "menus");
		return this;
	}
	
}
