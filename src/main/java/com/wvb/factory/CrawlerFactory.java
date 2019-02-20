package com.wvb.factory;

import com.wvb.crawlers.HTMLCrawler;
import com.wvb.crawlers.OCRCrawler;
import com.wvb.crawlers.PDFCrawler;

public class CrawlerFactory {

	public ParentCrawler getCrawler(String crawlerType) {
		if (crawlerType == null) {
			return null;
		}
		if (crawlerType.equalsIgnoreCase("HTML")) {
			System.out.println("Read From Html Page");
			return new HTMLCrawler();

		} else if (crawlerType.equalsIgnoreCase("PDF")) {
			System.out.println("Read From PDF Page");
			return new PDFCrawler();

		} else if (crawlerType.equalsIgnoreCase("OCR")) {
			return new OCRCrawler();
		}

		return null;
	}

}
