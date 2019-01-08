/*


 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wvb.engine;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author AL-ME3RAJ
 */
public class FixDate {

	public static java.sql.Date getDate(String date) {
		java.util.Date d = null;
		java.sql.Date sqlDate = null;
		try {
			java.util.Date myDate = new java.util.Date(convertDate(date));
			sqlDate = new java.sql.Date(myDate.getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}

		return sqlDate;
	}

	public static String convertDate(String datex) {

		java.util.Date date = null;
		String dateString2 = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(datex);
			dateString2 = new SimpleDateFormat("yyyy/MM/dd").format(date);
		} catch (ParseException e) {

		}

		return dateString2;
	}

	public static String genrateDatex(String date) {
		String newDate = null;
		if (date.contains("/January/")) {
			newDate = date.replaceAll("/January/", "/01/");

		} else if (date.contains("/February/")) {
			newDate = date.replaceAll("/February/", "/02/");
		} else if (date.contains("/March/")) {
			newDate = date.replaceAll("/March/", "/03/");
		} else if (date.contains("/April/")) {
			newDate = date.replaceAll("/April/", "/04/");
		} else if (date.contains("/May/")) {
			newDate = date.replaceAll("/May/", "/05/");
		} else if (date.contains("/June/")) {
			newDate = date.replaceAll("/June/", "/06/");
		} else if (date.contains("/July/")) {
			newDate = date.replaceAll("/July/", "/07/");
		} else if (date.contains("/August/")) {
			newDate = date.replaceAll("/August/", "/08/");
		} else if (date.contains("/September/")) {
			newDate = date.replaceAll("/September/", "/09/");
		} else if (date.contains("/October/")) {
			newDate = date.replaceAll("/October/", "/10/");
		} else if (date.contains("/November/")) {
			newDate = date.replaceAll("/November/", "/11/");
		} else if (date.contains("/December/")) {
			newDate = date.replaceAll("/December/", "/12/");
		}

		return newDate;

	}

	public static String genrateDatexx(String date) {
		String newDate = null;
		if (date.contains("/Jan/")) {
			newDate = date.replaceAll("/Jan/", "/01/");

		} else if (date.contains("/Feb/")) {
			newDate = date.replaceAll("/Feb/", "/02/");
		} else if (date.contains("/Mar/")) {
			newDate = date.replaceAll("/Mar/", "/03/");
		} else if (date.contains("/Apr/")) {
			newDate = date.replaceAll("/Apr/", "/04/");
		} else if (date.contains("/May/")) {
			newDate = date.replaceAll("/May/", "/05/");
		} else if (date.contains("/Jun/")) {
			newDate = date.replaceAll("/Jun/", "/06/");
		} else if (date.contains("/Jul/")) {
			newDate = date.replaceAll("/Jul/", "/07/");
		} else if (date.contains("/Aug/")) {
			newDate = date.replaceAll("/Aug/", "/08/");
		} else if (date.contains("/Sep/")) {
			newDate = date.replaceAll("/Sep/", "/09/");

		} else if (date.contains("/Oct/")) {
			newDate = date.replaceAll("/Oct/", "/10/");
		} else if (date.contains("/Nov/")) {
			newDate = date.replaceAll("/Nov/", "/11/");
		} else if (date.contains("/Dec/")) {
			newDate = date.replaceAll("/Dec/", "/12/");
		}

		return newDate;

	}
}
