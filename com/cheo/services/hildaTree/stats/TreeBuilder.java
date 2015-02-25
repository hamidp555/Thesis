package com.cheo.services.hildaTree.stats;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.cheo.model.Comment;
import com.cheo.model.repositories.CommentRepository;
import com.cheo.services.hildaTree.BinaryTree;
import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.HildaStyleParser;
import com.cheo.services.hildaTree.LinkedBinaryTree;
import com.google.common.collect.TreeBasedTable;

public class TreeBuilder implements InitializingBean{

	private static Logger logger = Logger.getLogger(TreeBuilder.class);

	private final static String TREE_EXT = "tree";

	private final static String HILDA_BASE_FOLDER = 
			"/Users/hamidpoursepanj/Desktop/Masters-semesters/Project-Data/annotated-new/first_annotator_discourse_segmented/output-hilda";

	private final static String HILDA_INPUT11_FOLDER = HILDA_BASE_FOLDER + "/input11";
	private final static String HILDA_INPUT12_FOLDER = HILDA_BASE_FOLDER + "/input12";
	private final static String HILDA_INPUT13_FOLDER = HILDA_BASE_FOLDER + "/input13";

	private CommentRepository repository;

	private TreeBasedTable<Integer, Integer, Comment> comments;

	private List<ITreeInitializer> initializers;

	public void setInitializers(List<ITreeInitializer> initializers) {
		this.initializers = initializers;
	}

	public void setRepository(CommentRepository repository) {
		this.repository = repository;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		comments = repository.getComments("first");
	}

	public TreeBasedTable<Integer, Integer, BinaryTree<Element>> getDiscourseTrees() {

		TreeBasedTable<Integer, Integer, BinaryTree<Element>> table = TreeBasedTable.create();

		try {
			//First sheet
			int sheetID = 1;

			for(File file: getAllTreeFiles(HILDA_INPUT11_FOLDER)){
				int commentID = getCommentNum(file,1);
				LinkedBinaryTree<Element> parseTree = buildTree(file, 1, commentID);
				if(parseTree != null){
					table.put(sheetID, commentID, parseTree);	
				}
			}

			//Second sheet
			sheetID = 2;
			for(File file : getAllTreeFiles(HILDA_INPUT12_FOLDER)){
				int commentID = getCommentNum(file,2);
				LinkedBinaryTree<Element> parseTree = buildTree(file, 2, commentID);
				if(parseTree != null){
					table.put(sheetID, commentID, parseTree);	
				}
			}

			//Third sheet
			sheetID = 3;
			for(File file : getAllTreeFiles(HILDA_INPUT13_FOLDER)){
				int commentID = getCommentNum(file,3);
				LinkedBinaryTree<Element> parseTree = buildTree(file, 3, commentID);
				if(parseTree != null){
					table.put(sheetID, commentID, parseTree);	
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}	

		return table;
	}

	public LinkedBinaryTree<Element> buildTree(
			File file, int sheetID, int commentID) throws Exception{

		LinkedBinaryTree<Element> tree = null;
		Comment comment = getComment(sheetID, commentID);
		//logger.info("investigated : " + "sheetID" + sheetID + "commentID" + commentID);
		if(comment != null){
			tree = HildaStyleParser.parse(file.getAbsolutePath());
			//logger.info("extracted : " + "sheetID" + sheetID + "commentID" + commentID);
			for(ITreeInitializer init : initializers){
				init.initialize(tree, sheetID, commentID);
			}
		}
		return tree;
	}


	private Comment getComment(int sheetID, int commentID) throws Exception{
		Comment comment = comments.get(sheetID, commentID);
		if(comment == null){
			logger.info("sheetID: " + sheetID + " commentID: " + commentID + "is not an eligible comment! please check!");
		}
		return comment;	
	}


	private  List<File> getAllTreeFiles(String folderPath){
		File folder = new File(folderPath);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(TREE_EXT);
			}
		};
		@SuppressWarnings("unchecked")
		Collection<File> files = FileUtils.listFiles(
				folder, 
				FileFilterUtils.asFileFilter(filter), 
				TrueFileFilter.INSTANCE);

		List<File> filesList = new ArrayList<File>(files);

		Collections.sort(filesList, getComparator());
		return filesList;
	}

	private  Comparator<File> getComparator(){
		return  new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				String fileName1 = file1.getName();
				String fileName2 = file2.getName();
				Integer commentNum1 = null;
				Integer commentNum2 = null;
				if(!StringUtils.isBlank(fileName1) && !StringUtils.isBlank(fileName2)){
					commentNum1 = Integer.valueOf(StringUtils.substringBetween(fileName1, "_", "."));
					commentNum2 = Integer.valueOf(StringUtils.substringBetween(fileName2, "_", "."));
				}
				return commentNum1 < commentNum2 ? -1 : (commentNum1 == commentNum2 ? 0 : 1);
			}
		};  
	}

	//94 removed from input11
	//comment id mapping
	private Integer getCommentNum(File file, int sheetID) throws Exception{
		int result = 0;
		String fileName = file.getName();
		if(!StringUtils.isBlank(fileName)){
			String commentIDStr = StringUtils.substringBetween(fileName, "_", ".");
			int commentID = Integer.valueOf(commentIDStr);
			if(sheetID == 1){
				if(commentID < 95){
					result = commentID;
				}else{
					result = commentID -1;
				}
			}
			if(sheetID == 2){
				result = commentID + 1;
			}
			if(sheetID == 3){
				result = commentID;
			}
		}
		return result;
	}


}
