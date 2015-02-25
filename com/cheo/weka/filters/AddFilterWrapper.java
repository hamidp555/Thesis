package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

public class AddFilterWrapper{

	public static Add build(String attrName, String attrIndex, String labels){
		Add add = new Add();
		add.setAttributeName(attrName);
		add.setAttributeIndex(attrIndex);
		add.setNominalLabels(labels);
		return add;
	}

	public static Instances apply(Instances data, String attrName, String attrIndex, String labels) throws Exception {
		Add add = build(attrName, attrIndex, labels);
		add.setInputFormat(data);
		Instances filteredData =  Filter.useFilter(data, add);
		return ClassAssignerWrapper.apply(filteredData, "first");
	}

}
