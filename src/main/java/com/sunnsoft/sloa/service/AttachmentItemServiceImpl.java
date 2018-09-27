/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.context.ApplicationContext;

import org.gteam.db.dao.PageList;
import org.gteam.db.dao.PageScroll;
import org.gteam.db.dao.OrderBy;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.db.dao.BaseDAO;
import org.gteam.config.ConfigHelper;
import com.sunnsoft.sloa.helper.AttachmentItemHelper;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class AttachmentItemServiceImpl implements AttachmentItemService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<AttachmentItem> getAll() {
		return (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteria(DetachedCriteria.forClass(AttachmentItem.class));
	}

	public List<AttachmentItem> getAllWithOrderBy(OrderBy o) {
		return (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(AttachmentItem.class)));
	}

	public PageList<AttachmentItem> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<AttachmentItem> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<AttachmentItem> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<AttachmentItem> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<AttachmentItem> getByPage(int pageSize,int currentPage) {
		return (PageList<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteriaByPage(DetachedCriteria.forClass(AttachmentItem.class),currentPage,pageSize);
	}

	public PageScroll<AttachmentItem> getPageScroll(int start,int limit) {
		return (PageScroll<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(AttachmentItem.class),start,limit);
	}

	public PageList<AttachmentItem> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(AttachmentItem.class)),currentPage,pageSize);
	}

	public PageScroll<AttachmentItem> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(AttachmentItem.class)),start,limit);
	}

	public PageList<AttachmentItem> findByExampleByPage(AttachmentItem vo,int pageSize,int currentPage) {
		return (PageList<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<AttachmentItem> findByExampleByPageScroll(AttachmentItem vo,int start,int limit) {
		return (PageScroll<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<AttachmentItem> findByExampleByPageWithOrderBy(AttachmentItem vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<AttachmentItem> findByExampleByPageScrollWithOrderBy(AttachmentItem vo,int start,int limit,OrderBy o) {
		return (PageScroll<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<AttachmentItem> findByExampleByDefaultPage(AttachmentItem vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<AttachmentItem> findByExampleByDefaultPageScroll(AttachmentItem vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<AttachmentItem> findByExampleByDefaultPageWithOrderBy(AttachmentItem vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<AttachmentItem> findByExampleByDefaultPageScrollWithOrderBy(AttachmentItem vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<AttachmentItem> findByExample(AttachmentItem vo) {
		return (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findExactLike(vo);
	}

	public List<AttachmentItem> findByExampleWithOrderBy(AttachmentItem vo,OrderBy o) {
		return (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findExactLikeWithOrderBy(vo,o);
	}

	public AttachmentItem findById(Long id) {
		return this.daoHandler.getAttachmentItemDAO().findById(id);
	}

	public List<AttachmentItem> findByIds(Long[] ids) {
		List<AttachmentItem> result = new ArrayList<AttachmentItem>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			AttachmentItem vo = this.daoHandler.getAttachmentItemDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<AttachmentItem> findByIdsIncludeNull(Long[] ids) {
		List<AttachmentItem> result = new ArrayList<AttachmentItem>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			AttachmentItem vo = this.daoHandler.getAttachmentItemDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<AttachmentItem> findByProperty(String propName,Object value) {
		return this.daoHandler.getAttachmentItemDAO().findByProperty(propName,value);
	}

	public List<AttachmentItem> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findByPropertyWithOrderBy(AttachmentItem.class, propName, value, o);
	}

	public PageList<AttachmentItem> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findByPropertyByPage(AttachmentItem.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<AttachmentItem> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findByPropertyByPageScroll(AttachmentItem.class,propName,value,start,limit);
	}

	public PageList<AttachmentItem> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findByPropertyByPageWithOrderBy(AttachmentItem.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<AttachmentItem> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findByPropertyByPageScrollWithOrderBy(AttachmentItem.class,propName,value,start,limit,o);
	}

	public PageList<AttachmentItem> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<AttachmentItem> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<AttachmentItem> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<AttachmentItem> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<AttachmentItem> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteria(dc);
	}

	public PageList<AttachmentItem> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<AttachmentItem> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<AttachmentItem> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<AttachmentItem> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(AttachmentItem vo) {
		this.daoHandler.getAttachmentItemDAO().update(vo);
	}

	public void saveOrUpdate(AttachmentItem vo) {
		this.daoHandler.getAttachmentItemDAO().saveOrUpdate(vo);
	}

	public void add(AttachmentItem vo) {
		this.daoHandler.getAttachmentItemDAO().save(vo);
	}

	public void delete(AttachmentItem vo) {
		this.daoHandler.getAttachmentItemDAO().delete(vo);
	}

	public void deleteById(Long id) {
		this.daoHandler.getAttachmentItemDAO().delete(AttachmentItem.class, id);
	}

	public void deleteByIdList(List<Long> ids) {
		this.daoHandler.getAttachmentItemDAO().deleteListByPk(AttachmentItem.class, ids);
	}

	public void deleteByIdArray(Long[] ids) {
		this.daoHandler.getAttachmentItemDAO().deleteListByPk(AttachmentItem.class, ids);
	}

	public void deleteList(List<AttachmentItem> voList) {
		this.daoHandler.getAttachmentItemDAO().deleteList(voList);
	}

	public boolean validate(AttachmentItem vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(AttachmentItem.class);
	}

	public DetachedCriteria createCriteriaWithAssociatePaths(String[] associatePaths){
		DetachedCriteria detachedCriteria = this.createCriteria();
		this.daoHandler.getCommonDAO().setDetachedCriteriaInitPaths(detachedCriteria, associatePaths);
		return detachedCriteria;
	}

	public Object execute(HibernateCallback<?> callBack){
		return this.daoHandler.getCommonDAO().execute(callBack);
	}

	public Object nonTransactionalExecute(HibernateCallback<?> callBack){
		return this.daoHandler.getCommonDAO().execute(callBack);
	}

	public Object executeTransactional(TransactionalCallBack callBack){
		return callBack.execute(this);
	}

	public DaoHandler getDaoHandler(){
		return this.daoHandler;
	}

	public int getRowCount(){
		return this.getRowCountByDc(this.createCriteria());
	}

	public int getRowCountByDc(DetachedCriteria dc){
		return this.daoHandler.getAttachmentItemDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getAttachmentItemDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getAttachmentItemDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getAttachmentItemDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getAttachmentItemDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getAttachmentItemDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getAttachmentItemDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getAttachmentItemDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getAttachmentItemDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getAttachmentItemDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getAttachmentItemDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getAttachmentItemDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getAttachmentItemDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getAttachmentItemDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getAttachmentItemDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getAttachmentItemDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getAttachmentItemDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public AttachmentItem findUniqueByHQL(String hql, Object[] params){
		return (AttachmentItem)this.daoHandler.getAttachmentItemDAO().findUniqueByHQL(hql,params);
	}

	public AttachmentItem findUniqueByProperty(String propName,Object value){
		return (AttachmentItem)this.daoHandler.getAttachmentItemDAO().findUniqueByProperty(AttachmentItem.class,propName,value);
	}

	public AttachmentItem findUniqueByDc(DetachedCriteria detachedCriteria){
		return (AttachmentItem)this.daoHandler.getAttachmentItemDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getAttachmentItemDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<AttachmentItem> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public AttachmentItem getFirstByHQL(String hql,Object[] params){
		List<AttachmentItem> l = (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (AttachmentItem)l.get(0);
	}

	public AttachmentItem getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<AttachmentItem> l = (List<AttachmentItem>) this.daoHandler.getAttachmentItemDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (AttachmentItem)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(AttachmentItem vo){
		this.daoHandler.getAttachmentItemDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getAttachmentItemDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getAttachmentItemDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getAttachmentItemDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getAttachmentItemDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public AttachmentItemHelper createHelper(){
		return new AttachmentItemHelper((AttachmentItemService)this.getBean("attachmentItemService"));
	}

}
