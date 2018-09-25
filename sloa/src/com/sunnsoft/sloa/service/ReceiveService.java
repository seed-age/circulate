/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.sunnsoft.sloa.service;

import java.util.List;
import java.util.Map;
import org.gteam.service.IService;
import org.gteam.db.dao.PageList;
import org.gteam.db.dao.PageScroll;
import org.gteam.db.dao.OrderBy;
import org.gteam.db.dao.TransactionalCallBack;
import com.sunnsoft.sloa.db.handler.DaoHandler;

import org.hibernate.criterion.DetachedCriteria;

import org.springframework.orm.hibernate3.HibernateCallback;

import java.io.Serializable;
import com.sunnsoft.sloa.helper.ReceiveHelper;
import com.sunnsoft.sloa.db.vo.Receive;

@SuppressWarnings("unchecked")
public interface ReceiveService extends IService<Receive,java.lang.Long> {
	/**
	 * 获得所有数据记录
	 * @return 包含所有记录java.util.List;
	 */
	public List<Receive> getAll();
	/**
	 * 获得所有数据记录
	 * @param o 排序对象{@link OrderBy}
	 * @return 包含所有记录java.util.List;
	 */
	public List<Receive> getAllWithOrderBy(OrderBy o);
	/**
	 * 返回默认的分页对象，默认页大小参见(system-dao.properties中dao.default.page.size属性设定的值)
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList<Receive> getByDefaultPageSize(int currentPage);
	/**
	 * 返回默认的不带总页数和记录总数的"卷轴分页"对象，默认页大小参见(system-dao.properties中dao.default.page.size属性设定的值)
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> getPageScrollByDefaultPageSize(int start);
	/**
	 * 返回默认的分页对象，默认页大小参见(system-dao.properties中dao.default.page.size属性设定的值)
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList<Receive> getByDefaultPageSizeWithOrderBy(int currentPage,OrderBy o);
	/**
	 * 返回默认的不带总页数和记录总数的"卷轴分页"对象，默认页大小参见(system-dao.properties中dao.default.page.size属性设定的值)
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> getPageScrollByDefaultPageSizeWithOrderBy(int start,OrderBy o);
	/**
	 * 返回指定页面大小的分页对象
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList<Receive> getByPage(int pageSize,int currentPage);
	/**
	 * 起始位置算起，limit条记录，返回不带总页数和记录总数的"卷轴分页"对象
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> getPageScroll(int start,int limit);
	/**
	 * 返回指定页面大小的分页对象
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList<Receive> getByPageWithOrderBy(int pageSize,int currentPage,OrderBy o);
	/**
	 * 起始位置算起，limit条记录，返回不带总页数和记录总数的"卷轴分页"对象
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> getPageScrollWithOrderBy(int start,int limit,OrderBy o);
	/**
	 * 根据给定的vo进行查询，并返回指定页面大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList<Receive> findByExampleByPage(Receive vo,int pageSize,int currentPage);
	/**
	 * 根据给定的vo进行查询，起始位置算起，limit条记录，返回不带总页数和记录总数的"卷轴分页"对象
	 * @param vo 设置了属性的vo对象
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByExampleByPageScroll(Receive vo,int start,int limit);
	/**
	 * 根据给定的vo进行查询，并返回指定页面大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList<Receive> findByExampleByPageWithOrderBy(Receive vo,int pageSize,int currentPage,OrderBy o);
	/**
	 * 根据给定的vo进行查询，起始位置算起，limit条记录，返回不带总页数和记录总数的"卷轴分页"对象
	 * @param vo 设置了属性的vo对象
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByExampleByPageScrollWithOrderBy(Receive vo,int start,int limit,OrderBy o);
	/**
	 * 根据给定的vo进行查询，并返回默认页(system-dao.properties中dao.default.page.size属性设定的值)大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList<Receive> findByExampleByDefaultPage(Receive vo,int currentPage);
	/**
	 * 根据给定的vo进行查询，起始位置算起，并返回默认页(system-dao.properties中dao.default.page.size属性设定的值)大小的不带总页数和记录总数的"卷轴分页"对象
	 * @param vo 设置了属性的vo对象
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByExampleByDefaultPageScroll(Receive vo,int start);
	/**
	 * 根据给定的vo进行查询，并返回默认页(system-dao.properties中dao.default.page.size属性设定的值)大小的分页对象
	 * @param vo 设置了属性的vo对象
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果分页对象 {@link PageList}
	 */
	public PageList<Receive> findByExampleByDefaultPageWithOrderBy(Receive vo,int currentPage,OrderBy o);
	/**
	 * 根据给定的vo进行查询，起始位置算起，并返回默认页(system-dao.properties中dao.default.page.size属性设定的值)大小的不带总页数和记录总数的"卷轴分页"对象
	 * @param vo 设置了属性的vo对象
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByExampleByDefaultPageScrollWithOrderBy(Receive vo,int start,OrderBy o);
	/**
	 * 根据给定的vo进行查询,返回所有符合条件的vo
	 * @param vo 设置了属性的vo对象
	 * @return 结果
	 */
	public List<Receive> findByExample(Receive vo);
	/**
	 * 根据给定的vo进行查询,返回所有符合条件的vo
	 * @param vo 设置了属性的vo对象
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果
	 */
	public List<Receive> findByExampleWithOrderBy(Receive vo,OrderBy o);
	/**
	 * 根据ID查找vo
	 * @param id
	 * @return vo
 	 */
	public Receive findById(java.lang.Long id);
	/**
	 * 根据ID列表查找vo 列表,不包括找不到的对象。
	 * @param ids
	 * @return vo 列表
 	 */
	public List<Receive> findByIds(java.lang.Long[] ids);
	/**
	 * 根据ID列表查找vo 列表，找不到的对象用null代替。
	 * @param ids
	 * @return vo 列表
 	 */
	public List<Receive> findByIdsIncludeNull(java.lang.Long[] ids);
	/**
	 * 根据某个属性的值查找vo
	 * @param propName
	 * @param value
	 * @return 结果列表
 	 */
	public List<Receive> findByProperty(String propName,Object value);
	/**
	 * 根据某个属性的值查找vo，并排序
	 * @param propName
	 * @param value
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果列表
 	 */
	public List<Receive> findByPropertyWithOrderBy(String propName,Object value,OrderBy o);
	/**
	 * 根据某个属性的值查找vo,分页
	 * @param propName
	 * @param value
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList<Receive> findByPropertyByPage(String propName,Object value,int pageSize,int currentPage);
	/**
	 * 根据某个属性的值查找vo，起始位置算起，返回不带总页数和记录总数的"卷轴分页"
	 * @param propName
	 * @param value
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByPropertyByPageScroll(String propName,Object value,int start,int limit);
	/**
	 * 根据某个属性的值查找vo，并排序
	 * @param propName
	 * @param value
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果分页对象
 	 */
	public PageList<Receive> findByPropertyByPageWithOrderBy(String propName,Object value,int pageSize,int currentPage,OrderBy o);
	/**
	 * 根据某个属性的值查找vo，起始位置算起，并排序，返回不带总页数和记录总数的"卷轴分页"
	 * @param propName
	 * @param value
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByPropertyByPageScrollWithOrderBy(String propName,Object value,int start,int limit,OrderBy o);
	/**
	 * 根据某个属性的值查找vo，按默认大小分页
	 * @param propName
	 * @param value
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList<Receive> findByPropertyByDefaultPage(String propName,Object value,int currentPage);
	/**
	 * 根据某个属性的值查找vo，起始位置算起，按默认大小分页，返回不带总页数和记录总数的"卷轴分页"
	 * @param propName
	 * @param value
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByPropertyByDefaultPageScroll(String propName,Object value,int start);
	/**
	 * 根据某个属性的值查找vo，并排序，按默认大小分页
	 * @param propName
	 * @param value
	 * @param currentPage 需要返回的页
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果分页对象
 	 */
	public PageList<Receive> findByPropertyByDefaultPageWithOrderBy(String propName,Object value,int currentPage,OrderBy o);
	/**
	 * 根据某个属性的值查找vo，并排序，起始位置算起，按默认大小分页，返回不带总页数和记录总数的"卷轴分页"
	 * @param propName
	 * @param value
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param o 排序对象{@link OrderBy}
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByPropertyByDefaultPageScrollWithOrderBy(String propName,Object value,int start,OrderBy o);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @return 结果列表
 	 */
	public List<Receive> findByDetachedCriteria(DetachedCriteria dc);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo，并分页
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @param pageSize 页面大小
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList<Receive> findByDetachedCriteriaByPage(DetachedCriteria dc,int pageSize,int currentPage);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo，起始位置算起，返回不带总页数和记录总数的"卷轴分页"
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByDetachedCriteriaByPageScroll(DetachedCriteria dc,int start,int limit);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo，并按默认大小分页
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @param currentPage 需要返回的页
	 * @return 结果分页对象
 	 */
	public PageList<Receive> findByDetachedCriteriaByDefaultPage(DetachedCriteria dc,int currentPage);
	/**
	 * 根据Hibernate的DetachedCriteria对象查找vo，并按默认大小分页，起始位置算起，返回不带总页数和记录总数的"卷轴分页"
	 * @param dc Hibernate的DetachedCriteria对象(标准离线查询对象)
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<Receive> findByDetachedCriteriaByDefaultPageScroll(DetachedCriteria dc,int limit);
	/**
	 * 更新给定的vo，根据vo的id决定更新的是哪个vo
	 * @param vo 给定的vo
	 */
	public void update(Receive vo);
	/**
	 * 更新给定的vo，根据vo的id是否是unsave-value决定更新还是插入vo到数据库
	 * @param vo 给定的vo
	 */
	public void saveOrUpdate(Receive vo);
	/**
	 * 添加给定的vo到数据库
	 * @param vo 给定的vo
	 */
	public void add(Receive vo);
	/**
	 * 根据id从数据库删除vo对应的记录
	 * @param id
	 */
	public void deleteById(java.lang.Long id);
	/**
	 * 根据id列表从数据库删除vo对应的记录
	 * @param ids
	 */
	public void deleteByIdList(List<java.lang.Long> ids);
	/**
	 * 根据id列表从数据库删除vo对应的记录
	 * @param ids
	 */
	public void deleteByIdArray(java.lang.Long[] ids);
	/**
	 * 删除vo列表
	 * @param voList
	 */
	public void deleteList(List<Receive> voList);
	/**
	 * 从数据库删除vo对应的记录
	 * @param vo
	 */
	public void delete(Receive vo);
	/**
	 * 检查vo的合法性，本方法默认是返回true，即所有vo都是合法的。可以根据实际情况在子类改写该方法
	 * @param vo 需要检测的vo
	 * @return
	 */
	public boolean validate(Receive vo);
	/**
	 * 获得离线查询对象的快捷方法
	 * @return 对应 泛型 T的DetachedCriteria 对象
	 */
	public DetachedCriteria createCriteria();
	/**
	 * 获得离线查找对象，并设定初始化对象路径。see also {@link BaseDAO.setDetachedCriteriaInitPaths(DetachedCriteria detachedCriteria,String[] associatePaths)}
	 * 注意，如果使用此方法获得离线查询对象自动带有对应路径的别名，例如xxx.yyy.zzz.vvv,则有别名: xxx,xxx_yyy,xxx_yyy_zzz,xxx_yyy_zzz_vvv ，如果后续设置条件，则必须沿用这些别名
	 * @param associatePaths 以"."分割的属性查询链条（例如xxx.yyy.zzz）所组成的数组
	 * @return 离线查询对象
	 */
	public DetachedCriteria createCriteriaWithAssociatePaths(String[] associatePaths);
	/**
	 * 直接使用Hibernate Session ，处理复杂的查询，例如大数据量迭代的更新、ScrollabelResults控制数据库的cursor等。
	 * @param callBack 
	 * @return 处理结果，可能是null(void)或者List等表示结果的对象
	 */
	public Object execute(HibernateCallback<?> callBack);
	/**
	 * 非事务直接使用直接使用Hibernate Session ，处理复杂的查询，例如大数据量迭代的更新、ScrollabelResults控制数据库的cursor等，避免锁定过大的结果集。
	 * @param callBack 
	 * @return 处理结果，可能是null(void)或者List等表示结果的对象
	 */
	public Object nonTransactionalExecute(HibernateCallback<?> callBack);
	/**
	 * 曝露事务回调接口。
	 * @param callBack 
	 * @return 处理结果或者Json对象等可以直接使用的对象，避免lazy-init、lazy-load异常
	 */
	public Object executeTransactional(TransactionalCallBack callBack);
	/**
	 * 直接获取DaoHandler
	 * @return DaoHandler
	 */
	public DaoHandler getDaoHandler();
	/**
	 * 获得记录行数
	 * @return 行数
	 */
	public int getRowCount();
	/**
	 * 通过DetachedCriteria获得记录行数
	 * @param callBack 
	 * @return 行数
	 */
	public int getRowCountByDc(DetachedCriteria dc);
	/**
	 * 根据HQL获取总数
	 * @param hql 查询hql不必用select count(*) from ...开头，给予合法的HQL语句，程序会自动加上count函数并获取总行数 
	 * @return 行数
	 */
	public int getRowCountByHQL(String hql);
	/**
	 * 根据HQL获取总数
	 * @param hql 查询hql不必用select count(*) from ...开头，给予合法的HQL语句，程序会自动加上count函数并获取总行数 
	 * @param params 可以为null,null表示没有参数
	 * @return 行数
	 */
	public int getRowCountByHQL(String hql,Object[] params);
	/**
	 * 返回hql查询结果
	 * @param hql 查询hql 
	 * @return 
	 */
	public List<?> runHQL(String hql);
	/**
	 * 返回hql查询结果
	 * @param hql 查询hql 
	 * @param param 参数 
	 * @return 
	 */
	public List<?> runHQL(String hql,Object param);
	/**
	 * 返回hql查询结果
	 * @param hql 查询hql 
	 * @param params 按先后参数先后顺序组成的数组
	 * @return 
	 */
	public List<?> runHQL(String hql,Object[] params);
	/**
	 * 批量更新语句执行
	 * @param hql update 或 delete hql 
	 * @return 受影响的行数
	 */
	public int runUpdateHQL(String hql);
	/**
	 * 批量更新语句执行
	 * @param hql update 或 delete hql 带单个“?”参数
	 * @param param 参数 
	 * @return 受影响的行数
	 */
	public int runUpdateHQL(String hql,Object param);
	/**
	 * 批量更新语句执行
	 * @param hql update 或 delete hql 可带多个“?”
	 * @param params 按先后参数先后顺序组成的数组
	 * @return 受影响的行数
	 */
	public int runUpdateHQL(String hql,Object[] params);
	/**
	 * 返回hql查询分页结果
	 * @param hql 查询hql 
	 * @param pageNo 
	 * @param pageSize 
	 * @return 分页对象
	 */
	public PageList<?> runHQLByPage(String hql,int pageNo,int pageSize);
	/**
	 * 返回hql查询结果，起始位置算起，返回不带总页数和记录总数的"卷轴分页"
	 * @param hql 查询hql 
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<?> runHQLByPageScroll(String hql,int start,int limit);
	/**
	 * 返回hql查询结果
	 * @param hql 查询hql 
	 * @param param 单个参数 
	 * @param pageNo 
	 * @param pageSize 
	 * @return 分页对象
	 */
	public PageList<?> runHQLByPage(String hql,Object param,int pageNo,int pageSize);
	/**
	 * 返回hql查询结果，起始位置算起，返回不带总页数和记录总数的"卷轴分页"
	 * @param hql 查询hql 
	 * @param param 单个参数 
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<?> runHQLByPageScroll(String hql,Object param,int start,int limit);
	/**
	 * 返回hql查询结果
	 * @param hql 查询hql 
	 * @param params 按先后参数先后顺序组成的数组 
	 * @param pageNo 
	 * @param pageSize 
	 * @return 分页对象
	 */
	public PageList<?> runHQLByPage(String hql,Object[] params,int pageNo,int pageSize);
	/**
	 * 返回hql查询结果，起始位置算起，返回不带总页数和记录总数的"卷轴分页"
	 * @param hql 查询hql 
	 * @param params 按先后参数先后顺序组成的数组 
	 * @param start 起始位置（第一条记录起始位置是0）
	 * @param limit 从起始位置start开始，取limit 条记录 (包括起始记录)
	 * @return 结果"卷轴分页"对象 {@link PageScroll}
	 */
	public PageScroll<?> runHQLByPageScroll(String hql,Object[] params,int start,int limit);
	/**
	 * Hibernate Named Query方式使用update hql 例如 update  foobar xxx = :value  where yyy in (:value_list),而且支持List和数组参数类型。
	 * 参数是参数名和值组成的Map类型。
	 * @param hql update hql 
	 * @param params 参数名和值组成的Map类型。 
	 * @return 
	 */
	public int runUpdateHQLWithNamedParameters(String hql, Map<String, Object> params);
	/**
	 * Hibernate Named Query方式使用查询 hql 例如 from  foobar  where yyy in (:value_list),而且支持List和数组参数类型。
	 * 参数是参数名和值组成的Map类型。
	 * @param hql 查询 hql 
	 * @param params 参数名和值组成的Map类型。 
	 * @param uniqueResult 是否是唯一值。 
	 * @return 
	 */
	public Object runFindHQLWithNamedParameters(String hql, Map<String, Object> params , boolean uniqueResult);
	/**
	 * 根据HQL查询，只返回唯一结果对象，假如结果不唯一，则抛出异常
	 * @param hql 查询hql 
	 * @param params 可以为Null，Null表示没有HQL参数  
	 * @return 
	 */
	public Receive findUniqueByHQL(String hql,Object[] params);
	/**
	 * 根据属性值来查询，只返回唯一结果对象，假如结果不唯一，则抛出异常
	 * @param voClass 
	 * @param propName 
	 * @param value 
	 * @return 
	 */
	public Receive findUniqueByProperty(String propName,Object value);
	/**
	 * 根据DetachedCriteria来查询，只返回唯一结果对象，假如结果不唯一，则抛出异常
	 * @param detachedCriteria 
	 * @return 
	 */
	public Receive findUniqueByDc(DetachedCriteria detachedCriteria);
	/**
	 * 用hql和参数，根据上下限查找，只取其中的第begin到第begin+size条记录。
	 * @param hql 
	 * @param params 参数 可以为Null，Null表示没有HQL参数 
	 * @param begin 第一条记录 index是0
	 * @param size 取size条记录
	 * @return 
	 */
	public List<?> findWithHQLByLimit(String hql,Object[] params,int begin,int size);
	/**
	 * 用DetachedCriteria,根据上下限查找，只取其中的第begin到第begin+size条记录。
	 * @param detachedCriteria 
	 * @param begin 第一条记录 index是0
	 * @param size 取size条记录
	 * @return 
	 */
	public List<Receive> findWithCriteriaByLimit(DetachedCriteria detachedCriteria,int begin,int size);
	/**
	 * 根据hql和参数，查找符合条件的第一条记录。
	 * @param hql 
	 * @param params 参数 可以为Null，Null表示没有HQL参数 
	 * @return 
	 */
	public Receive getFirstByHQL(String hql,Object[] params);
	/**
	 * 从spring容器获取bean
	 * @param beanId 
	 * @return 
	 */
	public Object getBean(String beanId);
	/**
	 * 重新从数据库刷新对象（重新读取），强制读取并和当前Hibernate会话绑定，再某些场景中有用
	 * @param vo vo对象 
	 */
	public void refresh(Receive vo);
	/**
	 * 使用DetachedCriteria设置条件 查询出唯一对象，可能是Hibernate实体VO，也可能是单个值对象，调用者自行判断和处理
	 * @param detachedCriteria 
	 * @return 唯一结果
	 */
	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria);
	/**
	 * 使用DetachedCriteria设置条件，并从start位置查询出唯一对象，可能是Hibernate实体VO，也可能是单个值对象，调用者自行判断和处理
	 * @param detachedCriteria 
	 * @param start 
	 * @return 唯一结果
	 */
	public Object findRawUniqueResultByDetachedCriteria(DetachedCriteria detachedCriteria,int start);
	/**
	 * 使用DetachedCriteria设置条件 查询出List，可能是VO，也可能是数组或Hibernate的Projection值，调用者自行判断和处理
	 * @param detachedCriteria 
	 * @return 结果List
	 */
	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria);
	/**
	 * 使用DetachedCriteria设置条件 ，并限定结果集位置start 和大小 limit，查询出List，可能是VO，也可能是数组或Hibernate的Projection值，调用者自行判断和处理
	 * @param detachedCriteria 
	 * @param start 
	 * @param limit 
	 * @return 结果List
	 */
	public List<?> findRawListResultByDetachedCriteria(DetachedCriteria detachedCriteria, int start , int limit);
	/**
	 * 创建Helper类
	 * @return 
	 */
	public ReceiveHelper createHelper();
}
