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
	String searchDate = (String) request.getAttribute("searchDate");
	String IMGURL_DAY = (String) request.getAttribute("IMGURL_DAY");
	int todayInt = Integer.parseInt(todayStr);
	ArrayList<Entry> entrySearchResult = (ArrayList<Entry>) request.getAttribute("DayResult");
	long total = 0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>money.jar | 日別データ表示</title>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
	<jsp:include page="/header.jsp" />

	<div id="wrapper">
	<jsp:include page="/menu.jsp" />
		<div id="main">
			<h2><%=DateUtils.StrToDate(searchDate)%> 品目別支出リスト</h2>

			<table border=1>
				<tr>
					<th>費目</th>
					<th>品目</th>
					<th>金額</th>
				</tr>

				<%
					for (Entry entryRecord : entrySearchResult) {
				%>
				<tr class="item">
					<td><%=entryRecord.getCategoryName()%></td>
					<td><%=entryRecord.getItemName()%></td>
					<td>\<%=DateUtils.StrToDecimal(entryRecord.getAmount())%></td>
				</tr>
				<%
					total += Integer.parseInt(entryRecord.getAmount());
					}
				%>
				<tr>
					<td colspan="3" align="right"><b>合計（税別）\<%=DateUtils.StrToDecimal(total)%></b></td>
				</tr>

			</table>

			<h2><%=DateUtils.StrToDate(searchDate)%> 費目別支出グラフ</h2>
			<img src="<%=IMGURL_DAY%>" />
			<div class="center">
			<form action="show" method="post">
				<select name="date">
					<%
						for (int i = 0; i < 7; i++) {
					%>
					<option value="<%=todayInt - i%>"><%=DateUtils.StrToDate(Integer.toString(todayInt - i))%></option>
					<%
						}
					%>
				</select> <input type="submit" name="submit" value="日付変更" />
			</form>
			</div>
		</div>
	</div>
	<h6>Copyright (C) 2012 FutureArchitect money.jar All Rights Reserved.</h6>
</body>
</html>