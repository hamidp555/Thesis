package com.cheo.junit.arff;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.services.arff.ArffConfig;
import com.cheo.services.arff.ArffConfigReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class ArffConfigReaderTest {


	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();
	
	@Test
	public void test() throws Exception{
		ArffConfigReader service = 
				(ArffConfigReader)applicationContext.getBean("config.reader");	
		ArffConfig config = service.read();
		
		collector.checkThat(config.getFeatureFileName(), is("edu-posNegMixNeutIrrel.xml"));
		collector.checkThat(config.includeExtraFeatures(), is(true));
		collector.checkThat(config.getLevel(), is("edu"));
		
	}

}
