package com.cheo.weka.filters;

import weka.filters.unsupervised.attribute.Remove;
import weka.filters.Filter;
import weka.filters.MultiFilter;


public class FilterFactory {

	public static Filter buildMultiFilter(Filter[] filters) throws Exception{
		MultiFilter m_Filter  = new MultiFilter();
		m_Filter.setFilters(filters);
		return m_Filter;
	}

	public static Filter buildRemove(String rangeToRemove) throws Exception{
		Remove remove = new Remove();
		remove.setInvertSelection(false);
		remove.setAttributeIndices(rangeToRemove); 
		return remove;
	}

}
