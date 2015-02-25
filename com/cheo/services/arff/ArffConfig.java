package com.cheo.services.arff;

import java.util.LinkedList;
import java.util.List;

import com.cheo.base.enums.ClassLabel;

public class ArffConfig {

	private Boolean includeExtraFeatures;

	private String featureFileName;

	private String level;
	
	private boolean normilized;

	private FeatureConfig featureConfig = new FeatureConfig();

	public boolean isNormilized() {
		return normilized;
	}

	public void setNormilized(boolean normilized) {
		this.normilized = normilized;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public FeatureConfig getFeatureConfig() {
		return featureConfig;
	}

	public void setFeatureConfig(FeatureConfig featureConfig) {
		this.featureConfig = featureConfig;
	}

	public Boolean includeExtraFeatures() {
		return includeExtraFeatures;
	}

	public void setIncludeExtraFeatures(Boolean includeExtraFeatures) {
		this.includeExtraFeatures = includeExtraFeatures;
	}

	public String getFeatureFileName() {
		return featureFileName;
	}

	public void setFeatureFileName(String featureFileName) {
		this.featureFileName = featureFileName;
	}

	public class FeatureConfig{

		private Boolean hasNumPositiveSWN = false;
		private Boolean hasNumPositiveGI = false;;
		private Boolean hasNumPositivePL = false;
		private Boolean hasNumPositiveDepmode = false;
		private Boolean hasNumPositiveNRC = false;
		private Boolean hasNumPositiveSLANG = false;
		private Boolean hasNumPositiveEMOTICON = false;
		private Boolean hasNumNegativeSWN = false;
		private Boolean hasNumNegativeGI = false;
		private Boolean hasNumNegativePL = false;
		private Boolean hasNumNegativeDepmode = false;
		private Boolean hasNumNegativeNRC = false;
		private Boolean hasNumNegativeSLANG = false;
		private Boolean hasNumNegativeEMOTICON = false;
		private Boolean hasNumPunctuations = false;
		private Boolean hasNumElongatedWords = false;
		private Boolean hasNumDSLWords = false;
		private Boolean hasNumWeakSubjPL = false;
		private Boolean hasNumStrongSubjPL = false;

		private Boolean hasSheetID = false;
		private Boolean hasCommentID = false;
		private Boolean hasEduID = false;
		private Boolean hasPriorClass = false;
		
		private Boolean hasLastEduSenti = false;
	
		private List<ClassLabel> classLabels = new LinkedList<ClassLabel>();

		public Boolean hasLastEduSenti() {
			return hasLastEduSenti;
		}
		public void setHasLastEduSenti(Boolean hasLastEduSenti) {
			this.hasLastEduSenti = hasLastEduSenti;
		}
		public Boolean hasSheetID() {
			return hasSheetID;
		}
		public void setHasSheetID(Boolean hasSheetID) {
			this.hasSheetID = hasSheetID;
		}
		public Boolean hasCommentID() {
			return hasCommentID;
		}
		public void setHasCommentID(Boolean hasCommentID) {
			this.hasCommentID = hasCommentID;
		}
		public Boolean hasEduID() {
			return hasEduID;
		}
		public void setHasEduID(Boolean hasEduID) {
			this.hasEduID = hasEduID;
		}
		public Boolean hasPriorClass() {
			return hasPriorClass;
		}
		public Boolean hasNumPositiveSWN() {
			return hasNumPositiveSWN;
		}
		public Boolean hasNumPositiveGI() {
			return hasNumPositiveGI;
		}
		public Boolean hasNumPositivePL() {
			return hasNumPositivePL;
		}
		public Boolean hasNumPositiveDepmode() {
			return hasNumPositiveDepmode;
		}
		public Boolean hasNumPositiveSLANG() {
			return hasNumPositiveSLANG;
		}
		public Boolean hasNumPositiveEMOTICON() {
			return hasNumPositiveEMOTICON;
		}
		public Boolean hasNumNegativeSWN() {
			return hasNumNegativeSWN;
		}
		public Boolean hasNumNegativeGI() {
			return hasNumNegativeGI;
		}
		public Boolean hasNumNegativePL() {
			return hasNumNegativePL;
		}
		public Boolean hasNumNegativeDepmode() {
			return hasNumNegativeDepmode;
		}
		public Boolean hasNumNegativeSLANG() {
			return hasNumNegativeSLANG;
		}
		public Boolean hasNumNegativeEMOTICON() {
			return hasNumNegativeEMOTICON;
		}
		public Boolean hasNumPunctuations() {
			return hasNumPunctuations;
		}
		public Boolean hasNumElongatedWords() {
			return hasNumElongatedWords;
		}
		public Boolean hasNumDSLWords() {
			return hasNumDSLWords;
		}
		public Boolean hasNumWeakSubjPL() {
			return hasNumWeakSubjPL;
		}
		public Boolean hasNumStrongSubjPL() {
			return hasNumStrongSubjPL;
		}
		public Boolean hasNumPositiveNRC() {
			return hasNumPositiveNRC;
		}
		public Boolean hasNumNegativeNRC() {
			return hasNumNegativeNRC;
		}
		public void setHasNumPositiveNRC(Boolean hasNumPositiveNRC) {
			this.hasNumPositiveNRC = hasNumPositiveNRC;
		}
		public void setHasNumNegativeNRC(Boolean hasNumNegativeNRC) {
			this.hasNumNegativeNRC = hasNumNegativeNRC;
		}
		public void setHasPriorClass(Boolean hasPriorClass) {
			this.hasPriorClass = hasPriorClass;
		}
		public void setHasNumPositiveSWN(Boolean hasNumPositiveSWN) {
			this.hasNumPositiveSWN = hasNumPositiveSWN;
		}
		public void setHasNumPositiveGI(Boolean hasNumPositiveGI) {
			this.hasNumPositiveGI = hasNumPositiveGI;
		}
		public void setHasNumPositivePL(Boolean hasNumPositivePL) {
			this.hasNumPositivePL = hasNumPositivePL;
		}
		public void setHasNumPositiveDepmode(Boolean hasNumPositiveDepmode) {
			this.hasNumPositiveDepmode = hasNumPositiveDepmode;
		}
		public void setHasNumPositiveSLANG(Boolean hasNumPositiveSLANG) {
			this.hasNumPositiveSLANG = hasNumPositiveSLANG;
		}
		public void setHasNumPositiveEMOTICON(Boolean hasNumPositiveEMOTICON) {
			this.hasNumPositiveEMOTICON = hasNumPositiveEMOTICON;
		}
		public void setHasNumNegativeSWN(Boolean hasNumNegativeSWN) {
			this.hasNumNegativeSWN = hasNumNegativeSWN;
		}
		public void setHasNumNegativeGI(Boolean hasNumNegativeGI) {
			this.hasNumNegativeGI = hasNumNegativeGI;
		}
		public void setHasNumNegativePL(Boolean hasNumNegativePL) {
			this.hasNumNegativePL = hasNumNegativePL;
		}
		public void setHasNumNegativeDepmode(Boolean hasNumNegativeDepmode) {
			this.hasNumNegativeDepmode = hasNumNegativeDepmode;
		}
		public void setHasNumNegativeSLANG(Boolean hasNumNegativeSLANG) {
			this.hasNumNegativeSLANG = hasNumNegativeSLANG;
		}
		public void setHasNumNegativeEMOTICON(Boolean hasNumNegativeEMOTICON) {
			this.hasNumNegativeEMOTICON = hasNumNegativeEMOTICON;
		}
		public void setHasNumPunctuations(Boolean hasNumPunctuations) {
			this.hasNumPunctuations = hasNumPunctuations;
		}
		public void setHasNumElongatedWords(Boolean hasNumElongatedWords) {
			this.hasNumElongatedWords = hasNumElongatedWords;
		}
		public void setHasNumDSLWords(Boolean hasNumDSLWords) {
			this.hasNumDSLWords = hasNumDSLWords;
		}
		public void setClassLabels(List<ClassLabel> classLabels) {
			this.classLabels = classLabels;
		}
		public void setHasNumWeakSubjPL(Boolean hasNumWeakSubjPL) {
			this.hasNumWeakSubjPL = hasNumWeakSubjPL;
		}
		public void setHasNumStrongSubjPL(Boolean hasNumStrongSubjPL) {
			this.hasNumStrongSubjPL = hasNumStrongSubjPL;
		}

		public int getNumExtraFeatures(){

			if(!includeExtraFeatures) return 0;

			int counter=0;
			//Negative
			if(this.hasNumNegativeEMOTICON()){
				counter++;
			}
			if(this.hasNumNegativeGI()){
				counter++;
			}
			if(this.hasNumNegativePL()){
				counter++;
			}
			if(this.hasNumNegativeDepmode()){
				counter++;
			}
			if(this.hasNumNegativeSLANG()){
				counter++;
			}
			if(this.hasNumNegativeSWN()){
				counter++;
			}
			if(this.hasNumNegativeNRC()){
				counter++;
			}
			//Positive
			if(this.hasNumPositiveEMOTICON()){
				counter++;
			}
			if(this.hasNumPositiveGI()){
				counter++;
			}
			if(this.hasNumPositivePL()){
				counter++;
			}
			if(this.hasNumPositiveDepmode()){
				counter++;
			}
			if(this.hasNumPositiveSLANG()){
				counter++;
			}
			if(this.hasNumPositiveSWN()){
				counter++;
			}
			if(this.hasNumPositiveNRC()){
				counter++;
			}
			//Others
			if(this.hasNumDSLWords()){
				counter++;
			}
			if(this.hasNumElongatedWords()){
				counter++;
			}
			if(this.hasNumPunctuations()){
				counter++;
			}
			if(this.hasNumStrongSubjPL()){
				counter++;
			}
			if(this.hasNumWeakSubjPL()){
				counter++;
			}
			//For relevant irrelevant classification
			if(this.hasPriorClass()){
				counter++;
			}
			//For reference to the parent comment
			if("edu".equalsIgnoreCase(level)){
				if(this.hasSheetID()){
					counter++;
				}if(this.hasCommentID()){
					counter++;
				}if(this.hasSheetID()){
					counter++;
				}
			}
			if(this.hasLastEduSenti()){
				counter++;
			}
				
			return counter;
		}

		public List<ClassLabel> getClassLabels(){
			return classLabels;
		}
		
		public boolean hasRelIrrelClasses(){
			return  classLabels.size() == 2 &&
					classLabels.contains(ClassLabel.RELEVANT) &&
					classLabels.contains(ClassLabel.IRRELEVANT);
		}

	}

}
