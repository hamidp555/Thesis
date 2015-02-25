package com.cheo.junit;

import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.base.TextUnitWrapper;
import com.cheo.model.Comment;
import com.cheo.services.AnalyzerService;
import com.cheo.services.excel.ExcelServiceImpl;
import com.cheo.services.excel.TableType;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class AnalyzerTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();	

	@SuppressWarnings("unused")
	@Test
	public void test() throws Exception{
		AnalyzerService analyserService = 
				(AnalyzerService)applicationContext.getBean("analyzer.service");

		ExcelServiceImpl excelService = 
				(ExcelServiceImpl)applicationContext.getBean("excel.service");

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");

		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				TreeBasedTable<Integer, Integer, Comment> comments = excelService.read(1, TableType.comment, resource);
				Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
				while(cmIter.hasNext()){
					Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
					Comment cm = cell.getValue();
					TextUnitWrapper txw = analyserService.analyze(cm.getComment());
				}
			}
		}
		
	}

}
