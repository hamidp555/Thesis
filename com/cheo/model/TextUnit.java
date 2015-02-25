package com.cheo.model;

import com.cheo.base.enums.ClassLabel;

public interface TextUnit {
	
	ClassLabel getClassLabel() throws Exception;
	
	String getComment();

	Integer getSheetID();
	
	Integer getCommentID();
	
	Integer getEduID();
	
	boolean isRelevant();
	
	boolean hasClassLabel();
	
	boolean isAnnotated();

}
