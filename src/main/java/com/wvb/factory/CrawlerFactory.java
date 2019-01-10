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
			return new HTMLCrawler();

		} else if (crawlerType.equalsIgnoreCase("PDF")) {
			return new PDFCrawler();

		} else if (crawlerType.equalsIgnoreCase("OCR")) {
			return new OCRCrawler();
		}

		return null;
	}

}
