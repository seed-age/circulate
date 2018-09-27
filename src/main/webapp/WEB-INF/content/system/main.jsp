<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
  <head>

    <title>管理系统界面</title>
	 <%@include file="/include/header.jsp" %>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<script type="text/javascript">
		var debugMode = ${debugMode};
	</script>
<%--主样式--%>
	<link rel="stylesheet" type="text/css" href="<c:url value="/script/ext4/resources/css/ext-all.css"/>" />
<%--覆盖样式--%>
	<link rel="stylesheet" type="text/css" href="<c:url value="/extjs/css-override/main.css"/>" />
<%--插件样式--%>
<%--图标样式--%>
	<link rel="stylesheet" href="<c:url value='/extjs/icons/css/icons.css'/>" type="text/css"></link>
<%-- 登录加密库--%>
	<script src="<c:url value="/script/jsencrypt.min.js"/>"></script>
<%--EXT主JS--%>
	<script type="text/javascript" src="<c:url value="/script/ext4/bootstrap.js"/>"></script>
<%--BUG-FIX JS --%>
<%--	<script type="text/javascript" src="<c:url value="/ext/bug-fix.js"/>">--%>
<%--	</script>--%>

<%--中文支持--%>
	<script type="text/javascript" src="<c:url value="/script/ext4/locale/ext-lang-zh_CN.js"/>">
	</script>
<%-- 系统初始化 配置JS--%>
	<script type="text/javascript" src="<c:url value="/extjs/init-config.js"/>">	</script>
	<%-- Extjs4 loader配置  --%>
	<script type="text/javascript">
	//空白图片
	Ext.BLANK_IMAGE_URL = Ext.isIE6 || Ext.isIE7 || Ext.isAir ? '<c:url value="/extjs/s.gif"/>' : 'data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
	Ext.Loader.setConfig({
		enabled: true,
		paths:{
			'Ext.ux':'${pageContext.request.contextPath}/script/ext4/ux'
		}
	});

	Ext.require([
	    'Ext.*',
	    'Ext.ux.TabCloseMenu' //注意 ux无法用*全部加载
	]);
	var _userName = '${userName}';
	var _publicKey = '${publicKey}';//登录加密公钥
	var _context = '${pageContext.request.contextPath}/';
	var _loginUrl = '${pageContext.request.contextPath}/system/login-process'
	var _apiHelpUrl = "http://extjs-doc-cn.github.io/ext4api/";
	
	Ext.Ajax.defaultHeaders = { '${_csrf.headerName}' : '${_csrf.token}' };
	
	</script>
<%-- 系统菜单JS--%>
	<script type="text/javascript" src="<c:url value="/extjs/menu.js"/>"></script>
<%--系统启动JS--%>
	<script type="text/javascript" src="<c:url value="/extjs/main.js"/>">
	</script>
<%--系统桌面配置JS 暂时用不到--%>
<%--	<script type="text/javascript" src="<c:url value="/extjs/desktop-config.js"/>"></script>--%>
	<style type="text/css">
		 #loading-page{
	        height:auto;
	        position:absolute;
	        left:49%;
	        top:40%;
	        padding:2px;
	        z-index:20001;
	    }
	</style>
	
  </head>

  <body>
  	<div id="loading-page">
  	<img alt="加载...." src="<c:url value="/extjs/icons/img/extanim32.gif"/>" />
  	</div>
  </body>
</html>
