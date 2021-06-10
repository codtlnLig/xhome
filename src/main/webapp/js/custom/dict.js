
var dictRules = {
	"title":{required:true, minlength:3, maxlength:100},
	"key":{required:true,maxlength:100},
	"value":{required:true,maxlength:100},
	"code":{required:true,maxlength:100}
};
var dicttable;
var validator;

var fixHelper = function(e, ui) { 
            ui.children().each(function() { 
                $(this).width($(this).width());     //在拖动时，拖动行的cell（单元格）宽度会发生改变。在这里做了处理就没问题了  
            }); 
            return ui; 
        };
function showDictList() {
	$.ajax({url:"dict/listPage.do", type:"get", dataType:"html", success:function (data) {
		$("#mainForm").html(data);
		dicttable=$("#dictTable").table({
			width:"100%", 
			form:{
				url:"comm/search.do", 
				id:"#dictSearchForm", 
				type:"post", 
				pageNumberText:"#pageNum"
			}, 
			pageable:true,
			selectable:true, 
			colums:[
			    {columnName:"字典类别",width:'35%', name:"title", sortable:false,handle:"editDict"},
				{columnName:"字典名",width:'35%', name:"value", sortable:false,handle:"editDict"},
				{columnName:"字典值",width:'25%', name:"key", sortable:false, handle:"editDict"}
				],
			orderBy:{
				field:"num",
				type:"asc"
			}
		});
		//新建按钮
		$("#createBtn").click(function(){
			editDict();
		});
		//查询按钮
		$("#searchBtn").click(function(){
			var comvalue= $("#categoryList").val();
			if(comvalue == null || comvalue == ''){
				showDialog("请选择 [ 字典类别名 ] ");
				return;
			}
			$("#pageNum").val(1);
			dicttable.refresh();
		});
		//删除按钮
		$("#deleteBtn").click(function(){
			batchDeleteWidthCallback('Dict',dicttable.refresh);
		});
		$(".comboBox").kendoComboBox({filter:"contains", suggest:true });
		// 拖拽处理
		$( "#dictTable tbody" ).sortable({ 
			 axis: 'y' ,
			 tolerance: 'pointer',
			 cursor: 'crosshair',
			 helper: fixHelper,
			 revert: true,
			 cursor: 'move', //拖动的时候鼠标样式 
			 containment: 'parent',
			 delay:100,
			 start:function(e, ui){ 
                    ui.helper.addClass("moveAble");
                    return ui; 
             }, 
             stop:function(e, ui){ 
             	var i=0;
             	$(".records tr").each(function(){
             		$(this).removeClass("r1").removeClass("r2");
             		if(i%2==0){
             			$(this).addClass("r1");
             		}else{
             			$(this).addClass("r2");
             		}
             		i++;
             	});
                 ui.item.removeClass("ui-state-highlight"); //释放鼠标时，要用ui.item才是释放的行  
                 ui.item.removeClass("moveAble");
                 return ui; 
             }  
		}).disableSelection();
		//排序保存按钮
		$("#sortSaveBtn").click(function(){
			saveSortDict();
		});
	}});
}  
//编辑操作，
//如果传了id值，就是编辑，如果没有id值，就是新建
function editDict(id) {
	var title = "添加字典";
	var dict="";
	if(id){
		dict="?dict.id=" + id;
		title = "编辑字典";
	}
	$.ajax({url:"dict/edit.do"+dict, type:"get", dataType:"html", success:function (data) {
		$("#commonDialog").html(data);
		$("#commonDialog").dialog({title:title, modal:true, width:659, height:206, resizable:false,buttons:{
			'保存':function(){
				saveDict();
			},
			'取消':function(){
				$("#commonDialog").dialog('close');
			}
		},close:function(){$(this).dialog('destroy');}});
		$("#saveBtn").click(saveDict);
		//使用jquery默认消息做验证
		validator=$("#editForm").validate({rules:dictRules, messages:{}});
	}});
}  
//保存
function saveDict() {
	if(!validator.form()){
		return;
	}
	$("#editForm").ajaxSubmit({url:"dict/save.do", type:"post", dataType:"json", success:function (data) {
		$("#commonDialog").dialog("close");
		if (data.success) {
			showDialog("保存成功");
			dicttable.refresh();
		} else {
			showDialog("保存失败,原因->"+data.message);
		}
	}});
}

//排序保存
function saveSortDict() {
	var ids = "";
	var num = "";
	var i=1;
	$(".ids").each(function () {
			ids += "dicIds=" + this.value + "&";
			num += "dicNums=" + i + "&";
			i += 1;
	});
	
	if(ids.length == 0){
		return;
	}
	$("#msgDialog").html("<p>确定要保存当前排序吗？</p>");
	$("#msgDialog").dialog({
		modal:true,
		width:250,
		height:160,
		buttons:{"确定":function(){
					$(this).dialog("close");
					$.ajax({url:"dict/savesort.do?" + ids + "&" + num, type:"get", dataType:"json", success:function (data) {
					if (data.success) {
						showDialog("保存成功");
						dicttable.refresh();
					}else{
						showDialog("保存失败，原因："+data.message);
					}
			}});
				},"取消":function(){
					$(this).dialog("close");
				}
		},close:function(){$(this).dialog('destroy');}
	});
}