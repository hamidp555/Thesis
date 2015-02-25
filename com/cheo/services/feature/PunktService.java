package com.cheo.services.feature;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.RegexUtils;
import com.cheo.base.TokenWrapper;


public class PunktService implements  ITokenLevelFeatureService{

	@Override
	public void updateStatistics(TextUnitWrapper comment, TokenWrapper tokenWrapper) {

		String token = tokenWrapper.getToken();
		if(edu.stanford.nlp.util.StringUtils.isPunct(token) && 
				RegexUtils.isPunchWithSentiment(token)){
			comment.getStatistics().incrementNumPunctuation();
		}

	}

	@Override
	public void init() {}

}
