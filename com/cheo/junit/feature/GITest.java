package com.cheo.junit.feature;

import static org.hamcrest.CoreMatchers.is;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;
import com.cheo.services.PreprocessorServiceEDUs;
import com.cheo.services.feature.GeneralInquirerService;

public class GITest extends AbstractFeatureTest{


	@Autowired
	private ApplicationContext applicationContext;

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test() throws Exception{
		GeneralInquirerService giService = (GeneralInquirerService)applicationContext.getBean("generalIquirer.service");
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
				giService.updateStatistics(cw, tw);
			}
		}

		collector.checkThat(20, is(cw.getStatistics().getNumPositiveGI()));
		collector.checkThat(13, is(cw.getStatistics().getNumNegativeGI()));
	}

}
