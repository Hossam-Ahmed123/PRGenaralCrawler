/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wvb.dto;

import com.wvb.apis.FixDate;
import com.wvb.connection.ConnectionManager;
import com.wvb.model.DataModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import org.slf4j.LoggerFactory;

/**
 *
 * @author AL-ME3RAJ
 */
public class DAO {
	static org.slf4j.Logger logger = LoggerFactory.getLogger(DAO.class);
	static Connection con;

	static {
		try {
			if (con == null || con.isClosed()) {
				con = ConnectionManager.connectDataBase();
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}

	}

	// public static void addToDb2(ArrayList<DataModel> list) {
	// /* insert data to database */
	// try {
	//
	// String query = "insert into NEWS (NEWSID , "
	// + "NEWSBODY,"
	// + "NEWSTITLE,"
	// + "NEWSCATGID,"
	// + "WHOCREATED,"
	// + "DTCREATED,"
	// + "COMPNYID,"
	// + "PUBLISHDATE,NEWSURL,ATTCHMENT ) "
	// + " select NEWSSEQ.nextval , ? , ? ,?,'wvb',sysdate, ? , ?, ? , ? from dual "
	// + " where not exists ( select * from NEWS WHERE PUBLISHDATE = ? and
	// NEWSTITLE=? and COMPNYID=? ) ";
	// System.out.println("start query " + query);
	// PreparedStatement ps = con.prepareStatement(query);
	// for (DataModel dm : list) {
	//
	// try {
	// File blob = new File(dm.getOfflinePath());
	// FileInputStream in = new FileInputStream(blob);
	// ps.setString(1, dm.getDiscription());
	// ps.setString(2, dm.getNewsTitle());
	// ps.setInt(3, dm.getNewsID());
	// ps.setInt(4, dm.getCompanyId());
	// ps.setDate(5, dm.getDate());
	// ps.setString(6, dm.getDataSheetUrl());
	// ps.setBlob(7, in);
	// ps.setDate(8, dm.getDate());
	//
	// ps.setString(9, dm.getNewsTitle());
	// ps.setInt(10, dm.getCompanyId());
	// ps.addBatch();
	// } catch (Exception e) {
	//
	// }
	//
	// }
	// ps.executeBatch();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	public static int getCompanyID(int companyPermId) {
		PreparedStatement preparedStmt = null;

		int cID = 0;
		try {

			String sql = "select COMPNYID  from COMPANIES where COMPNYPIRMID = ?";
			try {
				if (con == null || con.isClosed()) {
					con = ConnectionManager.connectDataBase();
				}
			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
			}

			preparedStmt = con.prepareStatement(sql);
			preparedStmt.setInt(1, companyPermId);
			ResultSet rs = preparedStmt.executeQuery();
			if (rs.next()) {
				cID = rs.getInt(1);
				logger.info("Company ID =====> " + cID);

			}
		} catch (SQLException ex) {
			logger.error("Error", ex);
			System.out.println(ex);

		}

		return cID;

	}

	@SuppressWarnings("unlikely-arg-type")
	public static void addToDb(Set<DataModel> mySet) {
		/* insert data to database */
		try {
			try {
				if (con == null || con.isClosed()) {
					con = ConnectionManager.connectDataBase();
				}
			} catch (Exception e) {
				System.out.println("Error" + e);
			}

			String query = "insert into NEWS (NEWSID , " + "ATTCHMENT," + "NEWSTITLE," + "NEWSCATGID," + "WHOCREATED,"
					+ "DTCREATED," + "COMPNYID," + "PUBLISHDATE,NEWSURL, BYGENERALCRAWLER) "
					+ " select  NEWSSEQ.nextval , ? , ? ,?,'wvb',sysdate, ? , ?, ? , 1 from dual "
					+ " where not exists ( select * from NEWS WHERE PUBLISHDATE = ? and NEWSTITLE=? and COMPNYID=? and NEWSURL=?) ";
			logger.info("start query " + query);
			try {
				if (con == null || con.isClosed()) {
					con = ConnectionManager.connectDataBase();
				}
			} catch (Exception e) {
				System.out.println("Error" + e);
			}
			PreparedStatement ps = con.prepareStatement(query);

			ps.clearBatch();
			InputStream is1 = null;
			int x = 0;
			for (DataModel dm : mySet) {
				// while ( dm.getDataSheetUrl() not in )
				// ps.
				String pRContent = dm.getDiscription();
				if (pRContent != null) {
					is1 = new ByteArrayInputStream(dm.getDiscription().getBytes(StandardCharsets.UTF_8.name()));
				}
				ps.setBlob(1, is1);
				ps.setString(2, dm.getNewsTitle());
				ps.setInt(3, dm.getNewsId());
				ps.setInt(4, dm.getCompanyId());
				if (dm.getDate() == null || dm.getDate().equals("null") || dm.getDate().equals("")) {
					ps.setDate(5, FixDate.getDate(java.time.LocalDate.now().toString()));
					ps.setDate(7, FixDate.getDate(java.time.LocalDate.now().toString()));
				} else {

					ps.setDate(5, dm.getDate());
					ps.setDate(7, dm.getDate());
				}

				ps.setString(6, dm.getDataSheetUrl());

				ps.setString(8, dm.getNewsTitle());
				ps.setInt(9, dm.getCompanyId());
				ps.setString(10, dm.getDataSheetUrl());

				ps.addBatch();
				System.out.println(dm.getNewsTitle() + "\t" + dm.getDate() + "\t" + dm.getCompanyId() + "\t"
						+ dm.getDataSheetUrl());
			}
			int[] arr = ps.executeBatch();

			logger.info("Done Insert ============= " + arr.length);
			System.out.println("Done Insert ============= " + arr.length);
			if (arr.length > 0) {
				logger.info("Done Insert ============= " + arr.length);
				System.out.println("Done Insert ============= " + arr.length);
			}
		} catch (Exception e) {
			logger.error("Error", e);
			System.out.println(e);
		}
	}

	public static void addToDb2(Set<DataModel> mySet) {
		/* insert data to database */
		try {

			String query = "insert into NEWS (NEWSID , " + "NEWSBODY," + "NEWSTITLE," + "NEWSCATGID," + "WHOCREATED,"
					+ "DTCREATED," + "COMPNYID," + "PUBLISHDATE,NEWSURL,ATTCHMENT,BYGENERALCRAWLER ) "
					+ " select  NEWSSEQ.nextval , ? , ? ,?,'wvb',sysdate, ? , ?, ? , ? , 1 from dual "
					+ " where not exists ( select * from NEWS WHERE PUBLISHDATE = ? and NEWSTITLE=? and COMPNYID=? and NEWSURL=? ) ";
			System.out.println("start query " + query);
			PreparedStatement ps = con.prepareStatement(query);
			for (DataModel dm : mySet) {
				try {
					
					File blob = new File(dm.getOfflinePath());
					FileInputStream in = new FileInputStream(blob);
					ps.setString(1, dm.getNewsTitle());
					ps.setString(2, dm.getNewsTitle());
					ps.setInt(3, dm.getNewsId());
					ps.setInt(4, dm.getCompanyId());
					if (dm.getDate() == null || dm.getDate().equals("null") || dm.getDate().equals("") ) {
						ps.setDate(5, FixDate.getDate(java.time.LocalDate.now().toString()));
 					} else {

						ps.setDate(5, dm.getDate());
						 
					}
					ps.setString(6, dm.getDataSheetUrl());
					ps.setBlob(7, in);
					ps.setDate(8, dm.getDate());

					ps.setString(9, dm.getNewsTitle());
					ps.setInt(10, dm.getCompanyId());
					ps.setString(11, dm.getDataSheetUrl());
					ps.addBatch();
					System.out.println(dm.getNewsTitle() + "\t" + dm.getDate() + "\t" + dm.getCompanyId() + "\t"
							+ dm.getDataSheetUrl());
				} catch (Exception e) {
					e.printStackTrace();

				}

			}
			int[] arr = ps.executeBatch();

			logger.info("Done Insert ============= " + arr.length);
			System.out.println("Done Insert ============= " + arr.length);
			if (arr.length > 0) {
				logger.info("Done Insert ============= " + arr.length);
				System.out.println("Done Insert ============= " + arr.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
