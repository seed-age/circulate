package com.sunnsoft.sloa.actions.system.users;

import com.opensymphony.xwork2.ActionSupport;

public class EditForm extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long editId;

	public Long getEditId() {
		return editId;
	}

	public void setEditId(Long editId) {
		this.editId = editId;
	}
	
}
