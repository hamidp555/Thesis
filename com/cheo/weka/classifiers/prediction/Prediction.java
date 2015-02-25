package com.cheo.weka.classifiers.prediction;


import java.util.LinkedList;
import java.util.List;

import com.cheo.base.enums.ClassLabel;

public class Prediction {
	
	private Integer index;

	private ClassLabel actual;

	private ClassLabel predicted;

	private Boolean error = false;

	private List<Double> distribution = new LinkedList<Double>();
	
	private Reference reference = new Reference();

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public ClassLabel getActual() {
		return actual;
	}

	public void setActual(ClassLabel actual) {
		this.actual = actual;
	}

	public ClassLabel getPredicted() {
		return predicted;
	}

	public void setPredicted(ClassLabel predicted) {
		this.predicted = predicted;
	}
	
	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public List<Double> getDistribution() {
		return distribution;
	}

	public void setDistribution(List<Double> distribution) {
		this.distribution = distribution;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public boolean isEmpty(){
		return this.getActual() == null &&
				this.getPredicted() == null &&
				this.getReference().isEmpty() &&
				this.getDistribution().isEmpty() &&
				this.getError() == null &&
				this.getIndex() == null;
	}


	public class Reference {
		
		private Integer sheetID;
		private Integer commentID;
		private Integer eduID;
		
		public Integer getSheetID() {
			return sheetID;
		}

		public void setSheetID(Integer sheetID) {
			this.sheetID = sheetID;
		}

		public Integer getCommentID() {
			return commentID;
		}

		public void setCommentID(Integer commentID) {
			this.commentID = commentID;
		}

		public Integer getEduID() {
			return eduID;
		}

		public void setEduID(Integer eduID) {
			this.eduID = eduID;
		}

		public boolean isEmpty(){
			return this.getCommentID() == null &&
					this.getCommentID() == null &&
					this.getEduID() == null;
		}
	}
}




