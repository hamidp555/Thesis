package com.cheo.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheo.model.Comment;
import com.cheo.services.excel.ExcelService;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class DiscourseParserService implements InitializingBean {

	final static private String BASE_FOLDER = "/Users/hamidpoursepanj/Desktop/Masters-semesters/Project-Data/annotated-new/first_annotator_discourse_segmented"; 

	final static private String INPUT_FOLDER = BASE_FOLDER + "/input";

	final static private String OUTPUT_FOLDER = BASE_FOLDER + "/output";

	final static private String CHARNIAK_ARG =  "./parser05Aug16 -T50";

	private ExcelService excelService;

	private SpellChekerService spellChecker;

	private TreeBasedTable<Integer, Integer, Comment> comments ;

	private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			.newInstance();


	public void setSpellChecker(SpellChekerService spellChecker) {
		this.spellChecker = spellChecker;
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		comments = excelService.readAllCommentsFiltered("first");
	}

	public void parse(boolean spellChecked) throws Exception{

		prepareForSegmentation(spellChecked);

		StringBuilder parseCommand = new StringBuilder();
		parseCommand.append("python /usr/local/discourseParser/SLSeg_ver_0.2/run_all.py ");
		parseCommand.append(INPUT_FOLDER);
		parseCommand.append(" ");
		parseCommand.append(OUTPUT_FOLDER);
		parseCommand.append(" ");
		parseCommand.append(CHARNIAK_ARG);

		executeCommand(parseCommand.toString());
	}

	private void prepareForSegmentation(boolean SpellChecked) throws Exception{
		int counter = 1;
		Iterator<Table.Cell<Integer, Integer, Comment>> iter = comments.cellSet().iterator();
		while(iter.hasNext()){
			Table.Cell<Integer, Integer, Comment> cell = iter.next();
			Comment comment = cell.getValue();
			String commentBody = comment.getComment();
			if(SpellChecked)
				commentBody = spellChecker.spellCheckComment(commentBody);

			StringBuilder sb = new StringBuilder();
			sb.append(INPUT_FOLDER);
			sb.append("/comment_");
			sb.append(counter);
			sb.append(".txt");

			counter++;
			File file = new File(sb.toString());

			if(file.exists()  && file.isFile() && file.length()>0)
				break;

			if (!file.exists()) 
				file.createNewFile();

			Writer writer = 
					new BufferedWriter(
							new OutputStreamWriter(
									new FileOutputStream(sb.toString()), "utf-8"));

			writer.write(commentBody);
			writer.close();
		}
	}

	private String executeCommand(String command) {
		StringBuffer output = new StringBuffer();

		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
					new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}

	/**
	 * <ol>
	 * <li>&lt;T&gt; &lt;&#47;T&gt; - these tags capture the entire text; they delineate where the text begins and ends</li>
	 * <li>&lt;P&gt; &lt;&#47;P&gt; - these tags mark paragraph boundaries</li>
	 * <li>&lt;s&gt; &lt;&#47;s&gt; - these tags mark sentence boundaries</li>
	 * <li>&lt;c&gt; &lt;&#47;c&gt; - these tags mark clause boundaries</li>
	 * <li>&lt;M&gt; &lt;&#47;M&gt; - MisusedtomarkthetraceofaNPwhichhasbeen</li>
	 * </ol>
	 * @return elementary discourse units
	 */
	public Map<Integer, List<String>> getEDUs() throws Exception{

		Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();

		//read all files in output folder
		File folder = new File(OUTPUT_FOLDER + "/step5_segmented");

		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		};

		File[] listOfFiles = folder.listFiles(filter);

		//parse all text files for EDUs
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		for (int i = 0; i < listOfFiles.length; i++) {
			List<String> edus = new ArrayList<String>();
			makeCompliantWithXML(listOfFiles[i]);
			Document document = documentBuilder.parse(listOfFiles[i]);
			Element root = document.getDocumentElement();
			if(root!=null){
				//text
				NodeList textTags= root.getElementsByTagName("T");
				for (int j = 0; j <  textTags.getLength(); j++) {
					if ( textTags.item(j).getNodeType() == Node.ELEMENT_NODE) {
						Element textTag = (Element) textTags.item(j);
						//paragraphs
						NodeList paragraphTags = textTag.getElementsByTagName("P");
						for (int k = 0; k <  paragraphTags.getLength(); k++) {
							if ( paragraphTags.item(k).getNodeType() == Node.ELEMENT_NODE) {
								Element paragraphTag = (Element) paragraphTags.item(k);
								//sentences
								NodeList sentenceTags = paragraphTag.getElementsByTagName("S");
								for (int l = 0; l <  sentenceTags.getLength(); l++) {
									if ( sentenceTags.item(l).getNodeType() == Node.ELEMENT_NODE) {
										Element sentenceTag = (Element) sentenceTags.item(l);
										//discourse unit tags
										NodeList eduTags = sentenceTag.getElementsByTagName("C");
										for (int m = 0; m <  eduTags.getLength(); m++) {
											if ( eduTags.item(m).getNodeType() == Node.ELEMENT_NODE) {
												Element eduTag = (Element) eduTags.item(m);
												edus.add(eduTag.getTextContent());
											}

										}

									}
								}

							}
						}
					}
				}
			}
			result.put(i, edus);
		}		
		return result;	
	}

	/**
	 * converts TXT output from SLSeg_2.0 to XML
	 * XML escape characters
	 * <UL>
	 * <li>"   &quot;</li>
	 * <li>'   &apos;</li>
	 * <li><   &lt;</li>
	 * <li>>   &gt;</li>
	 * <li>&   &amp;</li>
	 * </ul>
	 * @throws IOException 
	 */
	private void makeCompliantWithXML(File file) throws IOException{
		@SuppressWarnings("unchecked")
		List<String> lines = FileUtils.readLines(file);
		List<String> modifiedLines = new ArrayList<String>();
		for(String line : lines){
			if (line.contains("&")){
				line = line.replaceAll("&", "&amp;");
			}
			if(line.contains("<breakWithinParens>")){
				line = line.replaceAll("<breakWithinParens>", "");
			}
			if(line.matches("<C>.*<img.*>.*</C>")){
				line = line.replaceAll("<img.*>/\\w+(?=\\s)", "");
			}
			if(line.matches("<C>><br.*/>/\\w+(?=\\s).*</C>")){
				line = line.replaceAll("<br.*/>/\\w+(?=\\s)", "");
			}
			if(line.matches("<C><br.*/>.*</C>")){
				line = line.replaceAll("<br.*/>", "");
			}
			modifiedLines.add(line);
		}
		FileUtils.writeLines(file, modifiedLines);
	}

}
