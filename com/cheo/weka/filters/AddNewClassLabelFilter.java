package com.cheo.weka.filters;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.cheo.base.enums.ClassLabel;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class AddNewClassLabelFilter {

	public static Instances applyForComment(Instances data, String AttrNameWithNewClassValues) throws Exception{

		//Remove old classLabel attribute
		String classAttrIndex = String.valueOf(data.classIndex() +1);
		Instances classLabelRemove = RemoveFilterWrapper.apply(data, classAttrIndex);

		//Add a new class label
		StringBuilder sb = new StringBuilder();
		sb.append(ClassLabel.POSITIVE.getValue());
		sb.append(",");
		sb.append(ClassLabel.NEGATIVE.getValue());
		sb.append(",");
		sb.append(ClassLabel.MIX.getValue());

		Instances newClassLabelAdded = 
				AddFilterWrapper.apply(classLabelRemove, "classLabel", "first", sb.toString()); 

		//Set classLabel values for each instance
		//Attribute index starts from 0
		int numInstances = newClassLabelAdded.numInstances();
		Attribute priorClss = newClassLabelAdded.attribute(AttrNameWithNewClassValues);
		for(int indx=0; indx<numInstances; indx++){    			
			Instance instance = newClassLabelAdded.instance(indx);
			String value = getPriorClasValue(instance, priorClss);
			newClassLabelAdded.instance(indx).setClassValue(value);
		}

		return newClassLabelAdded;
	}

	public static Instances applyForEDU(Instances data, String AttrNameWithNewClassValues) throws Exception{

		//Remove old classLabel attribute
		String classAttrIndex = String.valueOf(data.classIndex() +1);
		Instances classLabelRemove = RemoveFilterWrapper.apply(data, classAttrIndex);

		//Add a new class label
		StringBuilder sb = new StringBuilder();
		sb.append(ClassLabel.POSITIVE.getValue());
		sb.append(",");
		sb.append(ClassLabel.NEGATIVE.getValue());
		sb.append(",");
		sb.append(ClassLabel.NEUTRAL.getValue());

		Instances newClassLabelAdded = 
				AddFilterWrapper.apply(classLabelRemove, "classLabel", "first", sb.toString()); 

		//Set classLabel values for each instance
		//Attribute index starts from 0
		int numInstances = newClassLabelAdded.numInstances();
		Attribute priorClss = newClassLabelAdded.attribute(AttrNameWithNewClassValues);
		for(int indx=0; indx<numInstances; indx++){    
			Instance instance = newClassLabelAdded.instance(indx);
			String value = getPriorClasValue(instance, priorClss);
			newClassLabelAdded.instance(indx).setClassValue(value);
		}

		return newClassLabelAdded;
	}
	
	@SuppressWarnings("unchecked")
	private static  String getPriorClasValue(Instance instance, Attribute priorClss){
		
		Map<Integer, String> map = new HashMap<Integer, String>();
		Enumeration<String> enums = priorClss.enumerateValues();
		int index = 0;
		while(enums.hasMoreElements()){
			String strValue = enums.nextElement();
			map.put(index, strValue);
			index++;
		}
		Double classLabelIndex = Double.valueOf(instance.value(priorClss));
		return map.get(classLabelIndex.intValue());
	}

}
