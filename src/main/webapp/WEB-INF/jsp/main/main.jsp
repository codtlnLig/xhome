<%@page pageEncoding="utf-8"%>
<%@ taglib uri="/script-tag" prefix="script"%>
<!DOCTYPE html>
<html>
	<head>
		<title>${initParam.WebAppName}</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/themes/base/jquery.ui.all.css">
		<link id='favicon' rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/pagination.css"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/wp.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.table.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/dhtml/dhtmlxtree.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.treeTable.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/kendoui/styles/kendo.default.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/kendoui/styles/kendo.common.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.Jcrop.min.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/ztree/demo.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/ztree/zTreeStyle.css">

		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/kendoui/js/kendo.web.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/kendoui/js/cultures/kendo.culture.zh-CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/jquery.ui.core.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/jquery-ui-1.8.23.custom.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/jquery.ui.dialog.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ui/i18n/jquery-ui-i18n.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.table.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.validate.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/messages_zh.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.pagination.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ajaxfileupload.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" charset="utf-8"></script>
		 <script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtml/dhtmlxcommon.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/dhtml/dhtmlxtree.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.tree.dialog.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.Jcrop.min.js"></script>
		
		<!-- Load common js -->
		<script:inport folder="js/custom" />
		
		<script type="text/javascript" src="js/ztree/jquery.ztree-core.3.5.js"></script>
		<script type="text/javascript" src="js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
		
		<style>
		.myDialog .ui-icon-closethick {display: none;}
		</style>
	</head>
	<body>
		<div class='header'>
			<div class='logo'>
				${initParam.WebAppName}
			</div>
			<div class='userinfo' id='userinfo' rid='${user.role.id}'>
				欢迎您，${user.loginName}&nbsp;|&nbsp;<a href='#' onclick="profile()">修改密码</a>&nbsp;|&nbsp;<a href='logout.do'>退出</a>
			</div>
		</div>
		<div class='mainBody'>
			<div class="container">
				<div id="tabs">
						
				</div>
			</div>
		</div>
<script type="text/html" id="test">
<ul class='topMenu'>
	<li  style="margin-left: 320px;"></li>
    {{each roots as value i}}
		<li id='li{{value.id}}'><em class='icon'></em><a href="#tab-{{value.id}}">{{value.title}}</a></li>
    {{/each}}
</ul>
{{each roots as value i}}
	<div id='tab-{{value.id}}'>
		<div class='leftPanel'>
		<ul>
            <s:iterator value='subMenus'>
            {{each value.subMenus as menu}}
				<li onclick="{{menu.event}}">
					<div class='caption'><span class='title'>{{menu.title}}</span><a class='icon'></a></div>
					<ul class='subMenu'>
                     {{each menu.subMenus as subMenu}}
					 <li onclick="{{subMenu.event}}"><a>{{subMenu.title}}</a></li>
			         {{/each}}
				    </ul>
                </li>
			{{/each}}
       </ul>
       </div>
    </div>
{{/each}}
<div class='rightPanel' id='mainForm'></div>
</script>
<script>
var roots = ${roots};
	var data = {
		roots: roots
	};
	if(null!=roots&&roots.length>0){
		var html = template('test', data);
		document.getElementById('tabs').innerHTML = html;
	}
</script>
	<div id='commonDialog' title="提示"></div>
	<div id='profileDialog' title="提示"></div>
	<div id='msgDialog' title="提示"></div>
	<div id="treeDialog"  style="display: none"></div>
	<div id='enterpriseEditDialog' style="display: none"></div>
	<div id='smsDialog'  style="display: none"></div>
	<div id='commonDialogDiv' style="display: none"></div>
	<div id='tips' style="display: none">
	</div>
	</body>
</html>
