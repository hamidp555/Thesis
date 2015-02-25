package com.cheo.base;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import com.cheo.base.enums.ClassLabel;
import com.cheo.services.arff.ArffConfig;
import com.cheo.services.arff.ArffConfigReader;

public class FeatureSelectionResultLocBuilder {

	private String baseFolder;
	
	private ArffConfigReader configReader;

	public void setConfigReader(ArffConfigReader configReader) {
		this.configReader = configReader;
	}

	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}

	public String buildFeatureSelectionFilePathByConfig() throws Exception{

		ArffConfig config =  configReader.read();
		List<ClassLabel> classLabelEnums = config.getFeatureConfig().getClassLabels();

		StringBuilder sb = new StringBuilder();
		sb.append(baseFolder);
		sb.append(File.separator );
		sb.append("featureSelection_");

		int index = 0;
		for(ClassLabel classLabelEnum: classLabelEnums){
			String calssLaeblValue = 
					index>0 ? WordUtils.capitalize(classLabelEnum.getValue()):classLabelEnum.getValue();
					sb.append(calssLaeblValue);
					index++;
		}

		sb.append(".txt");
		return sb.toString();	
	}

}
