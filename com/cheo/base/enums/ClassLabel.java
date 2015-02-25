package com.cheo.base.enums;

import java.util.LinkedList;
import java.util.List;

public enum ClassLabel {

	IRRELEVANT("irrelevant"),
	RELEVANT("relevant"),
	POSITIVE("positive"),
	NEGATIVE("negative"),
	MIX("mix"),
	NEUTRAL("neutral"),
	SUBJECTIVE("subjective"),
	OBJECTIVE("objective"),
	NONE("none"),
	
	//for binomial feature selection
	MAJORITY("majority");

	private final String label;

	ClassLabel(String label){
		this.label=label;
	}

	public String getValue(){
		return label;
	}

	public static ClassLabel fromString(String label){
		if(label!=null && !label.isEmpty()){
			return 	ClassLabel.valueOf(label.toUpperCase());	
		}
		return null;
	}

	public static List<ClassLabel> getLabels(){
		List<ClassLabel> labels = new LinkedList<ClassLabel>();
		labels.add(IRRELEVANT);	
		labels.add(POSITIVE);
		labels.add(NEGATIVE);
		labels.add(MIX);
		labels.add(NEUTRAL);
		labels.add(OBJECTIVE);	
		return labels;
	}

}
