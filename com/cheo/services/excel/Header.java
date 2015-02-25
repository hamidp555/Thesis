package com.cheo.services.excel;

public class Header{
	private String name;
	private int index;
	
	public Header(String name, int index){
		this.name=name;
		this.index=index;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
