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
public class MailHelper extends HelperAbstract<Mail>{

	MailService service;
	
	private boolean distinctRoot;
	
	protected int pageSize = 25;
	
	protected boolean setLimit;
	
	protected boolean setStart;
	
	protected int limit;
	
	protected int start;
	
	protected Set aliases = new HashSet();
	
	protected MailJSONHelper jsonHelper;
	
	/**
	 * pdm定义的 属性名 和 字段中文名的Map
	 */
	private static Map<String,String> titles = new LinkedHashMap<String,String>();
	
	static{
		titles.put("mailId", "传阅id");
		titles.put("userId", "发件人id");
		titles.put("workCode", "发件人工作编号");
		titles.put("lastName", "发件人姓名");
		titles.put("loginId", "发件人登录名");
		titles.put("subcompanyName", "发件人的分部全称");
		titles.put("departmentName", "发件人的部门全称");
		titles.put("allReceiveName", "收件人名字");
		titles.put("title", "传阅主题");
		titles.put("mailContent", "邮件内容");
		titles.put("createTime", "创建传阅的时间");
		titles.put("sendTime", "发送传阅的时间");
		titles.put("status", "传阅状态");
		titles.put("stepStatus", "传阅流程状态");
		titles.put("completeTime", "设置的完成时间");
		titles.put("ifImportant", "重要传阅");
		titles.put("ifUpdate", "允许修订附件");
		titles.put("ifUpload", "允许上传附件");
		titles.put("ifRead", "开封已阅确认");
		titles.put("ifNotify", "短信提醒");
		titles.put("ifRemind", "确认时提醒");
		titles.put("ifRemindAll", "确认时提醒所有传阅对象");
		titles.put("ifSecrecy", "传阅密送");
		titles.put("ifAdd", "允许新添加人员");
		titles.put("ifSequence", "有序确认");
		titles.put("hasAttachment", "是否有附件");
		titles.put("enabled", "是否启用软删除");
		titles.put("attention", "是否关注");
		titles.put("ruleName", "传阅规则");
		titles.put("deleteTime", "删除传阅的时间");
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
	public MailService service(){
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
	public MailHelper ignoreEmptyValueCondiction(){
		this.ignoreEmpty = true;
		return this;
	}
	
	public MailHelper(MailService service,String alias) {
		super();
		this.service = service;
		this.alias = alias.toString();
		if(StringUtils.isEmpty(this.alias)){
			this.dc = service.createCriteria();
		}
	}
	
	public MailHelper(MailService service) {
		this(service,"");
	}
	
	/**
	 * 设置本对象的pageSize，设置后，则不使用默认pageSize
	 * @param pageSize
	 * @return
	 */
	public MailHelper setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 新建一个干净的helper类。
	 * @return
	 */
	public MailHelper newOne(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以newDc!");
		}
		return new MailHelper(service,"");
	}
	/**
	 * 为Helper类注入一个附属dc
	 * @return
	 */
	public MailHelper setDc(DetachedCriteria dc){
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
	public MailHelper distinctEntity(){
		BaseDAO.distinctRootWithPk(dc,"mailId");
		this.distinctRoot = true;
		return this;
	}
	
	/**
	 * excel工具类
	 * @return
	 */
	public MailXls xls(){
		return new MailXls(this);
	}
	
	/**
	 * JSON工具类
	 * @return
	 */
	public MailJSONHelper json(){
		if(jsonHelper != null) {
			return jsonHelper;
		}
		return new MailJSONHelper(this);
	}
	
	/**
	 * Bean工具类,可以直接创建实体类并操作，也可以直接操作查询到的实体类，
	 * 并可以调用update/insert方法更新到数据库，工具类是链式调用的，可简化数据库操作。
	 * @return
	 */
	public MailBean bean(){
		if(ignoreEmpty){
			throw new HelperException("ignoreEmptyValueCondiction情况下，不允许使用Bean工具类，避免条件判断失误导致系统损失!");
		}
		return new MailBean(this);
	}
	
	/**
	 * 删除符合条件的对象,并返回被删除的结果集
	 * @return
	 */
	public List<Mail> delete(){
		List<Mail> deleteList = list();
		service.deleteList(deleteList);
		return deleteList;
	}
	
	@Override
	public List<Mail> list(){
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
	public PageList<Mail> listPage(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll<Mail> listPageScroll(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public PageList<Mail> listPage(int page){
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
	public Mail uniqueResult(){
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
	public List<Mail> each(final Each<Mail> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用each!");
		}
		return (List<Mail>)this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				List<Mail> list = list();
				for (Iterator<Mail> iterator = list.iterator(); iterator.hasNext();) {
					Mail bean = iterator.next();
					Mail oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				return list;
			}
			
		});
	}
	
	@Override
	public long scrollResult(final ScrollEach<Mail> each){
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
					Mail bean = (Mail)results.get(0);
					Mail oldBean = beforeEachUpdate(bean);
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
	public PageList<Mail> eachInPage(final int page,final Each<Mail> each){
		return eachInPage(page,pageSize,each);
	}
	
	@Override
	public PageScroll<Mail> eachInPageScroll(final int page,final Each<Mail> each){
		return eachInPageScroll(page,pageSize,each);
	}
	
	@Override
	public PageList<Mail> eachInPage(final int page,final int pageSize,final Each<Mail> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageList<Mail>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageList<Mail> pageList = listPage(page,pageSize);
				List<Mail> list = pageList.getList();
				for (Iterator<Mail> iterator = list.iterator(); iterator.hasNext();) {
					Mail bean = iterator.next();
					Mail oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageList;
			}
			
		});
	}
	
	@Override
	public PageScroll<Mail> eachInPageScroll(final int page,final int pageSize,final Each<Mail> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageScroll<Mail>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageScroll<Mail> pageScroll = listPageScroll(page,pageSize);
				List<Mail> list = pageScroll.getList();
				for (Iterator<Mail> iterator = list.iterator(); iterator.hasNext();) {
					Mail bean = iterator.next();
					Mail oldBean = beforeEachUpdate(bean);
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
	public MailHelper startAnd(){
		Junction junction = Restrictions.conjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * and关系的括号结束
	 * @return
	 */
	public MailHelper stopAnd(){
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
	public MailHelper startOr(){
		Junction junction = Restrictions.disjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * or关系的括号结束
	 * @return
	 */
	public MailHelper stopOr(){
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
	public MailHelper limit(int limit){
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
	public MailHelper limit(int start ,int limit){
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
	 * inner join AttachmentItem ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public AttachmentItemHelper enterAttachmentItems(){
		return this.enterAttachmentItemsWithJoinType(0);
	}
	
	/**
	 * left join AttachmentItem ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public AttachmentItemHelper leftJoinAttachmentItems(){
		return this.enterAttachmentItemsWithJoinType(1);
	}
	
	/**
	 * full join AttachmentItem ，进入关联关系实体设置查询条件,full join 查询较少用到。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public AttachmentItemHelper fullJoinAttachmentItems(){
		return this.enterAttachmentItemsWithJoinType(2);
	}
	
	/**
	 * 创建AttachmentItem别称attachmentItems查询条件，并根据joinType参数设置jion类型。
	 * 本方法只被内部3种join类型方法调用。
	 * 
	 * @param joinType
	 * @return
	 */
	private AttachmentItemHelper enterAttachmentItemsWithJoinType(int joinType){
		AttachmentItemService attachmentItemService = (AttachmentItemService) this.service.getBean("attachmentItemService");
		String enterAlias = this.alias+"_attachmentItems";//本级别称加上"_"加上属性名来创建子别称
		boolean notExists = this.aliases.add(enterAlias);
		if(notExists){
			switch (joinType) {
			case 1:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"attachmentItems", enterAlias,CriteriaSpecification.LEFT_JOIN);//以left join 方式创建别称。
				break;
				
			case 2:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"attachmentItems", enterAlias,CriteriaSpecification.FULL_JOIN);//以full join 方式创建别称。
				break;
				
			default:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"attachmentItems", enterAlias);//默认以inner join 方式创建别称。
				break;
			}
		}
		AttachmentItemHelper h = new AttachmentItemHelper(attachmentItemService,enterAlias);//用别称创建别称helper
		h._injectDc(dc);//为别称helper注入本helper dc
		h.aliases = this.aliases;
		h.ignoreEmpty = this.ignoreEmpty;
		this.helpers.push(this);//把自己压入堆栈
		h.helpers = this.helpers;//把堆栈传递给别称helper
		h.junctions = this.junctions;//把堆栈传递给别称helper
		return h;
	}
	/**
	 * 从AttachmentItem别称attachmentItems返回
	 * @return
	 */
	public AttachmentItemHelper back2AttachmentItems(){
		if(this.helpers.empty()){
			throw new HelperException("无法回到helper类:null");
		}
		HelperAbstract<AttachmentItem> h =  this.helpers.pop();
		return (AttachmentItemHelper)h;
	}
	/**
	 * inner join Receive ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public ReceiveHelper enterReceives(){
		return this.enterReceivesWithJoinType(0);
	}
	
	/**
	 * left join Receive ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public ReceiveHelper leftJoinReceives(){
		return this.enterReceivesWithJoinType(1);
	}
	
	/**
	 * full join Receive ，进入关联关系实体设置查询条件,full join 查询较少用到。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public ReceiveHelper fullJoinReceives(){
		return this.enterReceivesWithJoinType(2);
	}
	
	/**
	 * 创建Receive别称receives查询条件，并根据joinType参数设置jion类型。
	 * 本方法只被内部3种join类型方法调用。
	 * 
	 * @param joinType
	 * @return
	 */
	private ReceiveHelper enterReceivesWithJoinType(int joinType){
		ReceiveService receiveService = (ReceiveService) this.service.getBean("receiveService");
		String enterAlias = this.alias+"_receives";//本级别称加上"_"加上属性名来创建子别称
		boolean notExists = this.aliases.add(enterAlias);
		if(notExists){
			switch (joinType) {
			case 1:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"receives", enterAlias,CriteriaSpecification.LEFT_JOIN);//以left join 方式创建别称。
				break;
				
			case 2:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"receives", enterAlias,CriteriaSpecification.FULL_JOIN);//以full join 方式创建别称。
				break;
				
			default:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"receives", enterAlias);//默认以inner join 方式创建别称。
				break;
			}
		}
		ReceiveHelper h = new ReceiveHelper(receiveService,enterAlias);//用别称创建别称helper
		h._injectDc(dc);//为别称helper注入本helper dc
		h.aliases = this.aliases;
		h.ignoreEmpty = this.ignoreEmpty;
		this.helpers.push(this);//把自己压入堆栈
		h.helpers = this.helpers;//把堆栈传递给别称helper
		h.junctions = this.junctions;//把堆栈传递给别称helper
		return h;
	}
	/**
	 * 从Receive别称receives返回
	 * @return
	 */
	public ReceiveHelper back2Receives(){
		if(this.helpers.empty()){
			throw new HelperException("无法回到helper类:null");
		}
		HelperAbstract<Receive> h =  this.helpers.pop();
		return (ReceiveHelper)h;
	}
	/**
	 * inner join Discuss ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public DiscussHelper enterDiscusses(){
		return this.enterDiscussesWithJoinType(0);
	}
	
	/**
	 * left join Discuss ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public DiscussHelper leftJoinDiscusses(){
		return this.enterDiscussesWithJoinType(1);
	}
	
	/**
	 * full join Discuss ，进入关联关系实体设置查询条件,full join 查询较少用到。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public DiscussHelper fullJoinDiscusses(){
		return this.enterDiscussesWithJoinType(2);
	}
	
	/**
	 * 创建Discuss别称discusses查询条件，并根据joinType参数设置jion类型。
	 * 本方法只被内部3种join类型方法调用。
	 * 
	 * @param joinType
	 * @return
	 */
	private DiscussHelper enterDiscussesWithJoinType(int joinType){
		DiscussService discussService = (DiscussService) this.service.getBean("discussService");
		String enterAlias = this.alias+"_discusses";//本级别称加上"_"加上属性名来创建子别称
		boolean notExists = this.aliases.add(enterAlias);
		if(notExists){
			switch (joinType) {
			case 1:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"discusses", enterAlias,CriteriaSpecification.LEFT_JOIN);//以left join 方式创建别称。
				break;
				
			case 2:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"discusses", enterAlias,CriteriaSpecification.FULL_JOIN);//以full join 方式创建别称。
				break;
				
			default:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"discusses", enterAlias);//默认以inner join 方式创建别称。
				break;
			}
		}
		DiscussHelper h = new DiscussHelper(discussService,enterAlias);//用别称创建别称helper
		h._injectDc(dc);//为别称helper注入本helper dc
		h.aliases = this.aliases;
		h.ignoreEmpty = this.ignoreEmpty;
		this.helpers.push(this);//把自己压入堆栈
		h.helpers = this.helpers;//把堆栈传递给别称helper
		h.junctions = this.junctions;//把堆栈传递给别称helper
		return h;
	}
	/**
	 * 从Discuss别称discusses返回
	 * @return
	 */
	public DiscussHelper back2Discusses(){
		if(this.helpers.empty()){
			throw new HelperException("无法回到helper类:null");
		}
		HelperAbstract<Discuss> h =  this.helpers.pop();
		return (DiscussHelper)h;
	}
	/**
	 * inner join UserCollection ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UserCollectionHelper enterUserCollections(){
		return this.enterUserCollectionsWithJoinType(0);
	}
	
	/**
	 * left join UserCollection ，进入关联关系实体设置查询条件。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UserCollectionHelper leftJoinUserCollections(){
		return this.enterUserCollectionsWithJoinType(1);
	}
	
	/**
	 * full join UserCollection ，进入关联关系实体设置查询条件,full join 查询较少用到。
	 * 注意，此时不能进行分页查询（不能用listPage,listPageJson等方法），得到每一页的实体数量可能是不正确的。
	 * 如果实现需要关联查询并分页，请用纯SQL操作，或用冗余表和字段的方式把关联关系合并到一个表，必要的时候可以考虑编写触发器，或将此类信息复制一份到mongdb，然后进行查询。
	 */
	public UserCollectionHelper fullJoinUserCollections(){
		return this.enterUserCollectionsWithJoinType(2);
	}
	
	/**
	 * 创建UserCollection别称userCollections查询条件，并根据joinType参数设置jion类型。
	 * 本方法只被内部3种join类型方法调用。
	 * 
	 * @param joinType
	 * @return
	 */
	private UserCollectionHelper enterUserCollectionsWithJoinType(int joinType){
		UserCollectionService userCollectionService = (UserCollectionService) this.service.getBean("userCollectionService");
		String enterAlias = this.alias+"_userCollections";//本级别称加上"_"加上属性名来创建子别称
		boolean notExists = this.aliases.add(enterAlias);
		if(notExists){
			switch (joinType) {
			case 1:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"userCollections", enterAlias,CriteriaSpecification.LEFT_JOIN);//以left join 方式创建别称。
				break;
				
			case 2:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"userCollections", enterAlias,CriteriaSpecification.FULL_JOIN);//以full join 方式创建别称。
				break;
				
			default:
				dc.createAlias(this.alias+("".equals(this.alias)?"":".")+"userCollections", enterAlias);//默认以inner join 方式创建别称。
				break;
			}
		}
		UserCollectionHelper h = new UserCollectionHelper(userCollectionService,enterAlias);//用别称创建别称helper
		h._injectDc(dc);//为别称helper注入本helper dc
		h.aliases = this.aliases;
		h.ignoreEmpty = this.ignoreEmpty;
		this.helpers.push(this);//把自己压入堆栈
		h.helpers = this.helpers;//把堆栈传递给别称helper
		h.junctions = this.junctions;//把堆栈传递给别称helper
		return h;
	}
	/**
	 * 从UserCollection别称userCollections返回
	 * @return
	 */
	public UserCollectionHelper back2UserCollections(){
		if(this.helpers.empty()){
			throw new HelperException("无法回到helper类:null");
		}
		HelperAbstract<UserCollection> h =  this.helpers.pop();
		return (UserCollectionHelper)h;
	}
	
	//== 对象属性处理 包括关联对象的属性的基本处理  ==//
	
	
	/**
	*Name:传阅id
	*Comment:传阅id
	*/

	public MailProperty<Long> getMailId(){
		return new MailProperty<Long>("mailId",this,Long.class);
	}
	
	
	/**
	*Name:发件人id
	*Comment:发件人id
	*/

	public MailProperty<Long> getUserId(){
		return new MailProperty<Long>("userId",this,ClassUtils.wrapperToPrimitive(Long.class));
	}
	
	
	/**
	*Name:发件人工作编号
	*Comment:发件人工作编号
	*/

	public MailStringProperty getWorkCode(){
		return new MailStringProperty("workCode",this);
	}
	
	
	/**
	*Name:发件人姓名
	*Comment:发件人的姓名
	*/

	public MailStringProperty getLastName(){
		return new MailStringProperty("lastName",this);
	}
	
	
	/**
	*Name:发件人登录名
	*Comment:用户的登录名称(冗余字段)
	*/

	public MailStringProperty getLoginId(){
		return new MailStringProperty("loginId",this);
	}
	
	
	/**
	*Name:发件人的分部全称
	*Comment:发件人的分部全称
	*/

	public MailStringProperty getSubcompanyName(){
		return new MailStringProperty("subcompanyName",this);
	}
	
	
	/**
	*Name:发件人的部门全称
	*Comment:发件人的部门全称
	*/

	public MailStringProperty getDepartmentName(){
		return new MailStringProperty("departmentName",this);
	}
	
	
	/**
	*Name:收件人名字
	*Comment:可以有多个(冗余字段)
	*/

	public MailStringProperty getAllReceiveName(){
		return new MailStringProperty("allReceiveName",this);
	}
	
	
	/**
	*Name:传阅主题
	*Comment:传阅主题(不做限制)
	*/

	public MailStringProperty getTitle(){
		return new MailStringProperty("title",this);
	}
	
	
	/**
	*Name:邮件内容
	*Comment:邮件内容
	*/

	public MailStringProperty getMailContent(){
		return new MailStringProperty("mailContent",this);
	}
	
	
	/**
	*Name:创建传阅的时间
	*Comment:创建传阅的时间
	*/

	public MailDateProperty getCreateTime(){
		return new MailDateProperty("createTime",this);
	}
	
	
	/**
	*Name:发送传阅的时间
	*Comment:发送传阅的时间
	*/

	public MailDateProperty getSendTime(){
		return new MailDateProperty("sendTime",this);
	}
	
	/**
	*Name:传阅状态
	*Comment:0 无状态   1 待发传阅  7 已删除
	*/

	public MailProperty<Integer> getStatus(){
		return new MailProperty<Integer>("status",this,Integer.class);
	}
	
	/**
	*Name:传阅流程状态
	*Comment:1 发阅中   3 已完成
	*/

	public MailProperty<Integer> getStepStatus(){
		return new MailProperty<Integer>("stepStatus",this,Integer.class);
	}
	
	
	/**
	*Name:设置的完成时间
	*Comment:传阅完成的时间
	*/

	public MailDateProperty getCompleteTime(){
		return new MailDateProperty("completeTime",this);
	}
	
	/**
	*Name:重要传阅
	*Comment:重要传阅
	*/

	public MailProperty<Boolean> getIfImportant(){
		return new MailProperty<Boolean>("ifImportant",this,Boolean.class);
	}
	
	/**
	*Name:允许修订附件
	*Comment:允许修订附件
	*/

	public MailProperty<Boolean> getIfUpdate(){
		return new MailProperty<Boolean>("ifUpdate",this,Boolean.class);
	}
	
	/**
	*Name:允许上传附件
	*Comment:允许上传附件
	*/

	public MailProperty<Boolean> getIfUpload(){
		return new MailProperty<Boolean>("ifUpload",this,Boolean.class);
	}
	
	/**
	*Name:开封已阅确认
	*Comment:开封已阅确认
	*/

	public MailProperty<Boolean> getIfRead(){
		return new MailProperty<Boolean>("ifRead",this,Boolean.class);
	}
	
	/**
	*Name:短信提醒
	*Comment:短信提醒
	*/

	public MailProperty<Boolean> getIfNotify(){
		return new MailProperty<Boolean>("ifNotify",this,Boolean.class);
	}
	
	/**
	*Name:确认时提醒
	*Comment:确认时提醒
	*/

	public MailProperty<Boolean> getIfRemind(){
		return new MailProperty<Boolean>("ifRemind",this,Boolean.class);
	}
	
	/**
	*Name:确认时提醒所有传阅对象
	*Comment:确认时提醒所有传阅对象
	*/

	public MailProperty<Boolean> getIfRemindAll(){
		return new MailProperty<Boolean>("ifRemindAll",this,Boolean.class);
	}
	
	/**
	*Name:传阅密送
	*Comment:传阅密送(不用实现)
	*/

	public MailProperty<Boolean> getIfSecrecy(){
		return new MailProperty<Boolean>("ifSecrecy",this,Boolean.class);
	}
	
	/**
	*Name:允许新添加人员
	*Comment:允许新添加人员
	*/

	public MailProperty<Boolean> getIfAdd(){
		return new MailProperty<Boolean>("ifAdd",this,Boolean.class);
	}
	
	/**
	*Name:有序确认
	*Comment:有序确认(留个字段,不实现)
	*/

	public MailProperty<Boolean> getIfSequence(){
		return new MailProperty<Boolean>("ifSequence",this,Boolean.class);
	}
	
	/**
	*Name:是否有附件
	*Comment:0 为没有 （false），1 为存在（true）
	*/

	public MailProperty<Boolean> getHasAttachment(){
		return new MailProperty<Boolean>("hasAttachment",this,Boolean.class);
	}
	
	
	/**
	*Name:是否启用软删除
	*Comment:0 为未删除（false），1 为 删除（true）
	*/

	public MailProperty<Boolean> getEnabled(){
		return new MailProperty<Boolean>("enabled",this,ClassUtils.wrapperToPrimitive(Boolean.class));
	}
	
	/**
	*Name:是否关注
	*Comment:0 为未关注 （false），1 已关注（true）
	*/

	public MailProperty<Boolean> getAttention(){
		return new MailProperty<Boolean>("attention",this,Boolean.class);
	}
	
	
	/**
	*Name:传阅规则
	*Comment:该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；
	*/

	public MailStringProperty getRuleName(){
		return new MailStringProperty("ruleName",this);
	}
	
	
	/**
	*Name:删除传阅的时间
	*Comment:删除传阅的时间
	*/

	public MailDateProperty getDeleteTime(){
		return new MailDateProperty("deleteTime",this);
	}
	
	

	public MailListProperty getAttachmentItems(){
		return new MailListProperty("attachmentItems",this);
	}
	
	

	public MailListProperty getReceives(){
		return new MailListProperty("receives",this);
	}
	
	

	public MailListProperty getDiscusses(){
		return new MailListProperty("discusses",this);
	}
	
	

	public MailListProperty getUserCollections(){
		return new MailListProperty("userCollections",this);
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
	public MailHelper inAnywhere(String value,boolean eq){
		this.startOr();
		if (eq) {
			getWorkCode().Eq(value);
			getLastName().Eq(value);
			getLoginId().Eq(value);
			getSubcompanyName().Eq(value);
			getDepartmentName().Eq(value);
			getAllReceiveName().Eq(value);
			getTitle().Eq(value);
			getMailContent().Eq(value);
			getRuleName().Eq(value);
		} else {
			getWorkCode().Like(value);
			getLastName().Like(value);
			getLoginId().Like(value);
			getSubcompanyName().Like(value);
			getDepartmentName().Like(value);
			getAllReceiveName().Like(value);
			getTitle().Like(value);
			getMailContent().Like(value);
			getRuleName().Like(value);
		}
		this.stopOr();
		return this;
	}
	
	
	private UpdateListener<Mail> updateListener;
	/**
	 * 监听调用each/eachInPage和scrollResult方法修改的bean。
	 * @param listener
	 * @return
	 */
	public MailHelper onUpdate(UpdateListener<Mail> listener){
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
	public MailHelper sleepInteval(int interval){
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
	public MailHelper sort(String dir,String propName){
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
	
	private void afterEachUpdate(Mail bean, Mail oldBean) {
		if(updateListener != null){
			updateListener.each(oldBean, bean);
		}
	}
	
	private Mail beforeEachUpdate(Mail bean) {
		Mail oldBean = null;
		if(updateListener != null){
			oldBean = new Mail();
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
