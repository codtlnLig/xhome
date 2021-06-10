
var menuRules = {"menu.title":{required:true, minlength:2, maxlength:100}};
var table;
var validator;
function showMenuList() {
	$.ajax({url:"menu/listPage.do", type:"get",async:true, dataType:"html", success:function (data) {
		$("#mainForm").html(data);
		table=$("#menuTable").table({
			width:"100%", 
			form:{
				url:"comm/search.do", 
				id:"#menuSearchForm", 
				type:"post", 
				pageNumberText:"#pageNum"
			}, 
			pageable:true,
			selectable:true, 
			colums:[
				{columnName:"菜单名", name:"title", width:250, handle:"editMenu"}, 
				{columnName:"父菜单", name:"parent.title"}, 
				{columnName:"单击事件", name:"event"}, 
				{columnName:"权值", name:"weight"}
				]
		});
		//新建按钮
		$("#createBtn").click(function(){
			editMenu();
		});
		//查询按钮
		$("#searchBtn").click(function(){
			$("#pageNum").val(1);
			table.refresh();
		});
		//删除按钮
		$("#deleteBtn").click(deleteMenu);
	}});
}

function deleteMenu(){
	var count=0;
	var id=0;
	$(".ids").each(function() {
		if (this.checked == true) {
			id=this.value;
			count=count+1;
		}
	});
	if(count==0){
		showDialog("请至少选择一条记录！");
		return;
	}
	if(count>1){
		showDialog("为安全起见，您只能逐个删除菜单！");
		return;
	}
	$("#msgDialog").html("<p>确定要删除该菜单吗？</p>");
	$("#msgDialog").dialog({
		modal:true,
		width:250,
		height:160,
		buttons:{"确定":function(){
					$(this).dialog("close");
					$.ajax({url:"menu/delete.do?menu.id=" + id, type:"get", dataType:"json", success:function (data) {
					if (data.success) {
						table.refresh();
					}else{
						showDialog("删除失败，原因："+data.message);
					}
			}});
				},"取消":function(){
					$(this).dialog("close");
				}
		}
	});
}
//编辑操作，
//如果传了id值，就是编辑菜单，如果没有id值，就是新建菜单
function editMenu(id) {
	var title = "添加菜单";
	var menu="";
	if(id!==undefined){
		menu="?id=" + id;
		title = "编辑菜单";
	}
	$.ajax({url:"menu/edit.do"+menu, type:"get", dataType:"html", success:function (data) {
		$("#commonDialog").html(data);
		$("#commonDialog").dialog({title:title, modal:true, width:300, height:230, resizable:false,
		buttons:{
			'保存':function(){
				$("#commonDialog").dialog('close');
				saveMenu();
				
			},
			'返回':function(){
				$("#commonDialog").dialog('close');
			}
		}
		});
		validator=$("#menuEditForm").validate({rules:menuRules, messages:{}});
		$("#parentName").treeDialog({
			title:"请选择父菜单",
			type:"menu",
			callback:function(id,text){
				if(text!=0){
					$("#parentId").val(id);
					$("#parentName").val(text);
				}else{
					$("#parentId").val(0);
					$("#parentName").val("");
				}
			}
		});
	}});
}  
//保存
function saveMenu() {
	if(!validator.form()){
		return;
	}
	$("#menuEditForm").ajaxSubmit({url:"menu/save.do", type:"post", dataType:"json", success:function (data) {
		$("#commonDialog").dialog("close");
		if (data.success) {
			showDialog("保存成功");
			table.refresh();
		} else {
			showDialog("保存失败,原因->"+data.message);
		}
	}});
}