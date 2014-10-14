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
</style>
</head>
<body id="body">
<%String tmp = request.getParameter("id");
if (tmp == null) {%>
	<div><h3>无效的运单ID。</h3></div>
<%} else {
	long id = Long.parseLong(tmp);
	Order order = service.getById(id);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");%>
	<div>
		<div>
			<%boolean flag = false; %>
			<%for(OrderStatus status : OrderStatus.values()) { %>
			<%String labelType = "label-success"; %>
			<%if(status == OrderStatus.Exception) {continue;} %>
			<%if(order.getOrderType() == OrderType.CargoOnly && (status == OrderStatus.LoadingOre || status == OrderStatus.UnloadingOre || status == OrderStatus.WaitingOre)) {continue;} %>
			<%if(order.getOrderType() == OrderType.OreOnly && (status == OrderStatus.LoadingCargo || status == OrderStatus.UnloadingCargo || status == OrderStatus.WaitingCargo)) {continue;} %>
			<%if(flag) {labelType  = "label-default";} %>
			<%if(order.getStatus() == status) {labelType = "label-info"; flag = true;} %>
			  <span class="label <%=labelType %>" <%if(order.getStatus() == status) {%>style="font-size: 13px;"<%}%>><%=status.getDesciption() %></span>
			  <%if(status != OrderStatus.Closed) { %>
			  <span class="glyphicon glyphicon-arrow-right"></span>
			  <%} %>
			<%} %>
		</div>
		<hr />
		<table width="95%">
			<tr>
				<td>车队：<%=order.getTruck().getMotorcade().getName()%></td>
				<td>车号：<%=order.getTruck().getPlate() %></td>
				<td>驾驶员：<%=order.getDriver().getName() %></td>
				<td>客户：<%=order.getClient().getName() %></td>
				</tr>
				<tr>

				<td>类型：<%=order.getOrderType().getDescription() %></td>
				</tr>
				<tr>
				<%if (order.getDateDeparted() == null) {%>
				<td>出发时间：未出发</td>
				<%} else {%>
				<td>出发时间：<%=format.format(order.getDateDeparted()) %></td>
				<%} %>
				<%if (order.getDateArrived() == null) {%>
				<td>到站时间：未到站</td>
				<%} else {%>
				<td>到站时间：<%=format.format(order.getDateArrived()) %></td>
				<%} %>
				</tr>
				<tr>
				<%if (order.getDateReturn() == null) {%>
				<td>回程时间：未回程</td>
				<%} else {%>
				<td>回程时间：<%=format.format(order.getDateReturn()) %></td>
				<%} %>
				<%if (order.getDateReturned() == null) {%>
				<td>到达时间：未回程</td>
				<%} else {%>
				<td>到达时间：<%=format.format(order.getDateReturned()) %></td>
				<%} %>
				</tr>
				<tr>
				<td>里程表起：<%=order.getOdometerStart() %> 千米</td>
				<td>里程表止：<%=order.getOdometerEnd() %> 千米</td>
				<td>油耗：<%=order.getFuelUsed() %> 升</td>
				</tr>
				<tr>
				<td colspan="4"><hr /></td>
				</tr>
				<tr>
				<%if (order.getdId() == null) {%>
				<td>调运单号：无</td>				
				<%} else {%>
				<td>调运单号：<%=order.getdId() %></td>
				<%} %>
				<%if (order.getCargoSource() == null) {%>
				<td>装车点：待定</td>
				<%} else {%>
				<td>装车点：<%=order.getCargoSource().getName() %></td>
				<%} %>
				<%if (order.getCargoDestination() == null) {%>
				<td>到站：待定</td>
				<%} else {%>
				<td>到站：<%=order.getCargoDestination().getName() %></td>
				<%} %>
				</tr>
				<tr>
				<%if (order.getCargo() == null) { %>
				<td>出库单：待定</td>
				<%} else {%>
				<% for (Cargo cargo : order.getCargo()) {%>
				<td>出库单：<%= cargo.getcId()%></td>
				<td>货物：<%= String.format("%s%d件，%.3f吨", cargo.getProduct().getName(), cargo.getAmount(), cargo.getWeight())%></td>
				<%} %>
				<%} %>
				</tr>
				<tr>
				<td colspan="4"><hr /></td>
				</tr>
				<tr>
				<%if(order.getoId() == null) { %>
				<%if(order.getOrderType() != OrderType.CargoOnly) { %>
				<td>矿单号：待定</td>
				<%} else { %>
				<td>矿单号：无</td>
				<%} %>
				<%} else { %>
				<td>矿单号：<%=order.getoId() %></td>
				<td>装车点：<%=order.getOreSource().getName() %></td>
				</tr>
				<tr>
				<td>矿物：<%=order.getOre().getName() %></td>
				<td>矿重：<%=order.getOreWeight() %> 吨</td>
				<td>韶钢矿重：<%=order.getFinalOreWeight() %> 吨</td>
				</tr>
				<%} %>
				<%if(order.getTolls() != null) { %>
				<tr>
				<td colspan="4"><hr /></td>
				</tr>
				<%for(Toll toll : order.getTolls()) { %>
				<tr>
				<td>运费：<%=String.format("%s - %s，￥%.2f", toll.getEntry().getName(), toll.getExit().getName(), toll.getAmount())%></td>
				</tr>
				<%} %>
				<%} %>
				<%if(order.getExpenses() != null) { %>
				<tr>
				<td colspan="4"><hr /></td>
				</tr>
				<%for(Expenses expenses : order.getExpenses()) { %>
				<tr>
				<td><%=expenses.getType() %>：<%=String.format("%s，￥%.2f", expenses.getDescription(), expenses.getAmount())%></td>
				</tr>
				<%} %>
				<%} %>
		</table>
	</div>
	<%if (order.getImage() != null) {%>
	<div class="row" id="expensesImageContainer" style="width: 95%;">
	<%for (Image image : order.getImage()) { %>
	<%String base64 = new String(image.getData()); %>
		<div class="col-xs-3">
			<div class="thumbnail">
				<img src="<%=base64%>" title="<%=image.getType()%>"/>
		    </div>
		</div>
	<%} %>
	</div>
	<%} %>
<%}%>
</body>
</html>
