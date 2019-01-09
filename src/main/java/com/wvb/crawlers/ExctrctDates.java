package com.wvb.crawlers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.wvb.engine.FixDate;

public class ExctrctDates {
	public static String getDate(String content, int xx) {

		String date = "";
		try {
			Parser p = new Parser();
			// p.parse(s);
			int x = 0;
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDateTime now = LocalDateTime.now();
			for (DateGroup d : p.parse(content)) {
				System.out.println(d.getDates());

				String xdate = "" + d.getDates().get(0);
				if (!xdate.contains("2019") && p.parse(content).size() > 1) {
					date = xdate;
				}
				if (x == xx) {
					date = xdate;
				}
				x++;

			}
			String datex = genrateDate(date);
			date = datex;
		} catch (Exception ex) {
			date = "not found";
			ex.printStackTrace();
		}
		System.out.println(
				"new Date ************** =====================> " + date);
		return date;

	}

	public static String genrateDate(String oldDate) {
		String newDate = "not found";
		String day, month, year;
		try {
			String[] fixdate = oldDate.split(" ");
			month = fixdate[1];
			day = fixdate[2];
			year = fixdate[5];
			if (year.contains(",")) {
				year = year.replaceAll(",", "");
			}
			if (month.length() > 3) {
				newDate = FixDate.genrateDatex(day + "/" + month + "/" + year);
			} else {
				newDate = FixDate.genrateDatexx(day + "/" + month + "/" + year);
			}

		} catch (Exception e) {
			newDate = "not found";
			e.printStackTrace();
		}
		System.out.println(
				"new Date ************** =====================> " + newDate);

		return newDate;
	}
}
