

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.*;

/**
 * Servlet implementation class entryServlet
 */
@WebServlet(description = "エントリの管理を行うサーブレット", urlPatterns = { "/entry" })
public class EntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EntryServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // doPostメソッドの呼び出し
        doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ブラウザからサーブレット送信されるデータに対する入力設定_済
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		//セッションの取得、なければログインページへ遷移
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("UserObj");
		if(user == null) {
			response.sendRedirect("/Login.jsp");
			return;
		}

		// ブラウザから入力されたエントリの取得
		String cat_id = request.getParameter("cat_id");
		String item_name = request.getParameter("item_name");
		String amount = request.getParameter("amount");
		String selectedDate = request.getParameter("selectedDate");
		String submit = request.getParameter("submit");

		//セッションからUSERSNを取得
		String user_sn = Integer.toString(user.getUserSn());
		String todayStr = (String) session.getAttribute("todayStr");
		String time = null;

		if(selectedDate.equals(todayStr)) {
		// TIMESTAMP文字列の取得
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			time = sdf.format(date);
		} else {
			time = selectedDate;
		}


		//入力された品目(item_name)が既知かどうかをチェック
		//既知ならitem_idを、未知なら"unknown"を返す
		String item_id = null;
		try {
			item_id = EntryDAO.checkItem(item_name, user_sn);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		//未知ならITEMテーブルに登録し、item_idを取得
		if(item_id.equals("unknown")) {
			item_id = EntryDAO.insertItem(item_name, user_sn);
		}

		//item_idでEntryを作成し、エントリ登録
		Entry entry = new Entry(user_sn, cat_id, item_id, amount, time);

		// エントリをデータベースに登録
		boolean chk = EntryDAO.storeEntry(entry);

		// データベースに登録できたら成功画面へ、失敗したら失敗画面へ。
		if(chk == true) {
			request.getRequestDispatcher("/DataEntryComplete.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/DataEntryError.jsp").forward(request, response);
		}
	}

}
