package com.cheo.weka.services;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheo.base.enums.ClassifierType;

public class ClassifierConfigReader {

	private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			.newInstance();

	private Resource resource;
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public ClassifierConfig read() throws Exception{
		
		ClassifierConfig classifierConfig = new ClassifierConfig();
		
		try{
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(resource.getFile());
			Element root = document.getDocumentElement();
			if(root!=null){
				NodeList extraFeatures = root.getElementsByTagName("classifier");
				for (int i = 0; i < extraFeatures.getLength(); i++) {
					if (extraFeatures.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element extraFeature = (Element) extraFeatures.item(i);
						String type = extraFeature.getAttribute("name");
						classifierConfig.setType(ClassifierType.valueOf(type));
					}
				}
				
				NodeList featureFiles = root.getElementsByTagName("arffFile");
				for (int i = 0; i < featureFiles.getLength(); i++) {
					if (featureFiles.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element featureFile = (Element) featureFiles.item(i);
						String value = featureFile.getAttribute("value");
						classifierConfig.setArffFileName(value);
					}
				}
				
				NodeList levels = root.getElementsByTagName("level");
				for (int i = 0; i < levels.getLength(); i++) {
					if (levels.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element level = (Element) levels.item(i);
						String value = level.getAttribute("name");
						classifierConfig.setLevel(value);
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return classifierConfig;
	}
}
