package com.cheo.services.feature;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.RegexUtils;
import com.cheo.base.TokenWrapper;

public class SlangService implements  ITokenLevelFeatureService{

	
	@Override
	public void updateStatistics(TextUnitWrapper comment, TokenWrapper token){
		if(RegexUtils.isPositiveAbriviatedExpression(token.getToken()))
			comment.getStatistics().incrementNumPositiveSLANG();
		if(RegexUtils.isNegativeAbriviatedExpression(token.getToken()))
			comment.getStatistics().incrementNumNegativeSLANG();
	}

	@Override
	public void init() {}

}
