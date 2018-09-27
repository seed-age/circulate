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
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.sloa.db.vo.Menu;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class MenuServiceImpl implements MenuService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Menu> getAll() {
		return (List<Menu>) this.daoHandler.getMenuDAO().findWithCriteria(DetachedCriteria.forClass(Menu.class));
	}

	public List<Menu> getAllWithOrderBy(OrderBy o) {
		return (List<Menu>) this.daoHandler.getMenuDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Menu.class)));
	}

	public PageList<Menu> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Menu> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Menu> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Menu> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Menu> getByPage(int pageSize,int currentPage) {
		return (PageList<Menu>) this.daoHandler.getMenuDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Menu.class),currentPage,pageSize);
	}

	public PageScroll<Menu> getPageScroll(int start,int limit) {
		return (PageScroll<Menu>) this.daoHandler.getMenuDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Menu.class),start,limit);
	}

	public PageList<Menu> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Menu>) this.daoHandler.getMenuDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Menu.class)),currentPage,pageSize);
	}

	public PageScroll<Menu> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Menu>) this.daoHandler.getMenuDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Menu.class)),start,limit);
	}

	public PageList<Menu> findByExampleByPage(Menu vo,int pageSize,int currentPage) {
		return (PageList<Menu>) this.daoHandler.getMenuDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Menu> findByExampleByPageScroll(Menu vo,int start,int limit) {
		return (PageScroll<Menu>) this.daoHandler.getMenuDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Menu> findByExampleByPageWithOrderBy(Menu vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Menu>) this.daoHandler.getMenuDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Menu> findByExampleByPageScrollWithOrderBy(Menu vo,int start,int limit,OrderBy o) {
		return (PageScroll<Menu>) this.daoHandler.getMenuDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Menu> findByExampleByDefaultPage(Menu vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Menu> findByExampleByDefaultPageScroll(Menu vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Menu> findByExampleByDefaultPageWithOrderBy(Menu vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Menu> findByExampleByDefaultPageScrollWithOrderBy(Menu vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Menu> findByExample(Menu vo) {
		return (List<Menu>) this.daoHandler.getMenuDAO().findExactLike(vo);
	}

	public List<Menu> findByExampleWithOrderBy(Menu vo,OrderBy o) {
		return (List<Menu>) this.daoHandler.getMenuDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Menu findById(Long id) {
		return this.daoHandler.getMenuDAO().findById(id);
	}

	public List<Menu> findByIds(Long[] ids) {
		List<Menu> result = new ArrayList<Menu>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Menu vo = this.daoHandler.getMenuDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Menu> findByIdsIncludeNull(Long[] ids) {
		List<Menu> result = new ArrayList<Menu>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Menu vo = this.daoHandler.getMenuDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Menu> findByProperty(String propName,Object value) {
		return this.daoHandler.getMenuDAO().findByProperty(propName,value);
	}

	public List<Menu> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Menu>) this.daoHandler.getMenuDAO().findByPropertyWithOrderBy(Menu.class, propName, value, o);
	}

	public PageList<Menu> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Menu>) this.daoHandler.getMenuDAO().findByPropertyByPage(Menu.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Menu> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Menu>) this.daoHandler.getMenuDAO().findByPropertyByPageScroll(Menu.class,propName,value,start,limit);
	}

	public PageList<Menu> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Menu>) this.daoHandler.getMenuDAO().findByPropertyByPageWithOrderBy(Menu.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Menu> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Menu>) this.daoHandler.getMenuDAO().findByPropertyByPageScrollWithOrderBy(Menu.class,propName,value,start,limit,o);
	}

	public PageList<Menu> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Menu> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Menu> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Menu> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Menu> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Menu>) this.daoHandler.getMenuDAO().findWithCriteria(dc);
	}

	public PageList<Menu> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Menu>) this.daoHandler.getMenuDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Menu> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Menu>) this.daoHandler.getMenuDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Menu> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Menu> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Menu vo) {
		this.daoHandler.getMenuDAO().update(vo);
	}

	public void saveOrUpdate(Menu vo) {
		this.daoHandler.getMenuDAO().saveOrUpdate(vo);
	}

	public void add(Menu vo) {
		this.daoHandler.getMenuDAO().save(vo);
	}

	public void delete(Menu vo) {
		this.daoHandler.getMenuDAO().delete(vo);
	}

	public void deleteById(Long id) {
		this.daoHandler.getMenuDAO().delete(Menu.class, id);
	}

	public void deleteByIdList(List<Long> ids) {
		this.daoHandler.getMenuDAO().deleteListByPk(Menu.class, ids);
	}

	public void deleteByIdArray(Long[] ids) {
		this.daoHandler.getMenuDAO().deleteListByPk(Menu.class, ids);
	}

	public void deleteList(List<Menu> voList) {
		this.daoHandler.getMenuDAO().deleteList(voList);
	}

	public boolean validate(Menu vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Menu.class);
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
		return this.daoHandler.getMenuDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getMenuDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getMenuDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getMenuDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getMenuDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getMenuDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getMenuDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getMenuDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getMenuDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getMenuDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getMenuDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getMenuDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getMenuDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getMenuDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getMenuDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getMenuDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getMenuDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Menu findUniqueByHQL(String hql, Object[] params){
		return (Menu)this.daoHandler.getMenuDAO().findUniqueByHQL(hql,params);
	}

	public Menu findUniqueByProperty(String propName,Object value){
		return (Menu)this.daoHandler.getMenuDAO().findUniqueByProperty(Menu.class,propName,value);
	}

	public Menu findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Menu)this.daoHandler.getMenuDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getMenuDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Menu> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Menu>) this.daoHandler.getMenuDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Menu getFirstByHQL(String hql,Object[] params){
		List<Menu> l = (List<Menu>) this.daoHandler.getMenuDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Menu)l.get(0);
	}

	public Menu getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Menu> l = (List<Menu>) this.daoHandler.getMenuDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Menu)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Menu vo){
		this.daoHandler.getMenuDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getMenuDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getMenuDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getMenuDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getMenuDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public MenuHelper createHelper(){
		return new MenuHelper((MenuService)this.getBean("menuService"));
	}

}
