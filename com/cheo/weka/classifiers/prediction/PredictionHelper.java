package com.cheo.weka.classifiers.prediction;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import com.cheo.base.enums.ClassLabel;
import com.cheo.model.Comment;
import com.cheo.services.arff.ArffHelper;

public class PredictionHelper implements InitializingBean{
	
	private Map<String, Prediction> predictionMap;

	private ArffHelper arffHelper;
	
	private String predictonFileName;
	
	public void setPredictonFileName(String predictonFileName) {
		this.predictonFileName = predictonFileName;
	}

	public void setArffHelper(ArffHelper arffHelper) {
		this.arffHelper = arffHelper;
	}

	public Map<String, Prediction> getPredictions() {
		return predictionMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		PredictionUtils.getPredictionMap(predictonFileName);
	}

	//For some reason some edus are repeated from the first sheet
	public ClassLabel getLastEduClazzPrediction(Comment comment) throws Exception{

		String compositeKey = comment.getCompositeKey();
		Prediction prediction = predictionMap.get(compositeKey);

		ClassLabel predicted = null;
		if(prediction != null){
			ClassLabel label = prediction.getPredicted();
			predicted =  getAdaptedClassLabel(label);
		}else{
			predicted = ClassLabel.IRRELEVANT;
		}

		return   predicted; 
	}

	private ClassLabel getAdaptedClassLabel(ClassLabel predicted){

		//IF BINARY CLASIFICATION RELEVANT / IRRELEVANT
		if(arffHelper.hasRelIrrelClasses()){
			boolean relevant =  !predicted.equals(ClassLabel.IRRELEVANT) && 
					(predicted.equals(ClassLabel.SUBJECTIVE) || predicted.equals(ClassLabel.OBJECTIVE));
			return relevant ? 
					ClassLabel.RELEVANT : 
						ClassLabel.IRRELEVANT;
		}else{
			if(arffHelper.containsClazz(predicted)){
				return predicted;
			}else{
				return ClassLabel.IRRELEVANT;
			}
		}

	}

}
