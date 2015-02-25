package com.cheo.tagConv;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class AbstractTagConfigReader {
	
	private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			.newInstance();

	private Resource resource;
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	protected Multimap<String, String> readConfig(String rootName, String childName){

		Multimap<String, String> map = ArrayListMultimap.create();
		try{
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(resource.getFile());
			Element root = document.getDocumentElement();
			if(root!=null){
				NodeList convertToTags = root.getElementsByTagName(rootName);
				for (int i = 0; i < convertToTags.getLength(); i++) {
					if (convertToTags.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element convertToTag = (Element) convertToTags.item(i);
						String convertToTagName = convertToTag.getAttribute("name");
						NodeList convertFromTags = convertToTag.getElementsByTagName(childName);
						if(convertFromTags !=null){
							for (int j = 0; j < convertFromTags.getLength(); j++) {
								if (convertFromTags.item(j).getNodeType() == Node.ELEMENT_NODE) {
									Element convertFromTag = (Element) convertFromTags.item(j);
									String convertFromTagName = convertFromTag.getAttribute("name");
									map.put(convertToTagName, convertFromTagName);
								}
							}
						}

					}
				}
			}
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch (SAXException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return  Multimaps.invertFrom(map,
				ArrayListMultimap.<String, String>create());

	}

}
