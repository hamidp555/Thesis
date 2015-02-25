package com.cheo.services.arff;


import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import com.cheo.base.CommentWrapper;
import com.cheo.base.enums.ExtraFeatures;
import com.cheo.model.Comment;
import com.cheo.services.AnalyzerService;
import com.cheo.weka.classifiers.prediction.PredictionHelper;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class ArffServiceComments implements ArffService{

	private static Logger logger = Logger.getLogger(ArffServiceComments.class);

	private Instances dataset;

	private AnalyzerService alnalyzer;

	private ArffHelper arffHelper;

	private PredictionHelper predictionHelper;

	public void setPredictionHelper(PredictionHelper predictionHelper) {
		this.predictionHelper = predictionHelper;
	}

	public void setArffHelper(ArffHelper arffHelperComment) {
		this.arffHelper = arffHelperComment;
	}

	public void setDataset(Instances dataset) {
		this.dataset = dataset;
	}

	public void setAlnalyzer(AnalyzerService alnalyzer) {
		this.alnalyzer = alnalyzer;
	}

	@Override
	public Instances createArff(TreeBasedTable<Integer, Integer, Comment>  comments) throws Exception {

		dataset = DatasetFactory.createDataset(arffHelper.getConfig());

		Iterator<Table.Cell<Integer, Integer, Comment>> iter = comments.cellSet().iterator();
		while(iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();
			addToDataset(comment);
		}
		validate(dataset);
		return dataset;
	}

	private void addToDataset(Comment comment) throws Exception{
		double[] instance = getAttValues(comment);
		if(instance != null){
			dataset.add(new Instance(1.0, instance));
		}
		if(logger.isDebugEnabled()){
			logger.debug(comment.getSheetID() + " - " + comment.getCommentID() + " added to dataset");
		}
	}

	private double[] getAttValues(Comment comment) throws Exception{

		CommentWrapper commentWrapper = (CommentWrapper)alnalyzer.analyze(comment.getComment());

		StringBuilder sb = new StringBuilder();
		sb.append(commentWrapper.getFilteredCommentFromLemmas());
		if(!commentWrapper.getPosEmoticons().isEmpty()){
			sb.append(StringUtils.join(commentWrapper.getPosEmoticons(), " "));
		}
		if(!commentWrapper.getNegEmoticons().isEmpty()){
			sb.append(StringUtils.join(commentWrapper.getNegEmoticons(), " "));	
		}	

		if(StringUtils.isBlank(sb.toString())){
			return null;
		}

		double[] newInst = new double[dataset.numAttributes()];

		@SuppressWarnings("unchecked")
		Enumeration<Attribute> e = dataset.enumerateAttributes();

		int index=-1;
		while(e.hasMoreElements()){

			index++;
			int totalNumTermsInUnit = commentWrapper.getStatistics().getTotalNumTerms();
			Attribute att = e.nextElement();

			if(att.isNominal() && 
					att.name().equalsIgnoreCase(ExtraFeatures.CLASS_LABEL.getValue())){
				String clazz = ((ArffHelperComment)arffHelper).getClazz(comment);
				newInst[index] = dataset.attribute(0).indexOfValue(clazz);
				continue;
			}

			if(att.isString() && 
					att.name().equalsIgnoreCase(ExtraFeatures.CONTENT.getValue())){
				newInst[index] = (double)dataset.attribute(1).addStringValue(sb.toString());
				continue;
			}

			//FOR REL IRREL CLASIFICATION
			if(att.isNominal() && 
					att.name().equalsIgnoreCase(ExtraFeatures.PRIORCLASS.getValue())){
				String priorClass = comment.getClassLabel().getValue();
				newInst[index] = (double)dataset.attribute(2).indexOfValue(priorClass);
				continue;
			}			

			if(att.isNominal() && 
					att.name().equalsIgnoreCase(ExtraFeatures.LASTEDUCLASSLABEL.getValue())){
				String clazz = predictionHelper.getLastEduClazzPrediction(comment).getValue();
				newInst[index] = dataset.attribute(3).indexOfValue(clazz);
				continue;
			}

			//FOR REFERENCE TO THE PARENT COMMENT
			if(att.isNumeric() && 
					att.name().equalsIgnoreCase(ExtraFeatures.SHEETID.getValue())){
				newInst[index] = (double)comment.getSheetID();
				continue;
			}

			if(att.isNumeric() && 
					att.name().equalsIgnoreCase(ExtraFeatures.COMMENTID.getValue())){
				newInst[index] = (double)comment.getCommentID();
				continue;
			}

			if(att.isNumeric() && 
					att.name().equalsIgnoreCase(ExtraFeatures.EDUID.getValue())){
				newInst[index] = (double)comment.getEduID();
				continue;
			}

			//POSITIVE FEATURES
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVESWN.getValue())){
				int num = commentWrapper.getStatistics().getNumPositiveSWN();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEGI.getValue())){
				int num = commentWrapper.getStatistics().getNumPositiveGI();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEPL.getValue())){
				int num = commentWrapper.getStatistics().getNumPositivePL();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEDEPMODE.getValue())){
				int num = commentWrapper.getStatistics().getNumPositiveDepmode();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVNRC.getValue())){
				int num = commentWrapper.getStatistics().getNumPositiveNRC();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVESLANG.getValue())){
				int num = commentWrapper.getStatistics().getNumPositiveSLANG();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEEMOTICON.getValue())){
				int num = commentWrapper.getStatistics().getNumPositiveEMOTICON();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}

			//NEGATIVE FEATURES
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVESWN.getValue())){
				int num = commentWrapper.getStatistics().getNumNegativeSWN();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEGI.getValue())){
				int num = commentWrapper.getStatistics().getNumNegativeGI();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEPL.getValue())){
				int num = commentWrapper.getStatistics().getNumNegativePL();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEDEPMODE.getValue())){
				int num = commentWrapper.getStatistics().getNumNegativeDepmode();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVENRC.getValue())){
				int num = commentWrapper.getStatistics().getNumNegativeNRC();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVESLANG.getValue())){
				int num = commentWrapper.getStatistics().getNumNegativeSLANG();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEEMOTICON.getValue())){
				int num = commentWrapper.getStatistics().getNumNegativeEMOTICON();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}

			//OTHER FEATURES
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMPUNCTUATIONS.getValue())){
				int num = commentWrapper.getStatistics().getNumPunctuation();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMELONGATEDWORDS.getValue())){
				int num = commentWrapper.getStatistics().getNumElongatedWords();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMDSLWORDS.getValue())){
				int num = commentWrapper.getStatistics().getNumDSLWords();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMWEAKSUBJPL.getValue())){
				int num = commentWrapper.getStatistics().getNumWeakSubjPL();
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
			if(att.isNumeric() &&
					att.name().equalsIgnoreCase(ExtraFeatures.NUMSTRONGSUBJPL.getValue())){
				int num = commentWrapper.getStatistics().getNumStrongSubjPL();	
				newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
				continue;
			}
		}

		return newInst;
	}

	private boolean validate(Instances dataset){
		boolean missing = false;
		for (int i = 0; i < dataset.numInstances(); i++) {
			Instance inst = dataset.instance(i);

			for (int m = 0; m < inst.numValues(); m++) {
				int n = inst.index(m);
				if (inst.isMissing(n)) {
					missing = true;
					break;
				}
			}

		}
		return missing;
	}

}
