package com.cheo.services.excel;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.Resource;

import com.cheo.model.Comment;
import com.cheo.model.EDU;
import com.cheo.services.SpellChekerService;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


public class ExcelServiceImpl implements ExcelService {

	private static Logger logger = Logger.getLogger(ExcelServiceImpl.class);

	private ExcelConfig config;

	@SuppressWarnings("unused")
	private SpellChekerService spellChecker;

	private Resource  first_annotator_comments_1;

	private Resource  first_annotator_comments_2;

	private Resource  first_annotator_comments_3;

	private Resource  second_annotator_comments_1;

	private Resource  second_annotator_comments_2;

	private Resource  second_annotator_comments_3;

	private Resource  first_annotator_edus_1;

	private Resource  first_annotator_edus_2;

	private Resource  first_annotator_edus_3;

	public void setFirst_annotator_comments_1(Resource first_annotator_comments_1) {
		this.first_annotator_comments_1 = first_annotator_comments_1;
	}

	public void setFirst_annotator_comments_2(Resource first_annotator_comments_2) {
		this.first_annotator_comments_2 = first_annotator_comments_2;
	}

	public void setFirst_annotator_comments_3(Resource first_annotator_comments_3) {
		this.first_annotator_comments_3 = first_annotator_comments_3;
	}

	public void setSecond_annotator_comments_1(Resource second_annotator_comments_1) {
		this.second_annotator_comments_1 = second_annotator_comments_1;
	}

	public void setSecond_annotator_comments_2(Resource second_annotator_comments_2) {
		this.second_annotator_comments_2 = second_annotator_comments_2;
	}

	public void setSecond_annotator_comments_3(Resource second_annotator_comments_3) {
		this.second_annotator_comments_3 = second_annotator_comments_3;
	}

	public void setFirst_annotator_edus_1(Resource first_annotator_edus_1) {
		this.first_annotator_edus_1 = first_annotator_edus_1;
	}

	public void setFirst_annotator_edus_2(Resource first_annotator_edus_2) {
		this.first_annotator_edus_2 = first_annotator_edus_2;
	}

	public void setFirst_annotator_edus_3(Resource first_annotator_edus_3) {
		this.first_annotator_edus_3 = first_annotator_edus_3;
	}

	public void setConfig(ExcelConfig config) {
		this.config = config;
	}

	public void setSpellChecker(SpellChekerService spellChecker) {
		this.spellChecker = spellChecker;
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
					cm_with_edu.setCommentID(cm_without_edu.getCommentID());
					List<EDU> edus = cm_with_edu.getEdus();
					for(EDU edu : edus){					
						if(!edu.getSheetID().equals(cm_without_edu.getSheetID())){
							System.out.println("SheetID: "+ edu.getSheetID() + " EDU ID: " + edu.getEduID() + " :Edu sheet ID doesn't match the parent comment sheet ID");
							System.out.println("SheetID: "+ cm_without_edu.getSheetID() + " Comment ID: " + cm_without_edu.getCommentID() + " :Edu sheet ID doesn't match the parent comment sheet ID");
							throw new RuntimeException(edu.getEduID() + " :Edu sheet ID doesn't match the parent comment sheet ID");
						}
						edu.setCommentID(cm_without_edu.getCommentID());
					}
					cm_without_edu.setEdus(edus);
				}
			}
		}
	}

	public TreeBasedTable<Integer, Integer, Comment> read(int sheetNum, TableType type, Resource input){

		TreeBasedTable<Integer, Integer, Comment> rowTable = TreeBasedTable.create();
		List<Header> headers = config.getTableMap().get(type.toString());
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(input.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);
			Comment comment  = null;
			int index = 0;
			for (Row row : sheet) {
				//Skip the header row
				if (row.getRowNum() == 0) continue;

				if(type.equals(TableType.comment)){
					Class<?> clazz = Class.forName("com.cheo.model.Comment");
					comment = (Comment)clazz.newInstance();
					comment.setSheetID(sheetNum);
					comment.setCommentID(row.getRowNum());
					if(logger.isDebugEnabled()){
						logger.info("Comment with ID " + row.getRowNum() + " created!");
					}

					for(Header header: headers){
						String value = ExcelUtils.readCell(sheet, header.getIndex(), row.getRowNum());
						String methodName = "set" + StringUtils.capitalize(header.getName());
						Method method = clazz.getMethod(methodName, new Class[] {String.class});
						method.invoke(comment, new Object[] {value});
					}

					rowTable.put(sheetNum, row.getRowNum(), comment);
				}

				if(type.equals(TableType.edu)){
					Class<?> clazz = Class.forName("com.cheo.model.EDU");
					EDU edu = (EDU)clazz.newInstance();
					edu.setEduID(row.getRowNum());
					edu.setSheetID(sheetNum);

					if(logger.isDebugEnabled()){
						logger.info("EDU with ID " + row.getRowNum() + " created!");
					}
					for(Header header: headers){
						String value = ExcelUtils.readCell(sheet, header.getIndex(), row.getRowNum());
						String methodName = "set" + StringUtils.capitalize(header.getName());
						Method method = clazz.getMethod(methodName, new Class[] {String.class});
						method.invoke(edu, new Object[] {value});
					}
					if(!StringUtils.isBlank(edu.getAuthorName()) && 
							!StringUtils.isBlank(edu.getCommentTitle())){
						Class<?> clazz2 = Class.forName("com.cheo.model.Comment");
						index++;
						comment = (Comment)clazz2.newInstance();
						comment.setAuthorName(edu.getAuthorName());
						comment.setComment(edu.getComment());
						comment.setCommentTitle(edu.getCommentTitle());
						comment.setDiscussionTitle(edu.getDiscussionTitle());
						comment.setDateDownloaded(edu.getDateDownloaded());
						comment.setDiscriminator(edu.getDiscriminator());
						comment.setSheetID(sheetNum);
						comment.setCommentID(index);
						if(logger.isDebugEnabled()){
							logger.info("Comment with ID " + index + " created!");
						}
						rowTable.put(sheetNum, index, comment);

					}
					else{
						comment = rowTable.get(sheetNum, index);
						edu.setCommentID(index);
						comment.getEdus().add(edu);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rowTable;
	}

	private TreeBasedTable<Integer, Integer, Comment> readComments(String annotator, boolean filtered) {

		TreeBasedTable<Integer, Integer, Comment> cmTable = TreeBasedTable.create();

		switch(annotator){
		case "first":
			TreeBasedTable<Integer, Integer, Comment> comments_with_edus1 = FilterFactory.filterEdus(read(1, TableType.edu, first_annotator_edus_1));
			TreeBasedTable<Integer, Integer, Comment> comments_with_edus2 = FilterFactory.filterEdus(read(2, TableType.edu, first_annotator_edus_2));
			TreeBasedTable<Integer, Integer, Comment> comments_with_edus3 = FilterFactory.filterEdus(read(3, TableType.edu, first_annotator_edus_3));

			if(filtered){
				TreeBasedTable<Integer, Integer, Comment> first_comments1_filtered = FilterFactory.filterComments(read(1, TableType.comment, first_annotator_comments_1));
				match(first_comments1_filtered, comments_with_edus1);
				cmTable.putAll(first_comments1_filtered);

				TreeBasedTable<Integer, Integer, Comment> first_comments2_filtered = FilterFactory.filterComments(read(2, TableType.comment, first_annotator_comments_2));
				match(first_comments2_filtered, comments_with_edus2);
				cmTable.putAll(first_comments2_filtered);

				TreeBasedTable<Integer, Integer, Comment> first_comments3_filtered = FilterFactory.filterComments(read(3, TableType.comment, first_annotator_comments_3));
				match(first_comments3_filtered, comments_with_edus3);
				cmTable.putAll(first_comments3_filtered);
			}else{
				TreeBasedTable<Integer, Integer, Comment> first_comments1 = read(1, TableType.edu, first_annotator_edus_1);
				match(first_comments1, comments_with_edus1);
				cmTable.putAll(first_comments1);

				TreeBasedTable<Integer, Integer, Comment> first_comments2 = read(2, TableType.edu, first_annotator_edus_2);
				match(first_comments2, comments_with_edus2);
				cmTable.putAll(first_comments2);

				TreeBasedTable<Integer, Integer, Comment> first_comments3 = read(3, TableType.comment, first_annotator_comments_3);
				match(first_comments3, comments_with_edus3);
				cmTable.putAll(first_comments3);
			}
			break;
		case "second":
			if(filtered){
				cmTable.putAll(FilterFactory.filterComments(read(1, TableType.comment, second_annotator_comments_1)));
				cmTable.putAll(FilterFactory.filterComments(read(2, TableType.comment, second_annotator_comments_2)));
				cmTable.putAll(FilterFactory.filterComments(read(3, TableType.comment, second_annotator_comments_3)));

			}else{
				cmTable.putAll(read(1, TableType.comment, second_annotator_comments_1));
				cmTable.putAll(read(2, TableType.comment, second_annotator_comments_2));
				cmTable.putAll(read(3, TableType.comment, second_annotator_comments_3));
			}
			break;
		default: 
			throw new IllegalArgumentException("invalid annotataor!");
		}

		return cmTable;
	}

	@Override
	public TreeBasedTable<Integer, Integer, Comment> readAllComments(String annotator) {
		return readComments(annotator, false);
	}

	@Override
	public TreeBasedTable<Integer, Integer, Comment> readAllCommentsFiltered(String annotator){
		return readComments(annotator, true);
	}

}
