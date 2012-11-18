// Copyright (c) 2012 by Future Architect, Inc. Japan
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DBアクセス用のユーティリティクラス.
 *
 * @author kohara
 * @version 2012/06/04
 */
public class DBAccessUtils {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	//会社接続用
//	private static final String JDBC_URL = "jdbc:mysql://10.3.134.101/test?useUnicode=true&characterEncoding=UTF-8";// ★各自変更すること
//	private static final String JDBC_USER = "nakai";// ★各自変更すること
//	private static final String JDBC_PASSWORD = "nakai";// ★各自変更すること

	//中井自宅用
	private static final String JDBC_URL = "jdbc:mysql://localhost/moneyjar?useUnicode=true&characterEncoding=UTF-8";// ★各自変更すること
	private static final String JDBC_USER = "root";// ★各自変更すること
	private static final String JDBC_PASSWORD = "touit";// ★各自変更すること



	/**
	 * DBとのコネクションを生成する。 呼び出し元で必ずコネクションのクローズ処理を行う。
	 *
	 * @return DBとのコネクション
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
		//con.setAutoCommit(false);
		return con;
	}

	/**
	 * リソースを全てcloseする。
	 *
	 * @param rs
	 * @param pstmt
	 * @param con
	 */
	public static void closeResources(ResultSet rs, PreparedStatement pstmt,
			Connection con) {
		// TODO 8: 各種リソースのクローズ処理を実装s

		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (pstmt != null) {
				pstmt.close();
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

	/**
	 * PreparedStatementをcloseする。
	 *
	 * @param pstmt
	 */
	public static void closePreparedStatement(PreparedStatement pstmt) {
		closeResources(null, pstmt, null);
	}

}
