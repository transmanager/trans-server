<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<!-- <link rel="shortcut icon" href="resources/images/MAnywhere.ico"/> -->
<!-- jQuery -->
<script type="text/javascript" src="resources/js/jquery-1.9.1.js"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css" />

<!-- Optional theme -->
<link rel="stylesheet" href="resources/css/bootstrap-theme.min.css" />

<link rel="stylesheet" href="resources/css/style.css" />

<link rel="stylesheet" href="resources/css/tree.css" />

<!-- Latest compiled and minified JavaScript -->
<script src="resources/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		initTree();
	});

	function initTree() {
		$('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');
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
<body class="whitesmoke-body">
	<div class="tree well">
	    <ul>
	        <li>
	            <span><i class="glyphicon glyphicon-folder-open"></i> Parent</span> <a href="">Goes somewhere</a>
	            <ul>
	                <li>
	                	<span><i class="glyphicon glyphicon-minus-sign"></i> Child</span> <a href="">Goes somewhere</a>
	                    <ul>
	                        <li>
		                        <span><i class="glyphicon glyphicon-leaf"></i> Grand Child</span> <a href="">Goes somewhere</a>
	                        </li>
	                    </ul>
	                </li>
	                <li>
	                	<span><i class="glyphicon glyphicon-minus-sign"></i> Child</span> <a href="">Goes somewhere</a>
	                    <ul>
	                        <li>
		                        <span><i class="glyphicon glyphicon-leaf"></i> Grand Child</span> <a href="">Goes somewhere</a>
	                        </li>
	                        <li>
	                        	<span><i class="glyphicon glyphicon-minus-sign"></i> Grand Child</span> <a href="">Goes somewhere</a>
	                            <ul>
	                                <li>
		                                <span><i class="glyphicon glyphicon-minus-sign"></i> Great Grand Child</span> <a href="">Goes somewhere</a>
			                            <ul>
			                                <li>
				                                <span><i class="glyphicon glyphicon-leaf"></i> Great great Grand Child</span> <a href="">Goes somewhere</a>
			                                </li>
			                                <li>
				                                <span><i class="glyphicon glyphicon-leaf"></i> Great great Grand Child</span> <a href="">Goes somewhere</a>
			                                </li>
			                             </ul>
	                                </li>
	                                <li>
		                                <span><i class="glyphicon glyphicon-leaf"></i> Great Grand Child</span> <a href="">Goes somewhere</a>
	                                </li>
	                                <li>
		                                <span><i class="glyphicon glyphicon-leaf"></i> Great Grand Child</span> <a href="">Goes somewhere</a>
	                                </li>
	                            </ul>
	                        </li>
	                        <li>
		                        <span><i class="glyphicon glyphicon-leaf"></i> Grand Child</span> <a href="">Goes somewhere</a>
	                        </li>
	                    </ul>
	                </li>
	            </ul>
	        </li>
	        <li>
	            <span><i class="glyphicon glyphicon-folder-open"></i> Parent2</span> <a href="">Goes somewhere</a>
	            <ul>
	                <li>
	                	<span><i class="glyphicon glyphicon-leaf"></i> Child</span> <a href="">Goes somewhere</a>
			        </li>
			    </ul>
	        </li>
	    </ul>
	</div>
</body>
</html>
