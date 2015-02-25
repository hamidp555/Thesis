package com.cheo.services;

import com.cheo.base.TextUnitWrapper;

public interface PreprocessorService {
	
	TextUnitWrapper preprocess(String text) throws Exception;

}
