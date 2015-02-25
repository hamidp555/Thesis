package com.cheo.services.excel;


import org.springframework.core.io.Resource;

import com.cheo.model.Comment;
import com.google.common.collect.TreeBasedTable;

public interface ExcelService {
	
	TreeBasedTable<Integer, Integer, Comment> read(int sheetNum, TableType type, Resource input);
	
	/**
	 * 
	 * @param annotator - 'first' or 'second' annotator
	 * @return List of all comments
	 */
	public TreeBasedTable<Integer, Integer, Comment> readAllComments(String annotator);
	
	/**
	 * 
	 * @param fileName
	 * @return List : comments that are not duplicate and has one of positive/negative/mix/irrelevant class labels
	 */
	public TreeBasedTable<Integer, Integer, Comment> readAllCommentsFiltered(String annotator);
	
}
