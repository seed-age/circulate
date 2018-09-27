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
import com.sunnsoft.sloa.helper.RoleHelper;
import com.sunnsoft.sloa.db.vo.Role;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class RoleServiceImpl implements RoleService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Role> getAll() {
		return (List<Role>) this.daoHandler.getRoleDAO().findWithCriteria(DetachedCriteria.forClass(Role.class));
	}

	public List<Role> getAllWithOrderBy(OrderBy o) {
		return (List<Role>) this.daoHandler.getRoleDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Role.class)));
	}

	public PageList<Role> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Role> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Role> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Role> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Role> getByPage(int pageSize,int currentPage) {
		return (PageList<Role>) this.daoHandler.getRoleDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Role.class),currentPage,pageSize);
	}

	public PageScroll<Role> getPageScroll(int start,int limit) {
		return (PageScroll<Role>) this.daoHandler.getRoleDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Role.class),start,limit);
	}

	public PageList<Role> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Role>) this.daoHandler.getRoleDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Role.class)),currentPage,pageSize);
	}

	public PageScroll<Role> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Role>) this.daoHandler.getRoleDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Role.class)),start,limit);
	}

	public PageList<Role> findByExampleByPage(Role vo,int pageSize,int currentPage) {
		return (PageList<Role>) this.daoHandler.getRoleDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Role> findByExampleByPageScroll(Role vo,int start,int limit) {
		return (PageScroll<Role>) this.daoHandler.getRoleDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Role> findByExampleByPageWithOrderBy(Role vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Role>) this.daoHandler.getRoleDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Role> findByExampleByPageScrollWithOrderBy(Role vo,int start,int limit,OrderBy o) {
		return (PageScroll<Role>) this.daoHandler.getRoleDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Role> findByExampleByDefaultPage(Role vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Role> findByExampleByDefaultPageScroll(Role vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Role> findByExampleByDefaultPageWithOrderBy(Role vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Role> findByExampleByDefaultPageScrollWithOrderBy(Role vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Role> findByExample(Role vo) {
		return (List<Role>) this.daoHandler.getRoleDAO().findExactLike(vo);
	}

	public List<Role> findByExampleWithOrderBy(Role vo,OrderBy o) {
		return (List<Role>) this.daoHandler.getRoleDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Role findById(Long id) {
		return this.daoHandler.getRoleDAO().findById(id);
	}

	public List<Role> findByIds(Long[] ids) {
		List<Role> result = new ArrayList<Role>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Role vo = this.daoHandler.getRoleDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Role> findByIdsIncludeNull(Long[] ids) {
		List<Role> result = new ArrayList<Role>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Role vo = this.daoHandler.getRoleDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Role> findByProperty(String propName,Object value) {
		return this.daoHandler.getRoleDAO().findByProperty(propName,value);
	}

	public List<Role> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Role>) this.daoHandler.getRoleDAO().findByPropertyWithOrderBy(Role.class, propName, value, o);
	}

	public PageList<Role> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Role>) this.daoHandler.getRoleDAO().findByPropertyByPage(Role.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Role> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Role>) this.daoHandler.getRoleDAO().findByPropertyByPageScroll(Role.class,propName,value,start,limit);
	}

	public PageList<Role> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Role>) this.daoHandler.getRoleDAO().findByPropertyByPageWithOrderBy(Role.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Role> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Role>) this.daoHandler.getRoleDAO().findByPropertyByPageScrollWithOrderBy(Role.class,propName,value,start,limit,o);
	}

	public PageList<Role> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Role> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Role> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Role> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Role> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Role>) this.daoHandler.getRoleDAO().findWithCriteria(dc);
	}

	public PageList<Role> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Role>) this.daoHandler.getRoleDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Role> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Role>) this.daoHandler.getRoleDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Role> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Role> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Role vo) {
		this.daoHandler.getRoleDAO().update(vo);
	}

	public void saveOrUpdate(Role vo) {
		this.daoHandler.getRoleDAO().saveOrUpdate(vo);
	}

	public void add(Role vo) {
		this.daoHandler.getRoleDAO().save(vo);
	}

	public void delete(Role vo) {
		this.daoHandler.getRoleDAO().delete(vo);
	}

	public void deleteById(Long id) {
		this.daoHandler.getRoleDAO().delete(Role.class, id);
	}

	public void deleteByIdList(List<Long> ids) {
		this.daoHandler.getRoleDAO().deleteListByPk(Role.class, ids);
	}

	public void deleteByIdArray(Long[] ids) {
		this.daoHandler.getRoleDAO().deleteListByPk(Role.class, ids);
	}

	public void deleteList(List<Role> voList) {
		this.daoHandler.getRoleDAO().deleteList(voList);
	}

	public boolean validate(Role vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Role.class);
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
		return this.daoHandler.getRoleDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getRoleDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getRoleDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getRoleDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getRoleDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getRoleDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getRoleDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getRoleDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getRoleDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getRoleDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getRoleDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getRoleDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getRoleDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getRoleDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getRoleDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getRoleDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getRoleDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Role findUniqueByHQL(String hql, Object[] params){
		return (Role)this.daoHandler.getRoleDAO().findUniqueByHQL(hql,params);
	}

	public Role findUniqueByProperty(String propName,Object value){
		return (Role)this.daoHandler.getRoleDAO().findUniqueByProperty(Role.class,propName,value);
	}

	public Role findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Role)this.daoHandler.getRoleDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getRoleDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Role> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Role>) this.daoHandler.getRoleDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Role getFirstByHQL(String hql,Object[] params){
		List<Role> l = (List<Role>) this.daoHandler.getRoleDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Role)l.get(0);
	}

	public Role getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Role> l = (List<Role>) this.daoHandler.getRoleDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Role)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Role vo){
		this.daoHandler.getRoleDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getRoleDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getRoleDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getRoleDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getRoleDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public RoleHelper createHelper(){
		return new RoleHelper((RoleService)this.getBean("roleService"));
	}

}
