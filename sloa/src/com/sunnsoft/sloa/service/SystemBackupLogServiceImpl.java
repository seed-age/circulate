/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import com.sunnsoft.sloa.db.handler.DaoHandler;
import com.sunnsoft.sloa.db.vo.SystemBackupLog;
import com.sunnsoft.sloa.helper.SystemBackupLogHelper;
import org.gteam.config.ConfigHelper;
import org.gteam.db.dao.*;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class SystemBackupLogServiceImpl implements SystemBackupLogService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<SystemBackupLog> getAll() {
		return (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteria(DetachedCriteria.forClass(SystemBackupLog.class));
	}

	public List<SystemBackupLog> getAllWithOrderBy(OrderBy o) {
		return (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(SystemBackupLog.class)));
	}

	public PageList<SystemBackupLog> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<SystemBackupLog> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<SystemBackupLog> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<SystemBackupLog> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<SystemBackupLog> getByPage(int pageSize,int currentPage) {
		return (PageList<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteriaByPage(DetachedCriteria.forClass(SystemBackupLog.class),currentPage,pageSize);
	}

	public PageScroll<SystemBackupLog> getPageScroll(int start,int limit) {
		return (PageScroll<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(SystemBackupLog.class),start,limit);
	}

	public PageList<SystemBackupLog> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(SystemBackupLog.class)),currentPage,pageSize);
	}

	public PageScroll<SystemBackupLog> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(SystemBackupLog.class)),start,limit);
	}

	public PageList<SystemBackupLog> findByExampleByPage(SystemBackupLog vo,int pageSize,int currentPage) {
		return (PageList<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<SystemBackupLog> findByExampleByPageScroll(SystemBackupLog vo,int start,int limit) {
		return (PageScroll<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<SystemBackupLog> findByExampleByPageWithOrderBy(SystemBackupLog vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<SystemBackupLog> findByExampleByPageScrollWithOrderBy(SystemBackupLog vo,int start,int limit,OrderBy o) {
		return (PageScroll<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<SystemBackupLog> findByExampleByDefaultPage(SystemBackupLog vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<SystemBackupLog> findByExampleByDefaultPageScroll(SystemBackupLog vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<SystemBackupLog> findByExampleByDefaultPageWithOrderBy(SystemBackupLog vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<SystemBackupLog> findByExampleByDefaultPageScrollWithOrderBy(SystemBackupLog vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<SystemBackupLog> findByExample(SystemBackupLog vo) {
		return (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findExactLike(vo);
	}

	public List<SystemBackupLog> findByExampleWithOrderBy(SystemBackupLog vo,OrderBy o) {
		return (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findExactLikeWithOrderBy(vo,o);
	}

	public SystemBackupLog findById(java.lang.Long id) {
		return this.daoHandler.getSystemBackupLogDAO().findById(id);
	}

	public List<SystemBackupLog> findByIds(java.lang.Long[] ids) {
		List<SystemBackupLog> result = new ArrayList<SystemBackupLog>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			SystemBackupLog vo = this.daoHandler.getSystemBackupLogDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<SystemBackupLog> findByIdsIncludeNull(java.lang.Long[] ids) {
		List<SystemBackupLog> result = new ArrayList<SystemBackupLog>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			SystemBackupLog vo = this.daoHandler.getSystemBackupLogDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<SystemBackupLog> findByProperty(String propName,Object value) {
		return this.daoHandler.getSystemBackupLogDAO().findByProperty(propName,value);
	}

	public List<SystemBackupLog> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findByPropertyWithOrderBy(SystemBackupLog.class, propName, value, o);
	}

	public PageList<SystemBackupLog> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findByPropertyByPage(SystemBackupLog.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<SystemBackupLog> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findByPropertyByPageScroll(SystemBackupLog.class,propName,value,start,limit);
	}

	public PageList<SystemBackupLog> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findByPropertyByPageWithOrderBy(SystemBackupLog.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<SystemBackupLog> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findByPropertyByPageScrollWithOrderBy(SystemBackupLog.class,propName,value,start,limit,o);
	}

	public PageList<SystemBackupLog> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<SystemBackupLog> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<SystemBackupLog> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<SystemBackupLog> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<SystemBackupLog> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteria(dc);
	}

	public PageList<SystemBackupLog> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<SystemBackupLog> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<SystemBackupLog> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<SystemBackupLog> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(SystemBackupLog vo) {
		this.daoHandler.getSystemBackupLogDAO().update(vo);
	}

	public void saveOrUpdate(SystemBackupLog vo) {
		this.daoHandler.getSystemBackupLogDAO().saveOrUpdate(vo);
	}

	public void add(SystemBackupLog vo) {
		this.daoHandler.getSystemBackupLogDAO().save(vo);
	}

	public void delete(SystemBackupLog vo) {
		this.daoHandler.getSystemBackupLogDAO().delete(vo);
	}

	public void deleteById(java.lang.Long id) {
		this.daoHandler.getSystemBackupLogDAO().delete(SystemBackupLog.class, id);
	}

	public void deleteByIdList(List<java.lang.Long> ids) {
		this.daoHandler.getSystemBackupLogDAO().deleteListByPk(SystemBackupLog.class, ids);
	}

	public void deleteByIdArray(java.lang.Long[] ids) {
		this.daoHandler.getSystemBackupLogDAO().deleteListByPk(SystemBackupLog.class, ids);
	}

	public void deleteList(List<SystemBackupLog> voList) {
		this.daoHandler.getSystemBackupLogDAO().deleteList(voList);
	}

	public boolean validate(SystemBackupLog vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(SystemBackupLog.class);
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
		return this.daoHandler.getSystemBackupLogDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getSystemBackupLogDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getSystemBackupLogDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getSystemBackupLogDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getSystemBackupLogDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getSystemBackupLogDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getSystemBackupLogDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getSystemBackupLogDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getSystemBackupLogDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getSystemBackupLogDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getSystemBackupLogDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getSystemBackupLogDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getSystemBackupLogDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getSystemBackupLogDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getSystemBackupLogDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getSystemBackupLogDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getSystemBackupLogDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public SystemBackupLog findUniqueByHQL(String hql, Object[] params){
		return (SystemBackupLog)this.daoHandler.getSystemBackupLogDAO().findUniqueByHQL(hql,params);
	}

	public SystemBackupLog findUniqueByProperty(String propName,Object value){
		return (SystemBackupLog)this.daoHandler.getSystemBackupLogDAO().findUniqueByProperty(SystemBackupLog.class,propName,value);
	}

	public SystemBackupLog findUniqueByDc(DetachedCriteria detachedCriteria){
		return (SystemBackupLog)this.daoHandler.getSystemBackupLogDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getSystemBackupLogDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<SystemBackupLog> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public SystemBackupLog getFirstByHQL(String hql,Object[] params){
		List<SystemBackupLog> l = (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (SystemBackupLog)l.get(0);
	}

	public SystemBackupLog getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<SystemBackupLog> l = (List<SystemBackupLog>) this.daoHandler.getSystemBackupLogDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (SystemBackupLog)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(SystemBackupLog vo){
		this.daoHandler.getSystemBackupLogDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getSystemBackupLogDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getSystemBackupLogDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getSystemBackupLogDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getSystemBackupLogDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public SystemBackupLogHelper createHelper(){
		return new SystemBackupLogHelper((SystemBackupLogService)this.getBean("systemBackupLogService"));
	}

}
