<%@ page pageEncoding="UTF-8"%>
<form id="logSearchForm" method="post">
	<input type="hidden" name="pageSize" value="15">
	<input type="hidden" name="type"
		value='net.swa.system.beans.entity.OperationLog' />
	<input type="hidden" name="attrNames" value='userid' />
	<input type="hidden" name="userid_operator" value='=' />
	<input type="hidden" name="attrNames" value='status' />
	<input type="hidden" name="status_operator" value='=' />
	<input type="hidden" name="status" value='0' />
	<div class="buttonPanel" id="buttonPanel_log">
	</div>
<p>
	<div id='logTable' class='tablePanel'></div>
</form>

<script>
var admin="${admin}";
var html="<input type='hidden' name='userid' value='${loginName}'/>";
if(admin){
	if('true'==admin){
		html="<label>登陆名称</label><span class='k-textbox'><input type='text' name='loginName' /></span><a class='k-button' id='searchBtn'><span class='k-icon k-i-search'></span>查询</a>";
	}
}
html+="";
$("#buttonPanel_log").html(html);

</script>