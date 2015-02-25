package com.cheo.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Utils {
	
	private Utils(){}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K,V> map) {

		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

		Collections.sort(sortedEntries, 
				new Comparator<Entry<K,V>>() {
			@Override
			public int compare(Entry<K,V> e1, Entry<K,V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		}
				);

		Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : sortedEntries)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        
		return result;
	}
	
	public static  <K, V> Map<K, V> getTopNEntries(Map<K,V> map, int treshHold){
		Map<K, V> result = new LinkedHashMap<>();
		
		Set<Entry<K, V>> entrySet = map.entrySet();
		Iterator<Entry<K, V>> iter = entrySet.iterator();
		
		int count = 0;
		while(iter.hasNext() && count<treshHold){
			Entry<K, V> entry = iter.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	

}
