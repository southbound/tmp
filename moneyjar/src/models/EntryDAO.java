/**
 * このクラスはエントリが確定されたときに、エントリをデータベースに保存する機能を提供します.
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utils.DBAccessUtils;


public class EntryDAO {

	//エントリ履歴取得SQL
	private static final String SELECT_BY_DATE =
				  " SELECT"
				+ "     EX.USER_SN"
				+ "   , DATE_FORMAT(date(TIME), '%y%m%d') AS BUYDATE"
				+ "   , CAT.NAME AS CAT_NAME"
				+ "   , IT.NAME AS ITEM_NAME"
				+ "   , EX.AMOUNT AS AMOUNT"
				+ " FROM"
				+ "   EXCHANGE EX"
				+ " INNER JOIN"
				+ "   CATEGORY CAT ON EX.CAT_ID = CAT.ID"
				+ " INNER JOIN"
				+ "   ITEM IT ON EX.ITEM_ID = IT.ID"
				+ " WHERE"
				+ "   EX.USER_SN = ?"
				+ " AND"
				+ "   DATE_FORMAT(date(TIME), '%y%m%d') = ?"
				+ " ORDER BY"
				+ "   BUYDATE ASC";

	//費目名・費目ID取得SQL
	private static final String SELECT_CATEGORIES =
				   " SELECT"
				 + "     ID AS CAT_ID"
				 + "   , NAME AS CAT_NAME"
				 + " FROM"
				 + "   CATEGORY"
				 + " WHERE"
				 + "   USER_SN = 0000"
				 + " OR"
				 + "   USER_SN = ?"
				 + " ORDER BY"
				 + "   ID ASC";

	//品目名・品目ID取得SQLPIECHART_BY_DATE
	private static final String SELECT_ITEMS =
				   " SELECT"
				 + "     ID AS ITEM_ID"
				 + "   , NAME AS ITEM_NAME"
				 + " FROM"
				 + "   ITEM"
				 + " WHERE"
				 + "   USER_SN = ?"
				 + " AND"
				 + "   NAME = ?"
				 + " ORDER BY"
				 + "   ID ASC";

	//日別円グラフ取得SQL
	private static final String PIECHART_BY_DATE =
			 " SELECT"
		   + "     CAT.NAME AS CAT_NAME"
		   + "   , SUM(AMOUNT) AS AMOUNT"
		   + "   , TRUNCATE(SUM(AMOUNT) * 100 / (select SUM(AMOUNT) from EXCHANGE where USER_SN = ? and DATE_FORMAT(date(TIME), '%y%m%d') = ?)+ .05, 1) AS RATIO"
		   + " FROM"
		   + "   EXCHANGE EX"
		   + " INNER JOIN"
		   + "   CATEGORY CAT ON EX.CAT_ID = CAT.ID"
		   + " WHERE"
		   + "   EX.USER_SN = ?"
		   + " AND"
		   + "   DATE_FORMAT(date(TIME), '%y%m%d') = ?"
		   + " GROUP BY"
		   + "   CAT.NAME"
		   + " ORDER BY"
		   + "   AMOUNT DESC";

	//月別円グラフ取得SQL
	private static final String PIECHART_BY_MONTH =
			 " SELECT"
		   + "     CAT.NAME AS CAT_NAME"
		   + "   , SUM(AMOUNT) AS AMOUNT"
		   + "   , TRUNCATE(SUM(AMOUNT) * 100 / (select SUM(AMOUNT) from EXCHANGE where USER_SN = ? and DATE_FORMAT(date(TIME), '%y%m') = ?)+ .05, 1) AS RATIO"
		   + " FROM"
		   + "   EXCHANGE EX"
		   + " INNER JOIN"
		   + "   CATEGORY CAT ON EX.CAT_ID = CAT.ID"
		   + " WHERE"
		   + "   EX.USER_SN = ?"
		   + " AND"
		   + "   DATE_FORMAT(date(TIME), '%y%m') = ?"
		   + " GROUP BY"
		   + "   CAT.NAME"
		   + " ORDER BY"
		   + "   AMOUNT DESC";

	//全エントリ履歴取得SQL
	private static final String SELECT_ALL_ENTRY_BY_USERSN =
						 " SELECT"
					   + "     EX.USER_SN"
					   + "   , DATE_FORMAT(date(TIME), '%y%m%d') AS BUYDATE"
					   + "   , CAT.NAME AS CAT_NAME"
					   + "   , IT.NAME AS ITEM_NAME"
					   + "   , EX.AMOUNT AS AMOUNT"
					   + " FROM"
					   + "   EXCHANGE EX"
					   + " INNER JOIN"
					   + "   CATEGORY CAT ON EX.CAT_ID = CAT.ID"
					   + " INNER JOIN"
					   + "   ITEM IT ON EX.ITEM_ID = IT.ID"
					   + " WHERE"
					   + "   EX.USER_SN = ?"
					   + " ORDER BY"
					   + "   BUYDATE DESC";



	/**
	 * エントリ情報を受け取って、データベースにエントリ情報を保存します.
	 *
	 *
	 * @param items
	 *            購入した書籍情報を表すItemオブジェクトの配列
	 * @param order
	 *            配送先情報を格納したOrderオブジェクト
	 * @return データベースへの書き込みの成否（true=成功, false=失敗）
	 */

	public static boolean storeEntry(Entry entry) {
		boolean ret = false;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			con = DBAccessUtils.getConnection();

			// 自動コミットをオフにしてトランザクションで保護
			con.setAutoCommit(false);
			// Statementオブジェクトを取得
			stmt = con.createStatement();
			// EXCHANGEテーブルSNの最大値を取得
			rs = stmt.executeQuery("SELECT MAX(SN) FROM EXCHANGE");
			rs.next();
			long maxExchangeSN = rs.getLong(1) + 1;

			String sqlorder = "INSERT INTO EXCHANGE";
			sqlorder += " VALUES(";
			sqlorder +=
				String.valueOf(maxExchangeSN) + ", " + "'"
				+ entry.getUser() + "', " + "'"
				+ entry.getTime() + "', " + "'"
				+ entry.getAmount() + "', " + "'"
				+ entry.getCategoryID() + "', " + "'"
				+ entry.getItemID() + "')";

			stmt.executeUpdate(sqlorder);

			con.commit();

			ret = true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {

			try {
				if (con != null) {
					con.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	public static ArrayList<Entry> selectByDate(String userSN, String date_6digit)
			throws ClassNotFoundException {

		ArrayList<Entry> entryDateArrayList = new ArrayList<Entry>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBAccessUtils.getConnection();

			pstmt = con.prepareStatement(SELECT_BY_DATE);

			// プレースホルダ―にセット
			pstmt.setInt(1, Integer.parseInt(userSN));
			pstmt.setString(2, date_6digit);
			rs = pstmt.executeQuery();

			//検索結果を格納したEntryインスタンスをArrayListに追加
			while(rs.next()) {
				Entry resultEntry = new Entry();

				resultEntry.setCategoryName(rs.getString("CAT_NAME"));
				resultEntry.setItemName(rs.getString("ITEM_NAME"));
				resultEntry.setAmount(rs.getString("AMOUNT"));

				entryDateArrayList.add(resultEntry);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBAccessUtils.closeResources(rs, pstmt, con);
		}
		return entryDateArrayList;
	}
	public static ArrayList<Entry> selectCategoriesByUserSN(String userSN)
			throws ClassNotFoundException {

		ArrayList<Entry> categoriesList = new ArrayList<Entry>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBAccessUtils.getConnection();

			pstmt = con.prepareStatement(SELECT_CATEGORIES);

			// プレースホルダ―にセット
			pstmt.setInt(1, Integer.parseInt(userSN));
			rs = pstmt.executeQuery();

			//検索結果を格納したEntryインスタンスをArrayListに追加
			while(rs.next()) {
				Entry resultEntry = new Entry();

				resultEntry.setCategoryID(rs.getString("CAT_ID"));
				resultEntry.setCategoryName(rs.getString("CAT_NAME"));

				categoriesList.add(resultEntry);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBAccessUtils.closeResources(rs, pstmt, con);
		}
		return categoriesList;
	}

	public static String checkItem(String item_name, String userSN)
			throws ClassNotFoundException {

		String item_id = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBAccessUtils.getConnection();

			pstmt = con.prepareStatement(SELECT_ITEMS);

			// プレースホルダ―にセット
			pstmt.setInt(1, Integer.parseInt(userSN));
			pstmt.setString(2, item_name);
			rs = pstmt.executeQuery();

			//検索結果をからitem_id, item_nameを取得
			rs.next();

			item_id = rs.getString("ITEM_ID");



		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBAccessUtils.closeResources(rs, pstmt, con);
		}
		if(item_id == null) {
			item_id = "unknown";
		}
		return item_id;
	}

	public static String insertItem(String item_name, String userSN) {
		String item_id = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			con = DBAccessUtils.getConnection();

			// 自動コミットをオフにしてトランザクションで保護
			con.setAutoCommit(false);
			// Statementオブジェクトを取得
			stmt = con.createStatement();
			// ITEMテーブルIDの最大値を取得
			rs = stmt.executeQuery("SELECT MAX(ID) FROM ITEM");
			rs.next();
			long maxItemSN = rs.getLong(1) + 1;

			String sqlorder = "INSERT INTO ITEM";
			sqlorder += " VALUES(";
			sqlorder +=
				String.valueOf(maxItemSN) + ", " + "'"
				+ item_name + "', " + "'"
				+ userSN + "')";

			stmt.executeUpdate(sqlorder);

			con.commit();

			item_id = Long.toString(maxItemSN);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {

			try {
				if (con != null) {
					con.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return item_id;
	}

	public static ArrayList<Entry> selectPieChartEntryByDate(String userSN, String date_6digit) {

		ArrayList<Entry> dayPieChartEntryArrayList = new ArrayList<Entry>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBAccessUtils.getConnection();

			pstmt = con.prepareStatement(PIECHART_BY_DATE);

			// プレースホルダ―にセット
			pstmt.setInt(1, Integer.parseInt(userSN));
			pstmt.setString(2, date_6digit);
			pstmt.setInt(3, Integer.parseInt(userSN));
			pstmt.setString(4, date_6digit);
			rs = pstmt.executeQuery();

			//検索結果を格納したEntryインスタンスをArrayListに追加
			while(rs.next()) {
				Entry resultEntry = new Entry();

				resultEntry.setCategoryName(rs.getString("CAT_NAME"));
				resultEntry.setAmount(rs.getString("AMOUNT"));
				resultEntry.setRatio(rs.getString("RATIO"));

				dayPieChartEntryArrayList.add(resultEntry);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBAccessUtils.closeResources(rs, pstmt, con);
		}
	return dayPieChartEntryArrayList;
	}

	public static ArrayList<Entry> selectPieChartEntryByMonth(String userSN, String month_4digit) {

		ArrayList<Entry> monthPieChartEntryArrayList = new ArrayList<Entry>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBAccessUtils.getConnection();

			pstmt = con.prepareStatement(PIECHART_BY_MONTH);

			// プレースホルダ―にセット
			pstmt.setInt(1, Integer.parseInt(userSN));
			pstmt.setString(2, month_4digit);
			pstmt.setInt(3, Integer.parseInt(userSN));
			pstmt.setString(4, month_4digit);
			rs = pstmt.executeQuery();

			//検索結果を格納したEntryインスタンスをArrayListに追加
			while(rs.next()) {
				Entry resultEntry = new Entry();

				resultEntry.setCategoryName(rs.getString("CAT_NAME"));
				resultEntry.setAmount(rs.getString("AMOUNT"));
				resultEntry.setRatio(rs.getString("RATIO"));

				monthPieChartEntryArrayList.add(resultEntry);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBAccessUtils.closeResources(rs, pstmt, con);
		}
	return monthPieChartEntryArrayList;
	}

	public static ArrayList<Entry> selectAllEntry(String userSN) {

		ArrayList<Entry> allEntryArrayList = new ArrayList<Entry>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DBAccessUtils.getConnection();

			pstmt = con.prepareStatement(SELECT_ALL_ENTRY_BY_USERSN);

			// プレースホルダ―にセット
			pstmt.setInt(1, Integer.parseInt(userSN));
			rs = pstmt.executeQuery();

			//検索結果を格納したEntryインスタンスをArrayListに追加
			while(rs.next()) {
				Entry resultEntry = new Entry();

				resultEntry.setCategoryName(rs.getString("CAT_NAME"));
				resultEntry.setItemName(rs.getString("ITEM_NAME"));
				resultEntry.setAmount(rs.getString("AMOUNT"));
				resultEntry.setTime(rs.getString("BUYDATE"));

				allEntryArrayList.add(resultEntry);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBAccessUtils.closeResources(rs, pstmt, con);
		}
	return allEntryArrayList;
	}
}