package com.cheo.services.hildaTree.stats;

import org.springframework.beans.factory.InitializingBean;

import com.cheo.model.Comment;
import com.cheo.model.repositories.CommentRepository;
import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.LinkedBinaryTree;
import com.google.common.collect.TreeBasedTable;

public class EduIDInitializer  implements ITreeInitializer,  InitializingBean{

	private CommentRepository repository;

	private TreeBasedTable<Integer, Integer, Comment> comments;

	public void setRepository(CommentRepository repository) {
		this.repository = repository;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		comments = repository.getComments("first");
	}
	
	@Override
	public void initialize(LinkedBinaryTree<Element> tree, int sheetID,
			int commentID) throws Exception {
		Comment comment = comments.get(sheetID, commentID);
		Traverse traverse = new Traverse();
		traverse.addEduID(tree, tree.root(), comment.getEdus());
	}

}
