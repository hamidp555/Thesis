package com.cheo.model.repositories;

import java.util.Iterator;

import com.cheo.base.enums.Annotators;
import com.cheo.model.Comment;
import com.cheo.services.arff.ArffHelper;
import com.cheo.services.excel.ExcelService;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


public class CommentRepository {

	private ExcelService excelService;
	
	private ArffHelper arff;

	public void setArff(ArffHelper arff) {
		this.arff = arff;
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	private TreeBasedTable<Integer, Integer, Comment> filter(
			TreeBasedTable<Integer, Integer, Comment> comments) throws Exception{

		TreeBasedTable<Integer, Integer, Comment> copied = TreeBasedTable.create(comments);
		Iterator<Table.Cell<Integer, Integer, Comment>> iter = copied.cellSet().iterator();

		while(iter.hasNext()){
			
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();

			//IF BINARY CLASIFICATION RELEVANT / IRRELEVANT
			if(arff.hasRelIrrelClasses()){

				if(!comment.hasClassLabel()){
					iter.remove();
				}

			}else if(!arff.containsClazz(comment.getClassLabel())){
				iter.remove();
			}
		}

		return copied;
	}

	public TreeBasedTable<Integer, Integer, Comment> getComments(String annotator) throws Exception{

		TreeBasedTable<Integer, Integer, Comment> comments = TreeBasedTable.create();
		switch(annotator){
		case "first":
			comments = excelService.readAllComments(Annotators.FIRST.getValue());
			break;
		case "second":
			comments = excelService.readAllComments(Annotators.SECOND.getValue());
			break;
		default: 
			throw new IllegalArgumentException("invalid annotataor!");
		}
		
		//Add Arguments
		//Notice that some comments are filtered here, although these comments can be used for argumentation 
		//<Integer, Integer, Comment> commentsFiltered = filter(comments);
		//Iterator<Table.Cell<Integer, Integer, Comment>> cmTableIter = commentsFiltered.cellSet().iterator();
		//while(cmTableIter.hasNext()){
		//	Table.Cell<Integer, Integer, Comment> cell = cmTableIter.next();
		//	cell.getValue().addArguments();
		//}
		//return commentsFiltered;
		return comments;
	}

	public TreeBasedTable<Integer, Integer, Comment> getCommentsFiltered(String annotator) throws Exception{

		TreeBasedTable<Integer, Integer, Comment> comments = TreeBasedTable.create();
		switch(annotator){
		case "first":
			comments = excelService.readAllCommentsFiltered(Annotators.FIRST.getValue());
			break;
		case "second":
			comments = excelService.readAllCommentsFiltered(Annotators.SECOND.getValue());
			break;
		default: 
			throw new IllegalArgumentException("invalid annotataor!");
		}

		TreeBasedTable<Integer, Integer, Comment> commentsFiltered = filter(comments);
		
//		Add Arguments
//		Notice that some comments are filtered here, although these comments can be used for argumentation
//		Iterator<Table.Cell<Integer, Integer, Comment>> cmTableIter = commentsFiltered.cellSet().iterator();
//		while(cmTableIter.hasNext()){
//			Table.Cell<Integer, Integer, Comment> cell = cmTableIter.next();
//			cell.getValue().addArguments();
//		}
		return commentsFiltered;
	}
}