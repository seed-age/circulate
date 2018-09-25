<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>(function() {
			var login = function(){
			    var enc = new JSEncrypt();
			    enc.setPublicKey(_publicKey);
			    var username = loginForm.getForm().findField("j_username").getValue();
			    var password = loginForm.getForm().findField("j_password").getValue();
			    var encUsername = enc.encrypt(username);
			    var encPassword = enc.encrypt(password);
				Ext.Ajax.request({
					url : _loginUrl,
					params : {
						j_username : encUsername,
						j_password : encPassword,
						validateCode : loginForm
								.getForm()
								.findField("validateCode")
								.getValue()
					},
					success : function(xhr) {
						loginWin.hide();
						loginForm.getForm().reset();
					}
				});
			}
			
			var loginForm = Ext.widget("form", {
						labelWidth : 50,
						frame : true,
						bodyStyle : 'padding:5px 5px 0',
						width : 270,
						defaults : {
							width : 200
						},
						defaultType : 'textfield',

						items : [{
									fieldLabel : '用户名',
									name : 'j_username'
								}, {
									fieldLabel : '密码',
									name : 'j_password',
									enableKeyEvents:true,
									inputType : 'password'
								},{
									fieldLabel : '验证码',
									name : 'validateCode',
									listeners:{ 
										specialkey : function(field, e) {  
        								if (e.getKey() == Ext.EventObject.ENTER) {  
												login();
											}
										}
									}
								},{  
								    itemId: 'vcode',  
								    xtype: 'box',  
								    width: 100,  
								    height: 30,  
								    autoEl: {  
								        tag: 'img'
								    }  
								}],
						buttons : [{
										text : '重置验证码',
										handler : function() {
											try{
												loginForm.getComponent('vcode').getEl().dom.src = '<c:url value="/vcode.jpg"/>?v=' + Math.random();
											}catch(e){
												
											}
										}
									},{
										text : '登录',
										handler : function() {
											login();
										}
									}, {
										text : '取消',
										handler : function() {
											loginWin.hide();
										}
									}]
					});

			//
			var loginWin = Ext.widget("window", {
						layout : 'fit',
						title : '重登录',
						modal : true,
						closable : false,
						id : 'login-window',

						items : [loginForm],
						listeners:{
							show:function(){
								loginForm.getForm().findField('j_username').focus(true,500);
								try{
									loginForm.getComponent('vcode').getEl().dom.src = '<c:url value="/vcode.jpg"/>?v=' + Math.random();
								}catch(e){
									
								}
							}
						}
					});

			return loginWin;
		})();
