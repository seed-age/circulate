<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>收到传阅</title>
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


</head>
<body id="mainBody" class="layui-layout layui-layout-admin" scroll="no" style="overflow-y: hidden;">


<div id="contentcontainer" class="clearfix main">

    <div id="content" class="layui-body side-bar-right">
        <div id="e8rightContentDiv" >
            <div class="content-body">
                <h1 class="title">收到传阅</h1>
                <div class="read">
                    <div class="layui-form oa-read-form" action="">
                        <div class="oa-form-input">
                            <select name="process" lay-filter="process">
                                <option value="0">全部</option>
                                <option value="1">传阅中</option>
                                <option value="2">待办传阅</option>
                                <option value="3">已完成</option>
                            </select>
                        </div>
                        <div class="oa-form-input">
                            <select name="filtrate" lay-filter="filtrate">
                                <option value="0">传阅筛选</option>
                                <option value="5">未读</option>
                                <option value="6">已读</option>
                                <option value="7">我的关注</option>
                            </select>
                        </div>
                        <div class="oa-form-input oa-form-search">
                            <input type="text" class="form-control" id="search" placeholder="请输入...">
                            <div class="input-addon" id="search_btn">查询</div>
                        </div>
                        <div class="oa-form-input">
                            <input type="text" class="layui-input" id="calendar" placeholder="请选择时间">
                        </div>
                        <button type="button" class="layui-btn layui-btn-primary oa-read-skip">跳过</button>
                    </div>
                    <div class="oa-read-table layui-form">
                        <input type="hidden" name="currPage">
                        <table class="layui-table" lay-skin="nob" lay-filter="oa-read-table-data">
                            <colgroup>
                                <col width="50">
                                <col width="80">
                                <col>
                                <col width="120">
                                <col>
                                <col width="150">
                                <col width="50">
                            </colgroup>
                            <thead>
                            <tr>
                                <th><input type="checkbox" name="all-checked" lay-skin="primary" lay-filter="all-checked"></th>
                                <th>
                                    <img src="/resources/web/images/main-icon9.png" style="margin:0 3px;" alt="">
                                    <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">
                                </th>
                                <th>传阅主题</th>
                                <th>发起人</th>
                                <th>接收人</th>
                                <th>时间</th>
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
                            <tr>
                                <td colspan="7" style="text-align: right;">
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
        // 页面加载数据
        var receiveStatus,
            status
        startTime='',
            endTime='';
        var itemNum = $('select[name="item-num"]').val();
        var getState = Number(getQueryString('state'));
        // switch(getQueryString('state')){
        //     case '1':
        //         receiveStatus = 1;
        //         $('select[name="process"]').val(1);
        //         break;
        //     case '2':
        //         receiveStatus = 2;
        //         $('select[name="process"]').val(2);
        //         break;
        //     case '3':
        //         receiveStatus = 3;
        //         $('select[name="process"]').val(3);
        //         break;
        //     case '4':
        //         receiveStatus = 4;
        //         $('select[name="filtrate"]').val(4);
        //         break;
        //     default:
        //         if($('select[name="process"]').val()!=='0'){
        //             receiveStatus = $('select[name="process"]').val();
        //         }else if($('select[name="filtrate"]').val() !== '0'){
        //             receiveStatus = $('select[name="filtrate"]').val();
        //         }else{
        //             receiveStatus = 0;
        //         }
        //         break;
        // };
        if(getState<=3){
            $('select[name="process"]').val(getState);
            $('select[name="filtrate"]').val(0);
            receiveStatus = getState;
            status = 0;

        }else{
            $('select[name="process"]').val(0);
            $('select[name="filtrate"]').val(getState)
            receiveStatus = 0;
            status = getState;
        }
        var pages = new Pages({
            type:'post',
            url:'/web/received/grid-unread-state-list.htm',
            data:{
                userId:$.session.get('userId'),
                page:1,
                receiveStatus:receiveStatus,
                status:status,
                startTime:startTime,
                endTime:endTime,
                pageRows:itemNum,
                likeName:$('#search').val()
            },
            templateTag:'.oa-read-table .layui-table tbody'
        });
        pages.getData();
        // 流程状态数据加载
        form.on('select(process)', function(data){
            // $('select[name="filtrate"]').val(0);
            receiveStatus = data.value;
            pages = new Pages({
                type:'post',
                url:'/web/received/grid-unread-state-list.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    receiveStatus:receiveStatus,
                    status:status,
                    startTime:startTime,
                    endTime:endTime,
                    pageRows:itemNum,
                    likeName:$('#search').val()
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();
        });
        form.on('select(filtrate)', function(data){
            // $('select[name="process"]').val(0);
            // $('#search').val('');
            status = data.value;
            pages = new Pages({
                type:'post',
                url:'/web/received/grid-unread-state-list.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    receiveStatus:receiveStatus,
                    status:status,
                    startTime:startTime,
                    endTime:endTime,
                    pageRows:itemNum,
                    likeName:$('#search').val()
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();
        });


        // 每页显示几条
        form.on('select(item-num)', function(data){
            itemNum = $('select[name="item-num"]').val();
            pages = new Pages({
                type:'post',
                url:'/web/received/grid-unread-state-list.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    likeName:$('#search').val(),
                    receiveStatus:receiveStatus,
                    status:status,
                    startTime:startTime,
                    endTime:endTime,
                    pageRows:itemNum
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();
        });
        // 模糊查询数据加载
        $('#search_btn').on('click',function(){
            pages = new Pages({
                type:'post',
                url:'/web/received/grid-unread-state-list.htm',
                data:{
                    userId:$.session.get('userId'),
                    page:1,
                    likeName:$('#search').val(),
                    receiveStatus:receiveStatus,
                    status:status,
                    startTime:startTime,
                    endTime:endTime,
                    pageRows:itemNum
                },
                templateTag:'.oa-read-table .layui-table tbody'
            });
            pages.getData();
            // $('select[name="process"]').val(0);
            // $('select[name="filtrate"]').val(0);
        });
        // 跳过
        $('.oa-read-skip').on('click',function(){
            var choice = $('.oa-read-table tbody input[name="choice"]:checked');
            var choiceArr = [];
            var layer_del;
            for(var i=0;i<choice.length;i++){
                choiceArr.push(choice[i].value);
            };
            // 判断是否有选择的，如果没有不提交数据
            if(choiceArr.length>0){
                $.ajax({
                    type:'post',
                    url:'/web/received/batch-update-state.htm',
                    dataType:'json',
                    data:{
                        userId:$.session.get('userId'),
                        mailId:choiceArr
                    },
                    beforeSend:function(xhr){
                        layer_del = layer.msg('正在努力中', {
                            icon: 16,
                            shade: 0.01,
                            area:'40px',
                            time:0
                        });
                    },
                    success:function(res){
                        layer.close(layer_del)
                        var page = $('input[name="currPage"]').val();
                        var currPage = page === ''?1:page;
                        if(res.code === '200'){
                            pages.getData(parseInt(currPage));
                            setTimeout(function(){
                                layer.msg(res.msg,{time: 2000,icon: 1});
                            },200)
                        }else{
                            layer.msg('所勾选的传阅，已是已读传阅',{time: 2000,icon: 2});
                        }
                    },
                    error:function(){
                        layer.close(layer_del);
                        layer.msg('网络出错',{time: 1000,icon: 2});
                    }
                })
            }else{
                return false;
            }
        });
        //执行一个laydate实例
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
                }
                pages = new Pages({
                    type:'post',
                    url:'/web/received/grid-unread-state-list.htm',
                    data:{
                        userId:$.session.get('userId'),
                        page:1,
                        likeName:$('#search').val(),
                        startTime:startTime,
                        endTime:endTime,
                        receiveStatus:receiveStatus,
                        pageRows:itemNum
                    },
                    templateTag:'.oa-read-table .layui-table tbody'
                });
                pages.getData();
            }

        });
        // 点击tr跳转页面
        $(document).on('click','.oa-read-table .layui-table tbody tr td',function(){
            // var value = $(this).children().children('input[name=choice]').val();
            var value = $(this).parent('tr').children().children('input[name=choice]').val();
            if(value === undefined || $(this).index() === 0 || $(this).index() === $(this).parent('tr').children().length-1){
                return  false;
            };
            $.session.set('article',value);
            $.session.set('mailStatus',3);
            window.location.href = 'article.htm';
        });
        $.session.set('attentionStatus',3);
    });
</script>