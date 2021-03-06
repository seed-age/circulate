package com.sunnsoft.sloa.db.vo;
// Generated by Hibernate Tools 3.4.0.CR1


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AttachmentItem generated by hbm2java
 */
public class AttachmentItem  implements java.io.Serializable {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*Name:附件文件ID
	*Comment:附件文件ID
	*/
     private Long itemId;
     private Mail mail;
	/**
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/
     private String bulkId;
	/**
	*Name:创建人ID
	*Comment:上传这个附件的传阅对象的ID
	*/
     private Long userId;
	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/
     private String creator;
	/**
	*Name:创建时间
	*Comment:创建附件的时间
	*/
     private Date createTime;
	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/
     private String fileName;
	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/
     private String fileCategory;
	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/
     private String saveName;
	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/
     private String urlPath;
	/**
	*Name:是否实体附件
	*Comment:和实体绑定前，为false，绑定后为true
没有和实体绑定的附件，创建24小时之后都应该被删除
	*/
     private boolean attached;
	/**
	*Name:状态
	*Comment:0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)
	*/
     private Integer state;
	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/
     private String itemSize;
	/**
	*Name:网盘文件ID
	*Comment:存放文件上传到网盘的ID
	*/
     private Long itemNeid;
	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/
     private String itemRev;
	/**
	*Name:区别
	*Comment:区别附件存放在那个位置: 个人  还是  企业 (默认是个人)
	*/
     private Integer itemDifferentiate;
	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/
     private String localhostUrlPath;
	/**
	*Name:更新时间
	*Comment:更新时间(主要用于localhost_url_path的字段更新)
	*/
     private Date updateTime;
     private List<UpdateRecord> updateRecords = new ArrayList<UpdateRecord>(0);

    public AttachmentItem() {
    }

	
    public AttachmentItem(Date createTime, String fileName, String fileCategory, String saveName, String urlPath, boolean attached) {
        this.createTime = createTime;
        this.fileName = fileName;
        this.fileCategory = fileCategory;
        this.saveName = saveName;
        this.urlPath = urlPath;
        this.attached = attached;
    }
    public AttachmentItem(Mail mail, String bulkId, Long userId, String creator, Date createTime, String fileName, String fileCategory, String saveName, String urlPath, boolean attached, Integer state, String itemSize, Long itemNeid, String itemRev, Integer itemDifferentiate, String localhostUrlPath, Date updateTime, List<UpdateRecord> updateRecords) {
       this.mail = mail;
       this.bulkId = bulkId;
       this.userId = userId;
       this.creator = creator;
       this.createTime = createTime;
       this.fileName = fileName;
       this.fileCategory = fileCategory;
       this.saveName = saveName;
       this.urlPath = urlPath;
       this.attached = attached;
       this.state = state;
       this.itemSize = itemSize;
       this.itemNeid = itemNeid;
       this.itemRev = itemRev;
       this.itemDifferentiate = itemDifferentiate;
       this.localhostUrlPath = localhostUrlPath;
       this.updateTime = updateTime;
       this.updateRecords = updateRecords;
    }
   
	/**
	*Name:附件文件ID
	*Comment:附件文件ID
	*/
    public Long getItemId() {
        return this.itemId;
    }
    
	/**
	*Name:附件文件ID
	*Comment:附件文件ID
	*/
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public Mail getMail() {
        return this.mail;
    }
    
    public void setMail(Mail mail) {
        this.mail = mail;
    }
	/**
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/
    public String getBulkId() {
        return this.bulkId;
    }
    
	/**
	*Name:附件上传批次ID
	*Comment:用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null
	*/
    public void setBulkId(String bulkId) {
        this.bulkId = bulkId;
    }
	/**
	*Name:创建人ID
	*Comment:上传这个附件的传阅对象的ID
	*/
    public Long getUserId() {
        return this.userId;
    }
    
	/**
	*Name:创建人ID
	*Comment:上传这个附件的传阅对象的ID
	*/
    public void setUserId(Long userId) {
        this.userId = userId;
    }
	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/
    public String getCreator() {
        return this.creator;
    }
    
	/**
	*Name:创建人
	*Comment:可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。
	*/
    public void setCreator(String creator) {
        this.creator = creator;
    }
	/**
	*Name:创建时间
	*Comment:创建附件的时间
	*/
    public Date getCreateTime() {
        return this.createTime;
    }
    
	/**
	*Name:创建时间
	*Comment:创建附件的时间
	*/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/
    public String getFileName() {
        return this.fileName;
    }
    
	/**
	*Name:文件原名
	*Comment:文件上传时候的原名
	*/
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/
    public String getFileCategory() {
        return this.fileCategory;
    }
    
	/**
	*Name:分类
	*Comment:同一个实体可能有用不同用途的文件附件。开发人员自定义
默认值为空字符串。
	*/
    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }
	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/
    public String getSaveName() {
        return this.saveName;
    }
    
	/**
	*Name:文件保存名
	*Comment:由uuid+类型后缀 组成
	*/
    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }
	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/
    public String getUrlPath() {
        return this.urlPath;
    }
    
	/**
	*Name:链接地址
	*Comment:出去服务器地址和webapp后的URL地址，c:url可用的地址。
	*/
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
    public boolean isAttached() {
        return this.attached;
    }
    
	/**
	*Name:是否实体附件
	*Comment:和实体绑定前，为false，绑定后为true
没有和实体绑定的附件，创建24小时之后都应该被删除
	*/
    public void setAttached(boolean attached) {
        this.attached = attached;
    }
	/**
	*Name:状态
	*Comment:0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)
	*/
    public Integer getState() {
        return this.state;
    }
    
	/**
	*Name:状态
	*Comment:0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)
	*/
    public void setState(Integer state) {
        this.state = state;
    }
	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/
    public String getItemSize() {
        return this.itemSize;
    }
    
	/**
	*Name:文件大小
	*Comment:存放附件的大小, 
	*/
    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }
	/**
	*Name:网盘文件ID
	*Comment:存放文件上传到网盘的ID
	*/
    public Long getItemNeid() {
        return this.itemNeid;
    }
    
	/**
	*Name:网盘文件ID
	*Comment:存放文件上传到网盘的ID
	*/
    public void setItemNeid(Long itemNeid) {
        this.itemNeid = itemNeid;
    }
	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/
    public String getItemRev() {
        return this.itemRev;
    }
    
	/**
	*Name:网盘文件版本
	*Comment:存放文件上传到网盘的文件版本
	*/
    public void setItemRev(String itemRev) {
        this.itemRev = itemRev;
    }
	/**
	*Name:区别
	*Comment:区别附件存放在那个位置: 个人  还是  企业 (默认是个人)
	*/
    public Integer getItemDifferentiate() {
        return this.itemDifferentiate;
    }
    
	/**
	*Name:区别
	*Comment:区别附件存放在那个位置: 个人  还是  企业 (默认是个人)
	*/
    public void setItemDifferentiate(Integer itemDifferentiate) {
        this.itemDifferentiate = itemDifferentiate;
    }
	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/
    public String getLocalhostUrlPath() {
        return this.localhostUrlPath;
    }
    
	/**
	*Name:存储APP端下载到本地的URL
	*Comment:存储APP端下载到本地的URL
	*/
    public void setLocalhostUrlPath(String localhostUrlPath) {
        this.localhostUrlPath = localhostUrlPath;
    }
	/**
	*Name:更新时间
	*Comment:更新时间(主要用于localhost_url_path的字段更新)
	*/
    public Date getUpdateTime() {
        return this.updateTime;
    }
    
	/**
	*Name:更新时间
	*Comment:更新时间(主要用于localhost_url_path的字段更新)
	*/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public List<UpdateRecord> getUpdateRecords() {
        return this.updateRecords;
    }
    
    public void setUpdateRecords(List<UpdateRecord> updateRecords) {
        this.updateRecords = updateRecords;
    }




}


