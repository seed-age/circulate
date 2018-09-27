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
import com.sunnsoft.sloa.helper.SystemLogHelper;
import com.sunnsoft.sloa.db.vo.SystemLog;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class SystemLogServiceImpl implements SystemLogService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<SystemLog> getAll() {
		return (List<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteria(DetachedCriteria.forClass(SystemLog.class));
	}

	public List<SystemLog> getAllWithOrderBy(OrderBy o) {
		return (List<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(SystemLog.class)));
	}

	public PageList<SystemLog> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<SystemLog> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<SystemLog> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<SystemLog> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<SystemLog> getByPage(int pageSize,int currentPage) {
		return (PageList<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteriaByPage(DetachedCriteria.forClass(SystemLog.class),currentPage,pageSize);
	}

	public PageScroll<SystemLog> getPageScroll(int start,int limit) {
		return (PageScroll<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(SystemLog.class),start,limit);
	}

	public PageList<SystemLog> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(SystemLog.class)),currentPage,pageSize);
	}

	public PageScroll<SystemLog> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(SystemLog.class)),start,limit);
	}

	public PageList<SystemLog> findByExampleByPage(SystemLog vo,int pageSize,int currentPage) {
		return (PageList<SystemLog>) this.daoHandler.getSystemLogDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<SystemLog> findByExampleByPageScroll(SystemLog vo,int start,int limit) {
		return (PageScroll<SystemLog>) this.daoHandler.getSystemLogDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<SystemLog> findByExampleByPageWithOrderBy(SystemLog vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<SystemLog>) this.daoHandler.getSystemLogDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<SystemLog> findByExampleByPageScrollWithOrderBy(SystemLog vo,int start,int limit,OrderBy o) {
		return (PageScroll<SystemLog>) this.daoHandler.getSystemLogDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<SystemLog> findByExampleByDefaultPage(SystemLog vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<SystemLog> findByExampleByDefaultPageScroll(SystemLog vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<SystemLog> findByExampleByDefaultPageWithOrderBy(SystemLog vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<SystemLog> findByExampleByDefaultPageScrollWithOrderBy(SystemLog vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<SystemLog> findByExample(SystemLog vo) {
		return (List<SystemLog>) this.daoHandler.getSystemLogDAO().findExactLike(vo);
	}

	public List<SystemLog> findByExampleWithOrderBy(SystemLog vo,OrderBy o) {
		return (List<SystemLog>) this.daoHandler.getSystemLogDAO().findExactLikeWithOrderBy(vo,o);
	}

	public SystemLog findById(Long id) {
		return this.daoHandler.getSystemLogDAO().findById(id);
	}

	public List<SystemLog> findByIds(Long[] ids) {
		List<SystemLog> result = new ArrayList<SystemLog>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			SystemLog vo = this.daoHandler.getSystemLogDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<SystemLog> findByIdsIncludeNull(Long[] ids) {
		List<SystemLog> result = new ArrayList<SystemLog>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			SystemLog vo = this.daoHandler.getSystemLogDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<SystemLog> findByProperty(String propName,Object value) {
		return this.daoHandler.getSystemLogDAO().findByProperty(propName,value);
	}

	public List<SystemLog> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<SystemLog>) this.daoHandler.getSystemLogDAO().findByPropertyWithOrderBy(SystemLog.class, propName, value, o);
	}

	public PageList<SystemLog> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<SystemLog>) this.daoHandler.getSystemLogDAO().findByPropertyByPage(SystemLog.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<SystemLog> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<SystemLog>) this.daoHandler.getSystemLogDAO().findByPropertyByPageScroll(SystemLog.class,propName,value,start,limit);
	}

	public PageList<SystemLog> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<SystemLog>) this.daoHandler.getSystemLogDAO().findByPropertyByPageWithOrderBy(SystemLog.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<SystemLog> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<SystemLog>) this.daoHandler.getSystemLogDAO().findByPropertyByPageScrollWithOrderBy(SystemLog.class,propName,value,start,limit,o);
	}

	public PageList<SystemLog> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<SystemLog> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<SystemLog> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<SystemLog> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<SystemLog> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteria(dc);
	}

	public PageList<SystemLog> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<SystemLog> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<SystemLog> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<SystemLog> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(SystemLog vo) {
		this.daoHandler.getSystemLogDAO().update(vo);
	}

	public void saveOrUpdate(SystemLog vo) {
		this.daoHandler.getSystemLogDAO().saveOrUpdate(vo);
	}

	public void add(SystemLog vo) {
		this.daoHandler.getSystemLogDAO().save(vo);
	}

	public void delete(SystemLog vo) {
		this.daoHandler.getSystemLogDAO().delete(vo);
	}

	public void deleteById(Long id) {
		this.daoHandler.getSystemLogDAO().delete(SystemLog.class, id);
	}

	public void deleteByIdList(List<Long> ids) {
		this.daoHandler.getSystemLogDAO().deleteListByPk(SystemLog.class, ids);
	}

	public void deleteByIdArray(Long[] ids) {
		this.daoHandler.getSystemLogDAO().deleteListByPk(SystemLog.class, ids);
	}

	public void deleteList(List<SystemLog> voList) {
		this.daoHandler.getSystemLogDAO().deleteList(voList);
	}

	public boolean validate(SystemLog vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(SystemLog.class);
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
		return this.daoHandler.getSystemLogDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getSystemLogDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getSystemLogDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getSystemLogDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getSystemLogDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getSystemLogDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getSystemLogDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getSystemLogDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getSystemLogDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getSystemLogDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getSystemLogDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getSystemLogDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getSystemLogDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getSystemLogDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getSystemLogDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getSystemLogDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getSystemLogDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public SystemLog findUniqueByHQL(String hql, Object[] params){
		return (SystemLog)this.daoHandler.getSystemLogDAO().findUniqueByHQL(hql,params);
	}

	public SystemLog findUniqueByProperty(String propName,Object value){
		return (SystemLog)this.daoHandler.getSystemLogDAO().findUniqueByProperty(SystemLog.class,propName,value);
	}

	public SystemLog findUniqueByDc(DetachedCriteria detachedCriteria){
		return (SystemLog)this.daoHandler.getSystemLogDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getSystemLogDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<SystemLog> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public SystemLog getFirstByHQL(String hql,Object[] params){
		List<SystemLog> l = (List<SystemLog>) this.daoHandler.getSystemLogDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (SystemLog)l.get(0);
	}

	public SystemLog getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<SystemLog> l = (List<SystemLog>) this.daoHandler.getSystemLogDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (SystemLog)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(SystemLog vo){
		this.daoHandler.getSystemLogDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getSystemLogDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getSystemLogDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getSystemLogDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getSystemLogDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public SystemLogHelper createHelper(){
		return new SystemLogHelper((SystemLogService)this.getBean("systemLogService"));
	}

}
