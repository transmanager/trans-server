<%@page import="channy.transmanager.shaobao.feature.Action"%>
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

<!-- Latest compiled and minified JavaScript -->
<script src="resources/js/bootstrap.min.js"></script>

<!-- tablesorter -->
<script src="resources/js/jquery-ui.min.js"></script>
<script src="resources/js/tablesorter/jquery.tablesorter.min.js"></script>
<script src="resources/js/tablesorter/pager/jquery.tablesorter.pager.min.js"></script>
<script src="resources/js/tablesorter/jquery.tablesorter.widgets.min.js"></script>
<script src="resources/js/tablesorter/jquery.tablesorter.widgets-filter-formatter.min.js"></script>

<link rel="stylesheet" href="resources/css/tablesorter/theme.dropbox.css" />
<link rel="stylesheet" href="resources/css/tablesorter/pager/jquery.tablesorter.pager.css" />
<link rel="stylesheet" href="resources/css/tablesorter/filter.formatter.css" />
<link rel="stylesheet" href="resources/css/jquery-ui.css" />

<script src="resources/js/util.js"></script>
<link rel="stylesheet" href="resources/css/style.css" />

<script type="text/javascript">
	var currentPage = 0;
	$(document).ready(function() {
		var pagerOptions = {
			container : $(".pager"),
			output : '共 {totalPages} 页 &nbsp;&nbsp;&nbsp;{filteredRows} 匹配结果',
			updateArrows : true,
			page : currentPage,
			size : 15,
			fixedHeight : true,
			removeRows : false,
			cssNext : '.next',
			cssPrev : '.prev',
			cssFirst : '.first',
			cssLast : '.last', 
			cssGoto : '.gotoPage',
			cssPageDisplay : '.pagedisplay',
			cssPageSize : '.pagesize',
			cssDisabled : 'disabled',
			ajaxObject : {
				type : "post",
				data : {
					/* page : function() {
						return currentPage;
					}, */
				},
				cache : false,
				beforeSend : function() {
					$("#table tbody").empty();
					$("#table tfoot").hide();
					$("#table tbody").append('<tr><td colspan="6" style="text-align : center;">加载中...</td></tr>');
				},
				error : function (XMLHttpRequest, textStatus, errorThrown) {
					$("#table tbody").empty();
					$("#table tbody").append('<tr><td colspan="6" style="text-align :center; color: red;">' + '操作失败: ' + errorThrown + '</td></tr>');
				}
			},
			ajaxUrl: 'user/query?action=<%=Action.UserQuery%>&page={page}&pageSize={size}&{filterList:filter}&{sortList:column}',
			customAjaxUrl : function (table, url) {
				url = url.replace(/filter\[0\]/, 'employeeId')
					.replace(/filter\[1\]/, 'name')
					.replace(/filter\[2\]/, 'role')
					.replace(/filter\[3\]/, 'dateCreated')
					;
				return url;
			},
			ajaxProcessing : function (response) {
				var err = response.msg;
				$("#table tbody").empty();
				$("#table tfoot").hide();
				if (typeof(err) == 'undefined') {
					$("#table tbody").append('<tr><td colspan="6" style="text-align :center; color: DarkRed;">操作失败: ' + err + '</td></tr>');
					return [ 1 ];
				}
						
				if (err != "成功") {
					$("#table tbody").append('<tr><td colspan="6" style="text-align :center; color: DarkRed;">操作失败: ' + err + '</td></tr>');
					return [ 1 ];
				}

				total = response.data.total;
				var match = response.data.match;
				var r = response.data.data;
				if (total == 0) {
					$("#table tbody").append('<tr><td colspan="6" style="text-align : center;">无记录</td></tr>');
					return [ 1 ];
				}
						
				if (match == 0) {
					$("#table tbody").append('<tr><td colspan="6" style="text-align : center;">无匹配结果</td></tr>');
					return [ 1 ];
				}
						
				$("#table tfoot").show();
				$("#table tbody").empty();

				for (var i = 0; i < r.length; i++) {
					var id = r[i].id;
					var employeeId = r[i].employeeId;
					var isLocked = r[i].isLocked;
					var editable = r[i].editable;
					var name = r[i].name;
					var dateCreated = r[i].dateCreated;
					//var editable = r[i].editable;
					var role = r[i].role;

					var tipEntity =  employeeId + "(" + name + ")";

					var icon = "glyphicon-lock";
					var title = "已锁定/点击解锁";
					if (!isLocked) {
						icon = "glyphicon-bookmark";
						title = "未锁定/点击锁定";
					}
					title += ' ' + tipEntity;

					var row = '<tr id="user_' + id + '">';
					row += '<td>' + employeeId;
					/* if (!isLocked) {
						row += '&nbsp;&nbsp;<span class="label label-default">已锁定</span>';
					}  */
					row += '</td>';
					row += '<td>' + name + '</td>';
					row += '<td>' + role + '</td>';
					row += '<td>' + dateCreated + '</td>';
					row += '<td>' + '<button title="详情" class="row_button btn btn-default btn-xs"><span class="glyphicon glyphicon-info-sign"></span></button>';

					if (editable) {
						row += '<button class="row_button btn btn-default btn-xs" onclick="onToggleLock(\'' + id + '\', ' + !isLocked + ')" title="' + title + '"><span class="glyphicon ' + icon + '"></span></button>';
						row += '<button class="row_button btn btn-default btn-xs" onclick="onResetPassword(\'' + id + '\', \'' + employeeId + '\', \'' + name + '\')" title="重设' + tipEntity + '的密码"><span class="glyphicon glyphicon-certificate"></span></button>';
						row += '<button class="row_button btn btn-default btn-xs" title="编辑 ' + tipEntity + '" onclick="onEdit(\'' + id + '\', \'' + employeeId + '\', \'' + name + '\')"><span class="glyphicon glyphicon-edit"></span></button>';
						row += '<button class="row_button btn btn-danger btn-xs" title="删除 ' + tipEntity + '" onclick="onRemove(\'' + id + '\', \'' + employeeId + '\', \'' + name + '\')"><span class="glyphicon glyphicon-trash"></span></button>';
						row += '</td>';
						row += '<td style="text-align: center; padding-right: 0px;"><input type="checkbox" value="' + id + '" name="selected" id="checkbox_' + id + '" /></td>';
					} else {
						row += '</td><td></td>';
					}
					row += '</tr>';
					$("#table tbody").append(row);
				}
				return [ match ];
			},
		};

		$("#table").tablesorter({
			widthFixed : true,
			widgets : [ "zebra", "stickyHeaders", "filter" ],
			widgetOptions : {
				filter_liveSearch : false,
				filter_hideFilters : true,
				//filter_saveFilters : true,
				filter_placeholder : {
					from : '开始日期',
					to   : '结束日期'
				},
				filter_formatter : {
					3: function($cell, indx) {
						return null;
					},
					
					4: function($cell, indx) {
				        return $.tablesorter.filterFormatter.uiDatepicker( $cell, indx, {
				          textFrom: '',   // "from" label text
				          textTo: '',       // "to" label text
				          delayed : true,
				          changeMonth : true,
				  		  changeYear : true,
				  		  hideIfNoPrevNext : true,
				  		  prevText : "上个月",
				  		  nextText : "下个月",
				  		  dateFormat : 'yy-mm-dd',
				  		  maxDate : "+0d",
				  		  monthNames : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
				  		  monthNamesShort : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
				  		  dayNames : ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
				  		  dayNamesShort : ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
				  		  dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
				        });
					},

					5: function($cell, indx) {
						return null;
					}
				}
			}
		}).tablesorterPager(pagerOptions)
		.bind('pageMoved', function(e, c){
			currentPage = c.page;
			$('.gotoPageShadow').val(currentPage + 1);
		}).bind('pagerInitialized', function (e, c) {
			/* currentPage = parseInt(c.page);
			$('.gotoPageShadow').val(currentPage + 1);
			$('.gotoPageShadow').integerOnly().blur(function (event) {
				$('.gotoPageShadow').val(currentPage + 1);
			}); */
		});

		$(".hasDatePicker").addClass("form-control");
		$(".tablesorter-filter").addClass("form-control");
	});

	function onAdd() {
		showDialog("user/dialog?dialog=add", "添加用户", true);
		//$(".gotoPage").trigger("pageSet", 0);
		//$("#table").trigger("update");
		//$("table").trigger("update");
	}

	function onRemove(id, employeeId, name) {
		var page = $("#table")[0].config.pager.page;
		showDialog("user/dialog?dialog=remove&id=" + id + "&employeeId=" + employeeId + "&name=" + name + "&page=" + page, "删除用户" + employeeId + "(" + name + ")", true);
	}

	function onEdit(id, employeeId, name) {
		showDialog("user/dialog?dialog=edit&id=" + id, "编辑用户" + employeeId + "(" + name + ")", true);
	}

	function onResetPassword(id, employeeId, name) {
		showDialog("user/dialog?dialog=reset&id=" + id , "重设用户" + employeeId + "(" + name + ") 的密码", true);
	}

	function onToggleLock(id, isLocked) {
		var action = '<%=Action.UserLock%>';
		var url = 'user/lock';
		if (!isLocked) {
			action = '<%=Action.UserUnlock%>';
			url = 'user/unlock';
		}
		$.ajax({
			url : url,
			type : 'post',
			data : {
				action : action,
				id : id
			},
			beforeSend : function() {
			},
			success : function (response) {
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function onMobileLogin() {
		$.ajax({
			url : 'mobile/auth',
			type : 'post',
			data : {
				action : '<%=Action.MobileAuthenticate%>',
				employeeId: '10001',
				password: '888888',
			},
			beforeSend : function() {
			},
			success : function (response) {
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				var token = response.data.token;

				//sync(token);
				//uploadImage(token);
				multipleUpload(token);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
		//sync('b71014c8-b68b-4976-9233-5340abb433df');
	}

	function multipleUpload(token) {
		$.ajax({
			url : 'mobile/image/upload/init',
			type : 'post',
			data : {
				action : '<%=Action.MobileImageUploadInit%>',
				employeeId: '10001',
				token: token,
				orderId: '1',
				x: '108.2',
				y: '23.3',
				description: "test",
				dateTaken: "2014-09-10 09:09:09",
			},
			beforeSend : function() {
			},
			success : function (response) {
				alert(JSON.stringify(response));
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				uploadParts1(token, response.data.uploadId);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function uploadParts1(token, uploadId) {
		$.ajax({
			url : 'mobile/image/upload/parts',
			type : 'post',
			data : {
				action : '<%=Action.MobileImageUploadParts%>',
				employeeId: '10001',
				token: token,
				orderId: '1',
				payload: 'data:image/png;base64,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',
				offset: 0,
				uploadId: uploadId
			},
			beforeSend : function() {
			},
			success : function (response) {
				alert(JSON.stringify(response));
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}
				uploadParts2(token, uploadId);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function uploadParts2(token, uploadId) {
		$.ajax({
			url : 'mobile/image/upload/parts',
			type : 'post',
			data : {
				action : '<%=Action.MobileImageUploadParts%>',
				employeeId: '10001',
				token: token,
				orderId: '1',
				uploadId: uploadId,
				offset: 39781,
				payload: 'C'
			},
			beforeSend : function() {
			},
			success : function (response) {
				alert(JSON.stringify(response));
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				completeUpload(token, uploadId);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function completeUpload(token, uploadId) {
		$.ajax({
			url : 'mobile/image/upload/complete',
			type : 'post',
			data : {
				action : '<%=Action.MobileImageUploadComplete%>',
				employeeId: '10001',
				token: token,
				orderId: '1',
				uploadId: uploadId,
				md5: 'a0ba06fad13258ca08b5c6b02b62e824'
			},
			beforeSend : function() {
			},
			success : function (response) {
				alert(JSON.stringify(response));
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function uploadImage(token) {
		$.ajax({
			url : 'mobile/image/add',
			type : 'post',
			data : {
				action : '<%=Action.MobileImageAdd%>',
				employeeId: '10001',
				token: token,
				orderId: '1',
				picture: 'data:image/png;base64,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',
				x: '108.2',
				y: '23.3',
				description: "test",
				dateTaken: "2014-09-10 09:09:09",
			},
			beforeSend : function() {
			},
			success : function (response) {
				alert(JSON.stringify(response));
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function sync(token) {
		$.ajax({
			url : 'mobile/sync',
			type : 'post',
			data : {
				action : '<%=Action.MobileSync%>',
				employeeId: '10001',
				token: token,
			},
			beforeSend : function() {
			},
			success : function (response) {
				alert(JSON.stringify(response));
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}
</script>

</head>
<body class="whitesmoke-body">
	<div>
		<table width="100%" id="table" cellspacing="0" cellpadding="0" class="tablesorter-dropbox">
				<thead>
					<tr>
						<th colspan="6" class="filter-false sorter-false" style="cursor: default; padding-left: 0px;">
							<div class="highlight_black">
								<button type="button" id="button_toggleFilter" onclick="toggleFilter()" title="搜索"  class="btn btn-primary btn-xs">
									<span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;搜索
								</button>
								<button id="button_del" type="button" title="删除用户" onclick="onRemoveMultiple()" class="btn btn-danger btn-xs f_r" >
									<span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;删除
								</button>
								<button id="button_add" type="button" title="添加用户" onclick="onAdd()" class="btn btn-primary btn-xs f_r">
									<span class="glyphicon glyphicon-plus-sign"></span>&nbsp;&nbsp;添加
								</button>
								<!-- <button id="button_add" type="button" title="添加用户" onclick="onMobileLogin()" class="btn btn-primary btn-xs f_r">
									<span class="glyphicon glyphicon-plus-sign"></span>&nbsp;&nbsp;test
								</button> -->
							</div>
						</th>
					</tr>
					<tr class="headerRow highlight_black">
						<th class="sorter-false" data-placeholder="搜索账号">账号<i></i></th>
						<th class="sorter-false" data-placeholder="搜索姓名">姓名<i></i></th>
						<th class="sorter-false" data-placeholder="搜索角色">角色<i></i></th>
						<th class="sorter-false" style="width: 200px;">创建日期<i></i></th>
						<th class="sorter-false filter-false" style="text-align: center; width: 160px;"><i></i></th>
						<th class="filter-false sorter-false" style="text-align: center; width: 30px;">
							<input type="checkbox" onclick="checkAll('table', this)" />
						</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="6" class="pager" style="text-align: right; font-weight: bold;">
							<button class="first btn btn-default btn-xs" title="首页"><span class="glyphicon glyphicon-step-backward"></span></button>
							<button class="prev btn btn-default btn-xs" title="上页"><span class="glyphicon glyphicon-backward"></span></button>
							第 <select class="gotoPage"></select> 页
							<button class="next btn btn-default btn-xs" title="下页"><span class="glyphicon glyphicon-forward"></span></button>
							<button class="last btn btn-default btn-xs" title="末页"><span class="glyphicon glyphicon-step-forward"></span></button>
							&nbsp;&nbsp;
							<span class="pagedisplay"></span>
							&nbsp;&nbsp;
						</th>
					</tr>
				</tfoot>
			</table>
		</div>
</body>
</html>
