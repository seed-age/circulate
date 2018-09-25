Ext.onReady(function() {
			// 初始化全局 QuickTips 实例,为所有元素提供有吸引力和可定制的工具提示
			Ext.tip.QuickTipManager.init();
			// 表单元素的基类，提供事件操作、尺寸调整、值操作与其它功能
			Ext.data.proxy.Server.prototype.simpleSortMode = true;//覆盖ext的默认值
			Ext.get('loading-page').remove();

			// 一种基础性的tab容器
			var tabs =  Ext.widget('tabpanel',{
				id : 'mainTabs',
				resizeTabs : true, // turn on tab resizing
				region : 'center',
				minTabWidth : 135,
				tabWidth : 145,
				enableTabScroll : true,
				layoutOnTabChange : true,
				margins : '2 0',
				defaults : {
					autoScroll : true
				},
				plugins : Ext.create('Ext.ux.TabCloseMenu', {
							closeTabText : '关闭标签',
							closeOtherTabsText : '关闭其他',
							closeAllTabsText : '关闭所有'
						})
					/**
					 * , listeners : {
					 * 
					 * tabchange :function(field, e) { alert("15"); } }
					 */

				});

			// 生成主菜单
			var menu = menuInit();
			var topMenuItems = [{
							    	icon: _context+'extjs/icons/img/user.png',
							        text: _userName
							    },'->',{
							    	icon:_context+'extjs/icons/img/system.png',
							        text: '设置',
							        handler:function(){
							        	Ext.Ajax.request({
							        		url:_context+'system/setting.htm',
							        		success:function(xhr){
							        			var settingWin = eval(xhr.responseText);
							        			settingWin.show(button.getEl());
							        		},
							        		failure : commonAjaxFail
							        	});
							        }
							    },{
							        text: '退出',
							        icon:_context+'extjs/icons/img/user_go.png',
							        //href:_context+'system/logout',
							        //hrefTarget:'_self',
							        
							        handler:function(){
							        	Ext.Ajax.request({
							        		url:_context+'system/logout',
							        		method:'post'
							        	});
							        }
							    }];
			if(debugMode){
				topMenuItems.push({
							        text: 'API参考',
							        icon:_context+'extjs/icons/img/find.png',
							        href:_apiHelpUrl,
							        hrefTarget:'_blank'
							    });
			}
			// 一个表示程序可视区域的特殊容器（浏览器可视区域）。
			Ext.widget("viewport",{
						layout : 'border',
						title : 'Main',
						items : [
							Ext.create('Ext.toolbar.Toolbar', {
							    margin: '0 0 0 0',
							    region : 'north',
							    items: topMenuItems
							})
							, menu, tabs],
						renderTo : Ext.getBody()
					});
			// Ext.getCmp("menus").get(0).collapse();//是否开始的时候合起菜单？
			//超时的时候重登陆框
		//加载登录组件
		 Ext.Ajax.request({
			url:_context+'system/login-window.htm',
			success:function(xhr){
				loginWindow = eval(xhr.responseText);
			},
			failure : commonAjaxFail
		});
});

//增加一个桌面配置tab，可以显示待办事项等信息
function createDeskTop(tabs) {
	tabs.add({
				title : '工作台',
				itemId : 'work-desk',
				iconCls : 'tabs',
				closable : false
			}).show();
	tabs.activate(0);
}
//创建主菜单
function mainMenuCreate() {
	return Ext.widget("panel",{
		title : '主菜单',
		id : 'menus-panel',
		iconCls : 'main-menu-icon',
		region : 'west',
		width : 200,
		margins : '2 4',
		cmargins : '2 4',
		layout : 'accordion',
		collapsible : true,
		layoutConfig : {
			// layout-specific configs go here
			animate : true
			// activeOnTop: true
		},
		items : menuInit()
			// 调用menu.js中的方法：menuInit()初始化主菜单
		});
}
