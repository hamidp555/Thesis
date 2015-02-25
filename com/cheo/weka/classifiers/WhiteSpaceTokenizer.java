package com.cheo.weka.classifiers;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class WhiteSpaceTokenizer {
	
	public static List<String> tokenize(String line){
		String[] all = line.split(" ");
		List<String> tokens = new LinkedList<String>();
		for(int i=0; i<all.length ; i++){
			if(!StringUtils.isBlank(all[i])){
				tokens.add(StringUtils.trim(all[i]));
			}
		}
		return tokens;
	}

}
