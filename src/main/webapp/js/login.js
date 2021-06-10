
var validator = null;
$(function()
{
	$("input[class=table02textfield]").qtip( {
        position : {
            my : 'center left',
            at : 'center right'
        },
        show : {
            event : 'click mouseenter focus'
        },
        style : {
            classes : 'ui-tooltip-blue ui-tooltip-shadow'
        },
        hide : {
            event : 'mouseout blur'
        }
    });
    $("input[class=table03textfield]").qtip( {
        position : {
            my : 'top right',
            at : 'bottom left'
        },
        show : {
            event : 'click mouseenter focus'
        },
        style : {
            classes : 'ui-tooltip-blue ui-tooltip-shadow'
        },
        hide : {
            event : 'mouseout blur'
        }
    });
    
	validator = $("#loginForm").validate(
	{
		rules :
		{
			'user.userid' :
			{
				required : true
			},
			'user.password' :
			{
				required : true
			},
			'code' :
			{
				required : true,
				minlength : 4
			}
		},
		messages :
		{
			'user.userid' :
			{
				required : "用户名必填"
			},
			'user.password' :
			{
				required : "密码必填"
			},
			'code' :
			{
				required : "校验码必填",
				minlength : "校验码4位"
			}
		},
		errorPlacement : function(error, element) {
            $(element).click();
        },
		submitHandler : function(form)
		{
			form.submit();
		}
	});
});
//登陆验证
function check()
{
	if (validator.form())
	{
	$("#loginForm").ajaxSubmit(
	{
		url : "adminLogin.do",
		type : "post",
		dataType : "json",
		success : function(data)
		{
			if (data.success)
			{
			location.href = "main.do";
			}
			else
			{
			if (data.message == "1")
			{
			$("#imgCode").click();
			$("#checkCode").attr("title","请重新输入验证码").click();
			}
			else
			{
			alert(data.message);
			location.reload();
			}
			}
		}
	});
	}
}
function ok(e)
{
	var _event = window.event;
	if (!_event)
	{
	_event = e;
	}
	if (_event.keyCode == 13)
	{
	check();
	}
}
//注册页面
function checkRegist()
{
	$.ajax(
	{
		url : 'regist/checkRegist.do',
		type : 'post',
		dataType : 'json',
		success : function(json)
		{
			if (json.success)
			{
			location.href = "regist/step1.do";
			}
			else
			{
			alert("人数过多，暂不能注册！");
			}
		}
	});
}