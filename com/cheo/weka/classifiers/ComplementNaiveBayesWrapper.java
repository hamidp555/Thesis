package com.cheo.weka.classifiers;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.ComplementNaiveBayes;
import weka.core.Instances;
import weka.core.Range;

public class ComplementNaiveBayesWrapper  extends AbstractClassifierWrapper {

	@Override
	public ComplementNaiveBayes build() throws Exception{
		ComplementNaiveBayes classifier = new ComplementNaiveBayes();
		classifier.setNormalizeWordWeights(false);
		classifier.setSmoothingParameter(1.0);
		classifier.setDebug(false);
		
		return classifier;
	}

	@Override
	public void classifyAndEvaluate(Instances data,String predictionOutputRange) throws Exception {
		ComplementNaiveBayes classifier = build();
		classifier.buildClassifier(data);
		setClassifier(classifier);
		
		Evaluation evaluation = new Evaluation(data);
		StringBuffer prediction = new StringBuffer();
		evaluation.crossValidateModel(classifier, data, 10, new Random(1),prediction, new Range(predictionOutputRange), true);
		
		setEvaluation(evaluation);
		setPrediction(prediction);
		
	}

}
