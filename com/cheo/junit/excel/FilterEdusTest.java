package com.cheo.junit.excel;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import com.cheo.services.excel.ExcelServiceImpl;
import com.cheo.services.excel.TableType;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class FilterEdusTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test1() throws Exception{
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				printEdus(service.read(1, TableType.edu, resource));
			}
			if("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				printEdus(service.read(2, TableType.edu, resource));
			}
			if("GenomeSeqComments_mothering_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				printEdus(service.read(3, TableType.edu, resource));
			}
		}
	}
	
	@Test
	@Ignore
	public void test2() throws Exception{
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		
		TreeBasedTable<Integer, Integer, Comment> cmTable = TreeBasedTable.create();
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				TreeBasedTable<Integer, Integer, Comment> comments = service.read(1, TableType.edu, resource);
				cmTable.putAll(filterEdus(comments));
			}
			if("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				TreeBasedTable<Integer, Integer, Comment> comments = service.read(2, TableType.edu, resource);
				cmTable.putAll(filterEdus(comments));
			}
			if("GenomeSeqComments_mothering_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				TreeBasedTable<Integer, Integer, Comment> comments = service.read(3, TableType.edu, resource);
				cmTable.putAll(filterEdus(comments));
			}
		}
		printEdus(cmTable);
	}
	
	@Test
	@Ignore
	public void test3() throws Exception{
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		TreeBasedTable<Integer, Integer, Comment> comments = TreeBasedTable.create();
		TreeBasedTable<Integer, Integer, Comment> edus = TreeBasedTable.create();
		
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				comments.putAll(filterComments(service.read(1, TableType.comment, resource)));
			}
		}
		
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				edus.putAll(filterEdus(service.read(1, TableType.edu, resource)));
			}
		}
		match(comments, edus);
		printEdus(comments);
	}
	
	@Test
	@Ignore
	public void test4() throws Exception{
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");		
		TreeBasedTable<Integer, Integer, Comment> comments = service.readAllCommentsFiltered("first");
		printEdus(comments);
	}
	
	private void printEdus(TreeBasedTable<Integer, Integer, Comment> comments){
		Iterator<Table.Cell<Integer, Integer, Comment>> iter = comments.cellSet().iterator();
		while(iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();
			System.out.println("comment: sheet ID: " +comment.getSheetID() + " comment ID: " +  comment.getCommentID());
			System.out.println("EDUs: ");
			for(EDU edu : comment.getEdus()){
				System.out.println("	" + edu.getComment());
			}
		}
	}
	
	private TreeBasedTable<Integer, Integer, Comment> filterEdus(TreeBasedTable<Integer, Integer, Comment> input){

		Set<Table.Cell<Integer,Integer,Comment>> cells = input.cellSet();
		Iterator<Table.Cell<Integer,Integer,Comment>> tableIter = cells.iterator();

		while(tableIter.hasNext()){
			Table.Cell<Integer,Integer,Comment> cell = tableIter.next();
			Comment comment = cell.getValue();
			
			Iterator<EDU> eduIter  = comment.getEdus().iterator();
			while(eduIter.hasNext()){
				EDU edu = eduIter.next();
				if(!edu.isAnnotated() || !edu.hasClassLabel()){
					//System.out.println("Sheet ID: " + edu.getSheetID() +", " + "EDU ID: " + (edu.getEduID()+1));
					eduIter.remove();
				}
			}
		}
		return input;
	}
public void match(TreeBasedTable<Integer, Integer, Comment>  cms_without_edu, TreeBasedTable<Integer, Integer, Comment> cms_with_edu){
		
		Iterator<Table.Cell<Integer, Integer, Comment>> cms_without_edu_iter = cms_without_edu.cellSet().iterator();
		while(cms_without_edu_iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = cms_without_edu_iter.next();
			Comment cm_without_edu = cell.getValue();

			Iterator<Table.Cell<Integer, Integer, Comment>> cms_with_edu_iter = cms_with_edu.cellSet().iterator();
			while(cms_with_edu_iter.hasNext()){
				Table.Cell<Integer, Integer, Comment> cell2 = cms_with_edu_iter.next();
				Comment cm_with_edu = cell2.getValue();
				
				if(cm_without_edu.equalsSpecialCase(cm_with_edu)){
					
					//Set reference to comment
					List<EDU> edus = cm_with_edu.getEdus();
					for(EDU edu : edus){
						
						if(!edu.getSheetID().equals(cm_without_edu.getSheetID())){
							System.out.println("SheetID: "+ edu.getSheetID() + " EDU ID: " + edu.getEduID() + " :Edu sheet ID doesn't match the parent comment sheet ID");
							System.out.println("SheetID: "+ cm_without_edu.getSheetID() + " Comment ID: " + cm_without_edu.getCommentID() + " :Edu sheet ID doesn't match the parent comment sheet ID");
							throw new RuntimeException(edu.getEduID() + " :Edu sheet ID doesn't match the parent comment sheet ID");
						}
					}
					
					//Set edus 
					cm_without_edu.setEdus(edus);
				}
			}
		}
	}


private TreeBasedTable<Integer, Integer, Comment> filterComments(TreeBasedTable<Integer, Integer, Comment> input){

	Set<Table.Cell<Integer,Integer,Comment>> cells = input.cellSet();
	Iterator<Table.Cell<Integer,Integer,Comment>> iter = cells.iterator();

	while(iter.hasNext()){
		Table.Cell<Integer,Integer,Comment> cell = iter.next();
		Comment comment = cell.getValue();
		
		if(!comment.isAnnotated() || 
				comment.isDuplicate() || 
				!comment.hasClassLabel()){
			iter.remove();
		}
	}
	return input;
}
}
