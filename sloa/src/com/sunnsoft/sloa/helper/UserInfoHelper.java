package com.sunnsoft.sloa.helper;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.gteam.constants.SystemConstants;
import org.gteam.service.IService;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("unchecked")
public class UserInfoHelper extends HelperAbstract<UserInfo>{

	UserInfoService service;
	
	private boolean distinctRoot;
	
	protected int pageSize = 25;
	
	protected boolean setLimit;
	
	protected boolean setStart;
	
	protected int limit;
	
	protected int start;
	
	protected Set aliases = new HashSet();
	
	protected UserInfoJSONHelper jsonHelper;
	
	/**
	 * pdm定义的 属性名 和 字段中文名的Map
	 */
	private static Map<String,String> titles = new LinkedHashMap<String,String>();
	
	static{
		titles.put("userId", "用户ID");
		titles.put("lastName", "姓");
		titles.put("firstName", "名");
		titles.put("userName", "姓名");
		titles.put("email", "电子邮件");
		titles.put("phoneNumber", "联系电话");
		titles.put("remark", "备注");
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
	public UserInfoService service(){
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
	public UserInfoHelper ignoreEmptyValueCondiction(){
		this.ignoreEmpty = true;
		return this;
	}
	
	public UserInfoHelper(UserInfoService service,String alias) {
		super();
		this.service = service;
		this.alias = alias.toString();
		if(StringUtils.isEmpty(this.alias)){
			this.dc = service.createCriteria();
		}
	}
	
	public UserInfoHelper(UserInfoService service) {
		this(service,"");
	}
	
	/**
	 * 设置本对象的pageSize，设置后，则不使用默认pageSize
	 * @param pageSize
	 * @return
	 */
	public UserInfoHelper setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 新建一个干净的helper类。
	 * @return
	 */
	public UserInfoHelper newOne(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以newDc!");
		}
		return new UserInfoHelper(service,"");
	}
	/**
	 * 为Helper类注入一个附属dc
	 * @return
	 */
	public UserInfoHelper setDc(DetachedCriteria dc){
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
	public UserInfoHelper distinctEntity(){
		BaseDAO.distinctRootWithPk(dc,"userId");
		this.distinctRoot = true;
		return this;
	}
	
	/**
	 * excel工具类
	 * @return
	 */
	public UserInfoXls xls(){
		return new UserInfoXls(this);
	}
	
	/**
	 * JSON工具类
	 * @return
	 */
	public UserInfoJSONHelper json(){
		if(jsonHelper != null) {
			return jsonHelper;
		}
		return new UserInfoJSONHelper(this);
	}
	
	/**
	 * Bean工具类,可以直接创建实体类并操作，也可以直接操作查询到的实体类，
	 * 并可以调用update/insert方法更新到数据库，工具类是链式调用的，可简化数据库操作。
	 * @return
	 */
	public UserInfoBean bean(){
		if(ignoreEmpty){
			throw new HelperException("ignoreEmptyValueCondiction情况下，不允许使用Bean工具类，避免条件判断失误导致系统损失!");
		}
		return new UserInfoBean(this);
	}
	
	/**
	 * 删除符合条件的对象,并返回被删除的结果集
	 * @return
	 */
	public List<UserInfo> delete(){
		List<UserInfo> deleteList = list();
		service.deleteList(deleteList);
		return deleteList;
	}
	
	@Override
	public List<UserInfo> list(){
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
	public PageList<UserInfo> listPage(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll<UserInfo> listPageScroll(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public PageList<UserInfo> listPage(int page){
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
	public UserInfo uniqueResult(){
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
	public List<UserInfo> each(final Each<UserInfo> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用each!");
		}
		return (List<UserInfo>)this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				List<UserInfo> list = list();
				for (Iterator<UserInfo> iterator = list.iterator(); iterator.hasNext();) {
					UserInfo bean = iterator.next();
					UserInfo oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				return list;
			}
			
		});
	}
	
	@Override
	public long scrollResult(final ScrollEach<UserInfo> each){
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
					UserInfo bean = (UserInfo)results.get(0);
					UserInfo oldBean = beforeEachUpdate(bean);
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
	public PageList<UserInfo> eachInPage(final int page,final Each<UserInfo> each){
		return eachInPage(page,pageSize,each);
	}
	
	@Override
	public PageScroll<UserInfo> eachInPageScroll(final int page,final Each<UserInfo> each){
		return eachInPageScroll(page,pageSize,each);
	}
	
	@Override
	public PageList<UserInfo> eachInPage(final int page,final int pageSize,final Each<UserInfo> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageList<UserInfo>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageList<UserInfo> pageList = listPage(page,pageSize);
				List<UserInfo> list = pageList.getList();
				for (Iterator<UserInfo> iterator = list.iterator(); iterator.hasNext();) {
					UserInfo bean = iterator.next();
					UserInfo oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageList;
			}
			
		});
	}
	
	@Override
	public PageScroll<UserInfo> eachInPageScroll(final int page,final int pageSize,final Each<UserInfo> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageScroll<UserInfo>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageScroll<UserInfo> pageScroll = listPageScroll(page,pageSize);
				List<UserInfo> list = pageScroll.getList();
				for (Iterator<UserInfo> iterator = list.iterator(); iterator.hasNext();) {
					UserInfo bean = iterator.next();
					UserInfo oldBean = beforeEachUpdate(bean);
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
	public UserInfoHelper startAnd(){
		Junction junction = Restrictions.conjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * and关系的括号结束
	 * @return
	 */
	public UserInfoHelper stopAnd(){
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
	public UserInfoHelper startOr(){
		Junction junction = Restrictions.disjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * or关系的括号结束
	 * @return
	 */
	public UserInfoHelper stopOr(){
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
	public UserInfoHelper limit(int limit){
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
	public UserInfoHelper limit(int start ,int limit){
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
	/**
	 * inner join User ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UserHelper enterUser(){
		return this.enterUserWithJoinType(0);
	}
	
	/**
	 * left join User ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UserHelper leftJoinUser(){
		return this.enterUserWithJoinType(1);
	}
	
	/**
	 * full join User ，进入关联关系实体设置查询条件,full join 查询较少用到。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UserHelper fullJoinUser(){
		return this.enterUserWithJoinType(2);
	}
	
	/**
	 * 创建User别称user查询条件，并根据joinType参数设置jion类型。
	 * 本方法只被内部3种join类型方法调用。
	 * 
	 * @param joinType
	 * @return
	 */
	private UserHelper enterUserWithJoinType(int joinType){
		UserService userService = (UserService) this.service.getBean("userService");
		String enterAlias = this.alias+"_user";//本级别称加上"_"加上属性名来创建子别称
		boolean notExists = this.aliases.add(enterAlias);
		if(notExists){
			switch (joinType) {
			case 1:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"user", enterAlias,CriteriaSpecification.LEFT_JOIN);//以left join 方式创建别称。
				break;
				
			case 2:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"user", enterAlias,CriteriaSpecification.FULL_JOIN);//以full join 方式创建别称。
				break;
				
			default:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"user", enterAlias);//默认以inner join 方式创建别称。
				break;
			}
		}
		UserHelper h = new UserHelper(userService,enterAlias);//用别称创建别称helper
		h._injectDc(dc);//为别称helper注入本helper dc
		h.aliases = this.aliases;
		h.ignoreEmpty = this.ignoreEmpty;
		this.helpers.push(this);//把自己压入堆栈
		h.helpers = this.helpers;//把堆栈传递给别称helper
		h.junctions = this.junctions;//把堆栈传递给别称helper
		return h;
	}
	/**
	 * 从User别称user返回
	 * @return
	 */
	public UserHelper back2User(){
		if(this.helpers.empty()){
			throw new HelperException("无法回到helper类:null");
		}
		HelperAbstract<User> h =  this.helpers.pop();
		return (UserHelper)h;
	}
	
	//== 对象属性处理 包括关联对象的属性的基本处理  ==//
	
	
	
	/**
	*Name:用户ID
	*Comment:
	*/

	public UserInfoProperty<Long> getUserId(){
		return new UserInfoProperty<Long>("userId",this,ClassUtils.wrapperToPrimitive(Long.class));
	}
	
	
	
	/**
	*Name:姓
	*Comment:
	*/

	public UserInfoStringProperty getLastName(){
		return new UserInfoStringProperty("lastName",this);
	}
	
	
	/**
	*Name:名
	*Comment:
	*/

	public UserInfoStringProperty getFirstName(){
		return new UserInfoStringProperty("firstName",this);
	}
	
	
	/**
	*Name:姓名
	*Comment:
	*/

	public UserInfoStringProperty getUserName(){
		return new UserInfoStringProperty("userName",this);
	}
	
	
	/**
	*Name:电子邮件
	*Comment:
	*/

	public UserInfoStringProperty getEmail(){
		return new UserInfoStringProperty("email",this);
	}
	
	
	/**
	*Name:联系电话
	*Comment:
	*/

	public UserInfoStringProperty getPhoneNumber(){
		return new UserInfoStringProperty("phoneNumber",this);
	}
	
	
	/**
	*Name:备注
	*Comment:
	*/

	public UserInfoStringProperty getRemark(){
		return new UserInfoStringProperty("remark",this);
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
	public UserInfoHelper inAnywhere(String value,boolean eq){
		this.startOr();
		if (eq) {
			getLastName().Eq(value);
			getFirstName().Eq(value);
			getUserName().Eq(value);
			getEmail().Eq(value);
			getPhoneNumber().Eq(value);
			getRemark().Eq(value);
		} else {
			getLastName().Like(value);
			getFirstName().Like(value);
			getUserName().Like(value);
			getEmail().Like(value);
			getPhoneNumber().Like(value);
			getRemark().Like(value);
		}
		this.stopOr();
		return this;
	}
	
	
	private UpdateListener<UserInfo> updateListener;
	/**
	 * 监听调用each/eachInPage和scrollResult方法修改的bean。
	 * @param listener
	 * @return
	 */
	public UserInfoHelper onUpdate(UpdateListener<UserInfo> listener){
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
	public UserInfoHelper sleepInteval(int interval){
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
	public UserInfoHelper sort(String dir,String propName){
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
	
	private void afterEachUpdate(UserInfo bean, UserInfo oldBean) {
		if(updateListener != null){
			updateListener.each(oldBean, bean);
		}
	}
	
	private UserInfo beforeEachUpdate(UserInfo bean) {
		UserInfo oldBean = null;
		if(updateListener != null){
			oldBean = new UserInfo();
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
