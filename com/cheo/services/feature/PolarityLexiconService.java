package com.cheo.services.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.tartarus.snowball.ext.PorterStemmer;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.RegexUtils;
import com.cheo.base.TokenWrapper;
import com.cheo.tagConv.SNLPPLTagConv;
import com.google.common.collect.Multimap;

public class PolarityLexiconService implements  ITokenLevelFeatureService{

	private Map<Integer, PlEntry> plEntries;
	
	private Map<Integer, PlEntry> stemmedPLEnteries ;
	
	private Map<Integer, PlEntry> notStemmedPLEnteries ;

	private Multimap<String, String> posTagMap;

	private Resource resource;

	private SNLPPLTagConv tagReader;

	PorterStemmer stemmer = new PorterStemmer();

	public void setTagReader(SNLPPLTagConv tagReader) {
		this.tagReader = tagReader;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void init(){
		try{ 
			plEntries = new Hashtable<Integer, PlEntry>();
			stemmedPLEnteries = new Hashtable<Integer, PlEntry>(); 
			notStemmedPLEnteries = new Hashtable<Integer, PlEntry>();
			
			posTagMap = tagReader.getPosTagMap();
			InputStream fstream = resource.getInputStream();
			InputStreamReader isr = new InputStreamReader(fstream, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);

			int index=0;
			String strLine;
			while ((strLine = reader.readLine()) != null)   {
				strLine = StringUtils.chomp(strLine);
				strLine = StringUtils.lowerCase(strLine);
				if(StringUtils.isBlank(strLine)){
					continue;
				}
				plEntries.put(index, new PlEntry(strLine));
				index++;
			}

			reader.close();
			isr.close();
			fstream.close();

			for(Entry<Integer, PlEntry> entry : plEntries.entrySet()){
				PlEntry value = entry.getValue();
				Integer key = entry.getKey();
				if(value.getStemmed().equalsIgnoreCase("y")){
					stemmedPLEnteries.put(key, value);
				}else if(value.getStemmed().equalsIgnoreCase("n")){
					notStemmedPLEnteries.put(key, value);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}

	@Override
	public void updateStatistics(TextUnitWrapper textUnitWrapper, TokenWrapper tokenWrapper){

		String token = tokenWrapper.getToken();
		String lemma = tokenWrapper.getLemmatizedToken();
		String posTag = ConvertSNLPTagToPLTag(tokenWrapper.getPosTag());

		//LOOKUP IN NOT STEMMED TERMS LIST FIRST
		boolean found = false;
		for(Entry<Integer, PlEntry> entry : plEntries.entrySet()){

			PlEntry value = entry.getValue();
			String word = value.getWord();
			String pos = value.getPos();

			if((word.equalsIgnoreCase(token) || 
					word.equalsIgnoreCase(lemma)) && 
					(StringUtils.isBlank(pos) || 
					pos.equalsIgnoreCase(posTag))){

				value.update(textUnitWrapper);
				found = true;
				break;
			}

		}

		//IF NOT FOUND LOOKUP IN STEMMED TERMS LIST
		if(!found){
			//STEMMER
			stemmer.setCurrent(token);
			stemmer.stem();
			String stemmed = stemmer.getCurrent();

			for(Entry<Integer, PlEntry> entry : stemmedPLEnteries.entrySet()){

				PlEntry value = entry.getValue();
				String word = value.getWord();
				String pos = value.getPos();

				if((word.equalsIgnoreCase(stemmed)) && pos.equalsIgnoreCase(posTag)){
					value.update(textUnitWrapper);
					found = true;
					break;
				}
			}
		}

	}

	private String ConvertSNLPTagToPLTag(String posTag){	
		List<String> tagList = new ArrayList<String>(posTagMap.get(posTag));
		return !tagList.isEmpty() ? tagList.get(0): null; 
	}

	private class PlEntry{
		private String type;
		private String len;
		private String word;
		private String pos;
		private String stemmed;
		private String priorpolarity;

		public PlEntry(String strLine){

			List<String> tokens  = RegexUtils.getTermList(strLine);
			Map<String, String> map = new HashMap<String, String>();
			for(String token : tokens){
				map.put(StringUtils.substringBeforeLast(token, "="), StringUtils.substringAfterLast(token, "="));
			}
			for(Entry<String, String> entry: map.entrySet()){
				if(StringUtils.equalsIgnoreCase(entry.getKey(), "type")){
					this.setType(entry.getValue());
					continue;
				}
				if(StringUtils.equalsIgnoreCase(entry.getKey(), "len")){
					this.setLen(entry.getValue());
					continue;
				}
				if(StringUtils.equalsIgnoreCase(entry.getKey(), "word1")){
					this.setWord(entry.getValue());
					continue;
				}
				if(StringUtils.equalsIgnoreCase(entry.getKey(), "pos1")){
					this.setPos(entry.getValue());
					continue;
				}
				if(StringUtils.equalsIgnoreCase(entry.getKey(), "stemmed1")){
					this.setStemmed(entry.getValue());
					continue;
				}
				if(StringUtils.equalsIgnoreCase(entry.getKey(), "priorpolarity")){
					this.setPriorpolarity(entry.getValue());
					continue;
				}
			}
		}

		public void update(TextUnitWrapper textUnitWrapper){

			if(this.getType().equalsIgnoreCase("weaksubj")){
				textUnitWrapper.getStatistics().incrementNumWeakSubjPL();
			}
			if(this.getType().equalsIgnoreCase("strongsubj")){
				textUnitWrapper.getStatistics().incrementNumStrongSubjPL();
			}
			if(this.getPriorpolarity().equalsIgnoreCase("positive")){
				textUnitWrapper.getStatistics().incrementNumPositivePL();
			}
			if(this.getPriorpolarity().equalsIgnoreCase("negative")){
				textUnitWrapper.getStatistics().incrementNumNegativePL();
			}
		}

		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getLen() {
			return len;
		}
		public void setLen(String len) {
			this.len = len;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public String getPos() {
			return pos;
		}
		public void setPos(String pos) {
			this.pos = pos;
		}
		public String getStemmed() {
			return stemmed;
		}
		public void setStemmed(String stemmed) {
			this.stemmed = stemmed;
		}
		public String getPriorpolarity() {
			return priorpolarity;
		}
		public void setPriorpolarity(String priorpolarity) {
			this.priorpolarity = priorpolarity;
		}

		@Override 
		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (!(o instanceof PlEntry))
				return false;
			PlEntry pl = (PlEntry)o;
			return  pl.getLen().equalsIgnoreCase(this.getLen()) &&
					pl.getPos().equalsIgnoreCase(this.getPos()) &&
					pl.getPriorpolarity().equalsIgnoreCase(this.getPriorpolarity()) &&
					pl.getStemmed().equalsIgnoreCase(this.getStemmed()) &&
					pl.getWord().equalsIgnoreCase(this.getWord());
		}

		private volatile int hashCode;

		@Override 
		public int hashCode() {
			int result = hashCode;
			if (result == 0) {
				result = 17;
				result = 31 * result + ((this.getLen() == null) ? 0 : this.getLen().hashCode());
				result = 31 * result + ((this.getPos() == null) ? 0 : this.getPos().hashCode());
				result = 31 * result + ((this.getPriorpolarity() == null) ? 0 : this.getPriorpolarity().hashCode());
				result = 31 * result + ((this.getStemmed() == null) ? 0 : this.getStemmed().hashCode());
				result = 31 * result + ((this.getType() == null) ? 0 : this.getType().hashCode());
				result = 31 * result + ((this.getWord() == null) ? 0 : this.getWord().hashCode());
				hashCode = result;
			}
			return result;

		}

	}

}

