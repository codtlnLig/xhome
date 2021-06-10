
var roleRules = {"name":{required:true, minlength:2, maxlength:100}};
var table;
var validator;
var menuTree;
function showRoleList() {
	$.ajax({url:"role/listPage.do", type:"get", async:true,dataType:"html", success:function (data) {
		$("#mainForm").html(data);
		
		table=$("#roleTable").table({
			width:"100%", 
			form:{
				url:"comm/search.do", 
				id:"#roleSearchForm", 
				type:"post", 
				pageNumberText:"#pageNum"
			}, 
			pageable:true,
			selectable:true, 
			colums:[
				{columnName:"角色名", name:"name", width:450, handle:"editRole"}, 
				{columnName:"角色描述", name:"description"}
				]
		});
		//新建按钮
		$("#createBtn").click(function(){
			editRole();
		});
		//查询按钮
		$("#searchBtn").click(function(){
			$("#pageNum").val(1);
			table.refresh();
		});
		//删除按钮
		$("#deleteBtn").click(function(){
			batchDeleteWidthCallback('Role',table.refresh);
		});
	}});
}


//编辑操作，
//如果传了id值，就是编辑菜单，如果没有id值，就是新建菜单
function editRole(id) {
	var title = "添加角色";
	var val=null;
	var role="";
	if(id!==undefined){
		role="?id=" + id;
		val=id;
		title = "编辑角色";
	}else{
		id=0;
	}
	$.ajax({url:"role/edit.do"+role, type:"get", dataType:"html", success:function (data) {
		$("#commonDialog").html(data);
		$("#commonDialog").dialog({title:title, modal:true, width:500, height:230, resizable:false,buttons:{
			'保存':function(){
				saveRole();
			},
			'取消':function(){
				$("#commonDialog").dialog('close');
			}
		}});
		validator=$("#roleEditForm").validate({rules:roleRules, messages:{}});
		
		
		$("#selectMenuBtn").treeDialog({
			title:"请选择菜单列表",
			type:"menu",
			value:val,
			multiple:true,
			callback:function(ids,texts){
				var html="";
				for(var i=0;i<ids.length;i++){
					html+="<span>"+texts[i]+"</span>";
					html+="<input type='hidden' name='menuId' value='"+ids[i]+"'/>";
				}
				$("#menuTreePanel").html(html);
			}
		});
	}});
}

//保存
function saveRole() {
	if(!validator.form()){
		return;
	}
	$("#roleEditForm").ajaxSubmit({url:"role/save.do", type:"post", dataType:"json", success:function (data) {
		$("#commonDialog").dialog("close");
		if (data.success) {
			showDialog("保存成功");
			table.refresh();
		} else {
			showDialog("保存失败,原因->"+data.message);
		}
	}});
}