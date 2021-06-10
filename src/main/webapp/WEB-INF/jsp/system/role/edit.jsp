<%@ page pageEncoding="UTF-8"%>
<form id="roleEditForm" onsubmit="return false">
	<input type="hidden" name="id" value="${role.id}">
	<table width="250" border="0" cellpadding="0" cellspacing="0" class='hall_table'>
		<tbody>
			<tr>
				<td>
					角色名:
				</td>
				<td>
					<input type="text" name="name" value="${role.name}">
				</td>

			</tr>

			<tr style="height: 12px">
				<td>
				</td>
				<td>
					<label class="error" for="name" generated="true">
				</td>

			</tr>
			<tr>
				<td>
					角色描述:
				</td>
				<td>
					<input type="text" name="description" value="${role.description}">
				</td>
			</tr>
			<tr>
				<td>
					角色菜单:
				</td>
				<td>
					<input type="button" value="点击选择" id='selectMenuBtn'>
				</td>
			</tr>
		</tbody>
	</table>
	<div id="menuTreePanel">
	</div>
	<div style="float: right;">
		<div class='buttonPanel'>
			<a class="saveBtn" onclick='saveRole()'></a>
		</div>
	</div>
</form>

<script>
var menus = ${menus};
var html="";
	if(null!=menus&&menus.length>0){
		for(var i=0;i<menus.length;i++){
			html+="<span>"+menus[i].title+"</span><input type='hidden' name='menuId' value='"+menus[i].id+"'/>";   
		}
	}
	$("#menuTreePanel").html(html);
	
</script>
