package com.cheo.services.hildaTree.classifier;

import com.cheo.base.enums.ClassLabel;
import com.cheo.services.hildaTree.BinaryTree;
import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.Position;

public class HildaClassifier {
	
	
	@SuppressWarnings("unused")
	private ClassLabel classify(BinaryTree<Element> t, Position<Element> v) throws Exception{
		return traversePostorder(t, v);
	}
	
	private ClassLabel traversePostorder(BinaryTree<Element> t, Position<Element> v) throws Exception{
		
		ClassLabel leftResult = null;
		ClassLabel rightResult = null;
		if(t.hasLeft(v)){
			leftResult = traversePostorder(t, t.left(v));
		}
		if(t.hasRight(v)){
			rightResult =  traversePostorder(t, t.right(v));
		}
		
		//apply rule
		if(t.isInternal(v)){
			Element internalElem = (Element)v.element();
			return HildaEvaluator.evaluate(internalElem.getRstRelation(), leftResult, rightResult);
		}
		
		if(t.isExternal(v)){
			Element externalElem = (Element)v.element();
			return externalElem.getEdu().getPrediction().getPredicted();
		}
		
		return null;
	}

}
