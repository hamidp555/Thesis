package com.cheo.services.feature;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;

public interface ITokenLevelFeatureService extends IFeatureService {

	void updateStatistics(TextUnitWrapper comment, TokenWrapper token);

}
