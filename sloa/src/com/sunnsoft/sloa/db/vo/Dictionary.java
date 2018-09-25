package com.sunnsoft.sloa.db.vo;
// Generated by Hibernate Tools 3.4.0.CR1


import java.util.Date;

/**
 * Dictionary generated by hbm2java
 */
public class Dictionary  implements java.io.Serializable {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*Name:ID
	*Comment:
	*/
     private Long dicId;
	/**
	*Name:键名
	*Comment:
	*/
     private String keyName;
	/**
	*Name:键值
	*Comment:
	*/
     private String keyValue;
	/**
	*Name:更新日期
	*Comment:
	*/
     private Date updateTime;
	/**
	*Name:创建日期
	*Comment:
	*/
     private Date createTime;
	/**
	*Name:状态
	*Comment:0 为失效 （false），1 为正常（true）
	*/
     private boolean status;
	/**
	*Name:作用描述
	*Comment:
	*/
     private String description;
	/**
	*Name:类型
	*Comment:
	*/
     private String type;

    public Dictionary() {
    }

	
    public Dictionary(String keyName, String keyValue, boolean status) {
        this.keyName = keyName;
        this.keyValue = keyValue;
        this.status = status;
    }
    public Dictionary(String keyName, String keyValue, Date updateTime, Date createTime, boolean status, String description, String type) {
       this.keyName = keyName;
       this.keyValue = keyValue;
       this.updateTime = updateTime;
       this.createTime = createTime;
       this.status = status;
       this.description = description;
       this.type = type;
    }
   
	/**
	*Name:ID
	*Comment:
	*/
    public Long getDicId() {
        return this.dicId;
    }
    
	/**
	*Name:ID
	*Comment:
	*/
    public void setDicId(Long dicId) {
        this.dicId = dicId;
    }
	/**
	*Name:键名
	*Comment:
	*/
    public String getKeyName() {
        return this.keyName;
    }
    
	/**
	*Name:键名
	*Comment:
	*/
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
	/**
	*Name:键值
	*Comment:
	*/
    public String getKeyValue() {
        return this.keyValue;
    }
    
	/**
	*Name:键值
	*Comment:
	*/
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
	/**
	*Name:更新日期
	*Comment:
	*/
    public Date getUpdateTime() {
        return this.updateTime;
    }
    
	/**
	*Name:更新日期
	*Comment:
	*/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
	/**
	*Name:创建日期
	*Comment:
	*/
    public Date getCreateTime() {
        return this.createTime;
    }
    
	/**
	*Name:创建日期
	*Comment:
	*/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public boolean isStatus() {
        return this.status;
    }
    
	/**
	*Name:状态
	*Comment:0 为失效 （false），1 为正常（true）
	*/
    public void setStatus(boolean status) {
        this.status = status;
    }
	/**
	*Name:作用描述
	*Comment:
	*/
    public String getDescription() {
        return this.description;
    }
    
	/**
	*Name:作用描述
	*Comment:
	*/
    public void setDescription(String description) {
        this.description = description;
    }
	/**
	*Name:类型
	*Comment:
	*/
    public String getType() {
        return this.type;
    }
    
	/**
	*Name:类型
	*Comment:
	*/
    public void setType(String type) {
        this.type = type;
    }




}


