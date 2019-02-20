package com.wvb.crawlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.LoggerFactory;

import com.wvb.apis.ExctrctDates;
import com.wvb.apis.FixDate;
import com.wvb.connection.ConnectionManager;
import com.wvb.connection.GetDocumentJsoup;
import com.wvb.dto.DAO;
import com.wvb.factory.ParentCrawler;
import com.wvb.model.DataModel;

public class PDFCrawler implements ParentCrawler {
	static org.slf4j.Logger logger = LoggerFactory.getLogger(HTMLCrawler.class);
	static ArrayList<DataModel> modelJsoup;
	static ArrayList<DataModel> modelSelenium;

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

					if (!keyWord.equals("none")) {

						if (relHref.contains(keyWord)) {

							System.out.println(x0);
							if ((!relHref.startsWith("http")
									&& !relHref.startsWith("https")
									&& !relHref.startsWith("www")
									&& !relHref.startsWith("Http")
									&& !relHref.startsWith("https"))
									&& relHref.endsWith(".pdf")) {
								if (relHref.contains("../../")) {
									relHref = relHref.replace("../..", "");
								}
								String ccRelHref = domainFromDB + relHref;

								numberMapping.put(ccRelHref, title);
								System.out
										.println("child URL 00===> " + relHref);
							} else {
								String ccRelHref = "";
								if (domainFromDB == null
										|| domainFromDB.equals("null")
										|| domainFromDB.equals("")) {
									ccRelHref = relHref;
								} else {
									ccRelHref = domainFromDB + relHref;

								}

								numberMapping.put(ccRelHref, title);
								// numberMapping.put(relHref, title);
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

			DAO.addToDb2(mySet);
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
			String offlineUrl = null;
			try {
				offlineUrl = downloadDataSheetUrl2(url, title);
			} catch (Exception e1) {
				e1.printStackTrace();

			}
			String arr[] = readPDF(offlineUrl);
			content = arr[0];
			try {

				System.out.println("Title =====================> " + title);
				if (title.equals("Read more") || title.equals("")
						|| title.contains("view as pdf") || title.equals("more")
						|| title.equals("Read More") || title.equals("Download")
						|| title == null) {

					title = arr[1];
					// document.getElementsByTag(titleFromChildPage).text();

				}

			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
			}
			System.out.println("Title =====================> " + title);
			try {
				if (!title.equals("") || title != null) {

					DataModel dm = new DataModel();
					dm.setDataSheetUrl(url);
					dm.setDiscription(content);
					dm.setNewsTitle(title);
					dm.setCompanyId(companyPermID);
					dm.setNewsId(13);
					dm.setCompanyId(companyId);

					dm.setOfflinePath(offlineUrl);
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
						String genrateDate = ExctrctDates.getDate(arr[0], 1);
						System.out.println(
								"genrateDate ----------------------- ===============> "
										+ genrateDate);
						logger.info(
								"newDate ----------------------- ===============> "
										+ newDate);
						if (!(genrateDate.equals("not found")
								|| genrateDate == null
								|| genrateDate.equals("null"))) {

							newDate = FixDate.getDate(genrateDate);

							Thread.sleep(5);

							dm.setDate(newDate);
						}

					}

					modelJsoup.add(dm);

					logger.info("Title ===============> " + title);
					logger.info(" newDate ====================> " + newDate);

				}
			} catch (Exception ex) {

			}
		}

	}
	public static String createPathWithFullDate() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int mSecond = calendar.get(Calendar.MILLISECOND);

		String fullDateTime = year + "\\" + month + "\\" + day + "\\" + hour
				+ "\\" + minute + "\\" + second + "\\" + mSecond;

		return fullDateTime;
	}
	private static String downloadDataSheetUrl2(String urlx, String name) {
		urlx = urlx.replaceAll(" ", "%20");
		String save = "";
		try {
			System.out.println("url -----------> " + urlx);
			name = name.replace("/", "-");
			File file = null;
			BufferedInputStream in = null;
			FileOutputStream fout = null;
			String datePath = createPathWithFullDate();
			int random = (int) (Math.random() * 12754);
			file = new File(ConnectionManager.SavePath() + datePath + "/"
					+ random + "/");
			file.mkdirs();
			save = file.getPath() + random + ".pdf";
			System.out.println("file name -----------> " + save);

			fout = new FileOutputStream(save);

			URL url = new URL(urlx);

			HttpURLConnection conn = null;

			conn = (HttpURLConnection) url.openConnection();

			System.setProperty("user.agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			conn.setReadTimeout(6000000);

			try {
				doTrustToCertificates();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn.connect();
			in = new BufferedInputStream(conn.getInputStream());

			final byte data[] = new byte[2048];
			int count;

			while ((count = in.read(data, 0, 1024)) != -1) {

				fout.write(data, 0, count);

			}
			System.out.println("File Downloaded.............");
		} catch (IOException e) {
			e.printStackTrace();

		}

		return save;
	}
	public static String[] readPDF(String path) {
		StringBuffer buffer = new StringBuffer();
		String content = "";
		String title = "";
		String fixarr[] = new String[2];
		try (PDDocument document = PDDocument.load(new File(path))) {

			document.getClass();

			if (!document.isEncrypted()) {

				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);

				PDFTextStripper tStripper = new PDFTextStripper();

				String pdfFileInText = tStripper.getText(document);
				// System.out.println("Text:" + st);

				// split by whitespace
				String lines[] = pdfFileInText.split("\\r?\\n");
				int x = 0;
				System.out.println(
						"lines  =======================> " + lines.length);
				// try {
				// title = lines[0] + " " + lines[1];
				// } catch (Exception e) {
				// // TODO: handle exception
				// }

				System.out.println("Title =======================> " + title);
				for (String line : lines) {

					if (x == 3) {

						title = buffer.toString();
						x++;
					}

					buffer.append(line);
					System.out.println(line);
					x++;
				}
				content = buffer.toString();
			}
			fixarr[0] = content;
			fixarr[1] = title;
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block

		} catch (IOException e) {
			// TODO Auto-generated catch block

		}

		return fixarr;

	}
	private static void doTrustToCertificates() throws Exception {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkServerTrusted(X509Certificate[] certs,
							String authType) throws CertificateException {
						return;
					}

					public void checkClientTrusted(X509Certificate[] certs,
							String authType) throws CertificateException {
						return;
					}
				}};

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
					System.out.println("Warning: URL host '" + urlHostName
							+ "' is different to SSLSession host '"
							+ session.getPeerHost() + "'.");
				}
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	@Override
	public void crawleBySelenium(String url, String companyCode,
			int companyPermId, String keyWord, String domainFromDB,
			String dateWithTitle, String titleFromChildPage) {
		System.setProperty("webdriver.chrome.driver",
				"E:\\lib\\chromedriver.exe");
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

					if (!keyWord.equals("none")) {

						if (relHref.contains(keyWord)) {

							System.out.println(x0);
							if ((!relHref.startsWith("http")
									&& !relHref.startsWith("https")
									&& !relHref.startsWith("www")
									&& !relHref.startsWith("Http")
									&& !relHref.startsWith("https"))
									&& relHref.endsWith(".pdf")) {
								if (relHref.contains("../../")) {
									relHref = relHref.replace("../..", "");
								}

								if (relHref.contains("..")) {
									relHref = relHref.replace("..", "");
								}
								String ccRelHref = domainFromDB + relHref;

								numberMapping.put(ccRelHref, title);
								System.out
										.println("child URL 00===> " + relHref);
							} else {
								String ccRelHref = "";
								if (domainFromDB == null
										|| domainFromDB.equals("null")
										|| domainFromDB.equals("")) {
									ccRelHref = relHref;
								} else {
									ccRelHref = domainFromDB + relHref;

								}

								numberMapping.put(ccRelHref, title);
								// numberMapping.put(relHref, title);
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

			DAO.addToDb2(mySet);
			mySet = null;
		} catch (URISyntaxException ex) {
			logger.error("Error", ex);
			System.out.println(ex);
		}

		modelJsoup = null;

		System.gc();
		driver.close();
	}

	private void getDataSelenium(String companyCode, String parentUrl,
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
			String offlineUrl = null;
			try {
				offlineUrl = downloadDataSheetUrl2(url, title);
			} catch (Exception e1) {

			}
			String arr[] = readPDF(offlineUrl);
			content = arr[0];
			try {

				System.out.println("Title =====================> " + title);
				if (title.equals("Read more") || title.equals("")
						|| title.equals("more") || title.equals("Download")
						|| title.equals("Read More") || title == null) {

					title = arr[1];
					// document.getElementsByTag(titleFromChildPage).text();

				}

			} catch (Exception e) {
				logger.error("Error", e);
				System.out.println(e);
			}
			System.out.println("Title =====================> " + title);

			if (!title.equals("") || title == "null") {
				try {

					DataModel dm = new DataModel();
					dm.setDataSheetUrl(url);
					dm.setDiscription(content);
					dm.setNewsTitle(title);
					dm.setCompanyId(companyPermID);
					dm.setNewsId(13);
					dm.setCompanyId(companyId);

					dm.setOfflinePath(offlineUrl);
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
						String genrateDate = ExctrctDates.getDate(arr[0], 1);
						System.out.println(
								"genrateDate ----------------------- ===============> "
										+ genrateDate);
						logger.info(
								"newDate ----------------------- ===============> "
										+ newDate);
						if (!(genrateDate.equals("not found")
								|| genrateDate == null
								|| genrateDate.equals("null"))) {

							newDate = FixDate.getDate(genrateDate);

							Thread.sleep(5);

							dm.setDate(newDate);
						}

					}

					modelSelenium.add(dm);

					logger.info("Title ===============> " + title);
					logger.info(" newDate ====================> " + newDate);
				} catch (Exception ex) {
					System.out.println(companyPermID);
					logger.error("Error", ex);
					System.out.println(ex);
				}

			}
		}
	}

}
