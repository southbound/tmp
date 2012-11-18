<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// リクエストの文字エンコードをUTF-8に設定する
	request.setCharacterEncoding("UTF-8");

	// ログインチェック：セッションにユーザーがいない場合はログインページへ遷移
//	User user = (User) session.getAttribute(Const.S_USER_OBJ);
//	if (user == null) {
//		response.sendRedirect(Login.jsp);
//		return;
//	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Refresh" content="1; URL=/moneyjar/DataEntry.jsp">
<title>エントリ登録完了</title>
</head>
<body>
<h2>エントリ登録完了</h2>
</body>
</html>