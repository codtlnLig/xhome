
//多图片裁剪上传
function mulUploadPreview(obj,folder,model_imageUrl_mul,ulListId,width,height,minwidth,minheight,limitwidth,limitheight,uploadSpan_mul,modelName){
	if(!checkImgType(obj)){
		return ;
	}
	$("#imgCutPane_mul").empty();
	var lisize=$("#"+ulListId+" li").size();
	if(lisize<4){
	var html = '<div   style="width:'
			+ (width+4)
			+ 'px;height:'
			+ height
			+ 'px;display:inline-block; float:left;  border: 1px rgba(0,0,0,.4) solid;background-color: white;-webkit-border-radius: 6px;-moz-border-radius: 6px;border-radius: 6px;-webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);-moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);"><img id="preview_mul" style="width:'
			+ width + 'px;height:' + height
			+ 'px;display:inline-block; "/></div>';
	html += '<div id="preview-pane" style="width: '+minwidth+'px;height: '+minheight+'px;overflow: hidden;" > <div class="preview-container"  style="width: '+minwidth+'px;height: '+minheight+'px;overflow: hidden;"><img id="minImg_mul" class="jcrop-preview"/></div></div>';
	html += '<input type="hidden" name="x" id="x"/>';
	html += '<input type="hidden" name="y" id="y"/>';
	html += '<input type="hidden" name="w" value="' + minwidth + '" id="w"/>';
	html += '<input type="hidden" name="h" value="' + minheight + '" id="h"/>';
	html += '<input type="hidden" id="model_simageUrl_mul"/>';
	$("#imgCutPane_mul").html(html);
	$("#imgCutPane_mul").height(height + 5).show();
	var previewId = 'preview_mul', minImgId = 'minImg_mul', imageUrl = 'model_simageUrl_mul';

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
						$("#" + uploadSpan_mul).empty();
						$("#" + uploadSpan_mul)
								.append(
										"<input type='button' value='上传'  style='width: 90px;' onclick=\"mulUploadCutImg(this,\'"+ulListId+"\',\'"
												+ imageUrl
												+ "\',\'"
												+ model_imageUrl_mul
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
function mulUploadCutImg(obj,ulListId, imageUrl, httpPath, width, height, minwidth,
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
							$("#imgCutPane_mul").empty().hide();
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

function removeLi(obj){
	$(obj).parent().parent().remove();
}
