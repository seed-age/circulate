<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	
	var pageLimit = parseInt("${_userDic.defaultPageSize}");
	var gridDataUrl = "users/users-grid-data.htm";
	var newFormUrl = "users/new-form.htm";
	var deleteBeansUrl = "users/delete-beans.htm";
	var editFormUrl = "users/edit-form.htm";
	var rowId = 'userId';
	var storeFields = [
			rowId,
            'accountName',
            'nickName',
            {name: 'createTime', type: 'date',dateFormat:"Y-m-d H:i:s"},
            {name: 'lastLogin', type: 'date',dateFormat:"Y-m-d H:i:s"},
            {name: 'enabled',type:'boolean'},
            {name: 'admin',type:'boolean'}
        ];
    var tabTitle = "用户管理";
    var sm = Ext.create('Ext.selection.CheckboxModel',{checkOnly:true});//复选框
    //Ext.apply(sm,{locked: true});//为复选框加上locked属性，以便可以把复选框放在第一行并且被锁住。
    var gridColumns = [
    	Ext.create('Ext.grid.RowNumberer',{
    		header:'No',
    		//locked: true,
    		width: 40,
    		renderer: function(value,metadata,record,rowIndex){
    			return (store.currentPage -1)* pageLimit + rowIndex+1;
    		}
    	}),
    	{
            header: "登录账号",
            dataIndex: 'accountName',
            //locked: true,
            width: 100,
            flex: 1,
            sortable: true
        },
    	{
            header: "昵称",
            dataIndex: 'nickName',
            //locked: true,
            width: 200,
            sortable: true
        },{
            header: "创建时间",
            dataIndex: 'createTime', 
            renderer:renderDateISOLong,
            //locked: true,
            width: 200,
            sortable: true
        },{
            header: "最后登录时间",
            dataIndex: 'lastLogin',
            renderer:renderDateISOLong,
            width: 200,
            sortable: true
        },{
            header: "状态",
            dataIndex: 'enabled',
            align: 'center',
            width: 70,
            sortable: true,
            renderer: function(value,meta){
            	if(value){
            		meta.style = "color:green"; 
            		return '激活';
            	}else{
            		meta.style = "color:red"; 
            		return '禁止';
            	}
            }
        },{
            header: "是否系统管理员",
            dataIndex: 'admin',
            align: 'center',
            width: 80,
            sortable: true,
            renderer: function(value){
            	if(value){
            		return '是';
            	}else{
            		return '否';
            	}
            }
        }];
    //添加按钮
    var newButton = Ext.create('Ext.Button', {
    	text: '添加',
    	iconCls :'icon-add',
    	handler: function(){
    		Ext.Ajax.request({
    			method:'get',
    			url:newFormUrl,
    			success:function(xhr){
    				var newWin = eval(xhr.responseText);
    				newWin.show();
    			}
    		});
    	}
    });
    
    var reloadFn = function(xhr){
    	try{
    		var result = Ext.decode(xhr.responseText);
    		if(result.msg)Ext.Msg.alert("警告",result.msg);
    	}catch(e){
    		alert(e.description);
    	}
	    store.reload();
	};
    
    var delButton =  Ext.create('Ext.Button', {
        text: '删除',
        iconCls :'icon-delete',
        handler: function(){
            var selected = grid.getSelectionModel().getSelection();
            var params = []
            Ext.each(selected,function(item){
            	params.push(item.get(rowId));
            });
			if(params.length == 0){
		   		Ext.Msg.alert("提示","请选择要删除的数据！");
		   		return;
		    }else{
		       Ext.Msg.confirm('警告', '是否删除数据？', function(btn){
	        		if(btn == "yes"){
						Ext.Ajax.request({
						   url: deleteBeansUrl,
						   success: reloadFn,
						   failure: reloadFn,
						   params: { ids: params }
						});
	        		}
	        		
	        	});
	           
			}
        }
    });
    // 数据存储
    var store = new Ext.data.JsonStore({
    	storeId:"usersGridStore",
    	proxy: {
	        type: 'ajax',
	        url: gridDataUrl,
	        actionMethods:{read:'post'},//查询中文的话，需要Post方式发送参数
	        reader: {
	            type: 'json',
	            root: 'list',
	            idProperty: rowId,
	            totalProperty: 'totalRecord'
	        }
	    },
	    pageSize: pageLimit,//pageSize 表示每页有几条记录,
        remoteSort: true,
        fields: storeFields
       
    });
   
    //grid事件定义
    var gridListeners = {
        	cellclick:function(table,td,cellIndex,record,tr,rowIndex, e,options) {
			   if(cellIndex != 0){
	       			Ext.Ajax.request({
			    		url:editFormUrl,
			    		method:'get',
			    		params: { editId :record.get(rowId)},
			    		success:function(xhr){
			    			var editWin = eval(xhr.responseText);
			    			editWin.show();
			    		}
			    	});
				}
			}
        };
        
    function search() {
		 var search=textField.getValue();
		 store.getProxy().setExtraParam("search",search);
		 store.loadPage(1);
		 r.show();
	}
	
	var textField=Ext.widget('textfield',{
		fieldLabel:'账号',
		labelWidth:40,
		labelPad:0,
		width:200
	});
	var q = Ext.widget('button',{
		text : '查询',
		iconCls : 'icon-find',
		handler : search
	});
	var r = Ext.widget('button',{
		text : '清空',
		iconCls : 'icon-undo',
		handler : function() {
			textField.setValue("");
			store.getProxy().setExtraParam("search",null);
			store.loadPage(1);
			r.hide();
		}
	});
    
    var grid = new Ext.grid.GridPanel({
        title:tabTitle,
        store: store,
        //id:'userListGrid',
        trackMouseOver:true,
        stripeRows: true,
        stateId:'users-grid',
		stateful:true,
		stateEvents:['reconfigure'],
        //disableSelection:true,
        loadMask: true,
		tools:[{
			type:'gear',
			handler: function(){
				Ext.Msg.prompt('设置', '每页行数:', function(btn, text){
					var lines = parseInt(text);
	                if(btn == 'ok' && lines < 51 && lines > 4){
	                	pageLimit = lines;
	                	var callback = function(xhr,opts){
	                		var result = Ext.decode(xhr.responseText);
							if(result.success == false){
								Ext.Msg.alert('错误',result.msg);
							}else{
								store.pageSize = pageLimit;
								store.loadPage(1,{
								 	params:{
								 		"limit":pageLimit
								 	}
								});
							}
	                	}
	                	setDictionary("defaultPageSize",lines,callback);//详情参考 init-config.js里面的setDictionary方法。
	                }
	            });
				
				
			}
		}],
        // grid columns
        columns:gridColumns,
        // customize view config
        viewConfig: {
            forceFit:true
        },

		
		tbar: [newButton,'-',delButton,'->',textField,q,r],
        // paging bar on the bottom
        dockedItems: [{
	        xtype: 'pagingtoolbar',
	        store: store,   // same store GridPanel is using
	        dock: 'bottom',
	        displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
	    }],
       
        selModel: sm,
        listeners:gridListeners
    });
    
    
    
	grid.on("added",function(cmp,ct){//激活的时候自动刷新
    	ct.on("activate",function(p){
    		store.reload();
    	});
    	store.sort('createTime','DESC');
    });
    
    return grid;
})();
