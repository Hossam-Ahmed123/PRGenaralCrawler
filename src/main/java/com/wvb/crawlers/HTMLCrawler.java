package com.wvb.crawlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.LoggerFactory;

import com.wvb.apis.ExctrctDates;
import com.wvb.apis.FixDate;
import com.wvb.connection.GetDocumentJsoup;
import com.wvb.dto.DAO;
import com.wvb.factory.ParentCrawler;
import com.wvb.model.DataModel;

public class HTMLCrawler implements ParentCrawler {

	static org.slf4j.Logger logger = LoggerFactory.getLogger(HTMLCrawler.class);
	static ArrayList<DataModel> modelJsoup;
	static ArrayList<DataModel> modelSelenium;

	@Override
	public void crawleBySelenium(String url, String companyCode,
			int companyPermId, String keyWord, String domainFromDB,
			String dateWithTitle, String titleFromChildPage) {
		System.setProperty("webdriver.chrome.driver", "/home/user/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		Map<String, String> numberMapping = new HashMap<>();
		modelSelenium = new ArrayList<DataModel>();
		int companyID = DAO.getCompanyID(companyPermId);
		try {
			String title = "";
			String relHref = "";
			String date = "";

			URI uri = new URI(url);
			
			driver.get(url);
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Document document = Jsoup.parse(driver.getPageSource());
			System.out.println(document.outerHtml());
			String domain = uri.getHost();
			logger.info("URI -----|> " + uri);
			Elements urlsx = null;
			try {

				urlsx = document.getElementsByTag("a");

				int x = 0;
				String newChildUrl = "";
				int x0 = 0;

				for (Element urlc : urlsx) {

					relHref = urlc.attr("href");
					title = urlc.text();

					if (relHref.contains(keyWord)) {
						System.out.println("child URL 00===> " + relHref);

						System.out.println(x0);
						if (!relHref.endsWith(".pdf")
								&& !relHref.endsWith(".jpg")) {
							if (!relHref.startsWith("http")
									&& !relHref.startsWith("https")
									&& !relHref.startsWith("www")
									&& !relHref.startsWith("Http")
									&& !relHref.startsWith("https")) {
								if (relHref.contains("../../")) {
									relHref = relHref.replace("../..", "");
								}

								relHref = domainFromDB + relHref;
								numberMapping.put(relHref, title);
							} else {
								numberMapping.put(relHref, title);

							}
						}

					}

				}

			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
				e.printStackTrace();

			}
			getDataSelenium(companyCode, url, companyPermId, dateWithTitle,
					companyID, numberMapping, titleFromChildPage);
			Set<DataModel> mySet = new HashSet<DataModel>(modelSelenium);
			DAO.addToDb(mySet);

		} catch (URISyntaxException ex) {
			logger.error("Error", ex);
			System.out.println(ex);
		}

		modelSelenium = null;
		System.gc();
		driver.close();
		

	}

	private void getDataSelenium(String companyCode, String parentUrl,
			int companyPermId, String dateWithTitle, int companyID,
			Map<String, String> numberMapping, String titleFromChildPage) {
		System.setProperty("webdriver.chrome.driver", "/home/user/Downloads/chromedriver");
		WebDriver driverx = new ChromeDriver();
		Iterator iterator = numberMapping.entrySet().iterator();
		String date = "";
		String content = "";
		String url = "";
		String title = " ";
		int x = 0;
		while (iterator.hasNext()) {
			Map.Entry me2 = (Map.Entry) iterator.next();

			url = me2.getKey().toString();
			title = me2.getValue().toString();
			try {

				driverx.get(url);
				Document document = Jsoup.parse(driverx.getPageSource());
				if (title.equals("Read more") || title.equals("")
						|| title.equals("more") || title.equals("Read more..")
						|| title.equals("Read More") || title.equals("more...")
						|| title.equals("READ MORE")) {
					title = document.getElementsByTag(titleFromChildPage)
							.text();

					System.out.println("child URL 00===> " + title);
				}

				try {
					content = document.outerHtml();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
			}
			if (!title.equals("")) {
				try {

					DataModel dm = new DataModel();
					dm.setDataSheetUrl(url);
					dm.setDiscription(content);
					dm.setNewsTitle(title);
					dm.setCompanyId(companyPermId);
					dm.setNewsId(13);
					dm.setCompanyId(companyID);
					java.sql.Date newDate = null;
					if (dateWithTitle.equals("1")) {
						String datex = ExctrctDates.getDate(title, 1);
						newDate = FixDate.getDate(datex);

						dm.setDate(newDate);
					} else {
						String genrateDate = ExctrctDates.getDate(content, 1);
						newDate = FixDate.getDate(genrateDate);
						dm.setDate(newDate);
					}
					modelSelenium.add(dm);
					logger.info("Title ===============> " + title);
					logger.info(" newDate ====================> " + newDate);
				} catch (Exception ex) {
					logger.error("Error", ex);
					System.out.println(ex);
				}

			}
		}
		driverx.close();

	}

	@Override
	public void crawleByJsoup(String url, String companyCode, int companyPermId,
			String keyWord, String domainFromDB, String dateWithTitle,
			String titleFromChildPage) {
		Map<String, String> numberMapping = new HashMap<>();
		modelJsoup = new ArrayList<DataModel>();
		logger.info("companyPermId --===========---|> " + companyPermId);

		int companyID = DAO.getCompanyID(companyPermId);
		try {
			String title = "";
			String relHref = "";
			String date = "";

			URI uri = new URI(url);

			GetDocumentJsoup gdj = new GetDocumentJsoup();
			Document document = gdj.getDocument(url);
			
			
			String domain = uri.getHost();
			logger.info("URI -----|> " + uri);
			// Element cls = document.getElementsByClass(configClass).first();
			Elements urlsx = null;
			try {

				urlsx = document.getElementsByTag("a");

				int x = 0;
				String newChildUrl = "";
				int x0 = 0;

				for (Element urlc : urlsx) {

					relHref = urlc.attr("href");
					title = urlc.text();
					System.out.println("child URL 00===> " + relHref);

					if (!keyWord.equals("none")) {

						if (relHref.contains(keyWord)) {

							System.out.println(x0);
							if (!relHref.startsWith("http")
									&& !relHref.startsWith("https")
									&& !relHref.startsWith("www")
									&& !relHref.startsWith("Http")
									&& !relHref.startsWith("https")) {
								if (relHref.contains("../../")) {
									relHref = relHref.replace("../..", "");
								}
								relHref = domainFromDB + relHref;

								numberMapping.put(relHref, title);

							} else {
								numberMapping.put(relHref, title);
							}

						}

					}
				}
			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
				e.printStackTrace();

			}
			getDataJsoup(companyCode, url, companyPermId, dateWithTitle,
					companyID, numberMapping, titleFromChildPage);
			Set<DataModel> mySet = new HashSet<DataModel>(modelJsoup);

			DAO.addToDb(mySet);
			mySet = null;
		} catch (URISyntaxException ex) {
			logger.error("Error", ex);
			System.out.println(ex);
		}

		modelJsoup = null;

		System.gc();

	}

	private static void getDataJsoup(String companyCode, String parentUrl,
			int companyPermID, String dateWithTitle, int companyId,
			Map<String, String> numberMapping, String titleFromChildPage) {
		Iterator iterator = numberMapping.entrySet().iterator();
		String date = "";
		String content = "";
		String url = "";
		String title = " ";
		while (iterator.hasNext()) {
			Map.Entry me2 = (Map.Entry) iterator.next();
			System.out.println(
					"Key: " + me2.getKey() + " & Value: " + me2.getValue());
			url = me2.getKey().toString();
			title = me2.getValue().toString();
			try {
				GetDocumentJsoup gdj = new GetDocumentJsoup();
				Document document = gdj.getDocument(url);
				System.out.println("Title =====================> " + title);
				if (title.equals("Read more") || title.equals("")
						|| title.equals("more") || title.equals("Read more..")
						|| title.equals("Read More") || title.equals("more...")
						|| title.equals("READ MORE")) {
					title = document.getElementsByTag(titleFromChildPage)
							.text();

					System.out.println("child URL 00===> " + title);
				}

				try {
					content = document.outerHtml();

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
				}
			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
			}
			System.out.println("Title =====================> " + title);
			if (!title.equals("")) {
				try {

					DataModel dm = new DataModel();
					dm.setDataSheetUrl(url);
					dm.setDiscription(content);
					dm.setNewsTitle(title);
					dm.setCompanyId(companyPermID);
					dm.setNewsId(13);
					dm.setCompanyId(companyId);
					java.sql.Date newDate = null;
					System.out.println(
							"date With title ======>     " + dateWithTitle);
					if (dateWithTitle.equals("1")) {
						String datex = ExctrctDates.getDate(title, 1);
						System.out.println(
								"datexxxxx =====================> " + datex);
						newDate = FixDate.getDate(datex);

						dm.setDate(newDate);
					} else {
						String genrateDate = ExctrctDates.getDate(content, 1);
						System.out.println(
								"genrateDate ----------------------- ===============> "
										+ genrateDate);
						logger.info(
								"newDate ----------------------- ===============> "
										+ newDate);
						if (!(genrateDate.equals("not found"))) {

							newDate = FixDate.getDate(genrateDate);

							Thread.sleep(5);

							dm.setDate(newDate);
						}

					}

					modelJsoup.add(dm);

					logger.info("Title ===============> " + title);
					logger.info(" newDate ====================> " + newDate);
				} catch (Exception ex) {
					logger.error("Error", ex);
					System.out.println(ex);
				}

			}
		}

	}

}
