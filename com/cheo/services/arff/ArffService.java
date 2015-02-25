package com.cheo.services.arff;


import weka.core.Instances;

import com.cheo.model.Comment;
import com.google.common.collect.TreeBasedTable;


public interface ArffService {
	 
	Instances createArff(TreeBasedTable<Integer, Integer, Comment>  comment) throws Exception;
	
}
