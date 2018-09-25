<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	
	var pageLimit = parseInt("${_userDic.defaultPageSize}");
	var gridDataUrl = "log/log-grid-data.htm";
	var rowId = 'logId';
	var storeFields = [
			rowId,
            'action',
            'operator',
            'ip',
            'identityInfo',
            {name: 'logTime', type: 'date',dateFormat:"Y-m-d H:i:s"}
        ];
    var tabTitle = "日志查询";
    var sm = Ext.create('Ext.selection.CheckboxModel',{checkOnly:true});//复选框
    //Ext.apply(sm,{locked: true});//为复选框加上locked属性，以便可以把复选框放在第一行并且被锁住。
    var gridColumns = [
    	Ext.create('Ext.grid.RowNumberer',{
    		header:'No',
    		width: 40,
    		renderer: function(value,metadata,record,rowIndex){
    			return (store.currentPage -1)* pageLimit + rowIndex+1;
    		}
    	}),
    	{
            header: "操作",
            dataIndex: 'action',
            width:500,
            sortable: true,
            renderer:function(value, p, record) {
		        return Ext.String.format('<span title=\'{0}\'>{0}</span>',value);
		    }
        },{
            header: "操作人",
            dataIndex: 'operator',
            width: 100,
            sortable: true
        },{
            header: "ip地址",
            dataIndex: 'ip',
            width: 100,
            sortable: true
        },{
            header: "时间",
            dataIndex: 'logTime', 
            renderer:renderDateISOLong,
            //locked: true,
            width: 150,
            sortable: true
        },{
            header: "身份信息",
            dataIndex: 'identityInfo', 
            hidden:true,
            width: 200,
            sortable: true
        }];
    
    // 数据存储
    var store = new Ext.data.JsonStore({
    	proxy: {
	        type: 'ajax',
	        actionMethods:{read:'post'},
	        url: gridDataUrl,
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
    
	function search() {
		 store.getProxy().setExtraParam("ip",ipField.getValue());
		 store.getProxy().setExtraParam("userName",userField.getValue());
		 store.getProxy().setExtraParam("startTime",startTimeField.getValue());
		 store.getProxy().setExtraParam("endTime",endTimeField.getValue());
		 store.loadPage(1);
		 r.show();
	}
	
	var q = Ext.widget('button',{
		text : '查询',
		iconCls : 'icon-find',
		handler : search
	});
	
	var ipField = Ext.widget('textfield',{				
		fieldLabel:'ip',
		labelWidth:20,
		labelPad:0,
		width:100
	});
	
	var userField = Ext.widget('textfield',{				
		fieldLabel:'操作人',
		labelWidth:50,
		labelPad:0,
		width:120
	});
	
	var startTimeField = Ext.widget('datefield',{
		fieldLabel:'开始',
		labelWidth:40,
		format:'Y-m-d H:i:s',
		labelPad:0,
		width:200
	});
	var endTimeField = Ext.widget('datefield',{
		fieldLabel:'结束',
		labelWidth:40,
		format:'Y-m-d H:i:s',
		labelPad:0,
		width:200
	});
	
	var r = Ext.widget('button',{
		text : '清空',
		iconCls : 'icon-undo',
		handler : function() {
			ipField.setValue(null);
			userField.setValue(null);
			startTimeField.setValue(null);
			endTimeField.setValue(null);
			search();
		}
	});
        
    var grid = new Ext.grid.GridPanel({
        title:tabTitle,
        store: store,
        //id:'userListGrid',
        trackMouseOver:true,
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
            forceFit:true,
            overItemCls:'x-grid-cell-inner-nohand' //把grid的鼠标手指去掉
        },
		
		tbar: ['->',ipField,userField,startTimeField,endTimeField,q,r],
        // paging bar on the bottom
        dockedItems: [{
	        xtype: 'pagingtoolbar',
	        store: store,   // same store GridPanel is using
	        dock: 'bottom',
	        displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
	    }]
    });
	
	store.sort('logTime','DESC');
	
    grid.on("added",function(cmp,ct){
    	ct.on("activate",function(p){
    		store.reload();
    	});
    });
    return grid;
})();
