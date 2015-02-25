package com.cheo.weka.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.SpreadSubsample;

public class SpreadSubsampleWrapper {

	public static Instances apply(Instances data, double distributionSpread) throws Exception {
		SpreadSubsample filter = build(distributionSpread);
		filter.setInputFormat(data);
		Instances filteredData = Filter.useFilter(data, filter);
		return ClassAssignerWrapper.apply(filteredData, "first");
	}

	public static SpreadSubsample build(double distributionSpread) throws Exception {
		SpreadSubsample filter = new SpreadSubsample();
		filter.setAdjustWeights(false);
		filter.setDistributionSpread(distributionSpread);
		filter.setMaxCount(0.0);
		filter.setRandomSeed(1);
		return filter;
	}

}
