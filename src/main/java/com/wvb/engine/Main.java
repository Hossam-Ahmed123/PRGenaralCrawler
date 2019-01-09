/*


 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wvb.engine;

import com.wvb.connection.ConnectionManager;
import com.wvb.crawlers.HTMLCrawler;
import com.wvb.crawlers.PDFCrawler;
import com.wvb.factory.ParentCrawler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

/**
 *
 * @author AL-ME3RAJ
 */
public class Main {

	static Connection con;
	static org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
	static ParentCrawler crawler;
	static {
		try {
			if (con == null || con.isClosed()) {
				con = ConnectionManager.connectDataBase();
			}
		} catch (Exception e) {
			logger.error("Error", e);
			System.out.println(e);
		}

	}

	public static void main(String[] args) throws Exception {
		// readHtmlFromDataSource();
		readPDFFromDataSource();
	}

	private static void readHtmlFromDataSource() {
		PreparedStatement preparedStmt = null;
		try {

			String sql = "select WVB_NUMBER , PARNT_URL , KEYWORDS ,DOMAIN, COMPANY_PERM_ID ,DATEWITHTITLE,CRAWLER ,WHENNOTFOUNDTITLE  from PR_CONFIG where CONFIGER=1 and STATUS ='true' and CONTENTTYPE='html' and COMPANY_PERM_ID =30552 ";
			try {
				if (con == null || con.isClosed()) {
					con = ConnectionManager.connectDataBase();
				}
			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
			}

			preparedStmt = con.prepareStatement(sql);

			ResultSet rs = preparedStmt.executeQuery();
			crawler = new HTMLCrawler();
			while (rs.next()) {
				if (rs.getString(7).equals("S")) {

					crawler.crawleBySelenium(rs.getString(2), rs.getString(1),
							rs.getInt(5), rs.getString(3), rs.getString(4),
							rs.getString(6), rs.getString(8));
				} else {
					crawler.crawleByJsoup(rs.getString(2), rs.getString(1),
							rs.getInt(5), rs.getString(3), rs.getString(4),
							rs.getString(6), rs.getString(8));
				}

			}

		} catch (SQLException ex) {
			ex.printStackTrace();

		}

	}

	private static void readPDFFromDataSource() {
		PreparedStatement preparedStmt = null;
		try {

			String sql = "select WVB_NUMBER , PARNT_URL , KEYWORDS ,DOMAIN, COMPANY_PERM_ID ,DATEWITHTITLE,CRAWLER from PR_CONFIG where CONFIGER=1 and STATUS ='true' and CONTENTTYPE='pdf' and COMPANY_PERM_ID =2553344 ";
			try {
				if (con == null || con.isClosed()) {
					con = ConnectionManager.connectDataBase();
				}
			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
			}

			preparedStmt = con.prepareStatement(sql);

			ResultSet rs = preparedStmt.executeQuery();
			crawler = new PDFCrawler();
			while (rs.next()) {
				if (rs.getString(7).equals("S")) {
					crawler.crawleBySelenium(rs.getString(2), rs.getString(1),
							rs.getInt(5), rs.getString(3), rs.getString(4),
							rs.getString(6), "");
				} else {
					crawler.crawleByJsoup(rs.getString(2), rs.getString(1),
							rs.getInt(5), rs.getString(3), rs.getString(4),
							rs.getString(6), "");
				}

				// break;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();

		}

	}
}
