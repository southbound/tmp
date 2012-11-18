import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Entry;
import models.EntryDAO;
import models.User;

/**
 * Servlet implementation class entryServlet
 */
@WebServlet(description = "エントリ集計・表示の管理を行うサーブレット", urlPatterns = { "/show" })
public class ShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ShowServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // doPostメソッドの呼び出し
        doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

		SimpleDateFormat sdf_6digit = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat sdf_4digit = new SimpleDateFormat("yyMM");

		// ブラウザから入力された日付の取得
		String searchDate_6digit = request.getParameter("date");
		String searchMonth_4digit = request.getParameter("month");
		String allEntry = request.getParameter("entry");

		String user_sn = Integer.toString(user.getUserSn());

		//DayList.jspへ飛ばす処理
		if(searchMonth_4digit == null && allEntry == null) {
			//初期画面の処理
			//searchDate_6digitがnullならば、当日の日付を代入
			if(searchDate_6digit == null) {
				Date today = new Date();
				searchDate_6digit = sdf_6digit.format(today);
			}
			//user_snが持つエントリ情報を取得
			ArrayList<Entry> dayEntryList = null;
			try {
				dayEntryList = EntryDAO.selectByDate(user_sn, searchDate_6digit);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			//GoogleChartAPIに渡す文字列の作成
			String IMGURL_DAY = "http://chart.apis.google.com/chart?chs=500x200&cht=p3";
			String IMGURL_DAY_AMOUNT = "&chd=t:";
			String IMGURL_DAY_CATLIST = "&chl=";

			ArrayList<Entry> dayPieChartEntry = EntryDAO.selectPieChartEntryByDate(user_sn, searchDate_6digit);

			for(Entry entryRecord : dayPieChartEntry) {
				IMGURL_DAY_AMOUNT += entryRecord.getRatio() + ",";
				IMGURL_DAY_CATLIST += entryRecord.getCategoryName() + "／";
				IMGURL_DAY_CATLIST += entryRecord.getRatio() + "%|";

			}
			IMGURL_DAY_AMOUNT =IMGURL_DAY_AMOUNT.substring(0, IMGURL_DAY_AMOUNT.length()-1);
			IMGURL_DAY_CATLIST =IMGURL_DAY_CATLIST.substring(0, IMGURL_DAY_CATLIST.length()-1);
			IMGURL_DAY = IMGURL_DAY + IMGURL_DAY_AMOUNT + IMGURL_DAY_CATLIST;

			request.setAttribute("DayResult", dayEntryList);
			request.setAttribute("searchDate", searchDate_6digit);
			request.setAttribute("IMGURL_DAY", IMGURL_DAY);

			request.getRequestDispatcher("/DayList.jsp").forward(request, response);
		} else if (searchDate_6digit == null && allEntry == null) {	//MonthList.jspへ飛ばす処理
//			//初期画面の処理
//			//searchMonth_4digitがnullならば、当月の月を代入
//			if(searchMonth_4digit == null) {
//				Date today = new Date();
//				searchMonth_4digit = sdf_4digit.format(today);
//			}
			//user_snが持つエントリ情報を取得
			ArrayList<Entry> monthPieChartEntry = EntryDAO.selectPieChartEntryByMonth(user_sn, searchMonth_4digit);

			//GoogleChartAPIに渡す文字列の作成
			String IMGURL_MONTH = "http://chart.apis.google.com/chart?chs=500x200&cht=p3";
			String IMGURL_MONTH_AMOUNT = "&chd=t:";
			String IMGURL_MONTH_CATLIST = "&chl=";

			for(Entry entryRecord : monthPieChartEntry) {
				IMGURL_MONTH_AMOUNT += entryRecord.getRatio() + ",";
				IMGURL_MONTH_CATLIST += entryRecord.getCategoryName() + "／";
				IMGURL_MONTH_CATLIST += entryRecord.getRatio() + "%|";

			}
			IMGURL_MONTH_AMOUNT =IMGURL_MONTH_AMOUNT.substring(0, IMGURL_MONTH_AMOUNT.length()-1);
			IMGURL_MONTH_CATLIST =IMGURL_MONTH_CATLIST.substring(0, IMGURL_MONTH_CATLIST.length()-1);
			IMGURL_MONTH = IMGURL_MONTH + IMGURL_MONTH_AMOUNT + IMGURL_MONTH_CATLIST;

			request.setAttribute("MonthResult", monthPieChartEntry);
			request.setAttribute("searchMonth", searchMonth_4digit);
			request.setAttribute("IMGURL_MONTH", IMGURL_MONTH);

			request.getRequestDispatcher("/MonthList.jsp").forward(request, response);
		} else if(searchDate_6digit == null && searchMonth_4digit == null) { //AllEntryList.jspへ飛ばす処理
			//user_snが持つエントリ情報を取得
			ArrayList<Entry> allEntryList = null;
			allEntryList = EntryDAO.selectAllEntry(user_sn);
			request.setAttribute("allDayResult", allEntryList);
			request.getRequestDispatcher("/AllEntryList.jsp").forward(request, response);
		}



	}

}
