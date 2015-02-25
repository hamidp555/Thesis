package com.cheo.junit.arff;

import static org.hamcrest.CoreMatchers.is;

import java.util.Iterator;

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

import weka.core.Instances;

import com.cheo.base.ArffLocationBuilder;
import com.cheo.base.enums.Annotators;
import com.cheo.base.enums.ClassLabel;
import com.cheo.base.enums.FileType;
import com.cheo.model.Comment;
import com.cheo.model.repositories.CommentRepository;
import com.cheo.model.repositories.CommentFilter;
import com.cheo.services.ServiceRegistery;
import com.cheo.services.arff.ArffUtils;
import com.cheo.services.excel.ExcelServiceImpl;
import com.cheo.services.excel.TableType;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class ArffFileServiceTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();
	
	@Test
	public void arffForSecondClassifierIrrelevantEDUsRemoved() throws Exception {
		
		ServiceRegistery registery = 
				(ServiceRegistery)applicationContext.getBean("service.registery");

		CommentRepository repository = 
				(CommentRepository)applicationContext.getBean("comment.repository");
		
		ArffLocationBuilder locationBuilder = 
				(ArffLocationBuilder)applicationContext.getBean("arff.location.builder");
				
		CommentFilter filter = 
				(CommentFilter)applicationContext.getBean("filter.comments");

		//Get all comments
		TreeBasedTable<Integer, Integer, Comment> comments = 
				repository.getCommentsFiltered(Annotators.FIRST.getValue());
		
		//Get relevant comments predicted by the first classifier
		TreeBasedTable<Integer, Integer, Comment> relevaneComemnts = 
				filter.getCorrectlyClassifiedCommentsFromPredictionByClasslabel(comments, 
						"arff_relevantIrrelevant_vote_prediction_comments_bow_efs.txt", 
						ClassLabel.RELEVANT);
		
		//Remove irrelevant EDUs from relevant comments
		TreeBasedTable<Integer, Integer, Comment> relevaneComemntsIrrelevantEDUsRemoved = TreeBasedTable.create();
		Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = relevaneComemnts.cellSet().iterator();
		while(cmIter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
			Comment comment = cell.getValue();
			Comment filteredComment = filter.removeIrrelevantEDUs(comment);
			relevaneComemntsIrrelevantEDUsRemoved.put(comment.getSheetID(), comment.getCommentID(), filteredComment);
		}
		
		//Create and save arff
		Instances dataset = registery.getArffService().createArff(relevaneComemntsIrrelevantEDUsRemoved);
		ArffUtils.saveArff(dataset, locationBuilder.buildPath(FileType.ARFF));
		ArffUtils.saveText(dataset, locationBuilder.buildPath(FileType.TXT));

		collector.checkThat(comments.size(), is(936));

	}

	@Test
	@Ignore
	public void arffForClassificationTest() throws Exception {
		ServiceRegistery registery = 
				(ServiceRegistery)applicationContext.getBean("service.registery");

		CommentRepository repository = 
				(CommentRepository)applicationContext.getBean("comment.repository");

		TreeBasedTable<Integer, Integer, Comment> comments = 
				repository.getCommentsFiltered(Annotators.FIRST.getValue());
		
		ArffLocationBuilder locationBuilder = 
				(ArffLocationBuilder)applicationContext.getBean("arff.location.builder");	

		Instances dataset = registery.getArffService().createArff(comments);
		ArffUtils.saveArff(dataset, locationBuilder.buildPath(FileType.ARFF));
		ArffUtils.saveText(dataset, locationBuilder.buildPath(FileType.TXT));

		collector.checkThat(comments.size(), is(939));
		collector.checkThat(true, is(true));
	}
	
	@Test
	@Ignore
	public void arffForArgumentationTest() throws Exception {

		ServiceRegistery registery = 
				(ServiceRegistery)applicationContext.getBean("service.registery");

		ExcelServiceImpl excelService = 
				(ExcelServiceImpl)applicationContext.getBean("excel.service");

		ArffLocationBuilder locationBuilder = 
				(ArffLocationBuilder)applicationContext.getBean("arff.location.builder");	

		TreeBasedTable<Integer, Integer, Comment> comments = TreeBasedTable.create();

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		for(Resource resource : resources){
			if(resource.getFilename().equalsIgnoreCase("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls")){
				comments.putAll(excelService.read(1, TableType.edu, resource));
			}
			if(resource.getFilename().equalsIgnoreCase("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls")){
				comments.putAll(excelService.read(2, TableType.edu, resource));
			}
			if(resource.getFilename().equalsIgnoreCase("GenomeSeqComments_mothering_v2_corrected_AA_eduLevel.xls")){
				comments.putAll(excelService.read(3, TableType.edu, resource));
			}
		}

		Instances dataset = registery.getArffService().createArff(comments);
		ArffUtils.saveArff(dataset, locationBuilder.buildPath(FileType.ARFF));
		ArffUtils.saveText(dataset, locationBuilder.buildPath(FileType.TXT));
	}

}
