
var table,validator;
function showUserList() {
	$.ajax({url:"user/listPage.do", type:"get",async:true, dataType:"html", success:function (data) {
		$("#mainForm").html(data);
		
		table=$("#userTable").table({
			width:"100%", 
			form:{
				url:"comm/search.do", 
				id:"#userSearchForm", 
				type:"post"
			}, 
			pageable:true,
			selectable:true, 
			colums:[
				{columnName:"姓名", name:"realName",formatter:function(obj){
					if(obj.role.id==2){
						return "<span title='注册用户，不能编辑！' class='not-allowed'>"+obj.realName+"</span>";
					}else{
						return "<a onclick='editUser("+obj.id+")'>"+obj.realName+"</a>";
					}
				}}, 
				{columnName:"登录名", name:"loginName"}, 
				{columnName:"用户角色", name:"role.name"},
				{columnName:"注册时间", name:"regDate"} 
				]
		});
		//新建按钮
		$("#createBtn").click(function(){
			editUser();
		});
		//查询按钮
		$("#searchBtn").click(function(){
			$("#pageNum").val(1);
			table.refresh();
		});
		//删除按钮
		$("#deleteBtn").click(function(){
			batchDeleteWidthCallback('User',table.refresh);
		});
	}});
}

//编辑操作，
//如果传了id值，就是编辑菜单，如果没有id值，就是新建菜单
function editUser(id) {
	var title = "添加";
	var user="";
	if(id!==undefined){
		user="?id=" + id;
		title = "编辑";
	}
	$.ajax({url:"user/edit.do"+user, type:"get", dataType:"html", success:function (data) {
		$("#commonDialog").html(data);
		var roleId = $("#currentRoleId").val();
		title += "用户信息";
		$("#commonDialog").dialog({
			title:title, 
			modal:true, 
			width:880, 
			height:467, 
			buttons:{
				'保存':function(){
					saveUser();
				},
				'返回':function(){
					$("#commonDialog").dialog('close');
				}
			}
		});
		var rules={
				'loginName':{required:true,remote:{
					url:'user/checkUserId.do'
				}},
				'email':{required:true,email:true},
				'roleName':{required:true},
				'realName':{required:true}
		};
		var msgs={
				'loginName':{
					required:"请输入登录名！",
					remote:"该登录名已经被使用！"},
				'email':{
					required:"请输入邮箱地址！",
					email:"无效的邮箱地址！"
				},
				'roleName':{required:"请指定用户角色!"},
				'realName':{required:"请输入用户真实姓名！"}
		};
		if($("#user_id").val()!=0){
			rules['loginName'].remote=undefined;
			msgs['loginName'].remote=undefined;
		}else{
			rules['loginName'].remote={
					url:'user/checkUserId.do'
				};
			msgs['loginName'].remote="该登录名已经被使用！";
		}
		validator=$("#userEditForm").validate({
			rules:rules, 
			messages:msgs,
			submitHandler:function(form){
				form.submit();
			}
		});
		$("#roleName").treeDialog({
			title:"请选择用户角色",
			type:"role",
			callback:function(id,text){
				$("#roleId").val(id);
				$("#roleName").val(text);
			}
		});
	}});
}  
//保存
function saveUser() {
	if(!validator.form()){
		return;
	}
	$("#userEditForm").ajaxSubmit({url:"user/save.do", type:"post", dataType:"json", success:function (data) {
		$("#commonDialog").dialog("close");
		if (data.success) {
			showDialog("保存成功");
			table.refresh();
		} else {
			showDialog("保存失败,原因->"+data.message);
		}
	}});
}
