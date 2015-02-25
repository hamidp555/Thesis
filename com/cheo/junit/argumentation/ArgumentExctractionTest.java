package com.cheo.junit.argumentation;


import static org.hamcrest.CoreMatchers.is;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
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
import com.cheo.model.repositories.CommentRepository;
import com.cheo.services.argumentation.ArgumentationExtractionService;
import com.cheo.services.excel.ExcelServiceImpl;
import com.cheo.services.excel.TableType;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class ArgumentExctractionTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();
	
	@Test
	@Ignore
	public void test1() throws Exception {
		CommentRepository repoService = 
				(CommentRepository)applicationContext.getBean("comment.repository");	
		
		int count = 0;
		TreeBasedTable<Integer, Integer, Comment> comments = repoService.getComments("first");
		Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
		while(cmIter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
			Comment comment = cell.getValue();
			count += comment.getArguments().size();
			
		}
		
		List<String> filenames = new LinkedList<String>();
		filenames.add("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls");
		filenames.add("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls");
		collector.checkThat(count, is(numArguments(filenames)));
		
	}
	
	@Test
	@Ignore
	public void test3() throws Exception {
		List<String> filenames = new LinkedList<String>();
		filenames.add("argTest.xls");
		collector.checkThat(numTopics(filenames), is(numArguments(filenames)));
	}

	@Test
	@Ignore
	public void test4() throws Exception {
		List<String> filenames = new LinkedList<String>();
		filenames.add("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls");
		filenames.add("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls");
		collector.checkThat(numTopics(filenames), is(numArguments(filenames)));
	}
	
	@Test
	@Ignore
	public void test5() throws Exception {
		ArgumentationExtractionService service = 
				(ArgumentationExtractionService)applicationContext.getBean("arumentation.service");	
		
		service.processPro();
	}
	
	private int numTopics(List<String> filenames) throws Exception{
		int count =0;
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		for(Resource resource : resources){
			for(String filename : filenames){
				if(filename.equalsIgnoreCase(resource.getFilename())){
					TreeBasedTable<Integer, Integer, Comment> comments = service.read(1, TableType.edu, resource);
					Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
					while(cmIter.hasNext()){
						Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
						Comment comment = cell.getValue();
						for(EDU edu: comment.getEdus()){
							if(!StringUtils.isBlank(edu.getTopic())){
								count++;
							}
						}
					}
				}
			}
		}
		return count;
	}
	
	private int numArguments(List<String> filenames) throws Exception{
		int count =0;
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		for(Resource resource : resources){
			for(String filename : filenames){
				if(filename.equalsIgnoreCase(resource.getFilename())){
					TreeBasedTable<Integer, Integer, Comment> comments = service.read(1, TableType.edu, resource);
					Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
					while(cmIter.hasNext()){
						Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
						Comment comment = cell.getValue();
						comment.addArguments();
						count += comment.getArguments().size();
					}
					
				}
			}
		}
		return count;
	}

}
