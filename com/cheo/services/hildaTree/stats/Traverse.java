package com.cheo.services.hildaTree.stats;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cheo.model.Comment;
import com.cheo.model.EDU;
import com.cheo.services.hildaTree.BinaryTree;
import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.Position;
import com.cheo.services.hildaTree.RSTRelation;
import com.cheo.weka.classifiers.prediction.Prediction;

public class Traverse {
	
	private List<RelationWrapper> relationWrappers = new LinkedList<RelationWrapper>();

	private List<Element> relElements = new LinkedList<Element>();

	public void addPredictions(BinaryTree<Element> t, 
			Position<Element> v, 
			Map<String, Prediction> precidtions){

		//visit, initialize predictions
		if(t.isExternal(v)){
			Element elem = (Element)v.element();
			int commentID = elem.getEdu().getCommentID();
			int sheetID = elem.getEdu().getSheetID();
			int eduID = elem.getEdu().getEduID();
			String compKey = getCompositeKey(sheetID, commentID, eduID);
			elem.getEdu().setPrediction(precidtions.get(compKey));
		}

		if(t.hasLeft(v)){
			addPredictions(t, t.left(v), precidtions);
		}
		if(t.hasRight(v)){
			addPredictions(t, t.right(v), precidtions);
		}
	}

	public void addCommentID(BinaryTree<Element> t, Position<Element> v, int commentID){

		Element elem = (Element)v.element();
		elem.getEdu().setCommentID(commentID);

		if(t.hasLeft(v)){
			addCommentID(t, t.left(v), commentID);
		}
		if(t.hasRight(v)){
			addCommentID(t, t.right(v), commentID);
		}
	}

	public void addSheetID(BinaryTree<Element> t, Position<Element> v, int sheetID){

		Element elem = (Element)v.element();
		elem.getEdu().setSheetID(sheetID);

		if(t.hasLeft(v)){
			addSheetID(t, t.left(v), sheetID);
		}
		if(t.hasRight(v)){
			addSheetID(t, t.right(v), sheetID);
		}

	}

	public void addEduID(BinaryTree<Element> t, Position<Element> v, List<EDU> edus){

		for(EDU edu : edus){
			if(t.isExternal(v)){
				Element elem = (Element)v.element();
				String elemContent = elem.getEdu().getContent();
				if(edu.getComment().contains(elemContent)){
					elem.getEdu().setEduID(edu.getEduID());
					break;
				}
			}
		}

		if(t.hasLeft(v)){
			addEduID(t, t.left(v), edus);
		}
		if(t.hasRight(v)){
			addEduID(t, t.right(v), edus);
		}
	}

	public void addActualClassLables(
			BinaryTree<Element> t, Position<Element> v, Comment comment) throws Exception{

		//visit, initialize actual classLabel
		for(com.cheo.model.EDU edu : comment.getEdus()){
			if(t.isExternal(v)){
				Element elem = (Element)v.element();
				if(edu.getSheetID() == elem.getEdu().getSheetID() &&
						edu.getCommentID() == elem.getEdu().getCommentID() &&
						edu.getEduID() == elem.getEdu().getEduID()){
					elem.getEdu().setActualClassLabel(edu.getClassLabel());
				}
			}
		}

		if(t.hasLeft(v)){
			addActualClassLables(t, t.left(v), comment);
		}
		if(t.hasRight(v)){
			addActualClassLables(t, t.right(v), comment);
		}
	}

	public void traversePreorder(BinaryTree<Element> t, Position<Element> v, String type){
		switch(type){
		case "element":
			if(t.isInternal(v)){
				Element internalElem = (Element)v.element();
				if(internalElem.getRstRelation() != null){
					relElements.add(internalElem);
				}
			}
			break;
		case "rstRelation":
			if(t.isInternal(v)){
				Element internalElem = (Element)v.element();
				if(internalElem.getRstRelation() != null){
					RelationWrapper rel = new RelationWrapper();
					rel.setRelation(internalElem);
					for(Position<Element> w : t.children(v)){
						if(t.isExternal(w)){
							rel.getChildren().add(w.element());	
						}
					}
					relationWrappers.add(rel);
				}
			}
			break;
		}

		if(t.hasLeft(v)){
			traversePreorder(t, t.left(v), type);
		}
		if(t.hasRight(v)){
			traversePreorder(t, t.right(v), type);
		}
	}
	
	public void addRelations(BinaryTree<Element> t, Position<Element> v, RSTRelation relation){

		if(t.isInternal(v)){
			Element internalElem = (Element)v.element();
			if(internalElem.getRstRelation() != null &&
					internalElem.getRstRelation().equals(relation)){
				RelationWrapper rel = new RelationWrapper();
				rel.setRelation(internalElem);
				for(Position<Element> w : t.children(v)){
					if(t.isExternal(w)){
						rel.getChildren().add(w.element());	
					}
				}
				relationWrappers.add(rel);
			}
		}
		if(t.hasLeft(v)){
			addRelations(t, t.left(v), relation);
		}
		if(t.hasRight(v)){
			addRelations(t, t.right(v), relation);
		}

	}


	private String getCompositeKey(int sheetID, int commentID, int eduID){
		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		sb.append(sheetID);
		sb.append("-");
		sb.append(commentID);
		sb.append("-");
		sb.append(eduID);
		return	sb.toString(); 
	}



	public List<RelationWrapper> getRelations() {
		return relationWrappers;
	}

	public List<Element> getRelationElements() {
		return relElements;
	}

}
