package com.cheo.services;

import com.cheo.services.arff.ArffConfig;
import com.cheo.services.arff.ArffConfigReader;
import com.cheo.services.arff.ArffService;
import com.cheo.services.arff.ArffServiceComments;
import com.cheo.services.arff.ArffServiceEuds;

public class ServiceRegistery {

	private ArffServiceComments arffComment;

	private ArffServiceEuds arffEdu;

	private ArffConfigReader configReader;
	
	private PreprocessorServiceEDUs preprocessorEDUs;
	
	private PreprocessorServiceComments preprocessorComments;
	

	public void setConfigReader(ArffConfigReader configReader) {
		this.configReader = configReader;
	}

	public void setArffComment(ArffServiceComments arffComment) {
		this.arffComment = arffComment;
	}

	public void setArffEdu(ArffServiceEuds arffEdu) {
		this.arffEdu = arffEdu;
	}

	public void setPreprocessorEDUs(PreprocessorServiceEDUs preprocessorEDUs) {
		this.preprocessorEDUs = preprocessorEDUs;
	}

	public void setPreprocessorComments(
			PreprocessorServiceComments preprocessorComments) {
		this.preprocessorComments = preprocessorComments;
	}
	
	public PreprocessorService getPreprocessorService() throws Exception{
		ArffConfig config = configReader.read();
		if(config.getLevel().equalsIgnoreCase("edu")){
			return preprocessorEDUs;
		}
		if(config.getLevel().equalsIgnoreCase("comment")){
			return preprocessorComments;
		}
		throw new Exception("Preprocessor service can not be registered");
	}

	public ArffService getArffService() throws Exception{
		ArffConfig config = configReader.read();
		if(config.getLevel().equalsIgnoreCase("edu")){
			return arffEdu;
		}
		if(config.getLevel().equalsIgnoreCase("comment")){
			return arffComment;
		}
		throw new Exception("Arff service can not be registered");
	}

}
