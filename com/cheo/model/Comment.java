package com.cheo.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.cheo.base.enums.ClassLabel;
import com.cheo.base.exceptions.UnknownClassLabelException;
import com.cheo.model.argumentation.RuleEvaluator;

public class Comment implements TextUnit,Serializable{
	
	private static final long serialVersionUID = 6964541615454595640L;

	private Integer sheetID;
	private Integer commentID;
	private String authorName;
	private String commentTitle;
	private String comment;	
	private String discussionTitle;
	private String type;
	private String positive;
	private String negative;
	private String mixed;
	private String irrelevant;
	private String keyRelevance;
	private String keyPosition;
	private String dateDownloaded ;
	private String discriminator ;
	private String copy ;
	
	private List<EDU> edus = new LinkedList<EDU>();
	private List<Argument> arguments = new LinkedList<Argument>();

	public void setSheetID(Integer sheetID) {
		this.sheetID = sheetID;
	}
	public void setCommentID(Integer commentID) {
		this.commentID = commentID;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getCommentTitle() {
		return commentTitle;
	}
	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDiscussionTitle() {
		return discussionTitle;
	}
	public void setDiscussionTitle(String discussionTitle) {
		this.discussionTitle = discussionTitle;
	}
	public String getPositive() {
		return positive;
	}
	public void setPositive(String positive) {
		this.positive = positive;
	}
	public String getNegative() {
		return negative;
	}
	public void setNegative(String negative) {
		this.negative = negative;
	}
	public String getMixed() {
		return mixed;
	}
	public void setMixed(String mixed) {
		this.mixed = mixed;
	}
	public String getIrrelevant() {
		return irrelevant;
	}
	public void setIrrelevant(String irrelevant) {
		this.irrelevant = irrelevant;
	}
	public String getKeyRelevance() {
		return keyRelevance;
	}
	public void setKeyRelevance(String keyRelevance) {
		this.keyRelevance = keyRelevance;
	}
	public String getKeyPosition() {
		return keyPosition;
	}
	public void setKeyPosition(String keyPosition) {
		this.keyPosition = keyPosition;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDateDownloaded() {
		return dateDownloaded;
	}
	public void setDateDownloaded(String dateDownloaded) {
		this.dateDownloaded = dateDownloaded;
	}
	public String getDiscriminator() {
		return discriminator;
	}
	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}
	public String getCopy() {
		return copy;
	}
	public void setCopy(String copy) {
		this.copy = copy;
	}
	public List<EDU> getEdus() {
		return edus;
	}
	public void setEdus(List<EDU> edus) {
		this.edus = new LinkedList<EDU>(edus);
	}
	public List<Argument> getArguments() {
		return arguments;
	}
	public void setArguments(List<Argument> arguments) {
		this.arguments =new LinkedList<Argument>(arguments);
	}

	public boolean isEmpty(){
		return this.getAuthorName() == null &&
				this.getComment() == null &&
				this.getCommentTitle() == null &&
				this.getCopy() == null &&
				this.getDateDownloaded() == null &&
				this.getDiscriminator() == null &&
				this.getIrrelevant() == null &&
				this.getIrrelevant() == null &&
				this.getKeyPosition() ==  null &&
				this.getKeyRelevance() == null &&
				this.getMixed() == null &&
				this.getNegative() == null &&
				this.getPositive() == null &&
				this.getType() == null &&
				this.getEdus().isEmpty();
	}

	public boolean equalsSpecialCase(Comment comment){
		boolean sameAuthor = this.getAuthorName().equalsIgnoreCase(comment.getAuthorName());
		boolean sameComment = this.getComment().equalsIgnoreCase(comment.getComment());
		boolean sameDisTitle = this.getDiscussionTitle().equalsIgnoreCase(comment.getDiscussionTitle());
		boolean sameDiscr = this.getDiscriminator().equalsIgnoreCase(comment.getDiscriminator());
		return  sameAuthor && sameComment && sameDisTitle && sameDiscr;

	}

	@Override
	public Integer getSheetID() {
		return sheetID;
	}
	
	@Override
	public Integer getCommentID() {
		return commentID;
	}
	
	/**
	 * No eduID is defined for comment
	 */
	@Override
	public Integer getEduID() {
		return null;
	}
	
	@Override 
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Comment))
			return false;
		Comment c = (Comment)o;
		return c.getAuthorName().equalsIgnoreCase(getAuthorName()) &&
				c.getComment().equalsIgnoreCase(getComment()) &&
				c.getCommentTitle().equalsIgnoreCase(getCommentTitle()) &&
				c.getDiscriminator().equalsIgnoreCase(discriminator) &&
				c.getDiscussionTitle().equalsIgnoreCase(getDiscussionTitle()) &&
				c.getMixed().equalsIgnoreCase(getMixed()) &&
				c.getNegative().equalsIgnoreCase(getNegative()) &&
				c.getPositive().equalsIgnoreCase(getPositive()) &&
				c.getIrrelevant().equalsIgnoreCase(getIrrelevant()) &&
				c.getCopy().equalsIgnoreCase(copy) &&
				c.getEdus().equals(getEdus());
	}

	private volatile int hashCode;

	@Override 
	public int hashCode() {
		int result = hashCode;
		if (result == 0) {
			result = 17;
			result = 31 * result + + ((getAuthorName() == null) ? 0 : getAuthorName().hashCode());
			result = 31 * result + ((getComment() == null) ? 0 : getComment().hashCode());
			result = 31 * result + ((getCommentTitle() == null) ? 0 : getCommentTitle().hashCode());
			result = 31 * result + ((getDiscriminator() == null) ? 0 : getDiscriminator().hashCode());
			result = 31 * result + ((getDiscussionTitle() == null) ? 0 : getDiscussionTitle().hashCode());
			result = 31 * result + ((getMixed() == null) ? 0 : getMixed().hashCode());
			result = 31 * result + ((getNegative() == null) ? 0 : getNegative().hashCode());
			result = 31 * result + ((getPositive() == null) ? 0 : getPositive().hashCode());
			result = 31 * result + ((getIrrelevant() == null) ? 0 : getIrrelevant().hashCode());
			result = 31 * result + ((getCopy() == null) ? 0 : getCopy().hashCode());
			result = 31 * result + ((getKeyPosition() == null) ? 0 : getKeyPosition().hashCode());
			result = 31 * result + ((getKeyRelevance() == null) ? 0 : getKeyRelevance().hashCode());
			result = 31 * result + ((getDateDownloaded() == null) ? 0 : getDateDownloaded().hashCode());
			result = 31 * result + ((getEdus() == null) ? 0 : getEdus().hashCode());
			hashCode = result;
		}
		return result;

	}
	
	@Override 
	public ClassLabel getClassLabel() throws Exception{
		if(!StringUtils.isBlank(irrelevant) && "1.0".equalsIgnoreCase(irrelevant)){
			return ClassLabel.IRRELEVANT;
		}else{
			if(!StringUtils.isBlank(positive) && "1.0".equalsIgnoreCase(positive)){
				return ClassLabel.POSITIVE;
			}
			else if(!StringUtils.isBlank(negative) && "1.0".equalsIgnoreCase(negative)){
				return ClassLabel.NEGATIVE;
			}
			else if(!StringUtils.isBlank(mixed) && "1.0".equalsIgnoreCase(mixed)){
				return ClassLabel.MIX;
			}
		}
		throw new Exception("class label is not supported");
	}
	
	@Override 
	public boolean isRelevant(){
		if(!hasClassLabel()){
			throw new UnknownClassLabelException("Class Label is not supported");
		}
		boolean isIrrel = !StringUtils.isBlank(irrelevant) && "1.0".equalsIgnoreCase(irrelevant);
		boolean isPos = !StringUtils.isBlank(positive) && "1.0".equalsIgnoreCase(positive);
		boolean isNeg = !StringUtils.isBlank(negative) && "1.0".equalsIgnoreCase(negative);
		boolean isMix = !StringUtils.isBlank(mixed) && "1.0".equalsIgnoreCase(mixed);
		return  !isIrrel && (isNeg || isPos || isMix)  ? true : false;
	}
	
	@Override 
	public  boolean hasClassLabel(){
		boolean isIrrel = !StringUtils.isBlank(irrelevant) && "1.0".equalsIgnoreCase(irrelevant);
		boolean isPos = !StringUtils.isBlank(positive) && "1.0".equalsIgnoreCase(positive);
		boolean isNeg = !StringUtils.isBlank(negative) && "1.0".equalsIgnoreCase(negative);
		boolean isMix = !StringUtils.isBlank(mixed) && "1.0".equalsIgnoreCase(mixed);
		return isMix || isNeg || isPos || isIrrel;	
	}
	
	@Override 
	public boolean isAnnotated(){
		boolean isIrrel = !StringUtils.isBlank(irrelevant) && "1.0".equalsIgnoreCase(irrelevant);
		boolean isPos = !StringUtils.isBlank(positive) && "1.0".equalsIgnoreCase(positive);
		boolean isNeg = !StringUtils.isBlank(negative) && "1.0".equalsIgnoreCase(negative);
		boolean isMix = !StringUtils.isBlank(mixed) && "1.0".equalsIgnoreCase(mixed);
		boolean isCopy = !StringUtils.isBlank(copy) && "1.0".equalsIgnoreCase(copy);
		boolean hasKeyPos = !StringUtils.isBlank(keyPosition) && "1.0".equalsIgnoreCase(keyPosition);
		boolean hasKeyRel = !StringUtils.isBlank(keyRelevance) && "1.0".equalsIgnoreCase(keyRelevance);

		if(isPos || isNeg || isMix || isIrrel || isCopy || hasKeyPos || hasKeyRel){
			return true;
		}

		return false;
	} 
	
	public boolean isDuplicate(){
		return !StringUtils.isBlank(copy) && copy.equalsIgnoreCase("1.0") ? true : false;
	}

	public List<EDU> getEDUsWithTopics(){
		List<EDU> result = new LinkedList<EDU>();
		for(EDU edu : edus){
			if(edu.hasTopic()){
				result.add(edu);
			}
		}
		return result;
	}

	public List<String> getTopics(){
		List<String> result = new LinkedList<String>();
		for(EDU edu : edus){
			if(edu.hasTopic()){
				result.add(edu.getTopic());
			}
		}
		return result;
	}

	public void addArguments() throws Exception{
		for(int index=0;  index<edus.size(); index++){
			Argument arg = RuleEvaluator.evaluate(edus, index);
			if(arg != null){
				this.arguments.add(arg);
			}
			if(arg == null && !StringUtils.isBlank(edus.get(index).getTopic())){
				System.out.println("Sheet ID: " + edus.get(index).getSheetID() + " - Edu ID: " + edus.get(index).getEduID() + " - Argument is not extracted.");
			}
		}
		if(this.arguments.isEmpty() && this.hasTopic()){
			System.out.println("Sheet ID: " + this.getSheetID() + " - Comment ID: " + this.getCommentID() + " - Arguments are not extracted.");
		}
	}
	
	public boolean hasTopic(){
		for(EDU edu : this.getEdus()){
			if(edu.hasTopic()){
				return true;
			}
		}
		return false;
	}
	
	public String getCompositeKey(){
		EDU lastEdu = getLastEdu();
		String result = ClassLabel.IRRELEVANT.toString();
		StringBuilder sb = new StringBuilder();
		if(lastEdu != null){

			sb.setLength(0);
			sb.append(sheetID);
			sb.append("-");
			sb.append(commentID);
			sb.append("-");
			sb.append(lastEdu.getEduID());
			result=sb.toString();
		}
		return result;
	}
	
	private EDU getLastEdu(){

		EDU result = null;
		TreeMap<Integer, EDU> eduMap = new TreeMap<Integer, EDU>();
		for(EDU edu : edus){
			eduMap.put(edu.getEduID(), edu);
		}
		if(!eduMap.isEmpty()){
			result = eduMap.lastEntry().getValue();
		}else{
			result = null;
		}
		return result;
	}



}