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
import com.sunnsoft.sloa.helper.ReceiveHelper;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class ReceiveServiceImpl implements ReceiveService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Receive> getAll() {
		return (List<Receive>) this.daoHandler.getReceiveDAO().findWithCriteria(DetachedCriteria.forClass(Receive.class));
	}

	public List<Receive> getAllWithOrderBy(OrderBy o) {
		return (List<Receive>) this.daoHandler.getReceiveDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Receive.class)));
	}

	public PageList<Receive> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Receive> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Receive> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Receive> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Receive> getByPage(int pageSize,int currentPage) {
		return (PageList<Receive>) this.daoHandler.getReceiveDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Receive.class),currentPage,pageSize);
	}

	public PageScroll<Receive> getPageScroll(int start,int limit) {
		return (PageScroll<Receive>) this.daoHandler.getReceiveDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Receive.class),start,limit);
	}

	public PageList<Receive> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Receive>) this.daoHandler.getReceiveDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Receive.class)),currentPage,pageSize);
	}

	public PageScroll<Receive> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Receive>) this.daoHandler.getReceiveDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Receive.class)),start,limit);
	}

	public PageList<Receive> findByExampleByPage(Receive vo,int pageSize,int currentPage) {
		return (PageList<Receive>) this.daoHandler.getReceiveDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Receive> findByExampleByPageScroll(Receive vo,int start,int limit) {
		return (PageScroll<Receive>) this.daoHandler.getReceiveDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Receive> findByExampleByPageWithOrderBy(Receive vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Receive>) this.daoHandler.getReceiveDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Receive> findByExampleByPageScrollWithOrderBy(Receive vo,int start,int limit,OrderBy o) {
		return (PageScroll<Receive>) this.daoHandler.getReceiveDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Receive> findByExampleByDefaultPage(Receive vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Receive> findByExampleByDefaultPageScroll(Receive vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Receive> findByExampleByDefaultPageWithOrderBy(Receive vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Receive> findByExampleByDefaultPageScrollWithOrderBy(Receive vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Receive> findByExample(Receive vo) {
		return (List<Receive>) this.daoHandler.getReceiveDAO().findExactLike(vo);
	}

	public List<Receive> findByExampleWithOrderBy(Receive vo,OrderBy o) {
		return (List<Receive>) this.daoHandler.getReceiveDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Receive findById(java.lang.Long id) {
		return this.daoHandler.getReceiveDAO().findById(id);
	}

	public List<Receive> findByIds(java.lang.Long[] ids) {
		List<Receive> result = new ArrayList<Receive>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Receive vo = this.daoHandler.getReceiveDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Receive> findByIdsIncludeNull(java.lang.Long[] ids) {
		List<Receive> result = new ArrayList<Receive>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Receive vo = this.daoHandler.getReceiveDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Receive> findByProperty(String propName,Object value) {
		return this.daoHandler.getReceiveDAO().findByProperty(propName,value);
	}

	public List<Receive> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Receive>) this.daoHandler.getReceiveDAO().findByPropertyWithOrderBy(Receive.class, propName, value, o);
	}

	public PageList<Receive> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Receive>) this.daoHandler.getReceiveDAO().findByPropertyByPage(Receive.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Receive> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Receive>) this.daoHandler.getReceiveDAO().findByPropertyByPageScroll(Receive.class,propName,value,start,limit);
	}

	public PageList<Receive> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Receive>) this.daoHandler.getReceiveDAO().findByPropertyByPageWithOrderBy(Receive.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Receive> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Receive>) this.daoHandler.getReceiveDAO().findByPropertyByPageScrollWithOrderBy(Receive.class,propName,value,start,limit,o);
	}

	public PageList<Receive> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Receive> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Receive> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Receive> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Receive> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Receive>) this.daoHandler.getReceiveDAO().findWithCriteria(dc);
	}

	public PageList<Receive> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Receive>) this.daoHandler.getReceiveDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Receive> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Receive>) this.daoHandler.getReceiveDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Receive> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Receive> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Receive vo) {
		this.daoHandler.getReceiveDAO().update(vo);
	}

	public void saveOrUpdate(Receive vo) {
		this.daoHandler.getReceiveDAO().saveOrUpdate(vo);
	}

	public void add(Receive vo) {
		this.daoHandler.getReceiveDAO().save(vo);
	}

	public void delete(Receive vo) {
		this.daoHandler.getReceiveDAO().delete(vo);
	}

	public void deleteById(java.lang.Long id) {
		this.daoHandler.getReceiveDAO().delete(Receive.class, id);
	}

	public void deleteByIdList(List<java.lang.Long> ids) {
		this.daoHandler.getReceiveDAO().deleteListByPk(Receive.class, ids);
	}

	public void deleteByIdArray(java.lang.Long[] ids) {
		this.daoHandler.getReceiveDAO().deleteListByPk(Receive.class, ids);
	}

	public void deleteList(List<Receive> voList) {
		this.daoHandler.getReceiveDAO().deleteList(voList);
	}

	public boolean validate(Receive vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Receive.class);
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
		return this.daoHandler.getReceiveDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getReceiveDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getReceiveDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getReceiveDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getReceiveDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getReceiveDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getReceiveDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getReceiveDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getReceiveDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getReceiveDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getReceiveDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getReceiveDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getReceiveDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getReceiveDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getReceiveDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getReceiveDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getReceiveDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Receive findUniqueByHQL(String hql, Object[] params){
		return (Receive)this.daoHandler.getReceiveDAO().findUniqueByHQL(hql,params);
	}

	public Receive findUniqueByProperty(String propName,Object value){
		return (Receive)this.daoHandler.getReceiveDAO().findUniqueByProperty(Receive.class,propName,value);
	}

	public Receive findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Receive)this.daoHandler.getReceiveDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getReceiveDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Receive> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Receive>) this.daoHandler.getReceiveDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Receive getFirstByHQL(String hql,Object[] params){
		List<Receive> l = (List<Receive>) this.daoHandler.getReceiveDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Receive)l.get(0);
	}

	public Receive getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Receive> l = (List<Receive>) this.daoHandler.getReceiveDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Receive)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Receive vo){
		this.daoHandler.getReceiveDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getReceiveDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getReceiveDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getReceiveDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getReceiveDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public ReceiveHelper createHelper(){
		return new ReceiveHelper((ReceiveService)this.getBean("receiveService"));
	}

}
