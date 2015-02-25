package com.cheo.services.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ExcelConfig implements InitializingBean{

	private Resource resource;
	
	private Map<String, List<Header>> tableMap; 
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Map<String, List<Header>> getTableMap() {
		return tableMap;
	}

	public void setTableMap(Map<String, List<Header>> tableMap) {
		this.tableMap = tableMap;
	}

	private void read() {
		tableMap = new HashMap<String, List<Header>>();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(resource.getFile());
			Element root = document.getDocumentElement();
			if(root!=null){
				NodeList tables = root.getElementsByTagName("table");
				for (int i = 0; i < tables.getLength(); i++) {
					if (tables.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element table = (Element) tables.item(i);
						String type = table.getAttribute("type");
						
						tableMap.put(type, new ArrayList<Header>());
						
						NodeList columns = table.getElementsByTagName("column");
						for (int j = 0; j < columns.getLength(); j++) {
							if (columns.item(j).getNodeType() == Node.ELEMENT_NODE) {
								Element column = (Element) columns.item(j);
								String name = column.getAttribute("name");
								String index = column.getAttribute("index");
								
								tableMap.get(type).add(new Header(name, Integer.valueOf(index)));
							}
						}
					}
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		read();
	}
	
}
