package com.cheo.services.arff;

import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import com.cheo.base.EDUWrapper;
import com.cheo.base.enums.ClassLabel;
import com.cheo.base.enums.ExtraFeatures;
import com.cheo.model.Comment;
import com.cheo.model.EDU;
import com.cheo.services.AnalyzerService;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class ArffServiceEuds implements ArffService{

	private Instances dataset;

	private AnalyzerService alnalyzer;

	private ArffHelper arffHelper;

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
		for(EDU edu : comment.getEdus()){
			if(!isRemovable(edu)){
				double[] instance = getAttValues(edu);
				if(instance != null){
					dataset.add(new Instance(1.0, instance));
				}	
			}
		}
	}

	private boolean isRemovable(EDU edu) throws Exception{
		return edu.getClassLabel().equals(ClassLabel.OBJECTIVE);
	}

	private double[] getAttValues(EDU edu) throws Exception{

		if(StringUtils.isBlank(edu.getComment())){
			throw new RuntimeException("The content of edu is null!");
		}

		EDUWrapper eduWrapper = (EDUWrapper)alnalyzer.analyze(edu.getComment());

		StringBuilder sb = new StringBuilder();
		sb.append(eduWrapper.getFilteredEDUFromLemmas());
		if(!eduWrapper.getPosEmoticons().isEmpty()){
			sb.append(StringUtils.join(eduWrapper.getPosEmoticons(), " "));
		}
		if(!eduWrapper.getNegEmoticons().isEmpty()){
			sb.append(StringUtils.join(eduWrapper.getNegEmoticons(), " "));	
		}

		if(StringUtils.isBlank(sb.toString())){
			return null;
		}

		double[] newInst = new double[dataset.numAttributes()];

		@SuppressWarnings("unchecked")
		Enumeration<Attribute> e = dataset.enumerateAttributes();

		try{
			int index=-1;
			while(e.hasMoreElements()){

				index++;
				int totalNumTermsInUnit = eduWrapper.getStatistics().getTotalNumTerms();
				Attribute att = e.nextElement();
				
				if(att.isNominal() && 
						att.name().equalsIgnoreCase(ExtraFeatures.CLASS_LABEL.getValue())){
					String clazz = ((ArffHelperEdu)arffHelper).getClazz(edu);
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
					String priorClass = edu.getClassLabel().getValue();
					newInst[index] = (double)dataset.attribute(2).indexOfValue(priorClass);
					continue;
				}

				//FOR REFERENCE TO THE PARENT COMMENT
				if(att.isNumeric() && 
						att.name().equalsIgnoreCase(ExtraFeatures.SHEETID.getValue())){
					newInst[index] = (double)edu.getSheetID();
					continue;
				}

				if(att.isNumeric() && 
						att.name().equalsIgnoreCase(ExtraFeatures.COMMENTID.getValue())){
					newInst[index] = (double)edu.getCommentID();
					continue;
				}

				if(att.isNumeric() && 
						att.name().equalsIgnoreCase(ExtraFeatures.EDUID.getValue())){
					newInst[index] = (double)edu.getEduID();
					continue;
				}

				//POSITIVE FEATURES
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVESWN.getValue())){
					int num =  eduWrapper.getStatistics().getNumPositiveSWN();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEGI.getValue())){
					int num = eduWrapper.getStatistics().getNumPositiveGI();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEPL.getValue())){
					int num = eduWrapper.getStatistics().getNumPositivePL();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEDEPMODE.getValue())){
					int num = eduWrapper.getStatistics().getNumPositiveDepmode();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVNRC.getValue())){
					int num = eduWrapper.getStatistics().getNumPositiveNRC();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVESLANG.getValue())){
					int num = eduWrapper.getStatistics().getNumPositiveSLANG();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEEMOTICON.getValue())){
					int num = eduWrapper.getStatistics().getNumPositiveEMOTICON();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}

				//NEGATIVE FEATURES
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVESWN.getValue())){
					int num = eduWrapper.getStatistics().getNumNegativeSWN();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEGI.getValue())){
					int num = eduWrapper.getStatistics().getNumNegativeGI();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEPL.getValue())){
					int num = eduWrapper.getStatistics().getNumNegativePL();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEDEPMODE.getValue())){
					int num = eduWrapper.getStatistics().getNumNegativeDepmode();
					newInst[index] =  (double)((double)num/totalNumTermsInUnit);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVENRC.getValue())){
					int num = eduWrapper.getStatistics().getNumNegativeNRC();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVESLANG.getValue())){
					int num = eduWrapper.getStatistics().getNumNegativeSLANG();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEEMOTICON.getValue())){
					int num = eduWrapper.getStatistics().getNumNegativeEMOTICON();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}

				//OTHER FEATURES
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMPUNCTUATIONS.getValue())){
					int num = eduWrapper.getStatistics().getNumPunctuation();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMELONGATEDWORDS.getValue())){
					int num = eduWrapper.getStatistics().getNumElongatedWords();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMDSLWORDS.getValue())){
					int num = eduWrapper.getStatistics().getNumDSLWords();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMWEAKSUBJPL.getValue())){
					int num = eduWrapper.getStatistics().getNumWeakSubjPL();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
				if(att.isNumeric() &&
						att.name().equalsIgnoreCase(ExtraFeatures.NUMSTRONGSUBJPL.getValue())){
					int num = eduWrapper.getStatistics().getNumStrongSubjPL();
					newInst[index] = (double)(arffHelper.isNormilized() ? (((double)num)/totalNumTermsInUnit) : num);
					continue;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
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
