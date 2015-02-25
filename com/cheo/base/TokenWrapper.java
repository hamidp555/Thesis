package com.cheo.base;

public class TokenWrapper {
	
	private String token ;
	private String posTag;
	private String lemmatizedToken;

	public TokenWrapper(String token, String posTag, String lemmatizedToken){
		this.token=token;
		this.posTag=posTag;
		this.lemmatizedToken=lemmatizedToken;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPosTag() {
		return posTag;
	}

	public void setPosTag(String posTag) {
		this.posTag = posTag;
	}

	public String getLemmatizedToken() {
		return lemmatizedToken;
	}

	public void setLemmatizedToken(String lemmatizedToken) {
		this.lemmatizedToken = lemmatizedToken;
	}
	
}
