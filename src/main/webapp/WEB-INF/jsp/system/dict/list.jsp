<%@ page pageEncoding="UTF-8"%>
<form id="dictSearchForm" method="post" onsubmit="return false">
	<input type="hidden" name="pageSize" value="100">
	<input type="hidden" name="type" value='net.swa.system.beans.entity.Dict' />
	<input type="hidden" name="attrNames" value='title' />
	<input type="hidden" name="title_operator" value='=' />
	<div class='content'>
	<div class="buttonPanel">
		<label>字典类别名</label>	
		<select name='title'  id="categoryList"  class="comboBox" >
		</select>
		
		<a class="k-button" id='searchBtn'><span class="k-icon k-i-search"></span>查询</a>
		<a class="k-button" id='createBtn'><span class="k-icon k-i-plus"></span>新增</a>
		<a class="k-button" id='deleteBtn'><span class="k-icon k-i-close"></span>删除</a>
		<a class="k-button" id='sortSaveBtn'><span class="k-icon k-i-plus"></span>保存排序</a>
	</div>
	</div>
<p>
	<div id='dictTable' class='tablePanel'></div>
	
	</form>
<script>
var typeList = ${typeList};
	if(null!=typeList&&typeList.length>0){
		for(var i=0;i<typeList.length;i++){
			document.getElementById("categoryList").options.add(new Option(typeList[i].title,typeList[i].title));   
		}
	}
</script>