<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var saveFormUrl = 'users/update-bean.htm';

    var editForm = Ext.create("Ext.form.Panel",{
        labelWidth: 75, 
        url:saveFormUrl,
        frame:true,
       	width: 400,
       	iconCls: 'icon-person',
        title: '账号',
        trackResetOnLoad : true,//reset form的时候可以恢复到刚加载的时候的值
        fieldDefaults: {
              msgTarget: 'side',
              width: 325
        },
        defaultType: 'textfield',

        items: [{xtype: 'fieldset',
                title: '账号信息',
                defaults: {
                    labelWidth: 75
                    
                },
                defaultType: 'textfield',
                items:[{
		                name: 'userId',
		                xtype: 'hidden'
		            },
		        	{
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
		                //minLength:4,
		                inputType: 'password',
		                validator: function(value){
		                	if(value != "" && value.length < 4){
		                		return "密码至少4位";
		                	}
		                	return true;
		                }
		            },{
		                fieldLabel: '确认密码',
		                id: 'pwd-cfm',
		                inputType: 'password',
		                maxLength:20,
		                //minLength:4,
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
			            height: 25,
			            defaults: {
			                flex: 1
			            },
			            layout: 'hbox',
			            items: [
			                {
			                    boxLabel  : '激活',
			                    name      : 'enabled',
			                    checked   : true,
			                    inputValue: "true"//必须是字符串，form load的时候才能被识别
			                }, {
			                    boxLabel  : '禁止',
			                    name      : 'enabled',
			                    inputValue: "false"//必须是字符串，form load的时候才能被识别
			                }
			            ]
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
		            height: 25,
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
		 }]
    });
    editForm.on("render",function(cmp){
    	var config = {
    		url:'users/load-bean-data.htm', 
    		params:{id:'${editId}'}
    		};
    	Ext.apply(config,commonLoadFormConfig);
    	cmp.getForm().load(config);
    	roleStore.getProxy().setExtraParam("userId",'${editId}');
		roleStore.load({callback:function(){
			roleStore.getRootNode().expand(); 
		}});
    });
    
   var roleStore = Ext.create('Ext.data.TreeStore', {
        proxy: {
            type: 'ajax',
            url: 'users/load-user-roles-data.htm'
        },
        root: {
        	id :'0',
        	text: '角色',
        	iconCls: 'icon-group'
        }
	});
	
	var roleTree = Ext.widget("treepanel",{
		frame : true,
		split : true,
		iconCls: 'icon-group',
		minSize : 100,
		title : '角色',
		autoScroll : true,
		flex:1,
		// tree-specific configs:
		lines : false,
		useArrows : true,
		store : roleStore
		
	});
    
    var win = Ext.widget("window",{
        width: 650,
        height:400,
        autoScroll:true,
        layout: {
	        type: 'hbox',
	        align: 'stretch'
	    },
        title: '编辑用户',
        plain:true,
        modal:true,
        constrain: true,
        bodyStyle:'padding:1px;',
        items: [editForm,roleTree],

        tbar: [{
            text: '保存',
            iconCls:'icon-save',
            handler:function(){
            	var roles = roleTree.getView().getChecked(),
                    ids = [];
                    
                Ext.Array.each(roles, function(rec){
                    ids.push(rec.get('id'));
                });
            	editForm.getForm().submit({
            		params:{
						roleIds:ids
					},
            		success:function(form,action){
            			store.reload();//可以访问到被调用eval的方法块的store变量哦
            			win.close();
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
            	editForm.getForm().reset();
            }
        }]
    });

    return win;
})();
