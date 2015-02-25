package com.cheo.services.featureSelection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.rosuda.JRI.Rengine;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import com.cheo.base.Utils;
import com.cheo.base.enums.ClassLabel;
import com.cheo.services.arff.ArffConfig;
import com.cheo.services.arff.ArffConfigReader;
import com.cheo.weka.filters.WekaUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.lang3.StringUtils;

public class BiNormalSeperation {

	private final static String MAJORITY_CLASS = "majority";
	
	private Rengine re;
	
	private ArffConfigReader configReader;

	public void setArffConfigReader(ArffConfigReader configReader) {
		this.configReader = configReader;
	}

	public void init(){
		re=new Rengine (new String [] {"--vanilla"}, false, null);
		if (!re.waitForR()){
			System.out.println ("Cannot load R");
			return;
		}
	}

	public void performFeatureSelectionAndSave(String arffLoc, String featureSelectionReslultLoc)  throws Exception{
		Map<String, Map<String, Double>> bnsMaps = getBNSMap(arffLoc, getMinorityClassName());
		FileWriter writer = new FileWriter(featureSelectionReslultLoc, true);
		BufferedWriter output = new BufferedWriter(writer);
		boolean first = true;
		for(Entry<String, Map<String, Double>> entry : bnsMaps.entrySet()){
			if(!first)
				output.newLine();	
			output.append("##"+ entry.getKey() + "##");
			output.newLine();
			for(Entry<String, Double> featurePair : entry.getValue().entrySet()){
				output.append(featurePair.getKey() + "  " + featurePair.getValue());
				output.newLine();
			}
			first=false;
		}
		output.close();
		writer.close();
	}
	
	private String getMinorityClassName() throws Exception{
		ArffConfig config =  configReader.read();
		List<ClassLabel> classLabelEnums = config.getFeatureConfig().getClassLabels();
		
		//Make sure we have two class of which one is majority and the other one is either
		//positive or negative or mix
		assert(classLabelEnums.size() == 2);
		assert(classLabelEnums.contains(ClassLabel.MAJORITY));
		assert(classLabelEnums.contains(ClassLabel.POSITIVE) || 
				classLabelEnums.contains(ClassLabel.NEGATIVE) ||
				classLabelEnums.contains(ClassLabel.MIX));
		
		String minorityClassName = StringUtils.EMPTY;
		for(ClassLabel classLabelEnum: classLabelEnums){
			String className = classLabelEnum.getValue();
			if(!className.equalsIgnoreCase(ClassLabel.MAJORITY.getValue()))
				minorityClassName = className;
		}
		
		if (!minorityClassName.isEmpty()){
			return minorityClassName;
		}else{
			throw new Exception("Class name cannot be empty, please provide a class name!");
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, Double>> getBNSMap(String arffLoc, String minorityClass) throws Exception{

		Map<String, Double> bnsMap = new HashMap<String, Double>();

		Multimap<String, List<String>> commentsMultiMap = ArrayListMultimap.create();
		Bag termsBag = new HashBag();

		Instances dataset = WekaUtils.getInstances(arffLoc);

		int numInstances = dataset.numInstances();
		for(int indx=0; indx<numInstances; indx++){

			Instance instance = dataset.instance(indx);
			String classValue = getClassValue(instance);
			List<String> commentTerms = getCommentTerms(instance);

			String key = 
					classValue.equalsIgnoreCase(minorityClass) ? minorityClass : MAJORITY_CLASS;

			commentsMultiMap.put(key, commentTerms);

			termsBag.addAll(commentTerms);

		}

		Collection<List<String>> CommentsInMajorityClass = commentsMultiMap.get(MAJORITY_CLASS);
		Collection<List<String>> commentsInMinorityClass = commentsMultiMap.get(minorityClass);

		int majorityClassCount = commentsMultiMap.get(MAJORITY_CLASS).size();
		int minorityClassCount = commentsMultiMap.get(minorityClass).size();

		//Remove terms with frequency less than 3
		for(Iterator<String> itr = termsBag.iterator(); itr.hasNext();){
			String term = itr.next();
			if(termsBag.getCount(term) < 3){
				itr.remove();
			}
		}

		for (Iterator<String> itr = termsBag.uniqueSet().iterator(); itr.hasNext();){
			String term = itr.next();
			int tp = getCount(commentsInMinorityClass, term);
			int fp = getCount(CommentsInMajorityClass, term);

			float truePositiveRate = (float) tp/minorityClassCount;
			float falsePositiveRate = (float) fp/majorityClassCount;
			float fpr = falsePositiveRate == 0.0f ? 0.0005f : falsePositiveRate;
			float tpr = truePositiveRate == 0.0f ? 0.0005f : truePositiveRate;


			double rv = re.eval (String.format("rv <- qnorm(%f) - qnorm(%f)", tpr, fpr)).asDouble ();

			bnsMap.put(term, rv);
		}

		Map<String, Double> sorted = Utils.sortByValue(bnsMap);

		Map<String, Double> majorityClassFeatures = majorityClassFeatures(sorted);

		Map<String, Double> minorityClassFeature = minorityClassFeature(sorted);

		Map<String, Map<String, Double>> result = 
				new LinkedHashMap<String, Map<String, Double>>();

		result.put("majority", majorityClassFeatures);
		result.put("minority", minorityClassFeature);

		return result;
	}

	private int getCount(Collection<List<String>> comments, String term){
		int count = 0;
		for(List<String> comment : comments){
			if(comment.contains(term))
				count++;
		}
		return count;
	}

	private List<String> getCommentTerms(Instance instance){
		Attribute commentAttr = instance.attribute(1);
		String comment = instance.stringValue(commentAttr);

		List<String> commentTerms = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(comment);
		while (st.hasMoreTokens()) {
			commentTerms.add(st.nextToken());
		}
		return commentTerms;
	}

	private String getClassValue(Instance instance){
		Attribute classAttr = instance.classAttribute();
		String classValue = classAttr.value((int)instance.classValue());
		return classValue;
	}

	@SuppressWarnings("unused")
	private List<String> faltten(Collection<List<String>> collection){
		List<String> result = new LinkedList<String>();
		for(List<String> list: collection){
			result.addAll(list);
		}
		return result;
	}

	private Map<String, Double> majorityClassFeatures(Map<String, Double> featureEntries){
		Map<String, Double> majorityClassFeatures = new LinkedHashMap<>();

		for(Entry<String, Double> entry: featureEntries.entrySet()){
			if(entry.getValue() > 0d){
				majorityClassFeatures.put(entry.getKey(), entry.getValue());
			}
		}
		return majorityClassFeatures;
	}

	private Map<String, Double> minorityClassFeature(Map<String, Double>featureEntries){
		Map<String, Double> majorityClassFeatures = new LinkedHashMap<>();

		for(Entry<String, Double> entry: featureEntries.entrySet()){
			if(entry.getValue() < 0d){
				majorityClassFeatures.put(entry.getKey(), entry.getValue());
			}
		}
		return majorityClassFeatures;
	}

}
