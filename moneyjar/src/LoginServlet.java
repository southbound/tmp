
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.*;

/**
 * ログイン情報を操作するクラス.
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		ArrayList<Entry> categoriesEntry = null;

		// 本日の日付の取得(session格納用)
		Date date = new Date();
		SimpleDateFormat sdf_6digit = new SimpleDateFormat("yyMMdd");
		String todayStr_6digit= sdf_6digit.format(date);

		SimpleDateFormat sdf_4digit = new SimpleDateFormat("yyMM");
		String thisMonthStr_4digit= sdf_4digit.format(date);

		String submit = request.getParameter("Login");

		if (submit.equals("ログイン")) {
			String userId = request.getParameter("userId");
			String password = request.getParameter("password");

			try {
				UserDAO dao = new UserDAO();
				User user = dao.selectUserByUserId(userId, password);

				if(user != null) {

					//ユーザーごとの費目の取得
					try {
						categoriesEntry = EntryDAO.selectCategoriesByUserSN(Integer.toString(user.getUserSn()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

					//sessionの生成、セット
					HttpSession session = request.getSession(true);
					session.setAttribute("UserObj", user);
					session.setAttribute("todayStr", todayStr_6digit);
					session.setAttribute("thisMonthStr", thisMonthStr_4digit);
					session.setAttribute("categoriesEntry", categoriesEntry);

					RequestDispatcher dispatcher = request.getRequestDispatcher("DataEntry.jsp");
					dispatcher.forward(request, response);

				} else {
					request.setAttribute("message", "※ユーザID、パスワードが間違っています。");
					RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
					dispatcher.forward(request, response);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (submit.equals("ログアウト")) {
			HttpSession session = request.getSession(false);
			if(session != null) {
				session.invalidate();
			}
			response.sendRedirect("/moneyjar/Login.jsp");
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/moneyjar/Login.jsp");
	}
}