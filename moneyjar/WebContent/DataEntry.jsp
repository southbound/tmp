<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="models.*"%>
<%@ page import="utils.*"%>
<%@ page import="java.util.ArrayList"%>
<%
	// リクエストの文字エンコードをUTF-8に設定する
	request.setCharacterEncoding("UTF-8");

	// ログインチェック：セッションにユーザーがいない場合はログインページへ遷移
	User user = (User) session.getAttribute("UserObj");
	if (user == null) {
		response.sendRedirect("/Login.jsp");
		return;
	}

	String todayStr = (String) session.getAttribute("todayStr");
	String thisMonthStr = (String) session.getAttribute("thisMonthStr");
	int todayInt = Integer.parseInt(todayStr);
	ArrayList<Entry> categoriesEntry = (ArrayList<Entry>) session
			.getAttribute("categoriesEntry");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支出登録 | money.jar</title>
<!--viewportの指定-->
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<!--/viewportの指定-->
<!--メディアクエリでPC用・スマホ用CSSを振り分け-->
<link media="only screen and (min-device-width:501px)" href="css/common.css" type="text/css" rel="stylesheet" />
<link media="only screen and (max-device-width:500px)" href="css/mobile.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="js/validate.js"></script>
<script type="text/javascript">
var myForm = new Validate();
</script>
</head>
<body>

	<jsp:include page="/header.jsp" />

	<div id="wrapper">
	<jsp:include page="/menu.jsp" />

		<div id="main">
			<h2>
				<%=DateUtils.StrToDate(todayStr)%> 支出入力</h2>

			<p>
			<form action="entry" method="post" name="entry">
			<fieldset id="entry">
				<label for="selectedDate">日付：</label> <select id="selectedDate"
					name="selectedDate">
					<%
						for (int i = 0; i < 7; i++) {
					%>
					<option value="<%=todayInt - i%>"><%=DateUtils.StrToDate(Integer.toString(todayInt - i))%></option>
					<%
						}
					%>
				</select>

				<p class="innerfield">
					<label for="cat_id">費目：</label> <select id="cat_id" name="cat_id">
						<%
							for (Entry categoryRecord : categoriesEntry) {
						%>

						<option value="<%=categoryRecord.getCategoryID()%>"><%=categoryRecord.getCategoryName()%></option>

						<%
							}
						%>
					</select>
				</p>
				<p class="innerfield">
					<label for="item_name">品目：</label> <input type="text"
						id="item_name" name="item_name" />
				</p>
				<p class="innerfield">
					<label for="amount">金額：</label> <input type="text" id="amount"
						name="amount" />
				</p>

				<div class="center">
				<input type="submit" value="保存"
				onClick="if(!myForm.isNotEmpty(document.entry.item_name.value)) {alert('品目を入力して下さい。'); return false;}if(!myForm.isNotEmpty(document.entry.amount.value)) {alert('金額を入力して下さい。'); return false;} else { if(!myForm.isNum(document.entry.amount.value)) {alert('金額は半角数字、正の整数で入力して下さい。');return false;}}"/>
				<input type="reset" value="取消""/>
				</div>
			</fieldset>
			</form>
		</div>
	</div>
		<h6>Copyright (C) 2012 FutureArchitect money.jar All Rights Reserved.</h6>
</body>
</html>