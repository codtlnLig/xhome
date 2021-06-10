<%@ page pageEncoding="UTF-8"%>
<form id="userSearchForm" method="post" onsubmit="return false">
	<div class='panel'>
		<table class='title' cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class='left'></td>
				<td class='middle'>
					系统用户列表
				</td>
				<td class='right'></td>
			</tr>
		</table>
		<div class='content'>
			<input type="hidden" name="pageSize" value="15">
			<input type="hidden" name="type" value='net.swa.system.beans.entity.User' />
			<input type="hidden" name="attrNames" value='realName' />
			<div class="buttonPanel">
				<label>
					姓名
				</label>
				<input type='text' name="realName" />
				<a class="k-button" id='searchBtn'><em class='search'></em>查询</a>
				<a class="k-button" id='createBtn'><em class='add'></em>新增</a>
				<a class="k-button" id='deleteBtn'><em class='del'></em>删除</a>
			</div>
			<p><div id='userTable' class='tablePanel'></div>
		</div>
	</div>
</form>
