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
public class ReceiveHelper extends HelperAbstract<Receive>{

	ReceiveService service;
	
	private boolean distinctRoot;
	
	protected int pageSize = 25;
	
	protected boolean setLimit;
	
	protected boolean setStart;
	
	protected int limit;
	
	protected int start;
	
	protected Set aliases = new HashSet();
	
	protected ReceiveJSONHelper jsonHelper;
	
	/**
	 * pdm定义的 属性名 和 字段中文名的Map
	 */
	private static Map<String,String> titles = new LinkedHashMap<String,String>();
	
	static{
		titles.put("receiveId", "收件id");
		titles.put("userId", "收件人id");
		titles.put("workCode", "收件人工作编号");
		titles.put("lastName", "收件人姓名");
		titles.put("loginId", "收件人登录名");
		titles.put("subcompanyName", "收件人的分部全称");
		titles.put("departmentName", "收件人的部门全称");
		titles.put("receiveTime", "接收传阅的时间");
		titles.put("joinTime", "加入时间");
		titles.put("receiveStatus", "状态");
		titles.put("mailState", "传阅筛选状态");
		titles.put("stepStatus", "传阅流程状态");
		titles.put("openTime", "开封时间");
		titles.put("ifConfirm", "是否确认");
		titles.put("affirmTime", "确认时间");
		titles.put("remark", "确认信息备注");
		titles.put("confirmRecord", "确认/标识");
		titles.put("serialNum", "序号");
		titles.put("afreshConfim", "是否重新确认");
		titles.put("acRecord", "(重新)确认/标识");
		titles.put("afreshRemark", "(重新)确认信息备注");
		titles.put("mhTime", "(重新)确认时间");
		titles.put("receiveAttention", "是否关注");
		titles.put("reDifferentiate", "区别");
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
	public ReceiveService service(){
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
	public ReceiveHelper ignoreEmptyValueCondiction(){
		this.ignoreEmpty = true;
		return this;
	}
	
	public ReceiveHelper(ReceiveService service,String alias) {
		super();
		this.service = service;
		this.alias = alias.toString();
		if(StringUtils.isEmpty(this.alias)){
			this.dc = service.createCriteria();
		}
	}
	
	public ReceiveHelper(ReceiveService service) {
		this(service,"");
	}
	
	/**
	 * 设置本对象的pageSize，设置后，则不使用默认pageSize
	 * @param pageSize
	 * @return
	 */
	public ReceiveHelper setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 新建一个干净的helper类。
	 * @return
	 */
	public ReceiveHelper newOne(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以newDc!");
		}
		return new ReceiveHelper(service,"");
	}
	/**
	 * 为Helper类注入一个附属dc
	 * @return
	 */
	public ReceiveHelper setDc(DetachedCriteria dc){
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
	public ReceiveHelper distinctEntity(){
		BaseDAO.distinctRootWithPk(dc,"receiveId");
		this.distinctRoot = true;
		return this;
	}
	
	/**
	 * excel工具类
	 * @return
	 */
	public ReceiveXls xls(){
		return new ReceiveXls(this);
	}
	
	/**
	 * JSON工具类
	 * @return
	 */
	public ReceiveJSONHelper json(){
		if(jsonHelper != null) {
			return jsonHelper;
		}
		return new ReceiveJSONHelper(this);
	}
	
	/**
	 * Bean工具类,可以直接创建实体类并操作，也可以直接操作查询到的实体类，
	 * 并可以调用update/insert方法更新到数据库，工具类是链式调用的，可简化数据库操作。
	 * @return
	 */
	public ReceiveBean bean(){
		if(ignoreEmpty){
			throw new HelperException("ignoreEmptyValueCondiction情况下，不允许使用Bean工具类，避免条件判断失误导致系统损失!");
		}
		return new ReceiveBean(this);
	}
	
	/**
	 * 删除符合条件的对象,并返回被删除的结果集
	 * @return
	 */
	public List<Receive> delete(){
		List<Receive> deleteList = list();
		service.deleteList(deleteList);
		return deleteList;
	}
	
	@Override
	public List<Receive> list(){
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
	public PageList<Receive> listPage(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll<Receive> listPageScroll(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public PageList<Receive> listPage(int page){
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
	public Receive uniqueResult(){
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
	public List<Receive> each(final Each<Receive> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用each!");
		}
		return (List<Receive>)this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				List<Receive> list = list();
				for (Iterator<Receive> iterator = list.iterator(); iterator.hasNext();) {
					Receive bean = iterator.next();
					Receive oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				return list;
			}
			
		});
	}
	
	@Override
	public long scrollResult(final ScrollEach<Receive> each){
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
					Receive bean = (Receive)results.get(0);
					Receive oldBean = beforeEachUpdate(bean);
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
	public PageList<Receive> eachInPage(final int page,final Each<Receive> each){
		return eachInPage(page,pageSize,each);
	}
	
	@Override
	public PageScroll<Receive> eachInPageScroll(final int page,final Each<Receive> each){
		return eachInPageScroll(page,pageSize,each);
	}
	
	@Override
	public PageList<Receive> eachInPage(final int page,final int pageSize,final Each<Receive> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageList<Receive>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageList<Receive> pageList = listPage(page,pageSize);
				List<Receive> list = pageList.getList();
				for (Iterator<Receive> iterator = list.iterator(); iterator.hasNext();) {
					Receive bean = iterator.next();
					Receive oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageList;
			}
			
		});
	}
	
	@Override
	public PageScroll<Receive> eachInPageScroll(final int page,final int pageSize,final Each<Receive> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageScroll<Receive>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageScroll<Receive> pageScroll = listPageScroll(page,pageSize);
				List<Receive> list = pageScroll.getList();
				for (Iterator<Receive> iterator = list.iterator(); iterator.hasNext();) {
					Receive bean = iterator.next();
					Receive oldBean = beforeEachUpdate(bean);
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
	public ReceiveHelper startAnd(){
		Junction junction = Restrictions.conjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * and关系的括号结束
	 * @return
	 */
	public ReceiveHelper stopAnd(){
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
	public ReceiveHelper startOr(){
		Junction junction = Restrictions.disjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * or关系的括号结束
	 * @return
	 */
	public ReceiveHelper stopOr(){
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
	public ReceiveHelper limit(int limit){
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
	public ReceiveHelper limit(int start ,int limit){
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
	 * inner join Mail ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public MailHelper enterMail(){
		return this.enterMailWithJoinType(0);
	}
	
	/**
	 * left join Mail ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public MailHelper leftJoinMail(){
		return this.enterMailWithJoinType(1);
	}
	
	/**
	 * full join Mail ，进入关联关系实体设置查询条件,full join 查询较少用到。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public MailHelper fullJoinMail(){
		return this.enterMailWithJoinType(2);
	}
	
	/**
	 * 创建Mail别称mail查询条件，并根据joinType参数设置jion类型。
	 * 本方法只被内部3种join类型方法调用。
	 * 
	 * @param joinType
	 * @return
	 */
	private MailHelper enterMailWithJoinType(int joinType){
		MailService mailService = (MailService) this.service.getBean("mailService");
		String enterAlias = this.alias+"_mail";//本级别称加上"_"加上属性名来创建子别称
		boolean notExists = this.aliases.add(enterAlias);
		if(notExists){
			switch (joinType) {
			case 1:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"mail", enterAlias,CriteriaSpecification.LEFT_JOIN);//以left join 方式创建别称。
				break;
				
			case 2:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"mail", enterAlias,CriteriaSpecification.FULL_JOIN);//以full join 方式创建别称。
				break;
				
			default:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"mail", enterAlias);//默认以inner join 方式创建别称。
				break;
			}
		}
		MailHelper h = new MailHelper(mailService,enterAlias);//用别称创建别称helper
		h._injectDc(dc);//为别称helper注入本helper dc
		h.aliases = this.aliases;
		h.ignoreEmpty = this.ignoreEmpty;
		this.helpers.push(this);//把自己压入堆栈
		h.helpers = this.helpers;//把堆栈传递给别称helper
		h.junctions = this.junctions;//把堆栈传递给别称helper
		return h;
	}
	/**
	 * 从Mail别称mail返回
	 * @return
	 */
	public MailHelper back2Mail(){
		if(this.helpers.empty()){
			throw new HelperException("无法回到helper类:null");
		}
		HelperAbstract<Mail> h =  this.helpers.pop();
		return (MailHelper)h;
	}
	/**
	 * inner join UpdateRecord ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UpdateRecordHelper enterUpdateRecords(){
		return this.enterUpdateRecordsWithJoinType(0);
	}
	
	/**
	 * left join UpdateRecord ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UpdateRecordHelper leftJoinUpdateRecords(){
		return this.enterUpdateRecordsWithJoinType(1);
	}
	
	/**
	 * full join UpdateRecord ，进入关联关系实体设置查询条件,full join 查询较少用到。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UpdateRecordHelper fullJoinUpdateRecords(){
		return this.enterUpdateRecordsWithJoinType(2);
	}
	
	/**
	 * 创建UpdateRecord别称updateRecords查询条件，并根据joinType参数设置jion类型。
	 * 本方法只被内部3种join类型方法调用。
	 * 
	 * @param joinType
	 * @return
	 */
	private UpdateRecordHelper enterUpdateRecordsWithJoinType(int joinType){
		UpdateRecordService updateRecordService = (UpdateRecordService) this.service.getBean("updateRecordService");
		String enterAlias = this.alias+"_updateRecords";//本级别称加上"_"加上属性名来创建子别称
		boolean notExists = this.aliases.add(enterAlias);
		if(notExists){
			switch (joinType) {
			case 1:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"updateRecords", enterAlias,CriteriaSpecification.LEFT_JOIN);//以left join 方式创建别称。
				break;
				
			case 2:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"updateRecords", enterAlias,CriteriaSpecification.FULL_JOIN);//以full join 方式创建别称。
				break;
				
			default:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"updateRecords", enterAlias);//默认以inner join 方式创建别称。
				break;
			}
		}
		UpdateRecordHelper h = new UpdateRecordHelper(updateRecordService,enterAlias);//用别称创建别称helper
		h._injectDc(dc);//为别称helper注入本helper dc
		h.aliases = this.aliases;
		h.ignoreEmpty = this.ignoreEmpty;
		this.helpers.push(this);//把自己压入堆栈
		h.helpers = this.helpers;//把堆栈传递给别称helper
		h.junctions = this.junctions;//把堆栈传递给别称helper
		return h;
	}
	/**
	 * 从UpdateRecord别称updateRecords返回
	 * @return
	 */
	public UpdateRecordHelper back2UpdateRecords(){
		if(this.helpers.empty()){
			throw new HelperException("无法回到helper类:null");
		}
		HelperAbstract<UpdateRecord> h =  this.helpers.pop();
		return (UpdateRecordHelper)h;
	}
	
	//== 对象属性处理 包括关联对象的属性的基本处理  ==//
	
	
	/**
	*Name:收件id
	*Comment:收件id
	*/

	public ReceiveProperty<Long> getReceiveId(){
		return new ReceiveProperty<Long>("receiveId",this,Long.class);
	}
	
	

	public ReceiveEntityProperty<Mail> getMail(){
		return new ReceiveEntityProperty<Mail>("mail",this,Mail.class);
	}
	/**
	 * many-to-one对象Mail的主键 等于
	 * @param mailId 
	 * @return
	 */
	public ReceiveHelper MailIdEq(Long mailId){
		Mail prop = null;
		if(mailId != null){
			prop = ((MailService)this.service.getBean("mailService")).findById(mailId);
		}
		if(prop == null && mailId!= null){//提供的id找不到实体，则表示数据集应该为空
			return startAnd().getMail().IsNotNull().getMail().IsNull().stopAnd();//设置此条件表示不可能有符合条件的记录。
		}
		return getMail().Eq(prop);
	}
	
	
	/**
	*Name:收件人id
	*Comment:收件人id
	*/

	public ReceiveProperty<Long> getUserId(){
		return new ReceiveProperty<Long>("userId",this,ClassUtils.wrapperToPrimitive(Long.class));
	}
	
	
	/**
	*Name:收件人工作编号
	*Comment:收件人工作编号
	*/

	public ReceiveStringProperty getWorkCode(){
		return new ReceiveStringProperty("workCode",this);
	}
	
	
	/**
	*Name:收件人姓名
	*Comment:收件人的姓名
	*/

	public ReceiveStringProperty getLastName(){
		return new ReceiveStringProperty("lastName",this);
	}
	
	
	/**
	*Name:收件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public ReceiveStringProperty getLoginId(){
		return new ReceiveStringProperty("loginId",this);
	}
	
	
	/**
	*Name:收件人的分部全称
	*Comment:收件人的分部全称
	*/

	public ReceiveStringProperty getSubcompanyName(){
		return new ReceiveStringProperty("subcompanyName",this);
	}
	
	
	/**
	*Name:收件人的部门全称
	*Comment:收件人的部门全称
	*/

	public ReceiveStringProperty getDepartmentName(){
		return new ReceiveStringProperty("departmentName",this);
	}
	
	
	/**
	*Name:接收传阅的时间
	*Comment:接收传阅的时间
	*/

	public ReceiveDateProperty getReceiveTime(){
		return new ReceiveDateProperty("receiveTime",this);
	}
	
	
	/**
	*Name:加入时间
	*Comment:添加联系人的时间
	*/

	public ReceiveDateProperty getJoinTime(){
		return new ReceiveDateProperty("joinTime",this);
	}
	
	
	/**
	*Name:状态
	*Comment:状态: 0 未开封  1 已开封  
	*/

	public ReceiveProperty<Integer> getReceiveStatus(){
		return new ReceiveProperty<Integer>("receiveStatus",this,ClassUtils.wrapperToPrimitive(Integer.class));
	}
	
	/**
	*Name:传阅筛选状态
	*Comment:4 草稿  5 未读 6 已读  
	*/

	public ReceiveProperty<Integer> getMailState(){
		return new ReceiveProperty<Integer>("mailState",this,Integer.class);
	}
	
	/**
	*Name:传阅流程状态
	*Comment:(冗余字段) 1 发阅中 2 待办传阅 3 已完成
	*/

	public ReceiveProperty<Integer> getStepStatus(){
		return new ReceiveProperty<Integer>("stepStatus",this,Integer.class);
	}
	
	
	/**
	*Name:开封时间
	*Comment:记录打开传阅的时间
	*/

	public ReceiveDateProperty getOpenTime(){
		return new ReceiveDateProperty("openTime",this);
	}
	
	/**
	*Name:是否确认
	*Comment:false 未确认 true 已确认 (传阅开封, 不一定是传阅确认   但传阅确认必须是传阅开封
)
	*/

	public ReceiveProperty<Boolean> getIfConfirm(){
		return new ReceiveProperty<Boolean>("ifConfirm",this,Boolean.class);
	}
	
	
	/**
	*Name:确认时间
	*Comment:确认时间
	*/

	public ReceiveDateProperty getAffirmTime(){
		return new ReceiveDateProperty("affirmTime",this);
	}
	
	
	/**
	*Name:确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注
	*/

	public ReceiveStringProperty getRemark(){
		return new ReceiveStringProperty("remark",this);
	}
	
	
	/**
	*Name:确认/标识
	*Comment:该字段用于记录收件人在确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveStringProperty getConfirmRecord(){
		return new ReceiveStringProperty("confirmRecord",this);
	}
	
	/**
	*Name:序号
	*Comment:序号
	*/

	public ReceiveProperty<Integer> getSerialNum(){
		return new ReceiveProperty<Integer>("serialNum",this,Integer.class);
	}
	
	/**
	*Name:是否重新确认
	*Comment:该字段用于重新确认传阅, false 未重新确认 true 重新确认 
	*/

	public ReceiveProperty<Boolean> getAfreshConfim(){
		return new ReceiveProperty<Boolean>("afreshConfim",this,Boolean.class);
	}
	
	
	/**
	*Name:(重新)确认/标识
	*Comment:该字段用于记录收件人在重新 确认传阅时的  确认信息 + 确认时间 
	*/

	public ReceiveStringProperty getAcRecord(){
		return new ReceiveStringProperty("acRecord",this);
	}
	
	
	/**
	*Name:(重新)确认信息备注
	*Comment:当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注(重新)
	*/

	public ReceiveStringProperty getAfreshRemark(){
		return new ReceiveStringProperty("afreshRemark",this);
	}
	
	
	/**
	*Name:(重新)确认时间
	*Comment:确认时间(重新)
	*/

	public ReceiveDateProperty getMhTime(){
		return new ReceiveDateProperty("mhTime",this);
	}
	
	/**
	*Name:是否关注
	*Comment:收件人的关注状态:  0 为未关注 （false），1 已关注（true）
	*/

	public ReceiveProperty<Boolean> getReceiveAttention(){
		return new ReceiveProperty<Boolean>("receiveAttention",this,Boolean.class);
	}
	
	/**
	*Name:区别
	*Comment:区别是谁添加的联系人, 存放添加该联系人的用户ID
	*/

	public ReceiveProperty<Long> getReDifferentiate(){
		return new ReceiveProperty<Long>("reDifferentiate",this,Long.class);
	}
	
	

	public ReceiveListProperty getUpdateRecords(){
		return new ReceiveListProperty("updateRecords",this);
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
	public ReceiveHelper inAnywhere(String value,boolean eq){
		this.startOr();
		if (eq) {
			getWorkCode().Eq(value);
			getLastName().Eq(value);
			getLoginId().Eq(value);
			getSubcompanyName().Eq(value);
			getDepartmentName().Eq(value);
			getRemark().Eq(value);
			getConfirmRecord().Eq(value);
			getAcRecord().Eq(value);
			getAfreshRemark().Eq(value);
		} else {
			getWorkCode().Like(value);
			getLastName().Like(value);
			getLoginId().Like(value);
			getSubcompanyName().Like(value);
			getDepartmentName().Like(value);
			getRemark().Like(value);
			getConfirmRecord().Like(value);
			getAcRecord().Like(value);
			getAfreshRemark().Like(value);
		}
		this.stopOr();
		return this;
	}
	
	
	private UpdateListener<Receive> updateListener;
	/**
	 * 监听调用each/eachInPage和scrollResult方法修改的bean。
	 * @param listener
	 * @return
	 */
	public ReceiveHelper onUpdate(UpdateListener<Receive> listener){
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
	public ReceiveHelper sleepInteval(int interval){
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
	public ReceiveHelper sort(String dir,String propName){
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
	
	private void afterEachUpdate(Receive bean, Receive oldBean) {
		if(updateListener != null){
			updateListener.each(oldBean, bean);
		}
	}
	
	private Receive beforeEachUpdate(Receive bean) {
		Receive oldBean = null;
		if(updateListener != null){
			oldBean = new Receive();
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
