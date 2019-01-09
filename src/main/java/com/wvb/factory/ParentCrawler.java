package com.wvb.factory;

public interface ParentCrawler {
	public void crawleBySelenium(String url, String companyCode, int companyPermId, String keyWord, String domainFromDB,
			String dateWithTitle,String titleFromChildPage);

	public void crawleByJsoup(String url, String companyCode, int companyPermId, String keyWord, String domainFromDB,
			String dateWithTitle,String titleFromChildPage);
}
