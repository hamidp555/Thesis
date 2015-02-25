package com.cheo.preprocessing;

import com.cheo.base.annotations.Comment;
import com.cheo.base.annotations.EDU;
import com.cheo.base.annotations.Order;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.InitializingBean;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.RegexUtils;
import com.cheo.base.TokenWrapper;
import com.cheo.services.StopWordService;

@Order(order = 6)
@Comment
@EDU
public class RemoveStrategy implements PreprocessStrategy, InitializingBean {

	private StopWordService stopWordsService;
	
	private List<String> stopWords;
	
	public void setStopWordsService(StopWordService stopWordsService) {
		this.stopWordsService = stopWordsService;
	}
	
	@Override
	public void apply(TextUnitWrapper textUnitWrapper) throws Exception{
		Map<Integer, List<TokenWrapper>>  posMap = textUnitWrapper.getPosMap();
		Iterator<Entry<Integer, List<TokenWrapper>>> entryIter = posMap.entrySet().iterator();

		while(entryIter.hasNext()){
			Entry<Integer, List<TokenWrapper>> entry = entryIter.next();
			 List<TokenWrapper> tokenList = entry.getValue();
			Iterator<TokenWrapper> tokenIter = tokenList.iterator();

			while(tokenIter.hasNext()){
				
				TokenWrapper tokenWrapper = tokenIter.next();
				String token = tokenWrapper.getToken();
				
				boolean isURL = RegexUtils.isURL(token);
				boolean isASCII = org.apache.commons.lang3.StringUtils.isAsciiPrintable(token);
				boolean isAlpha = edu.stanford.nlp.util.StringUtils.isAlpha(token);
				boolean isGoodPunkt = edu.stanford.nlp.util.StringUtils.isPunct(token) && RegexUtils.isPunchWithSentiment(token);
				
				if(!isURL && 
						isASCII && 
						(isAlpha || isGoodPunkt) &&
						!stopWords.contains(token)){
					;//do nothing
				}else{
					tokenIter.remove();
				}
				
			}

		}

	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		stopWords = stopWordsService.getStopWords(); 
		
	}


}
