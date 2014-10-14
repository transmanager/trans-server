<%@page import="channy.transmanager.shaobao.data.user.UserDao"%>
<%@page import="channy.transmanager.shaobao.feature.Action"%>
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

<!-- select2 -->
<script src="../resources/js/select2/select2.min.js"></script>
<script src="../resources/js/select2/select2_locale_zh-CN.js"></script>
<link rel="stylesheet" href="../resources/css/select2/select2.css" />
<link rel="stylesheet" href="../resources/css/select2/select2-bootstrap.css" />

<script src="../resources/js/util.js"></script>
<link rel="stylesheet" href="../resources/css/style.css" />

<script type="text/javascript">
	$(document).ready(function() {
		<% if (dialog.equals("add") || dialog.equals("edit")) { %>
		$("#role").select2({
			placeholder: '请选择角色',
	        minimumInputLength: 0,
	        ajax: {
	            url: "../role/query",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.RoleQuery%>',
	                    q: term, // search term
	                    pageSize: 10, // page size
	                    page: page, // page number
	                };
	            },
	            results: function (result, page) {
	                var more = (page * 10) < result.data.total; // whether or not there are more results available
	                // notice we return the value of more so Select2 knows if more results can be loaded
	                return {results: result.data.results, more: more};
	            }
	        },
	        escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
		});
		<%}%>
	});

	function validateAddDialog() {
		return true;
	}

	function onAdd() {
		if (!validateAddDialog()) {
			return;
		}
		
		$.ajax({
			url : 'add',
			type : 'post',
			data : {
				action : '<%=Action.UserAdd%>',
				employeeId : $("#id").val(),
				name : $("#name").val(),
				role : $("#role").val(),
				password : $("#password").val()
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
				refresh('page_' + '<%=Action.UserAdd.getParent()%>', response.data.page);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				$("#button_submit").text("添加");
				$("#button_submit").removeClass("disabled");
				//$('#popup .text-warning').text("登录失败：" + textStatus);
				//$('#popup').modal('show');
			}
		});
	}

	function onRemove(id) {
		$.ajax({
			url : 'remove',
			type : 'post',
			data : {
				action : '<%=Action.UserRemove%>',
				id : id
			},
			beforeSend : function() {
				$("#button_submit").text("处理中...");
				$("#button_submit").addClass("disabled");
			},
			success : function (response) {
				$("#button_submit").text("删除");
				$("#button_submit").removeClass("disabled");

				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				closeDialog('<%=dialogId%>');
				refresh('page_' + '<%=Action.UserRemove.getParent()%>');
			},

			error : function (jqXHR, textStatus, errorThrown) {
				$("#button_submit").text("删除");
				$("#button_submit").removeClass("disabled");

				showErrorDialog(textStatus);
			}
		});
	}
</script>

</head>
<body id="body">
	<% if (dialog.equals("add")) { %>
	<div>
		<div class="input-group input-group-sm">
			<span class="input-group-addon">账号</span> 
			<input id="id" type="text" class="form-control" placeholder="请输入账号" autofocus/>
		</div>
		<div class="input-group input-group-sm dialog_row">
			<span class="input-group-addon">姓名</span> 
			<input id="name" type="text" class="form-control" placeholder="请输入姓名（可选）"/>
		</div>
		<div class="input-group input-group-sm dialog_row">
			<span class="input-group-addon">角色</span>
			<input id="role" class="form-control select2" />
		</div>
		<div class="input-group input-group-sm dialog_row">
			<span class="input-group-addon">密码</span> 
			<input id="password" type="password" class="form-control" placeholder="请输入密码"/>
		</div>
		<div class="input-group input-group-sm dialog_row">
			<span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> 
			<input id="password2" type="password" class="form-control" placeholder="请重复密码"/>
		</div>
		
		<div class="openWin_hr">
			<button id="button_submit" type="button" title="添加" class="btn btn-primary btn-xs f_r" onclick="onAdd()">添加</button>
		</div>
	</div>
	<% } else if (dialog.equals("remove")) {%>
	<%
	String id = request.getParameter("id");
	String employeeId = request.getParameter("employeeId");
	String name = request.getParameter("name");
	%>
	<div>
		<div>
			即将删除用户<%=employeeId%>(<%=name%>)，要继续吗？
		</div>
		<div class="openWin_hr">
			<button id="button_submit" type="button" title="删除" class="btn btn-danger btn-xs f_r" onclick="onRemove('<%=id%>')">删除</button>
		</div>
	</div>
	<%} else if (dialog.equals("edit")) {%>
	<div>
		<div class="input-group">
			<span class="input-group-addon">账号</span> 
			<input id="id" type="text" class="form-control" placeholder="请输入账号" autofocus/>
		</div>
		<div class="input-group input-group-sm dialog_row">
			<span class="input-group-addon">姓名</span> 
			<input id="name" type="text" class="form-control" placeholder="请输入姓名（可选）"/>
		</div>
		<div class="input-group input-group-sm dialog_row">
			<span class="input-group-addon">角色</span>
			<input id="role" class="form-control select2"/>
		</div>
		<div class="openWin_hr">
			<button id="button_submit" type="button" title="保存" class="btn btn-success btn-xs f_r" onclick="onAdd()">保存</button>
		</div>
	</div>
	<% } else if (dialog.equals("reset")) { %>
	<div>
		<div class="input-group">
			<span class="input-group-addon">密码</span> 
			<input id="password" type="password" class="form-control" placeholder="请输入密码" autofocus/>
		</div>
		<div class="input-group input-group-sm dialog_row">
			<span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> 
			<input id="password2" type="password" class="form-control" placeholder="请重复密码"/>
		</div>
		<div class="openWin_hr">
			<button id="button_submit" type="button" title="保存" class="btn btn-success btn-xs f_r" onclick="onAdd()">保存</button>
		</div>
	</div>
	<% } else { %>
	<div>
		<p>无效的请求。</p>
	</div>
	<% } %>

<!--     <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script src="http://ivaynberg.github.io/select2/select2-master/select2.js"></script>
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
    <script>

      var placeholder = "Select a State";

      $('.select2, .select2-multiple').select2({ placeholder: placeholder });
      $('.select2-allow-clear').select2({ allowClear: true, placeholder: placeholder });
      $('.select2-remote').select2({
          placeholder: "Search for a movie",
          minimumInputLength: 1,
          ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
              url: "http://api.rottentomatoes.com/api/public/v1.0/movies.json",
              dataType: 'jsonp',
              data: function (term, page) {
                  return {
                      q: term, // search term
                      page_limit: 10, // page size
                      page: page, // page number
                      apikey: "w2uyd7u9mj53nhq2f8mpzquq" // please do not use so this example keeps working
                  };
              },
              results: function (data, page) {
                  var more = (page * 10) < data.total; // whether or not there are more results available

                  // notice we return the value of more so Select2 knows if more results can be loaded
                  return {results: data.movies, more: more};
              }
          },
          initSelection: function(element, callback) {
              // the input tag has a value attribute preloaded that points to a preselected movie's id
              // this function resolves that id attribute to an object that select2 can render
              // using its formatResult renderer - that way the movie name is shown preselected
              var id=$(element).val();
              if (id!=="") {
                  $.ajax("http://api.rottentomatoes.com/api/public/v1.0/movies/"+id+".json", {
                      data: {
                          apikey: "w2uyd7u9mj53nhq2f8mpzquq"
                      },
                      dataType: "jsonp"
                  }).done(function(data) { callback(data); });
              }
          },
          formatResult: movieFormatResult, // omitted for brevity, see the source of this page
          formatSelection: movieFormatSelection,  // omitted for brevity, see the source of this page
          dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
          escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
      });

      function movieFormatResult(movie) {
          var markup = "<table class='movie-result'><tr>";
          if (movie.posters !== undefined && movie.posters.thumbnail !== undefined) {
              markup += "<td class='movie-image'><img src='" + movie.posters.thumbnail + "'/></td>";
          }
          markup += "<td class='movie-info'><div class='movie-title'>" + movie.title + "</div>";
          if (movie.critics_consensus !== undefined) {
              markup += "<div class='movie-synopsis'>" + movie.critics_consensus + "</div>";
          }
          else if (movie.synopsis !== undefined) {
              markup += "<div class='movie-synopsis'>" + movie.synopsis + "</div>";
          }
          markup += "</td></tr></table>"
          return markup;
      }

      function movieFormatSelection(movie) {
          return movie.title;
      }

      $('button[data-select2-open]').click(function(){
        $('#' + $(this).data('select2-open')).select2('open');
      });

      
        var select2OpenEventName = "select2-open";

        $(':checkbox').on( "click", function() {
          $(this).parent().nextAll('select').select2("enable", this.checked );
        });
      

      $(".select2, .select2-multiple, .select2-allow-clear, .select2-remote").on( select2OpenEventName, function() {
        if ( $(this).parents('[class*="has-"]').length ) {
          var classNames = $(this).parents('[class*="has-"]')[0].className.split(/\s+/);
          for (var i=0; i<classNames.length; ++i) {
              if ( classNames[i].match("has-") ) {
                $('#select2-drop').addClass( classNames[i] );
              }
          }
        }

      });

    </script> -->
</body>
</html>
