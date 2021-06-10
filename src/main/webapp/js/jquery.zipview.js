
/*
 * jquery 进度插件，作者：陈晓伟
 * Copyright (c) 2006 - 2012 Softweare
 * 
 * jquery.table.js
 *
 */

(function ($) {
	$.fn.zipView= function (fileID) {
		$this=this;
		this.each(function () {
			$(this).attr("cellpadding",0).attr("cellspacing",0).attr("border",0);
			$.ajax({
				url:"file/viewZip.do?fileId="+fileID,
				dataType:"html",
				async:false,
				success:function(data){
					var html="<thead><tr><th>标题</th><th>大小</th><th>类型</th></tr></thead><tbody>";
					html+=data;
					html+="</tbody>";
					$this.html(html);
					$this.treeTable({
				      expandable: true
				    });
				    $('.fancybox-buttons').fancybox({
							prevEffect : 'none',
							nextEffect : 'none',
							closeBtn  : true,
							helpers : {
								title : {
									type : 'inside'
								},
								buttons	: {}
							},
							afterLoad : function() {
							
							}
					});
				}
			});

		});
		this.show=function(msg){
			
		}
		this.hide=function(){
			
		}
		 
	};
})(jQuery);

