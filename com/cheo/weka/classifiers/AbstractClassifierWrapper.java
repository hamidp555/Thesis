package com.cheo.weka.classifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cheo.weka.filters.WekaUtils;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Debug;

public abstract class AbstractClassifierWrapper implements IClassifier {

	private Classifier classifier;
	
	private StringBuffer prediction;
	
	private Evaluation evaluation;

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	public StringBuffer getPrediction() {
		return prediction;
	}

	public void setPrediction(StringBuffer prediction) {
		this.prediction = prediction;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}
	
	@Override
	public void saveResult(String path) throws Exception{
		if(this.evaluation == null)
			throw new Exception("Evaluate classifier first.");
	
		List<String> toprint = new LinkedList<String>();
		toprint.add(this.prediction.toString());
		toprint.add(this.evaluation.toSummaryString(true));
		toprint.add(this.evaluation.toClassDetailsString("class detailed statistics"));
		toprint.add(this.evaluation.toMatrixString("confusion matrix"));
		WekaUtils.writeToFile(toprint,path);
	}

	@Override
	public void saveModel(String path) throws Exception{
		if(this.classifier == null)
			throw new Exception("Apply classifier first.");
		Debug.saveToFile(path, this.classifier);
	}
	
	public List<Integer> getMisclassifiedInstanceIndecies()throws Exception{
		if(getClassifier() == null)
			throw new Exception("Apply classifier first.");
		if(getEvaluation() == null)
			throw new Exception("Evaluate classifier first.");
		if(getPrediction() == null)
			throw new Exception("Np prediction found.");
		
		List<String> lines = Arrays.asList(getPrediction().toString().split("\n"));
		int indexToRemove=0;
		List<Integer> misclassified = new ArrayList<Integer>();
		for(String line: lines){
			List<String> tokens = new ArrayList<String>(Arrays.asList(line.split("\\s")));
			Iterator<String> itr = tokens.iterator();
			while(itr.hasNext()){
				String token = itr.next();
				StringUtils.remove(token," ");
				if(token.isEmpty())
					itr.remove();		
			}

			if(tokens.contains("+"))
				misclassified.add(indexToRemove);
			indexToRemove++;
		}
		
		return misclassified;
	}
	
}
