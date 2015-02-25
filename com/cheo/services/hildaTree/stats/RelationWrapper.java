package com.cheo.services.hildaTree.stats;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cheo.base.enums.ClassLabel;
import com.cheo.services.hildaTree.Element;
import com.cheo.services.hildaTree.RSTRelation;

public class RelationWrapper {

	private static Logger logger = Logger.getLogger(RelationWrapper.class);
	
	private Element relation;

	private List<Element> children = new LinkedList<Element>();

	public Element getRelation() {
		return relation;
	}

	public void setRelation(Element relation) {
		this.relation = relation;
	}

	public List<Element> getChildren() {
		return children;
	}

	public void setChildren(List<Element> children) {
		this.children = children;
	}

	public String getWeight(){
		if(children.isEmpty() ||children.size() == 1){
			throw new RuntimeException("something is wrong with edus for this relation");
		}
		StringBuilder sb = new StringBuilder();

		//Some edus class labels are not initialized since there is no match for them in comment \
		//after preprocessing
		for(Element child : children){
			try{
				if(ClassLabel.POSITIVE.equals(child.getEdu().getActualClassLabel())){
					sb.append("positive");
				}
				if(ClassLabel.NEGATIVE.equals(child.getEdu().getActualClassLabel())){
					sb.append("negative");
				}
				if(ClassLabel.IRRELEVANT.equals(child.getEdu().getActualClassLabel())){
					sb.append("irrelevant");
				}
				if(ClassLabel.NEUTRAL.equals(child.getEdu().getActualClassLabel())){
					sb.append("neutral");
				}
				if(ClassLabel.RELEVANT.equals(child.getEdu().getActualClassLabel())){
					sb.append("relevant");
				}
				if(ClassLabel.OBJECTIVE.equals(child.getEdu().getActualClassLabel())){
					sb.append("objective");
				}
				if(ClassLabel.MIX.equals(child.getEdu().getActualClassLabel())){
					sb.append("mix");
				}
				if(StringUtils.isBlank(sb.toString())){
					logger.info("sheetID : "+ child.getEdu().getSheetID() + 
							" commentID : " + child.getEdu().getCommentID() +
							" eduID : " + child.getEdu().getEduID());
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public boolean hasDirectEduChildren(){

		boolean hasDirectEduChild = false;
		if(children .size() == 2){
			for( Element child: children){
				if(child.getEdu() != null){
					hasDirectEduChild =  true;
				}
			}
		}
		return hasDirectEduChild;
	}

	public boolean hasRelation(RSTRelation rstRelation){
		return relation.getRstRelation().equals(rstRelation);
	}

}
