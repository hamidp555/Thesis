package com.cheo.weka.classifiers.prediction;

import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cheo.base.enums.ClassLabel;

import org.apache.commons.math3.stat.inference.TTest;

public class TestOfSignificance {

	final static private double SIGNIFICANCE_LEVEL= 0.05; 

	public boolean isDifferenceSignificantForFlatVsTwoLevel(String pathToClassifierPredictionsA, String pathToClassifiersPredictionsB) throws Exception{
		return isDifferenceSignificantFlatVsTwoLevel(pathToClassifierPredictionsA, pathToClassifiersPredictionsB);
	}

	public boolean isDifferenceInAccuracySignificantFlatVsFlat(String folderA, String folderB) throws Exception{
		return isDifferenceSignificantFlatVsFlat(folderA, folderB);
	}

	public boolean isDifferenceInAccuracySignificantTwoLevelVsTwoLevel(String folderA, String folderB) throws Exception{
		return isDifferenceSignificantTwoLevelVsTwoLevel(folderA, folderB);
	}
	
	private  boolean isDifferenceSignificantFlatVsTwoLevel(String pathToClassifierPredictionsA, String pathToClassifiersPredictionsB) throws Exception{
		Deque<Fold> foldsA = PredictionUtils.generateFoldsForPrediction(pathToClassifierPredictionsA);
		Map<String, Deque<Fold>> foldsB = PredictionUtils.generateFoldsForPredictions(pathToClassifiersPredictionsB);
		
		double[] accuraciesA =  calculateAccuraciesForClassifier(foldsA);
		double[] accuraciesB = calculateAccuraciesForClassifiers(foldsB);
		
		TTest ttest = new TTest();
		boolean isDifferenceSignificant = ttest.pairedTTest(accuraciesA, accuraciesB, SIGNIFICANCE_LEVEL);
		return isDifferenceSignificant;
		
	}
	
	private  boolean isDifferenceSignificantFlatVsFlat(String pathToClassifierPredictionsA, String pathToClassifierPredictionsB) throws Exception{
		Deque<Fold> foldsA = PredictionUtils.generateFoldsForPrediction(pathToClassifierPredictionsA);
		Deque<Fold> foldsB = PredictionUtils.generateFoldsForPrediction(pathToClassifierPredictionsB);
		
		double[] accuraciesA =  calculateAccuraciesForClassifier(foldsA);
		double[] accuraciesB = calculateAccuraciesForClassifier(foldsB);

		TTest ttest = new TTest();
		boolean isDifferenceSignificant = ttest.pairedTTest(accuraciesA, accuraciesB, SIGNIFICANCE_LEVEL);
		return isDifferenceSignificant;
	}

	private  boolean isDifferenceSignificantTwoLevelVsTwoLevel(String pathToClassifiersPredictionsA, String pathToClassifiersPredictionsB) throws Exception{

		Map<String, Deque<Fold>> foldsA= PredictionUtils.generateFoldsForPredictions(pathToClassifiersPredictionsA);
		Map<String, Deque<Fold>> foldsB = PredictionUtils.generateFoldsForPredictions(pathToClassifiersPredictionsB);

		double[] accuraciesA =  calculateAccuraciesForClassifiers(foldsA);
		double[] accuraciesB = calculateAccuraciesForClassifiers(foldsB);

		TTest ttest = new TTest();
		boolean isDifferenceSignificant = ttest.pairedTTest(accuraciesA, accuraciesB, SIGNIFICANCE_LEVEL);
		return isDifferenceSignificant;
	}

	private double[] calculateAccuraciesForClassifier(Deque<Fold> classifierFolds) throws Exception{
		
		Set<ClassLabel> classLabels = getClassLabels(classifierFolds);
		double[] accuracies = new double[10];
		int counter = 0;
		while(!classifierFolds.isEmpty()){

			Fold classifierFold = classifierFolds.poll();
			int totalNumInstances = classifierFold.size();
			int correctlyClassifiedCount = 0;
			for(ClassLabel classLabel : classLabels){
				correctlyClassifiedCount = correctlyClassifiedCount + classifierFold.getCorrectlyClassifiedCount(classLabel);
			}
			double accuracy = ((double)(correctlyClassifiedCount))/(totalNumInstances);
			accuracies[counter] = accuracy;
			counter++;
		}
		return accuracies;
	}

	private double[] calculateAccuraciesForClassifiers(Map<String, Deque<Fold>> classifiersFolds) throws Exception{

		Deque<Fold> firstClassifierFolds = null;
		Deque<Fold> secondClassifierFolds = null;
		for(Entry<String, Deque<Fold>> classifierFolds : classifiersFolds.entrySet()){
			if(classifierFolds.getKey().contains("_firstClassifier")){
				firstClassifierFolds = classifierFolds.getValue();
			}
			if(classifierFolds.getKey().contains("_secondClassifier")){
				secondClassifierFolds = classifierFolds.getValue();
			}
		}
		validate(firstClassifierFolds, secondClassifierFolds);

		Set<ClassLabel> classLabelsFirstClassifier = getClassLabels(firstClassifierFolds);
		Set<ClassLabel> classLabelsSecondClassifier =  getClassLabels(secondClassifierFolds);

		double[] accuracies = new double[10];
		int counter = 0;
		while(!firstClassifierFolds.isEmpty()){

			Fold firstClassifierFold = firstClassifierFolds.poll();
			Fold secondClassifierFold = secondClassifierFolds.poll();

			int totalNumInstances_firstClassifier = firstClassifierFold.size();
			int totalNumInstances_secondClassifier = secondClassifierFold.size();
			int totalNumInstances = totalNumInstances_firstClassifier + totalNumInstances_secondClassifier;

			int correctlyClassifiedFirstClassifier = 0;
			for(ClassLabel classLabel : classLabelsFirstClassifier){
				correctlyClassifiedFirstClassifier = correctlyClassifiedFirstClassifier + 
						firstClassifierFold.getCorrectlyClassifiedCount(classLabel);
			}

			int correctlyClassifiedSecondClassifier = 0;
			for(ClassLabel classLabel : classLabelsSecondClassifier){
				correctlyClassifiedSecondClassifier = correctlyClassifiedSecondClassifier + 
						secondClassifierFold.getCorrectlyClassifiedCount(classLabel);
			}

			int correctlyClassifiedCount = correctlyClassifiedFirstClassifier + correctlyClassifiedSecondClassifier;

			double accuracy = ((double)(correctlyClassifiedCount))/(totalNumInstances);
			accuracies[counter] = accuracy;
			counter++;
		}
		return accuracies;
	}

	private Set<ClassLabel> getClassLabels(Deque<Fold> folds){
		Set<ClassLabel> classLabels =  new HashSet<ClassLabel>();
		for(Fold fold : folds){
			classLabels.addAll(fold.getClassLabels());
		}
		return classLabels; 
	}

	private void validate(Deque<Fold> d1, Deque<Fold> d2  ) throws Exception{
		if(d1.size() != d2.size()){
			throw new Exception("folds size error!");
		}
		if(d1.size() != 10 || d2.size() != 10){
			throw new Exception("10 folds erquired!");
		}
	}

}
