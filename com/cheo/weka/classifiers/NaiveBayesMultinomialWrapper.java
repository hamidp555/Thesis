package com.cheo.weka.classifiers;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instances;
import weka.core.Range;

public class NaiveBayesMultinomialWrapper extends AbstractClassifierWrapper implements weka.core.WeightedInstancesHandler{

	@Override
	public NaiveBayesMultinomial build() throws Exception{
		NaiveBayesMultinomial classifier = new NaiveBayesMultinomial();
		classifier.setDebug(false);
		return classifier;
	}

	@Override
	public void classifyAndEvaluate(Instances data, String predictionOutputRange) throws Exception {
		NaiveBayesMultinomial classifier = build();
		classifier.buildClassifier(data);
		setClassifier(classifier);

		Evaluation evaluation = new Evaluation(data);
		StringBuffer prediction = new StringBuffer();
		evaluation.crossValidateModel(classifier, data, 10, new Random(1),prediction, new Range(predictionOutputRange), true);

		setEvaluation(evaluation);
		setPrediction(prediction);
	}
}

