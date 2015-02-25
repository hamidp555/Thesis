package com.cheo.services.hildaTree.stats;

import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.LinkedBinaryTree;

public class CommentIDInitializer implements ITreeInitializer {

	@Override
	public void initialize(LinkedBinaryTree<Element> tree, int sheetID, int commentID) {
		Traverse traverse = new Traverse();
		traverse.addCommentID(tree, tree.root(),  commentID);
	}

}
