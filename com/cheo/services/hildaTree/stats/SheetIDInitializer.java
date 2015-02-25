package com.cheo.services.hildaTree.stats;

import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.LinkedBinaryTree;

public class SheetIDInitializer implements ITreeInitializer {

	@Override
	public void initialize(LinkedBinaryTree<Element> tree, int sheetID,
			int commentID) throws Exception {
		Traverse traverse = new Traverse();
		traverse.addSheetID(tree, tree.root(), sheetID);
	}

}
