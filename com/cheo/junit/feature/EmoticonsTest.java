package com.cheo.junit.feature;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.services.feature.EmoticonService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class EmoticonsTest extends AbstractFeatureTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();
	
	@Test
	public void test() {
		EmoticonService service = (EmoticonService)context.getBean("emoticons.service");
		service.updateStatistics(textUnitWrapper);
		collector.checkThat(2, is(textUnitWrapper.getStatistics().getNumNegativeEMOTICON()));
		collector.checkThat(3, is(textUnitWrapper.getStatistics().getNumPositiveEMOTICON()));
	}

}
