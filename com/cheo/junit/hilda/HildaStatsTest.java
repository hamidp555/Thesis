package com.cheo.junit.hilda;

import java.util.Collection;
import java.util.Map.Entry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.services.hildaTree.RSTRelation;
import com.cheo.services.hildaTree.stats.HildaStatistics;
import com.cheo.services.hildaTree.stats.RelationWrapper;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class HildaStatsTest {


	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test1() throws Exception{
		HildaStatistics service = (HildaStatistics)applicationContext.getBean("hilda.statistics.service");

		for(RSTRelation rstRelation : RSTRelation.values()){
			System.out.println("=============" + rstRelation.toString() + "=============");
			for(Entry<String, Collection<RelationWrapper>> entry : service.getRelationstats(rstRelation).entrySet()){
				System.out.println(entry.getKey() + "  :  " + entry.getValue().size());	
			}
		}
		
		//service.getAllDiscourseStats();
	}

}
