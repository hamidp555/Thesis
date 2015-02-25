package com.cheo.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cheo.base.TokenWrapper;
import com.cheo.base.exceptions.NoWordnetLemmaException;
import com.cheo.base.exceptions.UnknownPosTagMappingException;
import com.cheo.tagConv.SNLPWordnetTagConv;
import com.google.common.collect.Multimap;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class WordNetService {

	private final static String DIR_WORDNET = "/usr/local/WordNet-3.0/dict";
	private IRAMDictionary dict;
	private Multimap<String, String> posTagMap;
	private SNLPWordnetTagConv tagReader; 

	public void setTagReader(SNLPWordnetTagConv tagReader) {
		this.tagReader = tagReader;
	}

	public void init() {
		loadDictionary();
		posTagMap = tagReader.getPosTagMap();	
	}

	private void loadDictionary(){
		try {
			URL url = new URL("file", null, DIR_WORDNET);
			dict = new RAMDictionary(url, ILoadPolicy.NO_LOAD);
			dict.open();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLemma(TokenWrapper token){
		POS pos = POS.valueOf(getWordnetPosTag(token.getPosTag()));
		IIndexWord idxWord = dict.getIndexWord(token.getToken(), pos); 
		if (idxWord == null) 
			throw new NoWordnetLemmaException("No Wordnet lemma forund.");

		IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning 
		IWord word = dict.getWord(wordID);
		return word.getLemma().toLowerCase();

	}

	public String closestEnglishWord(TokenWrapper token){
		List<String> matched = new ArrayList<String>();
		List<String> synsets = wordsListByPosTag(token.getPosTag());
		for(String word: synsets){
			int editDistance = edu.stanford.nlp.util.StringUtils.editDistance(token.getToken(), word);
			if(editDistance<2)
				matched.add(word);
		}
		return matched.isEmpty() ? token.getToken(): matched.get(0);
	}
	
	private String getWordnetPosTag(String tag){
		List<String> candidateWodnetTags = new ArrayList<String>(posTagMap.get(tag));
		if(candidateWodnetTags.isEmpty() || 
				StringUtils.isBlank(candidateWodnetTags.get(0))) 
			throw new UnknownPosTagMappingException("No pos maaping found from snlp to wordnet.");

		return candidateWodnetTags.get(0);
	}

	private List<String> wordsListByPosTag(String posTag){
		POS wordnetTag = POS.valueOf(getWordnetPosTag(posTag));
		List<String> wordsList = new ArrayList<String>();
		Iterator<ISynset> itr = dict.getSynsetIterator(wordnetTag);
		while(itr.hasNext()){
			ISynset synset = itr.next();
			for(IWord iword: synset.getWords()){
				wordsList.add(iword.getLemma());
			}
			
		}
		return wordsList;
	}

}
