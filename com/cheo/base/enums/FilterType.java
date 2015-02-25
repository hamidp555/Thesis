package com.cheo.base.enums;

public enum FilterType {
	
	STRING_TO_WORDVECTOR("stringToWordVector"),
	ADD("add"),
	ADD_CLASS_LABEL("addClassLabel"),
	ATTRIBUTE_SELCTION("attributeSelection"),
	CLASS_ASSIGNER("classAssigner"),
	MULTI_FILTER("multiFilter"),
	REMOVE("remove"),
	REMOVE_MISCLASSIFIED("removeMissClassified"),
	REMOVE_WITH_VALUES("removeWithValues"),
	PREPRDER("preorder"),
	SMOTE("smote"),
	STRING_TO_WORDVECTOR_NGRAM("stringToWordVectorNGram");
	
	private final String value;

	FilterType(String value){
		this.value =value;
	}
	
	public String getValue(){
		return this.value;
	}

}
