package com.sunnsoft.sloa.helper;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.gteam.constants.SystemConstants;
import org.gteam.service.IService;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("unchecked")
public class UserMssageHelper extends HelperAbstract<UserMssage>{

	UserMssageService service;
	
	private boolean distinctRoot;
	
	protected int pageSize = 25;
	
	protected boolean setLimit;
	
	protected boolean setStart;
	
	protected int limit;
	
	protected int start;
	
	protected Set aliases = new HashSet();
	
	protected UserMssageJSONHelper jsonHelper;
	
	/**
	 * pdm定义的 属性名 和 字段中文名的Map
	 */
	private static Map<String,String> titles = new LinkedHashMap<String,String>();
	
	static{
		titles.put("userMssageId", "用户信息id");
		titles.put("userId", "用户ID");
		titles.put("workCode", "用户编号");
		titles.put("lastName", "用户姓名");
		titles.put("loginId", "用户登录名");
		titles.put("deptFullname", "部门全称");
		titles.put("fullName", "分部全称");
		titles.put("departmentId", "用户部门ID");
		titles.put("subcompanyId1", "用户分部ID");
		titles.put("status", "用户状态");
		titles.put("dsporder", "显示顺序");
		titles.put("boxSession", "网盘session");
		titles.put("createTime", "创建时间");
		titles.put("updateTime", "更新时间");
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
	public UserMssageService service(){
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
	public UserMssageHelper ignoreEmptyValueCondiction(){
		this.ignoreEmpty = true;
		return this;
	}
	
	public UserMssageHelper(UserMssageService service,String alias) {
		super();
		this.service = service;
		this.alias = alias.toString();
		if(StringUtils.isEmpty(this.alias)){
			this.dc = service.createCriteria();
		}
	}
	
	public UserMssageHelper(UserMssageService service) {
		this(service,"");
	}
	
	/**
	 * 设置本对象的pageSize，设置后，则不使用默认pageSize
	 * @param pageSize
	 * @return
	 */
	public UserMssageHelper setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 新建一个干净的helper类。
	 * @return
	 */
	public UserMssageHelper newOne(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以newDc!");
		}
		return new UserMssageHelper(service,"");
	}
	/**
	 * 为Helper类注入一个附属dc
	 * @return
	 */
	public UserMssageHelper setDc(DetachedCriteria dc){
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
	public UserMssageHelper distinctEntity(){
		BaseDAO.distinctRootWithPk(dc,"userMssageId");
		this.distinctRoot = true;
		return this;
	}
	
	/**
	 * excel工具类
	 * @return
	 */
	public UserMssageXls xls(){
		return new UserMssageXls(this);
	}
	
	/**
	 * JSON工具类
	 * @return
	 */
	public UserMssageJSONHelper json(){
		if(jsonHelper != null) {
			return jsonHelper;
		}
		return new UserMssageJSONHelper(this);
	}
	
	/**
	 * Bean工具类,可以直接创建实体类并操作，也可以直接操作查询到的实体类，
	 * 并可以调用update/insert方法更新到数据库，工具类是链式调用的，可简化数据库操作。
	 * @return
	 */
	public UserMssageBean bean(){
		if(ignoreEmpty){
			throw new HelperException("ignoreEmptyValueCondiction情况下，不允许使用Bean工具类，避免条件判断失误导致系统损失!");
		}
		return new UserMssageBean(this);
	}
	
	/**
	 * 删除符合条件的对象,并返回被删除的结果集
	 * @return
	 */
	public List<UserMssage> delete(){
		List<UserMssage> deleteList = list();
		service.deleteList(deleteList);
		return deleteList;
	}
	
	@Override
	public List<UserMssage> list(){
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
	public PageList<UserMssage> listPage(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll<UserMssage> listPageScroll(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public PageList<UserMssage> listPage(int page){
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
	public UserMssage uniqueResult(){
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
	public List<UserMssage> each(final Each<UserMssage> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用each!");
		}
		return (List<UserMssage>)this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				List<UserMssage> list = list();
				for (Iterator<UserMssage> iterator = list.iterator(); iterator.hasNext();) {
					UserMssage bean = iterator.next();
					UserMssage oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				return list;
			}
			
		});
	}
	
	@Override
	public long scrollResult(final ScrollEach<UserMssage> each){
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
					UserMssage bean = (UserMssage)results.get(0);
					UserMssage oldBean = beforeEachUpdate(bean);
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
	public PageList<UserMssage> eachInPage(final int page,final Each<UserMssage> each){
		return eachInPage(page,pageSize,each);
	}
	
	@Override
	public PageScroll<UserMssage> eachInPageScroll(final int page,final Each<UserMssage> each){
		return eachInPageScroll(page,pageSize,each);
	}
	
	@Override
	public PageList<UserMssage> eachInPage(final int page,final int pageSize,final Each<UserMssage> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageList<UserMssage>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageList<UserMssage> pageList = listPage(page,pageSize);
				List<UserMssage> list = pageList.getList();
				for (Iterator<UserMssage> iterator = list.iterator(); iterator.hasNext();) {
					UserMssage bean = iterator.next();
					UserMssage oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageList;
			}
			
		});
	}
	
	@Override
	public PageScroll<UserMssage> eachInPageScroll(final int page,final int pageSize,final Each<UserMssage> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageScroll<UserMssage>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageScroll<UserMssage> pageScroll = listPageScroll(page,pageSize);
				List<UserMssage> list = pageScroll.getList();
				for (Iterator<UserMssage> iterator = list.iterator(); iterator.hasNext();) {
					UserMssage bean = iterator.next();
					UserMssage oldBean = beforeEachUpdate(bean);
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
	public UserMssageHelper startAnd(){
		Junction junction = Restrictions.conjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * and关系的括号结束
	 * @return
	 */
	public UserMssageHelper stopAnd(){
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
	public UserMssageHelper startOr(){
		Junction junction = Restrictions.disjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * or关系的括号结束
	 * @return
	 */
	public UserMssageHelper stopOr(){
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
	public UserMssageHelper limit(int limit){
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
	public UserMssageHelper limit(int start ,int limit){
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
	*Name:用户信息id
	*Comment:用户信息ID
	*/

	public UserMssageProperty<Long> getUserMssageId(){
		return new UserMssageProperty<Long>("userMssageId",this,Long.class);
	}
	
	/**
	*Name:用户ID
	*Comment:用户ID
	*/

	public UserMssageProperty<Integer> getUserId(){
		return new UserMssageProperty<Integer>("userId",this,Integer.class);
	}
	
	
	/**
	*Name:用户编号
	*Comment:用户工作编号
	*/

	public UserMssageStringProperty getWorkCode(){
		return new UserMssageStringProperty("workCode",this);
	}
	
	
	/**
	*Name:用户姓名
	*Comment:用户的姓名
	*/

	public UserMssageStringProperty getLastName(){
		return new UserMssageStringProperty("lastName",this);
	}
	
	
	/**
	*Name:用户登录名
	*Comment:系统账号(登录名)
	*/

	public UserMssageStringProperty getLoginId(){
		return new UserMssageStringProperty("loginId",this);
	}
	
	
	/**
	*Name:部门全称
	*Comment:部门全称
	*/

	public UserMssageStringProperty getDeptFullname(){
		return new UserMssageStringProperty("deptFullname",this);
	}
	
	
	/**
	*Name:分部全称
	*Comment:分部全称
	*/

	public UserMssageStringProperty getFullName(){
		return new UserMssageStringProperty("fullName",this);
	}
	
	
	/**
	*Name:用户部门ID
	*Comment:用户部门ID
	*/

	public UserMssageStringProperty getDepartmentId(){
		return new UserMssageStringProperty("departmentId",this);
	}
	
	
	/**
	*Name:用户分部ID
	*Comment:用户分部ID
	*/

	public UserMssageStringProperty getSubcompanyId1(){
		return new UserMssageStringProperty("subcompanyId1",this);
	}
	
	
	/**
	*Name:用户状态
	*Comment:用户状态
	*/

	public UserMssageStringProperty getStatus(){
		return new UserMssageStringProperty("status",this);
	}
	
	/**
	*Name:显示顺序
	*Comment:显示顺序
	*/

	public UserMssageProperty<Float> getDsporder(){
		return new UserMssageProperty<Float>("dsporder",this,Float.class);
	}
	
	
	/**
	*Name:网盘session
	*Comment:网盘session
	*/

	public UserMssageStringProperty getBoxSession(){
		return new UserMssageStringProperty("boxSession",this);
	}
	
	
	/**
	*Name:创建时间
	*Comment:创建时间
	*/

	public UserMssageDateProperty getCreateTime(){
		return new UserMssageDateProperty("createTime",this);
	}
	
	
	/**
	*Name:更新时间
	*Comment:更新时间
	*/

	public UserMssageDateProperty getUpdateTime(){
		return new UserMssageDateProperty("updateTime",this);
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
	public UserMssageHelper inAnywhere(String value,boolean eq){
		this.startOr();
		if (eq) {
			getWorkCode().Eq(value);
			getLastName().Eq(value);
			getLoginId().Eq(value);
			getDeptFullname().Eq(value);
			getFullName().Eq(value);
			getDepartmentId().Eq(value);
			getSubcompanyId1().Eq(value);
			getStatus().Eq(value);
			getBoxSession().Eq(value);
		} else {
			getWorkCode().Like(value);
			getLastName().Like(value);
			getLoginId().Like(value);
			getDeptFullname().Like(value);
			getFullName().Like(value);
			getDepartmentId().Like(value);
			getSubcompanyId1().Like(value);
			getStatus().Like(value);
			getBoxSession().Like(value);
		}
		this.stopOr();
		return this;
	}
	
	
	private UpdateListener<UserMssage> updateListener;
	/**
	 * 监听调用each/eachInPage和scrollResult方法修改的bean。
	 * @param listener
	 * @return
	 */
	public UserMssageHelper onUpdate(UpdateListener<UserMssage> listener){
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
	public UserMssageHelper sleepInteval(int interval){
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
	public UserMssageHelper sort(String dir,String propName){
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
	
	private void afterEachUpdate(UserMssage bean, UserMssage oldBean) {
		if(updateListener != null){
			updateListener.each(oldBean, bean);
		}
	}
	
	private UserMssage beforeEachUpdate(UserMssage bean) {
		UserMssage oldBean = null;
		if(updateListener != null){
			oldBean = new UserMssage();
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
