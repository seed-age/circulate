<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>登录</title>

 <%@include file="/include/header.jsp" %>
<link href="<c:url value="/resources/manage/css/login.css"/>" rel="stylesheet" type="text/css"/>

<script src="<c:url value="/script/jquery/jquery.js"/>">
</script>
<script src="<c:url value="/script/jsencrypt.min.js"/>"></script>
<script type="text/javascript">
	function login(){
		var publicKey = "${publicKey}";
		var enc = new JSEncrypt();
	    enc.setPublicKey(publicKey);
	    var username = $('#username').val();
	    var password = $('#password').val();
	    var encUsername = enc.encrypt(username);
	    var encPassword = enc.encrypt(password);
	    $("#j_username").val(encUsername);
	    $("#j_password").val(encPassword);
	    $("form").submit();
	}
 
</script>
<script type="text/javascript">
	
	$(function(){
		
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
		});
		
		$("#sub").click(function(){
			login();
		});
		$("#username").keydown(function(e){
			if(e.keyCode==13){
				$("#password").focus(function(){
					this.select();
				}).focus();
				return false;
			}
		});
		$(document).keydown(function(e){
			if(e.keyCode==13){
				login();
			}
		});
		$("#username").focus();
	});
</script>

</head>

<body>
	<form id="form"  action="<c:url value="/system/login-process"/>" method="post">
	<input id="j_username" name="j_username" type="hidden" />
	<input id="j_password" name="j_password" type="hidden" />
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	<div class="sjloginbox">
		<div class="cont1"><h2>系统登录</h2></div>
			
			<div class="cont2">
		        <div class="a1"><input id="username" type="text" class="intxt" /></div>
		        <div  class="a2"><input id="password" type="password" class="intxt"/></div>
		        <div class="a3"><input id="validateCode"  name="validateCode" type="text" class="intxtv"/><img id="vcode" align="middle" src="<c:url value="/vcode.jpg"/>" alt="验证码" /><a href="#!" style="padding-left:20px " onclick="$('#vcode').attr('src','<c:url value="/vcode.jpg"/>?'+Math.random())">换一换</a></div>
		        <div class="btnx"><a id="sub"  href="#!" title="" class="btn_en"></a>
		        </div>
		    </div>
	</div>
	<div style="text-align:center; padding-left: 35px">
		<input id="remember_me" name="remember_me" type="checkbox" value="true"/>两个星期内自动登录
	</div>
	</form>
	<div align="center" style="color:red;padding-top: 20px;">
		<c:if test="${param.error == 1}">
			用户名密码不正确
		</c:if>
		<c:if test="${param.error == 2}">
			验证码不正确
		</c:if>
	</div>
</body>
</html>
