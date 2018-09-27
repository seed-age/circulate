package com.sunnsoft.sloa.helper;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.UpdateRecord;
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
public class AttachmentItemJSONHelper {
	
	private AttachmentItemHelper helper;
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
	
	public AttachmentItemJSONHelper(AttachmentItemHelper helper) {
		super();
		this.helper = helper;
	}

	public String fastJsonSerializer(final Object object , boolean ignoreExcludeAndFilter){
		fastJsonHelper = FastJSONUtils.getJsonHelper();
		if(!ignoreExcludeAndFilter){
			if(excludeAll){
				fastJsonHelper.excludeAll(AttachmentItem.class);
				fastJsonHelper.removeExcludes(includes);
			}else{
				fastJsonHelper.addExcludes(excludes);
				switch (excludeRelactionType) {
				case 1:
					fastJsonHelper.excludeParent(AttachmentItem.class);
					break;
				case 2:
					fastJsonHelper.excludeChildren(AttachmentItem.class);
					break;
				case 3:
					fastJsonHelper.excludeForeignObject(AttachmentItem.class);
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
			jsonHelper.excludeAll(AttachmentItem.class);
			jsonHelper.removeExcludes(includes);
		}else{
			jsonHelper.addExcludes(excludes);
			switch (excludeRelactionType) {
			case 1:
				jsonHelper.excludeParent(AttachmentItem.class);
				break;
			case 2:
				jsonHelper.excludeChildren(AttachmentItem.class);
				break;
			case 3:
				jsonHelper.excludeForeignObject(AttachmentItem.class);
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
	public AttachmentItemJSONHelper setDateFormat(String format){
		this.dateFormat = format;
		return this;
	}
	
	/**
	 * 以缩进的方式输出，便于调试
	 */
	public AttachmentItemJSONHelper prettyFormat(){
		this.prettyFormat = true;
		return this;
	}
	
	/**
	 * 返回上一层helper对象继续其他操作
	 */
	public AttachmentItemHelper returnHelper(){
		return helper;
	}
	
	/**
	 * 打印调试信息。
	 */
	public AttachmentItemJSONHelper debug(){
		this.prettyFormat = true;
		this.debug = true;
		return this;
	}
	
	/**
	 * 对于Ext form load ，需要把boolean值转换为string类型才能支持radio类型的值的加载。
	 * 这个问题在ext3和4都存在，extjs官方没有给出解决办法。
	 * @return
	 */
	public AttachmentItemJSONHelper setBooleanToString(){
		convertBooleanToString = true;
		return this;
	}
	
	/**
	 * 关掉Null值显示，默认序列号为json的时候，null值会显示出来，关掉该选项，所有null值的Key在序列号json的时候被移除。
	 * 注意：本选项只在fastjson序列化的时候有效，转换成JSONObject的，该选项无效。
	 * @return
	 */
	public AttachmentItemJSONHelper disableNullValue(){
		enableNullValue = false;
		return this;
	}
	
	/**
	 * 设置序列化为json时，只排除OneToMany关系，默认是排除所有关联关系。
	 * @return
	 */
	public AttachmentItemJSONHelper cutOneToMany(){
		this.excludeRelactionType = 2;
		return this;
	}
	/**
	 * 设置序列化为json时，只排除ManyToOne关系，默认是排除所有关联关系。
	 * @return
	 */
	public AttachmentItemJSONHelper cutManyToOne(){
		this.excludeRelactionType = 1;
		return this;
	} 
	/**
	 * 设置序列化为json时，排除所有关联关系，这个是默认设置。
	 * @return
	 */
	public AttachmentItemJSONHelper cutXtoX(){//默认
		this.excludeRelactionType = 3;
		return this;
	}
	/**
	 * 设置序列化为json时，包含所有关联关系，这个很少用到，因为关联关系会导致，默认是排除所有关联关系。
	 * @return
	 */
	public AttachmentItemJSONHelper keepXtoX(){
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
	public String listJson(final EachEntity2Map<AttachmentItem> eachEntity2Map){
		final List list = new ArrayList();
		this.helper.each(new Each<AttachmentItem>() {

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> beans) {
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
	public String listPageJson(int page,final EachEntity2Map<AttachmentItem> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,new Each<AttachmentItem>() {

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> beans) {
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
	public String listPageScrollJson(int page,final EachEntity2Map<AttachmentItem> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,new Each<AttachmentItem>() {

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> beans) {
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
	public String listPageJson(int page ,int pageSize ,final EachEntity2Map<AttachmentItem> eachEntity2Map){
		final List list = new ArrayList();
		PageList pageList = this.helper.eachInPage(page,pageSize,new Each<AttachmentItem>() {

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> beans) {
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
	public String listPageScrollJson(int page ,int pageSize ,final EachEntity2Map<AttachmentItem> eachEntity2Map){
		final List list = new ArrayList();
		PageScroll pageScroll = this.helper.eachInPageScroll(page,pageSize,new Each<AttachmentItem>() {

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> beans) {
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
	public String uniqueJson(EachEntity2Map<AttachmentItem> eachEntity2Map){
		AttachmentItem bean = this.helper.uniqueResult();
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
	public JSONArray jsonEach(final EachJSON<AttachmentItem> eachJSON){
		if(enableNameFilter){
			throw new HelperException("使用replaceKeyFor方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		if(enableValueFilter){
			throw new HelperException("使用filter方法后，就不能转换为JSONObject或JSONArray进行处理");
		}
		final JSONArray jsonArray = new JSONArray();
		this.helper.each(new Each<AttachmentItem>(){

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> beans) {
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
	public JSONObject jsonEachInPage(int page,EachJSON<AttachmentItem> eachJSON){
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
	public JSONObject jsonEachInPageScroll(int page,EachJSON<AttachmentItem> eachJSON){
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
	public JSONObject jsonEachInPage(int page,int pageSize,final EachJSON<AttachmentItem> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPage(page, pageSize,new Each<AttachmentItem>(){

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> beans) {
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
	public JSONObject jsonEachInPageScroll(int page,int pageSize,final EachJSON<AttachmentItem> eachJSON){
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
		JSONObject result = innerHelper.toJSONObject(this.helper.eachInPageScroll(page, pageSize,new Each<AttachmentItem>(){

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> beans) {
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
	 * 序列化json时候，包含 itemId (附件文件ID)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeItemId(){
		this.excludeAll = true;
		this.includes.add("itemId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 itemId (附件文件ID)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeItemId(){
		this.excludeAll = false;
		this.excludes.add("itemId");
		return this;
	}
	/**
	 * 序列化json时候，包含 mail ()字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeMail(){
		this.excludeAll = true;
		this.includes.add("mail");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 mail ()字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeMail(){
		this.excludeAll = false;
		this.excludes.add("mail");
		return this;
	}
	/**
	 * 序列化json时候，包含 bulkId (附件上传批次ID)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeBulkId(){
		this.excludeAll = true;
		this.includes.add("bulkId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 bulkId (附件上传批次ID)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeBulkId(){
		this.excludeAll = false;
		this.excludes.add("bulkId");
		return this;
	}
	/**
	 * 序列化json时候，包含 userId (创建人ID)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeUserId(){
		this.excludeAll = true;
		this.includes.add("userId");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 userId (创建人ID)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeUserId(){
		this.excludeAll = false;
		this.excludes.add("userId");
		return this;
	}
	/**
	 * 序列化json时候，包含 creator (创建人)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeCreator(){
		this.excludeAll = true;
		this.includes.add("creator");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 creator (创建人)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeCreator(){
		this.excludeAll = false;
		this.excludes.add("creator");
		return this;
	}
	/**
	 * 序列化json时候，包含 createTime (创建时间)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeCreateTime(){
		this.excludeAll = true;
		this.includes.add("createTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 createTime (创建时间)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeCreateTime(){
		this.excludeAll = false;
		this.excludes.add("createTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 fileName (文件原名)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeFileName(){
		this.excludeAll = true;
		this.includes.add("fileName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 fileName (文件原名)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeFileName(){
		this.excludeAll = false;
		this.excludes.add("fileName");
		return this;
	}
	/**
	 * 序列化json时候，包含 fileCategory (分类)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeFileCategory(){
		this.excludeAll = true;
		this.includes.add("fileCategory");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 fileCategory (分类)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeFileCategory(){
		this.excludeAll = false;
		this.excludes.add("fileCategory");
		return this;
	}
	/**
	 * 序列化json时候，包含 saveName (文件保存名)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeSaveName(){
		this.excludeAll = true;
		this.includes.add("saveName");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 saveName (文件保存名)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeSaveName(){
		this.excludeAll = false;
		this.excludes.add("saveName");
		return this;
	}
	/**
	 * 序列化json时候，包含 urlPath (链接地址)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeUrlPath(){
		this.excludeAll = true;
		this.includes.add("urlPath");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 urlPath (链接地址)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeUrlPath(){
		this.excludeAll = false;
		this.excludes.add("urlPath");
		return this;
	}
	/**
	 * 序列化json时候，包含 attached (是否实体附件)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeAttached(){
		this.excludeAll = true;
		this.includes.add("attached");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 attached (是否实体附件)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeAttached(){
		this.excludeAll = false;
		this.excludes.add("attached");
		return this;
	}
	/**
	 * 序列化json时候，包含 state (状态)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeState(){
		this.excludeAll = true;
		this.includes.add("state");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 state (状态)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeState(){
		this.excludeAll = false;
		this.excludes.add("state");
		return this;
	}
	/**
	 * 序列化json时候，包含 itemSize (文件大小)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeItemSize(){
		this.excludeAll = true;
		this.includes.add("itemSize");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 itemSize (文件大小)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeItemSize(){
		this.excludeAll = false;
		this.excludes.add("itemSize");
		return this;
	}
	/**
	 * 序列化json时候，包含 itemNeid (网盘文件ID)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeItemNeid(){
		this.excludeAll = true;
		this.includes.add("itemNeid");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 itemNeid (网盘文件ID)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeItemNeid(){
		this.excludeAll = false;
		this.excludes.add("itemNeid");
		return this;
	}
	/**
	 * 序列化json时候，包含 itemRev (网盘文件版本)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeItemRev(){
		this.excludeAll = true;
		this.includes.add("itemRev");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 itemRev (网盘文件版本)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeItemRev(){
		this.excludeAll = false;
		this.excludes.add("itemRev");
		return this;
	}
	/**
	 * 序列化json时候，包含 itemDifferentiate (区别)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeItemDifferentiate(){
		this.excludeAll = true;
		this.includes.add("itemDifferentiate");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 itemDifferentiate (区别)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeItemDifferentiate(){
		this.excludeAll = false;
		this.excludes.add("itemDifferentiate");
		return this;
	}
	/**
	 * 序列化json时候，包含 localhostUrlPath (存储APP端下载到本地的URL)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeLocalhostUrlPath(){
		this.excludeAll = true;
		this.includes.add("localhostUrlPath");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 localhostUrlPath (存储APP端下载到本地的URL)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeLocalhostUrlPath(){
		this.excludeAll = false;
		this.excludes.add("localhostUrlPath");
		return this;
	}
	/**
	 * 序列化json时候，包含 updateTime (更新时间)字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeUpdateTime(){
		this.excludeAll = true;
		this.includes.add("updateTime");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 updateTime (更新时间)字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeUpdateTime(){
		this.excludeAll = false;
		this.excludes.add("updateTime");
		return this;
	}
	/**
	 * 序列化json时候，包含 updateRecords ()字段
	 * @return
	 */
	public AttachmentItemJSONHelper includeUpdateRecords(){
		this.excludeAll = true;
		this.includes.add("updateRecords");
		return this;
	}
	
	/**
	 * 序列化json时候，排除 updateRecords ()字段
	 * @return
	 */
	public AttachmentItemJSONHelper excludeUpdateRecords(){
		this.excludeAll = false;
		this.excludes.add("updateRecords");
		return this;
	}
	//----replaceKeyFor----//
	
	/**
	 * 序列化json时候， 替换 key itemId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForItemId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("itemId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "itemId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key mail 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForMail(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("mail", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "mail", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key bulkId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForBulkId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("bulkId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "bulkId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key userId 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForUserId(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("userId", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "userId", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key creator 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForCreator(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("creator", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "creator", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key createTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForCreateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("createTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "createTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key fileName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForFileName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("fileName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "fileName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key fileCategory 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForFileCategory(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("fileCategory", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "fileCategory", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key saveName 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForSaveName(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("saveName", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "saveName", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key urlPath 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForUrlPath(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("urlPath", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "urlPath", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key attached 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForAttached(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("attached", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "attached", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key state 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForState(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("state", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "state", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key itemSize 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForItemSize(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("itemSize", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "itemSize", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key itemNeid 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForItemNeid(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("itemNeid", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "itemNeid", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key itemRev 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForItemRev(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("itemRev", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "itemRev", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key itemDifferentiate 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForItemDifferentiate(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("itemDifferentiate", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "itemDifferentiate", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key localhostUrlPath 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForLocalhostUrlPath(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("localhostUrlPath", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "localhostUrlPath", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key updateTime 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForUpdateTime(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("updateTime", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "updateTime", keyName);
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 key updateRecords 为 指定的keyName
	 * @param keyName
	 * @return
	 */
	public AttachmentItemJSONHelper replaceKeyForUpdateRecords(String keyName){
		this.enableNameFilter = true;
		this.replaceMap.put("updateRecords", keyName);
		FastJSONUtils.addNameFilterToJsonHelper(nameFilters, AttachmentItem.class, "updateRecords", keyName);
		return this;
	}
	
	
	//---- valueFilters ----//
	
	/**
	 * 序列化json时候， 替换 itemId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterItemId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Long.class, filterValue, "itemId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 mail 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterMail(FilterValue<Mail> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Mail.class, filterValue, "mail");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 bulkId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterBulkId(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "bulkId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 userId 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterUserId(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Long.class, filterValue, "userId");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 creator 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterCreator(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "creator");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 createTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterCreateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Date.class, filterValue, "createTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 fileName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterFileName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "fileName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 fileCategory 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterFileCategory(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "fileCategory");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 saveName 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterSaveName(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "saveName");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 urlPath 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterUrlPath(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "urlPath");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 attached 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterAttached(FilterValue<Boolean> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Boolean.class, filterValue, "attached");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 state 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterState(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Integer.class, filterValue, "state");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 itemSize 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterItemSize(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "itemSize");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 itemNeid 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterItemNeid(FilterValue<Long> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Long.class, filterValue, "itemNeid");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 itemRev 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterItemRev(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "itemRev");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 itemDifferentiate 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterItemDifferentiate(FilterValue<Integer> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Integer.class, filterValue, "itemDifferentiate");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 localhostUrlPath 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterLocalhostUrlPath(FilterValue<String> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, String.class, filterValue, "localhostUrlPath");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 updateTime 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterUpdateTime(FilterValue<Date> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, Date.class, filterValue, "updateTime");
		return this;
	}
	
	/**
	 * 序列化json时候， 替换 updateRecords 的值为其他值
	 * @param filterValue
	 * @return
	 */
	
	public AttachmentItemJSONHelper filterUpdateRecords(FilterValue<UpdateRecord> filterValue){
		this.enableValueFilter = true;
		filterValue.setFieldReplaceMap(this.replaceMap);
		FastJSONUtils.addValueFilterToJsonHelper(valueFilters, AttachmentItem.class, UpdateRecord.class, filterValue, "updateRecords");
		return this;
	}
	
}
