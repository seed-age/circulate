/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import com.sunnsoft.sloa.db.handler.DaoHandler;
import com.sunnsoft.sloa.db.vo.Hrmdepartment;
import com.sunnsoft.sloa.helper.HrmdepartmentHelper;
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
public class HrmdepartmentServiceImpl implements HrmdepartmentService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Hrmdepartment> getAll() {
		return (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteria(DetachedCriteria.forClass(Hrmdepartment.class));
	}

	public List<Hrmdepartment> getAllWithOrderBy(OrderBy o) {
		return (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Hrmdepartment.class)));
	}

	public PageList<Hrmdepartment> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Hrmdepartment> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Hrmdepartment> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Hrmdepartment> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Hrmdepartment> getByPage(int pageSize,int currentPage) {
		return (PageList<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Hrmdepartment.class),currentPage,pageSize);
	}

	public PageScroll<Hrmdepartment> getPageScroll(int start,int limit) {
		return (PageScroll<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Hrmdepartment.class),start,limit);
	}

	public PageList<Hrmdepartment> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Hrmdepartment.class)),currentPage,pageSize);
	}

	public PageScroll<Hrmdepartment> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Hrmdepartment.class)),start,limit);
	}

	public PageList<Hrmdepartment> findByExampleByPage(Hrmdepartment vo,int pageSize,int currentPage) {
		return (PageList<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Hrmdepartment> findByExampleByPageScroll(Hrmdepartment vo,int start,int limit) {
		return (PageScroll<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Hrmdepartment> findByExampleByPageWithOrderBy(Hrmdepartment vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Hrmdepartment> findByExampleByPageScrollWithOrderBy(Hrmdepartment vo,int start,int limit,OrderBy o) {
		return (PageScroll<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Hrmdepartment> findByExampleByDefaultPage(Hrmdepartment vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Hrmdepartment> findByExampleByDefaultPageScroll(Hrmdepartment vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Hrmdepartment> findByExampleByDefaultPageWithOrderBy(Hrmdepartment vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Hrmdepartment> findByExampleByDefaultPageScrollWithOrderBy(Hrmdepartment vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Hrmdepartment> findByExample(Hrmdepartment vo) {
		return (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findExactLike(vo);
	}

	public List<Hrmdepartment> findByExampleWithOrderBy(Hrmdepartment vo,OrderBy o) {
		return (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Hrmdepartment findById(java.lang.Integer id) {
		return this.daoHandler.getHrmdepartmentDAO().findById(id);
	}

	public List<Hrmdepartment> findByIds(java.lang.Integer[] ids) {
		List<Hrmdepartment> result = new ArrayList<Hrmdepartment>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Hrmdepartment vo = this.daoHandler.getHrmdepartmentDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Hrmdepartment> findByIdsIncludeNull(java.lang.Integer[] ids) {
		List<Hrmdepartment> result = new ArrayList<Hrmdepartment>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Hrmdepartment vo = this.daoHandler.getHrmdepartmentDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Hrmdepartment> findByProperty(String propName,Object value) {
		return this.daoHandler.getHrmdepartmentDAO().findByProperty(propName,value);
	}

	public List<Hrmdepartment> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findByPropertyWithOrderBy(Hrmdepartment.class, propName, value, o);
	}

	public PageList<Hrmdepartment> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findByPropertyByPage(Hrmdepartment.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Hrmdepartment> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findByPropertyByPageScroll(Hrmdepartment.class,propName,value,start,limit);
	}

	public PageList<Hrmdepartment> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findByPropertyByPageWithOrderBy(Hrmdepartment.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Hrmdepartment> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findByPropertyByPageScrollWithOrderBy(Hrmdepartment.class,propName,value,start,limit,o);
	}

	public PageList<Hrmdepartment> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Hrmdepartment> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Hrmdepartment> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Hrmdepartment> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Hrmdepartment> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteria(dc);
	}

	public PageList<Hrmdepartment> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Hrmdepartment> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Hrmdepartment> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Hrmdepartment> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Hrmdepartment vo) {
		this.daoHandler.getHrmdepartmentDAO().update(vo);
	}

	public void saveOrUpdate(Hrmdepartment vo) {
		this.daoHandler.getHrmdepartmentDAO().saveOrUpdate(vo);
	}

	public void add(Hrmdepartment vo) {
		this.daoHandler.getHrmdepartmentDAO().save(vo);
	}

	public void delete(Hrmdepartment vo) {
		this.daoHandler.getHrmdepartmentDAO().delete(vo);
	}

	public void deleteById(java.lang.Integer id) {
		this.daoHandler.getHrmdepartmentDAO().delete(Hrmdepartment.class, id);
	}

	public void deleteByIdList(List<java.lang.Integer> ids) {
		this.daoHandler.getHrmdepartmentDAO().deleteListByPk(Hrmdepartment.class, ids);
	}

	public void deleteByIdArray(java.lang.Integer[] ids) {
		this.daoHandler.getHrmdepartmentDAO().deleteListByPk(Hrmdepartment.class, ids);
	}

	public void deleteList(List<Hrmdepartment> voList) {
		this.daoHandler.getHrmdepartmentDAO().deleteList(voList);
	}

	public boolean validate(Hrmdepartment vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Hrmdepartment.class);
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
		return this.daoHandler.getHrmdepartmentDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getHrmdepartmentDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getHrmdepartmentDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getHrmdepartmentDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getHrmdepartmentDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getHrmdepartmentDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getHrmdepartmentDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getHrmdepartmentDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getHrmdepartmentDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getHrmdepartmentDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getHrmdepartmentDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getHrmdepartmentDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getHrmdepartmentDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getHrmdepartmentDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getHrmdepartmentDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getHrmdepartmentDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getHrmdepartmentDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Hrmdepartment findUniqueByHQL(String hql, Object[] params){
		return (Hrmdepartment)this.daoHandler.getHrmdepartmentDAO().findUniqueByHQL(hql,params);
	}

	public Hrmdepartment findUniqueByProperty(String propName,Object value){
		return (Hrmdepartment)this.daoHandler.getHrmdepartmentDAO().findUniqueByProperty(Hrmdepartment.class,propName,value);
	}

	public Hrmdepartment findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Hrmdepartment)this.daoHandler.getHrmdepartmentDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getHrmdepartmentDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Hrmdepartment> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Hrmdepartment getFirstByHQL(String hql,Object[] params){
		List<Hrmdepartment> l = (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Hrmdepartment)l.get(0);
	}

	public Hrmdepartment getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Hrmdepartment> l = (List<Hrmdepartment>) this.daoHandler.getHrmdepartmentDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Hrmdepartment)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Hrmdepartment vo){
		this.daoHandler.getHrmdepartmentDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getHrmdepartmentDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getHrmdepartmentDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getHrmdepartmentDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getHrmdepartmentDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public HrmdepartmentHelper createHelper(){
		return new HrmdepartmentHelper((HrmdepartmentService)this.getBean("hrmdepartmentService"));
	}

}
