/*


 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wvb.engine;

import com.wvb.connection.ConnectionManager;
import com.wvb.factory.CrawlerFactory;
import com.wvb.factory.ParentCrawler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		CrawlerFactory crawlerFactory = new CrawlerFactory();
		readHtmlFromDataSource(crawlerFactory);

	}

	private static void readHtmlFromDataSource(CrawlerFactory factory) {

		PreparedStatement preparedStmt = null;
		try {

			String sql = "select WVB_NUMBER , PARNT_URL , KEYWORDS ,DOMAIN, COMPANY_PERM_ID ,DATEWITHTITLE,CRAWLER ,WHENNOTFOUNDTITLE,CONTENTTYPE from PR_CONFIG where CONFIGER=1 and STATUS ='true' ORDER BY COMPANY_PERM_ID DESC  ";
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

			while (rs.next()) {
				String type = rs.getString(9);
				if (type.equals("html")) {
					crawler = factory.getCrawler("HTML");
				} else if (type.equals("pdf")) {
					crawler = factory.getCrawler("PDF");
				}

				System.out.println("Company Perm id " + rs.getString(5));
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

}
