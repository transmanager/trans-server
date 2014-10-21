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

<link rel="stylesheet" href="resources/css/style.css" />

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
						$("#table tbody").append('<tr><td colspan="5" style="text-align : center;">加载中...</td></tr>');
					},
					error : function (XMLHttpRequest, textStatus, errorThrown) {
						$("#table tbody").empty();
						$("#table tbody").append('<tr><td colspan="5" style="text-align :center; color: red;">' + '操作失败: ' + errorThrown + '</td></tr>');
					}
				},
				ajaxUrl: 'role/query?action=<%=Action.RoleQuery%>&page={page}&pageSize={size}&{filterList:filter}&{sortList:column}',
				customAjaxUrl : function (table, url) {
					url = url.replace(/filter\[0\]/, 'name')
						.replace(/filter\[1\]/, 'dateCreated')
						;
					return url;
				},
				ajaxProcessing : function (response) {
					var err = response.msg;
					$("#table tbody").empty();
					$("#table tfoot").hide();
					if (typeof(err) == 'undefined') {
						$("#table tbody").append('<tr><td colspan="5" style="text-align :center; color: DarkRed;">操作失败: ' + err + '</td></tr>');
						return [ 1 ];
					}
							
					if (err != "成功") {
						$("#table tbody").append('<tr><td colspan="5" style="text-align :center; color: DarkRed;">操作失败: ' + err + '</td></tr>');
						return [ 1 ];
					}

					total = response.data.total;
					var match = response.data.match;
					var r = response.data.data;
					if (total == 0) {
						$("#table tbody").append('<tr><td colspan="5" style="text-align : center;">无记录</td></tr>');
						return [ 1 ];
					}
							
					if (match == 0) {
						$("#table tbody").append('<tr><td colspan="5" style="text-align : center;">无匹配结果</td></tr>');
						return [ 1 ];
					}
							
					$("#table tfoot").show();
					$("#table tbody").empty();

					for (var i = 0; i < r.length; i++) {
						var id = r[i].id;
						var editable = r[i].editable;
						var name = r[i].name;
						var dateCreated = r[i].dateCreated;
						var description = r[i].description;

						var row = '<tr id="user_' + id + '">';
						row += '<td>' + name + '</td>';
						row += '<td>' + dateCreated + '</td>';
						row += '<td>' + description + '</td>';
						row += '<td>' + '<button title="详情" class="row_button btn btn-default btn-xs"><span class="glyphicon glyphicon-info-sign"></span></button>';

						if (editable) {
							row += '<button class="row_button btn btn-default btn-xs" title="编辑 ' + name + '" onclick="onEdit(\'' + id + '\', \'' + name + '\')"><span class="glyphicon glyphicon-edit"></span></button>';
							row += '<button class="row_button btn btn-danger btn-xs" title="删除 ' + name + '" onclick="onRemove(\'' + id + '\', \'' + name + '\')"><span class="glyphicon glyphicon-trash"></span></button>';
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
						1: function($cell, indx) {
							return null;
						},
						
						2: function($cell, indx) {
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
</script>

</head>
<body class="whitesmoke-body">
	<div>
		<table width="100%" id="table" cellspacing="0" cellpadding="0" class="tablesorter-dropbox">
			<thead>
				<tr>
					<th colspan="5" class="filter-false sorter-false" style="cursor: default; padding-left: 0px;">
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
						</div>
					</th>
				</tr>
				<tr class="headerRow highlight_black">
					<th class="sorter-false" data-placeholder="搜索角色">角色<i></i></th>
					<th class="sorter-false" style="width: 200px;">创建日期<i></i></th>
					<th class="sorter-false filter-false">备注<i></i></th>
					<th class="sorter-false filter-false" style="text-align: center; width: 60px;"><i></i></th>
					<th class="filter-false sorter-false" style="text-align: center; width: 30px;">
						<input type="checkbox" onclick="checkAll('table', this)" />
					</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<th colspan="5" class="pager" style="text-align: right; font-weight: bold;">
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
