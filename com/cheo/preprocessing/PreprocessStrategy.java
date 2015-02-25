package com.cheo.preprocessing;

import com.cheo.base.TextUnitWrapper;

public interface PreprocessStrategy {
	
	public void apply(TextUnitWrapper textUnitWrapper) throws Exception;

}
