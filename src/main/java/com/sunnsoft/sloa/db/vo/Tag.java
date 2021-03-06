package com.sunnsoft.sloa.db.vo;
// Generated by Hibernate Tools 3.4.0.CR1



/**
 * Tag generated by hbm2java
 */
public class Tag  implements java.io.Serializable {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*Name:标签iid
	*Comment:标签ID
	*/
     private Long tagId;
	/**
	*Name:类型
	*Comment:上传文件的类型
	*/
     private String tagType;
	/**
	*Name:存放的方式
	*Comment:存放文件的方式, 网盘还是本地
	*/
     private String storageType;
	/**
	*Name:自定义的标识
	*Comment:自定义的标识
	*/
     private String selfName;
	/**
	*Name:公司名称
	*Comment:公司名称
	*/
     private String companyName;
	/**
	*Name:状态
	*Comment:0 正常  1 废弃不用
	*/
     private Integer labelStatus;

    public Tag() {
    }

    public Tag(String tagType, String storageType, String selfName, String companyName, Integer labelStatus) {
       this.tagType = tagType;
       this.storageType = storageType;
       this.selfName = selfName;
       this.companyName = companyName;
       this.labelStatus = labelStatus;
    }
   
	/**
	*Name:标签iid
	*Comment:标签ID
	*/
    public Long getTagId() {
        return this.tagId;
    }
    
	/**
	*Name:标签iid
	*Comment:标签ID
	*/
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
	/**
	*Name:类型
	*Comment:上传文件的类型
	*/
    public String getTagType() {
        return this.tagType;
    }
    
	/**
	*Name:类型
	*Comment:上传文件的类型
	*/
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
	/**
	*Name:存放的方式
	*Comment:存放文件的方式, 网盘还是本地
	*/
    public String getStorageType() {
        return this.storageType;
    }
    
	/**
	*Name:存放的方式
	*Comment:存放文件的方式, 网盘还是本地
	*/
    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
	/**
	*Name:自定义的标识
	*Comment:自定义的标识
	*/
    public String getSelfName() {
        return this.selfName;
    }
    
	/**
	*Name:自定义的标识
	*Comment:自定义的标识
	*/
    public void setSelfName(String selfName) {
        this.selfName = selfName;
    }
	/**
	*Name:公司名称
	*Comment:公司名称
	*/
    public String getCompanyName() {
        return this.companyName;
    }
    
	/**
	*Name:公司名称
	*Comment:公司名称
	*/
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
	/**
	*Name:状态
	*Comment:0 正常  1 废弃不用
	*/
    public Integer getLabelStatus() {
        return this.labelStatus;
    }
    
	/**
	*Name:状态
	*Comment:0 正常  1 废弃不用
	*/
    public void setLabelStatus(Integer labelStatus) {
        this.labelStatus = labelStatus;
    }




}


