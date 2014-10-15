<%@page import="channy.transmanager.shaobao.feature.Page"%>
<%@page import="channy.transmanager.shaobao.feature.Module"%>
<%@page import="channy.transmanager.shaobao.model.user.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
User user = (User)session.getAttribute("currentUser");
String userInfo = String.format("%s (%s)", user.getName(), user.getRole().getName());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<!-- <link rel="shortcut icon" href="images/MAnywhere.ico"/> -->
<title>韶宝运输管理系统</title>
<!-- jQuery -->
<script type="text/javascript" src="resources/js/jquery-1.9.1.js"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css" />

<!-- Optional theme -->
<link rel="stylesheet" href="resources/css/bootstrap-theme.min.css" />

<link rel="stylesheet" href="resources/css/main.css" />

<!-- Latest compiled and minified JavaScript -->
<script src="resources/js/bootstrap.min.js"></script>

<script src="resources/js/util.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		openTab('page_' + '<%=Page.Home%>', '<%=Page.Home.getDescription()%>', '<%=Page.Home.getLink()%>', false);
		initializeMenu();
		adjustLayout();
		ajustIframe("page_" + "<%=Page.Home%>");
		/* swap open/close side menu icons */
		$('[data-toggle=collapse]').click(function(){
		  	$(this).find("i:eq(1)").toggleClass("glyphicon-chevron-right glyphicon-chevron-down");
		});

		$('.modal').on('hidden.bs.modal', function( event ) {
	        $(this).removeClass( 'fv-modal-stack' );
	        $('body').data( 'fv_open_modals', $('body').data( 'fv_open_modals' ) - 1 );
	    });


		$('.modal').on('shown.bs.modal', function(event) {
					// keep track of the number of open modals
			if (typeof ($('body').data('fv_open_modals')) == 'undefined') {
				$('body').data('fv_open_modals', 0);
			}

			// if the z-index of this modal has been set, ignore.
			if ($(this).hasClass('fv-modal-stack')) {
				return;
			}

			$(this).addClass('fv-modal-stack');
			$('body').data('fv_open_modals', $('body').data('fv_open_modals') + 1);

			$(this).css('z-index', 1040 + (10 * $('body').data('fv_open_modals')));
			$('.modal-backdrop').not('.fv-modal-stack').css('z-index', 1039 + (10 * $('body').data('fv_open_modals')));
			$('.modal-backdrop').not('fv-modal-stack').addClass('fv-modal-stack');
		});
	});

	window.onresize = function() {
		adjustLayout();
		$("#tab-container .tab-pane").each(function (index, value) {
			ajustIframe(value.id);
		});
	}

	window.onbeforeunload = function(event) { 
		//confirmLogout();
		return '';
	}

	function initializeMenu() {
		<% for (Module module : Module.values()) {
			if (module.isMobileOnly() || !user.isGranted(module)) {
				continue;
			}
		%>
			var html = '<li class="nav-header">';
			html += '<a href="javascript:;" data-toggle="collapse" data-target="#module_' + '<%=module%>"' + '><h5><i class="glyphicon glyphicon-tags"></i>&nbsp;&nbsp;<%=module.getDescription()%>&nbsp;&nbsp;<i class="glyphicon glyphicon-chevron-right"></i></h5></a>';
			html += '<ul class="list-unstyled collapse" id="module_' + '<%=module%>' + '">';
			<% for (Page p : Page.values()) {
				if (p.getParent() != module || p == Page.Home || !user.isGranted(p)) {
					continue;
				}
			%>
				html += '<li><a href="javascript:;" onclick="openTab(' + '\'page_<%=p%>\'' + ', \'<%=p.getDescription()%>\'' + ', \'<%=p.getLink()%>\'' + ', true)"><i class="glyphicon glyphicon-stop"></i>' + ' <%=p.getDescription()%>' + '</a></li>';
				html += "</li>";
			<%}%>
			html += "</ul>";
			$("#menu").append(html);
		<%}%>
	}

	function openTab(id, header, link, closeable) {
		if ($("#" + id).length > 0) {
			$("#tabs li a[href=#" + id + "]").tab("show");
			return;
		}
		
		var tab = '<li><a href="#' + id + '" role="tab" data-toggle="tab">';
		if (closeable) {
			tab += '<button onclick="closeTab(\'' + id + '\')" class="close closeTab" type="button">×</button>';
		}
		tab += header + '</a></li>';
		var content = '<div class="tab-pane" id="' + id + '"><iframe style="border: 0; margin: 4px;" src="' + link + '"/></div>';
		$("#tabs").append(tab);
		$("#tab-container").append(content);
		ajustIframe(id);
		$("#tabs li a[href=#" + id + "]").tab("show");
	}

	function closeTab(id) {
		if ($("#tabs .active").find("a").attr("href") == ("#" + id)) {
			var prev = $("#tabs li a[href=#" + id + "]").parent().prev();
			var next = $("#tabs li a[href=#" + id + "]").parent().next();
			if (prev.length > 0) {
				prev.find("a").tab("show");
			} else if (next.length > 0) {
				next.find("a").tab("show");
			}
		}
		$("#tabs li a[href=#" + id + "]").parent().remove();
		$("#" + id).remove();
	}

	function adjustLayout() {
		var height = document.documentElement.clientHeight - $("#top").css("height").match(/\d+/g) - $("#bottom").css("height").match(/\d+/g) - 50;
		var width = document.documentElement.clientWidth - 155;
		var bottom = document.getElementById("bottom");
		if (height < 140) {
			height = 140;
		}
		$("#tab-container").css("height", height);
		$("#menu").css("max-height", height);
		$("#main").css("width", width);
	}

	function ajustIframe(id) {
		var height = $("#tab-container").css("height").match(/\d+/g) - 10;
		var width = $("#tab-container").css("width").match(/\d+/g) - 10;
		
		$("#" + id).find("iframe").css("width", width);
		$("#" + id).find("iframe").css("height", height);
	}

	function confirmLogout() {
		$("#confirm").modal("show");
	}

	function logout() {
		window.location.href = "logout";
	}
</script>
</head>
<body style="background-color: whitesmoke">
	<div id="top" style="width: 100%; border-bottom: 1px solid #999999; background: #333333">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="javascript:;">韶宝运输管理系统</a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right" style="padding-right: 10px;">
				<li class="dropdown"><a class="dropdown-toggle" role="button"
					data-toggle="dropdown" href="javascript:;"><i
						class="glyphicon glyphicon-user"></i> <%=userInfo %> <span class="caret"></span></a>
					<ul id="g-account-menu" class="dropdown-menu" role="menu">
						<li><a href="javascript:;">修改密码</a></li>
					</ul></li>
				<li><a href="javascript:;" onclick="confirmLogout()"><i class="glyphicon glyphicon-log-out"></i> 退出</a></li>
			</ul>
		</div>
	</div>
	<!-- Left -->
	<div class="col-md-2" id="left" style="width: 150px; padding-top: 10px; overflow: auto;">
		<ul id="menu" class="list-unstyled"></ul>
	</div>
	
	<!-- Main -->
	<div class="col-md-10" id="main" style="padding-top: 5px;">
		<ul class="nav nav-tabs" role="tablist" id="tabs"></ul>

		<!-- Tab panes -->
		<div class="tab-content box" id="tab-container"></div>
	</div>
	
	<!-- Bottom -->
	<div class="footer text-center" style="color: ivory" id="bottom">
		©2014 Invested by Shaobao, developed by Channy.
    </div>
    
    <div id="confirm" class="modal fade">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header" style="background-color: #555;">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="color: ivory;">&times;</button>
					<h4 class="modal-title" style="color: ivory;">韶宝运输管理系统</h4>
				</div>
				<div class="modal-body">
					<p>确认要退出吗?</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger btn-sm" onclick="logout()">退出</button>
					<button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="dialog_dock"></div>
	
	<div class="modal fade" id="error_dialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<p style="margin : 10px;" class="text-warning text-center"></p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>