package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;

public interface IFilter {
	
	 Instances apply(Instances trainData) throws Exception ;
	
	 Filter build() throws Exception;
}
