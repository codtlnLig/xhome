<%@ page pageEncoding="UTF-8"%>
<form id="menuEditForm">
	<input type="hidden" name="id" value="${menu.id}">
	<input type='hidden' name="parent.id" value="${menu.parent.id}" id='parentId'>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class='hall_table'>
		<tbody>
			<tr>
				<td>
					标题:
				</td>
				<td >
					<input type="text" name="title" value="${menu.title}">
				</td>
				
			</tr>
			<tr style="height: 12px">
				<td>
				</td>
				<td >
					<label class="error" for="menu.title" generated="true">
				</td>
				
			</tr>
			<tr>
				<td>
					父菜单:
				</td>
				<td>
					<input type='text' id="parentName" value="${menu.parent.title}">
				</td>
			</tr>
			<tr>
				<td>
					点击事件:
				</td>
				<td>
					<input type="text" name="event" value="${menu.event}">
				</td>
			</tr>
			<tr>
				<td>
					权值:
				</td>
				<td>
					<input type="text" name="weight" value="${menu.weight}" >
				</td>
			</tr>
		</tbody>
	</table>
	<div style="float: right;">
		<div class='buttonPanel' >
			<a class="saveBtn" onclick='saveMenu()'></a>
		</div>
	</div>
</form>