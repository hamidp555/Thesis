package com.cheo.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.languagetool.rules.Rule;
import org.languagetool.rules.spelling.morfologik.MorfologikSpellerRule;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cheo.base.RegexUtils;

public class SpellChekerService implements InitializingBean{

	private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			.newInstance();

	private Resource resource;

	private List<String> abbreviations = new LinkedList<String>();

	private org.languagetool.JLanguageTool langTool;

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		addAbbr();
		langTool = getLanguageTool();
	}

	public String spellCheckWord(String tokenString) throws IOException{
		List<org.languagetool.rules.RuleMatch> matches = langTool.check(tokenString);

		if (matches.isEmpty()){
			return tokenString;
		}

		List<String> suggestedTokensForMach = new LinkedList<String>();
		for (org.languagetool.rules.RuleMatch match : matches) {
			List<String> suggestedToeknsPerMatch = match.getSuggestedReplacements();
			if(suggestedToeknsPerMatch.isEmpty()){
				continue;
			}

			TreeMap<Integer,String> suggestedPerMatchMap = new TreeMap<Integer,String>();
			for(String suggestedTokenPerMatch : suggestedToeknsPerMatch){
				int lcsLenght = RegexUtils.getLCS(tokenString, suggestedTokenPerMatch).length();
				suggestedPerMatchMap.put(lcsLenght, suggestedTokenPerMatch);
			}

			String suggestedTokenForMatch = suggestedPerMatchMap.lastEntry().getValue();

			suggestedTokensForMach.add(suggestedTokenForMatch);
		}

		TreeMap<Integer, String> suggestedTokensMap = new TreeMap<Integer, String>();
		for(String suggestedTokenForMach : suggestedTokensForMach){
			int lcsLenght = RegexUtils.getLCS(tokenString, suggestedTokenForMach).length();
			suggestedTokensMap.put(lcsLenght, suggestedTokenForMach);
		}

		return suggestedTokensMap.lastEntry().getValue();
	}

	public String spellCheckComment(String comment) throws IOException{
		List<SpellUnit> suggestions = new LinkedList<SpellUnit>();
		List<org.languagetool.rules.RuleMatch> matches = langTool.check(comment);
		String cleaned = comment;
		for (org.languagetool.rules.RuleMatch match : matches) {

			String misspelledToken = 
					StringUtils.substring(comment, match.getFromPos(), match.getToPos()+1).trim();

			List<String> suggestedTokens = match.getSuggestedReplacements();

			if(suggestedTokens.isEmpty()){
				continue;
			}

			TreeMap<Integer,String> suggestedTokensMap = new TreeMap<Integer,String>();
			for(String suggestedToken : suggestedTokens){
				int lcsLenght = RegexUtils.getLCS(misspelledToken, suggestedToken).length();
				suggestedTokensMap.put(lcsLenght, suggestedToken);
			}

			String suggestedToken = suggestedTokensMap.lastEntry().getValue();
			suggestions.add(new SpellUnit(misspelledToken, suggestedToken));
		}
		for(SpellUnit suggestion : suggestions){
			cleaned = StringUtils.replace(cleaned, 
					suggestion.getMissspelled(), 
					suggestion.getSuggested());
		}
		
		return cleaned;
	}

	private  org.languagetool.JLanguageTool getLanguageTool() throws IOException{

		org.languagetool.JLanguageTool langTool = 
				new org.languagetool.JLanguageTool(
						new org.languagetool.language.CanadianEnglish());

		langTool.activateDefaultPatternRules();

		//disable all rules that are not spell checking rules
		List<Rule> rules = langTool.getAllActiveRules();
		for(Rule rule: rules){
			if(!rule.isSpellingRule()){
				langTool.disableRule(rule.getId());
			}
		}

		//list of words to be ignore example:'pku'
		List<String> ignoreTokens = new LinkedList<String>();
		ignoreTokens.addAll(abbreviations);

		List<Rule> activeRules = langTool.getAllActiveRules();
		for(Rule rule: activeRules){
			if(rule.getId().equalsIgnoreCase("MORFOLOGIK_RULE_EN_CA")){
				MorfologikSpellerRule morphRule = (MorfologikSpellerRule)rule;
				morphRule.addIgnoreTokens(ignoreTokens);
			}		
		}

		return langTool;
	}

	private List<String> addAbbr(){


		try{
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(resource.getFile());
			Element root = document.getDocumentElement();
			if(root!=null){
				NodeList terms = root.getElementsByTagName("term");
				for (int i = 0; i < terms.getLength(); i++) {
					if (terms.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element term = (Element) terms.item(i);
						String abbreviation = term.getAttribute("abbreviation");
						//String explanation = term.getAttribute("explanation");
						abbreviations.add(abbreviation.toLowerCase());
					}
				}
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return abbreviations;
	}

	class SpellUnit{

		private String missspelled;

		private String suggested;

		public SpellUnit(String missspelled, String suggested){
			this.missspelled=missspelled;
			this.suggested=suggested;
		}

		public String getMissspelled() {
			return missspelled;
		}

		public String getSuggested() {
			return suggested;
		}

	}

}
