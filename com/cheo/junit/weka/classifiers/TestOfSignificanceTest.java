package com.cheo.junit.weka.classifiers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.CoreMatchers.is;
import com.cheo.weka.classifiers.prediction.TestOfSignificance;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class TestOfSignificanceTest { 

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test() throws Exception{
		
		TestOfSignificance testOfSignificance = 
				(TestOfSignificance)applicationContext.getBean("stat.test.significance");

		//boolean result = testOfSignificance.isDifferenceInAccuracySignificantTwoLevelVsTwoLevel("T-Test/TwoLevelA/*.txt", "T-Test/TwoLevelB/*.txt");
		boolean result = testOfSignificance.
				isDifferenceSignificantForFlatVsTwoLevel("T-Test/FlatA/flatClassifier.txt", "T-Test/TwoLevelA/*.txt");
		collector.checkThat(result, is(true));
	}
}
