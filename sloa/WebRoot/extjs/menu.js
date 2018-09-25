

//根据设置创建主菜单
function menuInit(){
	return dynamicMenuCreate();
}

var menuConfig={
	title : '主菜单',
	id : 'menus',
	iconCls: 'icon-hotel',
	region : 'west',
	width : 200,
	split : true,
	//margins : '2 2',
	//cmargins : '2 2',
	layout : 'accordion',
	collapsible: true,
	layoutConfig : {
		// layout-specific configs go here
		animate : true
		// activeOnTop: true
	}
};

/**
 * 动态菜单
 * @return
 */
function dynamicMenuCreate(){
	var menuPanel = Ext.widget("panel",menuConfig);
	
	var menuSelFunction = function(view,record) {
		var n = record.raw;
		if (n.leaf == true) { 
			var layoutAttr = n.layoutAttr || {
				layout : "fit"
			};//自定义布局，默认布局是fit，自定义布局来自属性菜单定义
			var action = n.action ;
			var tabId = n.extId? n.extId + "Tab" : "menu" + n.id + "Tab";
			var tabTitle = n.text;
			addLazyLoadWorkSpaceTab(tabId,tabTitle,action,layoutAttr,null,n.icon,n.iconCls);
		}
	}
	
	menuPanel.on("render",function(){
		Ext.Ajax.request({
			url:_context+'system/menus.htm',
			success:function(xhr){
				var menus = Ext.decode(xhr.responseText);
				Ext.each(menus,function(item){
					var treeStore = Ext.create('Ext.data.TreeStore', {
				        proxy: {
				            type: 'ajax',
				            url: _context+'system/menus.htm'
				        },
				        root: item
					});
					var idConfig = item.extId ? {id:item.extId}:{};
					var treeConfig = {
						//id : item.id,//ID不能为数字
						title : item.text,
						split : true,
						minSize : 100,
						autoScroll : true,

						// tree-specific configs:
						rootVisible : false,
						lines : false,
						singleExpand : false,
						
						useArrows : true,
						listeners:{
							itemclick:menuSelFunction
						},
						store : treeStore

					}
					Ext.apply(treeConfig,idConfig);
					if(item.icon){
						Ext.apply(treeConfig,{icon: item.icon});
					}else if(item.iconCls){
						Ext.apply(treeConfig,{iconCls: item.iconCls});
					}
					menuPanel.add(Ext.widget("treepanel",treeConfig));
				});
				//menuPanel.doLayout();
			}
		});
	});
	
	return menuPanel;
}