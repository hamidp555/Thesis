package com.cheo.junit.feature;

import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.base.TextUnitWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class AbstractFeatureTest implements InitializingBean{
	
	@Autowired
	protected ApplicationContext context;	
	
	protected List<String> lines;
	
	protected String comment;
	
	protected TextUnitWrapper textUnitWrapper = new TextUnitWrapper();

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:/comment.txt");
		lines = FileUtils.readLines(resources[0].getFile());
		comment = StringUtils.join(lines, " ");
		textUnitWrapper.setTextUnit(comment);
	}

}
