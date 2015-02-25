package com.cheo.base;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.InitializingBean;

import com.cheo.base.enums.ClassLabel;
import com.cheo.base.enums.FileType;
import com.cheo.services.arff.ArffConfig;
import com.cheo.services.arff.ArffConfigReader;

public class ArffLocationBuilder implements LocationBuilder, InitializingBean{

	private ArffConfigReader arffConfigReader;

	private ArffConfig arffConfig;

	private String arffFolderPath;

	public void setArffConfigReader(ArffConfigReader arffConfigReader) {
		this.arffConfigReader = arffConfigReader;
	}

	public void setArffConfig(ArffConfig arffConfig) {
		this.arffConfig = arffConfig;
	}

	public void setArffFolderPath(String arffFolderPath) {
		this.arffFolderPath = arffFolderPath;
	}

	@Override
	public String buildPath(FileType type) throws Exception{

		List<ClassLabel> classLabelEnums = arffConfig.getFeatureConfig().getClassLabels();

		StringBuilder sb = new StringBuilder();

		switch(type){
		case ARFF:
			sb.append(arffFolderPath);
			sb.append(File.separator );
			break;
		case TXT:
			sb.append(arffFolderPath);
			sb.append(File.separator );
			break;
		default:
			//do nothing
		}

		switch(type){
		case ARFF:
			sb.append(FileType.ARFF.prefix());
			break;
		case TXT:
			sb.append(FileType.TXT.prefix());
			break;
		default:
			//do nothing
		}

		boolean first = true;
		for(ClassLabel classLabelEnum: classLabelEnums){
			String classLabel = null;
			if(first){
				classLabel = classLabelEnum.getValue();
				sb.append(classLabel);
				first=false;
				continue;
			}
			classLabel = WordUtils.capitalize(classLabelEnum.getValue());
			sb.append(classLabel);		
		}

		if(arffConfig.includeExtraFeatures()){
			sb.append("_efs");
		}else{
			sb.append("_bows");
		}

		if("edu".equalsIgnoreCase(arffConfig.getLevel())){
			sb.append("_edus");
		}else if("comment".equalsIgnoreCase(arffConfig.getLevel())){
			sb.append("_comments");
		}

		if(arffConfig.isNormilized()){
			sb.append("_normalized");
		}

		switch(type){
		case ARFF:
			sb.append(FileType.ARFF.suffix());
			break;
		case TXT:
			sb.append(FileType.TXT.suffix());
		default:
			//do nothing
		}

		return sb.toString();	
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		arffConfig =  arffConfigReader.read();
	}

}
