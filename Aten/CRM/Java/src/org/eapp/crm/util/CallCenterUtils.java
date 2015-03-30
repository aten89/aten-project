package org.eapp.crm.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.eapp.comobj.SessionAccount;
import org.eapp.util.spring.SpringHelper;

public class CallCenterUtils {

	private static DataSource dataSource = (DataSource) SpringHelper.getBean("dataSource");
//	private static String sessionUserKey = org.eapp.client.util.SystemProperties.SESSION_USER_KEY;
	private static String sessionUserKey = "sessionUser";//EAPP系统的SESSIONKEY
		
	public static String getStaffInfo(HttpSession httpSession) {
		SessionAccount user = (SessionAccount)httpSession.getAttribute(sessionUserKey);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String userAccount = user.getAccountID();
			String staffNO = userAccount;
			if (staffNO.startsWith("QY")) {
				staffNO = staffNO.substring(2);//工号后四位，如QY0001取0001
			}
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select STAFF_ID_,STAFF_NO_ from crm_cc_staff_info where STAFF_NO_=?");
			stmt.setString(1, staffNO);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1) + ";" + rs.getString(2);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
