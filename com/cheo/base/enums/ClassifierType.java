package com.cheo.base.enums;

public enum ClassifierType {

	ZEROR("zeror"),
	NB("nb"),
	SMO("smo"),
	LIBSVM("libsvm"),
	VOTE("vote"),
	ATTRIBUTE("attribute"),
	FILTERED("filtered"),
	NB_MULTI_DIM("naiveBayesMultidimentional"),
	NB_COMPLEMENT("complementNaiveBayes");
	
	private final String value;

	ClassifierType(String value){
		this.value =value;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
