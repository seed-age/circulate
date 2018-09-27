<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	//TODO 图标样式可选，图标可上传
	var iconsStoreCfg = {
		xtype : "store",
		autoLoad : true,
	    fields: ['iconPath'],
	    proxy: {
	         type: 'ajax',
	         url: 'menus/icon-selector.htm',
	         reader: {
	             type: 'json'
	         }
	     }
	};
	
	var iconsComboBoxCfg = {
		xtype : 'combo',
	    fieldLabel: '图标（选填）',
	    store: iconsStoreCfg,
	    name : 'icon',
	    queryMode: 'remote',
	    valueField: 'iconPath',
	    tpl: Ext.create('Ext.XTemplate',
	        '<tpl for=".">',
	            '<div class="x-boundlist-item"><img src="{iconPath}"/>{iconPath}</div>',
	        '</tpl>'
	    ),
	    displayTpl: Ext.create('Ext.XTemplate',
	        '<tpl for=".">',
	            '{iconPath}',
	        '</tpl>'
	    )
	};
	
	var formConfig={
        labelWidth: 80, 
        url:'menus/save-or-update-menu.htm',
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
                name: 'menuId',
                xtype:'hidden'
            },{
                name: 'parentId',
                xtype:'hidden'
            },{
                fieldLabel: '菜单名（必填）',
                name: 'menuText',
                maxLength:20,
                minLength:4,
                allowBlank:false
            },{
                fieldLabel: 'Ext Id（选填）',
                name: 'extId',
                maxLength:64
            },iconsComboBoxCfg,{
                fieldLabel: '图标class（选填）',
                name: 'iconCls',
                maxLength:32
            },{
               	fieldLabel: '菜单/目录',
                xtype: 'radiogroup',
                layout: 'hbox',
                items: [{
                    inputValue: 'true',
                    boxLabel: '菜单',
                    width:80,
                    name: 'leaf'
                },{
                    inputValue: 'false',
                    boxLabel: '目录',
                    flex:1,
                    name: 'leaf'
                }],
                listeners:{
					change:function(field,newValue,oldValue,e){
						if(newValue.leaf == 'true'){
							field.up("form").getForm().findField('actionPath').enable();
							field.up("form").getForm().findField('expanded-group').disable();
						}else{
							field.up("form").getForm().findField('actionPath').disable();
							field.up("form").getForm().findField('expanded-group').enable();
						}
						//alert(Ext.encode(newValue));
					}
				}
            },{
                fieldLabel: '链接action',
                name: 'actionPath',
                maxLength:128
            },{
               	fieldLabel: '展开目录',
				xtype: 'radiogroup',
				name: 'expanded-group',
				layout: 'hbox',
				items: [{
				    inputValue: 'true',
				    boxLabel: '是',
				    width:80,
				    name: 'expanded'
				},{
				    inputValue: 'false',
				    boxLabel: '否',
				    flex:1,
				    name: 'expanded'
				}]
            },{
               	fieldLabel: '启用菜单',
                xtype: 'radiogroup',
                layout: 'hbox',
                items: [{
                    inputValue: 'true',
                    boxLabel: '是',
                    width:80,
                    name: 'enabled'
                },{
                    inputValue: 'false',
                    boxLabel: '否',
                    flex:1,
                    name: 'enabled'
                }]
            },{
            	fieldLabel: '菜单属性（layoutAttr）',
                name: 'layoutAttr',
	            xtype: 'textarea',
	            maxLength:60,
	            flex: 1
            }]
    };
    var roleStoreCfg={
		autoLoad : false,
        proxy: {
            type: 'ajax',
            url: 'menus/load-menu-roles-data.htm'
        },
        root: {
        	id :'0',
        	text: '角色',
        	iconCls: 'icon-group'
        }
	};
	var roleTreeCfg = {
		frame : true,
		split : true,
		iconCls: 'icon-group',
		minSize : 100,
		title : '菜单使用角色',
		autoScroll : true,
		height:'40%',
		flex:1,
		// tree-specific configs:
		lines : false,
		useArrows : true
		
	};
    
    var win = null;
    
    var addRoleStore = Ext.create('Ext.data.TreeStore',Ext.applyIf({root: {
        	id :'0',
        	text: '角色',
        	iconCls: 'icon-group',
        	expanded: true
        }},roleStoreCfg ));
	
	var addRoleTree = Ext.widget("treepanel",Ext.apply({store : addRoleStore},roleTreeCfg));
    
	var newAction =  Ext.widget('button',{
		text : '增加',
		//disabled:true,
		iconCls : 'icon-add',
		handler : function() {
			var fp;
			if(!win){
				fp=Ext.widget("form",formConfig);
				win = new Ext.Window({
					title:'新增菜单',
					iconCls: 'icon-edit',
					height:560,
					tbar:[{
						text : '保存',
						//disabled:true,
						iconCls : 'icon-save',
						handler : function() {
							var roles = addRoleTree.getChecked(),
			                    ids = [];
			                    
			                Ext.Array.each(roles, function(rec){
			                    ids.push(rec.get('id'));
			                });
							var config = {
								params:{
									roleIds:ids
								},
								success:function(f,action){
									treeStore.load({ node:  treeStore.getNodeById(action.result.loadTreeId)});//重新加载那个节点。
									fp.getForm().reset();
									Ext.Array.each(roles, function(rec){
					                    rec.set('checked',false);
					                });
									win.hide();
								}
							};
							Ext.applyIf(config,commonSubmitConfig)
							fp.getForm().submit(config);
						}
					}],
					width:500,
					plain:true,
				    constrain: true,
				    bodyStyle:'padding:1px;',
					layout: {
				        type: 'vbox',
				        align: 'stretch'
				    },
					items:[fp,addRoleTree],
					modal : true,
					closeAction: 'hide'
				});
			}else{
				fp=win.getComponent(0);
			}
			if(fp){
				var sn = tree.getSelectionModel().getSelection();
				if( sn.length == 1 && sn[0])fp.getForm().findField('parentId').setValue(sn[0].get('id'));
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
				Ext.Msg.Alert('请选择要删除的菜单');
				return;
			}
			Ext.Msg.confirm('警告', '是否要删除菜单  '+(sn[0].get('text'))+'？', function(btn){
			    if (btn == 'yes'){
					Ext.Ajax.request({
							url:'menus/delete-menu.htm',
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
	
	var menuSelFunction = function(view,record) {
		var n = record.raw;
		editRoleStore.getProxy().setExtraParam("menuId",n.id);
		editRoleStore.load({callback:function(){
			editRoleStore.getRootNode().expand(); 
		}});
		
		form.getForm().reset();
		if(n.id == '0'){
			delAction.disable();
			newAction.enable();
			formPanel.disable();
		}else{
			delAction.enable();
			formPanel.enable();
			if(n.leaf){
				newAction.disable();
			}else{
				newAction.enable();
			}
			
			//form.doLayout();
			var config = {
	    		url:'menus/load-menu-bean-data.htm', 
	    		params:{id:n.id}
	    		};
	    	Ext.apply(config,commonLoadFormConfig);
	    	form.getForm().load(config);
		}
	}
	
	var treeStore = Ext.create('Ext.data.TreeStore', {
        proxy: {
            type: 'ajax',
            url: 'menus/load-menus-node-data.htm'
        },
        root: {
        	id:'0',
        	text: '主菜单',
        	iconCls: 'icon-hotel',
        	expanded: true
        }
	});
	var treeConfig = {
		region : 'west',
		title : '菜单树',
		split : true,
		minSize : 100,
		autoScroll : true,
		width:250,
		stateId:'menus-manage-tree',
		stateful:true,
		stateEvents:['resize'],
		// tree-specific configs:
		lines : false,
		icon: '../extjs/icons/img/manage.png',
		useArrows : true,
		listeners:{
			itemclick:menuSelFunction,
			itemmove:function(moveNode,oldParent,newParent,index){//拖拉排序
				Ext.Ajax.request({
					url:'menus/move-menu.htm',
					params:{
						indexValue : index,
						newParentId : newParent.get('id'),
						oldParentId : oldParent.get('id'),
						nodeId : moveNode.get('id')
					},
					success : function(){
						treeStore.load({node : newParent});
						if(oldParent != newParent){
							treeStore.load({node : oldParent});
						}
					}
				});
				
			}
		},
		viewConfig: {
		    plugins: { ptype: 'treeviewdragdrop' }
		},
		tbar:[newAction,'-',delAction],
		store : treeStore,
		tools:[{
				type:'help',
				qtip:'可以拖动菜单进行排序',
				handler:function(ev, el, p){
					Ext.Msg.alert('',"可以拖动菜单进行排序");
				}
			  },{
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
	var form = Ext.widget("form",Ext.apply({height:320},formConfig));
	
	var editRoleStore = Ext.create('Ext.data.TreeStore',roleStoreCfg );
	
	var editRoleTree = Ext.widget("treepanel",Ext.apply({flex:1,store : editRoleStore},roleTreeCfg));
	
	var formPanel = Ext.widget("panel",{
		region:'center',
		title:'菜单项编辑',
		iconCls: 'icon-edit',
		disabled: true,
		tbar:[{
			text : '保存',
			//disabled:true,
			iconCls : 'icon-save',
			handler : function() {
				var roles = editRoleTree.getChecked(),
                    ids = [];
                    
                Ext.Array.each(roles, function(rec){
                    ids.push(rec.get('id'));
                });
				var config = {
					params:{
						roleIds:ids
					},
					success:function(f,action){
						treeStore.load({ node:  treeStore.getNodeById(action.result.loadTreeId)});//重新加载那个节点。
					}
				};
				Ext.applyIf(config,commonSubmitConfig)
				form.getForm().submit(config);
			}
		}],
		bodyStyle:'padding:1px;',
		layout: {
	        type: 'vbox',
	        align: 'stretch'
	    },
		items:[form,editRoleTree]
	});
	
    
	var panel = Ext.widget("panel",{
		layout:'border',
		items:[tree,formPanel]
	});
	
	return panel;
})();
