/*
 * jquery 表格插件
 * Copyright (c) 2006 - 2012 Softweare
 * 
 * jquery.table.js
 *
 */
(function($)
{
	$.fn.table = function(opts)
	{
		var $this;
		var defaults =
		{
			width : "100%",
			url : "",
			data :
			{},
			defaultOrderBy : null,
			defaultOrderType : null,
			pageable : true,
			selectable : true,
			colums : [],
			renderTo : ""
		};
		var param = $.extend(defaults, opts);
		this.each(function()
		{
			$this = $(this);
			$this.orderBy = "";
			$this.orderType = "";
			var html = "";
			var attr = "";
			if (opts.defaultOrderBy)
			{
			$this.orderBy = opts.defaultOrderBy;
			$this.orderType = opts.defaultOrderType;
			}
			html += "<table class='main_table' cellpadding='0' cellspacing='0' style='width:" + param.width + "' >";
			html += "<thead><tr>";
			if (param.selectable)
			{
			html = html + "<th width='12px'><input type='checkbox'  class='checkall'/></th>";
			}
			var colums = param.colums;
			for ( var i = 0; i < colums.length; i = i + 1)
			{
				var style = "style='";
				if (i == colums.length - 1)
				{
				style += "border-right:none;";
				}
				if (colums[i].width)
				{
				style += "width:" + colums[i].width;
				}
				style += "'";
				if (colums[i])
				{
				if (colums[i].sortable === undefined)
				{
				colums[i].sortable = true;
				}
				html = html + "<th " + style + " class='state1' sortable='" + colums[i].sortable + "'  field='" + colums[i].name + "'>" + colums[i].columnName+ (colums[i].sortable ? "<em></em>" : "") + "</th>";
				}
			}
			html = html + "</tr></thead><tbody  class='records'></tbody>";
			if (param.pageable)
			{
				html += "<tfoot><tr><td colspan='"+(colums.length + 1) + "'><div  style='width:100%' class='pagination'></td></tr></tfoot>";
			}
			html += "</table>";
			$("<div id='_juqery_loading2' class='loading_bkg'></div><div id='_jquery_loading_box2' class='loading_box'><img src='images/loading.gif'/><span>查询中...</span></div>").appendTo("body");
			
			
			$this.html(html);
			refreshTable();
			var checkall = $this.find(".checkall");
			checkall.click(function(e)
			{
				$this.find(".ids").each(function()
				{
					this.checked = !this.checked;
				});
			});
			$this.find('th').each(function()
			{
				var sortable = $(this).attr('sortable');
				if (sortable == 'false')
				{
				return;
				}
				$(this).click(function()
				{
					var orderType = '';
					var orderBy = $(this).attr('field');
					if (orderBy)
					{
					var state = $(this).attr('class');
					$('.state2').attr('class', 'state1');
					$('.state3').attr('class', 'state1');
					if (state == 'state1')
					{
					$(this).attr('class', 'state2');
					orderType = 'DESC';
					}
					if (state == 'state2')
					{
					$(this).attr('class', 'state3');
					orderType = 'ASC';
					}
					if (state == 'state3')
					{
					$(this).attr('class', 'state1');
					orderType = ''
					orderBy = ''
					}
					$this.orderType = orderType;
					$this.orderBy = orderBy;
					refreshTable();
					}
				});
			});
		});
		this.refresh = function()
		{
			$this.find(".checkall").attr("checked", false);
			refreshTable();
		}
		this.getSelected = function()
		{
			var array = new Array();
			$this.find(".ids").each(function()
			{
				if (this.checked)
				{
				array.push(this.value);
				}
			});
			return array;
		}
		function refreshTable(tablePanel)
		{
			var form = param.form;
			var current = 1;
			var totalCount = 0;
			var pageSize = 0;
			var url = form ? form.url : param.url;
			var data = param.data ? param.data :
			{};
			data['orderBy'] = $this.orderBy;
			data['orderType'] = $this.orderType;
			if (param.pageSize)
			{
			data['pageSize'] = param.pageSize;
			}
			var opt =
			{
				url : url,
				type : 'post',
				data : data,
				async : true,
				dataType : "json",
				success : function(data)
				{
					var rp = $this.find('.records').get(0);
					$(rp).empty();
					var users;
					if (data.success)
					{
					users = data.result;
					totalCount = data.totalSize;
					current = data.currentPage;
					pageSize = param.pageSize;
					}
					else
					{
					alert(data.message);
					return;
					}
					$(rp).html(parseData(users));
					$("#_juqery_loading2").remove();
					$("#_jquery_loading_box2").remove();
					if (param.pageable)
					{
					initPageBar(data.totalSize, data.pageSize, current - 1);
					}
				}
			};
			if (param.form)
			{
			$(form.id).ajaxSubmit(opt);
			}
			else
			{
			$.ajax(opt);
			}
		};
		function initPageBar(totalSize, pageSize, current)
		{
			var opt =
			{
				callback : pageselectCallback,
				items_per_page : pageSize,
				num_display_entries : 10,
				current_page : current,
				num_edge_entries : 2,
				prev_text : "上一页",
				next_text : "下一页"
			};
			$this.find('.pagination').pagination(totalSize, opt);
		};
		function pageselectCallback(page, jq)
		{
			page = page + 1;
			var form = param.form;
			var url = form ? form.url : param.url;
			var data = param.data ? param.data :
			{};
			data['orderBy'] = $this.orderBy;
			data['orderType'] = $this.orderType;
			data.currentPage = page;
			
			$("<div id='_juqery_loading2' class='loading_bkg'></div><div id='_jquery_loading_box2' class='loading_box'><img src='images/loading.gif'/><span>查询中...</span></div>").appendTo("body");
			
			var opt =
			{
				url : url,
				data : data,
				async : true,
				type : 'post',
				dataType : "json",
				success : function(data)
				{
					var rp = $this.find('.records').get(0);
					$(rp).empty();
					var users;
					if (data.success)
					{
					users = data.result;
					totalCount = data.totalSize;
					current = data.currentPage;
					pageSize = data.pageSize;
					}
					else
					{
					alert(data.message);
					return;
					}
					$("#_juqery_loading2").remove();
					$("#_jquery_loading_box2").remove();
					$(rp).html(parseData(users));
				}
			};
			if (form)
			{
			$(form.id).ajaxSubmit(opt);
			}
			else
			{
			$.ajax(opt);
			}
		}
		function parseData(users)
		{
			var colums = param.colums;
			var html = "";
			for ( var i = 0; i < users.length; i = i + 1)
			{
			var user = users[i];
			html = html + "<tr class='" + (i % 2 == 0 ? 'r1' : 'r2') + "'>";
			if (param.selectable)
			{
			var tmp = "";
			if (param.selectType)
			{
			tmp = "<input type='" + param.selectType + "' class='ids' value='" + user.id + "' name='ids'>";
			}
			else
			{
			tmp = "<input type='checkbox' class='ids' value='" + user.id + "' name='ids'>";
			}
			html = html + "<td>" + tmp + "</td>";
			}
			for ( var j = 0; j < colums.length; j = j + 1)
			{
			var style = "";
			if (j == colums.length - 1)
			{
			style = "style='border-right:none;'";
			}
			if (colums[j].name.indexOf(".") < 0)
			{
			if (colums[j].handle)
			{
			html = html + "<td align='left'><a width='" + colums[j].width + "px' title='" + user[colums[j].name] + "'  onclick='" + colums[j].handle + "(" + user["id"] + ")'>"
					+ user[colums[j].name] + "</a></td>";
			}
			else
			{
			if (colums[j].processor)
			{
			html = html + "<td " + style + ">" + colums[j].processor(user[colums[j].name]) + "</td>";
			}
			else
			{
			if (colums[j].formatter)
			{
			html = html + "<td " + style + ">" + colums[j].formatter(user) + "</td>";
			}
			else
			{
			var cname = user[colums[j].name];
			html = html + "<td " + style + ">" + cname + "</td>";
			}
			}
			}
			}
			else
			{
			var names = colums[j].name.split(".");
			var obj = user[names[0]];
			if (obj)
			{
			if (colums[j].handle)
			{
			html = html + "<td><a  onclick='" + colums[j].handle + "(" + obj["id"] + ")'>" + obj[names[1]].replace(/</g, "&lt;") + "</a></td>";
			}
			else
			{
			if (colums[j].processor)
			{
			html = html + "<td " + style + ">" + colums[j].processor(user[obj[names[1]]]) + "</td>";
			}
			else
			{
			if (colums[j].formatter)
			{
			html = html + "<td " + style + ">" + colums[j].formatter(user) + "</td>";
			}
			else
			{
			html = html + "<td>" + obj[names[1]].replace(/</g, "&lt;") + "</td>";
			}
			}
			}
			}
			else
			{
			html = html + "<td></td>";
			}
			}
			}
			html = html + "</tr>";
			}
			return html;
		};
		return this;
	};
})(jQuery);
