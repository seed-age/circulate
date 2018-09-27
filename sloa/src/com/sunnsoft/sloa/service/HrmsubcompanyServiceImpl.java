/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import com.sunnsoft.sloa.db.handler.DaoHandler;
import com.sunnsoft.sloa.db.vo.Hrmsubcompany;
import com.sunnsoft.sloa.helper.HrmsubcompanyHelper;
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
public class HrmsubcompanyServiceImpl implements HrmsubcompanyService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Hrmsubcompany> getAll() {
		return (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteria(DetachedCriteria.forClass(Hrmsubcompany.class));
	}

	public List<Hrmsubcompany> getAllWithOrderBy(OrderBy o) {
		return (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Hrmsubcompany.class)));
	}

	public PageList<Hrmsubcompany> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Hrmsubcompany> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Hrmsubcompany> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Hrmsubcompany> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Hrmsubcompany> getByPage(int pageSize,int currentPage) {
		return (PageList<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Hrmsubcompany.class),currentPage,pageSize);
	}

	public PageScroll<Hrmsubcompany> getPageScroll(int start,int limit) {
		return (PageScroll<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Hrmsubcompany.class),start,limit);
	}

	public PageList<Hrmsubcompany> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Hrmsubcompany.class)),currentPage,pageSize);
	}

	public PageScroll<Hrmsubcompany> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Hrmsubcompany.class)),start,limit);
	}

	public PageList<Hrmsubcompany> findByExampleByPage(Hrmsubcompany vo,int pageSize,int currentPage) {
		return (PageList<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Hrmsubcompany> findByExampleByPageScroll(Hrmsubcompany vo,int start,int limit) {
		return (PageScroll<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Hrmsubcompany> findByExampleByPageWithOrderBy(Hrmsubcompany vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Hrmsubcompany> findByExampleByPageScrollWithOrderBy(Hrmsubcompany vo,int start,int limit,OrderBy o) {
		return (PageScroll<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Hrmsubcompany> findByExampleByDefaultPage(Hrmsubcompany vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Hrmsubcompany> findByExampleByDefaultPageScroll(Hrmsubcompany vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Hrmsubcompany> findByExampleByDefaultPageWithOrderBy(Hrmsubcompany vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Hrmsubcompany> findByExampleByDefaultPageScrollWithOrderBy(Hrmsubcompany vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Hrmsubcompany> findByExample(Hrmsubcompany vo) {
		return (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findExactLike(vo);
	}

	public List<Hrmsubcompany> findByExampleWithOrderBy(Hrmsubcompany vo,OrderBy o) {
		return (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Hrmsubcompany findById(java.lang.Integer id) {
		return this.daoHandler.getHrmsubcompanyDAO().findById(id);
	}

	public List<Hrmsubcompany> findByIds(java.lang.Integer[] ids) {
		List<Hrmsubcompany> result = new ArrayList<Hrmsubcompany>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Hrmsubcompany vo = this.daoHandler.getHrmsubcompanyDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Hrmsubcompany> findByIdsIncludeNull(java.lang.Integer[] ids) {
		List<Hrmsubcompany> result = new ArrayList<Hrmsubcompany>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Hrmsubcompany vo = this.daoHandler.getHrmsubcompanyDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Hrmsubcompany> findByProperty(String propName,Object value) {
		return this.daoHandler.getHrmsubcompanyDAO().findByProperty(propName,value);
	}

	public List<Hrmsubcompany> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findByPropertyWithOrderBy(Hrmsubcompany.class, propName, value, o);
	}

	public PageList<Hrmsubcompany> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findByPropertyByPage(Hrmsubcompany.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Hrmsubcompany> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findByPropertyByPageScroll(Hrmsubcompany.class,propName,value,start,limit);
	}

	public PageList<Hrmsubcompany> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findByPropertyByPageWithOrderBy(Hrmsubcompany.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Hrmsubcompany> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findByPropertyByPageScrollWithOrderBy(Hrmsubcompany.class,propName,value,start,limit,o);
	}

	public PageList<Hrmsubcompany> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Hrmsubcompany> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Hrmsubcompany> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Hrmsubcompany> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Hrmsubcompany> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteria(dc);
	}

	public PageList<Hrmsubcompany> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Hrmsubcompany> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Hrmsubcompany> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Hrmsubcompany> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Hrmsubcompany vo) {
		this.daoHandler.getHrmsubcompanyDAO().update(vo);
	}

	public void saveOrUpdate(Hrmsubcompany vo) {
		this.daoHandler.getHrmsubcompanyDAO().saveOrUpdate(vo);
	}

	public void add(Hrmsubcompany vo) {
		this.daoHandler.getHrmsubcompanyDAO().save(vo);
	}

	public void delete(Hrmsubcompany vo) {
		this.daoHandler.getHrmsubcompanyDAO().delete(vo);
	}

	public void deleteById(java.lang.Integer id) {
		this.daoHandler.getHrmsubcompanyDAO().delete(Hrmsubcompany.class, id);
	}

	public void deleteByIdList(List<java.lang.Integer> ids) {
		this.daoHandler.getHrmsubcompanyDAO().deleteListByPk(Hrmsubcompany.class, ids);
	}

	public void deleteByIdArray(java.lang.Integer[] ids) {
		this.daoHandler.getHrmsubcompanyDAO().deleteListByPk(Hrmsubcompany.class, ids);
	}

	public void deleteList(List<Hrmsubcompany> voList) {
		this.daoHandler.getHrmsubcompanyDAO().deleteList(voList);
	}

	public boolean validate(Hrmsubcompany vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Hrmsubcompany.class);
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
		return this.daoHandler.getHrmsubcompanyDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getHrmsubcompanyDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getHrmsubcompanyDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getHrmsubcompanyDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getHrmsubcompanyDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getHrmsubcompanyDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getHrmsubcompanyDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getHrmsubcompanyDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getHrmsubcompanyDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getHrmsubcompanyDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getHrmsubcompanyDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getHrmsubcompanyDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getHrmsubcompanyDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getHrmsubcompanyDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getHrmsubcompanyDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getHrmsubcompanyDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getHrmsubcompanyDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Hrmsubcompany findUniqueByHQL(String hql, Object[] params){
		return (Hrmsubcompany)this.daoHandler.getHrmsubcompanyDAO().findUniqueByHQL(hql,params);
	}

	public Hrmsubcompany findUniqueByProperty(String propName,Object value){
		return (Hrmsubcompany)this.daoHandler.getHrmsubcompanyDAO().findUniqueByProperty(Hrmsubcompany.class,propName,value);
	}

	public Hrmsubcompany findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Hrmsubcompany)this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getHrmsubcompanyDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Hrmsubcompany> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Hrmsubcompany getFirstByHQL(String hql,Object[] params){
		List<Hrmsubcompany> l = (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Hrmsubcompany)l.get(0);
	}

	public Hrmsubcompany getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Hrmsubcompany> l = (List<Hrmsubcompany>) this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Hrmsubcompany)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Hrmsubcompany vo){
		this.daoHandler.getHrmsubcompanyDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getHrmsubcompanyDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public HrmsubcompanyHelper createHelper(){
		return new HrmsubcompanyHelper((HrmsubcompanyService)this.getBean("hrmsubcompanyService"));
	}

}
