package com.cheo.services.hildaTree;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;


public class HildaUtils {

	private HildaUtils(){}


	public static <E> int depth(Tree<E> t, Position<E> v){
		if(t.isRoot(v)){
			return 0;
		}else{
			return 1+ depth(t, t.parent(v));
		}
	}

	public static <E> int height(Tree<E> t){
		int h = 0;
		for(Position<E> v: t.positions()){
			if(t.isExternal(v)){
				h = Math.max(h, depth(t, v));
			}
		}
		return h;
	}

	public static <E> int height(Tree<E> t, Position<E> v){
		if(t.isExternal(v)) return 0;
		int h=0;
		for(Position<E> w: t.children(v)){
			h = Math.max(h, height(t, w));
		}
		return 1+h;
	}

	public static <E> String toStringPreorder(Tree<E> t, Position<E> v){
		String s = v.element().toString();
		for(Position<E> w : t.children(v)){
			s +=", " + toStringPreorder(t, w);
		}
		return s;
	}

	public static <E> String paranthesisRepresentation(Tree<E> t, Position<E> v){
		String s = v.element().toString();
		if(t.isInternal(v)){
			boolean first = true;
			for(Position<E> w : t.children(v)){
				if(first){
					s += " ( " + paranthesisRepresentation(t, w);
					first=false;
				}else{
					s += ", " + paranthesisRepresentation(t, w);
				}
				s+=" )";
			}
		}
		return s;
	}

	public static <E> String IndentedParanthesisRepresentation(BinaryTree<E> t, Position<E> v, int depth){
		String s =  StringUtils.repeat("  ", depth) + 
				"(" + 
				v.element().toString() + 
				(t.isExternal(v) ? ")": "") + 
				"\n";

		if(t.isInternal(v)){
			for(Position<E> w : t.children(v)){
				s +=IndentedParanthesisRepresentation(t, w, depth(t, w)) ;
			}
			s+=StringUtils.repeat("  ", depth) + ")\n";
		}
		return s;
	}

	public static <E> String toStringPostorder(BinaryTree<E> t, Position<E> v){
		String s = "";
		for(Position<E> w : t.children(v)){
			s+=toStringPostorder(t, w) + "";
		}
		s+=v.element();
		return s;
	}

	public static <E> String toStringInorder(BinaryTree<E> t, Position<E> v){
		String s = "";
		if(t.hasLeft(v)){
			s+=toStringInorder(t, t.left(v));
		}
		if(t.hasRight(v)){
			toStringInorder(t, t.right(v));
		}
		s+=v.element();
		return s;
	}
	
	public static List<String> readAll(String fileName) throws Exception{
		FileInputStream inputStream = new FileInputStream(fileName);
		try {
			@SuppressWarnings("unchecked")
			List<String> lines = IOUtils.readLines(inputStream);
			List<String> newLines = new LinkedList<String>();
			for(String line: lines){
				newLines.add(StringUtils.chomp(line.trim()));
			}
			return newLines;

		} finally {
			inputStream.close();
		}
	}

}
