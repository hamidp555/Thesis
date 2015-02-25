package com.cheo.weka.classifiers;

import java.util.Random;

import org.springframework.util.Assert;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.Range;
import weka.filters.Filter;


public class FilteredClassifierWrapper extends AbstractClassifierWrapper{

	Classifier classifier;
	
	Filter filter;
	
	@Override
	public FilteredClassifier build() throws Exception{
		Assert.notNull(classifier, "Please add one and only one classifier!");
		Assert.notNull(filter, "Please add one and only one filter!");
		
		FilteredClassifier filtered = new FilteredClassifier();
		
		filtered.setClassifier(classifier);
		filtered.setDebug(false);
		filtered.setFilter(filter);

		return filtered;

	}

	@Override
	public void classifyAndEvaluate(Instances data, String predictionOutputRange) throws Exception {
		
		FilteredClassifier classifier = build();
		classifier.buildClassifier(data);
		setClassifier(classifier);

		Evaluation evaluation = new Evaluation(data);
		StringBuffer prediction = new StringBuffer();
		//"first,last" for range
		evaluation.crossValidateModel(classifier, data, 10, new Random(1),prediction, new Range(predictionOutputRange), true);

		setEvaluation(evaluation);
		setPrediction(prediction);

	}
	
	public void addClassifier(Classifier classifier){
		this.classifier = classifier;
	}
	
	public void addFilter(Filter filter){
		this.filter = filter;
	}

}
