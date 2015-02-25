package com.cheo.services.arff;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheo.base.enums.ClassLabel;
import com.cheo.base.enums.ExtraFeatures;

public class ArffConfigReader {

	private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			.newInstance();

	private Resource resource;

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public ArffConfig read() throws Exception{

		ArffConfig arffConfig = new ArffConfig();
		try{
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(resource.getFile());
			Element root = document.getDocumentElement();
			if(root!=null){
				NodeList extraFeatures = root.getElementsByTagName("extraFeatures");
				for (int i = 0; i < extraFeatures.getLength(); i++) {
					if (extraFeatures.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element extraFeature = (Element) extraFeatures.item(i);
						String value = extraFeature.getAttribute("value");

						if("include".equalsIgnoreCase(value)){
							arffConfig.setIncludeExtraFeatures(true);
						}else if("exclude".equalsIgnoreCase(value)){
							arffConfig.setIncludeExtraFeatures(false);
						}else{
							throw new Exception("The value of extraFeatures element is not acceptable!");
						}
					}
				}

				NodeList featureFiles = root.getElementsByTagName("featureFile");
				for (int i = 0; i < featureFiles.getLength(); i++) {
					if (featureFiles.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element featureFile = (Element) featureFiles.item(i);
						String value = featureFile.getAttribute("value");
						arffConfig.setFeatureFileName(value);
					}
				}
				
				NodeList levels = root.getElementsByTagName("level");
				for (int i = 0; i < levels.getLength(); i++) {
					if (levels.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element level = (Element) levels.item(i);
						String value = level.getAttribute("name");
						arffConfig.setLevel(value);
					}
				}
				
				NodeList arffFors = root.getElementsByTagName("normilized");
				for (int i = 0; i < arffFors.getLength(); i++) {
					if (arffFors.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element arffFor = (Element) arffFors.item(i);
						String value = arffFor.getAttribute("value");
						arffConfig.setNormilized(Boolean.valueOf(value));
					}
				}
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}

		Assert.notNull(arffConfig.getFeatureFileName());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:"+arffConfig.getFeatureFileName());
		for(Resource resource : resources){
			if(arffConfig.getFeatureFileName().equalsIgnoreCase(resource.getFilename())){
				readFeaturesConfig(resource, arffConfig);	
			}
		}

		return arffConfig;
	}

	private void readFeaturesConfig(Resource resource, ArffConfig arffConfig){
		try{
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(resource.getFile());
			Element root = document.getDocumentElement();
			if(root!=null){
				NodeList features = root.getElementsByTagName("feature");
				for (int i = 0; i < features.getLength(); i++) {
					if (features.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element feature = (Element) features.item(i);
						String featureName = feature.getAttribute("name");

						//BY DEFAULT INCLUDES 
						//CONTENT
						//CLASS LABEL
						if(featureName.equalsIgnoreCase(ExtraFeatures.CLASS_LABEL.getValue())){
							List<String> classLabels = new ArrayList<String>();
							NodeList values = feature.getElementsByTagName("value");
							if(values !=null){
								for (int j = 0; j < values.getLength(); j++) {
									if (values.item(j).getNodeType() == Node.ELEMENT_NODE) {
										Element value = (Element) values.item(j);
										classLabels.add(value.getAttribute("name"));
									}
								}
							}
							List<ClassLabel> enumLabels = new ArrayList<ClassLabel>();
							for(String label: classLabels){
								enumLabels.add(ClassLabel.fromString(label));
							}
							arffConfig.getFeatureConfig().setClassLabels(enumLabels);
						}
					}
				}

				//EXTRA FEATURES
				if(arffConfig.includeExtraFeatures()){
					NodeList extraFeatures = root.getElementsByTagName("extraFeatures");
					for (int i = 0; i < extraFeatures.getLength(); i++) {
						if (extraFeatures.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element extraFeature = (Element) extraFeatures.item(i);

							NodeList fts = extraFeature.getElementsByTagName("feature");
							for (int j = 0; j < fts.getLength(); j++) {
								if (fts.item(j).getNodeType() == Node.ELEMENT_NODE) {
									Element ft = (Element) fts.item(j);
									String ftName = ft.getAttribute("name");

									if(ftName.equalsIgnoreCase(ExtraFeatures.LASTEDUCLASSLABEL.getValue())){
										arffConfig.getFeatureConfig().setHasLastEduSenti(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMPOSITIVESWN.getValue())){
										arffConfig.getFeatureConfig().setHasNumPositiveSWN(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEGI.getValue())){
										arffConfig.getFeatureConfig().setHasNumPositiveGI(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEPL.getValue())){
										arffConfig.getFeatureConfig().setHasNumPositivePL(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEDEPMODE.getValue())){
										arffConfig.getFeatureConfig().setHasNumPositiveDepmode(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMPOSITIVNRC.getValue())){
										arffConfig.getFeatureConfig().setHasNumPositiveNRC(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMPOSITIVESLANG.getValue())){
										arffConfig.getFeatureConfig().setHasNumPositiveSLANG(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMPOSITIVEEMOTICON.getValue())){
										arffConfig.getFeatureConfig().setHasNumPositiveEMOTICON(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMNEGATIVESWN.getValue())){
										arffConfig.getFeatureConfig().setHasNumNegativeSWN(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEGI.getValue())){
										arffConfig.getFeatureConfig().setHasNumNegativeGI(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEPL.getValue())){
										arffConfig.getFeatureConfig().setHasNumNegativePL(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEDEPMODE.getValue())){
										arffConfig.getFeatureConfig().setHasNumNegativeDepmode(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMNEGATIVENRC.getValue())){
										arffConfig.getFeatureConfig().setHasNumNegativeNRC(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMNEGATIVESLANG.getValue())){
										arffConfig.getFeatureConfig().setHasNumNegativeSLANG(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMNEGATIVEEMOTICON.getValue())){
										arffConfig.getFeatureConfig().setHasNumNegativeEMOTICON(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMPUNCTUATIONS.getValue())){
										arffConfig.getFeatureConfig().setHasNumPunctuations(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMELONGATEDWORDS.getValue())){
										arffConfig.getFeatureConfig().setHasNumElongatedWords(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMDSLWORDS.getValue())){
										arffConfig.getFeatureConfig().setHasNumDSLWords(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMWEAKSUBJPL.getValue())){
										arffConfig.getFeatureConfig().setHasNumWeakSubjPL(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.NUMSTRONGSUBJPL.getValue())){
										arffConfig.getFeatureConfig().setHasNumStrongSubjPL(true);
									}
									//For rell irrel classification
									if(ftName.equalsIgnoreCase(ExtraFeatures.PRIORCLASS.getValue())){
										arffConfig.getFeatureConfig().setHasPriorClass(true);
									}
									//For reference to the parent comment
									if(ftName.equalsIgnoreCase(ExtraFeatures.SHEETID.getValue())){
										arffConfig.getFeatureConfig().setHasSheetID(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.COMMENTID.getValue())){
										arffConfig.getFeatureConfig().setHasCommentID(true);
									}
									if(ftName.equalsIgnoreCase(ExtraFeatures.EDUID.getValue())){
										arffConfig.getFeatureConfig().setHasEduID(true);
									}
								}
							}
						}
					}
				}//end of extra features

			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
