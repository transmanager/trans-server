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
				cache : false,
				beforeSend : function() {
					$("#table tbody").empty();
					$("#table tfoot").hide();
					$("#table tbody").append('<tr><td colspan="12" style="text-align : center;">加载中...</td></tr>');
				},
				error : function (XMLHttpRequest, textStatus, errorThrown) {
					$("#table tbody").empty();
					$("#table tbody").append('<tr><td colspan="12" style="text-align :center; color: red;">' + '操作失败: ' + errorThrown + '</td></tr>');
				}
			},
			ajaxUrl: 'order/query?action=<%=Action.OrderQuery%>&page={page}&pageSize={size}&{filterList:filter}&{sortList:column}',
			customAjaxUrl : function (table, url) {
				url = url.replace(/filter\[0\]/, 'status')
					.replace(/filter\[1\]/, 'motorcade')
					.replace(/filter\[2\]/, 'plate')
					.replace(/filter\[3\]/, 'driver')
					.replace(/filter\[4\]/, 'client')
					.replace(/filter\[5\]/, 'dId')
					.replace(/filter\[6\]/, 'cId')
					.replace(/filter\[7\]/, 'oId')
					.replace(/filter\[8\]/, 'dateDeparted')
					.replace(/filter\[9\]/, 'odometer')
					;
				return url;
			},
			ajaxProcessing : function (response) {
				var err = response.msg;
				$("#table tbody").empty();
				$("#table tfoot").hide();
				if (typeof(err) == 'undefined') {
					$("#table tbody").append('<tr><td colspan="12" style="text-align :center; color: DarkRed;">操作失败: ' + err + '</td></tr>');
					return [ 1 ];
				}
						
				if (err != "成功") {
					$("#table tbody").append('<tr><td colspan="12" style="text-align :center; color: DarkRed;">操作失败: ' + err + '</td></tr>');
					return [ 1 ];
				}

				total = response.data.total;
				var match = response.data.match;
				var r = response.data.data;
				if (total == 0) {
					$("#table tbody").append('<tr><td colspan="12" style="text-align : center;">无记录</td></tr>');
					return [ 1 ];
				}
						
				if (match == 0) {
					$("#table tbody").append('<tr><td colspan="12" style="text-align : center;">无匹配结果</td></tr>');
					return [ 1 ];
				}
						
				$("#table tfoot").show();
				$("#table tbody").empty();

				for (var i = 0; i < r.length; i++) {
					var id = r[i].id;
					var motorcade = r[i].motorcade;
					var truck = r[i].truck;
					var driver = r[i].driver;
					var client = r[i].client;
					var status = r[i].status;
					var distance = r[i].distance;
					var dateDeparted = r[i].dateDeparted;

					var dId = r[i].dId;
					var cId = r[i].cId;
					var oId = r[i].oId;

					var label = 'label-default';
					var row = '<tr id="order_' + id + '">';
					row += '<td>';
					if (status == '异常') {
						label = 'label-danger';
					} else if (status == '已完成') {
						label = 'label-success';
					} else if (status == '等待货运审核' || status == '等待运费审核' || status == '等待结算') {
						label = 'label-warning';
					}

					row += '<span class="label ' + label + '">' + status + '</span>';
					row += '</td>';
					row += '<td>' + motorcade + '</td>';
					row += '<td>' + truck + '</td>';
					row += '<td>' + driver + '</td>';
					row += '<td>' + client + '</td>';
					row += '<td>' + dId + '</td>';
					row += '<td>' + cId + '</td>';
					row += '<td>' + oId + '</td>';
					row += '<td>' + dateDeparted + '</td>';
					row += '<td>' + distance + '</td>';
					row += '<td>' + '<button onclick="onShowDetail(' + id + ')" title="详情" class="row_button btn btn-default btn-xs"><span class="glyphicon glyphicon-info-sign"></span></button>';
					row += '<td style="text-align: center; padding-right: 0px;"><input type="checkbox" value="' + id + '" name="selected" id="checkbox_' + id + '" /></td>';

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
						
					},
					8: function($cell, indx) {
						return null;
					},
					9: function($cell, indx) {
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

	function onMakeup() {
		showDialog("order/makeup", "补录运单", "true", "modal-lg");
	}

	function onShowDetail(id) {
		var url = 'order/detail?id=' + id + '&action=' + '<%=Action.OrderShowDetail%>';
		showDialog(url, '运单详情', true, 'modal-lg');
		<%-- $.ajax({
			url: "order/detail",
            type: "post",
            data: {
               	action: '<%=Action.OrderShowDetail%>',
               	type : 'all',
               	id : id,
            },
            beforeSend : function() {
			},
			success : function (response) {
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				var html = '<tr>';
				html += '<td>车队：' + response.data.motorcade + '</td>';
				html += '<td>车号：' + response.data.truck + '</td>';
				html += '<td>驾驶员：' + response.data.driver + '</td>';
				html += '<td>客户：' + response.data.client + '</td>';
				html += '</tr>';
				html += '<tr>';

				html += '<td>类型：' + response.data.type + '</td>';
				html += '</tr>';
				html += '<tr>';
				html += '<td>出发时间：' + response.data.dateDeparted + '</td>';
				html += '<td>到站时间：' + response.data.dateArrived + '</td>';
				html += '</tr>';
				html += '<tr>';
				html += '<td>回程时间：' + response.data.dateReturn + '</td>';
				html += '<td>到达时间：' + response.data.dateReturned + '</td>';
				html += '</tr>';
				html += '<tr>';
				html += '<td>里程表起：' + response.data.odometerStart + '千米</td>';
				html += '<td>里程表止：' + response.data.odometerEnd + '千米</td>';
				html += '<td>油耗：' + response.data.fuelUsed + '升</td>';
				html += '</tr>';
				html += '<tr>';
				html += '<td colspan="4"><hr /></td>';
				html += '</tr>';
				html += '<tr>';

				html += '<td>调运单号：' + response.data.dId + '</td>';
				html += '<td>装车点：' + response.data.cargoSource + '</td>';
				html += '<td>到站：' + response.data.cargoDestination + '</td>';
				html += '</tr>';
				/* html += '<tr>';
				$("#cargoContainer").find('.cIdContainer').each(function(index, value) {
					var cId = $(value).find("input").val();
					html += '<td>出库单：' + cId + '</td>';
					$(value).nextUntil('.cIdContainer').each(function(index2, value2) {
						var name = $(value2).find(".product").select2('data').text;
						var amount = $(value2).find("input:eq(2)").val();
						var weight = $(value2).find("input:eq(3)").val();
						if (index2 != 0) {
							html += '<tr>';
							html += '<td></td>';
						}
						html += '<td>货物：' + name + amount + '件，' + weight + '吨</td>';
						html += '</tr>';
					});
				});
				
				html += '<tr>';
				html += '<td colspan="4"><hr /></td>';
				html += '</tr>';
				html += '<tr>';
				html += '<td>矿单号：' + $("#oId").val() + '</td>';
				html += '<td>装车点：' + $("#oreSource").select2('data').text + '</td>';
				html += '</tr>';
				html += '<tr>';
				html += '<td>矿物：' + $("#ore").select2('data').text + '</td>';
				html += '<td>矿重：' + $("#oreWeight").val() + '吨</td>';
				html += '<td>韶钢磅重：' + $("#oreFinalWeight").val() + '吨</td>';
				html += '</tr>';
				html += '<tr>';
				html += '<td colspan="4"><hr /></td>';
				html += '</tr>';
				$(".toll").each(function(index, value) {
					var entry = $(value).find('.tollstation:eq(0)').select2('data').text;
					var exit = $(value).find('.tollstation:eq(2)').select2('data').text;
					var toll = $(value).find('.amount').val();
					
					html += '<tr>';
					html += '<td colspan="2">运费：' + entry + ' - ' + exit + '，￥' + toll;
					if ($(value).find('input:checkbox').is(":checked")) {
						html += '，载重';
					} 
					html += '</td>';
					html += '</tr>';
				});
				
				$('.fine').each(function(index, value) {
					var amount = $(value).find("input:eq(1)").val();
					var description = $(value).find("input:eq(0)").val();
					if (amount == "") {
						return false;
					}
					html += '<tr>';
					html += '<td>罚单：' + description +'，￥' + amount + '</td>';
					html += '</tr>';
				});
				$('.other-expenses').each(function(index, value) {
					var amount = $(value).find("input:eq(1)").val();
					var description = $(value).find("input:eq(0)").val();
					if (amount == "") {
						return false;
					}
					html += '<tr>';
					html += '<td>其余费用：' + description + '，￥' + amount + '</td>';
					html += '</tr>';
				}); */
				
				$("#confirm .modal-body table").empty();
				$("#confirm .modal-body table").append(html);
				$("#confirm").modal();
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(errorThrown);
			}
		}); --%>
	}
</script>

</head>
<body style="background-color: whitesmoke;">
	<div>
		<table id="table" cellspacing="0" cellpadding="0" class="tablesorter-dropbox" style="overflow-x: auto;">
				<thead>
					<tr>
						<th colspan="12" class="filter-false sorter-false" style="cursor: default; padding-left: 0px;">
							<div class="highlight_black">
								<button type="button" id="button_toggleFilter" onclick="toggleFilter()" title="搜索"  class="btn btn-primary btn-xs">
									<span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;搜索
								</button>
								<button id="button_add" type="button" title="补录运单" onclick="onMakeup()" class="btn btn-primary btn-xs f_r">
									<span class="glyphicon glyphicon-pencil"></span>&nbsp;&nbsp;补录
								</button>
							</div>
						</th>
					</tr>
					<tr class="headerRow highlight_black">
						<th class="sorter-false" data-placeholder="状态" style="width: 50px;">状态<i></i></th>
						<th class="sorter-false" data-placeholder="车队">车队<i></i></th>
						<th class="sorter-false" data-placeholder="车号">车号<i></i></th>
						<th class="sorter-false" data-placeholder="驾驶员">驾驶员<i></i></th>
						<th class="sorter-false" data-placeholder="客户">客户<i></i></th>
						<th class="sorter-false" data-placeholder="调运单号">调运单<i></i></th>
						<th class="sorter-false" data-placeholder="出库单号">出库单<i></i></th>
						<th class="sorter-false" data-placeholder="矿单号">矿单<i></i></th>
						<th class="sorter-false">出发时间<i></i></th>
						<th class="sorter-false" data-placeholder="里程">里程<i></i></th>
						<th class="sorter-false filter-false" style="text-align: center;"><i></i></th>
						<th class="filter-false sorter-false" style="text-align: center; width: 30px;">
							<input type="checkbox" onclick="checkAll('table', this)" />
						</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="12" class="pager" style="text-align: right; font-weight: bold;">
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
		<div id="confirm" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header" style="background-color: #555;">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="color: ivory;">&times;</button>
					<h4 class="modal-title" style="color: ivory;">运单详情</h4>
				</div>
				<div class="modal-body">
					<table width="100%" style="font-size: smaller; white-space: nowrap;">
						<tr>
							<td>车队：韶宝</td>
							<td>车号：粤F123456</td>
							<td>驾驶员：陈昶华</td>
							<td>客户：韶钢</td>
						</tr>
						<tr>
							<td>类型：双程</td>
						</tr>
						<tr>
							<td>出发时间：2014-08-10 15:23</td>
							<td>到站时间：2014-08-10 15:23</td>
						</tr>
						<tr>
							<td>返回时间：2014-08-10 15:23</td>
							<td>到达时间：2014-08-10 15:23</td>
						</tr>
						<tr>
							<td>里程表起：1234.56千米</td>
							<td>里程表止：7890.00千米</td>
							<td>油耗：203升</td>
						</tr>
						<tr>
							<td colspan="4"><hr /></td>
						</tr>
						<tr>
							<td>调运单号：无</td>
							<td>装车点：</td>
							<td>到站：乐从</td>
						</tr>
						<tr>
							<td>出库单：123456</td>
							<td>货物：船板3件，50.234吨</td>
						</tr>
						<tr>
							<td></td>
							<td>货物：螺纹20件，60.333吨</td>
						</tr>
						<tr>
							<td colspan="4"><hr /></td>
						</tr>
						<tr>
							<td>矿单号：87657</td>
							<td>装车点：</td>
						</tr>
						<tr>
							<td>矿物：纽曼混合矿</td>
							<td>矿重：60.98吨</td>
							<td>韶钢磅重：67.99吨</td>
						</tr>
						<tr>
							<td colspan="4"><hr /></td>
						</tr>
						<tr>
							<td>在外加油：否</td>
						</tr>
						<tr>
							<td colspan="2">去程运费：韶关南-土华，载重，￥777.9</td>
							<td colspan="2">回程运费：土华-韶关南，载重，￥345.88</td>
						</tr>
						<tr>
							<td>罚单：超速，￥200.00</td>
						</tr>
						<tr>
							<td>罚单：超重，￥120.00</td>
						</tr>
						<tr>
							<td>其余费用：加水，￥20.00</td>
						</tr>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary btn-sm" onclick="onEdit()">编辑运单</button>
					<!-- <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button> -->
				</div>
			</div>
		</div>
	</div>
</body>
</html>
