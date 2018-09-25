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
import com.sunnsoft.sloa.helper.UserInfoHelper;
import com.sunnsoft.sloa.db.vo.UserInfo;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class UserInfoServiceImpl implements UserInfoService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<UserInfo> getAll() {
		return (List<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteria(DetachedCriteria.forClass(UserInfo.class));
	}

	public List<UserInfo> getAllWithOrderBy(OrderBy o) {
		return (List<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserInfo.class)));
	}

	public PageList<UserInfo> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<UserInfo> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<UserInfo> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<UserInfo> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<UserInfo> getByPage(int pageSize,int currentPage) {
		return (PageList<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteriaByPage(DetachedCriteria.forClass(UserInfo.class),currentPage,pageSize);
	}

	public PageScroll<UserInfo> getPageScroll(int start,int limit) {
		return (PageScroll<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(UserInfo.class),start,limit);
	}

	public PageList<UserInfo> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserInfo.class)),currentPage,pageSize);
	}

	public PageScroll<UserInfo> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UserInfo.class)),start,limit);
	}

	public PageList<UserInfo> findByExampleByPage(UserInfo vo,int pageSize,int currentPage) {
		return (PageList<UserInfo>) this.daoHandler.getUserInfoDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<UserInfo> findByExampleByPageScroll(UserInfo vo,int start,int limit) {
		return (PageScroll<UserInfo>) this.daoHandler.getUserInfoDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<UserInfo> findByExampleByPageWithOrderBy(UserInfo vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserInfo>) this.daoHandler.getUserInfoDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<UserInfo> findByExampleByPageScrollWithOrderBy(UserInfo vo,int start,int limit,OrderBy o) {
		return (PageScroll<UserInfo>) this.daoHandler.getUserInfoDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<UserInfo> findByExampleByDefaultPage(UserInfo vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<UserInfo> findByExampleByDefaultPageScroll(UserInfo vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<UserInfo> findByExampleByDefaultPageWithOrderBy(UserInfo vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<UserInfo> findByExampleByDefaultPageScrollWithOrderBy(UserInfo vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<UserInfo> findByExample(UserInfo vo) {
		return (List<UserInfo>) this.daoHandler.getUserInfoDAO().findExactLike(vo);
	}

	public List<UserInfo> findByExampleWithOrderBy(UserInfo vo,OrderBy o) {
		return (List<UserInfo>) this.daoHandler.getUserInfoDAO().findExactLikeWithOrderBy(vo,o);
	}

	public UserInfo findById(java.lang.Long id) {
		return this.daoHandler.getUserInfoDAO().findById(id);
	}

	public List<UserInfo> findByIds(java.lang.Long[] ids) {
		List<UserInfo> result = new ArrayList<UserInfo>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			UserInfo vo = this.daoHandler.getUserInfoDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<UserInfo> findByIdsIncludeNull(java.lang.Long[] ids) {
		List<UserInfo> result = new ArrayList<UserInfo>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			UserInfo vo = this.daoHandler.getUserInfoDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<UserInfo> findByProperty(String propName,Object value) {
		return this.daoHandler.getUserInfoDAO().findByProperty(propName,value);
	}

	public List<UserInfo> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<UserInfo>) this.daoHandler.getUserInfoDAO().findByPropertyWithOrderBy(UserInfo.class, propName, value, o);
	}

	public PageList<UserInfo> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<UserInfo>) this.daoHandler.getUserInfoDAO().findByPropertyByPage(UserInfo.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<UserInfo> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<UserInfo>) this.daoHandler.getUserInfoDAO().findByPropertyByPageScroll(UserInfo.class,propName,value,start,limit);
	}

	public PageList<UserInfo> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<UserInfo>) this.daoHandler.getUserInfoDAO().findByPropertyByPageWithOrderBy(UserInfo.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<UserInfo> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<UserInfo>) this.daoHandler.getUserInfoDAO().findByPropertyByPageScrollWithOrderBy(UserInfo.class,propName,value,start,limit,o);
	}

	public PageList<UserInfo> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<UserInfo> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<UserInfo> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<UserInfo> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<UserInfo> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteria(dc);
	}

	public PageList<UserInfo> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<UserInfo> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<UserInfo> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<UserInfo> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(UserInfo vo) {
		this.daoHandler.getUserInfoDAO().update(vo);
	}

	public void saveOrUpdate(UserInfo vo) {
		this.daoHandler.getUserInfoDAO().saveOrUpdate(vo);
	}

	public void add(UserInfo vo) {
		this.daoHandler.getUserInfoDAO().save(vo);
	}

	public void delete(UserInfo vo) {
		this.daoHandler.getUserInfoDAO().delete(vo);
	}

	public void deleteById(java.lang.Long id) {
		this.daoHandler.getUserInfoDAO().delete(UserInfo.class, id);
	}

	public void deleteByIdList(List<java.lang.Long> ids) {
		this.daoHandler.getUserInfoDAO().deleteListByPk(UserInfo.class, ids);
	}

	public void deleteByIdArray(java.lang.Long[] ids) {
		this.daoHandler.getUserInfoDAO().deleteListByPk(UserInfo.class, ids);
	}

	public void deleteList(List<UserInfo> voList) {
		this.daoHandler.getUserInfoDAO().deleteList(voList);
	}

	public boolean validate(UserInfo vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(UserInfo.class);
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
		return this.daoHandler.getUserInfoDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getUserInfoDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getUserInfoDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getUserInfoDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getUserInfoDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getUserInfoDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getUserInfoDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getUserInfoDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getUserInfoDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getUserInfoDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getUserInfoDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getUserInfoDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getUserInfoDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getUserInfoDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getUserInfoDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getUserInfoDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getUserInfoDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public UserInfo findUniqueByHQL(String hql, Object[] params){
		return (UserInfo)this.daoHandler.getUserInfoDAO().findUniqueByHQL(hql,params);
	}

	public UserInfo findUniqueByProperty(String propName,Object value){
		return (UserInfo)this.daoHandler.getUserInfoDAO().findUniqueByProperty(UserInfo.class,propName,value);
	}

	public UserInfo findUniqueByDc(DetachedCriteria detachedCriteria){
		return (UserInfo)this.daoHandler.getUserInfoDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getUserInfoDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<UserInfo> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public UserInfo getFirstByHQL(String hql,Object[] params){
		List<UserInfo> l = (List<UserInfo>) this.daoHandler.getUserInfoDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (UserInfo)l.get(0);
	}

	public UserInfo getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<UserInfo> l = (List<UserInfo>) this.daoHandler.getUserInfoDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (UserInfo)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(UserInfo vo){
		this.daoHandler.getUserInfoDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUserInfoDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getUserInfoDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUserInfoDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getUserInfoDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public UserInfoHelper createHelper(){
		return new UserInfoHelper((UserInfoService)this.getBean("userInfoService"));
	}

}
