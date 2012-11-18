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
	String searchMonth = (String) request.getAttribute("searchMonth");
	String IMGURL_MONTH = (String) request.getAttribute("IMGURL_MONTH");
	int thisMonthInt = Integer.parseInt(thisMonthStr);
	ArrayList<Entry> entrySearchResult = (ArrayList<Entry>) request.getAttribute("MonthResult");
	long total = 0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>money.jar | 月別データ表示</title>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
	<jsp:include page="/header.jsp" />
	<div id="wrapper">
	<jsp:include page="/menu.jsp" />
		<div id="main">
			<h2><%=DateUtils.StrToMonth(searchMonth)%> 費目別支出リスト</h2>

			<table border=1>
				<tr>
					<th>費目</th>
					<th>金額</th>
				</tr>

				<%
					for (Entry entryRecord : entrySearchResult) {
				%>
				<tr class="item">
					<td><%=entryRecord.getCategoryName()%></td>
					<td>\<%=DateUtils.StrToDecimal(entryRecord.getAmount())%></td>
				</tr>
				<%
					total += Integer.parseInt(entryRecord.getAmount());
					}
				%>
				<tr>
					<td colspan="2" align="right"><b>合計（税別）\<%=DateUtils.StrToDecimal(total)%></b></td>
				</tr>

			</table>

			<h2><%=DateUtils.StrToMonth(searchMonth)%> 費目別支出グラフ</h2>
			<img src="<%=IMGURL_MONTH%>" />
				<div class="center">
				<form action="show" method="post">
				<select name="month">
					<%
						for (int i = 0; i < 3; i++) {
					%>
					<option value="<%=thisMonthInt - i%>"><%=DateUtils.StrToMonth(Integer.toString(thisMonthInt
						- i))%></option>
					<%
						}
					%>
				</select> <input type="submit" name="submit" value="月変更" />
				</form>
				</div>

		</div>
	</div>
		<h6>Copyright (C) 2012 FutureArchitect money.jar All Rights Reserved.</h6>
</body>
</html>