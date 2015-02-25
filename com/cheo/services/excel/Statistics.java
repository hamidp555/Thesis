package com.cheo.services.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.junit.Assert;
import org.springframework.beans.factory.InitializingBean;

import com.cheo.base.enums.Annotators;
import com.cheo.base.enums.ClassLabel;
import com.cheo.model.Comment;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import edu.stanford.nlp.util.Pair;

public class Statistics implements InitializingBean {

	private ExcelService excelService;

	private TreeBasedTable<Integer,Integer,Comment> commentsFirstAnnotator ;

	private TreeBasedTable<Integer,Integer,Comment>  commentsSecondAnnotator ;

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		commentsFirstAnnotator = 
				excelService.readAllComments(Annotators.FIRST.getValue());
		commentsSecondAnnotator = 
				excelService.readAllComments(Annotators.SECOND.getValue());
	}

	//Comments HildaStatistics
	public Integer numComments(){
		return commentsFirstAnnotator.size();
	}

	public  Double avgLenghtCommentsByWords(){
		List<Pair<String, Integer>> commentsByLenght = new ArrayList<Pair<String, Integer>>();
		
		Iterator<Table.Cell<Integer, Integer, Comment>> iter = commentsFirstAnnotator.cellSet().iterator();
		while(iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();

			int count = 0;
			StringTokenizer st = new StringTokenizer(comment.getComment());
			while(st.hasMoreTokens()){
				st.nextToken();
				count++;
			}
			Pair<String, Integer> pair = new Pair<String, Integer>();
			pair.setFirst(comment.getComment());
			pair.setSecond(count);
			commentsByLenght.add(pair);
		}

		int lenght = 0;
		int numComments = 0;
		for(Pair<String, Integer> pair : commentsByLenght){
			lenght = lenght + pair.second;
			numComments++;
		}

		double avgLenght = lenght/numComments;
		return avgLenght;

	}

	
	//Discussion Title HildaStatistics
	public Integer numDiscussions(){
		Set<String> discussionTitles = new TreeSet<String>();
		Iterator<Table.Cell<Integer, Integer, Comment>> iter = commentsFirstAnnotator.cellSet().iterator();
		while(iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();
			discussionTitles.add(comment.getDiscussionTitle());
		}
		return discussionTitles.size();

	}

	public Double avgLenghtDiscussions(){
		List<Pair<String, Integer>> discussionTitleByLength = new ArrayList<Pair<String, Integer>>();

		Set<String> discussionTitles = new TreeSet<String>();

		Iterator<Table.Cell<Integer, Integer, Comment>> iter = commentsFirstAnnotator.cellSet().iterator();
		while(iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();
			discussionTitles.add(comment.getDiscussionTitle());
		}

		for(String discussionTitle: discussionTitles){
			int count = 0;
			StringTokenizer st = new StringTokenizer(discussionTitle);
			while(st.hasMoreTokens()){
				st.nextToken();
				count++;
			}
			Pair<String, Integer> pair = new Pair<String, Integer>();
			pair.setFirst(discussionTitle);
			pair.setSecond(count);
			discussionTitleByLength.add(pair);
		}

		int lenght = 0;
		int numDiscussionTitles = 0;
		for(Pair<String, Integer> pair : discussionTitleByLength){
			lenght = lenght + pair.second;
			numDiscussionTitles++;
		}

		double avgLenght = lenght/numDiscussionTitles;
		return avgLenght;

	}

	public double calculateCohenKappa(){
		int [][] confusionMatrix = new int[4][4];
		
		Collection<Comment> coll1 = commentsFirstAnnotator.values();
		Collection<Comment> coll2 = commentsSecondAnnotator.values();

		Assert.assertEquals(commentsFirstAnnotator.cellSet().size(), commentsSecondAnnotator.cellSet().size());
		int numComments = commentsFirstAnnotator.cellSet().size();

		List<Comment> entries1 = new ArrayList<Comment>(coll1);
		List<Comment> entries2 = new ArrayList<Comment>(coll2);

		
		int pp00 = 0;
		int pn01 = 0;
		int pm02 = 0;
		int pi03 = 0;
		
		int np10 = 0;
		int nn11 = 0;
		int nm12 = 0;
		int ni13 = 0;
		
		int mp20 = 0;
		int mn21 = 0;
		int mm22 = 0;
		int mi23 = 0;
		
		int ip30 = 0;
		int in31 = 0;
		int im32 = 0;
		int ii33 = 0;	
		
		//marginals
		int firstRowSum = 0;
		int secondRowSum = 0;
		int thirdRowSum = 0;
		int forthRowSum = 0;
		
		int firstColumnSum = 0;
		int secondColumnSum = 0;
		int thirdColumnSum = 0;
		int forthColumnSum = 0;
		
		int total = 0;
		
		for(int i=0; i<numComments ; i++){
			Comment com1 = entries1.get(i);
			Comment com2 = entries2.get(i);

			if(com1.isAnnotated() && com2.isAnnotated()){

				//first row
				if(com1.getPositive().equalsIgnoreCase("1") && com2.getPositive().equalsIgnoreCase("1"))
					pp00++;
				if(com1.getPositive().equalsIgnoreCase("1") && com2.getNegative().equalsIgnoreCase("1"))
					pn01++;
				if(com1.getPositive().equalsIgnoreCase("1") && com2.getMixed().equalsIgnoreCase("1"))
					pm02++;
				if(com1.getPositive().equalsIgnoreCase("1") && com2.getIrrelevant().equalsIgnoreCase("1"))
					pi03++;
				
				//second row
				if(com1.getNegative().equalsIgnoreCase("1") && com2.getPositive().equalsIgnoreCase("1"))
					np10++;
				if(com1.getNegative().equalsIgnoreCase("1") && com2.getNegative().equalsIgnoreCase("1"))
					nn11++;
				if(com1.getNegative().equalsIgnoreCase("1") && com2.getMixed().equalsIgnoreCase("1"))
					nm12++;
				if(com1.getNegative().equalsIgnoreCase("1") && com2.getIrrelevant().equalsIgnoreCase("1"))
					ni13++;
				
				//third row
				if(com1.getMixed().equalsIgnoreCase("1") && com2.getPositive().equalsIgnoreCase("1"))
					mp20++;
				if(com1.getMixed().equalsIgnoreCase("1") && com2.getNegative().equalsIgnoreCase("1"))
					mn21++;
				if(com1.getMixed().equalsIgnoreCase("1") && com2.getMixed().equalsIgnoreCase("1"))
					mm22++;
				if(com1.getMixed().equalsIgnoreCase("1") && com2.getIrrelevant().equalsIgnoreCase("1"))
					mi23++;
				
				//forth row
				if(com1.getIrrelevant().equalsIgnoreCase("1") && com2.getPositive().equalsIgnoreCase("1"))
					ip30++;
				if(com1.getIrrelevant().equalsIgnoreCase("1") && com2.getNegative().equalsIgnoreCase("1"))
					in31++;
				if(com1.getIrrelevant().equalsIgnoreCase("1") && com2.getMixed().equalsIgnoreCase("1"))
					im32++;		
				if(com1.getIrrelevant().equalsIgnoreCase("1") && com2.getIrrelevant().equalsIgnoreCase("1"))
					ii33++;
			}
		}
			
		confusionMatrix[0][0]=pp00;
		confusionMatrix[0][1]=pn01;
		confusionMatrix[0][2]=pm02;
		confusionMatrix[0][3]=pi03;
		
		confusionMatrix[1][0]=np10;
		confusionMatrix[1][1]=nn11;
		confusionMatrix[1][2]=nm12;
		confusionMatrix[1][3]=ni13;
		
		confusionMatrix[2][0]=mp20;
		confusionMatrix[2][1]=mn21;
		confusionMatrix[2][2]=mm22;
		confusionMatrix[2][3]=mi23;
		
		confusionMatrix[3][0]=ip30;
		confusionMatrix[3][1]=in31;
		confusionMatrix[3][2]=im32;
		confusionMatrix[3][3]=ii33;
		
		firstRowSum = confusionMatrix[0][0] + confusionMatrix[0][1] + confusionMatrix[0][2]+ confusionMatrix[0][3];
		secondRowSum = confusionMatrix[1][0] + confusionMatrix[1][1] + confusionMatrix[1][2]+ confusionMatrix[1][3];;
		thirdRowSum = confusionMatrix[2][0] + confusionMatrix[2][1] + confusionMatrix[2][2]+ confusionMatrix[2][3];;
		forthRowSum = confusionMatrix[3][0] + confusionMatrix[3][1] + confusionMatrix[3][2]+ confusionMatrix[3][3];;
		
		firstColumnSum = confusionMatrix[0][0] + confusionMatrix[1][0] + confusionMatrix[2][0] + confusionMatrix[3][0];
		secondColumnSum = confusionMatrix[0][1] + confusionMatrix[1][1] + confusionMatrix[2][1] + confusionMatrix[3][1];;
		thirdColumnSum = confusionMatrix[0][2] + confusionMatrix[1][2] + confusionMatrix[2][2] + confusionMatrix[3][2];
		forthColumnSum = confusionMatrix[0][3] + confusionMatrix[1][3] + confusionMatrix[2][3] + confusionMatrix[3][3];
		
		int totalRows = firstRowSum + secondRowSum + thirdRowSum + forthRowSum;
		int totalColumns = firstColumnSum + secondColumnSum + thirdColumnSum + forthColumnSum;
		Assert.assertEquals(totalRows, totalColumns);
		total = firstRowSum + secondRowSum + thirdRowSum + forthRowSum ;
		
		double probObservedAgreed = (double)(confusionMatrix[0][0] + confusionMatrix[1][1] + confusionMatrix[2][2] + confusionMatrix[3][3])/total;
		double probExpectedPositive = (double)((firstRowSum * firstColumnSum)/total)/total;
		double probExpectedNegaitive = (double)((secondRowSum * secondColumnSum)/total)/total;
		double probExpectedMixed = (double)((thirdRowSum * thirdColumnSum)/total)/total;
		double probExpectedIrrelevant = (double)((forthRowSum * forthColumnSum)/total)/total;
		
		double proExpected = probExpectedPositive + probExpectedNegaitive + probExpectedMixed + probExpectedIrrelevant;
		double kappa = ((double)(probObservedAgreed - proExpected)/(1-proExpected));
		return kappa;
	}

	public double calculateFleissKappa(ClassLabel classLabel){

		Assert.assertEquals("Two annotators should have rated the same number of comments.", 
				commentsFirstAnnotator.cellSet().size(), 
				commentsSecondAnnotator.cellSet().size());

		if(!classLabel.equals(ClassLabel.IRRELEVANT) &&
				!classLabel.equals(ClassLabel.MIX) &&
				!classLabel.equals(ClassLabel.POSITIVE) &&
				!classLabel.equals(ClassLabel.NEGATIVE)){
			throw new IllegalArgumentException();
		}

		int numComments = commentsFirstAnnotator.size();

		int numYesYes = 0;
		int numYesNo = 0;
		int numNoNo = 0;
		int numNoYes = 0;

		Collection<Comment> coll1 = commentsFirstAnnotator.values();
		Collection<Comment> coll2 = commentsSecondAnnotator.values();


		List<Comment> entries1 = new ArrayList<Comment>(coll1);
		List<Comment> entries2 = new ArrayList<Comment>(coll2);

		Assert.assertTrue(entries1.size() == entries2.size());
		Assert.assertTrue(entries1.size() == numComments);

		//calculating confusion matrix
		switch(classLabel.getValue()){
		case "positive":
			for(int i=0; i<numComments ; i++){
				Comment com1 = entries1.get(i);
				Comment com2 = entries2.get(i);

				if(com1.isAnnotated() && com2.isAnnotated()){
					if(com1.getPositive().equalsIgnoreCase("1") && 
							com2.getPositive().equalsIgnoreCase("1")){
						numYesYes++;
					}
					if(com1.getPositive().equalsIgnoreCase("1") && 
							!com2.getPositive().equalsIgnoreCase("1")){
						numYesNo++;
					}
					if(!com1.getPositive().equalsIgnoreCase("1") && 
							com2.getPositive().equalsIgnoreCase("1")){
						numNoYes++;
					}
					if(!com1.getPositive().equalsIgnoreCase("1") && 
							!com2.getPositive().equalsIgnoreCase("1")){
						numNoNo++;
					}
				}
			}
			break;
		case "negative":
			for(int i=0; i<numComments ; i++){
				Comment com1 = entries1.get(i);
				Comment com2 = entries2.get(i);

				if(com1.isAnnotated() && com2.isAnnotated()){
					if(com1.getNegative().equalsIgnoreCase("1") && 
							com2.getNegative().equalsIgnoreCase("1")){
						numYesYes++;
					}
					if(com1.getNegative().equalsIgnoreCase("1") && 
							!com2.getNegative().equalsIgnoreCase("1")){
						numYesNo++;
					}
					if(!com1.getNegative().equalsIgnoreCase("1") && 
							com2.getNegative().equalsIgnoreCase("1")){
						numNoYes++;
					}
					if(!com1.getNegative().equalsIgnoreCase("1") && 
							!com2.getNegative().equalsIgnoreCase("1")){
						numNoNo++;
					}
				}
			}
			break;
		case "mix":
			for(int i=0; i<numComments ; i++){
				Comment com1 = entries1.get(i);
				Comment com2 = entries2.get(i);

				if(com1.isAnnotated() && com2.isAnnotated()){
					if(com1.getMixed().equalsIgnoreCase("1") && 
							com2.getMixed().equalsIgnoreCase("1")){
						numYesYes++;
					}
					if(com1.getMixed().equalsIgnoreCase("1") && 
							!com2.getMixed().equalsIgnoreCase("1")){
						numYesNo++;
					}
					if(!com1.getMixed().equalsIgnoreCase("1") && 
							com2.getMixed().equalsIgnoreCase("1")){
						numNoYes++;
					}
					if(!com1.getMixed().equalsIgnoreCase("1") && 
							!com2.getMixed().equalsIgnoreCase("1")){
						numNoNo++;
					}
				}
			}
			break;
		case "irrelevant":
			for(int i=0; i<numComments ; i++){
				Comment com1 = entries1.get(i);
				Comment com2 = entries2.get(i);

				if(com1.isAnnotated() && com2.isAnnotated()){
					if(com1.getIrrelevant().equalsIgnoreCase("1") && 
							com2.getIrrelevant().equalsIgnoreCase("1")){
						numYesYes++;
					}
					if(com1.getIrrelevant().equalsIgnoreCase("1") && 
							!com2.getIrrelevant().equalsIgnoreCase("1")){
						numYesNo++;
					}
					if(!com1.getIrrelevant().equalsIgnoreCase("1") && 
							com2.getIrrelevant().equalsIgnoreCase("1")){
						numNoYes++;
					}
					if(!com1.getIrrelevant().equalsIgnoreCase("1") && 
							!com2.getIrrelevant().equalsIgnoreCase("1")){
						numNoNo++;
					}
				}
			}
			break;
		default:
			throw new IllegalArgumentException("invalid classLabel argument!");
		}

		//Observed proportion of the times the judges agreed
		double observedJudgesAgreed = (double)(numYesYes + numNoNo)/numComments;

		//calculating pooled marginals
		double nonRelevant = (double)(numNoYes + numNoNo + numYesNo + numNoNo)/(numComments + numComments);
		double relevant  = (double)(numNoYes + numYesYes + numYesNo + numYesYes)/(numComments + numComments);

		//calculating probability that the two judges agreed by chance
		double judgesAgreedByChance = Math.pow(nonRelevant, 2) + Math.pow(relevant, 2); 

		//calculating Kappa statistic
		double kappa = (observedJudgesAgreed - judgesAgreedByChance)/(1 - judgesAgreedByChance);

		return kappa;

	}


}
