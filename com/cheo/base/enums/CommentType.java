package com.cheo.base.enums;

public enum CommentType {

	DIFFERENT("different"),
	SAME("same"),
	AA("AA"),
	AB("AB"),
	JUDGED("judged");
	
	private final String value;

	CommentType(String value){
		this.value =value;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
