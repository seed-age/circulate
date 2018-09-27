package com.sunnsoft.sloa.db.vo;
// Generated by Hibernate Tools 3.4.0.CR1



/**
 * UserInfo generated by hbm2java
 */
public class UserInfo  implements java.io.Serializable {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*Name:用户ID
	*Comment:
	*/
     private long userId;
     private User user;
	/**
	*Name:姓
	*Comment:
	*/
     private String lastName;
	/**
	*Name:名
	*Comment:
	*/
     private String firstName;
	/**
	*Name:姓名
	*Comment:
	*/
     private String userName;
	/**
	*Name:电子邮件
	*Comment:
	*/
     private String email;
	/**
	*Name:联系电话
	*Comment:
	*/
     private String phoneNumber;
	/**
	*Name:备注
	*Comment:
	*/
     private String remark;

    public UserInfo() {
    }

	
    public UserInfo(User user) {
        this.user = user;
    }
    public UserInfo(User user, String lastName, String firstName, String userName, String email, String phoneNumber, String remark) {
       this.user = user;
       this.lastName = lastName;
       this.firstName = firstName;
       this.userName = userName;
       this.email = email;
       this.phoneNumber = phoneNumber;
       this.remark = remark;
    }
   
	/**
	*Name:用户ID
	*Comment:
	*/
    public long getUserId() {
        return this.userId;
    }
    
	/**
	*Name:用户ID
	*Comment:
	*/
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
	/**
	*Name:姓
	*Comment:
	*/
    public String getLastName() {
        return this.lastName;
    }
    
	/**
	*Name:姓
	*Comment:
	*/
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
	/**
	*Name:名
	*Comment:
	*/
    public String getFirstName() {
        return this.firstName;
    }
    
	/**
	*Name:名
	*Comment:
	*/
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
	/**
	*Name:姓名
	*Comment:
	*/
    public String getUserName() {
        return this.userName;
    }
    
	/**
	*Name:姓名
	*Comment:
	*/
    public void setUserName(String userName) {
        this.userName = userName;
    }
	/**
	*Name:电子邮件
	*Comment:
	*/
    public String getEmail() {
        return this.email;
    }
    
	/**
	*Name:电子邮件
	*Comment:
	*/
    public void setEmail(String email) {
        this.email = email;
    }
	/**
	*Name:联系电话
	*Comment:
	*/
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
	/**
	*Name:联系电话
	*Comment:
	*/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
	/**
	*Name:备注
	*Comment:
	*/
    public String getRemark() {
        return this.remark;
    }
    
	/**
	*Name:备注
	*Comment:
	*/
    public void setRemark(String remark) {
        this.remark = remark;
    }




}

