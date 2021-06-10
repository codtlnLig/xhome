var Roles = {
	'管理员' : 1,
	'注册企业用户' : 2
};

$(function() {
	$("#tabs").tabs();
	$(".topMenu").css("display", "block");
	$(".topMenu li a").each(function() {
		$(this).click(function() {
			var href = $(this).attr('href');
			$($(href).find('li').get(0)).click();
		});
	});
	$(".leftPanel ul li").each(function() {
		$(this).click(function() {
			$(".current").each(function() {
				$(this).removeClass("current");
			});
			$(this).addClass("current");
		});
	});
	$(".subMenu li").each(function() {
		$(this).click(function() {
			$(".currentFun").each(function() {
				$(this).removeClass("currentFun");
			});
			$(this).addClass("currentFun");
		});
	});
	$(".leftPanel").each(function() {
		$(this).css("display", "block");
	});

	//弹出窗体代码:*******
	$($(".topMenu li a").get(0)).click();
});

//得到所有被选择的checkbox的值
function getCheckboxSelectItems() {
	var ids = "";
	$(".ids").each(function() {
		if (this.checked == true) {
			ids += "ids=" + this.value + "&";
		}
	});
	if (ids == "") {
		showDialog("\u8bf7\u81f3\u5c11\u9009\u62e9\u4e00\u6761\u8bb0\u5f55");
	}
	return ids;
}

//列表页面多记录“删除”
function batchDeleteWidthCallback(type, callback) {
	var ids = getCheckboxSelectItems();
	if (ids == "") {
		return;
	}
	$("#msgDialog").html("<p>确定要删除这些记录吗？</p>");
	$("#msgDialog").dialog( {
		modal : true,
		width : 250,
		height : 160,
		buttons : {
			"确定" : function() {
				$(this).dialog("close");
				$.ajax( {
					url : "comm/delete.do?status=1&type=" + type + "&" + ids,
					type : "get",
					dataType : "json",
					success : function(data) {
						if (data.success) {
							callback();
						} else {
							showDialog("删除失败，原因：" + data.message);
						}
					}
				});
			},
			"取消" : function() {
				$(this).dialog("close");
			}
		},close:function(){$(this).dialog('destroy');}
	});
}

//列表页面多记录“删除”
function batchDeleteWidthCallbackHis(type, stateName, callback) {
	var ids = getCheckboxSelectItems();
	if (ids == "") {
		return;
	}
	$("#msgDialog").html("<p>确定要删除这些记录吗？</p>");
	$("#msgDialog").dialog(
			{
				modal : true,
				width : 250,
				height : 160,
				buttons : {
					"确定" : function() {
						$(this).dialog("close");
						$.ajax( {
							url : "comm/deleteHis.do?stateName=" + stateName
									+ "&status=1&type=" + type + "&" + ids,
							type : "get",
							dataType : "json",
							success : function(data) {
								if (data.success) {
									callback();
								} else {
									showDialog("删除失败，原因：" + data.message);
								}
							}
						});
					},
					"取消" : function() {
						$(this).dialog("close");
					}
				},close:function(){$(this).dialog('destroy');}
			});
}
function showLoading(msg) {
	var html = "<div id='_juqery_loading' class='loading_bkg'></div>";
	html += "<div id='_jquery_loading_box' class='loading_box'><img src='images/loading.gif'/><span id='_juqery_loading_msg'>"
			+ msg + "</span></div>";
	$(html).appendTo("body");
}

function hideLoading() {
	$("#_juqery_loading").remove();
	$("#_jquery_loading_box").remove();
}

function formatTimestamp(ts) {
	if (ts) {
		var year = 1900 + ts.year;
		var month = ts.month + 1;
		var day = ts.date;
		return year + "-" + month + "-" + day;
	} else {
		return "";
	}
}

function logout() {
	$.post("user/logout.do", function(data) {
		location.href = "login.jsp";
	});
}
function ts() {
	return new Date().getTime();
}
function GetEditorValue(id) {
	var oEditor = CKEDITOR.instances[id];
	$("#" + id).val(oEditor.getData());
}
function setSimpleEditor(id) {
	CKEDITOR.replace(id,
			{
				toolbar : [ [ 'Bold', 'Italic', '-', 'NumberedList',
						'BulletedList', ] ]
			});
}
function SetEditor(id) {
	CKEDITOR.replace(id);
}

function showDialog(msg) {
	$("#msgDialog").html("<p>" + msg + "</p>");
	$("#msgDialog").dialog( {
		modal : true,
		width : 250,
		height : 160,
		resizable : false,
		buttons : {
			"<em class='back'></em>确定" : function() {
				$(this).dialog("close");
			}
		},close:function(){$(this).dialog('destroy');}
	});
}

function showWelcome() {
	$.ajax( {
		url : "welcome.do",
		type : "get",
		dataType : "html",
		success : function(data) {
			$("#mainForm").html(data);

		}
	});
}

function showShi(selectId) {
	$.ajax( {
		url : "dict/shi.do",
		dataType : "json",
		success : function(data) {
			if (data.success) {
				$('#' + selectId).empty();
				var list = data.result;
				for ( var i = 0; i < list.length; i++) {
					$(
							"<option value='" + list[i].value + "'>"
									+ list[i].key + "</option>").appendTo(
							'#' + selectId);
				}
			}
		}
	});
}
function showXian(obj, selectId) {
	var shi = $(obj).find("option[value='" + obj.value + "']").html();
	$.ajax( {
		url : "dict/xian.do",
		type : "post",
		data : {
			'shi' : shi
		},
		dataType : "json",
		success : function(data) {
			if (data.success) {
				$('#' + selectId).empty();
				var list = data.result;
				for ( var i = 0; i < list.length; i++) {
					$(
							"<option value='" + list[i].value + "'>"
									+ list[i].key + "</option>").appendTo(
							'#' + selectId);
				}
			}
		}
	});
}

//进入修改密码页面
function profile() {
	$.ajax( {
		url : "profile.do",
		date : 'get',
		dataType : "html",
		async:true,
		success : function(html) {
			$("#profileDialog").html(html);
			$("#profileDialog").dialog( {
				title : "修改密码",
				modal : true,
			//	draggable: false ,
				width : 600,
				height : 180,
				buttons : {
					'确定' : function() {
						resetPwd();
					},
					'关闭' : function() {
						$(this).dialog('close');
					}
				},close:function(){$(this).dialog('destroy');}
			});

			$("#pwdForm").validate({
				rules : {
					'oldpwd' : {
						required : true,
						rangelength : [ 6, 18 ]
					},
					'password' : {
						required : true,
						rangelength : [ 6, 18 ]
					},
					'confirmPwd' : {
						equalTo : '#password'
					}
				},
				messages : {
					'oldpwd' : {
						required : "请输入您的原密码！",
						rangelength : '请输入长度在{0}-{1}位的密码！'
					},
					'password' : {
						required : "请输入您的新密码！",
						rangelength : '请输入长度在{0}-{1}位的密码！'
					},
					'confirmPwd' : {
						equalTo : "两次输入的密码不同！"
					}
				},
				submitHandler : function(form) {
					form.submit();
				}
			});
		}
	});
}
//重置密码
function resetPwd() {
	$("#pwdForm").ajaxSubmit( {
		url : "resetPwd.do",
		type : "post",
		dataType : "json",
		success : function(data) {
			if (data.success) {
				showDialog("密码修改成功！请谨记您的新密码");
				$("#profileDialog").dialog('close');
			} else {
				alert(data.message);
			}
		}
	});
}
function showLoading(msg) {
	var html = "<div id='_juqery_loading' class='loading_bkg'></div>";
	html += "<div id='_jquery_loading_box' class='loading_box'><img src='images/loading.gif'/><span>"
			+ msg + "</span></div>";
	$(html).appendTo("body");
}

function hideLoading() {
   setTimeout(function(){
	   $("#_juqery_loading").remove();
	$("#_jquery_loading_box").remove();
   },100);	
}

function getRootPath() {
	var pathName = window.location.pathname.substring(1);
	var webName = pathName == '' ? '' : pathName.substring(0, pathName
			.indexOf('/'));
	return window.location.protocol + '//' + window.location.host + '/'
			+ webName + '/';
}
/**
 * 通用附件上传方法,
 * 调用示例：<input type="file" name="file" id='f_m_1' onchange="upload(this,'t_m_1','manager.photo',240,320,'jpg',false,'image')" >
 * 参数说明：
 *	obj:文件选择控件对象，必选项
 *	hiddenTag:隐藏域的name属性值，程序将自动生成一个隐藏域，可选项
 *	viewTag：img标签的ID,如果要在img标签预览图片的话，可以指定这个参数，可选项
 *	width:图片的宽度限制，可选项
 *	height:图片的高度限制，可选项
 *	ext:附件的格式限制，可选项
 *	multible:是否可以上传多个附件
 */
function upload(obj, hiddenTag, viewTag, width, height, ext, multible, folder) {
	var param = "";
	if (width) {
		param += "&width=" + width;
	}
	if (height) {
		param += "&height=" + height;
	}
	if (ext != 'null') {
		var file = $("#" + obj.id).val();
		var parts = file.split(".");
		var fileExt = parts[parts.length - 1];
		if (ext.toLowerCase() != fileExt.toLowerCase()) {
			$.messager.alert('操作失败', "文件格式必需为：" + ext);
			return;
		}
	}
	if (null == folder || '' == folder) {
		folder = "images";
	}
	var parent = $(obj).parent();
	$
			.ajaxFileUpload( {
				url : "file/upload.do?folder=" + folder + param,
				secureuri : false,
				fileElementId : obj.id,
				dataType : "json",
				success : function(data) {
					if (data.success) {
						var fileName = data.attribute["fileName"];
						if (viewTag) {
							try {
								var aid = new Date().getTime();
								//$("#"+viewTag).attr("src","file/download.do?folder="+folder+"&fileFileName="+fileName);//为img标签为src
				var fileId = hiddenTag.substring(hiddenTag.indexOf(".") + 1,
						hiddenTag.length);
				var html = "<div class='fileitemx'><input type='hidden' id='"
						+ fileId
						+ "' name='"
						+ hiddenTag
						+ "' value='"
						+ fileName
						+ "'/><a id='a_"
						+ aid
						+ "' class='fancybox' target='downloadFileIframe' onclick=\"viewImage('"
						+ fileName
						+ "');\">点击预览</a><a class='right' onclick=\"clearFile(this,'"
						+ folder + "','" + fileName + "')\">删除</a></div>";
				if (multible) {
					$(html).appendTo(parent);
				} else {
					parent.find(".fileitemx").remove();
					$(html).appendTo(parent);
				}
			} catch (e) {
				alert(e);
			}
		}
	} else {
		alert('操作失败', "上传失败，原因：" + data.message);
	}
},
error : function(xhr, status, e) {
	alert('操作失败', "上传失败，原因：" + xhr.responseText);
}
			});
}
function clearFile(obj, folder, fileName) {
	$(obj).parent().remove();
	$.ajax( {
		url : "file/removeFile.do?t=" + (new Date()),
		type : 'post',
		data : {
			fileFileName : fileName,
			folder : folder
		},
		dataType : "json",
		success : function(data) {
			if (data.success) {
				alert('删除成功！');
			}
		}
	});
}

function viewImage(loc) {
	$("#msgDialog").html("<div><a href='" + loc+ "' target='_blank'>点击下载</a></div>");
	$("#msgDialog").dialog( {
		title : "下载",
		width : 640,
		height : 400
	});
}

function commmonUpload(obj, hiddenTxt, tag) {
	$.ajaxFileUpload( {
		url : "../file/upload.action?folder=photo",
		secureuri : false,
		fileElementId : obj.attr('id'),
		dataType : "json",
		success : function(data) {
			var fileName = data.attribute["fileName"];
			hiddenTxt.val(fileName);
			if (tag) {
				try {
					tag.attr("src",
							"../file/download.action?folder=photo&fileFileName="
									+ fileName);//为img标签为src
					tag.attr("href",
							"../file/download.action?folder=photo&fileFileName="
									+ fileName);//为a标签加href
					tag.css("display", "block");
					tag.html("点击查看");
				} catch (e) {

				}
			}
		},
		error : function(xhr, status, e) {
			$.messager.alert('error', "上传失败，原因：" + xhr.responseText);
		}
	});
}

/**
 * 通用附件上传方法,
 * 调用示例：<input type="file" name="file" id='f_m_1' onchange="upload(this,'t_m_1','manager.photo',240,320,'jpg',false,'image')" >
 * 参数说明：
 *	obj:文件选择控件对象，必选项
 *	hiddenTag:隐藏域的name属性值，程序将自动生成一个隐藏域，可选项
 *	viewTag：img标签的ID,如果要在img标签预览图片的话，可以指定这个参数，可选项
 *	width:图片的宽度限制，可选项
 *	height:图片的高度限制，可选项
 *	ext:附件的格式限制，可选项
 *	multible:是否可以上传多个附件
 */
function upload(obj, hiddenTag, viewTag, width, height, ext, multible, folder,
		minTag) {
	var param = "";
	if (width) {
		param += "&width=" + width;
	}
	if (height) {
		param += "&height=" + height;
	}
	if (ext != 'null') {
		var file = $("#" + obj.id).val();
		var parts = file.split(".");
		var fileExt = parts[parts.length - 1];
		if (ext.toLowerCase() != fileExt.toLowerCase()) {
			$.messager.alert('操作失败', "文件格式必需为：" + ext);
			return;
		}
	}
	if (null == folder || '' == folder) {
		folder = "images";
	}
	var parent = $(obj).parent();
	$
			.ajaxFileUpload( {
				url : "file/upload.do?folder=" + folder + param,
				secureuri : false,
				fileElementId : obj.id,
				dataType : "json",
				success : function(data) {
					if (data.success) {
						var fileName = data.attribute["fileName"];
						var minfileName = data.attribute["minfileName"];
						if (viewTag) {
							try {
								var aid = new Date().getTime();
								//$("#"+viewTag).attr("src","file/download.do?folder="+folder+"&fileFileName="+fileName);//为img标签为src
				var fileId = hiddenTag.substring(hiddenTag.indexOf(".") + 1,
						hiddenTag.length);
				var html = "<div class='fileitemx'><input type='hidden' id='"
						+ fileId
						+ "min' name='"
						+ minTag
						+ "' value='"
						+ minfileName
						+ "'/><input type='hidden' id='"
						+ fileId
						+ "' name='"
						+ hiddenTag
						+ "' value='"
						+ fileName
						+ "'/><a id='a_"
						+ aid
						+ "' class='fancybox' target='downloadFileIframe' onclick=\"viewImage('"
						+ fileName
						+ "');\">点击预览</a><a class='right' onclick=\"clearFile(this,'"
						+ folder + "','" + fileName + "')\">删除</a></div>";
				if (multible) {
					$(html).appendTo(parent);
				} else {
					parent.find(".fileitemx").remove();
					$(html).appendTo(parent);
				}
			} catch (e) {
				alert(e);
			}
		}
	} else {
		alert('操作失败', "上传失败，原因：" + data.message);
	}
},
error : function(xhr, status, e) {
	alert('操作失败', "上传失败，原因：" + xhr.responseText);
}
			});
}

function uploadPreview(obj, folder, model_imageUrl, width, height, minwidth,
		minheight,limitwidth,limitheight, uploadSpan) {
	if(!checkImgType(obj)){
		return ;
	}
	$("#imgCutPane").empty();
	var html = '<div   style="width:'
			+ width
			+ 'px;height:'
			+ height
			+ 'px;display:inline-block; float:left;  border: 1px rgba(0,0,0,.4) solid;background-color: white;-webkit-border-radius: 6px;-moz-border-radius: 6px;border-radius: 6px;-webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);-moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);"><img id="preview" style="width:'
			+ width + 'px;height:' + height
			+ 'px;display:inline-block; "/></div>';
	html += '<div id="preview-pane" style="width: '+minwidth+'px;height: '+minheight+'px;overflow: hidden;" > <div class="preview-container" style="width: '+minwidth+'px;height: '+minheight+'px;overflow: hidden;"><img id="minImg" class="jcrop-preview"  /></div></div>';
	html += '<input type="hidden" name="x" id="x"/>';
	html += '<input type="hidden" name="y" id="y"/>';
	html += '<input type="hidden" name="w" value="' + minwidth + '" id="w"/>';
	html += '<input type="hidden" name="h" value="' + minheight + '" id="h"/>';
	html += '<input type="hidden" id="model_simageUrl"/>';
	$("#imgCutPane").html(html);
	$("#imgCutPane").height(height + 5).show();
	var previewId = 'preview', minImgId = 'minImg', imageUrl = 'model_simageUrl';
	$.ajaxFileUpload( {
				url : "jcrop/uploadImg.do?folder=" + folder + "&t="
						+ (new Date()),
				type : 'post',
				secureuri : false,
				fileElementId : obj.id,
				dataType : "json",
				data : {
					'folder' : folder,
					'limitwidth':limitwidth,
					'limitheight':limitheight
				},
				success : function(json) {
					if (json.success) {
						var fileName = json.attribute["fileName"];
						var sourceName = json.attribute["fileSourceName"];
						$("#" + previewId).attr("src", fileName).show();
						$("#" + minImgId).attr("src", fileName).show();
						$("#" + imageUrl).val(sourceName);
						$("#" + uploadSpan).empty();
						$("#" + uploadSpan)
								.append(
										"<input type='button' value='上传'  style='width: 90px;' onclick=\"uploadCutImg(this,\'"
												+ imageUrl
												+ "\',\'"
												+ model_imageUrl
												+ "\',"
												+ width
												+ ","
												+ height
												+ ","
												+ minwidth
												+ ","
												+ minheight
												+ ");\" />");
						cut(previewId, minImgId, minwidth, minheight);
					} else {
						alert('操作失败', "上传失败，原因：" + json.message);
					}
				},
				error : function(xhr, status, e) {
					alert('操作失败', "上传失败，原因：" + xhr.responseText);
				}
			});
}

function cut(id, mid, minwidth, minheight) {
	var jcrop_api, boundx, boundy, $preview = $('#preview-pane'), $pcnt = $('#preview-pane .preview-container'), $pimg = $('#preview-pane .preview-container img'), xsize = $pcnt
			.width(), ysize = $pcnt.height();
	$("#" + id).Jcrop( {
		onChange : function(c) {
			if (parseInt(c.w) > 0) {
				$('#x').val(c.x); //c.x 裁剪区域左上角顶点相对于图片左上角顶点的x坐标  
		$('#y').val(c.y); //c.y 裁剪区域顶点的y坐标</span>
		$('#w').val(c.w);
		$('#h').val(c.h);
		var rx = xsize / c.w;
		var ry = ysize / c.h;
		$("#" + mid).css( {
			width : Math.round(rx * boundx) + 'px',
			height : Math.round(ry * boundy) + 'px',
			marginLeft : '-' + Math.round(rx * c.x) + 'px',
			marginTop : '-' + Math.round(ry * c.y) + 'px'
		});
	}
},
onSelect : function(c) {
	if (parseInt(c.w) > 0) {
		var rx = xsize / c.w;
		var ry = ysize / c.h;
		$('#x').val(c.x); //c.x 裁剪区域左上角顶点相对于图片左上角顶点的x坐标  
		$('#y').val(c.y); //c.y 裁剪区域顶点的y坐标</span>
		$('#w').val(c.w);
		$('#h').val(c.h);
		$("#" + mid).css( {
			width : Math.round(rx * boundx) + 'px',
			height : Math.round(ry * boundy) + 'px',
			marginLeft : '-' + Math.round(rx * c.x) + 'px',
			marginTop : '-' + Math.round(ry * c.y) + 'px'
		});
	}
},
aspectRatio : xsize / ysize
	}, function() {
		var bounds = this.getBounds();
		boundx = bounds[0];
		boundy = bounds[1];
		jcrop_api = this;
		jcrop_api.setSelect( [ 0, 0, minwidth, minheight ]);
		jcrop_api.setOptions( {
			bgFade : true
		});
		jcrop_api.ui.selection.addClass('jcrop-selection');
		jcrop_api.setOptions( {
			allowResize : false
		});

		$preview.appendTo(jcrop_api.ui.holder);
	});
}
function uploadCutImg(obj, imageUrl, httpPath, width, height, minwidth,
		minheight) {
	if (imageUrl) {
		var x = $("#x").val();
		var y = $("#y").val();
		var w = $("#w").val();
		var h = $("#h").val();
		$.ajax( {
					url : "jcrop/cutImg.do?t=" + (new Date()),
					type : 'post',
					dataType : "json",
					data : {
						'imageUrl' : $("#" + imageUrl).val(),
						'sw' : width,
						'sh' : height,
						'x' : x,
						'y' : y,
						'w' : w,
						'h' : h
					},
					success : function(json) {
						if (json.success) {
							var fileName = json.attribute["fileName"];

							$("#imgCutPane").empty();
							var html = '<div style="width:'
									+ minwidth
									+ 'px;height:'
									+ minheight
									+ 'px;display:inline-block; float:left;  border: 1px rgba(0,0,0,.4) solid;background-color: white;-webkit-border-radius: 6px;-moz-border-radius: 6px;border-radius: 6px;-webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);-moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);"> <img id="minImg" src="'
									+ fileName + '" style="width:' + minwidth
									+ 'px;height:' + minheight
									+ 'px;display:inline-block; " /></div>';
							$("#imgCutPane").html(html);
							$("#imgCutPane").height(minheight + 5).show();
							$("#" + httpPath).val(fileName);
							var $p=$(obj).parent();
							$(obj).remove();
							$p.empty();
						    $p.append("<input type='button' value='删除'  style='width: 90px;' onclick=\"deleteJcropImg(this,'imgCutPane','"+httpPath+"');\" />");
							
							//alert('上传成功');
						} else {
							alert('操作失败', "上传失败，原因：" + json.message);
						}
					},
					error : function(xhr, status, e) {
						alert('操作失败', "上传失败，原因：" + xhr.responseText);
					}
				});
	} else {
		alert('请先选择要上传的图片');
	}
}

function checkImgType(ths){  
    if (ths.value == "") {
        alert("请上传图片");
        return false;
    } else {
        if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(ths.value)) {  
            alert("图片类型必须是.gif,jpeg,jpg,png中的一种");  
            ths.value = "";  
            return false;  
        }
    }
    return true;  
}
function deleteJcropImg(obj,div,httpPath){
	//没有后台真删除
	$("#"+div).empty().hide();
	$("#" + httpPath).val();
	$(obj).remove();
}

//多图片裁剪上传
function mulUploadPreview2(obj,folder,model_imageUrl,ulListId,width,height,minwidth,minheight,limitwidth,limitheight,uploadSpan,modelName){
	if(!checkImgType(obj)){
		return ;
	}
	$("#imgCutPane").empty();
	var lisize=$("#"+ulListId+" li").size();
	if(lisize<4){
	var html = '<div   style="width:'
			+ (width+4)
			+ 'px;height:'
			+ height
			+ 'px;display:inline-block; float:left;  border: 1px rgba(0,0,0,.4) solid;background-color: white;-webkit-border-radius: 6px;-moz-border-radius: 6px;border-radius: 6px;-webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);-moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);"><img id="preview" style="width:'
			+ width + 'px;height:' + height
			+ 'px;display:inline-block; "/></div>';
	html += '<div id="preview-pane" style="width: '+minwidth+'px;height: '+minheight+'px;overflow: hidden;" > <div class="preview-container"  style="width: '+minwidth+'px;height: '+minheight+'px;overflow: hidden;"><img id="minImg" class="jcrop-preview"/></div></div>';
	html += '<input type="hidden" name="x" id="x"/>';
	html += '<input type="hidden" name="y" id="y"/>';
	html += '<input type="hidden" name="w" value="' + minwidth + '" id="w"/>';
	html += '<input type="hidden" name="h" value="' + minheight + '" id="h"/>';
	html += '<input type="hidden" id="model_simageUrl"/>';
	$("#imgCutPane").html(html);
	$("#imgCutPane").height(height + 5).show();
	var previewId = 'preview', minImgId = 'minImg', imageUrl = 'model_simageUrl';

	$.ajaxFileUpload( {
				url : "jcrop/uploadImg.do?folder=" + folder + "&t="
						+ (new Date()),
				type : 'post',
				secureuri : false,
				fileElementId : obj.id,
				dataType : "json",
				data : {
					'folder' : folder,
					'limitwidth':limitwidth,
					'limitheight':limitheight
				},
				success : function(json) {
					if (json.success) {
						var fileName = json.attribute["fileName"];
						var sourceName = json.attribute["fileSourceName"];
						$("#" + previewId).attr("src", fileName).show();
						$("#" + minImgId).attr("src", fileName).show();
						$("#" + imageUrl).val(sourceName);
						$("#" + uploadSpan).empty();
						$("#" + uploadSpan)
								.append(
										"<input type='button' value='上传'  style='width: 90px;' onclick=\"mulUploadCutImg(this,\'"+ulListId+"\',\'"
												+ imageUrl
												+ "\',\'"
												+ model_imageUrl
												+ "\',"
												+ width
												+ ","
												+ height
												+ ","
												+ minwidth
												+ ","
												+ minheight
												+ ",\'"+modelName+"\');\" />");
						cut(previewId, minImgId, minwidth, minheight);
					} else {
						alert('操作失败', "上传失败，原因：" + json.message);
					}
				},
				error : function(xhr, status, e) {
					alert('操作失败', "上传失败，原因：" + xhr.responseText);
				}
			});}else{alert("最多上传4张图片");};
}
function mulUploadCutImg2(obj,ulListId, imageUrl, httpPath, width, height, minwidth,
		minheight,modelName) {
	if (imageUrl) {
		var x = $("#x").val();
		var y = $("#y").val();
		var w = $("#w").val();
		var h = $("#h").val();
		$.ajax( {
					url : "jcrop/cutImg.do?t=" + (new Date()),
					type : 'post',
					dataType : "json",
					data : {
						'imageUrl' : $("#" + imageUrl).val(),
						'sw' : width,
						'sh' : height,
						'x' : x,
						'y' : y,
						'w' : w,
						'h' : h
					},
					success : function(json) {
						if (json.success) {
							var fileName = json.attribute["fileName"];
							$("#imgCutPane").empty().hide();
							var html = '<li style="float:left; padding:5px; width:'+(minwidth+20)+'px;height:'+(minheight+20)+'px;"><div style="top: 18px; left: 58px; z-index: 11; display: block;-moz-border-bottom-colors: none;-moz-border-left-colors: none;-moz-border-right-colors: none;-moz-border-top-colors: none;border-color: #F9F9F9;border-image: none;border-radius: 4px;border-style: solid;border-width: 7px 7px 7px; box-shadow: 1px 1px 5px #555555;">';
							    html+= '<img style="width:'+minwidth+'px;height:'+minheight+'px;display:inline-block; " src="'+fileName+'">';
							    html+='<input type="hidden" value="'+fileName+'" name="'+modelName+'">';
							    html+='<span class="delete" onclick="removeLi(this)">&nbsp;</span></div></li>';
							$("#"+ulListId).append(html).show();
							$(obj).hide();
							//alert('上传成功');
						} else {
							alert('操作失败', "上传失败，原因：" + json.message);
						}
					},
					error : function(xhr, status, e) {
						alert('操作失败', "上传失败，原因：" + xhr.responseText);
					}
				});
	} else {
		alert('请先选择要上传的图片');
	}
}

/**
 * 初始化开始结束日期组件（两个日期）
 * @param {Object} fromId 开始日期文本框ID
 * @param {Object} toId 结束日期文本框ID
 * @param {Object} defaultFromDate 默认开始日期，格式为yyyy-MM-dd
 * @param {Object} defaultToDate 默认结束日期，格式为yyyy-MM-dd
 * @memberOf {TypeName} 
 */
function initFormToDatepicker(fromId, toId, defaultFromDate, defaultToDate)
{
    var dates = $( "#"+fromId+", #"+toId ).datepicker({
            showButtonPanel:true,
            clearText:"清除",
            showClearButton:true,
            closeText:"关闭",
            showOtherMonths: true,
            selectOtherMonths: true,
            dateFormat:"yy-mm-dd",
            regional:"zh-CN",
            onSelect: function( selectedDate ) {
                var option = this.id == fromId ? "minDate" : "maxDate",
                    instance = $( this ).data( "datepicker" ),
                    date = $.datepicker.parseDate(
                        instance.settings.dateFormat ||
                        $.datepicker._defaults.dateFormat,
                        selectedDate, instance.settings );
                dates.not( this ).datepicker( "option", option, date );
            }
        });
    $("#"+fromId).datepicker("setDate", defaultFromDate);
    $("#"+toId).datepicker("setDate", defaultToDate);
}

/**
 * 初始化开始结束日期组件（两个日期），并限定可选择的最大、最小日期
 * @param {Object} fromId 开始日期文本框ID
 * @param {Object} toId 结束日期文本框ID
 * @param {Object} defaultFromDate 默认开始日期，格式为yyyy-MM-dd
 * @param {Object} defaultToDate 默认结束日期，格式为yyyy-MM-dd
 * @param {Object} beginDate 允许选择的最小日期，格式为yyyy-MM-dd，如果为空，则表示不限制开始时间
 * @param {Object} endDate 允许选择的最大日期，格式为yyyy-MM-dd，如果为空，表示不限制截止时间
 * @memberOf {TypeName} 
 */
function initRangeDatepickerWithMaxMin(fromId, toId, defaultFromDate, defaultToDate, beginDate, endDate)
{
    var dates = $( "#"+fromId+", #"+toId ).datepicker({
            minDate: beginDate,
            maxDate: endDate,
            showButtonPanel:true,
            clearText:"清除",
            showClearButton:true,
            closeText:"关闭",
            showOtherMonths: true,
            selectOtherMonths: true,
            dateFormat:"yy-mm-dd",
            regional:"zh-CN",
            onSelect: function( selectedDate ) {
                var option = this.id == fromId ? "minDate" : "maxDate",
                    instance = $( this ).data( "datepicker" ),
                    date = $.datepicker.parseDate(
                        instance.settings.dateFormat ||
                        $.datepicker._defaults.dateFormat,
                        selectedDate, instance.settings );
                dates.not( this ).datepicker( "option", option, date );
            }
        });
    $("#"+fromId).datepicker("setDate", defaultFromDate);
    $("#"+toId).datepicker("setDate", defaultToDate);
}

        function FloatMul(arg1,arg2){
        var m=0,s1=arg1.toString(),s2=arg2.toString(); 
        try{m+=s1.split(".")[1].length;}catch(e){}
        try{m+=s2.split(".")[1].length;}catch(e){}
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
        }   
        
        function FloatAdd(arg1,arg2){
        var r1,r2,m;  
        try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;} 
        try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;}
        m=Math.pow(10,Math.max(r1,r2)) ; 
        return (arg1*m+arg2*m)/m;
        }
        
        //浮点数减法运算  
        function FloatSub(arg1,arg2){  
        var r1,r2,m,n;  
        try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;}  
        try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;}  
        m=Math.pow(10,Math.max(r1,r2));  
        //动态控制精度长度  
        n=(r1>=r2)?r1:r2;  
        return ((arg1*m-arg2*m)/m).toFixed(n); 
        }