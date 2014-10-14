<%@page import="channy.transmanager.shaobao.model.vehicle.Truck"%>
<%@page import="channy.transmanager.shaobao.data.truck.TruckDao"%>
<%@page import="channy.transmanager.shaobao.model.order.OrderType"%>
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

<!-- select2 -->
<script src="../resources/js/select2/select2.min.js"></script>
<script src="../resources/js/select2/select2_locale_zh-CN.js"></script>
<link rel="stylesheet" href="../resources/css/select2/select2.css" />
<link rel="stylesheet"
	href="../resources/css/select2/select2-bootstrap.css" />

<script src="../resources/js/util.js"></script>
<link rel="stylesheet" href="../resources/css/style.css" />

<style>
.input-group-addon {
	font-size: 12px;
}

.td-title {
	padding-right: 5px;
	text-align: right;
}

td {
	padding-top: 10px;
	padding-left: 5px;
}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		$("#motorcade").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/motorcade/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.MotorcadeQuery%>',
	                    name: term, // search term
	                    pageSize: 10, // page size
	                    page: page, // page number
	                };
	            },
	            results: function (result, page) {
	                var more = (page * 10) < result.data.total; // whether or not there are more results available
	                // notice we return the value of more so Select2 knows if more results can be loaded
	                return {results: result.data.data, more: more};
	            }
	        },
	        initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
            multiple: false,
		});

		$("#plate").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/vehicle/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.TruckQuery%>',
	                    name: term, // search term
	                    status: 'Idle',
	                    pageSize: 10, // page size
	                    page: page, // page number
	                };
	            },
	            results: function (result, page) {
	                var more = (page * 10) < result.data.total; // whether or not there are more results available
	                // notice we return the value of more so Select2 knows if more results can be loaded
	                return {results: result.data.data, more: more};
	            }
	        },
	        initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
            multiple: false,
		});
		
		$("#driver").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../user/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.UserQuery%>',
	                    name: term, // search term
	                    role : '驾驶员',
	                    status : 'Normal',
	                    pageSize: 10, // page size
	                    page: page, // page number
	                };
	            },
	            results: function (result, page) {
	                var more = (page * 10) < result.data.total; // whether or not there are more results available
	                // notice we return the value of more so Select2 knows if more results can be loaded
	                return {results: result.data.data, more: more};
	            }
	        },
	        initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
            multiple: false,
		});
		
		$("#client").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../user/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.UserQuery%>',
	                    name: term, // search term
	                    role : '客户',
	                    pageSize: 10, // page size
	                    page: page, // page number
	                };
	            },
	            results: function (result, page) {
	                var more = (page * 10) < result.data.total; // whether or not there are more results available
	                // notice we return the value of more so Select2 knows if more results can be loaded
	                return {results: result.data.data, more: more};
	            }
	        },
	        createSearchChoice: function(term, data) {
	            if ($(data).filter (function() { 
		            return this.text.localeCompare(term) === 0; 
		        }).length === 0) {
			        return {id:term, text:term};
			    }
	        },
            multiple: false,
		});

		onRefresh();
	});

	function onRefresh() {
		$.ajax({
			url : 'refreshSchedule',
			type : 'post',
			data : {
				action : '<%=Action.OrderRefreshSchedule%>',
			},
			beforeSend : function() {
			},
			success : function (response) {
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				$("#motorcade").select2('data', { id: response.data.motorcade.id, text: response.data.motorcade.text });
				$("#plate").select2('data', { id: response.data.truck.id, text: response.data.truck.text });
				$("#driver").select2('data', { id: response.data.driver.id, text: response.data.driver.text });
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function validate() {
		if ($("#autoSchedule").is(":checked")) {
			if ($("#motorcade").select2('data') == null) {
				$("#motorcade").select2('open');
				return false;
			}
	
			if ($("#plate").select2('data') == null) {
				$("#plate").select2('open');
				return false;
			}
	
			if ($("#driver").select2('data') == null) {
				$("#driver").select2('open');
				return false;
			}
		}
		
		if ($("#client").select2('data') == null) {
			$("#client").select2('open');
			return false;
		}

		var type = $("#orderType").find("input:radio:checked").val();
		return true;
	}

	function onSchedule() {
		if (!validate()) {
			return;
		}
		
		var html = '<tr>';
		html += '<td>车队：' + $("#motorcade").select2('data').text + '</td>';
		html += '<td>车号：' + $("#plate").select2('data').text + '</td>';
		html += '<td>驾驶员：' + $("#driver").select2('data').text + '</td>';
		html += '<td>客户：' + $("#client").select2('data').text + '</td>';
		html += '</tr>';
		html += '<tr>';

		var tmp = $("#orderType").find("input:radio:checked").val()
		var type = "双程";
		if (tmp == 'CargoOnly') {
			type = '单程货';
		} else if (tmp == 'OreOnly') {
			type = '单程矿';
		}
		
		html += '<td>类型：' + type + '</td>';
		html += '</tr>';

		$("#confirm .modal-body table").empty();
		$("#confirm .modal-body table").append(html);
		$("#confirm").modal();
	}

	function onConfirmSchedule() {
		var type = $("#orderType").find("input:radio:checked").val();
		
		var cargoInfo = {};
		var oreInfo = {};
		$.ajax({
			url : 'client',
			type : 'post',
			data : {
				scheduleType : 'client',
				action : '<%=Action.OrderSchedule%>',
				
				motorcade: $("#motorcade").select2('data').text,
				truck : $("#plate").select2('data').text,
				driver : $("#driver").select2('data').id,
				client : $("#client").select2('data').text,

				orderType : type,

				cargoInfo : JSON.stringify(cargoInfo),
				oreInfo : JSON.stringify(oreInfo),
			},
			beforeSend : function() {
			},
			success : function (response) {
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				<%-- closeDialog('<%=dialogId%>'); --%>
				<%-- refresh('page_' + '<%=Action.OrderMakeup.getParent()%>', response.data.page); --%>
				$("#confirm").modal('hide');
				window.location.reload();
				showErrorDialog("调度成功");
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function onToggleAutoSchedule() {
		if ($("#autoSchedule").is(":checked")) {
			$("#motorcade").attr("disabled", "true");
			$("#plate").attr("disabled", "true");
			$("#driver").attr("disabled", "true");

			onRefresh();
		} else {
			$("#motorcade").removeAttr("disabled");
			$("#plate").removeAttr("disabled");
			$("#driver").removeAttr("disabled");
		}
	}
</script>

</head>
<body style="background: whitesmoke;">
	<div>
		<table width="100%">
			<tr>
				<td width="55%" style="padding: 0px; padding-right: 10px;">
					<div class="panel panel-default">
						<div class="panel-heading" style="height: 40px;">
							<div class="pull-left">调运信息</div>
							<div class="pull-right">
								<input type="checkbox" id="autoSchedule" checked="checked" onchange="onToggleAutoSchedule()" /><label for="autoSchedule">自动调度</label>
								<button type="button" onclick="onRefresh()" title="刷新"  class="btn btn-default btn-sm">
									<span class="glyphicon glyphicon-refresh"></span>
								</button>
							</div>
						</div>
						<div class="panel-body" style="font-size: 12px;">
							<table>
								<tr>
									<td class="td-title"
										style="padding-left: 0px; padding-top: 0px;">车队</td>
									<td style="padding-top: 0px;"><input id="motorcade" disabled type="text" class="form-control input-sm" placeholder="车队" autofocus /></td>
								</tr>
								<tr>
									<td class="td-title">车号</td>
									<td width="120"><input id="plate" type="text" disabled class="form-control input-sm" placeholder="车号" /></td>
									<td class="td-title" width="60px;">驾驶员</td>
									<td width="160px;"><input id="driver" type="text"
										class="form-control input-sm select2" placeholder="驾驶员" disabled /></td>
								</tr>
							</table>
						</div>
					</div>
				</td>
				<td width="45%" style="padding: 0px;">
					<div class="panel panel-default">
						<div class="panel-heading">运单基本信息</div>
						<div class="panel-body" style="font-size: 12px;">
							<table>
								<tr>
									<td class="td-title" style="padding-left: 0px;">客户</td>
									<td><input id="client" type="text"
										class="form-control input-sm" placeholder="客户" /></td>
								</tr>
								<tr>
									<td class="td-title">类型</td>
									<td id="orderType"><input id="round_trip"
										checked="checked" type="radio"
										name="type" value="<%=OrderType.RoundTrip%>" /> <label
										for="round_trip">双程</label> <input id="cargo_only"
										type="radio" name="type" value="<%=OrderType.CargoOnly%>" /> <label for="cargo_only">单程货</label>
										<input id="ore_only" type="radio" name="type"
										value="<%=OrderType.OreOnly%>" />
										<label for="ore_only">单程矿</label></td>
								</tr>
							</table>
						</div>
					</div>
				</td>
			</tr>
		</table>

		<button id="button_submit" type="button" title="提交"
			class="btn btn-primary f_r" onclick="onSchedule()">提交</button>
	</div>

	<div id="confirm" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header" style="background-color: #555;">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true" style="color: ivory;">&times;</button>
					<h4 class="modal-title" style="color: ivory;">调度信息核对</h4>
				</div>
				<div class="modal-body">
					<table width="100%"
						style="font-size: smaller; white-space: nowrap;">
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success btn-sm"
						onclick="onConfirmSchedule()">确认无误</button>
					<!-- <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button> -->
				</div>
			</div>
		</div>
	</div>
</body>
</html>
