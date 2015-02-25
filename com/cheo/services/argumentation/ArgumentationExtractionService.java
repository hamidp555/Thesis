package com.cheo.services.argumentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.cheo.base.EDUWrapper;
import com.cheo.base.enums.Stance;
import com.cheo.model.Argument;
import com.cheo.model.Comment;
import com.cheo.model.EDU;
import com.cheo.model.Topic;
import com.cheo.services.PreprocessorServiceEDUs;
import com.cheo.services.excel.ExcelService;
import com.cheo.services.excel.TableType;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class ArgumentationExtractionService {

	private TreeBasedTable<Integer, Integer, Comment> allComments = TreeBasedTable.create();

	private ExcelService excelService;

	private PreprocessorServiceEDUs preprocessor;

	public void setPreprocessor(PreprocessorServiceEDUs preprocessor) {
		this.preprocessor = preprocessor;
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	public List<Topic> getTopics(){	
		List<Topic> topics = new LinkedList<Topic>();
		Iterator<Table.Cell<Integer, Integer, Comment>> iter = allComments.cellSet().iterator();
		while(iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();
			for(Argument arg : comment.getArguments())
				topics.add(arg.getTopic());
		}
		return topics;
	}

	public List<Argument> processPro() throws Exception{
		List<Argument> arguments = getArguments();
		List<Argument> pro = new LinkedList<Argument>();
		for(Argument arg : arguments){
			if(arg.getStance().equals(Stance.PRO)){
				pro.add(arg);
			}
		}
		return pro;
	}

	public List<Argument> processNone() throws Exception{
		List<Argument> arguments = getArguments();
		List<Argument> none = new LinkedList<Argument>();
		for(Argument arg : arguments){
			if(arg.getStance().equals(Stance.NONE)){
				none.add(arg);
			}
		}
		return none;
	}

	public List<Argument> processCon() throws Exception{
		List<Argument> arguments = getArguments();
		List<Argument> con = new LinkedList<Argument>();
		for(Argument arg : arguments){
			if(arg.getStance().equals(Stance.CON)){
				con.add(arg);
			}
		}
		return con;
	}

	private void  addArgumets() throws Exception{

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:*.xls");
		for(Resource resource : resources){
			if(resource.getFilename().equalsIgnoreCase("GenomeSeqComments_1_v2_corrected_AA_eduLevel.xls")){
				TreeBasedTable<Integer, Integer, Comment> comments = excelService.read(1, TableType.edu, resource);
				allComments.putAll(comments);
				Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
				while(cmIter.hasNext()){
					Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
					Comment comment = cell.getValue();
					comment.addArguments();
				}
			}
			if(resource.getFilename().equalsIgnoreCase("GenomeSeqComments_2_v2_corrected_AA_eduLevel.xls")){
				TreeBasedTable<Integer, Integer, Comment> comments =excelService.read(2, TableType.edu, resource);
				allComments.putAll(comments);
				Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
				while(cmIter.hasNext()){
					Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
					Comment comment = cell.getValue();
					comment.addArguments();
				}
			}
			if(resource.getFilename().equalsIgnoreCase("GenomeSeqComments_mothering_v2_corrected_AA_eduLevel.xls")){
				TreeBasedTable<Integer, Integer, Comment> comments = excelService.read(3, TableType.edu, resource);
				allComments.putAll(comments);
				Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = comments.cellSet().iterator();
				while(cmIter.hasNext()){
					Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
					Comment comment = cell.getValue();
					comment.addArguments();
				}
			}
		}
	}

	private List<Argument> getArguments() throws Exception{
		addArgumets();

		List<Argument> arguments = new ArrayList<Argument>();
		Iterator<Table.Cell<Integer, Integer, Comment>> cmIter = allComments.cellSet().iterator();
		while(cmIter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = cmIter.next();
			Comment comment = cell.getValue();
			arguments.addAll(comment.getArguments());
		}
		return arguments;
	}

	@SuppressWarnings("unused")
	private List<String> getLemmas(List<Argument> argument) throws Exception{
		List<String> temp = new LinkedList<String>();
		for(Argument arg : argument){
			for(EDU edu: arg.getAfter()){
				EDUWrapper wrapper = (EDUWrapper)preprocessor.preprocess(edu.getComment());
				temp.add(wrapper.getFilteredEDUFromLemmas());
			}
			for(EDU edu: arg.getBefor()){
				EDUWrapper wrapper = (EDUWrapper)preprocessor.preprocess(edu.getComment());
				temp.add(wrapper.getFilteredEDUFromLemmas());
			}
			EDUWrapper wrapper = (EDUWrapper)preprocessor.preprocess(arg.getTopic().getEdu().getComment());
			temp.add(wrapper.getFilteredEDUFromLemmas());
		}
		return temp;
	}

	@SuppressWarnings("unused")
	private void print(List<String> list, String stance){
		System.out.println("++++++++++++++++" + stance + "++++++++++++++++");
		for(String elem : list){
			System.out.println(elem);
		}
	}

}
