package com.cheo.base.enums;

public enum FileType {
	
	TXT("txt_", ".txt"),
	ARFF("arff_",".arff"),
	MODEL("model_", ".model"),
	EVAL("eval_", ".txt"),
	PRED("eval_", ".txt");
	
	private final String suffix;
	private final String prefix;

	FileType(String prefix, String suffix){
		this.suffix=suffix;
		this.prefix=prefix;
	}

	public String suffix(){ return suffix;}
	public String prefix(){ return prefix;}
}
