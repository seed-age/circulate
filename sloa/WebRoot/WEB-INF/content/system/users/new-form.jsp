<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
    var newForm = Ext.create("Ext.form.Panel",{
        labelWidth: 75, 
        url:'users/insert-bean.htm',
        frame:true,
        width: 400,
        iconCls: 'icon-person',
        title: '账号',
        bodyStyle:'padding:0px 0px 0',
        fieldDefaults: {
              msgTarget: 'side',
              width: 325
        },

        items: [{xtype: 'fieldset',
                title: '账号信息',
                defaults: {
                    labelWidth: 75
                    
                },
                defaultType: 'textfield',
                items:[{
		                fieldLabel: '账户名',
		                name: 'accountName',
		                maxLength:20,
		                minLength:4,
		                allowBlank:false
		            },{
		                fieldLabel: '昵称',
		                name: 'nickName',
		                maxLength:20,
		                minLength:1
		            },{
		                fieldLabel: '密码',
		                name: 'userPassword',
		                id: 'userPassword',
		                maxLength:20,
		                minLength:4,
		                allowBlank:false,
		                inputType: 'password'
		            },{
		                fieldLabel: '确认密码',
		                id: 'pwd-cfm',
		                inputType: 'password',
		                maxLength:20,
		                minLength:4,
		                allowBlank:false,
		                submitValue: false,//确认密码不用提交
		                vtype: 'password',
		        		initialPassField: 'userPassword' // id of the initial password field
		            }]
            },{xtype: 'fieldset',
                title: '激活状态下的账号才能登录系统',
                defaults: {
                    labelWidth: 75,
                    anchor: '100%'
                },
                items:[{
		            xtype      : 'fieldcontainer',
		            fieldLabel : '状态',
		            defaultType: 'radiofield',
		            layout: 'hbox',
		            defaults: {
		                flex: 1
		            },
		            items: [
		                {
		                    boxLabel  : '激活',
		                    name      : 'enabled',
		                    checked   : true,
		                    inputValue: true
		                }, {
		                    boxLabel  : '禁止',
		                    name      : 'enabled',
		                    inputValue: false
		                }]
	        	}]
	        },{xtype: 'fieldset',
                title: '系统管理员拥有最高权限',
                defaults: {
                    labelWidth: 75,
                    anchor: '100%'
                },
                items:[{
		            xtype      : 'fieldcontainer',
		            fieldLabel : '设置',
		            defaultType: 'radiofield',
		            defaults: {
		                flex: 1
		            },
		            layout: 'hbox',
		            items: [
		                {
		                    boxLabel  : '是',
		                    name      : 'admin',
		                    checked   : true,
		                    inputValue: 'true'
		                }, {
		                    boxLabel  : '否',
		                    name      : 'admin',
		                    inputValue: 'false'
		                }
		            ]
	        	}]
	        }
	        ]

    });
    
   var roleStore = Ext.create('Ext.data.TreeStore', {
        proxy: {
            type: 'ajax',
            url: 'users/load-user-roles-data.htm'
        },
        root: {
        	id :'0',
        	text: '角色',
        	iconCls: 'icon-group',
        	expanded: true
        }
	});
	
	var roleTree = Ext.widget("treepanel",{
		frame : true,
		split : true,
		iconCls: 'icon-group',
		minSize : 100,
		title : '可选角色',
		autoScroll : true,
		flex:1,
		// tree-specific configs:
		lines : false,
		useArrows : true,
		store : roleStore
		
	});
    
    var newWin = Ext.widget("window",{
    					title: '添加新用户',
				        width: 650,
        				height:400,
				        modal :true,
				        layout: {
					        type: 'hbox',
					        align: 'stretch'
					    },
				        plain:true,
				        constrain: true,
				        closable: true,
				        bodyStyle:'padding:1px;',
				        items: [newForm,roleTree],       
				        tbar: [{
				            text: '保存',
				            iconCls: 'icon-save',
				            handler:function(){
				            	var roles = roleTree.getView().getChecked(),
				                    ids = [];
				                    
				                Ext.Array.each(roles, function(rec){
				                    ids.push(rec.get('id'));
				                });
				            	newForm.getForm().submit({
				            		params:{
										roleIds:ids
									},
				            		success:function(form,action){
				            			store.reload();//可以访问到被调用eval的方法块的store变量哦
				            			newWin.close();
				            		},
				            		failure:function(form,action){
				                    	if(action.result){
						            		Ext.Msg.alert("保存失败",action.result.msg);
						            	}
				                    }
				            	});
				            }
				        },{
				            text: '重置',
				            iconCls:'icon-undo',
				            handler:function(){
				            	newForm.getForm().reset();
				            }
				        }]
    				});
    return newWin;
})();
