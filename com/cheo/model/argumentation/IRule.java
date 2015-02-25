package com.cheo.model.argumentation;

import com.cheo.model.Argument;

public interface IRule {
	
	boolean isMatch();
	
	Argument execute() throws Exception;

}
