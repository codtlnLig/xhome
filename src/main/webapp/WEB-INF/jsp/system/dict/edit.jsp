<%@ page pageEncoding="UTF-8"%>
<form id="editForm">
	<input type="hidden" name="id" value="${dict.id}">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class='edit_table'>
		<tbody>
			<tr>
				<th>
					字典值(key):
				</th>
				<td>
					<input type="text" name="key" value="${dict.key}" >
				</td>
				<td> 
					<label class="error" for="key" generated="true">
				</td>
			</tr>
			<tr>
				<th>
					字典名(value):
				</th>
				<td>
					<input type="text" name="value" value="${dict.value}">
				</td>
				<td> 
					<label class="error" for="value" generated="true">
				</td>
			</tr>
			<tr>
				<th >
					字典类别:
				</th>
				<td width="40%">
					<input type="text" name="title" value="${dict.title}">
				</td>
				<td width="40%"> 
					<label class="error" for="title" generated="true">
				</td>
			</tr>
			
		</tbody>
	</table>
	<div style="float: right;">
		<div class='buttonPanel' >
			<a class="saveBtn" id='saveBtn'></a>
		</div>
	</div>
</form>