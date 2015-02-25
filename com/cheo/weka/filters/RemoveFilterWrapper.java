package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class RemoveFilterWrapper {

	public static Remove build(String index) throws Exception{
		
		Remove remove = new Remove();
		remove.setInvertSelection(false);
		remove.setAttributeIndices(index);
		return remove;
	}

	public static Instances apply(Instances data, String index) throws Exception {
		
		Remove remove = build(index);
		remove.setInputFormat(data);
		
		Instances filteredData = Filter.useFilter(data, remove);
		return ClassAssignerWrapper.apply(filteredData, "first");
	}

}
