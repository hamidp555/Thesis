package com.cheo.services.arff;

import java.util.List;

//import org.apache.log4j.Logger;

import com.cheo.base.enums.ClassLabel;
import com.cheo.base.enums.ExtraFeatures;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

public class DatasetFactory {

	//private static Logger logger = Logger.getLogger(DatasetFactory.class);
	
	public static Instances createDataset(ArffConfig config){

		FastVector atts = new FastVector(2 + config.getFeatureConfig().getNumExtraFeatures());

		//CLASS LABELS
		List<ClassLabel> classLabels = config.getFeatureConfig().getClassLabels();
		FastVector classVals = new FastVector(classLabels.size());
		for(ClassLabel label: classLabels){
			classVals.addElement(label.getValue());
		}
		atts.addElement(new Attribute(ExtraFeatures.CLASS_LABEL.getValue(), classVals));
		atts.addElement(new Attribute(ExtraFeatures.CONTENT.getValue(), (FastVector) null));


		//EXTRA FEATRUES
		if(config.includeExtraFeatures()){
			
			//PRIORCLASS FOR REL IRREL CLASSIFICATION
			if(config.getFeatureConfig().hasPriorClass()){
				FastVector classVals3 = new FastVector(ClassLabel.values().length);
				for(ClassLabel label: ClassLabel.getLabels()){
					classVals3.addElement(label.getValue());
				}
				atts.addElement(new Attribute(ExtraFeatures.PRIORCLASS.getValue(), classVals3));
			}
			
			//EXTRA FEATURE FOR COMMENT LEVEL CLASSIFICATION
			if(config.getFeatureConfig().hasLastEduSenti()){
				FastVector classVals2 = new FastVector(ClassLabel.values().length);
				for(ClassLabel label: ClassLabel.values()){
					classVals2.addElement(label.getValue());
				}
				atts.addElement(new Attribute(ExtraFeatures.LASTEDUCLASSLABEL.getValue(), classVals2));
			}

			//FOR REFERENCE TO THE PARENT COMMENT
			if(config.getFeatureConfig().hasSheetID()){
				atts.addElement(new Attribute(ExtraFeatures.SHEETID.getValue()));
			}

			if(config.getFeatureConfig().hasCommentID()){
				atts.addElement(new Attribute(ExtraFeatures.COMMENTID.getValue()));
			}

			if(config.getFeatureConfig().hasEduID()){
				atts.addElement(new Attribute(ExtraFeatures.EDUID.getValue()));
			}

			//Positive
			if(config.getFeatureConfig().hasNumPositiveSWN()){
				atts.addElement(new Attribute(ExtraFeatures.NUMPOSITIVESWN.getValue()));
			}
			if(config.getFeatureConfig().hasNumPositiveGI()){
				atts.addElement(new Attribute(ExtraFeatures.NUMPOSITIVEGI.getValue()));
			}
			if(config.getFeatureConfig().hasNumPositivePL()){
				atts.addElement(new Attribute(ExtraFeatures.NUMPOSITIVEPL.getValue()));
			}
			if(config.getFeatureConfig().hasNumPositiveDepmode()){
				atts.addElement(new Attribute(ExtraFeatures.NUMPOSITIVEDEPMODE.getValue()));
			}
			if(config.getFeatureConfig().hasNumPositiveNRC()){
				atts.addElement(new Attribute(ExtraFeatures.NUMPOSITIVNRC.getValue()));
			}
			if(config.getFeatureConfig().hasNumPositiveSLANG()){
				atts.addElement(new Attribute(ExtraFeatures.NUMPOSITIVESLANG.getValue()));
			}
			if(config.getFeatureConfig().hasNumPositiveEMOTICON()){
				atts.addElement(new Attribute(ExtraFeatures.NUMPOSITIVEEMOTICON.getValue()));
			}

			//Negative
			if(config.getFeatureConfig().hasNumNegativeSWN()){
				atts.addElement(new Attribute(ExtraFeatures.NUMNEGATIVESWN.getValue()));
			}
			if(config.getFeatureConfig().hasNumNegativeGI()){
				atts.addElement(new Attribute(ExtraFeatures.NUMNEGATIVEGI.getValue()));
			}
			if(config.getFeatureConfig().hasNumNegativePL()){
				atts.addElement(new Attribute(ExtraFeatures.NUMNEGATIVEPL.getValue()));
			}
			if(config.getFeatureConfig().hasNumNegativeDepmode()){
				atts.addElement(new Attribute(ExtraFeatures.NUMNEGATIVEDEPMODE.getValue()));
			}
			if(config.getFeatureConfig().hasNumNegativeNRC()){
				atts.addElement(new Attribute(ExtraFeatures.NUMNEGATIVENRC.getValue()));
			}
			if(config.getFeatureConfig().hasNumNegativeSLANG()){
				atts.addElement(new Attribute(ExtraFeatures.NUMNEGATIVESLANG.getValue()));
			}
			if(config.getFeatureConfig().hasNumNegativeEMOTICON()){
				atts.addElement(new Attribute(ExtraFeatures.NUMNEGATIVEEMOTICON.getValue()));
			}


			//Others
			if(config.getFeatureConfig().hasNumPunctuations()){
				atts.addElement(new Attribute(ExtraFeatures.NUMPUNCTUATIONS.getValue()));
			}
			if(config.getFeatureConfig().hasNumElongatedWords()){
				atts.addElement(new Attribute(ExtraFeatures.NUMELONGATEDWORDS.getValue()));
			}
			if(config.getFeatureConfig().hasNumDSLWords()){
				atts.addElement(new Attribute(ExtraFeatures.NUMDSLWORDS.getValue()));
			}
			if(config.getFeatureConfig().hasNumWeakSubjPL()){
				atts.addElement(new Attribute(ExtraFeatures.NUMWEAKSUBJPL.getValue()));
			}
			if(config.getFeatureConfig().hasNumStrongSubjPL()){
				atts.addElement(new Attribute(ExtraFeatures.NUMSTRONGSUBJPL.getValue()));
			}
		}

		return  new Instances("Cheo_" + config.getLevel(), atts, 0);
	}


}
