/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.Serializable;

import org.hibernate.criterion.DetachedCriteria;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.context.ApplicationContext;

import org.gteam.db.dao.PageList;
import org.gteam.db.dao.PageScroll;
import org.gteam.db.dao.OrderBy;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.db.dao.BaseDAO;
import org.gteam.config.ConfigHelper;
import com.sunnsoft.sloa.helper.UserCollectionHelper;
import com.sunnsoft.sloa.db.vo.UserCollection;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class UserCollectionServiceImpl implements UserCollectionService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<UserCollection> getAll() {
		return (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteria(DetachedCriteria.forClass(UserCollection.class));
	}

	public List<UserCollection> getAllWithOrderBy(OrderBy o) {
		return (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserCollection.class)));
	}

	public PageList<UserCollection> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<UserCollection> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<UserCollection> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<UserCollection> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<UserCollection> getByPage(int pageSize,int currentPage) {
		return (PageList<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteriaByPage(DetachedCriteria.forClass(UserCollection.class),currentPage,pageSize);
	}

	public PageScroll<UserCollection> getPageScroll(int start,int limit) {
		return (PageScroll<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(UserCollection.class),start,limit);
	}

	public PageList<UserCollection> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserCollection.class)),currentPage,pageSize);
	}

	public PageScroll<UserCollection> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserCollection.class)),start,limit);
	}

	public PageList<UserCollection> findByExampleByPage(UserCollection vo,int pageSize,int currentPage) {
		return (PageList<UserCollection>) this.daoHandler.getUserCollectionDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<UserCollection> findByExampleByPageScroll(UserCollection vo,int start,int limit) {
		return (PageScroll<UserCollection>) this.daoHandler.getUserCollectionDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<UserCollection> findByExampleByPageWithOrderBy(UserCollection vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserCollection>) this.daoHandler.getUserCollectionDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<UserCollection> findByExampleByPageScrollWithOrderBy(UserCollection vo,int start,int limit,OrderBy o) {
		return (PageScroll<UserCollection>) this.daoHandler.getUserCollectionDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<UserCollection> findByExampleByDefaultPage(UserCollection vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<UserCollection> findByExampleByDefaultPageScroll(UserCollection vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<UserCollection> findByExampleByDefaultPageWithOrderBy(UserCollection vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<UserCollection> findByExampleByDefaultPageScrollWithOrderBy(UserCollection vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<UserCollection> findByExample(UserCollection vo) {
		return (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findExactLike(vo);
	}

	public List<UserCollection> findByExampleWithOrderBy(UserCollection vo,OrderBy o) {
		return (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findExactLikeWithOrderBy(vo,o);
	}

	public UserCollection findById(java.lang.Long id) {
		return this.daoHandler.getUserCollectionDAO().findById(id);
	}

	public List<UserCollection> findByIds(java.lang.Long[] ids) {
		List<UserCollection> result = new ArrayList<UserCollection>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			UserCollection vo = this.daoHandler.getUserCollectionDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<UserCollection> findByIdsIncludeNull(java.lang.Long[] ids) {
		List<UserCollection> result = new ArrayList<UserCollection>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			UserCollection vo = this.daoHandler.getUserCollectionDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<UserCollection> findByProperty(String propName,Object value) {
		return this.daoHandler.getUserCollectionDAO().findByProperty(propName,value);
	}

	public List<UserCollection> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findByPropertyWithOrderBy(UserCollection.class, propName, value, o);
	}

	public PageList<UserCollection> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<UserCollection>) this.daoHandler.getUserCollectionDAO().findByPropertyByPage(UserCollection.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<UserCollection> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<UserCollection>) this.daoHandler.getUserCollectionDAO().findByPropertyByPageScroll(UserCollection.class,propName,value,start,limit);
	}

	public PageList<UserCollection> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserCollection>) this.daoHandler.getUserCollectionDAO().findByPropertyByPageWithOrderBy(UserCollection.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<UserCollection> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<UserCollection>) this.daoHandler.getUserCollectionDAO().findByPropertyByPageScrollWithOrderBy(UserCollection.class,propName,value,start,limit,o);
	}

	public PageList<UserCollection> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<UserCollection> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<UserCollection> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<UserCollection> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<UserCollection> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteria(dc);
	}

	public PageList<UserCollection> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<UserCollection> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<UserCollection> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<UserCollection> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(UserCollection vo) {
		this.daoHandler.getUserCollectionDAO().update(vo);
	}

	public void saveOrUpdate(UserCollection vo) {
		this.daoHandler.getUserCollectionDAO().saveOrUpdate(vo);
	}

	public void add(UserCollection vo) {
		this.daoHandler.getUserCollectionDAO().save(vo);
	}

	public void delete(UserCollection vo) {
		this.daoHandler.getUserCollectionDAO().delete(vo);
	}

	public void deleteById(java.lang.Long id) {
		this.daoHandler.getUserCollectionDAO().delete(UserCollection.class, id);
	}

	public void deleteByIdList(List<java.lang.Long> ids) {
		this.daoHandler.getUserCollectionDAO().deleteListByPk(UserCollection.class, ids);
	}

	public void deleteByIdArray(java.lang.Long[] ids) {
		this.daoHandler.getUserCollectionDAO().deleteListByPk(UserCollection.class, ids);
	}

	public void deleteList(List<UserCollection> voList) {
		this.daoHandler.getUserCollectionDAO().deleteList(voList);
	}

	public boolean validate(UserCollection vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(UserCollection.class);
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
		return this.daoHandler.getUserCollectionDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getUserCollectionDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getUserCollectionDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getUserCollectionDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getUserCollectionDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getUserCollectionDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getUserCollectionDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getUserCollectionDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getUserCollectionDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getUserCollectionDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getUserCollectionDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getUserCollectionDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getUserCollectionDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getUserCollectionDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getUserCollectionDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getUserCollectionDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getUserCollectionDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public UserCollection findUniqueByHQL(String hql, Object[] params){
		return (UserCollection)this.daoHandler.getUserCollectionDAO().findUniqueByHQL(hql,params);
	}

	public UserCollection findUniqueByProperty(String propName,Object value){
		return (UserCollection)this.daoHandler.getUserCollectionDAO().findUniqueByProperty(UserCollection.class,propName,value);
	}

	public UserCollection findUniqueByDc(DetachedCriteria detachedCriteria){
		return (UserCollection)this.daoHandler.getUserCollectionDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getUserCollectionDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<UserCollection> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public UserCollection getFirstByHQL(String hql,Object[] params){
		List<UserCollection> l = (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (UserCollection)l.get(0);
	}

	public UserCollection getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<UserCollection> l = (List<UserCollection>) this.daoHandler.getUserCollectionDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (UserCollection)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(UserCollection vo){
		this.daoHandler.getUserCollectionDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUserCollectionDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getUserCollectionDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUserCollectionDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getUserCollectionDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public UserCollectionHelper createHelper(){
		return new UserCollectionHelper((UserCollectionService)this.getBean("userCollectionService"));
	}

}
