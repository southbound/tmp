package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBAccessUtils;

public class UserDAO {

	private static final String SELECT_USER_SQL = "select SN, ID, PW from USER where ID = ? and PW = ?";
	private static final String UPDATE_LOGIN_TIME_SQL = "update USER set LOGIN_TIME = now() where ID = ?";

	public User selectUserByUserId(String userId, String password)
			throws SQLException {

		User user = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBAccessUtils.getConnection();
			pstmt = con.prepareStatement(SELECT_USER_SQL);

			pstmt.setString(1, userId);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();

			while(rs.next()) {
			user = new User();
			user.setUserSn(rs.getInt("SN"));
			user.setUserId(rs.getString("ID"));
			user.setPassword(rs.getString("PW"));
			}

			if (user != null) {
				pstmt = con.prepareStatement(UPDATE_LOGIN_TIME_SQL);
				pstmt.setString(1, userId);
				pstmt.executeUpdate();

			}

			return user;

		} finally {
			DBAccessUtils.closeResources(rs, pstmt, con);
		}
	}

	public static boolean registerUser(String userId, String password, String mail)
			throws SQLException {

		Connection con = null;
		PreparedStatement pstmt = null;
		boolean result = false;


		final String REGISTER_USER_SQL = "INSERT INTO USER (ID, PW, MAIL, ADMIN_FLAG, REGIST_TIME, LOGIN_TIME)"
				+ "VALUES (?, ?, ?, 'F', NOW(), NOW())";

		try {
			con = DBAccessUtils.getConnection();
			pstmt = con.prepareStatement(REGISTER_USER_SQL);

			pstmt.setString(1, userId);
			pstmt.setString(2, password);
			pstmt.setString(3, mail);
			pstmt.executeUpdate();
			result = true;
			return result;

		} finally {
			DBAccessUtils.closeResources(null, pstmt, con);
		}
	}
}
