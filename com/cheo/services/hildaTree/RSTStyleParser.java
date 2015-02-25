package com.cheo.services.hildaTree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

public class RSTStyleParser {

	@SuppressWarnings("unchecked")
	public static LinkedBinaryTree<Element> parse(String fileName) throws Exception{
		String left = "(";
		String right = ")";
		Stack<LinkedBinaryTree<Element>> treeStack = new Stack<LinkedBinaryTree<Element>>();

		List<String> lines = HildaUtils.readAll(fileName);
		assert matchParanthesis(StringUtils.join(lines));

		for(String line : lines){

			//if the line is a leaf
			String leftMost = StringUtils.left(line, 1);
			//String rightMost = matchParanthesis(line) ? StringUtils.right(line, 1) : null;

			if(leftMost.equalsIgnoreCase(left)){
				LinkedBinaryTree<Element> tree = new LinkedBinaryTree<Element>();
				tree.addRoot(createElement(line));
				treeStack.push(tree);

			}else if(leftMost.equalsIgnoreCase(right)){
				try{
					LinkedBinaryTree<Element> rightTree = treeStack.pop();
					LinkedBinaryTree<Element> leftTree = treeStack.pop();
					LinkedBinaryTree<Element> rootTree = treeStack.pop();
					rootTree.attach(rootTree.root(), leftTree, rightTree);
					treeStack.push(rootTree);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		return treeStack.pop();
	}

	private static Element createElement(String line) throws Exception{

		String newLine = StringUtils.removeStart(line, "(");
		if(matchParanthesis(line))
			newLine = StringUtils.removeEnd(newLine, ")");

		String span = null;
		String relation = null;
		String text = null;
		String type = null;

		type = StringUtils.substringBefore(newLine, "(").trim().toLowerCase();
		String[] matches = StringUtils.substringsBetween(newLine, "(", ")");
		span = matches[0];
		if(matches.length == 2 || matches.length == 3)
			relation = matches[1];
		if((matches.length == 3))
			text = matches[2];

		Element elem = new Element();
		try{
			elem.getEdu().setContent(getText(text));;
			elem.setSpan(span);
			elem.setRstRelation((getRelation(relation)));
			elem.setNodeType(RSTNodeType.valueOf(type));
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
		if (line != null){
			List<String> tokens = tokenize(line);
			String lowerCase = StringUtils.lowerCase(tokens.get(1));
			String cleaned = StringUtils.remove(lowerCase, "-");
			String upperCase = StringUtils.upperCase(cleaned);
			result = RSTRelation.valueOf(upperCase);
		}
		return result;
	}

	private static String getText(String line){
		String modified = StringUtils.chomp(line);
		modified = StringUtils.substringBetween(modified, "_!_!", "!__!");
		modified = StringUtils.remove(modified,"<s>");
		modified = StringUtils.remove(modified,"<p>");
		modified = StringUtils.trim(modified);
		return line!= null ? modified : null;
	}

	private static List<String> tokenize(String line){
		List<String> list = new LinkedList<String>();
		list.addAll(Arrays.asList(line.split("\\s+")));
		Iterator<String> iter = list.iterator();
		while(iter.hasNext()){
			String token = iter.next();
			if(token == null || token.isEmpty()){
				iter.remove();
			}
		}
		return list;
	}

}
