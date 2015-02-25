package com.cheo.services.hildaTree.classifier;

import com.cheo.base.enums.ClassLabel;
import com.cheo.services.hildaTree.RSTRelation;

public interface IHildaRule {
	
	boolean isMatch(RSTRelation operator);
	
	ClassLabel apply(ClassLabel left, ClassLabel right);
	 
}
