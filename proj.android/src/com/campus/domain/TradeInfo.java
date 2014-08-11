package com.campus.domain;

import java.util.List;


public class TradeInfo {
	private String category;
	private int commentCount;
	private String publishDate;
	private String publisher;

	private String des;

	private int id;

	private List<String> images;

	private String name;

	private String newRate;
	private int praiseCount;

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	private double price;

	private String publishName;
	private String title;
	private String tradePlace;

	public String getCategory() {
		return category;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public String getDes() {
		return des;
	}

	public int getId() {
		return id;
	}

	public List<String> getImage() {
		return images;
	}

	public String getName() {
		return name;
	}

	public String getNewRate() {
		return newRate;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public double getPrice() {
		return price;
	}

	public String getPublishName() {
		return publishName;
	}

	public String getTitle() {
		return title;
	}

	public String getTradePlace() {
		return tradePlace;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImage(List<String> images) {
		this.images = images;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNewRate(String newRate) {
		this.newRate = newRate;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
	}

}
