<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="user" value="#session['_CURRENT_USER']" />
<div class='panel' style="width: 800px">
	<table class='title' cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class='left'></td>
			<td class='middle'>
				欢迎
			</td>
			<td class='right'></td>
		</tr>
	</table>
	<div class='content'>
		<div style="margin: 20px">
			<span>欢迎使用<b>${initParam.WebAppName}</b>
			</span>
			<hr />
			<div>
				<a class='k-button' href="${pageContext.request.contextPath}/${user.role.id}.doc"><span><em class="down"></em>下载${typeName }操作手册</span>
				</a>
				
			</div>
		</div>
	</div>
</div>
<div class='panel' style="width: 390px">
	<table class='title' cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class='left'></td>
			<td class='middle'>
				订单概览
			</td>
			<td class='right'></td>
		</tr>
	</table>
	<div class='content' style="height: 200px">
		<ul>
			<li>
				未派送
			</li>
			<li>
				未收款
			</li>
			<li>
				已派送
			</li>
			<li>
				已收款
			</li>
		</ul>
	</div>
</div>


<div class='panel' style="width: 400px;margin-right: 0px;float: right;">
	<table class='title' cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class='left'></td>
			<td class='middle'>
				库存概览
			</td>
			<td class='right'></td>
		</tr>
	</table>
	<div class='content' style="height: 200px">
		<ul>
			<s:if test="%{listSms.size()==0}">
				<li>
					暂无库存消息
				</li>
			</s:if>
			<s:else>
				<s:iterator value="listSms" id="opp">
					<li>
						<span class='left'><a href='#'
							onclick='viewSmsByUser(${opp.id},0)'>${opp.sms.title }</a> </span><span
							class='right'>${opp.sms.startTime}</span>
					</li>
				</s:iterator>
			</s:else>
		</ul>
	</div>
</div>
