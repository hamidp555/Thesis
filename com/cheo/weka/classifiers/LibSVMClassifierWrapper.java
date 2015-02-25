package com.cheo.weka.classifiers;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.Range;
import weka.core.SelectedTag;

public class LibSVMClassifierWrapper extends AbstractClassifierWrapper {

	@Override
	public LibSVM build() throws Exception{
		LibSVM libSvm = new LibSVM();

		SelectedTag svmType = new SelectedTag(LibSVM.SVMTYPE_C_SVC, LibSVM.TAGS_SVMTYPE);

		libSvm.setSVMType(svmType);
		libSvm.setCacheSize(40.0);
		libSvm.setCoef0(0);
		libSvm.setCost(1.0);
		libSvm.setDebug(false);
		libSvm.setDegree(3);
		libSvm.setDoNotReplaceMissingValues(false);
		libSvm.setEps(0.001);
		libSvm.setGamma(0);

		SelectedTag kernelType = new SelectedTag(LibSVM.KERNELTYPE_RBF, LibSVM.TAGS_KERNELTYPE);
		libSvm.setKernelType(kernelType);
		libSvm.setLoss(0.1);
		libSvm.setNormalize(false);
		libSvm.setNu(0.5);
		libSvm.setProbabilityEstimates(false);
		libSvm.setSeed(1);
		libSvm.setShrinking(true);
		libSvm.setWeights(org.apache.commons.lang3.StringUtils.EMPTY);

		return libSvm;
	}

	@Override
	public void classifyAndEvaluate(Instances data, String predictionOutputRange) throws Exception {
		LibSVM classifier = build();
		classifier.buildClassifier(data);

		setClassifier(classifier); ;

		Evaluation evaluation = new Evaluation(data);
		StringBuffer prediction = new StringBuffer();
		evaluation.crossValidateModel(classifier, data, 10, new Random(1),prediction, new Range(predictionOutputRange), true);
		prediction.toString();
		setEvaluation(evaluation);
		setPrediction(prediction);

	}

}
