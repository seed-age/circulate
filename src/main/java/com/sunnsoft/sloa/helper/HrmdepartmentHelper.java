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
import org.apache.commons.lang.ClassUtils;
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
public class HrmdepartmentHelper extends HelperAbstract<Hrmdepartment>{

	HrmdepartmentService service;
	
	private boolean distinctRoot;
	
	protected int pageSize = 25;
	
	protected boolean setLimit;
	
	protected boolean setStart;
	
	protected int limit;
	
	protected int start;
	
	protected Set aliases = new HashSet();
	
	protected HrmdepartmentJSONHelper jsonHelper;
	
	/**
	 * pdm定义的 属性名 和 字段中文名的Map
	 */
	private static Map<String,String> titles = new LinkedHashMap<String,String>();
	
	static{
		titles.put("id", "id");
		titles.put("departmentmark", "部门标识");
		titles.put("departmentname", "部门名称");
		titles.put("subcompanyid1", "所属分部1");
		titles.put("supdepid", "上级部门id");
		titles.put("allsupdepid", "所有上级部门id");
		titles.put("showorder", "显示顺序");
		titles.put("canceled", "封存标识");
		titles.put("departmentcode", "部门编码");
		titles.put("coadjutant", "协办人");
		titles.put("zzjgbmfzr", "组织架构部门负责人");
		titles.put("zzjgbmfgld", "组织架构部门分管领导");
		titles.put("jzglbmfzr", "矩阵管理部门负责人");
		titles.put("jzglbmfgld", "矩阵管理部门分管领导");
		titles.put("bmfzr", "矩阵部门负责人员");
		titles.put("bmfgld", "矩阵部门分管领导");
		titles.put("outkey", "outkey");
		titles.put("budgetatuomoveorder", "budgetatuomoveorder");
		titles.put("ecologyPinyinSearch", "ecology_pinyin_search");
		titles.put("tlevel", "tlevel");
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
	public HrmdepartmentService service(){
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
	public HrmdepartmentHelper ignoreEmptyValueCondiction(){
		this.ignoreEmpty = true;
		return this;
	}
	
	public HrmdepartmentHelper(HrmdepartmentService service,String alias) {
		super();
		this.service = service;
		this.alias = alias.toString();
		if(StringUtils.isEmpty(this.alias)){
			this.dc = service.createCriteria();
		}
	}
	
	public HrmdepartmentHelper(HrmdepartmentService service) {
		this(service,"");
	}
	
	/**
	 * 设置本对象的pageSize，设置后，则不使用默认pageSize
	 * @param pageSize
	 * @return
	 */
	public HrmdepartmentHelper setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 新建一个干净的helper类。
	 * @return
	 */
	public HrmdepartmentHelper newOne(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以newDc!");
		}
		return new HrmdepartmentHelper(service,"");
	}
	/**
	 * 为Helper类注入一个附属dc
	 * @return
	 */
	public HrmdepartmentHelper setDc(DetachedCriteria dc){
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
	public HrmdepartmentHelper distinctEntity(){
		BaseDAO.distinctRootWithPk(dc,"id");
		this.distinctRoot = true;
		return this;
	}
	
	/**
	 * excel工具类
	 * @return
	 */
	public HrmdepartmentXls xls(){
		return new HrmdepartmentXls(this);
	}
	
	/**
	 * JSON工具类
	 * @return
	 */
	public HrmdepartmentJSONHelper json(){
		if(jsonHelper != null) {
			return jsonHelper;
		}
		return new HrmdepartmentJSONHelper(this);
	}
	
	/**
	 * Bean工具类,可以直接创建实体类并操作，也可以直接操作查询到的实体类，
	 * 并可以调用update/insert方法更新到数据库，工具类是链式调用的，可简化数据库操作。
	 * @return
	 */
	public HrmdepartmentBean bean(){
		if(ignoreEmpty){
			throw new HelperException("ignoreEmptyValueCondiction情况下，不允许使用Bean工具类，避免条件判断失误导致系统损失!");
		}
		return new HrmdepartmentBean(this);
	}
	
	/**
	 * 删除符合条件的对象,并返回被删除的结果集
	 * @return
	 */
	public List<Hrmdepartment> delete(){
		List<Hrmdepartment> deleteList = list();
		service.deleteList(deleteList);
		return deleteList;
	}
	
	@Override
	public List<Hrmdepartment> list(){
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
	public PageList<Hrmdepartment> listPage(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll<Hrmdepartment> listPageScroll(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public PageList<Hrmdepartment> listPage(int page){
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
	public Hrmdepartment uniqueResult(){
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
	public List<Hrmdepartment> each(final Each<Hrmdepartment> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用each!");
		}
		return (List<Hrmdepartment>)this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				List<Hrmdepartment> list = list();
				for (Iterator<Hrmdepartment> iterator = list.iterator(); iterator.hasNext();) {
					Hrmdepartment bean = iterator.next();
					Hrmdepartment oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				return list;
			}
			
		});
	}
	
	@Override
	public long scrollResult(final ScrollEach<Hrmdepartment> each){
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
					Hrmdepartment bean = (Hrmdepartment)results.get(0);
					Hrmdepartment oldBean = beforeEachUpdate(bean);
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
	public PageList<Hrmdepartment> eachInPage(final int page,final Each<Hrmdepartment> each){
		return eachInPage(page,pageSize,each);
	}
	
	@Override
	public PageScroll<Hrmdepartment> eachInPageScroll(final int page,final Each<Hrmdepartment> each){
		return eachInPageScroll(page,pageSize,each);
	}
	
	@Override
	public PageList<Hrmdepartment> eachInPage(final int page,final int pageSize,final Each<Hrmdepartment> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageList<Hrmdepartment>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageList<Hrmdepartment> pageList = listPage(page,pageSize);
				List<Hrmdepartment> list = pageList.getList();
				for (Iterator<Hrmdepartment> iterator = list.iterator(); iterator.hasNext();) {
					Hrmdepartment bean = iterator.next();
					Hrmdepartment oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageList;
			}
			
		});
	}
	
	@Override
	public PageScroll<Hrmdepartment> eachInPageScroll(final int page,final int pageSize,final Each<Hrmdepartment> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageScroll<Hrmdepartment>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageScroll<Hrmdepartment> pageScroll = listPageScroll(page,pageSize);
				List<Hrmdepartment> list = pageScroll.getList();
				for (Iterator<Hrmdepartment> iterator = list.iterator(); iterator.hasNext();) {
					Hrmdepartment bean = iterator.next();
					Hrmdepartment oldBean = beforeEachUpdate(bean);
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
	public HrmdepartmentHelper startAnd(){
		Junction junction = Restrictions.conjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * and关系的括号结束
	 * @return
	 */
	public HrmdepartmentHelper stopAnd(){
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
	public HrmdepartmentHelper startOr(){
		Junction junction = Restrictions.disjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * or关系的括号结束
	 * @return
	 */
	public HrmdepartmentHelper stopOr(){
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
	public HrmdepartmentHelper limit(int limit){
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
	public HrmdepartmentHelper limit(int start ,int limit){
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

	public HrmdepartmentProperty<Integer> getId(){
		return new HrmdepartmentProperty<Integer>("id",this,ClassUtils.wrapperToPrimitive(Integer.class));
	}
	
	
	/**
	*Name:部门标识
	*Comment:
	*/

	public HrmdepartmentStringProperty getDepartmentmark(){
		return new HrmdepartmentStringProperty("departmentmark",this);
	}
	
	
	/**
	*Name:部门名称
	*Comment:
	*/

	public HrmdepartmentStringProperty getDepartmentname(){
		return new HrmdepartmentStringProperty("departmentname",this);
	}
	
	/**
	*Name:所属分部1
	*Comment:
	*/

	public HrmdepartmentProperty<Integer> getSubcompanyid1(){
		return new HrmdepartmentProperty<Integer>("subcompanyid1",this,Integer.class);
	}
	
	/**
	*Name:上级部门id
	*Comment:
	*/

	public HrmdepartmentProperty<Integer> getSupdepid(){
		return new HrmdepartmentProperty<Integer>("supdepid",this,Integer.class);
	}
	
	
	/**
	*Name:所有上级部门id
	*Comment:
	*/

	public HrmdepartmentStringProperty getAllsupdepid(){
		return new HrmdepartmentStringProperty("allsupdepid",this);
	}
	
	/**
	*Name:显示顺序
	*Comment:
	*/

	public HrmdepartmentProperty<Integer> getShoworder(){
		return new HrmdepartmentProperty<Integer>("showorder",this,Integer.class);
	}
	
	/**
	*Name:封存标识
	*Comment:
	*/

	public HrmdepartmentProperty<Character> getCanceled(){
		return new HrmdepartmentProperty<Character>("canceled",this,Character.class);
	}
	
	
	/**
	*Name:部门编码
	*Comment:
	*/

	public HrmdepartmentStringProperty getDepartmentcode(){
		return new HrmdepartmentStringProperty("departmentcode",this);
	}
	
	/**
	*Name:协办人
	*Comment:
	*/

	public HrmdepartmentProperty<Integer> getCoadjutant(){
		return new HrmdepartmentProperty<Integer>("coadjutant",this,Integer.class);
	}
	
	
	/**
	*Name:组织架构部门负责人
	*Comment:
	*/

	public HrmdepartmentStringProperty getZzjgbmfzr(){
		return new HrmdepartmentStringProperty("zzjgbmfzr",this);
	}
	
	
	/**
	*Name:组织架构部门分管领导
	*Comment:
	*/

	public HrmdepartmentStringProperty getZzjgbmfgld(){
		return new HrmdepartmentStringProperty("zzjgbmfgld",this);
	}
	
	
	/**
	*Name:矩阵管理部门负责人
	*Comment:
	*/

	public HrmdepartmentStringProperty getJzglbmfzr(){
		return new HrmdepartmentStringProperty("jzglbmfzr",this);
	}
	
	
	/**
	*Name:矩阵管理部门分管领导
	*Comment:
	*/

	public HrmdepartmentStringProperty getJzglbmfgld(){
		return new HrmdepartmentStringProperty("jzglbmfgld",this);
	}
	
	
	/**
	*Name:矩阵部门负责人员
	*Comment:
	*/

	public HrmdepartmentStringProperty getBmfzr(){
		return new HrmdepartmentStringProperty("bmfzr",this);
	}
	
	
	/**
	*Name:矩阵部门分管领导
	*Comment:
	*/

	public HrmdepartmentStringProperty getBmfgld(){
		return new HrmdepartmentStringProperty("bmfgld",this);
	}
	
	
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmdepartmentStringProperty getOutkey(){
		return new HrmdepartmentStringProperty("outkey",this);
	}
	
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/

	public HrmdepartmentProperty<Integer> getBudgetatuomoveorder(){
		return new HrmdepartmentProperty<Integer>("budgetatuomoveorder",this,Integer.class);
	}
	
	
	/**
	*Name:ecology_pinyin_search
	*Comment:
	*/

	public HrmdepartmentStringProperty getEcologyPinyinSearch(){
		return new HrmdepartmentStringProperty("ecologyPinyinSearch",this);
	}
	
	/**
	*Name:tlevel
	*Comment:
	*/

	public HrmdepartmentProperty<Integer> getTlevel(){
		return new HrmdepartmentProperty<Integer>("tlevel",this,Integer.class);
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
	public HrmdepartmentHelper inAnywhere(String value,boolean eq){
		this.startOr();
		if (eq) {
			getDepartmentmark().Eq(value);
			getDepartmentname().Eq(value);
			getAllsupdepid().Eq(value);
			getDepartmentcode().Eq(value);
			getZzjgbmfzr().Eq(value);
			getZzjgbmfgld().Eq(value);
			getJzglbmfzr().Eq(value);
			getJzglbmfgld().Eq(value);
			getBmfzr().Eq(value);
			getBmfgld().Eq(value);
			getOutkey().Eq(value);
			getEcologyPinyinSearch().Eq(value);
		} else {
			getDepartmentmark().Like(value);
			getDepartmentname().Like(value);
			getAllsupdepid().Like(value);
			getDepartmentcode().Like(value);
			getZzjgbmfzr().Like(value);
			getZzjgbmfgld().Like(value);
			getJzglbmfzr().Like(value);
			getJzglbmfgld().Like(value);
			getBmfzr().Like(value);
			getBmfgld().Like(value);
			getOutkey().Like(value);
			getEcologyPinyinSearch().Like(value);
		}
		this.stopOr();
		return this;
	}
	
	
	private UpdateListener<Hrmdepartment> updateListener;
	/**
	 * 监听调用each/eachInPage和scrollResult方法修改的bean。
	 * @param listener
	 * @return
	 */
	public HrmdepartmentHelper onUpdate(UpdateListener<Hrmdepartment> listener){
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
	public HrmdepartmentHelper sleepInteval(int interval){
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
	public HrmdepartmentHelper sort(String dir,String propName){
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
	
	private void afterEachUpdate(Hrmdepartment bean, Hrmdepartment oldBean) {
		if(updateListener != null){
			updateListener.each(oldBean, bean);
		}
	}
	
	private Hrmdepartment beforeEachUpdate(Hrmdepartment bean) {
		Hrmdepartment oldBean = null;
		if(updateListener != null){
			oldBean = new Hrmdepartment();
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
