package com.cheo.junit.hilda;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.services.hildaTree.BinaryTree;
import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.stats.TreeBuilder;
import com.google.common.collect.TreeBasedTable;

import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class HildaParserTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();
	
	@Test
	public void test1() throws Exception{
		TreeBuilder service = (TreeBuilder)applicationContext.getBean("hilda.tree.builder");
		TreeBasedTable<Integer, Integer, BinaryTree<Element>> trees = service.getDiscourseTrees();
		
		trees.get(1, 4);
		collector.checkThat(trees.size(), is(803));

	}
}
