package com.cheo.services.excel;

public class Counter {
	
	private Integer counts;
	
	public Counter(int value){
		counts=value;
	}
	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	
	public void increment() {
		counts++;
	}
	
	public void incrementBy(Integer value) {
		counts += value;
	}

}
