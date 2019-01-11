<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>待发传阅</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-touch-fullscreen" content="yes">
    <meta name="format-detection" content="telephone=no, email=no">
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
    <style>
        html {
            /*font-family:"Helvetica Neue",Helvetica,STHeiTi,Arial,sans-serif;*/
            -ms-text-size-adjust:100%;
            -webkit-text-size-adjust:100%;
            -webkit-user-select: auto;
            -khtml-user-select: auto;
            -moz-user-select: auto;
            -ms-user-select: auto;
            user-select: auto;
            /*font-size:62.5%;*/
        }
        body {
            margin:0;
            /*font-size:1.4rem;*/
            /*line-height:1.5;*/
            color:#333333;
            background-color:white;
            height:100%;
            overflow-x:hidden;
            -webkit-overflow-scrolling:touch;
        }
        article,aside,details,figcaption,figure,footer,header,hgroup,main,nav,section,summary {
            display:block;
        }
        audio,canvas,progress,video {
            display:inline-block;
            vertical-align:baseline;
        }
        audio:not([controls]) {
            display:none;
            height:0;
        }
        [hidden],template {
            display:none;
        }
        svg:not(:root) {
            overflow:hidden;
        }
        a {
            background:transparent;
            text-decoration:none;
            -webkit-tap-highlight-color:transparent;
        }
        a:active {
            outline:0;
        }
        abbr[title] {
            border-bottom:1px dotted;
        }
        b,strong {
            font-weight:bold;
        }
        dfn {
            font-style:italic;
        }
        mark {
            background:#ff0;
            color:#000;
        }
        small {
            font-size:80%;
        }
        sub,sup {
            font-size:75%;
            line-height:0;
            position:relative;
            vertical-align:baseline;
        }
        sup {
            top:-0.5em;
        }
        sub {
            bottom:-0.25em;
        }
        img {
            border:0;
            vertical-align:middle;
        }
        hr {
            -moz-box-sizing:content-box;
            box-sizing:content-box;
            height:0;
        }
        pre {
            overflow:auto;
            white-space:pre;
            white-space:pre-wrap;
            word-wrap:break-word;
        }
        code,kbd,pre,samp {
            font-family:monospace,monospace;
            font-size:1em;
        }
        button,input,optgroup,select,textarea {
            color:inherit;
            font:inherit;
            margin:0;
        }
        button {
            overflow:visible;
        }
        button,select {
            text-transform:none;
        }
        button,html input[type="button"],input[type="reset"],input[type="submit"] {
            -webkit-appearance:button;
            cursor:pointer;
        }
        button[disabled],html input[disabled] {
            cursor:default;
        }
        button::-moz-focus-inner,input::-moz-focus-inner {
            border:0;
            padding:0;
        }
        input {
            line-height:normal;
        }
        input[type="checkbox"],input[type="radio"] {
            box-sizing:border-box;
            padding:0;
        }
        input[type="number"]::-webkit-inner-spin-button,input[type="number"]::-webkit-outer-spin-button {
            height:auto;
        }
        input[type="search"] {
            -webkit-appearance:textfield;
            -moz-box-sizing:border-box;
            -webkit-box-sizing:border-box;
            box-sizing:border-box;
        }
        input[type="search"]::-webkit-search-cancel-button,input[type="search"]::-webkit-search-decoration {
            -webkit-appearance:none;
        }
        fieldset {
            border:1px solid #c0c0c0;
            margin:0 2px;
            padding:0.35em 0.625em 0.75em;
        }
        legend {
            border:0;
            padding:0;
        }
        textarea {
            overflow:auto;
            resize:vertical;
        }
        optgroup {
            font-weight:bold;
        }
        table {
            border-collapse:collapse;
            border-spacing:0;
        }
        td,th {
            padding:0;
        }
        html,button,input,select,textarea {
            font-family:"Helvetica Neue",Helvetica,STHeiTi,Arial,sans-serif;
        }
        h1,h2,h3,h4,h5,h6,p,figure,form,blockquote {
            margin:0;
        }
        ul,ol,li,dl,dd {
            margin:0;
            padding:0;
        }
        ul,ol {
            list-style:none outside none;
        }
        h1,h2,h3 {
            line-height:2;
            font-weight:normal;
        }
        h1 {
            font-size:1.8rem;
        }
        h2 {
            font-size:1.6rem;
        }
        h3 {
            font-size:1.4rem;
        }
        input::-moz-placeholder,textarea::-moz-placeholder {
            color:#cccccc;
        }
        input:-ms-input-placeholder,textarea:-ms-input-placeholder {
            color:#cccccc;
        }
        input::-webkit-input-placeholder,textarea::-webkit-input-placeholder {
            color:#cccccc;
        }
        * {
            -webkit-box-sizing:border-box;
            -moz-box-sizing:border-box;
            box-sizing:border-box;
        }
    </style>
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
            url:'/app/received/find-mail-content.htm',
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

