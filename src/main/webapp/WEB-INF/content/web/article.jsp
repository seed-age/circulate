<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>传阅详情</title>
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
	<link rel="stylesheet" href="/resources/web/webupload/webuploader.css">
	<script src="/resources/web/webupload/webuploader.js"></script>
	<link rel="stylesheet" href="/resources/web/css/article.css">
	<script src="/resources/web/js/article.js"></script>
	<link rel="stylesheet" type="text/css" href="/resources/web/site/assets/styles/simditor.css" />
</head>
<body id="mainBody" class="layui-layout layui-layout-admin" scroll="no" style="overflow-y: hidden;">


<div id="contentcontainer" class="clearfix main">
	<div id="content" class="layui-body side-bar-right">
		<div id="e8rightContentDiv" >
			<div class="content-body">
				<h1 class="title">收到传阅</h1>
				<div class="oa-receive">
					<div class="oa-receive-right">
						<div class="dialog-group">
							<h3 class="interacts-bd">传阅讨论互动（0）</h3>
							<div class="interacts-record">
								<div class="comment-box">
									<a href="javascript:void(0);" class="more">加载更多</a>
									<ul class="interacts-list">
										<!-- <li>
                                            <div class="user layui-clear">
                                                <h4>张珊珊</h4>
                                                <span>2017-12-02 23:31:24</span>
                                            </div>
                                            <p>传阅增加了那些新的功能？</p>
                                        </li> -->
									</ul>
								</div>
							</div>
							<div class="oa-form-input">
								<input type="text" name="discussContent" class="form-control">
								<button type="button" class="input-addon">发送</button>
							</div>
						</div>
					</div>
					<div class="oa-receive-left">
						<ul class="oa-receive-list">
							<!-- <li>
                                <strong>传阅主题：</strong>
                                <p>传阅迭代需求调研</p>
                                <span class="star active"></span>
                            </li>
                            <li>
                                <strong>发 件 人：</strong>
                                <p>张三</p>
                            </li>
                            <li>
                                <strong>接 收 人：</strong>
                                <p>张三；李经理；李总；张珊珊</p>
                            </li>
                            <li>
                                <strong>传阅规则：</strong>
                                <p>允许上传附件；允许修订Word、Excel；绝密</p>
                            </li>
                            <li>
                                <strong>附      件：</strong>
                                <p>2</p>
                            </li>
                            <li>
                                <strong>传阅时间：</strong>
                                <p>2017-12-20 14:20:01</p>
                            </li>   -->
						</ul>
						<div class="oa-import-content editor-style">
							传阅迭代需求调研，请各各部门激励配合完成市场调研工作
						</div>
						<div class="oa-receive-table attachmentItems-tab">
							<div class="table-title layui-clear">
								<div class="title-left">附件（0个）</div>
								<div class="title-right">
									<span class="annex-up" id="annex-up">上传附件</span>
									<span class="annex-down">下载附件</span>
								</div>
							</div>
							<div class="table-box attachment-box">
								<table class="layui-table layui-form" lay-skin="nob">
									<colgroup>
										<col width="50">
										<col width="100">
										<col>
										<col width="100">
										<col width="120">
										<col width="110">
										<col width="140">
									</colgroup>
									<thead>
									<tr>
										<th><input type="checkbox" name="attachment-checked" lay-skin="primary" lay-filter="attachment-checked"></th>
										<th>附件状态</th>
										<th>附件名称</th>
										<th>姓名</th>
										<th>大小</th>
										<th>时间</th>
										<th style="text-align:right;">操作</th>
									</tr>
									</thead>
									<tbody>
									<!-- <tr>
                                        <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                        <td>新增</td>
                                        <td>OA需求调研表.xscl</td>
                                        <td>张珊</td>
                                        <td>16K</td>
                                        <td>2017-12-20</td>
                                        <td style="text-align:right;">
                                            <a class="tab-icon tab-search " href=""><img src="/resources/web/images/handle01.png" alt=""></a>
                                            <a class="tab-icon tab-redact " href=""><img src="/resources/web/images/handle02.png" alt=""></a>
                                            <a class="tab-icon tab-down" href=""><img src="/resources/web/images/handle03.png" alt=""></a>
                                            <a class="tab-icon tab-dele " href=""><img src="/resources/web/images/handle04.png" alt=""></a>
                                        </td>
                                    </tr> -->


									</tbody>
								</table>
							</div>
						</div>
						<div class="oa-receive-table object-tab">
							<input type="hidden" name="object-page">
							<input type="hidden" name="object-total">
							<div class="table-title layui-clear">
								<div class="title-left title-obj">传阅对象（0个）</div>
								<div class="title-right">
									<span class="annex-dele">批量删除</span>
									<span class="annex-add">新增人员</span>
								</div>
							</div>
							<div class="table-box layui-form">
								<input type="hidden" name="currPage">
								<div class="layui-table-header">
									<table cellspacing="0" cellpadding="0" border="0" class="layui-table" lay-skin="nob">
										<colgroup>
											<col width="50">
											<col width="100">
											<col>
											<col width="170">
											<col width="170">
											<col width="80">
											<col width="80">
										</colgroup>
										<thead>
										<tr>
											<th><input type="checkbox" name="all-object" lay-skin="primary" lay-filter="all-object"></th>
											<th>姓名</th>
											<th>确认/标志</th>
											<th>发送时间</th>
											<th>开封时间</th>
											<th>状态</th>
											<th style="text-align:right;">操作</th>
										</tr>
										</thead>
									</table>
								</div>
								<div class="obj-table-header-height"></div>
								<div class="layui-table-body layui-table-main">
									<table cellspacing="0" cellpadding="0" border="0" class="layui-table" lay-skin="nob">
										<colgroup>
											<col width="50">
											<col width="100">
											<col>
											<col width="200">
											<col width="80">
											<col width="80">
										</colgroup>
										<%--<tbody>--%>

										<%--</tbody>--%>
									</table>
								</div>
								<!-- <div class="obj-table-footer-height"></div> -->
								<!-- <div class="layui-table-page">
                                    <div id="paging"></div>
                                </div> -->
								<!-- <table class="layui-table layui-form" lay-skin="nob">
                                    <colgroup>
                                        <col width="50">
                                        <col width="100">
                                        <col>
                                        <col width="200">
                                        <col width="80">
                                        <col width="80">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th><input type="checkbox" name="all-checked" lay-skin="primary" lay-filter="all-checked"></th>
                                            <th>姓名</th>
                                            <th>确认/标志</th>
                                            <th>时间</th>
                                            <th>状态</th>
                                            <th style="text-align:right;">操作</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                            <td>张珊</td>
                                            <td>暂无记录</td>
                                            <td>2017-12-20</td>
                                            <td>未读</td>
                                            <td style="text-align:right;">
                                                <a class="tab-icon tab-dele " href=""><img src="/resources/web/images/handle04.png" alt=""></a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                            <td>张珊</td>
                                            <td>暂无记录</td>
                                            <td>2017-12-20</td>
                                            <td>未读</td>
                                            <td style="text-align:right;">
                                                <a class="tab-icon tab-dele " href=""><img src="/resources/web/images/handle04.png" alt=""></a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                            <td>张珊</td>
                                            <td>暂无记录</td>
                                            <td>2017-12-20</td>
                                            <td>未读</td>
                                            <td style="text-align:right;">
                                                <a class="tab-icon tab-dele " href=""><img src="/resources/web/images/handle04.png" alt=""></a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><input type="checkbox" name="all-checked" lay-skin="primary"></td>
                                            <td>张珊</td>
                                            <td>暂无记录</td>
                                            <td>2017-12-20</td>
                                            <td>未读</td>
                                            <td style="text-align:right;">
                                                <a class="tab-icon tab-dele " href=""><img src="/resources/web/images/handle04.png" alt=""></a>
                                            </td>
                                        </tr>

                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="7" style="text-align: right;">
                                                <div id="paging"></div>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table> -->
							</div>
						</div>
						<div class="btn-all">
							<!-- <button type="button" class="layui-btn layui-btn-primary affirm-btn">确认</button>
                            <button type="button" class="layui-btn layui-btn-primary save-btn">保存</button>
                            <button type="button" class="layui-btn layui-btn-primary anew-btn">重新确认</button>
                            <button type="button" class="layui-btn layui-btn-primary goback-btn">返回</button> -->
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
<!-- </div> -->
<div class="contacts-content" style="display:none;">
	<h5>人员</h5>
	<div class="layer">
		<ul class="contacts-nav">
			<li data-group="最近" class="active">最近</li>
			<li data-group="同部门">同部门</li>
			<!-- <li data-group="我的下属">我的下属</li> -->
			<li data-group="组织结构">组织结构</li>
			<!-- <li data-group="常用组">常用组</li> -->
		</ul>
		<div class="contacts-box layui-clear">
			<div class="contacts-box-l">
				<div class="oa-form-input">
					<input class="form-control" id="contacts_search" placeholder="搜索姓名" type="text">
					<div class="input-addon" id="contacts_search_btn">查询</div>
				</div>
				<div class="contacts-context">
					<div class="contacts-table-outer">
						<!-- <table class="contacts-table">
                            <tbody>
                                <tr>
                                    <td style="width:50px;text-align:center;">
                                        <input type="checkbox" data-receiveuserid="6"
                                        name="contacts-choice" class="contacts-table-input" />
                                        <img src="/resources/web/images/head-icon.png" alt="">
                                    </td>
                                    <td style="color:#22262a;">
                                        启汇2
                                    </td>
                                    <td  style="color:#999;">
                                        经理
                                    </td>
                                </tr>
                            </tbody>
                        </table> -->
					</div>
				</div>
			</div>
			<div class="contacts-box-c">
				<div class="image-icon">
					<div class="single">
						<div class="single-r arr-icon layui-disabled">
							<img class="initial" src="/resources/web/images/contacts-arr01.png" alt="">
							<img class="hover" src="/resources/web/images/contacts-arr01-hover.png" alt="">
						</div>
						<div class="single-l arr-icon layui-disabled">
							<img class="initial" src="/resources/web/images/contacts-arr02.png" alt="">
							<img class="hover" src="/resources/web/images/contacts-arr02-hover.png" alt="">
						</div>
					</div>
					<div class="entire">
						<div class="entire-l arr-icon layui-disabled">
							<img class="initial" src="/resources/web/images/contacts-arr03.png" alt="">
							<img class="hover" src="/resources/web/images/contacts-arr03-hover.png" alt="">
						</div>
						<div class="entire-r arr-icon layui-disabled">
							<img class="initial" src="/resources/web/images/contacts-arr04.png" alt="">
							<img class="hover" src="/resources/web/images/contacts-arr04-hover.png" alt="">
						</div>
					</div>
				</div>
			</div>
			<div class="contacts-box-r">
				<div class="oa-form-input">
					<input class="form-control" id="have-search" placeholder="请输入关键字搜索" type="text">
					<div class="input-addon" id="have-search-btn">查询</div>
				</div>
				<div class="contacts-context">
					<div class="contacts-table-outer">
						<table class="contacts-table">
							<tbody>
							<!-- <tr>
                                <td style="width:50px;text-align:center;">
                                    <input type="checkbox" data-receiveuserid="6"  name="contacts-choice" class="contacts-table-input" />
                                    <img src="/resources/web/images/head-icon.png" alt="">
                                </td>
                                <td style="color:#22262a;">
                                    启汇2
                                </td>
                                <td  style="color:#999;">
                                    经理
                                </td>
                            </tr> -->
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>

