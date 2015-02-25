package com.cheo.weka.classifiers.prediction;

import java.io.File;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.cheo.base.enums.ClassLabel;
import com.google.common.collect.TreeBasedTable;

public class PredictionUtils {

	private PredictionUtils(){}

	private final static String NULL_KEY = "null-null-null"; 

	public static TreeBasedTable<String, Integer, Prediction> loadResources(String pattern) throws Exception{
		TreeBasedTable<String, Integer, Prediction>  predictionTable = TreeBasedTable.create();
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:"+pattern);
		for(Resource resource : resources){
			File file = resource.getFile();
			@SuppressWarnings("unchecked")
			List<String> lines = FileUtils.readLines(file, "utf-8");
			int instaneIndex = 0;
			for(String line : lines){
				Prediction pred = PredictionParser.parse(line);
				if (!pred.isEmpty()){
					predictionTable.put(file.getName(), instaneIndex, pred);
				}
				instaneIndex++;
			}
		}
		return predictionTable;
	}

	public static Map<Integer, Prediction> loadResource(String path) throws Exception{
		Map<Integer, Prediction>  predictionMap = new HashMap<Integer, Prediction>();
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:" + path);

		File file = resource.getFile();
		@SuppressWarnings("unchecked")
		List<String> lines = FileUtils.readLines(file, "utf-8");
		int instaneIndex = 0;
		for(String line : lines){
			Prediction pred = PredictionParser.parse(line);
			if (!pred.isEmpty()){
				predictionMap.put(instaneIndex, pred);
			}
			instaneIndex++;
		}

		return predictionMap;
	}

	public static Set<ClassLabel> getClasLabels(Map<Integer, Prediction> predictions){
		Set<ClassLabel> classLabels = new HashSet<ClassLabel>();
		for(Entry<Integer, Prediction> predictionEntry : predictions.entrySet()){
			Prediction prediction = predictionEntry.getValue();
			classLabels.add(prediction.getPredicted());
			classLabels.add(prediction.getActual());
		}
		return classLabels;
	}

	public static Map<String, Deque<Fold>>  generateFoldsForPredictions(String pattern) throws Exception{

		TreeBasedTable<String, Integer, Prediction>  predictionFiles = loadResources(pattern);
		
		Map<String, Deque<Fold>> result = new HashMap<String, Deque<Fold>>();
		for(Entry<String, Map<Integer, Prediction>> predictionFile : predictionFiles.rowMap().entrySet()){
			
			Deque<Fold> folds = new LinkedList<Fold>();
			Map<Integer, Prediction> predictions = predictionFile.getValue();
			String fileName = predictionFile.getKey();
			Set<ClassLabel> classLabels = getClasLabels(predictions);

			for(Entry<Integer, Prediction> predictionEntry : predictions.entrySet()){
				Prediction prediction = predictionEntry.getValue();
				if(prediction.getIndex() == 1){
					Fold fold = new Fold();
					fold.setClassLabels(classLabels);
					folds.add(fold);
					folds.getLast().setIndex(folds.size());
				}
				folds.getLast().getPredictions().add(prediction);
			}

			result.put(fileName, folds);
		}

		return result;
	}

	public static Deque<Fold> generateFoldsForPrediction(String path) throws Exception{

		Map<Integer, Prediction>  predictionMap = loadResource(path);

		Deque<Fold> folds = new LinkedList<Fold>();
		Set<ClassLabel> classLabels = getClasLabels(predictionMap);

		for(Entry<Integer, Prediction> predictionEntry : predictionMap.entrySet()){
			Prediction prediction = predictionEntry.getValue();
			if(prediction.getIndex() == 1){
				Fold fold = new Fold();
				fold.setClassLabels(classLabels);
				folds.add(fold);
				folds.getLast().setIndex(folds.size());
			}
			folds.getLast().getPredictions().add(prediction);
		}

		return folds;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Prediction> getPredictionMap(String predictionFilePath) throws Exception{
		Map<String, Prediction> predictionsMap = new HashMap<String, Prediction>();
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:" + predictionFilePath);
		List<String> lines = FileUtils.readLines(resource.getFile(), "utf-8");
		for(String line : lines){
			Prediction prediction = PredictionParser.parse(line);
			if (!prediction.isEmpty()){ 
				String key = getKeyForPrediction(prediction);
				if(!key.equalsIgnoreCase(NULL_KEY)){
					predictionsMap.put(key, prediction);
				}
			}
		}
		return predictionsMap;
	}

	private static String getKeyForPrediction(Prediction prediction){
		StringBuilder sb = new StringBuilder();
		sb.append(prediction.getReference().getSheetID());
		sb.append("-");
		sb.append(prediction.getReference().getCommentID());
		sb.append("-");
		sb.append(prediction.getReference().getEduID());
		return sb.toString();
	}
}
