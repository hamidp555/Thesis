package com.cheo.base.enums;

public enum Annotators {

	FIRST("first"),
	SECOND("second");
	
	private final String label;

	Annotators(String label){
		this.label=label;
	}

	public String getValue(){
		return label;
	}
	
}
