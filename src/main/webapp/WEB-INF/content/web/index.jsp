<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>传阅一览</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 关键字使用","分隔 -->
    <meta name="keywords" content="a,b,c">
    <!-- 禁止浏览器从本地机的缓存中调阅页面内容 -->
    <meta http-equiv="Pragma" content="no-cache">
    <!-- 用来防止别人在框架里调用你的页面 -->
    <meta http-equiv="Window-target" content="_top">
    <!-- content的参数有all，none，index，noindex，follow，nofollow，默认是all -->
    <meta name="robots" content="none">
    <!-- 收藏图标 -->
    <link rel="Shortcut Icon" href="favicon.ico">
    <!-- 网页不会被缓存 -->
    <link rel="stylesheet" href="/resources/web/layui/css/layui.css">
    <link rel="stylesheet" href="/resources/web/css/common.css">
    <script src="/resources/web/js/jquery.min.js"></script>
    <script src="/resources/web/js/jquerysession.js"></script>
    <script src="/resources/web/layui/layui.js"></script>
    <script src="/resources/web/js/base.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="/resources/web/js/html5.min.js"></script>
    <script src="/resources/web/js/respond.min.js"></script>
    <![endif]-->
    <!-- ***************以上是公共部分************ -->
    <link rel="stylesheet" href="/resources/web/css/index.css">
    <script src="/resources/web/js/index.js"></script>




</head>
<body id="mainBody" class="layui-layout layui-layout-admin" scroll="no" style="overflow-y: hidden;">


<div id="contentcontainer" class="clearfix main">
    
    <div id="content" class="layui-body side-bar-right">
        <div id="e8rightContentDiv">
            <div class="content-body">
                <h1 class="title">传阅一览</h1>
                <div class="layui-row layui-col-space30 sloa-list">
                    <!-- 收到传阅
                    <div class="layui-col-sm4 sloa-box">
                        <div class="sub-title">
                            <h2>收到传阅</h2>
                            <img src="/resources/web/images/question-icon.png"/>
                        </div>
                        <div class="content-box">
                            <div class="layui-row">
                                <div class="layui-col-sm6 content-item">
                                    <p class="txt">未读传阅</p>
                                    <h3>8</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                                <div class="layui-col-sm6 content-item">
                                    <p class="txt">待办传阅</p>
                                    <h3>10</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    已发传阅
                    <div class="layui-col-sm4 sloa-box">
                        <div class="sub-title">
                            <h2>已发传阅</h2>
                            <img src="/resources/web/images/question-icon.png"/>
                        </div>
                        <div class="content-box">
                            <div class="layui-row">
                                <div class="layui-col-sm6 content-item">
                                    <p class="txt">传阅中</p>
                                    <h3>8</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                                <div class="layui-col-sm6 content-item">
                                    <p class="txt">已完成</p>
                                    <h3>10</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    已删除
                    <div class="layui-col-sm4 sloa-box">
                        <div class="sub-title">
                            <h2>已删除</h2>
                            <img src="/resources/web/images/question-icon.png"/>
                        </div>
                        <div class="content-box">
                            <div class="layui-row">
                                <div class="layui-col-sm12 content-item">
                                    <p class="txt">已删除</p>
                                    <h3>8</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                            </div>
                        </div>
                    </div>
                                    </div>
                    
                                    <div class="layui-row layui-col-space30">
                    传阅完成
                    <div class="layui-col-sm4 sloa-box">
                        <div class="sub-title">
                            <h2>传阅完成</h2>
                            <img src="/resources/web/images/question-icon.png"/>
                        </div>
                        <div class="content-box">
                            <div class="layui-row">
                                <div class="layui-col-sm6 content-item">
                                    <p class="txt">发送传阅</p>
                                    <h3>8</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                                <div class="layui-col-sm6 content-item">
                                    <p class="txt">收到传阅</p>
                                    <h3>10</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    传阅中
                    <div class="layui-col-sm4 sloa-box">
                        <div class="sub-title">
                            <h2>传阅中</h2>
                            <img src="/resources/web/images/question-icon.png"/>
                        </div>
                        <div class="content-box">
                            <div class="layui-row">
                                <div class="layui-col-sm6 content-item">
                                    <p class="txt">发送传阅</p>
                                    <h3>8</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                                <div class="layui-col-sm6 content-item">
                                    <p class="txt">收到传阅</p>
                                    <h3>10</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    待发传阅
                    <div class="layui-col-sm4 sloa-box">
                        <div class="sub-title">
                            <h2>待发传阅</h2>
                            <img src="/resources/web/images/question-icon.png"/>
                        </div>
                        <div class="content-box">
                            <div class="layui-row">
                                <div class="layui-col-sm12 content-item">
                                    <p class="txt">待发传阅</p>
                                    <h3>8</h3>
                                    <a class="watch" href="">查看</a>
                                </div>
                            </div>
                        </div>
                    </div> -->
                </div>
            </div>
        </div>
    </div>
</div>
<!-- </div> -->

</html>
<script>
    //一般直接写在一个js文件中
    
</script> 
<script>
// {
//     ceceiveCompleteCount  传阅完成,收到传阅
//     completeCount   已发传阅,已完成
//     deleteCount   已删除
//     receiveInCount   传阅中:,收到传阅
//     sendInCount   已发传阅: 传阅中
//     todoCount   收到传阅,待办
//     unreadCount  收到传阅,为读
//     waitSendCount    待发传阅
// }
// var solaData = [
//     {
//         title:"收到传阅",
//         list:[
//             unreadCount, //未读
//             todoCount //待办
//         ]
//     },
//     {
//         title:"已发传阅",
//         list:[
//             sendInCount //传阅中
//             completeCount, //已完成
//         ]
        
//     },
//     {
//         title:"已删除",
//         list:[
//             deleteCount //已删除
//         ]
        
//     },
//     {
//         title:"传阅完成",
//         list:[
//             completeCount, //发送传阅
//             ceceiveCompleteCount, //收到传阅
//         ]
//     },
//     {
//         title:"传阅中",
//         list:[
//             sendInCount, //发送传阅
//             receiveInCount, //收到传阅
//         ]
//     },
//     {
//         title:"待发传阅",
//         list:[
//             waitSendCount //待发传阅
//         ]
//     }
// ]

</script>