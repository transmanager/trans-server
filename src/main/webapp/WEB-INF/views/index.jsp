<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<!-- <link rel="shortcut icon" href="resources/images/MAnywhere.ico"/> -->
<title>韶宝运输管理系统</title>
<!-- jQuery -->
<script type="text/javascript" src="resources/js/jquery-1.9.1.js"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css" />

<!-- Optional theme -->
<link rel="stylesheet" href="resources/css/bootstrap-theme.min.css" />

<link rel="stylesheet" href="resources/css/login.css" />

<!-- Latest compiled and minified JavaScript -->
<script src="resources/js/bootstrap.min.js"></script>

<!--<script src="resources/js/socket.io-1.0.6.js"></script>-->

<script type="text/javascript">
	//var socket = io.connect("http://localhost:7000");
	//io.set("polling duration", 1);

	$(document).ready(function() {
		$("#io").click(function() {
			socket.emit('chat message', $("#username").val());
		});
		
		$("#login").click(function() {
			if (!validate()) {
				return;
			}
			
			submit();
		});

		$("input").keypress(function (event){
			if (event.which == 13) {
				if (!validate()) {
					return;
				}

				submit();
			}
		});

		$('#username').popover({
			trigger: 'manual',
			content: '请输入用户名',
			placement : 'right',
		});

		$('#password').popover({
			trigger: 'manual',
			content: '请输入密码',
			placement : 'right',
		});

		$('#popup').on('hidden.bs.modal', function (e) {
			$("#password").val("");
			$("#username").select();
		});
	});

	function validate() {
		if ($("#username").val() == "") {
			$("#username").focus();
			$("#username").focus();
			$('#username').popover("show");
			setTimeout(function() {
				$('#username').popover("hide");
			}, 4000);
			return false;
		}
		if ($("#password").val() == "") {
			$("#password").focus();
			$('#password').popover("show");
			setTimeout(function() {
				$('#password').popover("hide");
			}, 4000);
			return false;
		}
		
		return true;
	}

	function submit() {
		$.ajax({
			url : 'login',
			type : 'post',
			data : {
				id : $("#username").val(),
				password : $("#password").val()
			},
			beforeSend : function() {
				$("#login").text("登录中...");
				$("#username").attr("disabled", "true");
				$("#password").attr("disabled", "true");
				$("#login").addClass("disabled");
			},
			success : function (response) {
				if (response.msg != "成功") {
					$('#popup .text-warning').text(response.msg);
					$('#popup').modal('show');

					$("#login").text("登录");
					$("#username").removeAttr("disabled");
					$("#password").removeAttr("disabled");
					$("#login").removeClass("disabled");
					return;
				}
				window.location.href = response.data.url;
			},

			error : function (jqXHR, textStatus, errorThrown) {
				$("#login").text("登录");
				$("#login").removeClass("disabled");
				$("#username").removeAttr("disabled");
				$("#password").removeAttr("disabled");
				$('#popup .text-warning').text("登录失败：" + textStatus);
				$('#popup').modal('show');
			}
		});
	}
</script>

</head>
<body>
	<div class="form-signin">
        <h2 class="form-signin-heading">请登录</h2>
        <input id="username" type="username" class="form-control" placeholder="用户名" autofocus />
        <input id="password" type="password" class="form-control" placeholder="密码" />
        <button class="btn btn-lg btn-primary btn-block" id="login">登录</button>
    </div>

	<div class="modal fade" id="popup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
