package com.cheo.services.arff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;


public class ArffUtils {

	private ArffUtils(){}

	public static void saveArff(Instances dataset, String destinationPath) {
		try{
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataset);
			saver.setFile(new File(destinationPath));
			saver.writeBatch();
		}catch(Exception e){
			e.printStackTrace();	
		}
	}


	public static void saveText(Instances dataset, String destinationPath) throws IOException {

		BufferedWriter output = null;	
		try{
			output = new BufferedWriter(new FileWriter(destinationPath, true));
			int numInstances = dataset.numInstances();
			for(int indx=0; indx<numInstances; indx++){
				output.append(dataset.instance(indx).toString());
				output.newLine();
			}
		}finally{
			if(output != null){
				output.close();
			}
		}
	}
}
