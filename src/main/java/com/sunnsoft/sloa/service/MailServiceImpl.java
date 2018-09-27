/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import com.sunnsoft.sloa.db.handler.DaoHandler;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.helper.MailHelper;
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
public class MailServiceImpl implements MailService {;

	private DaoHandler daoHandler;

	private ApplicationContext ctx;

	private int defaultPageSize = ConfigHelper.getIntegerValue(ConfigHelper.DAO_DEFAULT_PAGE_SIZE);

	public void setDaoHandler(DaoHandler daoHandler){
		this.daoHandler = daoHandler;
	}
	public List<Mail> getAll() {
		return (List<Mail>) this.daoHandler.getMailDAO().findWithCriteria(DetachedCriteria.forClass(Mail.class));
	}

	public List<Mail> getAllWithOrderBy(OrderBy o) {
		return (List<Mail>) this.daoHandler.getMailDAO().findWithCriteria(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Mail.class)));
	}

	public PageList<Mail> getByDefaultPageSize(int currentPage) {
		return this.getByPage(defaultPageSize,currentPage);
	}

	public PageScroll<Mail> getPageScrollByDefaultPageSize(int start) {
		return this.getPageScroll(start,defaultPageSize);
	}

	public PageList<Mail> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o) {
		return this.getByPageWithOrderBy(defaultPageSize,currentPage,o);
	}

	public PageScroll<Mail> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o) {
		return this.getPageScrollWithOrderBy(start,defaultPageSize,o);
	}

	public PageList<Mail> getByPage(int pageSize,int currentPage) {
		return (PageList<Mail>) this.daoHandler.getMailDAO().findWithCriteriaByPage(DetachedCriteria.forClass(Mail.class),currentPage,pageSize);
	}

	public PageScroll<Mail> getPageScroll(int start,int limit) {
		return (PageScroll<Mail>) this.daoHandler.getMailDAO().findWithCriteriaByPageScroll(DetachedCriteria.forClass(Mail.class),start,limit);
	}

	public PageList<Mail> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o) {
		return (PageList<Mail>) this.daoHandler.getMailDAO().findWithCriteriaByPage(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Mail.class)),currentPage,pageSize);
	}

	public PageScroll<Mail> getPageScrollWithOrderBy(int start,int limit,OrderBy o) {
		return (PageScroll<Mail>) this.daoHandler.getMailDAO().findWithCriteriaByPageScroll(BaseDAO.addOrderToDetachedCriteria(o,DetachedCriteria.forClass(Mail.class)),start,limit);
	}

	public PageList<Mail> findByExampleByPage(Mail vo,int pageSize,int currentPage) {
		return (PageList<Mail>) this.daoHandler.getMailDAO().findExactLikeByPage(vo,currentPage,pageSize);
	}

	public PageScroll<Mail> findByExampleByPageScroll(Mail vo,int start,int limit) {
		return (PageScroll<Mail>) this.daoHandler.getMailDAO().findExactLikeByPageScroll(vo,start,limit);
	}

	public PageList<Mail> findByExampleByPageWithOrderBy(Mail vo,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Mail>) this.daoHandler.getMailDAO().findExactLikeByPageWithOrderBy(vo,currentPage,pageSize,o);
	}

	public PageScroll<Mail> findByExampleByPageScrollWithOrderBy(Mail vo,int start,int limit,OrderBy o) {
		return (PageScroll<Mail>) this.daoHandler.getMailDAO().findExactLikeByPageScrollWithOrderBy(vo,start,limit,o);
	}

	public PageList<Mail> findByExampleByDefaultPage(Mail vo,int currentPage) {
		return this.findByExampleByPage(vo,defaultPageSize,currentPage);
	}

	public PageScroll<Mail> findByExampleByDefaultPageScroll(Mail vo,int start) {
		return this.findByExampleByPageScroll(vo,start,defaultPageSize);
	}

	public PageList<Mail> findByExampleByDefaultPageWithOrderBy(Mail vo,int currentPage,OrderBy o) {
		return this.findByExampleByPageWithOrderBy(vo,defaultPageSize,currentPage,o);
	}

	public PageScroll<Mail> findByExampleByDefaultPageScrollWithOrderBy(Mail vo,int start,OrderBy o) {
		return this.findByExampleByPageScrollWithOrderBy(vo,start,defaultPageSize,o);
	}

	public List<Mail> findByExample(Mail vo) {
		return (List<Mail>) this.daoHandler.getMailDAO().findExactLike(vo);
	}

	public List<Mail> findByExampleWithOrderBy(Mail vo,OrderBy o) {
		return (List<Mail>) this.daoHandler.getMailDAO().findExactLikeWithOrderBy(vo,o);
	}

	public Mail findById(Long id) {
		return this.daoHandler.getMailDAO().findById(id);
	}

	public List<Mail> findByIds(Long[] ids) {
		List<Mail> result = new ArrayList<Mail>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Mail vo = this.daoHandler.getMailDAO().findById(ids[i]);
			if(vo != null)result.add(vo);
		}
		return result;
	}

	public List<Mail> findByIdsIncludeNull(Long[] ids) {
		List<Mail> result = new ArrayList<Mail>();
		if(ids == null) return result;
		for(int i = 0 ; i < ids.length ; i++){
			Mail vo = this.daoHandler.getMailDAO().findById(ids[i]);
			result.add(vo);
		}
		return result;
	}

	public List<Mail> findByProperty(String propName,Object value) {
		return this.daoHandler.getMailDAO().findByProperty(propName,value);
	}

	public List<Mail> findByPropertyWithOrderBy(String propName,Object value,OrderBy o) {
		return (List<Mail>) this.daoHandler.getMailDAO().findByPropertyWithOrderBy(Mail.class, propName, value, o);
	}

	public PageList<Mail> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage) {
		return (PageList<Mail>) this.daoHandler.getMailDAO().findByPropertyByPage(Mail.class,propName,value,currentPage,pageSize);
	}

	public PageScroll<Mail> findByPropertyByPageScroll(String propName,Object value,int start,int limit) {
		return (PageScroll<Mail>) this.daoHandler.getMailDAO().findByPropertyByPageScroll(Mail.class,propName,value,start,limit);
	}

	public PageList<Mail> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o) {
		return (PageList<Mail>) this.daoHandler.getMailDAO().findByPropertyByPageWithOrderBy(Mail.class,propName,value,currentPage,pageSize,o);
	}

	public PageScroll<Mail> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o) {
		return (PageScroll<Mail>) this.daoHandler.getMailDAO().findByPropertyByPageScrollWithOrderBy(Mail.class,propName,value,start,limit,o);
	}

	public PageList<Mail> findByPropertyByDefaultPage(String propName,Object value,int currentPage) {
		return this.findByPropertyByPage(propName,value,defaultPageSize,currentPage);
	}

	public PageScroll<Mail> findByPropertyByDefaultPageScroll(String propName,Object value,int start) {
		return this.findByPropertyByPageScroll(propName,value,start,defaultPageSize);
	}

	public PageList<Mail> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o) {
		return this.findByPropertyByPageWithOrderBy(propName, value, defaultPageSize,currentPage, o);
	}

	public PageScroll<Mail> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o) {
		return this.findByPropertyByPageScrollWithOrderBy(propName, value, start , defaultPageSize, o);
	}

	public List<Mail> findByDetachedCriteria(DetachedCriteria dc) {
		return (List<Mail>) this.daoHandler.getMailDAO().findWithCriteria(dc);
	}

	public PageList<Mail> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage) {
		return (PageList<Mail>) this.daoHandler.getMailDAO().findWithCriteriaByPage(dc,currentPage,pageSize);
	}

	public PageScroll<Mail> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit) {
		return (PageScroll<Mail>) this.daoHandler.getMailDAO().findWithCriteriaByPageScroll(dc,start,limit);
	}

	public PageList<Mail> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage) {
		return this.findByDetachedCriteriaByPage(dc,defaultPageSize,currentPage);
	}

	public PageScroll<Mail> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int start) {
		return this.findByDetachedCriteriaByPageScroll(dc,start,defaultPageSize);
	}

	public void update(Mail vo) {
		this.daoHandler.getMailDAO().update(vo);
	}

	public void saveOrUpdate(Mail vo) {
		this.daoHandler.getMailDAO().saveOrUpdate(vo);
	}

	public void add(Mail vo) {
		this.daoHandler.getMailDAO().save(vo);
	}

	public void delete(Mail vo) {
		this.daoHandler.getMailDAO().delete(vo);
	}

	public void deleteById(Long id) {
		this.daoHandler.getMailDAO().delete(Mail.class, id);
	}

	public void deleteByIdList(List<Long> ids) {
		this.daoHandler.getMailDAO().deleteListByPk(Mail.class, ids);
	}

	public void deleteByIdArray(Long[] ids) {
		this.daoHandler.getMailDAO().deleteListByPk(Mail.class, ids);
	}

	public void deleteList(List<Mail> voList) {
		this.daoHandler.getMailDAO().deleteList(voList);
	}

	public boolean validate(Mail vo) {
		return true;
	}

	public DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(Mail.class);
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
		return this.daoHandler.getMailDAO().getRowCount(dc);
	}

	public int getRowCountByHQL(String hql){
		return this.daoHandler.getMailDAO().getRowCountByHQL(hql);
	}

	public int getRowCountByHQL(String hql, Object[] params){
		return this.daoHandler.getMailDAO().getRowCountByHQL(hql,params);
	}

	public List<?> runHQL(String hql){
		return this.daoHandler.getMailDAO().runHQL(hql);
	}

	public List<?> runHQL(String hql, Object param){
		return this.daoHandler.getMailDAO().runHQL(hql,param);
	}

	public List<?> runHQL(String hql, Object[] params){
		return this.daoHandler.getMailDAO().runHQL(hql,params);
	}

	public int runUpdateHQL(String hql){
		return this.daoHandler.getMailDAO().runUpdateHQL(hql);
	}

	public int runUpdateHQL(String hql, Object param){
		return this.daoHandler.getMailDAO().runUpdateHQL(hql,param);
	}

	public int runUpdateHQL(String hql, Object[] params){
		return this.daoHandler.getMailDAO().runUpdateHQL(hql,params);
	}

	public PageList<?> runHQLByPage(String hql, int pageNo,int pageSize){
		return this.daoHandler.getMailDAO().runHQLByPage(hql,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, int start,int limit){
		return this.daoHandler.getMailDAO().runHQLByPageScroll(hql,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object param, int pageNo, int pageSize){
		return this.daoHandler.getMailDAO().runHQLByPage(hql,param,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object param, int start, int limit){
		return this.daoHandler.getMailDAO().runHQLByPageScroll(hql,param,start,limit);
	}

	public PageList<?> runHQLByPage(String hql, Object[] params, int pageNo,int pageSize){
		return this.daoHandler.getMailDAO().runHQLByPage(hql,params,pageNo,pageSize);
	}

	public PageScroll<?> runHQLByPageScroll(String hql, Object[] params, int start,int limit){
		return this.daoHandler.getMailDAO().runHQLByPageScroll(hql,params,start,limit);
	}

	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params){
		return this.daoHandler.getMailDAO().runUpdateHQLWithNamedParameters(hql,params);
	}

	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult){
		return this.daoHandler.getMailDAO().runFindHQLWithNamedParameters(hql,params,uniqueResult);
	}

	public Mail findUniqueByHQL(String hql, Object[] params){
		return (Mail)this.daoHandler.getMailDAO().findUniqueByHQL(hql,params);
	}

	public Mail findUniqueByProperty(String propName,Object value){
		return (Mail)this.daoHandler.getMailDAO().findUniqueByProperty(Mail.class,propName,value);
	}

	public Mail findUniqueByDc(DetachedCriteria detachedCriteria){
		return (Mail)this.daoHandler.getMailDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size){
		return this.daoHandler.getMailDAO().findWithHQLByLimit(hql,params,begin,size);
	}

	public List<Mail> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size){
		return (List<Mail>) this.daoHandler.getMailDAO().findWithCriteriaByLimit(detachedCriteria,begin,size);
	}

	public Mail getFirstByHQL(String hql,Object[] params){
		List<Mail> l = (List<Mail>) this.daoHandler.getMailDAO().findWithHQLByLimit(hql,params,0,1);
		if(l.size() == 0) return null;
		return (Mail)l.get(0);
	}

	public Mail getFirstByDetachedCriteria(DetachedCriteria detachedCriteria){
		List<Mail> l = (List<Mail>) this.daoHandler.getMailDAO().findWithCriteriaByLimit(detachedCriteria,0,1);
		if(l.size() == 0) return null;
		return (Mail)l.get(0);
	}

	public void setApplicationContext(ApplicationContext ctx){
		this.ctx = ctx;
	}

	public Object getBean(String beanId){
		return this.ctx.getBean(beanId);
	}

	public void refresh(Mail vo){
		this.daoHandler.getMailDAO().refreshEntity(vo);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getMailDAO().findWithCriteriaInUniqueResult(detachedCriteria);
	}

	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start){
		return this.daoHandler.getMailDAO().findWithCriteriaAndStartPointInUniqueResult(detachedCriteria , start);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.daoHandler.getMailDAO().findWithCriteriaInRawListResult(detachedCriteria);
	}

	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria , int start , int limit){
		return this.daoHandler.getMailDAO().findWithCriteriaInRawListResultByLimit(detachedCriteria , start , limit);
	}

	public MailHelper createHelper(){
		return new MailHelper((MailService)this.getBean("mailService"));
	}

}
