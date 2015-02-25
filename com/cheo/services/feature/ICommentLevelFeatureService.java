package com.cheo.services.feature;

import com.cheo.base.TextUnitWrapper;

public interface ICommentLevelFeatureService extends IFeatureService {

	 void updateStatistics(TextUnitWrapper commentWraper);
	
}
