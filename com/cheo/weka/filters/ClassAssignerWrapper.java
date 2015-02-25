package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ClassAssigner;

public class ClassAssignerWrapper {

	public static Instances apply(Instances data, String classIndex) throws Exception {
		ClassAssigner filter = build(classIndex);
		filter.setInputFormat(data);
		return Filter.useFilter(data, filter);
		
	}
	
	public static ClassAssigner build(String classIndex) throws Exception {
		ClassAssigner filter = new ClassAssigner();
		filter.setDebug(false);
		filter.setClassIndex(classIndex);
		return filter;
	}

}
