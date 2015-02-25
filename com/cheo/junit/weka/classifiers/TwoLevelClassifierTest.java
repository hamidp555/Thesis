package com.cheo.junit.weka.classifiers;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.weka.services.WekaClassifierService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class TwoLevelClassifierTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test() throws Exception{
		
		WekaClassifierService classifierService = 
				(WekaClassifierService)applicationContext.getBean("weka.classifier");

		//If the instances are converted to wordVectors (filtered by stringToWordVector) 
		//true as the second argument
		classifierService.classifyCommentTwoLevelSecondClassifier(false);
		
	}
}
