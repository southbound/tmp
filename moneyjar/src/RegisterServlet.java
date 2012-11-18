
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
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String submit = request.getParameter("Register");

		if (submit.equals("登録")) {
			String userId = request.getParameter("userId");
			String password = request.getParameter("password");
			String passwordConf = request.getParameter("passwordConf");
			String mail = request.getParameter("mail");

			try {
				if (password.equals(passwordConf)) {
				boolean result = models.UserDAO.registerUser(userId, password, mail);
				if (result == true) {
					response.sendRedirect("/moneyjar/Login.jsp");
				}
				} else {
					request.setAttribute("message", "※パスワードを正しく入力してください。");
					RequestDispatcher dispatcher = request.getRequestDispatcher("Register.jsp");
					dispatcher.forward(request, response);
				}


			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			response.sendRedirect("/moneyjar/Login.jsp");

		}

	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/moneyjar/Login.jsp");
	}
}
