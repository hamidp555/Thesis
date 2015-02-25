package com.cheo.preprocessing;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.beans.factory.InitializingBean;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;
import com.cheo.base.annotations.Comment;
import com.cheo.base.annotations.EDU;
import com.cheo.base.annotations.Order;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

@Order(order = 4)
@Comment
@EDU
public class POSTagStrategy implements InitializingBean, PreprocessStrategy{

	private StanfordCoreNLP pipeline;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		Properties props;
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		pipeline = new StanfordCoreNLP(props);
	}
	
	@Override
	public void apply(TextUnitWrapper textUnitWrapper) throws Exception {
		String toprocess = textUnitWrapper.getCleaned();
		Map<Integer, List<TokenWrapper>> posMap = preprocess(toprocess);
		textUnitWrapper.setPosMap(posMap);
		String cleaned = toprocess;
		textUnitWrapper.setCleaned(cleaned);
	}

	private Map<Integer, List<TokenWrapper>> preprocess(String  text) throws Exception{
		
		Map<Integer, List<TokenWrapper>> result = new TreeMap<Integer, List<TokenWrapper>>(); 
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
  
		int key = 0;
		for(CoreMap sentence: document.get(SentencesAnnotation.class)){	
			List<TokenWrapper> value = new LinkedList<TokenWrapper>();
			for (CoreLabel tokenObj: sentence.get(TokensAnnotation.class)){

				String token = tokenObj.originalText();
				String posTag = tokenObj.get(PartOfSpeechAnnotation.class);
				String lemmatizedToken = tokenObj.get(LemmaAnnotation.class);
				TokenWrapper tokenWrapper = new TokenWrapper(token, posTag, lemmatizedToken);
				value.add(tokenWrapper);
			}
			result.put(key, value);
			key++;
		}
		return result;
	}
}
