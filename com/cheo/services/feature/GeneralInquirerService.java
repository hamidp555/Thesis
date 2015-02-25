package com.cheo.services.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class GeneralInquirerService implements  ITokenLevelFeatureService{

	private Multimap<String,String> positive_dic = ArrayListMultimap.create();

	private Multimap<String,String> negative_dic = ArrayListMultimap.create();

	private Resource pos_resource; 

	private Resource neg_resource;

	public void setPos_resource(Resource pos_resource) {
		this.pos_resource = pos_resource;
	}

	public void setNeg_resource(Resource neg_resource) {
		this.neg_resource = neg_resource;
	}

	@Override
	public void init(){
		try{

			//POSITIVE WORDS
			InputStream posStream = pos_resource.getInputStream();
			InputStreamReader isr = new InputStreamReader(posStream, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String strLine;
			while ((strLine = reader.readLine()) != null)   {

				if(!strLine.isEmpty()){
					strLine = StringUtils.chomp(strLine);
					strLine = StringUtils.lowerCase(strLine);
					if(StringUtils.contains(strLine, "#")){
						positive_dic.put("positive", StringUtils.substringBeforeLast(strLine, "#"));
					}else{
						positive_dic.put("positive", strLine);
					}
				}
			}
			posStream.close();
			isr.close();
			reader.close();

			//NEGATIVE WORDS
			InputStream negStream = neg_resource.getInputStream();
			isr = new InputStreamReader(negStream, "UTF-8");
			reader = new BufferedReader(isr);
			while ((strLine = reader.readLine()) != null)   {
				if(!strLine.isEmpty()){
					strLine = StringUtils.chomp(strLine);
					strLine = StringUtils.lowerCase(strLine);
					if(StringUtils.contains(strLine, "#")){
						negative_dic.put("negative", StringUtils.substringBeforeLast(strLine, "#"));
					}else{
						negative_dic.put("negative", strLine);
					}
				}
			}
			negStream.close();
			isr.close();
			reader.close();
		}
		catch(Exception e){e.printStackTrace();}	
	}

	//GENERAL INQUIRER CONTAINS LEMMAS
	@Override
	public void updateStatistics(TextUnitWrapper cw, TokenWrapper token){
		if(negative_dic.containsValue(token.getLemmatizedToken())){
			cw.getStatistics().incrementNumNegativeGI();
		}

		if(positive_dic.containsValue(token.getLemmatizedToken())){
			cw.getStatistics().incrementNumPositiveGI();
		}
		cw.getTextUnit();
	}

}
