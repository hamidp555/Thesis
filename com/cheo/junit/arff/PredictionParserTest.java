package com.cheo.junit.arff;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cheo.base.LocationBuilder;
import com.cheo.base.enums.FileType;
import com.cheo.weka.classifiers.prediction.Prediction;
import com.cheo.weka.classifiers.prediction.PredictionParser;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class PredictionParserTest {

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();


	@Test
	public void test() throws Exception {

		LocationBuilder locationBuilder = 
				(LocationBuilder)applicationContext.getBean("classification.location.builder");

		File file = new File(locationBuilder.buildPath(FileType.EVAL));
		@SuppressWarnings("unchecked")
		List<String> lines = FileUtils.readLines(file, "utf-8");

		Map<String, Prediction> predMap = new HashMap<String, Prediction>();

		StringBuilder sb = new StringBuilder();
		for(String line : lines){
			Prediction pred = PredictionParser.parse(line);
			if (pred.isEmpty()){
				continue;
			}

			sb.append(pred.getReference().getSheetID());
			sb.append("-");
			sb.append(pred.getReference().getCommentID());
			sb.append("-");
			sb.append(pred.getReference().getEduID());
			predMap.put(sb.toString(), pred);
			sb.setLength(0);
		}

		collector.checkThat(predMap.size(), is(13527));
	}

}
