package com.cheo.weka.services;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import weka.core.Instances;
import weka.filters.Filter;

import com.cheo.base.LocationBuilder;
import com.cheo.base.enums.FileType;
import com.cheo.weka.classifiers.ClassifierFactory;
import com.cheo.weka.classifiers.IClassifier;
import com.cheo.weka.classifiers.LibSVMClassifierWrapper;
import com.cheo.weka.classifiers.NaiveBayesMultinomialWrapper;
import com.cheo.weka.classifiers.SMOClassifierWrapper;
import com.cheo.weka.classifiers.VoteClassifierWrapper;
import com.cheo.weka.filters.SpreadSubsampleWrapper;
import com.cheo.weka.filters.StringToWordVectorNGramWrapper;
import com.cheo.weka.filters.WekaUtils;

public class EduClassifierForLastEduSentimentExtraction implements InitializingBean{

	private ClassifierConfig config;

	private ClassifierConfigReader configReader;
	
	private LocationBuilder locationBuilder;

	public void setLocationBuilder(LocationBuilder locationBuilder) {
		this.locationBuilder = locationBuilder;
	}

	public void setConfigReader(ClassifierConfigReader configReader) {
		this.configReader = configReader;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		config = configReader.read();
	}
	
	public void classify() throws Exception{

		Instances input =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVectors = StringToWordVectorNGramWrapper.apply(input);

		IClassifier classifierWrapper = null;
		Filter filter  = SpreadSubsampleWrapper.build(2.1);
		
		switch(config.getType()){
		case NB:
			classifierWrapper = ClassifierFactory.buildFilteredClassifier(
					new LibSVMClassifierWrapper().build(), 
					filter);
			break;
		case LIBSVM:
			classifierWrapper =  ClassifierFactory.buildFilteredClassifier(
					new LibSVMClassifierWrapper().build(), 
					filter);
			break;
		case SMO:
			classifierWrapper = ClassifierFactory.buildFilteredClassifier(
					new SMOClassifierWrapper().build(),
					filter);
			break;
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build() );
			voteWrapper.add(new NaiveBayesMultinomialWrapper().build());
			classifierWrapper = voteWrapper;
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}

		Assert.notNull(classifierWrapper, "Classifier is NULL!");

		classifierWrapper.classifyAndEvaluate(wordVectors, "2,3,4");	
		classifierWrapper.saveModel(locationBuilder.buildPath(FileType.MODEL));
		classifierWrapper.saveResult(locationBuilder.buildPath(FileType.EVAL));
	}
	
}
