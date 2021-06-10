<%@ page pageEncoding="UTF-8"%>
<form id="roleSearchForm" method="post" onsubmit="return false">
	<input type="hidden" name="pageSize" value="15">
	<input type="hidden" name="type" value='net.swa.system.beans.entity.Role' />
	<input type="hidden" name="attrNames" value='name' />
	<div class="SearchPanel">
			角色名
		<span class="k-textbox"  ><input type='text' name="name" /></span>
		
		<a class="k-button" id='searchBtn'><em class='search'></em>查询</a>
		<a class="k-button" id='createBtn'><em class='add'></em>新增</a>
		<a class="k-button del" id='deleteBtn'><em class='del'></em>删除</a>
	</div>
<p>
	<div id='roleTable' class='tablePanel'></div>
</form>