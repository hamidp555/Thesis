package com.cheo.weka.filters;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemoveMisclassified;

public class RemoveMisclassifiedFilterWrapper {

	public static RemoveMisclassified build(Classifier c) {
		RemoveMisclassified filter = new RemoveMisclassified();
		filter.setClassifier(c);
		filter.setClassIndex(-1);
		filter.setNumFolds(10);
		filter.setThreshold(0.1);
		filter.setMaxIterations(0);
		return filter;
	}

	public static Instances apply(Classifier classifier, Instances data) throws Exception {
		RemoveMisclassified filter = build(classifier);
		filter.setInputFormat(data);
		Instances filteredData = Filter.useFilter(data, filter);

		return ClassAssignerWrapper.apply(filteredData, "first");
	}

}
