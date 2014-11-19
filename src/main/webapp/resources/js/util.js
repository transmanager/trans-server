/**
 * Use along with jquery-ui.js
 */

function checkAll(tableName, checkbox) {
	//$('#' + tableName + " tbody tr :checkbox").prop("checked", false);
	//var rows = $('#' + tableName + " tbody tr");
	var boxes = $('#' + tableName + " tbody tr :checkbox");
	//rows.filter(':checked').prop("checked", true);
	var length = boxes.length;
	for (var i = 0; i < length; i++) {
		if(boxes.eq(i).parent().parent().css('display') !== 'none') {
			boxes[i].checked = checkbox.checked;
		}
	}
}

function checkLength(text, field, min, max) {
	if (text.val().length > max || text.val().length < min) {
		text.addClass("ui-state-error");
		text.focus();
		return false;
	}
	return true;
}

function mask() {
	var height = document.body.clientHeight + 30;
	$("body").append("<div class='dialog_mask'></div>");
	$(".dialog_mask").css("height", height);
	$(".dialog_mask").show();
}

function unmask() {
	$(".dialog_mask").remove();
}

function split(val) {
	return val.split(/,\s*/);
}

function validate(dialogId, inputId, min, max, errorMsg) {
	var dialog = $("#" + dialogId);
	var input = $("#" + inputId);
	
	if (input.val().length > max || input.val().length < min) {
		input.addClass("ui-state-error");
		input.focus();
		dialog.find(".errorTip").show();
		dialog.find("span:first").text(errorMsg);
		return false;
	}
	return true;
}

function validatePasswords(dialogId, pass1, pass2, errorMsg) {
	var dialog = $("#" + dialogId);
	var p1 = $("#" + pass1);
	var p2 = $("#" + pass2);
	if (p1.val() != p2.val()) {
		p2.addClass("ui-state-error");
		p2.focus();
		dialog.find(".errorTip").show();
		dialog.find("span:first").text(errorMsg);
		return false;
	}
	return true;
}

function validateNonEmptySelect(dialogId, selectId, errorMsg) {
	var dialog = $("#" + dialogId);
	var select = $("#" + selectId);
	if(select.val() == null) {
		select.addClass("ui-state-error");
		select.focus();
		dialog.find(".errorTip").show();
		dialog.find("span:first").text(errorMsg);
		return false;
	}
	return true;
}

function validateIp(dialogId, inputId, errorMsg) {
	var input = $("#" + inputId);
	var ip = input.val();
	if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ip)) {  
		return true;  
	}
	
	input.addClass("ui-state-error");
	input.focus();
	$(".errorTip").show();
	$("span:first").text(errorMsg);
	return false;
}

function numberWithCommas(number) {
	number = number.toString();
	var pattern = /(-?\d+)(\d{3})/;
	while(pattern.test(number)) {
		number = number.replace(pattern, "$1,$2");
	}
	return number;
}

function kiloByte(number) {
	var n = (Number(number) / 1024).toFixed(2);
	var result = numberWithCommas(n);
	result += " KB";
	return result;
}

function calculateBroadcast(ip, mask) {
	var ipPiece = ip.split(".");
	var maskPiece = mask.split(".");

	if (ipPiece.length != 4 || maskPiece.length != 4) {
		return "";
	}

	var binIp = 0;
	var binMask = 0;

	for (var i = 0; i < ipPiece.length; i++) {
		binIp += ipPiece[i] * Math.pow(256, 3 - i);
	}
	for (var i = 0; i < maskPiece.length; i++) {
		binMask += maskPiece[i] * Math.pow(256, 3 - i);
	}

	var netAddr = binIp & binMask;
	var broadCasting = netAddr | (~binMask);

	if (broadCasting < 0) {
		broadCasting += Math.pow(2, 32);
	}

	var result = "";
	for (var i = 0; i < 4; i++) {
		var q = parseInt(broadCasting / Math.pow(256, 3 - i));
		broadCasting %= Math.pow(256, 3 - i);
		if (result == "") {
			result = String(q);
		} else {
			result += "." + String(q);
		}
	}
	
	return result;
}

function dismiss(dialogId) {
	$("input").removeClass("ui-state-error");
	$(".errorTip").hide();
}

function escapeJavaScript(original) {
	if (original == null || original == 'undefined') {
		return '';
	}
	return original.replace(/\\/g, "\\\\").replace(/"/g, "&quot;").replace(/'/g, "\\\'").replace("/<%/g", "&lt;%").replace("/%>/g", "%\\>");
}

function resize(id) {
	var dialog = $("#" + id + "_frame");
	var iframe = window.document.getElementById(id + "_frame");
	var height = 200;
	
	if (iframe.contentDocument) {
		height = iframe.contentDocument.getElementById("body").scrollHeight;
	} else if (iframe.contentWindow) { 
		height = iframe.contentWindow.document.getElementById("body").scrollHeight;
	}
	
	var clientHeight = window.top.document.getElementById("bottom").offsetTop;
	if (height < clientHeight - 100) {
		dialog.css("height", height);
	} else {
		dialog.css("height", clientHeight - 100);
	}
}

function iframeLoaded(id) {
	var iframe = window.document.getElementById(id + "_frame");
	setTimeout('resize(\'' + id + '\')', 50);
	//resize(id);
	//iframe.contentWindow.onresize = resize(id);
}

function showDialog(url, title, open, size) {
	var date = new Date();
	var timestamp = date.getTime();
	var id = 'dialog' + timestamp;
	if (typeof(size) == 'undefined') {
		size = 'modal-sm';
	} else {
		if (size != 'modal-sm' && size != 'modal-lg') {
			size = "";
		}
	}
	
	var html = '<div id="' + id + '" class="modal fade">';
	html += '<div class="modal-dialog';
	if (size != '') {
		html += ' ' + size;
	}
	html += '">';
	html += '<div class="modal-content">';
	html += '<div class="modal-header" style="background-color: #555;">';
	html += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="color: ivory;">&times;</button>';
	html += '<h4 class="modal-title" style="color: ivory;">' + title + '</h4>';
	html += '</div>';
	html += '<div class="modal-body">';
//	html += '<div class="embed-responsive embed-responsive-4by3">';
	html += '<iframe id="' + id + '_frame" onload="iframeLoaded(\'' + id + '\')" style="border: 0; width: 100%; height: 10px;" src="' + url;
	if (url.indexOf('?') == -1) {
		html += "?";
	} else {
		html += "&";
	}
	html += "dialogId=" + id +'"/>';
//	html += '</div>';
	html += '</div>';
	html += '</div>';
	html += '</div>';
	html += '</div>';
	
	window.top.$('#dialog_dock').append(html);
	
	var dialog = window.top.$("#" + id);
	dialog.modal({
		show : open,
	}).on("hidden.bs.modal", function(e) {
		$(this).removeClass( 'fv-modal-stack' );
        window.top.$('body').data( 'fv_open_modals', $('body').data( 'fv_open_modals' ) - 1 );
		window.top.$("#" + id).remove();
	}).on("shown.bs.modal", function(e) {
		if (typeof ($('body').data('fv_open_modals')) == 'undefined') {
			$('body').data('fv_open_modals', 0);
		}

		// if the z-index of this modal has been set, ignore.
		if ($(this).hasClass('fv-modal-stack')) {
			return;
		}

		$(this).addClass('fv-modal-stack');
		window.top.$('body').data('fv_open_modals', $('body').data('fv_open_modals') + 1);

		$(this).css('z-index', 1040 + (10 * $('body').data('fv_open_modals')));
		window.top.$('.modal-backdrop').not('.fv-modal-stack').css('z-index', 1039 + (10 * $('body').data('fv_open_modals')));
		window.top.$('.modal-backdrop').not('fv-modal-stack').addClass('fv-modal-stack');
	});
	
	return id;
}

function closeDialog(id) {
	var dialog = window.top.$("#" + id);
	dialog.modal("hide");
}

function showErrorDialog(msg) {
	var dialog = window.top.$("#error_dialog");
	var p = dialog.find("p");
	p.empty();
	p.append(msg);
	dialog.modal("show");
}

function refresh(id, page) {
	var tabId = '#' + id;
	var tab = window.top.$(tabId);
	var iframe = tab.find("iframe")[0].contentWindow;
	if (typeof(page) == 'undefined') {
		page = iframe.$("table")[0].config.pager.page;
	}
	iframe.$(".gotoPage").trigger("pageSet", page + 1);
}

function showAlert(msg, autoClose, delay) {
	$(".errorTip span").text(msg);
	$(".errorTip").show();
	if (autoClose) {
		setTimeout(function() {
			$(".errorTip").hide();
		}, delay);
	}
}

function refreshWindow(id) {
	var tabId = '#' + id;
	var content = window.top.$(tabId).find('.tab')[0].contentWindow;
	content.location.reload();
}


/**
 * Use this along with tablesorter pager.
 * Declare currentPage in advance.
 */
jQuery.fn.integerOnly = function() {
	return this.each (function() {
		$(this).keypress(function (e) {
			if (e.which == 13) {
				e.preventDefault();
				if (currentPage == $('.gotoPageShadow').val() - 1) {
					return;
				}
				currentPage = $('.gotoPageShadow').val() - 1;
				$(".gotoPage").trigger('pageSet', currentPage + 1);
			} else if (!(e.which == 8 || e.which == 9
					|| (e.which >= 48 && e.which <= 57)
				)) {
				e.preventDefault();
			}
		});
	}).bind('cut copy paste', function(e) {
		e.preventDefault();
	});
};

function toggleFilter() {
	var v = undefined;
	if ($("#table-sticky").css("visibility") != 'visible') {
		v = $("#table").find(".tablesorter-filter-row");
	} else {
		v = $("#table-sticky").find(".tablesorter-filter-row");
	}
	
	if (v != undefined) {
		v.toggleClass("hideme");
		if (!v.hasClass("hideme")) {
			v.find("input:first").focus();
		}
	}
}

String.prototype.addSlashes = function() { 
   //no need to do (str+'') anymore because 'this' can only be a string
   return this.replace(/[\\"']/g, '\\$&').replace(/\u0000/g, '\\0');
}

// function showDialog(msg, title) {
//	var dialog = window.top.$("#popup");
//	if (title != 'undefined') {
//		dialog.dialog("option", "title", title);
//	}
//	dialog.empty();
//	dialog.dialog("option", "dialogClass", null);
//	alert(msg);
//	dialog.append(msg);
//	dialog.dialog("open");
//}

//function showWaitingDialog(msg) {
//	var dialog = window.top.$("#popup");
//	dialog.empty();
//	dialog.dialog("option", "dialogClass", 'no-close');
//	dialog.append(msg);
//	dialog.dialog("open");
//}

//function getDialog(url, action) {
//	var dialog = window.top.$("#popup");
//	$.ajax({
//		url : url,
//		data : {'action': action},
//		type : 'post',
//		beforeSend : function() {
//			dialog.dialog("option", "title", '请稍候');
//			dialog.dialog("option", "width", '320');
//			dialog.dialog("option", "height", '240');
//			showWaitingDialog('加载中...');
//		},
//		success : function(response) {
//			var msg = response.msg;
//			var title = response.data.title;
//			if (msg != '成功') {
//				title = '抱歉';
//				showDialog(msg, title);
//				return;
//			}
//			var style = response.data.style;
//			var body = response.data.body;
//			
//			var height = response.data.option.height;
//			var width = response.data.option.width;
//			
//			if (width != 'undefined') {
//				dialog.dialog("option", "width", width);
//			}
//			
//			if (height != 'undefined') {
//				dialog.dialog("option", "height", height);
//			}
//			
//			showDialog(body, title, style);
//		},
//		error : function(XMLHttpRequest, status, error) {
//			var msg = '我们遇到错误，无法继续。错误信息：' + error + '。';
//			showDialog(msg, '抱歉');
//		}
//	});
//}
