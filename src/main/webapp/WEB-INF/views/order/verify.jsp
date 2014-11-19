<%@page import="com.sun.org.apache.xerces.internal.impl.dv.util.Base64"%>
<%@page import="channy.transmanager.shaobao.model.Fine"%>
<%@page import="channy.transmanager.shaobao.model.Product"%>
<%@page import="channy.transmanager.shaobao.feature.Action"%>
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
<% 
String dialogId = request.getParameter("dialogId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
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

<link rel="stylesheet" href="../resources/css/style.css" />

<script src="../resources/js/util.js"></script>

<script src="../resources/js/jquery.mlens-1.5.min.js"></script>
<style type="text/css">
</style>
</head>
<body id="body">
<%String tmp = request.getParameter("id");
if (tmp == null) {%>
	<div><h3>无效的参数。</h3></div>
<% 
	return;
}%>

<%
long id = Long.parseLong(tmp);
Order order = service.getDetailById(id);

if (order == null) {%>
<div><h3>无效的运单ID：<%=id%>。</h3></div>
<%return;}%>

<%SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");%>
<%if (order.getStatus() == OrderStatus.CargoVerificationPending || order.getStatus() == OrderStatus.CargoVerificationFailed) { %>
<div class="container-fluid" style="font-size: 12px;">
	<div>
		<h4>货运信息</h4>
		<hr />
	</div>
	<div class="row">
		<div class="col-xs-8" style="max-height: 400px; overflow-y: auto;">
			<div class="row">
				<div class="col-xs-2">车队：</div>
				<div class="col-xs-4"><input id="motorcade" class="form-control input-sm"/></div>
				<div class="col-xs-2">车号：</div>
				<div class="col-xs-4"><input id="plate" class="form-control input-sm"/></div>
			</div>
			<div class="row" style="padding-top: 5px;">
				<div class="col-xs-2">驾驶员：</div>
				<div class="col-xs-4"><input id="driver" class="form-control input-sm"/></div>
				<div class="col-xs-2">客户：</div>
				<div class="col-xs-4"><input id="client" class="form-control input-sm"/></div>
			</div>
			<div class="row" style="padding-top: 5px;">
				<div class="col-xs-2">类型：</div>
				<div class="col-xs-4 btn-group btn-group-sm" data-toggle="buttons" id="order-type">
				  <label class="btn btn-default <% if (order.getOrderType() == OrderType.RoundTrip) {%> active <%} %>"><input type="radio" name="type" value="<%= OrderType.RoundTrip %>"/><%=OrderType.RoundTrip.getDescription()%></label>
				  <label class="btn btn-default <% if (order.getOrderType() == OrderType.CargoOnly) {%> active <%} %>"><input type="radio" name="type" value="<%= OrderType.CargoOnly %>"/><%=OrderType.CargoOnly.getDescription()%></label>
				  <label class="btn btn-default <% if (order.getOrderType() == OrderType.OreOnly) {%> active <%} %>"><input type="radio" name="type" value="<%=OrderType.OreOnly%>"/><%=OrderType.OreOnly.getDescription()%></label>
				</div>
			</div>
			<%if (order.getOrderType() == OrderType.RoundTrip || order.getOrderType() == OrderType.CargoOnly) {%>
			<div class="row outbound"><hr /></div>
			<div class="row outbound">
				<div class="col-xs-2">调运单：</div>
				<div class="input-group input-group-sm col-xs-6">
					<span class="input-group-addon"><input id="has_dId" type="checkbox" <%if (order.getdId() != null) {%>checked="checked" <%}%> onchange="onToggleDId(this)" /></span>
					<input type="text" id="dId" class="form-control" id="dId" <%if (order.getdId() == null) {%> disabled="disabled" <%} else {%> value="<%=order.getdId()%>" <%}%>/>
				</div>
			</div>
			<div class="row outbound" style="padding-top: 5px;">
				<div class="col-xs-2">装车点：</div>
				<div class="col-xs-4"><input type="text" class="form-control place input-sm" placeholder="装车点" id="cargoSource" /></div>
				<div class="col-xs-2" style="vertical-align: middle;">到站：</div>
				<div class="col-xs-4"><input type="text" class="form-control place input-sm" placeholder="到站" id="cargoDestination" /></div>
			</div>
			<%if(order.getCargo() == null || order.getCargo().isEmpty()) { %>
			<div class="row cid-row outbound" style="padding-top: 5px;">
				<div class="col-xs-2">出库单：</div>
				<div class="col-xs-5" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm" placeholder="出库单" /></div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px;">
					<div class="btn-group btn-group-sm">
						<button type="button" onclick="onAddCId(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-plus-sign"></span>
						</button>
						<button type="button" onclick="onRemoveCId(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-minus-sign"></span>
						</button>
					</div>
				</div>
			</div>
			<div class="row product-row outbound" style="padding-top: 5px;">
				<div class="col-xs-2 col-xs-offset-1" style="width:10%; padding-left: 5px; padding-right: 5px;">货物：</div>
				<div class="col-xs-4" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 16%;">
					<div class="input-group input-group-sm">
						<input type="text" class="form-control input-sm cargo-amount" placeholder="数量" style="text-align: right;" />
						<span class="input-group-addon">件</span>
					</div>
				</div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 20%;">
					<div class="input-group input-group-sm">
						<input type="text" class="form-control input-sm cargo-weight" placeholder="重量" style="text-align: right;" />
						<span class="input-group-addon">吨</span>
					</div>
				</div>
				<div class="btn-group btn-group-sm">
					<button type="button" onclick="onAddCargo(this)" class="btn btn-default">
						<span class="glyphicon glyphicon-plus-sign"></span>
					</button>
					<button type="button" onclick="onRemoveCargo(this)" class="btn btn-default">
						<span class="glyphicon glyphicon-minus-sign"></span>
					</button>
				</div>
			</div>
			<%} %>
			<%String cid = "";%>
			<%for (Cargo cargo : order.getCargo()) {%>
			<%if(!cargo.getcId().equals(cid)) {%>
			<div class="row cid-row outbound" style="padding-top: 5px;">
				<div class="col-xs-2">出库单：</div>
				<div class="col-xs-5" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm" placeholder="出库单" value="<%=cargo.getcId()%>"/></div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px;">
					<div class="btn-group btn-group-sm">
						<button type="button" onclick="onAddCId(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-plus-sign"></span>
						</button>
						<button type="button" onclick="onRemoveCId(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-minus-sign"></span>
						</button>
					</div>
				</div>
			</div>
			<%}%>
			<div class="row product-row outbound" style="padding-top: 5px;">
				<div class="col-xs-2 col-xs-offset-1" style="width:10%; padding-left: 5px; padding-right: 5px;">货物：</div>
				<div class="col-xs-4" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 16%;">
					<div class="input-group input-group-sm">
						<input type="text" class="form-control input-sm cargo-amount" placeholder="数量" style="text-align: right;" value="<%= cargo.getAmount() %>"/>
						<span class="input-group-addon">件</span>
					</div>
				</div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 20%;">
					<div class="input-group input-group-sm">
						<input type="text" class="form-control input-sm cargo-weight" placeholder="重量" style="text-align: right;" value="<%= cargo.getWeight() %>"/>
						<span class="input-group-addon">吨</span>
					</div>
				</div>
				<div class="btn-group btn-group-sm">
					<button type="button" onclick="onAddCargo(this)" class="btn btn-default">
						<span class="glyphicon glyphicon-plus-sign"></span>
					</button>
					<button type="button" onclick="onRemoveCargo(this)" class="btn btn-default">
						<span class="glyphicon glyphicon-minus-sign"></span>
					</button>
				</div>
			</div>
			<% cid = cargo.getcId(); %>
			<%}%>
			<%} else {%>
			<div class="row outbound"><hr /></div>
			<div class="row outbound">
				<div class="col-xs-2">调运单：</div>
				<div class="input-group input-group-sm col-xs-6">
					<span class="input-group-addon"><input id="has_dId" type="checkbox" <%if (order.getdId() != null) {%>checked="checked" <%}%> onchange="onToggleDId(this)" /></span>
					<input type="text" id="dId" class="form-control" id="dId" <%if (order.getdId() == null) {%> disabled="disabled" <%} else {%> value="<%=order.getdId()%>" <%}%>/>
				</div>
			</div>
			<div class="row outbound" style="padding-top: 5px;">
				<div class="col-xs-2">装车点：</div>
				<div class="col-xs-4"><input type="text" class="form-control place input-sm" placeholder="装车点" id="cargoSource" /></div>
				<div class="col-xs-2" style="vertical-align: middle;">到站：</div>
				<div class="col-xs-4"><input type="text" class="form-control place input-sm" placeholder="到站" id="cargoDestination" /></div>
			</div>
			<div class="row cid-row outbound" style="padding-top: 5px;">
				<div class="col-xs-2">出库单：</div>
				<div class="col-xs-5" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm" placeholder="出库单" /></div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px;">
					<div class="btn-group btn-group-sm">
						<button type="button" onclick="onAddCId(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-plus-sign"></span>
						</button>
						<button type="button" onclick="onRemoveCId(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-minus-sign"></span>
						</button>
					</div>
				</div>
			</div>
			<div class="row product-row outbound" style="padding-top: 5px;">
				<div class="col-xs-2 col-xs-offset-1" style="width:10%; padding-left: 5px; padding-right: 5px;">货物：</div>
				<div class="col-xs-4" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 16%;">
					<div class="input-group input-group-sm">
						<input type="text" class="form-control input-sm cargo-amount" placeholder="数量" style="text-align: right;"/>
						<span class="input-group-addon">件</span>
					</div>
				</div>
				<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 20%;">
					<div class="input-group input-group-sm">
						<input type="text" class="form-control input-sm cargo-weight" placeholder="重量" style="text-align: right;"/>
						<span class="input-group-addon">吨</span>
					</div>
				</div>
				<div class="btn-group btn-group-sm">
					<button type="button" onclick="onAddCargo(this)" class="btn btn-default">
						<span class="glyphicon glyphicon-plus-sign"></span>
					</button>
					<button type="button" onclick="onRemoveCargo(this)" class="btn btn-default">
						<span class="glyphicon glyphicon-minus-sign"></span>
					</button>
				</div>
			</div>
			<%}%>
			<%if (order.getOrderType() == OrderType.RoundTrip || order.getOrderType() == OrderType.OreOnly) {%>
			<div class="row hr inbound"><hr /></div>
			<div class="row inbound" style="padding-top: 5px;">
				<div class="col-xs-2">矿单号：</div>
				<div class="col-xs-4"><input id="oId" type="text" class="form-control input-sm" <%if (order.getoId() != null) { %> value="<%=order.getoId()%>" <%}%>/></div>
			</div>
			<div class="row inbound" style="padding-top: 5px;">
				<div class="col-xs-2">装车点：</div>
				<div class="col-xs-4"><input type="text" class="form-control input-sm place" id="oreSource" placeholder="装车点" /></div>
				<div class="col-xs-2">矿物：</div>
				<div class="col-xs-4"><input type="text" class="form-control input-sm place" id="ore" placeholder="矿物" /></div>
			</div>
			<div class="row inbound" style="padding-top: 5px;">
				<div class="col-xs-2">矿重：</div>
				<div class="col-xs-4"><input id="oreWeight" type="text" class="form-control input-sm" value="<%=order.getOreWeight()%>" /></div>
				<div class="col-xs-2">韶钢矿重：</div>
				<div class="col-xs-4"><input id="oreFinalWeight" type="text" class="form-control input-sm" value="<%=order.getFinalOreWeight()%>" /></div>
			</div>
			<%} else {%>
			<div class="row hr inbound"><hr /></div>
			<div class="row inbound" style="padding-top: 5px;">
				<div class="col-xs-2">矿单号：</div>
				<div class="col-xs-4"><input id="oId" type="text" class="form-control input-sm" /></div>
			</div>
			<div class="row inbound" style="padding-top: 5px;">
				<div class="col-xs-2">装车点：</div>
				<div class="col-xs-4"><input type="text" class="form-control input-sm place" id="oreSource" placeholder="装车点" /></div>
				<div class="col-xs-2">矿物：</div>
				<div class="col-xs-4"><input type="text" class="form-control input-sm place" id="ore" placeholder="矿物" /></div>
			</div>
			<div class="row inbound" style="padding-top: 5px;">
				<div class="col-xs-2">矿重：</div>
				<div class="col-xs-4"><input id="oreWeight" type="text" class="form-control input-sm" /></div>
				<div class="col-xs-2">韶钢矿重：</div>
				<div class="col-xs-4"><input id="oreFinalWeight" type="text" class="form-control input-sm" /></div>
			</div>
			<%} %>
		<div>
	</div>
		</div>
		<div class="col-xs-4" style="max-height:400px; overflow-y: auto;">
			<div class="container-fluid">
				<div class="col-xs-12">
				<%if (order.getImage() == null || order.getImage().size() == 0) { %>
					<div>无图片信息</div>
				<%} else { %>
				<%for (Image image : order.getImage()) {%>
					<%
					if (image.getData() == null /* || !image.isReady() */ || (image.getType() != null && !image.getType().equals("货运信息"))) {
						continue;
					}
					%>
				<%String base64 = new String(image.getData());%>
					<div class="thumbnail" id="img_<%=image.getId()%>" >
						<img alt="" src="<%=base64%>"/>
						<div class="caption">
							<p><%if (image.getType() != null) { %><%=image.getType()%><%} %></p>
						</div>
					</div>
				<%}}%>
				</div>
			</div>
		</div>
	</div>
</div>
<%} else if (order.getStatus() == OrderStatus.ExpensesVerificationPending || order.getStatus() == OrderStatus.ExpensesVerificationFailed) { %>
<div class="container-fluid" style="font-size: 12px;">
	<div>
		<h4>运费信息</h4>
		<hr />
	</div>
	<div class="row">
		<div class="col-xs-8" style="max-height: 400px; overflow-y: auto;">
			<%if(order.getTolls() != null && !order.getTolls().isEmpty()) { %>
				<%for(Toll toll : order.getTolls()) { %>
				<div class="row toll" style="padding-top: 5px;">
					<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;"><p class="text-center">路费：</p></div>
					<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">
						<div class="input-group input-group-sm">
							<span class="input-group-addon">起</span>
							<input type="text" class="form-control input-sm tollstation" placeholder="入口"/>
						</div>
					</div>
					<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">
						<div class="input-group input-group-sm">
							<span class="input-group-addon">止</span>
							<input type="text" class="form-control input-sm tollstation" plateholder="出口"/>
						</div>
					</div>
					<div class="col-xs-2" style="padding-left: 2px; padding-right: 2px; width: 18%;">
						<div class="input-group input-group-sm">
							<span class="input-group-addon">￥</span>
							<input type="text" class="form-control input-sm toll-amount" style="text-align: right;" value="<%=String.format("%.2f", toll.getAmount())%>"/>
						</div>
					</div>
					<div class="col-xs-1 btn-group btn-group-sm" data-toggle="buttons" style="padding-left: 2px; padding-right: 2px;">
						<label class="btn btn-default <%if(toll.isLoading()) { %> active <%} %>">
							<input type="checkbox" name="isLoading" autocomplete="off"/>载重
						</label>
					</div>
					<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px; width: 15%;">
						<button type="button" onclick="onAddToll(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-plus-sign"></span>
						</button>
						<button type="button" onclick="onRemoveToll(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-minus-sign"></span>
						</button>
					</div>
				</div>
				<%} %>
				<%} else { %>
				<div class="row toll" style="padding-top: 5px;">
					<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;"><p class="text-center">路费：</p></div>
					<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">
						<div class="input-group input-group-sm">
							<span class="input-group-addon">起</span>
							<input type="text" class="form-control input-sm tollstation" placeholder="入口"/>
						</div>
					</div>
					<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">
						<div class="input-group input-group-sm">
							<span class="input-group-addon">止</span>
							<input type="text" class="form-control input-sm tollstation" placeholder="出口"/>
						</div>
					</div>
					<div class="col-xs-2" style="padding-left: 2px; padding-right: 2px; width: 18%;">
						<div class="input-group input-group-sm">
							<span class="input-group-addon">￥</span>
							<input type="text" class="form-control input-sm toll-amount" style="text-align: right;" />
						</div>
					</div>
					<div class="col-xs-1 btn-group btn-group-sm" data-toggle="buttons" style="padding-left: 2px; padding-right: 2px;">
						<label class="btn btn-default">
							<input type="checkbox" name="isLoading" autocomplete="off"/>载重
						</label>
					</div>
					<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px; width: 15%;">
						<button type="button" onclick="onAddToll(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-plus-sign"></span>
						</button>
						<button type="button" onclick="onRemoveToll(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-minus-sign"></span>
						</button>
					</div>
				</div>
				<%} %>
				<%if(order.getFines() != null) { %>
					<hr />
					<div class="row fine" <%if (!order.getFines().isEmpty()) { %> style="display: none;" <%} %>>
						<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;">罚单：</div>
						<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px;">
							<button type="button" onclick="onAddFine(this)" class="btn btn-default">
								<span class="glyphicon glyphicon-plus-sign"></span>
							</button>
						</div>
					</div>
				<%for(Fine fines : order.getFines()) { %>
					<div class="row fine" style="padding-top: 5px;">
						<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;">罚单：</div>
						<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">
							<div class="input-group input-group-sm">
							<span class="input-group-addon">类型</span>
							<input type="text" class="form-control input-sm" value="<%= fines.getType() %>"/>
						</div>
					</div>
					<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">
						<div class="input-group input-group-sm">
							<span class="input-group-addon">￥</span>
							<input type="text" class="form-control input-sm" style="text-align: right;" value="<%=fines.getAmount()%>"/>
						</div>
					</div>
					<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px; width: 15%;">
						<button type="button" onclick="onAddFine(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-plus-sign"></span>
						</button>
						<button type="button" onclick="onRemoveFine(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-minus-sign"></span>
						</button>
						</div>
					</div>
				<%} %>
				<%} %>
				<%if(order.getExpenses() != null) { %>
					<hr />
					<div class="row expenses" <%if (!order.getExpenses().isEmpty()) { %> style="display: none;" <%} %>>
						<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;">其它：</div>
						<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px;">
							<button type="button" onclick="onAddExpenses(this)" class="btn btn-default">
								<span class="glyphicon glyphicon-plus-sign"></span>
							</button>
						</div>
					</div>
				<%for(Expenses expenses : order.getExpenses()) { %>
					<div class="row expenses" style="padding-top: 5px;">
						<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;"><%= expenses.getType() %>：</div>
						<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">
							<div class="input-group input-group-sm">
							<span class="input-group-addon">类型</span>
							<input type="text" class="form-control input-sm" value="<%= expenses.getDescription() %>"/>
						</div>
					</div>
					<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">
						<div class="input-group input-group-sm">
							<span class="input-group-addon">￥</span>
							<input type="text" class="form-control input-sm" style="text-align: right;" value="<%=expenses.getAmount()%>"/>
						</div>
					</div>
					<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px; width: 15%;">
						<button type="button" onclick="onAddExpenses(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-plus-sign"></span>
						</button>
						<button type="button" onclick="onRemoveExpenses(this)" class="btn btn-default">
							<span class="glyphicon glyphicon-minus-sign"></span>
						</button>
						</div>
					</div>
				<%} %>
				<%} %>
		</div>
		<div class="col-xs-4" style="max-height: 400px; overflow-y: auto;">
			<div class="container-fluid">
				<div class="col-xs-12">
				<%if (order.getImage() == null || order.getImage().size() == 0) { %>
					<div>无图片信息</div>
				<%} else { %>
				<%for (Image image : order.getImage()) {%>
					<%
					if (image.getData() == null /* || !image.isReady() */ || (image.getType() != null && !image.getType().equals("费用信息"))) {
						continue;
					}
					%>
				<%String base64 = new String(image.getData());%>
					<div class="thumbnail" id="img_<%=image.getId()%>">
						<img src="<%=base64%>" title="<%=image.getType()%>" />
					</div>
				<%}}%>
				</div>
			</div>
		</div>
	</div>
</div>
<%} %>

	<hr />
	<div class="pull-right">
		<button id="button_submit" type="button" title="审核通过" class="btn btn-success btn-sm" onclick="onVerify()">审核通过</button>
		<button id="button_reject" type="button" title="审核失败" class="btn btn-danger btn-sm" onclick="onReject()">审核失败</button>
	</div>

	
	<div id="confirm" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header" style="background-color: #555;">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="color: ivory;">&times;</button>
					<h4 class="modal-title" style="color: ivory;">确认审核结果</h4>
				</div>
				<div class="modal-body">
					<input class="form-control" id="reason" placeholder="审核失败理由" autofocus/>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger btn-sm" onclick="onConfirmRejection()">审核失败</button>
					<!-- <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button> -->
				</div>
			</div>
		</div>
	</div>
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

		$("#motorcade").select2('data', { id: '<%=order.getTruck().getMotorcade().getId()%>', text: '<%=order.getTruck().getMotorcade().getName()%>' });
		$("#driver").select2('data', { id: '<%=order.getDriver().getId()%>', text: '<%=order.getDriver().getName()%>' });
		$("#plate").select2('data', { id: '<%=order.getTruck().getId()%>', text: '<%=order.getTruck().getPlate()%>' });
		$("#client").select2('data', { id: '<%=order.getClient().getId()%>', text: '<%=order.getClient().getName()%>' });
		<%if (order.getCargoSource() != null) {%>
		$("#cargoSource").select2('data', { id: '<%=order.getCargoSource().getId()%>', text: '<%=order.getCargoSource().getName()%>'});
		<%}%>
		<%if (order.getCargoDestination() != null) {%>
		$("#cargoDestination").select2('data', { id: '<%=order.getCargoDestination().getId()%>', text: '<%=order.getCargoDestination().getName()%>'});
		<%}%>
		<%if (order.getOreSource() != null) {%>
		$("#oreSource").select2('data', { id: '<%=order.getOreSource().getId()%>', text: '<%=order.getOreSource().getName()%>'});
		<%}%>
		<%if (order.getOre() != null) {%>
		$("#ore").select2('data', { id: '<%=order.getOre().getId()%>', text: '<%=order.getOre().getName()%>'});
		<%}%>

		<%if (order.getCargo() != null) {%>
		var i = 1;
		<%for (Cargo cargo : order.getCargo()) {%>
			$(".product:eq(" + i + ")").select2('data', { id: '<%=cargo.getProduct().getId()%>', text: '<%=cargo.getProduct().getName()%>'});
			i += 2;
		<%}%>
		<%}%>

		<%if (order.getOrderType() == OrderType.CargoOnly) {%>
			$(".inbound").hide();
		<%} else if (order.getOrderType() == OrderType.OreOnly) {%>
			$(".outbound").hide();
		<%}%>

		<%if (order.getTolls() != null) {%>
		var i = 1;
		<%for (Toll toll : order.getTolls()) {%>
			$(".tollstation:eq(" + i + ")").select2('data', { id: '<%=toll.getEntry().getId()%>', text: '<%=toll.getEntry().getName()%>'});
			$(".tollstation:eq(" + (i + 2) + ")").select2('data', { id: '<%=toll.getExit().getId()%>', text: '<%=toll.getExit().getName()%>'});
			i += 4;
		<%}%>
		<%}%>

		<%if (order.getImage() != null) {%>
		<%for (Image image : order.getImage()) {%>
			var image_id = '<%=image.getId()%>';
			var img = $("#img_" + image_id);
			<% 
			String data = new String(image.getData());
			int index = data.indexOf("data:image/jpeg;base64,");
			if (index != -1) {
				data = data.substring(index + "data:image/jpeg;base64,".length());
			}
			%>
			if (img.length != 0) {
				img.popover({
					placement : 'bottom',
					html : true,
					trigger : 'hover',
					content : function() {
						return '<img />';
					},
				});
			}
		<%}%>
		<%}%>

		$('input[name="type"]').change( function() {
			if ($(this).val() == 'RoundTrip') {
				$(".inbound").show();
				$(".outbound").show();
			} else if ($(this).val() == 'CargoOnly') {
				$(".inbound").hide();
				$(".outbound").show();
			} else if ($(this).val() == 'OreOnly') {
				$(".inbound").show();
				$(".outbound").hide();
			}
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

		var flag = true;
		var type = $("#order-type .active input").val();
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
	
			$(".cid-row").each(function(index, value) {
				if (!flag) {
					return false;
				}
				var cId = $(value).find("input");
				if (cId.val() == '') {
					cId.focus();
					flag = false;
					return false;
				}
				$(value).nextUntil('.cid-row, .hr').each(function(index2, value2) {
					var name = $(value2).find(".product");
					var amount = $(value2).find(".cargo-amount");
					var weight = $(value2).find(".cargo-weight");
	
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
			var toll = $(value).find('.toll-amount');

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

		$('.fine').each(function(index, value) {
			var type = $(value).find('input:first');
			if (type.val() == '') {
				type.focus();
				flag = false;
				return false;
			}

			var amount = $(value).find('input:eq(1)');
			if (amount.val() == '') {
				amount.focus();
				flag = false;
				return false;
			}
		});

		if (!flag) {
			return false;
		}

		$('.expenses').each(function(index, value) {
			var type = $(value).find('input:first');
			if (type.val() == '') {
				type.focus();
				flag = false;
				return false;
			}

			var amount = $(value).find('input:eq(1)');
			if (amount.val() == '') {
				amount.focus();
				flag = false;
				return false;
			}
		});

		return flag;
	}

	function onVerify() {
		if (!validate()) {
			return;
		}

		var type = $("#order-type .active input").val();
		var data = {};
		data.type = type;
		data.motorcade = $("#motorcade").select2('data').text;
		data.truck = $("#plate").select2('data').text;
		data.driver = $("#driver").select2('data').id;
		data.client = $("#client").select2('data').text;
		
		var cargoInfo = {};
		if (type != 'OreOnly') {
			if ($("#has_dId").is(":checked")) {
				cargoInfo.dId = $("#dId").val();
			}
			cargoInfo.cargoSource = $("#cargoSource").select2('data').text;
			cargoInfo.cargoDestination = $("#cargoDestination").select2('data').text;
			cargoInfo.cargo = [];
			$(".cid-row").each(function(index, value) {
				var cargo = {};
				var cId = $(value).find("input").val();
				cargo.cId = cId;
				var products = [];
				$(value).nextUntil('.cid-row, .hr').each(function(index2, value2) {
					var product = {};
					var name = $(value2).find(".product").select2('data').text;
					var amount = $(value2).find(".cargo-amount").val();
					var weight = $(value2).find(".cargo-weight").val();
					product.name = name;
					product.amount = amount;
					product.weight = weight;
	
					products.push(product);
				});
				cargo.products = products;
				cargoInfo.cargo.push(cargo);
			});

			/* cargoInfo.images = [];
			$("#outboundImageContainer").find("img").each(function (index, value){
				cargoInfo.images.push($(value).attr('src'));
			}); */
			data.cargoInfo = cargoInfo;
		}

		var oreInfo = {};
		if (type != 'CargoOnly') {
			oreInfo.oId = $("#oId").val();
			oreInfo.oreSource = $("#oreSource").select2('data').text;
			oreInfo.ore = $("#ore").select2('data').text;
			oreInfo.oreWeight = $("#oreWeight").val();
			oreInfo.oreFinalWeight = $("#oreFinalWeight").val();

			/* oreInfo.images = [];
			$("#inboundImageContainer").find("img").each(function (index, value){
				oreInfo.images.push($(value).attr('src'));
			}); */

			data.oreInfo = oreInfo;
		}
		

		var toll = [];
		$('.toll').each(function(index, value) {
			var t = {};
			t.entry = $(value).find(".tollstation:eq(0)").select2('data').text;
			t.exit = $(value).find(".tollstation:eq(2)").select2('data').text;
			t.amount = $(value).find(".toll-amount").val();
			t.isLoading = false;
			if ($(value).find(".active input").length > 0) {
				t.isLoading = true;
			}
			toll.push(t);

			data.tollInfo = toll;
		});

		var expensesInfo = {};
		var fine = [];
		$('.fine:gt(0)').each(function(index, value) {
			var f = {};
			var amount = $(value).find("input:eq(1)").val();
			f.amount = amount;
			f.type = $(value).find("input:eq(0)").val();

			if (amount == '') {
				return false;
			}
			fine.push(f);
		});
		if (fine.length > 0) {
			data.fineInfo = fine;
		}

		var expenses = [];
		$('.expenses:gt(0)').each(function(index, value) {
			var e = {};
			var amount = $(value).find("input:eq(1)").val();
			e.amount = amount;
			e.type = $(value).find("input:eq(0)").val();
			
			expenses.push(e);
		});
		if (expenses.length > 0) {
			data.expensesInfo = expenses;
		}
		

		/* expensesInfo.fine = fine;
		expensesInfo.otherExpenses = expenses;
		expensesInfo.images = [];
		$("#expensesImageContainer").find("img").each(function (index, value){
			expensesInfo.images.push($(value).attr('src'));
		}); */

		$.ajax({
			url : 'verify',
			type : 'post',
			data : {
				action : '<%=Action.OrderUpdateStatus%>',
				id : '<%=request.getParameter("id")%>',
				data : JSON.stringify(data)
			},
			beforeSend : function() {
			},
			success : function (response) {
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				closeDialog('<%=dialogId%>');
				refresh('page_' + '<%=Action.OrderUpdateStatus.getParent()%>', response.data.page);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				showErrorDialog(textStatus);
			}
		});
	}

	function onToggleDId(which) {
		if ($(which).is(":checked")) {
			$("#dId").removeAttr("disabled");
			$("#dId").select();
		} else {
			$("#dId").attr("disabled", "true");
		}
	}

	function onAddCId(which) {
		var html = '<div class="row cid-row outbound" style="padding-top: 5px;">';
		html += '<div class="col-xs-2">出库单：</div>';
		html += '<div class="col-xs-5" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm" placeholder="出库单"/></div>';
		html += '<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px;">';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddCId(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-plus-sign"></span>';
		html += '</button>';
		html += '<button type="button" onclick="onRemoveCId(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-minus-sign"></span>';
		html += '</button>';
		html += '</div>';
		html += '</div>';
		html += '</div>';
		html += '<div class="row product-row outbound" style="padding-top: 5px;">';
		html += '<div class="col-xs-2 col-xs-offset-1" style="width:10%; padding-left: 5px; padding-right: 5px;">货物：</div>';
		html += '<div class="col-xs-4" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></div>';
		html += '<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 16%;">';
		html += '<div class="input-group input-group-sm">';
		html += '<input type="text" class="form-control input-sm cargo-amount" placeholder="数量" style="text-align: right;"/>';
		html += '<span class="input-group-addon">件</span>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 20%;">';
		html += '<div class="input-group input-group-sm">';
		html += '<input type="text" class="form-control input-sm cargo-weight" placeholder="重量" style="text-align: right;" />';
		html += '<span class="input-group-addon">吨</span>';
		html += '</div>';
		html += '</div>';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddCargo(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-plus-sign"></span>';
		html += '</button>';
		html += '<button type="button" onclick="onRemoveCargo(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-minus-sign"></span>';
		html += '</button>';
		html += '</div>';

		var parent = $(which).parent().parent().parent();
		var theOne;
		var theInput;
		if ($(".cid-row").length > 1) {
			var nextSibling = parent.next();
			while (!nextSibling.hasClass('cid-row') && !nextSibling.hasClass('hr')) {
				nextSibling = nextSibling.next();
			}
			
			$(html).insertBefore(nextSibling);
			theOne = nextSibling.prev().find('.product');

			nextSibling = parent.next();
			while (!nextSibling.hasClass('cid-row') && !nextSibling.hasClass('hr')) {
				nextSibling = nextSibling.next();
			}
			theInput = nextSibling.find("input");
		} else {
			$(html).insertAfter('.product-row:last');
			theOne = $(".product:last");
			theInput = $(".cid-row:last input");
		}

		theInput.select();
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
		if (table.find(".cid-row").length == 1) {
			return;
		}
		parent.nextUntil('.cid-row, .hr').remove();
		parent.remove();
	}

	function onReject() {
		$("#confirm").modal();
	}

	function onConfirmRejection() {
		$.ajax({
			url : 'reject',
			type : 'post',
			data : {
				action : '<%=Action.OrderUpdateStatus%>',
				id : '<%=request.getParameter("id")%>',
				reason : $("#reason").val(),
			},
			beforeSend : function() {
				$("#confirm .modal-fotter button").attr("disabled", "true");
				$("#confirm .modal-fotter button").text('处理中...');
			},
			success : function (response) {
				$("#confirm").modal('hide');
				if (response.msg != "成功") {
					showErrorDialog(response.msg);
					return;
				}

				closeDialog('<%=dialogId%>');
				refresh('page_' + '<%=Action.OrderUpdateStatus.getParent()%>', response.data.page);
			},

			error : function (jqXHR, textStatus, errorThrown) {
				$("#confirm").modal('hide');
				showErrorDialog(textStatus);
			}
		});
	}

	function onAddCargo(which) {
		var parent = $(which).parent().parent();
		var html = '<div class="row product-row outbound" style="padding-top: 5px;">';
		html += '<div class="col-xs-2 col-xs-offset-1" style="width:10%; padding-left: 5px; padding-right: 5px;">货物：</div>';
		html += '<div class="col-xs-4" style="padding-left: 5px; padding-right: 5px;"><input type="text" class="form-control input-sm product" placeholder="名称"/></div>';
		html += '<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 16%;">';
		html += '<div class="input-group input-group-sm">';
		html += '<input type="text" class="form-control input-sm" placeholder="数量" style="text-align: right;"/>';
		html += '<span class="input-group-addon">件</span>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-3" style="padding-left: 5px; padding-right: 5px; width: 20%;">';
		html += '<div class="input-group input-group-sm">';
		html += '<input type="text" class="form-control input-sm" placeholder="重量" style="text-align: right;" />';
		html += '<span class="input-group-addon">吨</span>';
		html += '</div>';
		html += '</div>';
		html += '<div class="btn-group btn-group-sm">';
		html += '<button type="button" onclick="onAddCargo(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-plus-sign"></span>';
		html += '</button>';
		html += '<button type="button" onclick="onRemoveCargo(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-minus-sign"></span>';
		html += '</button>';
		html += '</div>';
		html += '</div>';

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
		var parent = $(which).parent().parent();
		var nextSibling = parent.next();
		var previousSibling = parent.prev();
		if ((nextSibling.hasClass("cid-row") && previousSibling.hasClass("cid-row"))
				|| (previousSibling.hasClass("cid-row") && !nextSibling.hasClass('product-row'))) {
			return;
		}
		parent.remove();
	}

	function onAddToll(which) {
		var parent = $(which).parent().parent();
		var html = '<div class="row toll" style="padding-top: 5px;">';
		html += '<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;">路费：</div>';
		html += '<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">';
		html += '<div class="input-group input-group-sm">';
		html += '<span class="input-group-addon">起</span>';
		html += '<input type="text" class="form-control input-sm tollstation"/>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">';
		html += '<div class="input-group input-group-sm">';
		html += '<span class="input-group-addon">止</span>';
		html += '<input type="text" class="form-control input-sm tollstation"/>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-2" style="padding-left: 2px; padding-right: 2px; width: 18%;">';
		html += '<div class="input-group input-group-sm">';
		html += '<span class="input-group-addon">￥</span>';
		html += '<input type="text" class="form-control input-sm toll-amount" style="text-align: right;"/>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-1 btn-group btn-group-sm" data-toggle="buttons" style="padding-left: 2px; padding-right: 2px;">';
		html += '<label class="btn btn-default">';
		html += '<input type="checkbox" name="isLoading" autocomplete="off"/>载重';
		html += '</label>';
		html += '</div>';
		html += '<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px; width: 15%;">';
		html += '<button type="button" onclick="onAddToll(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-plus-sign"></span>';
		html += '</button>';
		html += '<button type="button" onclick="onRemoveToll(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-minus-sign"></span>';
		html += '</button>';
		html += '</div>';
		html += '</div>';

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

		var parent = $(which).parent().parent();
		parent.remove();
	}

	function onAddFine(which) {
		var parent = $(which).parent().parent();
		var html = '<div class="row fine" style="padding-top: 5px;">';
		html += '<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;">罚单：</div>';
		html += '<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">';
		html += '<div class="input-group input-group-sm">';
		html += '<span class="input-group-addon">类型</span>';
		html += '<input type="text" class="form-control input-sm"/>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">';
		html += '<div class="input-group input-group-sm">';
		html += '<span class="input-group-addon">￥</span>';
		html += '<input type="text" class="form-control input-sm" style="text-align: right;"/>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px; width: 15%;">';
		html += '<button type="button" onclick="onAddFine(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-plus-sign"></span>';
		html += '</button>';
		html += '<button type="button" onclick="onRemoveFine(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-minus-sign"></span>';
		html += '</button>';
		html += '</div>';
		html += '</div>';

		$('.fine:first').hide();

		$(html).insertAfter(parent);
		parent.next().find('input:first').focus();
	}

	function onRemoveFine(which) {
		var parent = $(which).parent().parent();
		parent.remove();

		if ($('.fine').length == 1) {
			$('.fine:first').show();
		}
	}

	function onAddExpenses(which) {
		var parent = $(which).parent().parent();
		var html = '<div class="row expenses" style="padding-top: 5px;">';
		html += '<div class="col-xs-1" style="padding-left: 2px; padding-right: 2px; padding-top: 6px;">其它：</div>';
		html += '<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">';
		html += '<div class="input-group input-group-sm">';
		html += '<span class="input-group-addon">类型</span>';
		html += '<input type="text" class="form-control input-sm"/>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-3" style="padding-left: 2px; padding-right: 2px;">';
		html += '<div class="input-group input-group-sm">';
		html += '<span class="input-group-addon">￥</span>';
		html += '<input type="text" class="form-control input-sm" style="text-align: right;"/>';
		html += '</div>';
		html += '</div>';
		html += '<div class="col-xs-2 btn-group btn-group-sm" style="padding-left: 2px; padding-right: 2px; width: 15%;">';
		html += '<button type="button" onclick="onAddExpenses(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-plus-sign"></span>';
		html += '</button>';
		html += '<button type="button" onclick="onRemoveExpenses(this)" class="btn btn-default">';
		html += '<span class="glyphicon glyphicon-minus-sign"></span>';
		html += '</button>';
		html += '</div>';
		html += '</div>';

		$('.expenses:first').hide();

		$(html).insertAfter(parent);
		parent.next().find('input:first').focus();
	}

	function onRemoveExpenses(which) {
		var parent = $(which).parent().parent();
		parent.remove();

		if ($('.expenses').length == 1) {
			$('.expenses:first').show();
		}
	}

	function zoom(id, data) {
		
	}
</script>
</body>
</html>
