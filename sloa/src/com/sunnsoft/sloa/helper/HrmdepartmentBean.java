package com.sunnsoft.sloa.helper;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.gteam.db.helper.hibernate.Each;
import org.gteam.db.helper.hibernate.HelperException;
import org.gteam.db.helper.hibernate.ScrollEach;
import org.gteam.db.helper.json.EachEntity2Map;
import org.gteam.util.EntityUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 *  
 * @author llade
 *
 * Bean 操作类
 */
@SuppressWarnings("unchecked")
public class HrmdepartmentBean {
	private HrmdepartmentHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<Hrmdepartment> insertBeans = new ArrayList<Hrmdepartment>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = HrmdepartmentBean.class.getDeclaredMethods();
		List<Method> list = new ArrayList<Method>();
		for (Method m : ms) {
			if(m.getName().startsWith("set")){
				list.add(m);
			}
		}
		methods = list.toArray(new Method[0]);
	}
	
	private Method getSetterMethodFromPropName(String propName){
		String methodName = "set" + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
		for (Method m : methods) {
			if(m.getName().equals(methodName)){
				return m;
			}
		}
		return null;
	}
	/**
	 * 复制属性的核心方法
	 * @param source
	 * @param mode 0 表示复制所有属性，1表示不复制null属性，2表示不复制Null或者空字符串属性。
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 * @throws Exception
	 */
	private HrmdepartmentBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Field[] fs= source.getClass().getDeclaredFields();
		for (Field field : fs) {
			field.setAccessible(true);
			Object value = field.get(source);
			switch (mode) {
				case 0:
					invokePropSetter(field, value);
					break;
				case 1:
					if(value != null){
						invokePropSetter(field, value);
					}
					break;
				case 2:
					if(value != null){
						if(value instanceof String){
							if(StringUtils.isNotEmpty((String)value)){
								invokePropSetter(field, value);
							}
						}else{
							invokePropSetter(field, value);
						}
					}
					break;
				default:
					break;
			}
		}
		return this;
	}

	private void invokePropSetter(Field field, Object value)
			throws IllegalAccessException, InvocationTargetException {
		Method m = this.getSetterMethodFromPropName(field.getName());
		if(m != null){
			m.invoke(this, value);
		}
	}
	/**
	 * 从复制同名属性值到Bean操作类所操作的bean。
	 * 注意：不是通过getter来获取属性值的，而是直接访问source对象的同名属性。
	 * 并且继承的属性无效，一定要是当前source对象定义的属性
	 * source对象一般是struts action对象本身或者是bean对象相同的类。
	 * @param source
	 * @return
	 */
	public HrmdepartmentBean copyValueForm(Object source){
		try {
			this.copyValue(source, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	/**
	 * 类似copyValueForm，但不复制Null值
	 * @param source
	 * @return
	 */
	public HrmdepartmentBean copyNotNullValueForm(Object source){
		try {
			this.copyValue(source, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	/**
	 * 类似copyNotNullValueForm，除了不复制Null值，同时对于空String也不复制。
	 * @param source
	 * @return
	 */
	public HrmdepartmentBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public HrmdepartmentBean(HrmdepartmentHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:id
	*Comment:
	*/

	public HrmdepartmentBean setId(int id){
		paramTypeMap.put("id", ClassUtils.wrapperToPrimitive(Integer.class));
		paramValueMap.put("id", id);
		if(this.current != null){
			current.setId(id);
		}
		return this;
	}
	
	/**
	*Name:部门标识
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentmark(String departmentmark){
		paramTypeMap.put("departmentmark", String.class);
		paramValueMap.put("departmentmark", departmentmark);
		if(this.current != null){
			current.setDepartmentmark(departmentmark);
		}
		return this;
	}

	/**
	*Name:部门标识
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentmarkIfNotNull(String departmentmark){
	    if(departmentmark == null){
	    	return this;
	    }
		return setDepartmentmark(departmentmark);
	}
	/**
	*Name:部门标识
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentmarkWithDefaultValueWhenNull(String departmentmark,String defaultValue){
		String temp = departmentmark;
	    if(departmentmark == null){
	    	temp = defaultValue;
	    }
		return setDepartmentmark(temp);
	}
	
	/**
	*Name:部门标识
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentmarkIfNotNullAndNotEmpty(String departmentmark){
		if(StringUtils.isEmpty(departmentmark)){
	    	return this;
	    }
	    return setDepartmentmark(departmentmark);
	}
	/**
	*Name:部门标识
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentmarkWithDefaultValueWhenNullOrEmpty(String departmentmark,String defaultValue){
		String temp = departmentmark;
		if(StringUtils.isEmpty(departmentmark)){
	    	temp = defaultValue;
	    }
	    return setDepartmentmark(temp);
	}
	
	/**
	*Name:部门名称
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentname(String departmentname){
		paramTypeMap.put("departmentname", String.class);
		paramValueMap.put("departmentname", departmentname);
		if(this.current != null){
			current.setDepartmentname(departmentname);
		}
		return this;
	}

	/**
	*Name:部门名称
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentnameIfNotNull(String departmentname){
	    if(departmentname == null){
	    	return this;
	    }
		return setDepartmentname(departmentname);
	}
	/**
	*Name:部门名称
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentnameWithDefaultValueWhenNull(String departmentname,String defaultValue){
		String temp = departmentname;
	    if(departmentname == null){
	    	temp = defaultValue;
	    }
		return setDepartmentname(temp);
	}
	
	/**
	*Name:部门名称
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentnameIfNotNullAndNotEmpty(String departmentname){
		if(StringUtils.isEmpty(departmentname)){
	    	return this;
	    }
	    return setDepartmentname(departmentname);
	}
	/**
	*Name:部门名称
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentnameWithDefaultValueWhenNullOrEmpty(String departmentname,String defaultValue){
		String temp = departmentname;
		if(StringUtils.isEmpty(departmentname)){
	    	temp = defaultValue;
	    }
	    return setDepartmentname(temp);
	}
	
	/**
	*Name:所属分部1
	*Comment:
	*/

	public HrmdepartmentBean setSubcompanyid1(Integer subcompanyid1){
		paramTypeMap.put("subcompanyid1", Integer.class);
		paramValueMap.put("subcompanyid1", subcompanyid1);
		if(this.current != null){
			current.setSubcompanyid1(subcompanyid1);
		}
		return this;
	}

	/**
	*Name:所属分部1
	*Comment:
	*/

	public HrmdepartmentBean setSubcompanyid1IfNotNull(Integer subcompanyid1){
	    if(subcompanyid1 == null){
	    	return this;
	    }
		return setSubcompanyid1(subcompanyid1);
	}
	/**
	*Name:所属分部1
	*Comment:
	*/

	public HrmdepartmentBean setSubcompanyid1WithDefaultValueWhenNull(Integer subcompanyid1,Integer defaultValue){
		Integer temp = subcompanyid1;
	    if(subcompanyid1 == null){
	    	temp = defaultValue;
	    }
		return setSubcompanyid1(temp);
	}
	
	
	/**
	*Name:上级部门id
	*Comment:
	*/

	public HrmdepartmentBean setSupdepid(Integer supdepid){
		paramTypeMap.put("supdepid", Integer.class);
		paramValueMap.put("supdepid", supdepid);
		if(this.current != null){
			current.setSupdepid(supdepid);
		}
		return this;
	}

	/**
	*Name:上级部门id
	*Comment:
	*/

	public HrmdepartmentBean setSupdepidIfNotNull(Integer supdepid){
	    if(supdepid == null){
	    	return this;
	    }
		return setSupdepid(supdepid);
	}
	/**
	*Name:上级部门id
	*Comment:
	*/

	public HrmdepartmentBean setSupdepidWithDefaultValueWhenNull(Integer supdepid,Integer defaultValue){
		Integer temp = supdepid;
	    if(supdepid == null){
	    	temp = defaultValue;
	    }
		return setSupdepid(temp);
	}
	
	
	/**
	*Name:所有上级部门id
	*Comment:
	*/

	public HrmdepartmentBean setAllsupdepid(String allsupdepid){
		paramTypeMap.put("allsupdepid", String.class);
		paramValueMap.put("allsupdepid", allsupdepid);
		if(this.current != null){
			current.setAllsupdepid(allsupdepid);
		}
		return this;
	}

	/**
	*Name:所有上级部门id
	*Comment:
	*/

	public HrmdepartmentBean setAllsupdepidIfNotNull(String allsupdepid){
	    if(allsupdepid == null){
	    	return this;
	    }
		return setAllsupdepid(allsupdepid);
	}
	/**
	*Name:所有上级部门id
	*Comment:
	*/

	public HrmdepartmentBean setAllsupdepidWithDefaultValueWhenNull(String allsupdepid,String defaultValue){
		String temp = allsupdepid;
	    if(allsupdepid == null){
	    	temp = defaultValue;
	    }
		return setAllsupdepid(temp);
	}
	
	/**
	*Name:所有上级部门id
	*Comment:
	*/

	public HrmdepartmentBean setAllsupdepidIfNotNullAndNotEmpty(String allsupdepid){
		if(StringUtils.isEmpty(allsupdepid)){
	    	return this;
	    }
	    return setAllsupdepid(allsupdepid);
	}
	/**
	*Name:所有上级部门id
	*Comment:
	*/

	public HrmdepartmentBean setAllsupdepidWithDefaultValueWhenNullOrEmpty(String allsupdepid,String defaultValue){
		String temp = allsupdepid;
		if(StringUtils.isEmpty(allsupdepid)){
	    	temp = defaultValue;
	    }
	    return setAllsupdepid(temp);
	}
	
	/**
	*Name:显示顺序
	*Comment:
	*/

	public HrmdepartmentBean setShoworder(Integer showorder){
		paramTypeMap.put("showorder", Integer.class);
		paramValueMap.put("showorder", showorder);
		if(this.current != null){
			current.setShoworder(showorder);
		}
		return this;
	}

	/**
	*Name:显示顺序
	*Comment:
	*/

	public HrmdepartmentBean setShoworderIfNotNull(Integer showorder){
	    if(showorder == null){
	    	return this;
	    }
		return setShoworder(showorder);
	}
	/**
	*Name:显示顺序
	*Comment:
	*/

	public HrmdepartmentBean setShoworderWithDefaultValueWhenNull(Integer showorder,Integer defaultValue){
		Integer temp = showorder;
	    if(showorder == null){
	    	temp = defaultValue;
	    }
		return setShoworder(temp);
	}
	
	
	/**
	*Name:封存标识
	*Comment:
	*/

	public HrmdepartmentBean setCanceled(Character canceled){
		paramTypeMap.put("canceled", Character.class);
		paramValueMap.put("canceled", canceled);
		if(this.current != null){
			current.setCanceled(canceled);
		}
		return this;
	}

	/**
	*Name:封存标识
	*Comment:
	*/

	public HrmdepartmentBean setCanceledIfNotNull(Character canceled){
	    if(canceled == null){
	    	return this;
	    }
		return setCanceled(canceled);
	}
	/**
	*Name:封存标识
	*Comment:
	*/

	public HrmdepartmentBean setCanceledWithDefaultValueWhenNull(Character canceled,Character defaultValue){
		Character temp = canceled;
	    if(canceled == null){
	    	temp = defaultValue;
	    }
		return setCanceled(temp);
	}
	
	
	/**
	*Name:部门编码
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentcode(String departmentcode){
		paramTypeMap.put("departmentcode", String.class);
		paramValueMap.put("departmentcode", departmentcode);
		if(this.current != null){
			current.setDepartmentcode(departmentcode);
		}
		return this;
	}

	/**
	*Name:部门编码
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentcodeIfNotNull(String departmentcode){
	    if(departmentcode == null){
	    	return this;
	    }
		return setDepartmentcode(departmentcode);
	}
	/**
	*Name:部门编码
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentcodeWithDefaultValueWhenNull(String departmentcode,String defaultValue){
		String temp = departmentcode;
	    if(departmentcode == null){
	    	temp = defaultValue;
	    }
		return setDepartmentcode(temp);
	}
	
	/**
	*Name:部门编码
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentcodeIfNotNullAndNotEmpty(String departmentcode){
		if(StringUtils.isEmpty(departmentcode)){
	    	return this;
	    }
	    return setDepartmentcode(departmentcode);
	}
	/**
	*Name:部门编码
	*Comment:
	*/

	public HrmdepartmentBean setDepartmentcodeWithDefaultValueWhenNullOrEmpty(String departmentcode,String defaultValue){
		String temp = departmentcode;
		if(StringUtils.isEmpty(departmentcode)){
	    	temp = defaultValue;
	    }
	    return setDepartmentcode(temp);
	}
	
	/**
	*Name:协办人
	*Comment:
	*/

	public HrmdepartmentBean setCoadjutant(Integer coadjutant){
		paramTypeMap.put("coadjutant", Integer.class);
		paramValueMap.put("coadjutant", coadjutant);
		if(this.current != null){
			current.setCoadjutant(coadjutant);
		}
		return this;
	}

	/**
	*Name:协办人
	*Comment:
	*/

	public HrmdepartmentBean setCoadjutantIfNotNull(Integer coadjutant){
	    if(coadjutant == null){
	    	return this;
	    }
		return setCoadjutant(coadjutant);
	}
	/**
	*Name:协办人
	*Comment:
	*/

	public HrmdepartmentBean setCoadjutantWithDefaultValueWhenNull(Integer coadjutant,Integer defaultValue){
		Integer temp = coadjutant;
	    if(coadjutant == null){
	    	temp = defaultValue;
	    }
		return setCoadjutant(temp);
	}
	
	
	/**
	*Name:组织架构部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfzr(String zzjgbmfzr){
		paramTypeMap.put("zzjgbmfzr", String.class);
		paramValueMap.put("zzjgbmfzr", zzjgbmfzr);
		if(this.current != null){
			current.setZzjgbmfzr(zzjgbmfzr);
		}
		return this;
	}

	/**
	*Name:组织架构部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfzrIfNotNull(String zzjgbmfzr){
	    if(zzjgbmfzr == null){
	    	return this;
	    }
		return setZzjgbmfzr(zzjgbmfzr);
	}
	/**
	*Name:组织架构部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfzrWithDefaultValueWhenNull(String zzjgbmfzr,String defaultValue){
		String temp = zzjgbmfzr;
	    if(zzjgbmfzr == null){
	    	temp = defaultValue;
	    }
		return setZzjgbmfzr(temp);
	}
	
	/**
	*Name:组织架构部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfzrIfNotNullAndNotEmpty(String zzjgbmfzr){
		if(StringUtils.isEmpty(zzjgbmfzr)){
	    	return this;
	    }
	    return setZzjgbmfzr(zzjgbmfzr);
	}
	/**
	*Name:组织架构部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfzrWithDefaultValueWhenNullOrEmpty(String zzjgbmfzr,String defaultValue){
		String temp = zzjgbmfzr;
		if(StringUtils.isEmpty(zzjgbmfzr)){
	    	temp = defaultValue;
	    }
	    return setZzjgbmfzr(temp);
	}
	
	/**
	*Name:组织架构部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfgld(String zzjgbmfgld){
		paramTypeMap.put("zzjgbmfgld", String.class);
		paramValueMap.put("zzjgbmfgld", zzjgbmfgld);
		if(this.current != null){
			current.setZzjgbmfgld(zzjgbmfgld);
		}
		return this;
	}

	/**
	*Name:组织架构部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfgldIfNotNull(String zzjgbmfgld){
	    if(zzjgbmfgld == null){
	    	return this;
	    }
		return setZzjgbmfgld(zzjgbmfgld);
	}
	/**
	*Name:组织架构部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfgldWithDefaultValueWhenNull(String zzjgbmfgld,String defaultValue){
		String temp = zzjgbmfgld;
	    if(zzjgbmfgld == null){
	    	temp = defaultValue;
	    }
		return setZzjgbmfgld(temp);
	}
	
	/**
	*Name:组织架构部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfgldIfNotNullAndNotEmpty(String zzjgbmfgld){
		if(StringUtils.isEmpty(zzjgbmfgld)){
	    	return this;
	    }
	    return setZzjgbmfgld(zzjgbmfgld);
	}
	/**
	*Name:组织架构部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setZzjgbmfgldWithDefaultValueWhenNullOrEmpty(String zzjgbmfgld,String defaultValue){
		String temp = zzjgbmfgld;
		if(StringUtils.isEmpty(zzjgbmfgld)){
	    	temp = defaultValue;
	    }
	    return setZzjgbmfgld(temp);
	}
	
	/**
	*Name:矩阵管理部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfzr(String jzglbmfzr){
		paramTypeMap.put("jzglbmfzr", String.class);
		paramValueMap.put("jzglbmfzr", jzglbmfzr);
		if(this.current != null){
			current.setJzglbmfzr(jzglbmfzr);
		}
		return this;
	}

	/**
	*Name:矩阵管理部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfzrIfNotNull(String jzglbmfzr){
	    if(jzglbmfzr == null){
	    	return this;
	    }
		return setJzglbmfzr(jzglbmfzr);
	}
	/**
	*Name:矩阵管理部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfzrWithDefaultValueWhenNull(String jzglbmfzr,String defaultValue){
		String temp = jzglbmfzr;
	    if(jzglbmfzr == null){
	    	temp = defaultValue;
	    }
		return setJzglbmfzr(temp);
	}
	
	/**
	*Name:矩阵管理部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfzrIfNotNullAndNotEmpty(String jzglbmfzr){
		if(StringUtils.isEmpty(jzglbmfzr)){
	    	return this;
	    }
	    return setJzglbmfzr(jzglbmfzr);
	}
	/**
	*Name:矩阵管理部门负责人
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfzrWithDefaultValueWhenNullOrEmpty(String jzglbmfzr,String defaultValue){
		String temp = jzglbmfzr;
		if(StringUtils.isEmpty(jzglbmfzr)){
	    	temp = defaultValue;
	    }
	    return setJzglbmfzr(temp);
	}
	
	/**
	*Name:矩阵管理部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfgld(String jzglbmfgld){
		paramTypeMap.put("jzglbmfgld", String.class);
		paramValueMap.put("jzglbmfgld", jzglbmfgld);
		if(this.current != null){
			current.setJzglbmfgld(jzglbmfgld);
		}
		return this;
	}

	/**
	*Name:矩阵管理部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfgldIfNotNull(String jzglbmfgld){
	    if(jzglbmfgld == null){
	    	return this;
	    }
		return setJzglbmfgld(jzglbmfgld);
	}
	/**
	*Name:矩阵管理部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfgldWithDefaultValueWhenNull(String jzglbmfgld,String defaultValue){
		String temp = jzglbmfgld;
	    if(jzglbmfgld == null){
	    	temp = defaultValue;
	    }
		return setJzglbmfgld(temp);
	}
	
	/**
	*Name:矩阵管理部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfgldIfNotNullAndNotEmpty(String jzglbmfgld){
		if(StringUtils.isEmpty(jzglbmfgld)){
	    	return this;
	    }
	    return setJzglbmfgld(jzglbmfgld);
	}
	/**
	*Name:矩阵管理部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setJzglbmfgldWithDefaultValueWhenNullOrEmpty(String jzglbmfgld,String defaultValue){
		String temp = jzglbmfgld;
		if(StringUtils.isEmpty(jzglbmfgld)){
	    	temp = defaultValue;
	    }
	    return setJzglbmfgld(temp);
	}
	
	/**
	*Name:矩阵部门负责人员
	*Comment:
	*/

	public HrmdepartmentBean setBmfzr(String bmfzr){
		paramTypeMap.put("bmfzr", String.class);
		paramValueMap.put("bmfzr", bmfzr);
		if(this.current != null){
			current.setBmfzr(bmfzr);
		}
		return this;
	}

	/**
	*Name:矩阵部门负责人员
	*Comment:
	*/

	public HrmdepartmentBean setBmfzrIfNotNull(String bmfzr){
	    if(bmfzr == null){
	    	return this;
	    }
		return setBmfzr(bmfzr);
	}
	/**
	*Name:矩阵部门负责人员
	*Comment:
	*/

	public HrmdepartmentBean setBmfzrWithDefaultValueWhenNull(String bmfzr,String defaultValue){
		String temp = bmfzr;
	    if(bmfzr == null){
	    	temp = defaultValue;
	    }
		return setBmfzr(temp);
	}
	
	/**
	*Name:矩阵部门负责人员
	*Comment:
	*/

	public HrmdepartmentBean setBmfzrIfNotNullAndNotEmpty(String bmfzr){
		if(StringUtils.isEmpty(bmfzr)){
	    	return this;
	    }
	    return setBmfzr(bmfzr);
	}
	/**
	*Name:矩阵部门负责人员
	*Comment:
	*/

	public HrmdepartmentBean setBmfzrWithDefaultValueWhenNullOrEmpty(String bmfzr,String defaultValue){
		String temp = bmfzr;
		if(StringUtils.isEmpty(bmfzr)){
	    	temp = defaultValue;
	    }
	    return setBmfzr(temp);
	}
	
	/**
	*Name:矩阵部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setBmfgld(String bmfgld){
		paramTypeMap.put("bmfgld", String.class);
		paramValueMap.put("bmfgld", bmfgld);
		if(this.current != null){
			current.setBmfgld(bmfgld);
		}
		return this;
	}

	/**
	*Name:矩阵部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setBmfgldIfNotNull(String bmfgld){
	    if(bmfgld == null){
	    	return this;
	    }
		return setBmfgld(bmfgld);
	}
	/**
	*Name:矩阵部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setBmfgldWithDefaultValueWhenNull(String bmfgld,String defaultValue){
		String temp = bmfgld;
	    if(bmfgld == null){
	    	temp = defaultValue;
	    }
		return setBmfgld(temp);
	}
	
	/**
	*Name:矩阵部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setBmfgldIfNotNullAndNotEmpty(String bmfgld){
		if(StringUtils.isEmpty(bmfgld)){
	    	return this;
	    }
	    return setBmfgld(bmfgld);
	}
	/**
	*Name:矩阵部门分管领导
	*Comment:
	*/

	public HrmdepartmentBean setBmfgldWithDefaultValueWhenNullOrEmpty(String bmfgld,String defaultValue){
		String temp = bmfgld;
		if(StringUtils.isEmpty(bmfgld)){
	    	temp = defaultValue;
	    }
	    return setBmfgld(temp);
	}
	
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmdepartmentBean setOutkey(String outkey){
		paramTypeMap.put("outkey", String.class);
		paramValueMap.put("outkey", outkey);
		if(this.current != null){
			current.setOutkey(outkey);
		}
		return this;
	}

	/**
	*Name:outkey
	*Comment:
	*/

	public HrmdepartmentBean setOutkeyIfNotNull(String outkey){
	    if(outkey == null){
	    	return this;
	    }
		return setOutkey(outkey);
	}
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmdepartmentBean setOutkeyWithDefaultValueWhenNull(String outkey,String defaultValue){
		String temp = outkey;
	    if(outkey == null){
	    	temp = defaultValue;
	    }
		return setOutkey(temp);
	}
	
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmdepartmentBean setOutkeyIfNotNullAndNotEmpty(String outkey){
		if(StringUtils.isEmpty(outkey)){
	    	return this;
	    }
	    return setOutkey(outkey);
	}
	/**
	*Name:outkey
	*Comment:
	*/

	public HrmdepartmentBean setOutkeyWithDefaultValueWhenNullOrEmpty(String outkey,String defaultValue){
		String temp = outkey;
		if(StringUtils.isEmpty(outkey)){
	    	temp = defaultValue;
	    }
	    return setOutkey(temp);
	}
	
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/

	public HrmdepartmentBean setBudgetatuomoveorder(Integer budgetatuomoveorder){
		paramTypeMap.put("budgetatuomoveorder", Integer.class);
		paramValueMap.put("budgetatuomoveorder", budgetatuomoveorder);
		if(this.current != null){
			current.setBudgetatuomoveorder(budgetatuomoveorder);
		}
		return this;
	}

	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/

	public HrmdepartmentBean setBudgetatuomoveorderIfNotNull(Integer budgetatuomoveorder){
	    if(budgetatuomoveorder == null){
	    	return this;
	    }
		return setBudgetatuomoveorder(budgetatuomoveorder);
	}
	/**
	*Name:budgetatuomoveorder
	*Comment:
	*/

	public HrmdepartmentBean setBudgetatuomoveorderWithDefaultValueWhenNull(Integer budgetatuomoveorder,Integer defaultValue){
		Integer temp = budgetatuomoveorder;
	    if(budgetatuomoveorder == null){
	    	temp = defaultValue;
	    }
		return setBudgetatuomoveorder(temp);
	}
	
	
	/**
	*Name:ecology_pinyin_search
	*Comment:
	*/

	public HrmdepartmentBean setEcologyPinyinSearch(String ecologyPinyinSearch){
		paramTypeMap.put("ecologyPinyinSearch", String.class);
		paramValueMap.put("ecologyPinyinSearch", ecologyPinyinSearch);
		if(this.current != null){
			current.setEcologyPinyinSearch(ecologyPinyinSearch);
		}
		return this;
	}

	/**
	*Name:ecology_pinyin_search
	*Comment:
	*/

	public HrmdepartmentBean setEcologyPinyinSearchIfNotNull(String ecologyPinyinSearch){
	    if(ecologyPinyinSearch == null){
	    	return this;
	    }
		return setEcologyPinyinSearch(ecologyPinyinSearch);
	}
	/**
	*Name:ecology_pinyin_search
	*Comment:
	*/

	public HrmdepartmentBean setEcologyPinyinSearchWithDefaultValueWhenNull(String ecologyPinyinSearch,String defaultValue){
		String temp = ecologyPinyinSearch;
	    if(ecologyPinyinSearch == null){
	    	temp = defaultValue;
	    }
		return setEcologyPinyinSearch(temp);
	}
	
	/**
	*Name:ecology_pinyin_search
	*Comment:
	*/

	public HrmdepartmentBean setEcologyPinyinSearchIfNotNullAndNotEmpty(String ecologyPinyinSearch){
		if(StringUtils.isEmpty(ecologyPinyinSearch)){
	    	return this;
	    }
	    return setEcologyPinyinSearch(ecologyPinyinSearch);
	}
	/**
	*Name:ecology_pinyin_search
	*Comment:
	*/

	public HrmdepartmentBean setEcologyPinyinSearchWithDefaultValueWhenNullOrEmpty(String ecologyPinyinSearch,String defaultValue){
		String temp = ecologyPinyinSearch;
		if(StringUtils.isEmpty(ecologyPinyinSearch)){
	    	temp = defaultValue;
	    }
	    return setEcologyPinyinSearch(temp);
	}
	
	/**
	*Name:tlevel
	*Comment:
	*/

	public HrmdepartmentBean setTlevel(Integer tlevel){
		paramTypeMap.put("tlevel", Integer.class);
		paramValueMap.put("tlevel", tlevel);
		if(this.current != null){
			current.setTlevel(tlevel);
		}
		return this;
	}

	/**
	*Name:tlevel
	*Comment:
	*/

	public HrmdepartmentBean setTlevelIfNotNull(Integer tlevel){
	    if(tlevel == null){
	    	return this;
	    }
		return setTlevel(tlevel);
	}
	/**
	*Name:tlevel
	*Comment:
	*/

	public HrmdepartmentBean setTlevelWithDefaultValueWhenNull(Integer tlevel,Integer defaultValue){
		Integer temp = tlevel;
	    if(tlevel == null){
	    	temp = defaultValue;
	    }
		return setTlevel(temp);
	}
	
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<Hrmdepartment> update(){
		return this.helper.each(new Each<Hrmdepartment>(){

			@Override
			public void each(Hrmdepartment bean, List<Hrmdepartment> list) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 本方法的行为如下：
	 * 1.如果找到唯一的匹配对象，则update bean ,并返回该对象。
	 * 2.如果没找到，则不做任何动作，并返回null.
	 * 3.如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 的bean 或 null
	 */
	public Hrmdepartment updateUnique(){
		Hrmdepartment bean =  this.helper.uniqueResult();
		if(bean != null){
			this.invokeSetters(bean);
			this.helper.service.update(bean);
		}
		return bean;
	}
	
	/**
	 * 批量更新大量数据。对于大数量的修改有利。需要注意，大批量修改数据的话，可能引起数据库update锁冲突，慎用。
	 * 此外，可以调用helper类的sleepInteval方法设置每批次的线程挂起时间。避免CPU占用过高
	 * @param flushValve 设置每更新flushValve个bean后清hibernate一级缓存并同步到数据库，避免OutOfMemory。
	 * @return 返回迭代的结果的总和
	 */
	public long batchUpdate(final int flushValve){
		return this.helper.scrollResult(new ScrollEach<Hrmdepartment>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(Hrmdepartment bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<Hrmdepartment> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private Hrmdepartment current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public HrmdepartmentBean create(){
		Hrmdepartment bean = new Hrmdepartment();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<Hrmdepartment> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (Hrmdepartment bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public Hrmdepartment insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		Hrmdepartment bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public Hrmdepartment ifFoundUpdateElseInsert(){
		Hrmdepartment bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new Hrmdepartment();
		}
		this.invokeSetters(bean);
		this.helper.service.saveOrUpdate(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常。
	 * 最后返回bean的json 
	 * @return json字符串
	 */
	public String ifFoundUpdateElseInsertThenConvertJson(){
		Hrmdepartment bean = this.ifFoundUpdateElseInsert();
		return this.helper.json().fastJsonSerializer(bean, false);
	}
	
	/**
	  * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常。使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要
	 * 最后返回bean的json 
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return json字符串 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<Hrmdepartment> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Hrmdepartment bean = this.ifFoundUpdateElseInsert();
		Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
		return this.helper.json().fastJsonSerializer(map, true);
	}
	
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中，并返回插入对象的json序列化字符串。对于一个Bean操作类，本方法只能被调用一次。
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String insertAndReturnJson() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(false, false, null);
	}

	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入对象的json序列化字符串，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String insertUniqueAndReturnJson() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, false, null);
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中，并返回插入对象的json序列化字符串，使用EachEntity2Map 对每个实体转的map进行调整,以符合实际需要。对于一个Bean操作类，本方法只能被调用一次。
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String insertJson(EachEntity2Map<Hrmdepartment> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(false, false, eachEntity2Map);
	}
	/**
	  * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入对象的json序列化字符串，使用EachEntity2Map 对实体转换的map进行调整,以符合实际需要，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String insertUniqueJson(EachEntity2Map<Hrmdepartment> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, false, eachEntity2Map);
	}
	/**
	 * 持久化到数据库，返回更新的数据库记录集合转换的json字符串。
	 * @return json字符串
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String updateAndReturnJson() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(false, true, null);
	}
	/**
	 * 本方法的行为如下：
	 * 1.如果找到唯一的匹配对象，则update bean ,并返回该对象json。
	 * 2.如果没找到，则不做任何动作，并返回null.
	 * 3.如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String updateUniqueAndReturnJson() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, null);
	}
	/**
	 * 持久化到数据库，返回更新的数据库记录集合转换的json字符串。
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String updateJson(EachEntity2Map<Hrmdepartment> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(false, true, eachEntity2Map);
	}
	/**
	 * 本方法的行为如下：
	 * 1.如果找到唯一的匹配对象，则update bean ,并返回该对象json。
	 * 2.如果没找到，则不做任何动作，并返回null.
	 * 3.如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @param eachEntity2Map 实体和转换出来的map进行处理(排除了一对多，多对多，一对一关联关系)
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public String updateUniqueJson(EachEntity2Map<Hrmdepartment> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<Hrmdepartment> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<Hrmdepartment> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (Hrmdepartment bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			Hrmdepartment bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(Hrmdepartment bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = Hrmdepartment.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
