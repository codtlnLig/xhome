
var logtable;
function showLogList() {
	showLoading("查询中...");
	$.ajax({url:"log/listPage.do", type:"get",async:true, dataType:"html", success:function (data) {
		$("#mainForm").html(data);
		queryLogDatas();
		hideLoading();
		//查询按钮
		$("#searchBtn").click(function(){
			showLoading("查询中...");
			$("#pageNum").val(1);
			logtable.refresh();
			hideLoading();
		});
		$('#deleteBtn').click(function(){
			piliangShiXiao();
		})
	}});
}
function queryLogDatas(){
		logtable=$("#logTable").table({
			width:"100%", 
			defaultOrderBy:"id",
			defaultOrderType:"desc",
			form:{
				url:"comm/search.do", 
				id:"#logSearchForm", 
				type:"post", 
				pageNumberText:"#pageNum"
			}, 
			pageable:true,
			selectable:true, 
			colums:[
				{columnName:"登陆名称", name:"loginName"}, 
				{columnName:"操作描述", name:"descript"}, 
				{columnName:"操作路径", name:"uri"},
				{columnName:"操作ip", name:"ip"},
				{columnName:"操作时间", name:"updateDate"}//processor:formatTimestamp
				]
		});
}
//批量删除 
function piliangShiXiao() {
	var ids = '';
	$('.ids').each(function() {
		if (this.checked == true) {
			ids += 'ids=' + this.value + '&';
		}
	});
	if (ids.length == 0) {
		showDialog('请至少选择一条记录！');
		return;
	}
	$('#msgDialog').html('<p>确定要删除记录信息？</p>');
	$('#msgDialog').dialog( {
		modal : true,
		width : 250,
		height : 160,
		buttons : {
			'确定' : function() {
				$(this).dialog('close');
				$.ajax( {
					url : 'log/ShiXiao.do?' + ids,
					type : 'get',
					dataType : 'json',
					success : function(data) {
						if (data.success) {
							showLogList();
							showDialog('操作成功');
						} else {
							showDialog('操作失败，原因：' + data.message);
						}
					}
				});
			},
			'取消' : function() {
				$(this).dialog('close');
			}
		}
	});
}
