package com.cheo.weka.classifiers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cheo.base.enums.ClassifierType;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.core.Instances;
import weka.filters.Filter;

public class ClassifierFactory {


	public static Classifier build(ClassifierType type) throws Exception{
		switch(type){
		case NB_COMPLEMENT:
			throw new Exception("Naive Bayes Complement is not supported.");
		case NB_MULTI_DIM:
			return new NaiveBayesMultinomialWrapper().build();
		case LIBSVM:
			return new LibSVMClassifierWrapper().build();
		case SMO:
			return new SMOClassifierWrapper().build();
		case VOTE:
			throw new Exception("Vote is not supported.");
		default:
			break;
		}
		throw new Exception("Classifeir type is not supported.");
	}

	public static Classifier buildAttributeSelectClassifeir(
			Classifier classifier) throws Exception{

		InfoGainAttributeEval infoGain = new InfoGainAttributeEval();
		infoGain.setBinarizeNumericAttributes(false);
		infoGain.setMissingMerge(true);

		Ranker ranker = new Ranker();
		ranker.setNumToSelect(-1);
		ranker.setStartSet(StringUtils.EMPTY);
		ranker.setThreshold(0);
		ranker.setGenerateRanking(true);

		AttributeSelectedClassifier attrSelectedClassifier = new AttributeSelectedClassifier();
		attrSelectedClassifier.setClassifier(classifier);
		attrSelectedClassifier.setEvaluator(infoGain);
		attrSelectedClassifier.setSearch(ranker);

		return attrSelectedClassifier;
	}
			

	public static IClassifier buildFilteredClassifier(
			Classifier classifier, Filter filter) throws Exception{

		FilteredClassifierWrapper filteredClassifierWarpper = new FilteredClassifierWrapper();
		filteredClassifierWarpper.addClassifier(classifier);
		filteredClassifierWarpper.addFilter(filter);
		return filteredClassifierWarpper;
	}

	public static Classifier buildVoteClassifier(
			List<Classifier> classifiers, Instances dataset) throws Exception{
		return null;

	}
}
