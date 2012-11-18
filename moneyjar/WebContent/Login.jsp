<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--viewportの指定-->
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<!--/viewportの指定-->
<!--メディアクエリでPC用・スマホ用CSSを振り分け-->
<link media="only screen and (min-device-width:1001px)"
	href="css/common.css" type="text/css" rel="stylesheet" />
<link media="only screen and (max-device-width:1000px)"
	href="css/mobile.css" type="text/css" rel="stylesheet" />

<title>money.jar | ログイン</title>
<!--viewportの指定-->
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<!--/viewportの指定-->
<!--メディアクエリでPC用・スマホ用CSSを振り分け-->
<link media="only screen and (min-device-width:1001px)"
	href="css/common.css" type="text/css" rel="stylesheet" />
<link media="only screen and (max-device-width:1000px)"
	href="css/mobile.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript" src="js/validate.js"></script>
<script type="text/javascript">
var myForm = new Validate();
</script>
</head>
<body>
	<h1>
		<a href="/moneyjar/"><img src="img/moneyjar_logo.png"></a>
	</h1>
	<div class="RegisterForm">

		<h2>money.jar | ログイン</h2>
<%
	String message = (String)request.getAttribute("message");
 	if (message != null) {
 	out.println(message);
 	}
%>
		<form action="login" method="post" name="login">
			<p>
			<div class="line">
				<div class="label">ユーザーID：</div>
				<div class="control">
					<input type="text" name="userId" />
				</div>
			</div>
			</p>
			<p>
			<div class="line">
				<div class="label">パスワード ：</div>
				<div class="control">
					<input type="password" name="password" />
				</div>
			</div>
			</p>
			<p>
				<input type="submit" name="Login" value="ログイン" 
				onClick="if(!myForm.isNotEmpty(document.login.userId.value)) {alert('ユーザー名を入力して下さい。'); return false;}if(!myForm.isNotEmpty(document.login.password.value)) {alert('パスワードを入力して下さい。'); return false;}"/> <input
					type="reset" name="Reset" value="リセット" />
			</p>
			<p>
				<a href="/moneyjar/Register.jsp" class="reg">新規登録はこちらから</a>
			</p>
		</form>

	</div>
	<h6>Copyright (C) 2012 FutureArchitect money.jar All Rights
		Reserved.</h6>
</body>
</html>