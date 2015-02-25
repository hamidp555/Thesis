package com.cheo.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

public class StopWordService {

	private Resource resource;

	private List<String> stopWordsList = new ArrayList<String>();

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void init() throws Exception{
		InputStream fstream = resource.getInputStream();
		InputStreamReader isr = new InputStreamReader(fstream, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String strLine;
		while ((strLine = reader.readLine()) != null){
			strLine = StringUtils.chomp(strLine);
			strLine = StringUtils.lowerCase(strLine);
			if(!StringUtils.isBlank(strLine)){
				stopWordsList.add(strLine.trim());
			}
		}
		reader.close();
		isr.close();
		fstream.close();

	}

	public List<String> getStopWords(){
		return stopWordsList;
	}

}
