package com.example.haozai.jen.Book;

public class OrderInfo {
	private int sid;
	private String orderinfo_uname;
	private String orderinfo_ming;
	private String orderinfo_img;
	private String orderinfo_author;
	private String orderinfo_price;
	private String orderinfo_press;
	private String orderinfo_zonge;
	private String orderinfo_mount;
	private String orderinfo_youfei;
	private String ordering;
	public String getOrderinfo_img() {
		return orderinfo_img;
	}
	public void setOrderinfo_img(String orderinfo_img) {
		this.orderinfo_img = orderinfo_img;
	}
	public String getOrderinfo_ming() {
		return orderinfo_ming;
	}
	public void setOrderinfo_ming(String orderinfo_ming) {
		this.orderinfo_ming = orderinfo_ming;
	}
	public String getOrderinfo_author() {
		return orderinfo_author;
	}
	public void setOrderinfo_author(String orderinfo_author) {
		this.orderinfo_author = orderinfo_author;
	}
	public String getOrderinfo_price() {
		return orderinfo_price;
	}
	public void setOrderinfo_price(String orderinfo_price) {
		this.orderinfo_price = orderinfo_price;
	}
	public String getOrderinfo_mount() {
		return orderinfo_mount;
	}
	public void setOrderinfo_mount(String orderinfo_mount) {
		this.orderinfo_mount = orderinfo_mount;
	}
	public String getOrderinfo_youfei() {
		return orderinfo_youfei;
	}
	public void setOrderinfo_youfei(String orderinfo_youfei) {
		this.orderinfo_youfei = orderinfo_youfei;
	}
	public String getOrderinfo_uname() {
		return orderinfo_uname;
	}
	public void setOrderinfo_uname(String orderinfo_uname) {
		this.orderinfo_uname = orderinfo_uname;
	}
	public String getOrderinfo_press() {
		return orderinfo_press;
	}
	public void setOrderinfo_press(String orderinfo_press) {
		this.orderinfo_press = orderinfo_press;
	}
	public String getOrderinfo_zonge() {
		return orderinfo_zonge;
	}
	public void setOrderinfo_zonge(String orderinfo_zonge) {
		this.orderinfo_zonge = orderinfo_zonge;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getOrdering() {
		return ordering;
	}
	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}
	@Override
	public String toString() {
		return "OrderInfo [sid=" + sid + ", orderinfo_uname=" + orderinfo_uname + ", orderinfo_ming=" + orderinfo_ming
				+ ", orderinfo_img=" + orderinfo_img + ", orderinfo_author=" + orderinfo_author + ", orderinfo_price="
				+ orderinfo_price + ", orderinfo_press=" + orderinfo_press + ", orderinfo_zonge=" + orderinfo_zonge
				+ ", orderinfo_mount=" + orderinfo_mount + ", orderinfo_youfei=" + orderinfo_youfei + ", ordering="
				+ ordering + "]";
	}
}
