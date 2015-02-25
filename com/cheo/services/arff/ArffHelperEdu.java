package com.cheo.services.arff;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.cheo.base.enums.ClassLabel;
import com.cheo.model.EDU;

public class ArffHelperEdu extends AbstractArffHelper implements
InitializingBean {
	private ArffConfigReader reader;

	public void setReader(ArffConfigReader reader) {
		this.reader = reader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		config = reader.read();
	}

	public String getClazz(EDU edu) throws Exception{

		String classLabel = StringUtils.EMPTY;
		if(hasRelIrrelClasses()){
			classLabel = edu.isRelevant() ? 
					ClassLabel.RELEVANT.getValue() : 
						ClassLabel.IRRELEVANT.getValue();
		}else{
			if(edu.getClassLabel().equals(ClassLabel.MIX)){
				classLabel =  ClassLabel.NEUTRAL.getValue();
			}else{
				classLabel =  edu.getClassLabel().getValue();
			}
		}
		return classLabel;
	}

}
