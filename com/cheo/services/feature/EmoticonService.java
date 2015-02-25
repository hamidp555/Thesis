package com.cheo.services.feature;


import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.cheo.base.TextUnitWrapper;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public class EmoticonService implements  ICommentLevelFeatureService{

	private Multimap<String, String> emoticons = LinkedHashMultimap.create();
	private final static String POSITIVE="positive";
	private final static String NEGATIVE="negative";

	@Override
	public void updateStatistics(TextUnitWrapper textUnitWrapper) {
		
		String toprocess = textUnitWrapper.getTextUnit();
		int posEmoticonsCount = 0;
		int negEmoticonsCount = 0;
		
		for(String emoticon:emoticons.get(POSITIVE)){
			int count = StringUtils.countMatches(toprocess, emoticon);
			if(count>0){
				textUnitWrapper.getPosEmoticons().add(emoticon);
			}
			posEmoticonsCount = posEmoticonsCount + count;
		}
		
		for(String emoticon:emoticons.get(NEGATIVE)){
			int count = StringUtils.countMatches(toprocess, emoticon);
			if(count>0){
				textUnitWrapper.getNegEmoticons().add(emoticon);
			}
			negEmoticonsCount = negEmoticonsCount + count;
		}
		
		textUnitWrapper.getStatistics().setNumNegativeEMOTICON(negEmoticonsCount);
		textUnitWrapper.getStatistics().setNumPositiveEMOTICON(posEmoticonsCount);
	}
	
	@Override
	public void init(){
		
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{":)",";)","8)",":p",":D",":>",":3",":*",":B",":-(",";-)","8-)",":-p",":-D",":->",":-3",":-*",":-B",":-)","<3"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{":-)",":)",":D",":o)",":]",":3",":c)",":>","=]","8)"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"=)",":}",":^)"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{":-D","8-D","8D","x-D","xD","X-D","XD","=-D","=D","=-3","=3","B^D"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{":-))"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{":'-)",":')"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{":*",":^*"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"));-)","));)","*-)","*)","));-]","));]","));D","));^)",":-,"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{">:P",":-P",":P",":-p",":p","=p",":-Þ",":Þ",":þ",":-þ",":-b",":b"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"<*)))-{","><(((*>","><>]"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"\\o/"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"*\0/*"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"@}-));-'---","@>-->--"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"~(_8^(I)"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"5:-)","~:-\\"}));
		emoticons.put(POSITIVE, "//0-0\\\\");
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"*<|:-)"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"=:o]"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{",:-)","7:^]"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"<3","</3"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"O:-)","0:-3","0:3","0:-)","0:)","0));^)"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{">:)",">));)",">:-)"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"}:-)","}:)","3:-)","3:)"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"|));-)","|-O"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{":-J"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"#-)"}));
		emoticons.putAll(POSITIVE, Arrays.asList(new String[]{"%-)","%)"}));

		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{"8.",":?","8o",">:(",":,","XD","D:",":c",":C",":(",":,",":|",":o",":O","8o",":\\",":x",":<",":["}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{">:[",":-(",":(","",":-c",":c",":-<",":<",":-[",":[",":{"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{"));("}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{":-||",":@",">:("}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{":'-(",":'("}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{"D:<","D:"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{">:O",":-O",":O",":-o",":o","8-0","O_O","o-o","O_o","o_O","o_o","O-"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{":|",":-|"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{":$"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{":-X",":X",":-#",":#"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{"o/\\o","^5",">_>^","^<_<"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{":-&",":&"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{":-###..",":###.."}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{"<:-|"}));
		emoticons.putAll(NEGATIVE, Arrays.asList(new String[]{">:\\",">:/",":-\\/",":-.","://",":\\","=//","=\\",":L",":S",">.<", ":L"}));
	}

}
