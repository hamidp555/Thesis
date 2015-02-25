package com.cheo.services.argumentation;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.InitializingBean;

import com.cheo.model.EDU;
import com.cheo.weka.filters.ClassAssignerWrapper;
import com.cheo.weka.filters.WekaUtils;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.stemmers.NullStemmer;
import weka.core.stemmers.Stemmer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class TFIDFService implements InitializingBean{

	private final static String M_DELIMITER = " \t\n\r\f\'\"\\!@#$%^&*()_-+={}<>,.;:|[]{}/*~`";
	private final static String ATTR_INDICIES = "first-last";

	private String arffPath;

	private Instances filteredDataset;

	private Instances dataset;
	
	private Map<String, Instance> tfidfMap;

	public void setArffPath(String arffPath) {
		this.arffPath = arffPath;
	}

	private void addTFIDF() throws Exception{

		SelectedTag tag = 
				new SelectedTag(StringToWordVector.FILTER_NONE, StringToWordVector.TAGS_FILTER);
		WordTokenizer wt = new WordTokenizer();
		wt.setDelimiters(M_DELIMITER);
		Stemmer stemmer = new NullStemmer();

		final StringToWordVector filter = new StringToWordVector();

		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setDoNotOperateOnPerClassBasis(false);
		filter.setInvertSelection(false);
		filter.setLowerCaseTokens(false);
		filter.setStopwords(null);
		filter.setOutputWordCounts(true);
		filter.setPeriodicPruning(-1.0);
		filter.setAttributeIndices(ATTR_INDICIES);
		filter.setMinTermFreq(2);
		filter.setWordsToKeep(10000000);
		filter.setNormalizeDocLength(tag);
		filter.setTokenizer(wt);
		filter.setStemmer(stemmer);

		filter.setInputFormat(dataset);
		filteredDataset =  Filter.useFilter(dataset, filter);
		ClassAssignerWrapper.apply(filteredDataset, "first");
	}

	private Map<String, Instance> getTFIDFMap(){

		Map<String, Instance> map = new HashMap<String, Instance>();

		Attribute attr1 = filteredDataset.attribute("sheetID");
		Attribute attr2 = filteredDataset.attribute("commentID");
		Attribute attr3 = filteredDataset.attribute("eduID");

		@SuppressWarnings("unchecked")
		Enumeration<Instance> instances = filteredDataset.enumerateInstances();

		while(instances.hasMoreElements()){
			Instance instance = instances.nextElement();

			double val1 = instance.value(attr1.index());
			double val2 = instance.value(attr2.index());
			double val3 = instance.value(attr3.index());

			StringBuilder sb = new StringBuilder();
			sb.append(ArgUtils.removeTrailingZero(val1));
			sb.append("_");
			sb.append(ArgUtils.removeTrailingZero(val2));
			sb.append("_");
			sb.append(ArgUtils.removeTrailingZero(val3));
			String key = sb.toString();

			map.put(key, instance);
		}
		
		return map;

	}

	public Map<String, Double> getTFDIScores(List<EDU> edus){

		List<Instance> instances = new LinkedList<Instance>();
		StringBuilder sb = new StringBuilder();
		for(EDU edu : edus){
			sb.append(edu.getSheetID());
			sb.append("_");
			sb.append(edu.getCommentID());
			sb.append("_");
			sb.append(edu.getEduID());
			String key = sb.toString();
			instances.add(tfidfMap.get(key));
			sb.setLength(0);
		}
		
		//[{0 irrelevant,1 1,2 8,3 202,181 4.4883,756 3.664949}]
		Map<String, Double> tfidfMap = new HashMap<String, Double>();
		for(Instance instance : instances){
			if(instance == null) continue;
			@SuppressWarnings("unchecked")
			Enumeration<Attribute> attrs = instance.enumerateAttributes();
			while(attrs.hasMoreElements()){
				Attribute attr = attrs.nextElement();
				double attrValue = instance.value(attr);
				if(!attr.isNominal() && 
						!"0".equalsIgnoreCase(ArgUtils.removeTrailingZero(attrValue)) &&
						attr.index() != 1 &&
						attr.index() != 2 &&
						attr.index() != 3){
					
					String key = attr.name();	
					Double value = Double.valueOf(instance.toString(attr.index()));//tfidf score
					
					tfidfMap.put(key, value);	
				}
			}
		}
		
		return sort(tfidfMap);
		
	}
	
	private Map<String, Double> sort(Map<String, Double> tfidfMap){

		//Descending order
		Comparator<Entry<String,Double>> comp = new Comparator<Entry<String,Double>>(){
			@Override
			public int compare(Entry<String, Double> o1,
					Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		};
		
		List<Entry<String,Double>> entries = new LinkedList<Entry<String,Double>>(tfidfMap.entrySet());
		
		Collections.sort(entries, comp);
		
		Map<String, Double> sortedTfidfMap = new LinkedHashMap<String, Double>();
		for(Entry<String,Double> entry : entries){
			sortedTfidfMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedTfidfMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		dataset = WekaUtils.getInstances(arffPath);
		addTFIDF();
		tfidfMap = getTFIDFMap();
	}
}
