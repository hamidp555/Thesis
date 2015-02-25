package com.cheo.base;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.cheo.base.enums.FileType;
import com.cheo.weka.services.ClassifierConfig;
import com.cheo.weka.services.ClassifierConfigReader;

public class ClassificationLocationBuilder implements LocationBuilder, InitializingBean{
	
	private ClassifierConfigReader classifierConfigReader;
	
	private String arffFolderPath;
	
	private ClassifierConfig classifierConfig;
	
	private String evaluationFolderPath;

	private String modelFolderPath;

	public void setArffFolderPath(String arffFolderPath) {
		this.arffFolderPath = arffFolderPath;
	}

	public void setClassifierConfigReader(
			ClassifierConfigReader classifierConfigReader) {
		this.classifierConfigReader = classifierConfigReader;
	}

	public void setClassifierConfig(ClassifierConfig classifierConfig) {
		this.classifierConfig = classifierConfig;
	}

	public void setEvaluationFolderPath(String evaluationFolderPath) {
		this.evaluationFolderPath = evaluationFolderPath;
	}

	public void setModelFolderPath(String modelFolderPath) {
		this.modelFolderPath = modelFolderPath;
	}
	
	@Override
	public String buildPath(FileType type) throws Exception{

		String arffFile = classifierConfig.getArffFileName();

		StringBuilder sb = new StringBuilder();
		
		switch(type){
			case ARFF:
				sb.append(arffFolderPath);
				sb.append(File.separator );
				break;
			case MODEL:
				sb.append(modelFolderPath);
				sb.append(File.separator );
				break;
			case EVAL:
				sb.append(evaluationFolderPath);
				sb.append(File.separator );
				break;
			default:
				//do nothing
		}
		
		switch(type){
			case ARFF:
				sb.append(arffFile);
				break;
			case MODEL:
				sb.append(StringUtils.substringBeforeLast(arffFile, "."));
				sb.append(FileType.MODEL.suffix());
				break;
			case EVAL:
				sb.append(StringUtils.substringBeforeLast(arffFile, "."));
				sb.append(FileType.EVAL.suffix());
				break;
			default:
				//do nothing
		}

		return sb.toString();	
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		classifierConfig = classifierConfigReader.read();
	}
	

}
