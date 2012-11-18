<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="models.*"%>
<%
	// リクエストの文字エンコードをUTF-8に設定する
	request.setCharacterEncoding("UTF-8");

	User user = (User) session.getAttribute("UserObj");
	String todayStr = (String) session.getAttribute("todayStr");
	String thisMonthStr = (String) session.getAttribute("thisMonthStr");
%>
		<div id="menu">
			<div class="wrapper">
				<a href="/moneyjar/DataEntry.jsp">支出入力</a>
				<a href="/moneyjar/show?date=<%=todayStr%>">日別取引表示</a>
				<a href="/moneyjar/show?month=<%=thisMonthStr%>">月別取引表示</a>
				<a href="/moneyjar/show?entry=all">全取引表示</a>
				<h4><%=user.getUserId() %>さん、ようこそ！</h4>
			</div>
		</div>
