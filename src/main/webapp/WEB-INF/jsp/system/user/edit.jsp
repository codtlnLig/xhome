<%@ page pageEncoding="UTF-8"%>
<form id="userEditForm">
	<input type="hidden" name="id" value="${user.id}">
	<input type="hidden" name="password" value="${user.password }">
	<input type="hidden" name="role.id" id='roleId' value="${user.role.id }">
	<input type="hidden" name="regDate" value="${user.regDate }">
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		class='edit_table'>
		<tbody>
			<tr>
				<th>
					登录名:
				</th>
				<td>
					<input type="text" name="loginName" value="${user.loginName}"/>
				</td>
			</tr>

			<tr>
				<th>
					真实姓名:
				</th>
				<td>
					<input type="text"  name='realName' value="${user.realName }">
				</td>
			</tr>
			<tr id="roleTr">
				<th >
					用户角色:
				</th>
				<td>
					<input type="text" id='roleName' name='roleName' value="${user.role.name}">
				</td>
			</tr>
			<tr>
				<th>
					用户类型
				</th>
				<td>
				 <select name="userType" id="userType_edit" class='dropDownList'>
				 
				 </select>
				</td>
			</tr>
			<tr>
				<th>
					用户邮箱:
				</th>
				<td>
					<input type="text"  name="email" value="${user.email }"/>
				</td>
			</tr>
		</tbody>
	</table>
	<div>
		*注：新用户的初始密码888888
	</div>
</form>
<script>
var userTypes = ${userTypes};
var userType="${user.userType}";
	if(null!=userTypes&&userTypes.length>0){
		for(var i=0;i<userTypes.length;i++){
			document.getElementById("userType_edit").options.add(new Option(userTypes[i].value,userTypes[i].key));
			if(userType==userTypes[i].key){
				document.getElementById("userType_edit").options[i].selected=true;
			}
		}
	}
</script>