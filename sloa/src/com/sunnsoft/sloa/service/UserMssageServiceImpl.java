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
import com.sunnsoft.sloa.helper.UserMssageHelper;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class UserMssageServiceImpl implements UserMssageService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<UserMssage> getAll() {
		return (List<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteria(DetachedCriteria.forClass(UserMssage.class));
	}

	public List<UserMssage> getAllWithOrderBy(OrderBy o) {
		return (List<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserMssage.class)));
	}

	public PageList<UserMssage> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<UserMssage> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<UserMssage> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<UserMssage> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<UserMssage> getByPage(int pageSize,int currentPage) {
		return (PageList<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteriaByPage(DetachedCriteria.forClass(UserMssage.class),currentPage,pageSize);
	}

	public PageScroll<UserMssage> getPageScroll(int start,int limit) {
		return (PageScroll<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(UserMssage.class),start,limit);
	}

	public PageList<UserMssage> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserMssage.class)),currentPage,pageSize);
	}

	public PageScroll<UserMssage> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserMssage.class)),start,limit);
	}

	public PageList<UserMssage> findByExampleByPage(UserMssage vo,int pageSize,int currentPage) {
		return (PageList<UserMssage>) this.daoHandler.getUserMssageDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<UserMssage> findByExampleByPageScroll(UserMssage vo,int start,int limit) {
		return (PageScroll<UserMssage>) this.daoHandler.getUserMssageDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<UserMssage> findByExampleByPageWithOrderBy(UserMssage vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserMssage>) this.daoHandler.getUserMssageDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<UserMssage> findByExampleByPageScrollWithOrderBy(UserMssage vo,int start,int limit,OrderBy o) {
		return (PageScroll<UserMssage>) this.daoHandler.getUserMssageDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<UserMssage> findByExampleByDefaultPage(UserMssage vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<UserMssage> findByExampleByDefaultPageScroll(UserMssage vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<UserMssage> findByExampleByDefaultPageWithOrderBy(UserMssage vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<UserMssage> findByExampleByDefaultPageScrollWithOrderBy(UserMssage vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<UserMssage> findByExample(UserMssage vo) {
		return (List<UserMssage>) this.daoHandler.getUserMssageDAO().findExactLike(vo);
	}

	public List<UserMssage> findByExampleWithOrderBy(UserMssage vo,OrderBy o) {
		return (List<UserMssage>) this.daoHandler.getUserMssageDAO().findExactLikeWithOrderBy(vo,o);
	}

	public UserMssage findById(java.lang.Long id) {
		return this.daoHandler.getUserMssageDAO().findById(id);
	}

	public List<UserMssage> findByIds(java.lang.Long[] ids) {
		List<UserMssage> result = new ArrayList<UserMssage>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			UserMssage vo = this.daoHandler.getUserMssageDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<UserMssage> findByIdsIncludeNull(java.lang.Long[] ids) {
		List<UserMssage> result = new ArrayList<UserMssage>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			UserMssage vo = this.daoHandler.getUserMssageDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<UserMssage> findByProperty(String propName,Object value) {
		return this.daoHandler.getUserMssageDAO().findByProperty(propName,value);
	}

	public List<UserMssage> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<UserMssage>) this.daoHandler.getUserMssageDAO().findByPropertyWithOrderBy(UserMssage.class, propName, value, o);
	}

	public PageList<UserMssage> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<UserMssage>) this.daoHandler.getUserMssageDAO().findByPropertyByPage(UserMssage.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<UserMssage> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<UserMssage>) this.daoHandler.getUserMssageDAO().findByPropertyByPageScroll(UserMssage.class,propName,value,start,limit);
	}

	public PageList<UserMssage> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserMssage>) this.daoHandler.getUserMssageDAO().findByPropertyByPageWithOrderBy(UserMssage.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<UserMssage> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<UserMssage>) this.daoHandler.getUserMssageDAO().findByPropertyByPageScrollWithOrderBy(UserMssage.class,propName,value,start,limit,o);
	}

	public PageList<UserMssage> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<UserMssage> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<UserMssage> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<UserMssage> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<UserMssage> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteria(dc);
	}

	public PageList<UserMssage> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<UserMssage> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<UserMssage> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<UserMssage> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(UserMssage vo) {
		this.daoHandler.getUserMssageDAO().update(vo);
	}

	public void saveOrUpdate(UserMssage vo) {
		this.daoHandler.getUserMssageDAO().saveOrUpdate(vo);
	}

	public void add(UserMssage vo) {
		this.daoHandler.getUserMssageDAO().save(vo);
	}

	public void delete(UserMssage vo) {
		this.daoHandler.getUserMssageDAO().delete(vo);
	}

	public void deleteById(java.lang.Long id) {
		this.daoHandler.getUserMssageDAO().delete(UserMssage.class, id);
	}

	public void deleteByIdList(List<java.lang.Long> ids) {
		this.daoHandler.getUserMssageDAO().deleteListByPk(UserMssage.class, ids);
	}

	public void deleteByIdArray(java.lang.Long[] ids) {
		this.daoHandler.getUserMssageDAO().deleteListByPk(UserMssage.class, ids);
	}

	public void deleteList(List<UserMssage> voList) {
		this.daoHandler.getUserMssageDAO().deleteList(voList);
	}

	public boolean validate(UserMssage vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(UserMssage.class);
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
		return this.daoHandler.getUserMssageDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getUserMssageDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getUserMssageDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getUserMssageDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getUserMssageDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getUserMssageDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getUserMssageDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getUserMssageDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getUserMssageDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getUserMssageDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getUserMssageDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getUserMssageDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getUserMssageDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getUserMssageDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getUserMssageDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getUserMssageDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getUserMssageDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public UserMssage findUniqueByHQL(String hql, Object[] params){
		return (UserMssage)this.daoHandler.getUserMssageDAO().findUniqueByHQL(hql,params);
	}

	public UserMssage findUniqueByProperty(String propName,Object value){
		return (UserMssage)this.daoHandler.getUserMssageDAO().findUniqueByProperty(UserMssage.class,propName,value);
	}

	public UserMssage findUniqueByDc(DetachedCriteria detachedCriteria){
		return (UserMssage)this.daoHandler.getUserMssageDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getUserMssageDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<UserMssage> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public UserMssage getFirstByHQL(String hql,Object[] params){
		List<UserMssage> l = (List<UserMssage>) this.daoHandler.getUserMssageDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (UserMssage)l.get(0);
	}

	public UserMssage getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<UserMssage> l = (List<UserMssage>) this.daoHandler.getUserMssageDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (UserMssage)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(UserMssage vo){
		this.daoHandler.getUserMssageDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUserMssageDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getUserMssageDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUserMssageDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getUserMssageDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public UserMssageHelper createHelper(){
		return new UserMssageHelper((UserMssageService)this.getBean("userMssageService"));
	}

}
