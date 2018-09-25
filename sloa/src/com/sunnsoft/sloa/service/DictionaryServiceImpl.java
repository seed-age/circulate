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
import com.sunnsoft.sloa.helper.DictionaryHelper;
import com.sunnsoft.sloa.db.vo.Dictionary;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class DictionaryServiceImpl implements DictionaryService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Dictionary> getAll() {
		return (List<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteria(DetachedCriteria.forClass(Dictionary.class));
	}

	public List<Dictionary> getAllWithOrderBy(OrderBy o) {
		return (List<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Dictionary.class)));
	}

	public PageList<Dictionary> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Dictionary> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Dictionary> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Dictionary> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Dictionary> getByPage(int pageSize,int currentPage) {
		return (PageList<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Dictionary.class),currentPage,pageSize);
	}

	public PageScroll<Dictionary> getPageScroll(int start,int limit) {
		return (PageScroll<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Dictionary.class),start,limit);
	}

	public PageList<Dictionary> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Dictionary.class)),currentPage,pageSize);
	}

	public PageScroll<Dictionary> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Dictionary.class)),start,limit);
	}

	public PageList<Dictionary> findByExampleByPage(Dictionary vo,int pageSize,int currentPage) {
		return (PageList<Dictionary>) this.daoHandler.getDictionaryDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Dictionary> findByExampleByPageScroll(Dictionary vo,int start,int limit) {
		return (PageScroll<Dictionary>) this.daoHandler.getDictionaryDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Dictionary> findByExampleByPageWithOrderBy(Dictionary vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Dictionary>) this.daoHandler.getDictionaryDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Dictionary> findByExampleByPageScrollWithOrderBy(Dictionary vo,int start,int limit,OrderBy o) {
		return (PageScroll<Dictionary>) this.daoHandler.getDictionaryDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Dictionary> findByExampleByDefaultPage(Dictionary vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Dictionary> findByExampleByDefaultPageScroll(Dictionary vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Dictionary> findByExampleByDefaultPageWithOrderBy(Dictionary vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Dictionary> findByExampleByDefaultPageScrollWithOrderBy(Dictionary vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Dictionary> findByExample(Dictionary vo) {
		return (List<Dictionary>) this.daoHandler.getDictionaryDAO().findExactLike(vo);
	}

	public List<Dictionary> findByExampleWithOrderBy(Dictionary vo,OrderBy o) {
		return (List<Dictionary>) this.daoHandler.getDictionaryDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Dictionary findById(java.lang.Long id) {
		return this.daoHandler.getDictionaryDAO().findById(id);
	}

	public List<Dictionary> findByIds(java.lang.Long[] ids) {
		List<Dictionary> result = new ArrayList<Dictionary>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Dictionary vo = this.daoHandler.getDictionaryDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Dictionary> findByIdsIncludeNull(java.lang.Long[] ids) {
		List<Dictionary> result = new ArrayList<Dictionary>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Dictionary vo = this.daoHandler.getDictionaryDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Dictionary> findByProperty(String propName,Object value) {
		return this.daoHandler.getDictionaryDAO().findByProperty(propName,value);
	}

	public List<Dictionary> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Dictionary>) this.daoHandler.getDictionaryDAO().findByPropertyWithOrderBy(Dictionary.class, propName, value, o);
	}

	public PageList<Dictionary> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Dictionary>) this.daoHandler.getDictionaryDAO().findByPropertyByPage(Dictionary.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Dictionary> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Dictionary>) this.daoHandler.getDictionaryDAO().findByPropertyByPageScroll(Dictionary.class,propName,value,start,limit);
	}

	public PageList<Dictionary> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Dictionary>) this.daoHandler.getDictionaryDAO().findByPropertyByPageWithOrderBy(Dictionary.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Dictionary> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Dictionary>) this.daoHandler.getDictionaryDAO().findByPropertyByPageScrollWithOrderBy(Dictionary.class,propName,value,start,limit,o);
	}

	public PageList<Dictionary> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Dictionary> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Dictionary> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Dictionary> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Dictionary> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteria(dc);
	}

	public PageList<Dictionary> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Dictionary> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Dictionary> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Dictionary> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Dictionary vo) {
		this.daoHandler.getDictionaryDAO().update(vo);
	}

	public void saveOrUpdate(Dictionary vo) {
		this.daoHandler.getDictionaryDAO().saveOrUpdate(vo);
	}

	public void add(Dictionary vo) {
		this.daoHandler.getDictionaryDAO().save(vo);
	}

	public void delete(Dictionary vo) {
		this.daoHandler.getDictionaryDAO().delete(vo);
	}

	public void deleteById(java.lang.Long id) {
		this.daoHandler.getDictionaryDAO().delete(Dictionary.class, id);
	}

	public void deleteByIdList(List<java.lang.Long> ids) {
		this.daoHandler.getDictionaryDAO().deleteListByPk(Dictionary.class, ids);
	}

	public void deleteByIdArray(java.lang.Long[] ids) {
		this.daoHandler.getDictionaryDAO().deleteListByPk(Dictionary.class, ids);
	}

	public void deleteList(List<Dictionary> voList) {
		this.daoHandler.getDictionaryDAO().deleteList(voList);
	}

	public boolean validate(Dictionary vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Dictionary.class);
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
		return this.daoHandler.getDictionaryDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getDictionaryDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getDictionaryDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getDictionaryDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getDictionaryDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getDictionaryDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getDictionaryDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getDictionaryDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getDictionaryDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getDictionaryDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getDictionaryDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getDictionaryDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getDictionaryDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getDictionaryDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getDictionaryDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getDictionaryDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getDictionaryDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Dictionary findUniqueByHQL(String hql, Object[] params){
		return (Dictionary)this.daoHandler.getDictionaryDAO().findUniqueByHQL(hql,params);
	}

	public Dictionary findUniqueByProperty(String propName,Object value){
		return (Dictionary)this.daoHandler.getDictionaryDAO().findUniqueByProperty(Dictionary.class,propName,value);
	}

	public Dictionary findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Dictionary)this.daoHandler.getDictionaryDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getDictionaryDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Dictionary> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Dictionary getFirstByHQL(String hql,Object[] params){
		List<Dictionary> l = (List<Dictionary>) this.daoHandler.getDictionaryDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Dictionary)l.get(0);
	}

	public Dictionary getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Dictionary> l = (List<Dictionary>) this.daoHandler.getDictionaryDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Dictionary)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Dictionary vo){
		this.daoHandler.getDictionaryDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getDictionaryDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getDictionaryDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getDictionaryDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getDictionaryDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public DictionaryHelper createHelper(){
		return new DictionaryHelper((DictionaryService)this.getBean("dictionaryService"));
	}

}
