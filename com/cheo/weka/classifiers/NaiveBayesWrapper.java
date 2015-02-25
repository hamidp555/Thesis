package com.cheo.weka.classifiers;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.Range;

public class NaiveBayesWrapper extends AbstractClassifierWrapper implements weka.core.WeightedInstancesHandler{

	@Override
	public NaiveBayes build() throws Exception {
		NaiveBayes classifier = new NaiveBayes();
		classifier.setDebug(false);
		return classifier;
	}

	@Override
	public void classifyAndEvaluate(Instances data, String predictionOutputRange)
			throws Exception {
		NaiveBayes classifier = build();
		classifier.buildClassifier(data);
		setClassifier(classifier);

		Evaluation evaluation = new Evaluation(data);
		StringBuffer prediction = new StringBuffer();
		evaluation.crossValidateModel(classifier, data, 10, new Random(1),prediction, new Range(predictionOutputRange), true);

		setEvaluation(evaluation);
		setPrediction(prediction);
		
	}


}
