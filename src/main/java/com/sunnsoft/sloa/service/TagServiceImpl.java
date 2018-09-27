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
import com.sunnsoft.sloa.helper.TagHelper;
import com.sunnsoft.sloa.db.vo.Tag;
import com.sunnsoft.sloa.db.handler.DaoHandler;

/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class TagServiceImpl implements TagService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Tag> getAll() {
		return (List<Tag>) this.daoHandler.getTagDAO().findWithCriteria(DetachedCriteria.forClass(Tag.class));
	}

	public List<Tag> getAllWithOrderBy(OrderBy o) {
		return (List<Tag>) this.daoHandler.getTagDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Tag.class)));
	}

	public PageList<Tag> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Tag> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Tag> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Tag> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Tag> getByPage(int pageSize,int currentPage) {
		return (PageList<Tag>) this.daoHandler.getTagDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Tag.class),currentPage,pageSize);
	}

	public PageScroll<Tag> getPageScroll(int start,int limit) {
		return (PageScroll<Tag>) this.daoHandler.getTagDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Tag.class),start,limit);
	}

	public PageList<Tag> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Tag>) this.daoHandler.getTagDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Tag.class)),currentPage,pageSize);
	}

	public PageScroll<Tag> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Tag>) this.daoHandler.getTagDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Tag.class)),start,limit);
	}

	public PageList<Tag> findByExampleByPage(Tag vo,int pageSize,int currentPage) {
		return (PageList<Tag>) this.daoHandler.getTagDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Tag> findByExampleByPageScroll(Tag vo,int start,int limit) {
		return (PageScroll<Tag>) this.daoHandler.getTagDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Tag> findByExampleByPageWithOrderBy(Tag vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Tag>) this.daoHandler.getTagDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Tag> findByExampleByPageScrollWithOrderBy(Tag vo,int start,int limit,OrderBy o) {
		return (PageScroll<Tag>) this.daoHandler.getTagDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Tag> findByExampleByDefaultPage(Tag vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Tag> findByExampleByDefaultPageScroll(Tag vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Tag> findByExampleByDefaultPageWithOrderBy(Tag vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Tag> findByExampleByDefaultPageScrollWithOrderBy(Tag vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Tag> findByExample(Tag vo) {
		return (List<Tag>) this.daoHandler.getTagDAO().findExactLike(vo);
	}

	public List<Tag> findByExampleWithOrderBy(Tag vo,OrderBy o) {
		return (List<Tag>) this.daoHandler.getTagDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Tag findById(Long id) {
		return this.daoHandler.getTagDAO().findById(id);
	}

	public List<Tag> findByIds(Long[] ids) {
		List<Tag> result = new ArrayList<Tag>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Tag vo = this.daoHandler.getTagDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Tag> findByIdsIncludeNull(Long[] ids) {
		List<Tag> result = new ArrayList<Tag>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Tag vo = this.daoHandler.getTagDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Tag> findByProperty(String propName,Object value) {
		return this.daoHandler.getTagDAO().findByProperty(propName,value);
	}

	public List<Tag> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Tag>) this.daoHandler.getTagDAO().findByPropertyWithOrderBy(Tag.class, propName, value, o);
	}

	public PageList<Tag> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Tag>) this.daoHandler.getTagDAO().findByPropertyByPage(Tag.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Tag> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Tag>) this.daoHandler.getTagDAO().findByPropertyByPageScroll(Tag.class,propName,value,start,limit);
	}

	public PageList<Tag> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Tag>) this.daoHandler.getTagDAO().findByPropertyByPageWithOrderBy(Tag.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Tag> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Tag>) this.daoHandler.getTagDAO().findByPropertyByPageScrollWithOrderBy(Tag.class,propName,value,start,limit,o);
	}

	public PageList<Tag> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Tag> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Tag> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Tag> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Tag> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Tag>) this.daoHandler.getTagDAO().findWithCriteria(dc);
	}

	public PageList<Tag> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Tag>) this.daoHandler.getTagDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Tag> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Tag>) this.daoHandler.getTagDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Tag> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Tag> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Tag vo) {
		this.daoHandler.getTagDAO().update(vo);
	}

	public void saveOrUpdate(Tag vo) {
		this.daoHandler.getTagDAO().saveOrUpdate(vo);
	}

	public void add(Tag vo) {
		this.daoHandler.getTagDAO().save(vo);
	}

	public void delete(Tag vo) {
		this.daoHandler.getTagDAO().delete(vo);
	}

	public void deleteById(Long id) {
		this.daoHandler.getTagDAO().delete(Tag.class, id);
	}

	public void deleteByIdList(List<Long> ids) {
		this.daoHandler.getTagDAO().deleteListByPk(Tag.class, ids);
	}

	public void deleteByIdArray(Long[] ids) {
		this.daoHandler.getTagDAO().deleteListByPk(Tag.class, ids);
	}

	public void deleteList(List<Tag> voList) {
		this.daoHandler.getTagDAO().deleteList(voList);
	}

	public boolean validate(Tag vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Tag.class);
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
		return this.daoHandler.getTagDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getTagDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getTagDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getTagDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getTagDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getTagDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getTagDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getTagDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getTagDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getTagDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getTagDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getTagDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getTagDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getTagDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getTagDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getTagDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getTagDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Tag findUniqueByHQL(String hql, Object[] params){
		return (Tag)this.daoHandler.getTagDAO().findUniqueByHQL(hql,params);
	}

	public Tag findUniqueByProperty(String propName,Object value){
		return (Tag)this.daoHandler.getTagDAO().findUniqueByProperty(Tag.class,propName,value);
	}

	public Tag findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Tag)this.daoHandler.getTagDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getTagDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Tag> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Tag>) this.daoHandler.getTagDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Tag getFirstByHQL(String hql,Object[] params){
		List<Tag> l = (List<Tag>) this.daoHandler.getTagDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Tag)l.get(0);
	}

	public Tag getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Tag> l = (List<Tag>) this.daoHandler.getTagDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Tag)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Tag vo){
		this.daoHandler.getTagDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getTagDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getTagDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getTagDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getTagDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public TagHelper createHelper(){
		return new TagHelper((TagService)this.getBean("tagService"));
	}

}
