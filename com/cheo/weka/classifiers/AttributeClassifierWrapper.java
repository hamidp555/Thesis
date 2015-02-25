package com.cheo.weka.classifiers;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.core.Instances;
import weka.core.Range;


public class AttributeClassifierWrapper extends AbstractClassifierWrapper{

	private Classifier classifier;

	@Override
	public void classifyAndEvaluate(Instances data, String predictionOutputRange) throws Exception {
		AttributeSelectedClassifier classifier = build();
		setClassifier(classifier);

		Evaluation evaluation = new Evaluation(data);
		StringBuffer prediction = new StringBuffer();
		//"first,last" for range
		evaluation.crossValidateModel(classifier, data, 10, new Random(1),prediction, new Range(predictionOutputRange), true);

		setEvaluation(evaluation);
		setPrediction(prediction);

	}

	@Override
	public AttributeSelectedClassifier build() throws Exception {
		Assert.notNull(classifier, "Classifier is NULL!");
		setClassifier(classifier);
		
		InfoGainAttributeEval infoGain = new InfoGainAttributeEval();
		infoGain.setBinarizeNumericAttributes(false);
		infoGain.setMissingMerge(true);

		Ranker ranker = new Ranker();
		ranker.setNumToSelect(-1);
		ranker.setStartSet(StringUtils.EMPTY);
		ranker.setThreshold(0);
		ranker.setGenerateRanking(true);

		AttributeSelectedClassifier attrSelectedClassifier = new AttributeSelectedClassifier();
		attrSelectedClassifier.setClassifier(classifier);
		attrSelectedClassifier.setEvaluator(infoGain);
		attrSelectedClassifier.setSearch(ranker);

		return attrSelectedClassifier;
	}
	
	public void add(Classifier classifier){
		this.classifier = classifier;
	}
}
