<%@page import="channy.transmanager.shaobao.feature.Action"%>
<%@page import="channy.transmanager.shaobao.feature.Page"%>
<%@page import="channy.transmanager.shaobao.feature.Module"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

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

<link rel="stylesheet" href="../resources/css/style.css" />

<link rel="stylesheet" href="../resources/css/tree.css" />

<!-- Latest compiled and minified JavaScript -->
<script src="../resources/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		initTree();
	});

	function initTree() {
		$('.tree li:has(ul)').addClass('parent_li').find(' > span')/* .attr('title', 'Collapse this branch') */;
	    $('.tree li.parent_li > span').on('click', function (e) {
	        var children = $(this).parent('li.parent_li').find(' > ul > li');
	        if (children.is(":visible")) {
	            children.hide('fast');
	            //$(this).attr('title', 'Expand this branch').find(' > i').addClass('glyphicon glyphicon-plus-sign').removeClass('glyphicon glyphicon-minus-sign');
	        } else {
	            children.show('fast');
	            //$(this).attr('title', 'Collapse this branch').find(' > i').addClass('glyphicon glyphicon-minus-sign').removeClass('glyphicon glyphicon-plus-sign');
	        }
	        e.stopPropagation();
	    });
	}
</script>

</head>
<body class="whitesmoke-body" id="body">
	<div class="tree" style="height: 200px; overflow-y: auto;">
	    <ul id="root" class="list-inline">
	    <% for (Module module : Module.values()) { %>
	    	<li>
	    		<span><i class="glyphicon glyphicon-th-list"></i> <%=module.getDescription()%></span>
	    		<ul>
	    		<% for (Page p : Page.values()) { %>
	    			<% if (p.getParent() != module) { continue; } %>
	    			<li>
	    				<span><%=p.getDescription()%></span>
	    				<ul>
	    				<% for (Action action : Action.values()) { %>
	    					<% if (action.getParent() != p) { continue; } %>
		    				<li>
		    					<span><%=action.getDescription()%></span>
		    				</li>
	    				<% } %>
	    				</ul>
	    			</li>
	    		<% } %>
	    		</ul>
	    	</li>
	    <% } %>
	    </ul>
	</div>
</body>
</html>
