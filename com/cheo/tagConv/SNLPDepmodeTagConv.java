package com.cheo.tagConv;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Multimap;

public class SNLPDepmodeTagConv
	extends AbstractTagConfigReader implements InitializingBean{

	private Multimap<String, String> posTagMap;

	public Multimap<String, String> getPosTagMap() {
		return posTagMap;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		posTagMap = readConfig("depmode","snlp");
	}

}
