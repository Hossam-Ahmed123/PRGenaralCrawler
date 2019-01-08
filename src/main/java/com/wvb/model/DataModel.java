/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wvb.model;

import java.sql.Date;

/**
 *
 * @author Hossam
 */
public class DataModel {

	private String discription;
	private String newsTitle;
	private String dataSheetUrl;
	private int wvbNumber;
	private int companyId;
	private java.sql.Date date;
	private String parentUrl;
	private String offlinePath;
	private int newsId;

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getDataSheetUrl() {
		return dataSheetUrl;
	}

	public void setDataSheetUrl(String dataSheetUrl) {
		this.dataSheetUrl = dataSheetUrl;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public int getWvbNumber() {
		return wvbNumber;
	}

	public void setWvbNumber(int wvbNumber) {
		this.wvbNumber = wvbNumber;
	}

	public String getOfflinePath() {
		return offlinePath;
	}

	public void setOfflinePath(String offlinePath) {
		this.offlinePath = offlinePath;
	}

}
