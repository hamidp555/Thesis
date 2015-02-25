package com.cheo.junit.feature;


import static org.hamcrest.CoreMatchers.is;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;
import com.cheo.services.PreprocessorServiceEDUs;
import com.cheo.services.feature.PunktService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class PunktTest extends AbstractFeatureTest{
	
	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void testContainPunctation() throws Exception{
		PunktService punktStrategy = (PunktService)context.getBean("punkt.service");
		PreprocessorServiceEDUs preprocessorServiceEDUs = (PreprocessorServiceEDUs)applicationContext.getBean("preprocessor.service");
		
		TextUnitWrapper cw = preprocessorServiceEDUs.preprocess(comment);	
		Map<Integer, List<TokenWrapper>>  posMap = cw.getPosMap();
		Iterator<Entry<Integer, List<TokenWrapper>>> entryIter = posMap.entrySet().iterator();

		while(entryIter.hasNext()){
			Entry<Integer, List<TokenWrapper>> entry = entryIter.next();
			 List<TokenWrapper> tokenList = entry.getValue();
			Iterator<TokenWrapper> tokenIter = tokenList.iterator();

			while(tokenIter.hasNext()){
				TokenWrapper tokenWrapper = tokenIter.next();
				punktStrategy.updateStatistics(textUnitWrapper, tokenWrapper);
			}
		}
		collector.checkThat(10, is(textUnitWrapper.getStatistics().getNumPunctuation()));
		
	}

}
