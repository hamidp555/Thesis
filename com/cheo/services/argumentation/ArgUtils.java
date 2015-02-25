package com.cheo.services.argumentation;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.cheo.model.Argument;
import com.cheo.model.EDU;

public class ArgUtils {

	private ArgUtils(){}

	public static List<EDU> flatten(List<Argument> args){

		List<EDU> edus  =new LinkedList<EDU>();
		for(Argument arg : args){
			edus.addAll(arg.getBefor());
			edus.addAll(arg.getAfter());
			edus.add(arg.getTopic().getEdu());
		}
		return edus;
	}

	public static String removeTrailingZero(double num){
		String numStr = Double.toString(num);
		Assert.notNull(numStr);
		String cleaned =  StringUtils.substringBeforeLast(numStr, ".");
		Assert.notNull(cleaned);
		return cleaned;
	}

}
