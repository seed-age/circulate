package com.sunnsoft.sloa.db.vo;
// Generated by Hibernate Tools 3.4.0.CR1


import java.util.Date;

/**
 * UpdateRecord generated by hbm2java
 */
public class UpdateRecord  implements java.io.Serializable {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*Name:修改id
	*Comment:修改ID
	*/
     private Long updateId;
     private AttachmentItem attachmentItem;
     private Receive receive;
	/**
	*Name:修改的时间
	*Comment:修改的时间
	*/
     private Date updateTime;
	/**
	*Name:修改动作
	*Comment:0 为新增 ; 1 为修改 ; 2 为删除
	*/
     private Integer updateAction;

    public UpdateRecord() {
    }

    public UpdateRecord(AttachmentItem attachmentItem, Receive receive, Date updateTime, Integer updateAction) {
       this.attachmentItem = attachmentItem;
       this.receive = receive;
       this.updateTime = updateTime;
       this.updateAction = updateAction;
    }
   
	/**
	*Name:修改id
	*Comment:修改ID
	*/
    public Long getUpdateId() {
        return this.updateId;
    }
    
	/**
	*Name:修改id
	*Comment:修改ID
	*/
    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }
    public AttachmentItem getAttachmentItem() {
        return this.attachmentItem;
    }
    
    public void setAttachmentItem(AttachmentItem attachmentItem) {
        this.attachmentItem = attachmentItem;
    }
    public Receive getReceive() {
        return this.receive;
    }
    
    public void setReceive(Receive receive) {
        this.receive = receive;
    }
	/**
	*Name:修改的时间
	*Comment:修改的时间
	*/
    public Date getUpdateTime() {
        return this.updateTime;
    }
    
	/**
	*Name:修改的时间
	*Comment:修改的时间
	*/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
	/**
	*Name:修改动作
	*Comment:0 为新增 ; 1 为修改 ; 2 为删除
	*/
    public Integer getUpdateAction() {
        return this.updateAction;
    }
    
	/**
	*Name:修改动作
	*Comment:0 为新增 ; 1 为修改 ; 2 为删除
	*/
    public void setUpdateAction(Integer updateAction) {
        this.updateAction = updateAction;
    }




}


