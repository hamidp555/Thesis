package com.cheo.model;

import java.util.LinkedList;
import java.util.List;

import com.cheo.base.enums.Stance;


public  class Argument{
	private List<EDU> befor = new LinkedList<EDU>();
	private List<EDU> after = new LinkedList<EDU>();
	private Topic topic = new Topic();
	public List<EDU> getBefor() {
		return befor;
	}
	public void setBefor(List<EDU> befor) {
		this.befor = befor;
	}
	public List<EDU> getAfter() {
		return after;
	}
	public void setAfter(List<EDU> after) {
		this.after = after;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	
	public Stance getStance(){
		return topic.getStance();
	}
	
}