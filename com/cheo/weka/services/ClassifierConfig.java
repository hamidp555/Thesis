package com.cheo.weka.services;

import com.cheo.base.enums.ClassifierType;

public class ClassifierConfig {

	private String arffFileName;
	
	private ClassifierType type;
	
	private String Level;

	public String getArffFileName() {
		return arffFileName;
	}

	public void setArffFileName(String arffFileName) {
		this.arffFileName = arffFileName;
	}

	public ClassifierType getType() {
		return type;
	}

	public void setType(ClassifierType type) {
		this.type = type;
	}

	public String getLevel() {
		return Level;
	}

	public void setLevel(String level) {
		Level = level;
	}
	
}
