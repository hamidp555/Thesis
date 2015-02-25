package com.cheo.services.excel;

public enum ColumnHeaders {

	AUTHOR_NAME("AUTHOR_NAME"),
	COMMENT_TITLE("COMMENT_TITLE"),
	COMMENT("COMMENT"),
	DISCUSSION_TITLE("DISCUSSION_TITLE"),
	DATE_DOWNLOADED("DATE_DOWNLOADED"),
	DISCRIMINATOR("DISCRIMINATOR"),
	OBJECTIVE("objective"),
	POSITIVE("POSITIVE"),
	NEGATIVE("NEGATIVE"),
	MIXED("MIXED"),
	NEUTRAL("neutral"),
	IRRELEVANT("IRRELEVANT"),
	KEY_RELEVANCE("KEY_RELEVANCE"),
	KEY_POSITION("KEY_POSITION"),
	COPY("COPY"),
	TOPICS("topics"),
	PRO("pro"),
	CON("con");
	
	private final String value;

	ColumnHeaders(String value){
		this.value =value;
	}
	
	public String getValue(){
		return this.value;
	}

}
