package com.cheo.weka.classifiers.prediction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.cheo.base.enums.ClassLabel;

public class Fold {

	private List<Prediction> predictions = new LinkedList<Prediction>();

	private Integer index ;

	private Double accuracy;

	private Set<ClassLabel> classLabels;

	public List<Prediction> getPredictions() {
		return predictions;
	}

	public void setPredictions(List<Prediction> predictions) {
		this.predictions = predictions;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public Set<ClassLabel> getClassLabels() {
		return classLabels;
	}

	public void setClassLabels(Set<ClassLabel> classLabels) {
		this.classLabels = classLabels;
	}

	public Map<ClassLabel, List<Prediction>> getCorrectlyClassifiedInstances(){
		Map<ClassLabel, List<Prediction>> result = new HashMap<ClassLabel, List<Prediction>>();
		for(ClassLabel classLabel : this.getClassLabels()){
			result.put(classLabel, getCorrectlyClassifiedInstances(classLabel));
		}
		return result;
	}
	
	public List<Prediction> getCorrectlyClassifiedInstances(ClassLabel classLabel){
		List<Prediction> predictionList = new LinkedList<Prediction>();
		for(Prediction prediction : this.getPredictions()){
			ClassLabel actualClass = prediction.getActual();
			ClassLabel predictedClass = prediction.getPredicted();
			if(actualClass.equals(predictedClass) && predictedClass.equals(classLabel)){
				predictionList.add(prediction);
			}
		}
		return predictionList;
	}
	
	public Map<ClassLabel, Integer> getCorrectlyClassifiedMap(){

		Map<ClassLabel, Integer> correctlyClassifiedMap = 
				new HashMap<ClassLabel, Integer>();

		for(ClassLabel classLabel : this.getClassLabels()){
			Integer correctlyClassified = 0;
			for(Prediction prediction : this.getPredictions()){

				ClassLabel actualClass = prediction.getActual();
				ClassLabel predictedClass = prediction.getPredicted();

				if(actualClass.equals(predictedClass) && predictedClass.equals(classLabel)){
					correctlyClassified++;
				}
			}
			correctlyClassifiedMap.put(classLabel, correctlyClassified);
		}
		return correctlyClassifiedMap;
	}

	public double calculateAccuracy(){

		int total = this.getPredictions().size();

		Map<ClassLabel, Integer> correctlyClassifiedMap = getCorrectlyClassifiedMap();

		int correctlyClassifiedCount = 0;
		for(Entry<ClassLabel, Integer> entry : correctlyClassifiedMap.entrySet()){
			correctlyClassifiedCount = correctlyClassifiedCount + entry.getValue();
		}

		double  accuracy = ((double) correctlyClassifiedCount)/total;

		return accuracy;
	}
	
	public int size(){
		return this.getPredictions().size();
	}
	
	public int getCorrectlyClassifiedCount(ClassLabel classLabel){
		return this.getCorrectlyClassifiedMap().get(classLabel);
	}
	
	public int getCorrectlyClassifiedCount(){
		int correctlyClassifiedCount = 0;
		for(Entry<ClassLabel, Integer> entry : this.getCorrectlyClassifiedMap().entrySet()){
			correctlyClassifiedCount = correctlyClassifiedCount + entry.getValue();
		}
		return correctlyClassifiedCount;
	}

}
