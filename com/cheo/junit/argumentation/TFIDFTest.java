package com.cheo.junit.argumentation;

import java.util.List;
import java.util.Map.Entry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.model.Argument;
import com.cheo.model.EDU;
import com.cheo.services.argumentation.ArgUtils;
import com.cheo.services.argumentation.ArgumentationExtractionService;
import com.cheo.services.argumentation.TFIDFService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class TFIDFTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();
	
	@Test
	public void test() throws Exception {
		TFIDFService tfidfService = 
				(TFIDFService)applicationContext.getBean("tfidf.service");
		
		ArgumentationExtractionService argService = 
				(ArgumentationExtractionService)applicationContext.getBean("arumentation.service");	
		
		List<Argument> args = argService.processNone();
		List<EDU> edus = ArgUtils.flatten(args);
		for(Entry<String, Double> entry : tfidfService.getTFDIScores(edus).entrySet()){
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
	}

}
