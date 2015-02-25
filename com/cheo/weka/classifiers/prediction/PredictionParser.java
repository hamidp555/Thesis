package com.cheo.weka.classifiers.prediction;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cheo.base.enums.ClassLabel;
import com.cheo.weka.classifiers.WhiteSpaceTokenizer;


public class PredictionParser {

	private static final Pattern PREDICTION_PATTERN = Pattern.compile("[\\s+|\\t+]*(\\d+)([\\s+|\\t+](\\d?).*:\\w+){1,2}.*");

	private static final Pattern CLASS_PATTERN = Pattern.compile("\\d?.*:\\w+");

	private static final Pattern DISTRIBUTION_PATTERN = Pattern.compile("((\\*?[0-9]*\\.*[0-9]+),*){2,}");

	private static final Pattern REFERENCE_PATTERN = Pattern.compile("\\(\\d+,\\d+,\\d+\\)|\\(\\d+,\\d+\\)");

	public static Prediction parse( String line) throws Exception{

		Prediction pred = new Prediction();
		Matcher m1 = PREDICTION_PATTERN.matcher(line);
		boolean actualSet = false;

		if(m1.matches()){

			line = StringUtils.chomp(line);
			line = StringUtils.trim(line);
			List<String> tokens = WhiteSpaceTokenizer.tokenize(line);
			pred.setIndex(Integer.valueOf(tokens.get(0)));
			
			for(String token : tokens){
				Matcher m3 = CLASS_PATTERN.matcher(token);
				Matcher m5 = DISTRIBUTION_PATTERN.matcher(token);
				Matcher m6 = REFERENCE_PATTERN.matcher(token);

				if(m3.matches()){					
					String classLabelStr = StringUtils.substringAfterLast(token, ":");
					ClassLabel clazz = getClazz(classLabelStr);
					if(!actualSet){
						pred.setActual(clazz);
						actualSet=true;
					}else{
						pred.setPredicted(clazz);
						actualSet=false;
					}
					continue;
				}

				//distribution of classes 
				//In order defined in arff
				if(m5.matches()){
					token = StringUtils.replace(token, "*", "");
					for(String doubleStr : Arrays.asList(StringUtils.split(token, ","))){
						pred.getDistribution().add(Double.valueOf(doubleStr));
					}
					continue;
				}

				if(m6.matches()){

					token = StringUtils.substringBeforeLast(token, ")");
					token = StringUtils.substringAfter(token, "(");
					String[] references = StringUtils.split(token, ",");

					Integer sheetID = Integer.valueOf(references[0]);
					Integer commentID = Integer.valueOf(references[1]);
					pred.getReference().setSheetID(sheetID);
					pred.getReference().setCommentID((commentID));
					
					if(references.length == 3){
						Integer eduID = Integer.valueOf(references[2]);
						pred.getReference().setEduID(eduID);
					}
					continue;
				}

				if(StringUtils.equalsIgnoreCase(token, "+")){
					pred.setError(true);
					continue;
				}
			}
		}

		return pred;
	}

	private static ClassLabel getClazz(String classLabelStr) throws Exception{
		if(classLabelStr.startsWith("irreleva")){
			return ClassLabel.IRRELEVANT;
		}
		if(classLabelStr.startsWith("releva")){
			return ClassLabel.RELEVANT;
		}
		
		for(ClassLabel label: ClassLabel.values()){
			if(label.getValue().contains(classLabelStr)){
				return label;
			}
		}
		throw new Exception("Clas label not found for this prediction");
	}

}
