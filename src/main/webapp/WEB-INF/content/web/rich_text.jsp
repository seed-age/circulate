<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>待发传阅</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 关键字使用","分隔 -->
    <meta name="keywords" content="a,b,c">
    <!-- 禁止浏览器从本地机的缓存中调阅页面内容 -->
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
    <meta http-equiv="expires" content="0">
    <!-- 用来防止别人在框架里调用你的页面 -->
    <meta http-equiv="Window-target" content="_top">
    <!-- content的参数有all，none，index，noindex，follow，nofollow，默认是all -->
    <meta name="robots" content="none">
    <!-- 收藏图标 -->
    <link rel="Shortcut Icon" href="favicon.ico">
    <!-- 网页不会被缓存 -->
    <!-- 使用el表达式获取当前时间 -->
    <% long date = new Date().getTime(); request.setAttribute("date", date); %>
    <link rel="stylesheet" href="/resources/web/layui/css/layui.css?${date}">
    <link rel="stylesheet" href="/resources/web/css/common.css?${date}">
    <script src="/resources/web/js/jquery.min.js?${date}"></script>
    <script src="/resources/web/js/jquerysession.js?${date}"></script>
    <script src="/resources/web/layui/layui.js?${date}"></script>
    <script src="/resources/web/js/base.js?${date}"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="/resources/web/js/html5.min.js?${date}"></script>
    <script src="/resources/web/js/respond.min.js?${date}"></script>
    <![endif]-->
    <!-- ***************以上是公共部分************ -->
    <link rel="stylesheet" type="text/css" href="/resources/web/site/assets/styles/simditor.css?${date}" />
</head>
<body>
<div class="editor-style">

</div>
<script>
    layui.use(['jquery','layer'], function(){
        var layer = layui.layer;
        var userId = Number(getQueryString('userId'));
        var mailId = Number(getQueryString('mailId'));
        $.ajax({
            type:'post',
            url:'/web/received/find-mail-content.htm',
            data:{
                userId:userId,
                mailId:mailId
            },
            dataType:'json',
            success:function(res){
                if(res.code == 200){
                    $('.editor-style').html(res.data.mailContent)
                }else{
                    layer.msg(res.msg,{time: 2000,icon: 2});
                }
            },
            error:function(error){
                layer.msg(error.statusText+'：'+error.status,{time: 2000,icon: 2});
            }
        });
    });
</script>
</body>
</html>

