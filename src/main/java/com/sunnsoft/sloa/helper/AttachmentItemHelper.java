package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.UpdateRecord;
import com.sunnsoft.sloa.service.AttachmentItemService;
import com.sunnsoft.sloa.service.MailService;
import com.sunnsoft.sloa.service.UpdateRecordService;
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
public class AttachmentItemHelper extends HelperAbstract<AttachmentItem>{

	AttachmentItemService service;
	
	private boolean distinctRoot;
	
	protected int pageSize = 25;
	
	protected boolean setLimit;
	
	protected boolean setStart;
	
	protected int limit;
	
	protected int start;
	
	protected Set aliases = new HashSet();
	
	protected AttachmentItemJSONHelper jsonHelper;
	
	/**
	 * pdm定义的 属性名 和 字段中文名的Map
	 */
	private static Map<String,String> titles = new LinkedHashMap<String,String>();
	
	static{
		titles.put("itemId", "附件文件ID");
		titles.put("bulkId", "附件上传批次ID");
		titles.put("userId", "创建人ID");
		titles.put("creator", "创建人");
		titles.put("createTime", "创建时间");
		titles.put("fileName", "文件原名");
		titles.put("fileCategory", "分类");
		titles.put("saveName", "文件保存名");
		titles.put("urlPath", "链接地址");
		titles.put("attached", "是否实体附件");
		titles.put("state", "状态");
		titles.put("itemSize", "文件大小");
		titles.put("itemNeid", "网盘文件ID");
		titles.put("itemRev", "网盘文件版本");
		titles.put("itemDifferentiate", "区别");
		titles.put("localhostUrlPath", "存储APP端下载到本地的URL");
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
	public AttachmentItemService service(){
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
	public AttachmentItemHelper ignoreEmptyValueCondiction(){
		this.ignoreEmpty = true;
		return this;
	}
	
	public AttachmentItemHelper(AttachmentItemService service,String alias) {
		super();
		this.service = service;
		this.alias = alias.toString();
		if(StringUtils.isEmpty(this.alias)){
			this.dc = service.createCriteria();
		}
	}
	
	public AttachmentItemHelper(AttachmentItemService service) {
		this(service,"");
	}
	
	/**
	 * 设置本对象的pageSize，设置后，则不使用默认pageSize
	 * @param pageSize
	 * @return
	 */
	public AttachmentItemHelper setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	/**
	 * 新建一个干净的helper类。
	 * @return
	 */
	public AttachmentItemHelper newOne(){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以newDc!");
		}
		return new AttachmentItemHelper(service,"");
	}
	/**
	 * 为Helper类注入一个附属dc
	 * @return
	 */
	public AttachmentItemHelper setDc(DetachedCriteria dc){
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
	public AttachmentItemHelper distinctEntity(){
		BaseDAO.distinctRootWithPk(dc,"itemId");
		this.distinctRoot = true;
		return this;
	}
	
	/**
	 * excel工具类
	 * @return
	 */
	public AttachmentItemXls xls(){
		return new AttachmentItemXls(this);
	}
	
	/**
	 * JSON工具类
	 * @return
	 */
	public AttachmentItemJSONHelper json(){
		if(jsonHelper != null) {
			return jsonHelper;
		}
		return new AttachmentItemJSONHelper(this);
	}
	
	/**
	 * Bean工具类,可以直接创建实体类并操作，也可以直接操作查询到的实体类，
	 * 并可以调用update/insert方法更新到数据库，工具类是链式调用的，可简化数据库操作。
	 * @return
	 */
	public AttachmentItemBean bean(){
		if(ignoreEmpty){
			throw new HelperException("ignoreEmptyValueCondiction情况下，不允许使用Bean工具类，避免条件判断失误导致系统损失!");
		}
		return new AttachmentItemBean(this);
	}
	
	/**
	 * 删除符合条件的对象,并返回被删除的结果集
	 * @return
	 */
	public List<AttachmentItem> delete(){
		List<AttachmentItem> deleteList = list();
		service.deleteList(deleteList);
		return deleteList;
	}
	
	@Override
	public List<AttachmentItem> list(){
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
	public PageList<AttachmentItem> listPage(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPage(dc, pageSize, page);
	}
	
	@Override
	public PageScroll<AttachmentItem> listPageScroll(int page,int pageSize){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用listPage!");
		}
		while(!junctions.empty()){
			this.dc.add(junctions.pop());
		}
		return service.findByDetachedCriteriaByPageScroll(dc, (page-1)*pageSize, pageSize);
	}
	
	@Override
	public PageList<AttachmentItem> listPage(int page){
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
	public AttachmentItem uniqueResult(){
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
	public List<AttachmentItem> each(final Each<AttachmentItem> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用each!");
		}
		return (List<AttachmentItem>)this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				List<AttachmentItem> list = list();
				for (Iterator<AttachmentItem> iterator = list.iterator(); iterator.hasNext();) {
					AttachmentItem bean = iterator.next();
					AttachmentItem oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				return list;
			}
			
		});
	}
	
	@Override
	public long scrollResult(final ScrollEach<AttachmentItem> each){
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
					AttachmentItem bean = (AttachmentItem)results.get(0);
					AttachmentItem oldBean = beforeEachUpdate(bean);
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
	public PageList<AttachmentItem> eachInPage(final int page,final Each<AttachmentItem> each){
		return eachInPage(page,pageSize,each);
	}
	
	@Override
	public PageScroll<AttachmentItem> eachInPageScroll(final int page,final Each<AttachmentItem> each){
		return eachInPageScroll(page,pageSize,each);
	}
	
	@Override
	public PageList<AttachmentItem> eachInPage(final int page,final int pageSize,final Each<AttachmentItem> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageList<AttachmentItem>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageList<AttachmentItem> pageList = listPage(page,pageSize);
				List<AttachmentItem> list = pageList.getList();
				for (Iterator<AttachmentItem> iterator = list.iterator(); iterator.hasNext();) {
					AttachmentItem bean = iterator.next();
					AttachmentItem oldBean = beforeEachUpdate(bean);
					each.each(bean, list);
					afterEachUpdate(bean, oldBean);
				}
				
				return pageList;
			}
			
		});
	}
	
	@Override
	public PageScroll<AttachmentItem> eachInPageScroll(final int page,final int pageSize,final Each<AttachmentItem> each){
		if(StringUtils.isNotEmpty(alias)){
			throw new HelperException("别称类不可以调用eachInPage!");
		}
		return (PageScroll<AttachmentItem>) this.service.executeTransactional(new TransactionalCallBack(){

			@Override
			public Object execute(IService service) {
				PageScroll<AttachmentItem> pageScroll = listPageScroll(page,pageSize);
				List<AttachmentItem> list = pageScroll.getList();
				for (Iterator<AttachmentItem> iterator = list.iterator(); iterator.hasNext();) {
					AttachmentItem bean = iterator.next();
					AttachmentItem oldBean = beforeEachUpdate(bean);
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
	public AttachmentItemHelper startAnd(){
		Junction junction = Restrictions.conjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * and关系的括号结束
	 * @return
	 */
	public AttachmentItemHelper stopAnd(){
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
	public AttachmentItemHelper startOr(){
		Junction junction = Restrictions.disjunction();
		junctions.push(junction);
		return this;
	}
	/**
	 * or关系的括号结束
	 * @return
	 */
	public AttachmentItemHelper stopOr(){
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
	public AttachmentItemHelper limit(int limit){
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
	public AttachmentItemHelper limit(int start ,int limit){
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
	*Name:附件文件ID
	*Comment:附件文件ID
	*/

	public AttachmentItemProperty<Long> getItemId(){
		return new AttachmentItemProperty<Long>("itemId",this,Long.class);
	}
	
	

	public AttachmentItemEntityProperty<Mail> getMail(){
		return new AttachmentItemEntityProperty<Mail>("mail",this,Mail.class);
	}
	/**
	 * many-to-one对象Mail的主键 等于
	 * @param mailId 
	 * @return
	 */
	public AttachmentItemHelper MailIdEq(Long mailId){
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
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/

	public AttachmentItemStringProperty getBulkId(){
		return new AttachmentItemStringProperty("bulkId",this);
	}
	
	/**
	*Name:创建人ID
	*Comment:上传这个附件的传阅对象的ID
	*/

	public AttachmentItemProperty<Long> getUserId(){
		return new AttachmentItemProperty<Long>("userId",this,Long.class);
	}
	
	
	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public AttachmentItemStringProperty getCreator(){
		return new AttachmentItemStringProperty("creator",this);
	}
	
	
	/**
	*Name:创建时间
	*Comment:创建附件的时间
	*/

	public AttachmentItemDateProperty getCreateTime(){
		return new AttachmentItemDateProperty("createTime",this);
	}
	
	
	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/

	public AttachmentItemStringProperty getFileName(){
		return new AttachmentItemStringProperty("fileName",this);
	}
	
	
	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/

	public AttachmentItemStringProperty getFileCategory(){
		return new AttachmentItemStringProperty("fileCategory",this);
	}
	
	
	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/

	public AttachmentItemStringProperty getSaveName(){
		return new AttachmentItemStringProperty("saveName",this);
	}
	
	
	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/

	public AttachmentItemStringProperty getUrlPath(){
		return new AttachmentItemStringProperty("urlPath",this);
	}
	
	
	/**
	*Name:是否实体附件
	*Comment:和实体绑定前，为false，绑定后为true
没有和实体绑定的附件，创建24小时之后都应该被删除
	*/

	public AttachmentItemProperty<Boolean> getAttached(){
		return new AttachmentItemProperty<Boolean>("attached",this,ClassUtils.wrapperToPrimitive(Boolean.class));
	}
	
	/**
	*Name:状态
	*Comment:0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)
	*/

	public AttachmentItemProperty<Integer> getState(){
		return new AttachmentItemProperty<Integer>("state",this,Integer.class);
	}
	
	
	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/

	public AttachmentItemStringProperty getItemSize(){
		return new AttachmentItemStringProperty("itemSize",this);
	}
	
	/**
	*Name:网盘文件ID
	*Comment:存放文件上传到网盘的ID
	*/

	public AttachmentItemProperty<Long> getItemNeid(){
		return new AttachmentItemProperty<Long>("itemNeid",this,Long.class);
	}
	
	
	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/

	public AttachmentItemStringProperty getItemRev(){
		return new AttachmentItemStringProperty("itemRev",this);
	}
	
	/**
	*Name:区别
	*Comment:区别附件存放在那个位置: 个人  还是  企业 (默认是个人)
	*/

	public AttachmentItemProperty<Integer> getItemDifferentiate(){
		return new AttachmentItemProperty<Integer>("itemDifferentiate",this,Integer.class);
	}
	
	
	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/

	public AttachmentItemStringProperty getLocalhostUrlPath(){
		return new AttachmentItemStringProperty("localhostUrlPath",this);
	}
	
	
	/**
	*Name:更新时间
	*Comment:更新时间(主要用于localhost_url_path的字段更新)
	*/

	public AttachmentItemDateProperty getUpdateTime(){
		return new AttachmentItemDateProperty("updateTime",this);
	}
	
	

	public AttachmentItemListProperty getUpdateRecords(){
		return new AttachmentItemListProperty("updateRecords",this);
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
	public AttachmentItemHelper inAnywhere(String value,boolean eq){
		this.startOr();
		if (eq) {
			getBulkId().Eq(value);
			getCreator().Eq(value);
			getFileName().Eq(value);
			getFileCategory().Eq(value);
			getSaveName().Eq(value);
			getUrlPath().Eq(value);
			getItemSize().Eq(value);
			getItemRev().Eq(value);
			getLocalhostUrlPath().Eq(value);
		} else {
			getBulkId().Like(value);
			getCreator().Like(value);
			getFileName().Like(value);
			getFileCategory().Like(value);
			getSaveName().Like(value);
			getUrlPath().Like(value);
			getItemSize().Like(value);
			getItemRev().Like(value);
			getLocalhostUrlPath().Like(value);
		}
		this.stopOr();
		return this;
	}
	
	
	private UpdateListener<AttachmentItem> updateListener;
	/**
	 * 监听调用each/eachInPage和scrollResult方法修改的bean。
	 * @param listener
	 * @return
	 */
	public AttachmentItemHelper onUpdate(UpdateListener<AttachmentItem> listener){
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
	public AttachmentItemHelper sleepInteval(int interval){
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
	public AttachmentItemHelper sort(String dir,String propName){
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
	
	private void afterEachUpdate(AttachmentItem bean, AttachmentItem oldBean) {
		if(updateListener != null){
			updateListener.each(oldBean, bean);
		}
	}
	
	private AttachmentItem beforeEachUpdate(AttachmentItem bean) {
		AttachmentItem oldBean = null;
		if(updateListener != null){
			oldBean = new AttachmentItem();
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
