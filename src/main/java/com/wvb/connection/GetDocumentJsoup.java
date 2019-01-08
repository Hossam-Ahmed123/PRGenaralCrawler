
package com.wvb.connection;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetDocumentJsoup {

	public Document getDocument(String url) {

		url = parseUrlHttp(url);
		Document doc = null;
		try {

			doc = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
					.timeout(60000000).get();

		} catch (IOException ex) {
			// Logger.getLogger(GetDocumentJsoup.class.getName()).log(Level.SEVERE, null,
			// ex);
		}
		return doc;
	}

	public Document getDocumentx(String url) {

		File input = new File(url);
		Document doc = null;
		try {
			doc = Jsoup.parse(input, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	public static String parseUrlHttp(String url) {
		String newURL = "";
		String start = "WWW";
		if (url.toUpperCase().startsWith(start)) {
			newURL = "http://" + url;
		} else {
			newURL = url;
		}
		return newURL;
	}
}
