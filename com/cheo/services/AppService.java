package com.cheo.services;

import java.util.List;
import java.util.Map;

import weka.core.Instances;

import com.cheo.base.FeatureSelectionResultLocBuilder;
import com.cheo.base.LocationBuilder;
import com.cheo.base.enums.Annotators;
import com.cheo.base.enums.ClassLabel;
import com.cheo.base.enums.ClassifierType;
import com.cheo.base.enums.FileType;
import com.cheo.model.Comment;
import com.cheo.model.repositories.CommentRepository;
import com.cheo.services.arff.ArffUtils;
import com.cheo.services.excel.Statistics;
import com.cheo.services.featureSelection.BiNormalSeperation;
import com.cheo.weka.services.WekaClassifierService;
import com.google.common.collect.TreeBasedTable;

public class AppService {

	private ServiceRegistery serviceRegistery;

	private CommentRepository repository;

	private WekaClassifierService wekaClassifier;
	
	private LocationBuilder arffLocationBuilder;

	private FeatureSelectionResultLocBuilder featureSelectionResultLocBuilder;

	private BiNormalSeperation bnsService;

	private SpellChekerService spellCheckingService;

	private DiscourseParserService discourseParser;

	private Statistics statistics;

	public void setDiscourseParser(DiscourseParserService discourseParser) {
		this.discourseParser = discourseParser;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public void setSpellCheckingService(SpellChekerService spellCheckingService) {
		this.spellCheckingService = spellCheckingService;
	}

	public void setFeatureSelectionResultLocBuilder(
			FeatureSelectionResultLocBuilder featureSelectionResultLocBuilder) {
		this.featureSelectionResultLocBuilder = featureSelectionResultLocBuilder;
	}

	public void setBnsService(BiNormalSeperation bnsService) {
		this.bnsService = bnsService;
	}

	public void setArffLocationBuilder(LocationBuilder arffLocationBuilder) {
		this.arffLocationBuilder = arffLocationBuilder;
	}

	public void setServiceRegistery(ServiceRegistery serviceRegistery) {
		this.serviceRegistery = serviceRegistery;
	}

	public void setRepository(CommentRepository repository) {
		this.repository = repository;
	}

	public void setWekaClassifier(WekaClassifierService wekaClassifier) {
		this.wekaClassifier = wekaClassifier;
	}

	/**
	 * 
	 * @param includeEFs - If true, all extra features will be included
	 * @throws Exception
	 */
	public  void createArffFromComments() throws Exception{
		TreeBasedTable<Integer, Integer, Comment> comm = 
				repository.getCommentsFiltered(Annotators.FIRST.getValue());

		Instances dataset = serviceRegistery.getArffService().createArff(comm);
		ArffUtils.saveArff(dataset, arffLocationBuilder.buildPath(FileType.ARFF));
		ArffUtils.saveText(dataset, arffLocationBuilder.buildPath(FileType.TXT));
	}

	/**
	 * Classification includes feature selection and over sampling (smooth)<br>
	 * ClassifierType includes:
	 * <ul>
	 * <li>Naive Bayes</li>
	 * <li>SMO</li>
	 * <li>LibSvm</li>
	 * <li>VOTE</li>
	 * </ul>
	 * @param includeEFs - If true, all extra features will be included
	 * @throws Exception
	 */
	public void classifyCommentTwoLevelFirstClassifier(boolean includeEFs, ClassifierType classifierType) throws Exception{
		wekaClassifier.classifyCommentTwoLevelFirstClassifier();
	}

	/**
	 * Classification includes feature selection and over sampling (smooth)<br>
	 * ClassifierType includes:
	 * <ul>
	 * <li>Naive Bayes</li>
	 * <li>SMO</li>
	 * <li>LibSvm</li>
	 * <li>VOTE</li>
	 * </ul>
	 * @param includeEFs - If true, all extra features will be included
	 * @throws Exception
	 */
	public void classifyCommentTwoLevel(boolean includeEFs, ClassifierType classifierType) throws Exception{
		wekaClassifier.classifyCommentTwoLevel();
	}

	/**
	 * Classification includes feature selection and over sampling (smooth)<br>
	 * ClassifierType includes:
	 * <ul>
	 * <li>Naive Bayes</li>
	 * <li>SMO</li>
	 * <li>LibSvm</li>
	 * <li>VOTE</li>
	 * </ul>
	 * @param includeEFs - If true, all extra features will be included
	 * @throws Exception
	 */
	public void classifyCommentTwoLevelActual(boolean includeEFs, ClassifierType classifierType) throws Exception{
		wekaClassifier.classifyCommentTwoLevelActual();
	}

	/**
	 * Classification includes feature selection and over sampling (smooth)
	 * @param includeEFs - If true, all extra features will be included
	 * @throws Exception
	 */
	public void classifyCommentFlat(boolean includeEFs, ClassifierType classifierType) throws Exception{
		wekaClassifier.classifyCommentFlat();
	}

	/**
	 * 
	 * @param includeEFs - If true, all extra features will be included
	 * @throws Exception
	 */
	public void performFeatureSelection(boolean includeEFs) throws Exception{
		bnsService.performFeatureSelectionAndSave(
				arffLocationBuilder.buildPath(FileType.ARFF), 
				featureSelectionResultLocBuilder.buildFeatureSelectionFilePathByConfig());
	}

	public String spellCheckComment(String comment) throws Exception{
		return spellCheckingService.spellCheckComment(comment);
	}

	public String spellCheckWord(String word) throws Exception{
		return spellCheckingService.spellCheckWord(word);
	}
	
	//TODO move to junit stats

	public Integer numComments(){
		return statistics.numComments();
	}

	public Integer numDiscussions(){
		return statistics.numDiscussions();
	}

	public Double avgLenghtCommentsByWords(){
		return statistics.avgLenghtCommentsByWords();
	}

	public Double avgLenghtDiscussions(){
		return statistics.avgLenghtDiscussions();
	}

	/**
	 * 
	 * @return All Discourse Elementary Units as a list of EDUs for each comment
	 * @throws Exception
	 */
	public Map<Integer, List<String>> getAllEDUS() throws Exception{
		return discourseParser.getEDUs();
	}

	/**
	 * 
	 * @param classLabel
	 * @return fleiss kappa statistics calculated for each class label
	 */
	public double calculateFleissKappa(ClassLabel classLabel){
		return statistics.calculateFleissKappa(classLabel);
	}

	/**
	 * 
	 * @return  kappa statistics calculated
	 */
	public double calculateKappa(){
		return statistics.calculateCohenKappa();
	}

}
