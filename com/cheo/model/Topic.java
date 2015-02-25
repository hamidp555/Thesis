package com.cheo.model;

import com.cheo.base.enums.Stance;

public class Topic{
	private String topic;
	private EDU edu;
	private Stance stance;

	public Stance getStance() {
		return stance;
	}
	public void setStance(Stance stance) {
		this.stance = stance;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public EDU getEdu() {
		return edu;
	}
	public void setEdu(EDU edu) {
		this.edu = edu;
	}
}
