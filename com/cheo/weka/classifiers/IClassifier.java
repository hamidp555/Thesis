package com.cheo.weka.classifiers;


import weka.classifiers.Classifier;
import weka.core.Instances;

public interface IClassifier {
	
	Classifier build() throws Exception;
	
	void classifyAndEvaluate(Instances data, String predictionOutputRange) throws Exception ;

	void saveModel(String modelDestinationFilePath) throws Exception;

	void saveResult(String crossValidationDestFilePath) throws Exception;
	
}
