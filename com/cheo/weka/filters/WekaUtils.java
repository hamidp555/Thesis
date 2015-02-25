package com.cheo.weka.filters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class WekaUtils {

	private WekaUtils(){}
	
	public static void writeToFile(List<String> content, String destination) throws IOException{
		FileWriter writer = new FileWriter(destination, true);
		BufferedWriter output = new BufferedWriter(writer);
		for(String e:content){
			output.append(e);
			output.newLine();
		}
		output.close();
	}

	public static Instances getInstances(String arffFileName) throws Exception{
		DataSource source = new DataSource(arffFileName);
		Instances data = source.getDataSet();

		//In my arff file classLabel is the first attribute
		return ClassAssignerWrapper.apply(data, "first");

	}
	
	public static Instances removeIrrelevantAttrs(Instances data) throws Exception {
		String[] options = { "-R", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14" };
		Remove remove = new Remove();                         // new instance of filter
		remove.setOptions(options);                           // set options
		remove.setInputFormat(data);                          // inform filter about dataset **AFTER** setting options
		Instances newData = Filter.useFilter(data, remove);   // apply filter
		return newData;
	}
	
	public static Instances filterByRemove(Instances input, List<Integer> indecies){
		int countRemoved=0;
		for(Integer index :indecies){
			try{
				input.delete((index-countRemoved-1));
				countRemoved++;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return input;
	}
	
}
