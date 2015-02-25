package com.cheo.services.hildaTree.stats;

import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.LinkedBinaryTree;

public interface ITreeInitializer {
	
	void initialize(LinkedBinaryTree<Element> tree, int sheetID, int commentID) throws Exception;

}
