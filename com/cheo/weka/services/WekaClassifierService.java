package com.cheo.weka.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.cheo.base.LocationBuilder;
import com.cheo.base.enums.FileType;
import com.cheo.services.arff.ArffUtils;
import com.cheo.weka.classifiers.AttributeClassifierWrapper;
import com.cheo.weka.classifiers.FilteredClassifierWrapper;
import com.cheo.weka.classifiers.IClassifier;
import com.cheo.weka.classifiers.LibSVMClassifierWrapper;
import com.cheo.weka.classifiers.NaiveBayesMultinomialWrapper;
import com.cheo.weka.classifiers.NaiveBayesWrapper;
import com.cheo.weka.classifiers.SMOClassifierWrapper;
import com.cheo.weka.classifiers.VoteClassifierWrapper;
import com.cheo.weka.classifiers.ZeroRClassifierWrapper;
import com.cheo.weka.filters.AddNewClassLabelFilter;
import com.cheo.weka.filters.RemoveFilterWrapper;
import com.cheo.weka.filters.RemoveMisclassifiedFilterWrapper;
import com.cheo.weka.filters.RemoveWithValuesFilterWrapper;
import com.cheo.weka.filters.StringToWordVectorNGramWrapper;
import com.cheo.weka.filters.WekaUtils;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;


public class WekaClassifierService implements InitializingBean{

	private ClassifierConfig config;

	private ClassifierConfigReader configReader;

	private LocationBuilder locationBuilder;

	public void setConfigReader(ClassifierConfigReader configReader) {
		this.configReader = configReader;
	}

	public void setLocationBuilder(LocationBuilder locationBuilder) {
		this.locationBuilder = locationBuilder;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		config = configReader.read();
	}

	/**
	 * This method runs the first of two classifiers used in two level classification
	 * @param arffLocation
	 * @param classifierType
	 * @throws Exception
	 */
	public void classifyCommentTwoLevelFirstClassifier() throws Exception{

		train_comment_classifier_firstOfTwoLevel_fs();
	}

	public void classifyCommentTwoLevelIrrelEDusRemoved() throws Exception{
		
		switch(config.getType()){
		case NB:
			tarin_comment_classifier_twoLevel_irrelEDusRemoved( 
					new NaiveBayesWrapper(), 
					builbClassifier_fs(new NaiveBayesWrapper().build()));
			break;
		case LIBSVM:
			tarin_comment_classifier_twoLevel_irrelEDusRemoved( 
					new NaiveBayesWrapper(),
					builbClassifier_fs(new LibSVMClassifierWrapper().build()));
			break;
		case SMO:
			tarin_comment_classifier_twoLevel_irrelEDusRemoved( 
					new SMOClassifierWrapper(),
					builbClassifier_fs(new SMOClassifierWrapper().build()));
			break;
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build());
			voteWrapper.add(new NaiveBayesWrapper().build());

			VoteClassifierWrapper voteWrapper2 = new VoteClassifierWrapper();  
			voteWrapper2.add(new SMOClassifierWrapper().build());
			voteWrapper2.add(new LibSVMClassifierWrapper().build() );
			voteWrapper2.add(new NaiveBayesWrapper().build());

			tarin_comment_classifier_twoLevel_irrelEDusRemoved( 
					voteWrapper, 
					builbClassifier_fs(voteWrapper2.build()));
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}
	}
	
	public void classifyCommentTwoLevelSecondClassifier(boolean convertedToWordVetors) throws Exception{
		
		switch(config.getType()){
		case ZEROR:
			tarin_comment_classifier_twoLevel_secondClassifier(
					new ZeroRClassifierWrapper(), convertedToWordVetors);
			break;
		case NB:
			tarin_comment_classifier_twoLevel_secondClassifier(
					new NaiveBayesWrapper(), convertedToWordVetors);
			break;
		case LIBSVM:
			tarin_comment_classifier_twoLevel_secondClassifier(
					new LibSVMClassifierWrapper(), convertedToWordVetors);
			break;
		case SMO:
			tarin_comment_classifier_twoLevel_secondClassifier(
					new SMOClassifierWrapper(), convertedToWordVetors);
			break;
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build());
			voteWrapper.add(new NaiveBayesWrapper().build());
			tarin_comment_classifier_twoLevel_secondClassifier(
					voteWrapper, convertedToWordVetors);
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}
	}

	//First classifier is always VOTE since it achieved the best results in relevant/irrelevant classification
	public void classifyCommentTwoLevel() throws Exception{

		VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
		voteWrapper.add(new SMOClassifierWrapper().build());
		voteWrapper.add(new LibSVMClassifierWrapper().build());
		voteWrapper.add(new NaiveBayesWrapper().build());
		SMOClassifierWrapper smo = new SMOClassifierWrapper();
		
		switch(config.getType()){
		case NB:
			tarin_comment_classifier_twoLevel( 
					smo, 
					new NaiveBayesWrapper());
			break;
		case LIBSVM:
			tarin_comment_classifier_twoLevel( 
					smo,
					new LibSVMClassifierWrapper());
			break;
		case SMO:
			tarin_comment_classifier_twoLevel( 
					smo,
					new SMOClassifierWrapper());
			break;
		case VOTE:
			tarin_comment_classifier_twoLevel( 
					smo,
					voteWrapper);
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}
	}

	public void classifyEDUTwoLevel() throws Exception{

		switch(config.getType()){
		case NB:
			tarin_edu_classifier_twoLevel( 
					new NaiveBayesWrapper(), 
					builbClassifier_fs(new NaiveBayesWrapper().build()));
			break;
		case LIBSVM:
			tarin_edu_classifier_twoLevel( 
					new NaiveBayesWrapper(),
					builbClassifier_fs(new LibSVMClassifierWrapper().build()));
			break;
		case SMO:
			tarin_edu_classifier_twoLevel( 
					new SMOClassifierWrapper(),
					builbClassifier_fs(new SMOClassifierWrapper().build()));
			break;
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build());
			voteWrapper.add(new NaiveBayesWrapper().build());

			VoteClassifierWrapper voteWrapper2 = new VoteClassifierWrapper();  
			voteWrapper2.add(new SMOClassifierWrapper().build());
			voteWrapper2.add(new LibSVMClassifierWrapper().build() );
			voteWrapper2.add(new NaiveBayesWrapper().build());

			tarin_edu_classifier_twoLevel( 
					voteWrapper, 
					builbClassifier_fs(voteWrapper2.build()));
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}
	}
	
	public void saveArffCommentAfterIrrelevantRemoved(boolean convertedToWordVectors) throws Exception{
		
		Instances data =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVectors = convertedToWordVectors ? data : StringToWordVectorNGramWrapper.apply(data);
		
		//irrelevant instances removed
		Instances irrelevantRemoved = RemoveWithValuesFilterWrapper.
				apply(wordVectors, "first", "2");

		//old classLabels (relevant/irrelevant) removed and new ones (positive/negative/mix) 
		//added based on priorClass
		Instances newClassLabelAdded = AddNewClassLabelFilter.
				applyForComment(irrelevantRemoved ,"priorClass");

		//priorClass attribute removed
		Attribute priorClass =  newClassLabelAdded.attribute("priorClass");
		String indexToRemove = String.valueOf(priorClass.index() + 1);
		Instances priprClassRemoved = RemoveFilterWrapper.apply(newClassLabelAdded, indexToRemove);

		ArffUtils.saveArff(priprClassRemoved, locationBuilder.buildPath(FileType.ARFF) + ".arff");
		ArffUtils.saveText(priprClassRemoved, locationBuilder.buildPath(FileType.TXT) + ".arff");
	}
	
	public void classifyEDUTwoLevelSecondClassifier(boolean convertedToWordVectors) throws Exception{
	
		switch(config.getType()){
		case ZEROR:
			tarin_edu_second_classifier_twoLevel(
					new ZeroRClassifierWrapper(), convertedToWordVectors);
			break;	
		case NB_MULTI_DIM:
			tarin_edu_second_classifier_twoLevel(
					new NaiveBayesMultinomialWrapper(), convertedToWordVectors);
			break;
		case NB:
			tarin_edu_second_classifier_twoLevel(
					new NaiveBayesWrapper(), convertedToWordVectors);
			break;
		case LIBSVM:
			tarin_edu_second_classifier_twoLevel(
					new LibSVMClassifierWrapper(), convertedToWordVectors);
			break;
		case SMO:
			tarin_edu_second_classifier_twoLevel(
					new SMOClassifierWrapper(), convertedToWordVectors);
			break; 
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build());
			voteWrapper.add(new NaiveBayesMultinomialWrapper().build());
			tarin_edu_second_classifier_twoLevel(
					voteWrapper, convertedToWordVectors);
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}
		
	}
	
	public void classifyCommentTwoLevelActual() throws Exception{

		switch(config.getType()){
		case NB:
			train_comment_classifier_actual_twoLevel(
					builbClassifier_fs(new NaiveBayesWrapper().build()));
			break;
		case LIBSVM:
			train_comment_classifier_actual_twoLevel(
					builbClassifier_fs(new LibSVMClassifierWrapper().build()));
			break;
		case SMO:
			train_comment_classifier_actual_twoLevel(
					builbClassifier_fs(new SMOClassifierWrapper().build()));
			break;
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build() );
			voteWrapper.add(new NaiveBayesWrapper().build());

			train_comment_classifier_actual_twoLevel(
					builbClassifier_fs(voteWrapper.build()));
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}
	}

	/**
	 * Classification is done with feature selection and over sampling (smooth)<br>
	 * This method supports the following classifiers:
	 * <ul>
	 * <li>naive bayes</li>
	 * <li>libsvm</li>
	 * <li>smo</li>
	 * <li>vote</li>
	 * </ul>
	 * @param arffLocation
	 * @param classifierType
	 * @throws Exception
	 */
	public void classifyFlat_comment_fs_os() throws Exception{

		train_comment_classifier_Flat_fs_os();
	}
	
	
	public void classifyCommentFlat() throws Exception{

		train_comment_classifier_Flat();
	}

	private void train_comment_classifier_Flat() throws Exception{

		Instances input =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVectors = StringToWordVectorNGramWrapper.apply(input);

		IClassifier classifierWrapper = null;

		switch(config.getType()){
		case NB:
			classifierWrapper = new NaiveBayesWrapper();
			break;
		case LIBSVM:
			classifierWrapper =  new LibSVMClassifierWrapper();
			break;
		case SMO:
			classifierWrapper = new SMOClassifierWrapper();
			break;
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build() );
			voteWrapper.add(new NaiveBayesWrapper().build());
			classifierWrapper = voteWrapper;
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}

		Assert.notNull(classifierWrapper, "Classifier is NULL!");

		classifierWrapper.classifyAndEvaluate(wordVectors, StringUtils.EMPTY);	
		classifierWrapper.saveModel(locationBuilder.buildPath(FileType.MODEL));
		classifierWrapper.saveResult(locationBuilder.buildPath(FileType.EVAL));
	}

	//Helper methods
	private void train_comment_classifier_Flat_fs_os() throws Exception{

		Instances input =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVectors = StringToWordVectorNGramWrapper.apply(input);

		IClassifier classifierWrapper = null;

		switch(config.getType()){
		case NB:
			classifierWrapper = builbClassifier_fs(new NaiveBayesWrapper().build());
			break;
		case LIBSVM:
			classifierWrapper =  builbClassifier_fs(new LibSVMClassifierWrapper().build());
			break;
		case SMO:
			classifierWrapper = builbClassifier_fs(new SMOClassifierWrapper().build());
			break;
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build() );
			voteWrapper.add(new NaiveBayesWrapper().build());
			classifierWrapper = builbClassifier_fs(voteWrapper.build());
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}

		Assert.notNull(classifierWrapper, "Classifier is NULL!");

		classifierWrapper.classifyAndEvaluate(wordVectors, StringUtils.EMPTY);	
		classifierWrapper.saveModel(locationBuilder.buildPath(FileType.MODEL));
		classifierWrapper.saveResult(locationBuilder.buildPath(FileType.EVAL));
	}

	/**
	 * Classification is done with feature selection and over sampling (smooth)<br>
	 * This method supports the following classifiers:
	 * <ul>
	 * <li>naive bayes</li>
	 * <li>libsvm</li>
	 * <li>smo</li>
	 * <li>vote</li>
	 * </ul>
	 * @param firstArffLocation
	 * @param classifierType
	 * @throws Exception
	 */
	private void train_comment_classifier_firstOfTwoLevel_fs()
			throws Exception{

		Instances input =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVectors = StringToWordVectorNGramWrapper.apply(input);

		//categoryLabel attribute removed
		Instances categoryLabelRemoved = RemoveFilterWrapper.apply(wordVectors, "2");

		AttributeClassifierWrapper classifierWrapper = new AttributeClassifierWrapper();

		switch(config.getType()){
		case NB:
			classifierWrapper.add(new NaiveBayesWrapper().build());
			break;
		case LIBSVM:
			classifierWrapper.add(new LibSVMClassifierWrapper().build());
			break;
		case SMO:
			classifierWrapper.add(new SMOClassifierWrapper().build());
			break;
		case VOTE:
			VoteClassifierWrapper voteWrapper = new VoteClassifierWrapper();  
			voteWrapper.add(new SMOClassifierWrapper().build());
			voteWrapper.add(new LibSVMClassifierWrapper().build() );
			voteWrapper.add(new NaiveBayesWrapper().build());

			classifierWrapper.add(voteWrapper.build());
			break;
		default:
			throw new IllegalArgumentException("Invalid classifeir type!");
		}

		classifierWrapper.classifyAndEvaluate(categoryLabelRemoved, StringUtils.EMPTY);
		classifierWrapper.saveModel(locationBuilder.buildPath(FileType.MODEL));
		classifierWrapper.saveResult(locationBuilder.buildPath(FileType.EVAL));

	}
	
	public void saveArffEDUAfterIrrelevantRemoved(boolean convertedToWordVectors) throws Exception{

		Instances data =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVectors = convertedToWordVectors ? data : StringToWordVectorNGramWrapper.apply(data);
		
		//irrelevant instances removed
		Instances irrelevantRemoved = RemoveWithValuesFilterWrapper.
				apply(wordVectors, "first", "2");

		//old classLabels (relevant/irrelevant) removed and new ones (positive/negative/mix) 
		//added based on priorClass
		Instances newClassLabelAdded = AddNewClassLabelFilter.
				applyForEDU(irrelevantRemoved ,"priorClass");

		//priorClass attribute removed
		Attribute priorClass =  newClassLabelAdded.attribute("priorClass");
		String indexToRemove = String.valueOf(priorClass.index() + 1);
		Instances priprClassRemoved = RemoveFilterWrapper.apply(newClassLabelAdded, indexToRemove);
		
		ArffUtils.saveArff(priprClassRemoved, locationBuilder.buildPath(FileType.ARFF) + ".arff");
		ArffUtils.saveText(priprClassRemoved, locationBuilder.buildPath(FileType.TXT) + ".arff");
	}


	private void train_comment_classifier_actual_twoLevel(IClassifier classifierWrapper)
			throws Exception{

		Instances input =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVectors = StringToWordVectorNGramWrapper.apply(input);

		//Remove all irrelevant instances
		Instances irrelevantRemoved = 
				RemoveWithValuesFilterWrapper.apply(wordVectors, "first", "2");

		//old classLabel (relevant/irrelevant) removed and new one (positive/negative/mix) added
		Instances newClassLabelAdded = 
				AddNewClassLabelFilter.applyForComment(irrelevantRemoved ,"priorClass");

		//priorClass attribute removed
		Attribute priprClass =  newClassLabelAdded.attribute("priorClass");
		String indexToRemove = String.valueOf(priprClass.index() + 1);
		Instances categoryLabelRemoved = RemoveFilterWrapper.apply(newClassLabelAdded, indexToRemove);

		classifierWrapper.classifyAndEvaluate(categoryLabelRemoved, StringUtils.EMPTY);
		classifierWrapper.saveModel(locationBuilder.buildPath(FileType.MODEL));
		classifierWrapper.saveResult(locationBuilder.buildPath(FileType.EVAL));

	}

	private void tarin_edu_second_classifier_twoLevel(
			IClassifier classifier, boolean convertedToWordVectors) throws Exception{
		
		Instances data =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVectors = convertedToWordVectors ? data : StringToWordVectorNGramWrapper.apply(data);
		
		//irrelevant instances removed
		Instances irrelevantRemoved = RemoveWithValuesFilterWrapper.
				apply(wordVectors, "first", "2");

		//old classLabels (relevant/irrelevant) removed and new ones (positive/negative/mix) 
		//added based on priorClass
		Instances newClassLabelAdded = AddNewClassLabelFilter.
				applyForEDU(irrelevantRemoved ,"priorClass");

		//priorClass attribute removed
		Attribute priorClass =  newClassLabelAdded.attribute("priorClass");
		String indexToRemove = String.valueOf(priorClass.index() + 1);
		Instances priprClassRemoved = RemoveFilterWrapper.apply(newClassLabelAdded, indexToRemove);

		classifier.classifyAndEvaluate(priprClassRemoved, StringUtils.EMPTY);
		classifier.saveModel(locationBuilder.buildPath(FileType.MODEL));
		classifier.saveResult(locationBuilder.buildPath(FileType.EVAL));
	}
	
	private void tarin_edu_classifier_twoLevel(IClassifier firstClassifier, 
			IClassifier secondClassifier) throws Exception{

		String path  =locationBuilder.buildPath(FileType.ARFF);
		Instances input =  WekaUtils.getInstances(path);
		Instances wordVector = StringToWordVectorNGramWrapper.apply(input);

		//classified to relevant/irrelevant and missClassified instances removed
		Instances misClassifiedRemoved = RemoveMisclassifiedFilterWrapper.
				apply(firstClassifier.build(), wordVector);

		//irrelevant instances removed
		Instances irrelevantRemoved = RemoveWithValuesFilterWrapper.
				apply(misClassifiedRemoved, "first", "2");

		//old classLabel (relevant/irrelevant) removed and new one (positive/negative/mix) added
		Instances newClassLabelAdded = AddNewClassLabelFilter.
				applyForEDU(irrelevantRemoved ,"priorClass");

		//priorClass attribute removed
		Attribute priprClass =  newClassLabelAdded.attribute("priorClass");
		String indexToRemove = String.valueOf(priprClass.index() + 1);
		Instances priorClassRemoved = RemoveFilterWrapper.apply(newClassLabelAdded, indexToRemove);

		secondClassifier.classifyAndEvaluate(priorClassRemoved, StringUtils.EMPTY);
		secondClassifier.saveModel(locationBuilder.buildPath(FileType.MODEL));
		secondClassifier.saveResult(locationBuilder.buildPath(FileType.EVAL));

	}

	/**
	 * 
	 * @param firstArffLocation
	 * @param firstClassifier
	 * @param secondClassifier
	 * @throws Exception
	 * <ul>
	 * Here is the workflow for this classifier
	 * <li>arff file contains categoryLabel attribute that contains positive/negative/mix label for each comment</li>
	 * <li>classified to relevant/irrelevant</li>
	 * <li>missClassified instances removed</li>
	 * <li>irrelevant instances removed</li>
	 * <li>old classLabel (relevant/irrelevant) removed and new one positive/negative/mix added</li>
	 * <li>categoryLabel attribute removed</li>
	 * <li>classify positive/negative/mix fom the result of first clasifier</li>
	 * </ul>
	 */
	private void tarin_comment_classifier_twoLevel(IClassifier firstClassifier, 
			IClassifier secondClassifier) throws Exception{

		Instances input =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVector = StringToWordVectorNGramWrapper.apply(input);

		//Filter priorClass attribute when classifying to relevant/irrelevant
		//AttributeClassifierWrapper attrSelectedClassifierWrapper =  new AttributeClassifierWrapper();
		//attrSelectedClassifierWrapper.add(firstClassifier.build());
		//AttributeSelectedClassifier attrSelectedClassifier = attrSelectedClassifierWrapper.build();

		Attribute priorClass =  wordVector.attribute("priorClass");
		String indexToRemove = String.valueOf(priorClass.index() + 1);
		Remove removeFilter = RemoveFilterWrapper.build(indexToRemove);

		FilteredClassifierWrapper filteredClassifier = new FilteredClassifierWrapper(); 
		filteredClassifier.addFilter(removeFilter);
		filteredClassifier.addClassifier(firstClassifier.build());

		//classified to relevant/irrelevant and missClassified instances removed
		Instances misClassifiedRemoved = RemoveMisclassifiedFilterWrapper.
				apply(filteredClassifier.build(), wordVector);

		//irrelevant instances removed
		Instances irrelevantRemoved = RemoveWithValuesFilterWrapper.
				apply(misClassifiedRemoved, "first", "2");

		//old classLabel (relevant/irrelevant) removed and new one (positive/negative/mix) added
		Instances newClassLabelAdded = AddNewClassLabelFilter.
				applyForComment(irrelevantRemoved ,"priorClass");

		//priorClass attribute removed
		priorClass =  newClassLabelAdded.attribute("priorClass");
		indexToRemove = String.valueOf(priorClass.index() + 1);
		Instances priprClassRemoved = RemoveFilterWrapper.apply(newClassLabelAdded, indexToRemove);

		secondClassifier.classifyAndEvaluate(priprClassRemoved, StringUtils.EMPTY);
		secondClassifier.saveModel(locationBuilder.buildPath(FileType.MODEL));
		secondClassifier.saveResult(locationBuilder.buildPath(FileType.EVAL));
	}

	private void tarin_comment_classifier_twoLevel_secondClassifier(IClassifier classifier, boolean convertedToWordVectors) throws Exception{

		Instances data =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));
		
		Instances wordVectors = convertedToWordVectors ? data : StringToWordVectorNGramWrapper.apply(data);
		
		//irrelevant instances removed
		Instances irrelevantRemoved = RemoveWithValuesFilterWrapper.
				apply(wordVectors, "first", "2");

		//old classLabels (relevant/irrelevant) removed and new ones (positive/negative/mix) 
		//added based on priorClass
		Instances newClassLabelAdded = AddNewClassLabelFilter.
				applyForComment(irrelevantRemoved ,"priorClass");

		//priorClass attribute removed
		Attribute priorClass =  newClassLabelAdded.attribute("priorClass");
		String indexToRemove = String.valueOf(priorClass.index() + 1);
		Instances priprClassRemoved = RemoveFilterWrapper.apply(newClassLabelAdded, indexToRemove);

		classifier.classifyAndEvaluate(priprClassRemoved, StringUtils.EMPTY);
		classifier.saveModel(locationBuilder.buildPath(FileType.MODEL));
		classifier.saveResult(locationBuilder.buildPath(FileType.EVAL));
	}
	
	private void tarin_comment_classifier_twoLevel_irrelEDusRemoved(IClassifier firstClassifier, 
			IClassifier secondClassifier) throws Exception{

		Instances input =  
				WekaUtils.getInstances(locationBuilder.buildPath(FileType.ARFF));

		Instances wordVector = StringToWordVectorNGramWrapper.apply(input);

		Attribute priprClass =  wordVector.attribute("priorClass");
		String indexToRemove = String.valueOf(priprClass.index() + 1);
		//Remove removeFilter = RemoveFilterWrapper.build(indexToRemove);

		//FilteredClassifierWrapper filteredClassifier = new FilteredClassifierWrapper(); 
		//filteredClassifier.addFilter(removeFilter);
		//filteredClassifier.addClassifier(firstClassifier.build());

		//classified to relevant/irrelevant and missClassified instances removed
		Instances misClassifiedRemoved = RemoveMisclassifiedFilterWrapper.
				apply(firstClassifier.build(), wordVector);

		//irrelevant instances removed
		Instances irrelevantRemoved = RemoveWithValuesFilterWrapper.
				apply(misClassifiedRemoved, "first", "2");

		//old classLabel (relevant/irrelevant) removed and new one (positive/negative/mix) added
		Instances newClassLabelAdded = AddNewClassLabelFilter.
				applyForComment(irrelevantRemoved ,"priorClass");

		//priorClass attribute removed
		priprClass =  newClassLabelAdded.attribute("priorClass");
		indexToRemove = String.valueOf(priprClass.index() + 1);
		Instances categoryLabelRemoved = RemoveFilterWrapper.apply(newClassLabelAdded, indexToRemove);

		secondClassifier.classifyAndEvaluate(categoryLabelRemoved, StringUtils.EMPTY);
		secondClassifier.saveModel(locationBuilder.buildPath(FileType.MODEL));
		secondClassifier.saveResult(locationBuilder.buildPath(FileType.EVAL));
	}

	/**
	 * 
	 * @param classifier
	 * @return a filtered classifier wrapper with attribute selection and over sampling (smooth)
	 * @throws Exception
	 */
	private IClassifier builbClassifier_fs(Classifier classifier) throws Exception{
		AttributeClassifierWrapper classifierWrapper =  new AttributeClassifierWrapper();
		classifierWrapper.add(classifier);
		return classifierWrapper;
	}


}
