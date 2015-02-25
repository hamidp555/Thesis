package com.cheo.base.enums;

public enum ExtraFeatures {
	
	 CLASS_LABEL("classLabel"),
	 LASTEDUCLASSLABEL("lastEduClassLabel"),
	 
	 CONTENT("content"),
	 PRIORCLASS("priorClass"),
	
	 NUMPOSITIVESWN("numPositiveSWN"),
	 NUMPOSITIVEGI("numPositiveGI"),
	 NUMPOSITIVEPL("numPositivePL"),
	 NUMPOSITIVEDEPMODE("numPositiveDepmode"),
	 NUMPOSITIVNRC("numPositiveNRC"),
	 NUMPOSITIVESLANG("numPositiveSLANG"),
	 NUMPOSITIVEEMOTICON("numPositiveEMOTICON"),
	 NUMPOSEMOTICONS("numPositiveEMOTICON"),
	 
	 NUMNEGATIVESWN("numNegativeSWN"),
	 NUMNEGATIVEGI("numNegativeGI"),
	 NUMNEGATIVEPL("numNegativePL"),
	 NUMNEGATIVEDEPMODE("numNegativeDepmode"),
	 NUMNEGATIVENRC("numNegativeNRC"),
	 NUMNEGATIVESLANG("numNegativeSLANG"),
	 NUMNEGATIVEEMOTICON("numNegativeEMOTICON"),
	 NUMNEGEMOTICONS("numNegativeEMOTICON"),

	 NUMPUNCTUATIONS("numPunctuations"),
	 NUMELONGATEDWORDS("numElongatedWords"),
	 NUMDSLWORDS("numDSLWords"),
	 NUMWEAKSUBJPL("numWeakSubjPL"),
	 NUMSTRONGSUBJPL("numStrongSubjPL"),
	 
	 //FOR EDUS
	 SHEETID("sheetID"),
	 COMMENTID("commentID"),
	 EDUID("eduID");


	private final String value;

	ExtraFeatures(String value){
		this.value =value;
	}
	
	public String getValue(){
		return this.value;
	}
}
