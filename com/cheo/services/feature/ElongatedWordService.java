package com.cheo.services.feature;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.cheo.base.TextUnitWrapper;


public class ElongatedWordService implements ICommentLevelFeatureService{

	private final static String REGEX_FIND_ELONGATED = "\\b[A-Za-z]*([A-Za-z])\\1{2,}[A-Za-z]*\\b";
	
	private final static String RREMOVE_ELONGATED = "([A-Za-z])\\1{2,}";

	@Override
	public void init() {}

	@Override
	public void updateStatistics(TextUnitWrapper textUnitWrapper) {

		String toProcess = textUnitWrapper.getTextUnit();
		int numElongWords = getElongatedTokens(toProcess).size();
		textUnitWrapper.getStatistics().setNumElongatedWords(numElongWords);
		
	}
	
	public  String cleanElongatedWords(String comment) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		for(String token : getElongatedTokens(comment)){
			String reptRemoved = removeRepetition(token);
			map.put(token, reptRemoved);
		}
		Set<String> keysSet = map.keySet();
		Collection<String> valuesColl = map.values();
		String[] keys = keysSet.toArray(new String[keysSet.size()]);
		String[] values = valuesColl.toArray(new String[valuesColl.size()]);
		String cleaned = StringUtils.replaceEach(comment,keys, values);
		return cleaned;
	}

	public List<String> getElongatedTokens(String comment){
		List<String> matched = new LinkedList<String>();
		Pattern p = Pattern.compile(REGEX_FIND_ELONGATED);
		Matcher m = p.matcher(comment);
		while(m.find()){
			matched.add(m.group());
		}
		return  matched;
	}

	public  String removeRepetition(String token){
		return token.replaceAll( RREMOVE_ELONGATED, "$1" );
	}

}
