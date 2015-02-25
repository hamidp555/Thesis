package com.cheo.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.cheo.base.enums.ClassLabel;
import com.cheo.base.enums.Stance;

public class EDU implements TextUnit,Serializable{

	private static final long serialVersionUID = -5131676246131068507L;

	private Integer commentID;
	private Integer sheetID;
	private Integer eduID;

	private String authorName;
	private String commentTitle;
	private String comment;	
	private String discussionTitle;
	private String dateDownloaded ;
	private String discriminator ;

	private String irrelevant;
	private String subjective;
	private String objective;

	private String positive;
	private String negative;
	private String mixed;
	private String neutral;

	private String topic;
	private String pro;
	private String con;

	public void setCommentID(Integer commentID) {
		this.commentID = commentID;
	}
	public void setSheetID(Integer sheetID) {
		this.sheetID = sheetID;
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
	public String getNeutral() {
		return neutral;
	}
	public void setNeutral(String neutral) {
		this.neutral = neutral;
	}
	public String getIrrelevant() {
		return irrelevant;
	}
	public void setIrrelevant(String irrelevant) {
		this.irrelevant = irrelevant;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getPro() {
		return pro;
	}
	public void setPro(String pro) {
		this.pro = pro;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
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
	public String getSubjective() {
		return subjective;
	}
	public void setSubjective(String subjective) {
		this.subjective = subjective;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public void setEduID(Integer eduID) {
		this.eduID = eduID;
	}

	@Override
	public Integer getSheetID() {
		return sheetID;
	}

	@Override
	public Integer getCommentID() {
		return commentID;
	}

	@Override
	public Integer getEduID() {
		return eduID;
	}

	@Override 
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Comment))
			return false;
		EDU edu = (EDU)o;
		return edu.getAuthorName().equalsIgnoreCase(getAuthorName()) &&
				edu.getComment().equalsIgnoreCase(getComment()) &&
				edu.getCommentTitle().equalsIgnoreCase(getCommentTitle()) &&
				edu.getDiscussionTitle().equalsIgnoreCase(getDiscussionTitle()) &&
				edu.getDiscriminator().equalsIgnoreCase(getDiscriminator()) &&

				edu.getIrrelevant().equalsIgnoreCase(getIrrelevant()) &&
				edu.getIrrelevant().equalsIgnoreCase(getSubjective()) &&
				edu.getIrrelevant().equalsIgnoreCase(getObjective()) &&
				edu.getPositive().equalsIgnoreCase(getPositive()) &&
				edu.getNegative().equalsIgnoreCase(getNegative()) &&
				edu.getMixed().equalsIgnoreCase(getMixed()) &&
				edu.getNeutral().equalsIgnoreCase(getNeutral()) &&

				edu.getTopic().equalsIgnoreCase(getTopic()) &&
				edu.getPro().equalsIgnoreCase(getPro()) &&
				edu.getCon().equalsIgnoreCase(getCon());
	}

	private volatile int hashCode;

	@Override 
	public int hashCode() {
		int result = hashCode;
		if (result == 0) {
			result = 17;
			result = 31 * result + ((getAuthorName() == null) ? 0 : getAuthorName().hashCode());
			result = 31 * result + ((getComment() == null) ? 0 : getComment().hashCode());
			result = 31 * result + ((getCommentTitle() == null) ? 0 : getCommentTitle().hashCode());
			result = 31 * result + ((getDiscriminator() == null) ? 0 : getDiscriminator().hashCode());
			result = 31 * result + ((getDiscussionTitle() == null) ? 0 : getDiscussionTitle().hashCode());

			result = 31 * result + ((getIrrelevant() == null) ? 0 : getIrrelevant().hashCode());
			result = 31 * result + ((getSubjective() == null) ? 0 : getSubjective().hashCode());
			result = 31 * result + ((getObjective() == null) ? 0 : getObjective().hashCode());
			result = 31 * result + ((getPositive()== null) ? 0 : getPositive().hashCode());
			result = 31 * result + ((getNegative() == null) ? 0 : getNegative().hashCode());
			result = 31 * result + ((getMixed() == null) ? 0 : getMixed().hashCode());
			result = 31 * result + ((getNeutral() == null) ? 0 : getNeutral().hashCode());

			result = 31 * result + ((getTopic() == null) ? 0 : getTopic().hashCode());
			result = 31 * result + ((getPro()== null) ? 0 : getPro().hashCode());
			result = 31 * result + ((getCon() == null) ? 0 : getCon().hashCode());
			hashCode = result;
		}
		return result;
	}

	//Alternate this method for discourse and classification based on the need for class labels
	@Override 
	public ClassLabel getClassLabel() throws Exception{
		if(!StringUtils.isBlank(irrelevant) && "1.0".equalsIgnoreCase(irrelevant)){
			return ClassLabel.IRRELEVANT;
		}else{
			if(!StringUtils.isBlank(objective) && "1.0".equalsIgnoreCase(objective)){
				return ClassLabel.OBJECTIVE;
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
				else if(!StringUtils.isBlank(neutral) && "1.0".equalsIgnoreCase(neutral)){
					return ClassLabel.NEUTRAL;
				}
			}
		}
		throw new Exception("CLass label is not supported");
	}

	@Override 
	public boolean isRelevant(){
		
		boolean isIrrel = !StringUtils.isBlank(irrelevant) && "1.0".equalsIgnoreCase(irrelevant);
		boolean isObj = !StringUtils.isBlank(objective) && "1.0".equalsIgnoreCase(objective);
		boolean isSubj = !StringUtils.isBlank(subjective) && "1.0".equalsIgnoreCase(subjective);
		
		return !isIrrel && (isObj || isSubj) ? true : false;
	}

	@Override
	public boolean hasClassLabel() {
		boolean isIrrel = !StringUtils.isBlank(irrelevant) && "1.0".equalsIgnoreCase(irrelevant);
		boolean isPos = !StringUtils.isBlank(positive) && "1.0".equalsIgnoreCase(positive);
		boolean isNeg = !StringUtils.isBlank(negative) && "1.0".equalsIgnoreCase(negative);
		boolean isMix = !StringUtils.isBlank(mixed) && "1.0".equalsIgnoreCase(mixed);
		boolean isNeutral = !StringUtils.isBlank(neutral) && "1.0".equalsIgnoreCase(neutral);
		return isIrrel || isNeg || isPos || isMix || isNeutral ? true : false;
	}
	
	@Override
	public boolean isAnnotated() {
		boolean isIrrel = !StringUtils.isBlank(irrelevant) && "1.0".equalsIgnoreCase(irrelevant);
		boolean isObj = !StringUtils.isBlank(objective) && "1.0".equalsIgnoreCase(objective);
		boolean isSubj = !StringUtils.isBlank(subjective) && "1.0".equalsIgnoreCase(subjective);
		boolean isPos = !StringUtils.isBlank(positive) && "1.0".equalsIgnoreCase(positive);
		boolean isNeg = !StringUtils.isBlank(negative) && "1.0".equalsIgnoreCase(negative);
		boolean isMix = !StringUtils.isBlank(mixed) && "1.0".equalsIgnoreCase(mixed);
		boolean isNeutral = !StringUtils.isBlank(neutral) && "1.0".equalsIgnoreCase(neutral);
		boolean hasTopics = !StringUtils.isBlank(topic);
		boolean isPro = !StringUtils.isBlank(pro) && "1.0".equalsIgnoreCase(pro); 
		boolean isCon = !StringUtils.isBlank(con) && "1.0".equalsIgnoreCase(con);
		return isIrrel || isObj || isSubj || isPos || isNeg ||
				isMix || isNeutral || hasTopics || isPro || isCon;
	}

	public Stance getStance(){
		if(this.hasTopic()){
			if(this.isPro()){
				return Stance.PRO;
			}
			if(this.isCon()){
				return Stance.CON;
			}
		}
		return Stance.NONE;
	}

	public boolean hasTopic(){
		return !StringUtils.isBlank(topic) ? true : false;
	}

	public boolean isSubjective(){
		return !StringUtils.isBlank(subjective) ? true : false; 
	}

	public boolean isObjective(){
		return !StringUtils.isBlank(objective) ? true : false;
	}

	public boolean isPro(){
		return !StringUtils.isBlank(pro) ? true : false;
	}

	public boolean isCon(){
		return !StringUtils.isBlank(con) ? true : false;
	}

	//reference to comment
	public String getCompositeID(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getSheetID());
		sb.append("-");
		sb.append(this.getCommentID());
		return sb.toString();
	}
	
	//reference to comment
	public String getCompositeKey(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getSheetID());
		sb.append("-");
		sb.append(this.getCommentID());
		sb.append("-");
		sb.append(this.getEduID());
		return sb.toString();
	}

}
