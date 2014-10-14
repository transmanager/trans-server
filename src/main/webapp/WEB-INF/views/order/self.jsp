<%@page import="channy.transmanager.shaobao.model.vehicle.Truck"%>
<%@page import="channy.transmanager.shaobao.data.truck.TruckDao"%>
<%@page import="channy.transmanager.shaobao.model.order.OrderType"%>
<%@page import="channy.transmanager.shaobao.feature.Action"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	//Truck truck = TruckDao.getNextCandidate();
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
            multiple: false,
            initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
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
            multiple: false,
            initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
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
            multiple: false,
            initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
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

		$(".place").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/place/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.PlaceQuery%>',
	                    name: term, // search term
	                    //role : '客户',
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
	        initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
            multiple: false,
		});

		$("#ore").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/ore/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.OreQuery%>',
	                    name: term, // search term
	                    //role : '客户',
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
	        initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
            multiple: false,
		});

		$(".product").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/product/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.ProductQuery%>',
	                    name: term, // search term
	                    //role : '客户',
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
	        initSelection: function(element, callback) {
                var data = {};
            	callback(data);
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
		if (type != 'OreOnly') {
			/* if ($("#has_dId").is(":checked") && $("#dId").val() == '') {
				$("#dId").focus();
				return false;
			} */
	
			if ($("#cargoSource").select2('data') == null) {
				$("#cargoSource").select2('open');
				return false;
			}
	
			if ($("#cargoDestination").select2('data') == null) {
				$("#cargoDestination").select2('open');
				return false;
			}
	
			var flag = true;
			$("#cargoContainer").find('.cIdContainer').each(function(index, value) {
				if (!flag) {
					return false;
				}
				var cId = $(value).find("input");
				if (cId.val() == '') {
					cId.focus();
					flag = false;
					return false;
				}
				$(value).nextUntil('.cIdContainer').each(function(index2, value2) {
					var name = $(value2).find(".product");
					var amount = $(value2).find("input:eq(2)");
					var weight = $(value2).find("input:eq(3)");
	
					if (name.select2('data') == null) {
						name.select2('open');
						flag = false;
						return false;
					}
	
					if (amount.val() == '') {
						amount.focus();
						flag = false;
						return false;
					}
	
					if (weight.val() == '') {
						weight.focus();
						flag = false;
						return false;
					}
				});
			});
	
			if (!flag) {
				return false;
			}
		}

		if (type != 'CargoOnly') {
			if ($("#oId").val() == '') {
				$("#oId").focus();
				return false;
			}
	
			if ($("#oreSource").select2('data') == null) {
				$("#oreSource").select2('open');
				return false;
			}
	
			if ($("#ore").select2('data') == null) {
				$("#ore").select2('open');
				return false;
			}
		}
		return true;
	}

	function onAddCargo(which) {
		var parent = $(which).parent().parent().parent();
		var html = "<tr>";
		html += '<td width="30px;" style="text-align: right; padding-top: 2px;">货物</td>';
		html += '<td width="150px" style="padding-top: 2px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></td>';
		html += '<td style="padding-top: 2px;"><div class="input-group"><input type="text" class="form-control input-sm" placeholder="数量"/><span class="input-group-addon">件</span></div></td>';
		html += '<td style="padding-top: 2px;"><div class="input-group"><input type="text" class="form-control input-sm" placeholder="重量"/><span class="input-group-addon">吨</span></div></td>';
		html += '<td width="75px;" style="padding-top: 2px;">';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddCargo(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>';
		html += '<button type="button" onclick="onRemoveCargo(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>';
		html += '</div>';
		html += '</td>';
		html += "</tr>";

		$(html).insertAfter(parent);

		parent.next().find(".product").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/product/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.ProductQuery%>',
	                    name: term, // search term
	                    //role : '客户',
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
	        initSelection: function(element, callback) {
                var data = {};
            	callback(data);
            },
            multiple: false,
		});
	}

	function onRemoveCargo(which) {
		var parent = $(which).parent().parent().parent();
		var nextSibling = parent.next();
		var previousSibling = parent.prev();
		if ((nextSibling.hasClass("cIdContainer") && previousSibling.hasClass("cIdContainer"))
				|| (previousSibling.hasClass("cIdContainer") && nextSibling.length == 0)) {
			return;
		}
		parent.remove();
	}

	function onAddCId(which) {
		var html = '<tr class="cIdContainer">';
		html += '<td width="50px;">出库单</td>';
		html += '<td colspan="3"><input type="text" class="form-control input-sm" id="cId"/></td>';
		html += '<td>';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddCId(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>';
		html += '<button type="button" onclick="onRemoveCId(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>';
		html += '</div>';
		html += '</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td width="30px" style="text-align: right; padding-top: 2px;">货物</td>';
		html += '<td width="150px" style="padding-top: 2px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></td>';
		html += '<td style="padding-top: 2px;">';
		html += '<div class="input-group">';
		html += '<input type="text" class="form-control input-sm" placeholder="数量"/>';
		html += '<span class="input-group-addon">件</span>';
		html += '</div>';
		html += '</td>';
		html += '<td style="padding-top: 2px;">';
		html += '<div class="input-group">';
		html += '<input type="text" class="form-control input-sm" placeholder="重量"/>';
		html += '<span class="input-group-addon">吨</span>';
		html += '</div>';
		html += '</td>';
		html += '<td width="75px;" style="padding-top: 2px;">';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddCargo(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>';
		html += '<button type="button" onclick="onRemoveCargo(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>';
		html += '</div>';
		html += '</td>';
		html += '</tr>';

		var parent = $(which).parent().parent().parent();
		var table = $("#cargoContainer");
		var theOne;

		if ($("#cargoContainer .cIdContainer").length > 1) {
			var nextSibling = parent.next();
			while (!nextSibling.hasClass('cIdContainer')) {
				nextSibling = nextSibling.next();
			}
			
			$(html).insertBefore(nextSibling);
			theOne = nextSibling.prev().find('.product');
			
		} else {
			table.append(html);
			theOne = $("#cargoContainer").find(".product:last");
		}

		theOne.select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/product/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.PlaceQuery%>',
	                    name: term, // search term
	                    //role : '客户',
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
	}

	function onRemoveCId(which) {
		var parent = $(which).parent().parent().parent();
		var table = parent.parent();
		if (table.find(".cIdContainer").length == 1) {
			return;
		}
		//var nextSibling = parent.next('.cIdContainer');
		parent.nextUntil('.cIdContainer').remove();
		parent.remove();
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

		if (type != '单程矿') {
			html += '<tr>';
			html += '<td colspan="4"><hr /></td>';
			html += '</tr>';
			html += '<tr>';
			/* if($("#has_dId").is(':checked')) {
				html += '<td>调运单号：' + $("#dId").val() + '</td>';
			} else {
				html += '<td>调运单号：无</td>';
			} */
			html += '<td>装车点：' + $("#cargoSource").select2('data').text + '</td>';
			html += '<td>到站：' + $("#cargoDestination").select2('data').text + '</td>';
			html += '</tr>';
			html += '<tr>';
			$("#cargoContainer").find('.cIdContainer').each(function(index, value) {
				var cId = $(value).find("input").val();
				html += '<td>出库单：' + cId + '</td>';
				$(value).nextUntil('.cIdContainer').each(function(index2, value2) {
					var name = $(value2).find("input:eq(1)").select2('data').text;
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
		}

		if (type != '单程货') {
			html += '<tr>';
			html += '<td colspan="4"><hr /></td>';
			html += '</tr>';
			html += '<tr>';
			html += '<td>矿单号：' + $("#oId").val() + '</td>';
			html += '<td>装车点：' + $("#oreSource").select2('data').text + '</td>';
			html += '</tr>';
			html += '<tr>';
			html += '<td>矿物：' + $("#ore").select2('data').text + '</td>';
			html += '</tr>';
		}

		$("#confirm .modal-body table").empty();
		$("#confirm .modal-body table").append(html);
		$("#confirm").modal();
	}

	function onConfirmSchedule() {
		var type = $("#orderType").find("input:radio:checked").val();
		
		var cargoInfo = {};
		if (type != 'OreOnly') {
			/* if ($("#has_dId").is(":checked")) {
				cargoInfo.dId = $("#dId").val();
			} */
			cargoInfo.cargoSource = $("#cargoSource").select2('data').text;
			cargoInfo.cargoDestination = $("#cargoDestination").select2('data').text;
			cargoInfo.cargo = [];
			$("#cargoContainer").find('.cIdContainer').each(function(index, value) {
				var sheet = {};
				var cId = $(value).find("input").val();
				sheet.cId = cId;
				var products = [];
				$(value).nextUntil('.cIdContainer').each(function(index2, value2) {
					var product = {};
					var name = $(value2).find("input:eq(1)").select2('data').text;
					var amount = $(value2).find("input:eq(2)").val();
					var weight = $(value2).find("input:eq(3)").val();
					product.name = name;
					product.amount = amount;
					product.weight = weight;
	
					products.push(product);
				});
				sheet.products = products;
				cargoInfo.cargo.push(sheet);
			});
		}

		var oreInfo = {};
		if (type != 'CargoOnly') {
			oreInfo.oId = $("#oId").val();
			oreInfo.oreSource = $("#oreSource").select2('data').text;
			oreInfo.ore = $("#ore").select2('data').text;
		}

		$.ajax({
			url : 'self',
			type : 'post',
			data : {
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

	function onSelectRoundTrip() {
		$(".panel:eq(2) input").removeAttr("disabled");
		$(".panel:eq(2) button").removeAttr("disabled");
		$(".panel:eq(3) input").removeAttr("disabled");
	}

	function onSelectOreOnly() {
		$(".panel:eq(2) input").attr("disabled", "true");
		$(".panel:eq(2) button").attr("disabled", "true");

		$(".panel:eq(3) input").removeAttr("disabled");
	}

	function onSelectCargoOnly() {
		$(".panel:eq(3) input").attr("disabled", "true");

		$(".panel:eq(2) input").removeAttr("disabled");
		$(".panel:eq(2) button").removeAttr("disabled");
	}

	function onToggleDId(which) {
		if ($(which).is(":checked")) {
			$("#dId").removeAttr("disabled");
			$("#dId").focus();
		} else {
			$("#dId").attr("disabled", "true");
		}
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
										checked="checked" onclick="onSelectRoundTrip()" type="radio"
										name="type" value="<%=OrderType.RoundTrip%>" /> <label
										for="round_trip">双程</label> <input id="cargo_only"
										type="radio" name="type" value="<%=OrderType.CargoOnly%>"
										onclick="onSelectCargoOnly()" /> <label for="cargo_only">单程货</label>
										<input id="ore_only" type="radio" name="type"
										value="<%=OrderType.OreOnly%>" onclick="onSelectOreOnly()" />
										<label for="ore_only">单程矿</label></td>
								</tr>
							</table>
						</div>
					</div>
				</td>
			</tr>
			<tr style="vertical-align: top;">
				<td width="55%" style="padding: 0px; padding-right: 10px;">
					<div class="panel panel-default ">
						<div class="panel-heading">去程</div>
						<div class="panel-body"
							style="font-size: 12px; max-height: 240px; min-height: 240px; overflow-y: auto;">
							<table>
								<!-- <tr>
									<td class="td-title" colspan="4"
										style="padding-top: 0px; padding-left: 0px;">
										<div class="input-group">
											<span class="input-group-addon"><input id="has_dId"
												type="checkbox" checked="checked" onchange="onToggleDId(this)" /><label for="has_dId">调运单</label></span> <input
												type="text" id="dId" class="form-control input-sm has-error" id="dId" />
										</div>
									</td>
								</tr> -->
								<tr>
									<td class="td-title" style="padding-left: 0px;" width="67px;">装车点</td>
									<td><input type="text" class="form-control input-sm place" placeholder="装车点" id="cargoSource" /></td>
									<td class="td-title" width="50px">到站</td>
									<td><input type="text" class="form-control input-sm place" placeholder="到站" id="cargoDestination" /></td>
								</tr>
								<tr>
									<td colspan="4">
										<table id="cargoContainer">
											<tr class="cIdContainer">
												<td width="50px;">出库单</td>
												<td colspan="3"><input type="text"
													class="form-control input-sm" id="cId" /></td>
												<td>
													<div class="btn-group btn-group-sm">
														<button type="button" onclick="onAddCId(this)"
															class="btn btn-default">
															<span class="glyphicon glyphicon-plus-sign"></span>
														</button>
														<button type="button" onclick="onRemoveCId(this)"
															class="btn btn-default">
															<span class="glyphicon glyphicon-minus-sign"></span>
														</button>
													</div>
												</td>
											</tr>
											<tr>
												<td width="30px" style="text-align: right; padding-top: 2px;">货物</td>
												<td width="150px" style="padding-top: 2px;"><input type="text" class="form-control input-sm product" placeholder="名称" /></td>
												<td style="padding-top: 2px;">
													<div class="input-group">
														<input type="text" class="form-control input-sm" placeholder="数量" />
														<span class="input-group-addon">件</span>
													</div>
												</td>
												<td style="padding-top: 2px;">
													<div class="input-group">
														<input type="text" class="form-control input-sm" placeholder="重量" />
														<span class="input-group-addon">吨</span>
													</div>
												</td>
												<td width="75px;" style="padding-top: 2px;">
													<div class="btn-group btn-group-sm">
														<button type="button" onclick="onAddCargo(this)"
															class="btn btn-default">
															<span class="glyphicon glyphicon-plus-sign"></span>
														</button>
														<button type="button" onclick="onRemoveCargo(this)"
															class="btn btn-default">
															<span class="glyphicon glyphicon-minus-sign"></span>
														</button>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</td>
				<td width="45%" style="padding: 0px;">
					<div class="panel panel-default">
						<div class="panel-heading">回程</div>
						<div class="panel-body"
							style="font-size: 12px; max-height: 240px; min-height: 240px; overflow-y: auto;">
							<table>
								<tr>
									<td class="td-title"
										style="padding-left: 0px; padding-top: 0px;" width="67px;">矿单号</td>
									<td colspan="2" style="padding-top: 0px;"><input
										type="text" class="form-control input-sm" id="oId" /></td>
								</tr>
								<tr>
									<td class="td-title" style="padding-left: 0px;">装车点</td>
									<td><input type="text" class="form-control input-sm place" id="oreSource" placeholder="装车点" /></td>
								</tr>
								<tr>
									<td class="td-title" style="padding-left: 0px;">矿物</td>
									<td><input type="text" class="form-control input-sm" id="ore" placeholder="矿物" /></td>
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
		<div class="modal-dialog modal-lg">
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
