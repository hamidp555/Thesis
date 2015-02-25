package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

public class ReorderFilterWrapper{

	public static Instances moveClassToFirst(Instances data) throws Exception {
		//Class label should be moved to the first place!!
		int lastAttrIndex = data.numAttributes();
		StringBuilder sb = new  StringBuilder();
		sb.append(lastAttrIndex);
		sb.append(",");
		sb.append("1-");
		sb.append((lastAttrIndex-1));
		
		Reorder reorder = new Reorder();
		reorder.setAttributeIndices(sb.toString());
		reorder.setInputFormat(data);
		Instances reordered = Filter.useFilter(data, reorder);
		
		return ClassAssignerWrapper.apply(reordered, "first");
	}

}
