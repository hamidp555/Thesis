package com.cheo.services.hildaTree;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class HildaStyleParser {

	private static final Pattern REL_PATTERN = Pattern.compile("^\\(.*\\[\\w+\\]\\[\\w+\\]$");

	private static final Pattern END_PARA_PATTERN = Pattern.compile(".*\\)+$");

	private static final Pattern EDU_PATTERN = Pattern.compile("^_!.+!_\\)*$");

	private static final Pattern AS_TWO_LINES_PATTERN = Pattern.compile("^.*\\[\\w+\\]\\[\\w+\\].*_!.+!_\\)*$");

	@SuppressWarnings("unchecked")
	public static LinkedBinaryTree<Element> parse(String fileName) throws Exception{

		Stack<LinkedBinaryTree<Element>> treeStack = new Stack<LinkedBinaryTree<Element>>();

		List<String> lines = HildaUtils.readAll(fileName);
		assert matchParanthesis(StringUtils.join(lines));

		for(String compositeLine : lines){

			if(StringUtils.isBlank(compositeLine)){
				throw new Exception("Line cannot be null.");
			}

			for(String line : breakLine(compositeLine)){
				try{
					int count = countClosePara(line);

					LinkedBinaryTree<Element> tree = new LinkedBinaryTree<Element>();
					tree.addRoot(createElement(line));
					treeStack.push(tree);

					Matcher end_param_m = END_PARA_PATTERN.matcher(line);
					if(end_param_m.matches()){

						for(int i=0; i<count ; i++){
							LinkedBinaryTree<Element> rightTree = treeStack.pop();
							LinkedBinaryTree<Element> leftTree = treeStack.pop();
							LinkedBinaryTree<Element> rootTree = treeStack.pop();
							rootTree.attach(rootTree.root(), leftTree, rightTree);
							treeStack.push(rootTree);
						}
					}
				}catch(Exception e){
					throw new Exception(line, e);
				}
			}
		}

		return treeStack.pop();
	}

	private static int countClosePara(String line){
		int count = 0 ;
		Pattern p = Pattern.compile("!_\\)+");
		Matcher m = p.matcher(line);
		if(m.find()){
			String subLine = m.group();
			int lenght = subLine.length();
			Multiset<Character> chars = HashMultiset.create();
			for (int i = 0; i < lenght; i++) {
				chars.add(subLine.charAt(i));
			}
			count = chars.count(')');
		}
		return count;
	}

	private static List<String> breakLine(String line){

		List<String> lines = new LinkedList<String>();
		Matcher compositeLine_m = AS_TWO_LINES_PATTERN.matcher(line);

		if(compositeLine_m.matches()){

			String relation = null;
			List<String> edus = new LinkedList<String>();

			Pattern relation_p = Pattern.compile("(.*\\[\\w+\\]\\[\\w+\\])");
			Matcher relation_m = relation_p.matcher(line);
			if(relation_m.find()){
				relation = relation_m.group();
			}
			String removeRelation = StringUtils.substringAfter(line, relation).trim();
			edus.add(StringUtils.substringBeforeLast(removeRelation, "_!").trim());
			edus.add(StringUtils.substringAfter(removeRelation, "!_").trim());

			lines.add(relation);
			lines.addAll(edus);

		}else{

			lines.add(line);
		}

		return lines;
	}

	private static Element createElement(String line) throws Exception{

		if(StringUtils.isBlank(line)){
			throw new Exception("Line cannot be null.");
		}
		String relation = null;
		String text = null;

		//relation
		Matcher rel_m = REL_PATTERN.matcher(line);
		if(rel_m.matches()){
			String newLine = StringUtils.removeStart(line, "(");
			relation = StringUtils.substringBefore(newLine, "[");
		}else{
			Matcher edu_m = EDU_PATTERN.matcher(line);
			if(edu_m.matches()){
				text = line;
			}else{
				throw new Exception("The format doesn't match edu or relation : " + line);
			}
		}

		Element elem = new Element();
		try{
			elem.getEdu().setContent(getText(text));;
			elem.setRstRelation((getRelation(relation)));
		}catch(Exception e){
			e.printStackTrace();
		}

		return elem;
	}


	private static boolean matchParanthesis(String input){
		Stack<String> stack = new Stack<String>();
		String[] left = {"(", "{", "["};
		String[] right = {")","}","]"};
		String newInput = StringUtils.chomp(input);
		char[] charArray = newInput.toCharArray();
		for(int i=0; i<charArray.length ; i++){
			String charString = String.valueOf(charArray[i]);
			if(contains(left,charString)){
				stack.push(charString);
			}else if(contains(right, charString)){	
				if(stack.isEmpty()){
					return false;
				}
				if(indexOf(right, charString) != indexOf(left, stack.pop())){
					return false;
				}
			}
		}
		return stack.isEmpty();
	}

	private static boolean contains(String[] array, String match){
		for(int i=0; i<array.length ; i++){
			if(array[i].equalsIgnoreCase(match)){
				return true;
			}
		}
		return false;
	}

	private static int indexOf(String[] array, String match){
		for(int i=0; i<array.length ; i++){
			if(array[i].equalsIgnoreCase(match)){
				return i;
			}
		}
		return -1;
	}

	//rel2par span
	private static RSTRelation getRelation(String line){
		RSTRelation result = null;
		if (!StringUtils.isBlank(line)){
			String lowerCase = StringUtils.lowerCase(line);
			String cleaned = StringUtils.remove(lowerCase, "-");
			String upperCase = StringUtils.upperCase(cleaned);
			result = RSTRelation.valueOf(upperCase);
		}
		return result;
	}

	private static String getText(String line) throws Exception{
		String modified = null;
		if(!StringUtils.isBlank(line)){
			modified = StringUtils.chomp(line);
			modified = StringUtils.substringBetween(modified, "_!", "!_");
			modified = StringUtils.remove(modified,"<s>");
			modified = StringUtils.remove(modified,"<p>");
			modified = StringUtils.trim(modified);
		}

		return modified;
	}	

}
