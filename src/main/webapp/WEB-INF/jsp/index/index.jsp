<%@page pageEncoding="utf-8"%>
<%@ taglib uri="/file" prefix="f"%>
<div class="panel">
	<table cellspacing="0" cellpadding="0" border="0" class="title">
		<tbody>
			<tr>
				<td class="left"></td>
				<td class="middle">
					软件版本信息
				</td>
				<td class="right"></td>
			</tr>
		</tbody>
	</table>
	<div class="content" style="height: 600px;">
		<lable>
		当前版本信息
		</lable>
		<hr>
		<div>
		<table width="100%" border="0" id="DianChiTable" cellpadding="0" cellspacing="0" class='edit_table'>
				<tbody>
				   <tr>
						<th style="width: 15%;">
							软件中文名称:
						</th>
						<td style="width: 45%;">
							 ${model.name}
						</td>
						<th style="width: 15%;">
							版本包名:
						</th>
						<td style="width: 45%;">
							 ${model.packageName}
						</td>
					</tr>
					
					<tr>
						<th>
							版本名称:
						</th>
						<td>
							 ${model.verName}
						</td>
						<th>
							版本序号:
						</th>
						<td>
							 ${model.verCode}
						</td>
					</tr>
					<tr>
						<th>
							版本备注:
						</th>
						<td colspan="3"> ${model.detail}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<lable>
		上传最新版本
		</lable>
		<hr>
		<form id="versionForm">
			<table width="100%" border="0" id="DianChiTable" cellpadding="0" cellspacing="0" class='edit_table'>
				<tbody>
				     <tr>
						<th>
							软件中文名称:
						</th>
						<td>
							<input type="text" name="name" value="${model.name}">
						</td>
					</tr>
					<tr>
						<th>
							版本名称:
						</th>
						<td>
							<input type="text" name="verName" value="${model.verName}">
						</td>
					</tr>
					<tr>
						<th>
							版本序号:
						</th>
						<td>
							<input type="text" name="verCode" value="${model.verCode}">
						</td>
					</tr>
					<tr>
						<th>
							版本包名:
						</th>
						<td>
							<input type="text" name="packageName" value="${model.packageName}">
						</td>
					</tr>
					<tr>
						<th>
							上传版本:
						</th>
						<td>
							<f:upload fileName="${model.verPath}" folder="mendian" name="verPath" />
						</td>
					</tr>

					<tr>
						<th>
							备注信息:
						</th>
						<td>
							<textarea name="detail" rows="3" style="width:99%;text-align: left;">
					${model.detail}
					</textarea>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">
							<a class="k-button" id='saveBtn'><span class="k-icon k-i-plus"></span>保存</a>
						</td>
					</tr>
				</tbody>
			</table>
			<iframe name="downloadFileIframe" id="downloadFileIframe" style="display: none"></iframe>
		</form>
	</div>
</div>