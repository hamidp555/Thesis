package com.cheo.weka.classifiers;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.rules.ZeroR;
import weka.core.Instances;
import weka.core.Range;

public class ZeroRClassifierWrapper extends  AbstractClassifierWrapper{

	@Override
	public ZeroR build() throws Exception {
		ZeroR classifier = new ZeroR();
		classifier.setDebug(false);
		return classifier;
	}

	@Override
	public void classifyAndEvaluate(Instances instances, String predictionOutputRange)
			throws Exception {
		ZeroR classifier = build();
		classifier.buildClassifier(instances);
		setClassifier(classifier); ;
		
		Evaluation evaluation = new Evaluation(instances);
		StringBuffer prediction = new StringBuffer();
		// valid "first,last"
		//2,3,4 for sheetID, comemntID, eduID
		evaluation.crossValidateModel(classifier, instances, 10, new Random(1), prediction, new Range(predictionOutputRange), true);
		
		setEvaluation(evaluation);
		setPrediction(prediction);
		
	}

}
