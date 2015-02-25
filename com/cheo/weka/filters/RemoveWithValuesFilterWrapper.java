package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemoveWithValues;

public class RemoveWithValuesFilterWrapper{

	public static Instances apply(Instances data, String AttrIndexToremove, String attrLabelToRemove) throws Exception {
		RemoveWithValues filter = build(AttrIndexToremove, attrLabelToRemove);
		filter.setInputFormat(data);
		Instances filteredData = Filter.useFilter(data, filter);
		return ClassAssignerWrapper.apply(filteredData, "first");
	}

	public static RemoveWithValues build(String AttrIndexToremove, String attrLabelToRemove) throws Exception {

		RemoveWithValues removeFilter = new RemoveWithValues();
		removeFilter.setDontFilterAfterFirstBatch(false);
		removeFilter.setInvertSelection(false);
		removeFilter.setMatchMissingValues(false);
		removeFilter.setModifyHeader(false);
		removeFilter.setAttributeIndex(AttrIndexToremove);
		removeFilter.setNominalIndices(attrLabelToRemove); // to remove irrelevant  comments - 1  is for relevant  comments

		removeFilter.setSplitPoint(0.0);
		
		return removeFilter;
	}

}
