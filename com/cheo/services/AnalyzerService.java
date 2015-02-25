package com.cheo.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.InitializingBean;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;
import com.cheo.services.feature.ICommentLevelFeatureService;
import com.cheo.services.feature.ITokenLevelFeatureService;
import com.cheo.services.feature.IFeatureService;

public class AnalyzerService implements InitializingBean{

	//the token level and comment level services are divided by the super class they extend
	private List<ICommentLevelFeatureService> commentLevelServices = new LinkedList<ICommentLevelFeatureService>();
	private List<ITokenLevelFeatureService> tokenLevelServices = new LinkedList<ITokenLevelFeatureService>();
	private List<IFeatureService> serviceList;
	
	private ServiceRegistery serviceRegistery;
	
	public void setServiceRegistery(ServiceRegistery serviceRegistery) {
		this.serviceRegistery = serviceRegistery;
	}

	public void setServiceList(List<IFeatureService> serviceList) {
		this.serviceList = serviceList;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (IFeatureService service : serviceList) {
			if(ICommentLevelFeatureService.class.isAssignableFrom(service.getClass())){
				commentLevelServices.add((ICommentLevelFeatureService)service);
			}else if(ITokenLevelFeatureService.class.isAssignableFrom(service.getClass())){
				tokenLevelServices.add((ITokenLevelFeatureService)service);
			}
		}
	}

	public TextUnitWrapper analyze(String text) throws Exception{
		TextUnitWrapper textUnit = serviceRegistery.getPreprocessorService().preprocess(text);
		notifyCommentLevelServices(textUnit);
		
		Map<Integer, List<TokenWrapper>> posMap = textUnit.getPosMap();
		textUnit.getStatistics().setTotalNumTerms(getTotalNumTerms(posMap));
		for(Entry<Integer, List<TokenWrapper>> entry : posMap.entrySet()){
			List<TokenWrapper> tokens = entry.getValue();
			for(TokenWrapper token : tokens){
				notifyTokenLevelServices(textUnit, token);	
			}
		}
		return textUnit;
	}
	
	private int getTotalNumTerms(Map<Integer, List<TokenWrapper>> posMap){
		int count = 0;
		for(Entry<Integer, List<TokenWrapper>>  entry : posMap.entrySet()){
			count += entry.getValue().size();
		}
		return count;
	}

	private  void notifyCommentLevelServices(TextUnitWrapper txw) {
		for (ICommentLevelFeatureService service : commentLevelServices) {
			service.updateStatistics(txw);
		}
	}

	private  void notifyTokenLevelServices(TextUnitWrapper textUnit, TokenWrapper token) {
		for (ITokenLevelFeatureService service : tokenLevelServices) {
			service.updateStatistics(textUnit, token);
		}
	}

}
