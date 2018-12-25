<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>已发传阅</title>
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
    <script src="/resources/web/js/send.js"></script>


</head>
<body id="mainBody" class="layui-layout layui-layout-admin" scroll="no" style="overflow-y: hidden;">


<div id="contentcontainer" class="clearfix main">

    <div id="content" class="layui-body side-bar-right">
        <div id="e8rightContentDiv" >
            <div class="content-body">
                <h1 class="title">已发传阅</h1>
                <div class="read">
                    <div class="layui-form oa-read-form" action="">
                        <div class="oa-form-input">
                            <select name="mailStatus" lay-filter="mailStatus">
                                <option value="0">全部</option>
                                <option value="1">传阅中</option>
                                <option value="3">已完成</option>
                            </select>
                        </div>
                        <!-- <select name="" lay-verify="required">
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
                            <input type="text" class="layui-input" id="calendar" placeholder="请选择发起时间">
                        </div>
                        <button type="button" class="layui-btn oa-read-delete layui-btn-disabled layui-btn-action" disabled>删除</button>
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
                                <col width="120">
                                <col width="150">
                                <col width="50">
                            </colgroup>
                            <thead>
                            <tr>
                                <th><input type="checkbox" name="all-checked" lay-skin="primary" lay-filter="all-checked"></th>
                                <th>
                                    <img src="/resources/web/images/main-icon9-cur.png" style="margin:0 3px;" alt="">
                                    <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">
                                </th>
                                <th>传阅主题</th>
                                <th>发起人</th>
                                <th>接收人</th>
                                <th>状态</th>
                                <th>发起时间</th>
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
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/main-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr>
                            <tr>
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
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/main-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr>
                            <tr>
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
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/main-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr>
                            <tr>
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
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/main-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/accessory-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr>
                            <tr>
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
                            </tr>
                            <tr>
                                <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                <td>
                                    <img src="/resources/web/images/main-icon9.png" style="margin:0 3px;" alt="">
                                </td>
                                <td>OA迭代升级时长调研专题研讨会等你来</td>
                                <td>张珊</td>
                                <td>张飞 李经理 郝敏</td>
                                <td>12月15日</td>
                                <td style="text-align:right;"><span class="star"></span></td>
                            </tr> -->

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="8" style="text-align: right;" class="page-layout">
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


