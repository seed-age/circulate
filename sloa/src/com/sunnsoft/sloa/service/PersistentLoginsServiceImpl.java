/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import com.sunnsoft.sloa.db.handler.DaoHandler;
import com.sunnsoft.sloa.db.vo.PersistentLogins;
import com.sunnsoft.sloa.helper.PersistentLoginsHelper;
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
public class PersistentLoginsServiceImpl implements PersistentLoginsService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<PersistentLogins> getAll() {
		return (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteria(DetachedCriteria.forClass(PersistentLogins.class));
	}

	public List<PersistentLogins> getAllWithOrderBy(OrderBy o) {
		return (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(PersistentLogins.class)));
	}

	public PageList<PersistentLogins> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<PersistentLogins> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<PersistentLogins> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<PersistentLogins> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<PersistentLogins> getByPage(int pageSize,int currentPage) {
		return (PageList<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteriaByPage(DetachedCriteria.forClass(PersistentLogins.class),currentPage,pageSize);
	}

	public PageScroll<PersistentLogins> getPageScroll(int start,int limit) {
		return (PageScroll<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(PersistentLogins.class),start,limit);
	}

	public PageList<PersistentLogins> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(PersistentLogins.class)),currentPage,pageSize);
	}

	public PageScroll<PersistentLogins> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(PersistentLogins.class)),start,limit);
	}

	public PageList<PersistentLogins> findByExampleByPage(PersistentLogins vo,int pageSize,int currentPage) {
		return (PageList<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<PersistentLogins> findByExampleByPageScroll(PersistentLogins vo,int start,int limit) {
		return (PageScroll<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<PersistentLogins> findByExampleByPageWithOrderBy(PersistentLogins vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<PersistentLogins> findByExampleByPageScrollWithOrderBy(PersistentLogins vo,int start,int limit,OrderBy o) {
		return (PageScroll<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<PersistentLogins> findByExampleByDefaultPage(PersistentLogins vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<PersistentLogins> findByExampleByDefaultPageScroll(PersistentLogins vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<PersistentLogins> findByExampleByDefaultPageWithOrderBy(PersistentLogins vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<PersistentLogins> findByExampleByDefaultPageScrollWithOrderBy(PersistentLogins vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<PersistentLogins> findByExample(PersistentLogins vo) {
		return (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findExactLike(vo);
	}

	public List<PersistentLogins> findByExampleWithOrderBy(PersistentLogins vo,OrderBy o) {
		return (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findExactLikeWithOrderBy(vo,o);
	}

	public PersistentLogins findById(java.lang.String id) {
		return this.daoHandler.getPersistentLoginsDAO().findById(id);
	}

	public List<PersistentLogins> findByIds(java.lang.String[] ids) {
		List<PersistentLogins> result = new ArrayList<PersistentLogins>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			PersistentLogins vo = this.daoHandler.getPersistentLoginsDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<PersistentLogins> findByIdsIncludeNull(java.lang.String[] ids) {
		List<PersistentLogins> result = new ArrayList<PersistentLogins>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			PersistentLogins vo = this.daoHandler.getPersistentLoginsDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<PersistentLogins> findByProperty(String propName,Object value) {
		return this.daoHandler.getPersistentLoginsDAO().findByProperty(propName,value);
	}

	public List<PersistentLogins> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findByPropertyWithOrderBy(PersistentLogins.class, propName, value, o);
	}

	public PageList<PersistentLogins> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findByPropertyByPage(PersistentLogins.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<PersistentLogins> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findByPropertyByPageScroll(PersistentLogins.class,propName,value,start,limit);
	}

	public PageList<PersistentLogins> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findByPropertyByPageWithOrderBy(PersistentLogins.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<PersistentLogins> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findByPropertyByPageScrollWithOrderBy(PersistentLogins.class,propName,value,start,limit,o);
	}

	public PageList<PersistentLogins> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<PersistentLogins> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<PersistentLogins> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<PersistentLogins> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<PersistentLogins> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteria(dc);
	}

	public PageList<PersistentLogins> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<PersistentLogins> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<PersistentLogins> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<PersistentLogins> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(PersistentLogins vo) {
		this.daoHandler.getPersistentLoginsDAO().update(vo);
	}

	public void saveOrUpdate(PersistentLogins vo) {
		this.daoHandler.getPersistentLoginsDAO().saveOrUpdate(vo);
	}

	public void add(PersistentLogins vo) {
		this.daoHandler.getPersistentLoginsDAO().save(vo);
	}

	public void delete(PersistentLogins vo) {
		this.daoHandler.getPersistentLoginsDAO().delete(vo);
	}

	public void deleteById(java.lang.String id) {
		this.daoHandler.getPersistentLoginsDAO().delete(PersistentLogins.class, id);
	}

	public void deleteByIdList(List<java.lang.String> ids) {
		this.daoHandler.getPersistentLoginsDAO().deleteListByPk(PersistentLogins.class, ids);
	}

	public void deleteByIdArray(java.lang.String[] ids) {
		this.daoHandler.getPersistentLoginsDAO().deleteListByPk(PersistentLogins.class, ids);
	}

	public void deleteList(List<PersistentLogins> voList) {
		this.daoHandler.getPersistentLoginsDAO().deleteList(voList);
	}

	public boolean validate(PersistentLogins vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(PersistentLogins.class);
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
		return this.daoHandler.getPersistentLoginsDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getPersistentLoginsDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getPersistentLoginsDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getPersistentLoginsDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getPersistentLoginsDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getPersistentLoginsDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getPersistentLoginsDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getPersistentLoginsDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getPersistentLoginsDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getPersistentLoginsDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getPersistentLoginsDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getPersistentLoginsDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getPersistentLoginsDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getPersistentLoginsDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getPersistentLoginsDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getPersistentLoginsDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getPersistentLoginsDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public PersistentLogins findUniqueByHQL(String hql, Object[] params){
		return (PersistentLogins)this.daoHandler.getPersistentLoginsDAO().findUniqueByHQL(hql,params);
	}

	public PersistentLogins findUniqueByProperty(String propName,Object value){
		return (PersistentLogins)this.daoHandler.getPersistentLoginsDAO().findUniqueByProperty(PersistentLogins.class,propName,value);
	}

	public PersistentLogins findUniqueByDc(DetachedCriteria detachedCriteria){
		return (PersistentLogins)this.daoHandler.getPersistentLoginsDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getPersistentLoginsDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<PersistentLogins> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public PersistentLogins getFirstByHQL(String hql,Object[] params){
		List<PersistentLogins> l = (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (PersistentLogins)l.get(0);
	}

	public PersistentLogins getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<PersistentLogins> l = (List<PersistentLogins>) this.daoHandler.getPersistentLoginsDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (PersistentLogins)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(PersistentLogins vo){
		this.daoHandler.getPersistentLoginsDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getPersistentLoginsDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getPersistentLoginsDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getPersistentLoginsDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getPersistentLoginsDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public PersistentLoginsHelper createHelper(){
		return new PersistentLoginsHelper((PersistentLoginsService)this.getBean("persistentLoginsService"));
	}

}
