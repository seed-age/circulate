	function deskTop(tabs){
		Ext.Ajax.request({
			url:'task/desk-top-items.htm',
			success:function(xhr){
				var content = Ext.decode(xhr.responseText);
				//alert(content);
				if(content.length > 0 ){
					tabs.add({
			            title: '工作台',
			            itemId:'work-deskTab',
			            layout: 'column',
					    defaults: {
					    	columnWidth:.33,
							border:false,
							defaults:{
								layout:'fit',
								collapsible:true,
								border:false,
								style:'margin-bottom:10px',
								listeners:{
									render:function(){
										var panel = this;
										//alert(panel.loadPanel);
										Ext.Ajax.request({
											url:panel.loadPanel,
											success:function(xhr){
												//alert(xhr.responseText);
												panel.add(eval(xhr.responseText));
												panel.doLayout();
											}
										});
									}
								}
							},
					        bodyStyle:'padding:10px 10px'
					    },
			            iconCls: 'icon-folder-user',
			            closable:false,
			            items:content,
			            listeners:{
			            	'activate':function(p){
			            		Ext.each(p.findByType('grid',false),function(){
			            			if(this.store){
			            				this.store.load(this.store.lastOptions);
			            			}	
			            		});
			            	}
			            }
			        }).show();
				    tabs.activate(0);
				}
			    //alert(Ext.getCmp("tabs").getItem("work-desk"));
			}
		});
	    
    }
    
