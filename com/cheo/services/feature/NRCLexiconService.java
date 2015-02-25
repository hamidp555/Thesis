package com.cheo.services.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.Resource;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.RegexUtils;
import com.cheo.base.TokenWrapper;
import com.google.common.collect.ArrayListMultimap;



public class NRCLexiconService implements ITokenLevelFeatureService {

	Map<String, NRCEntry> dictionary;

	private Resource resource;

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void init() {
		try{ 

			ArrayListMultimap<String, Pair<String, Integer>> lines = ArrayListMultimap.create();

			InputStream fstream = resource.getInputStream();
			InputStreamReader isr = new InputStreamReader(fstream, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);

			String strLine;
			while ((strLine = reader.readLine()) != null)   {
				strLine = StringUtils.chomp(strLine);
				strLine = StringUtils.lowerCase(strLine);
				List<String> terms = RegexUtils.getTermList(strLine);

				String term = terms.get(0);
				String emotion = terms.get(1);
				Integer weight = Integer.valueOf(terms.get(2));

				Pair<String, Integer> pair = new ImmutablePair<String, Integer>(emotion, weight); 
				lines.put(term, pair);
			}

			dictionary = new Hashtable<String, NRCEntry>();
			Map<String, Collection<Pair<String, Integer>>> entries = lines.asMap();
			for(Entry<String, Collection<Pair<String, Integer>>> entry : entries.entrySet()){
				dictionary.put(entry.getKey(), new NRCEntry(entry));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}	

	}

	@Override
	public void updateStatistics(TextUnitWrapper textUnitWrapper, TokenWrapper tokenWrapper) {
		String token = tokenWrapper.getLemmatizedToken(); 
		NRCEntry entry = dictionary.get(token);
		if(entry != null){
			if(entry.isNegative()){
				textUnitWrapper.getStatistics().incrementNumNegativeNRC();
			}
			if(entry.isPositive()){
				textUnitWrapper.getStatistics().incrementNumPositiveNRC();
			}
		}
	}

	private class NRCEntry{

		private String word;

		private int anger;
		private int anticipation;	
		private int disgust;	
		private int fear;	
		private int joy;	
		private int negative;	
		private int positive;	
		private int sadness;	
		private int surprise;	
		private int trust;
		private boolean isPositive = false;
		private boolean isNegative = false;

		public NRCEntry(Entry<String, Collection<Pair<String, Integer>>> entry){
			String word = entry.getKey();
			this.setWord(word);
			Collection<Pair<String, Integer>> emotions = entry.getValue();
			if(!emotions.isEmpty()){
				for(Pair<String, Integer> emotionPair : emotions){
					String emotion = emotionPair.getLeft();
					int weight = emotionPair.getRight();

					if("anger".equalsIgnoreCase(emotion)){
						this.setAnger(weight);
					}
					if("anticipation".equalsIgnoreCase(emotion)){
						this.setAnticipation(weight);
					}
					if("disgust".equalsIgnoreCase(emotion)){
						this.setDisgust(weight);
					}
					if("fear".equalsIgnoreCase(emotion)){
						this.setFear(weight);
					}
					if("joy".equalsIgnoreCase(emotion)){
						this.setJoy(weight);
					}
					if("negative".equalsIgnoreCase(emotion)){
						this.setNegative(weight);
					}
					if("positive".equalsIgnoreCase(emotion)){
						this.setPositive(weight);
					}
					if("sadness".equalsIgnoreCase(emotion)){
						this.setSadness(weight);
					}
					if("surprise".equalsIgnoreCase(emotion)){
						this.setSurprise(weight);
					}
					if("trust".equalsIgnoreCase(emotion)){
						this.setTrust(weight);
					}
					if(this.getPositive() == 1){
						this.setPositive(true);
					}
					if(this.getNegative() == 1){
						this.setNegative(true);
					}
				}
			}
		}
		public int getAnger() {
			return anger;
		}
		public void setAnger(int anger) {
			this.anger = anger;
		}
		public int getAnticipation() {
			return anticipation;
		}
		public void setAnticipation(int anticipation) {
			this.anticipation = anticipation;
		}
		public int getDisgust() {
			return disgust;
		}
		public void setDisgust(int disgust) {
			this.disgust = disgust;
		}
		public int getFear() {
			return fear;
		}
		public void setFear(int fear) {
			this.fear = fear;
		}
		public int getJoy() {
			return joy;
		}
		public void setJoy(int joy) {
			this.joy = joy;
		}
		public int getNegative() {
			return negative;
		}
		public void setNegative(int negative) {
			this.negative = negative;
		}
		public int getPositive() {
			return positive;
		}
		public void setPositive(int positive) {
			this.positive = positive;
		}
		public int getSadness() {
			return sadness;
		}
		public void setSadness(int sadness) {
			this.sadness = sadness;
		}
		public int getSurprise() {
			return surprise;
		}
		public void setSurprise(int surprise) {
			this.surprise = surprise;
		}
		public int getTrust() {
			return trust;
		}
		public void setTrust(int trust) {
			this.trust = trust;
		}
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}

		public boolean isPositive() {
			return isPositive;
		}

		public void setPositive(boolean isPositive) {
			this.isPositive = isPositive;
		}

		public boolean isNegative() {
			return isNegative;
		}

		public void setNegative(boolean isNegative) {
			this.isNegative = isNegative;
		}
		
		@Override 
		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (!(o instanceof NRCEntry))
				return false;
			NRCEntry entry = (NRCEntry)o;
			return  entry.getAnger() == this.getAnger() &&
					entry.getAnticipation() == this.getAnticipation() &&
					entry.getDisgust() == this.getDisgust() &&
					entry.getFear() == this.getFear() &&
					entry.getJoy() == this.getJoy() &&
					entry.getNegative() == this.getNegative() &&
					entry.getPositive() == this.getPositive() &&
					entry.getSadness() == this.getSadness() &&
					entry.getSurprise() == this.getSurprise() &&
					entry.getTrust() == this.getTrust() &&
					entry.getWord().equalsIgnoreCase(this.getWord());
		}
		
		private volatile int hashCode;

		@Override 
		public int hashCode() {
			int result = hashCode;
			if (result == 0) {
				result = 17;
				result = 31 * result + (this.getWord().hashCode());
				result = 31 * result + (this.getAnger());
				result = 31 * result + (this.getAnticipation());
				result = 31 * result + (this.getDisgust());
				result = 31 * result + (this.getFear());
				result = 31 * result + (this.getJoy());
				result = 31 * result + (this.getNegative());
				result = 31 * result + (this.getPositive());
				result = 31 * result + (this.getSadness());
				result = 31 * result + (this.getTrust());
				result = 31 * result + (this.getSurprise());
				hashCode = result;
			}
			return result;

		}
		
	}

}
