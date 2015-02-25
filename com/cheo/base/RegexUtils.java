package com.cheo.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import edu.stanford.nlp.util.Pair;

public class RegexUtils {

	private RegexUtils(){}

	//Contains punctuation
	public static boolean hasPunctButNotJustPunct(String token){
		boolean hasPunct = false;
		if(!org.apache.commons.lang3.StringUtils.isNumeric(token) &&
				!org.apache.commons.lang3.StringUtils.isAlpha(token) &&
				!org.apache.commons.lang3.StringUtils.isAlphanumeric(token) &&
				!edu.stanford.nlp.util.StringUtils.isPunct(token)){
			hasPunct = true;
		}
		return hasPunct;
	}
	
	public static boolean isPunchWithSentiment(String token){
		return token.matches("\\?*|!*");
	}
	
	public static boolean isURL(String token){
		return token.matches("(http:)?//(-.)?([^\\s/?.#-]+.?)+(/[^\\s]*)?$");
	}
	
	
	public static List<String> tokenizeByPunct(String token){
		//Can not modify Array.asList
		List<String> l  = new LinkedList<String>(Arrays.asList(token.split("[\\p{Punct}]")));
		Iterator<String> itr = l.iterator();
		while(itr.hasNext()){
			if(StringUtils.isBlank(itr.next()))
				itr.remove();
		}
		return l;
	}

	public static boolean isAphabet(String word){
		return StringUtils.isAlpha(word);
	}

	public static boolean isPositiveAbriviatedExpression(String token){
		boolean result = false;
		if(token.matches("w+o+w+") ||
				token.matches("l(o)\\1{0,}l") ||
				token.matches("(ha)*") ||
				token.matches("y+a+y+") ||
				token.matches("y+a+")  ||
				token.matches("ew+") ||
				token.matches("a+w+") ||
				token.matches("wo+ho+") ||
				token.matches("wo+a+h+") ||
				token.matches("wo+t") ||
				token.matches("wo+p") ||
				token.matches("o+m+g+") ||
				token.matches("o+m+g+o+sh+"))
			result = true;
		return result;
	}

	public static boolean isNegativeAbriviatedExpression(String originalToken){
		boolean result = false;
		if(originalToken.matches("u+g+h+") ||
				originalToken.matches("(a|u)+h+") ||
				originalToken.matches("g+r+"))
			result = true;
		return result;
	}

	public static boolean isElongated(String token){
		return token.matches(".*(\\w)\\1{3,}.*");
	}

	public static boolean isDateTimeFormat(String originalToken){
		String MMDDYYYY = "(0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d";
		String MMDDYY = "(0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])[- /.]([0-9][0-9])";
		String HHMMSS = "([0-9]{2}):([0-9]{2}):([0-9]{2})";
		String HHMM = "([0-9]{2}):([0-9]{2})";

		boolean result = false;

		if(originalToken.matches(MMDDYYYY) || 
				originalToken.matches(MMDDYY) || 
				originalToken.matches(HHMMSS) || 
				originalToken.matches(HHMM))
			result=true;

		return result;
	}

	public static boolean isAmPm(String originalToken){	
		boolean result = false;
		if( 
				originalToken.matches("(?i)(am|pm)") ||
				originalToken.matches("(?i)(a\\.m\\.|p\\.m\\.)") ||
				originalToken.matches("(1[012]|[1-9])(:[0-5][0-9])?(\\s)?(?i)(am|pm)?-(1[012]|[1-9])(:[0-5][0-9])?(\\s)?(?i)(am|pm)?") ||
				originalToken.matches("(1[012]|[1-9])(:[0-5][0-9])?(\\s)?(?i)(am|pm)?"))
			result = true;
		return result;
	}


	//New methods
	public static String cleanApostrofe(String document){
		
		

		document=document.replaceAll("can't", "cannot");
		document=document.replaceAll("ain't", "am not");
		document=document.replaceAll("don't", "do not");
		document=document.replaceAll("doesn't", "does not");
		document=document.replaceAll("didn't", "did not");
		document=document.replaceAll("won't", "will not");
		document=document.replaceAll("shudn't", "should not");
		document=document.replaceAll("couldn't", "could not");
		document=document.replaceAll("haven't", "have not");
		document=document.replaceAll("weren't", "were not");
		document=document.replaceAll("can'ta", "can not a");
		document=document.replaceAll("hasn't", "has not");
		document=document.replaceAll("isn't", "is not");
		document=document.replaceAll("aren't", "are not");
		document=document.replaceAll("wasn't", "was not");
		document=document.replaceAll("hadn't", "had not");
		
		document=document.replaceAll("i've", "i have");
		document=document.replaceAll("he's", "he is");
		document=document.replaceAll("she's", "she is");
		document=document.replaceAll("i'd", "i would");
		document=document.replaceAll("u'd", "you would");
		document=document.replaceAll("u've", "you have");
		document=document.replaceAll("they're", "they are");
		document=document.replaceAll("we're", "we are");
		document=document.replaceAll("we've", "we have");
		document=document.replaceAll("we'd", "we would");
		document=document.replaceAll("you're", "you are");
		document=document.replaceAll("you've", "you have");
		document=document.replaceAll("u're", "you are");
		document=document.replaceAll("they'r", "they are");

		document=document.replaceAll("that's", "that is");
		document=document.replaceAll("there's", "there is");
		document=document.replaceAll("it's", "it is");
		document=document.replaceAll("i'm", "i am");
		document=document.replaceAll("what's", "what is");


		document=document.replaceAll("u'll", "you will");
		document=document.replaceAll("we'll", "we will");
		document=document.replaceAll("i'll", "i will");
		document=document.replaceAll("he'll", "he will");
		document=document.replaceAll("you'll", "you will");
		document=document.replaceAll("it'll", "it will");

		document=document.replaceAll("shouldn't", "should not");
		document=document.replaceAll("here's", "here is");
		document=document.replaceAll("they'll", "they will");

		document=document.replaceAll("c'mon", "come on"); 
		document=document.replaceAll("gov't", "government");
		document=document.replaceAll("'em", " them"); 
		document=document.replaceAll("y'all", "you all");
		document=document.replaceAll("mom's", "mother");
		document=document.replaceAll("b'cause", "because");
		document=document.replaceAll("cud", "could");
		document=document.replaceAll("cook'n", "cooking");
		document=document.replaceAll("d'una", "dont know");
		document=document.replaceAll("what'cha", "what are you");
		document=document.replaceAll("hop'n", "hoping");

		//document=document.replaceAll("'s", ""); 

		return document;
	}

	public static String cleanURL(String token){
		String urlPAttern = "(http:)?//(-.)?([^\\s/?.#-]+.?)+(/[^\\s]*)?$";
		if(token.matches(urlPAttern)){
			List<String> terms = new ArrayList<String>();
			if(!token.startsWith("http:"))
				token = "http:"+token;
			try {
				URL aURL = new URL(token);
				terms.addAll(tokenizePart(aURL.getProtocol()));
				terms.addAll(tokenizePart(aURL.getAuthority()));
				terms.addAll(tokenizePart(aURL.getHost()));
				terms.addAll(tokenizePart(Integer.toString(aURL.getPort())));
				terms.addAll(tokenizePart(aURL.getPath()));
				terms.addAll(tokenizePart(aURL.getQuery()));
				terms.addAll(tokenizePart(aURL.getFile()));
				terms.addAll(tokenizePart(aURL.getRef()));

				return StringUtils.join(terms, " ");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return token;
	}


	private static List<String> tokenizePart(String doc){
		List<String> l = new ArrayList<String>();
		Scanner scanner = new Scanner(doc);
		scanner.useDelimiter("/|\\?|\\.|-|=|:|_");
		while (scanner.hasNext()){
			l.add(scanner.next());
		}
		scanner.close();
		return l;
	}
	
	public static ArrayList<String> getTermList(String query){
		ArrayList<String> tokens = new ArrayList<String>();		
		Scanner tokenize = new Scanner(query);
		while (tokenize.hasNext()) {
			tokens.add(tokenize.next());
		}
		tokenize.close();
		return tokens;
	}
	

	public static int countElongatedTokens(String comment){
		int count = 0;
		StringTokenizer st = new StringTokenizer(comment);
		while (st.hasMoreElements()) {
			String token = (String)st.nextElement();
			if(isElongated(token))
				count++;
		}
		return count;
	}
	
	public static String elongatedWordsCleaned(String comment){
		List<String> cleanedTokens = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(comment);
		while (st.hasMoreElements()) {
			String token = (String)st.nextElement();
			cleanedTokens.add(cleanElongatedToken(token));	
		}
		return StringUtils.join(cleanedTokens, " ");
	}
	
	public static String cleanElongatedToken(String token){

		if(token.matches(".*(\\w)\\1{3,}.*")){
			char[] charArray = token.toCharArray();

			Deque<Pair<Character, Integer>> charDeque = new ArrayDeque<Pair<Character, Integer>>();
			Pair<Character, Integer> pair = new Pair<Character, Integer>();
			pair.first=charArray[0];
			pair.second=0;
			charDeque.addLast(pair);
			for(int indx = 1; indx<charArray.length; indx++){

				if(charArray[indx] == charArray[indx-1]){
					Pair<Character, Integer> extracted = charDeque.pollLast();
					extracted.second = extracted.second() + 1;
					charDeque.addLast(extracted);
				}else{
					Pair<Character, Integer> pair2 = new Pair<Character, Integer>();
					pair2.first=charArray[indx];
					pair2.second=0;
					charDeque.addLast(pair2);
				}
			}

			StringBuilder sb = new StringBuilder();
			for( Pair<Character, Integer>  p : charDeque){
				sb.append(p.first());
			}
			
			return sb.toString();
		}
		
		return token;
	}
	
	public static String getLCS(String x, String y){
		int M = x.length();
        int N = y.length();
		 // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[M+1][N+1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                if (x.charAt(i) == y.charAt(j))
                    opt[i][j] = opt[i+1][j+1] + 1;
                else 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
            }
        }

        // recover LCS itself and print it to standard output
        String lcs = StringUtils.EMPTY;
        int i = 0, j = 0;
        while(i < M && j < N) {
            if (x.charAt(i) == y.charAt(j)) {
                lcs += x.charAt(i);
                i++;
                j++;
            }
            else if (opt[i+1][j] >= opt[i][j+1]) i++;
            else                                 j++;
        }
        return lcs.trim();
	}

}
