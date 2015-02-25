package com.cheo.services.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.core.io.Resource;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.TokenWrapper;
import com.cheo.tagConv.SNLPDepmodeTagConv;
import com.google.common.collect.Multimap;

public class DepechemodeService  implements  ITokenLevelFeatureService{

	private Multimap<String, String> posTagMap;

	private Hashtable<String, DepechemodeEntry> tableEntries = new Hashtable<String, DepechemodeEntry>();

	private Resource resource; 

	private SNLPDepmodeTagConv tagReader;

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setTagReader(SNLPDepmodeTagConv tagReader) {
		this.tagReader = tagReader;
	}

	@Override
	public void init() {
		try{
			posTagMap = tagReader.getPosTagMap();
			InputStream fstream = resource.getInputStream();
			InputStreamReader isr = new InputStreamReader(fstream, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb= new StringBuilder();
			String strLine;
			while ((strLine = reader.readLine()) != null)   {
				if(!strLine.isEmpty()){
					DepechemodeEntry entry = getEntry(strLine);
					sb.append(entry.getLemma());
					sb.append("#");
					sb.append(entry.getPos());
					tableEntries.put(sb.toString(), entry);
					sb.setLength(0);
				}

			}
			fstream.close();
			isr.close();
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void updateStatistics(TextUnitWrapper comment, TokenWrapper token) {
		String lemma = token.getLemmatizedToken();
		String pos = ConvertSNLPTagToDepmodeTag(token.getPosTag());
		StringBuilder sb = new StringBuilder();
		sb.append(lemma);
		sb.append("#");
		sb.append(pos);

		DepechemodeEntry entry = tableEntries.get(sb.toString());
		if(entry != null){
			if(isPositive(entry))
				comment.getStatistics().incrementNumPositiveDepmode();

			if(isNegative(entry))
				comment.getStatistics().incrementNumNegativeDepmode();

			if(isNeutral(entry)){
				;//do nothing
			}			
		}
	}

	private boolean isPositive(DepechemodeEntry entry){

		double negativeWeight = entry.getAffraid() + entry.getAngry() + entry.getAnnoyed() + entry.getSad();
		double positiveWeight = entry.getAmused() + entry.getHappy() + entry.getInspired();
		double dontCare = entry.getDontCare();

		if(dontCare < positiveWeight && dontCare < negativeWeight &&
				positiveWeight > negativeWeight){
			return true;
		}
		return false;
	}

	private boolean isNegative(DepechemodeEntry entry){

		double negativeWeight = entry.getAffraid() + entry.getAngry() + entry.getAnnoyed() + entry.getSad();
		double positiveWeight = entry.getAmused() + entry.getHappy() + entry.getInspired();
		double dontCare = entry.getDontCare();

		if(dontCare < positiveWeight && dontCare < negativeWeight &&
				positiveWeight < negativeWeight){
			return true;
		}
		return false;
	}

	private boolean isNeutral(DepechemodeEntry entry){

		double negativeWeight = entry.getAffraid() + entry.getAngry() + entry.getAnnoyed() + entry.getSad();
		double positiveWeight = entry.getAmused() + entry.getHappy() + entry.getInspired();
		double dontCare = entry.getDontCare();

		if(dontCare > positiveWeight && dontCare > negativeWeight){
			return true;
		}
		return false;
	}

	/**
	 * @param strLine
	 * @return An entry containing lemma pos and emotion-scores
	 * <br>
	 * <br>
	 * <ul> Emotion scores are in the following order
	 * <li>AFRAID</li>
	 * <li>AMUSED</li>
	 * <li>ANGRY</li>
	 * <li>ANNOYED</li>
	 * <li>DONT_CARE</li>
	 * <li>HAPPY</li>
	 * <li>INSPIRED</li>
	 * <li>SAD</li>
	 * </ul>
	 */
	private DepechemodeEntry getEntry(String strLine){
		DepechemodeEntry entry = new DepechemodeEntry();
		List<Double> scores = new LinkedList<Double>();
		StringTokenizer st = new StringTokenizer(strLine);
		boolean first = true;
		while (st.hasMoreElements()) {
			String element = st.nextElement().toString();
			if(!first)
				scores.add(Double.parseDouble(element));
			if(first){
				String[] lemmaPos = element.split("#");
				entry.setLemma(lemmaPos[0]);
				entry.setPos(lemmaPos[1]);
			}
			first=false;
		}
		entry.setAffraid(scores.get(0));
		entry.setAmused(scores.get(1));
		entry.setAngry(scores.get(2));
		entry.setAnnoyed(scores.get(3));
		entry.setDontCare(scores.get(4));
		entry.setHappy(scores.get(5));
		entry.setInspired(scores.get(6));
		entry.setSad(scores.get(7));

		return entry;
	}

	private String ConvertSNLPTagToDepmodeTag(String posTag){	
		List<String> tagList = new ArrayList<String>(posTagMap.get(posTag));
		return !tagList.isEmpty() ? tagList.get(0): null; 

	}

	private class DepechemodeEntry{

		private String lemma;

		private String pos;

		private Double affraid;

		private Double amused;

		private Double angry;

		private Double annoyed;

		private Double dontCare;

		private Double happy;

		private Double inspired;

		private Double sad;

		public String getLemma() {
			return lemma;
		}

		public void setLemma(String lemma) {
			this.lemma = lemma;
		}

		public String getPos() {
			return pos;
		}

		public void setPos(String pos) {
			this.pos = pos;
		}

		public Double getAffraid() {
			return affraid;
		}

		public void setAffraid(Double affraid) {
			this.affraid = affraid;
		}

		public Double getAmused() {
			return amused;
		}

		public void setAmused(Double amused) {
			this.amused = amused;
		}

		public Double getAngry() {
			return angry;
		}

		public void setAngry(Double angry) {
			this.angry = angry;
		}

		public Double getAnnoyed() {
			return annoyed;
		}

		public void setAnnoyed(Double annoyed) {
			this.annoyed = annoyed;
		}

		public Double getDontCare() {
			return dontCare;
		}

		public void setDontCare(Double dontCare) {
			this.dontCare = dontCare;
		}

		public Double getHappy() {
			return happy;
		}

		public void setHappy(Double happy) {
			this.happy = happy;
		}

		public Double getInspired() {
			return inspired;
		}

		public void setInspired(Double inspired) {
			this.inspired = inspired;
		}

		public Double getSad() {
			return sad;
		}

		public void setSad(Double sad) {
			this.sad = sad;
		}
	}

}
