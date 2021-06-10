<%@page pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>${initParam.WebAppName}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=9" />
		<meta http-equiv="Content-Language" content="zh-CN" />
		<!-- 已知需要添加公共资源（其他资源根据需要添加，不需要的css,js待清理）begin -->
		<link rel="stylesheet" type="text/css" href="css/login.css">
		<link rel="stylesheet" type="text/css" href="css/jquery.qtip.css">

		<script type="text/javascript" src="js/jquery-1.8.0.js"></script>
		<script type="text/javascript" src="js/jquery.form.js"></script>
		<script type="text/javascript" src="js/jquery.qtip.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.validate.js"></script>
		<script type="text/javascript" src="js/login.js"></script>
	</head>
	<body>
		<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="center" valign="middle">

					<table width="958" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="4" style="background: url(images/login_02.jpg) no-repeat; height: 142px">
							</td>
						</tr>
						<tr>
							<td colspan="4" class='login-logo'>
								${initParam.WebAppName}
							</td>
						</tr>
						<tr>
							<td width="197" style="background: url(images/login_08.jpg) no-repeat; height: 152px">
							</td>
							<td width="258" style="background: url(images/login_04.jpg) no-repeat; height: 152px">
							</td>
							<td width="269" align="left" background="images/login_05.jpg">
								<form id='loginForm'>
									<table width="264" border="0" cellspacing="0" cellpadding="0">

										<tr>
											<td width="62" class="table01">
												用户名：
											</td>
											<td colspan="2" class="table01">
												<label>
													<input type="text" name="loginName" id="userid" title="请输入用户名" class="table02textfield" maxlength="30">
												</label>
											</td>
										</tr>
										<tr>
											<td class="table01">
												密&nbsp;&nbsp;码：
											</td>
											<td colspan="2" class="table01">
												<input type="password" name="password" id="pwd" maxlength="30" title="请输入密码" class="table02textfield">
											</td>
										</tr>
										<tr>
											<td class="table01">
												验证码：
											</td>
											<td colspan="2">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td>
															<input class="table03textfield" type="text" name="code" id="checkCode" title="请输入验证码" style="width: 85px; margin: 0px" size="4" onkeyup="ok(event)">
														</td>
														<td>
															<div style="height: 24px; margin-left: 15px">
																<img src='image/validateCode.do' id="imgCode" title="点击刷新" onclick="$(this).attr('src','image/validateCode.do?ts='+new Date().getTime())" />
															</div>
														</td>
													</tr>
												</table>
											</td>

										</tr>
										<tr>
											<td height="39" class="table01">
												&nbsp;
											</td>
											<td width="86">
												<a class='loginBtn' onclick="check()">登录</a>
											</td>
										</tr>
									</table>
								</form>
							</td>
							<td width="234" style="background: url(images/login_06.jpg) no-repeat; height: 152px">
							</td>
						</tr>
						<tr>
							<td height="85" colspan="4" align="center" valign="top" class="table02" style="padding-top: 8px; background: url(images/login_07.jpg) no-repeat;">
								*注：请输入用户名和密码进行登录
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
		