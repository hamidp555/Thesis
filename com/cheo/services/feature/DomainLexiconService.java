package com.cheo.services.feature;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;

public class DomainLexiconService implements  ITokenLevelFeatureService{

	private Resource resource; 

	private List<String> domainWords = new ArrayList<String>();

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void init() {
		try{
			InputStreamReader isr = new InputStreamReader(resource.getInputStream(), "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String strLine;
			while ((strLine = reader.readLine()) != null)   {
				if(!strLine.isEmpty()){
					StringTokenizer stk = new StringTokenizer(strLine, ",");
					while(stk.hasMoreTokens()){
						String token = stk.nextToken();
						token = StringUtils.remove(token.trim(), "'");
						domainWords.add(token);

					}
				}
			}

			isr.close();
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void updateStatistics(TextUnitWrapper comment, TokenWrapper token) {
		if(domainWords.contains(token.getLemmatizedToken())){
			comment.getStatistics().incrementNumDSLWords();
		}
	}
}
