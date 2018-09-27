package com.sunnsoft.sloa.db.vo;
// Generated by Hibernate Tools 3.4.0.CR1


import java.util.Date;

/**
 * Discuss generated by hbm2java
 */
public class Discuss  implements java.io.Serializable {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*Name:讨论id
	*Comment:讨论id
	*/
     private Long discussId;
     private Mail mail;
	/**
	*Name:发布的时间
	*Comment:发布的时间
	*/
     private Date createTime;
	/**
	*Name:讨论的内容
	*Comment:讨论的内容
	*/
     private String discussContent;
	/**
	*Name:讨论人id
	*Comment:讨论人id
	*/
     private long userId;
	/**
	*Name:讨论人工作编号
	*Comment:讨论人工作编号
	*/
     private String workCode;
	/**
	*Name:讨论人的姓名
	*Comment:讨论人的姓名
	*/
     private String lastName;
	/**
	*Name:讨论人登录名
	*Comment:用户的登录名称(冗余字段)
	*/
     private String loginId;
	/**
	*Name:讨论人的头像url
	*Comment:讨论人的头像URL
	*/
     private String headPortraitUrl;
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人评论的记录或是已收传阅人的评论记录
	*/
     private String differentiate;

    public Discuss() {
    }

	
    public Discuss(String discussContent, long userId) {
        this.discussContent = discussContent;
        this.userId = userId;
    }
    public Discuss(Mail mail, Date createTime, String discussContent, long userId, String workCode, String lastName, String loginId, String headPortraitUrl, String differentiate) {
       this.mail = mail;
       this.createTime = createTime;
       this.discussContent = discussContent;
       this.userId = userId;
       this.workCode = workCode;
       this.lastName = lastName;
       this.loginId = loginId;
       this.headPortraitUrl = headPortraitUrl;
       this.differentiate = differentiate;
    }
   
	/**
	*Name:讨论id
	*Comment:讨论id
	*/
    public Long getDiscussId() {
        return this.discussId;
    }
    
	/**
	*Name:讨论id
	*Comment:讨论id
	*/
    public void setDiscussId(Long discussId) {
        this.discussId = discussId;
    }
    public Mail getMail() {
        return this.mail;
    }
    
    public void setMail(Mail mail) {
        this.mail = mail;
    }
	/**
	*Name:发布的时间
	*Comment:发布的时间
	*/
    public Date getCreateTime() {
        return this.createTime;
    }
    
	/**
	*Name:发布的时间
	*Comment:发布的时间
	*/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
	/**
	*Name:讨论的内容
	*Comment:讨论的内容
	*/
    public String getDiscussContent() {
        return this.discussContent;
    }
    
	/**
	*Name:讨论的内容
	*Comment:讨论的内容
	*/
    public void setDiscussContent(String discussContent) {
        this.discussContent = discussContent;
    }
	/**
	*Name:讨论人id
	*Comment:讨论人id
	*/
    public long getUserId() {
        return this.userId;
    }
    
	/**
	*Name:讨论人id
	*Comment:讨论人id
	*/
    public void setUserId(long userId) {
        this.userId = userId;
    }
	/**
	*Name:讨论人工作编号
	*Comment:讨论人工作编号
	*/
    public String getWorkCode() {
        return this.workCode;
    }
    
	/**
	*Name:讨论人工作编号
	*Comment:讨论人工作编号
	*/
    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }
	/**
	*Name:讨论人的姓名
	*Comment:讨论人的姓名
	*/
    public String getLastName() {
        return this.lastName;
    }
    
	/**
	*Name:讨论人的姓名
	*Comment:讨论人的姓名
	*/
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
	/**
	*Name:讨论人登录名
	*Comment:用户的登录名称(冗余字段)
	*/
    public String getLoginId() {
        return this.loginId;
    }
    
	/**
	*Name:讨论人登录名
	*Comment:用户的登录名称(冗余字段)
	*/
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
	/**
	*Name:讨论人的头像url
	*Comment:讨论人的头像URL
	*/
    public String getHeadPortraitUrl() {
        return this.headPortraitUrl;
    }
    
	/**
	*Name:讨论人的头像url
	*Comment:讨论人的头像URL
	*/
    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人评论的记录或是已收传阅人的评论记录
	*/
    public String getDifferentiate() {
        return this.differentiate;
    }
    
	/**
	*Name:区别
	*Comment:区别是: 已发传阅人评论的记录或是已收传阅人的评论记录
	*/
    public void setDifferentiate(String differentiate) {
        this.differentiate = differentiate;
    }




}


