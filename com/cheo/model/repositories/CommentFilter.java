package com.cheo.model.repositories;

import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cheo.base.enums.ClassLabel;
import com.cheo.model.Comment;
import com.cheo.model.EDU;
import com.cheo.weka.classifiers.prediction.Fold;
import com.cheo.weka.classifiers.prediction.Prediction;
import com.cheo.weka.classifiers.prediction.PredictionUtils;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import org.springframework.beans.factory.InitializingBean;

public class CommentFilter implements InitializingBean {

	public static final Comparator<Prediction> COMPARATOR = new Comparator<Prediction>() {
		@Override
		public int compare(Prediction o1, Prediction o2) {
			return o1.getReference().getSheetID().compareTo(o2.getReference().getSheetID());
		}
	};

	private Map<String, Prediction> predictionMap;

	private String predictonFileName;

	public void setPredictonFileName(String predictonFileName) {
		this.predictonFileName = predictonFileName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		predictionMap = PredictionUtils.getPredictionMap(predictonFileName);
	}

	public TreeBasedTable<Integer, Integer, Comment> getCorrectlyClassifiedCommentsFromPredictionByClasslabel(
			TreeBasedTable<Integer, Integer, Comment> comments, 
			String predictionLocation, 
			ClassLabel correctlyClassifiedClasLabel) throws Exception{

		//Get correctly classified RELEVANT comments references
		Deque<Fold> folds = PredictionUtils.generateFoldsForPrediction(predictionLocation);
		List<Prediction> predictions = new LinkedList<Prediction>();
		for(Fold fold : folds){
			List<Prediction> predictionsForFold = fold.getCorrectlyClassifiedInstances(correctlyClassifiedClasLabel);
			Collections.sort(predictionsForFold, COMPARATOR);
			predictions.addAll(predictionsForFold);
		}

		//Get correctly classified RELEVANT comments 
		TreeBasedTable<Integer, Integer, Comment> filteredComments = TreeBasedTable.create();
		Iterator<Table.Cell<Integer, Integer, Comment>> iter = comments.cellSet().iterator();

		while(iter.hasNext()){
			Comment comment = iter.next().getValue();
			int sheetID = comment.getSheetID();
			int commentID = comment.getCommentID();
			for(Prediction prediction : predictions){				
				int predictionSheetDI = prediction.getReference().getSheetID();
				int predictionCommentID = prediction.getReference().getCommentID();
				if(sheetID == predictionSheetDI && commentID == predictionCommentID){
					filteredComments.put(sheetID, commentID, comment);
					break;
				}
			}
		}

		return filteredComments;
	}

	public Comment removeIrrelevantEDUs(Comment comment) throws Exception{

		List<EDU> relevantEDUs = new LinkedList<EDU>();
		List<String> relevantEDUsText = new LinkedList<String>();

		for(EDU edu: comment.getEdus()){
			String compositeKey = edu.getCompositeKey();
			Prediction predictionForEDU = predictionMap.get(compositeKey);
			if(predictionForEDU != null){
				relevantEDUs.add(edu);
				relevantEDUsText.add(edu.getComment());
			}
		}

		String commentWithRelevantEDUs = StringUtils.join(relevantEDUsText, " ");
		comment.setComment(commentWithRelevantEDUs);
		comment.setEdus(relevantEDUs);

		return comment;

	}

}
