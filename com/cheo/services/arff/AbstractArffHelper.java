package com.cheo.services.arff;

import java.util.List;

import com.cheo.base.enums.ClassLabel;

public abstract class AbstractArffHelper implements ArffHelper {

	protected ArffConfig config;

	public void setConfig(ArffConfig config) {
		this.config = config;
	}
	
	@Override
	public ArffConfig getConfig(){
		return this.config;
	}
	
	@Override
	public boolean hasRelIrrelClasses(){
		return  config.getFeatureConfig().hasRelIrrelClasses();
	}
	
	@Override
	public List<ClassLabel> getClassLabels(){
		return config.getFeatureConfig().getClassLabels();
	}
	
	@Override
	public boolean containsClazz(ClassLabel clazz){
		return config.getFeatureConfig().getClassLabels().contains(clazz);
	}
	
	@Override
	 public boolean isNormilized(){
		return config.isNormilized();
	}
	
}
