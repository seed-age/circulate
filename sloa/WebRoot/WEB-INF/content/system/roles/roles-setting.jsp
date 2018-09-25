<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	//TODO 图标样式可选，图标可上传
	function checkAll(node,b){
	  node.set('checked',b);
	  if(node.hasChildNodes){
	  	Ext.Array.each(node.childNodes,function(child){
	  		checkAll(child,b);
	  	});
	  }
	}
	var formConfig={
        labelWidth: 80, 
        url:'roles/save-or-update-role.htm',
        frame:true,
        autoScroll : true,
        bodyStyle:'padding:0px 0px',
        bodyPadding: '5 5 0',
        defaultType: 'textfield',
        fieldDefaults: {
              msgTarget: 'side',
              width: 400
        },
        items: [{
                name: 'roleId',
                xtype:'hidden'
            },{
                fieldLabel: '角色名称',
                name: 'roleName',
                maxLength:32,
                minLength:2,
                allowBlank:false
            },{
                fieldLabel: '角色描述',
                name: 'roleDescription',
                maxLength:200,
                xtype: 'textarea'
            },{
               	fieldLabel: '启用',
                xtype: 'fieldcontainer',
                defaultType: 'radiofield',
                name: 'status',
	            layout: 'hbox',
                items: [{
                    inputValue: 'true',
                    boxLabel: '是',
                    width:80,
                    name: 'status',
                    checked:true
                },{
                    inputValue: 'false',
                    boxLabel: '否',
                    flex:1,
                    name: 'status'
                }]
            }]
    };
    var menuStoreCfg={
        proxy: {
            type: 'ajax',
            url: 'roles/load-role-menus-data.htm'
        },
        root: {
        	id :'0',
        	text: '菜单',
        	iconCls: 'icon-group',
        	expanded: true
        }
	};
	var userStoreCfg={
        proxy: {
            type: 'ajax',
            url: 'roles/load-role-users-data.htm'
        },
        root: {
        	id :'0',
        	text: '用户',
        	iconCls: 'icon-group',
        	expanded: true
        }
	};
	var treeCfg = {
		frame : true,
		split : true,
		minSize : 100,
		autoScroll : true,
		flex:1,
		height:'100%',
		// tree-specific configs:
		lines : false,
		useArrows : true,
		tools: [{
	        type: 'plus',
	        tooltip: '全选',
	        handler: function(event, target, owner, tool){
	            var items = this.up('treepanel').getRootNode().childNodes;
	            Ext.Array.each(items,function(rec){
	            	checkAll(rec,true);
	            })
	        }
    	},{
	        type: 'minus',
	        tooltip: '全不选',
	        handler: function(event, target, owner, tool){
	            var items = this.up('treepanel').getRootNode().childNodes;
	            Ext.Array.each(items,function(rec){
	            	checkAll(rec,false);
	            })
	        }
    	}],
    	listeners:{
    		checkchange:function(node,checked){
    			checkAll(node,checked);
    		}
    	}
	};
	var menuStore = Ext.create('Ext.data.TreeStore',menuStoreCfg);
	var userStore = Ext.create('Ext.data.TreeStore',userStoreCfg);
	var menuTree = Ext.widget("treepanel",
		Ext.apply({
			icon: 'extjs/icons/img/system.png',
			title : '角色菜单',
			store:menuStore
		},treeCfg));
	var userTree = Ext.widget("treepanel",
		Ext.apply({
			iconCls: 'icon-person',
			title : '角色用户',
			store:userStore
		},treeCfg));
	var newMenuTree = Ext.widget("treepanel",
		Ext.apply({
			icon: 'extjs/icons/img/system.png',
			title : '角色菜单',
			store:Ext.create('Ext.data.TreeStore',menuStoreCfg)
		},treeCfg));
	var newUserTree = Ext.widget("treepanel",
		Ext.apply({
			iconCls: 'icon-person',
			title : '角色用户',
			store:Ext.create('Ext.data.TreeStore',userStoreCfg)
		},treeCfg));
    
    var win = null;
	var newAction =  Ext.widget('button',{
		text : '增加',
		//disabled:true,
		iconCls : 'icon-add',
		handler : function() {
			var fp;
			if(!win){
				fp=Ext.widget("form",Ext.apply({region:'center'},formConfig));
				win = new Ext.Window({
					title:'新增角色',
					iconCls: 'icon-edit',
					height:560,
					tbar:[{
						text : '保存',
						//disabled:true,
						iconCls : 'icon-save',
						handler : function() {
							var menus = newMenuTree.getChecked(),
								users = newUserTree.getChecked(),
			                    userIds = [],
			                    menuIds = [];
			                    
			                Ext.Array.each(menus, function(rec){
			                    menuIds.push(rec.get('id'));
			                });
			                Ext.Array.each(users, function(rec){
			                    userIds.push(rec.get('id'));
			                });
							var config = {
								params:{
									userIds:userIds,
									menuIds:menuIds
								},
								success:function(f,action){
									treeStore.load({ node:  treeStore.getNodeById(action.result.loadTreeId)});//重新加载那个节点。
									fp.getForm().reset();
									Ext.Array.each(menus, function(rec){
					                    rec.set('checked',false);
					                });
					                Ext.Array.each(users, function(rec){
					                    rec.set('checked',false);
					                });
									win.hide();
								}
							};
							Ext.applyIf(config,commonSubmitConfig)
							fp.getForm().submit(config);
						}
					}],
					width:600,
					plain:true,
				    constrain: true,
				    bodyStyle:'padding:1px;',
					layout:'border',
					modal : true,
					closeAction: 'hide',
					items:[
						fp,{
							xtype:'container',
							region:'south',
							height:'70%',
							layout:'hbox',
							items:[newMenuTree,newUserTree]
						}
					]
				});
			}else{
				fp=win.getComponent(0);
			}
			win.show(this.el);
		}
	});
	
	var delAction = Ext.widget('button',{
		text:'删除',
		disabled:true,
		iconCls:'icon-delete',
		handler:function(){
			var sn = tree.getSelectionModel().getSelection();
			if(sn.length == 0 || sn[0].get('id') == 0){
				Ext.Msg.Alert('请选择要删除的角色');
				return;
			}
			Ext.Msg.confirm('警告', '是否要删除角色  '+(sn[0].get('text'))+'？', function(btn){
			    if (btn == 'yes'){
					Ext.Ajax.request({
							url:'roles/delete-role.htm',
							params:{id:sn[0].get('id')},
							success:function(xhr){
								var result = Ext.decode(xhr.responseText);
								if(result.success){
									treeStore.load({ node: treeStore.getNodeById(result.loadTreeId)});//重新加载那个节点。
									form.getForm().reset();
								}else{
									Ext.Msg.alert('失败',result.msg);
								}
							}
					});
			    }
			});
		}
	});
	
	function checkSelectedMenus(roleId){
		checkSelected(roleId,'roles/selected-role-menu-ids.htm',menuStore);
	}
	
	function checkSelectedUsers(roleId){
		checkSelected(roleId,'roles/selected-role-user-ids.htm',userStore);
	}
	
	//重新从数据库读取被选中的ID，用id去选中树形中选中的选项
	function checkSelected(roleId,url,store){
		checkAll(store.getRootNode(),false);
		Ext.Ajax.request({
				url:url,
				params:{roleId:roleId},
				success:function(xhr){
					var result = Ext.decode(xhr.responseText);
					Ext.Array.each(result,function(id){
						var node = store.getNodeById(id);
						if(node){
							node.set("checked",true);
						}
					});
				}
		});
	}
	
	var roleSelFunction = function(view,record) {
		var n = record.raw;
		checkSelectedMenus(n.id);
		checkSelectedUsers(n.id);
		
		
		form.getForm().reset();
		if(n.id == '0'){
			delAction.disable();
			formPanel.disable();
		}else{
			delAction.enable();
			formPanel.enable();
			var config = {
	    		url:'roles/load-role-bean-data.htm', 
	    		params:{id:n.id}
	    		};
	    	Ext.apply(config,commonLoadFormConfig);
	    	form.getForm().load(config);
		}
	}
	
	var treeStore = Ext.create('Ext.data.TreeStore', {
        proxy: {
            type: 'ajax',
            url: 'roles/load-roles-node-data.htm'
        },
        root: {
        	id:'0',
        	text: '角色',
        	iconCls: 'icon-group',
        	expanded: true
        }
	});
	var treeConfig = {
		region : 'west',
		title : '角色',
		split : true,
		minSize : 100,
		autoScroll : true,
		width:250,
		// tree-specific configs:
		lines : false,
		iconCls: 'icon-group',
		useArrows : true,
		listeners:{
			itemclick:roleSelFunction
		},
		tbar:[newAction,'-',delAction],
		store : treeStore,
		tools:[{
				type:'plus',
				qtip:'全部展开',
				handler:function(ev, el, p){
					tree.expandAll();
				}
			  },{
				type:'minus',
				qtip:'全部折叠',
				handler:function(ev, el, p){
					tree.collapseAll();
				}
			  }]
	}
	var tree = Ext.widget("treepanel",treeConfig);
	var form = Ext.widget("form",Ext.apply({flex:1,region:'center'},formConfig));
	
	var formPanel = Ext.widget("panel",{
		region:'center',
		title:'角色编辑',
		iconCls: 'icon-edit',
		disabled: true,
		tbar:[{
			text : '保存',
			//disabled:true,
			iconCls : 'icon-save',
			handler : function() {
				var menus = menuTree.getChecked(),
					users = userTree.getChecked(),
                    userIds = [],
                    menuIds = [];
                    
                Ext.Array.each(menus, function(rec){
                    menuIds.push(rec.get('id'));
                });
                Ext.Array.each(users, function(rec){
                    userIds.push(rec.get('id'));
                });
				var config = {
					params:{
						userIds:userIds,
						menuIds:menuIds
					},
					success:function(f,action){
						treeStore.load({ node:  treeStore.getNodeById(action.result.loadTreeId)});//重新加载那个节点。
						checkSelectedMenus(action.result.roleId);
						checkSelectedUsers(action.result.roleId);

					}
				};
				Ext.applyIf(config,commonSubmitConfig)
				form.getForm().submit(config);
			}
		}],
		bodyStyle:'padding:1px;',
		layout: 'border',
		items:[form,{
			xtype:'container',
			height:'60%',
			region:'south',
			layout:'hbox',
			items:[menuTree,userTree]
			}]
	});
	
    
	var panel = Ext.widget("panel",{
		layout:'border',
		items:[tree,formPanel]
	});
	
	return panel;
})();
