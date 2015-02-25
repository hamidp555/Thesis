package com.cheo.junit.excel;


import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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

import com.cheo.model.Comment;
import com.cheo.model.EDU;
import com.cheo.services.excel.ExcelServiceImpl;
import com.cheo.services.excel.TableType;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class FilterTopicTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test() throws Exception{
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		TreeBasedTable<Integer, Integer, Comment> comments = TreeBasedTable.create();
		
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				comments.putAll(service.read(1, TableType.edu, resource));
			}
			if("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				comments.putAll(service.read(2, TableType.edu, resource));
			}
			if("GenomeSeqComments_mothering_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				comments.putAll(service.read(3, TableType.edu, resource));
			}
		}
		
		filterTopic(comments);
	}
	

	private TreeBasedTable<Integer, Integer, Comment> filterTopic(TreeBasedTable<Integer, Integer, Comment> input){

		Set<Table.Cell<Integer,Integer,Comment>> cells = input.cellSet();
		Iterator<Table.Cell<Integer,Integer,Comment>> tableIter = cells.iterator();

		while(tableIter.hasNext()){
			Table.Cell<Integer,Integer,Comment> cell = tableIter.next();
			Comment comment = cell.getValue();
			
			Iterator<EDU> eduIter  = comment.getEdus().iterator();
			while(eduIter.hasNext()){
				EDU edu = eduIter.next();
				if(edu.hasTopic() && 
						StringUtils.isBlank(edu.getPro()) && 
						StringUtils.isBlank(edu.getCon())){
					System.out.println("Sheet ID: " + edu.getSheetID() +", " + "EDU ID: " + (edu.getEduID()+1));
					eduIter.remove();
				}
			}
		}
		return input;
	}

}
