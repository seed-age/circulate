package com.sunnsoft.sloa.helper;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.HibernateCallback;

import org.gteam.constants.SystemConstants;
import org.gteam.db.dao.*;
import org.gteam.db.helper.hibernate.*;
import com.sunnsoft.sloa.db.vo.*;
import com.sunnsoft.sloa.service.*;
import org.gteam.service.IService;

@SuppressWarnings("unchecked")
public class SystemLogHelper extends HelperAbstract<SystemLog>{

	SystemLogService service;
	
	private boolean distinctRoot;
	
	protected int pageSize = 25;
	
	protected boolean setLimit;
	
	protected boolean setStart;
	
	protected int limit;
	
	protected int start;
	
	protected Set aliases = new HashSet();
	
	protected SystemLogJSONHelper jsonHelper;
	
	/**
	 * pdm定义的 属性名 和 字段中文名的Map
	 */
	private static Map<String,String> titles = new LinkedHashMap<String,String>();
	
	static{
		titles.put("logId", "ID");
		titles.put("operator", "操作人");
		titles.put("logTime", "日志时间");
		titles.put("action", "操作");
		titles.put("ip", "ip地址");
		titles.put("identityInfo", "身份信息");
		Collections.unmodifiableMap(titles);
	}
	
	/**
	 * 返回pdm中的字段对应中文名的map
	 * @return
	 */
	public Map<String,String> titlesMap(){
		return titles;
	}
	
	/**
	 * 返回 所使用的service对象。
	 * @return
	 */
	public SystemLogService service(){
		return service;
	}
	
	/**
	 * 别称，当有别称的时候，helper类的查询方法和更新方法，全部不可调用。别称一般是上级的别称+“_”+本级的属性名
	 */
	String alias="";
	
	/**
	 * 括号对象的栈，其他helper类可以设值
	 */
	Stack<Junction> junctions = new Stack<Junction>();//需要传递给别称helper
	/**
	 * Helper类的栈，其他helper类可以设值
	 */
	Stack<HelperAbstract> helpers = new Stack<HelperAbstract>();//需要传递给别称helper
	/**
	 * 设置条件的时候，空对象条件不设置，类似于findByExample;
	 */
	boolean ignoreEmpty;
	/**
	 * 开启"空对象不设置条件",相当于伪代码:
	 * if(value != null){
	 * 		addCondiction(value);
	 * }else{
	 * 		doNotAddCondition(value);
	 * }
	 * @return
	 */
	public SystemLogHelper ignoreEmptyValueCondiction(){
		this.ignoreEmpty = true;
		return this;
	}
	
	public SystemLogHelper(SystemLogService service,String alias) {
		super();
		this.service = service;
		this.alias = alias.toString();
		if(StringUtils.isEmpty(this.alias)){
			this.dc = service.createCriteria();
		}
	}
	
	public SystemLogHelper(SystemLogService service) {
		this(service,"");
	}
	
	/**
	 * 设置本对象的pageSize，设置后，则不使用默认pageSize
	 * @param pageSize
	 * @return
	 */
	public SystemLogHelper setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 新建一个干净的helper类。
	 * @return
	 */
	public SystemLogHelper newOne(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以newDc!");
		}
		return new SystemLogHelper(service,"");
	}
	/**
	 * 为Helper类注入一个附属dc
	 * @return
	 */
	public SystemLogHelper setDc(DetachedCriteria dc){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以setDc!");
		}
		this.dc = dc;
		return this;
	}
	
	/**
	 * 当返回的实体，有多个重复的时候，调用本方法，可以distinct约束实体。
	 * @return
	 */
	public SystemLogHelper distinctEntity(){
		BaseDAO.distinctRootWithPk(dc,"logId");
		this.distinctRoot = true;
		return this;
	}
	
	/**
	 * excel工具类
	 * @return
	 */
	public SystemLogXls xls(){
		return new SystemLogXls(this);
	}
	
	/**
	 * JSON工具类
	 * @return
	 */
	public SystemLogJSONHelper json(){
		if(jsonHelper != null) {
			return jsonHelper;
		}
		return new SystemLogJSONHelper(this);
	}
	
	/**
	 * Bean工具类,可以直接创建实体类并操作，也可以直接操作查询到的实体类，
	 * 并可以调用update/insert方法更新到数据库，工具类是链式调用的，可简化数据库操作。
	 * @return
	 */
	public SystemLogBean bean(){
		if(ignoreEmpty){
			throw new HelperException("ignoreEmptyValueCondiction情况下，不允许使用Bean工具类，避免条件判断失误导致系统损失!");
		}
		return new SystemLogBean(this);
	}
	
	/**
	 * 删除符合条件的对象,并返回被删除的结果集
	 * @return
	 */
	public List<SystemLog> delete(){
		List<SystemLog> deleteList = list();
		service.deleteList(deleteList);
		return deleteList;
	}
	
	@Override
	public List<SystemLog> list(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用list!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		if(setLimit){
			if(setStart){
				return service.findWithCriteriaByLimit(dc, start, limit);
			}else{
				return service.findWithCriteriaByLimit(dc, 0, limit);
			}
		}
		return service.findByDetachedCriteria(dc);
	}
	
	@Override
	public long rowCount(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用rowCount!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.getRowCountByDc(dc);
	}
	
	@Override
	public PageList<SystemLog> listPage(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll<SystemLog> listPageScroll(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public PageList<SystemLog> listPage(int page){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll listPageScroll(int page){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public SystemLog uniqueResult(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用uniqueResult!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		if(setLimit){
			if(limit != 1){
				throw new HelperException("uniqueResult limit参数必须是1");
			}
			return service.getFirstByDetachedCriteria(dc);
		}
		return service.findUniqueByDc(dc);
	}
	
	@Override
	public List<SystemLog> each(final Each<SystemLog> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用each!");
		}
		return (List<SystemLog>)this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				List<SystemLog> list = list();
				for (Iterator<SystemLog> iterator = list.iterator(); iterator.hasNext();) {
					SystemLog bean = iterator.next();
					SystemLog oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				return list;
			}
			
		});
	}
	
	@Override
	public long scrollResult(final ScrollEach<SystemLog> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用scrollResult!");
		}
		HibernateCallback callBack = new HibernateCallback(){

			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				while(!junctions.empty()){
					dc.add(junctions.pop());
				}
				Criteria c = dc.getExecutableCriteria(session);
				if(distinctRoot){
					c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
				}
				
				if(setLimit){
					if(setStart){
						c.setFirstResult(start).setMaxResults(limit);
					}else{
						c.setFirstResult(0).setMaxResults(limit);
					}
				}
				ScrollableResults results =  c.setCacheMode(CacheMode.IGNORE).scroll(
						ScrollMode.FORWARD_ONLY);
				long count = 0L ;
				int valve = each.flushValve();
				while(results.next()){
					SystemLog bean = (SystemLog)results.get(0);
					SystemLog oldBean = beforeEachUpdate(bean);
					each.each(bean,count);
					afterEachUpdate(bean, oldBean);
					if(!each.isTransactional()){
						session.update(bean);
					}
					if (++count % (long)valve == 0L) {
						session.flush();
						session.clear();
						if(sleepInterval > 0){
							try {
								Thread.sleep(sleepInterval);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return count;
			}
			
		};
		return each.isTransactional()? (Long)this.service.execute(callBack) : (Long)this.service.nonTransactionalExecute(callBack);
		
	}
	
	@Override
	public PageList<SystemLog> eachInPage(final int page,final Each<SystemLog> each){
		return eachInPage(page,pageSize,each);
	}
	
	@Override
	public PageScroll<SystemLog> eachInPageScroll(final int page,final Each<SystemLog> each){
		return eachInPageScroll(page,pageSize,each);
	}
	
	@Override
	public PageList<SystemLog> eachInPage(final int page,final int pageSize,final Each<SystemLog> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageList<SystemLog>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageList<SystemLog> pageList = listPage(page,pageSize);
				List<SystemLog> list = pageList.getList();
				for (Iterator<SystemLog> iterator = list.iterator(); iterator.hasNext();) {
					SystemLog bean = iterator.next();
					SystemLog oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageList;
			}
			
		});
	}
	
	@Override
	public PageScroll<SystemLog> eachInPageScroll(final int page,final int pageSize,final Each<SystemLog> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageScroll<SystemLog>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageScroll<SystemLog> pageScroll = listPageScroll(page,pageSize);
				List<SystemLog> list = pageScroll.getList();
				for (Iterator<SystemLog> iterator = list.iterator(); iterator.hasNext();) {
					SystemLog bean = iterator.next();
					SystemLog oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageScroll;
			}
			
		});
	}
	
	/**
	 * 括号开始，括号内部条件是and关系
	 * @return
	 */
	public SystemLogHelper startAnd(){
		Junction junction = Restrictions.conjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * and关系的括号结束
	 * @return
	 */
	public SystemLogHelper stopAnd(){
		if(junctions.empty()){
			throw new HelperException("调用stopAnd之前，应该调用startAnd!");
		}
		Junction junction = junctions.pop();
		if(!(junction instanceof Conjunction)){
			throw new HelperException("应该调用stopOr而不是stopEnd!");
		}
		if(!junctions.empty()){
			junctions.peek().add(junction);
		}else{
			this.dc.add(junction);
		}
		return this;
	}
	/**
	 * 括号开始，括号内部条件是or关系
	 * @return
	 */
	public SystemLogHelper startOr(){
		Junction junction = Restrictions.disjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * or关系的括号结束
	 * @return
	 */
	public SystemLogHelper stopOr(){
		if(junctions.empty()){
			throw new HelperException("调用stopOr之前，应该调用startOr!");
		}
		Junction junction = junctions.pop();
		if(!(junction instanceof Disjunction)){
			throw new HelperException("应该调用stopOr而不是stopEnd!");
		}
		if(!junctions.empty()){
			junctions.peek().add(junction);
		}else{
			this.dc.add(junction);
		}
		return this;
	}
	
	
	/**
	 * 设置获取记录数
	 * @param limit
	 * @return
	 */
	public SystemLogHelper limit(int limit){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用limit!");
		}
		this.limit = limit;
		this.setLimit = true;
		return this;
	}
	/**
	 *设置从start条记录开始，获取limit条记录
	 * @param start 第一条记录index为0,以此类推
	 * @param limit
	 * @return
	 */
	public SystemLogHelper limit(int start ,int limit){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用limit!");
		}
		this.limit = limit;
		this.setLimit = true;
		this.start = start;
		this.setStart = true;
		return this;
	}
	
	//== 关联关系处理  ==//
	
	//== 对象属性处理 包括关联对象的属性的基本处理  ==//
	
	
	/**
	*Name:ID
	*Comment:
	*/

	public SystemLogProperty<Long> getLogId(){
		return new SystemLogProperty<Long>("logId",this,Long.class);
	}
	
	
	/**
	*Name:操作人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public SystemLogStringProperty getOperator(){
		return new SystemLogStringProperty("operator",this);
	}
	
	
	/**
	*Name:日志时间
	*Comment:
	*/

	public SystemLogDateProperty getLogTime(){
		return new SystemLogDateProperty("logTime",this);
	}
	
	
	/**
	*Name:操作
	*Comment:
	*/

	public SystemLogStringProperty getAction(){
		return new SystemLogStringProperty("action",this);
	}
	
	
	/**
	*Name:ip地址
	*Comment:
	*/

	public SystemLogStringProperty getIp(){
		return new SystemLogStringProperty("ip",this);
	}
	
	
	/**
	*Name:身份信息
	*Comment:可以是用户名，用户实体id，session id等描述用户的信息，以便核对。
不和用户表直接外键关联。
	*/

	public SystemLogStringProperty getIdentityInfo(){
		return new SystemLogStringProperty("identityInfo",this);
	}
	
	
	/**
	 * 设定条件：“在所有字符串属性中出现”。
	 * 如果参数eq为true
	 *   相当于伪hql语句：where stringProperty1 = 'value' or stringProperty2 = 'value' or .....
	 * 如果参数eq为 false
	 *   相当于伪hql语句：where stringProperty1 like '%value%' or stringProperty2 like '%value%' or .....
	 * 注意：如果 实体类没有一个字符串属性，该方法无效。
	 * 另外需要注意：该方法可能会暴露数据信息，请自己评估安全影响。该方法会使用like，所以会全表扫描，性能影响需要自己考虑权衡。
	 * @param value
	 * @param eq
	 * @return
	 */
	public SystemLogHelper inAnywhere(String value,boolean eq){
		this.startOr();
		if (eq) {
			getOperator().Eq(value);
			getAction().Eq(value);
			getIp().Eq(value);
			getIdentityInfo().Eq(value);
		} else {
			getOperator().Like(value);
			getAction().Like(value);
			getIp().Like(value);
			getIdentityInfo().Like(value);
		}
		this.stopOr();
		return this;
	}
	
	
	private UpdateListener<SystemLog> updateListener;
	/**
	 * 监听调用each/eachInPage和scrollResult方法修改的bean。
	 * @param listener
	 * @return
	 */
	public SystemLogHelper onUpdate(UpdateListener<SystemLog> listener){
		this.updateListener = listener;
		return this ;
	}
	
	private int sleepInterval ;
	
	/**
	 * 设置scrollResult方法每处理N(默认20)个bean 后，线程sleep的时间，避免CPU一直占用过高。
	 * 默认值是0，不进行sleep
	 * 一般给慢速而又处理大数据量的场景使用，例如半夜的定时任务。
	 * @param interval 每处理一批bean后sleep的毫秒
	 * @return
	 */
	public SystemLogHelper sleepInteval(int interval){
		this.sleepInterval = interval;
		return this;
	}
	
	/**
	 * 如果给定的dir和propName合法的话。按其排序。
	 * 如果不合法（找不到属性propName）,则按自然顺序
	 * @param dir 合法的值"DESC"或"ASC"(大小写均可)
	 * @param propName 合法的字段属性，
	 * @return
	 */
	public SystemLogHelper sort(String dir,String propName){
		if(titles.containsKey(propName)){
			boolean asc = SystemConstants.SORT_ASC.equalsIgnoreCase(dir);
			boolean desc = SystemConstants.SORT_DESC.equalsIgnoreCase(dir);
			if( asc || desc ){
				String temp = "".equals(this.alias)?"":(this.alias + ".");
				dc.addOrder(asc ? Order.asc(temp + propName):Order.desc(temp + propName));
			}
		}
		return this;
	}
	
	private void afterEachUpdate(SystemLog bean, SystemLog oldBean) {
		if(updateListener != null){
			updateListener.each(oldBean, bean);
		}
	}
	
	private SystemLog beforeEachUpdate(SystemLog bean) {
		SystemLog oldBean = null;
		if(updateListener != null){
			oldBean = new SystemLog();
			try {
				PropertyUtils.copyProperties(oldBean, bean);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return oldBean;
	}
	
}