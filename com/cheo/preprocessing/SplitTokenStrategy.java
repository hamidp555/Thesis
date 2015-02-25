package com.cheo.preprocessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.RegexUtils;
import com.cheo.base.TokenWrapper;
import com.google.common.collect.TreeBasedTable;
import com.cheo.base.annotations.Comment;
import com.cheo.base.annotations.EDU;
import com.cheo.base.annotations.Order;

@Order(order = 5)
@Comment
@EDU
public class SplitTokenStrategy implements PreprocessStrategy {

	@Override
	public void apply(TextUnitWrapper textUnitWrapper) throws Exception {
		Map<Integer, List<TokenWrapper>>  posMap = textUnitWrapper.getPosMap();
		Iterator<Entry<Integer, List<TokenWrapper>>> sentIter = posMap.entrySet().iterator();

		TreeBasedTable<Integer, Integer, List<TokenWrapper>> modif = TreeBasedTable.create();
		int sentIndex = 0;
		while(sentIter.hasNext()){
			Entry<Integer, List<TokenWrapper>> entry = sentIter.next();
			List<TokenWrapper> tokenList = entry.getValue();
			Iterator<TokenWrapper> tokenIter = tokenList.iterator();

			int tkIndex = 0;
			while(tokenIter.hasNext()){
				TokenWrapper tokenWrapper = tokenIter.next();
				String token = tokenWrapper.getToken();
				String posTag = tokenWrapper.getPosTag();
				
				if(RegexUtils.hasPunctButNotJustPunct(token)){
					List<TokenWrapper> toAdd = new  ArrayList<TokenWrapper>();
					List<String> tokens = RegexUtils.tokenizeByPunct(token);

					for(String tkn:tokens){
						TokenWrapper newTokenWrapper = new TokenWrapper(tkn, posTag, tkn);
						toAdd.add(newTokenWrapper);
					}
					modif.put(sentIndex, tkIndex, toAdd);
				}
				tkIndex++;
			}
			sentIndex++;
		}
		
		//If there is not split then do not do anything
		if (modif.isEmpty()) return;
		
		Map<Integer, List<TokenWrapper>>  posMap2 =  new TreeMap<Integer, List<TokenWrapper>>();
		//iterate again
		Iterator<Entry<Integer, List<TokenWrapper>>> sentIter2 = posMap.entrySet().iterator();
		int sentIndex2 = 0;
		while(sentIter2.hasNext()){
			Iterator<TokenWrapper> tkIter2 = sentIter2.next().getValue().iterator();
			List<TokenWrapper> tkList = new  ArrayList<TokenWrapper>();
			int tkIndex2 = 0;
			while(tkIter2.hasNext()){
				TokenWrapper tokenWrapper = tkIter2.next();
				List<TokenWrapper> toAdd = modif.get(sentIndex2, tkIndex2);
				tkList.add(tokenWrapper);
				if(toAdd != null){
					tkList.addAll(toAdd);
				}
				tkIndex2++;
			}
			posMap2.put(sentIndex2, tkList);
			sentIndex2++;
		}
		
		textUnitWrapper.setPosMap(posMap2);
	}

}
