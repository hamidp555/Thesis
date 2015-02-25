package com.cheo.services.hildaTree.stats;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import com.cheo.services.excel.Counter;
import com.cheo.services.hildaTree.BinaryTree;
import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.HildaStyleParser;
import com.cheo.services.hildaTree.RSTRelation;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;


public class StatsUtil {

	private final static String HILDA_FOLDER = 
			"/Users/hamidpoursepanj/Desktop/Masters-semesters/Project-Data/annotated-new/first_annotator_discourse_segmented/output-hilda";

	private final static String TREE_EXT = "tree";
	private final static String EDU_EXT = "edus";
	private final static RSTRelation[] RST_RELATIONS = RSTRelation.class.getEnumConstants();
	private final static List<File> TREE_FILES = getAllTreeFiles();
	private final static List<File> EDU_FILES = getAllEduFiles();
	private final static Integer TOTAL_NUM_EDUS = getTotalNumEdus();
	public static ListMultimap<Integer, BinaryTree<Element>> DISCOURSE_TREES = getDiscourseTrees();

	private StatsUtil (){}

	private static Comparator<File> getComparator(){
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
				return commentNum1 > commentNum2 ? -1 : (commentNum1 == commentNum2 ? 0 : 1);
			}
		};  
	}

	private static List<File> getAllEduFiles(){
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(EDU_EXT);
			}
		};
		File folder = new File(HILDA_FOLDER);
		@SuppressWarnings("unchecked")
		Collection<File> files = FileUtils.listFiles(
				folder, 
				FileFilterUtils.asFileFilter(filter), 
				TrueFileFilter.INSTANCE);

		List<File> filesList = new ArrayList<File>(files);
		Collections.sort(filesList, getComparator());
		return filesList;

	}

	private static List<File> getAllTreeFiles(){
		File folder = new File(HILDA_FOLDER);
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

	private static Integer getCommentNum(File file){
		String fileName = file.getName();
		if(!StringUtils.isBlank(fileName)){
			return Integer.valueOf(StringUtils.substringBetween(fileName, "_", "."));
		}
		return null;
	}

	private static ListMultimap<Integer, BinaryTree<Element>> getDiscourseTrees(){
		ListMultimap<Integer, BinaryTree<Element>> multiMap = ArrayListMultimap.create();
		for (File file: TREE_FILES) {
			try {
				multiMap.put(getCommentNum(file), 
						HildaStyleParser.parse(file.getAbsolutePath()));
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		return multiMap;
	}

	@SuppressWarnings("unchecked")
	private static int getNumEdus(File file) throws IOException{
		List<String> lines = FileUtils.readLines(file, "utf-8");
		int counter = 0;
		for(String line : lines){
			String lineNoNewLine = StringUtils.chomp(line);
			if(StringUtils.startsWith(lineNoNewLine, "<edu>") && 
					StringUtils.endsWith(lineNoNewLine, "</edu>")){
				counter++;
			}
		}
		return counter;
	}

	public static int getTotalNumEdus(){
		int total = 0;
		for(File file :EDU_FILES){
			try {
				total += getNumEdus(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return total;
	}

	private static Map<RSTRelation, Integer> getNumRelationsByRelation(BinaryTree<Element> tree){

		Map<RSTRelation, Integer> map = new HashMap<RSTRelation, Integer>();
		List<RSTRelation> relationsList = new LinkedList<RSTRelation>();

		Traverse traverse = new Traverse();
		traverse.traversePreorder(tree, tree.root(), "element");
		for(Element elem : traverse.getRelationElements()){
			relationsList.add(elem.getRstRelation());
		}

		Set<RSTRelation> relationsSet = new HashSet<RSTRelation>(relationsList);
		for(RSTRelation relation: relationsSet){
			int occurrences = Collections.frequency(relationsList, relation);
			map.put(relation, occurrences);
		}
		return map;
	}

	private static Map<RSTRelation, Integer> getNumEdusByRelation(BinaryTree<Element> tree){
		Map<RSTRelation, Integer> map = new HashMap<RSTRelation, Integer>();
		List<RSTRelation> relationsList = new LinkedList<RSTRelation>();
		Traverse traverse = new Traverse();
		traverse.traversePreorder(tree, tree.root(), "rstRelation");
		for(RelationWrapper relationWrapper : traverse.getRelations()){
			for(Element element: relationWrapper.getChildren()){
				if(element.getEdu() != null){
					relationsList.add(relationWrapper.getRelation().getRstRelation());
				}
			}
		}
		Set<RSTRelation> relationsSet = new HashSet<RSTRelation>(relationsList);
		for(RSTRelation relation: relationsSet){
			int occurrences = Collections.frequency(relationsList, relation);
			map.put(relation, occurrences);
		}
		return map;
	}

	private static Integer getNumRelations(BinaryTree<Element> tree){
		Map<RSTRelation, Integer> relationMap = getNumRelationsByRelation(tree);
		Set<Entry<RSTRelation, Integer>> entries = relationMap.entrySet();
		int totalNumRelations = 0;
		for(Entry<RSTRelation, Integer> entry : entries){
			totalNumRelations += entry.getValue();
		}
		return totalNumRelations;
	}

	public static Integer getTotalNumRelations(){
		int numTotalRelations = 0;
		for(Entry<Integer, BinaryTree<Element>> entry : DISCOURSE_TREES.entries()){
			numTotalRelations += getNumRelations(entry.getValue());
		}
		return numTotalRelations;
	}

	public static Map<RSTRelation, Integer> getTotalNumRelationsByRelation(){

		List<RSTRelation> relations = new ArrayList<RSTRelation>(Arrays.asList(RST_RELATIONS));
		Map<RSTRelation, Counter> counterMap = new HashMap<RSTRelation, Counter>();
		for(RSTRelation relation: relations){
			counterMap.put(relation, new Counter(0));
		}

		for(Entry<Integer, BinaryTree<Element>> treeEntry : DISCOURSE_TREES.entries()){
			for(Entry<RSTRelation, Integer> relationEntry : 
				getNumRelationsByRelation(treeEntry.getValue()).entrySet()){
				RSTRelation relation = relationEntry.getKey();
				Integer count = relationEntry.getValue();
				Counter counter = counterMap.get(relation);
				counter.incrementBy(count);
				counterMap.put(relation, counter);
			}
		}

		Map<RSTRelation, Integer> result = new HashMap<RSTRelation, Integer>();
		for(Entry<RSTRelation, Counter> entry : counterMap.entrySet()){
			result.put(entry.getKey(), entry.getValue().getCounts());
		}
		return result;
	}

	public static Map<RSTRelation, Double> getProbabilityDistOfEdusByRelation(){

		List<RSTRelation> relations = new ArrayList<RSTRelation>(Arrays.asList(RST_RELATIONS));
		Map<RSTRelation, Counter> counterMap = new HashMap<RSTRelation, Counter>();
		for(RSTRelation relation: relations){
			counterMap.put(relation, new Counter(0));
		}

		for(Entry<Integer, BinaryTree<Element>> treeEntry : DISCOURSE_TREES.entries()){
			for(Entry<RSTRelation, Integer> relationEntry : 
				getNumEdusByRelation(treeEntry.getValue()).entrySet()){
				RSTRelation relation = relationEntry.getKey();
				Integer count = relationEntry.getValue();
				Counter counter = counterMap.get(relation);
				counter.incrementBy(count);
				counterMap.put(relation, counter);	
			}
		}

		Map<RSTRelation, Double> result = new HashMap<RSTRelation, Double>();
		for(Entry<RSTRelation, Counter> relationEntry : counterMap.entrySet()){
			RSTRelation relation = relationEntry.getKey();
			Integer count = relationEntry.getValue().getCounts();
			double prob = ((double)count.intValue())/TOTAL_NUM_EDUS.intValue();
			result.put(relation, prob);
		}

		return result;
	}

	


}
