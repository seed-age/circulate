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
import com.sunnsoft.sloa.helper.UpdateRecordHelper;
import com.sunnsoft.sloa.db.vo.UpdateRecord;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class UpdateRecordServiceImpl implements UpdateRecordService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<UpdateRecord> getAll() {
		return (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteria(DetachedCriteria.forClass(UpdateRecord.class));
	}

	public List<UpdateRecord> getAllWithOrderBy(OrderBy o) {
		return (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UpdateRecord.class)));
	}

	public PageList<UpdateRecord> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<UpdateRecord> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<UpdateRecord> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<UpdateRecord> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<UpdateRecord> getByPage(int pageSize,int currentPage) {
		return (PageList<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteriaByPage(DetachedCriteria.forClass(UpdateRecord.class),currentPage,pageSize);
	}

	public PageScroll<UpdateRecord> getPageScroll(int start,int limit) {
		return (PageScroll<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(UpdateRecord.class),start,limit);
	}

	public PageList<UpdateRecord> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UpdateRecord.class)),currentPage,pageSize);
	}

	public PageScroll<UpdateRecord> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(UpdateRecord.class)),start,limit);
	}

	public PageList<UpdateRecord> findByExampleByPage(UpdateRecord vo,int pageSize,int currentPage) {
		return (PageList<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<UpdateRecord> findByExampleByPageScroll(UpdateRecord vo,int start,int limit) {
		return (PageScroll<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<UpdateRecord> findByExampleByPageWithOrderBy(UpdateRecord vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<UpdateRecord> findByExampleByPageScrollWithOrderBy(UpdateRecord vo,int start,int limit,OrderBy o) {
		return (PageScroll<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<UpdateRecord> findByExampleByDefaultPage(UpdateRecord vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<UpdateRecord> findByExampleByDefaultPageScroll(UpdateRecord vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<UpdateRecord> findByExampleByDefaultPageWithOrderBy(UpdateRecord vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<UpdateRecord> findByExampleByDefaultPageScrollWithOrderBy(UpdateRecord vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<UpdateRecord> findByExample(UpdateRecord vo) {
		return (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findExactLike(vo);
	}

	public List<UpdateRecord> findByExampleWithOrderBy(UpdateRecord vo,OrderBy o) {
		return (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findExactLikeWithOrderBy(vo,o);
	}

	public UpdateRecord findById(java.lang.Long id) {
		return this.daoHandler.getUpdateRecordDAO().findById(id);
	}

	public List<UpdateRecord> findByIds(java.lang.Long[] ids) {
		List<UpdateRecord> result = new ArrayList<UpdateRecord>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			UpdateRecord vo = this.daoHandler.getUpdateRecordDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<UpdateRecord> findByIdsIncludeNull(java.lang.Long[] ids) {
		List<UpdateRecord> result = new ArrayList<UpdateRecord>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			UpdateRecord vo = this.daoHandler.getUpdateRecordDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<UpdateRecord> findByProperty(String propName,Object value) {
		return this.daoHandler.getUpdateRecordDAO().findByProperty(propName,value);
	}

	public List<UpdateRecord> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findByPropertyWithOrderBy(UpdateRecord.class, propName, value, o);
	}

	public PageList<UpdateRecord> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findByPropertyByPage(UpdateRecord.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<UpdateRecord> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findByPropertyByPageScroll(UpdateRecord.class,propName,value,start,limit);
	}

	public PageList<UpdateRecord> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findByPropertyByPageWithOrderBy(UpdateRecord.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<UpdateRecord> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findByPropertyByPageScrollWithOrderBy(UpdateRecord.class,propName,value,start,limit,o);
	}

	public PageList<UpdateRecord> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<UpdateRecord> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<UpdateRecord> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<UpdateRecord> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<UpdateRecord> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteria(dc);
	}

	public PageList<UpdateRecord> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<UpdateRecord> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<UpdateRecord> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<UpdateRecord> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(UpdateRecord vo) {
		this.daoHandler.getUpdateRecordDAO().update(vo);
	}

	public void saveOrUpdate(UpdateRecord vo) {
		this.daoHandler.getUpdateRecordDAO().saveOrUpdate(vo);
	}

	public void add(UpdateRecord vo) {
		this.daoHandler.getUpdateRecordDAO().save(vo);
	}

	public void delete(UpdateRecord vo) {
		this.daoHandler.getUpdateRecordDAO().delete(vo);
	}

	public void deleteById(java.lang.Long id) {
		this.daoHandler.getUpdateRecordDAO().delete(UpdateRecord.class, id);
	}

	public void deleteByIdList(List<java.lang.Long> ids) {
		this.daoHandler.getUpdateRecordDAO().deleteListByPk(UpdateRecord.class, ids);
	}

	public void deleteByIdArray(java.lang.Long[] ids) {
		this.daoHandler.getUpdateRecordDAO().deleteListByPk(UpdateRecord.class, ids);
	}

	public void deleteList(List<UpdateRecord> voList) {
		this.daoHandler.getUpdateRecordDAO().deleteList(voList);
	}

	public boolean validate(UpdateRecord vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(UpdateRecord.class);
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
		return this.daoHandler.getUpdateRecordDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getUpdateRecordDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getUpdateRecordDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getUpdateRecordDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getUpdateRecordDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getUpdateRecordDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getUpdateRecordDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getUpdateRecordDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getUpdateRecordDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getUpdateRecordDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getUpdateRecordDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getUpdateRecordDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getUpdateRecordDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getUpdateRecordDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getUpdateRecordDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getUpdateRecordDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getUpdateRecordDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public UpdateRecord findUniqueByHQL(String hql, Object[] params){
		return (UpdateRecord)this.daoHandler.getUpdateRecordDAO().findUniqueByHQL(hql,params);
	}

	public UpdateRecord findUniqueByProperty(String propName,Object value){
		return (UpdateRecord)this.daoHandler.getUpdateRecordDAO().findUniqueByProperty(UpdateRecord.class,propName,value);
	}

	public UpdateRecord findUniqueByDc(DetachedCriteria detachedCriteria){
		return (UpdateRecord)this.daoHandler.getUpdateRecordDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getUpdateRecordDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<UpdateRecord> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public UpdateRecord getFirstByHQL(String hql,Object[] params){
		List<UpdateRecord> l = (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (UpdateRecord)l.get(0);
	}

	public UpdateRecord getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<UpdateRecord> l = (List<UpdateRecord>) this.daoHandler.getUpdateRecordDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (UpdateRecord)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(UpdateRecord vo){
		this.daoHandler.getUpdateRecordDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUpdateRecordDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getUpdateRecordDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getUpdateRecordDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getUpdateRecordDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public UpdateRecordHelper createHelper(){
		return new UpdateRecordHelper((UpdateRecordService)this.getBean("updateRecordService"));
	}

}
