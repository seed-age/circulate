package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
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
public class AttachmentItemBean {
	private AttachmentItemHelper helper;
	
	private Map<String, Class> paramTypeMap = new HashMap<String, Class>();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	/**
	 * 插入队列
	 */
	private List<AttachmentItem> insertBeans = new ArrayList<AttachmentItem>(10);
	/**
	 * 缓存的setter
	 */
	static Method[] methods ;
	
	static {
		Method[] ms = AttachmentItemBean.class.getDeclaredMethods();
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
	private AttachmentItemBean copyValue(Object source,int mode) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public AttachmentItemBean copyValueForm(Object source){
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
	public AttachmentItemBean copyNotNullValueForm(Object source){
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
	public AttachmentItemBean copyNotEmptyValueForm(Object source){
		try {
			this.copyValue(source, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public AttachmentItemBean(AttachmentItemHelper helper){
		this.helper = helper;
	}
	
	/**
	*Name:附件文件ID
	*Comment:附件文件ID
	*/

	public AttachmentItemBean setItemId(Long itemId){
		paramTypeMap.put("itemId", Long.class);
		paramValueMap.put("itemId", itemId);
		if(this.current != null){
			current.setItemId(itemId);
		}
		return this;
	}

	/**
	*Name:附件文件ID
	*Comment:附件文件ID
	*/

	public AttachmentItemBean setItemIdIfNotNull(Long itemId){
	    if(itemId == null){
	    	return this;
	    }
		return setItemId(itemId);
	}
	/**
	*Name:附件文件ID
	*Comment:附件文件ID
	*/

	public AttachmentItemBean setItemIdWithDefaultValueWhenNull(Long itemId,Long defaultValue){
		Long temp = itemId;
	    if(itemId == null){
	    	temp = defaultValue;
	    }
		return setItemId(temp);
	}
	
	

	public AttachmentItemBean setMail(Mail mail){
		paramTypeMap.put("mail", Mail.class);
		paramValueMap.put("mail", mail);
		if(this.current != null){
			current.setMail(mail);
		}
		return this;
	}


	public AttachmentItemBean setMailIfNotNull(Mail mail){
	    if(mail == null){
	    	return this;
	    }
		return setMail(mail);
	}

	public AttachmentItemBean setMailWithDefaultValueWhenNull(Mail mail,Mail defaultValue){
		Mail temp = mail;
	    if(mail == null){
	    	temp = defaultValue;
	    }
		return setMail(temp);
	}
	
	
	/**
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/

	public AttachmentItemBean setBulkId(String bulkId){
		paramTypeMap.put("bulkId", String.class);
		paramValueMap.put("bulkId", bulkId);
		if(this.current != null){
			current.setBulkId(bulkId);
		}
		return this;
	}

	/**
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/

	public AttachmentItemBean setBulkIdIfNotNull(String bulkId){
	    if(bulkId == null){
	    	return this;
	    }
		return setBulkId(bulkId);
	}
	/**
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/

	public AttachmentItemBean setBulkIdWithDefaultValueWhenNull(String bulkId,String defaultValue){
		String temp = bulkId;
	    if(bulkId == null){
	    	temp = defaultValue;
	    }
		return setBulkId(temp);
	}
	
	/**
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/

	public AttachmentItemBean setBulkIdIfNotNullAndNotEmpty(String bulkId){
		if(StringUtils.isEmpty(bulkId)){
	    	return this;
	    }
	    return setBulkId(bulkId);
	}
	/**
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/

	public AttachmentItemBean setBulkIdWithDefaultValueWhenNullOrEmpty(String bulkId,String defaultValue){
		String temp = bulkId;
		if(StringUtils.isEmpty(bulkId)){
	    	temp = defaultValue;
	    }
	    return setBulkId(temp);
	}
	
	/**
	*Name:创建人ID
	*Comment:上传这个附件的传阅对象的ID
	*/

	public AttachmentItemBean setUserId(Long userId){
		paramTypeMap.put("userId", Long.class);
		paramValueMap.put("userId", userId);
		if(this.current != null){
			current.setUserId(userId);
		}
		return this;
	}

	/**
	*Name:创建人ID
	*Comment:上传这个附件的传阅对象的ID
	*/

	public AttachmentItemBean setUserIdIfNotNull(Long userId){
	    if(userId == null){
	    	return this;
	    }
		return setUserId(userId);
	}
	/**
	*Name:创建人ID
	*Comment:上传这个附件的传阅对象的ID
	*/

	public AttachmentItemBean setUserIdWithDefaultValueWhenNull(Long userId,Long defaultValue){
		Long temp = userId;
	    if(userId == null){
	    	temp = defaultValue;
	    }
		return setUserId(temp);
	}
	
	
	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public AttachmentItemBean setCreator(String creator){
		paramTypeMap.put("creator", String.class);
		paramValueMap.put("creator", creator);
		if(this.current != null){
			current.setCreator(creator);
		}
		return this;
	}

	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public AttachmentItemBean setCreatorIfNotNull(String creator){
	    if(creator == null){
	    	return this;
	    }
		return setCreator(creator);
	}
	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public AttachmentItemBean setCreatorWithDefaultValueWhenNull(String creator,String defaultValue){
		String temp = creator;
	    if(creator == null){
	    	temp = defaultValue;
	    }
		return setCreator(temp);
	}
	
	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public AttachmentItemBean setCreatorIfNotNullAndNotEmpty(String creator){
		if(StringUtils.isEmpty(creator)){
	    	return this;
	    }
	    return setCreator(creator);
	}
	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/

	public AttachmentItemBean setCreatorWithDefaultValueWhenNullOrEmpty(String creator,String defaultValue){
		String temp = creator;
		if(StringUtils.isEmpty(creator)){
	    	temp = defaultValue;
	    }
	    return setCreator(temp);
	}
	
	/**
	*Name:创建时间
	*Comment:创建附件的时间
	*/

	public AttachmentItemBean setCreateTime(Date createTime){
		paramTypeMap.put("createTime", Date.class);
		paramValueMap.put("createTime", createTime);
		if(this.current != null){
			current.setCreateTime(createTime);
		}
		return this;
	}

	/**
	*Name:创建时间
	*Comment:创建附件的时间
	*/

	public AttachmentItemBean setCreateTimeIfNotNull(Date createTime){
	    if(createTime == null){
	    	return this;
	    }
		return setCreateTime(createTime);
	}
	/**
	*Name:创建时间
	*Comment:创建附件的时间
	*/

	public AttachmentItemBean setCreateTimeWithDefaultValueWhenNull(Date createTime,Date defaultValue){
		Date temp = createTime;
	    if(createTime == null){
	    	temp = defaultValue;
	    }
		return setCreateTime(temp);
	}
	
	
	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/

	public AttachmentItemBean setFileName(String fileName){
		paramTypeMap.put("fileName", String.class);
		paramValueMap.put("fileName", fileName);
		if(this.current != null){
			current.setFileName(fileName);
		}
		return this;
	}

	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/

	public AttachmentItemBean setFileNameIfNotNull(String fileName){
	    if(fileName == null){
	    	return this;
	    }
		return setFileName(fileName);
	}
	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/

	public AttachmentItemBean setFileNameWithDefaultValueWhenNull(String fileName,String defaultValue){
		String temp = fileName;
	    if(fileName == null){
	    	temp = defaultValue;
	    }
		return setFileName(temp);
	}
	
	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/

	public AttachmentItemBean setFileNameIfNotNullAndNotEmpty(String fileName){
		if(StringUtils.isEmpty(fileName)){
	    	return this;
	    }
	    return setFileName(fileName);
	}
	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/

	public AttachmentItemBean setFileNameWithDefaultValueWhenNullOrEmpty(String fileName,String defaultValue){
		String temp = fileName;
		if(StringUtils.isEmpty(fileName)){
	    	temp = defaultValue;
	    }
	    return setFileName(temp);
	}
	
	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/

	public AttachmentItemBean setFileCategory(String fileCategory){
		paramTypeMap.put("fileCategory", String.class);
		paramValueMap.put("fileCategory", fileCategory);
		if(this.current != null){
			current.setFileCategory(fileCategory);
		}
		return this;
	}

	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/

	public AttachmentItemBean setFileCategoryIfNotNull(String fileCategory){
	    if(fileCategory == null){
	    	return this;
	    }
		return setFileCategory(fileCategory);
	}
	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/

	public AttachmentItemBean setFileCategoryWithDefaultValueWhenNull(String fileCategory,String defaultValue){
		String temp = fileCategory;
	    if(fileCategory == null){
	    	temp = defaultValue;
	    }
		return setFileCategory(temp);
	}
	
	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/

	public AttachmentItemBean setFileCategoryIfNotNullAndNotEmpty(String fileCategory){
		if(StringUtils.isEmpty(fileCategory)){
	    	return this;
	    }
	    return setFileCategory(fileCategory);
	}
	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/

	public AttachmentItemBean setFileCategoryWithDefaultValueWhenNullOrEmpty(String fileCategory,String defaultValue){
		String temp = fileCategory;
		if(StringUtils.isEmpty(fileCategory)){
	    	temp = defaultValue;
	    }
	    return setFileCategory(temp);
	}
	
	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/

	public AttachmentItemBean setSaveName(String saveName){
		paramTypeMap.put("saveName", String.class);
		paramValueMap.put("saveName", saveName);
		if(this.current != null){
			current.setSaveName(saveName);
		}
		return this;
	}

	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/

	public AttachmentItemBean setSaveNameIfNotNull(String saveName){
	    if(saveName == null){
	    	return this;
	    }
		return setSaveName(saveName);
	}
	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/

	public AttachmentItemBean setSaveNameWithDefaultValueWhenNull(String saveName,String defaultValue){
		String temp = saveName;
	    if(saveName == null){
	    	temp = defaultValue;
	    }
		return setSaveName(temp);
	}
	
	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/

	public AttachmentItemBean setSaveNameIfNotNullAndNotEmpty(String saveName){
		if(StringUtils.isEmpty(saveName)){
	    	return this;
	    }
	    return setSaveName(saveName);
	}
	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/

	public AttachmentItemBean setSaveNameWithDefaultValueWhenNullOrEmpty(String saveName,String defaultValue){
		String temp = saveName;
		if(StringUtils.isEmpty(saveName)){
	    	temp = defaultValue;
	    }
	    return setSaveName(temp);
	}
	
	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/

	public AttachmentItemBean setUrlPath(String urlPath){
		paramTypeMap.put("urlPath", String.class);
		paramValueMap.put("urlPath", urlPath);
		if(this.current != null){
			current.setUrlPath(urlPath);
		}
		return this;
	}

	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/

	public AttachmentItemBean setUrlPathIfNotNull(String urlPath){
	    if(urlPath == null){
	    	return this;
	    }
		return setUrlPath(urlPath);
	}
	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/

	public AttachmentItemBean setUrlPathWithDefaultValueWhenNull(String urlPath,String defaultValue){
		String temp = urlPath;
	    if(urlPath == null){
	    	temp = defaultValue;
	    }
		return setUrlPath(temp);
	}
	
	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/

	public AttachmentItemBean setUrlPathIfNotNullAndNotEmpty(String urlPath){
		if(StringUtils.isEmpty(urlPath)){
	    	return this;
	    }
	    return setUrlPath(urlPath);
	}
	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/

	public AttachmentItemBean setUrlPathWithDefaultValueWhenNullOrEmpty(String urlPath,String defaultValue){
		String temp = urlPath;
		if(StringUtils.isEmpty(urlPath)){
	    	temp = defaultValue;
	    }
	    return setUrlPath(temp);
	}
	
	/**
	*Name:是否实体附件
	*Comment:和实体绑定前，为false，绑定后为true
没有和实体绑定的附件，创建24小时之后都应该被删除
	*/

	public AttachmentItemBean setAttached(boolean attached){
		paramTypeMap.put("attached", ClassUtils.wrapperToPrimitive(Boolean.class));
		paramValueMap.put("attached", attached);
		if(this.current != null){
			current.setAttached(attached);
		}
		return this;
	}
	
	/**
	*Name:状态
	*Comment:0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)
	*/

	public AttachmentItemBean setState(Integer state){
		paramTypeMap.put("state", Integer.class);
		paramValueMap.put("state", state);
		if(this.current != null){
			current.setState(state);
		}
		return this;
	}

	/**
	*Name:状态
	*Comment:0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)
	*/

	public AttachmentItemBean setStateIfNotNull(Integer state){
	    if(state == null){
	    	return this;
	    }
		return setState(state);
	}
	/**
	*Name:状态
	*Comment:0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)
	*/

	public AttachmentItemBean setStateWithDefaultValueWhenNull(Integer state,Integer defaultValue){
		Integer temp = state;
	    if(state == null){
	    	temp = defaultValue;
	    }
		return setState(temp);
	}
	
	
	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/

	public AttachmentItemBean setItemSize(String itemSize){
		paramTypeMap.put("itemSize", String.class);
		paramValueMap.put("itemSize", itemSize);
		if(this.current != null){
			current.setItemSize(itemSize);
		}
		return this;
	}

	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/

	public AttachmentItemBean setItemSizeIfNotNull(String itemSize){
	    if(itemSize == null){
	    	return this;
	    }
		return setItemSize(itemSize);
	}
	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/

	public AttachmentItemBean setItemSizeWithDefaultValueWhenNull(String itemSize,String defaultValue){
		String temp = itemSize;
	    if(itemSize == null){
	    	temp = defaultValue;
	    }
		return setItemSize(temp);
	}
	
	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/

	public AttachmentItemBean setItemSizeIfNotNullAndNotEmpty(String itemSize){
		if(StringUtils.isEmpty(itemSize)){
	    	return this;
	    }
	    return setItemSize(itemSize);
	}
	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/

	public AttachmentItemBean setItemSizeWithDefaultValueWhenNullOrEmpty(String itemSize,String defaultValue){
		String temp = itemSize;
		if(StringUtils.isEmpty(itemSize)){
	    	temp = defaultValue;
	    }
	    return setItemSize(temp);
	}
	
	/**
	*Name:网盘文件ID
	*Comment:存放文件上传到网盘的ID
	*/

	public AttachmentItemBean setItemNeid(Long itemNeid){
		paramTypeMap.put("itemNeid", Long.class);
		paramValueMap.put("itemNeid", itemNeid);
		if(this.current != null){
			current.setItemNeid(itemNeid);
		}
		return this;
	}

	/**
	*Name:网盘文件ID
	*Comment:存放文件上传到网盘的ID
	*/

	public AttachmentItemBean setItemNeidIfNotNull(Long itemNeid){
	    if(itemNeid == null){
	    	return this;
	    }
		return setItemNeid(itemNeid);
	}
	/**
	*Name:网盘文件ID
	*Comment:存放文件上传到网盘的ID
	*/

	public AttachmentItemBean setItemNeidWithDefaultValueWhenNull(Long itemNeid,Long defaultValue){
		Long temp = itemNeid;
	    if(itemNeid == null){
	    	temp = defaultValue;
	    }
		return setItemNeid(temp);
	}
	
	
	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/

	public AttachmentItemBean setItemRev(String itemRev){
		paramTypeMap.put("itemRev", String.class);
		paramValueMap.put("itemRev", itemRev);
		if(this.current != null){
			current.setItemRev(itemRev);
		}
		return this;
	}

	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/

	public AttachmentItemBean setItemRevIfNotNull(String itemRev){
	    if(itemRev == null){
	    	return this;
	    }
		return setItemRev(itemRev);
	}
	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/

	public AttachmentItemBean setItemRevWithDefaultValueWhenNull(String itemRev,String defaultValue){
		String temp = itemRev;
	    if(itemRev == null){
	    	temp = defaultValue;
	    }
		return setItemRev(temp);
	}
	
	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/

	public AttachmentItemBean setItemRevIfNotNullAndNotEmpty(String itemRev){
		if(StringUtils.isEmpty(itemRev)){
	    	return this;
	    }
	    return setItemRev(itemRev);
	}
	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/

	public AttachmentItemBean setItemRevWithDefaultValueWhenNullOrEmpty(String itemRev,String defaultValue){
		String temp = itemRev;
		if(StringUtils.isEmpty(itemRev)){
	    	temp = defaultValue;
	    }
	    return setItemRev(temp);
	}
	
	/**
	*Name:区别
	*Comment:区别附件存放在那个位置: 个人  还是  企业 (默认是个人)
	*/

	public AttachmentItemBean setItemDifferentiate(Integer itemDifferentiate){
		paramTypeMap.put("itemDifferentiate", Integer.class);
		paramValueMap.put("itemDifferentiate", itemDifferentiate);
		if(this.current != null){
			current.setItemDifferentiate(itemDifferentiate);
		}
		return this;
	}

	/**
	*Name:区别
	*Comment:区别附件存放在那个位置: 个人  还是  企业 (默认是个人)
	*/

	public AttachmentItemBean setItemDifferentiateIfNotNull(Integer itemDifferentiate){
	    if(itemDifferentiate == null){
	    	return this;
	    }
		return setItemDifferentiate(itemDifferentiate);
	}
	/**
	*Name:区别
	*Comment:区别附件存放在那个位置: 个人  还是  企业 (默认是个人)
	*/

	public AttachmentItemBean setItemDifferentiateWithDefaultValueWhenNull(Integer itemDifferentiate,Integer defaultValue){
		Integer temp = itemDifferentiate;
	    if(itemDifferentiate == null){
	    	temp = defaultValue;
	    }
		return setItemDifferentiate(temp);
	}
	
	
	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/

	public AttachmentItemBean setLocalhostUrlPath(String localhostUrlPath){
		paramTypeMap.put("localhostUrlPath", String.class);
		paramValueMap.put("localhostUrlPath", localhostUrlPath);
		if(this.current != null){
			current.setLocalhostUrlPath(localhostUrlPath);
		}
		return this;
	}

	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/

	public AttachmentItemBean setLocalhostUrlPathIfNotNull(String localhostUrlPath){
	    if(localhostUrlPath == null){
	    	return this;
	    }
		return setLocalhostUrlPath(localhostUrlPath);
	}
	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/

	public AttachmentItemBean setLocalhostUrlPathWithDefaultValueWhenNull(String localhostUrlPath,String defaultValue){
		String temp = localhostUrlPath;
	    if(localhostUrlPath == null){
	    	temp = defaultValue;
	    }
		return setLocalhostUrlPath(temp);
	}
	
	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/

	public AttachmentItemBean setLocalhostUrlPathIfNotNullAndNotEmpty(String localhostUrlPath){
		if(StringUtils.isEmpty(localhostUrlPath)){
	    	return this;
	    }
	    return setLocalhostUrlPath(localhostUrlPath);
	}
	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/

	public AttachmentItemBean setLocalhostUrlPathWithDefaultValueWhenNullOrEmpty(String localhostUrlPath,String defaultValue){
		String temp = localhostUrlPath;
		if(StringUtils.isEmpty(localhostUrlPath)){
	    	temp = defaultValue;
	    }
	    return setLocalhostUrlPath(temp);
	}
	
	/**
	*Name:更新时间
	*Comment:更新时间(主要用于localhost_url_path的字段更新)
	*/

	public AttachmentItemBean setUpdateTime(Date updateTime){
		paramTypeMap.put("updateTime", Date.class);
		paramValueMap.put("updateTime", updateTime);
		if(this.current != null){
			current.setUpdateTime(updateTime);
		}
		return this;
	}

	/**
	*Name:更新时间
	*Comment:更新时间(主要用于localhost_url_path的字段更新)
	*/

	public AttachmentItemBean setUpdateTimeIfNotNull(Date updateTime){
	    if(updateTime == null){
	    	return this;
	    }
		return setUpdateTime(updateTime);
	}
	/**
	*Name:更新时间
	*Comment:更新时间(主要用于localhost_url_path的字段更新)
	*/

	public AttachmentItemBean setUpdateTimeWithDefaultValueWhenNull(Date updateTime,Date defaultValue){
		Date temp = updateTime;
	    if(updateTime == null){
	    	temp = defaultValue;
	    }
		return setUpdateTime(temp);
	}
	
	
	/**
	 * 持久化到数据库。返回更新的数据库记录集合
	 * @return
	 */
	public List<AttachmentItem> update(){
		return this.helper.each(new Each<AttachmentItem>(){

			@Override
			public void each(AttachmentItem bean, List<AttachmentItem> list) {
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
	public AttachmentItem updateUnique(){
		AttachmentItem bean =  this.helper.uniqueResult();
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
		return this.helper.scrollResult(new ScrollEach<AttachmentItem>(){
			
			@Override
			public int flushValve() {
				return flushValve;
			}

			@Override
			public void each(AttachmentItem bean,long index) {
				invokeSetters(bean);
			}
			
		});
	}
	
	/**
	 * 获取新建的对象列表。
	 * @return
	 */
	public List<AttachmentItem> getCreateBeans(){
		return this.insertBeans;
	}
	
	/**
	 * 新创建的Bean对象的引用
	 */
	private AttachmentItem current;
	
	/**
	 * 创建新的Bean对象，可以在本类对新对象进行操作。
	 * 每次调用本方法都会创建一个新的可操作Bean，并且该Bean被放在“插入队列”中，
	 * 调用insert方法后，所有被创建的bean都将被持久化插入到数据库中。
	 * @return
	 */
	public AttachmentItemBean create(){
		AttachmentItem bean = new AttachmentItem();
		this.insertBeans.add(bean);
		this.current = bean;
		return this;
	}
	/**
	 * 将“插入队列”中的Bean持久化插入到数据库中。对于一个Bean操作类，本方法只能被调用一次。
	 * @return 返回被插入的Bean 列表，每个Bean的ID都和数据库中的实际ID相等。
	 */
	public List<AttachmentItem> insert(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		for (AttachmentItem bean : insertBeans) {
			this.helper.service.add(bean);
		}
		return this.insertBeans;
	}
	
	/**
	 * 将“插入队列”中的唯一的Bean持久化插入到数据库中，并返回插入的bean对象，超过1个bean或插入队列为空，则抛出异常。对于一个Bean操作类，本方法只能被调用一次。
	 * @return insert的bean
	 */
	public AttachmentItem insertUnique(){
		if(insertBeans.size() == 0){
			throw new HelperException("“插入队列”为空，请先调用create()方法new Bean再setter");
		}
		if(insertBeans.size() > 1){
			throw new HelperException("“插入队列”超过1个以上对象，请insert()方法批量插入数据库");
		}
		AttachmentItem bean = insertBeans.get(0);
		this.helper.service.add(bean);
		return bean;
	}
	
	/**
	 * 如果找到唯一的匹配对象，则update bean ,否则，insert bean
	 * 注意如果找到2个以上的bean，本方法会抛出Hibernate异常
	 * @return update 或insert的bean
	 */
	public AttachmentItem ifFoundUpdateElseInsert(){
		AttachmentItem bean =  this.helper.uniqueResult();
		if(bean == null){
			bean = new AttachmentItem();
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
		AttachmentItem bean = this.ifFoundUpdateElseInsert();
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
	public String ifFoundUpdateElseInsertThenConvertJson(EachEntity2Map<AttachmentItem> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		AttachmentItem bean = this.ifFoundUpdateElseInsert();
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
	public String insertJson(EachEntity2Map<AttachmentItem> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String insertUniqueJson(EachEntity2Map<AttachmentItem> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateJson(EachEntity2Map<AttachmentItem> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
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
	public String updateUniqueJson(EachEntity2Map<AttachmentItem> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		return this._insertOrUpdateJson(true, true, eachEntity2Map);
	}
	
	private String _insertOrUpdateJson(boolean unique,boolean isUpdate,EachEntity2Map<AttachmentItem> eachEntity2Map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(!unique){
			List<AttachmentItem> list = isUpdate ? this.update() : this.insert();
			if(eachEntity2Map != null){
				List<Map> listMap = new ArrayList<Map>();
				for (AttachmentItem bean : list) {
					Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
					listMap.add(map);
					eachEntity2Map.each(bean,map);
				}
				return this.helper.json().fastJsonSerializer(listMap, true);
			}else{
				return this.helper.json().fastJsonSerializer(list, false);
			}
		}else{
			AttachmentItem bean = isUpdate ? this.updateUnique() : this.insertUnique();
			if(eachEntity2Map != null){
				Map map = EntityUtils.convertEntityToMapWithoutForgeinRelation(bean);
				return this.helper.json().fastJsonSerializer(map, true);
			}else{
				return this.helper.json().fastJsonSerializer(bean, false);
			}
		}
	}

	private void invokeSetters(AttachmentItem bean) {
		for (Iterator<String> iter = paramTypeMap.keySet().iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0))+propertyName.substring(1);
			Object value = paramValueMap.get(propertyName);
			try {
				Method m = AttachmentItem.class.getMethod(setMethodName, paramTypeMap.get(propertyName));
				m.invoke(bean, value);
			} catch (Exception e) {
				throw new HelperException("方法反射调用异常：methodName:"+setMethodName+",value:"+ value,e);
			}
		}
	}
}
