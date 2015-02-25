package com.cheo.base;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class EDUWrapper extends TextUnitWrapper{
	
	public String getFilteredEDUFromLemmas(){
		Set<Entry<Integer, List<TokenWrapper>>> entries = getPosMap().entrySet();
		List<String> sentencesFromLemmas = new LinkedList<String>();
		for(Entry<Integer, List<TokenWrapper>> entry : entries){
			List<TokenWrapper> tokens = entry.getValue();
			List<String> lemmas = new LinkedList<String>();
			for(TokenWrapper token : tokens){
				String lemma = token.getLemmatizedToken();
				lemmas.add(lemma);
			}
			String sentenceFromLemmas = StringUtils.join(lemmas, " ");
			sentencesFromLemmas.add(sentenceFromLemmas);
		}
		String commentFromLemmas = StringUtils.join(sentencesFromLemmas, " ");
		return commentFromLemmas;
	}
	
	public String getFilteredEDUFromWords(){
		Set<Entry<Integer, List<TokenWrapper>>> entries = getPosMap().entrySet();
		List<String> sentencesFromWords = new LinkedList<String>();
		for(Entry<Integer, List<TokenWrapper>> entry : entries){
			List<TokenWrapper> tokens = entry.getValue();
			List<String> words = new LinkedList<String>();
			for(TokenWrapper token : tokens){
				String word = token.getToken();
				words.add(word);
			}
			String sentenceFromWords = StringUtils.join(words, " ");
			sentencesFromWords.add(sentenceFromWords);
		}
		String commentFromWords = StringUtils.join(sentencesFromWords, " ");
		return commentFromWords;
	}

}
