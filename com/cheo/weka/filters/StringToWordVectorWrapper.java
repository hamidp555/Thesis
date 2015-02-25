package com.cheo.weka.filters;

import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.stemmers.NullStemmer;
import weka.core.stemmers.Stemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class StringToWordVectorWrapper {
	
	private final static String M_DELIMITER = " \t\n\r\f\'\"\\!@#$%^&*()_-+={}<>,.;:|[]{}/*~`";
	private final static String ATTR_INDICIES = "first-last";
	
	public static StringToWordVector build() {
		//Define the stringToVecotr filter here
		final StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setDoNotOperateOnPerClassBasis(false);
		filter.setInvertSelection(false);
		filter.setLowerCaseTokens(false);

		SelectedTag tag = new SelectedTag(StringToWordVector.FILTER_NONE, 
				StringToWordVector.TAGS_FILTER);
		filter.setNormalizeDocLength(tag);

		filter.setOutputWordCounts(true);
		filter.setPeriodicPruning(-1.0);
		filter.setAttributeIndices(ATTR_INDICIES);
		filter.setMinTermFreq(2);
		filter.setWordsToKeep(6000000);

		WordTokenizer wt = new WordTokenizer();
		
		wt.setDelimiters(M_DELIMITER);
		filter.setTokenizer(wt);

		Stemmer stemmer = new NullStemmer();
		filter.setStemmer(stemmer);

		filter.setStopwords(null);
		return filter;
	}

	public static Instances apply(Instances data) throws Exception {
		StringToWordVector filter = build();
		filter.setInputFormat(data);
		Instances filteredData =  Filter.useFilter(data, filter);
		
		//First attribute is class label
		return ClassAssignerWrapper.apply(filteredData, "first");
	}
	
}
