package com.cheo.junit;

import static org.hamcrest.CoreMatchers.is;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.base.enums.ClassLabel;
import com.cheo.model.Comment;
import com.cheo.model.repositories.CommentRepository;
import com.cheo.services.excel.ExcelService;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class ComentRepoTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test() throws Exception{
		CommentRepository repoService = 
				(CommentRepository)applicationContext.getBean("comment.repository");	
		
		TreeBasedTable<Integer, Integer, Comment> comments = repoService.getComments("first");
		collector.checkThat(comments.size(), is(numComments()));
	}
	
	private int numComments() throws Exception{
		
		//should match the configuration for arff file
		List<ClassLabel> classLabels = new LinkedList<ClassLabel>();
		classLabels.add(ClassLabel.POSITIVE);
		classLabels.add(ClassLabel.NEGATIVE);
		classLabels.add(ClassLabel.MIX);
		classLabels.add(ClassLabel.IRRELEVANT);
		
		ExcelService service = (ExcelService)applicationContext.getBean("excel.service");
		return filter(service.readAllComments("first"), classLabels).size();

	}

	private TreeBasedTable<Integer, Integer, Comment> filter(
			TreeBasedTable<Integer, Integer, Comment> comments, List<ClassLabel> classLabels) throws Exception{

		TreeBasedTable<Integer, Integer, Comment> copied = TreeBasedTable.create(comments);
		Iterator<Table.Cell<Integer, Integer, Comment>> iter = copied.cellSet().iterator();

		while(iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();
			if(!classLabels.contains(comment.getClassLabel())){
				iter.remove();
			}
		}
		return copied;
	}

}
