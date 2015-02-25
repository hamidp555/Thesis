package com.cheo.junit.feature;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Rule;
import org.junit.Test;
//import org.junit.Assert;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;
import com.cheo.services.PreprocessorServiceEDUs;
import com.cheo.services.feature.DepechemodeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class DepechemodeTest extends AbstractFeatureTest{

	@Autowired
	private ApplicationContext applicationContext;
	
	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test() throws Exception{
		
		DepechemodeService depmodeSerivce = (DepechemodeService)applicationContext.getBean("depechemode.service");
		PreprocessorServiceEDUs preprocessorServiceEDUs = (PreprocessorServiceEDUs)applicationContext.getBean("preprocessor.service");
		
		TextUnitWrapper cw = preprocessorServiceEDUs.preprocess(comment);	
		Map<Integer, List<TokenWrapper>>  posMap = cw.getPosMap();
		Iterator<Entry<Integer, List<TokenWrapper>>> entryIter = posMap.entrySet().iterator();

		while(entryIter.hasNext()){
			Entry<Integer, List<TokenWrapper>> entry = entryIter.next();
			 List<TokenWrapper> tokenWrapperList = entry.getValue();
			Iterator<TokenWrapper> tokenWrapperIter = tokenWrapperList.iterator();

			while(tokenWrapperIter.hasNext()){
				
				TokenWrapper tw = tokenWrapperIter.next();
				depmodeSerivce.updateStatistics(cw, tw);
			}
		}
		
		collector.checkThat(0, is(cw.getStatistics().getNumPositiveDepmode()));
		collector.checkThat(7, is(cw.getStatistics().getNumNegativeDepmode()));
		
	}
}
