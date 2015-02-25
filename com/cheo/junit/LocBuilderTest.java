package com.cheo.junit;


import static org.hamcrest.CoreMatchers.is;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.base.ArffLocationBuilder;
import com.cheo.base.ClassificationLocationBuilder;
import com.cheo.base.enums.FileType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class LocBuilderTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test1() throws Exception {
		ArffLocationBuilder arffService = 
				(ArffLocationBuilder)applicationContext.getBean("arff.location.builder");
		
		ClassificationLocationBuilder classifiService = 
				(ClassificationLocationBuilder)applicationContext.getBean("classification.location.builder");
		
		String arffPath = arffService.buildPath(FileType.ARFF);
		String txtPath = arffService.buildPath(FileType.TXT);
		String modelPath = classifiService.buildPath(FileType.MODEL);
		String evalPath = classifiService.buildPath(FileType.EVAL);
		
		collector.checkThat(arffPath, is("/Users/hamidpoursepanj/Documents/workspace.luna/config/arff/arff_positiveNegativeMixIrrelevant_efs_comments.arff"));	
		collector.checkThat(txtPath, is("/Users/hamidpoursepanj/Documents/workspace.luna/config/arff/txt_positiveNegativeMixIrrelevant_efs_comments.txt"));
		collector.checkThat(modelPath, is("/Users/hamidpoursepanj/Documents/workspace.luna/config/model/model_positiveNegativeMixIrrelevant_efs_comments_smo.model"));
		collector.checkThat(evalPath, is("/Users/hamidpoursepanj/Documents/workspace.luna/config/evaluation/eval_positiveNegativeMixIrrelevant_efs_comments_smo.txt"));
	
	}
	


}
