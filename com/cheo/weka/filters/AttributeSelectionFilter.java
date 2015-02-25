package com.cheo.weka.filters;

import org.apache.commons.lang3.StringUtils;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class AttributeSelectionFilter{

	public static Instances apply(Instances data) throws Exception {
		AttributeSelection filter = build();
		filter.setInputFormat(data);
		Instances filteredData = Filter.useFilter(data, filter);
		return ReorderFilterWrapper.moveClassToFirst(filteredData);
	}


	public static AttributeSelection build() throws Exception {
		AttributeSelection fsFilter = new AttributeSelection();

		InfoGainAttributeEval infoGain = new InfoGainAttributeEval();
		infoGain.setBinarizeNumericAttributes(false);
		infoGain.setMissingMerge(true);

		Ranker ranker = new Ranker();
		ranker.setNumToSelect(-1);
		ranker.setStartSet(StringUtils.EMPTY);
		ranker.setThreshold(0);
		ranker.setGenerateRanking(true);

		fsFilter.setEvaluator(infoGain);
		fsFilter.setSearch(ranker);
		
		return fsFilter;
	}

}
