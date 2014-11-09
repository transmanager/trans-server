<%@page import="channy.transmanager.shaobao.model.user.Role"%>
<%@page import="channy.transmanager.shaobao.service.user.RoleService"%>
<%@page import="channy.transmanager.shaobao.feature.Action"%>
<%@page import="channy.transmanager.shaobao.feature.Page"%>
<%@page import="channy.transmanager.shaobao.feature.Module"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<% 
String dialogId = request.getParameter("dialogId");
String dialog = request.getParameter("dialog");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<!-- <link rel="shortcut icon" href="../resources/images/MAnywhere.ico"/> -->
<!-- jQuery -->
<script type="text/javascript" src="../resources/js/jquery-1.9.1.js"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="../resources/css/bootstrap.min.css" />

<!-- Optional theme -->
<link rel="stylesheet" href="../resources/css/bootstrap-theme.min.css" />

<!-- Latest compiled and minified JavaScript -->
<script src="../resources/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="../resources/css/jstree/proton/style.min.css" />
<script src="../resources/js/jstree.min.js"></script>

<script src="../resources/js/util.js"></script>

<link rel="stylesheet" href="../resources/css/style.css" />

<script type="text/javascript">
	$(document).ready(function() {
		initTree();
		<%if(dialog.equals("edit")) {%>
			$("#name").select();
		<%}%>
	});

	function initTree() {
		$("#tree").jstree({
			'plugins': ["wholerow", "checkbox"],
			'core' : {
				'themes' : {
					'name' : 'proton',
					'responsive' : true
				}
			}
		});
	}

	function onAdd() {
		if ($("#name").val() == "") {
			showErrorDialog("请输入角色名称");
			return;
		}
		var privileges = [];
		var selected = $('#tree').jstree("get_selected", true);
		if (selected.length == 0) {
			showErrorDialog("请选择至少一项权限");
			return;
		}
		$.each(selected, function() {
			privileges.push(this.id);
		});

		$.ajax({
			url : 'add',
			type : 'post',
			data : {
				action : '<%=Action.RoleAdd%>',
				name : $("#name").val(),
				description : $("#description").val(),
				privileges : privileges
			},
			beforeSend : function() {
				$("#button_submit").text("处理中...");
				$("#button_submit").addClass("disabled");
			},
			success : function (response) {
				$("#button_submit").text("添加");
				$("#button_submit").removeClass("disabled");
				
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				closeDialog('<%=dialogId%>');
				refresh('page_' + '<%=Action.RoleAdd.getParent()%>', response.data.page);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				$("#button_submit").text("添加");
				$("#button_submit").removeClass("disabled");
				showErrorDialog(errorThrown);
			}
		});
	}

	function onEdit() {
		if ($("#name").val() == "") {
			showErrorDialog("请输入角色名称");
			return;
		}
		var privileges = [];
		var selected = $('#tree').jstree("get_selected", true);
		if (selected.length == 0) {
			showErrorDialog("请选择至少一项权限");
			return;
		}
		$.each(selected, function() {
			privileges.push(this.id);
		});

		$.ajax({
			url : 'edit',
			type : 'post',
			data : {
				action : '<%=Action.RoleEdit%>',
				id : '<%=request.getParameter("role")%>',
				name : $("#name").val(),
				description : $("#description").val(),
				privileges : privileges
			},
			beforeSend : function() {
				$("#button_submit").text("处理中...");
				$("#button_submit").addClass("disabled");
			},
			success : function (response) {
				$("#button_submit").text("保存");
				$("#button_submit").removeClass("disabled");
				
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				closeDialog('<%=dialogId%>');
				refresh('page_' + '<%=Action.RoleEdit.getParent()%>', <%=request.getParameter("page")%>);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				$("#button_submit").text("保存");
				$("#button_submit").removeClass("disabled");
				showErrorDialog(errorThrown);
			}
		});
	}
</script>

</head>
<body id="body">
<%if(dialog.equals("add")) {%>
	<div class="container">
		<form class="form-horizontal" role="form">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">角色名称</label>
				<div class="col-sm-10">
					<input id="name" type="text" class="form-control" placeholder="角色名称" autofocus/>
				</div>
			</div>
			<div class="form-group">
				<label for="description" class="col-sm-2 control-label">描述</label>
				<div class="col-sm-10">
					<input id="description" type="text" class="form-control" placeholder="描述"/>
				</div>
			</div>
		</form>
	</div>
	
	<div style="height: 200px; overflow-y: auto;" id="tree">
	    <ul id="root">
	    <% for (Module module : Module.values()) { %>
	    	<li id="module_<%=module.toString()%>"><%=module.getDescription()%>
	    		<ul>
	    		<% for (Page p : Page.values()) { %>
	    			<% if (p.getParent() != module) { continue; } %>
	    			<li id="page_<%=p.toString()%>"><%=p.getDescription()%>
	    				<ul>
	    				<% for (Action action : Action.values()) { %>
	    					<% if (action.getParent() != p) { continue; } %>
		    				<li id="action_<%=action.toString()%>"><%=action.getDescription()%></li>
	    				<% } %>
	    				</ul>
	    			</li>
	    		<% } %>
	    		</ul>
	    	</li>
	    <% } %>
	    </ul>
	</div>
	<div class="openWin_hr">
		<button id="button_submit" type="button" title="添加" class="btn btn-success btn-xs f_r" onclick="onAdd()">添加</button>
	</div>
<%} else if (dialog.equals("edit")) { %>
<% 
String r = request.getParameter("role");
RoleService service = new RoleService();
Role role = service.getById(Long.parseLong(r));
%>
	<div class="container">
		<form class="form-horizontal" role="form">
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">角色名称</label>
				<div class="col-sm-10">
					<input id="name" type="text" class="form-control" placeholder="角色名称" autofocus value="<%=role.getName() %>" />
				</div>
			</div>
			<div class="form-group">
				<label for="description" class="col-sm-2 control-label">描述</label>
				<div class="col-sm-10">
					<input id="description" type="text" class="form-control" placeholder="描述" value="<%=role.getDescription() %>"/>
				</div>
			</div>
		</form>
	</div>
	
	<div style="height: 200px; overflow-y: auto;" id="tree">
	    <ul id="root">
	    <% for (Module module : Module.values()) { %>
	    	<li id="module_<%=module.toString()%>" data-jstree='{"opened":false}'><%=module.getDescription()%>
	    		<ul>
	    		<% for (Page p : Page.values()) { %>
	    			<% if (p.getParent() != module) { continue; } %>
	    			<li id="page_<%=p.toString()%>"><%=p.getDescription()%>
	    				<ul>
	    				<% for (Action action : Action.values()) { %>
	    					<% if (action.getParent() != p) { continue; } %>
		    				<li id="action_<%=action.toString()%>" <%if (role.getGrantedActions().contains(action)) { %> data-jstree='{"selected":true}' <%} %>><%=action.getDescription()%></li>
	    				<% } %>
	    				</ul>
	    			</li>
	    		<% } %>
	    		</ul>
	    	</li>
	    <% } %>
	    </ul>
	</div>
	<div class="openWin_hr">
		<button id="button_submit" type="button" title="保存" class="btn btn-success btn-xs f_r" onclick="onEdit()">保存</button>
	</div>
<%} %>
</body>
</html>
