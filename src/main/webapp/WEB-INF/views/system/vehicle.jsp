<%@page import="channy.transmanager.shaobao.feature.Action"%>
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

<!-- Latest compiled and minified JavaScript -->
<script src="../resources/js/bootstrap.min.js"></script>

<!-- tablesorter -->
<script src="../resources/js/tablesorter/jquery.tablesorter.min.js"></script>
<script src="../resources/js/tablesorter/pager/jquery.tablesorter.pager.min.js"></script>
<script src="../resources/js/tablesorter/jquery.tablesorter.widgets.min.js"></script>
<link rel="stylesheet" href="../resources/css/tablesorter/theme.dropbox.css" />
<link rel="stylesheet" href="../resources/css/tablesorter/pager/jquery.tablesorter.pager.css" />
<link rel="stylesheet" href="../resources/css/tablesorter/filter.formatter.css" />

<script src="../resources/js/util.js"></script>
<link rel="stylesheet" href="../resources/css/style.css" />

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
					page : function() {
						return currentPage;
					},
				},
				cache : false,
				beforeSend : function() {
					$("#table tbody").empty();
					$("#table tfoot").hide();
					$("#table tbody").append('<tr><td colspan="4" style="text-align : center;">加载中...</td></tr>');
				},
				error : function (XMLHttpRequest, textStatus, errorThrown) {
					$("#table tbody").empty();
					$("#table tbody").append('<tr><td colspan="4" style="text-align :center; color: red;">' + '操作失败: ' + errorThrown + '</td></tr>');
				}
			},
			ajaxUrl: 'user?action=<%=Action.UserQuery%>&page={page}&pageSize={size}&{filterList:filter}&{sortList:column}',
			customAjaxUrl : function (table, url) {
				url = url.replace(/filter\[0\]/, 'id')
					.replace(/filter\[1\]/, 'role');
				return url;
			},
			ajaxProcessing : function (response) {
				var err = response.msg;
				$("#table tbody").empty();
				$("#table tfoot").hide();
				if (typeof(err) == 'undefined') {
					$("#table tbody").append('<tr><td colspan="4" style="text-align :center; color: DarkRed;">操作失败: ' + err + '</td></tr>');
					return [ 1 ];
				}
						
				if (err != "成功") {
					$("#table tbody").append('<tr><td colspan="4" style="text-align :center; color: DarkRed;">操作失败: ' + err + '</td></tr>');
					return [ 1 ];
				}

				total = response.data.total;
				var match = response.data.match;
				var r = response.data.data;
				if (total == 0) {
					$("#table tbody").append('<tr><td colspan="4" style="text-align : center;">无记录</td></tr>');
					return [ 1 ];
				}
						
				if (match == 0) {
					$("#table tbody").append('<tr><td colspan="4" style="text-align : center;">无匹配结果</td></tr>');
					return [ 1 ];
				}
						
				$("#table tfoot").show();
				$("#table tbody").empty();

				for (var i = 0; i < r.length; i++) {
					var id = r[i].id;
					var isLocked = r[i].locked;
					var editable = true;
					//var editable = r[i].editable;
					var role = r[i].dbRole;

					var src = "../resources/images/locked.png";
					var title = "已锁定/点击解锁";
					if (!isLocked) {
						src = "../resources/images/unlocked.png";
						title = "未锁定/点击锁定";
					}
					title += ' ' + id;

					var row = '<tr id="user_' + id + '">';
					row += '<td>' + id + '</td>';
					row += '<td>' + role + '</td>';
					row += '<td>';
					row += '<button class="row_button btn btn-default btn-xs" onclick="onResetPassword(\'' + id + '\')" title="重设' + id + '的密码"><img src="../resources/images/key.png"/></button>';
					if (editable) {
						row += '<button class="row_button btn btn-default btn-xs" onclick="onToggleLock(\'' + id + '\', ' + !isLocked + ')" title="' + title + '"><img src="' + src + '"/></button>';
						row += '<button class="row_button btn btn-default btn-xs" title="编辑 ' + id + '" onclick="onEdit(\'' + id + '\')"><img src="../resources/images/edit.png"/></button>';
						row += '<button class="row_button btn btn-default btn-xs" title="删除 ' + id + '" onclick="onRemove(\'' + id + '\')"><img src="../resources/images/del.png"/></button>' + '</td>'
						+ '<td style="text-align: center; padding-right: 0px;"><input type="checkbox" value="' + id + '" name="selected" id="checkbox_' + id + '" /></td>';
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
			widgets : [ "zebra", "stickyHeaders", "filter" ],
			widgetOptions : {
				filter_liveSearch : false,
				filter_hideFilters : true,
			}
		}).tablesorterPager(pagerOptions)
		.bind('pageMoved', function(e, c){
			currentPage = c.page;
			$('.gotoPageShadow').val(currentPage + 1);
		}).bind('pagerInitialized', function (e, c) {
			currentPage = parseInt(currentPage);
			$('.gotoPageShadow').val(currentPage + 1);
			$('.gotoPageShadow').integerOnly().blur(function (event) {
				$('.gotoPageShadow').val(currentPage + 1);
			});
		});
	});

	function onAdd() {
		showDialog("user_dialog?dialog=add", "添加用户", true);
	}
</script>

</head>
<body>
	<div>
		<table width="100%" id="table" border="0" cellspacing="0" cellpadding="0" class="tablesorter-dropbox">
				<thead>
					<tr>
						<th colspan="4" class="filter-false sorter-false" style="cursor: default; padding-left: 0px;">
							<div class="highlight_blue">
								<button type="button" id="button_toggleFilter" onclick="toggleFilter()" title="搜索"  class="btn btn-primary btn-xs">
									<img src="../resources/images/search.png"/>搜索
								</button>
								<button id="button_del" type="button" title="删除用户" onclick="onRemoveMultiple()" class="btn btn-primary btn-xs f_r" >
									<img class="button_img" src="../resources/images/del.png" />删除
								</button>
								<button id="button_add" type="button" title="添加用户" onclick="onAdd()" class="btn btn-primary btn-xs f_r">
									<img class="button_img" src="../resources/images/add.png" />添加
								</button>
							</div>
						</th>
					</tr>
					<tr class="headerRow">
						<th class="sorter-false">账号<i></i></th>
						<th class="sorter-false">角色<i></i></th>
						<th class="filter-false sorter-false" style="text-align: center; width: 160px;"></th>
						<th class="filter-false sorter-false" style="text-align: center; width: 30px;">
							<input type="checkbox" onclick="checkAll('table', this)" />
						</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="4" class="pager" style="text-align: right; font-weight: bold;">
							<img border="0" src="../resources/css/tablesorter/pager/icons/first.png" class="first" alt="First" title="首页" /> 
							<img border="0" src="../resources/css/tablesorter/pager/icons/prev.png" class="prev" alt="Prev" title="上页" />
							<select style="display: none;" class="gotoPage"></select>
							 第 <input style="width: 48px; height:20px; text-align: center; display: inline;" type="text" class="gotoPageShadow form-control" /> 页
							<img border="0" src="../resources/css/tablesorter/pager/icons/next.png" class="next" alt="Next" title="下页" /> 
							<img border="0" src="../resources/css/tablesorter/pager/icons/last.png" class="last" alt="Last" title="末页" /> 
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
