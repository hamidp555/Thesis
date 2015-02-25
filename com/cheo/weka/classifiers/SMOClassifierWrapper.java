package com.cheo.weka.classifiers;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instances;
import weka.core.Range;
import weka.core.SelectedTag;

public class SMOClassifierWrapper extends  AbstractClassifierWrapper {

	@Override
	public SMO build() throws Exception{
		SMO classifier = new SMO();
		classifier.setBuildLogisticModels(false);
		classifier.setC(1.0);
		classifier.setChecksTurnedOff(false);
		classifier.setDebug(false);
		classifier.setEpsilon(1.0E-12);

		SelectedTag tag = new SelectedTag(SMO.FILTER_NORMALIZE, SMO.TAGS_FILTER);
		classifier.setFilterType(tag);

		PolyKernel kernel = new PolyKernel();
		kernel.setCacheSize(250007);
		kernel.setChecksTurnedOff(false);
		kernel.setDebug(false);
		kernel.setExponent(1.0);
		kernel.setUseLowerOrder(false);
		classifier.setKernel(kernel);

		classifier.setNumFolds(-1);
		classifier.setRandomSeed(1);
		classifier.setToleranceParameter(0.001);

		return classifier;
	}

	@Override
	public void classifyAndEvaluate(Instances instances, String predictionOutputRange) 
			throws Exception {
		SMO classifier = build();
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
