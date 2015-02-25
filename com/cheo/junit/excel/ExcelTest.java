package com.cheo.junit.excel;

import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import com.cheo.services.excel.ExcelService;
import com.cheo.services.excel.ExcelServiceImpl;
import com.cheo.services.excel.ExcelUtils;
import com.cheo.services.excel.TableType;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/META-INF/applicationContext.xml"})
public class ExcelTest {

	private static Logger logger = Logger.getLogger(ExcelTest.class);

	@Autowired
	private ApplicationContext applicationContext;	

	@Rule
	public ErrorCollector collector= new ErrorCollector();

	@Test
	public void test_commentID_edusLevel() throws Exception{
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		TreeBasedTable<Integer, Integer, Comment> comments = service.readAllComments("first");
		Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
		while(cmIter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
			Comment cm = cell.getValue();
			System.out.println("SheetID: " +cm.getSheetID() + " - Author: " + cm.getAuthorName()+ " - CommentID: " +cm.getCommentID());

		}
	}

	@Test
	@Ignore
	public void test1() throws Exception{
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				TreeBasedTable<Integer, Integer, Comment> comments = service.read(1, TableType.edu, resource);
				Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
				while(cmIter.hasNext()){
					Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
					Comment cm = cell.getValue();
					logger.info("comment DI (1) is " + cm.getCommentID());
				}

				//collector.checkThat(comments.size(), is(numCommentsWithEdu(resource)));
			}
			if("GenomeSeqComments_1_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				TreeBasedTable<Integer, Integer, Comment> comments = service.read(1, TableType.comment, resource);
				Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
				while(cmIter.hasNext()){
					Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
					Comment cm = cell.getValue();
					logger.info("comment DI is " + cm.getCommentID());
				}
			}
		}
	}

	@Test
	@Ignore
	public void test2() throws Exception{
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		for(Resource resource : resources){
			if("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				TreeBasedTable<Integer, Integer, Comment> comments = service.read(1, TableType.edu, resource);
				Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
				while(cmIter.hasNext()){
					Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
					Comment cm = cell.getValue();
					System.out.println(cm.getAuthorName());
					for(EDU edu : cm.getEdus()){
						System.out.println(edu.getComment());
					}
					System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
				}
				collector.checkThat(comments.size(), is(numCommentsWithEdu(resource)));

			}
		}
	}


	@Test
	@Ignore
	public void test3() throws Exception {
		ExcelService service = (ExcelService)applicationContext.getBean("excel.service");
		TreeBasedTable<Integer, Integer, Comment> comments = service.readAllComments("first");
		collector.checkThat(totalNumComments(), is(comments.cellSet().size()));
	}

	@Test
	@Ignore
	public void test4() throws Exception {
		ExcelService service = (ExcelService)applicationContext.getBean("excel.service");
		TreeBasedTable<Integer, Integer, Comment> comments = service.readAllCommentsFiltered("first");
		collector.checkThat(totalNumCommentsFiltered(), is(comments.cellSet().size()));
	}

	@Test
	@Ignore
	public void test5() throws Exception {
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");

		TreeBasedTable<Integer, Integer, Comment> edus = null;
		TreeBasedTable<Integer, Integer, Comment> comments = null;
		for(Resource resource : resources){
			if("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				edus = service.read(1, TableType.edu, resource);
			}
			if("GenomeSeqComments_2_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				comments = service.read(1, TableType.comment, resource);
			}
		}
		int numCm = 0;
		service.match(comments, edus);
		Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
		while(cmIter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
			Comment cm = cell.getValue();
			if(!cm.getEdus().isEmpty()){
				numCm++;
			}
		}

		collector.checkThat(numCm, is(edus.size()));
	}

	@Test
	@Ignore
	public void test6() throws Exception {
		ExcelServiceImpl service = (ExcelServiceImpl)applicationContext.getBean("excel.service");
		TreeBasedTable<Integer, Integer, Comment> comments = service.readAllComments("first");

		int numCm = 0;
		Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
		while(cmIter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
			Comment cm = cell.getValue();
			if(!cm.getEdus().isEmpty()){
				numCm++;
			}
		}

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		TreeBasedTable<Integer, Integer, Comment> edus = TreeBasedTable.create();
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				edus.putAll(service.read(1, TableType.edu, resource));
			}
			if("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls".equalsIgnoreCase(resource.getFilename())){
				edus.putAll(service.read(2, TableType.edu, resource));
			}
		}
		collector.checkThat(numCm, is(edus.size()));
	}



	//--------------------------------//Helper methods//-------------------------------//

	private int totalNumComments() throws Exception{
		int totalNumComments = 0;
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				totalNumComments += numComments(resource);
			}
			if("GenomeSeqComments_2_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				totalNumComments += numComments(resource);
			}
			if("GenomeSeqComments_mothering_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				totalNumComments += numComments(resource);
			}
		}
		return totalNumComments;
	}

	//Helper methods
	private int totalNumCommentsFiltered() throws Exception{
		int totalNumCommentsFiltered = 0;
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		for(Resource resource : resources){
			if("GenomeSeqComments_1_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				totalNumCommentsFiltered += numCommentsFiltered(resource);
			}
			if("GenomeSeqComments_2_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				totalNumCommentsFiltered += numCommentsFiltered(resource);
			}
			if("GenomeSeqComments_mothering_v2_corrected_AA.xls".equalsIgnoreCase(resource.getFilename())){
				totalNumCommentsFiltered += numCommentsFiltered(resource);
			}
		}
		return totalNumCommentsFiltered;
	}

	private int numComments(Resource input) throws IOException{
		int numComments = 0;
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(input.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				if (row.getRowNum() == 0) continue;
				numComments ++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return numComments;
	}

	private int numCommentsWithEdu(Resource input) throws IOException{
		int numComments = 0;
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(input.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);

			int authorIndex = 0;
			int commentTitleIndex = 0;
			int commentIndex = 0;
			int discussionIndex = 0;
			int dateIndex = 0;
			int discriminIndex = 0;
			List<String> headers;

			for (Row row : sheet) {
				if (row.getRowNum() == 0){
					headers = new ArrayList<String>();
					for (Cell cell : row) {
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						headers.add(value);
					}
					int index = 0;
					for(String header : headers){
						String cleaned = StringUtils.deleteWhitespace(header);
						if(StringUtils.equalsIgnoreCase(cleaned, "AuthorName")){
							authorIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(cleaned, "CommentTitle")){
							commentTitleIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(cleaned, "Comment")){
							commentIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(cleaned, "discussiontitle")){
							discussionIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(cleaned, "DateDownloaded")){
							dateIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(cleaned, "Discriminator")){
							discriminIndex = index;
						}
						index++;
					}
					continue;
				}

				boolean hasAuthor = false;
				boolean hasCommentTitle = false;
				boolean haComment = false;
				boolean hasDiscTitle = false;
				boolean hasDate = false;
				boolean hasDiscrimin = false;

				for (Cell cell : row) {

					if(cell.getColumnIndex() == authorIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null){
							hasAuthor = true;
						}
					}
					if(cell.getColumnIndex() == commentTitleIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null){
							hasCommentTitle = true;
						}
					}
					if(cell.getColumnIndex() == commentIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null){
							haComment = true;
						}
					}
					if(cell.getColumnIndex() == discussionIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null){
							hasDiscTitle = true;
						}
					}
					if(cell.getColumnIndex() == dateIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null){
							hasDate = true;
						}
					}
					if(cell.getColumnIndex() == discriminIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null){
							hasDiscrimin = true;
						}
					}
				}

				if(hasAuthor && hasCommentTitle && haComment && hasDiscTitle && hasDate && hasDiscrimin){
					numComments ++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return numComments;
	}


	private int numCommentsFiltered(Resource input){

		int numComments = 0;
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(input.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);

			List<String> headers;

			int copyIndex = 0;
			int posIndex = 0;
			int negIndex = 0;
			int mixIndex = 0;
			int irrelIndex = 0;

			for (Row row : sheet) {
				if (row.getRowNum() == 0){
					headers = new ArrayList<String>();
					for (Cell cell : row) {
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						headers.add(value);
					}
					int index = 0;
					for(String header : headers){
						if(StringUtils.equalsIgnoreCase(header, "copy")){
							copyIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(header, "positive")){
							posIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(header, "negative")){
							negIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(header, "mix")){
							mixIndex = index;
						}
						if(StringUtils.equalsIgnoreCase(header, "irrelevant")){
							irrelIndex = index;
						}

						index++;
					}
					continue;
				}
				for (Cell cell : row) {

					boolean isCopy = false;
					boolean isPos = false;
					boolean isNeg = false;
					boolean isMix = false;
					boolean isIrrel = false;

					if(cell.getColumnIndex() == copyIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null && value.equalsIgnoreCase("1.0")){
							isCopy = true;
						}
					}
					if(cell.getColumnIndex() == posIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null && value.equalsIgnoreCase("1.0")){
							isPos = true;
						}
					}
					if(cell.getColumnIndex() == negIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null && value.equalsIgnoreCase("1.0")){
							isNeg = true;
						}
					}
					if(cell.getColumnIndex() == mixIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null && value.equalsIgnoreCase("1.0")){
							isMix = true;
						}
					}
					if(cell.getColumnIndex() == irrelIndex){
						String value = ExcelUtils.readCell(sheet, cell.getColumnIndex(), cell.getRowIndex());
						if(value != null && value.equalsIgnoreCase("1.0")){
							isIrrel = true;
						}
					}

					if((isPos || isNeg || isMix || isIrrel) && !isCopy){
						numComments++;
					}

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return numComments;
	}



}
