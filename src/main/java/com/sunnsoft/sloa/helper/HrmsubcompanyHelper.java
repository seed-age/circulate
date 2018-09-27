package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.Hrmsubcompany;
import com.sunnsoft.sloa.service.HrmsubcompanyService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.gteam.constants.SystemConstants;
import org.gteam.db.dao.BaseDAO;
import org.gteam.db.dao.PageList;
import org.gteam.db.dao.PageScroll;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.db.helper.hibernate.*;
import org.gteam.service.IService;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("unchecked")
public class HrmsubcompanyHelper extends HelperAbstract<Hrmsubcompany>{

	HrmsubcompanyService service;
	
	private boolean distinctRoot;
	
	protected int pageSize = 25;
	
	protected boolean setLimit;
	
	protected boolean setStart;
	
	protected int limit;
	
	protected int start;
	
	protected Set aliases = new HashSet();
	
	protected HrmsubcompanyJSONHelper jsonHelper;
	
	/**
	 * pdm定义的 属性名 和 字段中文名的Map
	 */
	private static Map<String,String> titles = new LinkedHashMap<String,String>();
	
	static{
		titles.put("id", "id");
		titles.put("subcompanyname", "分部简称");
		titles.put("subcompanydesc", "分部描述");
		titles.put("companyid", "所属总部ID");
		titles.put("supsubcomid", "上级分部Id");
		titles.put("url", "url");
		titles.put("showorder", "序列号");
		titles.put("canceled", "封存标识");
		titles.put("subcompanycode", "分部编码");
		titles.put("outkey", "outkey");
		titles.put("budgetatuomoveorder", "budgetatuomoveorder");
		titles.put("ecologyPinyinSearch", "拼音");
		titles.put("limitusers", "限制用户数");
		titles.put("tlevel", "等级");
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
	public HrmsubcompanyService service(){
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
	public HrmsubcompanyHelper ignoreEmptyValueCondiction(){
		this.ignoreEmpty = true;
		return this;
	}
	
	public HrmsubcompanyHelper(HrmsubcompanyService service,String alias) {
		super();
		this.service = service;
		this.alias = alias.toString();
		if(StringUtils.isEmpty(this.alias)){
			this.dc = service.createCriteria();
		}
	}
	
	public HrmsubcompanyHelper(HrmsubcompanyService service) {
		this(service,"");
	}
	
	/**
	 * 设置本对象的pageSize，设置后，则不使用默认pageSize
	 * @param pageSize
	 * @return
	 */
	public HrmsubcompanyHelper setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 新建一个干净的helper类。
	 * @return
	 */
	public HrmsubcompanyHelper newOne(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以newDc!");
		}
		return new HrmsubcompanyHelper(service,"");
	}
	/**
	 * 为Helper类注入一个附属dc
	 * @return
	 */
	public HrmsubcompanyHelper setDc(DetachedCriteria dc){
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
	public HrmsubcompanyHelper distinctEntity(){
		BaseDAO.distinctRootWithPk(dc,"id");
		this.distinctRoot = true;
		return this;
	}
	
	/**
	 * excel工具类
	 * @return
	 */
	public HrmsubcompanyXls xls(){
		return new HrmsubcompanyXls(this);
	}
	
	/**
	 * JSON工具类
	 * @return
	 */
	public HrmsubcompanyJSONHelper json(){
		if(jsonHelper != null) {
			return jsonHelper;
		}
		return new HrmsubcompanyJSONHelper(this);
	}
	
	/**
	 * Bean工具类,可以直接创建实体类并操作，也可以直接操作查询到的实体类，
	 * 并可以调用update/insert方法更新到数据库，工具类是链式调用的，可简化数据库操作。
	 * @return
	 */
	public HrmsubcompanyBean bean(){
		if(ignoreEmpty){
			throw new HelperException("ignoreEmptyValueCondiction情况下，不允许使用Bean工具类，避免条件判断失误导致系统损失!");
		}
		return new HrmsubcompanyBean(this);
	}
	
	/**
	 * 删除符合条件的对象,并返回被删除的结果集
	 * @return
	 */
	public List<Hrmsubcompany> delete(){
		List<Hrmsubcompany> deleteList = list();
		service.deleteList(deleteList);
		return deleteList;
	}
	
	@Override
	public List<Hrmsubcompany> list(){
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
	public PageList<Hrmsubcompany> listPage(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll<Hrmsubcompany> listPageScroll(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public PageList<Hrmsubcompany> listPage(int page){
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
	public Hrmsubcompany uniqueResult(){
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
	public List<Hrmsubcompany> each(final Each<Hrmsubcompany> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用each!");
		}
		return (List<Hrmsubcompany>)this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				List<Hrmsubcompany> list = list();
				for (Iterator<Hrmsubcompany> iterator = list.iterator(); iterator.hasNext();) {
					Hrmsubcompany bean = iterator.next();
					Hrmsubcompany oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				return list;
			}
			
		});
	}
	
	@Override
	public long scrollResult(final ScrollEach<Hrmsubcompany> each){
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
					Hrmsubcompany bean = (Hrmsubcompany)results.get(0);
					Hrmsubcompany oldBean = beforeEachUpdate(bean);
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
	public PageList<Hrmsubcompany> eachInPage(final int page,final Each<Hrmsubcompany> each){
		return eachInPage(page,pageSize,each);
	}
	
	@Override
	public PageScroll<Hrmsubcompany> eachInPageScroll(final int page,final Each<Hrmsubcompany> each){
		return eachInPageScroll(page,pageSize,each);
	}
	
	@Override
	public PageList<Hrmsubcompany> eachInPage(final int page,final int pageSize,final Each<Hrmsubcompany> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageList<Hrmsubcompany>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageList<Hrmsubcompany> pageList = listPage(page,pageSize);
				List<Hrmsubcompany> list = pageList.getList();
				for (Iterator<Hrmsubcompany> iterator = list.iterator(); iterator.hasNext();) {
					Hrmsubcompany bean = iterator.next();
					Hrmsubcompany oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageList;
			}
			
		});
	}
	
	@Override
	public PageScroll<Hrmsubcompany> eachInPageScroll(final int page,final int pageSize,final Each<Hrmsubcompany> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageScroll<Hrmsubcompany>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageScroll<Hrmsubcompany> pageScroll = listPageScroll(page,pageSize);
				List<Hrmsubcompany> list = pageScroll.getList();
				for (Iterator<Hrmsubcompany> iterator = list.iterator(); iterator.hasNext();) {
					Hrmsubcompany bean = iterator.next();
					Hrmsubcompany oldBean = beforeEachUpdate(bean);
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
	public HrmsubcompanyHelper startAnd(){
		Junction junction = Restrictions.conjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * and关系的括号结束
	 * @return
	 */
	public HrmsubcompanyHelper stopAnd(){
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
	public HrmsubcompanyHelper startOr(){
		Junction junction = Restrictions.disjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * or关系的括号结束
	 * @return
	 */
	public HrmsubcompanyHelper stopOr(){
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
	public HrmsubcompanyHelper limit(int limit){
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
	public HrmsubcompanyHelper limit(int start ,int limit){
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
	*Name:id
	*Comment:
	*/

	public HrmsubcompanyProperty<Integer> getId(){
		return new HrmsubcompanyProperty<Integer>("id",this,ClassUtils.wrapperToPrimitive(Integer.class));
	}
	
	
	/**
	*Name:分部简称
	*Comment:
	*/

	public HrmsubcompanyStringProperty getSubcompanyname(){
		return new HrmsubcompanyStringProperty("subcompanyname",this);
	}
	
	
	/**
	*Name:分部描述
	*Comment:
	*/

	public HrmsubcompanyStringProperty getSubcompanydesc(){
		return new HrmsubcompanyStringProperty("subcompanydesc",this);
	}
	
	/**
	*Name:所属总部ID
	*Comment:
	*/

	public HrmsubcompanyProperty<Integer> getCompanyid(){
		return new HrmsubcompanyProperty<Integer>("companyid",this,Integer.class);
	}
	
	/**
	*Name:上级分部Id
	*Comment:
	*/

	public HrmsubcompanyProperty<Integer> getSupsubcomid(){
		return new HrmsubcompanyProperty<Integer>("supsubcomid",this,Integer.class);
	}
	
	
	/**
	*Name:url
	*Comment:
	*/

	public HrmsubcompanyStringProperty getUrl(){
		return new HrmsubcompanyStringProperty("url",this);
	}
	
	/**
	*Name:序列号
	*Comment:
	*/

	public HrmsubcompanyProperty<Integer> getShoworder(){
		return new HrmsubcompanyProperty<Integer>("showorder",this,Integer.class);
	}
	
	/**
	*Name:封存标识
	*Comment:
	*/

	public HrmsubcompanyProperty<Character> getCanceled(){
		return new HrmsubcompanyProperty<Character>("canceled",this,Character.class);
	}
	
	
	/**
	*Name:分部编码
	*Comment:
	*/

	public HrmsubcompanyStringProperty getSubcompanycode(){
		return new HrmsubcompanyStringProperty("subcompanycode",this);
	}
	
	
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmsubcompanyStringProperty getOutkey(){
		return new HrmsubcompanyStringProperty("outkey",this);
	}
	
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/

	public HrmsubcompanyProperty<Integer> getBudgetatuomoveorder(){
		return new HrmsubcompanyProperty<Integer>("budgetatuomoveorder",this,Integer.class);
	}
	
	
	/**
	*Name:拼音
	*Comment:
	*/

	public HrmsubcompanyStringProperty getEcologyPinyinSearch(){
		return new HrmsubcompanyStringProperty("ecologyPinyinSearch",this);
	}
	
	/**
	*Name:限制用户数
	*Comment:
	*/

	public HrmsubcompanyProperty<Integer> getLimitusers(){
		return new HrmsubcompanyProperty<Integer>("limitusers",this,Integer.class);
	}
	
	/**
	*Name:等级
	*Comment:
	*/

	public HrmsubcompanyProperty<Integer> getTlevel(){
		return new HrmsubcompanyProperty<Integer>("tlevel",this,Integer.class);
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
	public HrmsubcompanyHelper inAnywhere(String value,boolean eq){
		this.startOr();
		if (eq) {
			getSubcompanyname().Eq(value);
			getSubcompanydesc().Eq(value);
			getUrl().Eq(value);
			getSubcompanycode().Eq(value);
			getOutkey().Eq(value);
			getEcologyPinyinSearch().Eq(value);
		} else {
			getSubcompanyname().Like(value);
			getSubcompanydesc().Like(value);
			getUrl().Like(value);
			getSubcompanycode().Like(value);
			getOutkey().Like(value);
			getEcologyPinyinSearch().Like(value);
		}
		this.stopOr();
		return this;
	}
	
	
	private UpdateListener<Hrmsubcompany> updateListener;
	/**
	 * 监听调用each/eachInPage和scrollResult方法修改的bean。
	 * @param listener
	 * @return
	 */
	public HrmsubcompanyHelper onUpdate(UpdateListener<Hrmsubcompany> listener){
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
	public HrmsubcompanyHelper sleepInteval(int interval){
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
	public HrmsubcompanyHelper sort(String dir,String propName){
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
	
	private void afterEachUpdate(Hrmsubcompany bean, Hrmsubcompany oldBean) {
		if(updateListener != null){
			updateListener.each(oldBean, bean);
		}
	}
	
	private Hrmsubcompany beforeEachUpdate(Hrmsubcompany bean) {
		Hrmsubcompany oldBean = null;
		if(updateListener != null){
			oldBean = new Hrmsubcompany();
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
