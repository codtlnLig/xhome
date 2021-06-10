
/*
 * jquery  下拉列表框  陈晓伟
 * Copyright (c) 2012 softweare.net
 * Date: 2012-07-14
 */
 var mouseInComponent;
(function ($) {
	$.fn.multiselect = function (param) {
		var defaults = {evenRowClass:"evenRow", oddRowClass:"oddRow", activeRowClass:"activeRow"};
		var param = $.extend(defaults, param);
		this.each(function () {
			$(param.renderTo).empty();
			if (param.multiable === undefined) {
				param.multiable = true;
			}
			var paraName = param.name;
			param.name = param.name.replace(/\./g, "__");
			var width = param.width + "";
			if (width.indexOf("%") < 0) {
				width = width + "px";
			}
			var html = "<div class='MultiSelect' style=\"width:" + width + ";max-width:" + param.width + "px;\" onclick=\"" + "$('#" + param.name + "InputTxt').select();" + "\">";
			html = html + "<ul class='MultiSelectPanel' multiable='" + param.multiable + "' id=\"" + param.name + "Panel\" style=\" ;max-width: " + param.width + "; \"></ul>";
			html = html + "<input class='MultiSelectInput' id=\"" + param.name + "InputTxt\"  type=\"text\"  size=\"2\" onkeydown=\"_changeLength(event,this)\" title='" + param.name + "'  onkeyup=\"_choise(this)\" />";
			html = html + "</div>";
			html = html + "<a  title=\"Show List\" id=\"" + param.name + "AddBtn\" onclick=\"return false;\" class=\"addBtn\" ></a>";
			html = html + "<div style='position: relative;width: 100%;text-align:left;'>";
			html = html + "<div class='itemPanel' style=';width: " + width + "' id=\"" + param.name + "List\">";
			html = html + "<ul  class=\"itemList\" id=\"" + param.name + "Items\" >";
			html = html + "</ul>";
			html = html + "</div>";
			html = html + "</div>";
			$(this).html(html);
			if (param.url) {
				var url = param.url;
				if (url.indexOf("?") < 0) {
					url = url + "?ts=" + new Date().getTime();
				} else {
					if (url.indexOf("?") > 0) {
						url = url + "&ts=" + new Date().getTime();
					}
				}
				$.ajax({url:url, dataType:"json", success:function (data) {
					if (data.success) {
						var modules = data.result;
						_appendData(modules, param);
						try {
							param.callback;
						}
						catch (e) {
						}
					} else {
						alert(data.message);
					}
				}});
			} else {
				_appendData(param.data, param);
			}
		});
	};
})(jQuery);

function _appendData(modules, param) {
	var lis = "";
	for (var i = 0; i < modules.length; i = i + 1) {
		lis = lis + "<li  class='item'><a   id='" + modules[i].id + "' nullable='" + param.nullable + "'  name='" + param.name + "'  onclick=\"_addItem(this)\">" + modules[i].title + "</a></li>";
	}
	$("#" + param.name + "Items").html(lis);
	$("#" + param.name + "Items").children("li").each(function (e) {
		$(this).children("a").each(function () {
			if (param.value instanceof Array) {
				for (var k = 0; k < param.value.length; k++) {
					if (this.id == param.value[k]) {
						this.click();
					}
				}
			} else {
				if (this.id == param.value) {
					this.click();
				}
			}
			if (param.onchange) {
				$(this).click(param.onchange);
			}
		});
	});
	if (modules.length > 10) {
		$("#" + param.name + "Items").css("max-height", "250px");
		$("#" + param.name + "Items").css("overflow-y", "scroll");
		$("#" + param.name + "Items").css("overflow-x", "hidden");
	}
	$("#" + param.name + "InputTxt").live("blur", function (e) {
		mouseInComponent = false;
		var div = $(this).parent().get(0);
		$(div).removeClass("MultiSelectFocus");
	});
	$("#" + param.name + "InputTxt").live("focus", function (e) {
		var div = $(this).parent().get(0);
		$(div).addClass("MultiSelectFocus");
	});
	$("#" + param.name + "List").live("mouseover", function (e) {
		mouseInComponent = true;
	});
	$("#" + param.name + "List").live("mouseout", function (e) {
		mouseInComponent = false;
	});
	if (param.readonly) {
	} else {
		$("#" + param.name + "AddBtn").live("click", function (e) {
			$("#" + param.name + "List").css("display", "block");
			if (!window.event) {
				$("#" + param.name + "List").css("margin-top", "22px");
			}
			mouseInComponent = true;
			return false;
		});
	}
	$("#" + param.name + "AddBtn").live("blur", function (e) {
		mouseInComponent = false;
	});
	$("body").click(function (e) {
		if (!mouseInComponent) {
			$("#" + param.name + "List").css("display", "none");
		}
	});
	if (param.callback) {
		param.callback();
	}
}
function _addItem(obj) {
	var type = obj.name;
	var html = "<li id='" + obj.id + "' class='MultiSelectItem'>";
	html = html + "<div>" + obj.innerHTML+"</div>";
	if ($(obj).attr("nullable")==true) {
		html = html + "<div class='clsBtn' title='" + obj.innerHTML + "' name='" + obj.id + "' type='" + type + "' onclick='_deleteItem(this)'></div>";
	}else{
		html = html + "<div class='right'></div>";
	}
	html = html + "<input type='hidden' name='" + (type.replace(/__/g, ".")) + "' id='" + (type.replace(/__/g, "_")) + "' value='" + obj.id + "'/>";
	html = html + "</li>";
	if ($("#" + type + "Panel").attr("multiable") != "true") {
		$("#" + type + "Panel").empty();
		$(html).appendTo("#" + type + "Panel");
	} else {
		$(html).appendTo("#" + type + "Panel");
		$("#" + type + "Items").children("li").each(function () {
			if ($(this).children("a")[0].id == obj.id) {
				document.getElementById(type + "Items").removeChild(this);
			}
		});
	}
	$("#" + type + "List").css("display", "none");//hide list
	$("#"+type + "InputTxt").val("");
	$("#" + type + "Items").children("li").each(function () {
		$(this).css("display", "block");
	});
}
function _deleteItem(obj) {
	var type = $(obj).attr("type");
	
	$("#" + type + "Panel").children("li").each(function () {
		if (this.id == $(obj).attr("name")) {
			$(this).remove();
		}
	});
	if ($("#" + type + "Panel").attr("multiable") != "true") {
	} else {
		var name = obj.title.replace(/\</g, "&lt;").replace(/\>/g, "&gt;");
		var html = "<li   class='item' ><a id='" + obj.name + "' name='" + type + "' onclick='_addItem(this)'>" + name + "</a></li>";
		$(html).appendTo("#" + type + "Items");
	}
}