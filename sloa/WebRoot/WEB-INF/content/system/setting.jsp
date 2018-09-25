<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var saveFormUrl = 'save-setting.htm';

    var editForm = Ext.create("Ext.form.Panel",{
        labelWidth: 75, 
        url:saveFormUrl,
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        height:380,
       // width: 500,
       // defaults: {width: 350},
        trackResetOnLoad : true,//reset form的时候可以恢复到刚加载的时候的值
        width: 350,
        defaults: {width: 230},
        defaultType: 'textfield',

        items: [
        	{
                fieldLabel: '旧密码',
                name: 'oldPassword',
                maxLength:20,
                //minLength:4,
                inputType: 'password',
                validator: function(value){
                	if(value != "" && value.length < 4){
                		return "密码至少4位";
                	}
                	return true;
                }
            },
        	{
                fieldLabel: '新密码',
                name: 'password',
                id: 'settingPassword',
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
                id: 'settingPassword-cfg',
                inputType: 'password',
                maxLength:20,
                //minLength:4,
                submitValue: false,//确认密码不用提交
                vtype: 'password',
        		initialPassField: 'settingPassword' // id of the initial password field
            }
	        ]
    });
    editForm.on("render",function(cmp){
    	var config = {
    		url:'load-setting-data.htm'
    		};
    	Ext.apply(config,commonLoadFormConfig);
    	cmp.getForm().load(config);
    });
    
    var win = Ext.widget("window",{
        title: '${formTitle}',
        width: 350,
        height:250,
        layout: 'fit',
        title: '设置',
        plain:true,
        modal:true,
        constrain: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: editForm,

        buttons: [{
            text: '保存',
            handler:function(){
            	editForm.getForm().submit({
            		success:function(){
            			win.close();
            		},
            		failure:function(form,action){
                    	if(action.result){
		            		Ext.Msg.alert("出错",action.result.msg);
		            	}
                    }
            	});
            }
        },{
            text: '重置',
            handler:function(){
            	editForm.getForm().reset();
            }
        }]
    });

    return win;
})();
