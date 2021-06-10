
/*
 * jquery  通用树选择框
 * Copyright (c) 2012 softweare.net
 * Date: 2012-07-14
 */
(function ($) {
	$.fn.treeDialog=function(opts){
		var $this=this;
		var defaults={
			title:"请选择",
			type:"",
			value:null,
			multiple:false,
			callback:function(id,text){}
		};
		var opts = $.extend(defaults, opts);
		this.each(function () {
			
			$(this).focus(function(){
				showDialog(opts);
			});
			
			$(this).attr("readonly","readonly");
		});
		function showDialog(opts){
			var dialogWidth;
			$("#treeDialog").empty();
			if(opts.search)
			{
				var htmlConent = "<div class='buttonPanel'> " +
					"<label>接收者：</label>" +
					"<input type='text' id='titleTree' />&nbsp;" +
					"<a class='k-button' id='searchTreeBtn'><em class='search'></em>查询</a>" +
					"</div>";
				$("#treeDialog").html(htmlConent);
				//查询按钮
				$("#searchTreeBtn").click(function(){
					searchTree(opts);
				});
			}
			var deptTree = new dhtmlXTreeObject("treeDialog", "100%", "100%", 0);
			deptTree.setSkin("dhx_skyblue");
			deptTree.setImagePath("js/dhtml/imgs/csh_bluebooks/");
			if(opts.multiple){
				deptTree.enableCheckBoxes(1);
			}
			var param="";
			if(opts.value){
				param="value="+encodeURI(encodeURI(opts.value));
				$("#titleTree").val(opts.value);
			}
			deptTree.loadXML(opts.type+"/rootTree.do?"+param);
			var buttons;
			if(opts.multiple){
				buttons={"全不选":function(){
					deptTree.setSubChecked(0,false);
				},"全选":function(){
					deptTree.setSubChecked(0,true);
				},"确定":function(){
				doOk(deptTree,opts);
				$(this).dialog("close");
				},"取消":function(){
					$(this).dialog("close");
				}};
			}else{
				buttons={"确定":function(){
				doOk(deptTree,opts);
				},"取消":function(){
					$(this).dialog("close");
				}};
			}
			$("#treeDialog").dialog({title:opts.title, modal:true, width:300, height:330, resizable:false,buttons:buttons});
		}
		function doOk(deptTree,opts){
				var id;
				var text;
				if(opts.multiple){
					id=deptTree.getAllChecked();
					id=id.split(",");
					text=[];
					for(var i=0;i<id.length;i++){
						var label=deptTree.getItemText(id[i]);
						text.push(label);
					}
				}else{
					id=deptTree.getSelectedItemId();
					text=deptTree.getItemText(id);
				}
				if(opts.message){
					if(parseInt(id) < 0){
						alert(opts.message);
						return;
					}
				}
				if(opts.callback){
					opts.callback(id,text);
				}
				$("#treeDialog").dialog("close");
		}
		
		function searchTree(opts)
		{
			var title = $("#titleTree").val();
			opts.value = title;
			showDialog(opts);
		}
	};
	
})(jQuery);