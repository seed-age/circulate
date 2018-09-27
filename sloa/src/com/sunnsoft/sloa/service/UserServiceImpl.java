/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import com.sunnsoft.sloa.db.handler.DaoHandler;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.helper.UserHelper;
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
public class UserServiceImpl implements UserService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<User> getAll() {
		return (List<User>) this.daoHandler.getUserDAO().findWithCriteria(DetachedCriteria.forClass(User.class));
	}

	public List<User> getAllWithOrderBy(OrderBy o) {
		return (List<User>) this.daoHandler.getUserDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(User.class)));
	}

	public PageList<User> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<User> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<User> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<User> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<User> getByPage(int pageSize,int currentPage) {
		return (PageList<User>) this.daoHandler.getUserDAO().findWithCriteriaByPage(DetachedCriteria.forClass(User.class),currentPage,pageSize);
	}

	public PageScroll<User> getPageScroll(int start,int limit) {
		return (PageScroll<User>) this.daoHandler.getUserDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(User.class),start,limit);
	}

	public PageList<User> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<User>) this.daoHandler.getUserDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(User.class)),currentPage,pageSize);
	}

	public PageScroll<User> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<User>) this.daoHandler.getUserDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(User.class)),start,limit);
	}

	public PageList<User> findByExampleByPage(User vo,int pageSize,int currentPage) {
		return (PageList<User>) this.daoHandler.getUserDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<User> findByExampleByPageScroll(User vo,int start,int limit) {
		return (PageScroll<User>) this.daoHandler.getUserDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<User> findByExampleByPageWithOrderBy(User vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<User>) this.daoHandler.getUserDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<User> findByExampleByPageScrollWithOrderBy(User vo,int start,int limit,OrderBy o) {
		return (PageScroll<User>) this.daoHandler.getUserDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<User> findByExampleByDefaultPage(User vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<User> findByExampleByDefaultPageScroll(User vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<User> findByExampleByDefaultPageWithOrderBy(User vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<User> findByExampleByDefaultPageScrollWithOrderBy(User vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<User> findByExample(User vo) {
		return (List<User>) this.daoHandler.getUserDAO().findExactLike(vo);
	}

	public List<User> findByExampleWithOrderBy(User vo,OrderBy o) {
		return (List<User>) this.daoHandler.getUserDAO().findExactLikeWithOrderBy(vo,o);
	}

	public User findById(java.lang.Long id) {
		return this.daoHandler.getUserDAO().findById(id);
	}

	public List<User> findByIds(java.lang.Long[] ids) {
		List<User> result = new ArrayList<User>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			User vo = this.daoHandler.getUserDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<User> findByIdsIncludeNull(java.lang.Long[] ids) {
		List<User> result = new ArrayList<User>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			User vo = this.daoHandler.getUserDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<User> findByProperty(String propName,Object value) {
		return this.daoHandler.getUserDAO().findByProperty(propName,value);
	}

	public List<User> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<User>) this.daoHandler.getUserDAO().findByPropertyWithOrderBy(User.class, propName, value, o);
	}

	public PageList<User> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<User>) this.daoHandler.getUserDAO().findByPropertyByPage(User.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<User> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<User>) this.daoHandler.getUserDAO().findByPropertyByPageScroll(User.class,propName,value,start,limit);
	}

	public PageList<User> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<User>) this.daoHandler.getUserDAO().findByPropertyByPageWithOrderBy(User.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<User> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<User>) this.daoHandler.getUserDAO().findByPropertyByPageScrollWithOrderBy(User.class,propName,value,start,limit,o);
	}

	public PageList<User> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<User> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<User> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<User> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<User> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<User>) this.daoHandler.getUserDAO().findWithCriteria(dc);
	}

	public PageList<User> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<User>) this.daoHandler.getUserDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<User> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<User>) this.daoHandler.getUserDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<User> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<User> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(User vo) {
		this.daoHandler.getUserDAO().update(vo);
	}

	public void saveOrUpdate(User vo) {
		this.daoHandler.getUserDAO().saveOrUpdate(vo);
	}

	public void add(User vo) {
		this.daoHandler.getUserDAO().save(vo);
	}

	public void delete(User vo) {
		this.daoHandler.getUserDAO().delete(vo);
	}

	public void deleteById(java.lang.Long id) {
		this.daoHandler.getUserDAO().delete(User.class, id);
	}

	public void deleteByIdList(List<java.lang.Long> ids) {
		this.daoHandler.getUserDAO().deleteListByPk(User.class, ids);
	}

	public void deleteByIdArray(java.lang.Long[] ids) {
		this.daoHandler.getUserDAO().deleteListByPk(User.class, ids);
	}

	public void deleteList(List<User> voList) {
		this.daoHandler.getUserDAO().deleteList(voList);
	}

	public boolean validate(User vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(User.class);
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
		return this.daoHandler.getUserDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getUserDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getUserDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getUserDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getUserDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getUserDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getUserDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getUserDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getUserDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getUserDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getUserDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getUserDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getUserDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getUserDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getUserDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getUserDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getUserDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public User findUniqueByHQL(String hql, Object[] params){
		return (User)this.daoHandler.getUserDAO().findUniqueByHQL(hql,params);
	}

	public User findUniqueByProperty(String propName,Object value){
		return (User)this.daoHandler.getUserDAO().findUniqueByProperty(User.class,propName,value);
	}

	public User findUniqueByDc(DetachedCriteria detachedCriteria){
		return (User)this.daoHandler.getUserDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getUserDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<User> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<User>) this.daoHandler.getUserDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public User getFirstByHQL(String hql,Object[] params){
		List<User> l = (List<User>) this.daoHandler.getUserDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (User)l.get(0);
	}

	public User getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<User> l = (List<User>) this.daoHandler.getUserDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (User)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(User vo){
		this.daoHandler.getUserDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUserDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getUserDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUserDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getUserDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public UserHelper createHelper(){
		return new UserHelper((UserService)this.getBean("userService"));
	}

}
