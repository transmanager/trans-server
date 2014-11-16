<%@page import="channy.transmanager.shaobao.model.order.OrderType"%>
<%@page import="channy.transmanager.shaobao.data.user.UserDao"%>
<%@page import="channy.transmanager.shaobao.feature.Action"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% 
String dialogId = request.getParameter("dialogId");
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

<!-- datetimepicker -->
<script src="../resources/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script src="../resources/js/datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" href="../resources/css/bootstrap-datetimepicker.min.css"/>

<script src="../resources/js/util.js"></script>
<link rel="stylesheet" href="../resources/css/style.css" />

<script src="../resources/js/bootstrap-filestyle.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('.form_datetime').datetimepicker({
	        language:  'zh-CN',
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			endDate : new Date(),
			minuteStep : 1,
	    });

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
	        initSelection: function(element, callback) {
                var data = {};
            	callback(data);
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

		$(".tollstation").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/tollstation/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.TollStationQuery%>',
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
	});

	function validate() {
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
		
		if ($("#client").select2('data') == null) {
			$("#client").select2('open');
			return false;
		}

		if ($("#dateDeparted").val() == '') {
			$("#dateDeparted").focus();
			return false;
		}
		if ($("#dateArrived").val() == '') {
			$("#dateArrived").focus();
			return false;
		}
		if ($("#dateReturn").val() == '') {
			$("#dateReturn").focus();
			return false;
		}
		if ($("#dateReturned").val() == '') {
			$("#dateReturned").focus();
			return false;
		}

		var flag = true;
		var type = $("#orderType").find("input:radio:checked").val();
		if (type != 'OreOnly') {
			 if ($("#has_dId").is(":checked") && $("#dId").val() == '') {
				$("#dId").focus();
				return false;
			}
	
			if ($("#cargoSource").select2('data') == null) {
				$("#cargoSource").select2('open');
				return false;
			}
	
			if ($("#cargoDestination").select2('data') == null) {
				$("#cargoDestination").select2('open');
				return false;
			}
	
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

			if($("#oreWeight").val() == '') {
				$("#oreWeight").focus();
				return false;
			}

			if($("#oreFinalWeight").val() == '') {
				$("#oreFinalWeight").focus();
				return false;
			}
		}

		$(".toll").each(function(index, value) {
			var toll = $(value).find('.amount');

			if ($(value).find(".tollstation:eq(0)").select2('data') == null) {
				$(value).find(".tollstation:eq(0)").select2('open');
				flag = false;
				return false;
			}

			if ($(value).find(".tollstation:eq(2)").select2('data') == null) {
				$(value).find(".tollstation:eq(2)").select2('open');
				flag = false;
				return false;
			}

			if (toll.val() == '') {
				toll.focus();
				flag = false;
				return false;
			}
		});

		if (!flag) {
			return false;
		}
		
		return true;
	}

	function onMakeup() {
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

		var tmp = $("#orderType").find("input:radio:checked").val();
		var type = "双程";
		if (tmp == 'CargoOnly') {
			type = '单程货';
		} else if (tmp == 'OreOnly') {
			type = '单程矿';
		}
		
		html += '<td>类型：' + type + '</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td>出发时间：' + $("#dateDeparted").val() + '</td>';
		html += '<td>到站时间：' + $("#dateArrived").val() + '</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td>回程时间：' + $("#dateReturn").val() + '</td>';
		html += '<td>到达时间：' + $("#dateReturned").val() + '</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td>里程表起：' + $("#odometerStart").val() + '千米</td>';
		html += '<td>里程表止：' + $("#odometerEnd").val() + '千米</td>';
		html += '<td>油耗：' + $("#fuelUsed").val() + '升</td>';
		html += '</tr>';
		html += '<tr>';
		html += '<td colspan="4"><hr /></td>';
		html += '</tr>';
		html += '<tr>';

		var type = $("#orderType").find("input:radio:checked").val();
		if (type != 'OreOnly') {
			if($("#has_dId").is(':checked')) {
				html += '<td>调运单号：' + $("#dId").val() + '</td>';
			} else {
				html += '<td>调运单号：无</td>';
			}
			html += '<td>装车点：' + $("#cargoSource").select2('data').text + '</td>';
			html += '<td>到站：' + $("#cargoDestination").select2('data').text + '</td>';
			html += '</tr>';
			html += '<tr>';
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
		}

		if (type != 'CargoOnly') {
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
		}
		
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
		});
		
		$("#confirm .modal-body table").empty();
		$("#confirm .modal-body table").append(html);
		$("#confirm").modal();
	}

	function onConfirmMakeup() {
		var type = $("#orderType").find("input:radio:checked").val();
		
		var cargoInfo = {};
		if (type != 'OreOnly') {
			if ($("#has_dId").is(":checked")) {
				cargoInfo.dId = $("#dId").val();
			}
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
					var name = $(value2).find(".product").select2('data').text;
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

			cargoInfo.images = [];
			$("#outboundImageContainer").find("img").each(function (index, value){
				cargoInfo.images.push($(value).attr('src'));
			});
		}

		var oreInfo = {};
		if (type != 'CargoOnly') {
			oreInfo.oId = $("#oId").val();
			oreInfo.oreSource = $("#oreSource").select2('data').text;
			oreInfo.ore = $("#ore").select2('data').text;
			oreInfo.oreWeight = $("#oreWeight").val();
			oreInfo.oreFinalWeight = $("#oreFinalWeight").val();
			oreInfo.images = [];
			$("#inboundImageContainer").find("img").each(function (index, value){
				oreInfo.images.push($(value).attr('src'));
			});
		}

		var toll = [];
		$('.toll').each(function(index, value) {
			var t = {};
			t.entry = $(value).find(".tollstation:eq(0)").select2('data').text;
			t.exit = $(value).find(".tollstation:eq(2)").select2('data').text;
			t.amount = $(value).find(".amount").val();
			t.isLoading = $(value).find("input:checkbox").is(":checked");
			toll.push(t);
		});

		var expensesInfo = {};
		var fine = [];
		var expenses = [];
		$('.fine').each(function(index, value) {
			var f = {};
			var amount = $(value).find("input:eq(1)").val();
			f.amount = amount;
			f.description = $(value).find("input:eq(0)").val();

			if (amount == '') {
				return false;
			}
			fine.push(f);
		});
		$('.other-expenses').each(function(index, value) {
			var e = {};
			var amount = $(value).find("input:eq(1)").val();
			e.amount = amount;
			e.description = $(value).find("input:eq(0)").val();
			
			if (amount == '') {
				return false;
			}
			expenses.push(e);
		});

		expensesInfo.fine = fine;
		expensesInfo.otherExpenses = expenses;
		expensesInfo.images = [];
		$("#expensesImageContainer").find("img").each(function (index, value){
			expensesInfo.images.push($(value).attr('src'));
		});

		$.ajax({
			url : 'makeup',
			type : 'post',
			data : {
				action : '<%=Action.OrderMakeup%>',
				
				motorcade: $("#motorcade").select2('data').text,
				truck : $("#plate").select2('data').text,
				driver : $("#driver").select2('data').id,
				client : $("#client").select2('data').text,

				odometerStart : $("#odometerStart").val(),
				odometerEnd : $("#odometerEnd").val(),
				fuelUsed : $("#fuelUsed").val(),
				
				orderType : $("#orderType").find("input:radio:checked").val(),

				dateDeparted : $("#dateDeparted").val(),
				dateArrived : $("#dateArrived").val(),
				dateReturn : $("#dateReturn").val(),
				dateReturned : $("#dateReturned").val(),

				cargoInfo : JSON.stringify(cargoInfo),
				oreInfo : JSON.stringify(oreInfo),
				toll : JSON.stringify(toll),
				expensesInfo : JSON.stringify(expensesInfo)
			},
			beforeSend : function() {
			},
			success : function (response) {
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				closeDialog('<%=dialogId%>');
				refresh('page_' + '<%=Action.OrderMakeup.getParent()%>', response.data.page);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function onAddCargo(which) {
		var parent = $(which).parent().parent().parent();
		var html = "<tr>";
		html += '<td width="30px;" style="text-align: right; padding-top: 2px;">货物</td>';
		html += '<td width="110px" style="padding-top: 2px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></td>';
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

	function onAddToll(which) {
		var html = '<tr class="toll">';
		html += '<td class="td-title" style="padding-left: 0px;" width="67px;">运费</td>';
		html += '<td width="200">';
		html += '<div class="input-group">';
		html += '<span class="input-group-addon">起</span>';
		html += '<input type="text" class="form-control input-sm tollstation" placeholder="收费站"/>';
		html += '</div>';
		html += '</td>';
		html += '<td width="200">';
		html += '<div class="input-group">';
		html += '<span class="input-group-addon">止</span>';
		html += '<input type="text" class="form-control input-sm tollstation" placeholder="收费站"/>';
		html += '</div>';
		html += '</td>';
		html += '<td>';
		html += '<div class="input-group">';
		html += '<span class="input-group-addon">￥</span>';
		html += '<input type="text" class="form-control input-sm amount"/>';

		var len = $(".toll").length;
		var id = 'isLoading_' + len;
		
		html += '<span class="input-group-addon"><input type="checkbox" id="' + id + '"/><label for="' + id + '">&nbsp;载重</label></span>';
		html += '</div>';
		html += '</td>';
		html += '<td>';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddToll(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>';
		html += '<button type="button" onclick="onRemoveToll(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>';
		html += '</div>';
		html += '</td>';
		html += '</tr>';

		var parent = $(which).parent().parent().parent();
		$(html).insertAfter(parent);

		parent.next().find(".tollstation").select2 ({
	        minimumInputLength: 0,
	        ajax: {
	            url: "../system/tollstation/select",
	            type: "post",
	            dataType: 'json',
	            data: function (term, page) {
	                return {
	                	action: '<%=Action.TollStationQuery%>',
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

	function onRemoveToll(which) {
		if ($(".toll").length == 1) {
			return;
		}

		var parent = $(which).parent().parent().parent();
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
		html += '<td width="110px" style="padding-top: 2px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></td>';
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

	function onAddFine(which) {
		var html = '<tr class="fine">';
		html += '<td class="td-title" style="padding-left: 0px;">罚单</td>';
		html += '<td>';
		html += '<div class="input-group">';
		html += '<span class="input-group-addon">说明</span>';
		html += '<input name="fine" type="text" class="form-control input-sm"/>';
		html += '</div>';
		html += '</td>';
		html += '<td>';
		html += '<div class="input-group">';
		html += '<span class="input-group-addon">￥</span>';
		html += '<input type="text" class="form-control input-sm"/>';
		html += '</div>';
		html += '</td>';
		html += '<td>';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddFine(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>';
		html += '<button type="button" onclick="onRemoveFine(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>';
		html += '</div>';
		html += '</td>';
		html += '</tr>';

		var parent = $(which).parent().parent().parent();
		$(html).insertAfter(parent);
	}

	function onRemoveFine(which) {
		if ($(".fine").length == 1) {
			return;
		}

		var parent = $(which).parent().parent().parent();
		parent.remove();
	}

	function onAddExpenses(which) {
		var html = '<tr class="other-expenses">';
		html += '<td class="td-title" style="padding-left: 0px;">其余费用</td>';
		html += '<td>';
		html += '<div class="input-group">';
		html += '<span class="input-group-addon">说明</span>';
		html += '<input name="fine" type="text" class="form-control input-sm"/>';
		html += '</div>';
		html += '</td>';
		html += '<td>';
		html += '<div class="input-group">';
		html += '<span class="input-group-addon">￥</span>';
		html += '<input type="text" class="form-control input-sm"/>';
		html += '</div>';
		html += '</td>';
		html += '<td>';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddExpenses(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>';
		html += '<button type="button" onclick="onRemoveExpenses(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>';
		html += '</div>';
		html += '</td>';
		html += '</tr>';

		var parent = $(which).parent().parent().parent();
		$(html).insertAfter(parent);
	}

	function onRemoveExpenses(which) {
		if ($(".other-expenses").length == 1) {
			return;
		}

		var parent = $(which).parent().parent().parent();
		parent.remove();
	}

	function onSelectRoundTrip() {
		$(".panel:eq(1) input").removeAttr("disabled");
		$(".panel:eq(1) button").removeAttr("disabled");
		$(".panel:eq(2) input").removeAttr("disabled");

		$(":file").filestyle('disabled', false);
	}

	function onSelectOreOnly() {
		$(".panel:eq(1) input").attr("disabled", "true");
		$(".panel:eq(1) :file").filestyle('disabled', true);
		$(".panel:eq(1) button").attr("disabled", "true");

		$(".panel:eq(2) input").removeAttr("disabled");
		$(".panel:eq(2) :file").filestyle('disabled', false);
	}

	function onSelectCargoOnly() {
		$(".panel:eq(2) input").attr("disabled", "true");
		$(".panel:eq(2) :file").filestyle('disabled', true);

		$(".panel:eq(1) input").removeAttr("disabled");
		$(".panel:eq(1) :file").filestyle('disabled', false);
		$(".panel:eq(1) button").removeAttr("disabled");
	}

	function onToggleDId(which) {
		if ($(which).is(":checked")) {
			$("#dId").removeAttr("disabled");
			$("#dId").focus();
		} else {
			$("#dId").attr("disabled", "true");
		}
	}

	function readImage(input, size) {
		if (input.files) {
			var container = $(input).siblings('.row');
			for (var i = 0; i < input.files.length; i++) {
				var file = input.files[i];
				var reader = new FileReader();
	            reader.onload = (function (file) {
		            return function() {
		            	var html = '<div class="col-xs-3">';
			            if (typeof(size) != 'undefined') {
			            	html = '<div class="col-xs-6">';
				        }
			            html += '<button type="button" class="close" onclick="onRemoveImage(this)">';
						html += '<span aria-hidden="true" style="margin-right: 5px;">&times;</span>';
						html += '</button>';
			            html += '<div class="thumbnail">';
		            	html += '<img src="' + this.result + '" title="' + file.name + '"/>';
		            	html += '</div>';
		            	html += '</div>';

		            	container.append(html);
		                var len = container.children().length;
		                $(input).siblings('.bootstrap-filestyle').find('.badge').html(len);
		                // Trick to crack onchange
		                $(input).val('');
			        }
	            })(file);
	            reader.readAsDataURL(file);
			}
        }
	}

	function onRemoveImage(which) {
		var container = $(which).parent().parent();
		var badge = container.siblings('.bootstrap-filestyle').find('.badge');
		$(which).parent().remove();
		var len = container.children().length;
		if (len > 0) {
			badge.html(len);
		} else {
			badge.remove();
		}
	}
</script>

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

</head>
<body id="body">
	<div>
		<table width="100%">
			<tr>
				<td style="padding: 0px;" colspan="2">
					<div class="panel panel-default">
						<div class="panel-heading">
							<div>运单基本信息</div>
						</div>
						<div class="panel-body" style="font-size: 12px;">
							<table>
								<tr>
									<td class="td-title"
										style="padding-left: 0px; padding-top: 0px;">车队</td>
									<td width="180" style="padding-top: 0px;"><input id="motorcade" type="text" class="form-control input-sm" placeholder="车队" autofocus /></td>
									<td style="padding-top: 0px;" class="td-title">车号</td>
									<td width="180" style="padding-top: 0px;"><input id="plate" type="text" class="form-control input-sm" placeholder="车号" /></td>
									<td style="padding-top: 0px;" class="td-title" width="60">驾驶员</td>
									<td width="180" style="padding-top: 0px;"><input id="driver" type="text" class="form-control input-sm select2" placeholder="驾驶员" /></td>
								</tr>
								<tr>
									<td class="td-title" style="padding-left: 0px;">客户</td>
									<td width="180"><input id="client" type="text" class="form-control input-sm" placeholder="客户" /></td>
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
										<label for="ore_only">单程矿</label>
									</td>
								</tr>
							</table>
							<table>
								<tr>
									<td class="td-title">里程</td>
									<td>
										<div class="input-group">
											<span class="input-group-addon">起</span>
											<input type="text" class="form-control input-sm" id="odometerStart"/>
											<span class="input-group-addon">公里</span>
										</div>
									</td>
									<td>
										<div class="input-group">
											<span class="input-group-addon">止</span>
											<input type="text" class="form-control input-sm" id="odometerEnd"/>
											<span class="input-group-addon">公里</span>
										</div>
									</td>
									<td class="td-title">油量</td>
									<td colspan="2">
										<div class="input-group">
											<input type="text" class="form-control input-sm" id="fuelUsed"/>
											<span class="input-group-addon">升</span>
										</div>
									</td>
								</tr>
							</table>
							<table>
								<tr>
									<td width="60" >出发时间</td>
									<td>
										<div class="input-group">
											<input type="text" class="form-control input-sm" placeholder="月/日/时:分" id="dateDeparted"/>
										</div>
									</td>
									<td width="67" class="td-title">到站时间</td>
									<td>
										<div class="input-group">
											<input type="text" class="form-control input-sm" placeholder="月/日/时:分" id="dateArrived"/>
										</div>
									</td>
									<td width="67" class="td-title">返程时间</td>
									<td>
										<div class="input-group">
											<input type="text" class="form-control input-sm" placeholder="月/日/时:分" id="dateReturn"/>
										</div>
									</td>
									<td width="67" class="td-title">到达时间</td>
									<td>
										<div class="input-group">
											<input type="text" class="form-control input-sm" placeholder="月/日/时:分" id="dateReturned"/>
										</div>
									</td>
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
								<tr>
									<td class="td-title" colspan="4"
										style="padding-top: 0px; padding-left: 0px;">
										<div class="input-group">
											<span class="input-group-addon"><input id="has_dId"
												type="checkbox" checked="checked" onchange="onToggleDId(this)" /><label for="has_dId">调运单</label></span> <input
												type="text" id="dId" class="form-control input-sm has-error" id="dId" />
										</div>
									</td>
								</tr>
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
												<td width="30" style="text-align: right; padding-top: 2px;">货物</td>
												<td width="110" style="padding-top: 2px;"><input type="text" class="form-control input-sm product" placeholder="名称" /></td>
												<td style="padding-top: 2px;">
													<div class="input-group">
														<input type="text" class="form-control input-sm" placeholder="数量" />
														<span class="input-group-addon">件</span>
													</div>
												</td>
												<td style="padding-top: 2px;" width="110">
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
						<div class="panel-footer" style="max-height: 200px; overflow-y: auto;">
							<input type="file" multiple onchange="readImage(this, true)" class="filestyle" data-input="false" data-buttonText="添加照片" data-size="xs" data-iconName="glyphicon-picture"/>
							<div class="row" id="outboundImageContainer" style="padding-top: 5px;"></div>
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
										style="padding-left: 0px; padding-top: 0px;" width="50px;">矿单号</td>
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
								<tr>
									<td class="td-title" style="padding-left: 0px;">矿重</td>
									<td>
										<div class="input-group">
											<input type="text" class="form-control input-sm" placeholder="重量" id="oreWeight"/>
											<span class="input-group-addon">吨</span>
										</div>
									</td>
									<td class="td-title" style="padding-left: 0px;">韶钢矿重</td>
									<td>
										<div class="input-group">
											<input type="text" class="form-control input-sm" placeholder="重量" id="oreFinalWeight" />
											<span class="input-group-addon">吨</span>
										</div>
									</td>
								</tr>
							</table>
						</div>
						<div class="panel-footer" style="max-height: 200px; overflow-y: auto;">
							<input type="file" multiple onchange="readImage(this, true)" class="filestyle" data-input="false" data-buttonText="添加照片" data-size="xs" data-iconName="glyphicon-picture"/>
							<div class="row" id="inboundImageContainer" style="padding-top: 5px;"></div>
						</div>
					</div>
				</td>
			</tr>
		</table>
		<div class="panel panel-default">
			<div class="panel-heading">费用信息</div>
			<div class="panel-body" style="font-size: 12px;">
				<table>
					<tr class="toll">
						<td class="td-title" style="padding-left: 0px;" width="67px;">运费</td>
						<td width="200">
							<div class="input-group">
								<span class="input-group-addon">起</span>
								<input type="text" class="form-control input-sm tollstation" placeholder="收费站"/>
							</div>
						</td>
						<td width="200">
							<div class="input-group">
								<span class="input-group-addon">止</span>
								<input type="text" class="form-control input-sm tollstation" placeholder="收费站"/>
							</div>
						</td>
						<td>
							<div class="input-group">
								<span class="input-group-addon">￥</span>
								<input type="text" class="form-control input-sm amount"/>
								<span class="input-group-addon"><input type="checkbox" id="isLoading_0"/><label for="isLoading_0">&nbsp;载重</label></span>
							</div>
						</td>
						<td>
							<div class="btn-group btn-group-sm">
								<button type="button" onclick="onAddToll(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>
								<button type="button" onclick="onRemoveToll(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>
							</div>
						</td>
					</tr>
					<tr class="fine">
						<td class="td-title" style="padding-left: 0px;">罚单</td>
						<td>
							<div class="input-group">
								<span class="input-group-addon">说明</span>
								<input name="fine" type="text" class="form-control input-sm"/>
							</div>
						</td>
						<td>
							<div class="input-group">
								<span class="input-group-addon">￥</span>
								<input type="text" class="form-control input-sm"/>
							</div>
						</td>
						<td>
							<div class="btn-group btn-group-sm">
								<button type="button" onclick="onAddFine(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>
								<button type="button" onclick="onRemoveFine(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>
							</div>
						</td>
					</tr>
					<tr class="other-expenses">
						<td class="td-title" style="padding-left: 0px;">其余费用</td>
						<td>
							<div class="input-group">
								<span class="input-group-addon">说明</span>
								<input name="otherFee" type="text" class="form-control input-sm"/>
							</div>
						</td>
						<td>
							<div class="input-group">
								<span class="input-group-addon">￥</span>
								<input type="text" class="form-control input-sm"/>
							</div>
						</td>
						<td>
							<div class="btn-group btn-group-sm">
								<button type="button" onclick="onAddExpenses(this)" class="btn btn-default"><span class="glyphicon glyphicon-plus-sign"></span></button>
								<button type="button" onclick="onRemoveExpenses(this)" class="btn btn-default"><span class="glyphicon glyphicon-minus-sign"></span></button>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="panel-footer" style="max-height: 200px; overflow-y: auto;">
				<input type="file" multiple onchange="readImage(this)" class="filestyle" data-input="false" data-buttonText="添加照片" data-size="xs" data-iconName="glyphicon-picture"/>
				<div class="row" id="expensesImageContainer" style="padding-top: 5px;"></div>
			</div>
		</div>
		
		<div class="openWin_hr">
			<button id="button_submit" type="button" title="添加" class="btn btn-primary btn-sm f_r" onclick="onMakeup()">保存</button>
		</div>
	</div>
	
	<div id="confirm" class="modal fade">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header" style="background-color: #555;">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="color: ivory;">&times;</button>
					<h4 class="modal-title" style="color: ivory;">运单信息核对</h4>
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
					<button type="button" class="btn btn-success btn-sm" onclick="onConfirmMakeup()">确认无误</button>
					<!-- <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button> -->
				</div>
			</div>
		</div>
	</div>
</body>
</html>
