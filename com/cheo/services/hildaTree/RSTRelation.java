package com.cheo.services.hildaTree;

public enum RSTRelation {
	
	ELABORATION("elaboration"),
	ENABLEMENT("enablement"),
	CONTRAST("contrast"),
	ATTRIBUTION("attribution"),
	JOINT("joint"),
	BACKGROUND("background"),
	SPAN("span"),
	SAMEUNIT("sameunit"),
	CONDITION("condition"),
	EVALUATION("evaluation"),
	EXPLANATION("explanation"),
	GENERALIZATION("generalization"),
	SIMILARITY("similarity"),
	CAUSEEFFECT("causeeffect"),
	EXAMPLE("example"),
	VIOLATEDEXPECTATIONS("violatedexpectations"),
	TEMPORALSEQUENCE("temporalsequence"),
	TEMPORAL("temporal"),
	TEXTUALORGANIZATION("textualorganization"),
	SUMMARY("summary"),
	CAUSE("cause"),
	TOPICCOMMENT("topiccomment"),
	MANNERMEANS("mannermeans"),
	COMPARISON("comparison"),
	TOPICCHANGE("topicchange");
	
	private final String value;

	RSTRelation(String value){
		this.value =value;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
