<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>新建传阅</title>
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
	<meta   http-equiv="Expires"   content="0">
	<meta   http-equiv="Cache-Control"   content="no-cache">
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
	
	<script src="/resources/web/js/jquery.ui.widget.min.js"></script>
	<script src="/resources/web/js/jquery.manifest.js"></script>
	<script src="/resources/web/js/jquery.marcopolo.min.js"></script>
	<!-- 附件上传 -->
	<link rel="stylesheet" href="/resources/web/webupload/webuploader.css">
	<script src="/resources/web/webupload/webuploader.js"></script>
	<!-- 百度富文本 -->
	<!-- <script src="/resources/web/ueditor/ueditor.config.js"></script>
	<script src="/resources/web/ueditor/ueditor.all.js"></script>
	<script src="/resources/web/ueditor/lang/zh-cn/zh-cn.js"></script> -->
	<link rel="stylesheet" type="text/css" href="/resources/web/site/assets/styles/simditor.css" />
    <script type="text/javascript" src="/resources/web/site/assets/scripts/module.js"></script>
    <script type="text/javascript" src="/resources/web/site/assets/scripts/hotkeys.js"></script>
    <script type="text/javascript" src="/resources/web/site/assets/scripts/uploader.js"></script>
	<script type="text/javascript" src="/resources/web/site/assets/scripts/simditor.js"></script>
	
	<link rel="stylesheet" href="/resources/web/css/new.css">
	<script src="/resources/web/js/new.js"></script>

</head>
<body id="mainBody" class="layui-layout layui-layout-admin" scroll="no" style="overflow-y: hidden;">
	
	<!-- 中 -->
	<div id="contentcontainer" class="clearfix main">
		
		<div id="content" class="layui-body side-bar-right">
			<div id="e8rightContentDiv" >
				<div class="content-body">
					<h1 class="title">新建传阅</h1>
					<div class="readinfo">
						<div class="rightArea">
							<!-- <div class="loading"><span class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63d;</span></div> -->
							<div class="layui-tab contact-tab" lay-filter="contact-tab">
								<ul class="layui-tab-title clearfix">
									<li class="layui-this">
										<span>联系人</span>
									</li>
									<li>
										<span>常用组</span>
									</li>
								</ul>
								<div class="layui-tab-content">
									<div class="layui-tab-item layui-show">
										<ul class="contact-tree">
											<li>
												<div class="tree-branch org-branch-one">
													<img class="tree-icon icon-up" src="/resources/web/images/contact-icon01.png" alt="">
													<img class="tree-icon icon-down" src="/resources/web/images/contact-icon02.png" alt=""> 组织架构
												</div>
											</li>
										</ul>
									</div>
									<div class="layui-tab-item">
										<ul class="contact-tree">
											<li>
											<p style="text-align:center;">暂无数据</p>
												<!-- <div class="tree-branch tree-branch-one" data-statusHrm="1">
													<img class="tree-icon icon-up" src="/resources/web/images/contact-icon01.png" alt="">
													<img class="tree-icon icon-down" src="/resources/web/images/contact-icon02.png" alt=""> 私人组
												</div> -->
											</li>
										</ul>
					
									</div>
					
								</div>
							</div>
						</div>
						<div class="lfettArea">
							<div class="readinfo layui-form">
								<div class="layui-form-item table-input">
									<label for="recipients" class="layui-form-label">收件人</label>
									<div class="layui-input-block">
										<input type="text" name="recipients" required lay-verify="required" id="recipients" placeholder="" autocomplete="off" class="layui-input">
									</div>
									<span class="add-search" /></span>
								</div>
								<div class="layui-form-item table-input">
									<label for="recipients" class="layui-form-label">主题</label>
									<div class="layui-input-block">
										<input type="text" name="theme" required lay-verify="required" id="theme" placeholder="" autocomplete="off" class="layui-input">
									</div>
								</div>
					
					
								<div class="uploader-accessory">
									<a href="javascript:void(0)" id="filePicker">添加附件</a>
									<!-- <table id="container" class="layui-table" lay-skin="line"></table> -->
					
									<!--用来存放item-->
									<ol id="thelist" class="uploader-list">
										<!-- <li>
											<p class="txt">传阅表格.xlsx </p>
											<i>（17.33k）</i>
											<div class="progress">
												<div class="progress-border">
													<span class="progress-bar"></span>
												</div>
												<i class="progress-percent">50%</i>
											</div>
											<span class="state">上传附件成功..</span>
											<span class="dele">删除</span>
										</li> -->
					
									</ol>
								</div>
								<div class="ueditor">
									<textarea id="editor" style="visibility: hidden;"></textarea>
								</div>
								<div class="choose">
									<div class="bd">选项</div>
									<div class="form-checked-box">
										<div class="form-checked layui-clear">
											<div class="checked-item">
												<input type="checkbox" id="ifImportant" value="重要传阅" title="重要传阅" lay-skin="primary" checked>
											</div>
											<div class="checked-item" style="display:none;" >
												<input id="ifUpdate" value="允许修订Word、Excel附件" title="允许修订Word、Excel附件" lay-skin="primary" checked>
											</div>
											<div class="checked-item">
												<input type="checkbox" id="ifUpload" value="允许上传附件" title="允许上传附件" lay-skin="primary" checked>
											</div>
											<div class="checked-item">
												<input type="checkbox" id="ifRemind" value="确认时提醒" title="确认时提醒" lay-skin="primary" checked>
											</div>
											<div class="checked-item">
												<input type="checkbox" id="ifAdd" value="允许添加新人员" title="允许添加新人员" lay-skin="primary" checked>
											</div>
											<a class="more" href="javascript:void(0)">更多发送选项</a>
										</div>
										<div class="form-checked form-checked-pact" style="display:none;">
											<div style="display:none;" class="checked-item">
												<input type="checkbox" id="ifNotify" value="短信提醒" title="短信提醒" lay-skin="primary">
											</div>
											<div class="checked-item">
												<input type="checkbox" id="ifRemindAll" value="确认时提醒所有传阅对象" title="确认时提醒所有传阅对象" lay-skin="primary">
											</div>
											<div class="checked-item">
												<input type="checkbox" id="ifRead" value="开封已阅确认" title="开封已阅确认" lay-skin="primary">
											</div>
											<div class="checked-item" style="display:none;">
												<input type="checkbox" id="ifSecrecy" value="传阅密送" title="传阅密送" lay-skin="primary" disabled>
											</div>
											<div class="checked-item" style="display:none;">
												<input type="checkbox" id="ifSequence" value="有序确认" title="有序确认" lay-skin="primary" disabled>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-all-btn">
						<button class="layui-btn layui-btn-primary start-btn" onclick="sendData(true)">开始传阅</button>
						<button class="layui-btn layui-btn-primary save-btn" onclick="sendData(false)">保存</button>
						<!-- <button class="layui-btn layui-btn-primary goback-btn">返回</button> -->
					</div>
					
				</div>
			</div>
		</div>
	</div>
	<!-- 弹窗 -->
	<!-- <div class="layui-dialog-content" style="display:none;">
		<textarea name="notes" class="layui-layer-textarea" placeholder="请输入传阅的确认信息备注"></textarea>
	</div> -->
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
	<script>
		layui.use(['element','form'], function(){
			var element = layui.element;
			var form = layui.form;
			form.render();
		});
	</script>

</body>
</html>





