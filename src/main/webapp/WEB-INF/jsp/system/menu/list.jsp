<%@ page pageEncoding="UTF-8"%>
<div class='panel'>
	<table class='title' cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class='left'></td>
			<td class='middle'>
				功能菜单列表
			</td>
			<td class='right'></td>
		</tr>
	</table>
	<div class='content'>
		<form id="menuSearchForm" method="post">
			<input type="hidden" name="pageSize" value="15">
			<input type="hidden" name="type" value='net.swa.system.beans.entity.Menu' />
			<input type="hidden" name="attrNames" value='title' />
			<div class="buttonPanel">
				<label>
					菜单名
				</label>
				<input type='text' name="title" />
				<a class="k-button" id='searchBtn'><em class='search'></em>查询</a>
				<a class="k-button" id='createBtn'><em class='add'></em>新增</a>
				<a class="k-button del" id='deleteBtn'><em class='del'></em>删除</a>
			</div>
<p>
			<div id='menuTable' class='tablePanel'></div>
		</form>
	</div>
</div>