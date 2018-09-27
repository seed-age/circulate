/**
 * 本文件存放ext公共方法 
 */

//通用日期模式
Ext.Date.patterns = {
	ISO8601Long : "Y-m-d H:i:s",
	ISO8601Short : "Y-m-d",
	ShortDate : "n/j/Y",
	LongDate : "l, F d, Y",
	FullDateTime : "l, F d, Y g:i:s A",
	MonthDay : "F d",
	ShortTime : "g:i A",
	LongTime : "g:i:s A",
	H24ShortTime : "H:i",
	H24LongTime : "H:i:s",
	SortableDateTime : "Y-m-d\\TH:i:s",
	UniversalSortableDateTime : "Y-m-d H:i:sO",
	YearMonth : "F, Y",
	WeekDay : "l"
};
//cookie记录组件状态
Ext.state.Manager.setProvider( Ext.create('Ext.state.CookieProvider', {
	expires : new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 7))
		// 7 days from now
	}));

// 判断是否登录
Ext.Ajax.on('requestcomplete', function(conn, xhr, options) {
	if (xhr.getResponseHeader){
		if(xhr.getResponseHeader("ext-logout") == "true") {
			window.location.reload();
		}
		if(xhr.getResponseHeader("ext-login") == "true") {
			options.failure = Ext.emptyFn ;//取消后续回调事件触发
			if(xhr.getResponseHeader("ext-error") == "1"){
				options.success = function(xhr){
					alert("用户名密码不正确");
					return;
				}
			}else if(xhr.getResponseHeader("ext-error") == "2"){
				options.success = function(xhr){
					alert("验证码不正确");
					return;
				}
			}else{
			   options.success = Ext.emptyFn ;//取消后续回调事件触发
			}
			var tabs = Ext.getCmp("mainTabs");
			if(tabs)tabs.getEl().unmask();
			showLoginWin();
		}
		var _csrf_token = xhr.getResponseHeader("X-CSRF-TOKEN");
		if( _csrf_token){
			Ext.Ajax.defaultHeaders = { "X-CSRF-TOKEN" : _csrf_token };
		}
	}
});

function commonAjaxFail(){
	Ext.Msg.alert("失败", "网络通信故障,加载失败");
}

// Ext.Ajax.on('requestexception', this.hideSpinner, this);
// var arr = ['a','b',false];
// alert(Ext.flatten(arr));
// alert(arr);
/**
 * 日期格式化
 */
function renderDateISOLong(value) {
	return value && Ext.isDate(value)
			? Ext.Date.format(value,Ext.Date.patterns.ISO8601Long)
			: "";
}

function renderDateISOShort(value) {
	return value && Ext.isDate(value) ? Ext.Date.format(value,Ext.Date.patterns.ISO8601Short) : "";
}

function renderShortTime(value) {
	return value && Ext.isDate(value)
			? Ext.Date.format(value,Ext.Date.patterns.ShortTime)
			: "";
}
function renderLongTime(value) {
	return value && Ext.isDate(value)
			? Ext.Date.format(value,Ext.Date.patterns.LongTime)
			: "";
}
function renderH24LongTime(value) {
	return value && Ext.isDate(value)
			? Ext.Date.format(value,Ext.Date.patterns.H24LongTime)
			: "";
}
function renderWeekDay(value) {
	return value && Ext.isDate(value)
			? Ext.Date.format(value,Ext.Date.patterns.WeekDay)
			: "";
}

// form validation，增加daterange,email,密码等特殊验证
Ext.apply(Ext.form.field.VTypes, {
		daterange : function(val, field) {
            var date = field.parseDate(val);
            if (!date) {
                return false;
            }
            if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
                var start = field.up('form').down('#' + field.startDateField);
                start.setMaxValue(date);
                start.validate();
                this.dateRangeMax = date;
            }
            else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
                var end = field.up('form').down('#' + field.endDateField);
                end.setMinValue(date);
                end.validate();
                this.dateRangeMin = date;
            }
            return true;
		},
		daterangeText: '开始时间必须比结束时间早',
		password : function(val, field) {
		    if (field.initialPassField) {
		        var pwd = field.up('form').down('#' + field.initialPassField);
		        return (val == pwd.getValue());
		    }
		    return true;
		},
		
		passwordText : '确认密码不匹配'

//extjs 4.1内置email验证
//			email : function(v) {
//				var email = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/;
//				return email.test(v) || v == "";
//			}
		});
/**
 * 通用提交表单的配置，根据closeId和reloadId确定删除和刷新的组件
 */
var commonSubmitConfig = {
	clientValidation : true,
	success : function(form, action) {
		if (action.result) {
			if (action.result.msg) {
				Ext.Msg.alert('成功', action.result.msg);
			}
		}
	},
	failure : function(form, action) {
		switch (action.failureType) {
			case Ext.form.action.Action.CLIENT_INVALID :
				// Ext.Msg.alert('Failure', 'Form fields may not be submitted
				// with invalid values');
				break;
			case Ext.form.action.Action.CONNECT_FAILURE :
				Ext.MessageBox.alert('系统错误', '网络连接异常');
				break;
			case Ext.form.action.Action.SERVER_INVALID :
				Ext.MessageBox.alert('错误', action.result.msg);
		}
	}
};

var commonLoadFormConfig = {
	failure : function(form, action) {
		switch (action.failureType) {
			case Ext.form.action.Action.CLIENT_INVALID :
				// Ext.Msg.alert('Failure', 'Form fields may not be submitted
				// with invalid values');
				break;
			case Ext.form.action.Action.CONNECT_FAILURE :
				Ext.MessageBox.alert('错误', '网络连接异常');
				break;
			case Ext.form.action.Action.SERVER_INVALID :
				Ext.MessageBox.alert('错误', action.result.errorMessage);
				break;
			case Ext.form.action.Action.LOAD_FAILURE :
				Ext.MessageBox.alert('错误', action.result.msg);
		}
		// var tabs = Ext.getCmp("tabs");

		// var panel = new Ext.Panel({
		// html:action.result
		// })
	}
};
//自动刷新视图的方法
var commonReloadTask = null;
var stopCommonReloadTask = function() {
	if (commonReloadTask) {
		Ext.TaskManager.stop(commonReloadTask);
	}
}
// 添加的tab的方法
function addLazyLoadWorkSpaceTab(tabId, tabTitle, loadUrl, layoutConfig,
		params, icon ,iconCls) {
	var tabs = Ext.getCmp("mainTabs");
	var tab = tabs.getComponent(tabId);
	if (tab) {// 如果已经添加，仅仅激活它
		tabs.setActiveTab(tab);
	} else {
		tabs.getEl().mask("请稍等...");
		var ajaxConfig = {
			url : loadUrl,
			success : function(xhr) {
			    try{
					var newComponent = eval(xhr.responseText);
					var config = {
						itemId : tabId,
						title : tabTitle,
						closable : true
					};
					if (icon){
						Ext.apply(config, {
							icon : icon
						});
					}else if(iconCls){
						Ext.apply(config, {
							iconCls : iconCls
						});
					}
					if (layoutConfig){
						Ext.apply(config, layoutConfig);
					}
					var panel = Ext.widget("panel",config);
					panel.add(newComponent);
					tabs.add(panel);
					tabs.setActiveTab(panel);
					//panel.doLayout();
					//panel.show();
					tabs.getEl().unmask();
				}catch(e){
				   tabs.getEl().unmask();
				   tabs.remove(panel);
				   if(debugMode){
				   		alert(e);
				   }
				}
			},
			failure : commonAjaxFail
		};
		if (params) {
			Ext.apply(ajaxConfig, params);
		}
		Ext.Ajax.request(ajaxConfig);
	}
}
var loginWindow = null;
//显示重登陆框。
function showLoginWin() {
	loginWindow.show(Ext.getBody());
}

//设置数据字典
function setDictionary(keyName,keyValue,callback){
	Ext.Ajax.request({
		url : "dictionary-config.htm",
		params :{
			"keyName":keyName,
			"keyValue":keyValue
		},
		success : callback
	});
}






