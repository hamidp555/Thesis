package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;

public class SMOTEFilterWrapper{

	public static Instances apply(Instances data, String classIndex, double percentToCreate) throws Exception {
		SMOTE filter = build(classIndex, percentToCreate);
		filter.setInputFormat(data);
		Instances filteredData = Filter.useFilter(data, filter);
		
		return ClassAssignerWrapper.apply(filteredData, "first");
	}

	/**
	 * 
	 * @param classLabelToOverSample
	 * @return
	 * @throws Exception
	 */
	public static SMOTE build(String classIndex, double percentToCreate) throws Exception {
		SMOTE smote = new SMOTE();
		smote.setClassValue(classIndex);
		smote.setNearestNeighbors(5);
		smote.setPercentage(percentToCreate);
		smote.setRandomSeed(1);
		return smote;
	}

}
