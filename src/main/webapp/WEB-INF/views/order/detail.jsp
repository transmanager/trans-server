<%@page import="channy.transmanager.shaobao.model.Product"%>
<%@page import="channy.transmanager.shaobao.service.OrderService"%>
<%@page import="channy.transmanager.shaobao.model.order.OrderStatus"%>
<%@page import="channy.transmanager.shaobao.model.Image"%>
<%@page import="channy.transmanager.shaobao.model.Expenses"%>
<%@page import="channy.transmanager.shaobao.model.Toll"%>
<%@page import="channy.transmanager.shaobao.model.order.OrderType"%>
<%@page import="channy.transmanager.shaobao.model.Cargo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="channy.transmanager.shaobao.data.order.OrderDao"%>
<%@page import="channy.transmanager.shaobao.model.order.Order"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<% OrderService service = new OrderService(); %>

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

<script type="text/javascript">
	$(document).ready(function() {
		
	});
</script>
<style>
td {
	padding: 5px;
	font-size: smaller;
}

.wide-row {
	margin-bottom: 8px;
}
</style>
</head>
<body id="body">
<%String tmp = request.getParameter("id");
if (tmp == null) {%>
	<div><h3>无效的运单ID。</h3></div>
<%} else {
	long id = Long.parseLong(tmp);
	Order order = service.getDetailById(id);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");%>
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-1" style="max-height: 480px; overflow-y: auto;">
				<%String labelType = "label-success"; boolean flag = false;%>
				<%for(OrderStatus status : OrderStatus.values()) { %>
				<%if (status.getDesciption().contains("失败")) {continue;} %>
				<%if (status == OrderStatus.CargoVerificationPending && order.getStatus() == OrderStatus.CargoVerificationFailed) { %>
				<div class="row text-center" style="padding-top: 5px; padding-bottom: 5px;">
					<span class="label label-danger"><%= order.getStatus().getDesciption() %></span>
				</div>
				<div class="row text-center" >
					<span class="glyphicon glyphicon-arrow-down"></span>
				</div>
				<% flag = true; continue; }%>
				<%if (status == OrderStatus.ExpensesVerificationPending && order.getStatus() == OrderStatus.ExpensesVerificationFailed) {%>
				<div class="row text-center" style="padding-top: 5px; padding-bottom: 5px;">
					<span class="label label-danger"><%= order.getStatus().getDesciption() %></span>
				</div>
				<div class="row text-center" >
					<span class="glyphicon glyphicon-arrow-down"></span>
				</div>
				<% flag = true; continue; }%>
				<%if(order.getOrderType() == OrderType.CargoOnly && (status == OrderStatus.LoadingOre || status == OrderStatus.UnloadingOre || status == OrderStatus.WaitingOre)) {continue;} %>
				<%if(order.getOrderType() == OrderType.OreOnly && (status == OrderStatus.LoadingCargo || status == OrderStatus.UnloadingCargo || status == OrderStatus.WaitingCargo)) {continue;} %>
				<%if(flag) {labelType  = "label-default";} %>
				<%if(order.getStatus() == status) {labelType = "label-info"; flag = true;} %>
				<div class="row text-center" style="padding-top: 5px; padding-bottom: 5px;">
					<span class="label <%= labelType %>"><%= status.getDesciption() %></span>
				</div>
				<%if(status != OrderStatus.Closed) { %>
				<div class="row text-center" >
					<span class="glyphicon glyphicon-arrow-down"></span>
				</div>
			  	<%} %>
				<%}%>
			</div>
			<div class="col-xs-7" style="font-size: 12px; max-height: 480px; overflow-y: auto;">
				<div class="container-fluid">
					<div class="row"><h5>基本信息</h5></div>
					<hr style="margin-top: 0px; margin-bottom: 8px;"/>
					<div class="row wide-row">
						<div class="col-xs-3">车队：<%=order.getTruck().getMotorcade().getName()%></div>
						<div class="col-xs-3">车号：<%=order.getTruck().getPlate()%></div>
						<div class="col-xs-3">驾驶员：<%=order.getDriver().getName()%></div>
						<div class="col-xs-3">客户：<%=order.getClient().getName()%></div>
					</div>
					<div class="row wide-row">
						<div class="col-xs-3">类型：<%=order.getOrderType().getDescription()%></div>
						<div class="col-xs-3">里程：<%=order.getDistance()%></div>
						<div class="col-xs-3">油耗：<%=order.getFuelUsed()%></div>
					</div>
					<% if (order.getOrderType() != OrderType.OreOnly) { %>
					<div class="row"><h5>货运信息</h5></div>
					<hr style="margin-top: 0px; margin-bottom: 8px;"/>
					<div class="row wide-row">
						<div class="col-xs-5">
						装车点：<% if (order.getCargoSource() == null) { %>待定<% } else { %><%=order.getCargoSource().getName()%><% } %>
						</div>
						<div class="col-xs-5 col-xs-offset-1">
						到站：<% if (order.getCargoDestination() == null) { %>待定<% } else { %><%=order.getCargoDestination().getName()%><% } %>
						</div>
					</div>
					<div class="row wide-row">
						<div class="col-xs-5">
						出发时间：<% if (order.getDateDeparted() == null) { %>未出发<% } else { %><%=format.format(order.getDateDeparted())%><% } %>
						</div>
						<div class="col-xs-5 col-xs-offset-1">
						到站时间：<% if (order.getDateArrived() == null) { %>未到站<% } else { %><%=format.format(order.getDateArrived())%><% } %>
						</div>
					</div>
					<div class="row wide-row">
						<div class="col-xs-12">调运单号：<% if (order.getdId() == null) { %>无<% } else { %><%=order.getdId()%><% } %></div>
					</div>
					<% if (order.getCargo() == null) { %>
					<div class="row wide-row">
						<div class="col-xs-12">货物待定</div>
					</div>
					<% } else { %>
						<% String cid = ""; %>
						<% for (Cargo cargo : order.getCargo()) { %>
						<div class="row wide-row">
						<% if (!cid.equals(cargo.getcId())) { %>
						<div class="col-xs-4">出库单：<%= cargo.getcId() %></div>
						<div class="col-xs-6">货物：<%= String.format("%s%d件，%.3f吨", cargo.getProduct().getName(), cargo.getAmount(), cargo.getWeight())%></div>
						<% } else { %>
						<div class="col-xs-6 col-xs-offset-4">货物：<%= String.format("%s%d件，%.3f吨", cargo.getProduct().getName(), cargo.getAmount(), cargo.getWeight())%></div>
						<% } %>
						</div>
						<% cid = cargo.getcId(); %>
						<% } %>
					<% } %>
					<% } %>
					<% if (order.getOrderType() != OrderType.CargoOnly) { %>
					<div class="row"><h5>矿运信息</h5></div>
					<hr style="margin-top: 0px; margin-bottom: 8px;"/>
					<div class="row wide-row">
						<div class="col-xs-12">矿单号：<%= order.getoId() %></div>
					</div>
					<div class="row wide-row">
						<div class="col-xs-5">
						装车点：<% if (order.getOreSource() == null) { %>待定<% } else { %><%=order.getOreSource().getName()%><% } %>
						</div>
						<% if (order.getOreDestination() != null) { %>
						<div class="col-xs-5 col-xs-offset-1">
						到站：<%=order.getOreDestination().getName()%>
						</div>
						<%}%>
					</div>
					<div class="row wide-row">
						<div class="col-xs-5">
						回程时间：<% if (order.getDateReturn() == null) { %>未回程<% } else { %><%=format.format(order.getDateReturn())%><% } %>
						</div>
						<div class="col-xs-5 col-xs-offset-1">
						到达时间：<% if (order.getDateReturned() == null) { %>未到达<% } else { %><%=format.format(order.getDateReturned())%><% } %>
						</div>
					</div>
					<div class="row wide-row">
						<div class="col-xs-5">
						初始矿重：<%= order.getOreWeight() %>
						</div>
						<div class="col-xs-5 col-xs-offset-1">
						到站矿重：<%= order.getFinalOreWeight() %>
						</div>
					</div>
					<% } %>
				</div>
			</div>
			<div class="col-xs-4" style="max-height: 480px; overflow-y: auto;">
				<div class="container-fluid">
				<% if (order.getImage() == null || order.getImage().isEmpty()) { %>
					<div class="row">
						<div class="col-xs-12 text-center">无图片信息</div>
					</div>
				<% } else { %>
				<% for (Image image : order.getImage()) { %>
					<% if (image.getData() == null) { continue; } %>
					<%String base64 = new String(image.getData()); %>
					<div class="row">
						<div class="col-xs-12 text-center">
							<a href="#" class="thumbnail">
						      <img src="<%= base64 %>" />
						    </a>
						</div>
					</div>
				<% } %>
				<% } %>
				</div>
			</div>
		</div>
	</div>
<%}%>
</body>
</html>
