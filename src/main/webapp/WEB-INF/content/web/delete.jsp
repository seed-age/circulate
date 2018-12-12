<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>已删除</title>
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
    <link rel="stylesheet" href="/resources/web/css/read.css">
    <style>
        .oa-read-table .layui-table tbody tr td:first-child{
            display: none;
        }
    </style>

</head>
<body id="mainBody" class="layui-layout layui-layout-admin" scroll="no" style="overflow-y: hidden;">


<div id="contentcontainer" class="clearfix main">

    <div id="content" class="layui-body side-bar-right">
        <div id="e8rightContentDiv" >
            <div class="content-body">
                <h1 class="title">已删除</h1>
                <div class="read">
                    <div class="layui-form oa-read-form" action="">
                        <!-- <select name="" lay-verify="required">
                            <option value="0">流程状态</option>
                            <option value="1">传阅中</option>
                            <option value="2">待办传阅</option>
                            <option value="3">已完成</option>
                        </select>
                        <select name="" lay-verify="required">
                            <option value="0">传阅筛选</option>
                            <option value="1">我的关注</option>
                            <option value="2">已读</option>
                            <option value="3">未读</option>
                        </select> -->
                        <div class="oa-form-input oa-form-search">
                            <input type="text" class="form-control" id="search" placeholder="请输入关键字搜索">
                            <div class="input-addon" id="search_btn">查询</div>
                        </div>
                        <div class="oa-form-input">
                            <input type="text" class="layui-input" id="calendar" placeholder="删除时间">
                        </div>
                    </div>
                    <div class="oa-read-table layui-form">
                        <input type="hidden" name="currPage">
                        <table class="layui-table" lay-skin="nob" lay-filter="oa-read-table-data">
                            <colgroup>
                                <!-- <col width="50"> -->
                                <col width="80">
                                <col>
                                <col width="120">
                                <col>
                                <col width="150">
                                <col width="50">
                            </colgroup>
                            <thead>
                            <tr>
                                <th style="display:none;"><input type="checkbox" name="all-checked" lay-skin="primary" lay-filter="all-checked"></th>
                                <th>
                                    <img src="/resources/web/images/main-icon9.png" style="margin:0 3px;" alt="">
                                    <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">
                                </th>
                                <th>传阅主题</th>
                                <th>发起人</th>
                                <th>接收人</th>
                                <th>删除时间</th>
                                <th style="text-align:right;">关注</th>
                            </tr>
                            </thead>
                            <tbody>
                            <!-- <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/main-icon9.png" style="margin:0 3px;" alt="">
                                    <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star active"></span></td>
                            </tr>-->
                            </tbody>
                            <tfoot>
                            <tr style="text-align: right;">
                                <td colspan="6">
                                    <div class="item-box layui-form oa-read-form">
                                        每页
                                        <select name="item-num" lay-filter="item-num">
                                            <option value="10">10</option>
                                            <option value="20">20</option>
                                            <option value="50">50</option>
                                            <option value="100">100</option>
                                        </select>
                                    </div>
                                    <div id="paging"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- </div> -->

</html>
<script>
    //一般直接写在一个js文件中

    //JavaScript代码区域
    layui.use(['element','form','table','jquery','laydate'], function(){
        var element = layui.element;
        var table = layui.table;
        var form = layui.form;
        var $ = layui.jquery;
        var laydate = layui.laydate;
        var startTime = '';
        var endTime = '';
        itemNum = $('select[name="item-num"]').val();
        // 打开页面数据加载
        var pages = new Pages({
            type:'post',
            url:'/web/alreadydelete/find-like-delete.htm',
            data:{
                userId:$.session.get('userId'),
                page:1,
                startTime:startTime,
                endTime:endTime,
                pageRows:itemNum,
                likeName:$('#search').val()
            },
            templateTag:'.oa-read-table .layui-table tbody'
        });
        pages.getData();

        // 查询数据加载
        $('#search_btn').on('click',function(){
            var pages = new Pages({
                type:'post',
                url:'/web/alreadydelete/find-like-delete.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    startTime:startTime,
                    endTime:endTime,
                    pageRows:itemNum,
                    likeName:$('#search').val()
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();
        });
        // 查询数据加载
        $('#search').on('keydown',function(e){
            if(e.keyCode === 13){
                var pages = new Pages({
                    type:'post',
                    url:'/web/alreadydelete/find-like-delete.htm',
                    data:{
                        userId:$.session.get('userId'),
                        page:1,
                        startTime:startTime,
                        endTime:endTime,
                        pageRows:itemNum,
                        likeName:$('#search').val()
                    },
                    templateTag:'.oa-read-table .layui-table tbody'
                });
                pages.getData();
            }
        });
        // 每页显示几条
        form.on('select(item-num)', function(data){
            itemNum = $('select[name="item-num"]').val();
            var pages = new Pages({
                type:'post',
                url:'/web/alreadydelete/find-like-delete.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    startTime:startTime,
                    endTime:endTime,
                    pageRows:itemNum,
                    likeName:$('#search').val()
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();
        });

        laydate.render({
            elem: '#calendar', //指定元素
            type:'date',
            range:'到',
            theme:'#008cd6',
            done: function(value, date, endDate){
                if(date.year === undefined || endDate.year === undefined){
                    startTime = '';
                    endTime = '';
                }else{
                    startTime = date.year+'-'+date.month+'-'+date.date;
                    endTime = endDate.year+'-'+endDate.month+'-'+endDate.date;
                };
                var pages = new Pages({
                    type:'post',
                    url:'/web/alreadydelete/find-like-delete.htm',
                    data:{
                        userId:$.session.get('userId'),
                        page:1,
                        startTime:startTime,
                        endTime:endTime,
                        pageRows:itemNum,
                        likeName:$('#search').val()
                    },
                    templateTag:'.oa-read-table .layui-table tbody'
                });
                pages.getData();
            }


        });
        $(document).on('click','.oa-read-table .layui-table tbody tr td',function(){
            var value = $(this).parent('tr').children().children('input[name=choice]').val();
            if(value === undefined || $(this).index() === 0 || $(this).index() === $(this).parent('tr').children().length-1){
                return  false;
            };
            // var value = $(this).children().children('input[name=choice]').val();
            // if(value === undefined){
            //     return  false;
            // };
            $.session.set('article',value);
            $.session.set('mailStatus',2);
            window.location.href = 'article.htm';
        });
        $.session.set('attentionStatus',2);
    });



</script>