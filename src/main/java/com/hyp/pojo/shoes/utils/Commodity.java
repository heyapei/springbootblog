package com.hyp.pojo.shoes.utils;
 
public class Commodity {
 
	// 商品名称
	private String name;
	// 单价
	private String unit_price;
	// 数量
	private String num;
 
	// 总价
	private String sum;
 
	public Commodity(String name, String unit_price, String num, String sum, String barcode) {
		super();
		this.name = name;
		this.unit_price = unit_price;
		this.num = num;
		this.sum = sum;
		this.barcode = barcode;
	}
 
	// 条码
	private String barcode;
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public String getUnit_price() {
		return unit_price;
	}
 
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
 
	public String getNum() {
		return num;
	}
 
	public void setNum(String num) {
		this.num = num;
	}
 
	public String getSum() {
		return sum;
	}
 
	public void setSum(String sum) {
		this.sum = sum;
	}
 
	public String getBarcode() {
		return barcode;
	}
 
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
 
}