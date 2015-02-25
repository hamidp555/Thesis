package com.cheo.weka.classifiers;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.util.Assert;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.Vote;
import weka.core.Instances;
import weka.core.Range;
import weka.core.SelectedTag;

public class VoteClassifierWrapper extends  AbstractClassifierWrapper {
	
	List<Classifier> classifers = new LinkedList<Classifier>();

	@Override
	public Vote build() throws Exception {
		
		Assert.notEmpty(classifers, "Please add at laest two classifeirs!");
		int numClassifiers = classifers.size();
		
		Classifier[] css = new Classifier[numClassifiers];
		for(int i=0; i<numClassifiers; i++){
			css[i]=classifers.get(i);
		}
				
		Vote classifier = new Vote();
		classifier.setClassifiers(css);
		
		SelectedTag selectedTag = new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES);
		classifier.setCombinationRule(selectedTag);
		classifier.setDebug(false);
		classifier.setSeed(1);
		
		return classifier;
	}


	@Override
	public void classifyAndEvaluate(Instances instances, String predictionOutputRange) throws Exception {

		Vote classifier = build();
		classifier.buildClassifier(instances);
		setClassifier(classifier);
		

		Evaluation evaluation = new Evaluation(instances);
		StringBuffer prediction = new StringBuffer();
		evaluation.crossValidateModel(classifier, instances, 10, new Random(1),prediction, new Range(predictionOutputRange), false);

		setEvaluation(evaluation);
		setPrediction(prediction);
		
	}
	
	public void add(Classifier classifier){
		classifers.add(classifier);
	}

}
