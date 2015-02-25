package com.cheo.services.hildaTree;

import com.cheo.base.enums.ClassLabel;
import com.cheo.weka.classifiers.prediction.Prediction;

public class Element {
	
	RSTNodeType nodeType;
	
	RSTRelation rstRelation;
	
	TreeEDU edu = new TreeEDU();
	
	String span;

	public String getSpan() {
		return span;
	}

	public void setSpan(String span) {
		this.span = span;
	}

	public TreeEDU getEdu() {
		return edu;
	}

	public void setEdu(TreeEDU edu) {
		this.edu = edu;
	}

	public RSTNodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(RSTNodeType nodeType) {
		this.nodeType = nodeType;
	}

	public RSTRelation getRstRelation() {
		return rstRelation;
	}

	public void setRstRelation(RSTRelation rstRelation) {
		this.rstRelation = rstRelation;
	}

	@Override
	public String toString(){
		String s = nodeType +" (" + span + ") "+ "(" + rstRelation + " )";
		if(edu != null)
			s+= "(" + "text _!_!"+ edu + "!__!" + ")";
		return s;
	}
	
	public class TreeEDU{
		
		private ClassLabel calculatedClassLabel;
		
		private ClassLabel ActualClassLabel;
		
		private Prediction prediction;
		
		private String content;
		
		private int sheetID;
		
		private int eduID;
		
		private int commentID;

		public int getSheetID() {
			return sheetID;
		}

		public void setSheetID(int sheetID) {
			this.sheetID = sheetID;
		}

		public int getEduID() {
			return eduID;
		}

		public void setEduID(int eduID) {
			this.eduID = eduID;
		}

		public int getCommentID() {
			return commentID;
		}

		public void setCommentID(int commentID) {
			this.commentID = commentID;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public ClassLabel getCalculatedClassLabel() {
			return calculatedClassLabel;
		}

		public void setCalculatedClassLabel(ClassLabel calculatedClassLabel) {
			this.calculatedClassLabel = calculatedClassLabel;
		}

		public ClassLabel getActualClassLabel() {
			return ActualClassLabel;
		}

		public void setActualClassLabel(ClassLabel actualClassLabel) {
			ActualClassLabel = actualClassLabel;
		}

		public Prediction getPrediction() {
			return prediction;
		}

		public void setPrediction(Prediction prediction) {
			this.prediction = prediction;
		}
		
	}

}
