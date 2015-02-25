package com.cheo.services.excel;

import java.util.Iterator;
import java.util.Set;

import com.cheo.model.Comment;
import com.cheo.model.EDU;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class FilterFactory {
	
	private FilterFactory(){}

	public static TreeBasedTable<Integer, Integer, Comment> filterEdus(TreeBasedTable<Integer, Integer, Comment> input){

		Set<Table.Cell<Integer,Integer,Comment>> cells = input.cellSet();
		Iterator<Table.Cell<Integer,Integer,Comment>> tableIter = cells.iterator();

		while(tableIter.hasNext()){
			Table.Cell<Integer,Integer,Comment> cell = tableIter.next();
			Comment comment = cell.getValue();

			Iterator<EDU> eduIter  = comment.getEdus().iterator();
			while(eduIter.hasNext()){
				EDU edu = eduIter.next();
				if(!edu.isAnnotated() || !edu.hasClassLabel() || edu.isObjective() ){
					eduIter.remove();
				}
			}
		}
		return input;
	}
	
	public static TreeBasedTable<Integer, Integer, Comment> filterComments(TreeBasedTable<Integer, Integer, Comment> input){

		Set<Table.Cell<Integer,Integer,Comment>> cells = input.cellSet();
		Iterator<Table.Cell<Integer,Integer,Comment>> iter = cells.iterator();

		while(iter.hasNext()){
			Table.Cell<Integer,Integer,Comment> cell = iter.next();
			Comment comment = cell.getValue();

			if(!comment.isAnnotated() || 
					comment.isDuplicate() || 
					!comment.hasClassLabel()){
				iter.remove();
			}
		}
		return input;
	}

}
