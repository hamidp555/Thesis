package com.cheo.services.argumentation;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import weka.core.tokenizers.NGramTokenizer;

public class Tokenizer {

	private final static String M_DELIMITER = " .,;:'\"()?!";
	private final static  String PRO_RESOURCE = "pro_arguments_edus.txt";
	private final static  String CON_RESOURCE = "con_arguments_edus.txt";

	public void tokenize(String type) throws Exception{

		Map<String, List<String>> result  = getResources();
		String proStr = StringUtils.join(result.get("pro"), " ");
		String conStr = StringUtils.join(result.get("con"), " ");

		NGramTokenizer nGramTokenizer = new NGramTokenizer();
		nGramTokenizer.setNGramMinSize(2);
		nGramTokenizer.setNGramMaxSize(2);
		nGramTokenizer.setDelimiters(M_DELIMITER);

		switch(type){
		case "con":
			nGramTokenizer.tokenize(conStr);
			break;
		case "pro":
			nGramTokenizer.tokenize(proStr);
			break;
		case "both":
			nGramTokenizer.tokenize(conStr);
			nGramTokenizer.tokenize(proStr);
			break;
		default:
			throw new Exception("Type is not supported!");
		}
		
		//Count the frequency of each n_grams
		Map<String, Integer> map = new HashMap<String, Integer>();
		ListMultimap<String, String> multimap = ArrayListMultimap.create();
		while (nGramTokenizer.hasMoreElements()){
			Object ngram = nGramTokenizer.nextElement();
			multimap.put(ngram.toString(), ngram.toString());
		}
		for(Entry<String, Collection<String>> entry : multimap.asMap().entrySet()){
			map.put(entry.getKey(), entry.getValue().size());
		}

		//Sort Map
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(map.entrySet());
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>(){
			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}} );

		Map<String, Integer> sortedResult = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list){
			sortedResult.put( entry.getKey(), entry.getValue() );
		}

		//Print Result
		for(Entry<String, Integer> entry : sortedResult.entrySet()){
			System.out.println(entry.getKey() + "  feq: " + entry.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, List<String>> getResources() throws Exception{
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		List<String> pro = new LinkedList<String>();
		List<String> con = new LinkedList<String>();

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.txt");
		for(Resource resource : resources){
			if(PRO_RESOURCE.equalsIgnoreCase(resource.getFilename())){
				pro = FileUtils.readLines(resource.getFile(), "utf-8");
			}

			if(CON_RESOURCE.equalsIgnoreCase(resource.getFilename())){
				con = FileUtils.readLines(resource.getFile(), "utf-8");
			}
		}
		result.put("pro", pro);
		result.put("con", con);
		return result;
	}
}
