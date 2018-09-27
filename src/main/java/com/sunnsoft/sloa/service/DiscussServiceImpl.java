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
import com.sunnsoft.sloa.helper.DiscussHelper;
import com.sunnsoft.sloa.db.vo.Discuss;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class DiscussServiceImpl implements DiscussService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Discuss> getAll() {
		return (List<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteria(DetachedCriteria.forClass(Discuss.class));
	}

	public List<Discuss> getAllWithOrderBy(OrderBy o) {
		return (List<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Discuss.class)));
	}

	public PageList<Discuss> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Discuss> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Discuss> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Discuss> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Discuss> getByPage(int pageSize,int currentPage) {
		return (PageList<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Discuss.class),currentPage,pageSize);
	}

	public PageScroll<Discuss> getPageScroll(int start,int limit) {
		return (PageScroll<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Discuss.class),start,limit);
	}

	public PageList<Discuss> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Discuss.class)),currentPage,pageSize);
	}

	public PageScroll<Discuss> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Discuss.class)),start,limit);
	}

	public PageList<Discuss> findByExampleByPage(Discuss vo,int pageSize,int currentPage) {
		return (PageList<Discuss>) this.daoHandler.getDiscussDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Discuss> findByExampleByPageScroll(Discuss vo,int start,int limit) {
		return (PageScroll<Discuss>) this.daoHandler.getDiscussDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Discuss> findByExampleByPageWithOrderBy(Discuss vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Discuss>) this.daoHandler.getDiscussDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Discuss> findByExampleByPageScrollWithOrderBy(Discuss vo,int start,int limit,OrderBy o) {
		return (PageScroll<Discuss>) this.daoHandler.getDiscussDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Discuss> findByExampleByDefaultPage(Discuss vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Discuss> findByExampleByDefaultPageScroll(Discuss vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Discuss> findByExampleByDefaultPageWithOrderBy(Discuss vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Discuss> findByExampleByDefaultPageScrollWithOrderBy(Discuss vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Discuss> findByExample(Discuss vo) {
		return (List<Discuss>) this.daoHandler.getDiscussDAO().findExactLike(vo);
	}

	public List<Discuss> findByExampleWithOrderBy(Discuss vo,OrderBy o) {
		return (List<Discuss>) this.daoHandler.getDiscussDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Discuss findById(Long id) {
		return this.daoHandler.getDiscussDAO().findById(id);
	}

	public List<Discuss> findByIds(Long[] ids) {
		List<Discuss> result = new ArrayList<Discuss>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Discuss vo = this.daoHandler.getDiscussDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Discuss> findByIdsIncludeNull(Long[] ids) {
		List<Discuss> result = new ArrayList<Discuss>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Discuss vo = this.daoHandler.getDiscussDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Discuss> findByProperty(String propName,Object value) {
		return this.daoHandler.getDiscussDAO().findByProperty(propName,value);
	}

	public List<Discuss> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Discuss>) this.daoHandler.getDiscussDAO().findByPropertyWithOrderBy(Discuss.class, propName, value, o);
	}

	public PageList<Discuss> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Discuss>) this.daoHandler.getDiscussDAO().findByPropertyByPage(Discuss.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Discuss> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Discuss>) this.daoHandler.getDiscussDAO().findByPropertyByPageScroll(Discuss.class,propName,value,start,limit);
	}

	public PageList<Discuss> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Discuss>) this.daoHandler.getDiscussDAO().findByPropertyByPageWithOrderBy(Discuss.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Discuss> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Discuss>) this.daoHandler.getDiscussDAO().findByPropertyByPageScrollWithOrderBy(Discuss.class,propName,value,start,limit,o);
	}

	public PageList<Discuss> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Discuss> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Discuss> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Discuss> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Discuss> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteria(dc);
	}

	public PageList<Discuss> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Discuss> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Discuss> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Discuss> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Discuss vo) {
		this.daoHandler.getDiscussDAO().update(vo);
	}

	public void saveOrUpdate(Discuss vo) {
		this.daoHandler.getDiscussDAO().saveOrUpdate(vo);
	}

	public void add(Discuss vo) {
		this.daoHandler.getDiscussDAO().save(vo);
	}

	public void delete(Discuss vo) {
		this.daoHandler.getDiscussDAO().delete(vo);
	}

	public void deleteById(Long id) {
		this.daoHandler.getDiscussDAO().delete(Discuss.class, id);
	}

	public void deleteByIdList(List<Long> ids) {
		this.daoHandler.getDiscussDAO().deleteListByPk(Discuss.class, ids);
	}

	public void deleteByIdArray(Long[] ids) {
		this.daoHandler.getDiscussDAO().deleteListByPk(Discuss.class, ids);
	}

	public void deleteList(List<Discuss> voList) {
		this.daoHandler.getDiscussDAO().deleteList(voList);
	}

	public boolean validate(Discuss vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Discuss.class);
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
		return this.daoHandler.getDiscussDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getDiscussDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getDiscussDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getDiscussDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getDiscussDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getDiscussDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getDiscussDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getDiscussDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getDiscussDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getDiscussDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getDiscussDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getDiscussDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getDiscussDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getDiscussDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getDiscussDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getDiscussDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getDiscussDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Discuss findUniqueByHQL(String hql, Object[] params){
		return (Discuss)this.daoHandler.getDiscussDAO().findUniqueByHQL(hql,params);
	}

	public Discuss findUniqueByProperty(String propName,Object value){
		return (Discuss)this.daoHandler.getDiscussDAO().findUniqueByProperty(Discuss.class,propName,value);
	}

	public Discuss findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Discuss)this.daoHandler.getDiscussDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getDiscussDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Discuss> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Discuss getFirstByHQL(String hql,Object[] params){
		List<Discuss> l = (List<Discuss>) this.daoHandler.getDiscussDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Discuss)l.get(0);
	}

	public Discuss getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Discuss> l = (List<Discuss>) this.daoHandler.getDiscussDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Discuss)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Discuss vo){
		this.daoHandler.getDiscussDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getDiscussDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getDiscussDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getDiscussDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getDiscussDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public DiscussHelper createHelper(){
		return new DiscussHelper((DiscussService)this.getBean("discussService"));
	}

}
