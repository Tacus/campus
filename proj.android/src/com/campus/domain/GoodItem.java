package com.campus.domain;

import java.util.Date;

public class GoodItem {
	private float price;
	private String name;
	private String des;
	private int id;
	private String newRate;
	private String category;
	private Date date;

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNewRate() {
		return newRate;
	}

	public void setNewRate(String newRate) {
		this.newRate = newRate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
