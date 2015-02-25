package com.cheo.services.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;
import com.cheo.tagConv.SNLPSWNTagConv;
import com.google.common.collect.Multimap;

public class SentiWordNetService implements ITokenLevelFeatureService{

	private Multimap<String, String> posTagMap;

	private HashMap<String, String> _dict;
	
	private Resource resource;
	
	private SNLPSWNTagConv tagReader;

	public void setTagReader(SNLPSWNTagConv tagReader) {
		this.tagReader = tagReader;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void init(){
		posTagMap = tagReader.getPosTagMap();
		_dict = new HashMap<String, String>();
		HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();
		try{
			InputStream posStream = resource.getInputStream();
			InputStreamReader isr = new InputStreamReader(posStream, "UTF-8");
			BufferedReader csv = new BufferedReader(isr);
			String line = "";			
			while((line = csv.readLine()) != null)
			{
				String[] data = line.split("\t");
				Double score = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
				String[] words = data[4].split(" ");
				for(String w:words)
				{
					String[] w_n = w.split("#");
					w_n[0] += "#"+data[0];
					int index = Integer.parseInt(w_n[1])-1;
					if(_temp.containsKey(w_n[0]))
					{
						Vector<Double> v = _temp.get(w_n[0]);
						if(index>v.size())
							for(int i = v.size();i<index; i++)
								v.add(0.0);
						v.add(index, score);
						_temp.put(w_n[0], v);
					}
					else
					{
						Vector<Double> v = new Vector<Double>();
						for(int i = 0;i<index; i++)
							v.add(0.0);
						v.add(index, score);
						_temp.put(w_n[0], v);
					}
				}
			}
			Set<String> temp = _temp.keySet();
			for (Iterator<String> iterator = temp.iterator(); iterator.hasNext();) {
				String word = (String) iterator.next();
				Vector<Double> v = _temp.get(word);
				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for(int i = 0; i < v.size(); i++)
					score += ((double)1/(double)(i+1))*v.get(i);
				for(int i = 1; i<=v.size(); i++)
					sum += (double)1/(double)i;
				score /= sum;
				String sent = "";								
				if(score > 0 )
					sent = "positive";
				else if(score < 0)
					sent = "negative";			
				_dict.put(word, sent);
			}
			csv.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void updateStatistics(TextUnitWrapper comment, TokenWrapper token){

		String sentimentWeight = getWeigth(token.getLemmatizedToken(), token.getPosTag());

		if (!StringUtils.isEmpty(sentimentWeight) && sentimentWeight.equalsIgnoreCase("positive"))
			comment.getStatistics().incrementNumPositiveSWN();

		if(!StringUtils.isEmpty(sentimentWeight) && sentimentWeight.equalsIgnoreCase("negative"))
			comment.getStatistics().incrementNumNegativeSWN();

	}
	
	private String getWeigth(String word, String SNLPposTag)
	{
		String SWNposTag = ConvertSNLPTagToSWNTag(SNLPposTag);
		return _dict.get(word+"#"+SWNposTag);
	}

	private String ConvertSNLPTagToSWNTag(String posTag){	
		List<String> tagList = new ArrayList<String>(posTagMap.get(posTag));
		return !tagList.isEmpty() ? tagList.get(0): null; 

	}

}
