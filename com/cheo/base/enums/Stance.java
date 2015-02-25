package com.cheo.base.enums;

public enum Stance {
	PRO("pro"),
	CON("con"),
	NONE("none");
	private final String value;
	Stance(String value){
		this.value =value;
	}
	public String getValue(){
		return this.value;
	}
}
