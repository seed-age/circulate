var HTMLEditor = Ext
		.extend(
				Ext.form.HtmlEditor,
				{
					addImage : function() {
						var editor = this;
						var imgform = new Ext.FormPanel(
								{
									region : 'center',
									labelWidth : 55,
									frame : true,
									bodyStyle : 'padding:5px 5px 0',
									autoScroll : true,
									border : false,
									fileUpload : true,
									items : [ {
										xtype : 'textfield',
										fieldLabel : '选择图片',
										name : 'userfile',
										id : 'userfile',
										inputType : 'file',
										allowBlank : false,
										blankText : '文件不能为空',
										height : 25,
										anchor : '98%'
									}],
									buttons : [
											{
												text : '上传',
												type : 'submit',
												handler : function() {
													var furl = "";
													furl = imgform.form
															.findField(
																	'userfile')
															.getValue();
													var type = furl.substring(
															furl.length - 3)
															.toLowerCase();
													if (furl == ""
															|| furl == null) {
														return;
													}
													if (type != 'jpg'
															&& type != 'bmp'
															&& type != 'gif'
															&& type != 'png') {
														alert('仅支持jpg、bmp、gif、png格式的图片');
														return;
													}
													imgform.form
															.submit( {
																url : 'upload/upload-file.htm',
																params : {
																	fu : furl
																},
																waitMsg : '正在上传......',
																waitTitle : '请等待',
																method : 'POST',
																success : function(
																		form,
																		action) {
																	var element = document
																			.createElement("img");
																	element.src = action.result.fileURL;
																	if (Ext.isIE) {
																		editor
																				.insertAtCursor(element.outerHTML);
																	} else {
																		var selection = editor.win
																				.getSelection();
																		if (!selection.isCollapsed) {
																			selection
																					.deleteFromDocument();
																		}
																		selection
																				.getRangeAt(
																						0)
																				.insertNode(
																						element);
																	}
																	form
																			.reset();
																	win.close();
																},
																failure : function(
																		form,
																		action) {
																	form
																			.reset();
																	if (action.failureType == Ext.form.Action.SERVER_INVALID)
																		Ext.MessageBox
																				.alert(
																						'警告',
																						'上传失败，仅支持jpg、bmp、gif、png格式的图片');

																}

															});
												}
											}, {
												text : '关闭',
												type : 'submit',
												handler : function() {
													win.close(this);
												}
											} ]
								})

						var win = new Ext.Window( {
							title : "上传图片",
							id : 'picwin',
							width : 400,
							height : 150,
							modal : true,
							border : false,

							layout : "fit",
							items : imgform

						});
						win.show();

					},
					addFile : function() {
						var editor = this;
						var fileform = new Ext.FormPanel(
								{
									region : 'center',
									labelWidth : 55,
									frame : true,
									bodyStyle : 'padding:5px 5px 0',
									autoScroll : true,
									border : false,
									fileUpload : true,
									items : [ {
										xtype : 'textfield',
										fieldLabel : '选择文件',
										name : 'userfile',
										id : 'userfile',
										inputType : 'file',
										allowBlank : false,
										blankText : '文件不能为空',
										height : 25,
										anchor : '98%'
									} ],
									buttons : [
											{
												text : '上传',
												type : 'submit',
												handler : function() {
													var furl = "";//文件物理地址
													var fname = "";//文件名称
													furl = fileform.form
															.findField(
																	'userfile')
															.getValue();
													var type = furl.substring(
															furl.length - 3)
															.toLowerCase();
													if (furl == ""
															|| furl == null) {
														return;
													}
													if (type != 'doc'
															&& type != 'xls') {
														alert('仅支持上传doc、xls格式的文件!');
														return;
													}
													fname = furl
															.substring(furl
																	.lastIndexOf("\\") + 1);
													fileform.form
															.submit( {
																url : 'xxfb.htm?action=addFileInNews&fu=' + furl,
																waitMsg : '正在上传......',
																waitTitle : '请等待',
																method : 'POST',
																success : function(
																		form,
																		action) {
																	var element = document
																			.createElement("a");
																	element.href = action.result.fileURL;
																	element.target = '_blank';
																	element.innerHTML = fname;
																	if (Ext.isIE) {
																		editor
																				.insertAtCursor(element.outerHTML);
																	} else {
																		var selection = editor.win
																				.getSelection();
																		if (!selection.isCollapsed) {
																			selection
																					.deleteFromDocument();
																		}
																		selection
																				.getRangeAt(
																						0)
																				.insertNode(
																						element);
																	}
																	form
																			.reset();
																	winFile
																			.close();
																},
																failure : function(
																		form,
																		action) {
																	form
																			.reset();
																	if (action.failureType == Ext.form.Action.SERVER_INVALID)
																		Ext.MessageBox
																				.alert(
																						'警告',
																						'上传失败，仅支持上传doc、xls格式的文件!');

																}

															});
												}
											}, {
												text : '关闭',
												type : 'submit',
												handler : function() {
													winFile.close(this);
												}
											} ]
								})

						var winFile = new Ext.Window( {
							title : "上传附件",
							id : 'picwin',
							width : 400,
							height : 150,
							modal : true,
							border : false,

							layout : "fit",
							items : fileform

						});
						winFile.show();

					},
					createToolbar : function(editor) {
						HTMLEditor.superclass.createToolbar.call(this, editor);
						this.tb.insertButton(16, {
							cls : "x-btn-icon",
							icon : "picture.png",
							handler : this.addImage,
							scope : this
						});
						this.tb.insertButton(17, {
							cls : "x-btn-icon",
							icon : "public/images/icons/doc.png",
							handler : this.addFile,
							scope : this
						});
					}
				});
Ext.reg('starthtmleditor', HTMLEditor);