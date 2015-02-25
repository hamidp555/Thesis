package com.cheo.services.hildaTree.stats;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.LinkedBinaryTree;
import com.cheo.weka.classifiers.prediction.Prediction;
import com.cheo.weka.classifiers.prediction.PredictionHelper;

public class PredictionInitializer implements ITreeInitializer, InitializingBean {

	private PredictionHelper predictionHelper;

	private Map<String, Prediction> precidtions;

	public void setPredictionHelper(PredictionHelper predictionHelper) {
		this.predictionHelper = predictionHelper;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		precidtions = predictionHelper.getPredictions();
	}
	
	@Override
	public void initialize(LinkedBinaryTree<Element> tree, int sheetID,
			int commentID) {
		Traverse traverse = new Traverse();
		traverse.addPredictions(tree, tree.root(), precidtions);

	}

}
