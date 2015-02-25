package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.MultiFilter;

public class MultiFilterWrapper {

	public static Instances apply(Instances data) throws Exception{
		
		MultiFilter filter = build();
		filter.setInputFormat(data);
		Instances filteredData =  Filter.useFilter(data, filter);
		return ReorderFilterWrapper.moveClassToFirst(filteredData);
	}

	public static MultiFilter build() throws Exception{
		MultiFilter m_Filter  = new MultiFilter();
		Filter[] filters = new Filter[3];
		
		filters[0]= StringToWordVectorWrapper.build();
		filters[1]= AttributeSelectionFilter.build();

		m_Filter.setDebug(false);
		m_Filter.setFilters(filters);
		return m_Filter;
	}
	
}
