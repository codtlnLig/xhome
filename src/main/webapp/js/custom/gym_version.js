function queryVersion () {
    showLoading("查询中...");
    $.ajax({url:"version/index.do", type:"get",async:true, dataType:"html", success:function (data) {
        $("#mainForm").html(data);
        hideLoading();
        
        $("#saveBtn").click(function(){
        	$("#versionForm").ajaxSubmit({url:"version/save.do", type:"post", dataType:"json", success:function (data) {
	            $("#commonDialog").dialog("close");
	            if (data.success) {
	                showDialog("保存成功");
	                queryVersion ();
	            } else {
	                showDialog("保存失败,原因->"+data.message);
	            }
	        }});
        });
    }});
}