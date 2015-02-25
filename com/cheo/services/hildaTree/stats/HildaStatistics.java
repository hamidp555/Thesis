 package com.cheo.services.hildaTree.stats;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cheo.services.hildaTree.BinaryTree;
import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.RSTRelation;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class HildaStatistics {
	
	private com.cheo.services.excel.Statistics excelStats;
	
	private TreeBuilder treeBuilder;
	
	public void setExcelStats(com.cheo.services.excel.Statistics excelStats) {
		this.excelStats = excelStats;
	}

	public void setTreeBuilder(TreeBuilder treeBuilder) {
		this.treeBuilder = treeBuilder;
	}

	public  Double avgLenghtCommentsByEdu() throws IOException{
		int numComments = excelStats.numComments();
		int totalNumEdus = StatsUtil.getTotalNumEdus();
		double avgLenght = totalNumEdus/numComments;
		return avgLenght;
	}
	
	public void getAllDiscourseStats() throws Exception{
		System.out.println("Average lenght of comments by EDU - " + avgLenghtCommentsByEdu());
		System.out.println("Total number of EDUS - " + StatsUtil.getTotalNumEdus());
		System.out.println("Total number of relations - " + StatsUtil.getTotalNumRelations());
		System.out.println("------Relations count -------");
		Map<RSTRelation, Integer> relations = StatsUtil.getTotalNumRelationsByRelation();
		for(Entry<RSTRelation, Integer> entry : relations.entrySet()){
			System.out.println(entry.getKey().toString() + " - " + entry.getValue());
		}
		System.out.println("------probabilty distribution for relations by EDU -------");
		Map<RSTRelation, Double> prob = StatsUtil.getProbabilityDistOfEdusByRelation();
		for(Entry<RSTRelation, Double> entry : prob.entrySet()){
			System.out.println(entry.getValue().toString() + " - " +  entry.getKey());
		}
	}
	
	public Map<String,  Collection<RelationWrapper>> getRelationstats(RSTRelation rstRel){

		ListMultimap<String, RelationWrapper> result = ArrayListMultimap.create();
		TreeBasedTable<Integer, Integer, BinaryTree<Element>> trees = treeBuilder.getDiscourseTrees();
		Iterator<Table.Cell<Integer, Integer, BinaryTree<Element>>> iter = trees.cellSet().iterator();
		
		while(iter.hasNext()){
			Table.Cell<Integer, Integer, BinaryTree<Element>> cell = iter.next();
			BinaryTree<Element> tree = cell.getValue();
			
			for(RelationWrapper relationWrapper : 
				getRelations(tree, rstRel)){
				result.put(relationWrapper.getWeight(), relationWrapper);
			}
		}
		return result.asMap();
	}


	private static List<RelationWrapper> getRelations(
			BinaryTree<Element> tree, RSTRelation rstRelation){

		List<RelationWrapper> result = new LinkedList<RelationWrapper>();

		Traverse traverse = new Traverse();
		traverse.traversePreorder(tree, tree.root(), "rstRelation");
		List<RelationWrapper> relations = traverse.getRelations();
		
		for(RelationWrapper rw : relations){
			if(rw.hasDirectEduChildren() && rw.hasRelation(rstRelation)){
				result.add(rw);
			}
		}

		return result;
	}

}
